package com.team04.buy_gurus.category.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response<T> {

    private boolean success;
    private int code;
    private T result;


    public static Response success(){return new Response(true, 0, null);}

    public static <T> Response success(T data){return new Response(true, 0, data);}

    public static Response failure(int code, String msg){ return new Response(false, code, new Failure(msg));}
}
