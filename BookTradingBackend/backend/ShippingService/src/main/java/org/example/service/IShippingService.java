package org.example.service;

import org.example.dto.request.CreateExchangeRequest;
import org.example.dto.request.CreateShippingRequest;
import org.example.dto.request.UpdateShippingRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.entity.Shippings;
import org.example.entity.enums.ExchangeStatus;
import org.example.entity.enums.ShippingStatus;

import java.util.List;

public interface IShippingService {

    /**
     *
     * Yeni bir takas kargosu oluşturur.
     *
     * @param createShippingRequest Yeni kargo bilgileri
     * @return Oluşturulan kargo nesnesi
     *
     */
    Boolean createShipping(CreateShippingRequest createShippingRequest);


    public Boolean createExchange(CreateExchangeRequest createExchangeRequest);




    /**
     * Kargo durumunu günceller.
     *
     * @param updateShippingRequest Güncellenecek kargonun seri nosu
     * @return Güncellenen kargo nesnesi
     */
    public ShippingResponse updateShippingStatus(UpdateShippingRequest updateShippingRequest);

    /**
     * Gönderici veya alıcı kullanıcı ID'sine göre kargo listesini döner.
     *
     * @param userId Kullanıcı ID
     * @return Kullanıcıya ait kargoların listesi
     */
    List<ShippingResponse> getUsersShippings(String userId);

    /**
     * Takas işlemi için iki tarafın kargo süreçlerini kontrol eder.
     *
     *
     */
    public void checkExchangeStatus();


    /**
     *
     *
     * @param transactionId takas numararsı
     * @return Takas nesnesi
     */
    ExchangeResponse findByTransactionId(String transactionId);



    public ExchangeResponse cancelExchangeStatus(String transactionId) ;

    ShippingResponse delivered(String shippingSerialNumber);
    /**
     * Tüm kargoları listeleyen bir metot.
     *
     * @return List<ShippingResponse> - Tüm kargo işlemlerinin yanıt nesnesi.
     */
    List<ShippingResponse> getAllShippings();

    /**
     * Seri numarasına göre bir kargo işlemini getirir.
     *
     * @param serialNumber - Aranan kargonun seri numarası.
     * @return ShippingResponse - İlgili kargo işleminin yanıt nesnesi.
     */
    ShippingResponse getShippingBySn(String serialNumber);

    /**
     * Tüm takas işlemlerini listeleyen bir metot.
     *
     * @return List<ExchangeResponse> - Tüm takas işlemlerinin yanıt nesnesi.
     */
    List<ExchangeResponse> getAllExchanges();

    /**
     * Belirli bir duruma göre takas işlemlerini listeleyen bir metot.
     *
     * @param exchangeStatus - Aranan takas durumunun adı (örn: BEKLEMEDE, TAMAMLANDI).
     * @return List<ExchangeResponse> - Belirtilen durumdaki takas işlemlerinin yanıt nesnesi.
     */
    List<ExchangeResponse> getAllExchangesByStatus(String exchangeStatus);

    /**
     * İşlem ID'sine göre bir takas işlemini getirir.
     *
     * @param transactionId - Aranan takasın işlem ID'si.
     * @return ExchangeResponse - İlgili takas işleminin yanıt nesnesi.
     */
    ExchangeResponse getExchangeByTID(String transactionId);

    /**
     * Kullanıcı ID'sine göre takas işlemlerini listeleyen bir metot.
     *
     * @param userId - Kullanıcının ID'si.
     * @return List<ExchangeResponse> - Kullanıcıya ait tüm takas işlemlerinin yanıt nesnesi.
     */
    List<ExchangeResponse> getExchangeByUserID(String userId);


}
