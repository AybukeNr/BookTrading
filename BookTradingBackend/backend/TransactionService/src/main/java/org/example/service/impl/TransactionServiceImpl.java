package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.response.ListResponsePayment;
import org.example.dto.response.TransactionResponse;
import org.example.dto.response.enums.ListType;
import org.example.entity.Account;
import org.example.entity.Transactions;
import org.example.entity.enums.TransactionStatus;
import org.example.entity.enums.TransactionType;
import org.example.exception.ErrorType;
import org.example.exception.TransactionException;
import org.example.external.ListManager;
import org.example.mapper.CardMapper;
import org.example.mapper.PaymentMapper;
import org.example.mapper.TransactionMapper;
import org.example.repository.AccountRepsoitory;
import org.example.repository.CardsRepository;
import org.example.repository.PaymentRepository;
import org.example.repository.TransactionRepository;
import org.example.service.ITransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final CardsRepository cardsRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final CardMapper cardMapper;
    private final PaymentMapper paymentMapper;
    private final ListManager listManager;
    private final TransactionMapper transactionMapper;
    private final AccountRepsoitory accountRepository;



    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transactions> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new TransactionException(ErrorType.NO_TRANSACTIONS_FOUND);
        }
        return transactions.stream()
                .map(transactionMapper::transactionToResponse)
                .toList();

    }


    /**
     * her dakika güvence bedelleri yatırıldı mı diye kontrol eder
     */

    @Override
    @Transactional
    @Scheduled(cron = "* 5 * * * *")
    public void checkTransactionStatus() {
        // Devam eden (ONGOING) işlemleri filtrele
        List<Transactions> ongoingTransactions = transactionRepository.findAll()
                .stream()
                .filter(t -> t.getStatus().equals(TransactionStatus.ONGOING))
                .toList();

        // Eğer devam eden işlem yoksa işlem yapılmaz
        if (ongoingTransactions.isEmpty()) {
            log.info("No ongoing transactions to check.");
            return;
        }

        // Thread havuzu oluştur (örnek: 10 thread)
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Paralel işlem için her bir transaction'ı ayrı bir thread'de çalıştır
        List<Transactions> transactionsToUpdate = ongoingTransactions.parallelStream().map(transaction -> {
            executorService.submit(() -> processTransaction(transaction));
            return transaction;
        }).toList();

        // Thread havuzunu kapat
        executorService.shutdown();

        // Güncellemeleri toplu olarak kaydet
        transactionRepository.saveAll(transactionsToUpdate);
        log.info("Batch update completed for {} transactions.", transactionsToUpdate.size());
    }

    private void processTransaction(Transactions transaction) {
        try {
            if (isDeadlineExceeded(transaction)) {
                log.warn("Transaction deadline passed: {}", transaction.getTransactionId());

                if (transaction.getTransactionType() == TransactionType.SALE) {
                    // SALE tipi işlem: Sadece OffererDeposit kontrol edilir
                    if (transaction.getOffererDeposit() == null || transaction.getOffererDeposit() <= 0.0) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        log.warn("Transaction failed due to missing offerer deposit: {}", transaction.getTransactionId());
                    } else {
                        transaction.setStatus(TransactionStatus.COMPLETED);
                        log.info("Transaction completed successfully: {}", transaction.getTransactionId());
                    }
                } else {
                    // EXCHANGE tipi işlem: Hem OwnerDeposit hem OffererDeposit kontrol edilir
                    //todo -> sadece birinin yatırmamış olması durumunu
                    if ((transaction.getOwnerDeposit() == null || transaction.getOwnerDeposit() <= 0.0) ||
                            (transaction.getOffererDeposit() == null || transaction.getOffererDeposit() <= 0.0)) {
                        transaction.setStatus(TransactionStatus.FAILED);
                        log.warn("Transaction failed due to missing deposits: {} {} {}",
                                transaction.getTransactionId(), transaction.getOwnerDeposit(), transaction.getOffererDeposit());
                    } else {
                        transaction.setStatus(TransactionStatus.COMPLETED);
                        log.info("Transaction completed successfully: {}", transaction.getTransactionId());
                    }
                }

                // Güncelleme zamanı ayarla
                transaction.setUpdatedDate(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("Error processing transaction {}: {}", transaction.getTransactionId(), e.getMessage());
        }
    }


    /**
     * Takas durumunda kargolar teslim edildiğinde güvence bedelleri iade edilir.
     *
     * @param exchangeComplatedRequest Takas tamamlanma isteği.
     */
    @Override
    @Transactional
    public TransactionResponse refundBothParties(ExchangeComplatedRequest exchangeComplatedRequest) {
        // Retrieve the transaction
        Transactions transactions = transactionRepository.findByOwnerIdAndOffererIdAndTransactionId(
                        exchangeComplatedRequest.getOwnerId(),
                        exchangeComplatedRequest.getOffererId(),
                        exchangeComplatedRequest.getTransactionId())
                .orElseThrow(() -> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));

        // Ensure the transaction is completed before refunds
        if (!TransactionStatus.COMPLETED.equals(transactions.getStatus())) {
            throw new TransactionException(ErrorType.INVALID_TRANSACTION_STATUS);
        }

        // Retrieve accounts of both parties
        Account offererAccount = accountRepository.findByUserId(exchangeComplatedRequest.getOffererId())
                .orElseThrow(() -> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));
        Account ownerAccount = accountRepository.findByUserId(exchangeComplatedRequest.getOwnerId())
                .orElseThrow(() -> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));

        // Refund deposits
        if (transactions.getOffererDeposit() > 0.0) {
            offererAccount.setBalance(offererAccount.getBalance() + transactions.getOffererDeposit());
            log.info("Refunded {} to offerer account: {}", transactions.getOffererDeposit(), offererAccount.getIban());
        }

        if (transactions.getOwnerDeposit() > 0.0) {
            ownerAccount.setBalance(ownerAccount.getBalance() + transactions.getOwnerDeposit());
            log.info("Refunded {} to owner account: {}", transactions.getOwnerDeposit(), ownerAccount.getIban());
        }

        // Update and save accounts
        accountRepository.save(offererAccount);
        accountRepository.save(ownerAccount);

        // Update the transaction status to reflect refunds
        transactions.setStatus(TransactionStatus.REFUNDED);
        transactions.setUpdatedDate(LocalDateTime.now());
        transactionRepository.save(transactions);
        UpdateListReq updateListReq = UpdateListReq.builder().listStatus(String.valueOf(ListsStatus.CLOSED))
                        .listingId(transactions.getListId()).build();
        listManager.updateListStatus(updateListReq);
        log.info("Update List Request: {}", updateListReq);

        log.info("Refund process completed for transaction: {}", transactions.getTransactionId());
        return transactionMapper.transactionToResponse(transactions);    }


    /***
     * satış durumunda kargo teslim edildiğinde ücret sahibe aktarılır
     * @param exchangeComplatedRequest
     */
    @Override
    @Transactional
    public TransactionResponse finalizePayment(ExchangeComplatedRequest exchangeComplatedRequest) {

        Transactions transactions = transactionRepository.findByOwnerIdAndOffererIdAndTransactionId(
                        exchangeComplatedRequest.getOwnerId(),
                        exchangeComplatedRequest.getOffererId(),
                        exchangeComplatedRequest.getTransactionId())
                .orElseThrow(() -> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        // Ensure the transaction is completed before refunds
        if (!TransactionStatus.COMPLETED.equals(transactions.getStatus())) {
            throw new TransactionException(ErrorType.INVALID_TRANSACTION_STATUS);
        }
        Account ownerAccount = accountRepository.findByUserId(exchangeComplatedRequest.getOwnerId())
                .orElseThrow(() -> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));
        if (transactions.getOffererDeposit() > 0.0) {
            ownerAccount.setBalance(ownerAccount.getBalance() + transactions.getOffererDeposit());
            log.info("Sent {} to offerer account: {}", transactions.getOffererDeposit(), ownerAccount.getIban());
        }
        accountRepository.save(ownerAccount);

        // Update the transaction status to reflect refunds
        transactions.setStatus(TransactionStatus.SWAPPED);
        transactions.setUpdatedDate(LocalDateTime.now());
        transactionRepository.save(transactions);
        UpdateListReq updateListReq = UpdateListReq.builder().listStatus(String.valueOf(ListsStatus.CLOSED))
                .listingId(transactions.getListId()).build();
        listManager.updateListStatus(updateListReq);
        log.info("Update List Request: {}", updateListReq);

        log.info("Payment Finalize process completed for transaction: {}", transactions.getTransactionId());
        return transactionMapper.transactionToResponse(transactions);
    }

    /**
     * başarısızlık durumunda güvence bedeli mağdur tarafa geçer
     * @param transferAllReq
     * @return
     */
    @Override
    @Transactional
    public TransactionResponse transferAll(TransferAllReq transferAllReq) {
        Transactions transactions = transactionRepository.findByTransactionId(transferAllReq.getTransactionId()).
                orElseThrow(() -> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        Account transferAcc = accountRepository.findByUserId(transferAllReq.getTransferUserId()).
                orElseThrow(()-> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));
        transferAcc.setBalance(transactions.getOffererDeposit()+transactions.getOwnerDeposit()+transferAcc.getBalance());
        transactions.setStatus(TransactionStatus.FAILED);
        log.info("All deposit transferred to account {}",transferAcc);
        UpdateListReq updateListReq = UpdateListReq.builder().listStatus(String.valueOf(ListsStatus.OPEN))
                .listingId(transactions.getListId()).build();
        listManager.updateListStatus(updateListReq);
        log.info("Update List Request: {}", updateListReq);
        return transactionMapper.transactionToResponse(transactions);
    }

    @Override
    public boolean isDeadlineExceeded(Transactions transaction) {
        return transaction.getCreatedDate()
                .plusDays(5)
                .isBefore(LocalDateTime.now());
    }

    @Override
    @Transactional
    public Boolean createAccount(AccountRequest accountRequest) {
        log.info("AccountRequest: {}" ,accountRequest);
        Account account = Account.builder()
                .fullName(accountRequest.getFullName())
                .iban(accountRequest.getIban())
                .userId(accountRequest.getUserId())
                .balance(0.0).build();
        accountRepository.save(account);
        return true;
    }


    @Override
    @Transactional
    public TransactionResponse takePayment(TakePaymentRequest takePaymentRequest) {
        Transactions transactions = transactionRepository.findByListId(takePaymentRequest.getListId()).filter(t -> t.getStatus().equals(TransactionStatus.ONGOING))
                .orElseThrow(()-> new TransactionException(ErrorType.LIST_NOT_FOUND));
        if(Objects.equals(transactions.getOwnerId(), takePaymentRequest.getUserId())) {
            transactions.setOwnerDeposit(takePaymentRequest.getAmount());
        } else if (Objects.equals(transactions.getOffererId(), takePaymentRequest.getUserId())) {
            transactions.setOffererDeposit(takePaymentRequest.getAmount());
        }
        UpdateListReq updateListReq = UpdateListReq.builder().listStatus(String.valueOf(ListsStatus.SUSPENDED))
                .listingId(transactions.getListId()).build();
        listManager.updateListStatus(updateListReq);
        log.info("Update List Request: {}", updateListReq);

        return transactionMapper.transactionToResponse(transactions);
    }

    @Override
    @Transactional
    public TransactionResponse createTransaciton(TransactionRequest transactionRequest) {
        log.info("Creating transaction for list: {}",transactionRequest.getListId());
        log.info("Transaciton request {}",transactionRequest);
        Transactions transactions = transactionMapper.requestToTransaction(transactionRequest);
        transactions.setOwnerId(transactionRequest.getOwnerId());
        transactions.setOffererId(transactionRequest.getOffererId());
        transactions.setOffererDeposit(0.0);
        transactions.setCreatedDate(LocalDateTime.now());
        transactions.setUpdatedDate(LocalDateTime.now());
        if(transactionRequest.getListType() == ListType.EXCHANGE){
            transactions.setTransactionType(TransactionType.EXCHANGE);
            transactions.setOwnerDeposit(0.0);
        }
        else{
            transactions.setTransactionType(TransactionType.SALE);
        }
        transactions.setStatus(TransactionStatus.ONGOING);
        transactions.setDeadline(LocalDateTime.now().plusMinutes(5));
        transactionRepository.save(transactions);
        log.info("Transaction created",transactions);
        return transactionMapper.transactionToResponse(transactions);
    }

    @Override
    public TransactionResponse getTransaction(String transactionId) {
        return transactionMapper.transactionToResponse(transactionRepository.findByTransactionId(transactionId).orElseThrow(()->
                new TransactionException(ErrorType.TRANSACTION_NOT_FOUND)));
    }

    @Override
    public List<TransactionResponse> getTransactionsByUserId(String userId) {
        List<Transactions> transactions = transactionRepository.findAllByOwnerIdOrOffererId(userId,userId).orElseThrow(()-> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        return transactions.stream().map(transactionMapper::transactionToResponse).toList();

    }





}
