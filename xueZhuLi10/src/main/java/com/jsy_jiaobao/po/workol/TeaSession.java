package com.jsy_jiaobao.po.workol;

/**
 * 选择章节
 */
public class TeaSession {
	private String TabIDStr;// "",
	private int TabID;// 1,
	private int subjectID;// 1,教版代码
	private int TVersionID;// 3,科目代码
	private int GradeID;// 1,年级代码
	private int Unid;// 418,单位Id
	private int Pid;// 0,
	private int chapterCode;// 1,章节代码
	private String chapterName = "没有章节";// "英语第一节（测试）",
	private String Remark;// null,
	private int isEnable;// 1,
	private int orderby;// 1,
	private int chapterCodePid;// 0

	private int Level;// 难度

	private boolean isVisible;// 是否可见
	private boolean isChildrenOpened;// 子菜单是否展开
	private boolean haveChild;// 是否有子菜单

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

	public int getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}

	public int getTVersionID() {
		return TVersionID;
	}

	public void setTVersionID(int tVersionID) {
		TVersionID = tVersionID;
	}

	public int getGradeID() {
		return GradeID;
	}

	public void setGradeID(int gradeID) {
		GradeID = gradeID;
	}

	public int getUnid() {
		return Unid;
	}

	public void setUnid(int unid) {
		Unid = unid;
	}

	public int getPid() {
		return Pid;
	}

	public void setPid(int pid) {
		Pid = pid;
	}

	public int getChapterCode() {
		return chapterCode;
	}

	public void setChapterCode(int chapterCode) {
		this.chapterCode = chapterCode;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
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

	public int getChapterCodePid() {
		return chapterCodePid;
	}

	public void setChapterCodePid(int chapterCodePid) {
		this.chapterCodePid = chapterCodePid;
	}

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isChildrenOpened() {
		return isChildrenOpened;
	}

	public void setChildrenOpened(boolean isChildrenOpened) {
		this.isChildrenOpened = isChildrenOpened;
	}

	public boolean isHaveChild() {
		return haveChild;
	}

	public void setHaveChild(boolean haveChild) {
		this.haveChild = haveChild;
	}
}
