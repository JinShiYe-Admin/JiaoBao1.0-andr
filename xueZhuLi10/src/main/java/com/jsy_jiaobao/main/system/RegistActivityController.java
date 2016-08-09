package com.jsy_jiaobao.main.system;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

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
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RegistActivityController implements ConstantUrl{
	private static RegistActivityController instance;
	private BitmapUtils bitmapUtils;
	private Context mContext;
	public static synchronized final RegistActivityController getInstance() {
		if (instance == null) {
			instance = new RegistActivityController();
		}
		return instance;
	}

	public RegistActivityController setContext(Context pActivity) {
		mContext = pActivity;
		bitmapUtils = new BitmapUtils(mContext);
		return this;
	}
	/**
	 * 检查手机是否重复 功能：检查手机号码是否重复（已注册），true没有注册，false有注册
	 */
	public void checkMobileAcc(String phone){
		RequestParams params = new RequestParams();
		params.addBodyParameter("accid",phone);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_checkmobileAcc);
		HttpUtil.InstanceSend(checkmobileAcc,params, callback);
	}
	/**
	 * 功能：用户输入手机号码，并输入图片验证码，app客户端把图片验证码和手机发回服务器，服务器验证后向手机发送验证码（6位数字）。
	 */
	public void SendCheckCode(String phone,String picnumber){
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilenum",phone);
		params.addBodyParameter("vCode",picnumber);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_SendCheckCode);
		HttpUtil.InstanceSend(SendCheckCode,params, callback);
	}
	/**
	 * 功能：重置用户密码时发送手机验证码，系统会检查手机号码对应的帐户信息是否存在，存在才能发送。
	 */
	public void ReSendCheckCode(String phone,String picnumber){
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilenum",phone);
		params.addBodyParameter("vCode",picnumber);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_ReSendCheckCode);
		HttpUtil.InstanceSend(ReSendCheckCode,params, callback);
	}
	/**
	 * 功能：用户输入收到的验证码，并输入图片验证码，app客户端把图片验证码和手机验证码发回服务器，检查用户输入是否正确。
	 */
	public void RegCheckMobileVcode(String phone,String picnumber,String phonenumber){
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilenum",phone);
		params.addBodyParameter("vCode",picnumber);
		params.addBodyParameter("checkcode",phonenumber);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_RegCheckMobileVcode);
		HttpUtil.InstanceSend(RegCheckMobileVcode,params, callback);
	}
	/**
	 * 功能：用于在重置密码时验证用户手机（与注册时验证手机的接口不一样）。
	 */
	public void CheckMobileVcode(String phone,String picnumber,String phonenumber){
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobilenum",phone);
		params.addBodyParameter("vCode",picnumber);
		params.addBodyParameter("checkcode",phonenumber);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.user_regist_CheckMobileVcode);
		HttpUtil.InstanceSend(CheckMobileVcode,params, callback);
	}
	/**
	 * 验证手机号码正确后，用户输入输入密码，app把用户注册手机和密码发给服务器完成注册手续
	 */
	public void RegAccId(String phone,String pwd,String time){
		try {
			LogUtils.e("----------RegAccId");
			String regAccIdStr = "{\"MobileNum\":\""+phone+"\",\"UserPW\":\""+pwd+"\",\"TimeStamp\":\""+time+"\"}";
			String rsaLoginstr = RsaHelper.encryptDataFromStr(regAccIdStr);
			String sign = BaseUtils.getVersion(mContext)+rsaLoginstr+ BaseActivity.sp_sys.getString("ClientKey", "")+time;
			String enMD5 = Base64Helper.encode(Coder.encryptMD5(sign.getBytes()));
			RequestParams params = new RequestParams();
			params.addBodyParameter("CliVer",BaseUtils.getVersion(mContext));
			params.addBodyParameter("IAMSCID",BaseActivity.sp_sys.getString("IAMSCID", ""));
			params.addBodyParameter("regAccIdStr",rsaLoginstr);
			params.addBodyParameter("TimeStamp",time);
			params.addBodyParameter("Sign",enMD5);
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.user_regist_RegAccId);
			HttpUtil.InstanceSend(RegAccId,params, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ResetAccPw(String phone,String pwd){
		try {
			LogUtils.e("----------ResetAccPw");
			String regAccIdStr = "{\"mobilenum\":\""+phone+"\",\"npw\":\""+pwd+"\"}";
			String rsaLoginstr = RsaHelper.encryptDataFromStr(regAccIdStr);
			RequestParams params = new RequestParams();
			params.addBodyParameter("resetobjstr",rsaLoginstr);
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.user_regist_ResetAccPw);
			HttpUtil.InstanceSend(ResetAccPw,params, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private class CallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("",this.getUserTag());
				if(BaseUtils.isNetworkAvailable(mContext)){
					ToastUtil.showMessage(mContext,R.string.phone_no_web);
					}else{
					ToastUtil.showMessage(mContext,R.string.error_internet);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				switch ((Integer) this.getUserTag()) {
				case Constant.user_regist_checkmobileAcc:
					dealResponseInfo(arg0.result,this.getUserTag());
					break;
				default:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);//{"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
						String ResultCode = jsonObj.getString("ResultCode");
						if ("0".equals(ResultCode)) {
							switch ((Integer)this.getUserTag()) {
							case Constant.user_regist_SendCheckCode:
							case Constant.user_regist_ReSendCheckCode:
							case Constant.user_regist_RegCheckMobileVcode:
							case Constant.user_regist_CheckMobileVcode:
							case Constant.user_regist_RegAccId:
							case Constant.user_regist_ResetAccPw:
								dealResponseInfo("success",this.getUserTag());
								break;
							default:
								String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
								dealResponseInfo(data,this.getUserTag());
								break;
							}

						}else if("8".equals(ResultCode)){
							dealResponseInfo("",this.getUserTag());
						} else {
							ToastUtil.showMessage(mContext, jsonObj.getString("ResultDesc"));
							dealResponseInfo("false",this.getUserTag());
						}
					} catch (Exception e) {
						dealResponseInfo("",this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_serverconnect)+"r1002");
					} 
					break;
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer)userTag) {
		case Constant.user_regist_checkmobileAcc:
		case Constant.user_regist_SendCheckCode:
		case Constant.user_regist_ReSendCheckCode:
		case Constant.user_regist_RegCheckMobileVcode:
		case Constant.user_regist_CheckMobileVcode:
		case Constant.user_regist_getTime:
		case Constant.user_regist_RegAccId:
		case Constant.user_regist_ResetAccPw:
			post.add(result);
			break;
		case Constant.user_regist_hello:
			if (result.equals(hellostr)) {
				post.add(result);
			}else{
				post.add("");
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

    /**
     * 通讯握手
     * base64(MD5(Ver + IAMSCID + hellostr + ClientKey))
     */
	private String hellostr;
    public void helloService(){
    	try {
    		hellostr = BaseUtils.getRandomString(4);
    		BaseActivity.editor.putString("hellostr", hellostr).commit();
			String sign = BaseUtils.getVersion(mContext.getApplicationContext())+BaseActivity.sp_sys.getString("IAMSCID", "")+hellostr+BaseActivity.sp_sys.getString("ClientKey", "");
			RequestParams params = new RequestParams();
			params.addBodyParameter("CliVer",BaseUtils.getVersion(mContext.getApplicationContext()));
			params.addBodyParameter("hellostr",hellostr);
			params.addBodyParameter("IAMSCID",BaseActivity.sp_sys.getString("IAMSCID", ""));
			params.addBodyParameter("Sign",Base64Helper.encode(Coder.encryptMD5(sign.getBytes())));
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.user_regist_hello);
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, sys_hello, params,callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public void getTime(){
    	
		HttpUtil.getInstanceNew().send(HttpRequest.HttpMethod.POST,"http://www.jiaobao.net/jbclient/Account/getcurTime",new RequestCallBack<String>() {
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				try {
					Date date =  new Date(System.currentTimeMillis());
					String str_time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(date);
					dealResponseInfo(str_time,Constant.user_regist_getTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String	str_time = jsonObj.getString("Data");
					dealResponseInfo(str_time,Constant.user_regist_getTime);
				} catch (JSONException e) {
					e.printStackTrace();
					Date date =  new Date(System.currentTimeMillis());
					String str_time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()).format(date);
					dealResponseInfo(str_time,Constant.user_regist_getTime);
				}
			}
		});
    }

	public void GetValidateCode(final ImageView iv_first_picnumber) {
		final String filePath = JSYApplication.getInstance().FILE_PATH+"number.png";
		bitmapUtils.clearCache(filePath);
		HttpUtil.getInstance().download(GetValidateCode,filePath, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				bitmapUtils.display(iv_first_picnumber, filePath);
				Log.i("GetValidateCode", "onSuccess");
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				bitmapUtils.display(iv_first_picnumber, filePath);
				Log.i("GetValidateCode", "onFailure");
			}
		});
	}
}