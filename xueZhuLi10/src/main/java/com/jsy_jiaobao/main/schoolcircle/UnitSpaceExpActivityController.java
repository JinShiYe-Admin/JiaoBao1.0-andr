package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
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
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.NoticeGetUnitClass;
import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
import com.jsy_jiaobao.po.personal.Userinfo;
import com.jsy_jiaobao.po.sys.UnitGroupInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 单位成员的Controller
 */
public class UnitSpaceExpActivityController implements ConstantUrl {
	private static UnitSpaceExpActivityController instance;
	private Activity mcontext;

	public static synchronized UnitSpaceExpActivityController getInstance() {
		if (instance == null) {
			instance = new UnitSpaceExpActivityController();
		}
		return instance;
	}

	public UnitSpaceExpActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}

	/**
	 * 取单位简介
	 */
	public void getintroduce(String unitid, String uType) {
		DialogUtil.getInstance().getDialog(mcontext,
				mcontext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("unitid", unitid);
		params.addBodyParameter("uType", uType);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getintroduce);
		HttpUtil.InstanceNewSend(ACache.get(mcontext.getApplicationContext())
				.getAsString("MainUrl") + getintroduce, params, callback);

	}

	/**
	 * 取单位内所有组 应用系统通过单位ID，获取单位所有组
	 */
	public void getUnitGroups(int UID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_chat_getUnitGroups);
		HttpUtil.InstanceSend(getUnitGroups, params, callback);
	}

	/**
	 * 取单位内所有人员
	 */
	public void getUserInfoByUnitID(int unitID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(unitID));
		params.addBodyParameter("AccIDFilter", String.valueOf(0));// 0全部；1有账号
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_chat_getUserInfoByUnitID);
		HttpUtil.InstanceSend(getUserInfoByUnitID, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mcontext) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mcontext)) {
					ToastUtil.showMessage(mcontext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mcontext) {
				DialogUtil.getInstance().cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						dealResponseInfo(jsonObj.getString("Data"),
								this.getUserTag());

					} else if ("8".equals(ResultCode)) {
						LoginActivityController.getInstance().helloService(
								mcontext);
						dealResponseInfo("", this.getUserTag());
					} else {
						dealResponseInfo("", this.getUserTag());
						ToastUtil.showMessage(mcontext,
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("", this.getUserTag());
					ToastUtil.showMessage(mcontext, mcontext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		switch ((Integer) userTag) {
		case Constant.msgcenter_show_getintroduce:
			post.add(Constant.msgcenter_show_getintroduce);
			post.add(result);
			break;
		case Constant.msgcenter_chat_getUnitGroups:
			post.add(Constant.msgcenter_chat_getUnitGroups);
			try {
				String data = Des.decrypt(result,
						BaseActivity.sp_sys.getString("ClientKey", ""));
				ArrayList<UnitGroupInfo> list = GsonUtil.GsonToList(data,
						new TypeToken<ArrayList<UnitGroupInfo>>() {
						}.getType());
				post.add(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constant.msgcenter_chat_getUserInfoByUnitID:
			post.add(Constant.msgcenter_chat_getUserInfoByUnitID);
			try {
				String data = Des.decrypt(result,
						BaseActivity.sp_sys.getString("ClientKey", ""));
				ArrayList<Userinfo> list1 = GsonUtil.GsonToList(data,
						new TypeToken<ArrayList<Userinfo>>() {
						}.getType());
				post.add(list1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constant.msgcenter_notice_getMySubUnitInfo:
			post.add(Constant.msgcenter_notice_getMySubUnitInfo);
			try {
				String data = Des.decrypt(result,
						BaseActivity.sp_sys.getString("ClientKey", ""));
				result = "{\"list\":" + data + "}";
				NoticeGetUnitInfo getUnitInfo = GsonUtil.GsonToObject(result,
						NoticeGetUnitInfo.class);
				post.add(getUnitInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constant.msgcenter_show_getSchoolClassInfo:
			post.add(Constant.msgcenter_show_getSchoolClassInfo);
			try {
				String data = Des.decrypt(result,
						BaseActivity.sp_sys.getString("ClientKey", ""));
				result = "{\"list\":" + data + "}";
				NoticeGetUnitClass getUnitClass = GsonUtil.GsonToObject(result,
						NoticeGetUnitClass.class);
				post.add(getUnitClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}