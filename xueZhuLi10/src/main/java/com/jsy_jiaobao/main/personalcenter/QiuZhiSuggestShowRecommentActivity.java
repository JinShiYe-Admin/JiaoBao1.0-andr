package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentDetails;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;

/**
 * 推荐详情页面
 * 
 * @author admin
 * 
 */
public class QiuZhiSuggestShowRecommentActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView>, OnClickListener, Watcher,
		OnItemClickListener {
	public static HashMap<String, Drawable> cache = new HashMap<String, Drawable>();
	private PullToRefreshScrollView mPullRefreshScrollView;
	private RecommentIndexItem recomment;// 回答
	private Context mContext;
	private TextView tv_question;// 问题标题
	private TextView tv_topic;// 所属话题
	private TextView tv_answernum;// 回答数量
	private TextView tv_attnum;// 关注数量
	private TextView tv_clicknum;// 点击数量
	private CusListView listView;// listView
	private LinearLayout layout_deal;
	private QiuZhiSuggestAnswerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.getSerializable("QuestionIndexItem") instanceof RecommentIndexItem) {
				recomment = (RecommentIndexItem) savedInstanceState
						.getSerializable("QuestionIndexItem");
			}
		} else {
			initPassData();
		}
		initViews();
	}

	/**
	 * @method 自定义方法
	 * @功能 获取Intent 携带数据
	 */
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				if (bundle.getSerializable("QuestionIndexItem") instanceof RecommentIndexItem) {
					recomment = (RecommentIndexItem) bundle
							.getSerializable("QuestionIndexItem");
				}
			}
		}
	}

	/**
	 * @功能 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("QuestionIndexItem", recomment);
	}

	/**
	 * @功能 初始化界面 注册监听 等
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_question_answerlist);
		mContext = this;
		getWindow().setBackgroundDrawable(null);
		QiuZhiQuestionAnswerListActivityController.getInstance().setContext(
				this);
		WatchedEntityIndexItem.getInstance().addWatcher(this);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		tv_question = (TextView) findViewById(R.id.qiuzhi_question_tv_question);
		tv_topic = (TextView) findViewById(R.id.qiuzhi_question_tv_topic);
		tv_answernum = (TextView) findViewById(R.id.qiuzhi_question_tv_answernum);
		tv_attnum = (TextView) findViewById(R.id.qiuzhi_question_tv_attnum);
		tv_clicknum = (TextView) findViewById(R.id.qiuzhi_question_tv_clicknum);
		listView = (CusListView) findViewById(R.id.qiuzhi_question_listview);
		TextView tv_yuanwen = (TextView) findViewById(R.id.qiuzhi_question_tv_details);
		LinearLayout layout_tab = (LinearLayout) findViewById(R.id.qiuzhi_question_layout_chose);
		layout_deal = (LinearLayout) findViewById(R.id.qiuzhi_question_layout_deal);
		layout_tab.setVisibility(View.GONE);
		layout_deal.removeAllViews();
		tv_yuanwen.setText(getResources().getString(R.string.sourse_text));
		tv_yuanwen.setOnClickListener(this);
		tv_question.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		mPullRefreshScrollView.setMode(Mode.BOTH);
		mPullRefreshScrollView.setOnRefreshListener(this);
		adapter = new QiuZhiSuggestAnswerListAdapter(mContext, listView);
		listView.setAdapter(adapter);
		if (null != recomment) {
			adapter.setQuestion(recomment);
			setActionBarTitle(recomment.getQuestion().getTitle());
			tv_question.setText(recomment.getQuestion().getTitle());
			tv_topic.setText(recomment.getQuestion().getCategorySuject());
			tv_attnum.setText(String.valueOf(recomment.getQuestion()
					.getAttCount()));
			tv_answernum.setText(String.valueOf(recomment.getQuestion()
					.getAnswersCount()));
			tv_clicknum.setText(String.valueOf(recomment.getQuestion()
					.getViewCount()));
			QiuZhiQuestionAnswerListActivityController.getInstance()
					.ShowRecomment(recomment.getTabid());
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

	@Override
	protected void onDestroy() {
		layout_deal.removeAllViews();
		adapter.Destroy();
		adapter = null;
		cache.clear();
		super.onDestroy();
		WatchedEntityIndexItem.getInstance().removeWatcher(this);
	}

	/**
	 * EventBus 功能模块
	 * 
	 * @功能 获取网络请求返回的数据 并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_ShowRecomment:
			mPullRefreshScrollView.onRefreshComplete();
			RecommentDetails recomment1 = (RecommentDetails) list.get(1);
			if (null != recomment1) {
				// 回答不为空
				if (null != recomment1.getQuestion()) {
					// 回答的问题部位空
					tv_question.setText(recomment1.getQuestion().getTitle());
					tv_topic.setText(recomment1.getQuestion()
							.getCategorySuject());
					tv_attnum.setText(String.valueOf(recomment1.getQuestion()
							.getAttCount()));
					tv_answernum.setText(String.valueOf(recomment1
							.getQuestion().getAnswersCount()));
					tv_clicknum.setText(String.valueOf(recomment1.getQuestion()
							.getViewCount()));
					layout_deal.removeAllViews();
					layout_deal.setPadding(10, 0, 10, 0);
					final WebView content = new WebView(this);
					// 设置webView属性
					WebSetUtils.getWebSetting(this, content);
					String url = (String) recomment1.getQuestion()
							.getKnContent();
					// 判断回复内容是否为空，不为空则加载url
					if (url != null && url.length() > 0) {
						content.loadDataWithBaseURL(
								null,
								StringUtils.xml2webview(url.replaceAll("width",
										"").replaceAll("height", "")),
								"text/html", "utf-8", null);
						layout_deal.addView(content);
					}
				} else {
					ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
							"false");
					ToastUtil.showMessage(mContext,
							R.string.this_question_deleted);
					mPullRefreshScrollView.setVisibility(8);
				}
				adapter.setData(recomment1.getAnswers());
				adapter.notifyDataSetChanged();
			} else {
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ToastUtil.showMessage(mContext, R.string.this_question_deleted);
				mPullRefreshScrollView.setVisibility(8);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 覆写接口
	 * 
	 * @功能 各种点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 详情 问题标题的点击事件
		case R.id.qiuzhi_question_tv_details:
		case R.id.qiuzhi_question_tv_question:
			Intent intent = new Intent(mContext,
					QiuZhiQuestionAnswerListActivity.class);
			intent.putExtra("QuestionIndexItem", recomment.getQuestion());
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	/**
	 * 系统返回按键 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下拉刷新
	 * 
	 * @功能 重新请求数据
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (null != recomment) {
			QiuZhiQuestionAnswerListActivityController.getInstance()
					.ShowRecomment(recomment.getTabid());
		}
	}

	/**
	 * 上拉加载 ，一样重新加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (null != recomment) {
			QiuZhiQuestionAnswerListActivityController.getInstance()
					.ShowRecomment(recomment.getTabid());
		} else {
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	@Override
	public void update(QuestionIndexItem qEntity) {
		if (qEntity != null) {
			if (recomment.getQuestion().getTabID() == qEntity.getTabID()) {
				tv_attnum.setText(String.valueOf(qEntity.getAttCount()));
				tv_answernum.setText(String.valueOf(qEntity.getAnswersCount()));
				tv_clicknum.setText(String.valueOf(qEntity.getViewCount()));
			}
		}
	}

	@Override
	public void update(AnswerItem answer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}

}
