package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.ClassLeavesPost;
import com.jsy_jiaobao.po.leave.GateQueryLeavesPost;
import com.jsy_jiaobao.po.leave.Leave;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.MyLeaves;
import com.jsy_jiaobao.po.leave.MyLeavesPost;
import com.jsy_jiaobao.po.leave.UnitClassLeaves;
import com.jsy_jiaobao.po.leave.UnitLeavesPost;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class LeaveFragmentControl implements ConstantUrl {
	private static final String TAG = "LeaveFragmentControl";
	private Fragment mFragment;
	int mUid = 0, mChapterid = 0;
	private static LeaveFragmentControl instance;
	private Context mContext;

	public static synchronized final LeaveFragmentControl getInstance() {
		if (instance == null) {
			instance = new LeaveFragmentControl();
		}
		return instance;
	}

	public LeaveFragmentControl setContext(Fragment pActivity) {
		mContext = pActivity.getActivity();
		mFragment = pActivity;
		return this;
	}

	/**
	 * 增加新假条
	 * @param leave 假条model
	 */
	public void NewLeaveModel(Leave leave) {
		DialogUtil.getInstance().getDialog(mContext, "正在发送数据");
		RequestParams params = leave.getParams();
		CallBack callback = new CallBack();
		callback.setType(leave.getManType());
		callback.setUserTag(LeaveConstant.leave_NewLeaveModel);
		HttpUtil.InstanceSend(NewLeavelModel, params, callback);
	}

	/**
	 * 获取班主任关联班级
	 * 
	 * @param jiaobaohao 教宝号
	 */
	public void GetMyAdminClass(String jiaobaohao) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", jiaobaohao);
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetMyAdminClass);
		HttpUtil.InstanceSend(GetMyAdminClass, params, callback);
	}

	/**
	 * 获取本人请假记录
	 * 
	 * @param post
	 */
	public void GetMyLeaves(MyLeavesPost post) {
		DialogUtil.getInstance().getDialog(mContext, "正在获取请假记录");
		System.out.println(post.getsDateTime());
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setType(post.getManType());
		callback.setUserTag(LeaveConstant.leave_GetMyLeaves);
		HttpUtil.InstanceSend(GetMyLeaves, params, callback);
	}

	/**
	 * 班主任查询本班请假记录
	 * 
	 * @param post
	 */
	public void GetClassLeaves(ClassLeavesPost post) {
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetClassLeaves);
		HttpUtil.InstanceSend(GetClassLeaves, params, callback);
	}

	/**
	 * 审核人员取本单位的请假记录 
	 * 
	 * @param post
	 */
	public void GetUnitLeaves(UnitLeavesPost post) {
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetUnitLeaves);
		HttpUtil.InstanceSend(GetUnitLeaves, params, callback);
	}

	/**
	 * 门卫获取请假记录
	 * 
	 * @param post
	 */
	public void GetGateLeaves(GateQueryLeavesPost post) {
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(LeaveConstant.leave_GetGateLeaves);
		HttpUtil.InstanceSend(GetGateLeaves, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {
		private int mType;

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false", this.getUserTag(), 0);
				DialogUtil.getInstance().cannleDialog();
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
				DialogUtil.getInstance().cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);//
					String ResultCode = jsonObj.getString("ResultCode");
					if (!TextUtils.isEmpty(ResultCode)) {
						if ("0".equals(ResultCode)) {
							switch ((Integer) this.getUserTag()) {
							case LeaveConstant.leave_GetMyLeaves:
							case LeaveConstant.leave_GetMyAdminClass:
							case LeaveConstant.leave_GetGateLeaves:
							case LeaveConstant.leave_GetUnitLeaves:
								dealResponseInfo(jsonObj.getString("Data"),
										this.getUserTag(), mType);
								break;
							case LeaveConstant.leave_NewLeaveModel:
								dealResponseInfo("0", this.getUserTag(), mType);
								break;
							default:
								break;
							}
						} else if ("999999".equals(ResultCode)) {
							ToastUtil.showMessage(mContext, "发送失败，请将html标签去掉");
							Log.i("有html标签", jsonObj + "-" + this.getUserTag());
						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
						}
					}
				} catch (Exception e) {
					dealResponseInfo("false", this.getUserTag(), 0);
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}

		public int getType() {
			return mType;
		}

		public void setType(int type) {
			mType = type;
		}

	}

	private void dealResponseInfo(String result, Object userTag, int type) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case LeaveConstant.leave_GetMyAdminClass:
			result = "{\"list\":" + result + "}";
			Log.i("leave_GetMyAdminClass", result);
			MyAdminClasses myAdminClasses = GsonUtil.GsonToObject(result,
					MyAdminClasses.class);
			if (myAdminClasses != null) {
				Log.d(TAG + "GetMyAdminClass", myAdminClasses.toString());
			} else {
				Log.d(TAG + "GetMyAdminClass", "班主任获取关联班级为空");
			}
			post.add(myAdminClasses);
			break;
		case LeaveConstant.leave_GetClassLeaves:
			result = "{\"list\":" + result + "}";
			UnitClassLeaves unitClassLeaves = GsonUtil.GsonToObject(result,
					UnitClassLeaves.class);
			post.add(unitClassLeaves);
			break;
		case LeaveConstant.leave_GetMyLeaves:
			result = "{\"list\":" + result + "}";
			MyLeaves myLeaves = GsonUtil.GsonToObject(result, MyLeaves.class);
			post.add(myLeaves);
			post.add(type);
			break;
		case LeaveConstant.leave_GetUnitLeaves:
			result = "{\"list\":" + result + "}";
			UnitClassLeaves unitClassLeaves2 = GsonUtil.GsonToObject(result,
					UnitClassLeaves.class);
			post.add(unitClassLeaves2);
			break;
		case LeaveConstant.leave_GetGateLeaves:
			break;
		case LeaveConstant.leave_NewLeaveModel:
			post.add(true);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
