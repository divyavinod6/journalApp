package com.edigest.journalApp.service;

import com.edigest.journalApp.api.response.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> responseClass){
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(),responseClass);

        }catch (Exception e){
            log.error("ERROR AT RedisService GET :"  + e);
            return null;
        }
    }
    public void set(String key,Object o,Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,jsonStr,ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("Error at RedisService Set " + e);
        }
    }
}
