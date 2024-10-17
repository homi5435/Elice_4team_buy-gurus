package com.team04.buy_gurus.sellerinfo.repository;

import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo, Long> {
    @Query("SELECT si FROM SellerInfo si JOIN si.user u WHERE u.email=:email")
    Optional<SellerInfo> findByUseremail(@Param("email") String email);
}
