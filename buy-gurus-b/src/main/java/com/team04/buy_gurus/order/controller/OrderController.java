package com.team04.buy_gurus.order.controller;

import com.team04.buy_gurus.common.dto.CommonResponseDTO;
import com.team04.buy_gurus.common.enums.CommonSuccess;
import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.dto.*;
import com.team04.buy_gurus.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 완료 시 저장
    @PostMapping
    public ResponseEntity<CommonResponseDTO<String>> saveOrder(@Valid @RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        orderService.save(orderRequest, userDetails);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_SAVE_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponseDTO<OrderListResponse>> getAllOrder(
            @Valid OrderPageRequest.Type type,
            OrderPageRequest.Pageable page,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println(userDetails);
        Page<Order> paged = orderService.getOrders(type, page, userDetails);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_FOUND, new OrderListResponse(paged)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponseDTO<OrderResponse>> getOrder(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Order order = orderService.getOrder(id, userDetails);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_FOUND, new OrderResponse(order)));
    }

    @PatchMapping("/{id}/invoice")
    public ResponseEntity<CommonResponseDTO<String>> updateInvoiceNumber(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Invoice request,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        orderService.updateInvoiceNumber(id, userDetails, request);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_UPDATE_SUCCESS));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Status request,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        orderService.updateStatus(id, userDetails, request);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_UPDATE_SUCCESS));
    }

    @PatchMapping("/{id}/address")
    public ResponseEntity<?> updateAddress(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderUpdateRequest.Address request,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        orderService.updateAddress(id, userDetails, request);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_UPDATE_SUCCESS));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        orderService.delete(id, userDetails);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_DELETE_SUCCESS));
    }
}
