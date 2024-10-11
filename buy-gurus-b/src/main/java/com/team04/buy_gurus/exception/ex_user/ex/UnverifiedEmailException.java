package com.team04.buy_gurus.exception.ex_user.ex;

public class UnverifiedEmailException extends RuntimeException {

    public UnverifiedEmailException(){
        super("인증이 완료되지 않은 이메일입니다.");
    }
}
