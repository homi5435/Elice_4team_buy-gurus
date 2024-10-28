package com.team04.buy_gurus.order.controller;

import com.team04.buy_gurus.common.dto.CommonResponseDTO;
import com.team04.buy_gurus.common.enums.CommonSuccess;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.service.OrderService;
import com.team04.buy_gurus.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderService orderService;

    @PatchMapping("/{id}/invoice")
    public ResponseEntity<CommonResponseDTO<String>> updateInvoiceNumber(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderUpdateRequest.Invoice request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {
        orderService.updateInvoiceNumber(orderId, userDetails, request);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_UPDATE_SUCCESS));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable("id") Long orderId,
            @Valid @RequestBody OrderUpdateRequest.Status request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {
        orderService.updateStatus(orderId, userDetails, request);
        return ResponseEntity.ok(new CommonResponseDTO<>(CommonSuccess.ORDER_UPDATE_SUCCESS));
    }
}
