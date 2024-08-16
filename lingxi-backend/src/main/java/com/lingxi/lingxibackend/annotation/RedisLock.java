package com.lingxi.lingxibackend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    // 锁名
    String lockName();
    //锁的失效时间
    long leaseTime() default 3;
    //是否开启看门狗，默认开启，开启时锁的失效时间不执行。任务未完成时会自动续期锁时间
    //使用看门狗，锁默认redis失效时间未30秒。失效时间剩余1/3时进行续期判断，是否需要续期
    boolean watchdog() default true;

}

