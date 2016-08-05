package com.jsy_jiaobao.po.leave;

/**
 * 单位班级请假统计Model
 * 
 * @author admin
 * 
 */

public class UnitClassLeaveModel {
	private int TabID;// 假条记录ID
	private String ManName;// 请假人姓名
	private String WriteDate;// 发起日期
	private String LeaveType;// 请假类型
	private String StatusStr;// 状态
	private int RowCount;// 记录数量

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public String getManName() {
		return ManName;
	}

	public void setManName(String manName) {
		ManName = manName;
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
