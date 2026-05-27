package com.basscode.employee_leave_management.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggingAspect {
    @Around("execution(* com.basscode.employee_leave_management.service.*.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {

        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args     = joinPoint.getArgs();

        AppLogger.TRACE.trace("[SERVICE] {}.{}() called | args={}",
                className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            AppLogger.TRACE.trace("[SERVICE] {}.{}() result={}",
                    className, methodName, result);

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;

            AppLogger.ERROR.error("[SERVICE] {}.{}() FAILED | duration={}ms | error={}",
                    className, methodName, duration, e.getMessage(), e);

            throw e;
        }
    }

    @Around("execution(* com.basscode.employee_leave_management.repository.*.*(..))")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {

        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        AppLogger.DATABASE.debug("[REPOSITORY] {}.{}() executing...",
                className, methodName);

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            AppLogger.DATABASE.debug("[REPOSITORY] {}.{}() done | duration={}ms",
                    className, methodName, duration);

            return result;

        } catch (Exception e) {
            AppLogger.ERROR.error("[REPOSITORY] {}.{}() FAILED | error={}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}
