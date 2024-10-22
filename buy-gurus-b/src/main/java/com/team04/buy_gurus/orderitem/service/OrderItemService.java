package com.team04.buy_gurus.orderitem.service;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import com.team04.buy_gurus.orderitem.dto.OrderItemRequestDto;
import com.team04.buy_gurus.orderitem.dto.OrderItemResponseDto;
import com.team04.buy_gurus.orderitem.repository.OrderItemRepository;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.product.repository.ProductRepository;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 장바구니 추가
    @Transactional
    public void addOrderItem(OrderItemRequestDto request, Long productId, Long userId) {
      User user = userRepository.findById(userId)
                                            .orElseThrow(IllegalArgumentException::new);

      Product product = productRepository.findById(productId)
                                                    .orElseThrow(IllegalArgumentException::new);

      OrderItem existOrderItem = orderItemRepository.findByUserAndProduct(user, product);

      // 희원의 장바구니에 상품이 없다면
      if(existOrderItem == null){
          Long amount = product.getQuantity();
          Long price = product.getQuantity() * product.getPrice();

          OrderItem orderItem = new OrderItem(amount, price, user, product);

          orderItemRepository.save(orderItem);
      }
      // 희원의 장바구니에 상품이 있다면
      else{
            existOrderItem.setAmount(existOrderItem.getAmount() + product.getQuantity());
            existOrderItem.setPrice(existOrderItem.getAmount() + product.getQuantity()
                                    * product.getPrice());

            orderItemRepository.save(existOrderItem);
      }
    }

    // 장바구니 조회
    @Transactional
    public List<OrderItemResponseDto> readOrderItem(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        List<OrderItem> readOrderItem = orderItemRepository.findByUser(user);

        List<OrderItemResponseDto> response = new ArrayList<>();

        for(OrderItem orderItem : readOrderItem){
            response.add(new OrderItemResponseDto(orderItem));
        }

        return response;
    }

    // 장바구니 수정
    @Transactional
    public void patchOrderItem(Long id, Long amount) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        orderItem.setAmount(amount);
        
        orderItemRepository.save(orderItem);
    }

    // 장바구니 전체 삭제
    @Transactional
    public void deleteAllOrderItem(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        orderItemRepository.deleteAllByUser(user.getId());
    }

    // 장바구니 일부 삭제
    @Transactional
    public void deleteOrderItem(Long id) {
        OrderItem orderitem = orderItemRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        orderItemRepository.delete(orderitem);
    }
}
