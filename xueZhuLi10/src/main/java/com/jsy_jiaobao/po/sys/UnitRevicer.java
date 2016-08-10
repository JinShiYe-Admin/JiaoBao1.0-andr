package com.jsy_jiaobao.po.sys;

import java.util.List;

public class UnitRevicer {
	private String UnitName;//": "单位下级",
	private String TabIDStr;//": "单位下级",
    private List<GroupUserList> UserList;
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public List<GroupUserList> getUserList() {
		return UserList;
	}
	public void setUserList(List<GroupUserList> userList) {
		UserList = userList;
	}
	public String getTabIDStr() {
		return TabIDStr;
	}
	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}
}