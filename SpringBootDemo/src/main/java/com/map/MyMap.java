package com.map;

public interface MyMap<K,V> {

	int size();  //大小
	
	Boolean isEmpty();
	
	Object get (Object  obj);
	
	  //添加元素
    Object put(Object key,Object value);
    
    interface Entry<k,v>{
        k getkey();
        v getValue();
    }
	
}
