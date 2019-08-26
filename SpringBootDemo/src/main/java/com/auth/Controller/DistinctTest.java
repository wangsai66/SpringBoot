package com.auth.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.auth.entity.StaggerOrder;

public class DistinctTest {
	
	public static void main(String[] args) {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
		/*	List<StaggerOrder> list =new ArrayList<>();
			List<StaggerOrder> list2 =new ArrayList<>();
			List<StaggerOrder> list3 =new ArrayList<>();
			StaggerOrder order =new StaggerOrder();
			order.setBeginTime(sdf.parse("2019-07-10 10:00:00"));
			order.setEndTime(sdf.parse("2019-07-10 12:00:00"));
			order.setOperateType(2);
			list.add(order);
			
			StaggerOrder order2 =new StaggerOrder();
			order.setBeginTime(sdf.parse("2019-07-01 01:00:00"));
			order.setEndTime(sdf.parse("2019-07-02 02:00:00"));
			order.setOperateType(2);
			list2.add(order2);
			
			StaggerOrder order3 =new StaggerOrder();
			order.setBeginTime(sdf.parse("2019-07-01 01:00:00"));
			order.setEndTime(sdf.parse("2019-07-03 02:00:00"));
			order.setOperateType(2);
			list.add(order3);
			list3.addAll(list2);
			list3.addAll(list);
			
			
			
			Log.info("2111====="+JSON.toJSONString(list3));
			List<StaggerOrder> uquenlist = removeDuplicate(list3);
			for (StaggerOrder staggerOrder : uquenlist) {
				System.out.println(staggerOrder.getOperateType());
			}
			*/
			
			/*Set<StaggerOrder> set = new HashSet<StaggerOrder>(); 
			set.addAll(list); 
			List<StaggerOrder> listnewList = new ArrayList<StaggerOrder>(set);
			for (StaggerOrder staggerOrder : listnewList) {
				System.out.println(JSON.toJSON(staggerOrder));
			}*/
			/*List<StaggerOrder> listnewList = new ArrayList<StaggerOrder>(set);
			for (StaggerOrder staggerOrder : listnewList) {
				
			}
			System.out.println(JSON.toJSONString(listnewList));*/
			/*List<StaggerOrder>  dislist = (List<StaggerOrder>) ReduceUtil.reduce(list, null);
			System.out.println(JSON.toJSONString(dislist));*/
			
		} catch (Exception e) {
			Log.info("转化错误"+e.getMessage());
		}
		
	}
	
	
	public static List removeDuplicate(List list){  
        List listTemp = new ArrayList();  
        for(int i=0;i<list.size();i++){  
            if(!listTemp.contains(list.get(i))){  
                listTemp.add(list.get(i));  
            }  
        }  
        return listTemp;  
    }  

}
