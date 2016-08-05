package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;
import java.util.ArrayList;

public class RecommentDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3654578425364041440L;
	private int tabid; // 推荐ID
	private QuestionIndexItem question;
	private ArrayList<RecommentIndexAnswer> answers;
	public int getTabid() {
		return tabid;
	}
	public void setTabid(int tabid) {
		this.tabid = tabid;
	}
	public ArrayList<RecommentIndexAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<RecommentIndexAnswer> answers) {
		this.answers = answers;
	}
	public QuestionIndexItem getQuestion() {
		return question;
	}
	public void setQuestion(QuestionIndexItem question) {
		this.question = question;
	}
	
}
