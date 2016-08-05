package com.jsy_jiaobao.main.leave;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.AdminClassModel;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.jsy_jiaobao.po.sys.GetStuInfo;
import com.jsy_jiaobao.po.sys.StuInfo;
import com.umeng.analytics.MobclickAgent;

/**
 * 功能说明：班主任请假时显示本班学生名单。通过教宝号获取班级Id，再通过班级Id获取学生的信息
 * 
 * @author 
 * 
 */
public class AllClassNamesActivity extends BaseActivity implements
		OnItemClickListener, OnItemSelectedListener {
	public static final String NAME = "com.jsy_jiaobao.main.leave.AllClassNamesActivity.name";
	private final static String TAG = "AllClassNamesActivity";
	private Context mContext;
	private String name;// 学生姓名
	private StuInfo choseStu;// 选择的学生的信息
	private AdminClassModel choseClass;// 选择的班级
	private MyAdminClasses myAdminClasses;// 获取的班级信息
	private ArrayList<String> classNameList;// 班级名称
	private ArrayList<String> nameList;// 学生名称
	private ArrayList<StuInfo> stuInfoList;// 学生信息
	private ArrayList<AdminClassModel> classModelList;// 班级信息

	private LinearLayout lLayout_chooseClass;// 选择班级布局
	private CusListView clv_stuNames;// 学生姓名列表
	private Spinner spn_class;// 选择班级
	private SpinnerAdapter spnAdapter_className;// 班级名称
	private ArrayAdapter<String> arrayAdapter_stuName;// 学生名称

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initViews();
	}

	private void initViews() {
		setContentLayout(R.layout.leave_activity_class_names);
		setActionBarTitle(R.string.leave_class_stuNames);
		AllClassNamesActivityController.getInstance().setContext(this);
		lLayout_chooseClass = (LinearLayout) findViewById(R.id.leave_llayout_choose_class);
		clv_stuNames = (CusListView) findViewById(R.id.leave_clv_stunames);
		spn_class = (Spinner) findViewById(R.id.leave_spn_class);
		classNameList = new ArrayList<String>();
		nameList = new ArrayList<String>();
		classNameList = new ArrayList<String>();
		spnAdapter_className = new SpinnerAdapter(mContext, classNameList);
		spn_class.setAdapter(spnAdapter_className);
		spn_class.setOnItemSelectedListener(this);
		String jiaobaohao = BaseActivity.sp.getString("JiaoBaoHao", null);// 获取账号的教宝号
		if (jiaobaohao != null) {
			AllClassNamesActivityController.getInstance().GetMyAdminClass(
					jiaobaohao);// 通过教宝号获取班级信息
		} else {
			Log.e(TAG, "jiaobao号为空");
		}
	}

	/**
	 * 处理获取的班级名单
	 */
	private void getClassNameList() {
		classNameList.clear();
		if (classModelList.size() > 0) {// 获取到班级
			if (classModelList.size() == 1) {// 只有一个关联的班级
				lLayout_chooseClass.setVisibility(View.VISIBLE);
				choseClass = classModelList.get(0);
				classNameList.add(choseClass.getClsName());
			} else if (classModelList.size() > 1) {// 关联多个班级
				lLayout_chooseClass.setVisibility(View.VISIBLE);
				for (int i = 0; i < classModelList.size(); i++) {
					classNameList.add(classModelList.get(i).getClsName());
				}
			}
			spnAdapter_className.notifyDataSetChanged();// 显示班级名单
		} else {
			ToastUtil.showMessage(mContext, R.string.leave_noConect_Class);// 提示未获取到班级
		}
	}

	/**
	 * 监听选取的班级，并通过班级Id获取到该班级的学生信息
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		choseClass = (AdminClassModel) classModelList.get(position);
		if (choseClass != null) {
			AllClassNamesActivityController.getInstance().getClassStdInfo(
					choseClass.getTabID());// 获取学生信息
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 监听选取的学生，并返回选取的学生信息
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		name = (String) nameList.get(position);
		choseStu = (StuInfo) stuInfoList.get(position);
		Log.d(TAG, name);
		Intent data = new Intent();
		data.putExtra(NAME, choseStu);
		setResult(Activity.RESULT_OK, data);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		EventBusUtil.unregister(this);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void setResultForLastActivity() {
		super.setResultForLastActivity();
		Intent i = new Intent();
		setResult(404, i);//点击标题的返回键返回404
	}

	/**
	 * 处理获取的数据
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetMyAdminClass:// 获取班级信息
			myAdminClasses = (MyAdminClasses) list.get(1);
			classModelList = myAdminClasses.getList();
			getClassNameList();//处理获取的班级名单
			break;
		case Constant.msgcenter_chat_getClassStdInfo:// 获取学生信息
			GetStuInfo getStuInfo = (GetStuInfo) list.get(1);
			nameList.clear();
			stuInfoList = new ArrayList<StuInfo>();
			ArrayList<StuInfo> mstuInfoList = getStuInfo.getList();
			if (mstuInfoList != null && mstuInfoList.size() != 0) {//获取到学生信息
				for (int i = 0; i < mstuInfoList.size(); i++) {
					nameList.add(mstuInfoList.get(i).getStdName());
				}
				String[] names = new String[nameList.size()];
				for (int i = 0; i < names.length; i++) {
					names[i] = nameList.get(i);
				}
				Comparator c = Collator.getInstance(Locale.CHINA);
				Arrays.sort(names, c);// 对学生姓名进行排序
				for (int i = 0; i < names.length; i++) {
					for (int j = 0; j < mstuInfoList.size(); j++) {
						if (names[i].equals(mstuInfoList.get(j).getStdName())) {
							stuInfoList.add(mstuInfoList.get(j));// 通过排序后的学生姓名，将学生信息排序
						}
					}
				}
				arrayAdapter_stuName = new ArrayAdapter<String>(mContext,
						android.R.layout.simple_list_item_1, names);
				clv_stuNames.setAdapter(arrayAdapter_stuName);
				clv_stuNames.setOnItemClickListener(this);
			} else {
				ToastUtil.showMessage(mContext,
						R.string.leave_getstudentinfo_false);//提示未获取到学生信息
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
