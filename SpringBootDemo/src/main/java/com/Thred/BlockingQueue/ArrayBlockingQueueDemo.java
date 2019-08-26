package com.Thred.BlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.mortbay.log.Log;



/**
 * 
 * 内部采用  数组对象  items来存储所有的数据
 * 
 * 常用方法解析：
 * 插入方法：
			add(E e) : 添加成功返回true，失败抛IllegalStateException异常
			offer(E e) : 成功返回 true，如果此队列已满，则返回 false。
			put(E e) :将元素插入此队列的尾部，如果该队列已满，则一直阻塞
	删除方法:
			remove(Object o) :移除指定元素,成功返回true，失败返回false
			poll() : 获取并移除此队列的头元素，若队列为空，则返回 null
			take()：获取并移除此队列头元素，若没有元素则一直阻塞。
	检查方法:
			element() ：获取但不移除此队列的头元素，没有元素则抛异常
			peek() :获取但不移除此队列的头；若队列为空，则返回 null。
			
			
             其他方法:
	    //自动移除此队列中的所有元素。
		void clear() 
		
		//如果此队列包含指定的元素，则返回 true。          
		boolean contains(Object o) 
		
		//移除此队列中所有可用的元素，并将它们添加到给定collection中。           
		int drainTo(Collection<? super E> c) 
		
		//最多从此队列中移除给定数量的可用元素，并将这些元素添加到给定collection 中。       
		int drainTo(Collection<? super E> c, int maxElements) 
		
		//返回在此队列中的元素上按适当顺序进行迭代的迭代器。         
		Iterator<E> iterator() 
		
		//返回队列还能添加元素的数量
		int remainingCapacity() 
		
		//返回此队列中元素的数量。      
		int size() 
		
		//返回一个按适当顺序包含此队列中所有元素的数组。
		Object[] toArray() 
	
	    //返回一个按适当顺序包含此队列中所有元素的数组；返回数组的运行时类型是指定数组的运行时类型。      
	    <T> T[] toArray(T[] a)
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2019年8月14日 下午4:07:10  by 王赛(wangsai@zhihuihutong.com)
 */
public class ArrayBlockingQueueDemo {
	
	//公平队列   被阻塞的线程可以按照阻塞的先后顺序访问队列
	private final static  ArrayBlockingQueue<Apple> queue =new ArrayBlockingQueue<>(10,true);
	
	/**
	 * ArrayBlockingQueue内部的阻塞队列是通过重入锁ReenterLock和Condition条件队列实现的，所以
	 * 						ArrayBlockingQueue中的元素存在公平访问与非公平访问的区别
	 * 
	 * 默认的 非公平阻塞队列  阻塞的线程将进入争夺访问资源的竞争中，也就是说谁先抢到谁就执行
	 * private final static  ArrayBlockingQueue<Apple> queue =new ArrayBlockingQueue<>(1);
	 * 
	 */
	
	public static void main(String[] args) {
		ExecutorService exe = Executors.newFixedThreadPool(3);
        exe.execute(new Producer(queue));
        exe.execute(new Producer(queue));
        exe.execute(new Consumer(queue));
        
        exe.shutdown();  
        while (true) {  
           if (exe.isTerminated()) {  
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
	
	class Apple{
		public Apple(){
			
		};
	}
	
	
	/**
	 * 生产者线程
	 * Description: 
	 * All Rights Reserved.
	 * @version 1.0  2019年8月14日 下午4:02:00  by 王赛(wangsai@zhihuihutong.com)
	 */
	class Producer  implements Runnable {
		
	   private final ArrayBlockingQueue<Apple> mAbq;
	   
		
	   Producer(ArrayBlockingQueue<Apple> mAbq){
		   this.mAbq =mAbq;
	   }
	   
		@Override
		public void run() {
				Producer();
		}
		
		private void Producer() {
			try {
				Apple apple =new Apple();
				mAbq.put(apple);
				System.out.println("生产队列==="+apple.hashCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	class Consumer implements Runnable {
		private  ArrayBlockingQueue<Apple> mAbq;
		
		
		Consumer(ArrayBlockingQueue<Apple> mAbq){
			this.mAbq=mAbq;
		}
		
		@Override
		public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
					consumner();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		}

		private void consumner() throws InterruptedException{
			Apple take = mAbq.take();
			System.out.println("消费队列==="+take.hashCode());
		}
		
	}
	
