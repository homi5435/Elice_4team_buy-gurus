package com.team04.buy_gurus.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
// @ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey = "yourverylongsecretkeyshouldbe64bytesormore1234567890";
    private Long accessTokenExpiration = 3600L;
    private Long refreshTokenExpiration = 604800L;
    private String accessTokenHeader = "Authorization";
    private String refreshTokenHeader = "Authorization-refresh";

    /*
    @PostConstruct
    public void init() {
        System.out.println("secretKey: " + secretKey);
        System.out.println("accessTokenExpiration: " + accessTokenExpiration);
        System.out.println("refreshTokenExpiration: " + refreshTokenExpiration);
        System.out.println("accessTokenHeader: " + accessTokenHeader);
        System.out.println("refreshTokenHeader: " + refreshTokenHeader);
    }*/
}
