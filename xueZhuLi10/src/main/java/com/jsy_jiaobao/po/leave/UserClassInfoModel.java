package com.jsy_jiaobao.po.leave;

import java.io.Serializable;

/**
 * 用户关联班级信息Model
 * 
 * @author admin
 * 
 */

public class UserClassInfoModel implements Serializable {

	private static final long serialVersionUID = -784545844741515L;
	private int ClassID;// int 班级ID,惟一标识
	private String ClassNo;// string 班级代码，用户自定义的
	private String ClassName;// string 班级名称
	private int GradeYear;// int 入学年份
	private String GradeName;// string 年级名称
	private int State;// int 状态，0禁用，1正常
	private int SchoolID;// int 学校ID

	public int getClassID() {
		return ClassID;
	}

	public void setClassID(int classID) {
		ClassID = classID;
	}

	public String getClassNo() {
		return ClassNo;
	}

	public void setClassNo(String classNo) {
		ClassNo = classNo;
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public int getGradeYear() {
		return GradeYear;
	}

	public void setGradeYear(int gradeYear) {
		GradeYear = gradeYear;
	}

	public String getGradeName() {
		return GradeName;
	}

	public void setGradeName(String gradeName) {
		GradeName = gradeName;
	}

	public int getState() {
		return State;
	}

	public void setState(int state) {
		State = state;
	}

	public int getSchoolID() {
		return SchoolID;
	}

	public void setSchoolID(int schoolID) {
		SchoolID = schoolID;
	}

}
