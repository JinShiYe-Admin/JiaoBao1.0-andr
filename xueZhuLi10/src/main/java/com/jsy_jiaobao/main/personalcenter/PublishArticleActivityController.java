package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
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
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.UpFiles;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * PublishArticleActivity的Controller
 * 
 * @功能 主要负责数据的发送和接收、传递
 * @author admin
 * 
 */

public class PublishArticleActivityController implements ConstantUrl {
	private static PublishArticleActivityController instance;
	private Activity mcontext;

	public static synchronized  PublishArticleActivityController getInstance() {
		if (instance == null) {
			instance = new PublishArticleActivityController();
		}
		return instance;
	}

	public PublishArticleActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}

//	/**
//	 * 切换到当前单位
//	 *
//	 * @param params
//	 */
//	public void changeUnit(RequestParams params) {
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_changeUnit);
//		HttpUtil.InstanceSend(changeCurUnit, params, callback);
//	}

//	/**
//	 * 上传音频
//	 *
//	 * @param params
//	 */
//	public void uploadSectionAudio(RequestParams params) {
//		DialogUtil.getInstance().getDialog(mcontext,
//				mcontext.getResources().getString(R.string.public_loading));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_uploadSectionAudio);
//		HttpUtil.InstanceSend(uploadSectionImg, params, callback);
//
//	}

//	/**
//	 * 上传视频
//	 *
//	 * @param params
//	 */
//	public void uploadSectionVideo(RequestParams params) {
//		DialogUtil.getInstance().getDialog(mcontext,
//				mcontext.getResources().getString(R.string.public_loading));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_uploadSectionVideo);
//		HttpUtil.InstanceSend(uploadSectionImg, params, callback);
//
//	}

	/**
	 * 上传图片
	 * 
	 * @param params 请求数据
	 */
	public void uploadSectionImg(RequestParams params) {
		DialogUtil.getInstance().getDialog(mcontext,
				mcontext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_uploadSectionImg);
		HttpUtil.InstanceSend(uploadSectionImg, params, callback);

	}

	/**
	 * 发布文章
	 * 
	 * @param params 请求数据
	 */
	public void savepublishArticle(RequestParams params) {
		DialogUtil.getInstance().getDialog(mcontext,
				mcontext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_savepublishArticle);
		HttpUtil.InstanceSend(savepublishArticle, params, callback);

	}

	/**
	 * @功能 返回数据情况及处理
	 * @author admin
	 * 
	 */
	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mcontext != null) {
				if (BaseUtils.isNetworkAvailable(mcontext)) {
					ToastUtil.showMessage(mcontext, R.string.phone_no_web);
				}
				if (!mcontext.isFinishing()) {
					DialogUtil.getInstance().cannleDialog();
					dealResponseInfo("", this.getUserTag());
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (!mcontext.isFinishing()) {
				DialogUtil.getInstance().cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_notice_savepublishArticle:
							dealResponseInfo(null, this.getUserTag());
							break;
						default:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									MessageCenterActivity.sp_sys.getString(
											"ClientKey", ""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}
					} else if ("8".equals(ResultCode)) {

						dealResponseInfo("", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mcontext);
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
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		// 上传图片后返回的数据
		case Constant.msgcenter_notice_uploadSectionImg:
			UpFiles list2 = GsonUtil.GsonToObject(result, UpFiles.class);
			post.add(list2);
			break;
		case Constant.msgcenter_notice_savepublishArticle:
			break;
		case Constant.msgcenter_notice_uploadSectionAudio:
			List<UpFiles> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UpFiles>>() {
					}.getType());
			post.add(list);
			break;
		case Constant.msgcenter_notice_uploadSectionVideo:
			List<UpFiles> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UpFiles>>() {
					}.getType());
			post.add(list1);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
