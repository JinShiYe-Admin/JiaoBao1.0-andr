package com.jsy_jiaobao.po.personal;

import java.util.ArrayList;

/**
 * 评论列表
 */
public class CommentsList {

	private ArrayList<Comment> commentsList;
	private ArrayList<RefComment> refcomments;

	public ArrayList<Comment> getCommentsList() {
		return commentsList;
	}

	public void setCommentsList(ArrayList<Comment> commentsList) {
		this.commentsList = commentsList;
	}

	public ArrayList<RefComment> getRefcomments() {
		return refcomments;
	}

	public void setRefcomments(ArrayList<RefComment> refcomments) {
		this.refcomments = refcomments;
	}

}
