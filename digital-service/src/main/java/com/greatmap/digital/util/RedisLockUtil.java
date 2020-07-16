package com.greatmap.digital.util;

import com.greatmap.framework.commons.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lock(String key,String value){
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            return true;
        }

        String currentTime = (String)redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(currentTime) && (Long.parseLong(currentTime) < System.currentTimeMillis())){
            String oldTime = (String)redisTemplate.opsForValue().getAndSet(key,value);
            if(StringUtils.isNotEmpty(oldTime) && oldTime.equals(currentTime)){
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     * @param key 键
     * @param value 值
     */
    public void unlock(String key,String value){
        String currentTime = (String)redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(currentTime) && currentTime.equals(value)){
            redisTemplate.opsForValue().getOperations().delete(key);
        }
    }

}
