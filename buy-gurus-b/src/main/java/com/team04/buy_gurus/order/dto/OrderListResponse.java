package com.team04.buy_gurus.order.dto;

import com.team04.buy_gurus.order.domain.Order;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderListResponse {
    private int pages;
    private List<OrderResponse> orderList;

    public OrderListResponse(Page<Order> pages) {
        this.pages = pages.getTotalPages();
        this.orderList = pages.getContent().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }
}
