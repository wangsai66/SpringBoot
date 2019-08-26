package com.Thred;



public class MyThread extends Thread{
	
	@Override
	public void run() {
		System.out.println(MyObject.getInstance().hashCode());
	}

}
