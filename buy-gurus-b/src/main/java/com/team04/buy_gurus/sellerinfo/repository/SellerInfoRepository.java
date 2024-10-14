package com.team04.buy_gurus.sellerinfo.repository;

import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo, Long> {
}
