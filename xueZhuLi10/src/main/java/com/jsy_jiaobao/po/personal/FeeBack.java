package com.jsy_jiaobao.po.personal;
/**
 * 注释不是你想加，想加就能加。(ノಠ益ಠ)ノ彡┻━┻。--ShangLin Mo
 */

/**
 *  回复内容
 */
public class FeeBack {

    private String TabIDStr;// "MzExQkMwQzk0Qzk1NjQzQg",//当前行数据的ID
    private String MsgTabIDStr;// "MzExQkMwQzk0Qzk1NjQzQg",//点击回复我的信息时用
    private int TabID;// 174844,
    private int Jiaobaohao;// 5150062,//教宝号
    private int MsgID;// 57359,
    private String FeeBackMsg;// "收到",//回复我的
    private String RecDate;// "2014-08-16T11:12:56",//时间
    private int FeeBackNo;// 1,
    private int State;// 1,
    private int ReadFlag;// 0,
    private String UnitShortName;// null,//单位名简称
    private String UserName;// null,//名称
    private String MsgContent;// null,//内容
    private String MsgRecDate;// "2014-08-13T11:39:57",
    private String ReaderList;// null
	public String getTabIDStr() {
		return TabIDStr;
	}
	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public int getJiaobaohao() {
		return Jiaobaohao;
	}
	public void setJiaobaohao(int jiaobaohao) {
		Jiaobaohao = jiaobaohao;
	}
	public int getMsgID() {
		return MsgID;
	}
	public void setMsgID(int msgID) {
		MsgID = msgID;
	}
	public String getFeeBackMsg() {
		return FeeBackMsg;
	}
	public void setFeeBackMsg(String feeBackMsg) {
		FeeBackMsg = feeBackMsg;
	}
	public String getRecDate() {
		return RecDate.replace("T", " ");
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public int getFeeBackNo() {
		return FeeBackNo;
	}
	public void setFeeBackNo(int feeBackNo) {
		FeeBackNo = feeBackNo;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public int getReadFlag() {
		return ReadFlag;
	}
	public void setReadFlag(int readFlag) {
		ReadFlag = readFlag;
	}
	public String getUnitShortName() {
		return UnitShortName;
	}
	public void setUnitShortName(String unitShortName) {
		UnitShortName = unitShortName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getMsgContent() {
		return MsgContent;
	}
	public void setMsgContent(String msgContent) {
		MsgContent = msgContent;
	}
	public String getMsgRecDate() {
		return MsgRecDate;
	}
	public void setMsgRecDate(String msgRecDate) {
		MsgRecDate = msgRecDate;
	}
	public String getReaderList() {
		return ReaderList;
	}
	public void setReaderList(String readerList) {
		ReaderList = readerList;
	}
	public String getMsgTabIDStr() {
		return MsgTabIDStr;
	}
	public void setMsgTabIDStr(String msgTabIDStr) {
		MsgTabIDStr = msgTabIDStr;
	}
    
}
