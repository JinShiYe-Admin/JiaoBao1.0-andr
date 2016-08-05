package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class RecommentIndexItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3654578425364041440L;
	private int tabid; // 推荐ID
	private int rowCount;
	private QuestionIndexItem question;
	private AnswerItem answer;
	public int getTabid() {
		return tabid;
	}
	public void setTabid(int tabid) {
		this.tabid = tabid;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public QuestionIndexItem getQuestion() {
		return question;
	}
	public void setQuestion(QuestionIndexItem question) {
		this.question = question;
	}
	public AnswerItem getAnswer() {
		return answer;
	}
	public void setAnswer(AnswerItem answer) {
		this.answer = answer;
	}
	
}
