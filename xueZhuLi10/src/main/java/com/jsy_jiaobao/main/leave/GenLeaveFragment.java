package com.jsy_jiaobao.main.leave;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.LeaveUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.GenStuInfo;
import com.jsy_jiaobao.po.leave.GenStuInfos;
import com.jsy_jiaobao.po.leave.Leave;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.umeng.analytics.MobclickAgent;

//一些修改1 2016-5-4 MSL
//1.增加-家长身份请假时，发起人姓名使用获取的学生信息中家长的姓名

/**
 * 功能说明：家长请假
 * 
 * @author ShangLin Mo
 * 
 */
public class GenLeaveFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, TextWatcher {
	private final static String TAG = "GenLeaveFragment";
	private View view;
	private Context mContext;
	private String typeChose;// 选择的请假类型
	private String reasonDetail;// 请假描述
	private Leave mLeave;// 假条
	private GenStuInfo mChoseGenStuInfo;// 选择的学生信息
	private ArrayList<String> nameList;// 学生姓名
	private ArrayList<String> typeList;// 请假类型
	private ArrayList<GenStuInfo> mGenStuInfoList;// 获取的学生信息
	private ArrayList<LeaveTime> mLeaveTimeList;// 请假时间段

	private LinearLayout lLayout_time;// 时间段布局
	private TextView tv_timeAdd;// 添加时间
	private EditText edt_reason;// 描述输入框
	private Button btn_commit;// 提交按钮
	private Spinner spn_leaver;// 请假人
	private Spinner spn_type;// 请假类型
	private SpinnerAdapter spnAdapter_leaver;// 请假人
	private SpinnerAdapter spnAdapter_type;// 请假类型

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLeaveTimeList = new ArrayList<LeaveTime>();
	}

	public static GenLeaveFragment newInstance() {
		return new GenLeaveFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leave_fragment_gen_leave, null);
		mContext = getActivity();
		initViews();
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lLayout_time.removeAllViews();
		mLeaveTimeList.clear();
		LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);// 生成默认的时间段
		GenFragmentController.getInstance().setContext(this);
		String jiaobaohao = BaseActivity.sp.getString("JiaoBaoHao", "");
		if (jiaobaohao != null) {
			GenFragmentController.getInstance().GetMyStdInfo(jiaobaohao);// 获取学生信息
		} else {
			Log.e(TAG, "jiaobaohao is null ");
		}
	}

	/**
	 * 初始化界面
	 */

	private void initViews() {
		lLayout_time = (LinearLayout) view
				.findViewById(R.id.leave_llayout_time);
		tv_timeAdd = (TextView) view.findViewById(R.id.leave_tv_timeadd);
		edt_reason = (EditText) view.findViewById(R.id.leave_edt_reason);
		btn_commit = (Button) view.findViewById(R.id.leave_btn_commit);
		spn_leaver = (Spinner) view.findViewById(R.id.leave_spn_name);
		spn_type = (Spinner) view.findViewById(R.id.leave_spn_type);
		tv_timeAdd.setOnClickListener(this);
		edt_reason.addTextChangedListener(this);
		btn_commit.setOnClickListener(this);
		nameList = new ArrayList<String>();
		typeList = new ArrayList<String>();
		mChoseGenStuInfo = new GenStuInfo();
		spnAdapter_leaver = new SpinnerAdapter(mContext, nameList);
		spnAdapter_type = new SpinnerAdapter(mContext, typeList);
		spn_leaver.setAdapter(spnAdapter_leaver);
		spn_leaver.setOnItemSelectedListener(this);
		String[] type = { "补课", "病假", "事假", "其他" };
		typeList.addAll(Arrays.asList(type));
		spn_type.setAdapter(spnAdapter_type);
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
		case R.id.leave_tv_timeadd:// 添加时间段
			MobclickAgent.onEvent(mContext,
					getResources()
							.getString(R.string.GenLeaveFragment_time_add));
			if (mLeaveTimeList.size() > 4) {
				ToastUtil.showMessage(mContext,
						R.string.leave_time_noMoreThanFive);// 提示不能超过5个时间段
			} else {
				LeaveUtils
						.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);// 添加默认的时间段
			}
			break;
		case R.id.leave_btn_commit:// 提交
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.GenLeaveFragment_btn_commit));
			if (mLeaveTimeList != null && mLeaveTimeList.size() > 0) {// 判断是否有时间段
				// 已经完成判断是否有时间段重叠的方法，但为了与苹果端保持一致，故将代码注释掉
				// Boolean boolean1
				// =CompareLeaveTimeList.compare(leaveTimeList);//判读时间段是否重叠
				// if (boolean1) {
				// Log.i("boolean1", "时间段无重叠");
				Builder commitDialog = new AlertDialog.Builder(mContext);
				commitDialog.setTitle(R.string.hint);
				commitDialog.setMessage(R.string.decided_to_submit);
				commitDialog.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								setLeave();// 设置假条参数
							}
						});
				commitDialog.setNegativeButton(R.string.cancel, null);
				commitDialog.show();
				// }else {
				// Log.i("boolean1", "时间段重叠");
				// ToastUtil.showMessage(mContext, "时间段有重叠！");
				// }

			} else {
				ToastUtil.showMessage(mContext, R.string.leave_add_time);// 提示时间段不能为空
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
		mLeave.setManId(mChoseGenStuInfo.getTabID());// 请假人的人员Id，学生ID或老师Id,非教宝号
		mLeave.setManName(mChoseGenStuInfo.getStdName());// 请假人姓名
		mLeave.setManType(0);// 学生设置为0
		mLeave.setLeaveType(typeChose);// 请假类型
		mLeave.setGradeStr(mChoseGenStuInfo.getGradeName());// 年级
		mLeave.setClassStr(mChoseGenStuInfo.getClsName());// 班级
		mLeave.setUnitClassId(mChoseGenStuInfo.getClassId());// 班级Id,学生请假须提供
		mLeave.setWriter(BaseActivity.sp.getString("UserName",
				mChoseGenStuInfo.getGenName()));// 发起人姓名
		reasonDetail = edt_reason.getText().toString();
		if (reasonDetail != null) {
			mLeave.setLeaveReason(reasonDetail);// 请假理由描述
		}
		mLeave.setLeaveTimeList(mLeaveTimeList);// 请假时间段
		GenFragmentController.getInstance().NewLeaveModel(mLeave);// 提交数据，生成一条请假条
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetMyStdInfo:// 获取学生信息
			GenStuInfos genStuInfos = (GenStuInfos) list.get(1);
			if (genStuInfos != null) {// 获取到学生信息
				mGenStuInfoList = genStuInfos.getList();
				nameList.clear();
				for (int i = 0; i < mGenStuInfoList.size(); i++) {
					nameList.add(mGenStuInfoList.get(i).getStdName());
					spnAdapter_leaver.notifyDataSetChanged();
				}
			} else {
				ToastUtil.showMessage(mContext,
						R.string.leave_getstudentinfo_false);// 没有获取到学生信息
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case LeaveConstant.leave_NewLeaveModel:// 请假成功
			ToastUtil.showMessage(mContext, R.string.leave_success);
			spn_leaver.setSelection(0); // 重置请假数据
			spn_type.setSelection(0);
			edt_reason.setText("");
			lLayout_time.removeAllViews();
			mLeaveTimeList.clear();
			LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTimeList);
			DialogUtil.getInstance().cannleDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.leave_spn_type:// 请假类型
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.GenLeaveFragment_leave_type));
			typeChose = typeList.get(position);
			break;
		case R.id.leave_spn_name:// 请假人
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.GenLeaveFragment_leaver_name));
			mChoseGenStuInfo = mGenStuInfoList.get(position);
			break;
		default:
			break;
		}
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
			if (s.subSequence(i - 1, i).toString().equals("\n")) {
				s.replace(i - 1, i, "");
			}
		}
	}
}
