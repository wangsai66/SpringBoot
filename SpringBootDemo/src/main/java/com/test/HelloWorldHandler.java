package com.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

import freemarker.template.utility.DateUtil;
import lombok.extern.log4j.Log4j;

/**
 * 代理类
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年11月27日 上午11:37:27  by 王赛(wangsai@zhihuihutong.com)
 */
@Log4j
public class HelloWorldHandler implements InvocationHandler {
	
	private Object object;
	
	public HelloWorldHandler (Object obj){
		super();
		this.object = obj;
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object o =null;
		System.out.println("调用时间"+System.currentTimeMillis());
		o=method.invoke(object, args);
		System.out.println("结束");
		return o;
	}





}
