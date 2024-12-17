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
    INVALID_TOKEN(1001, "Geçersiz token.", HttpStatus.BAD_REQUEST),
    INVALID_USER(1002, "Geçersiz kullanıcı.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003, "İstek formatı hatalı.", HttpStatus.BAD_REQUEST),
    OFFER_NOT_FOUND(2001, "Teklif bulunamadı.", HttpStatus.NOT_FOUND),
    OFFER_ALREADY_EXISTS(2002, "Bu teklif zaten mevcut.", HttpStatus.BAD_REQUEST),
    OFFER_CANNOT_BE_UPDATED(2003, "Bu teklif güncellenemez.", HttpStatus.BAD_REQUEST),
    OFFER_CANNOT_BE_DELETED(2004, "Bu teklif silinemez.", HttpStatus.BAD_REQUEST),
    INVALID_OFFER_STATUS(2005, "Geçersiz teklif durumu.", HttpStatus.BAD_REQUEST),
    OFFER_AMOUNT_EXCEEDS_LIMIT(2006, "Teklif miktarı izin verilen sınırı aşıyor.", HttpStatus.BAD_REQUEST),
    OFFER_CREATION_FAILED(2007, "Teklif oluşturulamadı.", HttpStatus.INTERNAL_SERVER_ERROR),
    OFFER_UPDATE_FAILED(2008, "Teklif güncellenemedi.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USER_FOR_OFFER(2009, "Teklif için geçersiz kullanıcı.", HttpStatus.FORBIDDEN),
    OFFER_OWNER_CONFLICT(2010, "Bu teklifi yalnızca sahibi değiştirebilir.", HttpStatus.FORBIDDEN),
    INVALID_OFFER_PARAMETERS(2011, "Teklif parametreleri hatalı.", HttpStatus.BAD_REQUEST),
    LIST_NOT_FOUND(1005, "İlan bulunamadı.", HttpStatus.NOT_FOUND),
    MAIL_TOKEN_NOT_FOUND(1006, "Token bulunamadı.", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED(1007, "Token süresi dolmuş.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_OR_PASSWORD(1008, "Kullanıcı adı veya şifre hatalı.", HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_AND_PROCESS_DOESNT_MATCH(1009, "Token tipi ve yapılan işlem uyuşmuyor.", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_USED(1010, "Token daha önce kullanılmış.", HttpStatus.BAD_REQUEST),
    MAIL_ADDRESS_ALREADY_EXISTS(1011, "Bu mail adresi kullanılamaz.", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND(1012,"Kitap bulunamadı" ,HttpStatus.NOT_FOUND );

    int code;
    String message;
    HttpStatus httpStatus;
}
