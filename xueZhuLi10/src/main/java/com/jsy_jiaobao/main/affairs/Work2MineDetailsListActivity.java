package com.jsy_jiaobao.main.affairs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Attlist;
import com.jsy_jiaobao.po.personal.FeeBack;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.jsy_jiaobao.po.personal.MySendMsg;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 我发的，并且是点击头像进入的事务详情Activity
 */
public class Work2MineDetailsListActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView>, OnClickListener {
	private static final String TAG = "Work2MineDetailsListActivity";
	final public static int Work2DetailsAttClick = 10;
	int reply_position = -1;
	private int pageNum = 1;
	private int pageNum_feeback = 1;
	/** 未读数量 */
	private int ReadFlag = 0;
	/** 第一条信息的id */
	private String TabIDStr;
	/** 第一条信息的日期 */
	private String MsgRecDate;
	private boolean haveMore_fee = true;
	private boolean haveMore_work = true;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private List<HttpHandler> httpDownList = new ArrayList<HttpHandler>();
	private ArrayList<Object> workList = new ArrayList<Object>();

	private Context mContext;
	private LinearLayout reply_layout;// 回复区域布局
	private IEditText edt_keywords;// 回复编辑框
	private Button btn_reply;// 回复按钮
	private CusListView listView;
	private Work2DetailListAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, TAG);
		if (savedInstanceState != null) {
			TabIDStr = savedInstanceState.getString("TabIDStr");
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
				TabIDStr = bundle.getString("TabIDStr");
				MsgRecDate = bundle.getString("MsgRecDate");
				ReadFlag = bundle.getInt("ReadFlag");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("ReadFlag", ReadFlag);
		outState.putString("TabIDStr", TabIDStr);
		outState.putString("MsgRecDate", MsgRecDate);
	}

	private void initViews() {
		setContentView(R.layout.activity_work2details);
		setActionBarTitle("我发送的信息");
		mContext = this;
		reply_layout = (LinearLayout) findViewById(R.id.article_layout_reply);
		edt_keywords = (IEditText) findViewById(R.id.article_edt_mywords);
		btn_reply = (Button) findViewById(R.id.article_btn_send);
		listView = (CusListView) findViewById(R.id.listview);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		Work2DetailsListActivityController.getInstance().setContext(this);

		mPullRefreshScrollView.setOnRefreshListener(this);
		mPullRefreshScrollView.setPullLabel(
				getResources().getString(
						R.string.pullToRefresh_header_pull_loadmore),
				Mode.PULL_DOWN_TO_REFRESH);
		reply_layout.setVisibility(View.VISIBLE);
		adapter = new Work2DetailListAdapter(this, onclickListener, mHandler);
		adapter.setData(workList);
		// adapter.setAuthor("我");
		adapter.setWorkType(1);
		listView.setAdapter(adapter);
		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
		btn_reply.setOnClickListener(this);
		Work2DetailsListActivityController.getInstance().MarkRead(TabIDStr);//标记为已读
	}

	Handler mHandler = new Handler();
	OnClickListener onclickListener = new OnClickListener() {// 监听点击附件
		@Override
		public void onClick(View v) {
			MobclickAgent
					.onEvent(
							mContext,
							getResources()
									.getString(
											R.string.Work2MineDetailsListActivity_onclickListener));
			Attlist att = (Attlist) v.getTag();
			dialog_down(att);//确认下载提示框
		}
	};

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.article_btn_send) {// 点击回复按钮
			MobclickAgent
					.onEvent(
							mContext,
							getResources()
									.getString(
											R.string.Work2MineDetailsListActivity_article_btn_send));
			String str = edt_keywords.getTextString();
			if (!TextUtils.isEmpty(str)) {
				btn_reply.setEnabled(false);
				RequestParams params = new RequestParams();
				params.addBodyParameter("uid", TabIDStr);//加密后的标识字段
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
		params.addBodyParameter("pageNum", String.valueOf(pageNum_feeback));//
		params.addBodyParameter("uid", TabIDStr);//
		params.addBodyParameter("ReadFlag", String.valueOf(ReadFlag));//
		Work2DetailsListActivityController.getInstance().ShowDetail(params);
	}

	/**
	 * 下拉加载
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (haveMore_work) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(pageNum));//
			Work2DetailsListActivityController.getInstance().GetMySendMsgList(
					params);//获取我发送的消息列表
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);
			mPullRefreshScrollView.onRefreshComplete();
		}
	}
	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (haveMore_fee) {
			pageNum_feeback++;
			ShowDetail();
		} else {
			ToastUtil.showMessage(mContext, R.string.no_more);
			mPullRefreshScrollView.onRefreshComplete();
		}
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
		super.onPause();
		MobclickAgent.onPause(this);
	}	

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work2_GetMySendMsgList://获取我发送的消息列表（下拉操作）
			mPullRefreshScrollView.onRefreshComplete();
			ArrayList<MySendMsg> mySendMsg = (ArrayList<MySendMsg>) list.get(1);
			if (mySendMsg != null) {
				if (mySendMsg.size() < 10) {
					haveMore_work = false;
				} else {
					haveMore_work = true;
				}
				if (pageNum == 1) {
					for (int i = 1; i < mySendMsg.size(); i++) {
						workList.add(0, mySendMsg.get(i));
					}
				} else {
					for (MySendMsg item : mySendMsg) {
						workList.add(0, item);
					}
				}
				adapter.notifyDataSetChanged();
				pageNum++;
			}
			break;
		case Constant.msgcenter_work2_addfeeback://成功回复，发给我的交流信息
			String result = (String) list.get(1);
			if ("succ".equals(result)) {
				ToastUtil.showMessage(mContext, R.string.reply_success);
				GetWorkMsgDetails firstWork = (GetWorkMsgDetails) workList
						.get(workList.size() - 1);
				FeeBack succ = new FeeBack();
				succ.setJiaobaohao(Integer.parseInt(BaseActivity.sp.getString(
						"JiaoBaoHao", "")));
				succ.setFeeBackMsg(edt_keywords.getTextString());
				succ.setUserName(BaseActivity.sp.getString("UserName", ""));
				succ.setRecDate(dateFormat.format(new Date(System
						.currentTimeMillis())));
				firstWork.getFeebackList().add(0, succ);
				adapter.notifyDataSetChanged();
				edt_keywords.setText("");
			}
			btn_reply.setEnabled(true);
			break;
		case Constant.msgcenter_work2_FirstWorkDetails://获取我发出的信息详情
			mPullRefreshScrollView.onRefreshComplete();
			GetWorkMsgDetails getWorkDetails = (GetWorkMsgDetails) list.get(1);
			if (pageNum_feeback == 1) {
				if (getWorkDetails.getFeebackList().size() < 20) {
					haveMore_fee = false;
				} else {
					haveMore_fee = true;
				}
				workList.clear();
				workList.add(getWorkDetails);
			} else {
				List<FeeBack> feeBackList = getWorkDetails.getFeebackList();
				if (feeBackList.size() < 20) {
					haveMore_fee = false;
				} else {
					haveMore_fee = true;
				}
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
	 * 确认下载提示框
	 * @param att 事务里的附件
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
		super.onDestroy();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}
}
