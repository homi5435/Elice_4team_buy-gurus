package com.team04.buy_gurus.exception.ex_order;

import com.team04.buy_gurus.common.dto.CommonResponseDTO;
import com.team04.buy_gurus.common.enums.CommonError;
import com.team04.buy_gurus.exception.ex_order.exception.NotExistsSellerException;
import com.team04.buy_gurus.exception.ex_order.exception.NotOrderedException;
import com.team04.buy_gurus.exception.ex_order.exception.NotSellerException;
import com.team04.buy_gurus.product.aop.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler {
    @ExceptionHandler(NotExistsSellerException.class)
    public ResponseEntity<CommonResponseDTO<String>> handleNotExistsSellerException(NotExistsSellerException e) {
        return ResponseEntity.badRequest().body(new CommonResponseDTO<>(CommonError.SELLER_NOT_FOUND));
    }

    @ExceptionHandler(NotOrderedException.class)
    public ResponseEntity<CommonResponseDTO<String>> handleNotOrderedException(NotOrderedException e) {
        return ResponseEntity.badRequest().body(new CommonResponseDTO<>(CommonError.USER_NOT_ORDERED));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CommonResponseDTO<String>> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(new CommonResponseDTO<>("BG29998", e.getMessage()));
    }

    @ExceptionHandler(NotSellerException.class)
    public ResponseEntity<CommonResponseDTO<String>> handleUserisNotSellerException(NotSellerException e) {
        return ResponseEntity.badRequest().body(new CommonResponseDTO<>(CommonError.NOT_SELLER));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDTO<String>> handleDefaultException(Exception e) {
        return ResponseEntity.badRequest().body(new CommonResponseDTO<>("BG00000", e.getMessage()));
    }
}
