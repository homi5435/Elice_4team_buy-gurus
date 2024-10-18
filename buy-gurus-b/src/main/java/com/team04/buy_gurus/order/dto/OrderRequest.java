package com.team04.buy_gurus.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    private List<BOrderRequest> orderRequests;
}
