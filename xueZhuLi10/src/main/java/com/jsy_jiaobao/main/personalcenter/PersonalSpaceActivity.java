package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoAdapter;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoGroupActivity;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.NoticeGetArthInfo;
import com.jsy_jiaobao.po.personal.Photo;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人空间
 * 
 * @author
 * 
 */
public class PersonalSpaceActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ScrollView> {
	private ImageView imageView;
	private TextView tv_name;
	private TextView title_more;
	private CusGridView gridView;
	private CusListView listView;
	private Context mContext;// 上下文
	private String JiaoBaoHao;// 教宝号
	private String UserName;// 用户名
	private int pageNum = 1;// 页码
	private NoticeArtListAdapter listAdapter;
	// private XListViewFooter mFooterView;
	private List<ArthInfo> arthList = new ArrayList<ArthInfo>();
	private boolean havemore = true;
	private UnitSpacePhotoAdapter photoAdapter;// 空间图片Adapter
	private PullToRefreshScrollView refreshView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			JiaoBaoHao = savedInstanceState.getString("JiaoBaoHao");
			UserName = savedInstanceState.getString("UserName");
		} else {
			// 传递过来的信息
			initPass();
		}
		// 初始化界面
		initViews();
		// 加载数据
		// initDatas();
	}

	/**
	 * 传递过来的信息
	 */
	private void initPass() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				JiaoBaoHao = bundle.getString("JiaoBaoHao");
				UserName = bundle.getString("UserName");
			}
		}
	}

	/**
	 * 保存意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("JiaoBaoHao", JiaoBaoHao);
		outState.putString("UserName", UserName);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentView(R.layout.activity_personalspace);
		findViews();
		mContext = this;
		mCache = ACache.get(getApplicationContext());
		PersonalSpaceActivityController.getInstance().setContext(this);
		if (TextUtils.isEmpty(UserName)) {
			UserName = getResources().getString(R.string.new_user);
		}
		setActionBarTitle(UserName + "的空间");
		tv_name.setText(UserName);
		title_more.setVisibility(View.GONE);
		listAdapter = new NoticeArtListAdapter(this, true);
		listView.setAdapter(listAdapter);
		String url = ACache.get(getApplicationContext()).getAsString("MainUrl")
				+ ConstantUrl.photoURL + "?AccID=" + JiaoBaoHao;
		JSYApplication.getInstance().bitmap.display(imageView, url);
		myShareingArth();
		PersonalSpaceActivityController.getInstance().GetNewPhoto(JiaoBaoHao,
				"3");

		photoAdapter = new UnitSpacePhotoAdapter(mContext);
		gridView.setAdapter(photoAdapter);
		/**
		 * 相册GridView的Item点击事件
		 */
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.PersonalSpaceActivity_album));
				intent.setClass(mContext, UnitSpacePhotoGroupActivity.class);
				bundle.putString("UnitID", JiaoBaoHao);
				bundle.putInt("UnitType", 9);
				bundle.putString("UnitName", UserName);
				bundle.putString("ClickName",
						getResources().getString(R.string.personal_album));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		refreshView.setMode(Mode.PULL_FROM_END);
		refreshView.setOnRefreshListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void findViews() {
		// TODO Auto-generated method
		imageView = (ImageView) findViewById(R.id.personalspace_img_photo);
		tv_name = (TextView) findViewById(R.id.personalspace_tv_name);
		title_more = (TextView) findViewById(R.id.personalspace_title_more);
		gridView = (CusGridView) findViewById(R.id.personalspace_gridview);
		listView = (CusListView) findViewById(R.id.personalspace_listview);
		refreshView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
	}

	/**
	 * 界面控件的点击事件监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 13:
			if (havemore) {

				if (arthList.size() % 10 > 0 || arthList.size() == 0) {
					ToastUtil.showMessage(this, R.string.no_more);
				} else {
					// mFooterView.setState(XListViewFooter.STATE_LOADING);
					// mFooterView.setClickable(false);
					pageNum++;
					myShareingArth();
				}
			} else {
				ToastUtil.showMessage(this, R.string.no_more);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 加载最新或者推荐的展示文章
	 */
	public void myShareingArth() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UnitID", JiaoBaoHao);
		params.addBodyParameter("SectionFlag", "99");// 1共享，2展示,99个人空间文章
		params.addBodyParameter("orderDirection", "0");// 0按最新排序，1按最热排序，默认为0
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		PersonalSpaceActivityController.getInstance().ArthListIndex(params);
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		EventBusUtil.register(this);
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		MobclickAgent.onPause(this);
		super.onPause();
	}

	/**
	 * 获取到的数据处理
	 * 
	 * @param list list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_notice_ArthListIndex:
			refreshView.onRefreshComplete();
			NoticeGetArthInfo getArthInfo = (NoticeGetArthInfo) list.get(1);
			if (null != getArthInfo && getArthInfo.getList().size() == 0) {
				havemore = false;
			} else if(getArthInfo!=null&&getArthInfo.getList().size()>0){
				arthList.addAll(getArthInfo.getList());
				listAdapter.setData(arthList);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case Constant.msgcenter_personalspace_GetNewPhoto:
			ArrayList<Photo> myGList = (ArrayList<Photo>) list.get(1);
			if (myGList == null || myGList.size() == 0) {
				myGList = new ArrayList<>();
				Photo photo = new Photo();
				photo.setSMPhotoPath("assets/drawable/pic_no.png");
				myGList.add(photo);
			}
			photoAdapter.setData(myGList);
			photoAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/***
	 * 系统返回按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub

	}

	// 上拉加载更多
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (havemore) {
			// 还有更多
			if (arthList.size() % 10 > 0 || arthList.size() == 0) {
				ToastUtil.showMessage(this, R.string.no_more);
				refreshView.onRefreshComplete();
			} else {
				pageNum++;
				myShareingArth();
			}
		} else {
			ToastUtil.showMessage(this, R.string.no_more);
			refreshView.onRefreshComplete();
		}

	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}

}
