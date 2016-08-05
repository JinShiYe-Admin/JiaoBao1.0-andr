package com.jsy_jiaobao.po.qiuzhi;

public class ShowPicked {
	private int TabID;// 内容ID
	private String Title;// 内容标题
	private String PContent;// 内容
	private int QId;// 该精选内容对应的问题ID,通过此ID查看原文
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getPContent() {
		return PContent;
	}
	public void setPContent(String pContent) {
		PContent = pContent;
	}
	public int getQId() {
		return QId;
	}
	public void setQId(int qId) {
		QId = qId;
	}

}
