package com.jsy_jiaobao.po.qiuzhi;

import java.util.ArrayList;

import android.text.TextUtils;

public class MyAttQItem {
	private int TabID;// 问题ID
	private String Title;// 问题标题
	private int AnswersCount;// 回答数量
	private int AttCount;// 关注数量
	private int ViewCount;// 浏览量
	private String CategorySuject;// 话题
	private int CategoryId;// 话题ID
	private String LastUpdate;// 最后更新时间
	private int AreaCode;// 区域代码
	private int JiaoBaoHao;// 教宝号
	private int rowCount;// 记录数量
	private int State;//1=z正常,0=禁用
	private ArrayList<String> hiddenid;
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
		return TextUtils.isEmpty(CategorySuject)?"":CategorySuject;
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
	public int getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(int areaCode) {
		AreaCode = areaCode;
	}
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public ArrayList<String> getHiddenid() {
		return hiddenid;
	}
	public void setHiddenid(ArrayList<String> hiddenid) {
		this.hiddenid = hiddenid;
	}

}
