package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.sys.GetStuInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * 功能说明：班主任请假时获取本班学生名单
 * 
 * @author MSL
 * 
 */
public class AllClassNamesActivityController implements ConstantUrl {
	private static final String TAG = "AllClssNAController";
	private Context mContext;
	private static AllClassNamesActivityController instance;

	public static synchronized  AllClassNamesActivityController getInstance() {
		if (instance == null) {
			instance = new AllClassNamesActivityController();
		}
		return instance;
	}

	public AllClassNamesActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 获取班主任关联班级
	 * 
	 * @param jiaobaohao
	 *            教宝号
	 */
	public void GetMyAdminClass(String jiaobaohao) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", jiaobaohao);
		Log.d(TAG, jiaobaohao);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyAdminClass);
		HttpUtil.InstanceSend(GetMyAdminClass, params, callback);
	}

	/**
	 * 获取班级学生列表
	 * 
	 * @param classId
	 *            班级ID
	 */
	public void getClassStdInfo(int classId) {
		RequestParams params = new RequestParams();
		Log.d(TAG, classId + "");
		params.addBodyParameter("UID", String.valueOf(classId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_chat_getClassStdInfo);
		HttpUtil.InstanceSend(getClassStdInfo, params, callback);
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
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if (!TextUtils.isEmpty(ResultCode)) {
						if ("0".equals(ResultCode)) {
							switch ((Integer) this.getUserTag()) {
							case LeaveConstant.leave_GetMyAdminClass:// 获取班主任关联班级
								dealResponseInfo(jsonObj.getString("Data"),
										this.getUserTag());
								break;
							case Constant.msgcenter_chat_getClassStdInfo:// 获取班级学生列表
								String result = Des.decrypt(jsonObj
										.getString("Data"),
										MessageCenterActivity.sp_sys.getString(
												"ClientKey", ""));
								dealResponseInfo(result, this.getUserTag());
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
		MyAdminClasses myAdminClasses ;
		switch ((Integer) userTag) {
		case LeaveConstant.leave_GetMyAdminClass:
			result = "{\"list\":" + result + "}";
			myAdminClasses = GsonUtil
					.GsonToObject(result, MyAdminClasses.class);
			post.add(myAdminClasses);
			break;
		case Constant.msgcenter_chat_getClassStdInfo:
			result = "{\"list\":" + result + "}";
			GetStuInfo getStuInfo = GsonUtil.GsonToObject(result,
					GetStuInfo.class);
			post.add(getStuInfo);
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
