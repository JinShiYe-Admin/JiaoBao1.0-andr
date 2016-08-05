package com.jsy_jiaobao.po.workol;

/**
 * 作业Model
 * 
 * @author admin
 * 
 */

public class HwPack {
	// 作业ID
	public int hwid;
	// 作业分发ID
	public int hwinfoid;
	// 作业名称
	public String homeworkname;
	// 作业开始时间
	public String HWStartTime;
	// 作答时长
	public int LongTime;
	// 题数
	public int Qsc;
	// 题序号_题库ID|
	public String QsIdQId;

	public int getHwid() {
		return hwid;
	}

	public void setHwid(int hwid) {
		this.hwid = hwid;
	}

	public int getHwinfoid() {
		return hwinfoid;
	}

	public void setHwinfoid(int hwinfoid) {
		this.hwinfoid = hwinfoid;
	}

	public String getHomeworkname() {
		return homeworkname;
	}

	public void setHomeworkname(String homeworkname) {
		this.homeworkname = homeworkname;
	}

	public String getHWStartTime() {
		return HWStartTime;
	}

	public void setHWStartTime(String hWStartTime) {
		HWStartTime = hWStartTime;
	}

	public int getLongTime() {
		return LongTime;
	}

	public void setLongTime(int longTime) {
		LongTime = longTime;
	}

	public int getQsc() {
		return Qsc;
	}

	public void setQsc(int qsc) {
		Qsc = qsc;
	}

	public String getQsIdQId() {
		return QsIdQId;
	}

	public void setQsIdQId(String qsIdQId) {
		QsIdQId = qsIdQId;
	}

}
