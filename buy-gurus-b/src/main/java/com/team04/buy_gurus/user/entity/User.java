package com.team04.buy_gurus.user.entity;

import com.team04.buy_gurus.refreshtoken.entity.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
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

    @Column
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens= new ArrayList<>();

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateEmail(String email){
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}

