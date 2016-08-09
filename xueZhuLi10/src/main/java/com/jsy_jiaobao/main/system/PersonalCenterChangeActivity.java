package com.jsy_jiaobao.main.system;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.sys.MyMobileUnit;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 个人中心修改昵称，姓名，密码，加入单位的Activity
 */
public class PersonalCenterChangeActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;
	private IEditText edt_nickname;// 昵称
	private IEditText edt_truename;// 真实姓名
	private IEditText edt_oldpwd;// 原密码
	private IEditText edt_newpwd;// 新密码
	private TextView tv_change;// 确定
	private String way;

	private PersonalCenterChangeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			way = savedInstanceState.getString("way");
		} else {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				way = bundle.getString("way");
			}
		}
		initView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("way", way);
	}

	private void initView() {
		setContentLayout(R.layout.activity_personalcenter_change);
		mContext = this;
		PersonalCenterChangeActivityController.getInstance().setContext(this);
		LinearLayout layout_name = (LinearLayout) findViewById(R.id.personal_layout_name);
		LinearLayout layout_pwd = (LinearLayout) findViewById(R.id.personal_layout_pwd);
		edt_nickname = (IEditText) findViewById(R.id.personal_edt_nickname);
		edt_truename = (IEditText) findViewById(R.id.personal_edt_truename);
		edt_oldpwd = (IEditText) findViewById(R.id.personal_edt_oldpwd);
		edt_newpwd = (IEditText) findViewById(R.id.personal_edt_newpwd);
		tv_change = (TextView) findViewById(R.id.personal_tv_change);
		CusListView listView = (CusListView) findViewById(R.id.personal_listview);
		tv_change.setOnClickListener(this);
		edt_nickname.setText(BaseActivity.sp.getString("Nickname",
				getResources().getString(R.string.new_user)));
		edt_truename.setText(BaseActivity.sp.getString("TrueName",
				getResources().getString(R.string.new_user)));
		if ("name".equals(way)) {
			setActionBarTitle(R.string.change_name);
			layout_name.setVisibility(View.VISIBLE);
		} else if ("pwd".equals(way)) {
			setActionBarTitle(R.string.change_passwords);
			layout_pwd.setVisibility(View.VISIBLE);
		} else if ("unit".equals(way)) {
			setActionBarTitle(R.string.join_unit);
			listView.setVisibility(View.VISIBLE);
			tv_change.setVisibility(View.GONE);
			adapter = new PersonalCenterChangeAdapter(this);
			listView.setAdapter(adapter);
			PersonalCenterChangeActivityController.getInstance()
					.GetMyMobileUnitList();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == tv_change) {
			if ("name".equals(way)) {
				String str_nick = edt_nickname.getTextString();
				String str_true = edt_truename.getTextString();
				if ((!TextUtils.isEmpty(str_true))
						&& (!TextUtils.isEmpty(str_nick))) {
					if (str_nick.equals(BaseActivity.sp.getString("Nickname",
							getResources().getString(R.string.new_user)))
							&& str_true.equals(BaseActivity.sp.getString(
									"TrueName", "新用户"))) {
						ToastUtil.showMessage(mContext,
								R.string.noChange_contains);
					} else {
						DialogUtil.getInstance().getDialog(mContext,
								R.string.updating_waiting);
						DialogUtil.getInstance().setCanCancel(false);
						PersonalCenterChangeActivityController.getInstance()
								.checkAccN(str_nick);
					}
				} else {
					ToastUtil.showMessage(mContext, R.string.cannot_empty);
				}
			} else if ("pwd".equals(way)) {
				String str_oldpwd = edt_oldpwd.getTextString();
				String str_newpwd = edt_newpwd.getTextString();
				String UserPW = BaseActivity.sp.getString("UserPW", "");
				if ((!TextUtils.isEmpty(str_oldpwd))
						&& (!TextUtils.isEmpty(str_newpwd))) {
					if (!TextUtils.isEmpty(UserPW)) {
						if (UserPW.equals(str_oldpwd)) {
							if (str_newpwd.length() < 6
									|| str_newpwd.length() > 18) {
								ToastUtil.showMessage(mContext,
										R.string.passwords_length_wrong);
							} else if (UserPW.equals(str_newpwd)) {
								ToastUtil.showMessage(mContext, "新旧密码不可以相同");
							} else {
								DialogUtil.getInstance().getDialog(mContext,
										R.string.updating_waiting);
								DialogUtil.getInstance().setCanCancel(false);
								PersonalCenterChangeActivityController
										.getInstance().ChangePW(str_oldpwd,
												str_newpwd);
							}
						} else {
							ToastUtil.showMessage(mContext,
									R.string.old_passwords_wrong);
						}
					}
				} else {
					ToastUtil.showMessage(mContext, R.string.cannot_empty);
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.user_regist_checkAccN:
			String checkAccN = (String) list.get(1);
			if ("True".equals(checkAccN)) {
				PersonalCenterChangeActivityController.getInstance()
						.UpateRecAcc(edt_nickname.getTextString(),
								edt_truename.getTextString());
			} else {
				ToastUtil.showMessage(mContext, "昵称不可用!昵称可能重复！不能全是数字，不能包含@");
				DialogUtil.getInstance().cannleDialog();
			}
			break;
		case Constant.user_regist_ChangePW:// 修改密码
			DialogUtil.getInstance().cannleDialog();
			String ChangePW = (String) list.get(1);
			if ("success".equals(ChangePW)) {
				edt_newpwd.setText("");
				edt_oldpwd.setText("");
				ToastUtil.showMessage(mContext, R.string.update_success);
				BaseActivity.editor.putString("UserPW",
						edt_newpwd.getTextString()).commit();
				// 修改密码成功后跳转到登录界面
				httpLogout();
				JSYApplication.getInstance().finishActivities();
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.showMessage(mContext, R.string.update_failed);
			}
			break;
		case Constant.user_regist_UpateRecAcc:
			DialogUtil.getInstance().cannleDialog();
			String UpateRecAcc = (String) list.get(1);
			if ("success".equals(UpateRecAcc)) {
				ToastUtil.showMessage(mContext, R.string.update_success);
				UserInfo userInfo = (UserInfo) ACache.get(
						getApplicationContext()).getAsObject("userInfo");
				userInfo.setNickName(edt_nickname.getTextString());
				ACache.get(getApplicationContext()).put("userInfo", userInfo);
				BaseActivity.editor.putString("TrueName",
						edt_truename.getTextString());
				BaseActivity.editor.putString("Nickname",
						edt_nickname.getTextString()).commit();
			} else {
				ToastUtil.showMessage(mContext, R.string.update_failed);
			}
			break;
		case Constant.user_regist_GetMyMobileUnitList:
			ArrayList<MyMobileUnit> unitList = (ArrayList<MyMobileUnit>) list
					.get(1);
			adapter.setData(unitList);
			adapter.notifyDataSetChanged();
			if (unitList == null || unitList.size() == 0) {
				ToastUtil.showMessage(mContext, "暂无可加入单位");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}