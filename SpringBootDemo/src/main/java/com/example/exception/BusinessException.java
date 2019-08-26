package com.example.exception;

import com.util.ResultEnum;

/**
 * 自定义业务异常类
 * @author wangsai
 *
 */
public class BusinessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//自定义错误码  
    private String code;
    
     
    
    public BusinessException() {}  
    
    public  BusinessException (ResultEnum resultEnum){
    	super(resultEnum.getMsg());
    	this.code=resultEnum.getCode();
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
	
	
    
    
	
}
