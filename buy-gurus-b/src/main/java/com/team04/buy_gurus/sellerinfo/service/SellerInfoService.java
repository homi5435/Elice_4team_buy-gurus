package com.team04.buy_gurus.sellerinfo.service;

import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.sellerinfo.dto.SellerRegisterRequestDto;
import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import com.team04.buy_gurus.sellerinfo.repository.SellerInfoRepository;
import com.team04.buy_gurus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class SellerInfoService {

    private final SellerInfoRepository sellerInfoRepository;
    private final UserRepository userRepository;
    private final String UPLOAD_DIR = "/uploads/seller-documents/";

    public void sellerInfoRegister(Long userId, SellerRegisterRequestDto sellerRegisterRequestDto){

        String businessRegistrationFilePath = saveFile(sellerRegisterRequestDto.getBusinessRegistrationFile());
        String bankAccountCopyFilePath = saveFile(sellerRegisterRequestDto.getBankAccountCopyFile());
        String identityProofFilePath = saveFile(sellerRegisterRequestDto.getIdentityProofFile());

        SellerInfo sellerInfo = SellerInfo.builder()
                .businessPhoneNum(sellerRegisterRequestDto.getBusinessPhoneNum())
                .businessRegistrationFile(businessRegistrationFilePath)
                .bankAccountCopyFile(bankAccountCopyFilePath)
                .identityProofFile(identityProofFilePath)
                .user(userRepository.findById(userId).orElseThrow(UserNotFoundException::new))
                .build();

        sellerInfoRepository.save(sellerInfo);
    }

    private String saveFile(MultipartFile file) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + file.getOriginalFilename(), e);
        }
    }
}
