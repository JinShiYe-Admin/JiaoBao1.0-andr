package com.jsy_jiaobao.main.leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.AdminClassModel;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MonPickerDialog;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.jsy_jiaobao.po.leave.UserClassInfoModel;
import com.jsy_jiaobao.po.leave.UserClassList;
import com.umeng.analytics.MobclickAgent;

/**
 * 功能说明：请假系统筛选条件
 * 
 * @author
 * 
 */
public class CheckerScreenConditionActivity extends BaseActivity implements
		OnCheckedChangeListener, OnItemSelectedListener, OnClickListener {
	private static final String TAG = "CheckerScreenConditionActivity";
	private static final int TYPE_TEA = 1;// 教职工
	private static final int TYPE_STU = 0;// 学生
	private static final int TYPE_GATE = 3;// 门卫
	private static final int ROLE_1 = 1;// 一审
	private static final int ROLE_2 = 2;// 二审
	private static final int ROLE_3 = 3;// 三审
	private static final int ROLE_4 = 4;// 四审
	private static final int ROLE_5 = 5;// 五审

	private int mType;//选择教职工或学生
	private int mCheckRol;// 审核级别，第几审
	private int choseClassId;// 班级ID
	private int checkType;// 0待审核，1已审核，2统计查询
	private boolean isHasStu;//有审核学生权限
	private boolean isHasTea;//有审核教职工权限
	private boolean isHeadTea;//是否是班主任
	private String choseTime;//选择的时间
	private String mCheckerRoleName;//审核人名称
	private String choseClassName;//选择的班级名称
	private String choseGradeName;//选择的年级名称
	private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM",
			Locale.getDefault());//时间格式
	private MyAdminClasses myAdminClasses;//获取的班级	
	
	private ArrayList<Integer> approvalList;//审核教职工级别列表
	private ArrayList<Integer> approvalStuList;//审核学生级别列表
	private ArrayList<String> approvalStuNoteList;//审核学生名称列表
	private ArrayList<String> approvalNoteList;//审核教职工名称列表
	private ArrayList<String> classNameList;//班级名称列表
	private ArrayList<String> gradeNameList;//年级名称列表
	private ArrayList<String> realGradeList;//
	private ArrayList<AdminClassModel> classModelList;
	private ArrayList<AdminClassModel> headChoseGradeClassList;
	private ArrayList<UserClassInfoModel> choseGradeClassList;
	private ArrayList<UserClassInfoModel> userClassModelList;
	private ArrayList<HashMap<String, ArrayList<UserClassInfoModel>>> gradeClass;
	private ArrayList<HashMap<String, ArrayList<AdminClassModel>>> headGradeClass;

	private Context mContext;
	private LinearLayout ly_checkRole;//审核级别
	private LinearLayout layout_grade;//年级
	private LinearLayout layout_class;//班级
	private TextView tv_time;//时间
	private TextView tv_symbol;//时间
	private Spinner spn_role;//审核级别
	private Spinner spn_grade;//审核年级
	private Spinner spn_class;//审核班级
	private RadioGroup rg_leaveRole;//教职工，学生审核选项布局
	private RadioButton rb_tea;//教职工选项
	private RadioButton rb_stu;//学生选项
	private SpinnerAdapter classAdapter;//班级
	private SpinnerAdapter gradeAdapter;//年级
	private SpinnerAdapter teaSAdapter;//教职工
	private SpinnerAdapter stuSAdapter;//学生

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		checkType = (Integer) getIntent().getIntExtra("CheckType", -1);
		if (sp.getInt("isAdmin", 0) == 2 || sp.getInt("isAdmin", 0) == 3) {// 判断是否是班主任
			isHeadTea = true;
		} else {
			isHasTea = false;
		}
		initViews();
	}

	private void initViews() {
		setContentLayout(R.layout.leave_checker_screen_condition);
		setActionBarTitle(R.string.leave_screen_condition);
		CheckerScreenConditionActivityControler.getInstance().setContext(this);
		ly_checkRole = (LinearLayout) findViewById(R.id.ly_choseRole);// 审核级别布局
		layout_grade = (LinearLayout) findViewById(R.id.layout_grade);// 年级布局
		layout_class = (LinearLayout) findViewById(R.id.layout_class);// 班级布局
		tv_time = (TextView) findViewById(R.id.tv_time);// 时间
		tv_symbol = (TextView) findViewById(R.id.tv_symbol);// 时间
		spn_role = (Spinner) findViewById(R.id.spn_checkRole);// 选择审核级别
		spn_grade = (Spinner) findViewById(R.id.sp_screen_grade);// 选择年级
		spn_class = (Spinner) findViewById(R.id.sp_screen_class);// 选择班级
		rg_leaveRole = (RadioGroup) findViewById(R.id.rg_leave_role);// 选择教职工或学生
		rb_tea = (RadioButton) findViewById(R.id.rb_leave_tea);// 选择教职工
		rb_stu = (RadioButton) findViewById(R.id.rb_leave_stu);// 选择教职工
		realGradeList = new ArrayList<String>();
		classNameList = new ArrayList<String>();
		headChoseGradeClassList = new ArrayList<AdminClassModel>();
		gradeClass = new ArrayList<HashMap<String, ArrayList<UserClassInfoModel>>>();
		headGradeClass = new ArrayList<HashMap<String, ArrayList<AdminClassModel>>>();
		classAdapter = new SpinnerAdapter(mContext, classNameList);
		tv_time.setText(sFormat.format(new Date()));
		getApprovalInfo();// 获取权限
		tv_time.setOnClickListener(this);
		tv_symbol.setOnClickListener(this);
		rg_leaveRole.setOnCheckedChangeListener(this);
		spn_role.setOnItemSelectedListener(this);
		spn_class.setAdapter(classAdapter);
		spn_class.setOnItemSelectedListener(this);
		initData();
	}

	@Override
	public void setResultForLastActivity() {
		super.setResultForLastActivity();
	}

	/**
	 * 返回选取的筛选条件
	 */
	private void setMyResult() {
		Intent i = new Intent();
		Bundle args = new Bundle();
		choseTime = tv_time.getText().toString();
		args.putInt("ChoseClassId", choseClassId);// 班级ID
		args.putString("ChoseClassName", choseClassName);// 班级名称
		args.putString("ChoseGradeName", choseGradeName);// 年级名称
		args.putString("ChoseTime", choseTime);// 选择的时间
		args.putString("CheckerRoleName", mCheckerRoleName);// 选择的审核人名称
		args.putInt("Type", mType);// 选择教职工或学生
		args.putInt("CheckRole", mCheckRol);// 审核级别，第几审
		args.putInt("CheckType", checkType);// 查询类型：0待审核，1已审核，2统计查询
		i.putExtras(args);
		setResult(999, i);
	}

	private void initData() {
		if (checkType == TYPE_GATE) {//门卫
			mType = TYPE_TEA;
			rg_leaveRole.setVisibility(View.VISIBLE);
			changeData(mType);
		} else {
			if (isHasStu && isHasTea) {// 同时有审核教职工，学生权限
				rg_leaveRole.setVisibility(View.VISIBLE);
				rb_tea.setChecked(true);
			} else {
				if (isHasStu) {//只有审核学生权限
					rb_tea.setVisibility(View.GONE);//隐藏教职工选项
					rb_stu.setChecked(true);
					mType = TYPE_STU;
					changeData(mType);
				} else if (isHasTea) {//只有审核教职工权限
					rb_stu.setVisibility(View.GONE);//隐藏审核学生选项
					rb_tea.setChecked(true);
					mType = TYPE_TEA;
					changeData(mType);
				}
			}
		}
	}

	/**
	 * 获取账号的权限
	 */
	private void getApprovalInfo() {
		approvalNoteList = new ArrayList<String>();
		approvalStuNoteList = new ArrayList<String>();
		approvalList = new ArrayList<Integer>();
		approvalStuList = new ArrayList<Integer>();
		boolean hasApprovalStdA = sp.getBoolean("ApproveListStdA", false);
		boolean hasApprovalStdB = sp.getBoolean("ApproveListStdB", false);
		boolean hasApprovalStdC = sp.getBoolean("ApproveListStdC", false);
		boolean hasApprovalStdD = sp.getBoolean("ApproveListStdD", false);
		boolean hasApprovalStdE = sp.getBoolean("ApproveListStdE", false);
		boolean hasApprovalA = sp.getBoolean("ApproveListA", false);
		boolean hasApprovalB = sp.getBoolean("ApproveListB", false);
		boolean hasApprovalC = sp.getBoolean("ApproveListC", false);
		boolean hasApprovalD = sp.getBoolean("ApproveListD", false);
		boolean hasApprovalE = sp.getBoolean("ApproveListE", false);
		String LevelNoteStdA = sp.getString("LevelNoteStdA", null);
		String LevelNoteStdB = sp.getString("LevelNoteStdB", null);
		String LevelNoteStdC = sp.getString("LevelNoteStdC", null);
		String LevelNoteStdD = sp.getString("LevelNoteStdD", null);
		String LevelNoteStdE = sp.getString("LevelNoteStdE", null);
		String LevelNoteA = sp.getString("LevelNoteA", null);
		String LevelNoteB = sp.getString("LevelNoteB", null);
		String LevelNoteC = sp.getString("LevelNoteC", null);
		String LevelNoteD = sp.getString("LevelNoteD", null);
		String LevelNoteE = sp.getString("LevelNoteE", null);

		if (hasApprovalA) {
			approvalNoteList.add(LevelNoteA == null ? "一审" : LevelNoteA);
			approvalList.add(ROLE_1);
		}

		if (hasApprovalB) {
			approvalNoteList.add(LevelNoteB == null ? "二审" : LevelNoteB);
			approvalList.add(ROLE_2);
		}
		if (hasApprovalC) {
			approvalNoteList.add(LevelNoteC == null ? "三审" : LevelNoteC);
			approvalList.add(ROLE_3);
		}
		if (hasApprovalD) {
			approvalNoteList.add(LevelNoteD == null ? "四审" : LevelNoteD);
			approvalList.add(ROLE_4);
		}
		if (hasApprovalE) {
			approvalNoteList.add(LevelNoteE == null ? "五审" : LevelNoteE);
			approvalList.add(ROLE_5);
		}
		if (hasApprovalStdA) {
			approvalStuNoteList.add(LevelNoteStdA == null ? "一审"
					: LevelNoteStdA);
			approvalStuList.add(ROLE_1);
		} else if (isHeadTea) {
			approvalStuNoteList.add("班主任");
			approvalStuList.add(ROLE_1);
		}
		if (hasApprovalStdB) {
			approvalStuNoteList.add(LevelNoteStdB == null ? "二审"
					: LevelNoteStdB);
			approvalStuList.add(ROLE_2);
		}
		if (hasApprovalStdC) {
			approvalStuNoteList.add(LevelNoteStdC == null ? "三审"
					: LevelNoteStdC);
			approvalStuList.add(ROLE_3);
		}
		if (hasApprovalStdD) {
			approvalStuNoteList.add(LevelNoteStdD == null ? "四审"
					: LevelNoteStdD);
			approvalStuList.add(ROLE_4);
		}
		if (hasApprovalStdE) {
			approvalStuNoteList.add(LevelNoteStdE == null ? "五审"
					: LevelNoteStdE);
			approvalStuList.add(ROLE_5);
		}
		if (approvalList.size() > 0 && approvalList != null) {
			isHasTea = true;
			teaSAdapter = new SpinnerAdapter(mContext, approvalNoteList);
		} else {
			isHasTea = false;
		}
		if (approvalStuList.size() > 0 && approvalStuList != null) {
			isHasStu = true;
			stuSAdapter = new SpinnerAdapter(mContext, approvalStuNoteList);
		} else {
			isHasStu = false;
		}
	}

	/**
	 * 教职工，学生的界面切换显示
	 * 
	 * @param type
	 */
	private void changeData(int type) {
		switch (type) {
		case TYPE_TEA:// 只有审核老师权限
			if (checkType == TYPE_GATE) {
				layout_grade.setVisibility(View.VISIBLE);
			} else {
				layout_grade.setVisibility(View.GONE);// 隐藏年级
			}
			layout_class.setVisibility(View.GONE);// 隐藏班级
			spn_role.setAdapter(teaSAdapter);// 加载审核老师权限数据
			spn_role.setOnItemSelectedListener(this);
			break;
		case TYPE_STU:// 只有审核学生权限
			layout_grade.setVisibility(View.VISIBLE);
			layout_class.setVisibility(View.VISIBLE);
			spn_role.setAdapter(stuSAdapter);// 加载审核学生权限数据
			spn_role.setOnItemSelectedListener(this);
			break;
		default:
			break;
		}
		if (checkType == 2) {// 统计查询
			layout_class.setVisibility(View.INVISIBLE);
			ly_checkRole.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * 不同的身份发送不同的请求
	 */
	private void changePost() {
		if (mCheckRol == ROLE_1 && mType == TYPE_STU) {// 班主任选择学生
			if (checkType == 2) {// 如果是查询统计
				int UID = sp.getInt("UnitID", 0);
				CheckerScreenConditionActivityControler.getInstance()
						.getunitclass(UID);

			} else {
				String jiaobaohao = sp.getString("JiaoBaoHao", null);
				CheckerScreenConditionActivityControler.getInstance()
						.GetMyAdminClass(jiaobaohao);// 班主任获取关联的班级
			}
		} else {
			int UID = sp.getInt("UnitID", 0);
			CheckerScreenConditionActivityControler.getInstance().getunitclass(
					UID);// 获取所有年级班级
		}
	}

	/**
	 * 选择教职工或学生
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_leave_tea://教职工
			mType = TYPE_TEA;
			changeData(mType);
			break;
		case R.id.rb_leave_stu://学生
			mType = TYPE_STU;
			changeData(mType);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_time || v.getId() == R.id.tv_symbol) {// 时间选择
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerScreenConditionActivity_tv_time));
			String textTime = tv_time.getText().toString();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(getDayTime(textTime));
			new MonPickerDialog(mContext, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					tv_time.setText(year + "-"
							+ String.format("%02d", (monthOfYear + 1)));
					choseTime = tv_time.getText().toString();
					Log.e("chose_time", choseTime);
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetMyAdminClass:// 班主任获取关联的班级
			DialogUtil.getInstance().cannleDialog();
			gradeClass.clear();
			myAdminClasses = (MyAdminClasses) list.get(1);
			classModelList = myAdminClasses.getList();
			if (classModelList.size() > 0) {
				classNameList.clear();
				gradeNameList = new ArrayList<String>();
				for (int i = 0; i < classModelList.size(); i++) {
					classNameList.add(classModelList.get(i).getClsName());
					gradeNameList.add(classModelList.get(i).getGradeName());
				}
				ArrayList<AdminClassModel> classlist = classModelList;
				for (int i = 0; i < classlist.size(); i++) {
					ArrayList<AdminClassModel> s = new ArrayList<AdminClassModel>();
					HashMap<String, ArrayList<AdminClassModel>> jh = new HashMap<String, ArrayList<AdminClassModel>>();
					s.add(classModelList.get(i));
					for (int j = i + 1; j < classlist.size(); j++) {
						if (classModelList.get(i).equals(classlist.get(j))) {
							s.add(classlist.get(j));
							classlist.remove(classlist.get(j));
						}
					}
					jh.put(classModelList.get(i).getGradeName(), s);
					headGradeClass.add(jh);
				}
			}
			realGradeList = gradeNameList;
			for (int i = 0; i < realGradeList.size(); i++) {
				String name = realGradeList.get(i);
				for (int j = i + 1; j < realGradeList.size(); j++) {
					if (name.equals(realGradeList.get(j))) {
						if (realGradeList.size() > 1) {
							realGradeList.remove(j);
						}
					}
				}
			}
			Log.d(TAG + "realNames", realGradeList.toString());
			Log.d(TAG + "gradeNames", gradeNameList.toString());
			gradeAdapter = new SpinnerAdapter(mContext, realGradeList);
			spn_grade.setAdapter(gradeAdapter);
			spn_grade.setOnItemSelectedListener(this);
			choseClassName = classNameList.get(0);
			break;
		case LeaveConstant.leave_getunitclass:
			DialogUtil.getInstance().cannleDialog();
			UserClassList userClassList = (UserClassList) list.get(1);
			userClassModelList = userClassList.getList();
			classNameList.clear();
			gradeNameList = new ArrayList<String>();
			for (UserClassInfoModel uClass : userClassModelList) {
				classNameList.add(uClass.getClassName());
				gradeNameList.add(uClass.getGradeName());
			}
			ArrayList<UserClassInfoModel> classlist = userClassModelList;
			for (int i = 0; i < classlist.size(); i++) {
				ArrayList<UserClassInfoModel> s = new ArrayList<UserClassInfoModel>();
				HashMap<String, ArrayList<UserClassInfoModel>> jh = new HashMap<String, ArrayList<UserClassInfoModel>>();
				s.add(classlist.get(i));
				for (int j = i + 1; j < classlist.size(); j++) {
					if (userClassModelList.get(i).getGradeName()
							.equals(classlist.get(j).getGradeName())) {
						s.add(classlist.get(j));
						classlist.remove(classlist.get(j));
					}
				}
				jh.put(userClassModelList.get(i).getGradeName(), s);
				gradeClass.add(jh);
			}
			realGradeList = gradeNameList;
			for (int i = 0; i < realGradeList.size(); i++) {
				String name = realGradeList.get(i);
				for (int j = i + 1; j < realGradeList.size(); j++) {
					if (name.equals(realGradeList.get(j))) {
						if (realGradeList.size() > 1) {
							realGradeList.remove(j);
						}
					}
				}
			}
			Log.d(TAG + "realNames", realGradeList.toString());
			Log.d(TAG + "gradeNames", gradeNameList.toString());
			realGradeList.add(0, "全部");// 全部年级
			gradeAdapter = new SpinnerAdapter(mContext, realGradeList);
			spn_grade.setAdapter(gradeAdapter);
			spn_grade.setOnItemSelectedListener(this);
			if (checkType == 2) {
				layout_class.setVisibility(View.GONE);
				ly_checkRole.setVisibility(View.GONE);
			}
		default:
			break;
		}
	}

	private long getDayTime(String date) {
		Date dt2 = null;
		try {
			dt2 = sFormat.parse(date);
		} catch (ParseException e) {
			dt2 = new Date();
		}
		return dt2.getTime();
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.sp_screen_grade:// 年级选择
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerScreenConditionActivity_grade));
			if ("全部".equals(realGradeList.get(position))) {
				choseGradeName = null;
				if (checkType != 2) {
					classNameList.clear();
					classNameList.add(0, "全部");
					classAdapter.notifyDataSetChanged();
				}
			} else {
				choseGradeName = realGradeList.get(position);
				if (checkType != 2) {
					if (mCheckRol == ROLE_1 && mType == TYPE_STU) {// 班主任
						if (headGradeClass == null
								|| headGradeClass.size() == 0) {
							// ToastUtil.showMessage(mContext, "没班级");
						} else {
							Log.d(TAG + "size", headGradeClass.size() + "");
							Log.d(TAG + "posdtion", position + "");

							headChoseGradeClassList = headGradeClass.get(
									position).get(choseGradeName);
							classNameList.clear();
							for (AdminClassModel cls : headChoseGradeClassList) {
								classNameList.add(cls.getClsName());
							}
							classAdapter.notifyDataSetChanged();
						}
					} else if (gradeClass != null && gradeClass.size() > 0) {
						choseGradeClassList = gradeClass.get(position - 1).get(
								choseGradeName);
						classNameList.clear();
						for (UserClassInfoModel cls : choseGradeClassList) {
							classNameList.add(cls.getClassName());
						}
						classNameList.add(0, "全部");
						spn_class.setAdapter(classAdapter);
						spn_class.setOnItemSelectedListener(this);
					} else {
						ToastUtil.showMessage(mContext, "没年级");
					}
				}
			}
			break;
		case R.id.sp_screen_class:// 班级选择
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerScreenConditionActivity_class));
			if ("全部".equals(classNameList.get(position))) {
				choseClassName = null;
			} else {
				if (mCheckRol == ROLE_1 && mType == TYPE_STU) {
					if (headChoseGradeClassList != null
							|| headChoseGradeClassList.size() != 0) {
						AdminClassModel choseClass = headChoseGradeClassList
								.get(position);
						choseClassId = choseClass.getTabID();
						choseClassName = choseClass.getClsName();
					}
				} else {
					if (choseGradeClassList != null) {
						UserClassInfoModel choseClass = choseGradeClassList
								.get(position - 1);
						choseClassId = choseClass.getClassID();
						choseClassName = choseClass.getClassName();
					}
				}
			}
			break;
		case R.id.spn_checkRole:// 审核级别选择
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.CheckerScreenConditionActivity_checkRole));
			switch (mType) {
			case TYPE_TEA:
				mCheckRol = approvalList.get(position);
				mCheckerRoleName = approvalNoteList.get(position);
				int UID = sp.getInt("UnitID", 0);
				CheckerScreenConditionActivityControler.getInstance()
						.getunitclass(UID);// 获取所有年级班级
				break;
			case TYPE_STU:
				mCheckRol = approvalStuList.get(position);
				mCheckerRoleName = approvalStuNoteList.get(position);
				changePost();
				break;
			default:
				break;
			}
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 标题右上角增加“确定”按钮
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub_menu = menu.addSubMenu("确定");
		sub_menu.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub_menu.getItem().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						setMyResult();
						finish();
						return true;
					}
				});
		return super.onCreateOptionsMenu(menu);
	}
}