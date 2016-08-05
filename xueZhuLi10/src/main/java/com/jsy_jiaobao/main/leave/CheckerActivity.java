package com.jsy_jiaobao.main.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.ClassSumLeaveModel;
import com.jsy_jiaobao.po.leave.GateQueryLeaveModel;
import com.jsy_jiaobao.po.leave.GateQueryLeaves;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.StuSumLeavesModel;
import com.jsy_jiaobao.po.leave.SumLeavesModel;
import com.jsy_jiaobao.po.leave.UnitClassLeaveModel;
import com.jsy_jiaobao.po.leave.UnitClassLeaves;
import com.jsy_jiaobao.po.leave.UnitLeavesPost;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//一些修改1 2016-5-4 MSL
//1.修改-修改班主任查看学生审核或未审核时，使用审核人员的接口

/**
 * 功能说明：请假系统，请假审核界面
 *
 * @author MSL
 */
public class CheckerActivity extends BaseActivity implements OnClickListener,
        OnRefreshListener2<ScrollView> {
    private static final int ROLE_1 = 1;// 一审
    private static final int ROLE_2 = 2;// 二审
    private static final int ROLE_3 = 3;// 三审
    private static final int ROLE_4 = 4;// 四审
    private static final int ROLE_5 = 5;// 五审
    private static final int ROLE_6 = 6;// 门卫
    private static final int TYPE_UNCHECK = 0;// 待审核
    private static final int TYPE_CHECKED = 1;// 已审核
    private static final int TYPE_QUERY = 2;// 查询统计
    private static final int TYPE_GATE = 3;// 门卫审核
    private static final int TYPE_TEA = 1;// 老师
    private static final int TYPE_STU = 0;// 学生

    private int pageNum = 1;// 第几页，默认为1
    private int rowCount = 0;// pageNum=1为0，第二页起从前一页的返回结果中获得
    private int unitId = 0;// 单位Id
    private int mCheckType = TYPE_UNCHECK;// 查询类型：0待审核，1已审核，2统计查询
    private boolean isHasStu;// 有学生的审核权限
    private boolean isHasTea;// 有老师的审核权限
    private boolean isGateGuard = false;// 有门卫的审核权限
    /* 筛选条件返回的值 */
    /**
     * 审核级别，第几审
     */
    private Integer mBackCheckRole;// 审核级别，第几审
    /**
     * 教职工或学生，0学生，1教职工
     */
    private Integer mBackType;// 教职工或学生，0学生，1教职工
    /**
     * 班级id
     */
//    private Integer mBackClassId;// 班级id
    /**
     * 查询类型：0待审核，1已审核，2统计查询
     */
    private Integer mBackCheckType;// 查询类型：0待审核，1已审核，2统计查询

    /**
     * 时间
     */
    private String mBackTime;// 时间
    /**
     * 班级名称
     */
    private String mBackClassName;// 班级名称
    /**
     * 年级名称
     */
    private String mBackGradeName;// 年级名称
    /* 筛选条件返回的值 */
    private UnitLeavesPost unitLeavesPost;// 请假审核记录查询post
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD",
            Locale.getDefault());
    private ArrayList<GateQueryLeaveModel> gateLeaveList;// 门卫审核假条列表
    private ArrayList<UnitClassLeaveModel> leaveList;// 假条列表
    private SparseArray<Object> mCheckTypeGradeClassMap;
    private Context mContext;
    private LinearLayout ly_condition;// 筛选条件区域
    private LinearLayout ly_listtitile;// 假条列表标题区域
    private LinearLayout ly_querylisttitile;// 查询统计标题区域
    private TextView tv_uncheck;// 待审核
    private TextView tv_checked;// 已审核
    private TextView tv_query;// 统计查询
    private TextView tv_gate;// 门卫审核
    private TextView tv_checkedflag;// 列表标题‘状态’
    private TextView tv_condition;// 筛选条件显示区域

    private CusListView listView;// 假条列表
    private CheckerQueryAdapter queryAdapter;// 统计查询
    private UnitClassLeavesAdapter checkAdapter;// 待审核，已审核，门卫审核
    private PullToRefreshScrollView refreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initViews();
    }

    /**
     * 加载控件
     */
    private void initViews() {
        setContentLayout(R.layout.leave_activity_checker);
        CheckerActivityControler.getInstance().setContext(this);
        ly_condition = (LinearLayout) findViewById(R.id.ly_condition);
        ly_listtitile = (LinearLayout) findViewById(R.id.ly_listtitile);
        ly_querylisttitile = (LinearLayout) findViewById(R.id.ly_querylisttitile);
        TextView tv_null;// 空白区域，只有门卫审核权限时，用于将布局居中
        TextView tv_null2;// 空白区域，只有门卫审核权限时，用于将布局居中
        tv_null = (TextView) findViewById(R.id.tv_null);
        tv_null2 = (TextView) findViewById(R.id.tv_null2);
        tv_uncheck = (TextView) findViewById(R.id.tv_uncheck);
        tv_checked = (TextView) findViewById(R.id.tv_checked);
        tv_query = (TextView) findViewById(R.id.tv_query);
        tv_gate = (TextView) findViewById(R.id.tv_gateGuard);


        TextView tv_screen = (TextView) findViewById(R.id.tv_screen_condition);
        tv_checkedflag = (TextView) findViewById(R.id.item_checkedcommited_flag);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        listView = (CusListView) findViewById(R.id.cus_listView);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        refreshScrollView.setOnRefreshListener(this);
        tv_uncheck.setOnClickListener(this);
        tv_checked.setOnClickListener(this);
        tv_query.setOnClickListener(this);
        tv_gate.setOnClickListener(this);
        tv_screen.setOnClickListener(this);
        ly_condition.setOnClickListener(this);
        getBasicData();
        getApprovalInfo();
        setDefaultFocusable();
        if (isGateGuard && !isHasStu && !isHasTea) {// 只有门卫审核权限，只显示标题门卫审核和假条列表，其他全部隐藏
            tv_uncheck.setVisibility(View.GONE);
            tv_checked.setVisibility(View.GONE);
            tv_query.setVisibility(View.GONE);
            ly_querylisttitile.setVisibility(View.GONE);
            ly_condition.setVisibility(View.GONE);
            tv_checkedflag.setVisibility(View.GONE);
            ly_listtitile.setVisibility(View.VISIBLE);
            tv_null.setVisibility(View.INVISIBLE);
            tv_null2.setVisibility(View.INVISIBLE);
            mCheckType = TYPE_GATE;
            leaveList.clear();
            gateLeaveList.clear();
            setViewFocusable(tv_uncheck);
            changeData();
        } else {
            setViewFocusable(tv_uncheck);
            getDefaultPost();
        }
    }

    /**
     * 默认都不获取焦点
     */
    private void setDefaultFocusable() {
        tv_uncheck.setFocusable(false);
        tv_checked.setFocusable(false);
        tv_query.setFocusable(false);
        tv_gate.setFocusable(false);
    }

    /**
     * 设置获取焦点的View
     *
     * @param view view
     */
    private void setViewFocusable(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * 初始化数值
     */
    private void getBasicData() {
        unitId = BaseActivity.sp.getInt("UnitID", 0);
        unitLeavesPost = new UnitLeavesPost(this);
        leaveList = new ArrayList<>();
        gateLeaveList = new ArrayList<>();
        mCheckTypeGradeClassMap = new SparseArray<>();
        checkAdapter = new UnitClassLeavesAdapter(this);
        queryAdapter = new CheckerQueryAdapter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_uncheck:// 待审核
                MobclickAgent.onEvent(mContext,
                        getResources().getString(R.string.CheckerActivity_uncheck));
                mCheckType = TYPE_UNCHECK;
                leaveList.clear();
                gateLeaveList.clear();
                ly_condition.setVisibility(View.VISIBLE);// 显示筛选条件区域
                tv_checkedflag.setVisibility(View.VISIBLE);// 显示状态项
                ly_listtitile.setVisibility(View.VISIBLE);// 显示假条列表标题
                ly_querylisttitile.setVisibility(View.GONE);// 隐藏统计查询列表标题
                setDefaultFocusable();
                break;
            case R.id.tv_checked:// 已审核
                MobclickAgent.onEvent(mContext,
                        getResources().getString(R.string.CheckerActivity_checked));
                mCheckType = TYPE_CHECKED;
                leaveList.clear();
                gateLeaveList.clear();
                ly_condition.setVisibility(View.VISIBLE);// 显示筛选条件区域
                tv_checkedflag.setVisibility(View.VISIBLE);// 显示状态项
                ly_listtitile.setVisibility(View.VISIBLE);// 显示假条列表标题
                ly_querylisttitile.setVisibility(View.GONE);// 隐藏统计查询列表标题
                setDefaultFocusable();
                break;
            case R.id.tv_query:// 统计查询
                MobclickAgent.onEvent(mContext,
                        getResources().getString(R.string.CheckerActivity_query));
                leaveList.clear();
                gateLeaveList.clear();
                ly_condition.setVisibility(View.VISIBLE);// 显示筛选条件区域
                tv_checkedflag.setVisibility(View.VISIBLE);// 显示状态项
                ly_listtitile.setVisibility(View.GONE);// 隐藏假条列表标题
                ly_querylisttitile.setVisibility(View.VISIBLE);// 显示统计查询列表标题
                mCheckType = TYPE_QUERY;
                setDefaultFocusable();
                break;
            case R.id.tv_gateGuard:// 门卫审核
                MobclickAgent.onEvent(mContext,
                        getResources()
                                .getString(R.string.CheckerActivity_gateGuard));
                mCheckType = TYPE_GATE;
                leaveList.clear();
                gateLeaveList.clear();
                ly_condition.setVisibility(View.GONE);// 隐藏筛选条件区域
                tv_checkedflag.setVisibility(View.GONE);// 隐藏状态项
                ly_listtitile.setVisibility(View.VISIBLE);// 显示假条列表标题
                ly_querylisttitile.setVisibility(View.GONE);// 显示统计查询列表标题
                setDefaultFocusable();
                break;
            case R.id.tv_screen_condition:// 筛选条件
            case R.id.ly_condition:// 筛选条件
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.CheckerActivity_screen_condition));
                if (isHasStu || isHasTea) {// 只允许有审核老师或学生权限的账号能够点击筛选条件
                    Intent i = new Intent(this,
                            CheckerScreenConditionActivity.class);
                    i.putExtra("CheckType", mCheckType);// 查询类型：0待审核，1已审核，2统计查询
                    startActivityForResult(i, 0);
                } else {
                    ToastUtil.showMessage(mContext, "无权限查询");
                }
                break;
            default:
                break;
        }
        changeData();
    }

    /**
     * 通过点击的标题切换不同的列表
     */
    private void changeData() {
        switch (mCheckType) {
            case TYPE_UNCHECK:// 待审核
                setViewFocusable(tv_uncheck);
                listView.setAdapter(checkAdapter);
                break;
            case TYPE_CHECKED:// 已审核
                setViewFocusable(tv_checked);
                listView.setAdapter(checkAdapter);
                break;
            case TYPE_QUERY:// 统计查询
                setViewFocusable(tv_query);
                listView.setAdapter(queryAdapter);
                break;
            case TYPE_GATE:// 门卫审核
                setViewFocusable(tv_gate);
                listView.setAdapter(checkAdapter);
                checkAdapter.setCheckRole(ROLE_6);
                break;
            default:
                break;
        }
        getDefaultChange();
    }

    /**
     * 通过点击的标题获取不同的数据
     */
    private void getDefaultChange() {
        switch (mCheckType) {
            case TYPE_UNCHECK:// 待审核
            case TYPE_CHECKED:// 已审核
            case TYPE_QUERY:// 统计查询
                if (isHasStu || isHasTea) {// 有审核学生或者老师的权限才可以查询
                    getDefaultPost();
                } else {
                    ToastUtil.showMessage(mContext,
                            R.string.leave_no_perminssion_to_query);
                }
                break;
            case TYPE_GATE:// 门卫审核
                getDefaultPost();
                break;
            default:
                break;
        }
    }

    /**
     * 待审核，已审核，统计查询，门卫审核，对应的获取数据
     */
    private void getDefaultPost() {
        if (mCheckTypeGradeClassMap.get(mCheckType) == null) {// 未选择筛选条件
            if (mCheckType == 3) {// 门卫审核
                String time = sdf.format(new Date());
                CheckerActivityControler.getInstance().GetGateLeaves(20,
                        pageNum, rowCount, unitId, time);
            } else {
                ToastUtil.showMessage(mContext,
                        R.string.leave_please_choose_condition);
            }
            tv_condition.setText("");
        } else {
            switch (mCheckType) {
                case TYPE_UNCHECK:// 待审核
                    unitLeavesPost = (UnitLeavesPost) mCheckTypeGradeClassMap
                            .get(TYPE_UNCHECK);
                    int levelUncheck = unitLeavesPost.getLevel();
                    int manTypeUncheck = unitLeavesPost.getManType();
                    unitLeavesPost.setRowCount(rowCount);
                    unitLeavesPost.setPageNum(pageNum);
                    unitLeavesPost.setCheckFlag(0);
                    CheckerActivityControler.getInstance().GetUnitLeaves(
                            unitLeavesPost);
                    // }
                    setConditionText(mCheckType, manTypeUncheck,
                            unitLeavesPost.getGradeStr(),
                            unitLeavesPost.getClassStr(), levelUncheck,
                            unitLeavesPost.getsDateTime());
                    break;
                case TYPE_CHECKED:// 已审核
                    unitLeavesPost = (UnitLeavesPost) mCheckTypeGradeClassMap
                            .get(TYPE_CHECKED);
                    int levelCheck = unitLeavesPost.getLevel();
                    int manTypeCheck = unitLeavesPost.getManType();
                    // if (levelCheck == ROLE_1 && manTypeCheck == 0) {// 班主任查询已审核
                    // int unitClassId = unitLeavesPost.getUnitId();
                    // String sDateTime = unitLeavesPost.getsDateTime();
                    // CheckerActivityControler.getInstance().GetClassLeaves(20,
                    // pageNum, rowCount, unitClassId, sDateTime, 1);
                    // } else {
                    unitLeavesPost.setRowCount(rowCount);
                    unitLeavesPost.setPageNum(pageNum);
                    unitLeavesPost.setCheckFlag(1);
                    CheckerActivityControler.getInstance().GetUnitLeaves(
                            unitLeavesPost);
                    // }
                    setConditionText(mCheckType, manTypeCheck,
                            unitLeavesPost.getGradeStr(),
                            unitLeavesPost.getClassStr(), levelCheck,
                            unitLeavesPost.getsDateTime());
                    break;
                case TYPE_QUERY:// 统计查询
                    if (isHasTea || isHasStu) {
                        String sDateTime = (String) mCheckTypeGradeClassMap
                                .get(TYPE_QUERY);
                        switch (mBackType) {
                            case TYPE_TEA:// 查询老师
                                CheckerActivityControler.getInstance().GetManSumLeaves(
                                        unitId, sDateTime);
                                setConditionText(mCheckType, mBackType, null, null, 0,
                                        sDateTime);
                                break;
                            case TYPE_STU:// 查询班级
                                String gradeStr = (String) mCheckTypeGradeClassMap
                                        .get(-TYPE_QUERY);
                                CheckerActivityControler.getInstance()
                                        .GetClassSumLeaves(unitId, sDateTime, gradeStr);// 所有班的统计情况
                                setConditionText(mCheckType, mBackType, gradeStr, null,
                                        0, sDateTime);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 将选取的筛选条件显示到筛选区域
     */
    private void setConditionText(int mCheckType, int mBackType,
                                  String mBackGradeName, String mBackClassName, int mBackCheckRole,
                                  String mBackTime) {
        tv_condition.setText("");
        String text = "";
        if (mBackType == 1) {
            text = text + "教职工 ";
        } else {
            text = text + "学生 ";
        }
        text = text + " ";
        switch (mBackCheckRole) {// 审核权限
            case 1:
                text = text + "一审";
                break;
            case 2:
                text = text + "二审";
                break;
            case 3:
                text = text + "三审";
                break;
            case 4:
                text = text + "四审";
                break;
            case 5:
                text = text + "五审";
                break;
            default:
                break;
        }
        text = text + " " + mBackTime;
        if (mBackType == 0) {// 筛选条件选取了学生
            if (mBackGradeName != null) {
                text = text + "\n" + "年级：" + mBackGradeName;// 年级
            } else {
                text = text + "\n" + "年级：" + "全部";// 年级
            }
            if (mCheckType != 2) {// 不是统计查询
                if (mBackClassName != null) {
                    text = text + " " + " " + "班级：" + mBackClassName;// 班级
                } else {
                    text = text + " " + " " + "班级：" + "全部" + " ";// 年级
                }
            }
        }
        tv_condition.setText(text);
    }

    /**
     * 权限设置
     */
    private void getApprovalInfo() {
        ArrayList<String> approvalNoteList = new ArrayList<>();
        ArrayList<String> approvalStuNoteList = new ArrayList<>();
        ArrayList<Integer> approvalList = new ArrayList<>();
        ArrayList<Integer> approvalStuList = new ArrayList<>();
        isGateGuard = sp.getBoolean("GateGuardList", false);
        boolean hasApprovalStdA = sp.getBoolean("ApproveListStdA", false);
        if (sp.getInt("isAdmin", 0) == 2 || sp.getInt("isAdmin", 0) == 3) {
            hasApprovalStdA = true;// 如果是班主任直接获取一审权限
        }
        boolean hasApprovalStdB = sp.getBoolean("ApproveListStdB", false);
        boolean hasApprovalStdC = sp.getBoolean("ApproveListStdC", false);
        boolean hasApprovalStdD = sp.getBoolean("ApproveListStdD", false);
        boolean hasApprovalStdE = sp.getBoolean("ApproveListStdE", false);
        boolean hasApprovalA = sp.getBoolean("ApproveListA", false);
        boolean hasApprovalB = sp.getBoolean("ApproveListB", false);
        boolean hasApprovalC = sp.getBoolean("ApproveListC", false);
        boolean hasApprovalD = sp.getBoolean("ApproveListD", false);
        boolean hasApprovalE = sp.getBoolean("ApproveListE", false);
        String LevelNoteStdA = sp.getString("LevelNoteStdA", null);
        String LevelNoteStdB = sp.getString("LevelNoteStdB", null);
        String LevelNoteStdC = sp.getString("LevelNoteStdC", null);
        String LevelNoteStdD = sp.getString("LevelNoteStdD", null);
        String LevelNoteStdE = sp.getString("LevelNoteStdE", null);
        String LevelNoteA = sp.getString("LevelNoteA", null);
        String LevelNoteB = sp.getString("LevelNoteB", null);
        String LevelNoteC = sp.getString("LevelNoteC", null);
        String LevelNoteD = sp.getString("LevelNoteD", null);
        String LevelNoteE = sp.getString("LevelNoteE", null);
        if (hasApprovalA) {
            approvalNoteList.add(LevelNoteA == null ? "一审" : LevelNoteA);
            approvalList.add(ROLE_1);
        }
        if (hasApprovalB) {
            approvalNoteList.add(LevelNoteB == null ? "二审" : LevelNoteB);
            approvalList.add(ROLE_2);
        }
        if (hasApprovalC) {
            approvalNoteList.add(LevelNoteC == null ? "三审" : LevelNoteC);
            approvalList.add(ROLE_3);
        }
        if (hasApprovalD) {
            approvalNoteList.add(LevelNoteD == null ? "四审" : LevelNoteD);
            approvalList.add(ROLE_4);
        }
        if (hasApprovalE) {
            approvalNoteList.add(LevelNoteE == null ? "五审" : LevelNoteE);
            approvalList.add(ROLE_5);
        }
        if (hasApprovalStdA) {
            approvalStuNoteList.add(LevelNoteStdA == null ? "一审"
                    : LevelNoteStdA);
            approvalStuList.add(ROLE_1);
        }
        if (hasApprovalStdB) {
            approvalStuNoteList.add(LevelNoteStdB == null ? "二审"
                    : LevelNoteStdB);
            approvalStuList.add(ROLE_2);
        }
        if (hasApprovalStdC) {
            approvalStuNoteList.add(LevelNoteStdC == null ? "三审"
                    : LevelNoteStdC);
            approvalStuList.add(ROLE_3);
        }
        if (hasApprovalStdD) {
            approvalStuNoteList.add(LevelNoteStdD == null ? "四审"
                    : LevelNoteStdD);
            approvalStuList.add(ROLE_4);
        }
        if (hasApprovalStdE) {
            approvalStuNoteList.add(LevelNoteStdE == null ? "五审"
                    : LevelNoteStdE);
            approvalStuList.add(ROLE_5);
        }
        isHasTea = approvalList.size() > 0;
        isHasStu = approvalStuList.size() > 0;
        getDefaultView();
    }

    /**
     * 如果有门卫权限，显示门卫审核标题
     */
    private void getDefaultView() {
        if (isGateGuard) {
            tv_gate.setVisibility(View.VISIBLE);
        } else {
            tv_gate.setVisibility(View.GONE);
        }
    }

    /**
     * 获取筛选条件获取的各个值
     */
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg1) {
            case 999:
                Bundle args = arg2.getExtras();
                if (args != null) {
                    try {
                        mBackTime = args.getString("ChoseTime");// 时间
                        /**
                         * 班级id
                         */
                        Integer mBackClassId = (Integer) args.get("ChoseClassId");// 班级ID
                        mBackType = (Integer) args.get("Type");// 教职工或学生，0学生，1教职工
                        mBackCheckRole = (Integer) args.get("CheckRole");// 审核权限，一审。。。
                        /**
                         * 选择的审核人名称
                         */
                        String mBackCheckerName = args
                                .getString("CheckerRoleName");// 选择的审核人名称
                        mBackClassName = args.getString("ChoseClassName");// 班级名字
                        mBackGradeName = args.getString("ChoseGradeName");// 年级名字
                        mBackCheckType = (Integer) args.get("CheckType");// 查询类型：0待审核，1已审核，2统计查询
                        if (mBackType == 1) {// 如果是教职工
                            mBackClassName = null;
                            mBackGradeName = null;
                        }
                        Log.i("onActivityResult", "时间-" + mBackTime + "-班级ID-"
                                + mBackClassId + "-教职工或学生-" + mBackType + "-审核权限-"
                                + mBackCheckRole + "-审核人姓名-" + mBackCheckerName
                                + "-班级名称-" + mBackClassName + "-年级名称-"
                                + mBackGradeName + "-查询类型-" + mBackCheckType);
                        setActionBarTitle(mBackCheckerName);
                        leaveList.clear();
                        gateLeaveList.clear();
                        setDefaultPageNumRowCount();
                        getHashMap();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.showMessage(mContext, "获取选取的筛选条件出现异常");
                }
                break;
            case 998:// 审核，门卫签字成功后返回列表自动刷新
            case 997:
                pageNum = 1;
                rowCount = 0;
                leaveList.clear();
                gateLeaveList.clear();
                getDefaultPost();
                break;
            default:
                break;
        }
    }

    /**
     * 设置PageNum RowCount值为获取第一页数据
     */
    private void setDefaultPageNumRowCount() {
        pageNum = 1;
        rowCount = 0;
    }

    /**
     * 处理发送请求获取的返回数据
     *
     * @param list list
     */
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case LeaveConstant.leave_GetUnitLeaves:// 审核人员取本单位的请假记录
            case LeaveConstant.leave_GetClassLeaves:// 班主任身份获取本班学生请假的审批记录
                DialogUtil.getInstance().cannleDialog();
                refreshScrollView.onRefreshComplete();
                UnitClassLeaves unitClassLeaves = (UnitClassLeaves) list.get(1);
                ArrayList<UnitClassLeaveModel> leaves = unitClassLeaves.getList();
                if (leaves != null && leaves.size() != 0) {
                    leaveList.addAll(leaves);
                    rowCount = leaves.get(0).getRowCount();
                    checkAdapter.setData(leaveList);
                } else {
                    checkAdapter.setData(null);
                    ToastUtil.showMessage(mContext,
                            R.string.leave_temporarily_no_data);
                }
                checkAdapter.setCheckType(1);
                checkAdapter.setCheckRole(mBackCheckRole);
                checkAdapter.notifyDataSetChanged();
                break;
            case LeaveConstant.leave_GetManSumLeaves:// 教职工请假统计
                DialogUtil.getInstance().cannleDialog();
                refreshScrollView.onRefreshComplete();
                @SuppressWarnings("unchecked")
                ArrayList<SumLeavesModel> s = (ArrayList<SumLeavesModel>) list
                        .get(1);
                if (s == null || s.size() == 0) {
                    queryAdapter.setData(null);
                    queryAdapter.notifyDataSetChanged();
                    ToastUtil.showMessage(mContext,
                            R.string.leave_temporarily_no_data);
                } else {
                    queryAdapter.setData(s);
                    queryAdapter.notifyDataSetChanged();
                }
                break;
            case LeaveConstant.leave_GetStudentSumLeaves:// 学生请假统计
                DialogUtil.getInstance().cannleDialog();
                refreshScrollView.onRefreshComplete();
                @SuppressWarnings("unchecked")
                ArrayList<StuSumLeavesModel> s1 = (ArrayList<StuSumLeavesModel>) list
                        .get(1);
                queryAdapter.setData(null);
                queryAdapter.notifyDataSetChanged();
                if (s1 == null || s1.size() == 0) {
                    ToastUtil.showMessage(mContext,
                            R.string.leave_temporarily_no_data);
                } else {
                    queryAdapter.setData(s1);
                    queryAdapter.notifyDataSetChanged();
                }
                break;
            case LeaveConstant.leave_GetClassSumLeaves:// 班级请假统计
                DialogUtil.getInstance().cannleDialog();
                refreshScrollView.onRefreshComplete();
                @SuppressWarnings("unchecked")
                ArrayList<ClassSumLeaveModel> s2 = (ArrayList<ClassSumLeaveModel>) list
                        .get(1);
                queryAdapter.setData(null);
                queryAdapter.notifyDataSetChanged();
                if (s2 == null || s2.size() == 0) {
                    ToastUtil.showMessage(mContext,
                            R.string.leave_temporarily_no_data);
                } else {
                    queryAdapter.setData(s2);
                    queryAdapter.setsDateTime(mBackTime);
                    queryAdapter.notifyDataSetChanged();
                }
                break;
            case LeaveConstant.leave_GetGateLeaves:// 门卫审核数据
                DialogUtil.getInstance().cannleDialog();
                refreshScrollView.onRefreshComplete();
                GateQueryLeaves gateLeave = (GateQueryLeaves) list.get(1);
                ArrayList<GateQueryLeaveModel> gateLeaves = gateLeave.getList();
                if (gateLeaves == null || gateLeaves.size() == 0) {
                    rowCount = 0;
                    ToastUtil.showMessage(mContext,
                            R.string.leave_temporarily_no_data);
                } else {
                    rowCount = gateLeaves.get(0).getRowCount();
                    gateLeaveList.addAll(gateLeaves);
                }
                checkAdapter.setCheckRole(ROLE_6);
                checkAdapter.setData(gateLeaveList);
                checkAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    /**
     * 不同的查询条件设置不同的请求条件
     */
    private void getHashMap() {
        if (mCheckType == mBackCheckType) {
            switch (mCheckType) {
                case TYPE_UNCHECK:// 待审核
                case TYPE_CHECKED:// 已审核
                    unitLeavesPost.setUnitId(unitId);
                    unitLeavesPost.setsDateTime(mBackTime);
                    unitLeavesPost.setGradeStr(mBackGradeName);
                    unitLeavesPost.setClassStr(mBackClassName);
                    unitLeavesPost.setManType(mBackType);
                    unitLeavesPost.setLevel(mBackCheckRole);
                    mCheckTypeGradeClassMap.put(mCheckType, unitLeavesPost);
                    break;
                case TYPE_QUERY:// 查询统计
                    mCheckTypeGradeClassMap.put(mCheckType, mBackTime);
                    mCheckTypeGradeClassMap.put(-mCheckType, mBackGradeName);
                    break;
                case TYPE_GATE:// 门卫审核
                    mCheckTypeGradeClassMap.put(mCheckType, mBackTime);
                    mCheckTypeGradeClassMap.put(-mCheckType, mBackGradeName);
                    break;
                default:
                    break;
            }
            getDefaultPost();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBusUtil.register(this);
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (mCheckTypeGradeClassMap.get(mCheckType) == null && mCheckType != 3) {
            refreshScrollView.onRefreshComplete();
            ToastUtil.showMessage(mContext, "请先选择筛选条件");
        } else {// 选取的标题不是门卫审核，并且已经选取了筛选条件
            pageNum = 1;
            rowCount = 0;
            leaveList.clear();
            gateLeaveList.clear();
            getDefaultPost();
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (mCheckTypeGradeClassMap.get(mCheckType) == null && mCheckType != 3) {
            refreshScrollView.onRefreshComplete();
            ToastUtil.showMessage(mContext, "请先选择筛选条件");
        } else {// 选取的标题不是门卫审核，并且已经选取了筛选条件
            switch (mCheckType) {
                case TYPE_GATE:// 门卫审核
                    judgeIsNoMore(rowCount, gateLeaveList);
                    break;
                case TYPE_CHECKED:// 已审核
                    judgeIsNoMore(rowCount, leaveList);
                    break;
                case TYPE_UNCHECK:// 待审核
                    judgeIsNoMore(rowCount, leaveList);
                    break;
                default:
                    ToastUtil.showMessage(mContext, "没有更多了");
                    refreshView.onRefreshComplete();
                    break;
            }
        }
    }

    /**
     * 判断是否还有更多
     *
     * @param RowCount  r
     * @param LeaveList l
     */
    private void judgeIsNoMore(int RowCount, ArrayList<?> LeaveList) {
        if (RowCount == LeaveList.size() || RowCount == 0) {
            ToastUtil.showMessage(mContext, "没有更多了");
            refreshScrollView.onRefreshComplete();
        } else {
            pageNum++;
            getDefaultPost();
        }
    }

    @Override
    public void onPullPageChanging(boolean isChanging) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
