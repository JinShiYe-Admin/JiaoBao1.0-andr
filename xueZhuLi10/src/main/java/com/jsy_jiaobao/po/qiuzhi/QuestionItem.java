package com.jsy_jiaobao.po.qiuzhi;

/** 问题列表（首页） */
public class QuestionItem {
	private int TabID;// 问题ID
	private String Title;// 标题
	private String Abstracts;// 摘要
	private int ViewCount;// 浏览人数
	private String LastUpdate;// 更新时间
	private int AnswersCount;// 答案数量
	private String Thumbnail;// 缩略图地址

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

	public String getAbstracts() {
		return Abstracts;
	}

	public void setAbstracts(String abstracts) {
		Abstracts = abstracts;
	}

	public int getViewCount() {
		return ViewCount;
	}

	public void setViewCount(int viewCount) {
		ViewCount = viewCount;
	}

	public String getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}

	public int getAnswersCount() {
		return AnswersCount;
	}

	public void setAnswersCount(int answersCount) {
		AnswersCount = answersCount;
	}

	public String getThumbnail() {
		return Thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}

}
