package com.team04.buy_gurus.exception.ex_user.ex;

public class DuplicateNicknameException extends RuntimeException {

    public DuplicateNicknameException() {
        super("이미 사용 중인 닉네임입니다.");
    }
}
