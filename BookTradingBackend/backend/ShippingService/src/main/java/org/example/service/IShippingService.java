package org.example.service;

import org.example.dto.request.CreateShippingRequest;
import org.example.dto.response.ShippingResponse;
import org.example.entity.ExchangeManager;
import org.example.entity.Shippings;
import org.example.entity.enums.ShippingStatus;

import java.util.List;

public interface IShippingService {

    /**
     * Yeni bir kargo oluşturur.
     *
     * @param createShippingRequest Yeni kargo bilgileri
     * @return Oluşturulan kargo nesnesi
     */
     ShippingResponse createShippingforExchange(CreateShippingRequest createShippingRequest);

    /**
     * Belirtilen kargo ID'sine göre kargo bilgisini döner.
     *
     * @param shippingSerialNumber Kargo ID
     * @return Kargo nesnesi
     */
    Shippings getShippingByShippingSerialNumber(String shippingSerialNumber);

    /**
     * Kullanıcının kargo takip numarasını günceller.
     *
     * @param transactionId takas numararsı
     * @return Takas nesnesi
     */
    ExchangeManager findByTransactionId(String transactionId);

    /**
     * Kargo durumunu günceller.
     *
     * @param shippingId Güncellenecek kargonun ID'si
     * @param status Yeni kargo durumu
     * @return Güncellenen kargo nesnesi
     */
    Shippings updateShippingStatus(String shippingId, ShippingStatus status);

    /**
     * Gönderici veya alıcı kullanıcı ID'sine göre kargo listesini döner.
     *
     * @param userId Kullanıcı ID
     * @return Kullanıcıya ait kargoların listesi
     */
    List<Shippings> getShippingsByUser(String userId);

    /**
     * Takas işlemi için iki tarafın kargo süreçlerini kontrol eder.
     *
     * @param exchange Takas işlemi
     * @return Eğer iki taraf da kargo süreçlerini tamamlamışsa true, aksi halde false
     */
    boolean isExchangeComplete(ExchangeManager exchange);

    /**
     * Kargo takip numarasını doğrular.
     *
     * @param trackingNumber Doğrulanacak kargo takip numarası
     * @return Geçerli bir takip numarası ise true, aksi halde false
     */
    boolean validateTrackingNumber(String trackingNumber);

    /**
     * Kargonun sürecinin belirtilen kullanıcı tarafından iptal edilip edilemeyeceğini kontrol eder.
     *
     * @param shippingId Kargo ID
     * @param userId Kullanıcı ID
     * @return Eğer iptal edilebilir ise true, aksi halde false
     */
    boolean canCancelShipping(String shippingId, String userId);

    /**
     * Belirtilen kargo ID'sini siler.
     *
     * @param shippingId Silinecek kargonun ID'si
     */
    void deleteShipping(String shippingId);
}
