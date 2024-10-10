package com.team04.buy_gurus.user.controller;

import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.user.dto.*;
import com.team04.buy_gurus.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("api/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody @Valid SignupRequestDto request) {

        try {
            userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/userMe")
    public ResponseEntity<UserInfoResponseDto> readUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserInfoResponseDto response = userService.loadUserInfo(authentication);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/userMe")
    public ResponseEntity<UserEditResponseDto> updateUser(@RequestBody @Valid UserEditRequestDto request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserEditResponseDto response = userService.editUserInfo(authentication, request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Error updating user info: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // 판매자 등록

    // 회원 탈퇴
    @DeleteMapping("/userMe")
    public ResponseEntity<Void> deleteUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            userService.withdrawal(authentication);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        jwtService.removeAccessTokenToCookie(response);
        jwtService.removeRefreshTokenToCookie(response);

        return ResponseEntity.ok().build();
    }

}
