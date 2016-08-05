package com.jsy_jiaobao.po.workol;

/**
 * 错题详情Model
 * 
 * @author admin
 * 
 */

public class StuErrDetailModel {
	private int hwid;// {"hwid":2,作业Id
	private int hwinfoid;// "hwinfoid":14,作业信息Id
	private int QsId;// "QsId":5,问题Id
	private int QId;// "QId":17,
	private int QsT;// "QsT":1,
	private String QsCon;// 问题内容"QsCon":"\u003cdiv class=\"shijuItem\"\u003e\u003cbr /\u003e\u003cspan style=\"font-family:微软雅黑;\"\u003e给下面句中的“寄”字选择解释，正确的一项是(      )\u003cbr /\u003e他从小寄养在姑母家。\u003c/span\u003e  \u003cbr /\u003e\u003clabel\u003e\u003cinput type=\"radio\" value=\"A\"  name=\"TopicRadio1\"  /\u003e\u003cspan style=\"font-family:微软雅黑;\"\u003eA.托付，寄托。\u003c/span\u003e  \u003cbr /\u003e\u003c/label\u003e\u003clabel\u003e\u003cinput type=\"radio\" value=\"B\"  name=\"TopicRadio1\" /\u003e\u003cspan style=\"font-family:微软雅黑;\"\u003eB.依靠，依附。\u003c/span\u003e  \u003cbr /\u003e\u003c/label\u003e\u003clabel\u003e\u003cinput type=\"radio\" value=\"C\"  name=\"TopicRadio1\" /\u003e\u003cspan style=\"font-family:微软雅黑;\"\u003eC.托人传送。   \u003c/span\u003e  \u003cbr /\u003e\u003c/label\u003e\u003clabel\u003e\u003cinput type=\"radio\" value=\"D\"  name=\"TopicRadio1\" /\u003e\u003cspan style=\"font-family:微软雅黑;\"\u003eD. \u003c/span\u003e  \u003cbr /\u003e\u003c/label\u003e\u003c/div\u003e",
	private String QsAns;// 问题 答案"QsAns":null,
	private String QsCorectAnswer;// 问题正确答案 "QsCorectAnswer":"A",
	private String QsExplain;// 问题解释"QsExplain":null}

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
