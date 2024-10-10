package com.team04.buy_gurus.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String verificationCode) throws MessagingException {
        log.info("이메일 서비스");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email);
        helper.setSubject("이메일 인증 코드");
        helper.setText("인증 코드: " + verificationCode);

        mailSender.send(message);
    }

    public String generateVerificationCode() {
        int randomCode = (int) (Math.random() * 1000000);
        return String.format("%06d", randomCode);
    }
}
