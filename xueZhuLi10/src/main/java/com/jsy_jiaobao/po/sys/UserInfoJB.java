package com.jsy_jiaobao.po.sys;

import java.io.Serializable;

public class UserInfoJB implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4785203855303453619L;
	private String AccID;
	private int UserID;//": 21,
    private int UserType;//": 1,
    private String UserName;//;": "张叶青",
    private int UnitID;//": 994,
    private int isAdmin;//": 0
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getUserType() {
		return UserType;
	}
	public void setUserType(int userType) {
		UserType = userType;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public int getUnitID() {
		return UnitID;
	}
	public void setUnitID(int unitID) {
		UnitID = unitID;
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getAccID() {
		return AccID;
	}
	public void setAccID(String accID) {
		AccID = accID;
	}
    
}
