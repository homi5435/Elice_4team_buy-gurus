package com.team04.buy_gurus.sellerinfo.controller;

import com.team04.buy_gurus.sellerinfo.service.SellerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SellerInfoController {

    private final SellerInfoService sellerInfoService;
}
