package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveDetail;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.text.TextUtils;

public class LeaveDetailsActivityControl implements ConstantUrl {
	private Context mContext;
	private static LeaveDetailsActivityControl instance;

	public static synchronized LeaveDetailsActivityControl getInstance() {
		if (instance == null) {
			instance = new LeaveDetailsActivityControl();
		}
		return instance;
	}

	public LeaveDetailsActivityControl setContext(Context mContext) {
		this.mContext = mContext;
		return this;
	}

	/**
	 * 6.取一个假条的明细信息
	 * @param tabId 记录id
	 */
	public void GetLeaveModel(int tabId) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(tabId));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetLeaveModel);
		HttpUtil.InstanceSend(GetLeaveModel, params, callback);
	}

	/**
	 * 11.门卫登记离校返校时间 功能：门卫登记离校返校时间。
	 * 
	 * @param tabid 请假条记录ID
	 * @param userName 登记人姓名
	 * @param flag 0离校，1返校
	 */
	public void UpdateGateInfol(int tabid, String userName, int flag) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabid", String.valueOf(tabid));
		params.addBodyParameter("userName", userName);
		params.addBodyParameter("flag", String.valueOf(flag));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetLeaveModel);
		HttpUtil.InstanceSend(GetLeaveModel, params, callback);
	}

	/**
	 * 删除假条
	 * 
	 * @param tabId 假条记录Id
	 */
	public void DeleteLeaveModel(int tabId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(tabId));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_DeleteLeaveModel);
		HttpUtil.InstanceSend(DeleteLeaveModel, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false", this.getUserTag());
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
				DialogUtil.getInstance().cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);//
					String ResultCode = jsonObj.getString("ResultCode");
					if (!TextUtils.isEmpty(ResultCode)) {
						if ("0".equals(ResultCode)) {
							switch ((Integer) this.getUserTag()) {
							case LeaveConstant.leave_GetLeaveModel:
								dealResponseInfo(jsonObj.getString("Data"),
										this.getUserTag());
								break;
							case LeaveConstant.leave_DeleteLeaveModel:
							case LeaveConstant.leave_UpdateGateInfo:
								dealResponseInfo("0", this.getUserTag());
								break;
							default:
								break;
							}
						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("0", 999);
						}
					}
				} catch (Exception e) {
					dealResponseInfo("false", this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case LeaveConstant.leave_GetLeaveModel:
			LeaveDetail leaveDetail = GsonUtil.GsonToObject(result,
					LeaveDetail.class);
			post.add(leaveDetail);
			break;
		case LeaveConstant.leave_DeleteLeaveModel:
		case LeaveConstant.leave_UpdateGateInfo:
			post.add(true);
		case 999:
			post.add(999);
			break;
		default:
			post.add(false);
			break;
		}
		EventBusUtil.post(post);
	}
}