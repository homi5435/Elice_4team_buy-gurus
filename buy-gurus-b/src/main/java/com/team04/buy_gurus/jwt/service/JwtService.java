package com.team04.buy_gurus.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.team04.buy_gurus.jwt.JwtProperties;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class JwtService {

    private final JwtProperties jwtProperties;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String USER_ID_CLAIM = "user_id";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + jwtProperties.getAccessTokenExpiration()))
                .withClaim(EMAIL_CLAIM, email)
                .withClaim(USER_ID_CLAIM, email)
                .withClaim()
                .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecretKey()));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey()))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void updateRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> {
                            user.updateRefreshToken(refreshToken);
                            userRepository.saveAndFlush(user);
                        },
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtProperties.getSecretKey())).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("토큰 검증 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("예상치 못한 오류: " + e.getMessage(), e);
        }
        return false;
    }

    public void addAccessTokenToCookie(HttpServletResponse response, String accessToken) {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        log.info("crate cookie: " + accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(2 * 24 * 60 * 60);
        response.addCookie(accessTokenCookie);
    }
}
