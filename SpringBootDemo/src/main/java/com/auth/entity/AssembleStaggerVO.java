package com.auth.entity;


import java.util.List;



public class AssembleStaggerVO extends SuperVO{
	// 订单ID
	private String orderId;
	
	private Integer ppsType; //订单类型0：错峰 1：包日 2：包周 3：包月 4：包年
	// 车牌号码
	private String plateNumber;
	// 车牌颜色
	private String plateColor;
	// 有效期，非连续时间片段集合
	private List<StaggerOrder> items;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getPpsType() {
		return ppsType;
	}
	public void setPpsType(Integer ppsType) {
		this.ppsType = ppsType;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}
	public List<StaggerOrder> getItems() {
		return items;
	}
	public void setItems(List<StaggerOrder> items) {
		this.items = items;
	}
	
	@Override
	public boolean equals(Object obj) {
		 super.equals(obj);
		 if(null == obj){
			 return false;
		 }
		 AssembleStaggerVO stagger=(AssembleStaggerVO) obj;
		 if(null == orderId){
			 if(null != stagger.getOrderId()){
				 return false;
			 }
		 }else if(null == plateNumber){
			 if(null != stagger.getPlateNumber()){
				 return false;
			 }
		 }
		 if(!this.getOrderId().equals(stagger.getOrderId()) && !this.getPlateNumber().equals(stagger.getPlateNumber())){
			 return false;
		 }
		 return true;
	}
	
	
}
