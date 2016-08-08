package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.AtMeUser;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.umeng.analytics.MobclickAgent;

/**
 * 求知问题的答案列表
 *
 * @author admin
 */
public class QiuZhiQuestionAnswerListActivity extends BaseActivity implements
        OnRefreshListener2<ScrollView>, OnClickListener, Watcher {
    private PullToRefreshScrollView mPullRefreshScrollView;
    private QuestionIndexItem question;// 问题数据
    private boolean isAtted = false;// 是否关注
    private int questionTabID;// questionId
    private Context mContext;
    private TextView tv_answernum;// 显示回答数量的View
    private TextView tv_attnum;// 显示关注数量的View
    private TextView tv_clicknum;// 显示点击数量的View
    private TextView tv_answer;// 回答该问题
    private TextView tv_attquestion;// 关注问题
    /***
     * 全部 有证据 有内容 无内容
     *
     * @功能 可筛选答案
     */
    private TextView tv_all, tv_evidence, tv_havecontent, tv_nocontent;
    private QiuZhiAnswerListAdapter adapter;
    private static final int TYPE_ALL = -1;// 全部
    private static final int TYPE_EVIDENCE = 1;// 有证据
    private static final int TYPE_HAVECONTENT = 2;// 有内容
    private static final int TYPE_NOCONTENT = 0;// 无内容
    private static int TYPE_SELECT = TYPE_ALL;// 默认为全部
    // -1全部，0无内容，2有内容，1有证据的回答
    private HashMap<Integer, ArrayList<AnswerItem>> dataMap = new HashMap<>();
    private HashMap<Integer, Integer> pageNumMap = new HashMap<>();
    private HashMap<Integer, Integer> RowCountMap = new HashMap<>();
    private QuestionDetails questiondetails;
    private int clickNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            question = (QuestionIndexItem) savedInstanceState
                    .getSerializable("QuestionIndexItem");
        } else {
            initPassData();
        }
        initViews();
    }

    /**
     * @method self Defined
     * @功能 获取传递过来的数值
     */
    public void initPassData() {
        Intent getPass = getIntent();
        if (getPass != null) {
            Bundle bundle = getPass.getExtras();
            if (bundle != null) {
                question = (QuestionIndexItem) bundle
                        .getSerializable("QuestionIndexItem");
            }
        }
    }

    /**
     * 保存因意外销毁的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QuestionIndexItem", question);
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        setContentLayout(R.layout.activity_qiuzhi_question_answerlist);
        mContext = this;
        QiuZhiQuestionAnswerListActivityController.getInstance().setContext(
                this);
        WatchedEntityIndexItem.getInstance().addWatcher(this);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        TextView tv_question;// 显示question标题View
        TextView tv_topic;// 显示所属话题View
        tv_question = (TextView) findViewById(R.id.qiuzhi_question_tv_question);
        TextView tv_details;// 详情按钮
        tv_details = (TextView) findViewById(R.id.qiuzhi_question_tv_details);
        tv_topic = (TextView) findViewById(R.id.qiuzhi_question_tv_topic);
        tv_answernum = (TextView) findViewById(R.id.qiuzhi_question_tv_answernum);
        tv_attnum = (TextView) findViewById(R.id.qiuzhi_question_tv_attnum);
        tv_clicknum = (TextView) findViewById(R.id.qiuzhi_question_tv_clicknum);
        CusListView listView;
        listView = (CusListView) findViewById(R.id.qiuzhi_question_listview);
        tv_answer = (TextView) findViewById(R.id.qiuzhi_question_tv_answer);
        TextView tv_invite;// 邀请回答
        tv_invite = (TextView) findViewById(R.id.qiuzhi_question_tv_invite);
        tv_attquestion = (TextView) findViewById(R.id.qiuzhi_question_tv_attquestion);
        TextView tv_report;// 举报问题
        tv_report = (TextView) findViewById(R.id.qiuzhi_question_tv_report);
        LinearLayout layout_tab = (LinearLayout) findViewById(R.id.qiuzhi_question_layout_chose);
        layout_tab.setVisibility(View.VISIBLE);
        tv_all = (TextView) findViewById(R.id.qiuzhi_question_tv_all);
        tv_evidence = (TextView) findViewById(R.id.qiuzhi_question_tv_evidence);
        tv_havecontent = (TextView) findViewById(R.id.qiuzhi_question_tv_havecontent);
        tv_nocontent = (TextView) findViewById(R.id.qiuzhi_question_tv_nocontent);
        // 各个view的点击事件监听的注册
        tv_details.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_evidence.setOnClickListener(this);
        tv_havecontent.setOnClickListener(this);
        tv_nocontent.setOnClickListener(this);
        tv_answer.setOnClickListener(this);
        tv_invite.setOnClickListener(this);
        tv_attquestion.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        tv_question.setOnClickListener(this);

        dataMap.put(TYPE_ALL, new ArrayList<AnswerItem>());
        dataMap.put(TYPE_EVIDENCE, new ArrayList<AnswerItem>());
        dataMap.put(TYPE_HAVECONTENT, new ArrayList<AnswerItem>());
        dataMap.put(TYPE_NOCONTENT, new ArrayList<AnswerItem>());

        pageNumMap.put(TYPE_ALL, 1);
        pageNumMap.put(TYPE_EVIDENCE, 1);
        pageNumMap.put(TYPE_HAVECONTENT, 1);
        pageNumMap.put(TYPE_NOCONTENT, 1);

        RowCountMap.put(TYPE_ALL, 0);
        RowCountMap.put(TYPE_EVIDENCE, 0);
        RowCountMap.put(TYPE_HAVECONTENT, 0);
        RowCountMap.put(TYPE_NOCONTENT, 0);

        if (null != question) {
            questionTabID = question.getTabID();
            QiuZhiQuestionAnswerListActivityController.getInstance()
                    .QuestionDetail(questionTabID);
            setActionBarTitle(question.getTitle());
            tv_question.setText(question.getTitle());
            tv_topic.setText(question.getCategorySuject());
            tv_attnum.setText(String.valueOf(question.getAttCount()));
            tv_answernum.setText(String.valueOf(question.getAnswersCount()));
            clickNum = question.getViewCount();
            tv_clicknum.setText(String.valueOf(question.getViewCount()));
        }
        mPullRefreshScrollView.setOnRefreshListener(this);
        adapter = new QiuZhiAnswerListAdapter(mContext);
        adapter.setQuestion(question);
        listView.setAdapter(adapter);
        initPopWindowComment();
    }

    /**
     *
     * @param flag flag
     */
    private void choseTabFlag(int flag) {
        TYPE_SELECT = flag;
        switch (flag) {
            case TYPE_ALL:
                tv_all.setBackgroundResource(R.drawable.rounded_rectangle);
                tv_all.setTextColor(getResources().getColor(R.color.color_03ab35));
                tv_evidence.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_evidence.setTextColor(getResources().getColor(R.color.black));
                tv_havecontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_havecontent.setTextColor(getResources().getColor(R.color.black));
                tv_nocontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_nocontent.setTextColor(getResources().getColor(R.color.black));
                break;
            case TYPE_EVIDENCE:
                tv_all.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_evidence.setBackgroundResource(R.drawable.rounded_rectangle);
                tv_evidence.setTextColor(getResources().getColor(
                        R.color.color_03ab35));
                tv_havecontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_havecontent.setTextColor(getResources().getColor(R.color.black));
                tv_nocontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_nocontent.setTextColor(getResources().getColor(R.color.black));
                break;
            case TYPE_HAVECONTENT:
                tv_all.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_evidence.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_evidence.setTextColor(getResources().getColor(R.color.black));
                tv_havecontent.setBackgroundResource(R.drawable.rounded_rectangle);
                tv_havecontent.setTextColor(getResources().getColor(
                        R.color.color_03ab35));
                tv_nocontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_nocontent.setTextColor(getResources().getColor(R.color.black));
                break;
            case TYPE_NOCONTENT:
                tv_all.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_all.setTextColor(getResources().getColor(R.color.black));
                tv_evidence.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_evidence.setTextColor(getResources().getColor(R.color.black));
                tv_havecontent.setBackgroundColor(getResources().getColor(
                        R.color.color_ebebeb));
                tv_havecontent.setTextColor(getResources().getColor(R.color.black));
                tv_nocontent.setBackgroundResource(R.drawable.rounded_rectangle);
                tv_nocontent.setTextColor(getResources().getColor(
                        R.color.color_03ab35));
                break;
            default:
                break;
        }
        ArrayList<AnswerItem> data = dataMap.get(flag);
        if (data.size() == 0) {
            QiuZhiQuestionAnswerListActivityController.getInstance()
                    .GetAnswerById(questionTabID, pageNumMap.get(flag), flag);
        }
        adapter.setData(dataMap.get(flag));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBusUtil.register(this);
        MobclickAgent.onResume(this);
        try {
            String isOld = ACache.get(getApplicationContext(), "qiuzhi")
                    .getAsString("isOldList");
            if ("false".equals(isOld)) {
                QiuZhiQuestionAnswerListActivityController.getInstance()
                        .QuestionDetail(questionTabID);
                pageNumMap.put(TYPE_SELECT, 1);
                dataMap.get(TYPE_SELECT).clear();
                QiuZhiQuestionAnswerListActivityController.getInstance()
                        .GetAnswerById(questionTabID,
                                pageNumMap.get(TYPE_SELECT), TYPE_SELECT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_clicknum.setText(clickNum);
    }

    @Override
    public void onPause() {
        EventBusUtil.unregister(this);
        MobclickAgent.onPause(this);
        ACache.get(getApplicationContext(), "qiuzhi").put("isOldList", "true");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WatchedEntityIndexItem.getInstance().removeWatcher(this);
        DialogUtil.getInstance().cannleDialog();
    }

    /**
     * EventBus功能模块
     *
     * @param list list
     * @功能 获取网络返回数据 并处理
     */
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_qiuzhi_GetAnswerById:
                // 功能：获取问题的答案 列表;
                mPullRefreshScrollView.onRefreshComplete();
                @SuppressWarnings("unchecked")
                ArrayList<AnswerItem> answerlist = (ArrayList<AnswerItem>) list
                        .get(1);
                if (null != answerlist && answerlist.size() > 0) {
                    RowCountMap.put(TYPE_SELECT, answerlist.size());
                    if (pageNumMap.get(TYPE_SELECT) == 1) {
                        dataMap.get(TYPE_SELECT).clear();
                    }
                    dataMap.get(TYPE_SELECT).addAll(answerlist);
                    if (answerlist.size() < 10) {
                        if (dataMap.get(TYPE_SELECT).size() != answerlist.size()) {
                            ToastUtil.showMessage(mContext, getResources()
                                    .getString(R.string.have_no_more));
                        }
                    }
                } else {
                    if (dataMap.get(TYPE_SELECT).size() > 0) {
                        ToastUtil.showMessage(mContext,
                                getResources().getString(R.string.have_no_more));
                    } else {
                        ToastUtil.showMessage(mContext,
                                getResources().getString(R.string.have_no_answer));
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case Constant.msgcenter_qiuzhi_AddMyAttQ:
                // 添加关注结果
                String result = (String) list.get(1);
                if (TextUtils.isEmpty(result)) {
                    ToastUtil.showMessage(mContext, R.string.fuck_failed);
                } else {
                    isAtted = true;
                    tv_attquestion.setText(getResources().getString(
                            R.string.unfollow));
                    ToastUtil.showMessage(mContext, R.string.follow_success);
                    question.setAttCount(question.getAttCount() + 1);
                    WatchedEntityIndexItem.getInstance().notifyWatcher(question);
                }
                break;
            case Constant.msgcenter_qiuzhi_RemoveMyAttQ:
                // 取消关注 结果
                String result1 = (String) list.get(1);
                if (TextUtils.isEmpty(result1)) {
                    ToastUtil.showMessage(mContext, R.string.fuck_failed);
                } else {
                    isAtted = false;
                    tv_attquestion.setText(getResources().getString(
                            R.string.follow_question));
                    ToastUtil.showMessage(mContext, R.string.unfollow_success);
                }
                break;
            case Constant.msgcenter_qiuzhi_AtMeForAnswer:
                // 我回答的问题结果
                popupWindowComment.dismiss();
                BaseUtils.hidepan(pp_input);
                String atmeResult = (String) list.get(1);
                if (TextUtils.isEmpty(atmeResult)) {
                    ToastUtil.showMessage(mContext, R.string.fuck_failed);
                } else if ("0".equals(atmeResult)) {
                    ToastUtil.showMessage(mContext, R.string.invite_success);
                    pp_input.setText("");
                } else if ("-1".equals(atmeResult)) {
                    ToastUtil.showMessage(mContext,
                            R.string.this_user_hasBeenInvited);
                    pp_input.setText("");
                } else if ("-2".equals(atmeResult)) {
                    ToastUtil.showMessage(mContext, R.string.this_user_hasAnswered);
                    pp_input.setText("");
                }
                break;
            case Constant.msgcenter_qiuzhi_GetAtMeUsers:
                // 邀请人回答时，获取回答该话题问题最多的用户列表（4个）
                @SuppressWarnings("unchecked")
                ArrayList<AtMeUser> users = (ArrayList<AtMeUser>) list.get(1);
                if (users != null && users.size() > 0) {
                    QiuZhiQuestionAnswerListActivityController.getInstance()
                            .AtMeForAnswer(users.get(0).getJiaoBaoHao(),
                                    question.getTabID());
                } else {
                    ToastUtil.showMessage(mContext, R.string.no_this_user);
                }
                break;
            case Constant.msgcenter_qiuzhi_QuestionDetail:
                // 功能：获取一个问题明细信息，包括问题内容;
                questiondetails = (QuestionDetails) list.get(1);
                if (questiondetails == null) {
                    ToastUtil.showMessage(mContext, R.string.this_question_deleted);
                    mPullRefreshScrollView.setVisibility(View.GONE);
                    return;
                }
                choseTabFlag(TYPE_ALL);
                if (questiondetails.getMyAnswerId() != 0) {
                    tv_answer.setText(getResources().getString(
                            R.string.change_answer));
                } else {
                    tv_answer.setText(getResources().getString(
                            R.string.answer_theQuestion));
                }
                if (questiondetails.getTag() == 0) {
                    tv_attquestion.setText(getResources().getString(
                            R.string.follow_question));
                    isAtted = false;
                } else {
                    tv_attquestion.setText(getResources().getString(
                            R.string.unfollow));
                    isAtted = true;
                }
                break;
            case Constant.msgcenter_qiuzhi_ReportAns:
                // 举报答案 结果
                String r = (String) list.get(1);
                if (r.equals("1")) {
                    ToastUtil.showMessage(mContext,
                            R.string.this_user_hasBeenReported);
                } else {
                    ToastUtil.showMessage(mContext, R.string.fuck_success);//
                }
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param refreshView r
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pageNumMap.put(TYPE_SELECT, 1);
        dataMap.get(TYPE_SELECT).clear();
        QiuZhiQuestionAnswerListActivityController.getInstance().GetAnswerById(
                questionTabID, pageNumMap.get(TYPE_SELECT), TYPE_SELECT);
    }

    /**
     * 上拉加载
     *
     * @功能 加载下一页数据
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (RowCountMap.get(TYPE_SELECT) < 10) {
            ToastUtil.showMessage(mContext, R.string.no_more);
            mPullRefreshScrollView.onRefreshComplete();
        } else {
            pageNumMap.put(TYPE_SELECT, pageNumMap.get(TYPE_SELECT) + 1);
            QiuZhiQuestionAnswerListActivityController.getInstance()
                    .GetAnswerById(questionTabID, pageNumMap.get(TYPE_SELECT),
                            TYPE_SELECT);
        }
    }

    /**
     *
     * @param v v
     */
    @Override
    public void onClick(View v) {
        UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
                .getAsObject("userInfo");
        switch (v.getId()) {
            case R.id.qiuzhi_question_tv_answer:
                // 回答问题
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_answerQuestion));
                if (userInfo.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                if (userInfo.getNickName() == null
                        || userInfo.getNickName().equals("")) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nonick));
                    return;
                }
                Intent intent = new Intent(mContext,
                        QiuZhiQuestionDetailsActivity.class);
                intent.putExtra("QuestionIndexItem", question);
                intent.putExtra("QuestionDetails", questiondetails);
                startActivity(intent);
                break;
            case R.id.qiuzhi_question_tv_details:
                // 问题详情
            case R.id.qiuzhi_question_tv_question:
                // 问题标题
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_questionTitleDetail));
                clickNum = clickNum + 1;
                Intent intent1 = new Intent(mContext,
                        QiuZhiQuestionDetailsActivity.class);
                question.setViewCount(clickNum);
                questiondetails.setViewCount(clickNum);
                intent1.putExtra("QuestionIndexItem", question);
                startActivity(intent1);
                break;
            case R.id.qiuzhi_question_tv_invite:
                // 邀请回答
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_inviteAnswer));
                if (userInfo.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                if (userInfo.getNickName() == null
                        || userInfo.getNickName().equals("")) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nonick));
                    return;
                }
                showPopComment();
                break;
            case R.id.qiuzhi_question_tv_attquestion:
                // 关注问题
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_attQuestion));
                if (userInfo.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                if (isAtted) {
                    QiuZhiQuestionAnswerListActivityController.getInstance()
                            .RemoveMyAttQ(question.getTabID());
                } else {
                    QiuZhiQuestionAnswerListActivityController.getInstance()
                            .AddMyAttQ(question.getTabID());
                }
                break;
            case R.id.qiuzhi_question_tv_report:
                // 举报问题
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_reportQuestion));
                if (userInfo.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                BaseUtils.createDialog(mContext, R.string.report_question,
                        R.string.report_orNot, android.R.drawable.ic_dialog_alert,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                QiuZhiQuestionAnswerListActivityController
                                        .getInstance().ReportAns(questionTabID, 1);
                            }
                        }).show();

                break;
            case R.id.qiuzhi_question_tv_all:
                // 全部
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_all));
                choseTabFlag(TYPE_ALL);
                break;
            case R.id.qiuzhi_question_tv_evidence:
                // 有证据
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_evidence));
                choseTabFlag(TYPE_EVIDENCE);
                break;
            case R.id.qiuzhi_question_tv_havecontent:
                // 有内容
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_content));
                choseTabFlag(TYPE_HAVECONTENT);
                break;
            case R.id.qiuzhi_question_tv_nocontent:
                // 无内容
                MobclickAgent.onEvent(
                        mContext,
                        mContext.getResources().getString(
                                R.string.QuestionAnswer_noContent));
                choseTabFlag(TYPE_NOCONTENT);
                break;

            default:
                break;
        }
    }

    /**
     *
     * @param keyCode jian
     * @param event s
     * @return b
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示popWindow
     */
    public void showPopComment() {
        pp_input.setFocusable(true);
        pp_input.setFocusableInTouchMode(true);
        pp_input.requestFocus();
        pp_input.requestFocusFromTouch();
        BaseUtils.openpan(pp_input);
        popupWindowComment.setFocusable(true);
        popupWindowComment.setOutsideTouchable(true);
        popupWindowComment.setBackgroundDrawable(new PaintDrawable());
        popupWindowComment.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindowComment
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindowComment.update();
        popupWindowComment.showAtLocation(mPullRefreshScrollView,
                Gravity.NO_GRAVITY, 0, Constant.ScreenHeight);
    }

    private PopupWindow popupWindowComment;

    private IEditText pp_input;

    /**
     * 初始化popWindow
     */
    private void initPopWindowComment() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View popView = inflater.inflate(R.layout.popup_artlistcomment, null);
        popupWindowComment = new PopupWindow(popView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ImageView pp_comment;
        pp_comment = (ImageView) popView
                .findViewById(R.id.item_show2_iv_commit);
        pp_comment.setImageResource(R.drawable.btn_worksend_invert);
        pp_input = (IEditText) popView.findViewById(R.id.item_show2_edt_commit);
        pp_input.setMaxLines(50);
        pp_input.setHint(getResources().getString(
                R.string.input_invitedPerson_nickname_orJiaoBao_orEMail));
        pp_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String str_comm = pp_input.getText().toString();
                    if (TextUtils.isEmpty(str_comm.trim())) {
                        ToastUtil
                                .showMessage(
                                        mContext,
                                        R.string.input_invitedPerson_nickname_orJiaoBao_orEMail);
                    } else {
                        QiuZhiQuestionAnswerListActivityController
                                .getInstance().GetAtMeUsers(str_comm,
                                questiondetails.getCategoryId());
                    }
                } catch (Exception e) {
                    ToastUtil.showMessage(mContext,
                            R.string.dataUnusual_closePage_refreshAndRetry);
                }
            }
        });
    }

    @Override
    public void update(QuestionIndexItem qEntity) {
        if (null != qEntity) {
            tv_attnum.setText(String.valueOf(qEntity.getAttCount()));
            tv_answernum.setText(String.valueOf(qEntity.getAnswersCount()));
            tv_clicknum.setText(String.valueOf(qEntity.getViewCount()));
        }
    }

    @Override
    public void update(AnswerItem answer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPullPageChanging(boolean isChanging) {
        // TODO Auto-generated method stub

    }
}
