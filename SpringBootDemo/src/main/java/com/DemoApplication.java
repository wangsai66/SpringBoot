package com;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.terran4j.commons.api2doc.config.EnableApi2Doc;

import lombok.extern.log4j.Log4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 全局启动类
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年4月9日 下午4:48:37  by 王赛(wangsai@zhihuihutong.com)
 */
//文档访问地址： http://localhost:8080/api2doc/home.html
@Log4j
@SpringBootApplication
@EnableSwagger2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		log.info("============启动完成==========");
	}
	
	@Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    public String home(@RequestParam String name) {
        return "hi "+name+",i am from port:" +port;
    }
}
