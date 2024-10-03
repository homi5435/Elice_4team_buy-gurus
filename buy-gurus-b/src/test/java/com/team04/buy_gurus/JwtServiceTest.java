package com.team04.buy_gurus;

import com.team04.buy_gurus.config.jwt.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testJwtServiceConfiguration() {
        assertNotNull(jwtService.getSecretKey());
        assertEquals("yourverylongsecretkeyshouldbe64bytesormore1234567890", jwtService.getSecretKey());
    }
}

