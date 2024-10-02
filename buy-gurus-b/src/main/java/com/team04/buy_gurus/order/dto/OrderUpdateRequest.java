package com.team04.buy_gurus.order.dto;

import lombok.Getter;

public class OrderUpdateRequest {
    @Getter
    public static class Address {
        private String address;
        private String name;
        private String phoneNumber;
    }

    @Getter
    public static class InvoiceNumber {
        private String shippingCompany;
        private String invoiceNumber;
    }

    @Getter
    public static class Status {
        private String status;
    }
}

