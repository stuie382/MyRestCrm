package com.stuart.mycrm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

@Aspect
@Component
public class CrmLoggingAspect {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.stuart.mycrm.service.*.*(..))")
    private void forServicePackage() {
        // Pointcut, intentionally empty
    }

    @Pointcut("execution(* com.stuart.mycrm.dao.*.*(..))")
    private void forDAOPackage() {
        // Pointcut, intentionally empty
    }

    @Pointcut("forServicePackage() || forDAOPackage()")
    private void forAppFlow() {
        // Pointcut, intentionally empty
    }

    @Before("forAppFlow()")
    public void before(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().toShortString();
        logger.info(() -> "Calling method: " + method);

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            logger.info(() -> "====> Argument: " + arg);
        }
    }

    @AfterReturning(pointcut = "forAppFlow()",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().toShortString();
        logger.info(() -> "=====> After Returning from method: " + method);
        logger.info(() -> "====> Result: " + result);
    }
}

