package com.team04.buy_gurus.config;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermitAllUrlConfig {

    public List<String> getPermitAllUrls() {
        return List.of(
                "/",
                "/css/**",
                "/images/**",
                "/js/**",
                "/favicon.ico",
                "/h2-console",
                "/signup",
                "/oauth2/authorization/**",
                "/api/auth/**"
        );
    }
}

