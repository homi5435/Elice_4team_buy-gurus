package com.team04.buy_gurus.order.service;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.dto.OrderPageRequest;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderRequest.OrderInfoRequest;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.repository.OrderInfoRepository;
import com.team04.buy_gurus.order.repository.OrderRepository;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.product.repository.ProductRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(force = true)
@Service
public class OrderService {
    private OrderInfoRepository orderInfoRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    @Autowired
    public OrderService(
            OrderInfoRepository orderInfoRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    )  {
        this.orderInfoRepository = orderInfoRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    private List<OrderInfo> createOrderInfos(List<OrderInfoRequest> orderRequest, Order order) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (OrderInfoRequest orderInfoRequest : orderRequest) {
            Product product = productRepository.findById(orderInfoRequest.getProductId()).orElse(null);
            orderInfoList.add(new OrderInfo(orderInfoRequest, product, order));
        }
        return orderInfoList;
    }

    @Transactional
    public void save(OrderRequest orderRequest) {

        OrderRequest.ShippingInfo shippingInfo = orderRequest.getShippingInfo();
        Order order = Order.builder()
                .status(Order.Status.PROCESSING)
                .shippingAddress(shippingInfo.getAddress())
                .customerName(shippingInfo.getName())
                .customerPhoneNum(shippingInfo.getPhoneNum())
                .shippingFee(orderRequest.getShippingFee())
                .build();

        List<OrderInfo> orderInfoList = createOrderInfos(orderRequest.getOrderInfoList(), order);
        order.setOrderInfoList(orderInfoList);

        orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Page<Order> getOrders(OrderPageRequest.Type typeReq, OrderPageRequest.Pageable pageReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), sort);
        Page<Order> paged;
        if (typeReq.getType().equals("c")) {
            paged = orderRepository.findAllByIsDeletedFalse(pageable);
        } else {
            paged = orderRepository.findAllByIsDeletedFalse(pageable);
        }
//        List<OrderInfo> info = orderInfoRepository.findOrderInfoByUserIdAndProductId(1L, 1L);
//        info.forEach(System.out::println);

        List<OrderInfo> infos = orderInfoRepository.findOrderInfoByProductId(1L);
        infos.forEach(System.out::println);
        return paged;
    }

    @Transactional
    public void updateInvoiceNumber(Long id, OrderUpdateRequest.Invoice request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null) {
            order.setInvoice(request);
            order.setStatus(Order.Status.SHIPPING.getStatus());
        }
    }

    @Transactional
    public void updateStatus(Long id, OrderUpdateRequest.Status request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null) {
            order.setStatus(request.getStatus());
        }
    }

    @Transactional
    public void updateAddress(Long id, OrderUpdateRequest.Address request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null) {
            order.setAddress(request);
        }
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null) {
            orderRepository.delete(order);
        }
    }
}
