package com.team04.buy_gurus.order.service;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.dto.OrderPageRequest;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderRequest.OrderInfoRequest;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.repository.OrderInfoRepository;
import com.team04.buy_gurus.order.repository.OrderRepository;
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

    @Autowired
    public OrderService(OrderInfoRepository orderInfoRepository, OrderRepository orderRepository) {
        this.orderInfoRepository = orderInfoRepository;
        this.orderRepository = orderRepository;
    }

    private List<OrderInfo> saveOrderInfos(List<OrderInfoRequest> orderRequest) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (OrderInfoRequest orderInfoRequest : orderRequest) {
            orderInfoList.add(new OrderInfo(orderInfoRequest));
        }
        return orderInfoRepository.saveAll(orderInfoList);
    }

    @Transactional
    public void save(OrderRequest orderRequest) {
        List<OrderInfo> orderInfoList = saveOrderInfos(orderRequest.getOrderInfoList());

        OrderRequest.ShippingInfo shippingInfo = orderRequest.getShippingInfo();
        Order order = Order.builder()
                .orderInfoList(orderInfoList)
                .status(Order.Status.PROCESSING)
                .shippingAddress(shippingInfo.getAddress())
                .customerName(shippingInfo.getName())
                .customerPhoneNum(shippingInfo.getPhoneNum())
                .shippingFee(orderRequest.getShippingFee())
                .build();

        orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<Order> getOrders(OrderPageRequest.Type typeReq, OrderPageRequest.Pageable pageReq) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), sort);
        Page<Order> paged;
        if (typeReq.getType().equals("c")) {
            paged = orderRepository.findAllByIsDeletedFalse(pageable);
        } else {
            paged = orderRepository.findAllByIsDeletedFalse(pageable);
        }
        return paged.getContent();
    }

    @Transactional
    public void updateInvoiceNumber(Long id, OrderUpdateRequest.InvoiceNumber request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        if (order != null) {
            order.setInvoiceNumber(request.getInvoiceNumber());
            order.setStatus(Order.Status.SHIPPED.getStatus());
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
