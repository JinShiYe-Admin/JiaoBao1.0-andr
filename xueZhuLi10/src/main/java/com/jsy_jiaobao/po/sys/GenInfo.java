package com.jsy_jiaobao.po.sys;

import java.io.Serializable;

/**
 * 家长
 */
public class GenInfo implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -5264017012553186770L;
	private int GenID;//	int	家长ID,惟一标识
	private int StudentID;//	int	学生ID
	private String StdName;//	string	学生姓名
	private int UnitClassID;//	int	班级ID
	private String ClassName;//	string	班级名称
	private int SchoolID;//	int	学校ID
	private int SrvFlag;//	int	服务状态0未开通1正式2试用3免费
    private int AccID;// 0
	public int getGenID() {
		return GenID;
	}
	public void setGenID(int genID) {
		GenID = genID;
	}
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
	public int getUnitClassID() {
		return UnitClassID;
	}
	public void setUnitClassID(int unitClassID) {
		UnitClassID = unitClassID;
	}
	public String getClassName() {
		return ClassName;
	}
	public void setClassName(String className) {
		ClassName = className;
	}
	public int getSchoolID() {
		return SchoolID;
	}
	public void setSchoolID(int schoolID) {
		SchoolID = schoolID;
	}
	public int getSrvFlag() {
		return SrvFlag;
	}
	public void setSrvFlag(int srvFlag) {
		SrvFlag = srvFlag;
	}
	public int getAccID() {
		return AccID;
	}
	public void setAccID(int accID) {
		AccID = accID;
	}

}
