package com.jsy_jiaobao.po.sys;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 我关联的 班级信息
 *
 */
@Table(name = "userclass") 
public class UserClass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 987207153246053610L;
	@Id
	private int id;
	@Column(column = "SchoolIDStr")
    private String SchoolIDStr;//": null,
	@Column(column = "TabIDStr")
    private String TabIDStr;//": null
	@Column(column = "ClassID")
	private int ClassID;
	@Column(column = "ClassNo")
	private String ClassNo;
	@Column(column = "ClassName")
	private String ClassName;
	@Column(column = "GradeYear")
	private int GradeYear;
	@Column(column = "GradeName")
    private String GradeName;
	@Column(column = "State")
    private int State;
	@Column(column = "SchoolID")
    private int SchoolID;
    private String SchoolName;
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
	public String getSchoolIDStr() {
		return SchoolIDStr;
	}
	public void setSchoolIDStr(String schoolIDStr) {
		SchoolIDStr = schoolIDStr;
	}
	public String getTabIDStr() {
		return TabIDStr;
	}
	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}
	public String getSchoolName() {
		return SchoolName;
	}
	public void setSchoolName(String schoolName) {
		SchoolName = schoolName;
	}
    
}
