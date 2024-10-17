package com.team04.buy_gurus.orderitem.controller;

import com.team04.buy_gurus.orderitem.dto.OrderItemRequestDto;
import com.team04.buy_gurus.orderitem.dto.OrderItemResponseDto;
import com.team04.buy_gurus.orderitem.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    // 장바구니 추가
    @PostMapping("api/orderitem/{productId}")
    public ResponseEntity<String> addOrderItem(@Valid @RequestBody OrderItemRequestDto request, @PathVariable Long productId,
                                                @AuthenticationPrincipal UserDetails userDetails){
        orderItemService.addOrderItem(request, productId, userDetails.getUsername());

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    // 장바구니 조회
    @GetMapping("api/orderitem")
    public ResponseEntity<List<OrderItemResponseDto>> readOrderItem(@AuthenticationPrincipal UserDetails userDetails){
        List<OrderItemResponseDto> response = orderItemService.readOrderItem(userDetails.getUsername());
        
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정
    @PatchMapping("api/orderitem/{orderItemId}")
    public ResponseEntity<String> patchOrderItem(@PathVariable(value = "orderItemId") Long id, @RequestBody OrderItemRequestDto request){
        orderItemService.patchOrderItem(id, request.getAmount());
        return ResponseEntity.ok("장바구니 수정 성공");
    }

    // 장바구니 전체 삭제
    @DeleteMapping("api/orderitem")
    public ResponseEntity<String> deleteAllOrderItem(@AuthenticationPrincipal UserDetails userDetails){
        orderItemService.deleteAllOrderItem(userDetails.getUsername());
        return ResponseEntity.ok("장바구니 전체 삭제 성공");
    }

    // 장바구니 일부 삭제
    @DeleteMapping("api/orderitem/{orderItemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable(value = "orderItemId") Long id){
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("장바구니 일부 삭제 성공");
    }

}
