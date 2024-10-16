package com.team04.buy_gurus.order.service;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.dto.OrderPageRequest;
import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.order.dto.OrderRequest.OrderInfoRequest;
import com.team04.buy_gurus.order.dto.OrderUpdateRequest;
import com.team04.buy_gurus.order.repository.OrderRepository;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    }

    private List<OrderInfo> createOrderInfos(List<OrderInfoRequest> orderRequest, Order order) throws Exception {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (OrderInfoRequest orderInfoRequest : orderRequest) {
            Product product = productRepository.findById(orderInfoRequest.getProductId())
                    .orElseThrow(() -> new Exception("product: " + orderInfoRequest.getProductId() + " is not exists"));
            orderInfoList.add(new OrderInfo(orderInfoRequest, product, order));
        }
        return orderInfoList;
    }

    private String getEmailFromUserdetails(UserDetails userDetails) {
        return userDetails.getUsername();
    }

    @Transactional
    public void save(OrderRequest orderRequest, UserDetails userDetails) throws Exception {
        OrderRequest.ShippingInfo shippingInfo = orderRequest.getShippingInfo();

        SellerInfo sellerInfo = sellerInfoRepository.findById(orderRequest.getSellerId()).orElseThrow(() -> new Exception("seller: " + orderRequest.getSellerId() + " is not exists"));

        String email = getEmailFromUserdetails(userDetails);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("User is not exists"));

        Order order = Order.builder()
                .status(Order.Status.PROCESSING)
                .shippingAddress(shippingInfo.getAddress())
                .customerName(shippingInfo.getName())
                .customerPhoneNum(shippingInfo.getPhoneNum())
                .shippingFee(orderRequest.getShippingFee())
                .sellerInfo(sellerInfo)
                .user(user)
                .build();
        order.setSeller(sellerInfo);

        List<OrderInfo> orderInfoList = createOrderInfos(orderRequest.getOrderInfoList(), order);
        order.setOrderInfoList(orderInfoList);

        orderRepository.save(order);
    }

    public Order getOrder(Long orderId, UserDetails userDetails) throws Exception {
        String email = getEmailFromUserdetails(userDetails);
        User user = userRepository.findByEmail(email).get();
        Optional<Order> orderOptional = orderRepository.findByOrderIdAndUserId(orderId, user.getId());
        if (orderOptional.isPresent()) {
            return orderOptional.get();
        } else {
            throw new Exception("해당 유저의 주문 내역이 아닙니다.");
        }
    }

    public Page<Order> getOrders(
            OrderPageRequest.Type typeReq,
            OrderPageRequest.Pageable pageReq,
            UserDetails userDetails
    ) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageReq.getPage() - 1, pageReq.getSize(), sort);
        Page<Order> paged;
        String email = getEmailFromUserdetails(userDetails);
        if (typeReq.getType().equals("c")) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("구매내역이 존재하지 않습니다."));
            paged = orderRepository.findAllByUser(user.getId(), pageable);
        } else {
            SellerInfo sellerInfo = sellerInfoRepository.findByUseremail(email).orElseThrow(() -> new Exception("판매자가 아닙니다."));
            User seller = sellerInfo.getUser();
            paged = orderRepository.findAllBySellerInfo(seller.getId(), pageable);
        }
        return paged;
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void isValidSeller(Order order, UserDetails userDetails) throws Exception {
        String email = getEmailFromUserdetails(userDetails);
        sellerInfoRepository.findByUseremail(email).orElseThrow(() -> new Exception("판매자가 아닙니다."));
        if (!order.getSellerInfo().getUser().getEmail().equals(email)) throw new Exception("해당 판매자는 편집 권한이 없습니다.");
    }

    private void isValidUser(Order order, UserDetails userDetails) throws Exception {
        String email = getEmailFromUserdetails(userDetails);
        if (!order.getUser().getEmail().equals(email)) throw new Exception("편집 권한이 존재하지 않습니다.");
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
