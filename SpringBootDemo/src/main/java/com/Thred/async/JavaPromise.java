package com.Thred.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import lombok.extern.log4j.Log4j;

/**
 * jdk 1.8 之后 实现任务的异步处理
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2019年7月6日 上午11:07:59  by 王赛(wangsai@zhihuihutong.com)
 */
@Log4j
public class JavaPromise {
	
	public static void main(String[] args) {
		try {
			 //创建一个固定大小的线程池
		       ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
		       CompletableFuture<String> futrue=CompletableFuture.supplyAsync(new Supplier<String>() {
					@Override
					public String get() {
						log.info("task started");
						try {
							//模拟耗时操作
							longTimeMethod();
						} catch (Exception e) {
							
						}
						return "task  sunccess";
					}

		       },fixedThreadPool);
		       futrue.thenAccept(e -> System.out.println(e + " ok"));
		} catch (Exception e2) {
			
		}
	  
	}
	
	
	public static void longTimeMethod() {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			
		}
		
	}

}
