package com.team04.buy_gurus.orderitem.controller;

import com.team04.buy_gurus.exception.ex_orderItem.exception.UnauthorizedException;
import com.team04.buy_gurus.orderitem.dto.OrderItemRequestDto;
import com.team04.buy_gurus.orderitem.dto.OrderItemResponseDto;
import com.team04.buy_gurus.orderitem.service.OrderItemService;
import com.team04.buy_gurus.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderitem")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    // 장바구니 추가
    @PostMapping("/{productId}")
    public ResponseEntity<String> addOrderItem(@Valid @RequestBody OrderItemRequestDto request, @PathVariable Long productId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            throw new UnauthorizedException("로그인을 해주세요.");
        }

        orderItemService.addOrderItem(request, productId, userDetails.getUserId());
        return ResponseEntity.ok("장바구니 추가 성공");
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<List<OrderItemResponseDto>> readOrderItem(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            throw new UnauthorizedException("로그인을 해주세요.");
        }

        List<OrderItemResponseDto> response = orderItemService.readOrderItem(userDetails.getUserId());
        
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정
    @PatchMapping("/{orderItemId}")
    public ResponseEntity<String> patchOrderItem(@PathVariable(value = "orderItemId") Long id, @RequestBody OrderItemRequestDto request){
        orderItemService.patchOrderItem(id, request.getAmount());
        return ResponseEntity.ok("장바구니 수정 성공");
    }

    // 장바구니 전체 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteAllOrderItem(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            throw new UnauthorizedException("로그인을 해주세요.");
        }

        orderItemService.deleteAllOrderItem(userDetails.getUserId());
        return ResponseEntity.ok("장바구니 전체 삭제 성공");
    }

    // 장바구니 일부 삭제
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable(value = "orderItemId") Long id){
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("장바구니 일부 삭제 성공");
    }
}
