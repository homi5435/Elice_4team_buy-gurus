package com.team04.buy_gurus.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailController {

    private final EmailService emailService;

    private Map<String, String> verificationCodes = new HashMap<>();

    @PostMapping("/send-verification-email")
    public ResponseEntity<Void> sendVerificationEmail(@RequestParam("email") String email) throws MessagingException {

        emailService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        log.info("인증번호 검증");
        emailService.verifyCode(email, code);
        return ResponseEntity.ok().build();
    }
}
