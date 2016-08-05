package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.schoolcircle.Show2ArtListAdapter.CommitAdapter;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * <pre>
 * 
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 			               佛祖保佑       永无BUG
 * 新版单位展示（学校圈）
 */
public class ShowFragment2 extends SherlockFragment implements OnClickListener,
		OnRefreshListener2<ScrollView> {
	final static int pos_all = 0;// 全部
	final static int pos_local = 1;// 本地
	final static int pos_curunit = 2;// 本单位
	final static int pos_curclass = 3;// 本班级
	final static int pos_att = 4;// 关注
	public ShowClickType clickType = ShowClickType.all;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ImageView btn_curunit;// 本单位
	private ImageView btn_curclass;// 本班
	private ImageView btn_local;// 本地
	private ImageView btn_att;// 关注
	private ImageView btn_all;// 全部
	private CusListView listView;//
	private Show2ArtListAdapter adapter;
	// private Intent intent = new Intent();
	private int curPageNum = 1;// 本单位页码
	private int classPageNum = 1;// 本班页码
	private int localPageNum = 1;// 本地页码
	private int allPageNum = 1;// 全部页码
	private int attPageNum = 1;// 关注页码
	/** 是否还有更多，默认为是 */
	private boolean curUnitHaveMore = true;
	private boolean curClassHaveMore = true;
	private boolean localHaveMore = true;
	private boolean allHaveMore = true;
	private boolean attHaveMore = true;
	// 本单位
	private ArrayList<ArthInfo> curUnitArtList = new ArrayList<ArthInfo>();
	// 本班
	private ArrayList<ArthInfo> curClassArtList = new ArrayList<ArthInfo>();
	// 本地
	private ArrayList<ArthInfo> localUnitArtList = new ArrayList<ArthInfo>();
	// 全部
	private ArrayList<ArthInfo> allUnitArtList = new ArrayList<ArthInfo>();
	// 关注
	private ArrayList<ArthInfo> attUnitArtList = new ArrayList<ArthInfo>();

	public static CommitAdapter commitAdapter;

	public static int unitID;
	protected static ArthInfo clickArthInfo;
	private View view;
	private final static String TAG = "ShowFragment2";

	/**
	 * @功能 生成ShowFragment2的实例
	 * @return 新实例
	 */
	public static ShowFragment2 newInstance() {
		return new ShowFragment2();
	}

	/**
	 * @功能 初始化系界面
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_personal_show2, container,
				false);
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		btn_curunit = (ImageView) view.findViewById(R.id.unitshow_btn_curunit);
		btn_curclass = (ImageView) view
				.findViewById(R.id.unitshow_btn_curclass);
		btn_local = (ImageView) view.findViewById(R.id.unitshow_btn_curlocal);
		btn_att = (ImageView) view.findViewById(R.id.unitshow_btn_att);
		btn_all = (ImageView) view.findViewById(R.id.unitshow_btn_all);
		listView = (CusListView) view.findViewById(R.id.unitshow_listview);
		return view;
	}

	/**
	 * @功能 界面相关处理
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		try {
			ShowFragmentController.getInstance().setContext(this);
			initViews();
			setOnClickListeners();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onViewCreated(view, savedInstanceState);
	}

	/**
	 * 设置Button监听事件
	 */
	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		btn_curunit.setOnClickListener(this);
		btn_curclass.setOnClickListener(this);// 本班
		btn_local.setOnClickListener(this);// 本地
		btn_att.setOnClickListener(this);// 关注
		btn_all.setOnClickListener(this);// 全部
	}

	/**
	 * @功能 界面初始化 焦点获取 加载数据 Adapter等
	 */
	private void initViews() {
		TextView title = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		title.setFocusable(true);
		title.setFocusableInTouchMode(true);
		title.requestFocus();
		title.requestFocusFromTouch();
		setSelectIndicator(pos_all);
		mPullRefreshScrollView.setOnRefreshListener(this);
		adapter = new Show2ArtListAdapter(getActivity());
		adapter.setData(allUnitArtList, clickType);
		listView.setAdapter(adapter);
		adapter.setParentView(getActivity().findViewById(
				R.id.fragment_personal_show2));
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			// 有单位
			ShowingUnitArthListAll();
		} else {
			// 无单位
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}

	}

	/**
	 * 设置Tab背景
	 * 
	 * @param position
	 */
	private void setSelectIndicator(int position) {
		try {
			btn_curunit.setImageResource(R.drawable.icon_show_curunit_unselect);
			btn_curclass
					.setImageResource(R.drawable.icon_show_curclass_unselect);
			btn_local.setImageResource(R.drawable.icon_show_local_unselect);
			btn_att.setImageResource(R.drawable.icon_show_att_unselect);
			btn_all.setImageResource(R.drawable.icon_show_all_unselect);
			// 选中状态
			switch (position) {
			case pos_curunit:
				btn_curunit
						.setImageResource(R.drawable.icon_show_curunit_selected);
				break;
			case pos_curclass:
				btn_curclass
						.setImageResource(R.drawable.icon_show_curclass_selected);
				break;
			case pos_local:
				btn_local.setImageResource(R.drawable.icon_show_local_selected);
				break;
			case pos_att:
				btn_att.setImageResource(R.drawable.icon_show_att_selected);
				break;
			case pos_all:
				btn_all.setImageResource(R.drawable.icon_show_all_selected);
				break;

			default:
				break;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

	}

	/**
	 * 本单位成果展示
	 */
	private void getUnitShowArt() {
		unitID = BaseActivity.sp.getInt("UnitID", 0);
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {

			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);

			ShowFragmentController.getInstance().UnitArthListIndex();
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 本单位活动分享
	 */
	private void getPersonShowArt() {
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {

			if (curPageNum > 1) {
				DialogUtil.getInstance().getDialog(
						getActivity(),
						getActivity().getResources().getString(
								R.string.public_loading));

				DialogUtil.getInstance().setCanCancel(false);
			}
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(curPageNum));
			ShowFragmentController.getInstance().PersonArthListIndex(params);
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 最新单位栏目文章 本地
	 */
	private void ShowingUnitArthListLocal() {
		unitID = BaseActivity.sp.getInt("UnitID", 0);
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(localPageNum));
			params.addBodyParameter("topFlags", String.valueOf(1));
			params.addBodyParameter("flag", "local");
			ShowFragmentController.getInstance().ShowingUnitArthListLocal(
					params);
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 最新单位栏目文章 全部
	 */
	private void ShowingUnitArthListAll() {
		unitID = BaseActivity.sp.getInt("UnitID", 0);
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(allPageNum));
			params.addBodyParameter("topFlags", String.valueOf(1));
			ShowFragmentController.getInstance().ShowingUnitArthList(params);
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 关注的
	 */
	private void MyAttUnitArthListIndex() {
		unitID = BaseActivity.sp.getInt("UnitID", 0);
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {

			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
			RequestParams params = new RequestParams();
			params.addBodyParameter("pageNum", String.valueOf(attPageNum));
			params.addBodyParameter("accId",
					BaseActivity.sp.getString("JiaoBaoHao", ""));
			ShowFragmentController.getInstance().MyAttUnitArthListIndex(params);
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 我的班级文章列表 1个人发布文章，2单位动态
	 */
	private void AllMyClassArthList(int sectionFlag) {
		unitID = BaseActivity.sp.getInt("UnitID", 0);
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			DialogUtil.getInstance().getDialog(
					getActivity(),
					getActivity().getResources().getString(
							R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
			RequestParams params = new RequestParams();
			if (sectionFlag == 1) {
				params.addBodyParameter("pageNum", String.valueOf(classPageNum));
			} else if (sectionFlag == 2) {
				DialogUtil.getInstance().getDialog(
						getActivity(),
						getActivity().getResources().getString(
								R.string.public_loading));
				params.addBodyParameter("pageNum", "1");
				params.addBodyParameter("numPerPage", "1");
			}
			params.addBodyParameter("sectionFlag", String.valueOf(sectionFlag));
			ShowFragmentController.getInstance().AllMyClassArthList(params,
					sectionFlag);
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
		}
	}

	/**
	 * 点击监听接口
	 * 
	 * @功能 title的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.unitshow_btn_curunit:
			// 本单位
			MobclickAgent.onEvent(getActivity(),
					getResources().getString(R.string.ShowFragment2_curunit));
			setSelectIndicator(pos_curunit);
			if (clickType != ShowClickType.curunit) {
				clickType = ShowClickType.curunit;
				adapter.setData(curUnitArtList, clickType);
				if (curUnitArtList.size() == 0) {
					curPageNum = 1;
					curUnitHaveMore = true;
					getUnitShowArt();
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.unitshow_btn_curclass:
			// 本班
			MobclickAgent.onEvent(getActivity(),
					getResources().getString(R.string.ShowFragment2_curclass));
			setSelectIndicator(pos_curclass);
			if (clickType != ShowClickType.curclass) {
				clickType = ShowClickType.curclass;
				adapter.setData(curClassArtList, clickType);
				if (curClassArtList.size() == 0) {
					classPageNum = 1;
					AllMyClassArthList(2);
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.unitshow_btn_curlocal:
			// 本地
			MobclickAgent.onEvent(getActivity(),
					getResources().getString(R.string.ShowFragment2_curlocal));
			setSelectIndicator(pos_local);
			if (clickType != ShowClickType.curlocal) {
				clickType = ShowClickType.curlocal;
				adapter.setData(localUnitArtList, clickType);
				if (localUnitArtList.size() == 0) {
					localPageNum = 1;
					ShowingUnitArthListLocal();
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.unitshow_btn_att:
			// 关注
			MobclickAgent.onEvent(getActivity(),
					getResources().getString(R.string.ShowFragment2_att));
			setSelectIndicator(pos_att);
			if (clickType != ShowClickType.att) {
				clickType = ShowClickType.att;
				adapter.setData(attUnitArtList, clickType);
				if (attUnitArtList.size() == 0) {
					attPageNum = 1;
					MyAttUnitArthListIndex();
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.unitshow_btn_all:
			// 全部
			MobclickAgent.onEvent(getActivity(),
					getResources().getString(R.string.ShowFragment2_all));
			setSelectIndicator(pos_all);
			if (clickType != ShowClickType.all) {
				clickType = ShowClickType.all;
				adapter.setData(allUnitArtList, clickType);
				if (allUnitArtList.size() == 0) {
					allPageNum = 1;
					ShowingUnitArthListAll();
				} else {
					adapter.notifyDataSetChanged();
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		MobclickAgent.onPageEnd(TAG);
		super.onPause();
	}

	@Override
	public void onDetach() {
		EventBusUtil.unregister(this);
		commitAdapter = null;
		super.onDetach();
	}

	@Override
	public void onAttach(Activity activity) {
		EventBusUtil.register(this);
		super.onAttach(activity);
	}

	Timer timer = new Timer();
	private boolean haveClass = true;
	private boolean haveCurUnit = true;

	/**
	 * EventBus功能模块
	 * 
	 * @功能 获取网络请求返回数据并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work_change:// 切换单位成功！这里要把数据全部清空，并重新获取当前页数据
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			boolean b = (Boolean) list.get(1);
			if (b) {
				haveClass = true;
				haveCurUnit = true;
				curUnitArtList.clear();
				curPageNum = 1;
				curUnitHaveMore = true;
				curClassArtList.clear();
				classPageNum = 1;
				curClassHaveMore = true;
				localPageNum = 1;
				localHaveMore = true;
				localUnitArtList.clear();
				allPageNum = 1;
				allHaveMore = true;
				allUnitArtList.clear();
				attPageNum = 1;
				attHaveMore = true;
				attUnitArtList.clear();
				switch (clickType) {
				case curunit:
					getUnitShowArt();
					break;
				case curclass:
					AllMyClassArthList(2);
					break;
				case curlocal:
					ShowingUnitArthListLocal();
					break;
				case all:
					ShowingUnitArthListAll();
					break;
				case att:
					MyAttUnitArthListIndex();
					break;
				default:
					break;
				}
			}
			break;
		case Constant.msgcenter_show_UnitArthListIndex:// 本单位第一条
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> unitShow = (ArrayList<ArthInfo>) list.get(1);
			if (unitShow != null && unitShow.size() > 0) {
				curUnitArtList.add(0, unitShow.get(0));
				haveCurUnit = true;
			} else {
				curUnitArtList.add(0, null);
				haveCurUnit = false;
			}
			getPersonShowArt();
			break;
		case Constant.msgcenter_show_PersonArthListIndex:// 本单位分享列表
			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> personShow = (ArrayList<ArthInfo>) list.get(1);
			if (personShow != null && personShow.size() > 0) {
				curUnitArtList.addAll(personShow);
				if (personShow.size() == 5) {
					curUnitHaveMore = true;
				} else {
					curUnitHaveMore = false;
				}
			} else {
				if (!haveCurUnit && curPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_noArtical);
				}
				curUnitArtList.add(1, null);
				curUnitHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_show_AllMyClassArthList_2:// 本班第一条
			mPullRefreshScrollView.onRefreshComplete();
			DialogUtil.getInstance().cannleDialog();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> classShow = (ArrayList<ArthInfo>) list.get(1);
			if (classShow != null && classShow.size() > 0) {
				curClassArtList.add(0, classShow.get(0));
				haveClass = true;
			} else {
				curClassArtList.add(0, null);
				haveClass = false;
			}
			AllMyClassArthList(1);
			break;
		case Constant.msgcenter_show_AllMyClassArthList_1:// 本班分享列表

			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> personClass = (ArrayList<ArthInfo>) list.get(1);
			if (personClass != null && personClass.size() > 0) {
				curClassArtList.addAll(personClass);
				if (personClass.size() == 5) {
					curClassHaveMore = true;
				} else {
					curClassHaveMore = false;
				}
			} else {
				if (!haveClass && classPageNum == 1) {
					ToastUtil
							.showMessage(
									getActivity(),
									R.string.temporarily_noRelatedClass_orClassNoArtical);
				}
				curClassArtList.add(1, null);
				curClassHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_show_ShowingUnitArthListLocal:// 获取本地文章列表-本地
			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> arthLocal = (ArrayList<ArthInfo>) list.get(1);
			if (arthLocal != null && arthLocal.size() > 0) {
				localUnitArtList.addAll(arthLocal);
				if (arthLocal.size() == 5) {
					localHaveMore = true;
				} else {
					localHaveMore = false;
				}
			} else {
				if (localPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.local_temporarily_noArtical);
				}
				localHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			// DialogUtil.getInstance().cannleDialog();
			timer.schedule(new TimerTask() {
				public void run() {
					DialogUtil.getInstance().cannleDialog();
				}
			}, 1000);
			break;
		case Constant.msgcenter_show_ShowingUnitArthListAll:// 获取本地文章列表-全部
			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> arthAll = (ArrayList<ArthInfo>) list.get(1);
			if (arthAll != null && arthAll.size() > 0) {
				allUnitArtList.addAll(arthAll);
				if (arthAll.size() == 5) {
					allHaveMore = true;
				} else {
					allHaveMore = false;
				}
			} else {
				if (localPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.temporarily_sharedArtical);
				}
				allHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			// DialogUtil.getInstance().cannleDialog();
			timer.schedule(new TimerTask() {
				public void run() {
					DialogUtil.getInstance().cannleDialog();
				}
			}, 1000);
			break;
		case Constant.msgcenter_show_MyAttUnitArthListIndex:// 取我关注的单位栏目文章
			mPullRefreshScrollView.onRefreshComplete();
			@SuppressWarnings("unchecked")
			ArrayList<ArthInfo> arthAtt = (ArrayList<ArthInfo>) list.get(1);
			if (arthAtt != null && arthAtt.size() > 0) {
				attUnitArtList.addAll(arthAtt);
				if (arthAtt.size() == 5) {
					attHaveMore = true;
				} else {
					attHaveMore = false;
				}
			} else {
				if (attPageNum == 1) {
					ToastUtil.showMessage(getActivity(),
							R.string.noFollow_orNoArtical);
				}
				attHaveMore = false;
			}
			adapter.notifyDataSetChanged();
			// DialogUtil.getInstance().cannleDialog();
			timer.schedule(new TimerTask() {
				public void run() {
					DialogUtil.getInstance().cannleDialog();
				}
			}, 1000);
			break;
		case Constant.msgcenter_articlelist_addComment:
			// 评论结果
			DialogUtil.getInstance().cannleDialog();
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * 下拉刷新
	 * 
	 * @功能 更新数据
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		MobclickAgent.onEvent(getActivity(),
				getResources().getString(R.string.ShowFragment2_refresh));
		dataRefresh();

	}

	/**
	 * @功能 刷新数据
	 */
	private void dataRefresh() {
		// TODO Auto-generated method stub
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			switch (clickType) {
			case curunit:
				curUnitArtList.clear();
				curPageNum = 1;
				curUnitHaveMore = true;
				getUnitShowArt();
				break;
			case curclass:
				haveClass = true;
				curClassArtList.clear();
				classPageNum = 1;
				curClassHaveMore = true;
				AllMyClassArthList(2);
				break;
			case curlocal:
				localPageNum = 1;
				localHaveMore = true;
				localUnitArtList.clear();
				ShowingUnitArthListLocal();
				break;
			case all:
				allPageNum = 1;
				allHaveMore = true;
				allUnitArtList.clear();
				ShowingUnitArthListAll();
				break;
			case att:
				attPageNum = 1;
				attHaveMore = true;
				attUnitArtList.clear();
				MyAttUnitArthListIndex();
				break;
			default:
				break;
			}
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	/**
	 * 上拉加载
	 * 
	 * @功能 加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		MobclickAgent.onEvent(getActivity(),
				getResources().getString(R.string.ShowFragment2_loadMore));
		if (BaseActivity.sp.getInt("UnitID", 0) != 0) {
			switch (clickType) {
			case curunit:
				if (curUnitHaveMore) {
					curPageNum++;
					getPersonShowArt();
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case curclass:
				if (curClassHaveMore) {
					classPageNum++;
					AllMyClassArthList(1);
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case curlocal:
				if (localHaveMore) {
					localPageNum++;
					ShowingUnitArthListLocal();
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case all:
				if (allHaveMore) {
					allPageNum++;
					ShowingUnitArthListAll();
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			case att:
				if (attHaveMore) {
					attPageNum++;
					MyAttUnitArthListIndex();
				} else {
					mPullRefreshScrollView.onRefreshComplete();
					ToastUtil.showMessage(getActivity(), R.string.no_more);
				}
				break;
			default:
				break;
			}
		} else {
			ToastUtil.showMessage(getActivity(), R.string.public_error_nounit);
			mPullRefreshScrollView.onRefreshComplete();
		}
	}

	/**
	 * 声明周期事件
	 * 
	 * @功能 销毁时清空数据
	 */
	@Override
	public void onDestroy() {
		haveClass = true;
		haveCurUnit = true;
		curUnitArtList.clear();
		curPageNum = 1;
		curUnitHaveMore = true;
		curClassArtList.clear();
		classPageNum = 1;
		curClassHaveMore = true;
		localPageNum = 1;
		localHaveMore = true;
		localUnitArtList.clear();
		allPageNum = 1;
		allHaveMore = true;
		allUnitArtList.clear();
		attPageNum = 1;
		attHaveMore = true;
		attUnitArtList.clear();
		super.onDestroy();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub
	}
}