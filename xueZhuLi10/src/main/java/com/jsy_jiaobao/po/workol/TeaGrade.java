package com.jsy_jiaobao.po.workol;

/** 布置作业选择年级 */
public class TeaGrade {
	private String TabIDStr;// null,Id名称
	private int TabID;// Id 0,
	private int GradeCode;// 1年级代码
	private String GradeName = "请选择年级";// "七年级",年级名称
	private int isEnable;// 0
	private int orderby;// 0

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

	public int getGradeCode() {
		return GradeCode;
	}

	public void setGradeCode(int gradeCode) {
		GradeCode = gradeCode;
	}

	public String getGradeName() {
		return GradeName;
	}

	public void setGradeName(String gradeName) {
		GradeName = gradeName;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

}
