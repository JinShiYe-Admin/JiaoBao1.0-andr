package com.jsy_jiaobao.po.leave;

import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.http.RequestParams;
import android.content.Context;

/**
 * 门卫查询接口数据
 * 
 * @author admin
 * 
 */
public class GateQueryLeavesPost {
	private int numPerPage;// 否 int 取回的记录数量，默认20
	private int pageNum;// 否 int 第几页，默认为1
	private int RowCount;//   是 int pageNum=1为0，第二页起从前一页的返回结果中获得
	private int unitId;// 是 Int 单位ID
	private String sDateTime;// 是 Datetime 按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。

	// 返回：data

	public GateQueryLeavesPost(Context context) {
		// TODO Auto-generated constructor stub
		unitId = BaseActivity.sp.getInt("UnitID", 0);
	}

	public RequestParams getParams() {
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
			params.addBodyParameter("pageNum", String.valueOf(pageNum));
			params.addBodyParameter("RowCount", String.valueOf(RowCount));
			params.addBodyParameter("unitId", String.valueOf(unitId));
			params.addBodyParameter("sDateTime", sDateTime);
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (RowCount < 0) {
			return false;
		}
		if (unitId == 0) {
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

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getsDateTime() {
		return sDateTime;
	}

	public void setsDateTime(String sDateTime) {
		this.sDateTime = sDateTime;
	}

}
