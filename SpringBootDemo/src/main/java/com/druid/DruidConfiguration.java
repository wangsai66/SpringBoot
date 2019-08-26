package com.druid;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;


/**
 * 　Druid是一个关系型数据库连接池,是阿里巴巴的一个开源项目,Druid在监控,
 * 可扩展性,稳定性和性能方面具有比较明显的优势.通过Druid提供的监控功能,
 * 可以实时观察数据库连接池和SQL查询的工作情况.使用Druid在一定程度上可以提高数据库的访问技能
 * Description: 
 * 启动项目后监控地址为  : http://localhost:8080/druid/index.html
 * All Rights Reserved.
 * @version 1.0  2018年4月12日 上午10:31:53  by 王赛(wangsai@zhihuihutong.com)
 */
/*@Configuration*/
public class DruidConfiguration {

	
	@Bean
    public ServletRegistrationBean statViewServle(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //白名单：
        servletRegistrationBean.addInitParameter("allow","192.168.1.218,127.0.0.1");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的即提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny","192.168.1.100");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername","druid");
        servletRegistrationBean.addInitParameter("loginPassword","12345678");
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean statFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
    
    
    
    /**
     * 告诉springboot采用Druid数据源:
     * Description: 
     * All Rights Reserved.
     * @return
     * @version 1.0  2018年4月12日 上午10:32:20  by 王赛(wangsai@zhihuihutong.com)
     */
    @Bean(name = "dataSource")
    @Primary
   /* @ConfigurationProperties(prefix = "spring.datasource")*/
    public DataSource dataSource(){
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }
	
	
}
