package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

//一些修改1 2016-5-16 MSL 
//1.切换到家校互动时，四个权限都没有的话提示“当前身份无班级，请切换身份”

public class WorkSendToSbActivity2Controller implements ConstantUrl {
	private static WorkSendToSbActivity2Controller instance;
	private FragmentActivity mContext;

	public static synchronized final WorkSendToSbActivity2Controller getInstance() {
		if (instance == null) {
			instance = new WorkSendToSbActivity2Controller();
		}
		return instance;
	}

	public WorkSendToSbActivity2Controller setContext(FragmentActivity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 切换到内部事务
	 */
	public void switchUnitFragment() {
		FragmentManager fragmentManager = mContext.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
		WorkSendUnitFragment unitFragment = (WorkSendUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendUnitFragment.TAG);
		WorkSendGenFragment genFragment = (WorkSendGenFragment) fragmentManager
				.findFragmentByTag(WorkSendGenFragment.TAG);
		WorkSendOtherUnitFragment otherUnitFragment = (WorkSendOtherUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendOtherUnitFragment.TAG);
		if (otherUnitFragment != null && !otherUnitFragment.isHidden()) {
			ft.hide(otherUnitFragment);
		}
		if (genFragment != null && !genFragment.isHidden()) {
			ft.hide(genFragment);
		}
		if (unitFragment == null) {
			unitFragment = new WorkSendUnitFragment();
			ft.add(R.id.worksendsb_ll_content, unitFragment,
					WorkSendUnitFragment.TAG);
		} else {
			ft.show(unitFragment);
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 切换到家校互动
	 */
	public void switchGenFragment() {
		FragmentManager fragmentManager = mContext.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
		WorkSendUnitFragment unitFragment = (WorkSendUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendUnitFragment.TAG);
		WorkSendGenFragment genFragment = (WorkSendGenFragment) fragmentManager
				.findFragmentByTag(WorkSendGenFragment.TAG);
		WorkSendOtherUnitFragment otherUnitFragment = (WorkSendOtherUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendOtherUnitFragment.TAG);

		if (otherUnitFragment != null && !otherUnitFragment.isHidden()) {
			ft.hide(otherUnitFragment);
		}
		if (unitFragment != null && !unitFragment.isHidden()) {
			ft.hide(unitFragment);
		}
		if (genFragment == null) {
			Log.i("genFragment", "genFragment-null");
			genFragment = new WorkSendGenFragment();
			ft.add(R.id.worksendsb_ll_content, genFragment,
					WorkSendGenFragment.TAG);
			if (unitFragment != null) {
				hasNoClassChangeRole();
			}
		} else {
			Log.i("genFragment", "genFragment-!null");
			ft.show(genFragment);
			hasNoClassChangeRole();
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 切换到多单位事务
	 */
	public void switchOtherUnitFragment() {
		FragmentManager fragmentManager = mContext.getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
		WorkSendUnitFragment unitFragment = (WorkSendUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendUnitFragment.TAG);
		WorkSendGenFragment genFragment = (WorkSendGenFragment) fragmentManager
				.findFragmentByTag(WorkSendGenFragment.TAG);
		WorkSendOtherUnitFragment otherUnitFragment = (WorkSendOtherUnitFragment) fragmentManager
				.findFragmentByTag(WorkSendOtherUnitFragment.TAG);
		if (genFragment != null && !genFragment.isHidden()) {
			ft.hide(genFragment);
		}
		if (unitFragment != null && !unitFragment.isHidden()) {
			ft.hide(unitFragment);
		}
		if (otherUnitFragment == null) {
			otherUnitFragment = new WorkSendOtherUnitFragment();
			ft.add(R.id.worksendsb_ll_content, otherUnitFragment,
					WorkSendOtherUnitFragment.TAG);
		} else {
			ft.show(otherUnitFragment);
			// 有权限，无单位时切换到多单位事务会有提示
			if (WorkSendToSbActivity2.myUnit != null) {
				if ((WorkSendToSbActivity2.SubUnits == null || WorkSendToSbActivity2.SubUnits
						.size() == 0)
						&& (WorkSendToSbActivity2.ParentUnits == null || WorkSendToSbActivity2.ParentUnits
								.size() == 0)) {
					ToastUtil.showMessage(mContext, R.string.noOther_unit);
				}
			}
		}
		ft.commitAllowingStateLoss();
	}

	/**
	 * 点击家校互动后，单位接收人或者短信直通车无相应数据，提示‘当前身份无班级，请切换身份’
	 */
	private void hasNoClassChangeRole() {
		Log.i("WorkSendToSbActivity2.SMSList", WorkSendToSbActivity2.SMSList
				+ "");
		Log.i("WorkSendToSbActivity2.UnitClass",
				WorkSendToSbActivity2.UnitClass + "");
		if (WorkSendToSbActivity2.SMSList != null
				|| WorkSendToSbActivity2.UnitClass != null) {
			// WorkSendToSbActivity2.SMSList 校园通知，多校家长
			// WorkSendToSbActivity2.UnitClass 班级通知，个性表现
			if (WorkSendToSbActivity2.UnitClass != null) {
				if (WorkSendToSbActivity2.UnitClass.size() == 0
						&& WorkSendToSbActivity2.SMSList == null) {// 有可能出现设置了权限，但是未分配班级或者接收人的情况
					ToastUtil.showMessage(mContext,
							R.string.currentRole_hasNoClass_changeRole);
				}
			}
			if (WorkSendToSbActivity2.SMSList != null) {
				if (WorkSendToSbActivity2.SMSList.size() == 0
						&& WorkSendToSbActivity2.UnitClass == null) {// 有可能出现设置了权限，但是未分配班级或者接收人的情况
					ToastUtil.showMessage(mContext,
							R.string.currentRole_hasNoClass_changeRole);
				}
			}
		} else {
			ToastUtil.showMessage(mContext,
					R.string.currentRole_hasNoClass_changeRole);
		}
	}

	/**
	 * 获取事务信息接收单位列表 功能：根据用户当前所在单位，获取事务信息接收单位列表。 用户可以切换单位，然后重新获取接收单位列表
	 */
	public void CommMsgRevicerUnitList() {
		DialogUtil.getInstance().getDialog(mContext, R.id.loading);
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work_CommMsgRevicerUnitList);
		HttpUtil.InstanceSend(CommMsgRevicerUnitList, null, callback);
	}

	/**
	 * 取发送事务权限 功能：当用户的身份为单位人员或老师时，需要判定用户是否有发送上级单位事务，本单位事务，下级单位事务的权限，如果用户不具备相应的权限
	 * ，在app界面不要提供相应功能
	 * 。这里指的新建事务的权限（权限对象为单位人员和老师），阅读和回复的权限人人都有。班级事务（家校沟通的权限）只要有关联的班级就可以使用，不受限制。
	 */
	public void GetCommPerm() {
		DialogUtil.getInstance().getDialog(mContext, R.id.loading);
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work_GetCommPerm);
		HttpUtil.InstanceSend(GetCommPerm, null, callback);
	}

	/**
	 * 功能：获取单位接收人
	 * 
	 * @param unitname
	 */
	public void GetUnitRevicer(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext, R.id.loading);
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work_GetUnitRevicer);
		HttpUtil.InstanceSend(GetUnitRevicer, params, callback);
	}

	/**
	 * 获取短信直通车接收单位树数据源
	 * 功能：根据用户当前所在单位，获取短信直通车接收单位树数据源（包括本单位及所有下级单位，跨级下级单位）。用户可以切换单位，
	 * 然后重新获取短信直通车接收单位树数据源
	 * 
	 * @param params
	 */
	public void SMSCommIndex() {
		DialogUtil.getInstance().getDialog(mContext, R.id.loading);
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work_SMSCommIndex);
		HttpUtil.InstanceSend(SMSCommIndex, null, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_internet));
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_work_CommMsgRevicerUnitList:
						case Constant.msgcenter_work_GetUnitRevicer:
						case Constant.msgcenter_work_GetCommPerm:
							// case
							// Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen:
							// case
							// Constant.msgcenter_work_GetMsgAllReviceUnitList:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							Log.i((Integer) this.getUserTag() + "",
									jsonObj.getString("Data"));
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
						dealResponseInfo("", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						switch ((Integer) getUserTag()) {
						case Constant.msgcenter_work_CreateCommMsg:
							break;
						case Constant.msgcenter_work_SMSCommIndex:
							ArrayList<Object> post = new ArrayList<Object>();
							post.add(Constant.msgcenter_work_SMSCommIndex);
							post.add(null);
							EventBusUtil.post(post);
							break;
						default:
							ArrayList<Object> post1 = new ArrayList<Object>();
							post1.add(Constant.msgcenter_work_geterror);
							EventBusUtil.post(post1);
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							break;
						}
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
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_work_GetCommPerm:
			com.jsy_jiaobao.po.sys.GetCommPerm getCommPerm = GsonUtil
					.GsonToObject(result,
							com.jsy_jiaobao.po.sys.GetCommPerm.class);
			post.add(getCommPerm);
			break;
		case Constant.msgcenter_work_GetUnitRevicer:
		case Constant.msgcenter_work_CommMsgRevicerUnitList:
			post.add(result);
			break;
		case Constant.msgcenter_work_SMSCommIndex:
			List<SMSTreeUnitID> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<SMSTreeUnitID>>() {
					}.getType());

			post.add(list);
			break;
		case Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit:
			post.add(result);
			break;
		case Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen:
			post.add(result);
			break;
		case Constant.msgcenter_work_GetMsgAllReviceUnitList:
			try {
				JSONObject obj = new JSONObject(result);
				HashMap<String, Boolean> map = new HashMap<String, Boolean>();
				map.put("MsgAll_ToSubUnitMem",
						obj.getBoolean("MsgAll_ToSubUnitMem"));
				map.put("MsgAll_ToGen", obj.getBoolean("MsgAll_ToGen"));
				post.add(map);
			} catch (JSONException e) {
				post.add(null);
			}
			break;

		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
