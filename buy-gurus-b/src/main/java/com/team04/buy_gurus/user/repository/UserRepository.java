package com.team04.buy_gurus.user.repository;

import com.team04.buy_gurus.user.entity.Provider;
import com.team04.buy_gurus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);
    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
}
