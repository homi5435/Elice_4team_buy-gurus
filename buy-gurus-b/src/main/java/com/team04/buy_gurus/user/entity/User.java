package com.team04.buy_gurus.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(name = "img_url", nullable = true)
    private String imageUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "provide_id", nullable = true)
    private String providerId;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateEmail(String email){
        this.email = email;
    }

    public void updateRole(){
        this.role = Role.SELLER;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
