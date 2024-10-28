package com.team04.buy_gurus.config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermitAllUrlConfig {

    public List<String> getPermitAllUrls() {
        return List.of(
                "/home",
                "/css/**",
                "/images/**",
                "/js/**",
                "/favicon.ico",
                "/h2-console/**",
                "/api/login",
                "/api/signup",
                "/oauth2/authorization/**",
                "/api/auth/**",
                "/api/reset-password",
                "/api/token",
                "/api/category",
                "/api/product/**"
        );
    }
}

