package com.jsy_jiaobao.main.workol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.StuInfo;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.workol.ErrorModel;
import com.jsy_jiaobao.po.workol.StuErrDetailModel;
import com.jsy_jiaobao.po.workol.StuErrorModel;
import com.jsy_jiaobao.po.workol.StuHW;
import com.jsy_jiaobao.po.workol.StudentErrorPost;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.jsy_jiaobao.po.workol.UnionChapterList;
import com.umeng.analytics.MobclickAgent;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

public class StudentWorkActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ScrollView> {
	// private final static String TAG="StudentWorkActivity";
	/** 年级 */
	final static int TYPE_GRADE = 0;
	/** 科目 */
	final static int TYPE_MODE = 1;
	/** 教版 */
	final static int TYPE_SUBJECT = 2;
	/** 章节 */
	final static int TYPE_SESSION = 3;
	/** 标题 */
//	final static int TYPE_TITLE = 4;
	private Context mContext;// 上下文
	private PullToRefreshScrollView mPullRefreshScrollView;// scrollView
	private TextView tv_work;// 作业选项
	private TextView tv_exercise;// 练习选项
	private TextView tv_nowork;// 没作业背景
	private TextView tv_queryEx;// 查询练习
	private TextView tv_queryErr;// 筛选结果显示界面
	// private TextView tv_screenErr;
	private CusListView listView;
	private LinearLayout ly_pubEx;
	private ViewGroup layout_tree;
	private Button btn_make;// 做练习按钮
	private AndroidTreeView tView;
	private TreeNode root;
	private StuInfo stuInfo;// 学生信息
	private ErrorCheckAdapter errorAdapter;
	private boolean isHW = true;
	private StuHWListAdapter<StuHW> workAdapter;// 学生作业Adapter
	private int IsSelf = 0;// : 0=作业,1=练习;2=查看练习
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
			Locale.getDefault());// 日期格式
	private int chapterId = -1;// 默认的错题本章节为全部=-1
	private StudentErrorPost errorPost;// 查询错题本接口所需数据
	private LinearLayout ly_big_screen;
	private int pageIndex = 1;// 起始页
	private final static int TYPE_WORK = 10;// 当前作业
	private final static int TYPE_DOEX = 11;// 做练习
	private final static int TYPE_QREX = 12;// 查询练习
	private final static int TYPE_ERRO = 13;// 查询错题本
	private int mType;
	private int exPageIndex = 1;// 请求页码 默认为起始页=1
	private boolean isFinishWork;// 是否完成工作
	private TextView tv_selectName;// 显示筛选条件的空间
	private ArrayList<StuHW> stuExList; // 练习列表数据 = new ArrayList<StuHW>();
	private ArrayList<StuErrorModel> getErrList;// 错题本列表数据
	private ArrayList<ErrorModel> errArrayList;// 错题本列表数据
	private int totalPageNum = 0;// 总页数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			stuInfo = (StuInfo) savedInstanceState.getSerializable("stuInfo");
		}
		initView();
		mType = TYPE_WORK;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("stuInfo", stuInfo);
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentLayout(R.layout.activity_workolstu);
		mContext = this;
		setActionBarTitle(R.string.my_homework);
		workAdapter = new StuHWListAdapter<>(mContext);
		errorAdapter = new ErrorCheckAdapter(mContext);
		stuExList = new ArrayList<>();
		errArrayList = new ArrayList<>();
		errorPost = new StudentErrorPost();
		StudentWorkActivityController.getInstance().setContext(this);
		tv_work = (TextView) findViewById(R.id.workolstu_tv_work);
		tv_exercise = (TextView) findViewById(R.id.workolstu_tv_exercise);
		RelativeLayout ly_screen;// 筛选
		ly_screen = (RelativeLayout) findViewById(R.id.ly_screen);
		ly_pubEx = (LinearLayout) findViewById(R.id.ly_publish_ex);
		tv_selectName = (TextView) findViewById(R.id.tv_selectName);
		tv_queryEx = (TextView) findViewById(R.id.workolstu_tv_query_exercise);
		tv_queryErr = (TextView) findViewById(R.id.workolstu_tv_query_error);
		tv_nowork = (TextView) findViewById(R.id.workolstu_bg);
		listView = (CusListView) findViewById(R.id.workolstu_listview);
		layout_tree = (ViewGroup) findViewById(R.id.workolstu_tree);
		btn_make = (Button) findViewById(R.id.workolstu_btn_make);
		// tv_screenErr = (TextView) findViewById(R.id.tv_query_error_screen);
		ly_big_screen = (LinearLayout) findViewById(R.id.big_ly_screen);
		// ly_webs = (LinearLayout) findViewById(R.id.ly_webs);
		ly_screen.setOnClickListener(this);
		// tv_screenErr.setOnClickListener(this);
		btn_make.setVisibility(View.GONE);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(this);
		mPullRefreshScrollView.setMode(Mode.BOTH);
		root = TreeNode.root();
		btn_make.setClickable(true);
		btn_make.setOnClickListener(this);
		layout_tree.setVisibility(View.GONE);
		listView.setAdapter(workAdapter);
		tv_work.setOnClickListener(this);
		setViewFocusable(tv_work);
		tv_exercise.setOnClickListener(this);
		tv_queryEx.setOnClickListener(this);
		tv_queryErr.setOnClickListener(this);
		String jiaobaohao = sp.getString("JiaoBaoHao", "");
		/**
		 * 根据教宝号获取学生信息
		 */
		if (!"".equals(jiaobaohao)) {
			if (Constant.listUserIdentity != null) {
				for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
					UserIdentity identity = Constant.listUserIdentity.get(i);
					if (identity.getRoleIdentity() == 4) {
						List<UserClass> list = identity.getUserClasses();
						if (list != null && list.size() > 0) {
							UserClass userClass = list.get(0);
							StudentWorkActivityController.getInstance()
									.getStuInfo(jiaobaohao,
											userClass.getClassID());

						}
					}
				}
			}
		}
	}

	private String slect(String content) {
		if (!TextUtils.isEmpty(content)) {
			if (content.startsWith(" ")) {
				content = content.substring(1);
				content = slect(content);
			}
			if (content.endsWith(" ")) {
				content = content.substring(0, content.length() - 1);
				content = slect(content);
			}
		}
		return content;

	}

	private void setDefaultFocusable() {
		tv_exercise.setFocusable(false);
		tv_queryEx.setFocusable(false);
		tv_work.setFocusable(false);
		tv_queryErr.setFocusable(false);
	}

	private void setViewFocusable(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	/**
	 * 各种控件的点击事件监听
	 */

	@Override
	public void onClick(View v) {

		ly_big_screen.setVisibility(View.GONE);
		ly_pubEx.setVisibility(View.GONE);
		switch (v.getId()) {
		// 作业
		case R.id.workolstu_tv_work:
			MobclickAgent
					.onEvent(
							mContext,
							getResources().getString(
									R.string.StudentWorkActivity_work));
			mType = TYPE_WORK;
			listView.setAdapter(workAdapter);
			setDefaultFocusable();
			changeData(0, 0);
			break;
		// 练习
		case R.id.workolstu_tv_exercise:
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.StudentWorkActivity_exercise));
			if (isFinishWork) {
				setDefaultFocusable();
				listView.setAdapter(workAdapter);
				mType = TYPE_DOEX;
				changeData(0, 0);
				//
			} else {
				ToastUtil.showMessage(mContext,
						R.string.no_freedomExercise_workUnDo);
			}
			break;
		// 查询练习
		case R.id.workolstu_tv_query_exercise:
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.StudentWorkActivity_exerciseQuery));
			mType = TYPE_QREX;
			stuExList.clear();
			exPageIndex = 1;
			listView.setAdapter(workAdapter);
			setDefaultFocusable();
			changeData(0, 0);
			break;
		// 查询错题本
		case R.id.workolstu_tv_query_error:
			MobclickAgent.onEvent(mContext,
					getResources()
							.getString(R.string.StudentWorkActivity_error));
			errArrayList.clear();
			setDefaultFocusable();
			mType = TYPE_ERRO;
			listView.setAdapter(errorAdapter);
			pageIndex = 1;
			changeData(0, 0);
			break;
		// 筛选
		case R.id.ly_screen:
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.StudentWorkActivity_screen));
			pageIndex = 1;
			mType = TYPE_ERRO;
			listView.setAdapter(errorAdapter);
			ly_big_screen.setVisibility(View.VISIBLE);
			// listView.setVisibility(View.GONE);
			Intent intent = new Intent(this, ErrorScreenActivity.class);
			intent.putExtra("role", 0);
			intent.putExtra("stuInfo", stuInfo);
			startActivityForResult(intent, 56);
			break;
		// 做练习按钮
		case R.id.workolstu_btn_make:
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.StudentWorkActivity_makeButton));
			mType = TYPE_DOEX;
			ly_pubEx.setVisibility(View.VISIBLE);
			setViewFocusable(tv_exercise);
			listView.setAdapter(workAdapter);
			boolean could = true;
			String name = "";
			int Unid = 0;
			int chapterID = 0;
			String homeworkName = "";
			for (int i = 0; i < root.getChildren().size(); i++) {
				TreeNode parent = root.getChildren().get(i);
				TreeItemRoot value = (TreeItemRoot) parent.getValue();
				if (value.TabID == 0) {
					could = false;
					name = value.select;
					break;
				}
				if (i == 2) {
					Unid = value.TabID;
				} else if (i == 3) {
					chapterID = value.TabID;
				} else if (i == 4) {
					homeworkName = slect(value.select);
				}
			}
			// 把作业名称中的<>转换为全角字符
			homeworkName = BaseUtils.startendNospacing(homeworkName)
					.replaceAll("<", "＜").replaceAll(">", "＞");
			// 学生信息不为空
			if (stuInfo != null) {
				// 有题
				if (haveTecQs) {
					// 能发布
					if (could) {

						if (!TextUtils.isEmpty(homeworkName.trim())) {
							// 如果标题长度小于6 或大于49
							if (homeworkName.length() < 6
									|| homeworkName.length() > 49) {
								// 提示
								ToastUtil.showMessage(mContext,
										R.string.exercise_title_length);
								// 否则，发布作业
							} else {
								StudentWorkActivityController.getInstance()
										.StuMakeSelf(stuInfo.getStudentID(),
												stuInfo.getUnitClassID(),
												stuInfo.getClassName(), Unid,
												chapterID, homeworkName);
								btn_make.setClickable(false);
							}
						} else {
							ToastUtil.showMessage(mContext,
									R.string.input_workTitle_please);
						}
					} else {
						ToastUtil.showMessage(mContext, name);
					}
				} else {
					ToastUtil.showMessage(
							mContext,
							getResources().getString(
									R.string.workol_session_nocontent));
				}
			} else {
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.error_indenity_get));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 当用户点击不同控件时 切换数据
	 * 
	 * @param modeId s
	 * @param chapterid s
	 */
	private void changeData(int modeId, int chapterid) {
		int pageSize = 10;// 每页请求数据条数
		if (stuInfo != null) {
			switch (mType) {
			case TYPE_WORK:
				IsSelf = 0;
				layout_tree.setVisibility(View.GONE);
				btn_make.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				isHW = true;
				workAdapter.setIsLook(false);
				setViewFocusable(tv_work);
				StudentWorkActivityController.getInstance().GetStuHWList(
						stuInfo.getStudentID(), IsSelf);
				break;
			case TYPE_DOEX:
				tv_nowork.setVisibility(View.GONE);
				ly_pubEx.setVisibility(View.VISIBLE);
				isHW = false;
				workAdapter.setIsLook(false);
				IsSelf = 1;
				workAdapter.setIsSelf(IsSelf);
				setViewFocusable(tv_exercise);
				StudentWorkActivityController.getInstance().GetStuHWList(
						stuInfo.getStudentID(), IsSelf);

				break;
			case TYPE_QREX:
				IsSelf = 1;
				isHW = false;
				setViewFocusable(tv_queryEx);
				StudentWorkActivityController.getInstance().GetStuHWListPage(
						stuInfo.getStudentID(), IsSelf, exPageIndex, pageSize);

				break;
			case TYPE_ERRO:
				/**
				 * 获取错题本
				 * 
				 */
				ly_big_screen.setVisibility(View.VISIBLE);
				setViewFocusable(tv_queryErr);
				errorPost.setPageIndex(pageIndex);
				errorPost.setPageSize(pageSize);
				errorPost.setGradeCode(GradeCode);
				errorPost.setSubjectCode(subCode);
				errorPost.setUnid(this.modeId);
				errorPost.setChapterid(chapterId);
				errorPost.setStuId(stuInfo.getStudentID());
				errorPost.setIsSelf(IsSelf);
				StudentWorkActivityController.getInstance()
						.GetStuErr(errorPost);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 获取启动Activity的返回值 ,获取筛选条件
	 * @param arg0 e
	 * @param arg1 d
	 * @param arg2 d
     */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		setViewFocusable(tv_queryErr);
		mType = TYPE_ERRO;
		switch (arg1) {
		case 999:
			Bundle args = arg2.getExtras();
			GradeCode = args.getInt("GradeCode");
			subCode = args.getInt("SubCode");
			modeId = args.getInt("ModeId");
			chapterId = args.getInt("ChapterId");
			String mSelect;// 选中的筛选条件的String值
			mSelect = (String) args.get("Select");
			Log.d("chapterId", chapterId + "");
			// ly_webs.removeAllViews();
			if (mSelect != null) {
				tv_selectName.setText(mSelect);
			}
			/**
			 * 根据 返回值更新数据
			 */
			if (chapterId != 0) {
				pageIndex = 1;
				errArrayList.clear();
				errorAdapter.notifyDataSetChanged();
				changeData(0, 0);
			}
			break;

		default:
			ToastUtil.showMessage(mContext, "你选的没题");
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		refreshData();
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	/**
	 * 错题本有两组数据，数据处理比较慢，中间休息了2秒
	 * 
	 * @Handler @Thread 构成其功能
	 */

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 99:
				DialogUtil.getInstance().cannleDialog();
				thread.interrupt();
				thread = null;
				break;

			default:
				break;
			}
		}
	};
	private Thread thread;

	private void createThread() {
		// TODO Auto-generated method stub
		if (thread == null) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(2000);
						handler.sendEmptyMessage(99);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}

	int questionNo = 0;

	/**
	 * 处理返回的数据
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		// 错题本
		case Constants.WORKOL_GetStuErr:
			mPullRefreshScrollView.onRefreshComplete();
			getErrList = (ArrayList<StuErrorModel>) list.get(1);
			if (getErrList.size() == 0 || getErrList == null) {
				DialogUtil.getInstance().cannleDialog();
				totalPageNum = 0;
				if (pageIndex == 1) {
					DialogUtil.getInstance().cannleDialog();
					tv_nowork.setVisibility(View.VISIBLE);
					ToastUtil.showMessage(mContext, "没有错题");
				} else if (pageIndex > 1) {
					ToastUtil.showMessage(mContext, "没有更多了");
				}
			} else {
				totalPageNum = Integer.valueOf(getErrList.get(0).getTabIDStr());
				if (pageIndex == 1) {
					errArrayList.clear();
					// ly_webs.removeAllViews();
				}
				tv_nowork.setVisibility(View.GONE);
				questionNo = 0;
				StuErrorModel stuErrorModel = getErrList.get(questionNo);
				StudentWorkActivityController.getInstance().GetStuHWQs(0,
						stuErrorModel.getQsID(), stuErrorModel);

			}
			break;
		// 错题本详情
		case Constants.WORKOL_GetStuHWQs:
			mPullRefreshScrollView.onRefreshComplete();
			questionNo++;
			StuErrDetailModel qsPack = (StuErrDetailModel) list.get(1);
			StuErrorModel stuErrorModel = (StuErrorModel) list.get(2);
			ErrorModel errorModel = new ErrorModel(mContext);
			errorModel.setStuErrDetailModel(qsPack);
			errorModel.setStuErrorModel(stuErrorModel);
			errArrayList.add(errorModel);
			if (questionNo < getErrList.size()) {
				StuErrorModel stuErrorModel1 = getErrList.get(questionNo);
				StudentWorkActivityController.getInstance().GetStuHWQs(0,
						stuErrorModel1.getQsID(), stuErrorModel1);
			} else {
				errorAdapter.setErrorModels(errArrayList);
				errorAdapter.notifyDataSetChanged();
				createThread();
				thread.start();
				getErrList.clear();
			}
			break;
		// 学生信息
		case Constants.WORKOL_getStuInfo:
			mPullRefreshScrollView.onRefreshComplete();
			stuInfo = (StuInfo) list.get(1);
			if (stuInfo != null) {
				errorPost.setStuId(stuInfo.getStudentID());
				StudentWorkActivityController.getInstance().GetStuHWList(
						stuInfo.getStudentID(), IsSelf);
			} else {
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.error_indenity_get));
			}
			break;
		// 练习列表
		case Constants.WORKOL_GetStuHWListPage:
			DialogUtil.getInstance().cannleDialog();
			mPullRefreshScrollView.onRefreshComplete();
			ArrayList<StuHW> getStuExList = (ArrayList<StuHW>) list.get(1);
			layout_tree.setVisibility(View.GONE);
			btn_make.setVisibility(View.GONE);
			if (getStuExList == null || getStuExList.size() == 0) {
				totalPageNum = 0;
				if (exPageIndex == 1) {
					tv_nowork.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					ToastUtil.showMessage(mContext, "暂无练习列表");
				} else {
					tv_nowork.setVisibility(View.GONE);
					ToastUtil.showMessage(mContext, "没有更多了");
				}
			} else {
				totalPageNum = Integer.valueOf(getStuExList.get(0)
						.getTabIDStr());
				stuExList.addAll(getStuExList);
				tv_nowork.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				workAdapter.setIsLook(true);
				workAdapter.setmData(stuExList);
				workAdapter.setIsSelf(IsSelf);
				workAdapter.notifyDataSetChanged();

			}

			break;
		// 作业礼拜哦
		case Constants.WORKOL_GetStuHWList:
			DialogUtil.getInstance().cannleDialog();
			mPullRefreshScrollView.onRefreshComplete();
			ArrayList<StuHW> getStuHWList = (ArrayList<StuHW>) list.get(1);
			btn_make.setClickable(true);
			if (isHW) {
				if (getStuHWList == null || getStuHWList.size() == 0) {
					tv_nowork.setVisibility(View.VISIBLE);
				} else {
					tv_nowork.setVisibility(View.GONE);
				}
				listView.setVisibility(View.VISIBLE);
				layout_tree.setVisibility(View.GONE);
				btn_make.setVisibility(View.GONE);
				workAdapter.setmData(getStuHWList);
				workAdapter.setIsSelf(IsSelf);
				workAdapter.notifyDataSetChanged();
				isFinishWork = workAdapter.getFinishFlag();
			} else {
				if (getStuHWList == null || getStuHWList.size() == 0) {

					listView.setVisibility(View.GONE);
					StudentWorkActivityController.getInstance().GetGradeList();
				} else {
					listView.setVisibility(View.VISIBLE);
					layout_tree.setVisibility(View.GONE);
					btn_make.setVisibility(View.GONE);
					workAdapter.setmData(getStuHWList);
					workAdapter.setIsSelf(IsSelf);
					workAdapter.notifyDataSetChanged();
				}
			}
			break;
		// 年级列表
		case Constants.WORKOL_GetGradeList:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<TeaGrade> glist = (ArrayList<TeaGrade>) list.get(1);

			if (glist != null && glist.size() > 0) {
				/** 删除所有child */
				for (int i = root.getChildren().size() - 1; i >= 0; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				TeaGrade t1 = glist.get(0);
				TreeNode computerRoot = new TreeNode(new TreeItemRoot(
						getResources()
								.getString(R.string.workol_selected_grade), t1))
						.setViewHolder(new TreeItemRootHolder(this, mHandler));
				for (TeaGrade teaGrade : glist) {
					TreeNode select = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_selected_grade), teaGrade))
							.setViewHolder(new TreeItemSelectHolder(this,
									mHandler));
					computerRoot.addChild(select);
				}
				if (tView != null) {
					tView.addNode(root, computerRoot);
				} else {
					root.addChild(computerRoot);
				}
				GradeCode = t1.getGradeCode();
				StudentWorkActivityController.getInstance()
						.GetUnionChapterList(TYPE_GRADE, GradeCode, 0, 0, 0);
			}

			break;
		/**
		 * <pre>
		 * 取出联动效果
		 * "gCode">年级代码
		 * "subCode">科目代码
		 * "uId">教版联动代码
		 * "flag">0： 根据年级获取科目，1：根据科目获取教版，2： 根据所有获取UID
		 * return
		 * Args1 为科目列表数据
		 * Args2为教版列表
		 * Args3为章节列表
		 * statusCode=200表示成功
		 */
		case Constants.WORKOL_GetUnionChapterList:
			DialogUtil.getInstance().cannleDialog();
			layout_tree.setVisibility(View.VISIBLE);
			btn_make.setVisibility(View.VISIBLE);
			DialogUtil.getInstance().cannleDialog();
			UnionChapterList union = (UnionChapterList) list.get(1);
			int typeMode = (Integer) list.get(2);
			ArrayList<TeaMode> args1 = union.getArgs1();// 科目列表数据
			ArrayList<TeaSubject> args2 = union.getArgs2();// 教版列表数据
			ArrayList<TeaSession> args3 = union.getArgs3();// 章节列表数据
			TeaSession selectSession = null;
			switch (typeMode) {
			// 年级
			case TYPE_GRADE:
				// 加载科目数据
				if (args1 != null && args1.size() > 0) {
					subCode = args1.get(0).getSubjectCode();
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_mode), args1.get(0)))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaMode teaMode : args1) {
						TreeNode select = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_mode), teaMode))
								.setViewHolder(new TreeItemSelectHolder(this,
										mHandler));
						computerRoot.addChild(select);
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(
							new TreeItemRoot(getResources().getString(
									R.string.workol_select_mode), new TeaMode()))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				// 加载教版数据
				if (args2 != null && args2.size() > 0) {
					modeId = args2.get(0).getTabID();
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_subject),
							args2.get(0), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaSubject teaSub : args2) {
						TreeNode select = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								teaSub, -1))
								.setViewHolder(new TreeItemSelectHolder(this,
										mHandler));
						computerRoot.addChild(select);
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_subject),
							new TeaSubject(), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				// 加载章节数据
				if (args3 != null && args3.size() > 0) {
					selectSession = args3.get(0);
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							selectSession, 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaSession session : args3) {
						if (session.getPid() == 0) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_session),
									session, 0))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							MakeSessionTree(select, session, args3, 1);
							computerRoot.addChild(select);
						}
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							new TeaSession(), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				break;
			// 科目
			case TYPE_MODE:
				// 加载教版数据
				if (args2 != null && args2.size() > 0) {
					modeId = args2.get(0).getTabID();
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_subject),
							args2.get(0), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaSubject teaSub : args2) {
						TreeNode select = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								teaSub, -1))
								.setViewHolder(new TreeItemSelectHolder(this,
										mHandler));
						computerRoot.addChild(select);
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_subject),
							new TeaSubject(), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				// 加载章节数据
				if (args3 != null && args3.size() > 0) {
					selectSession = args3.get(0);
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							selectSession, 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaSession session : args3) {
						if (session.getPid() == 0) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_session),
									session, 0))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							MakeSessionTree(select, session, args3, 1);
							computerRoot.addChild(select);
						}
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							new TeaSession(), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				break;
			// 教版
			case TYPE_SUBJECT:
				// 加载章节数据
				if (args3 != null && args3.size() > 0) {
					selectSession = args3.get(0);
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							selectSession, 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					for (TeaSession session : args3) {
						if (session.getPid() == 0) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_session),
									session, 0))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							MakeSessionTree(select, session, args3, 1);
							computerRoot.addChild(select);
						}
					}
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				} else {
					TreeNode computerRoot = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_select_session),
							new TeaSession(), 0))
							.setViewHolder(new TreeItemRootHolder(this,
									mHandler));
					if (tView != null) {
						tView.addNode(root, computerRoot);
					} else {
						root.addChild(computerRoot);
					}
				}
				break;
			default:
				break;
			}
			/**
			 * 选择章节不为空
			 */
			if (selectSession != null) {
				StudentWorkActivityController.getInstance().TecQs(
						selectSession.getTabID());
				TreeItemRoot subvalue = (TreeItemRoot) root.getChildren()
						.get(1).getValue();
				selectSession.setChapterName(formatter.format(new Date())
						+ subvalue.select + selectSession.getChapterName()
						+ "练习");
			}
			TreeNode computerRoot = new TreeNode(new TreeItemRoot(
					getResources().getString(R.string.workol_select_title),
					selectSession)).setViewHolder(new TreeItemHWTitleHolder(
					this));
			if (tView != null) {
				tView.addNode(root, computerRoot);
			} else {
				root.addChild(computerRoot);
			}
			if (tView == null) {
				if (root.getChildren().size() > 0) {

					tView = new AndroidTreeView(this, root);
					tView.setDefaultAnimation(false);
					layout_tree.addView(tView.getView());
					tView.setSelectionModeEnabled(true);
				}
			}
			break;
		// 判断有没题目
		case Constants.WORKOL_TecQs:
			String result = (String) list.get(1);
			if ("true".equals(result)) {
				haveTecQs = true;
			} else {
				ToastUtil.showMessage(
						mContext,
						getResources().getString(
								R.string.workol_session_nocontent));
				haveTecQs = false;
			}
			break;
		// 发布练习 收到的数据
		case Constants.WORKOL_StuMakeSelf:
			DialogUtil.getInstance().cannleDialog();
			String make = (String) list.get(1);
			if ("true".equals(make)) {
				// 有适合当前学力值的试题
				ToastUtil.showMessage(mContext,
						getResources()
								.getString(R.string.workol_makehw_success));
				if (stuInfo != null) {

					StudentWorkActivityController.getInstance().GetStuHWList(
							stuInfo.getStudentID(), IsSelf);
				}
			} else if ("okFalse".equals(make)) {
				// 没有合适的试题
				ToastUtil.showMessage(mContext, "根据您的当前学力值未抽选到合适的题目,无法生成试卷");
				btn_make.setClickable(true);
			} else {
				// 发布失败
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.workol_makehw_faild));
				btn_make.setClickable(true);

			}
			break;
		default:
			break;
		}
	}

	boolean haveTecQs = false;

	private void MakeSessionTree(TreeNode node, TeaSession psession,
			ArrayList<TeaSession> args3, int Padding) {
		for (TeaSession session : args3) {
			if (session.getPid() == psession.getTabID()) {
				TreeNode select = new TreeNode(new TreeItemRoot(getResources()
						.getString(R.string.workol_select_session), session,
						Padding)).setViewHolder(new TreeItemSelectHolder(this,
						mHandler));
				MakeSessionTree(select, session, args3, Padding + 1);
				node.addChild(select);
			}
		}
	}

	private int GradeCode = -1;
	private int subCode = -1;
	private int modeId = -1;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			TreeNode node = null;
			TreeItemRoot value;
			switch (msg.what) {
			// 年级
			case TYPE_GRADE:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				TreeNode parentGrade = root.getChildren().get(TYPE_GRADE);
				TreeItemRoot grade = (TreeItemRoot) parentGrade.getValue();
				// 选择年级
				grade.select = value.select;
				// 所选年级Id
				grade.TabID = value.TabID;
				parentGrade.getViewHolder().notifyValue(grade);
				tView.collapseNode(root.getChildren().get(TYPE_GRADE));
				List<TreeNode> list = root.getChildren().get(TYPE_GRADE)
						.getChildren();
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 移除以前的child
				for (int i = root.getChildren().size() - 1; i > TYPE_GRADE; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 年级ID
				GradeCode = value.TabID;
				// 通过年级Id 获取 科目 教版
				StudentWorkActivityController.getInstance()
						.GetUnionChapterList(TYPE_GRADE, GradeCode, 0, 0, 0);
				break;
			// 科目
			case TYPE_MODE:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				subCode = value.TabID;
				TreeNode parent = root.getChildren().get(TYPE_MODE);
				TreeItemRoot mode = (TreeItemRoot) parent.getValue();
				// 科目
				mode.select = value.select;
				// 选中科目Id
				mode.TabID = value.TabID;
				parent.getViewHolder().notifyValue(mode);
				tView.collapseNode(root.getChildren().get(TYPE_MODE));
				List<TreeNode> list1 = root.getChildren().get(TYPE_MODE)
						.getChildren();
				for (int i = 0; i < list1.size(); i++) {
					list1.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 移除子选项
				for (int i = root.getChildren().size() - 1; i > TYPE_MODE; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 通过 年级 科目 获取教版 章节等数据
				StudentWorkActivityController.getInstance()
						.GetUnionChapterList(TYPE_MODE, GradeCode, subCode, 0,
								1);
				break;
			// 教版
			case TYPE_SUBJECT:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				// 教版Id
				modeId = value.TabID;
				TreeNode parentSub = root.getChildren().get(TYPE_SUBJECT);
				TreeItemRoot subValue = (TreeItemRoot) parentSub.getValue();
				// 选中的教版 和教版Id
				subValue.select = value.select;
				subValue.TabID = value.TabID;
				parentSub.getViewHolder().notifyValue(subValue);
				tView.collapseNode(root.getChildren().get(TYPE_SUBJECT));
				List<TreeNode> list11 = root.getChildren().get(TYPE_SUBJECT)
						.getChildren();
				for (int i = 0; i < list11.size(); i++) {
					list11.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 移除教版子选项
				for (int i = root.getChildren().size() - 1; i > TYPE_SUBJECT; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 通过 年级 科目 教版 获取章节 及其他
				StudentWorkActivityController.getInstance()
						.GetUnionChapterList(TYPE_SUBJECT, GradeCode, subCode,
								modeId, 2);
				break;
			// 章节
			case TYPE_SESSION:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				setTreeNodeSelected(root.getChildren().get(TYPE_SESSION), false);
				node.setSelected(true);

				TreeNode p = root.getChildren().get(TYPE_SESSION);
				if (p != null) {
					TreeItemRoot pv = (TreeItemRoot) p.getValue();
					/**
					 * 选中的章节名称 Id
					 */
					pv.select = value.select;
					pv.TabID = value.TabID;
					p.getViewHolder().notifyValue(pv);
				}
				TreeNode c = root.getChildren().get(
						root.getChildren().size() - 1);
				if (c != null) {
					TreeItemRoot cv = (TreeItemRoot) c.getValue();
					if (cv.name.equals(getResources().getString(
							R.string.workol_select_title))) {
						TreeItemRoot subvalue = (TreeItemRoot) root
								.getChildren().get(1).getValue();
						cv.select = formatter.format(new Date())
								+ subvalue.select + value.select + "练习";
						c.getViewHolder().notifyValue(cv);
					}
				}
				// 通过章节Id判断是否有题
				StudentWorkActivityController.getInstance().TecQs(value.TabID);
				break;
			default:
				break;
			}
		}

	};

	private void setTreeNodeSelected(TreeNode node, boolean selected) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			TreeNode child = node.getChildren().get(i);
			setTreeNodeSelected(child, selected);
			child.setSelected(selected);
		}
	}

	/**
	 * 系统返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		MobclickAgent.onEvent(mContext,
				getResources().getString(R.string.StudentWorkActivity_refresh));
		refreshData();

	}

	/**
	 * 刷新数据
	 */
	private void refreshData() {
		// TODO Auto-generated method stub
		switch (mType) {
		case TYPE_ERRO:
			errArrayList.clear();
			// ly_webs.removeAllViews();
			pageIndex = 1;
			break;
		case TYPE_QREX:
			stuExList.clear();
			exPageIndex = 1;
		default:
			break;
		}
		changeData(0, 0);

	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		MobclickAgent
				.onEvent(
						mContext,
						getResources().getString(
								R.string.StudentWorkActivity_loadMore));
		dataLoadMore();
	}

	/**
	 * 加载更多
	 */
	private void dataLoadMore() {
		switch (mType) {
		case TYPE_ERRO:
			if (pageIndex < totalPageNum) {
				pageIndex = pageIndex + 1;
				changeData(0, 0);
			} else {
				mPullRefreshScrollView.onRefreshComplete();
				ToastUtil.showMessage(mContext, R.string.have_no_more);
			}
			break;
		case TYPE_QREX:
			if (exPageIndex < totalPageNum) {
				exPageIndex = exPageIndex + 1;
				changeData(0, 0);
			} else {
				ToastUtil.showMessage(mContext, R.string.have_no_more);
				mPullRefreshScrollView.onRefreshComplete();
			}
			break;
		default:
			ToastUtil.showMessage(mContext, R.string.have_no_more);
			mPullRefreshScrollView.onRefreshComplete();
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// HttpUtil.getInstance().getHttpClient().getConnectionManager().shutdown();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub

	}
}
