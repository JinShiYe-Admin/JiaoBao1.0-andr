package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;

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
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.app.gallery.Gallery;
import com.jsy_jiaobao.po.personal.Photo;
import com.jsy_jiaobao.po.personal.UnitPGroup;
import com.jsy_jiaobao.po.sys.UserClass;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 单位空间的Controller
 * 
 * @author admin
 */
public class UnitSpaceActivityController implements ConstantUrl {
	private static UnitSpaceActivityController instance;
	private Activity mcontext;

	public static synchronized UnitSpaceActivityController getInstance() {
		if (instance == null) {
			instance = new UnitSpaceActivityController();
		}
		return instance;
	}

	public UnitSpaceActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}

	/**
	 * 取单位最新图片
	 */
	public void GetUnitNewPhoto(String UnitID) {
		try {
			RequestParams params = new RequestParams("utf-8");
			params.addBodyParameter("UnitID", UnitID);
			params.addBodyParameter("count", "4");
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.msgcenter_unitspace_GetUnitNewPhoto);
			HttpUtil.InstanceNewSend(
					ACache.get(mcontext.getApplicationContext()).getAsString(
							"MainUrl")
							+ GetUnitNewPhoto, params, callback);
		} catch (Exception e) {
			e.printStackTrace();
			dealResponseInfo("", Constant.msgcenter_unitspace_GetUnitNewPhoto);
		}
	}

	/**
	 * 取单位相册
	 */
	public void GetUnitPGroup(String unitid) {
		try {
			DialogUtil.getInstance().getDialog(mcontext,
					mcontext.getResources().getString(R.string.public_loading));
			RequestParams params = new RequestParams("utf-8");
			params.addBodyParameter("UnitID", unitid);
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.msgcenter_unitspace_GetUnitPGroup);
			HttpUtil.InstanceNewSend(
					ACache.get(mcontext.getApplicationContext()).getAsString(
							"MainUrl")
							+ GetUnitPGroup, params, callback);
		} catch (NotFoundException e) {
			dealResponseInfo("", Constant.msgcenter_unitspace_GetUnitPGroup);
			e.printStackTrace();
		}
	}

	/**
	 * 取相册
	 */
	public void GetUnitPhotoByGroupID(int UID, int GroupID) {
		try {
			RequestParams params = new RequestParams("utf-8");
			params.addBodyParameter("UnitID", String.valueOf(UID));
			params.addBodyParameter("GroupID", String.valueOf(GroupID));
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.msgcenter_unitspace_GetUnitPhotoByGroupID);
			HttpUtil.InstanceNewSend(
					ACache.get(mcontext.getApplicationContext()).getAsString(
							"MainUrl")
							+ GetUnitPhotoByGroupID, params, callback);
		} catch (Exception e) {
			dealResponseInfo("",
					Constant.msgcenter_unitspace_GetUnitPhotoByGroupID);
			e.printStackTrace();
		}
	}

	/**
	 * 取相册下相片
	 */
	public void GetPhotoByGroup(int UID, String GroupID) {
		try {
			RequestParams params = new RequestParams("utf-8");
			params.addBodyParameter("JiaoBaoHao", String.valueOf(UID));
			params.addBodyParameter("GroupInfo", GroupID);
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.msgcenter_unitspace_GetPhotoByGroup);
			HttpUtil.InstanceNewSend(
					ACache.get(mcontext.getApplicationContext()).getAsString(
							"MainUrl")
							+ GetPhotoByGroup, params, callback);
		} catch (Exception e) {
			dealResponseInfo("", Constant.msgcenter_unitspace_GetPhotoByGroup);
			e.printStackTrace();
		}
	}

	/**
	 * 功能：要获取属于jiaobaohao下的相册
	 */
	public void GetPhotoList(String JiaoBaoHao) {
		try {
			RequestParams params = new RequestParams();
			params.addBodyParameter("JiaoBaoHao", JiaoBaoHao);
			CallBack callback = new CallBack();
			callback.setUserTag(Constant.msgcenter_personalspace_GetPhotoList);
			HttpUtil.InstanceNewSend(
					ACache.get(mcontext.getApplicationContext()).getAsString(
							"MainUrl")
							+ GetPhotoList, params, callback);
		} catch (Exception e) {
			dealResponseInfo("", Constant.msgcenter_personalspace_GetPhotoList);
			e.printStackTrace();
		}

	}

	/**
	 * 取教师关联的班级 
	 */
	public void getmyUserClass(int UnitID) {
		DialogUtil.getInstance().getDialog(mcontext,
				R.string.getting_myClass_waiting);
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_publish_getmyUserClass);
		HttpUtil.InstanceSend(getmyUserClass, params, callback);
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
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_publish_getmyUserClass:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									MessageCenterActivity.sp_sys.getString(
											"ClientKey", ""));
							dealResponseInfo(data, this.getUserTag());
							break;
						default:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						}

					} else if ("8".equals(ResultCode)) {
						LoginActivityController.getInstance().helloService(
								mcontext);
						dealResponseInfo("", this.getUserTag());
					} else {
						dealResponseInfo("", this.getUserTag());
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
		case Constant.msgcenter_unitspace_GetUnitPGroup:
			ArrayList<UnitPGroup> getPgroupList = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UnitPGroup>>() {
					}.getType());
			post.add(getPgroupList);
			break;
		case Constant.msgcenter_unitspace_GetUnitPhotoByGroupID:
			ArrayList<Photo> getPhotoList = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<Photo>>() {
					}.getType());
			post.add(getPhotoList);
			break;
		case Constant.msgcenter_unitspace_GetPhotoByGroup:
			ArrayList<Photo> getPhotoList1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<Photo>>() {
					}.getType());
			post.add(getPhotoList1);
			break;
		case Constant.msgcenter_personalspace_GetPhotoList:
			ArrayList<Gallery> getPhotoList11 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<Gallery>>() {
					}.getType());
			post.add(getPhotoList11);
			break;
		case Constant.msgcenter_unitspace_GetUnitNewPhoto:
			ArrayList<Photo> getPhotoList111 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<Photo>>() {
					}.getType());
			post.add(getPhotoList111);
			break;
		case Constant.msgcenter_publish_getmyUserClass:
			ArrayList<UserClass> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UserClass>>() {
					}.getType());
			post.add(list1);
			break;
		default:

			break;
		}
		EventBusUtil.post(post);
	}
}