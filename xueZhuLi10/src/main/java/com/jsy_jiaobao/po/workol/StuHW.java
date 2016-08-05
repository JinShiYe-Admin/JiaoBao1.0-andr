package com.jsy_jiaobao.po.workol;

/**
 * 学生作业
 */
public class StuHW {
	private String TabIDStr = "0";
	private int TabID;//学生作业Id
	private int HWID;//作业Id
	private int studentLevel;// 学生水平
	private int jiaobaohao;// 教宝号
	private int AccountID;// 0账号Id
	private int isHWFinish;// 作业是否完成
	private int isAdFinish;// 0,
	private String HWStartTime;//作业开始时间 "1970-01-01T00:00:00",
	private String HWEndTime;//作业结束时间 null,
	private int useLongtime;// 作业用时0,
	private int HWScore;// 分数0,
	private int EduLevel;// 水平0,
	private String AddStartTime;// null,
	private String AddEndTime;// null,
	private int CheckNum;// 0,
	private String CheckTeacher;//审核老师 null,
	private int AddScore;// 加分0,
	private int CheckResultJBH;//加载结果的教宝号 null,
	private int SubjectID;//教版Id -1,
	private int GradeID;//年级Id -1,
	private int classID;//班级Id -1,
	private String className;//班级名称 null,
	private int chapterID;// 章节Id-1,
	private int VersionID;// 版本Id0,
	private int modulusGrade;// 0,
	private int itemNumber;// 10,
	private String distribution;//各种类型与题目数量 "1:5,2:5",
	private int LongTime;// 市场,
	private String EXPIRYDATE;// "2015-11-11T23:00:00",
	private int EXPIRYINT;// 0,
	private String CreateDate;// 创建日期,
	private String homeworkName;//作业名称 "2015-11-11语文第一单元作业",
	private int jiaobaohao1;// 0,
	private int AccountID1;// 0,
	private int isEnable;// 0,
	private String schoolName;// 学校名称,
	private int isHaveAdd;// 1,
	private int isSelf;// 是否自己-1,
	private String subjectName;//教版名称 null,
	private String questionList;//问题列表 null,
	private String chapterName;//章节名称 null,
	private String Additional;// null,
	private String AdditionalDes;// null,
	private String AdditionalAnswer;// null,
	private String CheckResult;// null,
	private int isType;// 类型

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		if (tabIDStr == null) {
			TabIDStr = "0";
		} else {
			TabIDStr = tabIDStr;
		}
	}

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public int getHWID() {
		return HWID;
	}

	public void setHWID(int hWID) {
		HWID = hWID;
	}

	public int getStudentLevel() {
		return studentLevel;
	}

	public void setStudentLevel(int studentLevel) {
		this.studentLevel = studentLevel;
	}

	public int getJiaobaohao() {
		return jiaobaohao;
	}

	public void setJiaobaohao(int jiaobaohao) {
		this.jiaobaohao = jiaobaohao;
	}

	public int getAccountID() {
		return AccountID;
	}

	public void setAccountID(int accountID) {
		AccountID = accountID;
	}

	public int getIsHWFinish() {
		return isHWFinish;
	}

	public void setIsHWFinish(int isHWFinish) {
		this.isHWFinish = isHWFinish;
	}

	public int getIsAdFinish() {
		return isAdFinish;
	}

	public void setIsAdFinish(int isAdFinish) {
		this.isAdFinish = isAdFinish;
	}

	public String getHWStartTime() {
		return HWStartTime;
	}

	public void setHWStartTime(String hWStartTime) {
		HWStartTime = hWStartTime;
	}

	public String getHWEndTime() {
		return HWEndTime;
	}

	public void setHWEndTime(String hWEndTime) {
		HWEndTime = hWEndTime;
	}

	public int getUseLongtime() {
		return useLongtime;
	}

	public void setUseLongtime(int useLongtime) {
		this.useLongtime = useLongtime;
	}

	public int getHWScore() {
		return HWScore;
	}

	public void setHWScore(int hWScore) {
		HWScore = hWScore;
	}

	public int getEduLevel() {
		return EduLevel;
	}

	public void setEduLevel(int eduLevel) {
		EduLevel = eduLevel;
	}

	public String getAddStartTime() {
		return AddStartTime;
	}

	public void setAddStartTime(String addStartTime) {
		AddStartTime = addStartTime;
	}

	public String getAddEndTime() {
		return AddEndTime;
	}

	public void setAddEndTime(String addEndTime) {
		AddEndTime = addEndTime;
	}

	public int getCheckNum() {
		return CheckNum;
	}

	public void setCheckNum(int checkNum) {
		CheckNum = checkNum;
	}

	public String getCheckTeacher() {
		return CheckTeacher;
	}

	public void setCheckTeacher(String checkTeacher) {
		CheckTeacher = checkTeacher;
	}

	public int getAddScore() {
		return AddScore;
	}

	public void setAddScore(int addScore) {
		AddScore = addScore;
	}

	public int getCheckResultJBH() {
		return CheckResultJBH;
	}

	public void setCheckResultJBH(int checkResultJBH) {
		CheckResultJBH = checkResultJBH;
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

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public int getModulusGrade() {
		return modulusGrade;
	}

	public void setModulusGrade(int modulusGrade) {
		this.modulusGrade = modulusGrade;
	}

	public int getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public int getLongTime() {
		return LongTime;
	}

	public void setLongTime(int longTime) {
		LongTime = longTime;
	}

	public String getEXPIRYDATE() {
		return EXPIRYDATE;
	}

	public void setEXPIRYDATE(String eXPIRYDATE) {
		EXPIRYDATE = eXPIRYDATE;
	}

	public int getEXPIRYINT() {
		return EXPIRYINT;
	}

	public void setEXPIRYINT(int eXPIRYINT) {
		EXPIRYINT = eXPIRYINT;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
	}

	public int getJiaobaohao1() {
		return jiaobaohao1;
	}

	public void setJiaobaohao1(int jiaobaohao1) {
		this.jiaobaohao1 = jiaobaohao1;
	}

	public int getAccountID1() {
		return AccountID1;
	}

	public void setAccountID1(int accountID1) {
		AccountID1 = accountID1;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getIsHaveAdd() {
		return isHaveAdd;
	}

	public void setIsHaveAdd(int isHaveAdd) {
		this.isHaveAdd = isHaveAdd;
	}

	public int getIsSelf() {
		return isSelf;
	}

	public void setIsSelf(int isSelf) {
		this.isSelf = isSelf;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getQuestionList() {
		return questionList;
	}

	public void setQuestionList(String questionList) {
		this.questionList = questionList;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getAdditional() {
		return Additional;
	}

	public void setAdditional(String additional) {
		Additional = additional;
	}

	public String getAdditionalDes() {
		return AdditionalDes;
	}

	public void setAdditionalDes(String additionalDes) {
		AdditionalDes = additionalDes;
	}

	public String getAdditionalAnswer() {
		return AdditionalAnswer;
	}

	public void setAdditionalAnswer(String additionalAnswer) {
		AdditionalAnswer = additionalAnswer;
	}

	public String getCheckResult() {
		return CheckResult;
	}

	public void setCheckResult(String checkResult) {
		CheckResult = checkResult;
	}

	public int getIsType() {
		return isType;
	}

	public void setIsType(int isType) {
		this.isType = isType;
	}

}
