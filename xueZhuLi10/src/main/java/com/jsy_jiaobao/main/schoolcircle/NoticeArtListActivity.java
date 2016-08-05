package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.XListView;
import com.jsy_jiaobao.customview.XListView.IXListViewListener;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.personalcenter.PublishArticaleActivity;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.NoticeGetArthInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 展示文章界面
 * 
 * @author admin
 * 
 */
public class NoticeArtListActivity extends BaseActivity implements
		PublicMethod, IXListViewListener {
	public static String TAG = "NoticeArtListActivity";
	@ViewInject(R.id.layout_xlistview)
	private XListView listView;
	@ViewInject(R.id.noticeartlist_btn_send)
	private Button btn_send;

	private List<ArthInfo> arthList = new ArrayList<ArthInfo>();
	private NoticeArtListAdapter listAdapter;
	private Context mContext;
	private int currPage = 1;
	private int UnitID;
	private int SchoolID = 0;
	private int UnitType = 1;
	private String UnitName = "";
	private String SectionFlag = "1";
	private boolean havemore = false;
	private boolean myunit = false;
	private ACache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			UnitID = savedInstanceState.getInt("UnitID");
			SchoolID = savedInstanceState.getInt("SchoolID");
			UnitType = savedInstanceState.getInt("UnitType");
			UnitName = savedInstanceState.getString("UnitName");
			SectionFlag = savedInstanceState.getString("SectionFlag");
			if (null == SectionFlag) {
				SectionFlag = "1";
			}
			myunit = savedInstanceState.getBoolean("myunit");
			if (UnitType == 3) {
				if (UnitID > 0) {
					UnitID = UnitID * -1;
				}
			}
		} else {
			initPassData();
		}
		initViews();
		initListener();
	}

	/**
	 * 获取Intent携带过来的数据
	 */
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				UnitID = bundle.getInt("UnitID");
				SchoolID = bundle.getInt("SchoolID");
				UnitType = bundle.getInt("UnitType");
				UnitName = bundle.getString("UnitName");
				SectionFlag = bundle.getString("SectionFlag");

				if (null == SectionFlag) {
					SectionFlag = "1";
				}
				myunit = bundle.getBoolean("myunit");
				if (UnitType == 3) {
					if (UnitID > 0) {
						UnitID = UnitID * -1;
					}
				}
			}
		}
	}

	/**
	 * 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("SectionFlag", SectionFlag);
		outState.putString("UnitName", UnitName);
		outState.putInt("UnitType", UnitType);
		outState.putInt("SchoolID", SchoolID);
		outState.putInt("UnitID", UnitID);
	}
	/**
	 * 初始化界面
	 */
	@Override
	public void initViews() {
		setContentLayout(R.layout.ui_articlelist);
		ViewUtils.inject(this);
		mContext = this;
		btn_send.setVisibility(View.GONE);
		if (SectionFlag.equals("-1")) {
			SectionFlag = "1";
			setActionBarTitle(UnitName + ":"
					+ getResources().getString(R.string.share_artical));
		} else if (SectionFlag.equals("-2")) {
			SectionFlag = "2";
			setActionBarTitle(UnitName + ":"
					+ getResources().getString(R.string.show_artical));
		} else {
			// 是否是我的单位
			if (myunit) {
				btn_send.setVisibility(View.VISIBLE);
			} else {
				btn_send.setVisibility(View.GONE);
			}
			setActionBarTitle(UnitName + ":"
					+ MessageCenterActivity.MessageCenterTitle);
			if (getResources().getString(R.string.messagecenter_title_notice)
					.equals(MessageCenterActivity.MessageCenterTitle)) {
				SectionFlag = "1";
			} else if (getResources().getString(
					R.string.messagecenter_title_show).equals(
					MessageCenterActivity.MessageCenterTitle)) {
				SectionFlag = "2";
			}
		}
		ArtListActivityController.getInstance().setContext(this);
		listAdapter = new NoticeArtListAdapter(mContext, false);
		listView.setAdapter(listAdapter);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		mCache = ACache.get(getApplicationContext());
		// 刷新数据
		onRefresh();
	}

	/**
	 * 取本单位栏目文章
	 */
	@Override
	public void initDeatilsData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNum", String.valueOf(currPage));
		params.addBodyParameter("UnitID", String.valueOf(UnitID));
		params.addBodyParameter("SectionFlag", SectionFlag);// 1共享，2展示,99个人空间文章
		params.addBodyParameter("orderDirection", "0");// 0按最新排序，1按最热排序，默认为0
		ArtListActivityController.getInstance().ArthListIndex(params);
	}

	/**
	 * 监听事件
	 */
	@Override
	public void initListener() {
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						PublishArticaleActivity.class);
				if (SchoolID != 0) {
					intent.putExtra("uType", 3);
					intent.putExtra("SchoolID", SchoolID);
				} else {
					intent.putExtra("uType", UnitType);
				}
				intent.putExtra("UnitID", UnitID);
				intent.putExtra("UnitName", UnitName);
				intent.putExtra("SectionFlag", SectionFlag);
				startActivity(intent);
			}
		});
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void onRefresh() {
		havemore = true;
		if (arthList != null) {
			arthList.clear();
		}
		currPage = 1;
		initDeatilsData();
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		if (havemore) {
			currPage++;
			initDeatilsData();
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
	 * EventBus功能模块
	 * 
	 * @功能 获取网络请求返回数据 并作处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_notice_ArthListIndex:// 本单位文章列表
			listView.stopRefresh();
			listView.stopLoadMore();
			NoticeGetArthInfo getArthInfo = (NoticeGetArthInfo) list.get(1);
			if (getArthInfo.getList().size() < 20) {
				havemore = false;
			}
			arthList.addAll(getArthInfo.getList());
			listView.setRefreshTime(mCache.getAsString(UnitID + "refreshTime"));
			listAdapter.setData(arthList);
			listAdapter.notifyDataSetChanged();
			Date date = Calendar.getInstance().getTime();
			String str_time = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
			mCache.put(UnitID + "refreshTime", str_time);
			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回键
	 * 
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
