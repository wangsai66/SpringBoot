package com.Thred.BlockingQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 基础概念:
 *      组成构成：
 * 		LinkedBlockingQueue是一个由链表实现的有界队列阻塞队列，但大小默认值为Integer.MAX_VALUE,所以我们在使用
 * 				LinkedBlockingQueue时建议手动传值，为其提供我们所需的大小，避免队列过大造成机器负载或者内存爆满等情况
 * 			元素排列方式
 * 		LinkedBlockingQueue队列也是按 FIFO（先进先出）排序元素。队列的头部是在队列中时间最长的元素，队列的尾部 是在队列中时间
 * 			最短的元素，新元素插入到队列的尾部，而队列执行获取操作会获得位于队列头部的元素
 * 		性能比较:
 *			链接队列的吞吐量要高于基于数组的队列（ArrayBlockingQueue），因为其内部实现添加和删除操作使用的两个ReenterLock
 *				来控制并发执行，而ArrayBlockingQueue内部只是使用一个ReenterLock控制并发，因此LinkedBlockingQueue的吞吐量要高于ArrayBlockingQueue
 *		数据的存储过程以及特点
*			每个添加到LinkedBlockingQueue队列中的数据都将被封装成Node节点，添加的链表队列中，其中head和last分别指向
*        		队列的头结点和尾结点。与ArrayBlockingQueue不同的是，LinkedBlockingQueue内部分别使用了
 *        		  takeLock 和 putLock 对并发进行控制，也就是说，添加和删除操作并不是互斥操作，可以同时进行
					这样也就可以大大提高吞吐量。这里再次强调如果没有给LinkedBlockingQueue指定容量大小，
						其默认值将是Integer.MAX_VALUE，如果存在添加速度大于删除速度时候，有可能会内存溢出
 *		具体方法:
 *				阻塞队列的大小，默认为Integer.MAX_VALUE 
			    private final int capacity;
			
			            当前阻塞队列中的元素个数 
			    private final AtomicInteger count = new AtomicInteger();
			           
			           阻塞队列的头结点
			    transient Node<E> head;
			
			
			           阻塞队列的尾节点
			    private transient Node<E> last;
			
			   	获取并移除元素时使用的锁，如take, poll, etc 
			    private final ReentrantLock takeLock = new ReentrantLock();
			
			     notEmpty条件对象，当队列没有数据时用于挂起执行删除的线程 
			    private final Condition notEmpty = takeLock.newCondition();
			
			   	添加元素时使用的锁如 put, offer, etc 
			    private final ReentrantLock putLock = new ReentrantLock();
			
			    notFull条件对象，当队列数据已满时用于挂起执行添加的线程 
			    private final Condition notFull = putLock.newCondition();
    	
  		
 *		
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2019年8月14日 下午5:31:45  by 王赛(wangsai@zhihuihutong.com)
 */
public class LinkedBlockingQueueDemo {
	
	 //默认无界队列，使用时需指定固定大小，防止内存溢出
	 private static final LinkedBlockingQueue<Apple> queue =new LinkedBlockingQueue<>(3);
	 
	 public static void main(String[] args) {
		ExecutorService execu = Executors.newFixedThreadPool(3);
		execu.execute(new Producer2(queue));
		execu.execute(new Producer2(queue));
		execu.execute(new Consumer2(queue));
		
		execu.shutdown();
		while (true) {
			if(execu.isTerminated()){
				if(0 == queue.size()){
					System.out.println("队列消费完毕");
				}else  {
					System.out.println("当前剩余个数:"+queue.size());
				}
				break;
			}
		}
	 }
}	 
	 class Producer2 implements  Runnable {
		 
		 private final LinkedBlockingQueue<Apple> lbq;
		 
		 Producer2(LinkedBlockingQueue<Apple> lbq){
			 this.lbq = lbq;
		 }
		 
		public void run() {
			Apple apple=new Apple();
			try {
				lbq.put(apple);
				System.out.println("生产队列信息为===="+apple.hashCode());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	 
   class Consumer2 implements  Runnable {
		 
		 private final LinkedBlockingQueue<Apple> lbq;
		 
		 Consumer2(LinkedBlockingQueue<Apple> lbq){
			 this.lbq = lbq;
		 }
		 
		public void run() {
			try {
				TimeUnit.MILLISECONDS.sleep(1000);
				Apple take = lbq.take();
				System.out.println("消费队列信息为===="+take.hashCode());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	


