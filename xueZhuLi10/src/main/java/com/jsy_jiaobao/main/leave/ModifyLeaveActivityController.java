package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.UpdateLeaveModelPost;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class ModifyLeaveActivityController implements ConstantUrl {
	private static ModifyLeaveActivityController instance;
	private Context mContext;

	public static synchronized  ModifyLeaveActivityController getInstance() {
		if (instance == null) {
			instance = new ModifyLeaveActivityController();
		}
		return instance;
	}

	public ModifyLeaveActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 更新一条请假记录
	 * 
	 * @param post 请求信息
	 */
	public void UpdateLeaveModel(UpdateLeaveModelPost post) {
		// Log.i("UpdateLeaveModel", "更新一条请假记录");
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_UpdateLeaveModel);
		HttpUtil.InstanceSend(UpdateLeaveModel, params, callback);
	}

	/**
	 * 给一个假条新增一个时间段
	 * 
	 * @param leaveId
	 *            假条记录Id
	 * @param unitId
	 *            单位ID
	 * @param sDateTime
	 *            请假时间段开始时间
	 * @param eDateTime
	 *            请假时间段结束时间
	 */
	public void AddLeaveTime(int leaveId, int unitId, String sDateTime,
			String eDateTime) {
		// Log.i("AddLeaveTime", "给一个假条新增一个时间段");
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("leaveId", String.valueOf(leaveId));
		params.addBodyParameter("unitId", String.valueOf(unitId));
		params.addBodyParameter("sDateTime", sDateTime);
		params.addBodyParameter("eDateTime", eDateTime);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_AddLeaveTime);
		HttpUtil.InstanceSend(AddLeaveTime, params, callback);
	}

	/**
	 * 更新假条的一个时间段
	 * 
	 * @param tabId
	 *            时间段记录Id
	 * @param sDateTime
	 *            请假时间段开始时间
	 * @param eDateTime
	 *            请假时间段结束时间
	 */
	public void UpdateLeaveTime(int tabId, String sDateTime, String eDateTime) {
		// Log.i("UpdateLeaveTime", "更新假条的一个时间段");
		RequestParams params = new RequestParams();
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		params.addBodyParameter("tabId", String.valueOf(tabId));
		params.addBodyParameter("sDateTime", sDateTime);
		params.addBodyParameter("eDateTime", eDateTime);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_UpdateLeaveTime);
		HttpUtil.InstanceSend(UpdateLeaveTime, params, callback);
	}

	/**
	 * 删除假条的一个时间段
	 * 
	 * @param tabId
	 *            时间段记录Id
	 */
	public void DeleteLeaveTime(int tabId) {
		// Log.i("DeleteLeaveTime", "删除假条的一个时间段");
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(tabId));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_DeleteLeaveTime);
		HttpUtil.InstanceSend(DeleteLeaveTime, params, callback);
	}

//	/**
//	 * 删除假条
//	 *
//	 * @param tabId
//	 *            假条记录Id
//	 */
//	public void DeleteLeaveModel(int tabId) {
//		// Log.i("DeleteLeaveModel", "删除假条");
//		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("tabId", String.valueOf(tabId));
//		CallBack callback = new CallBack();
//		callback.setUserTag(LeaveConstant.leave_DeleteLeaveModel);
//		HttpUtil.InstanceSend(DeleteLeaveModel, params, callback);
//	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo( this.getUserTag());
				DialogUtil.getInstance().cannleDialog();
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, R.string.error_internet);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if (!TextUtils.isEmpty(ResultCode)) {

						if ("0".equals(ResultCode)) {
							switch ((Integer) this.getUserTag()) {
							case LeaveConstant.leave_DeleteLeaveModel:
								dealResponseInfo(this.getUserTag());
								break;
							case LeaveConstant.leave_UpdateLeaveModel:
								dealResponseInfo( this.getUserTag());
								break;
							case LeaveConstant.leave_DeleteLeaveTime:
								dealResponseInfo(this.getUserTag());
								break;
							case LeaveConstant.leave_UpdateLeaveTime:
								dealResponseInfo(this.getUserTag());
								break;
							case LeaveConstant.leave_AddLeaveTime:
								dealResponseInfo( this.getUserTag());
								break;
							default:
								break;
							}
						} else if ("999999".equals(ResultCode)) {
							ToastUtil.showMessage(mContext, "发送失败，请将html标签去掉");
							Log.i("有html标签", jsonObj + "-" + this.getUserTag());
							DialogUtil.getInstance().cannleDialog();
						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
							DialogUtil.getInstance().cannleDialog();
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							DialogUtil.getInstance().cannleDialog();
						}
					}
				} catch (Exception e) {
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
					DialogUtil.getInstance().cannleDialog();
				}
			}
		}
	}

	private void dealResponseInfo( Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case LeaveConstant.leave_DeleteLeaveModel:
			post.add(true);
			break;
		case LeaveConstant.leave_UpdateLeaveModel:
			post.add(true);
			break;
		case LeaveConstant.leave_DeleteLeaveTime:
			post.add(true);
			break;
		case LeaveConstant.leave_AddLeaveTime:
			post.add(true);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
