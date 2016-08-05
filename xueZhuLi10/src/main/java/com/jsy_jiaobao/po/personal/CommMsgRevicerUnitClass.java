package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 班级数组，我所执教的班级，数组，如果是教育局，这个对象为null
 */
public class CommMsgRevicerUnitClass implements Serializable {
	private static final long serialVersionUID = -533322920148979037L;
	private String ClsName;// 单位名称
	private int TabID;// 单位ID，取单位接收人用到
	private int flag;// 取单位接收人用到
	private GenRevicer UserList;// 本单位下的接收人

	public String getClsName() {
		return ClsName;
	}

	public void setClsName(String clsName) {
		ClsName = clsName;
	}

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public GenRevicer getUserList() {
		return UserList;
	}

	public void setUserList(GenRevicer userList2) {
		UserList = userList2;
	}

}
