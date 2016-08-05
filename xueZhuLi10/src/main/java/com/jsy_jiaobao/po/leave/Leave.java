package com.jsy_jiaobao.po.leave;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 假条model
 * 
 * @author Created by Administrator on 2016/1/13.
 */
public class Leave implements Serializable {

	private static final long serialVersionUID = -7611768379338080955L;
	private int mTabID;
	private int mUnitId;// 是 int 单位Id
	private int mManId;// 是 int 请假人的人员Id，学生ID或老师Id,非教宝号
	private String mManName;// 是 string 请假人姓名
	private int mWriterId;// 是 int 发起人教宝号
	private String mWriter;// 是 string 发起人姓名
	private String mGradeStr;// 否(学生时为是) string 年级名称
	private String mClassStr;// 否(学生时为是) string 班级名称
	private int mUnitClassId;// 是 int 班级Id,学生请假须提供，老师请假可以为0
	private int mManType;// 是 int 人员类型，0为学生，1为老师
	private String mLeaveType;// 是 string 请假类型，如：补课，病假
	private String mLeaveReason;// 否 string 请假理由
	private ArrayList<LeaveTime> mLeaveTimeList;// 是 datetime 请假时间段

	public Leave(Context context) {
		mUnitId = BaseActivity.sp.getInt("UnitID", 0);
		mWriter = BaseActivity.sp.getString("UserName", null);
		mWriterId = Integer.parseInt(BaseActivity.sp.getString("JiaoBaoHao",
				null));

	}

	public RequestParams getParams() {
		if (check()) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("UnitId", String.valueOf(mUnitId));
			params.addBodyParameter("manId", String.valueOf(mManId));
			params.addBodyParameter("manName", mManName);
			params.addBodyParameter("writerId", String.valueOf(mWriterId));
			params.addBodyParameter("writer", mWriter);
			params.addBodyParameter("gradeStr", mGradeStr);
			params.addBodyParameter("classStr", mClassStr);
			params.addBodyParameter("unitClassId", String.valueOf(mUnitClassId));
			params.addBodyParameter("manType", String.valueOf(mManType));
			params.addBodyParameter("leaveType", mLeaveType);
			params.addBodyParameter("leaveReason", mLeaveReason);
			params.addBodyParameter("sDateTime", mSDateTime);
			params.addBodyParameter("eDateTime", mEDateTime);
			params.addBodyParameter("sDateTime1", mSDateTime1);
			params.addBodyParameter("eDateTime1", mEDateTime1);
			params.addBodyParameter("sDateTime2", mSDateTime2);
			params.addBodyParameter("eDateTime2", mEDateTime2);
			params.addBodyParameter("sDateTime3", mSDateTime3);
			params.addBodyParameter("eDateTime3", mEDateTime3);
			params.addBodyParameter("sDateTime4", mSDateTime4);
			params.addBodyParameter("eDateTime4", mEDateTime4);
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (mUnitId == 0) {
			return false;
		}
		if (mManId == 0) {
			return false;
		}
		if (TextUtils.isEmpty(mManName)) {
			return false;
		}
		if (mWriterId == 0) {
			return false;
		}
		if (TextUtils.isEmpty(mWriter)) {
			return false;
		}
		if (mUnitClassId < 0) {
			return false;
		}
		if (mManType < 0) {
			return false;
		}
		if (mLeaveType == null) {
			return false;
		}
		if (mLeaveTimeList.size() == 0) {
			return false;
		}

		return true;
	}

	public int getUnitId() {
		return mUnitId;
	}

	public void setUnitId(int unitId) {
		mUnitId = unitId;
	}

	public int getManId() {
		return mManId;
	}

	public void setManId(int manId) {
		mManId = manId;
	}

	public String getManName() {
		return mManName;
	}

	public void setManName(String manName) {
		mManName = manName;
	}

	public int getWriterId() {
		return mWriterId;
	}

	public void setWriterId(int writerId) {
		mWriterId = writerId;
	}

	public String getWriter() {
		return mWriter;
	}

	public void setWriter(String writer) {
		mWriter = writer;
	}

	public String getGradeStr() {
		return mGradeStr;
	}

	public void setGradeStr(String gradeStr) {
		mGradeStr = gradeStr;
	}

