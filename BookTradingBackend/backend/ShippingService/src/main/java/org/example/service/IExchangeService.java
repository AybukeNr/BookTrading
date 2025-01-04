package org.example.service;

import org.example.dto.request.CreateExchangeRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.entity.Exchange;
import org.example.entity.enums.ExchangeStatus;

public interface IExchangeService {

    /**
     * Takas işlemi için iki tarafın kargo süreçlerini kontrol eder.
     *
     * @return Eğer iki taraf da kargo süreçlerini tamamlamışsa true, aksi halde false
     */
    public Boolean checkExchangeStatus();


    /**
     *
     *
     * @param transactionId takas numararsı
     * @return Takas nesnesi
     */
    ExchangeResponse findByTransactionId(String transactionId);



    public ExchangeResponse cancelExchangeStatus(String transactionId);


}
