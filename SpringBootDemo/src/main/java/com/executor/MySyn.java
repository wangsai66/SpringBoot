package com.executor;

public class MySyn implements  Runnable {
	
		 int  count =10;
		 
		 public void run() {
	        while(true){
	            synchronized (this) {                       
	                if(count>0){
	                    try {
	                        Thread.sleep(10);                               
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                    System.out.println(Thread.currentThread().getName() + " " + count--);
	                }
	            }
	        }
	    }
}
