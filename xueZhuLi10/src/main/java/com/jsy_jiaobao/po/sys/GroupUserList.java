package com.jsy_jiaobao.po.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单位接收人列表model
 */
public class GroupUserList implements Serializable {
	private static final long serialVersionUID = 7738097950833276361L;
	private String GroupName;// ": "基本人员",//分组名称
	private int MCount;// ": 1,//本组人数
	private ArrayList<Selit> groupselit_selit;// /需要把GroupSelit[]中的selit以seleit为name提交给api

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public int getMCount() {
		return MCount;
	}

	public void setMCount(int mCount) {
		MCount = mCount;
	}

	public ArrayList<Selit> getGroupselit_selit() {
		return groupselit_selit;
	}

	public void setGroupselit_selit(ArrayList<Selit> groupselit_selit) {
		this.groupselit_selit = groupselit_selit;
	}

}
