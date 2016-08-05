package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 单位接收人model
 */
public class CommMsgRevicerUnit implements Serializable{
	private static final long serialVersionUID = -2749974304422345903L;
	private String UintName;// 单位名称
	private int TabID;// 单位ID，取单位接收人用到
	private int flag;// 取单位接收人用到
	
	public String getUintName() {
		return UintName;
	}
	public void setUintName(String uintName) {
		UintName = uintName;
	}
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
