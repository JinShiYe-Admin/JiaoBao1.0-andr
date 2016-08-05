package com.jsy_jiaobao.po.sign;

public class ChildSignWay {
	private String TabID;
	private String GroupName;
	
	public ChildSignWay(String TabID,String GroupName){
		this.TabID = TabID;
		this.GroupName = GroupName;
	}
	public String getTabID() {
		return TabID;
	}
	public void setTabID(String tabID) {
		TabID = tabID;
	}
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	
}
