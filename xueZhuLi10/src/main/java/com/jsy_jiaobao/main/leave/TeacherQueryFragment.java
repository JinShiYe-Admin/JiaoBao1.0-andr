package com.jsy_jiaobao.main.leave;

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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MonPickerDialog;
import com.jsy_jiaobao.po.leave.MyLeaveModel;
import com.jsy_jiaobao.po.leave.MyLeaves;
import com.jsy_jiaobao.po.leave.MyLeavesPost;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 功能说明：老师请假记录查询
 * 
 * @author MSL
 * 
 */
public class TeacherQueryFragment extends Fragment implements OnClickListener,
		OnRefreshListener2<ScrollView> {
	private final static String TAG = "TeacherQueryFragment";
	private View view;
	private Context mContext;
	private int pageNum = 1;// 第几页，默认为1
	private int rowCount = 0;// pageNum=1为0，第二页起从前一页的返回结果中获得
	private String timeChose;// 选择的时间
	private boolean queryFirst = true;
	private boolean newLeaveModel = false;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM",
			Locale.getDefault());// 时间格式	
	private MyLeavesPost mLeavesPost;
	private ArrayList<MyLeaveModel> myLeaveList;

	private TextView tv_time;


	private PullToRefreshScrollView refreshView;
	private UnitClassLeavesAdapter unitClassLeavesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.leave_fragment_teacher_select, container,false);
		initViews();
		return view;
	}

	private void initViews() {
		LeaveFragmentControl.getInstance().setContext(this);
		tv_time = (TextView) view.findViewById(R.id.leave_tv_time);
		TextView tv_symbol;
		tv_symbol = (TextView) view.findViewById(R.id.leave_tv_symbol);
		ListView lv_leave;
		lv_leave = (ListView) view.findViewById(R.id.leave_cuslistview);
		refreshView = (PullToRefreshScrollView) view
				.findViewById(R.id.leave_pulltorefreshscrollview);
		tv_time.setOnClickListener(this);
		tv_symbol.setOnClickListener(this);
		refreshView.setOnRefreshListener(this);
		refreshView.setMode(Mode.BOTH);
		unitClassLeavesAdapter = new UnitClassLeavesAdapter(this);
		unitClassLeavesAdapter.setType(1);
		lv_leave.setAdapter(unitClassLeavesAdapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mLeavesPost = new MyLeavesPost(mContext);
		mLeavesPost.setManType(1);
		mLeavesPost.setRowCount(rowCount);
		String time = sdf.format(new Date());
		timeChose = time + "-01";// 特殊要求增加“-01”，我也不知道为什么
		tv_time.setText(time);
		mLeavesPost.setsDateTime(timeChose);
		myLeaveList = new ArrayList<>();
		getDefaultPost();
		// LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
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
		MobclickAgent.onPageStart(TAG);
	}

	/**
	 * 切换fragment，用户看到
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (queryFirst) {
				LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
				queryFirst = false;
			} else if (newLeaveModel) {
				getDefaultPost();
				LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
				newLeaveModel = false;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.leave_tv_time
				|| v.getId() == R.id.leave_tv_symbol) {// 切换时间查询
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.TeacherQueryFragment_tv_time));
			String textTime = tv_time.getText().toString();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(getDayTime(textTime));
			new MonPickerDialog(
					mContext,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							tv_time.setText(year + "-"
									+ String.format("%02d", (monthOfYear + 1)));
							timeChose = tv_time.getText().toString() + "-01";// 特殊要求增加“-01”，我也不知道为什么
							Log.e("chose_time", timeChose);
							getDefaultPost();
							LeaveFragmentControl.getInstance().GetMyLeaves(
									mLeavesPost);
						}
					}, calendar.get(Calendar.YEAR), calendar
							.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	}

	private long getDayTime(String date) {
		Date dt2;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			dt2 = new Date();
		}
		return dt2.getTime();
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetMyLeaves:
			DialogUtil.getInstance().cannleDialog();
			refreshView.onRefreshComplete();
			MyLeaves myLeaves = (MyLeaves) list.get(1);
			ArrayList<MyLeaveModel> myLeaveModels = myLeaves.getList();
			if (myLeaveModels == null || myLeaveModels.size() == 0) {// 获得的数据为空或者没有数据
				if (myLeaveList.size() == 0) {// 假条列表为空。因为删除一条假条后会获取一条新的记录补充列表，需要增加这个判断
					ToastUtil.showMessage(mContext, R.string.leave_no_myleaves);// 获得的数据为空并且没有原数据
				}
			} else {
				rowCount = myLeaveModels.get(0).getRowCount();
				myLeaveList.addAll(myLeaveModels);
			}
			unitClassLeavesAdapter.setData(myLeaveList);
			unitClassLeavesAdapter.notifyDataSetChanged();
			break;
		case LeaveConstant.leave_NewLeaveModel:
			// 请假成功后返回查询界面，必须刷新列表，不然进行撤回会出错
			newLeaveModel = (Boolean) list.get(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 撤回一条假条后补充列表的数据
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == LeaveConstant.leave_postDelelteLeaveId) {
			int po = data.getIntExtra("Position", -1);
			if (po != -1) {
				int size = myLeaveList.size();
				myLeaveList.remove(po);
				if (rowCount == size || rowCount == 0) {
					unitClassLeavesAdapter.setData(myLeaveList);
					unitClassLeavesAdapter.notifyDataSetChanged();
				} else {
					mLeavesPost.setNumPerPage(1);
					mLeavesPost.setPageNum(size);
					mLeavesPost.setRowCount(rowCount - 1);
					LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
				}
			}
		}
	}

	public static TeacherQueryFragment newInstance() {
		return new TeacherQueryFragment();
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		getDefaultPost();
		LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
	}

	/**
	 * 重置列表的参数
	 */
	private void getDefaultPost() {
		pageNum = 1;
		myLeaveList.clear();
		unitClassLeavesAdapter.setData(null);
		unitClassLeavesAdapter.notifyDataSetChanged();
		mLeavesPost.setNumPerPage(20);// 获取第一页数据
		mLeavesPost.setPageNum(1);
		mLeavesPost.setRowCount(0);
		mLeavesPost.setsDateTime(timeChose);
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (rowCount == myLeaveList.size() || rowCount == 0) {
			// 只有第一页有数据，或者通过第二页以后返回的值为0，来判断下一页是否有数据
			ToastUtil.showMessage(mContext, R.string.no_more);
			refreshView.onRefreshComplete();
		} else if (myLeaveList.size() % 20 == 0) {
			// 1.不满足第一个if条件，说明下一页可能有数据
			// 2.撤回假条后补充记录，但是列表依旧不满20条记录，说明下一页没有数据
			pageNum++;
			mLeavesPost.setPageNum(pageNum);
			LeaveFragmentControl.getInstance().GetMyLeaves(mLeavesPost);
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);// 提示没有更多了
			refreshView.onRefreshComplete();
		}
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}
}
