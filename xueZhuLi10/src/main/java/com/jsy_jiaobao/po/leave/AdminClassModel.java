package com.jsy_jiaobao.po.leave;

import java.io.Serializable;
//一些修改1 2016-5-6 MSL
//1.修改-将int ClsNo;//班级代码 改为 String ClsNo;//班级代码

/**
 * 班级信息model
 * 
 * @author
 * 
 */
public class AdminClassModel implements Serializable {
	private static final long serialVersionUID = -25131421541666001L;
	private int TabID;// 班级ID
	private String ClsName;// 班级名称
	private String ClsNo;// 班级代码
	private String GradeName;// 年级名称
	private String GradeYear;// 入学年份
	private int SchoolId;// 学校ID

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

	public String getGradeName() {
		return GradeName;
	}

	public void setGradeName(String gradeName) {
		GradeName = gradeName;
	}

	public String getGradeYear() {
		return GradeYear;
	}

	public void setGradeYear(String gradeYear) {
		GradeYear = gradeYear;
	}

	public int getSchoolId() {
		return SchoolId;
	}

	public void setSchoolId(int schoolId) {
		SchoolId = schoolId;
	}
}