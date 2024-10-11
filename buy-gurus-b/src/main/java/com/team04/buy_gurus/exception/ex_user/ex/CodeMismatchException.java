package com.team04.buy_gurus.exception.ex_user.ex;

public class CodeMismatchException extends RuntimeException {

    public CodeMismatchException() {
        super("인증번호가 올바르지 않습니다.");
    }
}
