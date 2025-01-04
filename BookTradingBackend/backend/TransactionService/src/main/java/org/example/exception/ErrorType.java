package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000, "Sunucuda bilinmeyen bir hata oluştu.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USER(1001,"Geçersiz Kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003, "İstek formatı hatalı.", HttpStatus.BAD_REQUEST),
    LIST_NOT_FOUND(2001, "İlan bulunamadı.", HttpStatus.NOT_FOUND),
    OFFER_ALREADY_EXISTS(2002, "Bu teklif zaten mevcut.", HttpStatus.BAD_REQUEST),
    OFFER_CANNOT_BE_UPDATED(2003, "Bu teklif güncellenemez.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(2004, "Yetersiz Bakiye.", HttpStatus.BAD_REQUEST),
    INVALID_OFFER_STATUS(2005, "Geçersiz teklif durumu.", HttpStatus.BAD_REQUEST),
    OFFER_AMOUNT_EXCEEDS_LIMIT(2006, "Teklif miktarı izin verilen sınırı aşıyor.", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND(2007, "Ödeme Bulunamadı.", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND(2008, "HESAP BULUNAMADI.", HttpStatus.NOT_FOUND),
    INVALID_USER_FOR_OFFER(2009, "Teklif için geçersiz kullanıcı.", HttpStatus.FORBIDDEN),
    OFFER_OWNER_CONFLICT(2010, "Bu teklifi yalnızca sahibi değiştirebilir.", HttpStatus.FORBIDDEN),
    INVALID_OFFER_PARAMETERS(2011, "Teklif parametreleri hatalı.", HttpStatus.BAD_REQUEST),
    CARD_NOT_FOUND(1005, "kart bulunamadı.", HttpStatus.NOT_FOUND),
    TOKEN_ALREADY_USED(1010, "Token daha önce kullanılmış.", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(1012,"Kitap bulunamadı" ,HttpStatus.NOT_FOUND ),
    TRANSACTION_NOT_FOUND(2001, "Ödeme Takası bulunamadı.", HttpStatus.NOT_FOUND),
    INVALID_TRANSACTION_STATUS(2002,"Geçersiz Takas Durumu" ,  HttpStatus.BAD_REQUEST ),
    NO_TRANSACTIONS_FOUND(2003,"Takas bulunamadı" ,HttpStatus.NOT_FOUND ),
    INVALID_CARD_INFO(2004,"Geçersiz kart bilgisi" ,HttpStatus.BAD_REQUEST );

    int code;
    String message;
    HttpStatus httpStatus;
}
