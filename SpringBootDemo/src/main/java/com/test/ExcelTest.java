package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.util.ExportAbstractUtil;

public class ExcelTest  extends ExportAbstractUtil implements Runnable {

	private final HttpServletResponse response;
	
	private final   List<List<String>> lists;
	
	public ExcelTest(HttpServletResponse response,List<List<String>> lists) {
		this.response = response;
		this.lists = lists;
	}
	
	
	@Override
	public void run() {
	    //拼接数据end
        write(response,lists,"导出Excel.xls");
	}
	
	
	
	

}
