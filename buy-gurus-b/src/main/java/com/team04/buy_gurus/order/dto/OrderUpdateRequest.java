package com.team04.buy_gurus.order.dto;

import com.team04.buy_gurus.common.annotation.validation.ValueInList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

public class OrderUpdateRequest {
    @Getter
    public static class Address {
        @NotBlank(message = "배송지는 공백이 될 수 없습니다.")
        private String address;

        @NotBlank(message = "수취자 이름은 공백이 될 수 없습니다.")
        private String name;

        @Pattern(regexp = "^(010)-?([0-9]{4})-?([0-9]{4})$", message = "전화번호는 010-XXXX-XXXX 또는 하이픈이 빠진 형태입니다.")
        private String phoneNum;
    }

    @Getter
    public static class Invoice {
        @NotBlank(message = "운송 회사는 공백이 될 수 없습니다.")
        private String shippingCompany;

        @NotBlank(message = "송장번호는 공백이 될 수 없습니다.")
        private String invoiceNum;
    }

    @Getter
    public static class Status {
        @ValueInList(values = {"배송중", "배송완료", "환불중", "환불완료", "배송준비"})
        @NotBlank(message = "주문 상태는 공백일 수 없습니다.")
        private String status;
    }
}

