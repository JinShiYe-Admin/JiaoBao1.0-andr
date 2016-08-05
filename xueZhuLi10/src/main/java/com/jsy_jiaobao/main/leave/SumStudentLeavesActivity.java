package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.StuSumLeavesModel;

/**
 * 功能说明：请假系统，统计查询学生的请假记录
 * 
 * @author ShangLin Mo
 * 
 */
public class SumStudentLeavesActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView> {
	private Context mContext;
	private int unitClassId;//班级Id
	private String sDateTime;//查询时间
	private ArrayList<StuSumLeavesModel> sumLeaveList;//列表

	private CusListView listView;
	private CheckerQueryAdapter<?> queryAdapter;
	private PullToRefreshScrollView refreshScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		unitClassId = getIntent().getIntExtra("UnitClassId", 0);//班级Id
		sDateTime = getIntent().getStringExtra("sDateTime");//时间
		EventBusUtil.register(this);
		initViews();
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	private void initViews() {
		setContentLayout(R.layout.leave_activity_leave_sum);
		setActionBarTitle(R.string.leave_statistical_query);
		CheckerActivityControler.getInstance().setContext(this);
		listView = (CusListView) findViewById(R.id.cus_listView);
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		refreshScrollView.setOnRefreshListener(this);
		refreshScrollView.setMode(Mode.PULL_FROM_START);// 只允许上拉加载
		initData();
	}

	private void initData() {
		sumLeaveList = new ArrayList<StuSumLeavesModel>();
		queryAdapter = new CheckerQueryAdapter(this);
		listView.setAdapter(queryAdapter);
		CheckerActivityControler.getInstance().GetStudentSumLeaves(unitClassId,
				sDateTime);
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		if (tag == LeaveConstant.leave_GetStudentSumLeaves) {
			refreshScrollView.onRefreshComplete();
			ArrayList<StuSumLeavesModel> s1 = (ArrayList<StuSumLeavesModel>) list
					.get(1);
			sumLeaveList.clear();
			if (s1 == null) {
				queryAdapter.setData(null);
				queryAdapter.notifyDataSetChanged();
				ToastUtil.showMessage(mContext,
						R.string.leave_temporarily_no_data);
			} else {
				sumLeaveList.addAll(s1);
			}
			queryAdapter.setData(sumLeaveList);
			queryAdapter.notifyDataSetChanged();
			DialogUtil.getInstance().cannleDialog();
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		CheckerActivityControler.getInstance().GetStudentSumLeaves(unitClassId,
				sDateTime);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
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