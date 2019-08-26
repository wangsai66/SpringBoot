package com.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.runnable.MyRunnable;
import com.util.TransformForYdp;

import lombok.extern.log4j.Log4j;


@Controller
@RequestMapping("/redisTest")
@Log4j
public class RedisTest {
	
	@Autowired
	private RedisCacheService  redisUtils;
	
	private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
	
	 public static final String SCORE_RANK = "score_rank";
	
	@RequestMapping(value="/test",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public  void  redisTest(){
		log.info("进入方法");
		boolean setNX = redisUtils.setNX("ws88", "888", 5);
		log.info("是否存在"+setNX);
		if(setNX){
			log.info("通过");
			MyRunnable run =new  MyRunnable(new RedisTest(),redisUtils);
			pool.schedule(run, 6, TimeUnit.SECONDS);
		}else  {
			log.info("该时间段重复请求");
		}
		
	}
	
	@RequestMapping(value="/batchAdd",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void batchAdd() {
	        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
	        long start = System.currentTimeMillis();
	        for (int i = 0; i < 100000; i++) {
	            DefaultTypedTuple<Object> tuple = new DefaultTypedTuple<>("张三" + i, 1D + i);
	            tuples.add(tuple);
	        }
	        System.out.println("循环时间:" +( System.currentTimeMillis() - start));
	        Long num = redisUtils.zadd(SCORE_RANK, tuples);
	        System.out.println("批量新增时间:" +(System.currentTimeMillis() - start));
	        System.out.println("受影响行数：" + num);
	 }
	
	

	@RequestMapping(value="/queryTop",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void queryTop() {
		 List<Object> topRankZSet = redisUtils.getTopRankZSet(SCORE_RANK,  9);
		 int count = 1;
		 for (Object object : topRankZSet) {
			 System.out.println("排行榜分别是"+"第"+count +"名" +object+"  分数是:"+ redisUtils.score(SCORE_RANK,object));
			 count ++;
		}
	}
	
	
	@RequestMapping(value="/add",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	 public void add(@RequestParam String param) {
		TransformForYdp<Map>  p = TransformForYdp.fromJson(param, Map.class);
		Map<String, Object> map= (Map<String, Object>) p.getData();
		redisUtils.zadd(SCORE_RANK, (double) map.get("value") , map.get("name").toString());
	 }
	
	@RequestMapping(value="/find",method=RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	 public void find(){
			Long llen = redisUtils.zCard(SCORE_RANK);
	        Long rankNum = redisUtils.zRank(SCORE_RANK, "李四");
	        System.out.println("李四的个人排名：" + (llen.intValue()-rankNum.intValue()));
	        Double score = redisUtils.score(SCORE_RANK, "李四");
	        System.out.println("李四的分数:" + score);
	 }
	
	
}
