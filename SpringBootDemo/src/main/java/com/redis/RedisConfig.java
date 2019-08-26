package com.redis;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import lombok.extern.log4j.Log4j;

/**
 * 继承CachingConfigurerSupport，为了自定义生成KEY的策略。可以不继承。
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年4月10日 下午5:58:22  by 王赛(wangsai@zhihuihutong.com)
 */
@Log4j
@Configuration
@EnableCaching//启用缓存，这个注解很重要
@JsonAutoDetect
public class RedisConfig  extends CachingConfigurerSupport{
	   
		@Configuration
	    static class LocalConfiguration {
	        //从application.yml中获得以下参数
	        @Value("${spring.redis.host}")
	        private String host;
	        @Value("${spring.redis.port}")
	        private Integer port;
	        @Value("${spring.redis.password}")
	        private String password;
	        
	        /**
	         * 缓存管理器.
	         * @param redisTemplate
	         * @return
	         */
	        @Bean
	        public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
	           CacheManager cacheManager = new RedisCacheManager(redisTemplate);
	           return cacheManager;
	        }
	        @Bean
	        public RedisTemplate<String, String> redisTemplate(
	                RedisConnectionFactory factory) {
	        	log.info("==================sssssss===============");
	            StringRedisTemplate template = new StringRedisTemplate(factory);
	            ObjectMapper om = new ObjectMapper();
	            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	            template.setValueSerializer(new StringRedisSerializer());
	            template.afterPropertiesSet();
	            return template;
	        }

	        @Bean
	        public JedisConnectionFactory redisConnectionFactory() {
	            JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
	            redisConnectionFactory.setHostName(host);
	            redisConnectionFactory.setPort(port);
	            redisConnectionFactory.setPassword(password);

	            return redisConnectionFactory;
	        }
	    }

	    /**
	     * 自定义key.
	     * 此方法将会根据类名+方法名+所有参数的值生成唯一的一个key
	     */
	    @Override
	    public KeyGenerator keyGenerator() {
	        log.info("RedisCacheConfig.keyGenerator()");
	       return new KeyGenerator() {
	           @Override
	           public Object generate(Object o, Method method, Object... objects){          
	              StringBuilder sb = new StringBuilder();
	              sb.append(o.getClass().getName());
	              sb.append(method.getName());
	              for (Object obj : objects) {
	                  sb.append(obj.toString());
	              }
	              log.info("keyGenerator=" + sb.toString());
	              return sb.toString();
	           }
	       };
	    }
	
}
