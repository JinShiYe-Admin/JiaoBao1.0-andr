package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONObject;

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
import com.jsy_jiaobao.po.personal.NoticeGetArthInfo;
import com.jsy_jiaobao.po.personal.Photo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class PersonalSpaceActivityController implements ConstantUrl{
	private static PersonalSpaceActivityController instance;
	private Activity mcontext;

	public static synchronized final PersonalSpaceActivityController getInstance() {
		if (instance == null) {
			instance = new PersonalSpaceActivityController();
		}
		return instance;
	}

	public PersonalSpaceActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}
	/**
	 * 功能：个人某个相册中的照片
	 */
	public void GetNewPhoto(String JiaoBaoHao,String count){
		RequestParams params = new RequestParams();
		params.addBodyParameter("JiaoBaoHao",JiaoBaoHao);
		params.addBodyParameter("Count",count);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_personalspace_GetNewPhoto);
		HttpUtil.InstanceNewSend(ACache.get(mcontext.getApplicationContext()).getAsString("MainUrl")+GetNewPhoto, params, callback);

	}
	/**
	 * 取本单位栏目文章
	 * 客户端通过本接口获取本单位栏目文章。
	 * @param params p
	 */
	public void ArthListIndex(RequestParams params){
		DialogUtil.getInstance().getDialog(mcontext, mcontext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_ArthListIndex);
		HttpUtil.InstanceSend(ArthListIndex, params, callback);

	}
	private class CallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mcontext) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo("",this.getUserTag());
				if(BaseUtils.isNetworkAvailable(mcontext)){
					ToastUtil.showMessage(mcontext,R.string.phone_no_web);
					
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
					switch ((Integer)this.getUserTag()) {
					case Constant.msgcenter_personalspace_GetNewPhoto:
						
						if ("0".equals(ResultCode)) {
							dealResponseInfo(jsonObj.getString("Data"),this.getUserTag());
						} else {
							dealResponseInfo("",this.getUserTag());
							ToastUtil.showMessage(mcontext,R.string.temporarily_no_photo);
						}
						break;

					default:
						
						if ("0".equals(ResultCode)) {
							String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
							dealResponseInfo(data,this.getUserTag());
						}else if("8".equals(ResultCode)){
							dealResponseInfo("",this.getUserTag());
							LoginActivityController.getInstance().helloService(mcontext);
						} else {
							ToastUtil.showMessage(mcontext,jsonObj.getString("ResultDesc"));
						}
						break;
					}
				} catch (Exception e) {
					dealResponseInfo("",this.getUserTag());
					ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}

	}
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		switch ((Integer)userTag) {
		case Constant.msgcenter_notice_ArthListIndex:
			try {
				result = Des.decrypt(result, BaseActivity.sp_sys.getString("ClientKey", ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
			post.add(Constant.msgcenter_notice_ArthListIndex);
			result = "{\"list\":"+result+"}";
			NoticeGetArthInfo getArthInfo1 = GsonUtil.GsonToObject(result, NoticeGetArthInfo.class);
			post.add(getArthInfo1);
			break;
		case Constant.msgcenter_personalspace_GetNewPhoto:
			post.add(Constant.msgcenter_personalspace_GetNewPhoto);
			ArrayList<Photo> getArthInfo = GsonUtil.GsonToList(result, new TypeToken<ArrayList<Photo>>() {}.getType());
			post.add(getArthInfo);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}


}
