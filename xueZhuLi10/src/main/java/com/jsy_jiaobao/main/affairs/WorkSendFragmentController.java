package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

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
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.GenRevicer;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.jsy_jiaobao.po.sys.UnitGroupInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.unnamed.b.atv.model.TreeNode;

public class WorkSendFragmentController implements ConstantUrl {
	private static WorkSendFragmentController instance;
	private Fragment mContext;
	private Context mAContext;

	public static synchronized  WorkSendFragmentController getInstance() {
		if (instance == null) {
			instance = new WorkSendFragmentController();
		}
		return instance;
	}

	public WorkSendFragmentController setContext(Fragment noticeFragment) {
		mContext = noticeFragment;
		mAContext = noticeFragment.getActivity();
		return this;
	}

	/**
	 * 功能：获取单位接收人
	 * @param uid  uid
	 * @param flag flag
	 * @param type type
     * @param node node
     */
	public void GetUnitRevicer(int uid, int flag, int type, TreeNode node) {
		DialogUtil.getInstance().getDialog(mAContext, R.string.public_loading);
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("unitId", String.valueOf(uid));
		params.addBodyParameter("flag", String.valueOf(flag));
		UnitRevicerCallBack callback = new UnitRevicerCallBack();
		ArrayList<Object> tag = new ArrayList<>();
		tag.add(uid);
		tag.add(node);
		tag.add(type);
		callback.setUserTag(tag);
		HttpUtil.InstanceSend(GetUnitRevicer, params, callback);
		// HttpUstil.InstanceSend(GetUnitClassRevice, params, callback);

	}

//	/**
//	 * 功能：获取单位接收人
//	 * @param uid
//	 * @param flag
//	 * @param type
//     * @param node
//     */
//	public void GetUnitClassRevicer(int uid, int flag, int type, TreeNode node) {
//		DialogUtil.getInstance().getDialog(mAContext, R.string.public_loading);
//		DialogUtil.getInstance().setCanCancel(false);
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("unitclassId", String.valueOf(uid));
//		params.addBodyParameter("flag", String.valueOf(flag));
//		UnitRevicerCallBack callback = new UnitRevicerCallBack();
//		ArrayList<Object> tag = new ArrayList<Object>();
//		tag.add(uid);
//		tag.add(node);
//		tag.add(type);
//		callback.setUserTag(tag);
//		HttpUtil.InstanceSend(GetUnitClassRevice, params, callback);
//
//		// HttpUstil.InstanceSend(GetUnitClassRevice, params, callback);
//
//	}

	private class UnitRevicerCallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mAContext != null) {
				if (BaseUtils.isNetworkAvailable(mAContext)) {
					ToastUtil.showMessage(mAContext, R.string.phone_no_web);
				} else {
					try {
						if (mContext != null) {
							ArrayList<Object> post = new ArrayList<>();
							post.add(
									0,
									Constant.msgcenter_worksend_GetUnitRevicer_otherunit);
							post.add(1, null);
							post.add(2, this.getUserTag());
							EventBusUtil.post(post);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mContext.isAdded() && !mContext.isDetached()) {
				ArrayList<Object> post = new ArrayList<>();
				post.add(0,
						Constant.msgcenter_worksend_GetUnitRevicer_otherunit);
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						post.add(1, jsonObj.getString("Data"));
					} else if ("8".equals(ResultCode)) {
						post.add(1, null);
						LoginActivityController.getInstance().helloService(
								mContext.getActivity());
					} else {
						post.add(1, null);
						ToastUtil.showMessage(mContext.getActivity(),
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					post.add(1, null);
					ToastUtil.showMessage(
							mContext.getActivity(),
							mContext.getResources().getString(
									R.string.error_serverconnect)
									+ "r1002");
				}
				post.add(2, this.getUserTag());
				EventBusUtil.post(post);
			}
		}

	}

	/**
	 * 功能：获取班级接收人
	 * @param params p
	 * @param commMsgRevicerUnitClass c
     */
	public void GetUnitRevicer(RequestParams params,
			CommMsgRevicerUnitClass commMsgRevicerUnitClass) {
		RevicerCallBack callback = new RevicerCallBack();
		callback.setUserTag(commMsgRevicerUnitClass);
		HttpUtil.InstanceSend(GetUnitClassRevice, params, callback);
	}

	/**
	 * 发送信息
	 * 
	 * @param params p
	 */
	public void CreateCommMsg(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext.getActivity(),
				mContext.getResources().getString(R.string.public_later));
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work_CreateCommMsg);
		HttpUtil.getInstance().configTimeout(120000);
		HttpUtil.InstanceSend(CreateCommMsg, params, callback);

	}

