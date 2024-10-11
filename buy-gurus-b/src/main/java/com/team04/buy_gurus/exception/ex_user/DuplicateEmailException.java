package com.team04.buy_gurus.exception.ex_user;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
