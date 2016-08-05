package com.jsy_jiaobao.po.personal;

/**
 * 最新更新文章单位信息Model
 * 
 * @author admin
 * 
 */
public class UpdataUnit {
	private String TabIDStr;// 加密的单位ID
	private String ClassIDStr; // 加密的班级ID
	private int UnitID;// 加密的单位ID
	private int UnitType;// 加密的单位ID
	private String UintName; // 单位名称
	private int UnitClassID;// 加密的班级ID
	private String ClsName;// 班级名称
	private String pubDate;// 最后发布时间 
	private String Title; // 最后发布的文章标题

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public String getClassIDStr() {
		return ClassIDStr;
	}

	public void setClassIDStr(String classIDStr) {
		ClassIDStr = classIDStr;
	}

	public int getUnitID() {
		return UnitID;
	}

	public void setUnitID(int unitID) {
		UnitID = unitID;
	}

	public String getUintName() {
		return UintName;
	}

	public void setUintName(String uintName) {
		UintName = uintName;
	}

	public int getUnitClassID() {
		return UnitClassID;
	}

	public void setUnitClassID(int unitClassID) {
		UnitClassID = unitClassID;
	}

	public String getClsName() {
		return ClsName;
	}

	public void setClsName(String clsName) {
		ClsName = clsName;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public int getUnitType() {
		return UnitType;
	}

	public void setUnitType(int unitType) {
		UnitType = unitType;
	}

}
