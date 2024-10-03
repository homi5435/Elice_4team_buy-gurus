package com.team04.buy_gurus.config.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
    private String accessTokenHeader;
    private String refreshTokenHeader;
}
