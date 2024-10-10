package com.team04.buy_gurus.jwt.filter;

import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import com.team04.buy_gurus.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL1 = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(NO_CHECK_URL1)) {
            filterChain.doFilter(request, response);
            log.info("JwtAuthenticationFilter 생략");
            return;
        }

        log.info("JwtAuthenticationFilter 동작");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (accessToken != null) {
            // accessToken = accessToken.substring(7);
            log.info("엑세스 토큰 검증 통과");
            checkAccessTokenAndAuthentication(request, response, filterChain, accessToken);
        }

        if (accessToken == null) {
            log.info("엑세스 토큰 검증 실패");
            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter(jwtService::isTokenValid)
                    .orElse(null);

            if (refreshToken != null) {
                User user = userRepository.findByRefreshToken(refreshToken)
                        .orElseThrow(() -> {
                            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
                        });
                checkRefreshTokenAndReIssueAccessToken(response, user);
            }

            // 로그인 화면으로 리다이렉트

            filterChain.doFilter(request, response);
        }
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, User user) {

        reIssueRefreshToken(user);
        response.setStatus(HttpServletResponse.SC_OK);
        String accessToken = jwtService.createAccessToken(user.getId());
        jwtService.addAccessTokenToCookie(response, accessToken);
    }

    private void reIssueRefreshToken(User user) {
        String refreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(refreshToken);
        userRepository.saveAndFlush(user);
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  FilterChain filterChain,
                                                  String accessToken) throws ServletException, IOException {

        jwtService.extractUserId(accessToken)
                .ifPresent(userId -> userRepository.findById(userId)
                        .ifPresent(this::saveAuthentication));

        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User user) {
        String password = user.getPassword();
        if (password == null) {
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));
        log.info("saveAuthentication 동작");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