	public String getClassStr() {
		return mClassStr;
	}

	public void setClassStr(String classStr) {
		mClassStr = classStr;
	}

	public int getUnitClassId() {
		return mUnitClassId;
	}

	public void setUnitClassId(int unitClassId) {
		mUnitClassId = unitClassId;
	}

	public int getManType() {
		return mManType;
	}

	public void setManType(int manType) {
		mManType = manType;
	}

	public String getLeaveType() {
		return mLeaveType;
	}

	public void setLeaveType(String leaveType) {
		mLeaveType = leaveType;
	}

	public String getLeaveReason() {
		return mLeaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		mLeaveReason = leaveReason;
	}

	public ArrayList<LeaveTime> getLeaveTimeList() {
		return mLeaveTimeList;
	}

	public void setLeaveTimeList(ArrayList<LeaveTime> leaveTimeList) {
		mLeaveTimeList = leaveTimeList;
		String[] s = new String[5];
		String[] e = new String[5];
		if (leaveTimeList != null && leaveTimeList.size() > 0) {
			for (int i = 0; i < leaveTimeList.size(); i++) {
				LeaveTime leaveTime = new LeaveTime();
				leaveTime = leaveTimeList.get(i);
				s[i] = leaveTime.getSdate();
				e[i] = leaveTime.getEdate();
			}
			mSDateTime = s[0];
			mEDateTime = e[0];
			mSDateTime1 = s[1];
			mEDateTime1 = e[1];
			mSDateTime2 = s[2];
			mEDateTime2 = e[2];
			mSDateTime3 = s[3];
			mEDateTime3 = e[3];
			mSDateTime4 = s[4];
			mEDateTime4 = e[4];
		}
	}

	public String getSDateTime() {
		return mSDateTime;
	}

	public void setSDateTime(String sDateTime) {
		mSDateTime = sDateTime;
	}

	public String getEDateTime() {
		return mEDateTime;
	}

	public void setEDateTime(String eDateTime) {
		mEDateTime = eDateTime;
	}

	public String getSDateTime1() {
		return mSDateTime1;
	}

	public void setSDateTime1(String sDateTime1) {
		mSDateTime1 = sDateTime1;
	}

	public String getEDateTime1() {
		return mEDateTime1;
	}

	public void setEDateTime1(String eDateTime1) {
		mEDateTime1 = eDateTime1;
	}

	public String getSDateTime2() {
		return mSDateTime2;
	}

	public void setSDateTime2(String sDateTime2) {
		mSDateTime2 = sDateTime2;
	}

	public String getEDateTime2() {
		return mEDateTime2;
	}

	public void setEDateTime2(String eDateTime2) {
		mEDateTime2 = eDateTime2;
	}

	public String getSDateTime3() {
		return mSDateTime3;
	}

	public void setSDateTime3(String sDateTime3) {
		mSDateTime3 = sDateTime3;
	}

	public String getEDateTime3() {
		return mEDateTime3;
	}

	public void setEDateTime3(String eDateTime3) {
		mEDateTime3 = eDateTime3;
	}

	public String getSDateTime4() {
		return mSDateTime4;
	}

	public void setSDateTime4(String sDateTime4) {
		mSDateTime4 = sDateTime4;
	}

	public String getEDateTime4() {
		return mEDateTime4;
	}

	public void setEDateTime4(String eDateTime4) {
		mEDateTime4 = eDateTime4;
	}

	private String mSDateTime;
	private String mEDateTime;
	private String mSDateTime1;
	private String mEDateTime1;
	private String mSDateTime2;
	private String mEDateTime2;
	private String mSDateTime3;
	private String mEDateTime3;
	private String mSDateTime4;
	private String mEDateTime4;
	private String timeList;
	/**
	 * 未审核=1、审核中=2、通过=3、拒绝=4
	 */
	private int CheckFlag;
	/**
	 * 1:补课 2:事假 3:病假 4:其他
	 */
	private int reasonFlag;
	private String reason;
	private String createTime;
	private int createYear;
	private int createMonth;
	private int CheckedTeacher;
	private String CheckedTeacherStr = "";
	/**
	 * 0=未审核,1=通过,2=拒绝
	 */
	private int CheckedAdminO;
	private String CheckedAdminOStr = "";
	private int CheckedAdminT;
	private String CheckedAdminTStr = "";

