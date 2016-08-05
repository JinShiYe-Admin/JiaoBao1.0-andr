package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.CheckLeaveModelPost;
import com.jsy_jiaobao.po.leave.ClassSumLeaveModel;
import com.jsy_jiaobao.po.leave.GateQueryLeaves;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.StuSumLeavesModel;
import com.jsy_jiaobao.po.leave.SumLeavesModel;
import com.jsy_jiaobao.po.leave.UnitClassLeaves;
import com.jsy_jiaobao.po.leave.UnitLeavesPost;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class CheckerActivityControler implements ConstantUrl {
	private static final String TAG = null;
	private static CheckerActivityControler instance;
	private Context mContext;

	public static synchronized final CheckerActivityControler getInstance() {
		if (instance == null) {
			instance = new CheckerActivityControler();
		}
		return instance;
	}

	public CheckerActivityControler setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 审核人员取本单位的请假记录
	 * 
	 * @param post
	 */
	public void GetUnitLeaves(UnitLeavesPost post) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取列表");
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetUnitLeaves);
		HttpUtil.InstanceSend(GetUnitLeaves, params, callback);
	}

	/**
	 * 14.学校班级请假查询统计 功能：统计学校各个班级的请假情况，以班级来显示结果。
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param unitId 是 int 单位Id
	 * @param sDateTime 是 String 时间
	 * @param gradeStr 是 String 年级名称
	 */
	public void GetClassSumLeaves(int unitId, String sDateTime, String gradeStr) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("unitId", String.valueOf(unitId));
		params.addBodyParameter("sDateTime", sDateTime);
		params.addBodyParameter("gradeStr", gradeStr);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetClassSumLeaves);
		HttpUtil.InstanceSend(GetClassSumLeaves, params, callback);
	}

	/**
	 * 16.教职工请假查询统计 功能：统计指定学校（单位）的教职工的请假情况
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param unitId 是	Int	单位Id
	 * @param sDateTime 是	Datetime	按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	 */
	public void GetManSumLeaves(int unitId, String sDateTime) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("unitId", String.valueOf(unitId));
		params.addBodyParameter("sDateTime", sDateTime);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetManSumLeaves);
		HttpUtil.InstanceSend(GetManSumLeaves, params, callback);
	}

	/**
	 * 15.班级学生查询统计 功能：统计指定班级的学生的请假情况，以学生来显示结果
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param unitClassId 是	Int	班级Id
	 * @param sDateTime 是	Datetime	按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	 */
	public void GetStudentSumLeaves(int unitClassId, String sDateTime) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("unitClassId", String.valueOf(unitClassId));
		params.addBodyParameter("sDateTime", sDateTime);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetStudentSumLeaves);
		HttpUtil.InstanceSend(GetStudentSumLeaves, params, callback);
	}

	/**
	 * 获取班主任关联班级
	 * 
	 * @param jiaobaohao
	 *            String 教宝号
	 */
	public void GetMyAdminClass(String jiaobaohao) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取关联班级");
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", jiaobaohao);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyAdminClass);
		HttpUtil.InstanceSend(GetMyAdminClass, params, callback);
	}

	/**
	 * 审批人审批假条，并做批注。
	 * 
	 * @param post
	 */
	public void CheckLeaveModel(CheckLeaveModelPost post) {
		DialogUtil.getInstance().getDialog(mContext, "正在传输数据");
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_CheckLeaveModel);
		HttpUtil.InstanceSend(CheckLeaveModel, params, callback);
	}

	/**
	 * 门卫取请假记录
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param numPerPage 否	int	取回的记录数量，默认20
	 * @param pageNum	否	int	第几页，默认为1
	 * @param RowCount 是	int	pageNum=1为0，第二页起从前一页的返回结果中获得
	 * @param unitId 是	Int	单位ID
	 * @param choseTime 是	Datetime	按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	 */
	public void GetGateLeaves(int numPerPage, int pageNum, int RowCount,
			int unitId, String choseTime) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取请假列表");
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		params.addBodyParameter("RowCount", String.valueOf(RowCount));
		params.addBodyParameter("unitId", String.valueOf(unitId));
		params.addBodyParameter("sDateTime", choseTime);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetGateLeaves);
		HttpUtil.InstanceSend(GetGateLeaves, params, callback);
	}

	/**
	 * 门卫登记离校返校时间
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param tabid 是	Int	请假时间记录ID
	 * @param userName 是	string	登记人姓名
	 * @param flag 是	int	0离校，1返校
	 */
	public void UpdateGateInfo(int tabid, String userName, int flag) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabid", String.valueOf(tabid));
		params.addBodyParameter("userName", userName);
		params.addBodyParameter("flag", String.valueOf(flag));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_UpdateGateInfo);
		HttpUtil.InstanceSend(UpdateGateInfo, params, callback);
	}

	/**
	 * 班主任身份获取本班学生请假的审批记录
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * @param numPerPage 否	int	取回的记录数量，默认20
	 * @param pageNum 否	int	第几页，默认为1
	 * @param RowCount  是	int	pageNum=1为0，第二页起从前一页的返回结果中获得
	 * @param unitClassId 是	Int	班级ID
	 * @param sDateTime	是	Datetime	按月查记录，月内任何一天都可以，这是申请日期，不是请假日期。
	 * @param checkFlag	是	int	0待审记录，1已审记录
	 */
	public void GetClassLeaves(int numPerPage, int pageNum, int RowCount,
			int unitClassId, String sDateTime, int checkFlag) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取班级,请稍后");
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		params.addBodyParameter("RowCount", String.valueOf(RowCount));
		params.addBodyParameter("unitClassId", String.valueOf(unitClassId));
		params.addBodyParameter("sDateTime", sDateTime);
		params.addBodyParameter("checkFlag", String.valueOf(checkFlag));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetClassLeaves);
		HttpUtil.InstanceSend(GetClassLeaves, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				try {
					dealResponseInfo("0", this.getUserTag());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mContext != null) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					System.out.println("---------ResultCode:" + ResultCode);

					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case LeaveConstant.leave_GetUnitLeaves:
						case LeaveConstant.leave_GetMyAdminClass:
						case LeaveConstant.leave_CheckLeaveModel:
						case LeaveConstant.leave_GetManSumLeaves:
						case LeaveConstant.leave_GetStudentSumLeaves:
						case LeaveConstant.leave_GetClassSumLeaves:
						case LeaveConstant.leave_GetGateLeaves:
						case LeaveConstant.leave_GetClassLeaves:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							Log.i("onSuccess", jsonObj.getString("Data") + "\n"
									+ (Integer) this.getUserTag());
							break;
						case LeaveConstant.leave_UpdateGateInfo://门卫签字
							dealResponseInfo("0", this.getUserTag());
							break;
						default:
							DialogUtil.getInstance().cannleDialog();
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}

					} else if ("999999".equals(ResultCode)) {
						ToastUtil.showMessage(mContext, "发送失败，请将html标签去掉");
						Log.i("有html标签", jsonObj + "-" + this.getUserTag());
						DialogUtil.getInstance().cannleDialog();
					} else if ("8".equals(ResultCode)) {
						DialogUtil.getInstance().cannleDialog();
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						DialogUtil.getInstance().cannleDialog();
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
						dealResponseInfo("0", 999);
					}
				} catch (Exception e) {
					DialogUtil.getInstance().cannleDialog();
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object tag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(tag);
		switch ((Integer) tag) {
		case LeaveConstant.leave_GetMyAdminClass://获取关联的班级
			result = "{\"list\":" + result + "}";
			MyAdminClasses myAdminClasses = GsonUtil.GsonToObject(result,
					MyAdminClasses.class);
			Log.d(TAG + "GetMyAdminClass", myAdminClasses.toString()+"");
			post.add(myAdminClasses);
			break;
		case LeaveConstant.leave_GetUnitLeaves://获取本单位的假条
		case LeaveConstant.leave_GetClassLeaves://班主任获取学生假条
			result = "{\"list\":" + result + "}";
			UnitClassLeaves unitClassLeaves2 = GsonUtil.GsonToObject(result,
					UnitClassLeaves.class);
			post.add(unitClassLeaves2);
			break;
		case LeaveConstant.leave_CheckLeaveModel://审核成功
			post.add(777);
			break;
		case LeaveConstant.leave_GetManSumLeaves://获取教职工统计查询
			ArrayList<SumLeavesModel> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<SumLeavesModel>>() {
					}.getType());
			post.add(list);
			break;
		case LeaveConstant.leave_GetStudentSumLeaves://获取学生统计查询
			ArrayList<StuSumLeavesModel> list0 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<StuSumLeavesModel>>() {
					}.getType());
			post.add(list0);
			break;
		case LeaveConstant.leave_GetClassSumLeaves://获取班级统计查询
			ArrayList<ClassSumLeaveModel> list2 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<ClassSumLeaveModel>>() {
					}.getType());
			post.add(list2);
			break;

		case LeaveConstant.leave_GetGateLeaves:// 门卫获取请假记录
			result = "{\"list\":" + result + "}";
			Log.e("CAGetGateLeaves", result);
			GateQueryLeaves list3 = GsonUtil.GsonToObject(result,
					GateQueryLeaves.class);
			post.add(list3);
			break;
		case LeaveConstant.leave_UpdateGateInfo:// 门卫签字成功
			post.add(true);
			break;
		case 999:
			post.add(999);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

}
