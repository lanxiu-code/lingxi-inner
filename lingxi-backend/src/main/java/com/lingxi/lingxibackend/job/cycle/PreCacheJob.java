package com.lingxi.lingxibackend.job.cycle;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingxi.lingxibackend.model.entity.User;
import com.lingxi.lingxibackend.service.UserService;
import com.lingxi.lingxibackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedissonClient redissonClient;
    // 重点用户
    private List<Long> mainUserList = Arrays.asList(1L);
    /**
     * 每6小时执行一次
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void run() {
        RLock clientLock = redissonClient.getLock("lingxi:precachejob:docache:lock");
        try{
            if(clientLock.tryLock(0, -1, TimeUnit.MILLISECONDS)){
                log.info("缓存预热开始");
                // 查询数据库
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                Page<User> page = userService.page(new Page<>(1, 20), wrapper);
                String redisKey = String.format("lingxi:user:recommend:%s",1L);
                //写入缓存
                try {
                    redisUtil.set(redisKey, page,30);
                }catch (Exception e){
                    log.info("redis set key error",e);
                }
            }
        }catch (Exception e){
            log.error("doCacheRecommendUser error", e);
        }finally {
            if(clientLock.isHeldByCurrentThread()){
                clientLock.unlock();
            }
        }
    }
}
