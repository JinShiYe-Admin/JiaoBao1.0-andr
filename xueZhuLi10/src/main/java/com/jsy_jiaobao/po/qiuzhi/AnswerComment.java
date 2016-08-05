package com.jsy_jiaobao.po.qiuzhi;

/** 答案的评论 */
public class AnswerComment {
	private String TabIDStr;// "RDZDODI1NzdENjdDMEZBMg",
	private int TabID;// 153,答案评论的Id
	private String Number;// "2楼",答案评论的楼层
	private int JiaoBaoHao;// 5233355,//评论者的教宝号
	private int CaiCount;// 0,//反对数
	private int LikeCount;// 0,//赞数
	private String WContent;// "好",//评论内容
	private String RecDate;// "2015-08-11T09:23:37",//评论时间
	private String RefIds;// null,//引用Id
	private String UserName;// "1601班"//用户名
	private boolean isAddScore = false;// 是否点赞/反对

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

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public int getCaiCount() {
		return CaiCount;
	}

	public void setCaiCount(int caiCount) {
		CaiCount = caiCount;
	}

	public int getLikeCount() {
		return LikeCount;
	}

	public void setLikeCount(int likeCount) {
		LikeCount = likeCount;
	}

	public String getWContent() {
		return WContent;
	}

	public void setWContent(String wContent) {
		WContent = wContent;
	}

	public String getRecDate() {
		return RecDate;
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
	}

	public String getRefIds() {
		return RefIds;
	}

	public void setRefIds(String refIds) {
		RefIds = refIds;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public boolean isAddScore() {
		return isAddScore;
	}

	public void setAddScore(boolean isAddScore) {
		this.isAddScore = isAddScore;
	}

}
