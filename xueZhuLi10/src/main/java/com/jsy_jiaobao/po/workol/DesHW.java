package com.jsy_jiaobao.po.workol;

/** 
 * 自定义作业 
 */
public class DesHW {
	private String TabIDStr;// null,
	private int TabID;// 33,
	private int AccountID;// 0,
	private int jiaobaohao;// 教宝号,
	private int SubjectID;// 教版ID,
	private int GradeID;// 科目ID
	private int chapterID;// 章节ID
	private int VersionID;//,
	private int itemNumber;// 数量
	private String homeworkName;// "2015-8-13英语英语第一节作业",
	private String questionList;// 问题列表,
	private String CreateTime;// 创建时间,
	private int itemSelect;// 0,
	private int itemInput;// 0

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

	public int getAccountID() {
		return AccountID;
	}

	public void setAccountID(int accountID) {
		AccountID = accountID;
	}

	public int getJiaobaohao() {
		return jiaobaohao;
	}

	public void setJiaobaohao(int jiaobaohao) {
		this.jiaobaohao = jiaobaohao;
	}

	public int getSubjectID() {
		return SubjectID;
	}

	public void setSubjectID(int subjectID) {
		SubjectID = subjectID;
	}

	public int getGradeID() {
		return GradeID;
	}

	public void setGradeID(int gradeID) {
		GradeID = gradeID;
	}

	public int getChapterID() {
		return chapterID;
	}

	public void setChapterID(int chapterID) {
		this.chapterID = chapterID;
	}

	public int getVersionID() {
		return VersionID;
	}

	public void setVersionID(int versionID) {
		VersionID = versionID;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
	}

	public String getQuestionList() {
		return questionList;
	}

	public void setQuestionList(String questionList) {
		this.questionList = questionList;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public int getItemSelect() {
		return itemSelect;
	}

	public void setItemSelect(int itemSelect) {
		this.itemSelect = itemSelect;
	}

	public int getItemInput() {
		return itemInput;
	}

	public void setItemInput(int itemInput) {
		this.itemInput = itemInput;
	}

}
