package com.team04.buy_gurus.exception.ex_user.ex;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
