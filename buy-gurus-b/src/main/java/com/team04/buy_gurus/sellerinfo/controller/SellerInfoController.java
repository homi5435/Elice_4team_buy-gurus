package com.team04.buy_gurus.sellerinfo.controller;

import com.team04.buy_gurus.sellerinfo.dto.SellerRegisterRequestDto;
import com.team04.buy_gurus.sellerinfo.service.SellerInfoService;
import com.team04.buy_gurus.user.CustomUserDetails;
import com.team04.buy_gurus.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SellerInfoController {

    private final SellerInfoService sellerInfoService;

    @PostMapping("/seller/register")
    public ResponseEntity<UserResponse<Void>> createSellerInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @ModelAttribute SellerRegisterRequestDto sellerRegisterRequestDto){

        sellerInfoService.sellerInfoRegister(customUserDetails.getUserId(), sellerRegisterRequestDto);
        return ResponseEntity.ok(new UserResponse<>("판매자 정보 등록 성공", null));
    }

    //@GetMapping("/admin/seller-registration")

}
