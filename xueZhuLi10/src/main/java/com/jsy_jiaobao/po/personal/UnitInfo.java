package com.jsy_jiaobao.po.personal;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 单位信息Model
 * 
 * @author admin
 * 
 */
@Table(name = "unitinfo")
public class UnitInfo {

	@Id
	public int id;
	@Column(column = "TabID")
	public int TabID;// //////////////////////////////////////////////
	// 加密ID 
	@Column(column = "TabIDStr")
	public String TabIDStr;// ////////////////////////////////////////////////
	// 单位名称 长度60 不允许空 数据类型nvarchar"UintName": "金视野总部",
	@Column(column = "UintName")
	public String UintName;// //////////////////////////////////////////////
	// 单位系统代码长度40 数据类型nvarchar "UnitCode": null,
	@Column(column = "UnitCode")
	public String UnitCode;
	// 单位简称 长度40 不允许空 数据类型nvarchar"ShortName": "金视野",
	@Column(column = "ShortName")
	public String ShortName;
	// 单位代码 不允许空 数据类型int"UnitNo": 220016,
	@Column(column = "UnitNo")
	public int UnitNo;
	// 教育局,学校,班级 不允许空 数据类型tinyint"UnitType": 1
	@Column(column = "UnitType")
	public int UnitType;// //////////////////////////////////////////////
	// 管理员账户ID 不允许空 数据类型int"AccountID": 5150027,
	@Column(column = "AccountID")
	public int AccountID;
	// 区域 代码 长度24 不允许空 数据类型nvarchar "CityCode": "370000",
	@Column(column = "CityCode")
	public String CityCode;
	// 乡镇 长度40 数据类型nvarchar "TownShip": null,
	@Column(column = "TownShip")
	public String TownShip;
	// 上级单位ID,多个以,分隔 长度200 不允许空 数据类型nvarchar"ParentID": "0",
	@Column(column = "ParentID")
	public String ParentID;// //////////////////////////////////////////////
	// 0禁用,1正常 不允许空 数据类型tinyint"State": 1,
	@Column(column = "State")
	public int State;
	// 文章总数 不允许空 数据类型int"ArtCount": 23,
	@Column(column = "ArtCount")
	public int ArtCount;// //////////////////////////////////////////////
	// 今日更新 不允许空 数据类型int"ArtUpdate": 23,
	@Column(column = "ArtUpdate")
	public int ArtUpdate;// //////////////////////////////////////////////
	// 教育系统隶属ID 不允许空 数据类型int "EduParentID": -1,
	@Column(column = "EduParentID")
	public int EduParentID;

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public String getUintName() {
		return UintName;
	}

	public void setUintName(String uintName) {
		UintName = uintName;
	}

	public String getUnitCode() {
		return UnitCode;
	}

	public void setUnitCode(String unitCode) {
		UnitCode = unitCode;
	}

	public String getShortName() {
		return ShortName;
	}

	public void setShortName(String shortName) {
		ShortName = shortName;
	}

	public int getUnitNo() {
		return UnitNo;
	}

	public void setUnitNo(int unitNo) {
		UnitNo = unitNo;
	}

	public int getUnitType() {
		return UnitType;
	}

	public void setUnitType(int unitType) {
		UnitType = unitType;
	}

	public int getAccountID() {
		return AccountID;
	}

	public void setAccountID(int accountID) {
		AccountID = accountID;
	}

	public String getCityCode() {
		return CityCode;
	}

	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}

	public String getTownShip() {
		return TownShip;
	}

	public void setTownShip(String townShip) {
		TownShip = townShip;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public int getArtCount() {
		return ArtCount;
	}

	public void setArtCount(int artCount) {
		ArtCount = artCount;
	}

	public int getArtUpdate() {
		return ArtUpdate;
	}

	public void setArtUpdate(int artUpdate) {
		ArtUpdate = artUpdate;
	}

	public int getEduParentID() {
		return EduParentID;
	}

	public void setEduParentID(int eduParentID) {
		EduParentID = eduParentID;
	}

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
