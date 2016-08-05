package com.jsy_jiaobao.po.qiuzhi;
/**我的评论*/
public class MyComms {
	private int TabID;// 评论ID
	private String WContent;// 内容
	private int QId;// 所属问题ID
	private int AId;// 所属答案ID
	private int LikeCount;// 支持数
	private int CaiCount;// 反对数
	private String RecDate;// 最后更新时间
	private int rowCount;// 记录数量
//	private int State;//1=z正常,0=禁用
	private AnswerDetails answerDetails;
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getWContent() {
		return WContent;
	}
	public void setWContent(String wContent) {
		WContent = wContent;
	}
	public int getQId() {
		return QId;
	}
	public void setQId(int qId) {
		QId = qId;
	}
	public int getAId() {
		return AId;
	}
	public void setAId(int aId) {
		AId = aId;
	}
	public int getLikeCount() {
		return LikeCount;
	}
	public void setLikeCount(int likeCount) {
		LikeCount = likeCount;
	}
	public int getCaiCount() {
		return CaiCount;
	}
	public void setCaiCount(int caiCount) {
		CaiCount = caiCount;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public AnswerDetails getAnswerDetails() {
		return answerDetails;
	}
	public void setAnswerDetails(AnswerDetails answerDetails) {
		this.answerDetails = answerDetails;
	}
//	public int getState() {
//		return State;
//	}
//	public void setState(int state) {
//		State = state;
//	}

}
