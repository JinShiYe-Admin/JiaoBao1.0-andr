package com.jsy_jiaobao.po.sys;

import java.util.List;

/**
 * 用户身份信息
 */
public class UserIdentity {
	/**1==教育局;2==老师;3==家长;4==学生;5==游客*/
	private int RoleIdentity;
	private String RoleIdName;
    private List<UserUnit> UserUnits;
	private List<UserClass> UserClasses;
	private int DefaultUnitId;
	public int getRoleIdentity() {
		return RoleIdentity;
	}
	public void setRoleIdentity(int roleIdentity) {
		RoleIdentity = roleIdentity;
	}
	public String getRoleIdName() {
		return RoleIdName;
	}
	public void setRoleIdName(String roleIdName) {
		RoleIdName = roleIdName;
	}
	public List<UserUnit> getUserUnits() {
		return UserUnits;
	}
	public void setUserUnits(List<UserUnit> userUnits) {
		UserUnits = userUnits;
	}
	public List<UserClass> getUserClasses() {
		return UserClasses;
	}
	public void setUserClasses(List<UserClass> userClasses) {
		UserClasses = userClasses;
	}
	public int getDefaultUnitId() {
		return DefaultUnitId;
	}
	public void setDefaultUnitId(int defaultUnitId) {
		DefaultUnitId = defaultUnitId;
	}
}