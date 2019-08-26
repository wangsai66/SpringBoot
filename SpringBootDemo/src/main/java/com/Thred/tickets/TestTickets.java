package com.Thred.tickets;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.redis.RedisCacheService;



/**
 * 车牌发售
 * 
 * 实现同时一百个人抢五张车牌，有序，先到先得
 * 
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2019年8月17日 下午2:06:08  by 王赛(wangsai@zhihuihutong.com)
 */
@Controller
@RequestMapping("/testTickets")
public class TestTickets {
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	 //采用非公平队列
	 private static final ArrayBlockingQueue<Station> queue =new ArrayBlockingQueue<>(100);
	
	 @RequestMapping(value={"/Tets"}, method=RequestMethod.GET)
	 public void  Tets(){
		 ExecutorService execu = Executors.newSingleThreadExecutor();
			execu.execute(new StationProducer(queue));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			execu.shutdown();
			
			ExecutorService exec = Executors.newFixedThreadPool(10);
			exec.execute(new StationConsumer(queue,redisCacheService));
			exec.execute(new StationConsumer(queue,redisCacheService));
			exec.execute(new StationConsumer(queue,redisCacheService));
			exec.execute(new StationConsumer(queue,redisCacheService));
			exec.execute(new StationConsumer(queue,redisCacheService));
			exec.execute(new StationConsumer(queue,redisCacheService));
			
			exec.shutdown();  
	        while (true) {  
	           if (exec.isTerminated()) {  
	        	   if(null == queue.peek()){
	               	System.out.println("队列数据消费完毕");
	               }else {
	               		System.out.println("剩余队列数量==="+queue.size());
	               }
	               break;  
	          }  
	        }  
	 }
	 
	 
	
	

}

class Station{
    //值班人数
    private String name;
    
    private int  buyNum;  //购买的张数
	
	public Station(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	
	
	
}

class StationProducer implements  Runnable{
	
	private final ArrayBlockingQueue<Station> mAbq;
	  
	
	StationProducer(ArrayBlockingQueue<Station> mAbq){
		   this.mAbq =mAbq;
	}
	
	public void run() {
		try {
			Station s =null;
			for (int i = 1; i < 10; i++) {
				s =new Station();
				s.setName("张"+i);
				s.setBuyNum(i);
				mAbq.put(s);
			}
			Station s1 =new Station();
			s1.setName("Mr.王");
			s1.setBuyNum(1);
			mAbq.put(s1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}




class StationConsumer implements Runnable{
	
	//固定的票数
    private  static   int  TicketsNum = 11;
    //抢票的人数
    private   ArrayBlockingQueue<Station> queue;
    
    private RedisCacheService redisCacheService;
    
    
    public StationConsumer (ArrayBlockingQueue<Station> queue,RedisCacheService redisCacheService){
    	this.queue =queue;
    	this.redisCacheService =redisCacheService;
    }
    

	@Override
	public void run() {
		try {
			while (true){
				boolean setNX = redisCacheService.setNX("TicketsNum", TicketsNum,5);
				if(setNX){
					//开始售票
					if(TicketsNum < 1){
						System.out.println("票卖完了");
						break;
					}else {
						if(queue.size() > 0){
							Station peek = queue.peek();
							if(TicketsNum >= peek.getBuyNum()){
								Station poll = queue.poll();
								System.out.println(poll.getName()+"买到了"+poll.getBuyNum()+"票");
								TicketsNum -= poll.getBuyNum();
							}else {
								System.out.println("不符合条件的人员是"+peek.getName() +"---"+peek.getBuyNum());
								boolean remove = queue.remove(peek);
								if(!remove){
									System.out.println("移除失败");
								}
							}
						}else {
							System.out.println("排队的人已经买完了");
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    
    
	
}






