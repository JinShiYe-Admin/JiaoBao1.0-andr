package com.jsy_jiaobao.po.sys;

import java.io.Serializable;
/**
 * 学生信息
 */
public class StuInfo implements Serializable {
	private static final long serialVersionUID = 3651071895443971441L;
	public int StudentID;// ": 37073,
	public String StdName;// ": "陈国选",
	public String Sex;// ": " 男",
	public String SchoolType;// ": "小初连读",
	public int GradeYear;// ": 2010,
	public String GradeName;// ": "10级",
	public String ClassNo;// ": "1002",
	public String ClassName;// ": "1002班",
	public int UnitClassID;// ": 987,
	public int SchoolID;// ": 1100

	public int getStudentID() {
		return StudentID;
	}

	public void setStudentID(int studentID) {
		StudentID = studentID;
	}

	public String getStdName() {
		return StdName;
	}

	public void setStdName(String stdName) {
		StdName = stdName;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getSchoolType() {
		return SchoolType;
	}

	public void setSchoolType(String schoolType) {
		SchoolType = schoolType;
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

	public int getUnitClassID() {
		return UnitClassID;
	}

	public void setUnitClassID(int unitClassID) {
		UnitClassID = unitClassID;
	}

	public int getSchoolID() {
		return SchoolID;
	}

	public void setSchoolID(int schoolID) {
		SchoolID = schoolID;
	}
}