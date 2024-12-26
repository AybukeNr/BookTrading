package org.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000,"Sunucuda Bilinmeyen bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(1001,"Geçersiz token",HttpStatus.BAD_REQUEST),
    INVALID_USER(1002, "Geçersiz kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003,"İstek formatı hatalı",HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1004, "Kullanıcı zaten kayıtlı", HttpStatus.BAD_REQUEST),
    LIST_NOT_FOUND(1005, "İlan Bulunamadı.", HttpStatus.BAD_REQUEST),
    MAIL_TOKEN_NOT_FOUND(1006, "Token bulunamadı.", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED(1007, "Token süresi dolmuş.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_OR_PASSWORD(1008, "Kullanıcı Adı veya Şifre Hatalı", HttpStatus.BAD_REQUEST),
    TOKEN_TYPE_AND_PROCESS_DOESNT_MATCH(1009, "Token tipi ve yapılan işlem uyuşmuyor.", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_USED(1010, "Token daha önce kullanılmış.", HttpStatus.BAD_REQUEST),
    MAIL_ADDRESS_ALREADY_EXISTS(1011,"Bu mail adresi kullanılamaz",HttpStatus.BAD_REQUEST),
    OFFER_NOT_FOUND(1012,"Teklif bulunamadı" ,HttpStatus.NOT_FOUND );
    int code;
    String message;
    HttpStatus httpStatus;
}