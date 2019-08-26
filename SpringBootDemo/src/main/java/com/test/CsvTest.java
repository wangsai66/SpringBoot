package com.test;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;
import org.springframework.web.bind.annotation.RequestMapping;

import com.util.CsvExportUtil;

@RequestMapping("/csv")
public class CsvTest  implements Runnable{
	
	private final HttpServletResponse response;
	private final List<Map<String, Object>> datas;
	public CsvTest(HttpServletResponse response,List<Map<String, Object>> datas) {
		this.response = response;
		this.datas  =datas;
	}

	@Override
	public void run() {
		test(response);
	}
	
	public void test (HttpServletResponse response){
	    // 构造导出数据结构
	    String titles = "组号,组名";  // 设置表头
	    String keys = "no,name";  // 设置每列字段
	    // 设置导出文件前缀
	    String fName = "data_";
	    // 文件导出
	    try {
	        OutputStream os = response.getOutputStream();
	        CsvExportUtil.responseSetProperties(fName, response);
	        CsvExportUtil.doExport(datas, titles, keys, os);
	        os.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	Log.info("csv导出失败====="+e);
	    }
	}

	
	
	
	

}
