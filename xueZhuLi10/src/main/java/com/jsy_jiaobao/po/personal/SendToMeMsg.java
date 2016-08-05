package com.jsy_jiaobao.po.personal;

/**
 * 发给我的信息
 */
public class SendToMeMsg {
	private String TabIDStr;
	private String MsgContent;//  内容 长度2000 不允许空 数据类型nvarchar
	private String RecDate;//  发送时间 不允许空 数据类型smalldatetime
	private int JiaoBaoHao;//  发送者AccID 不允许空 数据类型int
	private String UserName;
	private int NoReadCount;//  未读数量
	private int NoReplyCount;// 未回复数量
	private int ReadFlag;// 0未读，1已读未回复，2已回复

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

	public int getNoReadCount() {
		return NoReadCount;
	}

	public void setNoReadCount(int noReadCount) {
		NoReadCount = noReadCount;
	}

	public int getNoReplyCount() {
		return NoReplyCount;
	}

	public void setNoReplyCount(int noReplyCount) {
		NoReplyCount = noReplyCount;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	/**
	 * 0未读，1已读未回复，2已回复
	 */
	public int getReadFlag() {
		return ReadFlag;
	}

	public void setReadFlag(int readFlag) {
		ReadFlag = readFlag;
	}
}
