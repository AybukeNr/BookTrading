package org.example.entity.enums;

public enum ExchangeStatus {
    BEKLEMEDE,       // İki taraf da kargo takip numarasını girmedi
    KARGO_BEKLENIYOR, // Taraflardan biri kargo bilgisi girdi
    TAMAMLANDI,      // İki taraf da kargo sürecini tamamladı
    İPTAL_EDİLDİ     // Takas iptal edildi
}
