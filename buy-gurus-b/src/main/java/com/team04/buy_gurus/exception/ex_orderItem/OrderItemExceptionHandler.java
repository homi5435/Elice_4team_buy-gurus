package com.team04.buy_gurus.exception.ex_orderItem;

import com.team04.buy_gurus.exception.ex_orderItem.exception.InsufficientQuantityException;
import com.team04.buy_gurus.exception.ex_orderItem.exception.OrderItemNotFoundException;
import com.team04.buy_gurus.exception.ex_orderItem.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderItemExceptionHandler {
    
    // 401 에러
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // 404 에러
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 400 에러, 재고 부족 예외 처리
    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
