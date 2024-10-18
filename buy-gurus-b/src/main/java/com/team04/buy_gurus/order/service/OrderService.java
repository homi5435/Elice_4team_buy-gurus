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
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(force = true)
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private SellerInfoRepository sellerInfoRepository;
    private UserRepository userRepository;

    private StringBuilder sb;

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
        List<Long> notExistsProductList = new ArrayList<>();
        for (OrderInfoRequest orderInfoRequest : orderRequest) {
            try {
                Product product = productRepository.findById(orderInfoRequest.getProductId())
                        .orElseThrow(RuntimeException::new);
                orderInfoList.add(new OrderInfo(orderInfoRequest, product, order));
            } catch (Exception e) {
                notExistsProductList.add(orderInfoRequest.getProductId());
            }
        }

        if (!notExistsProductList.isEmpty()) {
            sb.setLength(0);
            sb.append("제품 ID가 등록되지 않았습니다. 다시 주문해주세요.: [");
            for (int i = 0; i < notExistsProductList.size(); i++) {
                sb.append(notExistsProductList.get(i));
                if (i < notExistsProductList.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            throw new ProductNotFoundException(sb.toString());
        }
        return orderInfoList;
    }

    private String getEmailFromUserdetails(UserDetails userDetails) {
        return userDetails.getUsername();
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
    public void save(OrderRequest orderRequests, UserDetails userDetails) throws Exception {
        for (BOrderRequest orderRequest: orderRequests.getOrderRequests()) {
            BOrderRequest.ShippingInfo shippingInfo = orderRequest.getShippingInfo();

            SellerInfo sellerInfo = sellerInfoRepository.findById(orderRequest.getSellerId()).orElseThrow(() -> new NotExistsSellerException(CommonError.SELLER_NOT_FOUND));

            String email = getEmailFromUserdetails(userDetails);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotOrderedException(CommonError.USER_NOT_ORDERED));

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
            int shippingFee = allPrice < 50000 ? 2500 : 0;
            order.setShippingFee(shippingFee);
            order.setOrderInfoList(orderInfoList);

            orderRepository.save(order);
        }
    }

    public Order getOrder(Long orderId, UserDetails userDetails) {
        String email = getEmailFromUserdetails(userDetails);
        User user = userRepository.findByEmail(email).get();
        Optional<Order> orderOptional = orderRepository.findByOrderIdAndUserId(orderId, user.getId());
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new NotOrderedException(CommonError.USER_NOT_ORDERED);
        }
    }

    public Page<Order> getOrders(
            OrderPageRequest.Type typeReq,
            OrderPageRequest.Pageable pageReq,
            UserDetails userDetails
    ) {
        String email = getEmailFromUserdetails(userDetails);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), sort);

        if (typeReq.getType().equals("c")) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotOrderedException(CommonError.USER_NOT_ORDERED));
            return orderRepository.findAllByUser(user.getId(), pageable);
        } else {
            SellerInfo sellerInfo = sellerInfoRepository.findByUseremail(email).orElseThrow(() -> new NotSellerException(CommonError.SELLER_NOT_SOLD));
            User seller = sellerInfo.getUser();
            return orderRepository.findAllBySellerInfo(seller.getId(), pageable);
        }
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void isValidSeller(Order order, UserDetails userDetails) throws Exception {
        String email = getEmailFromUserdetails(userDetails);
        sellerInfoRepository.findByUseremail(email).orElseThrow(() -> new NotSellerException(CommonError.NOT_SELLER));
        if (!order.getSellerInfo().getUser().getEmail().equals(email)) throw new NotSoldException(CommonError.SELLER_NOT_ASSIGN_EDIT);
    }

    private void isValidUser(Order order, UserDetails userDetails) throws Exception {
        String email = getEmailFromUserdetails(userDetails);
        if (!order.getUser().getEmail().equals(email)) throw new NotOrderedException(CommonError.USER_NOT_ASSIGN_EDIT);
    }

    @Transactional
    public void updateInvoiceNumber(Long id, UserDetails userDetails, OrderUpdateRequest.Invoice request) throws Exception {
        Order order = findOrderById(id);

        isValidSeller(order, userDetails);

        order.setInvoice(request);
        order.setStatus(Order.Status.SHIPPING.getStatus());
    }

    @Transactional
    public void updateStatus(Long id, UserDetails userDetails, OrderUpdateRequest.Status request) throws Exception {
        Order order = findOrderById(id);

        isValidSeller(order, userDetails);

        order.setStatus(request.getStatus());
    }

    @Transactional
    public void updateAddress(Long id, UserDetails userDetails, OrderUpdateRequest.Address request) throws Exception {
        Order order = findOrderById(id);

        isValidUser(order, userDetails);

        order.setAddress(request);
    }

    @Transactional
    public void delete(Long id, UserDetails userDetails) throws Exception {
        Order order = findOrderById(id);

        isValidUser(order, userDetails);

        orderRepository.delete(order);
    }
}
