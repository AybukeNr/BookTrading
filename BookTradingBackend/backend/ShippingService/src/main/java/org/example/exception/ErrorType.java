package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000,"Sunucuda Bilinmeyen bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(1001,"Geçersiz token",HttpStatus.BAD_REQUEST),
    INVALID_USER(1002, "Geçersiz kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003,"İstek formatı hatalı",HttpStatus.BAD_REQUEST),
    SHIPPING_NOT_FOUND(1004, "Kargo bulunamadı", HttpStatus.NOT_FOUND),
    EXCHANGE_NOT_FOUND(1005, "Kullanıcı Bulunamadı.", HttpStatus.BAD_REQUEST),
    MAIL_TOKEN_NOT_FOUND(1006, "Token bulunamadı.", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED(1007, "Token süresi dolmuş.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_OR_PASSWORD(1008, "Kullanıcı Adı veya Şifre Hatalı", HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_AND_PROCESS_DOESNT_MATCH(1009, "Token tipi ve yapılan işlem uyuşmuyor.", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_USED(1010, "Token daha önce kullanılmış.", HttpStatus.BAD_REQUEST),
    MAIL_ADDRESS_ALREADY_EXISTS(1011,"Bu mail adresi kullanılamaz",HttpStatus.BAD_REQUEST),
    EXCHANGE_CANNOT_BE_CANCELLED(1012, "Takas iptal edilemez" , HttpStatus.BAD_REQUEST ),
    INVALID_STATUS(1013,"Geçersiz takas durumu" , HttpStatus.BAD_REQUEST);
    int code;
    String message;
    HttpStatus httpStatus;
}