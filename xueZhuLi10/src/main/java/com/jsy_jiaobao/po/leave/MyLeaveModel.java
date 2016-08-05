package com.jsy_jiaobao.po.leave;

/**
 * 我的假条Model
 * 
 * @author admin
 * 
 */

public class MyLeaveModel {
	// "TabID":213,"WriteDate":"2016-03-31T09:31:03","LeaveType":"补课","ManName":"002班管理员","StatusStr":"审批中","RowCount":2
	private int TabID; // 记录ID
	private String WriteDate;// 申请日期
	private String LeaveType;// 请假类型
	private String ManName;
	private String StatusStr;// 审批中
	private int RowCount;// 记录总数

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public String getWriteDate() {
		return WriteDate;
	}

	public void setWriteDate(String writeDate) {
		WriteDate = writeDate;
	}

	public String getLeaveType() {
		return LeaveType;
	}

	public void setLeaveType(String leaveType) {
		LeaveType = leaveType;
	}

	public String getManName() {
		return ManName;
	}

	public void setManName(String manName) {
		ManName = manName;
	}

	public String getStatusStr() {
		return StatusStr;
	}

	public void setStatusStr(String statusStr) {
		StatusStr = statusStr;
	}

	public int getRowCount() {
		return RowCount;
	}

	public void setRowCount(int rowCount) {
		RowCount = rowCount;
	}

}
