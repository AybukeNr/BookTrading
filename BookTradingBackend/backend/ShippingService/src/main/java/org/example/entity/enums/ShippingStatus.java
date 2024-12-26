package org.example.entity.enums;
public enum ShippingStatus {
    BEKLEMEDE,      // Kargo bekleme aşamasında
    KARGOLANDI,     // Kargo teslim edildi
    TESLIM_EDILDI,  // Kargo alıcıya ulaştı
    İPTAL_EDİLDİ    // Kargo işlemi iptal edildi
}
