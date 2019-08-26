package com.util;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.DateMorpherEx;
import com.util.JsonDateFormater;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

public class TransformForYdp<T> implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	ydpCode;
	private java.lang.Object  Data;
	private String params;
	

	public static TransformForYdp fromJson(String json, Class clazz) {
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormater()); 
		generate(jsonConfig);
		Map<String,Class> map = new HashMap<String,Class>();
		map.put("data", clazz);
		String[] dateFormats = new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","HH:mm:ss"};  
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpherEx(dateFormats,(Date) null)); 
		return (TransformForYdp)JSONObject.toBean(JSONObject.fromObject(json,jsonConfig),TransformForYdp.class,map);
	}
	
	public static TransformForYdp fromJson(String json,Map<String,Class> map){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateFormater()); 
		generate(jsonConfig);
		String[] dateFormats = new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","HH:mm:ss"};  
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpherEx(dateFormats,(Date) null)); 
		return (TransformForYdp)JSONObject.toBean(JSONObject.fromObject(json,jsonConfig),TransformForYdp.class,map);
	}

	/*
	 * 对象的属性为Integer类型属性时，如果属性值为null，则json化的字符串属性值为null
	 * */
	private static void generate(JsonConfig jsonConfig){
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
		jsonConfig.registerDefaultValueProcessor(Date.class, defaultValueProcessor);
	}

	public String getYdpCode() {
		return ydpCode;
	}

	public void setYdpCode(String ydpCode) {
		this.ydpCode = ydpCode;
	}

	

	

	public java.lang.Object getData() {
		return Data;
	}

	public void setData(java.lang.Object data) {
		Data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	
}
