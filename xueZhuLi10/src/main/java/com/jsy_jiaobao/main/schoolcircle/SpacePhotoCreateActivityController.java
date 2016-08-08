package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class SpacePhotoCreateActivityController implements ConstantUrl{
	private static SpacePhotoCreateActivityController instance;
	private Activity mContext;

	public static synchronized SpacePhotoCreateActivityController getInstance() {
		if (instance == null) {
			instance = new SpacePhotoCreateActivityController();
		}
		return instance;
	}

	public SpacePhotoCreateActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}
	/**
	 * 个人空间添加相册
	 */
	public void AddPhotoGroup(RequestParams params){
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_unitspace_AddPhotoGroup);
		HttpUtil.InstanceNewSend(ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+ AddPhotoGroup, params, callback);

	}
	/**
	 * 创建单位相册
	 */
	public void CreateUnitPhotoGroup(RequestParams params){
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_unitspace_CreateUnitPhotoGroup);
		HttpUtil.InstanceNewSend(ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+ CreateUnitPhotoGroup, params, callback);

	}
	private class CallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo("",this.getUserTag());
				if(BaseUtils.isNetworkAvailable(mContext)){
					ToastUtil.showMessage(mContext,R.string.phone_no_web);
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
					if ("0".equals(ResultCode)) {
						dealResponseInfo(jsonObj.getString("Data"),this.getUserTag());

					}else if("8".equals(ResultCode)){
						dealResponseInfo("",this.getUserTag());
						LoginActivityController.getInstance().helloService(mContext);
					} else {
						dealResponseInfo("",this.getUserTag());
						ToastUtil.showMessage(mContext,jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("",this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}

	}
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		switch ((Integer)userTag) {
		case Constant.msgcenter_unitspace_AddPhotoGroup:
			post.add(Constant.msgcenter_unitspace_AddPhotoGroup);
			post.add(result);
			break;
		case Constant.msgcenter_unitspace_CreateUnitPhotoGroup:
			post.add(Constant.msgcenter_unitspace_CreateUnitPhotoGroup);
			post.add(result);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}