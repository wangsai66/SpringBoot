package com.Thred;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.collections.CollectionUtils;
import org.mortbay.log.Log;

import com.forkJoin.LineTask;

public class CallableTask implements Callable<List<Integer>>{
	private List<Integer> list;
	private List<Integer> checkedList;
    private int start;
    private int end;
    
	public CallableTask(List<Integer> list, List<Integer> checkedList,int start,int end) {
		super();
		this.list =list;
		this.checkedList =checkedList;
		this.start =start;
		this.end =end;
	}

	@Override
	public List<Integer> call() throws Exception {
		ExecutorService exs = Executors.newFixedThreadPool(5);
	   if (end - start < 150000) {
		  return  isCheck(list,checkedList,start,end);
        } else {
        	int mid = (start + end) / 2;
        	FutureTask<List<Integer>> futureTask1 = new FutureTask<List<Integer>>(new CallableTask(list, checkedList,start, mid));
        	FutureTask<List<Integer>> futureTask2 = new FutureTask<List<Integer>>(new CallableTask(list,checkedList , mid, end));
        	 List<FutureTask<List<Integer>>> futureList = new ArrayList<FutureTask<List<Integer>>>();
        	//提交任务，添加返回
	       	exs.submit(futureTask1);
	       	exs.submit(futureTask2);
	       	futureList.add(futureTask1);
	       	futureList.add(futureTask2);
	        List<Integer> finalList =new ArrayList<>();
	        //结果归集
	         for (FutureTask<List<Integer>> future : futureList) {
	              while (true) {
	                  if (future.isDone()&& !future.isCancelled()) {
	                	  List<Integer> i = future.get();//Future特性
	                      System.out.println("i="+i+"获取到结果!"+new Date());
	                      finalList.addAll(i);
	                      break;
	                  }else {
	                      Thread.sleep(1);//避免CPU高速轮循，可以休息一下。
	                  }
	              }
	          }
	         return finalList;
        }
	}
	
	public static List<Integer> isCheck(List<Integer> list, List<Integer> checkedList,int start,int end) {
		Log.info("开始截取集合==========="+ start + "=======" + end);
		List<Integer> finalList =new ArrayList<>();
		List<Integer> subList = list.subList(start, end);
		for (Integer integer : subList) {
				for (Integer checked : checkedList) {
					if(integer.equals(checked)){
						finalList.add(checked);
					}
				}
			}
		return finalList;
	}
	
	
}
