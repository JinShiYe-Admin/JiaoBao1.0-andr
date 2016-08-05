package com.jsy_jiaobao.po.workol;

/**
 * 选择教版
 */
public class TeaSubject {
	private int TabID;// ": 418, 教版Id
	private int VersionCode;// ": 3,代码
	private String VersionName = "没有教版";// ": "人教新课标",
	private int GradeCode;// ": 0,年级代码
	private String GradeName;// ": null,//年级名称
	private int subjectCode;// ": 0,//科目代码
	private String subjectName;// ": null//科目名称

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
