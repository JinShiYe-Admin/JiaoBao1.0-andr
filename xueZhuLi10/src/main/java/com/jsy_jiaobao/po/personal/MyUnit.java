package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 当前所在单位model
 */
public class MyUnit implements Serializable {
	private static final long serialVersionUID = -7098671533152539096L;
	private String UintName;// ": "金视野测试教育局",
	private int TabID;// ": 997,
	private String TabIDStr;// ": "OENFNDBBQjNBMzhCRjAwQg",
	private int flag;// ": 1

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

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
