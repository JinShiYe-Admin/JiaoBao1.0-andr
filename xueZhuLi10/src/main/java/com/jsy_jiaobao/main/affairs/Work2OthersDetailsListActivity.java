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
import com.jsy_jiaobao.po.personal.GetSendToMeMsg;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.jsy_jiaobao.po.personal.SendToMeMsg;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 全部，未读，未回复，已回复,点击头像进入的事务详情Activity
 */
public class Work2OthersDetailsListActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView>, OnClickListener {
	final public static int Work2DetailsAttClick = 10;
	private int pageNum = 1;
	/** 发送者AccID */
	private int senderAccId;
	/** 回复列表页码 **/
	private int pageNum_feeback = 1;
	/** 回复列表有无更多 **/
	private boolean haveMore_fee = true;
	/** 第一条信息的id */
	private String TabIDStr;
	/** 第一条信息的日期 */
	private String MsgRecDate;
	/** 发送者姓名 **/
	private String UserName;
	/** 分页标志值，此标志第1页为空，从第2页起须提供。 **/
	private String lastId = "1";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	private ArrayList<Object> workList = new ArrayList<>();
	private List<HttpHandler> httpDownList = new ArrayList<>();

	private Context mContext;
	private Button btn_reply;// 回复按钮
	private IEditText edt_keywords;// 回复编辑框
	private Work2DetailListAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			TabIDStr = savedInstanceState.getString("TabIDStr");
			MsgRecDate = savedInstanceState.getString("MsgRecDate");
			UserName = savedInstanceState.getString("UserName");
			senderAccId = savedInstanceState.getInt("senderAccId");
		} else {
			initPassData();
		}
		initViews();
	}

	private void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				TabIDStr = bundle.getString("TabIDStr");
				MsgRecDate = bundle.getString("MsgRecDate");
				UserName = bundle.getString("UserName");
				senderAccId = bundle.getInt("senderAccId");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("senderAccId", senderAccId);
		outState.putString("TabIDStr", TabIDStr);
		outState.putString("MsgRecDate", MsgRecDate);
		outState.putString("UserName", UserName);
	}

	private void initViews() {
		setContentView(R.layout.activity_work2details);
		setActionBarTitle(UserName);
		mContext = this;
		LinearLayout reply_layout;// 回复区域布局
		reply_layout = (LinearLayout) findViewById(R.id.article_layout_reply);
		btn_reply = (Button) findViewById(R.id.article_btn_send);
		edt_keywords = (IEditText) findViewById(R.id.article_edt_mywords);
		CusListView listView;
		listView = (CusListView) findViewById(R.id.listview);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		Work2DetailsListActivityController.getInstance().setContext(this);
		mPullRefreshScrollView.setMode(Mode.BOTH);
		mPullRefreshScrollView.setOnRefreshListener(this);
		mPullRefreshScrollView.setPullLabel(
				getResources().getString(
						R.string.pullToRefresh_header_pull_loadmore),
				Mode.PULL_FROM_START);
		btn_reply.setOnClickListener(this);
		reply_layout.setVisibility(View.VISIBLE);
		adapter = new Work2DetailListAdapter(this, onclickListener);
		adapter.setData(workList);
		adapter.setWorkType(2);
		listView.setAdapter(adapter);
		ShowDetail();
	}

	Handler mHandler = new Handler();
	OnClickListener onclickListener = new OnClickListener() {// 点击附件
		@Override
		public void onClick(View v) {
			MobclickAgent
					.onEvent(
							mContext,
							getResources()
									.getString(
											R.string.Work2OthersDetailsListActivity_onclickListener));
			Attlist att = (Attlist) v.getTag();
			dialog_down(att);
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
											R.string.Work2OthersDetailsListActivity_article_btn_send));
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
		params.addBodyParameter("pageNum", String.valueOf(pageNum_feeback));//
		params.addBodyParameter("uid", TabIDStr);//
		Work2DetailsListActivityController.getInstance().ShowDetail(params);
	}

	/**
	 * 下拉加载
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (!"".equals(lastId)) {
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(pageNum));//
			params.addBodyParameter("senderAccId", String.valueOf(senderAccId));//
			if (!"1".equals(lastId)) {
				params.addBodyParameter("lastId", lastId);//
			}
			Work2DetailsListActivityController.getInstance().SendToMeMsgList(
					params);// 取单个用户发给我消息列表
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
		case Work2DetailsAttClick:// 没找着这段代码的用处
			Attlist att = (Attlist) list.get(1);
			dialog_down(att);
			break;
		case Constant.msgcenter_work2_SendToMeMsgList:// 获取我发送的消息列表（下拉操作）
			mPullRefreshScrollView.onRefreshComplete();
			GetSendToMeMsg sendToMe = (GetSendToMeMsg) list.get(1);
			lastId = sendToMe.getLastID();
			ArrayList<SendToMeMsg> mySendMsg = sendToMe.getCommMsgList();
			if (pageNum == 1) {
				for (int i = 1; i <= mySendMsg.size(); i++) {
					workList.add(0, mySendMsg.get(i - 1));
				}
			} else {
				for (SendToMeMsg item : mySendMsg) {
					workList.add(0, item);
				}
			}
			adapter.notifyDataSetChanged();
			pageNum++;
			break;
		case Constant.msgcenter_work2_FirstWorkDetails:// 获取我发出的信息详情
			mPullRefreshScrollView.onRefreshComplete();
			GetWorkMsgDetails getWorkDetails = (GetWorkMsgDetails) list.get(1);
			List<FeeBack> feeBackList = getWorkDetails.getFeebackList();
			haveMore_fee=feeBackList.size()>=20;
			if (pageNum_feeback == 1) {
				workList.clear();
				workList.add(getWorkDetails);
			} else {
				GetWorkMsgDetails firstWork = (GetWorkMsgDetails) workList
						.get(workList.size() - 1);
				firstWork.getFeebackList().addAll(feeBackList);
			}
			adapter.notifyDataSetChanged();
			break;
		case Constant.msgcenter_work2_addfeeback:// 成功回复，发给我的交流信息
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
		default:
			break;
		}
	}

	/**
	 * 确认下载附件提示框
	 * 
	 * @param att att
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
