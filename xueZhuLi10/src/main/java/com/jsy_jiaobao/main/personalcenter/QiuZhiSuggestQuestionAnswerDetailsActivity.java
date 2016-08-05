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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;

/**
 * 推荐答案内容 ps 点击答案内容进来的
 */
public class QiuZhiSuggestQuestionAnswerDetailsActivity extends BaseActivity
		implements OnClickListener, Watcher {
	private PullToRefreshScrollView mPullRefreshScrollView;
	private RecommentIndexItem recomment;// 评论
	private int answerID;
	private Context mContext;
	private TextView tv_question;// 显示问题标题的View
	private TextView tv_topic;// 显示问题所属话题的View
	private TextView tv_answernum;// 显示回答数量的view
	private TextView tv_attnum;// 显示关注数量的View
	private TextView tv_clicknum;// 显示点击数量的View
	private CusListView listView;// 列表
	private ImageView iv_photo;// 显示头像的View
	private ImageView iv_like;// 显示赞的View
	private TextView tv_likenum;// 显示关注数量
	private TextView tv_name;
	private WebView web_detailsgist;// 答案内容详情
	private LinearLayout layout_web;
	private TextView tv_detailstime;
	private TextView tv_answerHead;
	private TextView img_answerHead;
	private TextView img_answerContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			recomment = (RecommentIndexItem) savedInstanceState
					.getSerializable("QuestionIndexItem");
			answerID = savedInstanceState.getInt("answerID");
		} else {
			initPassData();
		}
		initViews();
	}

	/**
	 * @method 自定义方法
	 * @功能 获取Intent携带的数据
	 */

	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				recomment = (RecommentIndexItem) bundle
						.getSerializable("QuestionIndexItem");
				answerID = recomment.getAnswer().getTabID();
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
		outState.putInt("answerID", answerID);
	}

	/**
	 * @功能 初始化界面 注册监听事件等
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_question_answerdetails);
		mContext = this;
		QiuZhiQuestionAnswerDetailsActivityController.getInstance().setContext(
				this);
		WatchedEntityIndexItem.getInstance().addWatcher(this);
		QiuZhiQuestionAnswerDetailsActivityController.getInstance()
				.AnswerDetail(answerID);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setMode(Mode.DISABLED);
		tv_answerHead = (TextView) findViewById(R.id.tv_answer_head);
		img_answerHead = (TextView) findViewById(R.id.img_answerHead);
		img_answerContent = (TextView) findViewById(R.id.img_answerContent);
		tv_question = (TextView) findViewById(R.id.qiuzhi_answer_tv_question);
		tv_topic = (TextView) findViewById(R.id.qiuzhi_answer_tv_topic);
		tv_answernum = (TextView) findViewById(R.id.qiuzhi_answer_tv_answernum);
		tv_attnum = (TextView) findViewById(R.id.qiuzhi_answer_tv_attnum);
		tv_clicknum = (TextView) findViewById(R.id.qiuzhi_answer_tv_clicknum);
		listView = (CusListView) findViewById(R.id.qiuzhi_answer_listview);
		iv_photo = (ImageView) findViewById(R.id.qiuzhi_answer_iv_photo);
		iv_like = (ImageView) findViewById(R.id.qiuzhi_answer_iv_like);
		tv_likenum = (TextView) findViewById(R.id.qiuzhi_answer_tv_likenum);
		tv_name = (TextView) findViewById(R.id.qiuzhi_answer_tv_name);
		layout_web = (LinearLayout) findViewById(R.id.qiuzhi_answer_layout_web);
		tv_detailstime = (TextView) findViewById(R.id.qiuzhi_answer_tv_detailstime);
		TextView tv_yuanwen = (TextView) findViewById(R.id.qiuzhi_answer_tv_question1);
		LinearLayout layout_deal = (LinearLayout) findViewById(R.id.qiuzhi_answer_layout_deal);
		layout_deal.setVisibility(View.GONE);
		tv_yuanwen.setText(mContext.getResources().getString(
				R.string.sourse_text));
		tv_yuanwen.setOnClickListener(this);
		iv_like.setOnClickListener(this);
		tv_likenum.setOnClickListener(this);
		if (null != recomment) {
			if (null != recomment.getQuestion()) {
				setActionBarTitle(recomment.getQuestion().getTitle());
				tv_question.setText(recomment.getQuestion().getTitle());
				tv_topic.setText(recomment.getQuestion().getCategorySuject());
				tv_answernum.setText(String.valueOf(recomment.getQuestion()
						.getAnswersCount()));
				tv_attnum.setText(String.valueOf(recomment.getQuestion()
						.getAttCount()));
				tv_clicknum.setText(String.valueOf(recomment.getQuestion()
						.getViewCount()));
			}
		}

		listView.setVisibility(View.GONE);
		web_detailsgist = new WebView(mContext);
		WebSetUtils.getWebSetting(this, web_detailsgist);

	}

	String str_clickComment;

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
		WatchedEntityIndexItem.getInstance().removeWatcher(this);
	}

	AnswerDetails answerdetails;

	/**
	 * EventBus 功能模块
	 * 
	 * @功能 获取网络请求返回数据并作处理
	 * @param list
	 */

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_SetYes:
			// 评价 支持或反对;
			String r1 = (String) list.get(1);
			if (TextUtils.isEmpty(r1)) {
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.public_syserr));
			} else if ("-1".equals(r1)) {
				ToastUtil.showMessage(mContext, "观点已发表");
			} else if ("1".equals(r1)) {
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.public_sysright));
				answerdetails.setLikeCount(answerdetails.getLikeCount() + 1);
				tv_likenum
						.setText(String.valueOf(answerdetails.getLikeCount()));
				if (recomment != null) {
					AnswerItem answer = recomment.getAnswer();
					if (answer != null) {
						answer.setLikeCount(answer.getLikeCount() + 1);
						WatchedEntityIndexItem.getInstance().notifyAnswerList(
								answer);
					}
				}
			} else if ("-2".equals(r1)) {
				ToastUtil.showMessage(mContext, "记录已被删除或被屏蔽！");
			}
			break;
		case Constant.msgcenter_qiuzhi_AnswerDetail:
			// 答案详情
			answerdetails = (AnswerDetails) list.get(1);
			DialogUtil.getInstance().cannleDialog();
			if (null != answerdetails) {
				// 回答者姓名
				if (TextUtils.isEmpty(recomment.getAnswer().getIdFlag())) {
					// 空为匿名
					tv_name.setText(mContext.getResources().getString(
							R.string.anonymity));
					JSYApplication.getInstance().bitmap.display(iv_photo, "");
				} else {
					// 实名？
					String url = ACache.get(mContext.getApplicationContext())
							.getAsString("MainUrl")
							+ ConstantUrl.photoURL
							+ "?AccID=" + recomment.getAnswer().getJiaoBaoHao();
					JSYApplication.getInstance().bitmap.display(iv_photo, url);
					tv_name.setText(answerdetails.getIdFlag());
				}
				// 显示赞数量
				tv_likenum
						.setText(String.valueOf(answerdetails.getLikeCount()));
				// tv_detailstitle.setText("问题:"+answerdetails.getATitle());
				tv_detailstime.setText(answerdetails.getRecDate().replace("T",
						" "));
				String con = answerdetails.getAContent();
				String tit = answerdetails.getATitle();
				tv_answerHead.setText(tit);

				// 0无内容1有内容2有证据
				if (answerdetails.getFlag() == 1) {
					img_answerHead
							.setBackgroundResource(R.drawable.icon_qiuzhi_answer_);
					img_answerContent
							.setBackgroundResource(R.drawable.icon_qiuzhi_content_);
				} else if (answerdetails.getFlag() == 2) {
					if (con == null || con.length() == 0) {
						img_answerContent
								.setBackgroundResource(R.drawable.icon_qiuzhi_nocontent_);
					} else {
						img_answerContent
								.setBackgroundResource(R.drawable.icon_qiuzhi_gist_);
					}
				} else {
					img_answerContent
							.setBackgroundResource(R.drawable.icon_qiuzhi_nocontent_);
				}
				if (con != null && con.length() > 0) {
					web_detailsgist.loadUrl(con);
				}

				if (layout_web.getChildCount() == 0) {
					layout_web.addView(web_detailsgist);
				}
			} else {
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ToastUtil.showMessage(mContext, R.string.this_answer_deleted);
				mPullRefreshScrollView.setVisibility(8);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 重写接口方法
	 * 
	 * @功能 各个View的点击事件
	 * 
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qiuzhi_answer_tv_question1:
			// 回答问题
			Intent intent = new Intent(mContext,
					QiuZhiQuestionAnswerListActivity.class);
			intent.putExtra("QuestionIndexItem", recomment.getQuestion());
			startActivity(intent);
			break;
		case R.id.qiuzhi_answer_iv_like:
			// 赞
		case R.id.qiuzhi_answer_tv_likenum:
			// 赞数量
			UserInfo userInfo111 = (UserInfo) ACache.get(
					getApplicationContext()).getAsObject("userInfo");
			if (userInfo111.isIsKnlFeezeUser()) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_user));
				return;
			}
			if (userInfo111.getDUnitId() == 0) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nounit));
				return;
			}
			// 点赞
			QiuZhiQuestionAnswerDetailsActivityController.getInstance().SetYes(
					answerID);
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
	public void update(QuestionIndexItem qEntity) {
		if (qEntity != null) {
			tv_answernum.setText(String.valueOf(qEntity.getAnswersCount()));
			tv_attnum.setText(String.valueOf(qEntity.getAttCount()));
			tv_clicknum.setText(String.valueOf(qEntity.getViewCount()));
		}
	}

	@Override
	public void update(AnswerItem answer) {

	}
}
