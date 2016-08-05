package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;
import android.app.Activity;
import android.util.Log;
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
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.UserClassList;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class CheckerScreenConditionActivityControler implements ConstantUrl {
	private static final String TAG = "CheckerScreenConditionActivityControler";
	private static CheckerScreenConditionActivityControler instance;
	private Activity mContext;

	public static synchronized final CheckerScreenConditionActivityControler getInstance() {
		if (instance == null) {
			instance = new CheckerScreenConditionActivityControler();
		}
		return instance;
	}

	public CheckerScreenConditionActivityControler setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 获取班主任关联班级
	 * 
	 * @param jiaobaohao 教宝号
	 */
	public void GetMyAdminClass(String jiaobaohao) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取班级,请稍后");
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", jiaobaohao);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyAdminClass);
		HttpUtil.InstanceSend(GetMyAdminClass, params, callback);
	}
	/**
	 * 获取所有年级班级
	 * @param UID 班级ID
	 */
	public void getunitclass(int UID) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取班级,请稍后");
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UID));
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_getunitclass);
		HttpUtil.InstanceSend(getunitclass, params, callback);
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
						case LeaveConstant.leave_GetMyAdminClass:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case LeaveConstant.leave_getunitclass:
							String result = Des.decrypt(jsonObj
									.getString("Data"),
									MessageCenterActivity.sp_sys.getString(
											"ClientKey", ""));
							dealResponseInfo(result, this.getUserTag());
							break;
						default:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}

					} else if ("8".equals(ResultCode)) {
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
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
		case LeaveConstant.leave_GetMyAdminClass:
			result = "{\"list\":" + result + "}";
			MyAdminClasses myAdminClasses = GsonUtil.GsonToObject(result,
					MyAdminClasses.class);
			Log.d(TAG + "GetMyAdminClass", myAdminClasses.toString());
			post.add(myAdminClasses);
			break;
		case LeaveConstant.leave_getunitclass:
			result = "{\"list\":" + result + "}";
			UserClassList classList = GsonUtil.GsonToList(result,
					UserClassList.class);
			post.add(classList);
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
