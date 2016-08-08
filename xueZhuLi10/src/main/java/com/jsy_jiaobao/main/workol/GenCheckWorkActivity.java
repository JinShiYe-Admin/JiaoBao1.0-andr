package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.GenInfo;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.workol.EduLevel;
import com.jsy_jiaobao.po.workol.ErrorModel;
import com.jsy_jiaobao.po.workol.HWStatus;
import com.jsy_jiaobao.po.workol.StuErrDetailModel;
import com.jsy_jiaobao.po.workol.StuErrorModel;
import com.jsy_jiaobao.po.workol.StuHW;
import com.jsy_jiaobao.po.workol.StudentErrorPost;
import com.umeng.analytics.MobclickAgent;

/**
 * 家长查询界面
 *
 * @author admin
 */
public class GenCheckWorkActivity extends BaseActivity implements
        OnClickListener, OnRefreshListener2<ScrollView> {
    private final int TYPE_CURR = 0;// 当前作业
    private final int TYPE_STATUS = 1;// 完成情况
    private final int TYPE_LEVEL = 2;// 学力值
    private final int TYPE_EXCERCISE = 3;// 练习
    private final int TYPE_ERROR = 4;// 错题本
    private int mType = TYPE_CURR;// 默认选定状态为当前作业
    private PullToRefreshScrollView mPullRefreshScrollView;// scrollview
    private Context mContext;// 上下文
    private TextView tv_curr;// 当前作业的选项卡
    private TextView tv_status;// 完成情况的选项卡
    private TextView tv_level;// 学力值的选项卡
    private TextView tv_exercise;// 练习列表的选项卡
    private TextView tv_errQuery;// 错题本选项卡
    // private TextView tv_errListreen;
    private TextView tv_nowork;// 没数据时要显示的view
    private FrameLayout fLayout;//
    private CusListView listView;// 列表
    private SpinnerAdapter _Adapter;
    private ArrayList<GenInfo> genList = new ArrayList<>();// 家长信息列表
    private GenHWListAdapter hwAdapter;// 作业列表的Adapter
    private GenCompleteHWListAdapter comphwAdapter;// 作业完成情况的Adapter
    private GenHWEduLevelAdapter edulevelAdapter;// 学力值的Adapter
    private ArrayList<EduLevel> baseEduList = new ArrayList<>();// 所有学历值列表
    private ArrayList<EduLevel> showEduList = new ArrayList<>();// 显示的学力值列表
    boolean haveChild = false;
    public static int StuId;
    private Thread thread;
    /**
     * 当前选中的学生
     */
    private GenInfo genInfo;// 家长信息
    private ErrorCheckAdapter errorAdapter;// 错一本的Adapter
    private StudentErrorPost errorPost;// 错题本Post
    private int chapterId = -1;// 章节Id 默认全部为-1
    private int pageIndex = 1;// 起始页码1

    private LinearLayout ly_webs;
    private LinearLayout ly_big_screen;
    private int exPageIndex = 1;// 错题本起始页码默认为1
    private int GradeCode = -1;// 年级代码 默认全部=-1
    private int subCode = -1;// 章节代码 全部=-1
    private int modeId = -1;// 教版代码 全部=-1
    private TextView tv_selectName;// 删选条件显示控件
    private ArrayList<ErrorModel> errArrayList;// 错题本列表
    private int totalPageNum = 0;// 页码总数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            genInfo = (GenInfo) savedInstanceState.getSerializable("genInfo");
            if(genInfo!=null)
            StuId = genInfo.getStudentID();
        }
        // 加载view
        initView();
    }

    /**
     * 保存意外销毁的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("genInfo", genInfo);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setContentLayout(R.layout.activity_parentcheck);
        mContext = this;
        hwAdapter = new GenHWListAdapter(mContext);
        comphwAdapter = new GenCompleteHWListAdapter(mContext);
        edulevelAdapter = new GenHWEduLevelAdapter(mContext);
        errorAdapter = new ErrorCheckAdapter(mContext);
        GenCheckWorkActivityController.getInstance().setContext(this);
        tv_nowork = (TextView) findViewById(R.id.workolstu_bg);
        fLayout = (FrameLayout) findViewById(R.id.frame_layout);
        tv_curr = (TextView) findViewById(R.id.parent_tv_curr);
        tv_status = (TextView) findViewById(R.id.parent_tv_status);
        tv_level = (TextView) findViewById(R.id.parent_tv_level);
        tv_exercise = (TextView) findViewById(R.id.parent_tv_exercise);
        tv_errQuery = (TextView) findViewById(R.id.parent_tv_query_error);
        // tv_errListreen = (TextView) findViewById(R.id.tv_errScreen);
        ly_webs = (LinearLayout) findViewById(R.id.ly_webs);
        ly_big_screen = (LinearLayout) findViewById(R.id.big_ly_screen);
        RelativeLayout ly_screen;// 错题本筛选 控件
        ly_screen = (RelativeLayout) findViewById(R.id.ly_screen);
        tv_selectName = (TextView) findViewById(R.id.tv_selectName);
        ly_screen.setOnClickListener(this);
        Spinner spinner;// 下拉框选择学生
        spinner = (Spinner) findViewById(R.id.parent_sp_stu);
        listView = (CusListView) findViewById(R.id.parent_listview_curr);
        errArrayList = new ArrayList<>();
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(this);
        mPullRefreshScrollView.setMode(Mode.BOTH);
        ly_big_screen.setVisibility(View.GONE);
        setViewFocusable(tv_curr);
        errorPost = new StudentErrorPost();
        listView.setAdapter(hwAdapter);
        tv_curr.setOnClickListener(this);
        tv_status.setOnClickListener(this);
        tv_level.setOnClickListener(this);
        tv_exercise.setOnClickListener(this);
        tv_errQuery.setOnClickListener(this);
        genList.clear();
        // 获取教宝好
        String jiaobaohao = sp.getString("JiaoBaoHao", "");
        // 获取教宝号下的用户班级
        if (!"".equals(jiaobaohao)) {
            if (Constant.listUserIdentity != null) {
                for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
                    UserIdentity identity = Constant.listUserIdentity.get(i);
                    if (identity.getRoleIdentity() == 3) {
                        List<UserClass> list = identity.getUserClasses();
                        if (list != null) {
                            for (int j = 0; j < list.size(); j++) {
                                UserClass userClass = list.get(j);
                                GenCheckWorkActivityController.getInstance()
                                        .getGenInfo(jiaobaohao,
                                                userClass.getClassID());
                            }
                        }
                    }
                }
            }
        }
        _Adapter = new SpinnerAdapter();
        // 加载Adapter
        spinner.setAdapter(_Adapter);
        setActionBarTitle(R.string.personal_roleIdentity_parent);
        // 家长信息下拉框的选中监听事件（切换学生）
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                genInfo = genList.get(position);
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_spinner));
                switch (mType) {
                    case TYPE_CURR:
                        break;
                    case TYPE_STATUS:
                        break;
                    case TYPE_EXCERCISE:
                        // 重置起始页
                        exPageIndex = 1;
                        break;
                    case TYPE_ERROR:
                        // 重置起始页
                        pageIndex = 1;
                        break;
                    case TYPE_LEVEL:
                        baseEduList.clear();
                        setEduLevelAdapterData();
                        break;

                    default:
                        break;
                }
                changeData(0, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * listView 中Item的点击事件监听
         */
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (mType) {
                    case TYPE_CURR:
                        break;
                    case TYPE_STATUS:
                        break;
                    case TYPE_EXCERCISE:
                        break;
                    case TYPE_ERROR:
                        break;
                    case TYPE_LEVEL:
                        // 学力值Item的点击事件
                        if (position > 0) {
                            EduLevel eduLevel = showEduList.get(position - 1);
                            if (eduLevel.isExpanded()) {
                                for (int i = 0; i < baseEduList.size(); i++) {
                                    EduLevel item = baseEduList.get(i);
                                    if (item.getLevelID().equals(
                                            eduLevel.getLevelID())) {
                                        baseEduList.get(i).setExpanded(false);
                                        break;
                                    }
                                }
                                closeChildren(eduLevel);
                                setEduLevelAdapterData();
                            } else {// 关闭状态转为打开,判断有没有孩子,若有,则把孩子的父状态设为打开,若没有,则根据当前levelID获取数据
                                String levelID = eduLevel.getLevelID();
                                String[] tags = levelID.split("\\|");

                                boolean isLoaded = false;// 是否已加载数据，默认为否
                                for (int i = 0; i < baseEduList.size(); i++) {
                                    EduLevel item = baseEduList.get(i);
                                    if (item.getLevelID().equals(
                                            eduLevel.getLevelID())) {
                                        baseEduList.get(i).setExpanded(true);
                                    }
                                    if (item.getParentID().equals(levelID)) {
                                        isLoaded = true;
                                        baseEduList.get(i).setParentExpanded(true);
                                    }
                                }
                                if (isLoaded) {// 数据一家在
                                    setEduLevelAdapterData();
                                } else {
                                    if (tags.length == 2) {
                                        changeData(Integer.parseInt(tags[1]), 0);
                                    } else if (tags.length == 1) {
                                        changeData(0, 0);
                                    } else if (tags.length == 3) {

                                        changeData(Integer.parseInt(tags[1]),
                                                Integer.parseInt(tags[2]));
                                    } else if (tags.length > 3) {// 多级菜单
                                        String[] chapterTags = Arrays.copyOfRange(
                                                tags, 2, tags.length);
                                        int[] chapterId = new int[chapterTags.length];
                                        for (int i = 0; i < chapterTags.length; i++) {
                                            chapterId[i] = Integer
                                                    .parseInt(chapterTags[i]);
                                        }
                                        changeData(Integer.parseInt(tags[1]),
                                                chapterId);
                                    }
                                }
                                // }
                            }

                        }
                        break;
                    default:
                        break;
                }
            }

        });
    }

    /**
     * 闭合所有的孩子,并且把孩子的当前状态和父状态设置为闭合
     */
    private void closeChildren(EduLevel eduLevel) {

        for (int i = 0; i < baseEduList.size(); i++) {
            EduLevel item = baseEduList.get(i);

            if (item.getParentID().equals(eduLevel.getLevelID())) {
                haveChild = true;
                break;
            }

        }
        if (haveChild) {
            for (int i = 0; i < baseEduList.size(); i++) {
                EduLevel item = baseEduList.get(i);
                if (item.getParentID().equals(eduLevel.getLevelID())) {
                    baseEduList.get(i).setExpanded(false);
                    if (!item.getParentID().equals("-")) {
                        baseEduList.get(i).setParentExpanded(false);
                    }
                    closeChildren(item);
                }
            }

        }
    }

    private void changeData(int uid, int[] chapterId) {
        if (genInfo != null) {
            switch (mType) {
                case TYPE_LEVEL:
                    GenCheckWorkActivityController.getInstance().GetStuEduLevel(
                            genInfo.getStudentID(), uid, chapterId);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 在不同选项下，去获取数据
     *
     * @param uid uid
     * @param chapterid cha
     */
    private void changeData(int uid, int chapterid) {
        if (genInfo != null) {
            switch (mType) {
                case TYPE_CURR:
                    setViewFocusable(tv_curr);
                    GenCheckWorkActivityController.getInstance().GetStuHWList(
                            genInfo.getStudentID(), 0);
                    break;
                case TYPE_STATUS:
                    setViewFocusable(tv_status);
                    GenCheckWorkActivityController.getInstance()
                            .GetCompleteStatusHW(genInfo.getStudentID());
                    break;
                case TYPE_LEVEL:
                    setViewFocusable(tv_level);
                    // 点击了学历按钮去获取数据
                    GenCheckWorkActivityController.getInstance().GetStuEduLevel(
                            genInfo.getStudentID(), uid, chapterid);
                    break;
                case TYPE_ERROR:
                    /**
                     * 获取错题本
                     *
                     */
                    if (pageIndex == 1) {
                        // ly_webs.removeAllViews();
                        errArrayList.clear();
                    }
                    setViewFocusable(tv_errQuery);
                    errorPost.setGradeCode(GradeCode);
                    errorPost.setSubjectCode(subCode);
                    errorPost.setUnid(modeId);
                    errorPost.setChapterid(chapterId);
                    errorPost.setPageIndex(pageIndex);
                    int pageSize = 10;// 每页数据10
                    int isSelf = 1;// 是否查看状态
                    errorPost.setIsSelf(isSelf);
                    errorPost.setPageSize(pageSize);
                    errorPost.setStuId(genInfo.getStudentID());
                    GenCheckWorkActivityController.getInstance().GetStuErr(
                            errorPost);
                    break;
                case TYPE_EXCERCISE:
                    // 点击了按钮去获取数据
                    setViewFocusable(tv_exercise);
                    if (exPageIndex == 1) {
                        // ly_webs.removeAllViews();
                        stuExList.clear();
                    }
                    GenCheckWorkActivityController.getInstance().GetStuHWListPage(
                            genInfo.getStudentID(), 1, exPageIndex, 10);
                    break;

                default:
                    break;
            }
        }
    }

    private void setDefaultFocusable() {
        tv_curr.setFocusable(false);
        tv_status.setFocusable(false);
        tv_level.setFocusable(false);
        tv_exercise.setFocusable(false);
        tv_errQuery.setFocusable(false);
    }

    private void setViewFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 界面中控件的点击事件
     */
    @Override
    public void onClick(View v) {
        stuExList.clear();// 清空数据
        errArrayList.clear();
        setDefaultFocusable();
        ly_big_screen.setVisibility(View.GONE);
        switch (v.getId()) {
            // 当前作业
            case R.id.parent_tv_curr:
                MobclickAgent.onEvent(mContext,
                        getResources()
                                .getString(R.string.GenCheckWorkActivity_curr));
                mType = TYPE_CURR;
                listView.setAdapter(hwAdapter);// listView加载Adapter
                hwAdapter.setIsHW(true);// adapter设置是作业
                changeData(0, 0);
                break;
            // 当前状态
            case R.id.parent_tv_status:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_status));
                // mType
                mType = TYPE_STATUS;
                listView.setAdapter(comphwAdapter);
                changeData(0, 0);
                break;
            case R.id.parent_tv_level:
                // 友盟监听事件，上下同
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_level));
                mType = TYPE_LEVEL;
                listView.setAdapter(edulevelAdapter);
                changeData(0, 0);
                break;
            // 练习列表
            case R.id.parent_tv_exercise:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_excercise));
                mType = TYPE_EXCERCISE;
                exPageIndex = 1;
                listView.setAdapter(hwAdapter);
                hwAdapter.setIsHW(false);
                changeData(0, 0);
                break;
            // 错题本
            case R.id.parent_tv_query_error:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_error));
                mType = TYPE_ERROR;
                pageIndex = 1;
                // ly_webs.removeAllViews();
                ly_big_screen.setVisibility(View.VISIBLE);
                listView.setAdapter(errorAdapter);
                // fLayout.setVisibility(View.GONE);
                changeData(0, 0);
                break;
            // 筛选控件
            case R.id.ly_screen:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_screen));
                pageIndex = 1;
                mType = TYPE_ERROR;
                ly_big_screen.setVisibility(View.VISIBLE);
                setViewFocusable(tv_errQuery);
                // fLayout.setVisibility(View.GONE);
                Intent i = new Intent(this, ErrorScreenActivity.class);
                i.putExtra("role", 1);
                i.putExtra("GenInfo", genInfo);
                startActivityForResult(i, 4596);
                break;
            default:
                break;
        }
    }

    // 生命周期事件
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        EventBusUtil.register(this);
    }

    // 生命周期事件
    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    // 筛选条件返回结果
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        ly_webs.removeAllViews();
        mType = TYPE_ERROR;
        switch (arg1) {
            case 999:
                Bundle args = arg2.getExtras();
                // 年纪代码
                GradeCode = args.getInt("GradeCode");
                // 科目代码
                subCode = args.getInt("SubCode");
                // 教版代码
                modeId = args.getInt("ModeId");
                // 章节代码
                chapterId = args.getInt("ChapterId");
                // 筛选提交名称
                String mSelect;// 筛选条件
                mSelect =  args.getString("Select");
                // 如果栓选条件内容部位空
                if ( mSelect != null &&mSelect.length()>0) {
                    // 显示筛选条件
                    tv_selectName.setText(mSelect);
                }
                /***
                 * 返回值 请求数据 pageIndex=1
                 */
                if (chapterId != 0) {
                    pageIndex = 1;
                    changeData(0, 0);
                }
                break;

            default:
                ToastUtil.showMessage(mContext, "你选的没题");
                break;
        }
    }

    /**
     * Handler
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

    /**
     * 错题本控件加载延迟
     */
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

    int size = 0;// 大小
    private int questionNo = 0;
    private ArrayList<StuHW> stuExList = new ArrayList<>();
    private ArrayList<StuErrorModel> getErrList;

    @SuppressWarnings("unchecked")
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            // 错题本
            case Constants.WORKOL_GetStuErr:
                mPullRefreshScrollView.onRefreshComplete();
                questionNo = 0;
                getErrList = (ArrayList<StuErrorModel>) list.get(1);
                // 如果获取的错题本数据为空
                if (getErrList.size() == 0 || getErrList == null) {
                    totalPageNum = 0;
                    tv_nowork.setVisibility(View.VISIBLE);
                    DialogUtil.getInstance().cannleDialog();
                    if (pageIndex == 1) {
                        // 显示无错题
                        tv_nowork.setVisibility(View.VISIBLE);
                        ToastUtil.showMessage(mContext, "没有错题");
                    } else if (pageIndex > 1) {
                        ToastUtil.showMessage(mContext, "没有更多了");
                    }
                    // 获取到错题数据
                } else {
                    totalPageNum = Integer.valueOf(getErrList.get(0).getTabIDStr());
                    // 提示无错题的控件消失
                    tv_nowork.setVisibility(View.GONE);
                    StuErrorModel stuErrorModel = getErrList.get(0);
                    // 获取第一条的错题详情
                    GenCheckWorkActivityController.getInstance().GetStuHWQs(0,
                            stuErrorModel.getQsID(), stuErrorModel);

                }
                break;
            // 错题本详细信息
            case Constants.WORKOL_GetStuHWQs:
                questionNo++;
                StuErrDetailModel qsPack = (StuErrDetailModel) list.get(1);
                StuErrorModel stuErrorModel = (StuErrorModel) list.get(2);
                list.get(2);
                ErrorModel errorModel = new ErrorModel(mContext);
                errorModel.setStuErrDetailModel(qsPack);
                errorModel.setStuErrorModel(stuErrorModel);
                errArrayList.add(errorModel);
                // 如果 错题详情大小小于错题大小，请求下一条错题详情
                if (questionNo < getErrList.size()) {
                    StuErrorModel stuErrorModel1 = getErrList.get(questionNo);
                    GenCheckWorkActivityController.getInstance().GetStuHWQs(0,
                            stuErrorModel1.getQsID(), stuErrorModel1);

                } else {
                    // 当错题本详情大小等于错题大小，adapter加载数据
                    errorAdapter.setErrorModels(errArrayList);
                    errorAdapter.notifyDataSetChanged();
                    mPullRefreshScrollView.onRefreshComplete();
                    createThread();
                    thread.start();
                    // DialogUtil.getInstance().cannleDialog();
                    getErrList.clear();
                }
                break;
            // 没有子菜单
            case Constants.WORKOL_Notify_nochild:
                DialogUtil.getInstance().cannleDialog();
                edulevelAdapter.notifyDataSetChanged();
                break;
            case Constant.user_regist_checkAccN:
                DialogUtil.getInstance().cannleDialog();
                break;
            // 家长信息
            case Constants.WORKOL_getGenInfo:
                DialogUtil.getInstance().cannleDialog();
                GenInfo genInfo1 = (GenInfo) list.get(1);
                if (genInfo1 != null) {
                    genList.add(genInfo1);
                    _Adapter.notifyDataSetChanged();
                }
                break;
            // 练习列表
            case Constants.WORKOL_GetStuHWListPage:
                mPullRefreshScrollView.onRefreshComplete();
                DialogUtil.getInstance().cannleDialog();
                ArrayList<StuHW> getStuExList = (ArrayList<StuHW>) list.get(1);
                if (getStuExList == null || getStuExList.size() == 0) {
                    totalPageNum = 0;
                    if (exPageIndex == 1) {
                        // 如果第一页数据为空，则提示 无联系
                        tv_nowork.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        ToastUtil.showMessage(mContext, "暂无练习列表");
                    } else {
                        // 否则，提示没有更多
                        tv_nowork.setVisibility(View.GONE);
                        ToastUtil.showMessage(mContext, "没有更多了");
                    }
                } else {
                    // 如果数据不为空，获取总页码，更新listview
                    totalPageNum = Integer.valueOf(getStuExList.get(0)
                            .getTabIDStr());
                    stuExList.addAll(getStuExList);
                    tv_nowork.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    hwAdapter.setmData(stuExList);
                    hwAdapter.notifyDataSetChanged();
                }

                break;
            // 学生作业列表
            case Constants.WORKOL_GetStuHWList:
                DialogUtil.getInstance().cannleDialog();
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<StuHW> getStuHWList1 = (ArrayList<StuHW>) list.get(1);
                if (getStuHWList1 == null||getStuHWList1.size()==0 ) {
                    // 如果学生作业未空，提示没有作业
                    tv_nowork.setVisibility(View.VISIBLE);
                    fLayout.setVisibility(View.GONE);
                } else {
                    // 否则 加载数据，更新ListView
                    tv_nowork.setVisibility(View.GONE);
                    fLayout.setVisibility(View.VISIBLE);
                    hwAdapter.setmData(getStuHWList1);
                    hwAdapter.notifyDataSetChanged();
                }
                break;
            // 完成情况
            case Constants.WORKOL_GetCompleteStatusHW:
                DialogUtil.getInstance().cannleDialog();
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<HWStatus> statushwList = (ArrayList<HWStatus>) list
                        .get(1);
                if (statushwList == null || statushwList.size() == 0) {
                    // 没有数据，显示为空
                    tv_nowork.setVisibility(View.VISIBLE);
                    fLayout.setVisibility(View.GONE);
                } else {
                    // 存在数据，更新ListView
                    tv_nowork.setVisibility(View.GONE);
                    fLayout.setVisibility(View.VISIBLE);
                    comphwAdapter.setmData(statushwList);
                    comphwAdapter.notifyDataSetChanged();
                }
                break;
            // 学力值
            case Constants.WORKOL_GetStuEduLevel:
                DialogUtil.getInstance().cannleDialog();
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<EduLevel> level = (ArrayList<EduLevel>) list.get(1);

                /** 把数据加入总数据总,若level有父数据,则加入父数据位置 */
                if (level != null && level.size() > 0) {
                    tv_nowork.setVisibility(View.GONE);
                    fLayout.setVisibility(View.VISIBLE);
                    if (!baseEduList.containsAll(level)) {
                        String parentID = level.get(0).getParentID();
                        if (baseEduList.size() == 0) {
                            baseEduList.addAll(level);
                        } else {
                            for (int i = 0; i < baseEduList.size(); i++) {
                                EduLevel item = baseEduList.get(i);
                                if (parentID.equals(item.getLevelID())) {
                                    baseEduList.addAll(i + 1, level);
                                    break;
                                }
                            }
                        }
                    }
                    setEduLevelAdapterData();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 对学力adapter设置数据,从总数据中取出父状态为展开的数据放入listview
     */
    private void setEduLevelAdapterData() {
        showEduList.clear();
        if (baseEduList.size() == 0 || baseEduList == null) {
            tv_nowork.setVisibility(View.VISIBLE);
            fLayout.setVisibility(View.GONE);
        } else {
            tv_nowork.setVisibility(View.GONE);
            fLayout.setVisibility(View.VISIBLE);
            for (EduLevel item : baseEduList) {
                Log.e("baseEduList", baseEduList.toString());
                if (item.isParentExpanded()) {
                    showEduList.add(item);
                }
            }
            edulevelAdapter.setmData(showEduList);
            // 修改已生成列表数据
            edulevelAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 下拉菜单的Adapter
     *
     * @author admin
     */
    private class SpinnerAdapter extends BaseAdapter {
        // 获取数目
        @Override
        public int getCount() {
            return genList == null ? 0 : genList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        // item的Id
        @Override
        public long getItemId(int position) {
            return 0;
        }

        // 配置View
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
                    parent, android.R.layout.simple_spinner_item, position);
            TextView txt = viewHolder.getView(android.R.id.text1);
            genInfo = genList.get(position);
            if (genInfo != null) {
                String name = genInfo.getStdName();
                if (TextUtils.isEmpty(name)) {
                    name = "";
                }
                txt.setText(name);
            }
            return viewHolder.getConvertView();
        }
    }

    /**
     * 下来刷新数据
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        MobclickAgent
                .onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_refresh));
        switch (mType) {
            //
            case TYPE_EXCERCISE:
                stuExList.clear();
                exPageIndex = 1;
                break;
            case TYPE_ERROR:
                pageIndex = 1;
                errArrayList.clear();
                // ly_webs.removeAllViews();
                break;
            default:
                pageIndex = 1;
                break;
        }
        changeData(0, 0);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        // TODO Auto-generated method stub
        MobclickAgent.onEvent(mContext,
                getResources()
                        .getString(R.string.GenCheckWorkActivity_loadMore));
        // 加载更多
        dataLoadMore();

    }

    /**
     * 加载更多
     */
    private void dataLoadMore() {
        switch (mType) {
            case TYPE_ERROR:
                if (pageIndex < totalPageNum) {
                    // 如果当前页码小于总页码，继续加载数据
                    pageIndex = pageIndex + 1;
                    changeData(0, 0);
                } else {
                    // 提示没有更多
                    mPullRefreshScrollView.onRefreshComplete();
                    ToastUtil.showMessage(mContext, R.string.have_no_more);
                }
                break;
            case TYPE_EXCERCISE:
                // 如果当前页码小于总页码，继续加载数据
                if (exPageIndex < totalPageNum) {
                    exPageIndex = exPageIndex + 1;
                    changeData(0, 0);
                } else {
                    // 提示没有更懂数据
                    ToastUtil.showMessage(mContext, R.string.have_no_more);
                    mPullRefreshScrollView.onRefreshComplete();
                }
                break;
            default:
                // 其他状况，没有更多数据
                ToastUtil.showMessage(mContext, R.string.have_no_more);
                mPullRefreshScrollView.onRefreshComplete();
                break;
        }

    }

    @Override
    public void onPullPageChanging(boolean isChanging) {
        // TODO Auto-generated method stub

    }

}
