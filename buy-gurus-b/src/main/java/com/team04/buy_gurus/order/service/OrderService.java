package com.team04.buy_gurus.order.service;

import com.team04.buy_gurus.common.enums.CommonError;
import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderPageRequest;
import com.team04.buy_gurus.order.dto.BOrderRequest;
import com.team04.buy_gurus.order.dto.BOrderRequest.OrderInfoRequest;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.exception.ex_order.exception.NotExistsSellerException;
import com.team04.buy_gurus.exception.ex_order.exception.NotOrderedException;
import com.team04.buy_gurus.exception.ex_order.exception.NotSellerException;
import com.team04.buy_gurus.exception.ex_order.exception.NotSoldException;
import com.team04.buy_gurus.order.repository.OrderRepository;
import com.team04.buy_gurus.product.aop.ProductNotFoundException;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.product.repository.ProductRepository;
import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import com.team04.buy_gurus.sellerinfo.repository.SellerInfoRepository;
import com.team04.buy_gurus.user.CustomUserDetails;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private SellerInfoRepository sellerInfoRepository;
    private UserRepository userRepository;

    private StringBuilder sb;

    private final int SHIPPING_FEE = 2500;
    private final int SHIPPING_FEE_CRITERIA = 50000;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            SellerInfoRepository sellerInfoRepository,
            UserRepository userRepository
    )  {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.sellerInfoRepository = sellerInfoRepository;
        this.userRepository = userRepository;
        this.sb = new StringBuilder();
    }

    private List<OrderInfo> createOrderInfos(List<OrderInfoRequest> orderRequest, Order order) {
        List<OrderInfo> orderInfoList = new ArrayList<>();

        Set<Long> productIdsFromReq = orderRequest.stream()
                .map(OrderInfoRequest::getProductId)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findAllById(productIdsFromReq);

        Set<Long> productIdsFromRepo = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        checkExistsMissingProduct(productIdsFromReq, productIdsFromRepo);

        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

        for (OrderInfoRequest orderInfoRequest : orderRequest) {
            Long productId = orderInfoRequest.getProductId();
            Product product = productMap.get(productId);
            orderInfoList.add(new OrderInfo(orderInfoRequest, product, order));
        }
        return orderInfoList;
    }

    private void checkExistsMissingProduct(Set<Long> fromRequest, Set<Long> fromRepository) {
        Set<Long> missingProductIds = new HashSet<>(fromRequest);
        missingProductIds.removeAll(fromRepository);

        if (!missingProductIds.isEmpty()) {
            throw new ProductNotFoundException(createErrorMessage(missingProductIds));
        }
    }

    private String createErrorMessage(List<Long> productIds) {
        sb.setLength(0);
        sb.append("제품 ID가 등록되지 않았습니다. 다시 주문해주세요.: [");
        for (int i = 0; i < productIds.size(); i++) {
            sb.append(productIds.get(i));
            if (i < productIds.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String createErrorMessage(Set<Long> productIds) {
        return createErrorMessage(productIds.stream().toList());
    }

    private int sumAllPrice(List<OrderInfoRequest> orderInfoList) {
        return orderInfoList.stream()
                            .mapToInt((orderInfo) -> {
                                Product product = productRepository.findById(orderInfo.getProductId()).orElse(null);
                                if (product != null) {
                                    return Math.toIntExact(product.getPrice() * orderInfo.getQuantity());
                                } else {
                                    return 0;
                                }
                            })
                            .reduce(0, Integer::sum);
    }

    @Transactional
    public void save(OrderRequest orderRequests, CustomUserDetails userDetails) throws Exception {
        for (BOrderRequest orderRequest: orderRequests.getOrderRequests()) {
            BOrderRequest.ShippingInfo shippingInfo = orderRequest.getShippingInfo();

            SellerInfo sellerInfo = sellerInfoRepository.findById(orderRequest.getSellerId()).orElseThrow(() -> new NotExistsSellerException(CommonError.SELLER_NOT_FOUND));

            User user = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new NotOrderedException(CommonError.USER_NOT_ORDERED));

            Order order = Order.builder()
                    .status(Order.Status.PROCESSING)
                    .shippingAddress(shippingInfo.getAddress())
                    .customerName(shippingInfo.getName())
                    .customerPhoneNum(shippingInfo.getPhoneNum())
                    .sellerInfo(sellerInfo)
                    .user(user)
                    .build();
            order.setSeller(sellerInfo);

            List<OrderInfo> orderInfoList = createOrderInfos(orderRequest.getOrderInfoList(), order);
            int allPrice = sumAllPrice(orderRequest.getOrderInfoList());
            int shippingFee = allPrice < SHIPPING_FEE_CRITERIA ? SHIPPING_FEE : 0;
            order.setShippingFee(shippingFee);
            order.setOrderInfoList(orderInfoList);

            orderRepository.save(order);
        }
    }

    public Order getOrder(Long orderId, CustomUserDetails userDetails) {
        Optional<Order> orderOptional = orderRepository.findByOrderIdAndUserId(orderId, userDetails.getUserId());
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new NotOrderedException(CommonError.USER_NOT_ORDERED);
        }
    }

    public Page<Order> getOrders(
            OrderPageRequest.Type typeReq,
            OrderPageRequest.Pageable pageReq,
            CustomUserDetails userDetails
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), sort);

        if (typeReq.getType().equals("c")) {
            User user = userRepository.findById(userDetails.getUserId()).orElseThrow(() -> new NotOrderedException(CommonError.USER_NOT_ORDERED));
            return orderRepository.findAllByUser(user.getId(), pageable);
        } else {
            SellerInfo sellerInfo = sellerInfoRepository.findByUserId(userDetails.getUserId()).orElseThrow(() -> new NotSellerException(CommonError.SELLER_NOT_SOLD));
            return orderRepository.findAllBySellerInfo(sellerInfo.getId(), pageable);
        }
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void isValidSeller(Order order, CustomUserDetails userDetails) throws Exception {
        sellerInfoRepository.findByUserId(userDetails.getUserId()).orElseThrow(() -> new NotSellerException(CommonError.NOT_SELLER));
        if (!order.getSellerInfo().getUser().getId().equals(userDetails.getUserId())) throw new NotSoldException(CommonError.SELLER_NOT_ASSIGN_EDIT);
    }

    private void isValidUser(Order order, CustomUserDetails userDetails) throws Exception {
        if (!order.getUser().getId().equals(userDetails.getUserId())) throw new NotOrderedException(CommonError.USER_NOT_ASSIGN_EDIT);
    }

    @Transactional
    public void updateInvoiceNumber(Long id, CustomUserDetails userDetails, OrderUpdateRequest.Invoice request) throws Exception {
        Order order = findOrderById(id);

        isValidSeller(order, userDetails);

        order.setInvoice(request);
        order.setStatus(Order.Status.SHIPPING.getStatus());
    }

    @Transactional
    public void updateStatus(Long id, CustomUserDetails userDetails, OrderUpdateRequest.Status request) throws Exception {
        Order order = findOrderById(id);

        isValidSeller(order, userDetails);

        order.setStatus(request.getStatus());
    }

    @Transactional
    public void updateAddress(Long id, CustomUserDetails userDetails, OrderUpdateRequest.Address request) throws Exception {
        Order order = findOrderById(id);

        isValidUser(order, userDetails);

        order.setAddress(request);
    }

    @Transactional
    public void delete(Long id, CustomUserDetails userDetails) throws Exception {
        Order order = findOrderById(id);

        isValidUser(order, userDetails);

        orderRepository.delete(order);
    }
}
