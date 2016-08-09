package com.jsy_jiaobao.main.system;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.RsaHelper;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.MyMobileUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 个人中心修改昵称，姓名，密码，加入单位的Activity的Controller
 */
public class PersonalCenterChangeActivityController implements ConstantUrl {
	private static PersonalCenterChangeActivityController instance;
	private Context mContext;

	public static synchronized PersonalCenterChangeActivityController getInstance() {
		if (instance == null) {
			instance = new PersonalCenterChangeActivityController();
		}
		return instance;
	}

	public PersonalCenterChangeActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	/** 在修改昵称时检查昵称是否重复 */
	public void checkAccN(String nickname) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("nickname", nickname);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_checkAccN);
		HttpUtil.InstanceSend(checkAccN, params, callback);
	}

	/** 修改帐户信息的昵称和姓名 */
	public void UpateRecAcc(String nickname, String truename) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		params.addBodyParameter("nickname", nickname);
		params.addBodyParameter("trueName", truename);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_UpateRecAcc);
		HttpUtil.InstanceSend(UpateRecAcc, params, callback);
	}

	/** 验证旧密码后修改帐户密码（不是重置密码），参数须加密 */
	public void ChangePW(String oldpwd, String newpwd) {
		String regAccIdStr = "{\"AccId\":\""
				+ BaseActivity.sp.getString("JiaoBaoHao", "") + "\",\"opw\":\""
				+ oldpwd + "\",\"npw\":\"" + newpwd + "\"}";
		String rsaLoginstr = RsaHelper.encryptDataFromStr(regAccIdStr);
		RequestParams params = new RequestParams();
		params.addBodyParameter("pwobjstr", rsaLoginstr);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_ChangePW);
		HttpUtil.InstanceSend(ChangePW, params, callback);
	}

	/** 根据手机号码自动配置的用户信息，获取用户所在单位及身份信息，每一条用户信息匹配一条记录。如果在一个单位有相同的身份，则会显示两条相同的记录。 */
	public void GetMyMobileUnitList() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_GetMyMobileUnitList);
		HttpUtil.InstanceSend(GetMyMobileUnitList, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false", this.getUserTag());
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
					JSONObject jsonObj = new JSONObject(arg0.result);// {"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.user_regist_checkAccN:
						case Constant.user_regist_GetMyMobileUnitList:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case Constant.user_regist_UpateRecAcc:
						case Constant.user_regist_ChangePW:
							dealResponseInfo("success", this.getUserTag());
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
						dealResponseInfo("false", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
						dealResponseInfo("false", this.getUserTag());
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
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.user_regist_checkAccN:
		case Constant.user_regist_UpateRecAcc:
		case Constant.user_regist_ChangePW:
			post.add(result);
			break;
		case Constant.user_regist_GetMyMobileUnitList:
			ArrayList<MyMobileUnit> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<MyMobileUnit>>() {
					}.getType());
			post.add(list);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}