package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.GetAllCategory;
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.jsy_jiaobao.po.qiuzhi.PickedIndex;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Subject;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 求知界面
 */
public class QiuZhiFragment extends SherlockFragment implements
		OnClickListener, OnRefreshListener2<ListView>, Watcher {
	private final static String TAG = "QiuZhiFragment";
	private Context mContext;
	final private static int CATEGORY_INDEX = -1;// 首页
	final private static int CATEGORY_RECOMMENT = -2;// 推荐
	final private static int CATEGORY_SIFTION = -3;// 精选

	final private static int CATEGORY_LV2_ALL = -1;// 全部
	final private static int CATEGORY_LV2_EVIDENCE = 1;// 有证据
	final private static int CATEGORY_LV2_DISCUSS = 0;// 在讨论
	// private PullToRefreshScrollView mPullRefreshScrollView;
	private PullToRefreshListView listView;// ,listViewTop;
	private LinearLayout layout_tabscroll;
	private LinearLayout layout_chose;
	private RelativeLayout layout_topic;
	private TextView tv_topic;
	private TextView tv_moretopic;
	private TextView tv_all, tv_evidence, tv_discuss, tv_dismiss;
	private ArrayList<View> tabViewList = new ArrayList<View>();
	private int selectedTab = 0;
	private int selectedTabID = 0;
	private TextView mNoContent;
	private ArrayList<GetAllCategory> allCategory;
	private QiuZhiIndexListAdapter adapterIndex; // ListView的Adapter
	ArrayList<Object> questionList;
	// private QiuZhiIndexTopAdapter adapterTop;
	/** <pre>	一级话题id	flag=全部，有证据，在讨论。 	数据 */
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, HashMap<Integer, ArrayList<Object>>> dataMap = new HashMap<Integer, HashMap<Integer, ArrayList<Object>>>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> tabRowCountMap = new HashMap<Integer, Integer>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> tabPageNumMap = new HashMap<Integer, Integer>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> categoryFlagMap = new HashMap<Integer, Integer>();
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> dismissMap = new HashMap<Integer, Integer>();
	// 二级话题id 数据列表
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, ArrayList<Object>> topDataMap = new HashMap<Integer, ArrayList<Object>>();
	private int hiddenNum = 0;// 隐藏数量
	private int mark = 0;// 标记

	public static QiuZhiFragment newInstance() {
		return new QiuZhiFragment();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		if (savedInstanceState != null) {
			try {
				allCategory = (ArrayList<GetAllCategory>) savedInstanceState
						.getSerializable("allCategory");
				selectedTabID = (Integer) savedInstanceState
						.getInt("selectedTabID");
				dataMap = (HashMap<Integer, HashMap<Integer, ArrayList<Object>>>) savedInstanceState
						.getSerializable("dataMap");
				tabPageNumMap = (HashMap<Integer, Integer>) savedInstanceState
						.getSerializable("tabPageNumMap");
				tabRowCountMap = (HashMap<Integer, Integer>) savedInstanceState
						.getSerializable("tabRowCountMap");
				categoryFlagMap = (HashMap<Integer, Integer>) savedInstanceState
						.getSerializable("categoryFlagMap");
				dismissMap = (HashMap<Integer, Integer>) savedInstanceState
						.getSerializable("dismissMap");
				selectedTab = (Integer) savedInstanceState
						.getInt("selectedTab");
				mark = (Integer) savedInstanceState.getInt("Mark");
				// 数据容易丢失
				questionList = (ArrayList<Object>) savedInstanceState
						.getSerializable("questionList");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (mark != 7) {
				reLogin();
			}
		}
		return inflater.inflate(R.layout.fragment_qiuzhi, container, false);
	}

	/**
	 * 重新登陆
	 */
	private void reLogin() {
		// TODO Auto-generated method stub
		Intent i = getActivity()
				.getBaseContext()
				.getPackageManager()
				.getLaunchIntentForPackage(
						getActivity().getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		getActivity().finish();
	}

	/**
	 * View
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		QiuZhiFragmentController.getInstance().setContext(this);
		initViews(view);
		questionList = new ArrayList<Object>();
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 保存意外销毁的数据
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		try {
			outState.putSerializable("allCategory", allCategory);
			outState.putInt("selectedTabID", selectedTabID);
			outState.putSerializable("dataMap", dataMap);
			outState.putSerializable("tabPageNumMap", tabPageNumMap);
			outState.putSerializable("tabRowCountMap", tabRowCountMap);
			outState.putSerializable("categoryFlagMap", categoryFlagMap);
			outState.putSerializable("dismissMap", dismissMap);
			outState.putInt("selectedTab", selectedTab);
			outState.putInt("Mark", 7);
			outState.putSerializable("questionList", questionList);

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onSaveInstanceState(outState);

	}

	/**
	 * 自定义方法
	 * 
	 * @功能 初始化界面
	 */
	private void initViews(View view) {
		TextView title = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		title.setFocusable(true);
		title.setFocusableInTouchMode(true);
		title.requestFocus();
		title.requestFocusFromTouch();
		WatchedEntityIndexItem.getInstance().addWatcher(this);

		listView = (PullToRefreshListView) view
				.findViewById(R.id.qiuzhi_listview_index);

		listView.setOnScrollListener(new PauseOnScrollListener(JSYApplication
				.getInstance().bitmap, false, true));

		layout_tabscroll = (LinearLayout) view
				.findViewById(R.id.qiuzhi_layout_scrolltab);
		layout_chose = (LinearLayout) view
				.findViewById(R.id.qiuzhi_index_layout_chose);
		layout_topic = (RelativeLayout) view
				.findViewById(R.id.qiuzhi_index_layout_topic);
		tv_topic = (TextView) view.findViewById(R.id.qiuzh_index_tv_topic);
		tv_moretopic = (TextView) view.findViewById(R.id.qiuzhi_tv_moretopic);
		tv_all = (TextView) view.findViewById(R.id.qiuzhi_index_tv_all);
		tv_evidence = (TextView) view
				.findViewById(R.id.qiuzhi_index_tv_evidence);
		tv_discuss = (TextView) view.findViewById(R.id.qiuzhi_index_tv_discuss);
		tv_dismiss = (TextView) view.findViewById(R.id.qiuzhi_index_tv_dismiss);
		mNoContent = (TextView) view.findViewById(R.id.qiuzhi_noContent);
		mNoContent.setVisibility(View.GONE);
		// 设置监听事件
		tv_moretopic.setOnClickListener(this);
		tv_all.setOnClickListener(this);
		tv_evidence.setOnClickListener(this);
		tv_discuss.setOnClickListener(this);
		layout_topic.setOnClickListener(this);
		listView.setOnRefreshListener(this);
		adapterIndex = new QiuZhiIndexListAdapter(getActivity(), true);
		// adapterTop = new QiuZhiIndexTopAdapter(getActivity());
		listView.setAdapter(adapterIndex);
		selectedTab = 0;
		/** 获取主题列表 */
		QiuZhiFragmentController.getInstance().GetAllCategory();
	}

	/**
	 * 二级菜单选项
	 * 
	 * @param flag
	 */
	@SuppressWarnings("deprecation")
	private void choseTabFlag(int flag) {
		questionList.clear();
		mNoContent.setVisibility(View.GONE);
		tv_dismiss.setVisibility(View.GONE);
		switch (flag) {
		case CATEGORY_LV2_ALL:
			// 全部
			categoryFlagMap.put(selectedTabID, CATEGORY_LV2_ALL);
			tv_all.setBackgroundResource(R.drawable.rounded_rectangle);
			tv_all.setTextColor(getResources().getColor(R.color.color_03ab35));
			tv_evidence.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_evidence.setTextColor(getResources().getColor(R.color.black));
			tv_discuss.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_discuss.setTextColor(getResources().getColor(R.color.black));
			break;
		case CATEGORY_LV2_EVIDENCE:
			// 有证据
			categoryFlagMap.put(selectedTabID, CATEGORY_LV2_EVIDENCE);
			tv_all.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_all.setTextColor(getResources().getColor(R.color.black));
			tv_evidence.setBackground(getResources().getDrawable(
					R.drawable.rounded_rectangle));
			tv_evidence.setTextColor(getResources().getColor(
					R.color.color_03ab35));
			tv_discuss.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_discuss.setTextColor(getResources().getColor(R.color.black));
			hiddenNum = dismissMap.get(selectedTabID);
			// 显示隐藏的数量
			setDismissText();
			break;
		case CATEGORY_LV2_DISCUSS:
			// 在讨论
			categoryFlagMap.put(selectedTabID, CATEGORY_LV2_DISCUSS);
			tv_all.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_all.setTextColor(getResources().getColor(R.color.black));
			tv_evidence.setBackgroundColor(getResources().getColor(
					R.color.color_ebebeb));
			tv_evidence.setTextColor(getResources().getColor(R.color.black));
			tv_discuss.setBackgroundResource(R.drawable.rounded_rectangle);
			tv_discuss.setTextColor(getResources().getColor(
					R.color.color_03ab35));
			break;

		default:
			break;
		}
	}

	/**
	 * 选择话题
	 * 
	 * @param isTabView
	 */
	private void choseTab(boolean isTabView) {
		questionList.clear();
		mNoContent.setVisibility(View.GONE);
		if (isTabView) {
			for (int i = 0; i < tabViewList.size(); i++) {
				TextView tab = (TextView) tabViewList.get(i);
				tab.setTextColor(getResources().getColor(R.color.black));
				tab.setBackgroundColor(Color.WHITE);
			}
			layout_chose.setVisibility(View.GONE);
			layout_topic.setVisibility(View.GONE);
			((TextView) tabViewList.get(selectedTab))
					.setTextColor(getResources().getColor(R.color.color_03ab35));
			tabViewList.get(selectedTab).setBackgroundResource(
					R.drawable.qiuzhi_tabbg_selected);
			selectedTabID = (Integer) tabViewList.get(selectedTab).getTag(
					R.id.tabtag_category);
		}
		listView.setMode(Mode.BOTH);
		HashMap<Integer, ArrayList<Object>> data = dataMap.get(selectedTabID);
		// 二级菜单
		int flag = categoryFlagMap.get(selectedTabID);
		// 隐藏数目
		hiddenNum = dismissMap.get(selectedTabID);
		// 有证据
		if (flag == CATEGORY_LV2_EVIDENCE) {
			setDismissText();
		}
		adapterIndex.setCategoryID(selectedTabID);
		choseTabFlag(flag);
		ArrayList<Object> list = data.get(flag);
		switch (selectedTab) {
		case 0:
			// 首页
			layout_chose.setVisibility(View.VISIBLE);
			if (null == list || list.size() == 0) {
				tabPageNumMap.put(CATEGORY_INDEX, 1);
				tabRowCountMap.put(CATEGORY_INDEX, 0);
				UserIndexQuestion(tabPageNumMap.get(CATEGORY_INDEX),
						tabRowCountMap.get(CATEGORY_INDEX), flag);
				list = new ArrayList<Object>();
				dataMap.get(selectedTabID).put(flag, list);
			}
			adapterIndex.setData(dataMap.get(selectedTabID).get(flag));
			break;
		case 1:
			// 推荐
			if (null == list || list.size() == 0) {
				tabPageNumMap.put(CATEGORY_RECOMMENT, 1);
				tabRowCountMap.put(CATEGORY_RECOMMENT, 0);
				RecommentIndex(tabPageNumMap.get(CATEGORY_RECOMMENT),
						tabRowCountMap.get(CATEGORY_RECOMMENT));
				list = new ArrayList<Object>();
				dataMap.get(selectedTabID).put(flag, list);
			}
			adapterIndex.setData(dataMap.get(selectedTabID).get(flag));
			break;
		case 2:
			// 精选
			// listView.setMode(Mode.PULL_FROM_START);
			if (null == list || list.size() == 0) {
				GetPickedById(0);
				list = new ArrayList<Object>();
				dataMap.get(selectedTabID).put(flag, list);
			} else {
				// listViewTop.setVisibility(0);
			}
			adapterIndex.setTopData(topDataMap.get(selectedTabID));
			adapterIndex.setData(dataMap.get(selectedTabID).get(flag));
			break;
		default:
			// 其他
			if (isTabView) {
				for (GetAllCategory getAll : allCategory) {
					int id = getAll.getItem().getTabID();
					if (selectedTabID == id) {
						tv_topic.setText(getAll.getItem().getSubject().trim());
						break;
					} else {
						ArrayList<Subject> subitem = getAll.getSubitem();
						for (Subject item : subitem) {
							int id1 = item.getTabID();
							if (selectedTabID == id1) {
								tv_topic.setText(item.getSubject().trim());
								break;
							}
						}
					}
				}
			}
			layout_topic.setVisibility(View.VISIBLE);
			layout_chose.setVisibility(View.VISIBLE);
			ArrayList<Object> topList = topDataMap.get(selectedTabID);
			// 置顶话题
			if (null == topList || topList.size() == 0) {
				// listViewTop.setVisibility(View.GONE);
				QiuZhiFragmentController.getInstance().GetCategoryTopQ(
						selectedTabID);
				// } else {
				// listViewTop.setVisibility(View.VISIBLE);
			}
			// 数据列表为空
			if (null == list || list.size() == 0) {
				tabPageNumMap.put(selectedTabID, 1);
				tabRowCountMap.put(selectedTabID, 0);
				CategoryIndexQuestion(tabPageNumMap.get(selectedTabID),
						tabRowCountMap.get(selectedTabID), selectedTabID, flag);
				list = new ArrayList<Object>();
				dataMap.get(selectedTabID).put(flag, list);
			}
			adapterIndex.setData(dataMap.get(selectedTabID).get(flag));
			break;
		}
		adapterIndex.setTopData(topDataMap.get(selectedTabID));
		adapterIndex.notifyDataSetChanged();
	}

	/**
	 * 一级菜单的监听方法
	 * 
	 * @功能 获取选择的一级菜单 选项
	 */
	private OnClickListener onTabClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.select_tab));
			selectedTab = (Integer) v.getTag(R.id.tabtag_position);
			choseTab(true);
		}
	};

	/**
	 * 界面控件控件的监听事件
	 * 
	 * @功能 点击事件的监听
	 */
	@Override
	public void onClick(View v) {
		hiddenNum = 0;
		switch (v.getId()) {
		case R.id.qiuzhi_tv_moretopic:
			// 更多话题
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.chose_attention_topic));
			Intent intent = new Intent(getActivity(),
					QiuZhiChoseClazzActivity.class);
			intent.putExtra("isIndex", true);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
			break;
		case R.id.qiuzhi_index_tv_all:
			// 二级菜单 全部
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.chose_topic_all));
			choseTabFlag(CATEGORY_LV2_ALL);
			choseTab(false);
			break;
		case R.id.qiuzhi_index_tv_evidence:
			// 二级菜单 有证据
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.chose_topic_evidence));
			choseTabFlag(CATEGORY_LV2_EVIDENCE);
			choseTab(false);
			break;
		case R.id.qiuzhi_index_tv_discuss:
			// 二级菜单 在讨论
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.chose_topic_discuss));
			choseTabFlag(CATEGORY_LV2_DISCUSS);
			choseTab(false);
			break;
		case R.id.qiuzhi_index_layout_topic:
			// 选择关注话题按钮
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.chose_topic_details));
			Intent intent1 = new Intent(getActivity(),
					QiuZhiChoseClazzActivity.class);
			intent1.putExtra("TabID", (Integer) tabViewList.get(selectedTab)
					.getTag(R.id.tabtag_category));
			startActivityForResult(intent1, 3);
			getActivity().overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取问题列表（首页）。
	 */
	public void UserIndexQuestion(int pageNum, int RowCount, int flag) {
		QiuZhiFragmentController.getInstance().UserIndexQuestion(
				String.valueOf(pageNum), String.valueOf(RowCount),
				String.valueOf(flag));
	}

	/**
	 * 获取问题列表（推荐）。
	 */
	public void RecommentIndex(int pageNum, int RowCount) {
		QiuZhiFragmentController.getInstance().RecommentIndex(
				String.valueOf(pageNum), String.valueOf(RowCount));
	}

	/**
	 * 获取问题列表（精选）。
	 */
	public void GetPickedById(int tabId) {
		QiuZhiFragmentController.getInstance().GetPickedById(tabId);
	}

	/**
	 * 获取问题列表（推荐后面的）。
	 * 
	 * @param flag
	 */
	public void CategoryIndexQuestion(int pageNum, int rowCount,
			int CategoryId, int flag) {
		QiuZhiFragmentController.getInstance().CategoryIndexQuestion(pageNum,
				rowCount, CategoryId, flag);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 生命周期时间
	 */
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		try {
			String isOld = ACache.get(getActivity().getApplicationContext(),
					"qiuzhi").getAsString("isOld");
			if ("false".equals(isOld)) {
				dataRefresh();
			}
		} catch (Exception e) {
		}
		adapterIndex.notifyDataSetChanged();
	}

	/***
	 * 生命周期事件
	 */
	@Override
	public void onPause() {
		MobclickAgent.onPageEnd(TAG);
		ACache.get(getActivity().getApplicationContext(), "qiuzhi").put(
				"isOld", "true");
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		adapterIndex.Destroy();
		EventBusUtil.unregister(this);
		WatchedEntityIndexItem.getInstance().removeWatcher(this);
		super.onDetach();
	}

	@Override
	public void onAttach(Activity activity) {
		EventBusUtil.register(this);
		super.onAttach(activity);
	}

	/**
	 * eventBus 获取到的数据的处理
	 * 
	 * @param list
	 */
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		mNoContent.setVisibility(View.GONE);
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.system_login_again:
			// 重新登陆
			QiuZhiFragmentController.getInstance().GetAllCategory();
			break;
		case Constant.msgcenter_qiuzhi_GetPickedById:
			// 通过指定ID获取一个精选问题集或最新的一个精选问题集
			listView.onRefreshComplete();
			GetPicked getPicked = (GetPicked) list.get(1);
			// 精选列表
			ArrayList<PickedIndex> pickList = getPicked.getPickContent();
			// 置顶列表
			ArrayList<Object> topList = new ArrayList<Object>();
			topList.add(getPicked);
			topDataMap.get(selectedTabID).clear();
			topDataMap.get(selectedTabID).addAll(topList);

			if (null != pickList && pickList.size() > 0) {
				dataMap.get(CATEGORY_SIFTION).get(CATEGORY_LV2_EVIDENCE)
						.clear();
				dataMap.get(CATEGORY_SIFTION).get(CATEGORY_LV2_EVIDENCE)
						.addAll(pickList);
				adapterIndex.notifyDataSetChanged();
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_qiuzhi_GetCategoryTopQ:
			// 置顶话题
			ArrayList<QuestionIndexItem> top = (ArrayList<QuestionIndexItem>) list
					.get(1);
			int stb = (Integer) list.get(2);
			if (stb == selectedTabID && null != top && top.size() > 0) {
				// listViewTop.setVisibility(View.VISIBLE);
				topDataMap.get(selectedTabID).clear();
				topDataMap.get(selectedTabID).addAll(top);
			} else {
				// listViewTop.setVisibility(View.GONE);
			}
			break;
		case Constant.msgcenter_qiuzhi_UserIndexQuestion:
			// 问题列表
			listView.onRefreshComplete();
			ArrayList<QuestionIndexItem> questionList = (ArrayList<QuestionIndexItem>) list
					.get(1);
			if (null != questionList && questionList.size() > 0) {

				int rowCount = questionList.get(0).getRowCount();
				tabRowCountMap.put(selectedTabID, rowCount);
				int num = 0;
				int flag = categoryFlagMap.get(selectedTabID);
				// 有证据
				if (flag == CATEGORY_LV2_EVIDENCE) {
					for (int i = 0; i < questionList.size(); i++) {
						AnswerItem an = questionList.get(i).getAnswer();
						if (an != null) {
							dataMap.get(selectedTabID)
									.get(categoryFlagMap.get(selectedTabID))
									.add(questionList.get(i));
						} else {
							// 无答案内容 的问题数量
							num++;
						}
					}
					this.questionList = dataMap.get(selectedTabID).get(
							categoryFlagMap.get(selectedTabID));
					if (num == 10) {
						// 如果一次加载中的全部问题 都无答案内容 请求下一页数据
						dataLoadMore();
					} else if (this.questionList.size() == 0
							|| this.questionList == null) {
						// 无数据，界面显示 无话题
						mNoContent.setVisibility(View.VISIBLE);
					}
					dismissMap.put(selectedTabID, dismissMap.get(selectedTabID)
							+ num);
					hiddenNum = dismissMap.get(selectedTabID);
					// 显示隐藏话题数量
					setDismissText();
				} else {
					hiddenNum = 0;
					dataMap.get(selectedTabID)
							.get(categoryFlagMap.get(selectedTabID))
							.addAll(questionList);
				}
				this.questionList = dataMap.get(selectedTabID).get(
						categoryFlagMap.get(selectedTabID));
				adapterIndex.notifyDataSetChanged();
			} else {
				ToastUtil.showMessage(getActivity(),
						R.string.temporarily_noContent);
				if (this.questionList.size() == 0 || this.questionList == null) {
					// 问题列表为空
					mNoContent.setVisibility(View.VISIBLE);
				}
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_qiuzhi_RecommentIndex:
			// 获取首页推荐列表
			listView.onRefreshComplete();
			ArrayList<RecommentIndexItem> recomments = (ArrayList<RecommentIndexItem>) list
					.get(1);
			if (null != recomments && recomments.size() > 0) {
				int rowCount = recomments.get(0).getRowCount();
				tabRowCountMap.put(selectedTabID, rowCount);
				dataMap.get(selectedTabID)
						.get(categoryFlagMap.get(selectedTabID))
						.addAll(recomments);
				adapterIndex.notifyDataSetChanged();
			} else {
				ToastUtil.showMessage(getActivity(),
						R.string.temporarily_noContent);
				mNoContent.setVisibility(View.VISIBLE);
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_qiuzhi_GetAllCategory:
			/** 获取主题列表 */
			layout_tabscroll.removeAllViews();
			tabViewList.clear();
			tabPageNumMap.clear();
			tabRowCountMap.clear();
			categoryFlagMap.clear();
			dismissMap.clear();
			selectedTab = 0;
			// 一级话题列表
			allCategory = (ArrayList<GetAllCategory>) list.get(1);
			ACache.get(getActivity().getApplicationContext(), "qiuzhi").put(
					"GetAllCategory", allCategory);
			/**
			 * 一级话题列表 布局
			 */
			Display display = getActivity().getWindowManager()
					.getDefaultDisplay();
			LayoutInflater inflater = getActivity().getLayoutInflater();
			String[] top3 = new String[] { "首页", "推荐", "精选" };
			for (int i = 0; i < top3.length; i++) {
				View view = inflater.inflate(R.layout.qiuzhi_tabview_textview,
						null);
				TextView tabView = (TextView) view
						.findViewById(R.id.simple_textview);
				tabView.setBackgroundColor(Color.WHITE);
				tabView.setText(top3[i]);
				tabView.setOnClickListener(onTabClickListener);
				tabView.setTag(R.id.tabtag_position, i);
				tabView.setTag(R.id.tabtag_category, i * -1 - 1);

				tabView.setTextColor(getResources().getColor(R.color.black));
				tabView.setGravity(Gravity.CENTER);
				layout_tabscroll.addView(tabView,
						(int) (display.getWidth() / 5),
						LayoutParams.MATCH_PARENT);
				tabViewList.add(i, tabView);
				tabPageNumMap.put(i * -1 - 1, 1);
				tabRowCountMap.put(i * -1 - 1, 0);
				categoryFlagMap.put(i * -1 - 1, 1);
				dismissMap.put(i * -1 - 1, 0);
				if (i == 2) {
					topDataMap.put(i * -1 - 1, new ArrayList<Object>());
				}
				HashMap<Integer, ArrayList<Object>> map = new HashMap<Integer, ArrayList<Object>>();
				map.put(-1, new ArrayList<Object>());
				dataMap.put(i * -1 - 1, map);
				mark = 7;
			}
			if (null != allCategory) {

				for (int i = 0; i < allCategory.size(); i++) {
					View view = inflater.inflate(
							R.layout.qiuzhi_tabview_textview, null);
					TextView tabView = (TextView) view
							.findViewById(R.id.simple_textview);
					// TextView tabView = new TextView(getActivity());
					tabView.setBackgroundColor(getResources().getColor(
							R.color.white));
					String txt = allCategory.get(i).getItem().getSubject()
							.trim();
					int tabid = allCategory.get(i).getItem().getTabID();
					tabView.setText(txt);
					tabView.setOnClickListener(onTabClickListener);
					tabView.setTag(R.id.tabtag_position, i + 3);
					tabView.setTag(R.id.tabtag_category, tabid);

					tabView.setTextColor(getResources().getColor(R.color.black));
					tabView.setGravity(Gravity.CENTER);
					layout_tabscroll.addView(tabView,
							(int) (display.getWidth() / 5),
							LayoutParams.FILL_PARENT);

					tabViewList.add(i + 3, tabView);
					tabPageNumMap.put(tabid, 1);
					tabRowCountMap.put(tabid, 0);
					categoryFlagMap.put(tabid, -1);
					dismissMap.put(tabid, 0);
					HashMap<Integer, ArrayList<Object>> map = new HashMap<Integer, ArrayList<Object>>();
					map.put(CATEGORY_LV2_ALL, new ArrayList<Object>());
					map.put(CATEGORY_LV2_EVIDENCE, new ArrayList<Object>());
					map.put(CATEGORY_LV2_DISCUSS, new ArrayList<Object>());
					dataMap.put(tabid, map);
				}
			}
			// 默认选择全部
			selectedTab = 0;
			choseTab(true);
			break;
		case Constant.msgcenter_qiuzhi_CategoryIndexQuestion:
			// 问题列表数据
			DialogUtil.getInstance().cannleDialog();
			listView.onRefreshComplete();
			ArrayList<QuestionIndexItem> questionList11 = (ArrayList<QuestionIndexItem>) list
					.get(1);
			if (null != questionList11 && questionList11.size() > 0) {
				int rowCount = questionList11.get(0).getRowCount();
				// mRowCount = rowCount;
				tabRowCountMap.put(selectedTabID, rowCount);
				int num = 0;
				// 二级菜单
				int flag = categoryFlagMap.get(selectedTabID);
				if (flag == CATEGORY_LV2_EVIDENCE) {
					for (int i = 0; i < questionList11.size(); i++) {
						AnswerItem an = questionList11.get(i).getAnswer();
						if (an != null) {
							dataMap.get(selectedTabID)
									.get(categoryFlagMap.get(selectedTabID))
									.add(questionList11.get(i));
						} else {
							// 隐藏的数目
							num++;
						}
					}
					this.questionList = dataMap.get(selectedTabID).get(
							categoryFlagMap.get(selectedTabID));
					if (num == 10) {
						// 此页全隐藏，加载下一页
						dataLoadMore();
					} else if (this.questionList.size() == 0
							|| this.questionList == null) {
						// 无问题 提示为空
						mNoContent.setVisibility(View.VISIBLE);
					}
					// 保存数据
					dismissMap.put(selectedTabID, dismissMap.get(selectedTabID)
							+ num);
					hiddenNum = dismissMap.get(selectedTabID);
					setDismissText();
				} else {
					hiddenNum = 0;
					dataMap.get(selectedTabID)
							.get(categoryFlagMap.get(selectedTabID))
							.addAll(questionList11);
				}
				this.questionList = dataMap.get(selectedTabID).get(
						categoryFlagMap.get(selectedTabID));
				adapterIndex.notifyDataSetChanged();
			} else {
				ToastUtil.showMessage(getActivity(),
						R.string.temporarily_noContent);
				if (this.questionList.size() == 0 || this.questionList == null) {
					mNoContent.setVisibility(View.VISIBLE);
				}
			}
			break;
		default:
			break;
		}
	}

	// 显示以藏问题数量提示
	private void setDismissText() {
		int dismiss = dismissMap.get(selectedTabID);
		if (dismiss == 0) {
			tv_dismiss.setVisibility(8);
		} else {
			tv_dismiss.setVisibility(0);
			tv_dismiss.setText("有" + dismiss + "条问题因答案被屏蔽或被删除无法查看");
		}
	}

	// 刷新数据
	private void dataRefresh() {
		tabPageNumMap.put(selectedTabID, 1);
		tabRowCountMap.put(selectedTabID, 0);
		if (null != dataMap.get(selectedTabID)) {
			dataMap.get(selectedTabID).get(categoryFlagMap.get(selectedTabID))
					.clear();
		}
		if (categoryFlagMap.get(selectedTabID) == CATEGORY_LV2_EVIDENCE) {
			dismissMap.put(selectedTabID, 0);
		}
		adapterIndex.setTopData(topDataMap.get(selectedTabID));
		adapterIndex.setData(dataMap.get(selectedTabID).get(
				categoryFlagMap.get(selectedTabID)));
		adapterIndex.notifyDataSetChanged();
		switch (selectedTab) {
		case 0:
			// 首页
			UserIndexQuestion(tabPageNumMap.get(selectedTabID),
					tabRowCountMap.get(selectedTabID),
					categoryFlagMap.get(selectedTabID));
			break;
		case 1:
			// 推荐
			RecommentIndex(tabPageNumMap.get(selectedTabID),
					tabRowCountMap.get(selectedTabID));
			break;
		case 2:
			// 精选
			GetPickedById(0);
			break;
		default:
			// 其他
			QiuZhiFragmentController.getInstance().GetCategoryTopQ(
					selectedTabID);
			CategoryIndexQuestion(tabPageNumMap.get(selectedTabID),
					tabRowCountMap.get(selectedTabID), selectedTabID,
					categoryFlagMap.get(selectedTabID));
			break;
		}
	}

	/**
	 * 重写方法
	 * 
	 * @功能 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		hiddenNum = 0;
		dataRefresh();
	}

	/**
	 * 自定义方法
	 * 
	 * @功能 加载更多
	 */
	private void dataLoadMore() {
		tabPageNumMap.put(selectedTabID, tabPageNumMap.get(selectedTabID) + 1);
		int rowCount = tabRowCountMap.get(selectedTabID);

		switch (selectedTab) {
		case 0:
			// 首页
			if (rowCount <= dataMap.get(selectedTabID)
					.get(categoryFlagMap.get(selectedTabID)).size()
					+ hiddenNum) {
				listView.post(new Runnable() {

					@Override
					public void run() {
						listView.onRefreshComplete();
					}
				});
				ToastUtil.showMessage(getActivity(), R.string.no_more);
			} else {
				UserIndexQuestion(tabPageNumMap.get(selectedTabID),
						tabRowCountMap.get(selectedTabID),
						categoryFlagMap.get(selectedTabID));
			}
			break;
		case 1:
			// 推荐
			if (rowCount <= dataMap.get(selectedTabID)
					.get(categoryFlagMap.get(selectedTabID)).size()
					+ hiddenNum) {
				listView.post(new Runnable() {

					@Override
					public void run() {
						listView.onRefreshComplete();
					}
				});

				ToastUtil.showMessage(getActivity(), R.string.no_more);
			} else {
				RecommentIndex(tabPageNumMap.get(selectedTabID),
						tabRowCountMap.get(selectedTabID));
			}
			break;
		case 2:
			// 精选
			listView.post(new Runnable() {

				@Override
				public void run() {
					listView.onRefreshComplete();
				}
			});
			ToastUtil.showMessage(getActivity(), R.string.no_more);
			break;
		default:
			// 其他
			if (rowCount <= dataMap.get(selectedTabID)
					.get(categoryFlagMap.get(selectedTabID)).size()
					+ hiddenNum) {
				listView.post(new Runnable() {

					@Override
					public void run() {
						listView.onRefreshComplete();
					}
				});
				ToastUtil.showMessage(getActivity(), R.string.no_more);
			} else {
				CategoryIndexQuestion(tabPageNumMap.get(selectedTabID),
						tabRowCountMap.get(selectedTabID), selectedTabID,
						categoryFlagMap.get(selectedTabID));
			}
			break;
		}
	}

	/**
	 * 重写方法
	 * 
	 * @功能 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		dataLoadMore();
	}

	/**
	 * 重写方法
	 * 
	 * @功能 获取activity 返回数据
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 3:
			try {
				Bundle b = data.getExtras(); // data为B中回传的Intent
				Subject subject = (Subject) b.getSerializable("Subject");
				String str_name = subject.getSubject().trim();
				int id = subject.getTabID();
				if (selectedTabID != id) {
					selectedTabID = id;
					tv_topic.setText(str_name);
					choseTabItem();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// ToastUtil.showMessage(getActivity(), "获取数据错误");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 选择话题返回后 刷新数据
	 */
	@SuppressLint("UseSparseArrays")
	private void choseTabItem() {
		tabPageNumMap.put(selectedTabID, 1);
		tabRowCountMap.put(selectedTabID, 0);
		categoryFlagMap.put(selectedTabID, -1);
		dismissMap.put(selectedTabID, 0);
		HashMap<Integer, ArrayList<Object>> map = new HashMap<Integer, ArrayList<Object>>();
		map.put(CATEGORY_LV2_ALL, new ArrayList<Object>());
		map.put(CATEGORY_LV2_EVIDENCE, new ArrayList<Object>());
		map.put(CATEGORY_LV2_DISCUSS, new ArrayList<Object>());
		dataMap.put(selectedTabID, map);
		QiuZhiFragmentController.getInstance().GetCategoryTopQ(selectedTabID);
		CategoryIndexQuestion(tabPageNumMap.get(selectedTabID),
				tabRowCountMap.get(selectedTabID), selectedTabID, -1);
		topDataMap.put(selectedTabID, new ArrayList<Object>());
		int flag = categoryFlagMap.get(selectedTabID);
		adapterIndex.setTopData(topDataMap.get(selectedTabID));
		adapterIndex.setData(dataMap.get(selectedTabID).get(flag));
		adapterIndex.notifyDataSetChanged();
		choseTabFlag(flag);
	}

	/**
	 * 更新
	 */
	@Override
	public void update(QuestionIndexItem qEntity) {
		switch (selectedTab) {
		case 0:
			// 首页
			if (null != qEntity) {
				ArrayList<Object> list = dataMap.get(selectedTabID).get(
						categoryFlagMap.get(selectedTabID));
				for (int i = 0; i < list.size(); i++) {
					QuestionIndexItem item = (QuestionIndexItem) list.get(i);
					if (null != item) {
						if (item.getTabID() == qEntity.getTabID()) {
							list.set(i, qEntity);
						}
					}
				}
				dataMap.get(selectedTabID).put(
						categoryFlagMap.get(selectedTabID), list);
				adapterIndex.notifyDataSetChanged();
			}
			break;
		case 1:
			// 推荐
			if (null != qEntity) {
				ArrayList<Object> list = dataMap.get(selectedTabID).get(
						categoryFlagMap.get(selectedTabID));
				for (int i = 0; i < list.size(); i++) {
					RecommentIndexItem item = (RecommentIndexItem) list.get(i);
					if (null != item) {
						if (item.getQuestion().getTabID() == qEntity.getTabID()) {
							item.setQuestion(qEntity);
							list.set(i, item);
						}
					}
				}
				dataMap.get(selectedTabID).put(
						categoryFlagMap.get(selectedTabID), list);
				adapterIndex.notifyDataSetChanged();
			}
			break;
		case 2:
			// 精选
			break;
		default:
			// 其他
			if (null != qEntity) {
				ArrayList<Object> list = dataMap.get(selectedTabID).get(
						categoryFlagMap.get(selectedTabID));
				for (int i = 0; i < list.size(); i++) {
					QuestionIndexItem item = (QuestionIndexItem) list.get(i);
					if (null != item) {
						if (item.getTabID() == qEntity.getTabID()) {
							list.set(i, qEntity);
						}
					}
				}
				dataMap.get(selectedTabID).put(
						categoryFlagMap.get(selectedTabID), list);
				adapterIndex.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void update(AnswerItem answer) {

	}

	/**
	 * 重写方法
	 * 
	 * @功能 列表在更新 或加载过程中 不可用（点击）
	 */
	@Override
	public void onPullPageChanging(boolean isChanging) {
		for (int i = 0; i < tabViewList.size(); i++) {
			View view = tabViewList.get(i);
			view.setEnabled(!isChanging);
		}
	}
}