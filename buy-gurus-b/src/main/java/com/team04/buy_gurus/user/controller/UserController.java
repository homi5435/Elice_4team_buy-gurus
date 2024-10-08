package com.team04.buy_gurus.user.controller;

import com.team04.buy_gurus.user.dto.SignupRequestDto;
import com.team04.buy_gurus.jwt.service.LoginService;
import com.team04.buy_gurus.user.dto.UserInfoResponseDto;
import com.team04.buy_gurus.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody SignupRequestDto request) {
        try {
            userService.signup(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "회원가입 성공");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원가입 실패: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // 회원 정보 불러오기
    @GetMapping("/userMe")
    public ResponseEntity<> readUser(HttpServletRequest request) {

    }

    // 회원 정보 수정
    // 판매자 등록
    // 회원 탈퇴

}
