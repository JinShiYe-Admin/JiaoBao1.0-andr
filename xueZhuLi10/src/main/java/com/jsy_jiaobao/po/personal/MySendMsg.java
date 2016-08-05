package com.jsy_jiaobao.po.personal;

/**
 * 我发出的信息
 */
public class MySendMsg {
	private String TabIDStr;
	//  内容 长度2000 不允许空 数据类型nvarchar
	private String MsgContent;
	//  发送时间 不允许空 数据类型smalldatetime
	private String RecDate;
	//  发送者AccID 不允许空 数据类型int
	private int JiaoBaoHao;
	private int FBCount; // 所有回复数量
	private int noReadCount; // 未读回复数量

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public String getMsgContent() {
		return MsgContent;
	}

	public void setMsgContent(String msgContent) {
		MsgContent = msgContent;
	}

	public String getRecDate() {
		return RecDate;
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
	}

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public int getFBCount() {
		return FBCount;
	}

	public void setFBCount(int fBCount) {
		FBCount = fBCount;
	}

	public int getNoReadCount() {
		return noReadCount;
	}

	public void setNoReadCount(int noReadCount) {
		this.noReadCount = noReadCount;
	}

}
