package com.jsy_jiaobao.po.leave;

//@Table(name = "unitLeave") 
/**
 * 单位请假系统设置 对应接口
 * 
 * 
 * @author admin
 * 
 */
public class UnitLeaveGson {

	private boolean StatusStd;// 是否有学生请假系统
	private int ApproveLevelStd;// 学生请假系统级数
	private String LevelNoteStd;// 学生请假系统获取的是String 各级名称
	private String ApproveListStd;// 学生请假系统 获取的是String 各级是否有全新啊
	private boolean Status;// 老师请假系统
	private int ApproveLevel;// 老师请假系统级数
	private String LevelNote;// 老师请假系统各级名称 String
	private String ApproveList;// 老师请假系统 各级权限 String
	private boolean GateGuardList;// 是否具有门卫权限

	public String getLevelNoteStd() {
		return LevelNoteStd;
	}

	public void setLevelNoteStd(String levelNoteStd) {
		LevelNoteStd = levelNoteStd;
	}

	public String getApproveListStd() {
		return ApproveListStd;
	}

	public void setApproveListStd(String approveListStd) {
		ApproveListStd = approveListStd;
	}

	public String getLevelNote() {
		return LevelNote;
	}

	public void setLevelNote(String levelNote) {
		LevelNote = levelNote;
	}

	public String getApproveList() {
		return ApproveList;
	}

	public void setApproveList(String approveList) {
		ApproveList = approveList;
	}

	public boolean isStatus() {
		return Status;
	}

	public boolean isStatusStd() {
		return StatusStd;
	}

	public void setStatusStd(boolean statusStd) {
		StatusStd = statusStd;
	}

	public int getApproveLevelStd() {
		return ApproveLevelStd;
	}

	public void setApproveLevelStd(int approveLevelStd) {
		ApproveLevelStd = approveLevelStd;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public int getApproveLevel() {
		return ApproveLevel;
	}

	public void setApproveLevel(int approveLevel) {
		ApproveLevel = approveLevel;
	}

	public boolean isGateGuardList() {
		return GateGuardList;
	}

	public void setGateGuardList(boolean gateGuardList) {
		GateGuardList = gateGuardList;
	}

}
