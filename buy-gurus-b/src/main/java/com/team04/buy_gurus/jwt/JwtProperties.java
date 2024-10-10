package com.team04.buy_gurus.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {

    private String secretKey;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
    private String accessTokenHeader;
    private String refreshTokenHeader;
}
