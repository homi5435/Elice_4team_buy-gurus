package com.team04.buy_gurus.sellerinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class SellerRegisterRequestDto {

    private String businessPhoneNum;

    private MultipartFile businessRegistrationFile;
    private MultipartFile bankAccountCopyFile;
    private MultipartFile identityProofFile;
}
