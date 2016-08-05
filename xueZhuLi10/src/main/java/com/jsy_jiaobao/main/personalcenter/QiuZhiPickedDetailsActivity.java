package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.SlideShowView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerComment;
import com.jsy_jiaobao.po.qiuzhi.AnswerRefcomment;
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.jsy_jiaobao.po.qiuzhi.PickedItem;

/**
 * 往期精选内容
 * 
 * @author admin
 * 
 */

public class QiuZhiPickedDetailsActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView>, OnClickListener {
	private PickedItem question;// 精选问题
	private Context mContext;
	private SlideShowView vp_sifttop;
	private ScrollView layout;
	private CusListView listView;
	private TextView tv_title;
	private TextView tv_time;
	private QiuZhiIndexListAdapter adapter;// adapter
	private PullToRefreshScrollView mScrollView;
	int index;
	boolean isTouch;
	private ArrayList<String> uriList;
	// 评论列表
	ArrayList<AnswerComment> commentsList = new ArrayList<AnswerComment>();
	// 评论回复列表
	ArrayList<AnswerRefcomment> refcommentsList = new ArrayList<AnswerRefcomment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			question = (PickedItem) savedInstanceState
					.getSerializable("PickedItem");
		} else {
			initPassData();
		}
		initViews();
	}

	/**
	 * 自定义方法
	 * 
	 * @功能 获取传递过来信息
	 */
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				question = (PickedItem) bundle.getSerializable("PickedItem");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("PickedItem", question);
	}

	/**
	 * 自定义方法
	 * 
	 * @功能 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_pickeddetails);
		mContext = this;
		QiuZhiPickedDetailsActivityController.getInstance().setContext(this);
		if (null != question) {
			setActionBarTitle(question.getPTitle());
			QiuZhiPickedDetailsActivityController.getInstance().GetPickedById(
					question.getTabID());
		}
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		vp_sifttop = (SlideShowView) findViewById(R.id.qiuzhi_item_siftiontop_slideShowView);
		listView = (CusListView) findViewById(R.id.qiuzhi_pickeddetails_listview);
		tv_title = (TextView) findViewById(R.id.qiuzhi_pickeddetails_title);
		tv_time = (TextView) findViewById(R.id.qiuzhi_pickeddetails_time);
		mScrollView.setMode(Mode.BOTH);
		mScrollView.setOnRefreshListener(this);
		adapter = new QiuZhiIndexListAdapter(mContext, true);
		adapter.setCategoryID(-3);
		uriList = new ArrayList<String>();
		listView.setAdapter(adapter);
	}

	/**
	 * 生命周期方法
	 */
	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}

	/**
	 * 生命周期方法
	 */
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	/**
	 * 生命周期方法
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * eventBus 功能模块
	 * 
	 * @功能 获取到数据 并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		// 获取一个精选问题集或最新的一个精选问题集
		case Constant.msgcenter_qiuzhi_GetPickedById_pd:
			GetPicked getPicked = (GetPicked) list.get(1);
			if (getPicked != null) {

				String mainURL = ConstantUrl.jsyoa.replaceAll("jbclient", "");
				String imgs = getPicked.getImgContent();
				tv_title.setText(getPicked.getPTitle());
				String time = getPicked.getRecDate();
				if (TextUtils.isEmpty(time)) {
					tv_time.setText("");
				} else {
					String[] times = getPicked.getRecDate().split("T");
					tv_time.setText(times[0]);
				}
				adapter.setData(getPicked.getPickContent());
				adapter.notifyDataSetChanged();
				setActionBarTitle(getPicked.getPTitle());
				if (TextUtils.isEmpty(imgs)) {
					vp_sifttop.setVisibility(View.GONE);
				} else {
					vp_sifttop.setVisibility(View.VISIBLE);
					vp_sifttop.removeAllViews();
					uriList.clear();
					try {
						final JSONArray jsonarray = new JSONArray(imgs);
						for (int i = 0; i < jsonarray.length(); i++) {
							jsonarray.put(i,
									mainURL + getPicked.getBaseImgUrl()
											+ jsonarray.getString(i));
							uriList.add(jsonarray.getString(i));
						}
						vp_sifttop.setImageUrls(uriList);
						vp_sifttop.setCurrentItem(0);
						vp_sifttop.startPlay();
						//
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				ToastUtil.showMessage(mContext,
						R.string.currentPeriod_selected_isDel_orIsHide);
				layout.setVisibility(8);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 下拉刷新
	 * 
	 * @功能 只是界面动画 没啥功能
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		mScrollView.onRefreshComplete();

	}

	/**
	 * 上拉加载
	 * 
	 * @功能 只是界面动画 么啥功能
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		mScrollView.onRefreshComplete();
	}

	/**
	 * 监听事件
	 * 
	 * @功能 界面控件点击事件的监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qiuzhi_answer_tv_question1:
		case R.id.qiuzhi_answer_tv_question:
			Intent intent = new Intent(mContext,
					QiuZhiQuestionDetailsActivity.class);
			intent.putExtra("QuestionIndexItem", question);
			startActivity(intent);
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
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}

}
