package com.Thred;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.log4j.Log4j;

@Log4j
public class ThreadTet {
	
	public static void main(String[] args) {
		
		ExecutorService exe = Executors.newFixedThreadPool(3);  
		MyThread my1 =new MyThread();
		MyThread my2 =new MyThread();
		MyThread my3 =new MyThread();
		MyThread my4 =new MyThread();
		MyThread my5 =new MyThread();
		exe.execute(my1);
		exe.execute(my2);
		exe.execute(my3);
		exe.execute(my4);
		exe.execute(my5);
		long start =System.currentTimeMillis();	
		exe.shutdown();  
        while (true) {  
           if (exe.isTerminated()) {  
        	   long time = System.currentTimeMillis() - start;
       			log.info("执行时间为====="+ time);
               break;  
          }  
        }  
	}
	

}
