package com.team04.buy_gurus.email;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class EmailController {

    private final EmailService emailService;

    private Map<String, String> verificationCodes = new HashMap<>();

    @PostMapping("/send-verification-email")
    public String sendVerificationEmail(@RequestParam("email") String email) throws MessagingException {
        String code = emailService.generateVerificationCode();
        verificationCodes.put(email, code);
        emailService.sendVerificationEmail(email, code);
        return "인증 코드가 이메일로 전송되었습니다.";
    }

    @PostMapping("/verify-code")
    public String verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        log.info("인증번호 검증");
        String savedCode = verificationCodes.get(email);
        if (savedCode != null && savedCode.equals(code)) {
            verificationCodes.remove(email);
            return "인증 성공";
        } else {
            return "인증 코드가 일치하지 않습니다.";
        }
    }
}
