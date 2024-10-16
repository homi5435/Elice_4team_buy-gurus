package com.team04.buy_gurus.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
public class OrderRequest {
    @PositiveOrZero(message = "배송 가격은 0 이상이어야 합니다.")
    private int shippingFee;

    @Valid
    private List<OrderInfoRequest> orderInfoList;

    @Valid
    private ShippingInfo shippingInfo;

    @Positive(message = "판매자 id가 존재하지 않습니다.")
    private Long sellerId;

    @Getter
    public static class OrderInfoRequest {
//        @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
//        private int price;

        @Positive(message = "물건 개수는 1 이상이어야 합니다.")
        private int quantity;

        @Positive(message = "물건 id는 1 이상이어야 합니다.")
        private Long productId;
    }

    @Getter
    public static class ShippingInfo {
        @NotBlank(message = "주소는 필수 입력 사항입니다.")
        private String address;

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        private String name;

        @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
        @Pattern(regexp = "^(010)-?([1-9]{1}[0-9]{3})-?([0-9]{4})$", message = "전화번호는 010-XXXX-XXXX 또는 하이픈이 빠진 형태입니다.")
        private String phoneNum;
    }
}