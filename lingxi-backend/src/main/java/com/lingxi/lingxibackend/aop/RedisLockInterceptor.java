package com.lingxi.lingxibackend.aop;

import cn.hutool.core.util.ObjectUtil;
import com.lingxi.lingxibackend.annotation.AuthCheck;
import com.lingxi.lingxibackend.annotation.RedisLock;
import com.lingxi.lingxibackend.common.ErrorCode;
import com.lingxi.lingxibackend.exception.BusinessException;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.enums.UserRoleEnum;
import com.lingxi.lingxibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 AOP
 * 
 */
@Aspect
@Component
@Slf4j
public class RedisLockInterceptor {

    @Resource
    private RedissonClient redissonClient;
    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param redisLock
     * @return
     */
    @Around("@annotation(redisLock)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        String lockName = redisLock.lockName();
        RLock clientLock = redissonClient.getLock(lockName);
        Object result = null;
        boolean isLock;
        if (redisLock.watchdog()) {
            isLock = clientLock.tryLock(0, TimeUnit.SECONDS);
        } else {
            isLock = clientLock.tryLock(0,redisLock.leaseTime(), TimeUnit.SECONDS);
        }
        if (isLock) {
            try {
                //执行方法
                result = joinPoint.proceed();
            } finally {
                if (clientLock.isLocked() && clientLock.isHeldByCurrentThread()) {
                    clientLock.unlock();
                }
            }
        }else{
            log.info("获取锁失败");
        }
        return result;
    }
}

