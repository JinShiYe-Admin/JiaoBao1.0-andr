package com.jsy_jiaobao.main.system;

import android.content.Context;
import android.util.Log;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Base64Helper;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Coder;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.RsaHelper;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * LoginActivity的Controller
 */
public class LoginActivityController implements ConstantUrl {
	private static LoginActivityController instance;
	private Context mContext;

	public static synchronized LoginActivityController getInstance() {
		if (instance == null) {
			instance = new LoginActivityController();
		}
		return instance;
	}

	public LoginActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	String str_time;
	boolean getInternet = false;

	// 获取网络时间
	public void getTime() {
		LogUtils.e("----------getTime");
		getInternet = false;
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				"http://www.jiaobao.net/jbclient/Account/getcurTime",
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						try {
							if (!getInternet) {
								getInternet = true;
								Date date = new Date(System.currentTimeMillis());
								str_time = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss", Locale
												.getDefault()).format(date);
								httpUserLogin();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							if (!getInternet) {
								getInternet = true;
								JSONObject jsonObj = new JSONObject(arg0.result);
								str_time = jsonObj.getString("Data");
								httpUserLogin();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 通讯握手 base64(MD5(Ver + IAMSCID + hellostr + ClientKey))
	 */
	private String hellostr;

	public void helloService(Context mContext) {
		try {
			Log.d("helloService","------------------helloService");
			this.setContext(mContext);
			ToastUtil
					.showMessage(mContext, R.string.login_loginFailed_andAgain);
			hellostr = BaseUtils.getRandomString(4);
			BaseActivity.editor.putString("hellostr", hellostr).commit();
			String sign = BaseUtils
					.getVersion(mContext.getApplicationContext())
					+ BaseActivity.sp_sys.getString("IAMSCID", "")
					+ hellostr
					+ BaseActivity.sp_sys.getString("ClientKey", "");
			RequestParams params = new RequestParams();
			params.addBodyParameter("CliVer",
					BaseUtils.getVersion(mContext.getApplicationContext()));
			params.addBodyParameter("hellostr", hellostr);
			params.addBodyParameter("IAMSCID",
					BaseActivity.sp_sys.getString("IAMSCID", ""));
			params.addBodyParameter("Sign",
					Base64Helper.encode(Coder.encryptMD5(sign.getBytes())));
			CusCallBack callback = new CusCallBack();
			callback.setUserTag("hello");
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, sys_hello,
					params, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户登录
	 */
	public void httpUserLogin() {
		try {

			String loginClass = "{\"UserName\":\""
					+ BaseActivity.sp.getString("str_username", "")
					+ "\",\"UserPW\":\""
					+ BaseActivity.sp.getString("UserPW", "")
					+ "\",\"TimeStamp\":\"" + str_time + "\"}";
			String rsaLoginstr = RsaHelper.encryptDataFromStr(loginClass);

			String sign = BaseUtils.getVersion(mContext) + rsaLoginstr
					+ BaseActivity.sp_sys.getString("ClientKey", "") + str_time;
			String enMD5 = Base64Helper
					.encode(Coder.encryptMD5(sign.getBytes()));
			RequestParams params = new RequestParams();
			params.addBodyParameter("CliVer", BaseUtils.getVersion(mContext));
			params.addBodyParameter("Loginstr", rsaLoginstr);
			params.addBodyParameter("TimeStamp", str_time);
			params.addBodyParameter("Sign", enMD5);
			CusCallBack callback = new CusCallBack();
			callback.setUserTag("login");
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
					user_login, params, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CusCallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext,
							R.string.data_communication_failed);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			String tag = (String) this.getUserTag();
			try {
				if (tag.equals("login")) {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						ToastUtil.showMessage(mContext,
								R.string.login_loginSuccess);
						ArrayList<Object> post = new ArrayList<>();
						post.add(Constant.system_login_again);
						EventBusUtil.post(post);
						try {
							String url = (String) BaseActivity.HttpPost.get(0);
							System.out.println("-----------request  url" + url);
							RequestParams params = (RequestParams) BaseActivity.HttpPost
									.get(1);
							System.out.println("-----------request  params"
									+ params);
							@SuppressWarnings("unchecked")
							RequestCallBack<String> callback = (RequestCallBack<String>) BaseActivity.HttpPost
									.get(2);
							System.out.println("-----------request  callback"
									+ callback);
							HttpUtil.getInstance().send(
									HttpRequest.HttpMethod.POST, url, params,
									callback);
						} catch (Exception e) {
							e.printStackTrace();
						}
						getToken();
					} else {
						ToastUtil.showMessage(mContext,
								R.string.login_data_communication_failed);
					}
				} else {
					System.out.println(arg0);
					JSONObject jsonObj = new JSONObject(arg0.result);
					String result = jsonObj.getString("ResultCode");
					if ("0".equals(result)) {
						String data = Des.decrypt(jsonObj.getString("Data"),
								BaseActivity.sp_sys.getString("ClientKey", ""));
						if (data.equals(hellostr)) {
							getTime();
						}
					}
				}
			} catch (Exception e) {
				ToastUtil.showMessage(mContext,
						R.string.login_data_communication_failed);
			}
		}

	}

	protected void getToken() {
		try {
			LogUtils.e("----------getToken");
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getToken,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							ToastUtil.showMessage(mContext,
									R.string.login_data_communication_failed);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");
								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(jsonObj
											.getString("Data"),
											BaseActivity.sp_sys.getString(
													"ClientKey", ""));
									LogUtils.e("---------" + data);
									loginJBApp(data);
								} else {
									BaseUtils
											.shortToast(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.login_get_token)
															+ ResultCode
															+ "\n"
															+ jsonObj
																	.getString("ResultDesc"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loginJBApp(String data) {
		try {
			LogUtils.e("----------loginJBApp");
			RequestParams params = new RequestParams();
			params.addBodyParameter("cliToken", data);
			HttpUtil.getInstanceNew().send(
					HttpRequest.HttpMethod.POST,
					ACache.get(mContext.getApplicationContext()).getAsString(
							"MainUrl")
							+ checkToken, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}