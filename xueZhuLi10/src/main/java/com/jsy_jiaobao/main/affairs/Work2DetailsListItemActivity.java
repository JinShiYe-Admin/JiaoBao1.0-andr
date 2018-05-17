package com.jsy_jiaobao.main.affairs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Attlist;
import com.jsy_jiaobao.po.personal.FeeBack;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 点击列表头像的右侧区域的事务详情Activity
 */
public class Work2DetailsListItemActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView>, OnClickListener {

	private int pageNum = 1;
	/** 来自我1/其他人2 */
	private int type;
	/** 未读数量 */
	private int ReadFlag = 0;
	private boolean haveMore = true;
	/** 加密后的标识字段 **/
	private String TabIDStr;
	/** 发送者姓名 **/
	private String UserName;
	/** 第一条信息的日期 */
	private String MsgRecDate;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	private List<HttpHandler> httpDownList = new ArrayList<>();
	private ArrayList<Object> workList = new ArrayList<>();

	private Context mContext;

	private Button btn_reply;// 回复按钮
	private IEditText edt_keywords;// 回复编辑框
	private PullToRefreshScrollView mPullRefreshScrollView;
	private Work2DetailListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			type = savedInstanceState.getInt("type");
			TabIDStr = savedInstanceState.getString("TabIDStr");
			UserName = savedInstanceState.getString("UserName");
			MsgRecDate = savedInstanceState.getString("MsgRecDate");
			ReadFlag = savedInstanceState.getInt("ReadFlag");
		} else {
			initPassData();
		}
		initViews();
		ShowDetail();
	}

	private void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				type = bundle.getInt("type");
				TabIDStr = bundle.getString("TabIDStr");
				MsgRecDate = bundle.getString("MsgRecDate");
				UserName = bundle.getString("UserName");
				ReadFlag = bundle.getInt("ReadFlag");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("type", type);
		outState.putInt("ReadFlag", ReadFlag);
		outState.putString("TabIDStr", TabIDStr);
		outState.putString("MsgRecDate", MsgRecDate);
		outState.putString("UserName", UserName);
	}

	private void initViews() {
		setContentView(R.layout.activity_work2details);
		setActionBarTitle(UserName);
		LinearLayout reply_layout;// 回复区域布局
		reply_layout = (LinearLayout) findViewById(R.id.article_layout_reply);
		edt_keywords = (IEditText) findViewById(R.id.article_edt_mywords);
		btn_reply = (Button) findViewById(R.id.article_btn_send);
		CusListView listView;
		listView = (CusListView) findViewById(R.id.listview);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mContext = this;
		httpDownList.clear();
		Work2DetailsListActivityController.getInstance().setContext(this);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);
		mPullRefreshScrollView.setOnRefreshListener(this);
		reply_layout.setVisibility(View.VISIBLE);
		adapter = new Work2DetailListAdapter(this, onclickListener);
		adapter.setData(workList);
		adapter.setWorkType(30 + type);
		listView.setAdapter(adapter);
		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
		Work2DetailsListActivityController.getInstance().MarkRead(TabIDStr);// 标记为已读
		btn_reply.setOnClickListener(this);
	}

	Handler mHandler = new Handler();
	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MobclickAgent
					.onEvent(
							mContext,
							getResources()
									.getString(
											R.string.Work2DetailsListItemActivity_onclickListener));
			Attlist att = (Attlist) v.getTag();
			String filePath = JSYApplication.getInstance().FILE_PATH
					+ att.getOrgFilename();
			File file = new File(filePath);
			if (!file.exists()) {
				dialog_down(att);// 确认下载附件提示框
			} else {
				PictureUtils.openFile(mContext, filePath);
			}
		}
	};


	public void onClick(View v) {
		if (v.getId() == R.id.article_btn_send) {
			MobclickAgent
					.onEvent(
							mContext,
							getResources()
									.getString(
											R.string.Work2DetailsListItemActivity_article_btn_send));
			String str = edt_keywords.getTextString();
			if (!TextUtils.isEmpty(str)) {
				btn_reply.setEnabled(false);
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", TabIDStr);// TabIDStr
				params.addBodyParameter("feebacktalkcontent", str);// 回复内容
				params.addBodyParameter("MsgRecDate", MsgRecDate);// 交流信息的RecDate
				Work2DetailsListActivityController.getInstance().addfeeback(
						params);
			} else {
				ToastUtil.showMessage(mContext,
						R.string.please_input_reply_content);
			}
		}
	}

	/**
	 * 获取信息详情
	 */
	private void ShowDetail() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNum", String.valueOf(pageNum));//
		params.addBodyParameter("uid", TabIDStr);//
		params.addBodyParameter("ReadFlag", String.valueOf(ReadFlag));//
		Work2DetailsListActivityController.getInstance().ShowDetail(params);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (haveMore) {
			pageNum++;
			ShowDetail();
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}

	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		adapter.bitmapUtils.clearCache();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work2_addfeeback:// 成功回复，发给我的交流信息
			String result = (String) list.get(1);
			if ("succ".equals(result)) {
				ToastUtil.showMessage(mContext, R.string.reply_success);
				if (!btn_reply.isEnabled()) {
					GetWorkMsgDetails firstWork = (GetWorkMsgDetails) workList
							.get(workList.size() - 1);
					FeeBack succ = new FeeBack();
					succ.setJiaobaohao(Integer.parseInt(BaseActivity.sp
							.getString("JiaoBaoHao", "")));
					succ.setFeeBackMsg(edt_keywords.getTextString());
					succ.setUserName("我");
					succ.setRecDate(dateFormat.format(new Date(System
							.currentTimeMillis())));
					firstWork.getFeebackList().add(0, succ);
					adapter.notifyDataSetChanged();
					edt_keywords.setText("");
				}
			}
			btn_reply.setEnabled(true);
			break;
		case Constant.msgcenter_work2_FirstWorkDetails:// 获取我发出的信息详情
			mPullRefreshScrollView.onRefreshComplete();
			GetWorkMsgDetails getWorkDetails = (GetWorkMsgDetails) list.get(1);
			List<FeeBack> feeBackList = getWorkDetails.getFeebackList();
			haveMore=feeBackList.size()>=20;
			if (pageNum == 1) {
//				haveMore=getWorkDetails.getFeebackList().size()>=20;
//				if (getWorkDetails.getFeebackList().size() < 20) {
//					haveMore = false;
//				} else {
//					haveMore = true;
//				}
				workList.clear();
				workList.add(getWorkDetails);
			} else {

//				if (feeBackList.size() < 20) {
//					haveMore = false;
//				} else {
//					haveMore = true;
//				}
				GetWorkMsgDetails firstWork = (GetWorkMsgDetails) workList
						.get(workList.size() - 1);
				firstWork.getFeebackList().addAll(feeBackList);
			}
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * 确认下载附件提示框
	 *
	 * @param att
	 *            附件
	 */
	protected void dialog_down(final Attlist att) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.hint);
		builder.setIcon(android.R.drawable.ic_dialog_info).setMessage(
				getResources().getString(R.string.beSure_toLoad_enclosure)
						+ att.getOrgFilename() + "？");
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						HttpHandler handler = Work2DetailsListActivityController
								.getInstance().downloadAtt(att);

						httpDownList.add(handler);
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		for (HttpHandler item : httpDownList) {
			if (item != null) {
				if (item.getState() == HttpHandler.State.LOADING) {
					item.cancel();
				}
			}
		}
		httpDownList.clear();
		if (adapter.mMediaPlayer.isPlaying()) {
			adapter.mMediaPlayer.stop();
		}
		super.onDestroy();
	}
}
