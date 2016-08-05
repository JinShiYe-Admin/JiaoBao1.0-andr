package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.Comment;
import com.jsy_jiaobao.po.personal.CommentsList;
import com.jsy_jiaobao.po.personal.RefComment;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 文章详情页面
 * 
 * @author admin
 * 
 */
public class ArticleDetailsActivity extends BaseActivity implements
		PublicMethod, OnClickListener, OnRefreshListener2<ScrollView> {
	private Button btn_send;
	private IEditText edt_keywords;
	private TextView tv_title;// 标题
	private TextView tv_author;// 作者
	private TextView tv_time;// 时间
	private LinearLayout ly_addWeb;
	private WebView web_content;
	private TextView tv_clickcount;
	private TextView tv_likecount;
	private LinearLayout layout_like;
	private TextView tv_viewcount;
	private TextView tv_likeanimation;
	private ImageView img_like;// 赞
	private ListView listView;
	private ArtCommentListAdapter commentAdapter;
	private Context mContext;
	private ArthInfo arthInfo = null;
	private ArthInfo addArthInfo = null;
	private String TabIDStr;
	private String SectionID;
	private int pageNum = 1;
	private ArrayList<Comment> commentsList = new ArrayList<Comment>();// 评论列表
	private ArrayList<RefComment> refcomments = new ArrayList<RefComment>();// 评论的回复列表
	// private XListViewFooter mFooterView;
	private boolean havemore = true;
	private boolean clickLike = false;// 点击了赞按钮
	private ArthInfo getArthInfo;// 文章详情
	private PullToRefreshScrollView refreshScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			getArthInfo = (ArthInfo) savedInstanceState
					.getSerializable("arthInfo");
			SectionID = getArthInfo.getSectionID();
			TabIDStr = getArthInfo.getTabIDStr();
		} else {
			// 获取Intent携带的数据
			initPassData();
		}
		// 初始化界面
		initViews();
		// 加载数据
		initDeatilsData();
		// 设置监听
		initListener();

	}

	/**
	 * @功能 获取Intent携带的数据
	 */
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				getArthInfo = (ArthInfo) bundle.getSerializable("arthInfo");
				SectionID = getArthInfo.getSectionID();
				TabIDStr = getArthInfo.getTabIDStr();
			}
		}
	}

	/**
	 * @功能 保存可能丢失的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("arthInfo", getArthInfo);
	}

	/**
	 * 初始化界面
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initViews() {
		setContentLayout(R.layout.ui_articledetails);
		findViews();
		mContext = this;
		setActionBarTitle("文章详情");
		img_like.setEnabled(false);
		tv_likecount.setEnabled(false);
		layout_like.setEnabled(false);
		ArticleDetailsActivityController.getInstance().setContext(this);
		WebSetUtils.getWebSetting(this, web_content);
		commentAdapter = new ArtCommentListAdapter(mContext);
		listView.setAdapter(commentAdapter);
		btn_send.setEnabled(false);
	}

	/**
	 * 更多初始化界面
	 */
	private void findViews() {
		// TODO Auto-generated method stub
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		btn_send = (Button) findViewById(R.id.article_btn_send);
		edt_keywords = (IEditText) findViewById(R.id.article_edt_mywords);
		tv_title = (TextView) findViewById(R.id.article_tv_title);
		tv_author = (TextView) findViewById(R.id.article_tv_author);
		tv_time = (TextView) findViewById(R.id.article_tv_time);
		ly_addWeb = (LinearLayout) findViewById(R.id.ly_add_web);
		web_content = new WebView(this);
		tv_clickcount = (TextView) findViewById(R.id.article_tv_clickcount);
		tv_likecount = (TextView) findViewById(R.id.article_tv_likecount);
		layout_like = (LinearLayout) findViewById(R.id.article_like);
		tv_viewcount = (TextView) findViewById(R.id.article_tv_viewcount);
		tv_likeanimation = (TextView) findViewById(R.id.articlle_tv_likeanimation);
		img_like = (ImageView) findViewById(R.id.articlle_img_like);
		listView = (ListView) findViewById(R.id.article_listview);
		tv_title.requestFocus();
		btn_send.setOnClickListener(this);
		layout_like.setOnClickListener(this);
		img_like.setOnClickListener(this);
		tv_likecount.setOnClickListener(this);
		refreshScrollView.setOnRefreshListener(this);
	}

	/**
	 * 请求数据
	 */
	@Override
	public void initDeatilsData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("sid", SectionID);
		params.addBodyParameter("aid", TabIDStr);
		ArticleDetailsActivityController.getInstance().ShowArthDetail(params);
		ArticleDetailsActivityController.getInstance().GetArthInfo(params);
	}

	/**
	 * 界面点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.article_btn_send:
			// 评论按钮
			MobclickAgent.onEvent(
					mContext,
					mContext.getResources().getString(
							R.string.ArticleDetailsActivity_commit));
			String str_reply = edt_keywords.getTextString();
			if (str_reply.equals("")) {
				ToastUtil.showMessage(mContext, "不能为空!");
			} else {
				try {
					String[] replys = str_reply.split(":");
					Comment comment = (Comment) edt_keywords.getTag();
					if (replys[0].equals("回复" + comment.getNumber())) {
						if (replys.length == 1) {
							ToastUtil.showMessage(mContext, "请输入回复内容!");
						} else {
							btn_send.setEnabled(false);
							ArticleDetailsActivityController.getInstance()
									.addComment(TabIDStr, replys[1],
											comment.getTabIDStr());
						}
					} else {
						btn_send.setEnabled(false);
						ArticleDetailsActivityController.getInstance()
								.addComment(TabIDStr, str_reply, "");
					}
				} catch (Exception e) {
					btn_send.setEnabled(false);
					ArticleDetailsActivityController.getInstance().addComment(
							TabIDStr, str_reply, "");
				}
			}
			break;
		case R.id.article_like:
			// 赞
		case R.id.articlle_img_like:
			// 图片赞
		case R.id.article_tv_likecount:
			// 赞数量
			MobclickAgent.onEvent(
					mContext,
					mContext.getResources().getString(
							R.string.ArticleDetailsActivity_like));
			if (!clickLike) {
				clickLike = true;
				ArticleDetailsActivityController.getInstance().LikeIt(TabIDStr,
						String.valueOf(addArthInfo.getLikeflag()));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void initListener() {

	}

	// 更新数据
	private void refreshData() {
		// TODO Auto-generated method stub
		commentsList.clear();
		refcomments.clear();
		pageNum = 1;
		ArticleDetailsActivityController.getInstance().CommentsList(TabIDStr,
				String.valueOf(pageNum));
	}

	// 加载更多
	private void loadMore() {
		// TODO Auto-generated method stub
		if (havemore) {
			if (commentsList.size() % 10 > 0 || commentsList.size() == 0) {
				ToastUtil.showMessage(mContext, "没有更多了");
				refreshScrollView.onRefreshComplete();
			} else {
				pageNum++;
				ArticleDetailsActivityController.getInstance().CommentsList(
						TabIDStr, String.valueOf(pageNum));
			}
		} else {
			refreshScrollView.onRefreshComplete();
			ToastUtil.showMessage(mContext, "没有更多了");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		web_content.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		web_content.onPause();
		MobclickAgent.onPause(this);
		super.onPause();
	}

	/**
	 * EventBus功能模块
	 * 
	 * @功能 获取数据并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_notice_ShowArthDetail:// 本单位文章详情
			btn_send.setEnabled(true);
			arthInfo = (ArthInfo) list.get(1);
			mHandler.sendEmptyMessage(0);
			pageNum = 1;
			break;
		case Constant.msgcenter_notice_GetArthInfo:// 本单位文章详情
			btn_send.setEnabled(true);
			addArthInfo = (ArthInfo) list.get(1);
			mHandler.sendEmptyMessage(1);
			commentsList.clear();
			refcomments.clear();
			pageNum = 1;
			if (addArthInfo.getFeeBackCount() > 0) {
				ArticleDetailsActivityController.getInstance().CommentsList(
						TabIDStr, String.valueOf(pageNum));
			}
			break;
		case Constant.msgcenter_article_LikeIt:// 点赞
			clickLike = false;
			String result = (String) list.get(1);
			if ("-1".equals(result)) {
				getArthInfo.setLikeflag(1);
				getArthInfo.setLikeCount(getArthInfo.getLikeCount() + 1);

				img_like.setEnabled(false);
				tv_likecount.setEnabled(false);
				layout_like.setEnabled(false);
				if (null != ShowFragment2.clickArthInfo) {
					ShowFragment2.clickArthInfo
							.setLikeCount(ShowFragment2.clickArthInfo
									.getLikeCount() + 1);
					ShowFragment2.clickArthInfo.setLikeflag(1);
					ArrayList<Object> post = new ArrayList<Object>();
					post.add(Constant.msgcenter_articlelist_addComment);
					EventBusUtil.post(post);
				}

				img_like.setBackgroundResource(R.drawable.icon_art3_2);
				Animation animation = AnimationUtils.loadAnimation(mContext,
						R.anim.zan);
				tv_likeanimation.setVisibility(View.VISIBLE);
				tv_likeanimation.startAnimation(animation);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						tv_likeanimation.setVisibility(View.GONE);
						tv_likecount.setText(String.valueOf(addArthInfo
								.getLikeCount() + 1));
					}
				}, 1000);
			}
			break;
		case Constant.msgcenter_article_addComment:// 评论
			btn_send.setEnabled(true);
			String addresult = (String) list.get(1);
			if ("0".equals(addresult)) {

				ToastUtil.showMessage(mContext, "回复成功");
				edt_keywords.setText("");
				commentsList.clear();
				refcomments.clear();
				havemore = true;
				pageNum = 1;
				ArticleDetailsActivityController.getInstance().CommentsList(
						TabIDStr, String.valueOf(pageNum));
			} else {
				ToastUtil.showMessage(mContext, "回复失败");
			}
			break;
		case Constant.msgcenter_article_click_reply:// 回复
			Comment clickComment = (Comment) list.get(1);
			String repcontent = "回复" + clickComment.getNumber() + ":";
			edt_keywords.setText(repcontent);
			edt_keywords.setTag(clickComment);
			edt_keywords.requestFocus();
			edt_keywords.setSelection(repcontent.length());
			BaseUtils.openpan(edt_keywords);
			break;
		case Constant.msgcenter_article_AddScore_like:
			Comment comment_like = (Comment) list.get(1);
			ArticleDetailsActivityController.getInstance().AddScoreCommon(
					comment_like, "1");
			break;
		case Constant.msgcenter_article_AddScore_cai:
			Comment comment_cai = (Comment) list.get(1);
			ArticleDetailsActivityController.getInstance().AddScoreCommon(
					comment_cai, "0");
			break;
		case Constant.msgcenter_article_AddScore_like_ref:
			RefComment refcomment_like = (RefComment) list.get(1);
			ArticleDetailsActivityController.getInstance().AddScoreRefCom(
					refcomment_like, "1");
			break;
		case Constant.msgcenter_article_AddScore_cai_ref:
			RefComment refcomment_cai = (RefComment) list.get(1);
			ArticleDetailsActivityController.getInstance().AddScoreRefCom(
					refcomment_cai, "0");
			break;
		case Constant.msgcenter_article_AddScore_callback:
			String tp = (String) list.get(2);
			try {
				Comment comment = (Comment) list.get(1);
				int like = comment.getLikeCount();
				int cai = comment.getCaiCount();
				ArrayList<RefComment> refcommonlist = commentAdapter
						.getRefcomments();
				if ("1".equals(tp)) {// 顶
					comment.setLikeCount(like + 1);
					for (RefComment item : refcommonlist) {
						if (item.getTabID() == comment.getTabID()) {
							item.setLikeCount(like + 1);
						}
					}
				} else if ("0".equals(tp)) {// 踩
					comment.setCaiCount(cai + 1);
					for (RefComment item : refcommonlist) {
						if (item.getTabID() == comment.getTabID()) {
							item.setCaiCount(cai + 1);
						}
					}
				}
			} catch (Exception e) {
				RefComment comment = (RefComment) list.get(1);
				int like = comment.getLikeCount();
				int cai = comment.getCaiCount();
				ArrayList<Comment> commonlist = commentAdapter
						.getCommentsList();
				ArrayList<RefComment> refcommonlist = commentAdapter
						.getRefcomments();
				if ("1".equals(tp)) {// 顶
					for (Comment self : commonlist) {
						if (self.getTabID() == comment.getTabID()) {
							self.setLikeCount(like + 1);
						}
					}
					for (RefComment item : refcommonlist) {
						if (item.getTabID() == comment.getTabID()) {
							item.setLikeCount(like + 1);
						}
					}
				} else if ("0".equals(tp)) {// 踩
					for (Comment self : commonlist) {
						if (self.getTabID() == comment.getTabID()) {
							self.setCaiCount(cai + 1);
						}
					}
					for (RefComment item : refcommonlist) {
						if (item.getTabID() == comment.getTabID()) {
							item.setCaiCount(cai + 1);
						}
					}
				}
			}
			commentAdapter.notifyDataSetChanged();
			break;
		case Constant.msgcenter_article_CommentsList:// 文章评论列表
			refreshScrollView.onRefreshComplete();
			CommentsList comList = (CommentsList) list.get(1);
			if (null == comList) {
				havemore = false;
			} else if (comList.getCommentsList().size() == 0) {
				havemore = false;
			} else {
				havemore = true;
				ArrayList<Comment> back = comList.getCommentsList();
				try {
					for (Comment item : back) {
						String[] number = item.getNumber().split("楼");
						int num = Integer.parseInt(number[0]);
						if (num <= 1) {
							havemore = false;
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				commentsList.addAll(back);
				refcomments.addAll(comList.getRefcomments());
				commentAdapter.setRefCommentsData(refcomments);
				commentAdapter.setCommentsData(commentsList);
				commentAdapter.notifyDataSetChanged();
				DialogUtil.getInstance().cannleDialog();
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					DialogUtil.getInstance().cannleDialog();
				}
			}, 500);
			if (pageNum == 1) {
				ShowFragment2.commitAdapter.setData(commentsList);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Handler处理事件
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0:
					tv_title.setText(arthInfo.getTitle());
					tv_author.setText(arthInfo.getUserName());
					tv_time.setText(arthInfo.getRecDate());
					web_content.loadDataWithBaseURL(
							null,
							StringUtils.xml2webview(arthInfo.getContext()
									.replaceAll("width", "")
									.replaceAll("height", "")), "text/html",
							"utf-8", null);
					ly_addWeb.removeAllViews();
					ly_addWeb.addView(web_content);
					break;
				case 1:
					if (addArthInfo.getLikeflag() == 0) {
						img_like.setEnabled(true);
						tv_likecount.setEnabled(true);
						layout_like.setEnabled(true);
						img_like.setBackgroundResource(R.drawable.icon_art3);
					} else {
						img_like.setEnabled(false);
						tv_likecount.setEnabled(false);
						layout_like.setEnabled(false);
						img_like.setBackgroundResource(R.drawable.icon_art3_2);
					}
					tv_clickcount.setText(String.valueOf(addArthInfo
							.getClickCount()));
					tv_likecount.setText(String.valueOf(addArthInfo
							.getLikeCount()));
					tv_viewcount.setText(String.valueOf(addArthInfo
							.getViewCount()));
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * 系统返回按钮
	 * 
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseUtils.hidepan(edt_keywords);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 按钮选中事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			BaseUtils.hidepan(edt_keywords);
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 下拉更新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		// 刷新数据
		refreshData();
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		// 加载更多
		loadMore();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}
}
