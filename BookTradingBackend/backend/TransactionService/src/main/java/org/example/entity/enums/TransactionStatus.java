package org.example.entity.enums;

public enum TransactionStatus {
    ONGOING,   // Güvence bedeli yatırılıyor
    COMPLETED, // İşlem tamamlandı, iade edildi
    CANCELLED  // İşlem iptal edildi, karşı tarafa aktarım
}
