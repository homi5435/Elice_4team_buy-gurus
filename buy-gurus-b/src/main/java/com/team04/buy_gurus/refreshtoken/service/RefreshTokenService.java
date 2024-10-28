package com.team04.buy_gurus.refreshtoken.service;

import com.team04.buy_gurus.email.EmailService;
import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.refreshtoken.entity.RefreshToken;
import com.team04.buy_gurus.refreshtoken.repository.RefreshTokenRepository;
import com.team04.buy_gurus.user.entity.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String USER_AGENT = "User-Agent";

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public RefreshToken createRefreshToken(User user, HttpServletRequest request) {

        String randomString = generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(randomString)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader(USER_AGENT))
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void tokenReissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = jwtService.extractRefreshToken(request)
                .orElse(null);

        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .filter(rt -> refreshTokenValid(rt, request))
                .orElse(null);

        if (refreshToken != null) {

            String newAccessToken = jwtService.createAccessToken(refreshToken.getUser().getId(), refreshToken.getUser().getRole().getValue());
            RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser(), request);

            refreshTokenRepository.delete(refreshToken);

            jwtService.addAccessTokenToCookie(response, newAccessToken);
            jwtService.addRefreshTokenToCookie(response, newRefreshToken.getToken());

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    public boolean refreshTokenValid(RefreshToken refreshToken, HttpServletRequest request) {

        return !refreshToken.getExpiryDate().isBefore(LocalDateTime.now()) &&
                refreshToken.getIpAddress().equals(request.getRemoteAddr()) &&
                refreshToken.getUserAgent().equals(request.getHeader(USER_AGENT));
    }


    private String generateRefreshToken() {

        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
