package com.jsy_jiaobao.po.workol;

import java.io.Serializable;
/**
 * 错题本Model
 * @author admin
 *
 */

public class StuErrorModel implements Serializable {

	private static final long serialVersionUID = 54874115464415L;
	private String TabIDStr; //
	private int Tabid;//Id {"Tabid":57,
	private int StuID;// 学生Id"StuID":3851578,
	private int HwID;// 作业Id"HwID":14,
	private int chapterID;// 章节Id"chapterID":14,
	private int QsID;// 问题Id"QsID":7,
	private int QsType;// 问题类型"QsType":1,
	private int QsLv;// 问题难度"QsLv":1,
	private String Answer;// 答案"Answer":null,
	private String Dodate;// 日期"DoDate":"2016-03-19T09:18:02.033",
	private int DoC;//亏难度 "DoC":1}

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

	public int getTabid() {
		return Tabid;
	}

	public void setTabid(int tabid) {
		Tabid = tabid;
	}

	public int getStuID() {
		return StuID;
	}

	public void setStuID(int stuID) {
		StuID = stuID;
	}

	public int getHwID() {
		return HwID;
	}

	public void setHwID(int hwID) {
		HwID = hwID;
	}

	public int getChapterID() {
		return chapterID;
	}

	public void setChapterID(int chapterID) {
		this.chapterID = chapterID;
	}

	public int getQsID() {
		return QsID;
	}

	public void setQsID(int qsID) {
		QsID = qsID;
	}

	public int getQsType() {
		return QsType;
	}

	public void setQsType(int qsType) {
		QsType = qsType;
	}

	public int getQsLv() {
		return QsLv;
	}

	public void setQsLv(int qsLv) {
		QsLv = qsLv;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

	public String getDodate() {
		return Dodate;
	}

	public void setDodate(String dodate) {
		Dodate = dodate;
	}

	public int getDoC() {
		return DoC;
	}

	public void setDoC(int doC) {
		DoC = doC;
	}

}
