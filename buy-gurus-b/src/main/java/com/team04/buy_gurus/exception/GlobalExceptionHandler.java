package com.team04.buy_gurus.exception;

import com.team04.buy_gurus.exception.ex_user.DuplicateEmailException;
import com.team04.buy_gurus.exception.ex_user.DuplicateNicknameException;
import com.team04.buy_gurus.exception.ex_user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DUPLICATE_NICKNAME_ERROR = "DUPLICATE_NICKNAME";
    private static final String DUPLICATE_EMAIL_ERROR = "DUPLICATE_EMAIL";
    private static final String USER_NOT_FOUND_ERROR = "USER_NOT_FOUND";

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNickname(DuplicateNicknameException e) {
        ErrorResponse errorResponse = new ErrorResponse(DUPLICATE_NICKNAME_ERROR, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        ErrorResponse errorResponse = new ErrorResponse(DUPLICATE_EMAIL_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(USER_NOT_FOUND_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}


