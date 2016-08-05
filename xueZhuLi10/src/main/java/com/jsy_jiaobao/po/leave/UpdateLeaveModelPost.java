package com.jsy_jiaobao.po.leave;

import com.lidroid.xutils.http.RequestParams;

/**
 * 请求一条请假条记录model所需数据
 * 
 * @author ShangLin Mo
 * 
 */
public class UpdateLeaveModelPost {
	private int tabId;// 是 int 请假记录Id
	private int manId;// 是 int 请假人的人员Id，学生ID或老师Id,非教宝号
	private String manName;// 是 string 请假人姓名
	private String gradeStr;// 是 string 年级名称
	private String classStr;// 是 string 班级名称
	private int manType;// 是 int 人员类型，0为学生，1为老师
	private String leaveType;// 是 string 请假类型，如：补课，病假
	private String leaveReason;// 否 string 请假理由

	public RequestParams getParams() {
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("tabId", String.valueOf(tabId));
			params.addBodyParameter("manId", String.valueOf(manId));
			params.addBodyParameter("manName", manName);
			params.addBodyParameter("gradeStr", gradeStr);
			params.addBodyParameter("classStr", classStr);
			params.addBodyParameter("manType", String.valueOf(manType));
			params.addBodyParameter("leaveType", leaveType);
			params.addBodyParameter("leaveReason", leaveReason);
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (tabId == 0) {
			return false;
		}
		if (manId == 0) {
			return false;
		}

		if (manName == null) {
			return false;
		}
		if (manType < 0) {
			return false;
		}
		if (leaveType == null) {
			return false;
		}

		return true;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public int getManId() {
		return manId;
	}

	public void setManId(int manId) {
		this.manId = manId;
	}

	public String getManName() {
		return manName;
	}

	public void setManName(String manName) {
		this.manName = manName;
	}

	public String getGradeStr() {
		return gradeStr;
	}

	public void setGradeStr(String gradeStr) {
		this.gradeStr = gradeStr;
	}

	public String getClassStr() {
		return classStr;
	}

	public void setClassStr(String classStr) {
		this.classStr = classStr;
	}

	public int getManType() {
		return manType;
	}

	public void setManType(int manType) {
		this.manType = manType;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

}
