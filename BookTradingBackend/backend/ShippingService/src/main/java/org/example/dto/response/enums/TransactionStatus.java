package org.example.dto.response.enums;

public enum TransactionStatus {
    ONGOING,   // Güvence bedeli yatırılıyor
    COMPLETED, // Her iki tarafta güvence bedelini yatırdı
    FAILED,
    SWAPPED,//SATIŞ TAMAMLANDI
    REFUNDED, //Takas tamamlandı ,Her iki tarafa güvence bedeli iade edildi
    CANCELLED  // İşlem iptal edildi, karşı tarafa aktarım
}
