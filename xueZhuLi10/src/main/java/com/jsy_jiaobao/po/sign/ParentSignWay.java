package com.jsy_jiaobao.po.sign;

import java.util.List;

public class ParentSignWay {
	
	private String GroupID;
	private String GroupTypeName;
	private List<ChildSignWay> GroupItems;
	
	public ParentSignWay(String GroupID,String GroupTypeName){
		this.GroupID = GroupID;
		this.GroupTypeName = GroupTypeName;
	}
	public String getGroupTypeName() {
		return GroupTypeName;
	}
	public void setGroupTypeName(String groupTypeName) {
		GroupTypeName = groupTypeName;
	}
	public List<ChildSignWay> getGroupItems() {
		return GroupItems;
	}
	public void setGroupItems(List<ChildSignWay> groupItems) {
		GroupItems = groupItems;
	}
	public String getGroupID() {
		return GroupID;
	}
	public void setGroupID(String groupID) {
		GroupID = groupID;
	}
}
