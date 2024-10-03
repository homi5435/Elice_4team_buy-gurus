package com.team04.buy_gurus.orderitem.controller;

import com.team04.buy_gurus.orderitem.dto.OrderItemRequestDto;
import com.team04.buy_gurus.orderitem.dto.OrderItemViewDto;
import com.team04.buy_gurus.orderitem.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;


    // 장바구니 추가
    @PostMapping("api/orderitem/{product_id}")
    public ResponseEntity<String> addOrderItem(@Valid @RequestBody OrderItemRequestDto orderItemRequestDto, @PathVariable Long product_id){
        orderItemService.addOrderItem(orderItemRequestDto, product_id);

        return ResponseEntity.ok("장바구니 추가 성공");
    }

    // 장바구니 조회
    @GetMapping("api/orderitem")
    public ResponseEntity<String> readOrderItem(@PathVariable Long user_id, Model model){
        List<OrderItemViewDto> orderItem = orderItemService.readOrderItem(user_id);

        int totalAmount = 0;
        int totalPrice = 0;

        for(OrderItemViewDto item: orderItem){
            totalAmount += item.getAmount();
            totalPrice += item.getAmount() * item.getPrice();
        }

        model.addAttribute("orderItem", orderItem);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalPrice", totalPrice);

        return ResponseEntity.ok("장바구니 조회 성공");
    }

    // 장바구니 수정
    @PatchMapping("api/orderitem/{id}")
    public ResponseEntity<String> patchOrderItem(@PathVariable Long id, @RequestBody int amount){
        orderItemService.patchOrderItem(id, amount);
        return ResponseEntity.ok("장바구니 수정 성공");
    }

    // 장바구니 전체 삭제
    @DeleteMapping("api/orderitem")
    public ResponseEntity<String> deleteAllOrderItem(@RequestBody Long user_id){
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
