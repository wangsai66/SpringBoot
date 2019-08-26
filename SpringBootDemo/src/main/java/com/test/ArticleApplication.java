package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/test")
public class ArticleApplication {
	
	
	@RequestMapping(value="/cvsTest", method = RequestMethod.GET)
    public void cvsTest(HttpServletResponse response){
		 //拼接数据start
        List<List<String>> datas = new ArrayList<List<String>>();
        String rows[] = {"year","month"};
        List<String> rowsTitle  = Arrays.asList(rows);
        datas.add(rowsTitle);
        for(int i = 0; i< 1000000 ;i++){
            String [] rowss = {"1","2"};
            List<String> rowssList = Arrays.asList(rowss);
            datas.add(rowssList);
        }
		/*List<Map<String, Object>> datas = new ArrayList<>();
	    Map<String, Object> map = null;
	    for (int i = 0; i < 1000000; i++) {
	    	   map = new HashMap<>();
	    	   map.put("no", i);
		       map.put("name", UUID.randomUUID().toString());
		       datas.add(map);
		}*/
		ExecutorService exec = Executors.newFixedThreadPool(1);
	    exec.execute(new ExcelTest(response,datas));
	    exec.shutdown();
		long start =System.currentTimeMillis();
	        while (true) {
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            System.out.println(CpuTets.getInstance().getProcessCpu());
	            if(exec.isTerminated()){
	            	 long end=System.currentTimeMillis();
	            	 long time = end-start;
	            	log.info("所用时间===="+time);
	            	break;
	            }
	        }
    }
}
