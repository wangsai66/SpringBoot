package com.filtel;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*@Component //实例化到spring容器
*/public class MyFilter  implements Filter {

	 /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        System.out.println("time is destory");
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("the filter is begin");
        long start=new Date().getTime();
        chain.doFilter(req, resp);
        System.out.println("耗时为:"+(new Date().getTime()-start)+"ms");
        System.out.println("the filter is end");
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("time is init");
    }

}
