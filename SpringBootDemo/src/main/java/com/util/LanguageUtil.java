package com.util;


import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageUtil {

	
	private static Logger log = LoggerFactory.getLogger(LanguageUtil.class);

	public static ResourceBundle getBundle(Locale locale) {
		ResourceBundle bundle = null;
		try {
			if(locale == null)
				locale = Locale.CHINA;
			/*********** 根据语言环境，获得相应bundle **********************/
			bundle = ResourceBundle.getBundle("framework_lang", locale);
		} catch (Exception e) {
			log.error("国际化属性文件加载失败", e);
		}
		return bundle;
	}
	
	public static ResourceBundle getBundle(final String fileName,Locale locale){
		ResourceBundle bundle = null;
		if(StringUtils.isStrTrimNull(fileName)){
			return bundle;
		}
		try {
			if(locale == null){
				locale = Locale.getDefault();
			}
			bundle = ResourceBundle.getBundle(fileName, locale);
			
		} catch (Exception e) {
			log.error("resource file: "+fileName+" load fail", e);
		}
		return bundle;
	}

	public static String getMsg(Locale locale,String code,Object... args) {
		try{
			return MessageFormat.format(LanguageUtil.getBundle(locale).getString(code), args);
		}catch(Exception e){
			return code;
		}
	}
	
	public static String getMsg(String code,Object... args) {
		try{
			return MessageFormat.format(LanguageUtil.getBundle(null).getString(code), args);
		}catch(Exception e){
			return code;
		}
	}
	
	public static String getErr(String code,Object... args) {
		try{
			return MessageFormat.format(code+":"+LanguageUtil.getBundle(null).getString(code), args);
		}catch(Exception e){
			return code;
		}
	}
}
