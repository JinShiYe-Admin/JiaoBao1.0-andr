package com.jsy_jiaobao.po.workol;

/**
 * 做完的题目详情
 * 
 * @author admin
 * 
 */

public class QsPack {
	/** 作业ID */
	public int hwid;
	/** 作业分发ID */
	public int hwinfoid;
	/** 题目ID */
	public int QsId;
	/** 题目题库ID */
	public int QId;
	/** 题型 */
	public int QsT;
	/** 题目 */
	public String QsCon;
	/** 当前答案 */
	public String QsAns;
	/** 试题正确答案 */
	public String QsCorectAnswer;
	/** 答案解析 */
	public String QsExplain;

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

	public int getQsId() {
		return QsId;
	}

	public void setQsId(int qsId) {
		QsId = qsId;
	}

	public int getQId() {
		return QId;
	}

	public void setQId(int qId) {
		QId = qId;
	}

	public int getQsT() {
		return QsT;
	}

	public void setQsT(int qsT) {
		QsT = qsT;
	}

	public String getQsCon() {
		return QsCon;
	}

	public void setQsCon(String qsCon) {
		QsCon = qsCon;
	}

	public String getQsAns() {
		return QsAns;
	}

	public void setQsAns(String qsAns) {
		QsAns = qsAns;
	}

	public String getQsCorectAnswer() {
		return QsCorectAnswer;
	}

	public void setQsCorectAnswer(String qsCorectAnswer) {
		QsCorectAnswer = qsCorectAnswer;
	}

	public String getQsExplain() {
		return QsExplain;
	}

	public void setQsExplain(String qsExplain) {
		QsExplain = qsExplain;
	}

}
