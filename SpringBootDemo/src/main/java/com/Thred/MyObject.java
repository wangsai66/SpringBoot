package com.Thred;

import lombok.extern.log4j.Log4j;

@Log4j
public class MyObject {
	
	private volatile static MyObject myObject;
	
	public MyObject (){
		
	}
	
	public static MyObject getInstance(){
		try {
			if(null == myObject){
				Thread.sleep(3000);
				synchronized(MyObject.class){
					if(null == myObject){
						myObject =new MyObject();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myObject;
	}
	
	

}
