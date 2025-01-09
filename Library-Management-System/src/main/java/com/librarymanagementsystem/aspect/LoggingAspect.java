package com.librarymanagementsystem.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect //extra functionality I want to add
@Component
//to add extra functionality to your code without changing the code
//itself. Instead of writing the same logic multiple times, you write
// it once and tell Spring where and when to apply it.
public class LoggingAspect {

    @Before("execution(* com.librarymanagementsystem.service.*.*(..))") // Pointcut expression (which methods or locations in the application the advice should apply to.)
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Executing method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(* com.librarymanagementsystem.service.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("Method executed successfully: " + joinPoint.getSignature().getName());
    }

    @AfterThrowing("execution(* com.librarymanagementsystem.service.*.*(..))")
    public void logAfterThrowing(JoinPoint joinPoint) {
        System.out.println("Exception occurred in method: " + joinPoint.getSignature().getName());
    }
}
