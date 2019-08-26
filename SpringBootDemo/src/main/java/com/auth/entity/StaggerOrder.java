package com.auth.entity;


import java.io.Serializable;
import java.util.Date;

import com.aliyun.openservices.shade.com.alibaba.fastjson.annotation.JSONField;
import com.auth.service.impl.ReduceAbstract;


public class StaggerOrder extends ReduceAbstract  implements Serializable{

	/***
	 * 开始日期
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date beginTime;
	/**
	 * 结束日期
	 */
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/**
	 * 操作类型  0-新增 1-修改 2-取消 0为预留 
	 */
	private Integer operateType;
	
	
	public StaggerOrder(Date beginTime, Date endTime,Integer operateType) {
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.operateType=operateType;
	}

	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}


	
	
}
