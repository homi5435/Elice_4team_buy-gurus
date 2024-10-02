package com.team04.buy_gurus.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OrderControlerAop {
    @Pointcut("execution(* com.team04.buy_gurus.order.controller.*.*(..))")
    public void pointcutTarget() {}

    @AfterThrowing(value = "pointcutTarget()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error(e.getMessage());
    }
}