//	/**
//	 * 短信直通车数据
//	 */
//	public void SMSCommIndex() {
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_work_SMSCommIndex);
//		HttpUtil.InstanceSend(SMSCommIndex, null, callback);
//	}

	private class RevicerCallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			DialogUtil.getInstance().cannleDialog();
			if (mAContext != null) {
				try {
					if (mContext != null) {
						ArrayList<Object> post = new ArrayList<>();
						post.add(0,
								Constant.msgcenter_worksend_GetUnitClassRevicer);
						post.add(1, null);
						post.add(2, this.getUserTag());
						EventBusUtil.post(post);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mAContext)) {
					ToastUtil.showMessage(mAContext, R.string.phone_no_web);

				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mContext.isAdded() && !mContext.isDetached()) {
				ArrayList<Object> post = new ArrayList<>();
				post.add(0, Constant.msgcenter_worksend_GetUnitClassRevicer);
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						GenRevicer UserList = GsonUtil.GsonToObject(
								jsonObj.getString("Data"), GenRevicer.class);
						post.add(1, UserList);
					} else if ("8".equals(ResultCode)) {
						post.add(1, null);
						LoginActivityController.getInstance().helloService(
								mContext.getActivity());
					} else {
						post.add(1, null);
						ToastUtil.showMessage(mContext.getActivity(),
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					post.add(1, null);
					ToastUtil.showMessage(
							mContext.getActivity(),
							mContext.getResources().getString(
									R.string.error_serverconnect)
									+ "r1002");
				}
				post.add(2, this.getUserTag());
				EventBusUtil.post(post);
			}
		}

	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mAContext != null) {
				try {
					if (mContext != null) {
						dealResponseInfo("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mAContext)) {
					ToastUtil.showMessage(mAContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mContext.isAdded() && !mContext.isDetached()) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						String data = Des.decrypt(jsonObj.getString("Data"),
								BaseActivity.sp_sys.getString("ClientKey", ""));
						dealResponseInfo(data, this.getUserTag());

					} else if ("8".equals(ResultCode)) {
						dealResponseInfo("0", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext.getActivity());
					} else if ("999999".equals(ResultCode)) {
						dealResponseInfo("0", this.getUserTag());
						ToastUtil.showMessage(mContext.getActivity(),"发送失败，请将html标签去掉");					
						Log.i("有html标签", jsonObj+"-"+this.getUserTag());
					} else {
						dealResponseInfo("0", this.getUserTag());
						ToastUtil.showMessage(mContext.getActivity(),
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("0", this.getUserTag());
					ToastUtil.showMessage(
							mContext.getActivity(),
							mContext.getResources().getString(
									R.string.error_serverconnect)
									+ "r1002");
				}
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_work_CreateCommMsg:
			HttpUtil.getInstance().configTimeout(30000);
			if ("0".equals(result)) {
				post.add(0);
			} else {
				post.add(1);
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work_SMSCommIndex:
			List<SMSTreeUnitID> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<SMSTreeUnitID>>() {
					}.getType());

			post.add(list);
			break;
		case Constant.msgcenter_worksend_getUnitGroups:
			ArrayList<UnitGroupInfo> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UnitGroupInfo>>() {
					}.getType());
			post.add(list1);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
