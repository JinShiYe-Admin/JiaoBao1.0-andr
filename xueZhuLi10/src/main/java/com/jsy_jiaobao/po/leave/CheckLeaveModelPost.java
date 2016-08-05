package com.jsy_jiaobao.po.leave;

import android.content.Context;

import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.http.RequestParams;

/**
 * 审批假条model
 * 
 * @author
 * 
 */
public class CheckLeaveModelPost {
	private int tabid;// 是 Int 请假条记录ID
	private int level;// 是 int 审批级别，1-5（一级到五级）
	private String userName;// 是 string 审批人姓名
	private String note;// 否 string 批注
	private int checkFlag;// 是 int 1通过，2拒绝

	public CheckLeaveModelPost(Context context) {
		userName = BaseActivity.sp.getString("UserName", null);
	}

	public RequestParams getParams() {
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("tabid", String.valueOf(tabid));
			params.addBodyParameter("level", String.valueOf(level));
			params.addBodyParameter("userName", userName);
			params.addBodyParameter("note", note);
			params.addBodyParameter("checkFlag", String.valueOf(checkFlag));
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (tabid == 0) {
			return false;
		}
		if (level == 0) {
			return false;
		}

		if (userName == null) {
			return false;
		}
		if (checkFlag == 0) {
			return false;
		}

		return true;
	}

	public int getTabid() {
		return tabid;
	}

	public void setTabid(int tabid) {
		this.tabid = tabid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(int checkFlag) {
		this.checkFlag = checkFlag;
	}

}
