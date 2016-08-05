package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 
 * 文章详情Model
 */
public class ArthInfo implements Serializable {

	private static final long serialVersionUID = -198102440773131573L;
	// 文章加密ID
	private String TabIDStr;
	// 文章栏目ID
	private String SectionID;
	// 作者姓名
	private String UserName;
	// 标题 长度100 不允许空 数据类型nvarchar
	private String Title;
	// 内容 长度2147483646 数据类型ntext ,列表时不返回此属性
	private String Context;
	// 发布人 不允许空 数据类型int
	private int JiaoBaoHao;
	// 日期 不允许空 数据类型smalldatetime
	private String RecDate;//  
	// 观看人数 不允许空 数据类型int
	private int ViewCount;
	// 点击数量 不允许空 数据类型int
	private int ClickCount;
	// 赞数量 不允许空 数据类型int
	private int LikeCount;
	// 来源网站，客户端 不允许空 数据类型int
	private int Source;
	private String StarJson;
	private int State;
	private int FeeBackCount;
	private int Likeflag;

	private int TabID;// ": 345882,
	private String Abstracts;// 内容详情
	private String Thumbnail;// 图片

	private String UnitName;// ": "金视野"

	private int UnitType;// ":0,
	private int unitId;// ":997,
	private int unitClassID;// ":997,

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public String getSectionID() {
		return SectionID;
	}

	public void setSectionID(String sectionID) {
		SectionID = sectionID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContext() {
		return Context;
	}

	public void setContext(String context) {
		Context = context;
	}

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public int getViewCount() {
		return ViewCount;
	}

	public void setViewCount(int viewCount) {
		ViewCount = viewCount;
	}

	public String getRecDate() {
		try {
			return RecDate.split("\\.")[0].replace("T", " ");
		} catch (Exception e) {
			return RecDate;
		}
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
	}

	public int getClickCount() {
		return ClickCount;
	}

	public void setClickCount(int clickCount) {
		ClickCount = clickCount;
	}

	public int getLikeCount() {
		return LikeCount;
	}

	public void setLikeCount(int likeCount) {
		LikeCount = likeCount;
	}

	public int getSource() {
		return Source;
	}

	public void setSource(int source) {
		Source = source;
	}

	public String getStarJson() {
		return StarJson;
	}

	public void setStarJson(String starJson) {
		StarJson = starJson;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public int getFeeBackCount() {
		return FeeBackCount;
	}

	public void setFeeBackCount(int feeBackCount) {
		FeeBackCount = feeBackCount;
	}

	public int getLikeflag() {
		return Likeflag;
	}

	public void setLikeflag(int likeflag) {
		Likeflag = likeflag;
	}

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
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

	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	public int getUnitType() {
		return UnitType;
	}

	public void setUnitType(int unitType) {
		UnitType = unitType;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getUnitClassID() {
		return unitClassID;
	}

	public void setUnitClassID(int unitClassID) {
		this.unitClassID = unitClassID;
	}
}
