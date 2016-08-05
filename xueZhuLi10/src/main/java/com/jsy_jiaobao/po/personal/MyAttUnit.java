package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

public class MyAttUnit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5567805992304991028L;
	
	private String CreateByjiaobaohao;//	Int	提供jiaobaohao 
	private String InterestUnitID;//	Int	我的关注单位ID
	private String CreateDatetime;//	Datetime	我创建关注的时间
	private String GroupID;//	Int	分组ID,，没有用到
	private String unitName;//	Int	分组ID,，没有用到
	private String isInUnit;//	Int	分组ID,，没有用到
	private String isAdmin;//	Int	分组ID,，没有用到
	private String unitType;//	Int	分组ID,，没有用到
	public String getCreateByjiaobaohao() {
		return CreateByjiaobaohao;
	}
	public void setCreateByjiaobaohao(String createByjiaobaohao) {
		CreateByjiaobaohao = createByjiaobaohao;
	}
	public String getInterestUnitID() {
		return InterestUnitID;
	}
	public void setInterestUnitID(String interestUnitID) {
		InterestUnitID = interestUnitID;
	}
	public String getCreateDatetime() {
		return CreateDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		CreateDatetime = createDatetime;
	}
	public String getGroupID() {
		return GroupID;
	}
	public void setGroupID(String groupID) {
		GroupID = groupID;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getIsInUnit() {
		return isInUnit;
	}
	public void setIsInUnit(String isInUnit) {
		this.isInUnit = isInUnit;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	
}
