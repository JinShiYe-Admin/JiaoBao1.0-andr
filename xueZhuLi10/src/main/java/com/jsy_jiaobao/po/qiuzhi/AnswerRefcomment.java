package com.jsy_jiaobao.po.qiuzhi;
/**答案评论的评论*/
public class AnswerRefcomment {
	private String TabIDStr;
	private int TabID;// 评论ID
	private int JiaoBaoHao;// 教宝号
	private String RecDate;// 回答时间
	private int CaiCount;// 踩的数量
	private int LikeCount;// 点赞数量
	private String WContent;// 内容
	private String UserName;// 评论者

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

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public String getRecDate() {
		return RecDate;
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
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

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
