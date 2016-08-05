package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.LeaveUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.Leave;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 功能说明：老师请假
 * 
 * @author 
 * 
 */
public class TeaLeaveFragment extends Fragment implements
		OnItemSelectedListener, OnClickListener, TextWatcher {
	private final static String TAG = "TeaLeaveFragment";
	private View view;
	private Context mContext;
	private String typeChose;// 选择的请假类型
	private Leave mLeave;// 假条
	private ArrayList<String> typeList;// 请假类型
	private ArrayList<LeaveTime> mLeaveTime;// 请假时间

	private LinearLayout lLayout_time;// 时间段布局
	private TextView tv_timeAdd;// 添加时间
	private EditText edt_reason;// 描述输入框
	private Spinner spn_type;// 请假类型
	private Button btn_commit;// 提交按钮

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mLeaveTime = new ArrayList<LeaveTime>();
	}

	public static TeaLeaveFragment newInstance() {
		return new TeaLeaveFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leave_fragment_leave_tea, null);
		mContext = getActivity();
		initViews();
		return view;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lLayout_time.removeAllViews();
		mLeaveTime.clear();
		LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTime);
		LeaveFragmentControl.getInstance().setContext(this);
	}

	private void initViews() {
		lLayout_time = (LinearLayout) view
				.findViewById(R.id.leave_llayout_time);
		tv_timeAdd = (TextView) view.findViewById(R.id.leave_tv_timeadd);
		edt_reason = (EditText) view.findViewById(R.id.leave_edt_reasion);
		spn_type = (Spinner) view.findViewById(R.id.leave_spn_type);
		btn_commit = (Button) view.findViewById(R.id.leave_btn_commit);
		tv_timeAdd.setOnClickListener(this);
		edt_reason.addTextChangedListener(this);
		btn_commit.setOnClickListener(this);
		typeList = new ArrayList<String>();
		SpinnerAdapter typeAdapter = new SpinnerAdapter(mContext, typeList);
		String[] type = { "事假", "病假", "其他" };
		for (int i = 0; i < type.length; i++) {
			typeList.add(type[i]);
		}
		spn_type.setAdapter(typeAdapter);
		spn_type.setOnItemSelectedListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBusUtil.register(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	/**
	 * 用户是否看到
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (edt_reason != null) {
				edt_reason.setEnabled(true);// 允许输入
			}
		} else {
			if (edt_reason != null) {
				edt_reason.setEnabled(false);// 不允许输入，并隐藏软键盘
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leave_tv_timeadd:// 增加时间段
			MobclickAgent.onEvent(mContext,
					getResources()
							.getString(R.string.TeaLeaveFragment_time_add));
			if (mLeaveTime.size() > 4) {
				ToastUtil.showMessage(mContext,
						R.string.leave_time_noMoreThanFive);
			} else {
				LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTime);
			}
			break;
		case R.id.leave_btn_commit:// 提交
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.TeaLeaveFragment_btn_commit));
			if (mLeaveTime != null && mLeaveTime.size() > 0) {
				Builder commitDialog = new AlertDialog.Builder(mContext);
				commitDialog.setTitle(R.string.hint);
				commitDialog.setMessage(R.string.decided_to_submit);
				commitDialog.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								setLeave();
							}
						});
				commitDialog.setNegativeButton(R.string.cancel, null);
				commitDialog.show();
			} else {
				ToastUtil.showMessage(mContext, R.string.leave_add_time);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置假条参数
	 */
	private void setLeave() {
		mLeave = new Leave(mContext);
		mLeave.setManId(BaseActivity.sp.getInt("UserID", 0));// 非教宝号
		mLeave.setManName(BaseActivity.sp.getString("UserName", null));
		mLeave.setUnitClassId(0);
		mLeave.setManType(1);
		mLeave.setLeaveType(typeChose);
		String leaveReason = (String) edt_reason.getText().toString();
		if (leaveReason != null) {
			mLeave.setLeaveReason(leaveReason);
		}
		mLeave.setLeaveTimeList(mLeaveTime);
		LeaveFragmentControl.getInstance().NewLeaveModel(mLeave);
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		if (tag == LeaveConstant.leave_NewLeaveModel) {// 请假成功
			DialogUtil.getInstance().cannleDialog();
			ToastUtil.showMessage(mContext, R.string.leave_success);
			spn_type.setSelection(0);// 成功后重置请假数据
			edt_reason.setText("");
			lLayout_time.removeAllViews();
			mLeaveTime.clear();
			LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTime);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		MobclickAgent.onEvent(mContext,
				getResources().getString(R.string.TeaLeaveFragment_choseType));
		typeChose = typeList.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
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

}
