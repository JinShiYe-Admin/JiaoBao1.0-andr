package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class RecommentIndexQuestion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4591961551656262940L;
	private int TabID;//
	private String Title;// 标题
	private int AnswersCount;// 回答数量
	private int AttCount;// 关注数量，
	private int ViewCount;// 浏览数量（打开看过明细的数量）
	private String CategorySuject;// 话题名称
	private int CategoryId;// 话题ID
	private String LastUpdate;// 动态更新日期
	private String AreaCode;// 区域代码
	private int JiaoBaoHao;//
	private String Thumbnail;// //图片url,字符串（url)json数组
	
    private String KnContent;// "<p><strong style=\"color: rgb(34, 34, 34); font-family: &#39;Helvetica Neue&#39;, Helvetica, Arial, sans-serif; font-size: 13px; line-height: 22.1000003814697px; white-space: normal;\"><a href=\"http://www.zhihu.com/roundtable/loneliness\" class=\"internal\" style=\"color: rgb(34, 85, 153); -webkit-tap-highlight-color: rgba(225, 225, 225, 0.498039);\">谁不孤独</a></strong></p>",
     
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
	public int getAnswersCount() {
		return AnswersCount;
	}
	public void setAnswersCount(int answersCount) {
		AnswersCount = answersCount;
	}
	public int getAttCount() {
		return AttCount;
	}
	public void setAttCount(int attCount) {
		AttCount = attCount;
	}
	public int getViewCount() {
		return ViewCount;
	}
	public void setViewCount(int viewCount) {
		ViewCount = viewCount;
	}
	public String getCategorySuject() {
		return CategorySuject;
	}
	public void setCategorySuject(String categorySuject) {
		CategorySuject = categorySuject;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public String getLastUpdate() {
		return LastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getKnContent() {
		return KnContent;
	}
	public void setKnContent(String knContent) {
		KnContent = knContent;
	}

}