	public int getCheckedTeacher() {
		return CheckedTeacher;
	}

	public void setCheckedTeacher(int checkedTeacher) {
		/**
		 * 未审核=1、审核中=2、同过=3、拒绝=4
		 */
		if (checkedTeacher == 1) {
			CheckFlag = 2;
		} else if (checkedTeacher == 2) {
			CheckFlag = 4;
		} else if (checkedTeacher == 0) {
			CheckFlag = 1;
		}
		CheckedTeacher = checkedTeacher;
	}

	public String getCheckedTeacherStr() {
		return TextUtils.isEmpty(CheckedTeacherStr) ? "无" : CheckedTeacherStr;
	}

	public void setCheckedTeacherStr(String checkedTeacherStr) {
		CheckedTeacherStr = checkedTeacherStr;
	}

	public int getCheckedAdminO() {

		return CheckedAdminO;
	}

	public void setCheckedAdminO(int checkedAdminO) {
		/**
		 * 未审核=1、审核中=2、同过=3、拒绝=4
		 */
		if (checkedAdminO == 1) {
			CheckFlag = 2;
		} else if (checkedAdminO == 2) {
			CheckFlag = 4;
		}
		CheckedAdminO = checkedAdminO;
	}

	public String getCheckedAdminOStr() {
		return TextUtils.isEmpty(CheckedAdminOStr) ? "无" : CheckedAdminOStr;
	}

	public void setCheckedAdminOStr(String checkedAdminOStr) {
		CheckedAdminOStr = checkedAdminOStr;
	}

	public int getCheckedAdminT() {
		return CheckedAdminT;
	}

	/**
	 * 0=未审核,1=通过,2=拒绝
	 */
	public void setCheckedAdminT(int checkedAdminT) {
		/**
		 * 未审核=1、审核中=2、同过=3、拒绝=4
		 */
		if (checkedAdminT == 1) {
			CheckFlag = 3;
		} else if (checkedAdminT == 2) {
			CheckFlag = 4;
		}
		CheckedAdminT = checkedAdminT;
	}

	public String getCheckedAdminTStr() {
		return TextUtils.isEmpty(CheckedAdminTStr) ? "无" : CheckedAdminTStr;
	}

	public void setCheckedAdminTStr(String checkedAdminTStr) {
		CheckedAdminTStr = checkedAdminTStr;
	}

	// public User getUser(DbManager db) throws DbException{
	// return db.findById(User.class, UserID);
	// }
	// public User getAuthor(DbManager db) throws DbException{
	// return db.findById(User.class, AuthorID);
	// }

	public ArrayList<LeaveTime> getTimeArrayList() {
		ArrayList<LeaveTime> list = GsonUtil.GsonToList(timeList,
				new TypeToken<ArrayList<LeaveTime>>() {
				}.getType());
		return list;
	}

	public String getTimeList() {
		return timeList;
	}

	public void setTimeList(ArrayList<LeaveTime> timeList) {
		Gson gson = new Gson();
		String s = gson.toJson(timeList);
		this.timeList = s;
	}

	public int getCheckFlag() {
		return CheckFlag;
	}

	public String getCheckFlagString() {
		switch (CheckFlag) {
		case 1:
			return "未审核";
		case 2:
			return "审核中";
		case 3:
			return "通过";
		case 4:
			return "拒绝";
		default:
			return "未知";
		}
	}

	public void setCheckFlag(int checkFlag) {
		CheckFlag = checkFlag;
	}

	public String getResion() {
		return reason;
	}

	public void setResion(String reason) {
		this.reason = reason;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getReasonFlag() {
		return reasonFlag;
	}

	public String getReasonFlagString() {
		switch (reasonFlag) {
		case 1:
			return "补课";
		case 2:
			return "事假";
		case 3:
			return "病假";
		default:
			return "其他";
		}
	}

	public void setResionFlag(int resionFlag) {
		this.reasonFlag = resionFlag;
	}

	public int getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}

	public int getCreateYear() {
		return createYear;
	}

	public void setCreateYear(int createYear) {
		this.createYear = createYear;
	}

}
