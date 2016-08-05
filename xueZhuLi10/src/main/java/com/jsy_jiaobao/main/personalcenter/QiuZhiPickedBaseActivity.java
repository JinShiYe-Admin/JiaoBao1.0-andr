package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.PickedIndex;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.ShowPicked;

/**
 * 精选内容
 * 
 * @author admin
 * 
 */
public class QiuZhiPickedBaseActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ScrollView> {
	private PickedIndex question;
	private Context mContext;
	private LinearLayout layout;
	private LinearLayout layout_wv;
	private TextView tv_question;
	private WebView web_describe;
	private TextView tv_time;
	private TextView tv_yuanwen;
	private PullToRefreshScrollView refreshScrollView;
	private int mQId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			question = (PickedIndex) savedInstanceState
					.getSerializable("PickedIndex");
		} else {
			initPassData();
		}
		initViews();
	}

	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				question = (PickedIndex) bundle.getSerializable("PickedIndex");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("PickedIndex", question);
	}

	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_basepicked);
		mContext = this;
		QiuZhiSiftListActivityController.getInstance().setContext(this);
		layout = (LinearLayout) findViewById(R.id.qiuzhi_basepicked_layout);
		tv_question = (TextView) findViewById(R.id.qiuzhi_answer_tv_question);
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		layout_wv = (LinearLayout) findViewById(R.id.qiuzhi_basepick_ll_wv);
		tv_time = (TextView) findViewById(R.id.qiuzhi_basepick_tv_time);
		tv_yuanwen = (TextView) findViewById(R.id.qiuzhi_answer_tv_question1);
		tv_yuanwen.setOnClickListener(this);
		refreshScrollView.setOnRefreshListener(this);
		if (null != question) {
			QiuZhiSiftListActivityController.getInstance().ShowPicked(
					question.getTabID());
			;
			setActionBarTitle(question.getTitle());
			tv_question.setText(question.getTitle());
		}
		web_describe = new WebView(mContext);
		// 设置WebView的各项属性
		WebSetUtils.getWebSetting(this, web_describe);
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

	QuestionDetails questiondetails;

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_ShowPicked:
			ShowPicked picked = (ShowPicked) list.get(1);
			if (picked == null) {
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ToastUtil.showMessage(mContext, "该问题已被删除");
				layout.setVisibility(View.GONE);
			} else {
				// 数据不为空 加载url
				web_describe.loadUrl(picked.getPContent());
				layout_wv.addView(web_describe);
				mQId = picked.getQId();
			}
			break;
		case Constant.msgcenter_qiuzhi_QuestionDetail:
			questiondetails = (QuestionDetails) list.get(1);
			if (null != questiondetails) {
				setActionBarTitle(questiondetails.getTitle());
				String time = questiondetails.getRecDate();
				if (!TextUtils.isEmpty(time)) {
					String[] times = time.split("T");
					if (times.length == 2) {
						tv_time.setText(getResources().getString(
								R.string.edit_at)
								+ times[0]);
					}
				}
				// 跳转到下个Activity
				onClickJump();
			} else {
				tv_time.setText("");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 保存数据 并传递给下个Activity
	 */
	private void onClickJump() {
		// TODO Auto-generated method stub
		QuestionIndexItem data = new QuestionIndexItem();
		data.setAnswersCount(questiondetails.getAnswersCount());
		data.setAttCount(questiondetails.getAttCount());
		data.setCategoryId(questiondetails.getCategoryId());
		data.setTabID(questiondetails.getTabID());
		data.setTitle(questiondetails.getTitle());
		data.setViewCount(questiondetails.getViewCount());
		data.setCategorySuject(questiondetails.getCategory().trim());
		Intent intent = new Intent(mContext,
				QiuZhiQuestionAnswerListActivity.class);
		intent.putExtra("QuestionIndexItem", data);
		mContext.startActivity(intent);
	}

	/**
	 * 重写接口方法
	 * 
	 * @功能 原文的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qiuzhi_answer_tv_question1:
			// 原文点击事件
			QiuZhiSiftListActivityController.getInstance().QuestionDetail(mQId);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重写方法
	 * 
	 * @功能 为了跟其他界面功能一致，只具有观赏性
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		refreshView.onRefreshComplete();
	}

	/**
	 * 重写方法
	 * 
	 * @功能 为了跟其他界面功能一致，只具有观赏性
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		refreshView.onRefreshComplete();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}
}
