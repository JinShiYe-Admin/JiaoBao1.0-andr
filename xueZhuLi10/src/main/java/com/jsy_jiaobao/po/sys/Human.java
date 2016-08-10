package com.jsy_jiaobao.po.sys;

import com.jsy.xuezhuli.utils.ConstantUrl;

import java.io.Serializable;

public class Human implements Serializable {
	private static final long serialVersionUID = -8351457881476907828L;
	public String UserID;// 用户Id
	public String UserName;// 用户名
	public String PhotoUrl;// 用户头像
	public int UserID2;

	public Human(String UserID, String UserName, String mainURL) {
		this.PhotoUrl = mainURL + ConstantUrl.photoURL + "?AccID=" + UserID;
		this.UserID = "jb_" + UserID;
		this.UserName = UserName;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPhotoUrl() {
		return PhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}

	public int getUserID2() {
		return UserID2;
	}

	public void setUserID2(int userID2) {
		UserID2 = userID2;
	}
}