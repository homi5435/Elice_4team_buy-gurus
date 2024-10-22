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

    @Column(name = "business_phone_number")
    private String businessPhoneNum;

    @Column(name = "business_registration_file")
    private String businessRegistrationFile;

    @Column(name = "bank_account_copy_file")
    private String bankAccountCopyFile;

    @Column(name = "identity_proof_file")
    private String identityProofFile;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
