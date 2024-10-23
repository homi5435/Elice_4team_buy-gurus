package com.team04.buy_gurus.jwt.handler;

import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.refreshtoken.entity.RefreshToken;
import com.team04.buy_gurus.refreshtoken.service.RefreshTokenService;
import com.team04.buy_gurus.user.CustomUserDetails;
import com.team04.buy_gurus.user.entity.User;
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
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {

        log.info("일반 로그인 성공 핸들러 동작");
        String email = extractUsername(authentication);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        String accessToken = jwtService.createAccessToken(user.getId(), user.getRole().getValue());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, request);

        response.setStatus(HttpServletResponse.SC_OK);
        jwtService.addAccessTokenToCookie(response, accessToken);
        jwtService.addRefreshTokenToCookie(response, refreshToken.getToken());
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}

