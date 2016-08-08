package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
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
import com.jsy_jiaobao.po.personal.NoticeGetArthInfo;
import com.jsy_jiaobao.po.personal.UnitNoticResult;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 文章列表的Controller
 */
public class ArtListActivityController implements ConstantUrl {
	private static ArtListActivityController instance;
	private Activity mContext;

	public static synchronized ArtListActivityController getInstance() {
		if (instance == null) {
			instance = new ArtListActivityController();
		}
		return instance;
	}

	public ArtListActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 取本单位栏目文章 客户端通过本接口获取本单位栏目文章。
	 */
	public void ArthListIndex(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_ArthListIndex);
		HttpUtil.InstanceSend(ArthListIndex, params, callback);
	}

	/**
	 * 切换单位
	 *
	 * @param userClass
	 */
	private UserClass userClass;

	public void changeCurUnit(UserClass userClass) {
		int RoleIdentity = 0;
		this.userClass = userClass;
		for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
			UserIdentity userIdentity = Constant.listUserIdentity.get(i);
			for (int j = 0; j < userIdentity.getUserClasses().size(); j++) {
				UserClass item = userIdentity.getUserClasses().get(j);
				if (item.getClassID() == userClass.getClassID()) {
					RoleIdentity = userIdentity.getRoleIdentity();
					break;
				}
			}
			if (RoleIdentity > 0) {
				break;
			}
		}
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(userClass.getSchoolID()));
		params.addBodyParameter("uType", String.valueOf(RoleIdentity));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_train_changeUnit);
		HttpUtil.InstanceSend(changeCurUnit, params, callback);
	}

	/**
	 * 获取用户所在单位的详情
	 */
	private void httpGetUserInfo() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		params.addBodyParameter("UID", String.valueOf(userClass.getSchoolID()));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_train_getUserInfo);
		HttpUtil.InstanceSend(user_getUserInfo, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				if (!mContext.isFinishing()) {
					DialogUtil.getInstance().cannleDialog();
					dealResponseInfo("", this.getUserTag());
				}
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);

				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (!mContext.isFinishing()) {

				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						String data = Des.decrypt(jsonObj.getString("Data"),
								MessageCenterActivity.sp_sys.getString(
										"ClientKey", ""));
						dealResponseInfo(data, this.getUserTag());

					} else if ("8".equals(ResultCode)) {
						dealResponseInfo("", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						dealResponseInfo("", this.getUserTag());
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("", this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		switch ((Integer) userTag) {
		case Constant.msgcenter_notice_ArthListIndex:
			DialogUtil.getInstance().cannleDialog();
			post.add(Constant.msgcenter_notice_ArthListIndex);
			result = "{\"list\":" + result + "}";
			NoticeGetArthInfo getArthInfo = GsonUtil.GsonToObject(result,
					NoticeGetArthInfo.class);
			post.add(getArthInfo);
			break;
		case Constant.msgcenter_train_getUnitNotics:
			DialogUtil.getInstance().cannleDialog();
			post.add(Constant.msgcenter_train_getUnitNotics);
			UnitNoticResult getNoticResult = GsonUtil.GsonToObject(result,
					UnitNoticResult.class);
			post.add(getNoticResult);
			break;
		case Constant.msgcenter_train_changeUnit:
			BaseActivity.editor.putInt("UnitID", userClass.getSchoolID());
			BaseActivity.editor.putInt("UnitType", 3);
			BaseActivity.editor.putString("UnitName", userClass.getClassName());
			BaseActivity.editor.putString("TabIDStr", userClass.getTabIDStr());
			BaseActivity.editor.commit();
			httpGetUserInfo();
			post.add(99999);
			break;
		case Constant.msgcenter_train_getUserInfo:
			try {
				JSONObject jsonData = new JSONObject(result);
				BaseActivity.editor.putInt("UserID", jsonData.getInt("UserID"));
				BaseActivity.editor.putInt("UserType",
						jsonData.getInt("UserType"));
				BaseActivity.editor.putInt("isAdmin",
						jsonData.getInt("isAdmin"));
				BaseActivity.editor.putString("UserName",
						jsonData.getString("UserName"));
				BaseActivity.editor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			post.add(Constant.msgcenter_train_getUserInfo);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
