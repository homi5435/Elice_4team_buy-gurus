package com.team04.buy_gurus.order.service;

import com.team04.buy_gurus.address.domain.AddressInfo;
import com.team04.buy_gurus.address.service.AddressInfoService;
import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderRequest.OrderInfoRequest;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.repository.OrderInfoRepository;
import com.team04.buy_gurus.order.repository.OrderRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(force = true)
@Service
public class OrderService {
    private OrderInfoRepository orderInfoRepository;
    private OrderRepository orderRepository;

    private AddressInfoService addressInfoService;

    @Autowired
    public OrderService(OrderInfoRepository orderInfoRepository,
                        OrderRepository orderRepository,
                        AddressInfoService addressInfoService) {
        this.orderInfoRepository = orderInfoRepository;
        this.orderRepository = orderRepository;
        this.addressInfoService = addressInfoService;
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

    public List<Order> getOrders() {
        return orderRepository.findAll();
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
