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
    private String invoiceNum;
    private int shippingFee;
    private List<OrderInfoResponse> orderInfoList;
    private ShippingAddressResponse shippingAddress;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.createdAt = order.getCreatedAt().toString();
        this.status = order.getStatus().getStatus();
        this.invoiceNum = order.getInvoiceNum() == null ? "" : order.getInvoiceNum();
        this.shippingFee = order.getShippingFee();
        this.orderInfoList = order.getOrderInfoList().stream()
                .map(OrderInfoResponse::new)
                .collect(Collectors.toList());
        this.shippingAddress = new ShippingAddressResponse(order);
    }

    @Getter
    private static class OrderInfoResponse {
        private final int productId;
        private final int price;
        private final int quantity;
        private final String imageUrl;

        public OrderInfoResponse(OrderInfo orderInfo) {
            this.productId = -1;
            this.quantity = orderInfo.getQuantity();
            this.price = orderInfo.getPrice();
            this.imageUrl = orderInfo.getProductImageUrl();
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
}
