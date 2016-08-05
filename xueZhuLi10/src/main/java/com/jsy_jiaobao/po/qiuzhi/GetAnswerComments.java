package com.jsy_jiaobao.po.qiuzhi;

import java.util.ArrayList;

public class GetAnswerComments {
	private int RowCount;
	private ArrayList<AnswerComment> commentsList;
	private ArrayList<AnswerRefcomment> refcomments;
	public ArrayList<AnswerComment> getCommentsList() {
		return commentsList;
	}
	public void setCommentsList(ArrayList<AnswerComment> commentsList) {
		this.commentsList = commentsList;
	}
	public ArrayList<AnswerRefcomment> getRefcomments() {
		return refcomments;
	}
	public void setRefcomments(ArrayList<AnswerRefcomment> refcomments) {
		this.refcomments = refcomments;
	}
	public int getRowCount() {
		return RowCount;
	}
	public void setRowCount(int rowCount) {
		RowCount = rowCount;
	}
}
