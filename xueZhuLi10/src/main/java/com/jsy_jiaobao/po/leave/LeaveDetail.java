package com.jsy_jiaobao.po.leave;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * 假条详情model
 * @author Vktuns
 *
 */
public class LeaveDetail implements Serializable {
   
	private static final long serialVersionUID = -2949736156925436743L;
	private int TabID;// 记录ID
    private String ManName;//请假人姓名
    private int ManId;//请假人的人员Id，学生ID或老师Id,非教宝号
    private int ManType;//0学生 1老师
    private int UnitClassId;//班级Id
    private String ClassStr;//班级名称
    private String GradeStr;//年级名称
    private int UnitId;//单位Id
    private String Writer;//发起人姓名
    private String WriteDate;//发起日期
    private String LeaveType;//请假类型
    private String LeaveReason;//理由
    private String StatusStr;// 状态
    private int ApproveStatus;//一审状态 0等待中，1通过，2拒绝
    private String Approve;//一审人姓名
    private String ApproveDate;//一审日期
    private String ApproveNote;//一审批注
    private int ApproveStatus1;//二审状态
    private String Approve1;//二审人姓名
    private String ApproveDate1;//二审日期
    private String ApproveNote1;//二审批注
    private int ApproveStatus2;//三审状态
    private String Approve2;//三审人姓名
    private String ApproveDate2;//三审日期
    private String ApproveNote2;//三审批注
    private int ApproveStatus3;//四审状态
    private String Approve3;//四审人姓名
    private String ApproveDate3;//四审日期
    private String ApproveNote3;//四审批注
    private int ApproveStatus4;//五审状态
    private String Approve4;//五审人姓名
    private String ApproveDate4;//五审日期
    private String ApproveNote4;//五审批注
    private ArrayList<LeaveTime> TimeList;// 时间段
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
	public int getManId() {
		return ManId;
	}
	public void setManId(int manId) {
		ManId = manId;
	}
	public int getManType() {
		return ManType;
	}
	public void setManType(int manType) {
		ManType = manType;
	}
	public int getUnitClassId() {
		return UnitClassId;
	}
	public void setUnitClassId(int unitClassId) {
		UnitClassId = unitClassId;
	}
	public String getClassStr() {
		return ClassStr;
	}
	public void setClassStr(String classStr) {
		ClassStr = classStr;
	}
	public String getGradeStr() {
		return GradeStr;
	}
	public void setGradeStr(String gradeStr) {
		GradeStr = gradeStr;
	}
	public int getUnitId() {
		return UnitId;
	}
	public void setUnitId(int unitId) {
		UnitId = unitId;
	}
	public String getWriter() {
		return Writer;
	}
	public void setWriter(String writer) {
		Writer = writer;
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
	public String getLeaveReason() {
		return LeaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		LeaveReason = leaveReason;
	}
	public String getStatusStr() {
		return StatusStr;
	}
	public void setStatusStr(String statusStr) {
		StatusStr = statusStr;
	}
	public int getApproveStatus() {
		return ApproveStatus;
	}
	public void setApproveStatus(int approveStatus) {
		ApproveStatus = approveStatus;
	}
	public String getApprove() {
		return Approve;
	}
	public void setApprove(String approve) {
		Approve = approve;
	}
	public String getApproveDate() {
		return ApproveDate;
	}
	public void setApproveDate(String approveDate) {
		ApproveDate = approveDate;
	}
	public String getApproveNote() {
		return ApproveNote;
	}
	public void setApproveNote(String approveNote) {
		ApproveNote = approveNote;
	}
	public int getApproveStatus1() {
		return ApproveStatus1;
	}
	public void setApproveStatus1(int approveStatus1) {
		ApproveStatus1 = approveStatus1;
	}
	public String getApprove1() {
		return Approve1;
	}
	public void setApprove1(String approve1) {
		Approve1 = approve1;
	}
	public String getApproveDate1() {
		return ApproveDate1;
	}
	public void setApproveDate1(String approveDate1) {
		ApproveDate1 = approveDate1;
	}
	public String getApproveNote1() {
		return ApproveNote1;
	}
	public void setApproveNote1(String approveNote1) {
		ApproveNote1 = approveNote1;
	}
	public int getApproveStatus2() {
		return ApproveStatus2;
	}
	public void setApproveStatus2(int approveStatus2) {
		ApproveStatus2 = approveStatus2;
	}
	public String getApprove2() {
		return Approve2;
	}
	public void setApprove2(String approve2) {
		Approve2 = approve2;
	}
	public String getApproveDate2() {
		return ApproveDate2;
	}
	public void setApproveDate2(String approveDate2) {
		ApproveDate2 = approveDate2;
	}
	public String getApproveNote2() {
		return ApproveNote2;
	}
	public void setApproveNote2(String approveNote2) {
		ApproveNote2 = approveNote2;
	}
	public int getApproveStatus3() {
		return ApproveStatus3;
	}
	public void setApproveStatus3(int approveStatus3) {
		ApproveStatus3 = approveStatus3;
	}
	public String getApprove3() {
		return Approve3;
	}
	public void setApprove3(String approve3) {
		Approve3 = approve3;
	}
	public String getApproveDate3() {
		return ApproveDate3;
	}
	public void setApproveDate3(String approveDate3) {
		ApproveDate3 = approveDate3;
	}
	public String getApproveNote3() {
		return ApproveNote3;
	}
	public void setApproveNote3(String approveNote3) {
		ApproveNote3 = approveNote3;
	}
	public int getApproveStatus4() {
		return ApproveStatus4;
	}
	public void setApproveStatus4(int approveStatus4) {
		ApproveStatus4 = approveStatus4;
	}
	public String getApprove4() {
		return Approve4;
	}
	public void setApprove4(String approve4) {
		Approve4 = approve4;
	}
	public String getApproveDate4() {
		return ApproveDate4;
	}
	public void setApproveDate4(String approveDate4) {
		ApproveDate4 = approveDate4;
	}
	public String getApproveNote4() {
		return ApproveNote4;
	}
	public void setApproveNote4(String approveNote4) {
		ApproveNote4 = approveNote4;
	}
	public ArrayList<LeaveTime> getTimeList() {
		return TimeList;
	}
	public void setTimeList(ArrayList<LeaveTime> timeList) {
		TimeList = timeList;
	}
    

}
