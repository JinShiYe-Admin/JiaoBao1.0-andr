package com.jsy_jiaobao.po.workol;

import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;

/**
 * 老师发布作业接口所需数据
 */
public class TeaMakeHW {
	private boolean isChecked;// 是否查看
	private int teacherJiaobaohao;// 老师教宝号
	private int classID;// 班级ID
	private String className;// 班级名称
	private int chapterID;// 章节ID
	private int DoLv;// 难度等级
	private int AllNum;// 总题量;//
	private int SelNum;// 选择题量
	private int InpNum;// 填空题
	private String Distribution;// 1:10,2:20 试题分布
	private int LongTime;// 作业时长
	private String ExpTime;// 过期时间
	private String homeworkName;// 作业名称--- 日期+科目+单元名称
	private String Additional;// “”
	private String AdditionalDes;// “”
	private String schoolName;// 学校名称
	private int HwType;// 作业类型 1为个性作业，2为AB卷，3为自定义作业，4统一作业（所有班级统一）
	private boolean IsAnSms;// 是否发送 答案 短信;// T F
	private boolean IsQsSms;// 是否发送 试题 通知;//
	private boolean IsRep;// 是否发送 老师反馈 短信
	private String TecName;// 老师的名称
	private int DesId;// 自定义作业ID，如果是自定义作业则加上自定义的ID

	public RequestParams getParams() {
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("teacherJiaobaohao",
					String.valueOf(teacherJiaobaohao));
			params.addBodyParameter("classID", String.valueOf(classID));
			params.addBodyParameter("className", className);
			params.addBodyParameter("chapterID", String.valueOf(chapterID));
			params.addBodyParameter("DoLv", String.valueOf(DoLv));
			params.addBodyParameter("AllNum", String.valueOf(AllNum));
			params.addBodyParameter("SelNum", String.valueOf(SelNum));
			params.addBodyParameter("InpNum", String.valueOf(InpNum));
			params.addBodyParameter("LongTime", String.valueOf(LongTime));
			params.addBodyParameter("ExpTime", ExpTime);
			params.addBodyParameter("homeworkName", homeworkName);
			params.addBodyParameter("schoolName", schoolName);
			params.addBodyParameter("TecName", TecName);
			params.addBodyParameter("Additional", "");
			params.addBodyParameter("AdditionalDes", "");
			params.addBodyParameter("HwType", String.valueOf(HwType));
			params.addBodyParameter("DesId", String.valueOf(DesId));
			params.addBodyParameter("IsAnSms", String.valueOf(IsAnSms));
			params.addBodyParameter("IsQsSms", String.valueOf(IsQsSms));
			params.addBodyParameter("IsRep", String.valueOf(IsRep));
			params.addBodyParameter("Distribution",
					String.valueOf(Distribution));
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (teacherJiaobaohao == 0) {
			return false;
		}
		if (classID == 0) {
			return false;
		}
		if (TextUtils.isEmpty(className)) {
			return false;
		}
		if (chapterID == 0) {
			return false;
		}
		if (DoLv == 0) {
			return false;
		}
		if (AllNum == 0) {
			return false;
		}
		if (SelNum == 0) {
			return false;
		}
		if (InpNum == 0) {
			return false;
		}
		if (LongTime == 0) {
			return false;
		}
		if (TextUtils.isEmpty(ExpTime)) {
			return false;
		}
		if (TextUtils.isEmpty(homeworkName)) {
			return false;
		}
		if (TextUtils.isEmpty(schoolName)) {
			return false;
		}
		if (TextUtils.isEmpty(TecName)) {
			return false;
		}
		if (HwType == 0) {
			return false;
		}
		return true;
	}

	public int getTeacherJiaobaohao() {
		return teacherJiaobaohao;
	}

	public void setTeacherJiaobaohao(int teacherJiaobaohao) {
		this.teacherJiaobaohao = teacherJiaobaohao;
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

	public int getDoLv() {
		return DoLv;
	}

	public void setDoLv(int doLv) {
		DoLv = doLv;
	}

	public int getAllNum() {
		return AllNum;
	}

	public void setAllNum(int allNum) {
		AllNum = allNum;
	}

	public int getSelNum() {
		return SelNum;
	}

	public void setSelNum(int selNum) {
		SelNum = selNum;
		Distribution = "1:" + SelNum + ",2:" + InpNum;
		AllNum = SelNum + InpNum;
	}

	public int getInpNum() {
		return InpNum;
	}

	public void setInpNum(int inpNum) {
		InpNum = inpNum;
		Distribution = "1:" + SelNum + ",2:" + InpNum;
		AllNum = SelNum + InpNum;
	}

	public String getDistribution() {
		return Distribution;
	}

	public void setDistribution(String distribution) {
		Distribution = distribution;
	}

	public int getLongTime() {
		return LongTime;
	}

	public void setLongTime(int longTime) {
		LongTime = longTime;
	}

	public String getExpTime() {
		return ExpTime;
	}

	public void setExpTime(String expTime) {
		ExpTime = expTime;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public int getHwType() {
		return HwType;
	}

	public void setHwType(int hwType) {
		HwType = hwType;
	}

	public boolean getIsAnSms() {
		return IsAnSms;
	}

	public void setIsAnSms(boolean isAnSms) {
		IsAnSms = isAnSms;
	}

	public boolean getIsQsSms() {
		return IsQsSms;
	}

	public void setIsQsSms(boolean isQsSms) {
		IsQsSms = isQsSms;
	}

	public boolean getIsRep() {
		return IsRep;
	}

	public void setIsRep(boolean isRep) {
		IsRep = isRep;
	}

	public String getTecName() {
		return TecName;
	}

	public void setTecName(String tecName) {
		TecName = tecName;
	}

	public int getDesId() {
		return DesId;
	}

	public void setDesId(int desId) {
		DesId = desId;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
