package com.team04.buy_gurus.jwt.filter;

import com.team04.buy_gurus.config.PermitAllUrlConfig;
import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.user.CustomUserDetails;
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

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PermitAllUrlConfig permitAllUrlConfig;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtAuthenticationFilter 동작");
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (accessToken != null) {
            // accessToken = accessToken.substring(7);
            log.info("엑세스 토큰 검증 통과");
            checkAccessTokenAndAuthentication(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    public void checkAccessTokenAndAuthentication(String accessToken) {

        Long userId = jwtService.extractUserId(accessToken).orElse(null);
        String role = jwtService.extractRole(accessToken).orElse(null);
        saveAuthentication(userId, role);
    }

    public void saveAuthentication(Long userId, String role) {

        CustomUserDetails customUserDetails = new CustomUserDetails(userId, role);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null,
                        authoritiesMapper.mapAuthorities(customUserDetails.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
