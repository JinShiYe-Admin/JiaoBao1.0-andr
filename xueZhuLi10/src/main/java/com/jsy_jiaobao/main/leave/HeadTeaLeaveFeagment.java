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
import com.jsy_jiaobao.po.sys.StuInfo;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
//一些修改1 2016-5-6 MSL
//1.增加-请假成功后将选择的学生置为null,把学生姓名改为“请选择学生”

/**
 * 功能说明：班主任请假（包括本人请假、替学生请假）
 * 
 * @author
 */
public class HeadTeaLeaveFeagment extends Fragment implements
		OnItemSelectedListener, OnClickListener, OnCheckedChangeListener,
		TextWatcher {
	private static final String TAG = "HeadTeaLeaveFeagment";
	private static final int TYPE_SELF = 1;// 本人
	private static final int TYPE_STU = 0;// 学生
	private View view;
	private Context mContext;
	private int mType;// 本人或学生请假
	private String stuName;// 学生请假人姓名
	private String typeChose;// 选择的请假类型
	private Leave mLeave;// 假条
	private StuInfo mStuInfo;// 选择的学生信息
	private ArrayList<String> typeList;// 请假类型
	private ArrayList<LeaveTime> mLeaveTimeList;// 请假时间段
	
	private LinearLayout lLayout_choseStu;// 选择学生
	private LinearLayout lLayout_time;// 时间段布局
	private RadioGroup rg_identity;// 本人、学生切换区域
	private TextView tv_leaver;// 请假人
	private TextView tv_timeAdd;// 添加时间
	private EditText edt_reason;// 描述输入框	
	private Button btn_commit;// 提交按钮
	private RadioButton rb_self;// 本人请假
	private Spinner spn_type;// 请假类型
	private SpinnerAdapter spnAdapter_type;// 请假类型

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLeaveTimeList = new ArrayList<LeaveTime>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leave_fragment_teareplace, container,false);
		mContext = getActivity();
		initViews();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		LeaveFragmentControl.getInstance().setContext(this);
		lLayout_time.removeAllViews();
		mLeaveTimeList.clear();
		mType = TYPE_SELF;
		rb_self.setChecked(true);
		changeData(mType);
		LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);
		rg_identity.setOnCheckedChangeListener(this);
		lLayout_choseStu.setOnClickListener(this);
		tv_timeAdd.setOnClickListener(this);
		edt_reason.addTextChangedListener(this);
		btn_commit.setOnClickListener(this);
		typeList = new ArrayList<String>();
		spnAdapter_type = new SpinnerAdapter(mContext, typeList);
		String[] type = { "事假", "病假", "其他" };
		for (int i = 0; i < type.length; i++) {
			typeList.add(type[i]);
		}
		spn_type.setAdapter(spnAdapter_type);
		spn_type.setOnItemSelectedListener(this);		
	}

	private void initViews() {
		lLayout_choseStu = (LinearLayout) view
				.findViewById(R.id.leave_llayout_chosestu);
		lLayout_time = (LinearLayout) view
				.findViewById(R.id.leave_llayout_time);
		rg_identity = (RadioGroup) view.findViewById(R.id.leave_rg);
		tv_leaver = (TextView) view.findViewById(R.id.leave_tv_stuname);
		tv_timeAdd = (TextView) view.findViewById(R.id.leave_tv_time_add);
		edt_reason = (EditText) view.findViewById(R.id.leave_edt_reason);
		spn_type = (Spinner) view.findViewById(R.id.leave_spn_type);
		btn_commit = (Button) view.findViewById(R.id.leave_btn_commit);
		rb_self = (RadioButton) view.findViewById(R.id.rb_self);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leave_tv_time_add:// 增加时间段
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.HeadTeaLeaveFeagment_time_add));
			if (mLeaveTimeList.size() > 4) {
				ToastUtil.showMessage(mContext,
						R.string.leave_time_noMoreThanFive);
			} else {
				LeaveUtils
						.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);
			}
			break;
		case R.id.leave_btn_commit:// 提交按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.HeadTeaLeaveFeagment_btn_commit));
			Builder commitDialog = new AlertDialog.Builder(mContext);
			commitDialog.setTitle(R.string.hint);
			commitDialog.setMessage(R.string.decided_to_submit);
			commitDialog.setPositiveButton(R.string.sure,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							changePostInfo(mType);
						}
					});
			commitDialog.setNegativeButton(R.string.cancel, null);
			commitDialog.show();
			break;
		case R.id.leave_llayout_chosestu:// 点击选择学生
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.HeadTeaLeaveFeagment_chose_stu));
			Intent i = new Intent(getActivity(), AllClassNamesActivity.class);
			startActivityForResult(i, 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else if (requestCode == 0) {// 获取学生信息			
			mStuInfo = (StuInfo) data
					.getSerializableExtra(AllClassNamesActivity.NAME);
			stuName = mStuInfo.getStdName();
			//Log.d(TAG, stuName);
			tv_leaver.setText(stuName);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		EventBusUtil.register(this);
	}

	@Override
	public void onDetach() {
		EventBusUtil.unregister(this);
		super.onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
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

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		if (tag == LeaveConstant.leave_NewLeaveModel) {// 请假成功						
			ToastUtil.showMessage(mContext, R.string.leave_success);			
			spn_type.setSelection(0);// 成功后初始化请假数据
			edt_reason.setText("");
			lLayout_time.removeAllViews();
			mLeaveTimeList.clear();
			mStuInfo = null;
			tv_leaver.setText(R.string.leave_chosestudent_please);
			LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);
			DialogUtil.getInstance().cannleDialog();
		}
	}

	/***
	 * 设置选择的理由类型
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		MobclickAgent.onEvent(
				mContext,
				getResources().getString(
						R.string.HeadTeaLeaveFeagment_choseType));
		typeChose = typeList.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 切换本人请假、学生请假
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_self://本人
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.HeadTeaLeaveFeagment_rb_self));
			mType = TYPE_SELF;
			changeData(mType);
			typeList.remove(0);
			spnAdapter_type.notifyDataSetChanged();
			break;
		case R.id.rb_stu://学生
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.HeadTeaLeaveFeagment_rb_stu));
			mType = TYPE_STU;
			changeData(mType);
			typeList.add(0, "补课");
			spnAdapter_type.notifyDataSetChanged();
			break;
		default:
			break;
		}

	}

	/**
	 * 是否显示选择学生区域
	 * 
	 * @param type
	 *            1 显示选择学生区域；0不显示选择学生区域
	 */
	private void changeData(int type) {
		if (type == TYPE_SELF) {// 本人
			lLayout_choseStu.setVisibility(View.GONE);
		} else if (type == TYPE_STU) {// 学生
			lLayout_choseStu.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 选取身份不同，上传参数不同
	 * 
	 * @param type
	 *            1 本人；0学生
	 */
	private void changePostInfo(int type) {
		mLeave = new Leave(mContext);
		mLeave.setLeaveType(typeChose);
		String leaveReason = (String) edt_reason.getText().toString();
		if (leaveReason != null) {
			mLeave.setLeaveReason(leaveReason);
		}
		if (type == TYPE_SELF) {
			mLeave.setManId(BaseActivity.sp.getInt("UserID", 0));// 非教宝号
			mLeave.setManName(BaseActivity.sp.getString("UserName", null));
			mLeave.setUnitClassId(0);
			mLeave.setManType(mType);
			getLeaveTime();
		} else if (type == TYPE_STU) {
			if (mStuInfo != null) {
				mLeave.setManId(mStuInfo.getStudentID());// 非教宝号
				mLeave.setManName(mStuInfo.getStdName());
				mLeave.setGradeStr(mStuInfo.getGradeName());
				mLeave.setClassStr(mStuInfo.getClassName());
				mLeave.setUnitClassId(mStuInfo.getUnitClassID());
				mLeave.setManType(mType);
				getLeaveTime();
			} else {
				ToastUtil.showMessage(mContext, R.string.leave_chosestudent_please);
			}
		}
	}

	/**
	 * 设置时间段参数
	 */
	private void getLeaveTime() {
		if (mLeaveTimeList.size() == 0 || mLeaveTimeList == null) {
			ToastUtil.showMessage(mContext, R.string.leave_add_time);
		} else {
			mLeave.setLeaveTimeList(mLeaveTimeList);
			LeaveFragmentControl.getInstance().NewLeaveModel(mLeave);
		}
	}

	public static HeadTeaLeaveFeagment newInstance() {
		return new HeadTeaLeaveFeagment();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	/**
	 * 
	 * 请假理由描述屏蔽回车
	 */
	@Override
	public void afterTextChanged(Editable s) {
		for (int i = s.length(); i > 0; i--) {
			if (s.subSequence(i - 1, i).toString().equals("\n"))
				s.replace(i - 1, i, "");
		}
	}
}
