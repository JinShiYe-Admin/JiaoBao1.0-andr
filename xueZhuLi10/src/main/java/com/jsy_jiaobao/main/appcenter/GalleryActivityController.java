package com.jsy_jiaobao.main.appcenter;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.app.gallery.GetGallery;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;


public class GalleryActivityController implements ConstantUrl{

	private static GalleryActivityController instance;
	private Activity mcontext;
	public static synchronized final GalleryActivityController getInstance() {
		if (instance == null) {
			instance = new GalleryActivityController();
		}
		return instance;
	}

	public GalleryActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}
	/**
	 * 取本相册列表
	 */
	public void GetPhotoList(){
		RequestParams params = new RequestParams();
		params.addBodyParameter("JiaoBaoHao",BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.appcenter_gallery_GetPhotoList);//ACache.get(getApplicationContext()).getAsString("mainURL")
		HttpUtil.InstanceNewSend(ACache.get(mcontext.getApplicationContext()).getAsString("MainUrl")+GetPhotoList, params, callback);
	}
	/**
	 * 个人相册上传图片
	 * @param params
	 */
	public void uploadSectionImg(File file, RequestParams params){
		uploadSectionImgCallBack callback = new uploadSectionImgCallBack();
		callback.setUserTag(file);
		HttpUtil.InstanceNewSend(ACache.get(mcontext.getApplicationContext()).getAsString("MainUrl")+ UpLoadPhotoFromAPP, params, callback);
	}
	/**
	 * 单位相册上传图片
	 * @param params
	 */
	public void UpLoadPhotoUnit(File file, RequestParams params){
		uploadSectionImgCallBack callback = new uploadSectionImgCallBack();
		callback.setUserTag(file);
		HttpUtil.InstanceNewSend(ACache.get(mcontext.getApplicationContext()).getAsString("MainUrl")+UpLoadPhotoUnit, params, callback);
	}
	private class uploadSectionImgCallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mcontext) {
				LogUtils.e(arg1);
				ArrayList<Object> post = new ArrayList<Object>();
				post.add(Constant.appcenter_gallery_UpLoadPhotofailed);
				EventBusUtil.post(post);
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if ( null != mcontext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					System.out.println("---------ResultCode:"+ResultCode);
					
					if ("0".equals(ResultCode)) {
//						String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
						File tag = (File) this.getUserTag();
						ArrayList<Object> post = new ArrayList<Object>();
						post.add(Constant.appcenter_gallery_UpLoadPhoto);
						post.add(tag);
						EventBusUtil.post(post);
					}else if("8".equals(ResultCode)){
						LoginActivityController.getInstance().helloService(mcontext);
					} else {
						ToastUtil.showMessage(mcontext,jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}
	}
	private class CallBack extends RequestCallBack<String>{
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mcontext) {
				LogUtils.e(arg1);
				dealResponseInfo("",this.getUserTag());
			}
		}
		
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if ( null != mcontext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					System.out.println("---------ResultCode:"+ResultCode);
					
					if ("0".equals(ResultCode)) {
//						String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
						dealResponseInfo(jsonObj.getString("Data"),this.getUserTag());
						
					}else if("8".equals(ResultCode)){
						dealResponseInfo("",this.getUserTag());
						LoginActivityController.getInstance().helloService(mcontext);
					} else {
						dealResponseInfo("",this.getUserTag());
						ToastUtil.showMessage(mcontext,jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("",this.getUserTag());
					ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}
	}
	private void dealResponseInfo(String result, Object tag) {
		ArrayList<Object> post = new ArrayList<Object>();
		switch ((Integer)tag) {
		case Constant.appcenter_gallery_GetPhotoList:
			post.add(Constant.appcenter_gallery_GetPhotoList);
			result = "{\"list\":"+result+"}";
			GetGallery getGallery = GsonUtil.GsonToObject(result, GetGallery.class);
			post.add(getGallery);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

}
