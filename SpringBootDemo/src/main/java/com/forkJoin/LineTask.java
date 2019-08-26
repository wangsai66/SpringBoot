package com.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class LineTask extends RecursiveTask<Integer>{

	  private String line[];
	  private int start;
	  private int end;
	  private String word;
	
	  public LineTask(String[] line, int start, int end, String word) {
		  this.line = line;
		  this.start = start;
		  this.end = end;
		  this.word = word;
	  }
	  

    @Override
	protected Integer compute() {
    	int result = 0;
        if (end - start < 100) {
            result = count(line, start, end, word);
        } else {
            int mid = (start + end) / 2;
            LineTask task1 = new LineTask(line, start, mid, word);
            LineTask task2 = new LineTask(line, mid, end, word);
            invokeAll(task1, task2);
            try {
                result = task1.get() + task2.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
	}


	private int count(String[] line2, int start2, int end2, String word2) {
			int counter = 0;
		    for (int i = start; i < end; i++) {
		        if (line[i].equals(word)) {
		            counter++;
		        }
		    }
		    //为了显示demo的执行，令任务睡眠10毫秒。
		    try {
		        Thread.sleep(10);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		 return counter;
	}

}
