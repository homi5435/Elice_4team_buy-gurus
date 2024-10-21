package com.team04.buy_gurus.order.dto;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponse {
    private Long orderId;
    private String createdAt;
    private String status;
    private ShippingInvoice invoice;
    private int shippingFee;
    private List<OrderInfoResponse> orderInfoList;
    private ShippingAddressResponse shippingAddress;
    private SellerInfo sellerInfo;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt().toString();
        this.status = order.getStatus().getStatus();
        this.invoice = new ShippingInvoice(order);
        this.shippingFee = order.getShippingFee();
        this.orderInfoList = order.getOrderInfoList().stream()
                .map(OrderInfoResponse::new)
                .collect(Collectors.toList());
        this.shippingAddress = new ShippingAddressResponse(order);
        this.sellerInfo = new SellerInfo(order);
    }

    @Getter
    private static class ShippingInvoice {
        private final String shippingCompany;
        private final String invoiceNum;

        public ShippingInvoice(Order order) {
            this.shippingCompany = order.getShippingCompany();
            this.invoiceNum= order.getInvoiceNum();
        }
    }

    @Getter
    private static class OrderInfoResponse {
        private final Long productId;
        private final Long price;
        private final int quantity;
        private final String name;
        private final String imageUrl;
        private final boolean isReviewed;

        public OrderInfoResponse(OrderInfo orderInfo) {
            this.productId = orderInfo.getProduct().getId();
            this.quantity = orderInfo.getQuantity();
            this.price = orderInfo.getPrice();
            this.name = orderInfo.getProductName();
            this.imageUrl = orderInfo.getProductImageUrl();
            this.isReviewed = orderInfo.isReviewed();
        }
    }

    @Getter
    private static class ShippingAddressResponse {
        private final String address;
        private final String phoneNum;
        private final String name;

        public ShippingAddressResponse(Order order) {
            this.address = order.getShippingAddress();
            this.phoneNum = order.getCustomerPhoneNum();
            this.name = order.getCustomerName();
        }
    }

    @Getter
    private static class SellerInfo {
        private final Long id;
        private final String name;
        private final String phoneNum;
        private final String company;

        public SellerInfo(Order order) {
            this.id = order.getSellerInfo().getId();
            this.name = order.getSellerName();
            this.phoneNum = order.getSellerPhoneNum();
            this.company = order.getSellerCompany();
        }
    }
}
