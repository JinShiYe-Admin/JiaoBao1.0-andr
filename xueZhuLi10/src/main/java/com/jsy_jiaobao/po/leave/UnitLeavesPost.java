package com.jsy_jiaobao.po.leave;

import java.io.Serializable;

import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.http.RequestParams;

import android.content.Context;
import android.util.Log;

/**
 * 单位请假系统设置 请求数据
 * 
 * @author
 * 
 */
public class UnitLeavesPost implements Serializable {

	private static final long serialVersionUID = 4778545646478L;
	private static final String TAG = "UnitLeavesPost";
	private int numPerPage;// 否 int 取回的记录数量，默认20
	private int pageNum;// 否 int 第几页，默认为1
	private int RowCount;//   是 int pageNum=1为0，第二页起从前一页的返回结果中获得
	private int unitId; // 是 Int 单位ID
	private String sDateTime;// 是 Datetime 按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	private int checkFlag;// 是 int 0待审记录，1已审记录
	private int level;// 是 int 1-5，审批级别一级到五级
	private int manType;// 是 int 0学生1老师
	private String gradeStr;// 否 string 年级名称
	private String classStr;// 否 string 班级名称

	public UnitLeavesPost(Context xContext) {

		unitId = BaseActivity.sp.getInt("UnitID", 0);
	}

	public RequestParams getParams() {
		Log.d(TAG, check() + "");
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
			params.addBodyParameter("pageNum", String.valueOf(pageNum));
			params.addBodyParameter("RowCount", String.valueOf(RowCount));
			params.addBodyParameter("unitId", String.valueOf(unitId));
			params.addBodyParameter("sDateTime", sDateTime);
			params.addBodyParameter("checkFlag", String.valueOf(checkFlag));
			params.addBodyParameter("level", String.valueOf(level));
			params.addBodyParameter("manType", String.valueOf(manType));
			params.addBodyParameter("gradeStr", gradeStr);
			params.addBodyParameter("classStr", classStr);

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
		if (checkFlag < 0) {
			return false;
		}
		if (level == 0) {
			return false;
		}
		if (manType < 0) {
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

	public int getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(int checkFlag) {
		this.checkFlag = checkFlag;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getManType() {
		return manType;
	}

	public void setManType(int manType) {
		this.manType = manType;
	}

	public String getGradeStr() {
		return gradeStr;
	}

	public void setGradeStr(String gradeStr) {
		this.gradeStr = gradeStr;
	}

	public String getClassStr() {
		return classStr;
	}

	public void setClassStr(String classStr) {
		this.classStr = classStr;
	}

}
