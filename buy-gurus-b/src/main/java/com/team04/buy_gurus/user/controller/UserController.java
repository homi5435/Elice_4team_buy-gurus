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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

            userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/userMe")
    public ResponseEntity<UserInfoResponseDto> readUser(@AuthenticationPrincipal UserDetails userDetails) {

            UserInfoResponseDto response = userService.loadUserInfo(userDetails.getUsername());
            return ResponseEntity.ok(response);
    }

    // Void로 바꿔야 하나?
    @PatchMapping("/userMe")
    public ResponseEntity<UserEditResponseDto> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UserEditRequestDto request) {

            UserEditResponseDto response = userService.editUserInfo(userDetails.getUsername(), request);
            return ResponseEntity.ok(response);
    }

    @PatchMapping("seller-registration")
    public ResponseEntity<Void> updateRole(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid SellerRegistrationRequestDto request) {

        userService.sellerRegistration(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid ResetPasswordRequestDto request) {

        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    // 소프트 딜리트로 바꿔야 하나?
    @DeleteMapping("/userMe")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {

            userService.withdrawal(userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        jwtService.removeAccessTokenToCookie(response);
        jwtService.removeRefreshTokenToCookie(response);

        return ResponseEntity.ok().build();
    }

}
