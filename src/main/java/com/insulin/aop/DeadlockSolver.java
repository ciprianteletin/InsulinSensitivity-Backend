package com.insulin.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Class that watches any deadlock that can occur when making db operations and retries the method in cause for three times in a row.
 * This approach is necessary because there could be unexpected situations that can generate a deadlock type error.
 */
@Aspect
@Component
public class DeadlockSolver implements Ordered {

    public static final Logger logger = LoggerFactory.getLogger(DeadlockSolver.class);

    /**
     * Order for this aspect, should be lower than for transaction manager which has 100
     **/
    protected int order = 99;

    /**
     * How many retries should be tried on deadlock
     **/
    protected int retryCount = 3;

    /**
     * How big is delay between deadlock retry (in ms)
     **/
    protected int delay = 1000;

    @Around(value = "@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object methodRetry(ProceedingJoinPoint pjp) throws Throwable {
        return detectDeadlocks(pjp);
    }

    @Around(value = "@within(org.springframework.transaction.annotation.Transactional)")
    public Object classRetry(ProceedingJoinPoint pjp) throws Throwable {
        return detectDeadlocks(pjp);
    }

    protected Object detectDeadlocks(ProceedingJoinPoint pjp) throws Throwable {
        if (logger.isTraceEnabled()) {
            logger.trace("Before pointcut {} with transaction manager active: {}",
                    pjp.toString(), TransactionSynchronizationManager.isActualTransactionActive());
        }

        try {
            int retryCount = getRetryCount();
            while (true) {
                try {
                    return pjp.proceed();
                } catch (LockAcquisitionException | CannotAcquireLockException ex) {
                    if (TransactionSynchronizationManager.isActualTransactionActive()) {
                        if (logger.isTraceEnabled())
                            logger.trace("Deadlock pointcut detected with transaction still active - propagating");
                        throw ex;
                    } else {
                        if (retryCount-- == 0) {
                            throw ex;
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("Deadlock pointcut retry with retryCount={} (sleeping {} ms)",
                                    retryCount, getDelay());
                        }

                        Thread.sleep(getDelay());
                    }
                }
            }
        } finally {
            if (logger.isTraceEnabled()) {
                logger.trace("After pointcut {} with transaction manager active: {}",
                        pjp.toString(), TransactionSynchronizationManager.isActualTransactionActive());
            }
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public int getDelay() {
        return delay;
    }
}
