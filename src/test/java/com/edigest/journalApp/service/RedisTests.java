package com.edigest.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests{

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    public void testRedisConnection(){
        redisTemplate.opsForValue().set("email","vipul@gmail.com");

        Object email = redisTemplate.opsForValue().get("email");
        int i=1;
    }


}
