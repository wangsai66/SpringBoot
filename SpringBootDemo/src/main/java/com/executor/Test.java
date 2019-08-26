package com.executor;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class Test {

	
	@Async
    public static void sayHello(String name) {
	      LoggerFactory.getLogger(Test.class).info(name + ":Hello World!");
    }
	
	
}
