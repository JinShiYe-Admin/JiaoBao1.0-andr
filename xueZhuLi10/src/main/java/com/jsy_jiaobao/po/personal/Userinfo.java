package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author admin
 * 
 */
public class Userinfo implements Serializable {

	private static final long serialVersionUID = -2610083668307798852L;
	private int isAdmin;// 是否管理员
	private int UserID; // 用户ID
	private String UserName;// 用户姓名
	private int AccID;// 账户ID
	private String GroupFlag; // 分组ID,多个以英文，分开。

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public int getAccID() {
		return AccID;
	}

	public void setAccID(int accID) {
		AccID = accID;
	}

	public String getGroupFlag() {
		return GroupFlag;
	}

	public void setGroupFlag(String groupFlag) {
		GroupFlag = groupFlag;
	}

}
