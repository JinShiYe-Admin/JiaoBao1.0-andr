package com.jsy_jiaobao.po.leave;

import java.io.Serializable;

/**
 * 假条时间段model
 * Created by Administrator on 2016/1/13.
 */
public class LeaveTime implements Serializable{	
	private static final long serialVersionUID = 1234329630766555480L;
	private int TabID;// 时间段记录ID
	private String Sdate;// 开始时间
	private String Edate;// 结束时间
	private String LeaveTime;// 离校时间
	private String LWriterName;// 门卫
	private String ComeTime;// 到校时间
	private String CWriterName;// 门卫
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getSdate() {
		return Sdate;
	}
	public void setSdate(String sdate) {
		Sdate = sdate;
	}
	public String getEdate() {
		return Edate;
	}
	public void setEdate(String edate) {
		Edate = edate;
	}
	public String getLeaveTime() {
		return LeaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		LeaveTime = leaveTime;
	}
	public String getLWriterName() {
		return LWriterName;
	}
	public void setLWriterName(String lWriterName) {
		LWriterName = lWriterName;
	}
	public String getComeTime() {
		return ComeTime;
	}
	public void setComeTime(String comeTime) {
		ComeTime = comeTime;
	}
	public String getCWriterName() {
		return CWriterName;
	}
	public void setCWriterName(String cWriterName) {
		CWriterName = cWriterName;
	}
	

}
