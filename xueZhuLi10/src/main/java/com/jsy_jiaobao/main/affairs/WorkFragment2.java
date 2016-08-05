package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.CommMsg;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

/**
 *查看事务有关的代码不是我写的，并且注释非常少，几乎没有<br>
 *虽然我补充了注释，但是写得对不对我也说不清<br>
 *为什么不由写代码的加注释呢，因为他已经离职了<br>
 *ShangLin Mo 2016-6-27 16:37:21
 */

/**
 * 查看事务Fragment2<br>
 * 友情提示：1.点击未读的事务，该事务被标记为已读，成为未回复的事务 --ShangLin Mo
 */
public class WorkFragment2 extends SherlockFragment implements
		OnRefreshListener2<ScrollView>, OnItemClickListener, ConstantUrl,
		OnClickListener {
	private static final String TAG = "WorkFragment2";
	/** 全部 **/
	final static int pos_all = 0;// 全部
	/** 未读 **/
	final static int pos_unread = 1;// 未读
	/** 未回复 **/
	final static int pos_uncomment = 2;// 未回复
	/** 已回复 **/
	final static int pos_commented = 3;// 已回复
	/** 我发的 **/
	final static int pos_fromme = 4;// 我发的

	public WorkClickType clickType = WorkClickType.all; // 选择的查询类型

	// 分页标志值，此标志第1页为空，从第2页起须提供。
	// private String lastId = "1";

	private int allPageNum = 1;// 全部，列表页数
	private int unreadPageNum = 1;// 未读，列表页数
	private int uncommentPageNum = 1;// 未回复，列表页数
	private int commentedPageNum = 1;// 已回复，列表页数
	private int frommePageNum = 1;// 我发的，列表页数

	private boolean allHaveMore = true;// 全部，下一页是否有数据
	private boolean unreadHaveMore = true;// 未读，下一页是否有数据
	private boolean uncommentHaveMore = true;// 未回复，下一页是否有数据
	private boolean commentedHaveMore = true;// 已回复，下一页是否有数据
	private boolean frommeHaveMore = true;// 我发的，下一页是否有数据

	private ArrayList<CommMsg> allMsgList = new ArrayList<CommMsg>();
	private ArrayList<CommMsg> unreadMsgList = new ArrayList<CommMsg>();
	private ArrayList<CommMsg> uncommentMsgList = new ArrayList<CommMsg>();
	private ArrayList<CommMsg> commentedMsgList = new ArrayList<CommMsg>();
	private ArrayList<CommMsg> frommeMsgList = new ArrayList<CommMsg>();

	private Context mContext;
	private ImageView btn_all;// 全部
	private ImageView btn_unread;// 未读
	private ImageView btn_uncomment;// 未回复
	private ImageView btn_commented;// 已回复
	private ImageView btn_fromme;// 我发的
	private CusListView listView;
	private Work2ListAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;

	public static WorkFragment2 newInstance() {
		WorkFragment2 fragment = new WorkFragment2();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		return inflater.inflate(R.layout.fragment_personal_work2, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		findViews(view);
		WorkFragment2Controller.getInstance().setContext(this);
		initViews();
		super.onViewCreated(view, savedInstanceState);
	}

	private void findViews(View view) {
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		listView = (CusListView) view.findViewById(R.id.work2_listview);
		btn_all = (ImageView) view.findViewById(R.id.work2_btn_all);// 全部
		btn_unread = (ImageView) view.findViewById(R.id.work2_btn_unread);// 未读
		btn_uncomment = (ImageView) view.findViewById(R.id.work2_btn_uncomment);// 未回复
		btn_commented = (ImageView) view.findViewById(R.id.work2_btn_commented);// 已回复
		btn_fromme = (ImageView) view.findViewById(R.id.work2_btn_fromme);// 我发的
		btn_all.setOnClickListener(this);
		btn_unread.setOnClickListener(this);
		btn_uncomment.setOnClickListener(this);
		btn_commented.setOnClickListener(this);
		btn_fromme.setOnClickListener(this);
	}

	private void initViews() {
		btn_all.setImageResource(R.drawable.icon_work_all_selected);
		btn_unread.setImageResource(R.drawable.icon_work_unread_unselect);
		btn_uncomment.setImageResource(R.drawable.icon_work_uncomment_unselect);
		btn_commented.setImageResource(R.drawable.icon_work_commented_unselect);
		btn_fromme.setImageResource(R.drawable.icon_work_fromme_unselect);
		TextView title = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		title.setFocusable(true);
		title.setFocusableInTouchMode(true);
		title.requestFocus();
		title.requestFocusFromTouch();
		// WorkFragment2Controller.getInstance().GetMySendMsgList();
		// SendToMeUserList();
		// String url =
		// ACache.get(getActivity().getApplicationContext()).getAsString("MainUrl")
		// +ConstantUrl.photoURL+"?AccID="+BaseActivity.sp.getString("JiaoBaoHao",
		// "");
		// JSYApplication.getInstance().bitmap.display(photo, url);
		adapter = new Work2ListAdapter(getActivity());
		adapter.setData(allMsgList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		mPullRefreshScrollView.setOnRefreshListener(this);
		CommList(pos_all);
	}

	/**
	 * 设置Tab背景，即点击类型选项时切换显示不同的图片。<br>
	 * 逻辑为：将上一次点击查看的事务类型切换为未选择的图片，再将选择的position设置为被点击后的图片
	 * 
	 * @param click
	 *            上一次点击查看的类型
	 * @param position
	 *            本次点击查看的类型
	 */
	private void setSelectIndicator(WorkClickType click, int position) {
		Log.i(TAG + "setSelectIndicator", click + "-" + position);
		switch (click) {// 上一次点击查看的类型,切换为未选择的图片
		case all:// 全部
			btn_all.setImageResource(R.drawable.icon_work_all_unselect);
			break;
		case unread:// 未读
			btn_unread.setImageResource(R.drawable.icon_work_unread_unselect);
			break;
		case uncomment:// 未回复
			btn_uncomment
					.setImageResource(R.drawable.icon_work_uncomment_unselect);
			break;
		case commented:// 已回复
			btn_commented
					.setImageResource(R.drawable.icon_work_commented_unselect);
			break;
		case fromme:// 我发的
			btn_fromme.setImageResource(R.drawable.icon_work_fromme_unselect);
			break;
		default:
			break;
		}
		switch (position) {// 本次点击查看的类型设为点击的图片
		case pos_all:// 全部
			btn_all.setImageResource(R.drawable.icon_work_all_selected);
			break;
		case pos_unread:// 未读
			btn_unread.setImageResource(R.drawable.icon_work_unread_selected);
			break;
		case pos_uncomment:// 未回复
			btn_uncomment
					.setImageResource(R.drawable.icon_work_uncomment_selected);
			break;
		case pos_commented:// 已回复
			btn_commented
					.setImageResource(R.drawable.icon_work_commented_selected);
			break;
		case pos_fromme:// 我发的
			btn_fromme.setImageResource(R.drawable.icon_work_fromme_selected);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据查询类型获取数据
	 * 
	 * @param msgType
	 *            查询类型
	 */
	private void CommList(int msgType) {
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			RequestParams params = new RequestParams();
			switch (msgType) {
			case pos_all:// 全部
				params.addBodyParameter("pageNum", String.valueOf(allPageNum));//
				WorkFragment2Controller.getInstance().CommList(params,
						Constant.msgcenter_work2_CommListToMeAll);
				break;
			case pos_unread:// 未读
				params.addBodyParameter("pageNum",
						String.valueOf(unreadPageNum));//
				params.addBodyParameter("readflag", "1");// 不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
				WorkFragment2Controller.getInstance().CommList(params,
						Constant.msgcenter_work2_CommListToMeUnRead);
				break;
			case pos_uncomment:// 未回复
				params.addBodyParameter("pageNum",
						String.valueOf(uncommentPageNum));//
				params.addBodyParameter("readflag", "2");// 不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
				WorkFragment2Controller.getInstance().CommList(params,
						Constant.msgcenter_work2_CommListToMeUnComment);
				break;
			case pos_commented:// 已回复
				params.addBodyParameter("pageNum",
						String.valueOf(commentedPageNum));//
				params.addBodyParameter("readflag", "3");// 不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
				WorkFragment2Controller.getInstance().CommList(params,
						Constant.msgcenter_work2_CommListToMeCommented);
				break;
			case pos_fromme:// 我发的
				params.addBodyParameter("pageNum",
						String.valueOf(frommePageNum));
				WorkFragment2Controller.getInstance().MySend(params,
						Constant.msgcenter_work2_CommListFromMe);
				break;
			default:
				break;
			}
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);// 未加入单位
		}
	}

	/**
	 * 点击不同的查询类型（全部，未读，未回复，已回复，我发的）
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.work2_btn_all:// 全部
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.WorkFragment2_all));
			setSelectIndicator(clickType, pos_all);
			if (clickType != WorkClickType.all) {
				clickType = WorkClickType.all;
				adapter.setData(allMsgList);
				if (allMsgList.size() == 0) {
					allPageNum = 1;
					CommList(pos_all);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.work2_btn_unread:// 未读
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.WorkFragment2_unread));
			setSelectIndicator(clickType, pos_unread);
			if (clickType != WorkClickType.unread) {
				clickType = WorkClickType.unread;
				adapter.setData(unreadMsgList);
				if (unreadMsgList.size() == 0) {
					unreadPageNum = 1;
					CommList(pos_unread);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.work2_btn_uncomment:// 未回复
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.WorkFragment2_uncomment));
			setSelectIndicator(clickType, pos_uncomment);
			if (clickType != WorkClickType.uncomment) {
				clickType = WorkClickType.uncomment;
				adapter.setData(uncommentMsgList);
				if (uncommentMsgList.size() == 0) {
					uncommentPageNum = 1;
					CommList(pos_uncomment);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.work2_btn_commented:// 已回复
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.WorkFragment2_commented));
			setSelectIndicator(clickType, pos_commented);
			if (clickType != WorkClickType.commented) {
				clickType = WorkClickType.commented;
				adapter.setData(commentedMsgList);
				if (commentedMsgList.size() == 0) {
					commentedPageNum = 1;
					CommList(pos_commented);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.work2_btn_fromme:// 我发的
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.WorkFragment2_fromme));
			setSelectIndicator(clickType, pos_fromme);
			if (clickType != WorkClickType.fromme) {
				clickType = WorkClickType.fromme;
				adapter.setData(frommeMsgList);
				if (frommeMsgList.size() == 0) {
					frommePageNum = 1;
					CommList(pos_fromme);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		default:
			break;
		}
	}

	// private void SendToMeUserList() {
	// RequestParams params = new RequestParams();
	// params.addBodyParameter("pageNum", String.valueOf(allPageNum));//
	// if (allPageNum != 1) {
	// params.addBodyParameter("lastId", lastId);//
	// }
	// WorkFragment2Controller.getInstance().SendToMeUserList(params);
	// }
	/**
	 * 我写注释的时候觉得这个好像没有用到，但是还是保留着吧--ShangLin Mo
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CommMsg item = (CommMsg) adapter.getData().get(position);
		Intent intent = new Intent(getActivity(),
				Work2OthersDetailsListActivity.class);
		intent.putExtra("TabIDStr", item.getTabIDStr());
		intent.putExtra("MsgRecDate", item.getRecDate());
		intent.putExtra("senderAccId", item.getJiaoBaoHao());
		intent.putExtra("UserName", item.getUserName());
		startActivity(intent);
	}

	/**
	 * 下拉刷新，获取第一页的数据
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {// 已经加入单位
			switch (clickType) {
			case all:// 全部
				allMsgList.clear();
				allPageNum = 1;
				allHaveMore = true;
				CommList(pos_all);
				break;
			case unread:// 未读
				unreadPageNum = 1;
				unreadHaveMore = true;
				unreadMsgList.clear();
				CommList(pos_unread);
				break;
			case uncomment:// 未回复
				uncommentPageNum = 1;
				uncommentHaveMore = true;
				uncommentMsgList.clear();
				CommList(pos_uncomment);
				break;
			case commented:// 已回复
				commentedPageNum = 1;
				commentedHaveMore = true;
				commentedMsgList.clear();
				CommList(pos_commented);
				break;
			case fromme:// 我发的
				frommePageNum = 1;
				frommeHaveMore = true;
				frommeMsgList.clear();
				CommList(pos_fromme);
				break;
			default:
				break;
			}
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);// 提示还未加入单位
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			switch (clickType) {
			case all:
				if (allHaveMore) {
					allPageNum++;
					CommList(pos_all);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case unread:
				if (unreadHaveMore) {
					unreadPageNum++;
					CommList(pos_unread);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case uncomment:
				if (uncommentHaveMore) {
					uncommentPageNum++;
					CommList(pos_uncomment);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case commented:
				if (commentedHaveMore) {
					commentedPageNum++;
					CommList(pos_commented);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case fromme:
				if (frommeHaveMore) {
					frommePageNum++;
					CommList(pos_fromme);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			}
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}

	@Override
	public void onAttach(Activity activity) {
		EventBusUtil.register(this);
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		EventBusUtil.unregister(this);
		super.onDetach();
	}

	@Override
	public void onResume() {
		initUnReadMessageCount();
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		// case Constant.msgcenter_work2_GetMySendMsgList:
		// mySend = (MySendMsg) list.get(1);
		// if (mySend != null) {
		// layout_mine.setVisibility(0);
		// if (mySend.getNoReadCount()>0) {
		// new_message.setVisibility(0);
		// new_message.setText(String.valueOf(mySend.getNoReadCount()));
		// }else{
		// new_message.setVisibility(8);
		// }
		// message.setText(mySend.getMsgContent());
		// time.setText(mySend.getRecDate().replace("T", " "));
		// }else{
		// layout_mine.setVisibility(8);
		// }
		// break;
		// case Constant.msgcenter_work2_SendToMeUserList:
		// mPullRefreshScrollView.onRefreshComplete();
		// GetSendToMeMsg sendtomeMsg = (GetSendToMeMsg) list.get(1);
		// lastId = sendtomeMsg.getLastID();
		// if (sendtomeMsg.getCommMsgList() != null) {
		// sendToMeList.addAll(sendtomeMsg.getCommMsgList());
		// adapter.notifyDataSetChanged();
		// }
		// break;
		case Constant.msgcenter_select_position_1:// 没弄懂这个是干嘛用的
			String query = (String) list.get(1);
			Intent intent_more = new Intent(getActivity(),
					WorkMoreActivity.class);
			intent_more.putExtra("msgType", CommListToMe + "?Content=" + query
					+ "&numPerPage=20&pageNum=");
			intent_more.putExtra("title",
					getResources().getString(R.string.check_message));
			intent_more.putExtra("time", "more_6");
			startActivity(intent_more);
			break;
		case Constant.msgcenter_work2_CommListToMeAll:// 全部
			ArrayList<CommMsg> all = (ArrayList<CommMsg>) list.get(1);
			if (all != null && all.size() > 0) {
				allMsgList.addAll(all);
				if (all.size() == 20) {// 这里有一个BUG，如果拿到最后一页的数据为20条，进行一次上拉加载更多不会提示‘没有更多了’，再进行一次上拉加载更多才会提示‘没有更多了’。同理，其他查询类型也有这个BUG
					allHaveMore = true;
				} else {
					allHaveMore = false;
				}
			} else {
				if (allPageNum == 1) {
					ToastUtil.showMessage(getActivity(), "暂无数据");
				}
				allHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work2_CommListToMeCommented:// 已回复
			ArrayList<CommMsg> commented = (ArrayList<CommMsg>) list.get(1);
			if (commented != null && commented.size() > 0) {
				commentedMsgList.addAll(commented);
				if (commented.size() == 20) {
					commentedHaveMore = true;
				} else {
					commentedHaveMore = false;
				}
			} else {
				if (commentedPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_noRepliedMessage);
				}
				commentedHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work2_CommListToMeUnComment:// 未回复
			ArrayList<CommMsg> uncomment = (ArrayList<CommMsg>) list.get(1);
			if (uncomment != null && uncomment.size() > 0) {
				uncommentMsgList.addAll(uncomment);
				if (uncomment.size() == 20) {
					uncommentHaveMore = true;
				} else {
					uncommentHaveMore = false;
				}
			} else {
				if (uncommentPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_noUnRepliedMessage);
				}
				uncommentHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work2_CommListToMeUnRead:// 未读
			ArrayList<CommMsg> unread = (ArrayList<CommMsg>) list.get(1);
			if (unread != null && unread.size() > 0) {
				unreadMsgList.addAll(unread);
				if (unread.size() == 20) {
					unreadHaveMore = true;
				} else {
					unreadHaveMore = false;
				}
			} else {
				if (unreadPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_noUnreadMessage);
				}
				unreadHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work2_CommListFromMe:// 我发的
			ArrayList<CommMsg> fromme = (ArrayList<CommMsg>) list.get(1);
			if (fromme != null && fromme.size() > 0) {
				frommeMsgList.addAll(fromme);
				if (fromme.size() == 20) {
					frommeHaveMore = true;
				} else {
					frommeHaveMore = false;
				}
			} else {
				if (frommePageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_noISendMessage);
				}
				frommeHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取未读消息条数
	 */
	private void initUnReadMessageCount() {
		/**
		 * 获得当前未读交流信息数量 我的理解是 未读的发给我的消息数量
		 */
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				getNoReadeCount, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (isAdded() && !isDetached()) {
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");

								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(jsonObj
											.getString("Data"),
											BaseActivity.sp_sys.getString(
													"ClientKey", ""));
									tomecount = Integer.parseInt(data);
									mHandler.sendEmptyMessage(0);
								} else if ("8".equals(ResultCode)) {
									LoginActivityController.getInstance()
											.helloService(getActivity());
								}
							} catch (Exception e) {
								ToastUtil.showMessage(
										getActivity(),
										getResources().getString(
												R.string.error_serverconnect)
												+ "r1002");
							}
						}
					}
				});
		/**
		 * 获得当前未读回复数量 我的理解是 未读的回复我的消息数量
		 */
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				getfbtomecount, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (isAdded() && !isDetached()) {
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");

								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(jsonObj
											.getString("Data"),
											BaseActivity.sp_sys.getString(
													"ClientKey", ""));
									replycount = Integer.parseInt(data);
									mHandler.sendEmptyMessage(0);
								} else if ("8".equals(ResultCode)) {
									LoginActivityController.getInstance()
											.helloService(getActivity());
								}
							} catch (Exception e) {
								ToastUtil.showMessage(
										getActivity(),
										getResources().getString(
												R.string.error_serverconnect)
												+ "r1002");
							}
						}
					}
				});
	}

	int totalcount = 0;
	int tomecount = 0;
	int replycount = 0;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			totalcount = tomecount + replycount;
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(Constant.msgcenter_work_notice);
			list.add(totalcount);
			EventBusUtil.post(list);
		}
	};
}
