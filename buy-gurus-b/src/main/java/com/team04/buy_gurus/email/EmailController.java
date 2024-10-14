package com.team04.buy_gurus.email;

import com.team04.buy_gurus.user.dto.UserResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-verification-email")
    public ResponseEntity<UserResponse<Void>> sendVerificationEmail(@RequestParam("email") String email) throws MessagingException {

        emailService.sendVerificationCode(email);
        return ResponseEntity.ok(new UserResponse<>("인증코드 전송 성공", null));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<UserResponse<Void>> verifyCode(@RequestBody VerifyRequestDto request) {
        log.info("인증번호 검증");
        emailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(new UserResponse<>("인증코드 검증 성공", null));
    }
}
