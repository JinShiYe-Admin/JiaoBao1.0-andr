package com.jsy_jiaobao.po.leave;

//@Table(name = "unitLeave") 
/**
 * 单位请假设置
 * 
 * @author admin
 * 
 */
public class UnitLeave {

	private boolean StatusStd;// 是否有学生请假
	private int ApproveLevelStd;// 学生请假系统级数
	private LevelNoteStd LevelNoteStd;// 学生请假系统各级名称
	private ApproveListStd ApproveListStd;// 学生请假系统审核权限
	private boolean Status;// 是否有老师请假系统
	private int ApproveLevel;// 老师请假系统级数
	private LevelNote LevelNote;// 老师请假系统各级名称
	private ApproveList ApproveList;// 老师请假系统各级审核权限
	private boolean GateGuardList;// 是否有门卫审核权限

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

	public LevelNoteStd getLevelNoteStd() {
		return LevelNoteStd;
	}

	public void setLevelNoteStd(LevelNoteStd levelNoteStd) {
		LevelNoteStd = levelNoteStd;
	}

	public ApproveListStd getApproveListStd() {
		return ApproveListStd;
	}

	public void setApproveListStd(ApproveListStd approveListStd) {
		ApproveListStd = approveListStd;
	}

	public boolean isStatus() {
		return Status;
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

	public LevelNote getLevelNote() {
		return LevelNote;
	}

	public void setLevelNote(LevelNote levelNote) {
		LevelNote = levelNote;
	}

	public ApproveList getApproveList() {
		return ApproveList;
	}

	public void setApproveList(ApproveList approveList) {
		ApproveList = approveList;
	}

	public boolean isGateGuardList() {
		return GateGuardList;
	}

	public void setGateGuardList(boolean gateGuardList) {
		GateGuardList = gateGuardList;
	}

}
