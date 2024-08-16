package com.lingxi.lingxibackend.service;

import javax.annotation.Resource;

import cn.hutool.core.date.StopWatch;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.model.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务测试
 *
 * 
 * 
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    @Test
    void userRegister() {
        String userAccount = "lingxi";
        String userPassword = "";
        String checkPassword = "123456";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
    @Test
    void searchUsersByTags(){
        List<String> tags = Arrays.asList("java");
        List<UserVO> userVOList = userService.searchUsersByTags(tags);
        System.out.println(userVOList);
    }

    @Test
    void insertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        final int INSERT_NUM = 1000;
        List<String> tags = Arrays.asList("java", "python", "c++", "c", "c#", "php", "go",
                "rust", "swift", "kotlin", "ruby", "scala", "haskell", "lisp",
                "perl", "sql", "plsql", "mysql", "mongodb", "redis", "postgresql",
                "羽毛球", "乒乓球", "足球", "篮球", "排球", "网球",
                "oracle", "sqlserver", "mssql");
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假沙鱼");
            user.setUserAccount("lingxi"+i);
            user.setAvatarUrl("https://img0.baidu.com/it/u=3860090450,2781056586&fm=253&fmt=auto?w=400&h=400");
            user.setUserProfile("这个人很懒，什么都没留下");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("123456789108");
            user.setEmail("123456789108@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setTags(tags.get(i%tags.size()));
            userService.save(user);
        }
        System.out.println( stopWatch.getLastTaskTimeMillis());
    }
    @Test
    void testWatchDog(){
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(-1, TimeUnit.MILLISECONDS)) {
                Thread.sleep(300000);
                System.out.println("getLock: " + Thread.currentThread().getId());
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
