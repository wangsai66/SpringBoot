package com.runnable;



import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.redis.RedisCacheService;
import com.redis.RedisTest;

import lombok.extern.log4j.Log4j;

@Log4j
public class MyRunnable  implements Runnable  {

	private RedisTest test;
	
	private RedisCacheService  redisUtils;
	
	public MyRunnable (RedisTest test, RedisCacheService  redisUtils){
		this.test  = test;
		this.redisUtils  = redisUtils;
	}
	
	
	@Override
	public void run() {
		boolean setNX = redisUtils.setNX("ws88", "888", 5);
		if(setNX){
			log.info("通过");
		}else  {
			log.info("该时间段重复请求");
		}
	}


}
