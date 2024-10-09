package com.team04.buy_gurus.jwt.handler;

import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        log.info("일반 로그인 성공 핸들러 동작");
        String email = extractUsername(authentication);
        Long userId = userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
                });
        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken();

        response.setStatus(HttpServletResponse.SC_OK);
        jwtService.addAccessTokenToCookie(response, accessToken);
        jwtService.addRefreshTokenToCookie(response, refreshToken);

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.updateRefreshToken(refreshToken);
                    userRepository.saveAndFlush(user);
                });
    }

        private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
