package com.team04.buy_gurus.order.domain;

import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id = ?")
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // 주문 시각
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    // 주문 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PROCESSING'")
    private Status status = Status.PROCESSING;

    private String invoiceNum;
    private String shippingCompany;

    @ColumnDefault("0")
    private int shippingFee;

    // 주문자 정보
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;
    private String shippingAddress;
    private String customerName;
    private String customerPhoneNum;

    // 판매자 정보
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    private String sellerName;
    private String sellerPhoneNum;
    private String sellerCompany;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderInfo> orderInfoList;

    @AllArgsConstructor
    @Getter
    public enum Status {
        PROCESSING("준비중"),
        SHIPPING("배송중"),
        SHIPPED("배송완료"),
        REFUNDING("환불중"),
        REFUNDED("환불완료");

        private final String status;
    }


    public void setInvoice(OrderUpdateRequest.Invoice request) {
        this.invoiceNum = request.getInvoiceNum();
        this.shippingCompany = request.getShippingCompany();
        this.setStatus(Status.SHIPPING.getStatus());
    }

    public void setStatus(String status) {
        this.status = fromString(status);
    }

    public void setAddress(OrderUpdateRequest.Address request) {
        this.shippingAddress = request.getAddress();
        this.customerName = request.getName();
        this.customerPhoneNum = request.getPhoneNum();
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public void setSeller(User seller) {
        this.seller = seller;
        this.sellerCompany = "buy gurus";
        this.sellerName = "buy gurus";
        this.sellerPhoneNum = "010-0000-0000";
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }
}
