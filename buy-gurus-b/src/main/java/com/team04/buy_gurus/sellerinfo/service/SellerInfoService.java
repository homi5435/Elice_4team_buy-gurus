package com.team04.buy_gurus.sellerinfo.service;

import com.team04.buy_gurus.sellerinfo.repository.SellerInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerInfoService {

    private final SellerInfoRepository sellerInfoRepository;
}
