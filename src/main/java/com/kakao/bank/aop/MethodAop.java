package com.kakao.bank.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodAop {

    @Around("execution(* com.kakao.bank.controller..*(..))")
    public Object runMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("\n" + joinPoint.toShortString() + "\n");

        return joinPoint.proceed();
    }
}
