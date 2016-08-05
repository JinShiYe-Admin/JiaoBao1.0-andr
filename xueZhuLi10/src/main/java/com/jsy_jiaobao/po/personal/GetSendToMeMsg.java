package com.jsy_jiaobao.po.personal;

import java.util.ArrayList;

public class GetSendToMeMsg {
	private String LastID;
	private ArrayList<SendToMeMsg> CommMsgList;
	
	public String getLastID() {
		return LastID;
	}
	public void setLastID(String lastID) {
		LastID = lastID;
	}
	public ArrayList<SendToMeMsg> getCommMsgList() {
		return CommMsgList;
	}
	public void setCommMsgList(ArrayList<SendToMeMsg> commMsgList) {
		CommMsgList = commMsgList;
	}
	
}
