package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;
/**回答列表中entity*/
public class AnswerItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6050311005336145283L;
	private int TabID;// 答案ID
	private int JiaoBaoHao;// 教宝号
	private int QId;//  问题ID
	private String RecDate;// 回答时间，
	private String ATitle;// 答案标题
	private int CCount;// 评论数量
	private int LikeCount;// 点赞数量
	private int CaiCount;// 反对数量
	private int Flag;//  0无内容1有内容2有证据
	private String Abstracts;// 摘要
	private String Thumbnail;// 缩略图地址
	private String IdFlag;// 回答者姓名
	private int AFlag;// 求真标志0=无内容，1=有内容，2=有证据
	private String AContent;// 
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
	public String getATitle() {
		return ATitle;
	}
	public void setATitle(String aTitle) {
		ATitle = aTitle;
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
	public int getCaiCount() {
		return CaiCount;
	}
	public void setCaiCount(int caiCount) {
		CaiCount = caiCount;
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
	public int getAFlag() {
		return AFlag;
	}
	public void setAFlag(int aFlag) {
		AFlag = aFlag;
	}
	public String getAContent() {
		return AContent;
	}
	public void setAContent(String aContent) {
		AContent = aContent;
	}

}
