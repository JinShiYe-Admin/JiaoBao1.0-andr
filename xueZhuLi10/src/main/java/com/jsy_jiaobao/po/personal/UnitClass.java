package com.jsy_jiaobao.po.personal;

/**
 * 
 * 指定学校的所有班级基础数据Model
 */
public class UnitClass {
	private String TabIDStr;// {get;set;}
	// 班级ID 不允许空 数据类型int
	public int TabID;
	// 班级名称 长度40 数据类型nvarchar
	private String ClsName;
	// 班级代码 长度40 数据类型nvarchar
	private String ClsNo;
	// 创建者ID 不允许空 数据类型int
	public int AccountID;
	// 入学年份 数据类型int
	public int GradeYear;
	// 学校类型 长度20 数据类型nvarchar
	private String SchoolType;
	// 年级名称 长度20 数据类型nvarchar
	private String GradeName;
	// 学校ID  不允许空 数据类型int
	private int ParentID;
	// 0禁用,1正常 不允许空 数据类型tinyint
	private int State;
	// 文章总数 不允许空 数据类型int
	public int ArtCount;
	// 今日更新 不允许空 数据类型int
	public int ArtUpdate;

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

	public String getClsName() {
		return ClsName;
	}

	public void setClsName(String clsName) {
		ClsName = clsName;
	}

	public String getClsNo() {
		return ClsNo;
	}

	public void setClsNo(String clsNo) {
		ClsNo = clsNo;
	}

	public int getAccountID() {
		return AccountID;
	}

	public void setAccountID(int accountID) {
		AccountID = accountID;
	}

	public int getGradeYear() {
		return GradeYear;
	}

	public void setGradeYear(int gradeYear) {
		GradeYear = gradeYear;
	}

	public String getSchoolType() {
		return SchoolType;
	}

	public void setSchoolType(String schoolType) {
		SchoolType = schoolType;
	}

	public String getGradeName() {
		return GradeName;
	}

	public void setGradeName(String gradeName) {
		GradeName = gradeName;
	}

	public int getParentID() {
		return ParentID;
	}

	public void setParentID(int parentID) {
		ParentID = parentID;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public int getArtCount() {
		return ArtCount;
	}

	public void setArtCount(int artCount) {
		ArtCount = artCount;
	}

	public int getArtUpdate() {
		return ArtUpdate;
	}

	public void setArtUpdate(int artUpdate) {
		ArtUpdate = artUpdate;
	}

}
