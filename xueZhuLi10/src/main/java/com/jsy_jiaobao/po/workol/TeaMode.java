package com.jsy_jiaobao.po.workol;

/**
 * 选择科目
 */

public class TeaMode {
	private int TabID;// ID科目Id
	private int VersionCode;// 教版名称
	private String VersionName;// 教版名称
	private int GradeCode;// 年级代码
	private String GradeName;// 年级名称
	private int subjectCode;// 科目代码
	private String subjectName = "没有科目";// 科目名称

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public int getVersionCode() {
		return VersionCode;
	}

	public void setVersionCode(int versionCode) {
		VersionCode = versionCode;
	}

	public String getVersionName() {
		return VersionName;
	}

	public void setVersionName(String versionName) {
		VersionName = versionName;
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

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
}
