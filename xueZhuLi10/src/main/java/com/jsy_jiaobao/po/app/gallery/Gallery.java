package com.jsy_jiaobao.po.app.gallery;

import java.io.Serializable;

/**
 * 获取个人相册列表中的一个相册
 */
public class Gallery implements Serializable {
	private static final long serialVersionUID = -128871080375448362L;
	private String ID; // 相册Id
	private String GroupName;// 相册名

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

}
