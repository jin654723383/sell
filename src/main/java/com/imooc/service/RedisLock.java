package com.imooc.service;

import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.core.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * redis锁
 * <B>文件名称：</B>RedisLock.java<BR>
 * <B>文件描述：</B><BR>
 * <BR>
 * <B>版权声明：</B>(C)2018-2015<BR>
 * <B>公司部门：</B>研发部<BR>
 * <B>创建时间：</B>2018年11月22日<BR>
 * 
 * @author 金松广
 * @version
 */
@Commit
@Slf4j
public class RedisLock {
   
    @Autowired
    public StringRedisTemplate redisTemplate;
   /**
    * 加锁
    * @param key 
    * @param value 当前时间+过期时间
    * @return
    */
    public boolean lock(String key,String value) {
        if(redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //取出数据
        String currentValue = redisTemplate.opsForValue().get(key);
        //锁过期
        if(StringUtils.isNotBlank(currentValue) &&
                    Long.valueOf(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if(currentValue.equals(oldValue)) {
                return true;
            }
        }
        return false;
    }
}
