package com.jsy_jiaobao.po.sys;

import java.io.Serializable;
import java.util.List;

public class UnitAdminRevicer implements Serializable{
	private static final long serialVersionUID = -1104437026906709327L;
	private String groupName;//": "初中",
	private int UnitType;//": 2,
	private List<Selit> UserList;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getUnitType() {
		return UnitType;
	}
	public void setUnitType(int unitType) {
		UnitType = unitType;
	}
	public List<Selit> getUserList() {
		return UserList;
	}
	public void setUserList(List<Selit> userList) {
		UserList = userList;
	}
}