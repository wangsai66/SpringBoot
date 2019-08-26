package com.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;


public class ResultUtil {
	

	private static final long serialVersionUID = 6843846545434770135L;
	private boolean status;
	private String code;
	private String msg;
	private Map<String,Object> data;
	private long total;
	
	public String success(){
		return this.success(Constant.RESULT_SUCCESS,0);
	}
	
	public String success(String msg){
		return this.toJson(true, Constant.RESULT_SUCCESS, msg);
	}
	
	public String success(String code, Object... args){
		return this.toJson(true, code, LanguageUtil.getMsg(code,args));
	}
	
	public String error(){
		return this.error(Constant.RESULT_ERROR,0);
	}
	
	public String error(String msg){
		return this.toJson(false, Constant.RESULT_ERROR, msg);
	}
	
	public String failure(String... errors) {
		return error(errors[0]);
	}
	
	public String error(String code, Object... args){
		return this.toJson(false, code, LanguageUtil.getMsg(code,args));
	}
	
	
	public String toJson(boolean status,String code,String msg){
		this.status = status;
		this.code = code;
		this.msg = msg;
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormater()); 
		generate(jsonConfig);
		return JSONObject.fromObject(this,jsonConfig).toString();
	}
	
	/*
	 * 对象的属性为Integer类型属性时，如果属性值为null，则json化的字符串属性值为null
	 * */
	private void generate(JsonConfig jsonConfig){
		DefaultValueProcessor defaultValueProcessor=new DefaultValueProcessor() {
			@Override
			public Object getDefaultValue(Class arg0) {
				return null;
			}
		};
		jsonConfig.registerDefaultValueProcessor(String.class, defaultValueProcessor);
		jsonConfig.registerDefaultValueProcessor(Integer.class, defaultValueProcessor);
		jsonConfig.registerDefaultValueProcessor(Double.class, defaultValueProcessor);
		jsonConfig.registerDefaultValueProcessor(Float.class, defaultValueProcessor);
		jsonConfig.registerDefaultValueProcessor(List.class, defaultValueProcessor);
	}
	public String toJson(){
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormater());
		generate(jsonConfig);
		return JSONObject.fromObject(this,jsonConfig).toString();
	}
	
	public void put(String key, Object obj){
		if(data == null)
			data = new HashMap<String,Object>();
		data.put(key, obj);
	}
	public void putAll(Map<String,Object> map){
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			put(entry.getKey(),entry.getValue());
	    }
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
	

}
