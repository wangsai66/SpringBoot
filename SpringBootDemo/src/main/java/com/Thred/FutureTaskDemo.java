package com.Thred;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.mortbay.log.Log;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

public class FutureTaskDemo {

	/*public static void main(String[] args) {
		 Long start = System.currentTimeMillis();
		 //开启多线程
         ExecutorService exs = Executors.newFixedThreadPool(5);
         try {
        	 List<Integer> list = new ArrayList<Integer>();
        	 for (int i = 0; i < 10000000; i++) {
        		 list.add(i);
			}
        	 List<Integer>   checkedList =new ArrayList<Integer>();
        	 checkedList.add(1);
        	 checkedList.add(67);
        	 checkedList.add(550);
        	 checkedList.add(963);
        	 Long getResultStart = System.currentTimeMillis();
        	 FutureTask<List<Integer>> futureTask = new FutureTask<List<Integer>>(new CallableTask(list,checkedList,0,list.size()));
        	 exs.submit(futureTask);
	         System.out.println("取出结果为==============="+ JSON.toJSONString(futureTask.get()));
	         System.out.println("总耗时="+(System.currentTimeMillis()-start)+",取结果归集耗时="+(System.currentTimeMillis()-getResultStart));
		} catch (Exception e) {
			Log.info("查询任务异常=========="+e);
		}
         
	}*/
	
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 1000; i++) {
   		 		list.add(i);
		 }
	   	 List<Integer>   checkedList =new ArrayList<Integer>();
	   	 checkedList.add(1);
	   	 checkedList.add(67);
	   	 checkedList.add(550);
	   	 checkedList.add(963);
	   	 Long getResultStart = System.currentTimeMillis();
	   	ForkTask forkTask = new ForkTask(list,checkedList,0 ,list.size());
	   	ForkJoinPool pool =new ForkJoinPool();
	   	pool.execute(forkTask);
	   	List<Integer> list2 =new ArrayList<>();
	    do {
	          System.out.printf("******************************************\n");
	          System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
	          System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
	          System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
	          System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
	          System.out.printf("***************************************** *\n");
	          try {
	              TimeUnit.SECONDS.sleep(1);
	          } catch (InterruptedException e) {
	              e.printStackTrace();
	          }
	      } while ((!forkTask.isDone()) || (!forkTask.isDone()));
		try {
			list2.addAll(forkTask.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	    System.out.println("取出结果为==============="+ JSON.toJSONString(list2));
        System.out.println("取结果归集耗时="+(System.currentTimeMillis()-getResultStart));
	}

	
}
