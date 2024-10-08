package com.team04.buy_gurus.address.domain;

import com.team04.buy_gurus.address.dto.AddressInfoRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE address_info SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor
@Data
@Table(name = "address_info")
public class AddressInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(nullable = false)
    private String address;

    private boolean isRecent;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNum;

    public AddressInfo(AddressInfoRequest addressInfoRequest) {
        this.address = addressInfoRequest.getAddress();
        this.name = addressInfoRequest.getName();
        this.phoneNum = addressInfoRequest.getPhoneNum();
    }

    public AddressInfo(String name, String phoneNum, String address) {
        this.address = address;
        this.name = name;
        this.phoneNum = phoneNum;
    }
}
