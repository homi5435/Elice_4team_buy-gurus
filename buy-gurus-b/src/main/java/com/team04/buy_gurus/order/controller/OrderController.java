package com.team04.buy_gurus.order.controller;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.dto.*;
import com.team04.buy_gurus.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
class SaveOrderResponse {
    private String code;
    private String message;
    private Object data;
    
    public SaveOrderResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public SaveOrderResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 완료 시 저장
    @PostMapping
    public ResponseEntity<SaveOrderResponse> saveOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            orderService.save(orderRequest, userDetails);
            return ResponseEntity.ok(new SaveOrderResponse("success", "성공"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SaveOrderResponse("BG30220", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<SaveOrderResponse> getAllOrder(
            @Valid OrderPageRequest.Type type,
            OrderPageRequest.Pageable page,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Page<Order> paged = orderService.getOrders(type, page, userDetails);
            return ResponseEntity.ok(new SaveOrderResponse("success", "조회 성공", new OrderListResponse(paged)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Order order = orderService.getOrder(id, userDetails);
            return ResponseEntity.ok(new OrderResponse(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/invoice")
    public ResponseEntity<?> updateInvoiceNumber(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Invoice request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            orderService.updateInvoiceNumber(id, userDetails, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Status request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            orderService.updateStatus(id, userDetails, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<?> updateAddress(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Address request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            orderService.updateAddress(id, userDetails, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            orderService.delete(id, userDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
