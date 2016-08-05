package com.jsy_jiaobao.main.leave;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.CheckLeaveModelPost;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 功能说明：请假系统审核功能
 * 
 * @author MSL
 * 
 */
public class CheckerAgreeOrActivity extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener, TextWatcher {
	private static final int FLAG_AGREE = 1;// 同意
	private static final int FLAG_DISAGREE = 2;// 拒绝
	private int checkFlag = FLAG_AGREE;// 选取同意或拒绝
	private int leaveId = 0;// 假条Id
	private int checkRole;// 审核级别
	private String manName;// 请假人
	private CheckLeaveModelPost post;// 审批的假条

	private Context mContext;
	private EditText edt_text;// 批注编辑框



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initViews();
		leaveId = getIntent().getIntExtra("LeaveId", 0);// 假条ID
		checkRole = getIntent().getIntExtra("CheckRole", 0);// 审核级别
		manName = getIntent().getStringExtra("ManName");// 请假人姓名
	}

	private void initViews() {
		setContentLayout(R.layout.leave_activity_checkleave);
		CheckerActivityControler.getInstance().setContext(this);
		setActionBarTitle(R.string.leave_approval);
		RadioGroup rg_agreeOr = (RadioGroup) findViewById(R.id.rg_agreeOr);
		RadioButton rb_agree = (RadioButton) findViewById(R.id.rb_agree);
		edt_text = (EditText) findViewById(R.id.edt_reason);
		edt_text.addTextChangedListener(this);
		rg_agreeOr.setOnCheckedChangeListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		rb_agree.setChecked(true);
		post = new CheckLeaveModelPost(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
		MobclickAgent.onPause(mContext);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_agree:// 同意
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerAgreeOrActivity_rb_agree));
			checkFlag = FLAG_AGREE;
			break;
		case R.id.rb_disagree:// 拒绝
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerAgreeOrActivity_rb_disagree));
			checkFlag = FLAG_DISAGREE;
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:// 提交按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerAgreeOrActivity_btn_submit));
			String text = "";
			switch (checkFlag) {
			case FLAG_AGREE:
				text = "“同意”";
				break;
			case FLAG_DISAGREE:
				text = "“拒绝”";
				break;
			default:
				break;
			}
			Builder submit = new AlertDialog.Builder(mContext);
			submit.setTitle("提示");
			submit.setMessage("您确定要" + text + manName + "的请假申请吗？");
			submit.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							post.setTabid(leaveId);
							post.setLevel(checkRole);
							if (edt_text.getText().toString().length()>0) {
								post.setNote(edt_text.getText().toString());
							}
							post.setCheckFlag(checkFlag);
							CheckerActivityControler.getInstance()
									.CheckLeaveModel(post);// 进行审批
						}
					});
			submit.setNegativeButton("取消", null);
			submit.show();
			break;
		default:
			break;
		}
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_CheckLeaveModel:
			int s = (Integer) list.get(1);
			if (s == 777) {
				DialogUtil.getInstance().cannleDialog();
				ToastUtil.showMessage(mContext, "审核成功");
				Intent i = new Intent();
				setResult(998, i);
				finish();
			}
			break;
		case 999:
			int a = (Integer) list.get(1);
			if (a == 999) {
				DialogUtil.getInstance().cannleDialog();
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	/**
	 * 屏蔽回车
	 */
	@Override
	public void afterTextChanged(Editable s) {
		for (int i = s.length(); i > 0; i--) {
			if (s.subSequence(i - 1, i).toString().equals("\n"))
				s.replace(i - 1, i, "");
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
