package org.example.entity.enums;
public enum ShippingStatus {
    BEKLEMEDE,      // Kargo bekleme aşamasında
    KARGOLANDI,     // Kargo yola çıktı
    TESLIM_EDILDI,  // Kargo alıcıya ulaştı
    İPTAL_EDİLDİ    // Kargo işlemi iptal edildi
}
