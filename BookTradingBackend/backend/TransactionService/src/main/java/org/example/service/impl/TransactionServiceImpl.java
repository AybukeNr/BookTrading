package org.example.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Manager.LibraryManager;
import org.example.dto.request.*;
import org.example.dto.request.mail.TransactionMailReq;
import org.example.dto.response.*;
import org.example.dto.response.enums.ListType;
import org.example.entity.Account;
import org.example.entity.Transactions;
import org.example.entity.enums.TransactionStatus;
import org.example.entity.enums.TransactionType;
import org.example.exception.ErrorType;
import org.example.exception.TransactionException;
import org.example.external.ListManager;
import org.example.external.ShippingManager;
import org.example.external.UserManager;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;




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
    private final LibraryManager libraryManager;
    private final UserManager userManager;
    private final ShippingManager shippingManager;


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
     * her dakika depozitler yatırıldı mı diye kontrol eder
     */

    @Override
    //@Scheduled(cron = "*/30 * * * * *")
    public void checkTransactionStatus() {

        List<Transactions> ongoingTransactions = transactionRepository.findAllByStatus(TransactionStatus.ONGOING);

        if (!ongoingTransactions.isEmpty()) {
            log.info("ONGOING Transactions are checking.");

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            List<Transactions> transactionsToUpdate = ongoingTransactions.parallelStream().map(transaction -> {
                executorService.submit(() -> processTransaction(transaction));
                return transaction;
            }).toList();

            executorService.shutdown();

            transactionRepository.saveAll(transactionsToUpdate);
            log.info("Batch update completed for {} transactions.", transactionsToUpdate.size());
        }
        else {
            log.info("No ongoing transactions to check.");
        }
    }

    @Override
    public Boolean setTransactionStatus(String transacId, String status) {
        Transactions transactions = transactionRepository.findByTransactionId(transacId).orElseThrow(() -> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        if(status.equals(TransactionStatus.valueOf("CANCELLED"))) {
            ExchangeComplatedRequest exchangeComplatedRequest = new ExchangeComplatedRequest();
            exchangeComplatedRequest.setTransactionId(transacId);
            exchangeComplatedRequest.setOffererId(transactions.getOffererId());
            exchangeComplatedRequest.setOwnerId(transactions.getOwnerId());
            refundBothParties(exchangeComplatedRequest);
        }
        transactions.setStatus(TransactionStatus.valueOf(status));
        return true;
    }

    @Override
    public Double calculateTrustFee(Long bookId) {
        // Kitap durumunu alıyoruz
        String bookCondition = libraryManager.getBookCondition(bookId);

        // Kitap durumu ile güvence bedelini hesaplıyoruz
        Map<String, Double> conditionTrustFees = Map.of(
                "Yeni", 50.0,
                "İyi", 40.0,
                "Kabul Edilebilir", 30.0,
                "Kötü", 10.0
        );
        Double trustFee = conditionTrustFees.getOrDefault(bookCondition, 30.0);
        double cargoFee = 60.0;
        trustFee += cargoFee;
        // Kitap durumu için güvence bedelini getir, yoksa varsayılan bedeli uygula
        return  trustFee;
    }

    @Transactional
    protected void processTransaction(Transactions transaction) {
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

                transaction.setUpdatedDate(LocalDateTime.now());
                transactionRepository.save(transaction);
//                TransactionMailReq transactionMailReq = new TransactionMailReq();
//                transactionMailReq.setStatus(String.valueOf(transaction.getStatus()));
//                transactionMailReq.setOffererId(transaction.getOffererId());
//                transactionMailReq.setOwnerId(transaction.getOwnerId());
//                transactionMailReq.setTrustFee(0.0);
                //sendComplatedMail(transactionMailReq);
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

        Transactions transactions = transactionRepository.findByOwnerIdAndOffererIdAndTransactionId(
                        exchangeComplatedRequest.getOwnerId(),
                        exchangeComplatedRequest.getOffererId(),
                        exchangeComplatedRequest.getTransactionId())
                .orElseThrow(() -> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));

        if (!TransactionStatus.COMPLETED.equals(transactions.getStatus())) {
            throw new TransactionException(ErrorType.INVALID_TRANSACTION_STATUS);
        }
        Account offererAccount = accountRepository.findByUserId(exchangeComplatedRequest.getOffererId())
                .orElseThrow(() -> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));
        Account ownerAccount = accountRepository.findByUserId(exchangeComplatedRequest.getOwnerId())
                .orElseThrow(() -> new TransactionException(ErrorType.ACCOUNT_NOT_FOUND));


            offererAccount.setBalance(offererAccount.getBalance() + transactions.getOffererDeposit());
            log.info("Refunded {} to offerer account: {}", transactions.getOffererDeposit(), offererAccount.getIban());
            
            ownerAccount.setBalance(ownerAccount.getBalance() + transactions.getOwnerDeposit());
            log.info("Refunded {} to owner account: {}", transactions.getOwnerDeposit(), ownerAccount.getIban());

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
     * @author SCTRLL
     * başarısızlık durumunda güvence bedeli mağdur tarafa geçer
     * @param transferAllReq
     * @return transactionResponse
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
        userManager.reduceTrustPoint(transferAllReq.getTransferUserId().equals(transactions.getOwnerId()) ? transactions.getOffererId() : transactions.getOwnerId());
        return transactionMapper.transactionToResponse(transactions);
    }

    @Override
    public boolean isDeadlineExceeded(Transactions transaction) {
        return transaction.getCreatedDate()
                .plusMinutes(1)
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
    public List<TransactionResponse> getUsersExchanges(String userId) {
        List<Transactions> exchanges = transactionRepository.findAllByOwnerIdOrOffererId(userId, userId)
                .orElseThrow(() -> new TransactionException(ErrorType.LIST_NOT_FOUND));

        return exchanges.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.EXCHANGE)
                .map(transactionMapper::transactionToResponse)
                .toList();
    }

    @Override
    public List<TransactionResponse> getUsersSales(String userId) {
        List<Transactions> sales = transactionRepository.findAllByOwnerIdOrOffererId(userId, userId)
                .orElseThrow(() -> new TransactionException(ErrorType.LIST_NOT_FOUND));

        return sales.stream()
                .filter(transaction -> transaction.getTransactionType() == TransactionType.SALE)
                .map(transactionMapper::transactionToResponse)
                .toList();
    }


    @Override
    @Transactional
    public void takePayment(TakePaymentRequest takePaymentRequest) {
        Transactions transactions = transactionRepository.findByListId(takePaymentRequest.getListId()).orElseThrow(()-> new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        if(Objects.equals(transactions.getOwnerId(), takePaymentRequest.getUserId())) {
            transactions.setOwnerDeposit(takePaymentRequest.getAmount());
        } else if (Objects.equals(transactions.getOffererId(), takePaymentRequest.getUserId())) {
            transactions.setOffererDeposit(takePaymentRequest.getAmount());
        }
        UpdateListReq updateListReq = UpdateListReq.builder().listStatus(String.valueOf(ListsStatus.SUSPENDED))
                .listingId(transactions.getListId()).build();
        listManager.updateListStatus(updateListReq);
        log.info("Update List Request: {}", updateListReq);
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
            transactions.setTrustFee(calculateTrustFee(transactionRequest.getBookId()));
        }
        else{
            transactions.setTransactionType(TransactionType.SALE);
            Double price = listManager.getListPrice(transactionRequest.getListId()).getBody();
            transactions.setTrustFee(price);
            transactions.setOwnerDeposit(0.0);
            transactions.setOffererDeposit(price);
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

    @Override
    public List<TransactionInfo> transactionAllInfos(String userId) {
        List<TransactionInfo> transactionInfos = new ArrayList<>();
        List<Transactions> transactions = transactionRepository.findAllByOwnerIdOrOffererId(userId,userId).orElseThrow(()->
                new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        for(Transactions transaction : transactions){
            TransactionInfo transactionInfo = new TransactionInfo();
            ExchangeDetails books = listManager.getExchangeBooks(transaction.getListId()).getBody();
            ExchangeInfos exchangeInfos = shippingManager.getExchangeInfos(userId,transaction.getListId()).getBody();
            transactionInfo.setUserId(userId);
            transactionInfo.setTransactionId(transaction.getTransactionId());
            transactionInfo.setOffererDeposit(transaction.getOffererDeposit());
            transactionInfo.setOwnerDeposit(transaction.getOwnerDeposit());
            transactionInfo.setTrustFee(transaction.getTrustFee());
            transactionInfo.setShippingSerialNumber(exchangeInfos.getShippingSerialNumber());
            transactionInfo.setStatus(exchangeInfos.getStatus());
            transactionInfos.add(transactionInfo);
        }

        return transactionInfos;
    }

    @Override
    public TransactionInfo transactionInfosByListIdAndUserId(String listId,String userId) {
        Transactions transactions = transactionRepository.findByListId(listId).orElseThrow(()->
                new TransactionException(ErrorType.TRANSACTION_NOT_FOUND));
        TransactionInfo transactionInfo = new TransactionInfo();
        ExchangeDetails books = listManager.getExchangeBooks(transactions.getListId()).getBody();
        ExchangeInfos exchangeInfos = shippingManager.getExchangeInfos(userId,transactions.getListId()).getBody();
        transactionInfo.setUserId(userId);
        transactionInfo.setTransactionId(transactions.getTransactionId());
        transactionInfo.setOffererDeposit(transactions.getOffererDeposit());
        transactionInfo.setOwnerDeposit(transactions.getOwnerDeposit());
        transactionInfo.setTrustFee(transactions.getTrustFee());
        transactionInfo.setShippingSerialNumber(exchangeInfos.getShippingSerialNumber());
        transactionInfo.setStatus(exchangeInfos.getStatus());
        return transactionInfo;
    }


}
