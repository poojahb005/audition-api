package com.audition.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(public * com.audition..*(..)) && @target(com.audition.common.annotation.LogEntryExit)")
    public Object logExecutions(ProceedingJoinPoint joinPoint) throws Throwable {
        LOG.info("Method Entry: {}", joinPoint.getSignature());
        Object result = joinPoint.proceed();
        LOG.info("Method Exit: {}", joinPoint.getSignature());
        return result;
    }

}