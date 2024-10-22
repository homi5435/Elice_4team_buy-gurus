package com.team04.buy_gurus.user;

import com.team04.buy_gurus.user.dto.SignupRequestDto;
import com.team04.buy_gurus.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        String adminEmail = "ho@naver.com";
        String adminNickname = "admin";
        String adminPassword = "123"; // 실제 환경에서는 해시 처리된 비밀번호를 사용해야 합니다.

        try {
            SignupRequestDto adminRequest = new SignupRequestDto();
            adminRequest.setEmail(adminEmail);
            adminRequest.setNickname(adminNickname);
            adminRequest.setPassword(adminPassword);

            userService.createAdmin(adminRequest);
            System.out.println("관리자 계정이 생성되었습니다: " + adminEmail);
        } catch (Exception e) {
            System.out.println("관리자 계정 생성 중 오류 발생: " + e.getMessage());
        }
    }
}