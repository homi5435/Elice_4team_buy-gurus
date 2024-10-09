package com.team04.buy_gurus.oauth.handler;

import com.team04.buy_gurus.jwt.JwtProperties;
import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.oauth.CustomOAuth2User;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, oAuth2User);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken();

        response.setStatus(HttpServletResponse.SC_OK);
        jwtService.addAccessTokenToCookie(response, accessToken);
        jwtService.addRefreshTokenToCookie(response, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getUserId(), refreshToken);

        try {
            String redirectUrl = "http://localhost:5173/home";
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
