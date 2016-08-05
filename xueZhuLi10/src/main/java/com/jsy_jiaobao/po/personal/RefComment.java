package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 回复一条
 */
public class RefComment implements Serializable {

	private static final long serialVersionUID = 6483021217004468391L;
	/** 加密ID */
	private String TabIDStr;// "NkYzNEM5NzIyMEU2RTQ0NA",
	/** 不加密的ID */
	private int TabID;// 1459125,
	/** 教宝号 */
	private int JiaoBaoHao;// 5150909,
	/** 踩的数量 */
	private int CaiCount;// 0,
	/** 赞的数量 */
	private int LikeCount;// 0,
	/** 内容 */
	private String Commnets;// "?",
	/** 评论者单位名称 */
	private String UnitShortname;// "金视野测试教育局",
	/** 日期 */
	private String RecDate;// "2015-01-16T15:24:00",
	/** 姓名 */
	private String UserName;// "郭瑞才"

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

	public String getCommnets() {
		return Commnets;
	}

	public void setCommnets(String commnets) {
		Commnets = commnets;
	}

	public String getUnitShortname() {
		return UnitShortname;
	}

	public void setUnitShortname(String unitShortname) {
		UnitShortname = unitShortname;
	}

	public String getRecDate() {
		return RecDate;
	}

	public void setRecDate(String recDate) {
		RecDate = recDate;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
