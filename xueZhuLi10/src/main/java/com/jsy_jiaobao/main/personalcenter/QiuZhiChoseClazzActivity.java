package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ExpandableListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.GetAllCategory;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * 选择关注的话题||选择话题
 * 
 * @author admin
 */
public class QiuZhiChoseClazzActivity extends BaseActivity {
	private ExpandableListView mExpandableListView;
	private QiuZhiExpandableListViewAdapter mExpandableListViewAdapter;
	private Context mContext;
	private int TabID = 0;
	private boolean isIndex = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			TabID = (int) savedInstanceState.getInt("TabID");
			isIndex = savedInstanceState.getBoolean("isIndex");
		} else {
			Intent getPass = getIntent();
			if (getPass != null) {
				Bundle bundle = getPass.getExtras();
				if (bundle != null) {
					TabID = bundle.getInt("TabID");
					isIndex = bundle.getBoolean("isIndex");
				}
			}
		}
		mContext = this;
		setContentView(R.layout.activity_qiuzhi_choseclazz);
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		if (isIndex) {
			setActionBarTitle(R.string.choose_topic_haveMyAttention);
			GetMyattCate();
		} else {
			setActionBarTitle(R.string.choose_topic);
			@SuppressWarnings("unchecked")
			ArrayList<GetAllCategory> allCategory = (ArrayList<GetAllCategory>) ACache
					.get(getApplicationContext(), "qiuzhi").getAsObject(
							"GetAllCategory");
			ArrayList<GetAllCategory> data = new ArrayList<GetAllCategory>();
			if (TabID == 0) {
				for (GetAllCategory item : allCategory) {
					data.add(item);
				}
			} else {
				for (GetAllCategory item : allCategory) {
					if (item.getItem().getTabID() == TabID) {
						data.add(item);
					}
				}
			}
			mExpandableListViewAdapter = new QiuZhiExpandableListViewAdapter(
					this, data, null);
			mExpandableListView.setAdapter(mExpandableListViewAdapter);
			for (int i = 0; i < data.size(); i++) {
				mExpandableListView.expandGroup(i);
			}
		}

	}

	/**
	 * 获取我关注的话题ID数组
	 */
	public void GetMyattCate() {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetMyattCate);
		HttpUtil.InstanceSend(GetMyattCate, null, callback);
	}

	/**
	 * 功能：更新我关注的话题 参数名称 是否必须 类型 描述 uid 是 string
	 * 我关注的所有话题ID，用逗号连接成一个字符串,如：11,15,45
	 */
	public void AddMyattCate(String uid) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AddMyattCate);
		HttpUtil.InstanceSend(AddMyattCate, params, callback);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("TabID", TabID);
		outState.putBoolean("isIndex", isIndex);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
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
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);// {"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_AddMyattCate:
							// 添加我的关注
							dealResponseInfo("1", this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_GetMyattCate:
							// 获取我的专注
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
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
					} else {
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
						dealResponseInfo("", this.getUserTag());
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
		case Constant.msgcenter_qiuzhi_AddMyattCate:
			// 添加我的关注
			DialogUtil.getInstance().cannleDialog();
			if (result.equals("1")) {
				ToastUtil.showMessage(mContext, R.string.update_success);
			}
			break;
		case Constant.msgcenter_qiuzhi_GetMyattCate:
			// 获取我的关注
			DialogUtil.getInstance().cannleDialog();
			try {
				JSONArray jsonarray = new JSONArray(result);
				@SuppressWarnings("unchecked")
				ArrayList<GetAllCategory> allCategory = (ArrayList<GetAllCategory>) ACache
						.get(getApplicationContext(), "qiuzhi").getAsObject(
								"GetAllCategory");

				mExpandableListViewAdapter = new QiuZhiExpandableListViewAdapter(
						this, allCategory, jsonarray);
				mExpandableListViewAdapter.setIndex(isIndex);
				mExpandableListView.setAdapter(mExpandableListViewAdapter);
				for (int i = 0; i < allCategory.size(); i++) {
					mExpandableListView.expandGroup(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

	/**
	 * @view 保存menu
	 * 
	 * @功能 保存选择关注状态
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub_search = menu.addSubMenu(R.string.save);
		sub_search.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub_search.getItem().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// 友盟事件监听
						MobclickAgent
								.onEvent(
										mContext,
										getResources()
												.getString(
														R.string.QiuZhiChoseClazzActivity_saveAttTopic));
						UserInfo userInfo = (UserInfo) ACache.get(
								getApplicationContext())
								.getAsObject("userInfo");
						if (userInfo.isIsKnlFeezeUser()) {
							//封号中
							ToastUtil.showMessage(
									mContext,
									mContext.getResources().getString(
											R.string.public_error_user));
							return false;
						}
						if (userInfo.getDUnitId() == 0) {
							//无单位
							ToastUtil.showMessage(
									mContext,
									mContext.getResources().getString(
											R.string.public_error_nounit));
							return false;
						}
						ArrayList<Integer> list = mExpandableListViewAdapter
								.getSelectedSubject();
						if (list != null) {
							String uid = "";
							for (int i = 0; i < list.size(); i++) {
								uid += list.get(i) + ",";
							}
							if (!TextUtils.isEmpty(uid)) {
								uid = uid.substring(0, uid.length() - 1);
							}
							AddMyattCate(uid);
						}
						return false;
					}
				});
		return isIndex;
	}

	/**
	 * 系统返回按键
	 * 
	 * @功能 结束Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * activity生命周期
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * activity生命周期
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPause(this);
		super.onPause();
	}

}