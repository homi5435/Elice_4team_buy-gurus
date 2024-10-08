package com.team04.buy_gurus.address.repository;

import com.team04.buy_gurus.address.domain.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {
}
