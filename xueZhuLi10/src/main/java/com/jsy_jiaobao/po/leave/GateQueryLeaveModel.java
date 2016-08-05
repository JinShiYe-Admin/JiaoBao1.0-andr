package com.jsy_jiaobao.po.leave;

import java.io.Serializable;

/**
 * 门卫审核假条model
 * 
 * @author
 * 
 */
public class GateQueryLeaveModel implements Serializable {
	private static final long serialVersionUID = -5418645564391917578L;
	private int TabID;// 时间段ID
	private String WriteDate;// ,发起时间
	private String ManName;// ,请假人姓名
	private String LeaveType;// ,请假类型
	private String Sdate;// 请假开始时间
	private String Edate;// ,请假结束时间
	private String LWriterName;// 离校登记人（门卫）
	private String LeaveTime;// 离校时间
	private String CWriterName;// 返校登记人(门卫）
	private String ComeTime;// 返校时间
	private int RowCount;// 记录数量

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

	public String getManName() {
		return ManName;
	}

	public void setManName(String manName) {
		ManName = manName;
	}

	public String getLeaveType() {
		return LeaveType;
	}

	public void setLeaveType(String leaveType) {
		LeaveType = leaveType;
	}

	public String getSdate() {
		return Sdate;
	}

	public void setSdate(String sdate) {
		Sdate = sdate;
	}

	public String getEdate() {
		return Edate;
	}

	public void setEdate(String edate) {
		Edate = edate;
	}

	public String getLWriterName() {
		return LWriterName;
	}

	public void setLWriterName(String lWriterName) {
		LWriterName = lWriterName;
	}

	public String getLeaveTime() {
		return LeaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		LeaveTime = leaveTime;
	}

	public String getCWriterName() {
		return CWriterName;
	}

	public void setCWriterName(String cWriterName) {
		CWriterName = cWriterName;
	}

	public String getComeTime() {
		return ComeTime;
	}

	public void setComeTime(String comeTime) {
		ComeTime = comeTime;
	}

	public int getRowCount() {
		return RowCount;
	}

	public void setRowCount(int rowCount) {
		RowCount = rowCount;
	}

}
