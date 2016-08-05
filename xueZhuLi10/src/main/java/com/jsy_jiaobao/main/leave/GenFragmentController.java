package com.jsy_jiaobao.main.leave;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.GenStuInfos;
import com.jsy_jiaobao.po.leave.Leave;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyLeaves;
import com.jsy_jiaobao.po.leave.MyLeavesPost;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class GenFragmentController implements ConstantUrl {

	private static GenFragmentController instance;

	private Context mContext;

	public static synchronized  GenFragmentController getInstance() {
		if (instance == null) {
			instance = new GenFragmentController();
		}
		return instance;
	}

	public GenFragmentController setContext(Fragment fragment) {
		mContext = fragment.getActivity();
		return this;
	}

	/**
	 * 功能说明：应用系统通过帐户ID，获得学生数据（家长身份）
	 * 
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * accId	是	Int	用户教宝号
	 */
	public void GetMyStdInfo(String jiaobaohao) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", jiaobaohao);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyStdInfo);
		HttpUtil.InstanceSend(GetMyStdInfo, params, callback);
	}

	/**
	 * 功能说明：生成一条请假条记录
	 * 
	 * @param post 生成请假记录要传递的数据
	 */
	public void NewLeaveModel(Leave post) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_NewLeaveModel);
		HttpUtil.InstanceSend(NewLeavelModel, params, callback);
	}

	/**
	 * 功能说明：获取请假记录列表
	 * 
	 * @param post 获取请假记录 发送的请求数据
	 */
	public void GetMyLeaves(MyLeavesPost post) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		System.out.println(post.getsDateTime());
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyLeaves);
		HttpUtil.InstanceSend(GetMyLeaves, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, R.string.error_internet);
				}
				DialogUtil.getInstance().cannleDialog();
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if (!TextUtils.isEmpty(ResultCode)) {
						if ("0".equals(ResultCode)) {// 成功
							switch ((Integer) this.getUserTag()) {
							case LeaveConstant.leave_GetMyStdInfo:// 获取学生信息
								dealResponseInfo(jsonObj.getString("Data"),
										this.getUserTag());
								break;
							case LeaveConstant.leave_GetMyLeaves:// 获取请假记录
								dealResponseInfo(jsonObj.getString("Data"),
										this.getUserTag());
								Log.i("GetMyLeaves", jsonObj.getString("Data"));
								break;
							case LeaveConstant.leave_NewLeaveModel:// 请假成功
								dealResponseInfo("0", this.getUserTag());
								break;
							default:
								break;
							}
						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
							DialogUtil.getInstance().cannleDialog();
						} else if ("999999".equals(ResultCode)) {							
							ToastUtil.showMessage(mContext, "发送失败，请将html标签去掉");
							Log.i("有html标签", jsonObj + "-" + this.getUserTag());
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

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case LeaveConstant.leave_GetMyStdInfo:
			result = "{\"list\":" + result + "}";
			GenStuInfos genStuInfos =  GsonUtil.GsonToObject(
					result, GenStuInfos.class);
			post.add(genStuInfos);
			break;
		case LeaveConstant.leave_GetMyLeaves:
			result = "{\"list\":" + result + "}";
			MyLeaves myLeaves = GsonUtil.GsonToObject(result, MyLeaves.class);
			post.add(myLeaves);
			break;
		case LeaveConstant.leave_NewLeaveModel:
			post.add(true);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
