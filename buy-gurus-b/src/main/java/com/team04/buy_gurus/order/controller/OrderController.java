package com.team04.buy_gurus.order.controller;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderResponse;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 완료 시 저장
    @PostMapping
    public ResponseEntity<?> saveOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.save(orderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrder() {
        List<Order> orderList = orderService.getOrders();
        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : orderList) {
            orderResponseList.add(new OrderResponse(order));
        }
        return ResponseEntity.ok(orderResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(new OrderResponse(order));
    }

    @PatchMapping("/{id}/invoice")
    public ResponseEntity<?> updateInvoiceNumber(@PathVariable("id") Long id, @Valid @RequestBody OrderUpdateRequest.InvoiceNumber request) {
        orderService.updateInvoiceNumber(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id, @Valid @RequestBody OrderUpdateRequest.Status request) {
        orderService.updateStatus(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @Valid @RequestBody OrderUpdateRequest.Address request) {
        orderService.updateAddress(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
