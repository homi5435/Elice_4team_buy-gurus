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
    @PostMapping("api/orderitem/{product_id}/{user_id}")
    public ResponseEntity<String> addOrderItem(@Valid @RequestBody OrderItemRequestDto request, @PathVariable Long product_id,
                                               @PathVariable Long user_id){
        orderItemService.addOrderItem(request, product_id, user_id);

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    // 장바구니 조회
    @GetMapping("api/orderitem/{user_id}")
    public ResponseEntity<List<OrderItemResponseDto>> readOrderItem(@PathVariable Long user_id){
        List<OrderItemResponseDto> response = orderItemService.readOrderItem(user_id);

        /* 프론트에서 처리할 예정
        int totalAmount = 0;
        int totalPrice = 0;

        for(OrderItemResponseDto item: response){
            totalAmount += item.getAmount();
            totalPrice += (int) (item.getAmount() * item.getPrice());
        }
        */
        
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정
    @PatchMapping("api/orderitem/{id}")
    public ResponseEntity<String> patchOrderItem(@PathVariable Long id, @RequestBody OrderItemRequestDto request){
        orderItemService.patchOrderItem(id, request.getAmount());
        return ResponseEntity.ok("장바구니 수정 성공");
    }

    // 장바구니 전체 삭제
    @DeleteMapping("api/orderitem/{user_id}")
    public ResponseEntity<String> deleteAllOrderItem(@PathVariable Long user_id){
        orderItemService.deleteAllOrderItem(user_id);
        return ResponseEntity.ok("장바구니 전체 삭제 성공");
    }

    // 장바구니 일부 삭제
    @DeleteMapping("api/orderitem/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long id){
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.ok("장바구니 일부 삭제 성공");
    }

}