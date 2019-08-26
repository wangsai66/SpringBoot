package com.util;

public enum ResultEnum {
	    UNKONW_ERROR("9999","未知错误"),  
	    SUCCESS("6666","成功"),  
	    ERROR("3333","失败"),  
	    ;  
	      
	    private String code;  
	    private String msg;  
	      
	    ResultEnum(String code,String msg) {  
	        this.code = code;  
	        this.msg = msg;  
	    }  
	  
	    public String getCode() {  
	        return code;  
	    }  
	  
	    public String getMsg() {  
	        return msg;  
	    }  
	    

		public static String getValue(String name) {  
	        for (ResultEnum c : values()) {  
	            if (c.getMsg() == name) {  
	                return c.code;  
	            }  
	        }  
	     return null;  
	   }  
		public static void main(String[] args) {
			String value ="车辆组";
			String[] split = value.split(">");
			String parent =split[0];
			System.out.println(parent);
			for (String string : split) {
				System.out.println(string);
			}
		}
}
