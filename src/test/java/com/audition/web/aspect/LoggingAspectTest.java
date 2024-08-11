package com.audition.web.aspect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoggingAspectTest {

    @Test
    public void testBeforeOtherControllerCall() throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        LoggingAspect logging = new LoggingAspect();
        logging.logExecutions(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(2)).getSignature();
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}
