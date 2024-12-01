package org.example.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {
    private final long expirationTime=  3600000; //1 hour

    @Value("${jwt.secretkey}")
    String secretKey;
    @Value("${jwt.audience}")
    String audience;
    @Value("${jwt.issuer}")
    String issuer;

    public Optional<String> createToken(Long id,String username, String phoneNumber){
        try{
            String token;
            token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .withClaim("id", id)
                    .withClaim("username",username)
                    .withClaim("phonelNumber", phoneNumber)
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<Long> getByIdFromToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT==null)
                return Optional.empty();
            Long id = decodedJWT.getClaim("id").asLong();
            return Optional.of(id);
        }catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<String> getRoleFromToken(String token){
        try{Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier=JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build();
            DecodedJWT decodeJWT=verifier.verify(token);
            if(decodeJWT==null){
                throw new AuthException(ErrorType.INVALID_TOKEN);
            }
            String role=decodeJWT.getClaim("role").asString();
            return Optional.of(role);
        }catch (Exception e){
            throw new AuthException(ErrorType.INVALID_TOKEN);
        }
    }

}