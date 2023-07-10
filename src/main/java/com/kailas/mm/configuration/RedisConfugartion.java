//package com.kailas.mm.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableRedisRepositories
//public class RedisConfugartion {
//    @Bean
//    public JedisConnectionFactory getConnectionFactory() {
//        RedisStandaloneConfiguration redisStandAloneConfig = new RedisStandaloneConfiguration();
//        redisStandAloneConfig.setPort(6379);
//        redisStandAloneConfig.setHostName("localhost");
//        return new JedisConnectionFactory(redisStandAloneConfig);
//    }
//
//    @Bean
//    public RedisTemplate<String ,Object> getRedisTemplate(){
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(getConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//      //  redisTemplate.
//
//
//        return new RedisTemplate();
//    }
//
//}
