package com.example.exception;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.util.ResultUtil;


/**
 * 全局异常类
 * @param request
 * @param e
 * @return
 */
@ControllerAdvice
@ResponseBody
public class GlobalDefultExceptionHandler {

	
	
	/**
	 *  所有异常报错 (现阶段不用这个，统一用ResultUtil来获取抛出异常)
	 * Description: 
	 * All Rights Reserved.
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 * @version 1.0  2018年4月8日 下午2:45:40  by 王赛(wangsai@zhihuihutong.com)
	 */
    @ExceptionHandler(value=Exception.class)  
    public String allExceptionHandler(HttpServletRequest request,  
            Exception e) throws Exception  
    {
    	ResultUtil r =new ResultUtil();
        e.printStackTrace();  
        if(e instanceof BusinessException) {  
            BusinessException businessException = (BusinessException)e;  
            return r.error(businessException.getCode(), e.getMessage());
        }  
        return "服务器异常，请联系管理员！";  
    }  
}
