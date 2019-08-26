package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author vic
 * @desc json util 
 */
public class JSONUtil {
	
	private static Gson gson = null; 
	
	static{
		gson  = new Gson();//todo yyyy-MM-dd HH:mm:ss 
	}
	
	public static synchronized Gson newInstance(){
		if(gson == null){
			gson =  new Gson();
		}
		return gson;
	}
	
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
	
	public static <T> T toBean(String json,Class<T> clz){
		
		return gson.fromJson(json, clz);
	}
	
	public static <T> Map<String, T> toMap(String json,Class<T> clz){
		 Map<String, JsonObject> map = gson.fromJson(json, new TypeToken<Map<String,JsonObject>>(){}.getType());
		 Map<String, T> result = new HashMap<>();
		 for(String key:map.keySet()){
			 result.put(key,gson.fromJson(map.get(key),clz) );
		 }
		 return result;
	}
	
	public static Map<String, Object> toMap(String json){
		 Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String,Object>>(){}.getType());
		 return map;
	}
	
	public static <T> List<T> toList(String json,Class<T> clz){
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();  
		List<T> list  = new ArrayList<>();
		for(final JsonElement elem : array){  
	         list.add(gson.fromJson(elem, clz));
	    }
	    return list;
	}
	
/*	public static void main(String[] args) {
	}
	*/
	public static void main(String[] args){  
        
        //假设返回的json字符串为 strJson  
        String strJson = "[{id:'001',name:'张三',age:'32'},{id:'002',name:'张四',age:'11'},{id:'003',name:'张五',age:'20'}]" ;  
//      String strJson = getURLContent("https://www.wikidata.org/w/api.php?action=wbsearchentities&search=Fudan&language=en&limit=20&format=json") ;  
        strJson="[" + strJson + "]" ;  
       /* System.out.println(strJson) ;  */
        //将字符串转换为JSONArray对象  
        try{  
            JSONArray jsonArray = JSONArray.fromObject(strJson) ;  
            if(jsonArray.size() > 0 ){  
                //遍历jsonArray数组，把每个对象转成json对象  
                for(int i = 0 ;i < jsonArray.size() ;i ++){  
                    JSONObject jsonObject = jsonArray.getJSONObject(i) ;  
                    System.out.println("d========"+jsonObject.get("strJson")) ;  
                }  
            }  
        }catch(Exception e){  
              
        }  
          
    } 
	
}
