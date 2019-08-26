package com.Thred;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveTask;

import org.mortbay.log.Log;

public class ForkTask extends RecursiveTask<List<Integer>>{
	
	private List<Integer> list;
	private List<Integer> checkedList;
	private int start;
	private int end;
	
	
	public ForkTask(List<Integer> list, List<Integer> checkedList,int start,int end) {
		super();
		this.list =list;
		this.checkedList =checkedList;
		this.start =start;
		this.end =end;
	}


	@Override
	protected List<Integer> compute() {
		if (end - start < 500) {
			  return  isCheck(list,checkedList,start,end);
	        } else {
	        	List<ForkTask> tasks =new ArrayList<>();
	        	int mid = (start + end) / 2;
	        	ForkTask task1 =new ForkTask(list, checkedList,start, mid);
	        	ForkTask task2 =new ForkTask(list,checkedList , mid, end);
	        	invokeAll(task1,task2);
	        	task1.fork();
	        	task2.fork();
	        	task1.join();
	        	task2.join();
	        	List<Integer> finalList = new ArrayList<>();
	        	try {
	        		finalList.addAll(task1.get());
	        		finalList.addAll(task2.get());
				} catch (Exception e) {
					
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
