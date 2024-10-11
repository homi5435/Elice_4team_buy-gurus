package com.team04.buy_gurus.orderitem.controller;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import com.team04.buy_gurus.orderitem.dto.OrderItemRequestDto;
import com.team04.buy_gurus.orderitem.dto.OrderItemResponseDto;
import com.team04.buy_gurus.orderitem.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
React에서 Axios.get()은 RequestBody로 받을수 없어 토큰을 받기 전 PathVariable 활용
*/
@RestController
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    // 장바구니 추가
    @PostMapping("api/orderitem/{productId}/{userId}")
    public ResponseEntity<String> addOrderItem(@Valid @RequestBody OrderItemRequestDto request, @PathVariable Long product_id,
                                               @PathVariable Long userId){
        orderItemService.addOrderItem(request, product_id, userId);

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    // 장바구니 조회
    @GetMapping("api/orderitem/{userId}")
    public ResponseEntity<List<OrderItemResponseDto>> readOrderItem(@PathVariable Long userId){
        List<OrderItemResponseDto> response = orderItemService.readOrderItem(userId);
        
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정
    @PatchMapping("api/orderitem/{orderItemId}")
    public ResponseEntity<String> patchOrderItem(@PathVariable(value = "orderItemId") Long id, @RequestBody OrderItemRequestDto request){
        orderItemService.patchOrderItem(id, request.getAmount());
        return ResponseEntity.ok("장바구니 수정 성공");
    }

    // 장바구니 전체 삭제
    @DeleteMapping("api/orderitem/{userId}")
    public ResponseEntity<String> deleteAllOrderItem(@PathVariable Long userId){
        orderItemService.deleteAllOrderItem(userId);
        return ResponseEntity.ok("장바구니 전체 삭제 성공");
    }

    // 장바구니 일부 삭제
    @DeleteMapping("api/orderitem/{orderItemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable(value = "orderItemId") Long id){
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("장바구니 일부 삭제 성공");
    }

}
