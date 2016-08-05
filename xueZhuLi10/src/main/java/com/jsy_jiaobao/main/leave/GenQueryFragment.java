package com.jsy_jiaobao.main.leave;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.GenStuInfo;
import com.jsy_jiaobao.po.leave.GenStuInfos;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MonPickerDialog;
import com.jsy_jiaobao.po.leave.MyLeaveModel;
import com.jsy_jiaobao.po.leave.MyLeaves;
import com.jsy_jiaobao.po.leave.MyLeavesPost;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 功能说明：家长请假记录查询
 * 
 * @author MSL
 * 
 */
public class GenQueryFragment extends Fragment implements
		OnItemSelectedListener, OnClickListener, OnRefreshListener2<ScrollView> {
	private final static String TAG = "GenQueryFragment";
	private View view;
	private Context mContext;
	private int pageNum = 1;// 第几页，默认为1
	private int rowCount = 0;// pageNum=1为0，第二页起从前一页的返回结果中获得
	private String timeChose;// 选择的时间
	private boolean newLeaveModel = false;// 生成了新的请假条，0 否,1 是
	private Calendar calendar;// 时间
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM",
			Locale.getDefault());// 时间格式	
	private GenStuInfo mGenStuInfoChose;// 选择的学生
	private MyLeavesPost myLeavesPost;// 查询假条
	private ArrayList<String> nameList;// 家长的学生
	private ArrayList<GenStuInfo> mGenStuInfoList;// 获取的学生信息
	private ArrayList<MyLeaveModel> myLeaveModelList;
	private TextView tv_time;// 选择时间
	private SpinnerAdapter spnAdapter_leaver;// 请假人
	private UnitClassLeavesAdapter<?> unitClassLeavesAdapter;// 假条列表
	private PullToRefreshScrollView refreshView;//上拉加载更多，下拉刷新的控件

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public static GenQueryFragment newInstance() {
		return new GenQueryFragment();
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leave_fragment_gen_query, null);
		initViews();
		return view;
	}

	private void initViews() {
		GenFragmentController.getInstance().setContext(this);
		tv_time = (TextView) view.findViewById(R.id.leave_tv_time);
		TextView tv_symbol;// 选择时间
		tv_symbol = (TextView) view.findViewById(R.id.leave_tv_symbol);
		ListView lv_leave;// 假条列表
		lv_leave = (ListView) view.findViewById(R.id.leave_cuslistview);
		Spinner spn_leaver;// 请假人
		spn_leaver = (Spinner) view.findViewById(R.id.leave_spn_name);
		refreshView = (PullToRefreshScrollView) view
				.findViewById(R.id.leave_pulltorefreshscrollview);
		tv_time.setOnClickListener(this);
		tv_symbol.setOnClickListener(this);
		refreshView.setOnRefreshListener(this);
		refreshView.setMode(Mode.BOTH);
		calendar = Calendar.getInstance();
		mGenStuInfoChose = new GenStuInfo();
		nameList = new ArrayList<>();
		myLeaveModelList = new ArrayList<>();
		spnAdapter_leaver = new SpinnerAdapter(mContext, nameList);
		unitClassLeavesAdapter = new UnitClassLeavesAdapter<Object>(this);
		spn_leaver.setAdapter(spnAdapter_leaver);
		spn_leaver.setOnItemSelectedListener(this);
		unitClassLeavesAdapter.setType(1);// 设置姓名列不可见
		lv_leave.setAdapter(unitClassLeavesAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		String time = sdf.format(new Date());
		myLeavesPost = new MyLeavesPost(mContext);
		myLeavesPost.setManType(0);// 0学生1老师
		myLeavesPost.setRowCount(0);
		timeChose = time + "-01";// 特殊要求增加“-01”，我也不知道为什么
		myLeavesPost.setsDateTime(timeChose);
		tv_time.setText(time);
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
	 * 切换fragment，用户是否看到
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			spnAdapter_leaver.notifyDataSetChanged();// 强制刷新请假人选项
			if (newLeaveModel) {// 生成了新的假条
				getDefaultPost();
				GenFragmentController.getInstance().GetMyLeaves(myLeavesPost);// 获取请假记录列表
				newLeaveModel = false;
			}
		}
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetMyStdInfo:// 家长的学生数据
			GenStuInfos genStuInfos = (GenStuInfos) list.get(1);
			if (genStuInfos != null) {
				mGenStuInfoList = genStuInfos.getList();
				nameList.clear();
				for (int i = 0; i < mGenStuInfoList.size(); i++) {
					nameList.add(mGenStuInfoList.get(i).getStdName());// 学生姓名数据源
				}
			} else {
				ToastUtil.showMessage(mContext,
						R.string.leave_getstudentinfo_false);// 没有获取到学生信息
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case LeaveConstant.leave_GetMyLeaves:// 家长请假记录数据
			refreshView.onRefreshComplete();
			MyLeaves myLeaves = (MyLeaves) list.get(1);
			ArrayList<MyLeaveModel> myLevelList = myLeaves.getList();
			if (myLevelList.size() == 0) {// 获得的数据为空或者没有数据
				if (myLeaveModelList.size() == 0) {// 假条列表为空。因为删除一条假条后会获取一条新的记录补充列表，需要增加这个判断
					ToastUtil.showMessage(mContext, R.string.leave_no_myleaves);
				}
			} else {
				MyLeaveModel myLeaveModel = myLevelList.get(0);
				myLeaveModelList.addAll(myLevelList);
				rowCount = myLeaveModel.getRowCount();
			}
			unitClassLeavesAdapter.setData(myLeaveModelList);
			unitClassLeavesAdapter.notifyDataSetChanged();
			DialogUtil.getInstance().cannleDialog();
			break;
		case LeaveConstant.leave_NewLeaveModel:
			newLeaveModel = (Boolean) list.get(1);// 请假成功后返回查询界面，必须刷新列表，不然再进行撤回会出错
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.leave_tv_time
				|| v.getId() == R.id.leave_tv_symbol) {// 切换时间查询
			MobclickAgent
					.onEvent(
							mContext,
							getResources().getString(
									R.string.GenQueryFragment_tv_time));
			new MonPickerDialog(mContext, new OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					tv_time.setText(year + "-"
							+ String.format("%02d", (monthOfYear + 1)));
					timeChose = tv_time.getText().toString() + "-01";// 特殊要求增加“-01”，我也不知道为什么
					getDefaultPost();// 设置获取第一页数据
					GenFragmentController.getInstance().GetMyLeaves(
							myLeavesPost);
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == R.id.leave_spn_name) {// 切换学生查询
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.GenQueryFragment_leaver_name));
			mGenStuInfoChose = mGenStuInfoList.get(position);
			getDefaultPost();// 设置获取第一页数据
			myLeavesPost.setName(mGenStuInfoChose.getStdName());
			GenFragmentController.getInstance().GetMyLeaves(myLeavesPost);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 撤回一条假条后补充列表的数据（即如果获取到记录，保持请假列表为20条记录）
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == LeaveConstant.leave_postDelelteLeaveId) {
			int position = data.getIntExtra("Position", -1);
			if (position != -1) {
				int size = myLeaveModelList.size();
				myLeaveModelList.remove(position);
				if (rowCount == size || rowCount == 0) {
					unitClassLeavesAdapter.setData(myLeaveModelList);
					unitClassLeavesAdapter.notifyDataSetChanged();
				} else {
					myLeavesPost.setNumPerPage(1);
					myLeavesPost.setPageNum(size);
					myLeavesPost.setRowCount(rowCount - 1);
					GenFragmentController.getInstance().GetMyLeaves(
							myLeavesPost);
				}
			}
		}
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		getDefaultPost();
		GenFragmentController.getInstance().GetMyLeaves(myLeavesPost);// 获取请假列表
	}

	/**
	 * 初始化列表的参数
	 */
	private void getDefaultPost() {
		pageNum = 1;
		myLeaveModelList.clear();
		unitClassLeavesAdapter.setData(null);
		unitClassLeavesAdapter.notifyDataSetChanged();
		myLeavesPost.setNumPerPage(20);
		myLeavesPost.setPageNum(1);
		myLeavesPost.setRowCount(0);
		myLeavesPost.setsDateTime(timeChose);
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		Log.i("onPullUpToRefresh", rowCount + "-" + myLeaveModelList.size());
		if (rowCount == myLeaveModelList.size() || rowCount == 0) {
			// 只有第一页有数据，或者通过第二页以后返回的值为0，来判断下一页是否有数据
			ToastUtil.showMessage(mContext, R.string.no_more);// 提示没有更多了
			refreshView.onRefreshComplete();
		} else if (myLeaveModelList.size() % 20 == 0) {
			// 1.不满足第一个if条件，说明下一页可能有数据
			// 2.撤回假条后补充记录，但是列表依旧不满20条记录，说明下一页没有数据
			pageNum = pageNum + 1;
			myLeavesPost.setPageNum(pageNum);
			GenFragmentController.getInstance().GetMyLeaves(myLeavesPost);// 提交数据，获取请假列表
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);// 提示没有更多了
			refreshView.onRefreshComplete();
		}
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}
}
