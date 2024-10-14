package com.team04.buy_gurus.sellerinfo.entity;

import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_number")
    private String businessNum;

    @Column(name = "business_phone_number")
    private String businessPhoneNum;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "business_adress")
    private String businessAdress;

    @OneToOne(mappedBy = "sellerInfo")
    private User user;

    // 필수 서류
}
