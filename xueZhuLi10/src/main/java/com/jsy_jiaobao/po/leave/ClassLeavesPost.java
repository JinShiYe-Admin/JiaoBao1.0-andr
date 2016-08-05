package com.jsy_jiaobao.po.leave;

import java.io.Serializable;

import com.lidroid.xutils.http.RequestParams;

/**
 * 班主任获取班级请假统计所需数据
 * 
 * @author admin
 * 
 */
public class ClassLeavesPost implements Serializable {

	private static final long serialVersionUID = 1454512484521645L;
	private int numPerPage;// 否 int 取回的记录数量，默认20
	private int pageNum;// 否 int 第几页，默认为1
	private int RowCount;// 是 int pageNum=1为0，第二页起从前一页的返回结果中获得
	private int unitClassId;// 是 Int 班级ID
	private String sDateTime;// 是 Datetime 按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	private int checkFlag;// 是 int 0待审记录，1已审记录

	public RequestParams getParams() {

		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
			params.addBodyParameter("pageNum", String.valueOf(pageNum));
			params.addBodyParameter("RowCount", String.valueOf(RowCount));
			params.addBodyParameter("unitClassId", String.valueOf(unitClassId));
			params.addBodyParameter("sDateTime", sDateTime);
			params.addBodyParameter("checkFlag", String.valueOf(checkFlag));

			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (unitClassId == 0) {
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

	public int getUnitClassId() {
		return unitClassId;
	}

	public void setUnitClassId(int unitClassId) {
		this.unitClassId = unitClassId;
	}

	public String getsDateTime() {
		return sDateTime;
	}

	public void setsDateTime(String sDateTime) {
		this.sDateTime = sDateTime;
	}

	public int getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(int checkFlag) {
		this.checkFlag = checkFlag;
	}

}
