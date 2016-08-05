package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class AnswerDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1099905251935677564L;
	private int TabID;// 答案ID
	private int JiaoBaoHao;// 教宝号
	private int QId;// 问题ID
	private String RecDate;// 回答时间
	private int CCount;// 评论数量
	private int LikeCount;// 点赞数量
	private int Flag;// 0普通回答1求真回答
	private String Abstracts;// 摘要
	private String Thumbnail;// 缩略图地址
	private String IdFlag;// 回答者姓名+称号
	private String AContent;// 内容 "<p>44<br/></p>",
	private int Tag;// 0,
	private int CategoryId;// 67,
	private String ATitle;// "标题",
    private int AFlag;// 0,
    private int Anonymous;// 0,
    private int CaiCount;// 0,
    private String LikeList;// null,
    private int State;// 1,
    private int ReportCount;// 0
    private String DefaultImg;// "113/20150908154643.jpg",
    private String ReportList;// "5232580,",
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
	public int getQId() {
		return QId;
	}
	public void setQId(int qId) {
		QId = qId;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public int getCCount() {
		return CCount;
	}
	public void setCCount(int cCount) {
		CCount = cCount;
	}
	public int getLikeCount() {
		return LikeCount;
	}
	public void setLikeCount(int likeCount) {
		LikeCount = likeCount;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}
	public String getAbstracts() {
		return Abstracts;
	}
	public void setAbstracts(String abstracts) {
		Abstracts = abstracts;
	}
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getIdFlag() {
		return IdFlag;
	}
	public void setIdFlag(String idFlag) {
		IdFlag = idFlag;
	}
	public String getAContent() {
		return AContent;
	}
	public void setAContent(String aContent) {
		AContent = aContent;
	}
	public int getTag() {
		return Tag;
	}
	public void setTag(int tag) {
		Tag = tag;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public String getATitle() {
		return ATitle;
	}
	public void setATitle(String aTitle) {
		ATitle = aTitle;
	}
	public int getAFlag() {
		return AFlag;
	}
	public void setAFlag(int aFlag) {
		AFlag = aFlag;
	}
	public int getAnonymous() {
		return Anonymous;
	}
	public void setAnonymous(int anonymous) {
		Anonymous = anonymous;
	}
	public int getCaiCount() {
		return CaiCount;
	}
	public void setCaiCount(int caiCount) {
		CaiCount = caiCount;
	}
	public String getLikeList() {
		return LikeList;
	}
	public void setLikeList(String likeList) {
		LikeList = likeList;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public int getReportCount() {
		return ReportCount;
	}
	public void setReportCount(int reportCount) {
		ReportCount = reportCount;
	}
	public String getDefaultImg() {
		return DefaultImg;
	}
	public void setDefaultImg(String defaultImg) {
		DefaultImg = defaultImg;
	}
	public String getReportList() {
		return ReportList;
	}
	public void setReportList(String reportList) {
		ReportList = reportList;
	}

}
