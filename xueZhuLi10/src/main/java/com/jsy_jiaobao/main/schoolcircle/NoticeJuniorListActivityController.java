package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.NoticeGetUnitClass;
import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
import com.jsy_jiaobao.po.sys.GetUserClass;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 下级单位列表界面controller
 * 
 * @author admin
 */
public class NoticeJuniorListActivityController implements ConstantUrl {
	private static NoticeJuniorListActivityController instance;
	private Activity mContext;

	public static synchronized final NoticeJuniorListActivityController getInstance() {
		if (instance == null) {
			instance = new NoticeJuniorListActivityController();
		}
		return instance;
	}

	public NoticeJuniorListActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 取教师关联的班级 
	 */
	public void getmyUserClass(int UnitID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_train_getmyUserClass);
		HttpUtil.InstanceSend(getmyUserClass, params, callback);
	}

	/**
	 * 取本单位的所有下级单位基础信息
	 * 客户端通过本接口获取上级单位的基础信息数据。基础信息数据包括名称，类型，区域，上级单位ID，栏目文章数量和文章更新数据等。 
	 */
	public void getMySubUnitInfo(int UnitID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_getMySubUnitInfo);
		HttpUtil.InstanceSend(getMySubUnitInfo, params, callback);
	}

	/**
	 * 获取指定学校的所有班级基础数据 客户端通过本接口获取指定学校的所有班级基础数据。基础信息数据包括名称，单位ID，栏目文章数量和文章更新数据等
	 */
	public void getSchoolClassInfo(int UnitID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_getSchoolClassInfo);
		HttpUtil.InstanceSend(getSchoolClassInfo, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				if (!mContext.isFinishing()) {
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

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		switch ((Integer) userTag) {
		case Constant.msgcenter_notice_getMySubUnitInfo:
			post.add(Constant.msgcenter_notice_getMySubUnitInfo);
			result = "{\"list\":" + result + "}";
			NoticeGetUnitInfo getUnitInfo = GsonUtil.GsonToObject(result,
					NoticeGetUnitInfo.class);
			post.add(getUnitInfo);
			break;
		case Constant.msgcenter_notice_getSchoolClassInfo:
			post.add(Constant.msgcenter_notice_getSchoolClassInfo);
			result = "{\"list\":" + result + "}";
			NoticeGetUnitClass getUnitClass = GsonUtil.GsonToObject(result,
					NoticeGetUnitClass.class);
			post.add(getUnitClass);
			break;
		case Constant.msgcenter_train_getmyUserClass:
			post.add(Constant.msgcenter_train_getmyUserClass);
			result = "{\"list\":" + result + "}";
			GetUserClass getUnitClass1 = GsonUtil.GsonToObject(result,
					GetUserClass.class);
			post.add(getUnitClass1);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
