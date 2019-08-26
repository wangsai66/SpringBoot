package com.forkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * 创建任务类
 * 
 * RecursiveTask：
 * 	  用于有返回结果的任务

 * Description: 
 * All Rights Reserved.
 * @version 1.0  2018年9月21日 下午4:33:15  by 王赛(wangsai@zhihuihutong.com)
 */
public class DocumentTask extends RecursiveTask<Integer>{

	
	 private String document[][];
	 private int start;
	 private int end;
	 private String word;

	 public DocumentTask(String document[][], int start, int end, String word) {
	    this.document = document;
	    this.start = start;
	    this.end = end;
	    this.word = word;
	  }

	
	@Override
	protected Integer compute() {
		int result =0;
		 if (end - start < 10) {
		        try {
					result = processLines(document, start, end, word);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    } else {
	        int mid = (start + end) / 2;
	        DocumentTask task1 = new DocumentTask(document, start, mid, word);
	        DocumentTask task2 = new DocumentTask(document, mid, end, word);
	        invokeAll(task1, task2);
	        try {
	            result = task1.get() + task2.get();
	        } catch (InterruptedException | ExecutionException e) {
	            e.printStackTrace();
	        }
	     }
		 return result;
	}


	private int processLines(String[][] document2, int start2, int end2, String word2) throws InterruptedException {
		List<LineTask> tasks = new ArrayList<LineTask>();
	    for (int i = start; i < end; i++) {
	        LineTask task = new LineTask(document[i], 0, document[i].length, word);
	        tasks.add(task);
	    }
	    invokeAll(tasks);
	    int result = 0;
	    for (int i = 0; i < tasks.size(); i++) {
	        LineTask task = tasks.get(i);
	        try {
	        	 result = result + task.get();
			} catch (Exception e) {
				  e.printStackTrace();
			}
	    }
	    return result;
	}


	
}
