package com.auth.entity;

import java.io.Serializable;

public class BossAPPMonitorOverviewVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String category;// 类型 1MS工作站 2闸机 3票箱 4LED 5认证缴费机 6一体机7诱导屏8自助缴费机
	private Integer linkStatus;// 连接状态 0离线1在线
	private Integer onLinenum;// 在线数量
	private Integer offLinenum;// 离线数量
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getLinkStatus() {
		return linkStatus;
	}
	public void setLinkStatus(Integer linkStatus) {
		this.linkStatus = linkStatus;
	}
	public Integer getOnLinenum() {
		return onLinenum;
	}
	public void setOnLinenum(Integer onLinenum) {
		this.onLinenum = onLinenum;
	}
	public Integer getOffLinenum() {
		return offLinenum;
	}
	public void setOffLinenum(Integer offLinenum) {
		this.offLinenum = offLinenum;
	}

	
	
}
