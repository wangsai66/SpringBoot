package com.util;

import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

public final class YdpCode {

	
	/**CIS00014： 文件清理*/
	public static final String CIS00014  = "CIS00014";
	
	public static void main(String[] args) {
		 Long start = System.currentTimeMillis();
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
    	 List<Integer> check = isCheck(list,checkedList);
    	 Log.info("查出结果集为======"+ JSON.toJSONString(check));
    	 System.out.println("总耗时="+(System.currentTimeMillis()-start)+",取结果归集耗时="+(System.currentTimeMillis()-getResultStart));
    	 
	}
	
	
	public static List<Integer> isCheck(List<Integer> list, List<Integer> checkedList) {
		List<Integer> finalList =new ArrayList<>();
		for (Integer integer : list) {
				for (Integer checked : checkedList) {
					if(integer.equals(checked)){
						finalList.add(checked);
					}
				}
			}
		return finalList;
	}
}
