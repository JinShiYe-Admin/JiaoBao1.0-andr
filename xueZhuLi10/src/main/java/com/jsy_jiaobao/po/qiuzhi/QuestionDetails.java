package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class QuestionDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2748644594152412821L;
	private int TabID;//问题ID
	private String Title;//  标题
	private String Abstracts;// 摘要
	private int ViewCount;// 浏览人数
	private String LastUpdate;// 更新时间
	private int AnswersCount;// 答案数量
	private String Thumbnail;// 缩略图地址
	private String KnContent;// 内容
	private String AreaCode;// 区域代码
	private String AtAccIds;// @回答人的教宝号
	private int Tag;//": 0,
    private String NickName;//": "1601班",
    private int JiaoBaoHao;//": 5233355,
    private int QFlag;//":QFlag 0=对回答无特殊要求，1=要求有证据的回答
    private String RecDate;//": "2015-08-13T09:10:48",
    private int CategoryId;//": 48,
    private String TagsList;//": "",
    private int State;//": 1,
    private int AttCount;//": 0,
    private int FactSign;//": 0
    private int MyAnswerId;//": 0
    
    private String Category;// "小学语文",
    
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
	public String getKnContent() {
		return KnContent;
	}
	public void setKnContent(String knContent) {
		KnContent = knContent;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public String getAtAccIds() {
		return AtAccIds;
	}
	public void setAtAccIds(String atAccIds) {
		AtAccIds = atAccIds;
	}
	public int getTag() {
		return Tag;
	}
	public void setTag(int tag) {
		Tag = tag;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public int getQFlag() {
		return QFlag;
	}
	public void setQFlag(int qFlag) {
		QFlag = qFlag;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public String getTagsList() {
		return TagsList;
	}
	public void setTagsList(String tagsList) {
		TagsList = tagsList;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public int getAttCount() {
		return AttCount;
	}
	public void setAttCount(int attCount) {
		AttCount = attCount;
	}
	public int getFactSign() {
		return FactSign;
	}
	public void setFactSign(int factSign) {
		FactSign = factSign;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public int getMyAnswerId() {
		return MyAnswerId;
	}
	public void setMyAnswerId(int myAnswerId) {
		MyAnswerId = myAnswerId;
	}

}
