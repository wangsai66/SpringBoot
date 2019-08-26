package com.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class TaskMain {
	public static void main(String[] args) {
		//查找文档中的指定字
	      DocumentMock documentMock = new DocumentMock();
	      int numLines = 100;
	      int lineWordsNum = 1000;
	      String word = "the";
	      String[][] document = documentMock.generateDocument(numLines, lineWordsNum, word);
	      DocumentTask task = new DocumentTask(document, 0, numLines, word);

	      ForkJoinPool forkJoinPool = new ForkJoinPool();
	      forkJoinPool.execute(task);
	      do {	
	          System.out.printf("******************************************\n");
	          System.out.printf("Main: Parallelism: %d\n", forkJoinPool.getParallelism());
	          System.out.printf("Main: Active Threads: %d\n", forkJoinPool.getActiveThreadCount());
	          System.out.printf("Main: Task Count: %d\n", forkJoinPool.getQueuedTaskCount());
	          System.out.printf("Main: Steal Count: %d\n", forkJoinPool.getStealCount());
	          System.out.printf("******************************************\n");
	          try {
	            TimeUnit.SECONDS.sleep(1);
	          } catch (InterruptedException e) {
	              e.printStackTrace();
	          }
	      } while (!task.isDone());

	      forkJoinPool.shutdown();

	      if (task.isCompletedNormally()){
	    	    System.out.printf("Main: The process has completednormally.\n");
	      }
	      try {
	          System.out.printf("Main: The word appears %d in the document", task.get());
	      } catch (InterruptedException | ExecutionException e) {
	          e.printStackTrace();
	      }
	   }

}
