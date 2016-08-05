package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.PickedItem;

/**
 * 往期精选界面
 * 
 * @author admin
 * 
 */
public class QiuZhiSiftListActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView> {
	private PullToRefreshScrollView mPullRefreshScrollView;
	private Context mContext;
	private int pageNum = 1;
	private CusListView listView;
	private QiuZhiSiftListAdapter adapter;
	private ArrayList<PickedItem> answerList = new ArrayList<PickedItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
		} else {
			initPassData();
		}
		initViews();
	}

	/**
	 * @method self defined
	 * @功能 获取Intent携带的数据
	 */
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
			}
		}
	}

	/**
	 * 覆写方法
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * @功能 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_siftlist);
		mContext = this;
		QiuZhiSiftListActivityController.getInstance().setContext(this);
		QiuZhiSiftListActivityController.getInstance().PickedIndex(pageNum,
				rowCount);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		listView = (CusListView) findViewById(R.id.qiuzhi_siftlist_listview);
		setActionBarTitle("往期回顾");
		mPullRefreshScrollView.setOnRefreshListener(this);
		adapter = new QiuZhiSiftListAdapter(mContext);
		adapter.setData(answerList);
		listView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	int rowCount = 0;

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_PickedIndex:
			// 各期精选列表
			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<PickedItem> answerlist = (ArrayList<PickedItem>) list
					.get(1);
			if (null != answerlist) {
				rowCount = answerlist.get(0).getRowCount();
				answerList.addAll(answerlist);
				adapter.notifyDataSetChanged();
			}
			break;
		case Constant.msgcenter_qiuzhi_ShowPicked:
			// ShowPicked picked = (ShowPicked) list.get(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 下拉刷新
	 * 
	 * @功能 清除数据 重新请求数据
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		pageNum = 1;
		answerList.clear();
		rowCount = 0;
		QiuZhiSiftListActivityController.getInstance().PickedIndex(pageNum,
				rowCount);
	}

	/**
	 * 上拉加载
	 * 
	 * @功能 判断是否还有数据 如果有加载下一页数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (rowCount == answerList.size()) {
			ToastUtil.showMessage(mContext, R.string.no_more);
			mPullRefreshScrollView.onRefreshComplete();
		} else {
			pageNum++;
			QiuZhiSiftListActivityController.getInstance().PickedIndex(pageNum,
					rowCount);
		}
	}

	/**
	 * android 系统返回键
	 * 
	 * @功能 结束当前activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub
	}
}
