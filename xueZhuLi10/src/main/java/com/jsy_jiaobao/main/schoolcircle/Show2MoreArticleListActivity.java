package com.jsy_jiaobao.main.schoolcircle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.XListView;
import com.jsy_jiaobao.customview.XListViewFooter;
import com.jsy_jiaobao.customview.XListView.IXListViewListener;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 本单位||本班更多文章界面
 * 
 * @author admin
 * 
 */
public class Show2MoreArticleListActivity extends BaseActivity implements
		IXListViewListener {
	public static String ActivityTag = "Show2MoreArticleListActivity";
	@ViewInject(R.id.layout_xlistview)
	private XListView listView;
	private Context mContext;
	private Show2ArtListAdapter adapter;
	private String moreType = "unit";
	private int curPageNum = 1;
	private boolean curUnitHaveMore = true;
	private ArrayList<ArthInfo> curUnitArtList = new ArrayList<ArthInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			moreType = savedInstanceState.getString("moreType");
		} else {
			initPassData();
		}
		initViews();
		initDatas();
	}

	/**
	 * 获取Intent携带的数据
	 * 
	 * @功能 Type
	 */
	private void initPassData() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				moreType = bundle.getString("moreType");
			}
		}
	}

	/**
	 * 保存意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("moreType", moreType);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentView(R.layout.layout_xlistview);
		ViewUtils.inject(this);
		mContext = this;
		adapter = new Show2ArtListAdapter(mContext);
		if ("unit".equals(moreType)) {
			adapter.setData(curUnitArtList, ShowClickType.unitmore);
			setActionBarTitle(R.string.unit_news);
		} else if ("class".equals(moreType)) {
			adapter.setData(curUnitArtList, ShowClickType.classmore);
			setActionBarTitle(R.string.class_news);
		}
		adapter.setParentView(findViewById(R.id.layout_xlistView));
		ShowMoreArticleListActivityController.getInstance().setContext(this);
		mCache = ACache.get(getApplicationContext());
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
	}

	/**
	 * 加载数据
	 */
	private void initDatas() {
		listView.getmFooterView().setState(XListViewFooter.STATE_LOADING);
		listView.getmFooterView().setClickable(false);
		if ("unit".equals(moreType)) {
			UnitArthListIndex();
		} else if ("class".equals(moreType)) {
			AllMyClassArthList();
		}

	}

	/**
	 * 获取当前单位展示
	 */
	public void UnitArthListIndex() {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNum", String.valueOf(curPageNum));
		ShowMoreArticleListActivityController.getInstance().UnitArthListIndex(
				params);
	}

	/**
	 * 获取所有班级展示
	 */
	public void AllMyClassArthList() {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNum", String.valueOf(curPageNum));
		ShowMoreArticleListActivityController.getInstance().AllMyClassArthList(
				params);
	}

	/**
	 * 刷新
	 */
	@Override
	public void onRefresh() {
		curUnitHaveMore = true;
		if (curUnitArtList != null) {
			curUnitArtList.clear();
		}
		curPageNum = 1;
		if ("unit".equals(moreType)) {
			UnitArthListIndex();
		} else if ("class".equals(moreType)) {
			AllMyClassArthList();
		}
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		if (curUnitHaveMore) {
			curPageNum++;
			if ("unit".equals(moreType)) {
				UnitArthListIndex();
			} else if ("class".equals(moreType)) {
				AllMyClassArthList();
			}
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);
			listView.stopLoadMore();
		}
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

	/**
	 * EventBus模块
	 * 
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_show_UnitArthListIndex_more:// 本单位更多文章
		case Constant.msgcenter_show_AllMyClassArthList_more:// 本班更多文章
			try {
				listView.getmFooterView()
						.setState(XListViewFooter.STATE_NORMAL);
				listView.getmFooterView().setClickable(true);
				listView.stopRefresh();
				listView.stopLoadMore();

				@SuppressWarnings("unchecked")
				ArrayList<ArthInfo> updata = (ArrayList<ArthInfo>) list.get(1);
				if (updata.size() < 20) {
					curUnitHaveMore = false;
				} else {
					curUnitHaveMore = true;
				}
				curUnitArtList.addAll(updata);

				adapter.notifyDataSetChanged();
				listView.setRefreshTime(mCache.getAsString(ActivityTag
						+ "refreshTime"));
				Date date = Calendar.getInstance().getTime();
				String str_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
						Locale.getDefault()).format(date);
				mCache.put(ActivityTag + "refreshTime", str_time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
