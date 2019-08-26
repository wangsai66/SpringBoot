package com.auth.service.impl;


import java.lang.reflect.Field;

import com.auth.service.Reducible;

public abstract class ReduceAbstract implements Reducible{
	
	private String[] typeArray;
	 
	@Override
	public void setType(String... type) {
		this.typeArray = type;
	}
 
	@Override
	public int hashCode() {
		int hash = 1;
		checkTypeArray();
		int size = typeArray.length;
		for (int i = 0; i < size; i++) {
			try {
				Field field = this.getClass().getDeclaredField(typeArray[i]);
				field.setAccessible(true);
				Object value = field.get(this);
				if(null != value){
					hash += value.hashCode();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hash;
	}
 
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		checkTypeArray();
		int size = typeArray.length;
		boolean result = true;
		for (int i = 0; i < size; i++) {
			try {
				Field field = this.getClass().getDeclaredField(typeArray[i]);
				field.setAccessible(true);
				Object subValue = field.get(this);
				Object otherValue = field.get(other);
				if ((subValue == null && otherValue != null) || (subValue != null && otherValue == null)) {
					result = false;
					break;
				}
				if (subValue != null && otherValue != null) {
					if (!subValue.equals(otherValue)) {
						result = false;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
 
	private void checkTypeArray() {
		if (typeArray == null || typeArray.length < 1 || typeArray[0].length() == 0) {
			// 如果不指定去重属性，则根据所有属性进行比对
			Field[] fields = this.getClass().getDeclaredFields();
			typeArray = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				typeArray[i] = fields[i].getName();
			}
		}

	}
}
