package hu.webuni.nyilvantarto.aspect;

import hu.webuni.nyilvantarto.exception.BackendNotAvailableException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FaultRetryAspect {


    @Around("@annotation(hu.webuni.nyilvantarto.aspect.FaultRetry)")
    public Object retry(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Szabi initialized");
        int retryCnt = 5;
        return retry(proceedingJoinPoint, retryCnt);
    }



    public Object retry(ProceedingJoinPoint proceedingJoinPoint, int retryCount) throws Throwable {

        System.out.println("A " + retryCount + " iterációban vagyunk");
        if (retryCount == 0)
            return null;

        try {
            Object o = proceedingJoinPoint.proceed();
            return o;
        } catch (BackendNotAvailableException e) {
            retry(proceedingJoinPoint, --retryCount);
        }
        return null;
    }

}
