package com.jsy_jiaobao.po.sys;

/**
 * 短信直通车接收单位树数据源model
 */
public class SMSTreeUnitID {
	private int id;// ": 987;单位ID
	private int pId;// ": -1;父级单位ID
	private String name;// ": "测试教育局4";单位名称
	private int uType;// ": 1 ;单位类型，1教育局，2学校
	private String TabIDStr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getuType() {
		return uType;
	}

	public void setuType(int uType) {
		this.uType = uType;
	}

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}
}