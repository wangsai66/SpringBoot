package com.auth.entity;

import java.util.Date;

public class FileVo {
	
	private String plNumber; //车牌号
	private String car;  //车主
	private String color; //yanse 
	private Date date;
	private String operator;
	private Date createDate;
	private String tishi;
	private String isru; //是否允许
	private String remarks;
	
	
	public String getPlNumber() {
		return plNumber;
	}
	public void setPlNumber(String plNumber) {
		this.plNumber = plNumber;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getTishi() {
		return tishi;
	}
	public void setTishi(String tishi) {
		this.tishi = tishi;
	}
	public String getIsru() {
		return isru;
	}
	public void setIsru(String isru) {
		this.isru = isru;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
