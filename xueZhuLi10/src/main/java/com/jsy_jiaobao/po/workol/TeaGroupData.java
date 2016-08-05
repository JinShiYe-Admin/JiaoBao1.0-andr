package com.jsy_jiaobao.po.workol;

/**
 * TeaModel
 * 
 * @author admin
 * 
 */

public class TeaGroupData {

	private int type;// 类型
	private int currID;// 当前Id
	private int VersionCode; // 代码
	private String currName;// 当前名称
	private Object data;// 数据
	private int LongTime;// 时长

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCurrID() {
		return currID;
	}

	public void setCurrID(int currID) {
		this.currID = currID;
	}

	public String getCurrName() {
		return currName;
	}

	public void setCurrName(String currName) {
		this.currName = currName;
	}

	public int getVersionCode() {
		return VersionCode;
	}

	public void setVersionCode(int versionCode) {
		VersionCode = versionCode;
	}

	public int getLongTime() {
		return LongTime;
	}

	public void setLongTime(int longTime) {
		LongTime = longTime;
	}

}
