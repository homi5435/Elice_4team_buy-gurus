package com.team04.buy_gurus.sellerinfo.repository;

import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo, Long> {
    @Query("Select si FROM SellerInfo si WHERE si.user.id=:userId")
    Optional<SellerInfo> findByUserId(@Param("id") Long userId);
}
