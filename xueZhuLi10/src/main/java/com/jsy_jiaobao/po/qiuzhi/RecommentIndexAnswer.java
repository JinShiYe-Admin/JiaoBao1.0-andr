package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class RecommentIndexAnswer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 898389518157491310L;
	private String ATitle;// 标题
	private String Abstracts;// 内容摘要
	private int AFlag;// 求真标志
	private int TabID;// 记录ID
	private String RecDate;// 日期
	private int LikeCount;// 支持数量
	private int CaiCount;// 反对数量
	private int JiaoBaoHao;//
	private String IdFlag;// //用户昵称或姓名，匿名为空字符串
	private int Flag;//0=无，1=有内容，2=有证据
	private String Thumbnail;// //图片url,字符串（url)json数组
	private int CCount;// 评论数量
    private String AContent;// 
	public String getATitle() {
		return ATitle;
	}
	public void setATitle(String aTitle) {
		ATitle = aTitle;
	}
	public String getAbstracts() {
		return Abstracts;
	}
	public void setAbstracts(String abstracts) {
		Abstracts = abstracts;
	}
	public int getAFlag() {
		return AFlag;
	}
	public void setAFlag(int aFlag) {
		AFlag = aFlag;
	}
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
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
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public String getIdFlag() {
		return IdFlag;
	}
	public void setIdFlag(String idFlag) {
		IdFlag = idFlag;
	}
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public int getCCount() {
		return CCount;
	}
	public void setCCount(int cCount) {
		CCount = cCount;
	}
	public String getAContent() {
		return AContent;
	}
	public void setAContent(String aContent) {
		AContent = aContent;
	}
	public int getFlag() {
		return Flag;
	}
	public void setFlag(int flag) {
		Flag = flag;
	}

}
