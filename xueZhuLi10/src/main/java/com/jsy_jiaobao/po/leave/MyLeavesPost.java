package com.jsy_jiaobao.po.leave;

import android.content.Context;
import android.util.Log;

import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.http.RequestParams;

/**
 * 获取我的请假记录的Post 数据
 * 
 * @author
 * 
 */
public class MyLeavesPost {
	private int numPerPage;// 否 int 取回的记录数量，默认20
	private int pageNum;// 否 int 第几页，默认为1
	private int RowCount;//   是 int pageNum=1为0，第二页起从前一页的返回结果中获得
	private int accId;// 是 Int 用户教宝号
	private String sDateTime;// 是 Datetime 按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。 //
								// doneflag 是 int 0进行中记录，1已结束的记录，此字段不要了
	private String mName;// 否 string 请假人姓名
	private int manType;// 否 int 人员类型，0学生1老师

	public MyLeavesPost(Context context) {
		accId = Integer.parseInt(BaseActivity.sp.getString("JiaoBaoHao", null));
		numPerPage = 20;
		pageNum = 1;
	}

	public RequestParams getParams() {
		if (check()) {
			Log.e("booleen", check() + "");
			RequestParams params = new RequestParams();
			params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
			params.addBodyParameter("pageNum", String.valueOf(pageNum));
			params.addBodyParameter("RowCount", String.valueOf(RowCount));
			params.addBodyParameter("accId", String.valueOf(accId));
			params.addBodyParameter("sDateTime", sDateTime);
			params.addBodyParameter("mName", mName);
			params.addBodyParameter("manType", String.valueOf(manType));
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (RowCount < 0) {
			return false;
		}
		if (accId == 0) {
			return false;
		}

		if (sDateTime == null) {
			return false;
		}
		return true;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getRowCount() {
		return RowCount;
	}

	public void setRowCount(int rowCount) {
		RowCount = rowCount;
	}

	public int getAccId() {
		return accId;
	}

	public void setAccId(int accId) {
		this.accId = accId;
	}

	public String getsDateTime() {
		return sDateTime;
	}

	public void setsDateTime(String sDateTime) {
		this.sDateTime = sDateTime;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getManType() {
		return manType;
	}

	public void setManType(int manType) {
		this.manType = manType;
	}
}
