package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerComment;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.AnswerRefcomment;
import com.jsy_jiaobao.po.qiuzhi.GetAnswerComments;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;

/**
 * 求知答案详情界面
 *
 * @author admin
 */
public class QiuZhiQuestionAnswerDetailsActivity extends BaseActivity implements
        OnRefreshListener2<ScrollView>, OnClickListener, Watcher {

    private PullToRefreshScrollView mPullRefreshScrollView;
    private QuestionIndexItem question;// 问题
    private int answerID;// answerId
    private AnswerItem answer;// 答案数据
    private Context mContext;
    private int pageNum = 1;// 请求页码
    private int RowCount = 0;// rowCount
    private TextView tv_question;// 问题View
    private TextView tv_topic;// 话题
    private TextView tv_answernum;// 回答数目
    private TextView tv_attnum;// 关注数目
    private TextView tv_clicknum;// 点击数目

    private ImageView iv_photo;// 头像
    private TextView tv_likenum;// 赞数量
    private ImageView iv_like;
    private TextView tv_name;// 作者名称
    private WebView web_detailsgist;
    private LinearLayout layout_web;
    private TextView tv_detailstime;// 时间
    private TextView tv_report;//
    private TextView tv_commit;// 评论
    private TextView tv_against;
    private QiuZhiAnswerCommentsListAdapter adapter;
    // 评论
    ArrayList<AnswerComment> commentsList = new ArrayList<>();
    // 评论的回复
    ArrayList<AnswerRefcomment> refcommentsList = new ArrayList<>();
    private int questionID;
    private int isAnswer;// 0答案 1问题
    private TextView tv_details;
    private TextView tv_answerHead;
    private TextView img_answerHead;
    private TextView img_answerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            question = (QuestionIndexItem) savedInstanceState
                    .getSerializable("QuestionIndexItem");
            answer = (AnswerItem) savedInstanceState.getSerializable("Answer");
            answerdetails = (AnswerDetails) savedInstanceState
                    .getSerializable("answerDetails");
            isAnswer = savedInstanceState.getInt("IsAnswer");
            if (question == null) {
                questionID = savedInstanceState.getInt("QuestionID");
            }
            if (answer != null) {
                answerID = answer.getTabID();
            } else if (answerdetails != null) {
                answerID = answerdetails.getTabID();
            } else {
                answerID = savedInstanceState.getInt("AnswerID");
            }
        } else {
            initPassData();
        }
        initViews();
        setViewState(isAnswer);
    }

    /**
     * 判断是哪个界面传进来的 控制界面显示状态
     *
     * @param isAnswer f
     */
    private void setViewState(int isAnswer) {
        // TODO Auto-generated method stub
        if (isAnswer == 1) {
            tv_question.setClickable(false);
            tv_details.setVisibility(View.GONE);
        } else {
            tv_details.setVisibility(View.VISIBLE);
            tv_question.setClickable(true);
        }
    }

    /**
     * @method selfDefined
     * @功能 获取传过来的数值
     */
    public void initPassData() {
        Intent getPass = getIntent();
        if (getPass != null) {
            Bundle bundle = getPass.getExtras();
            if (bundle != null) {
                question = (QuestionIndexItem) bundle
                        .getSerializable("QuestionIndexItem");
                answer = (AnswerItem) bundle.getSerializable("Answer");
                answerdetails = (AnswerDetails) bundle
                        .getSerializable("answerDetails");
                isAnswer = bundle.getSerializable("IsAnswer") == null ? 0
                        : (Integer) bundle.getSerializable("IsAnswer");
                if (question == null) {
                    questionID = bundle.getInt("QuestionID");
                }
                if (answer != null) {
                    answerID = answer.getTabID();
                } else if (answerdetails != null) {
                    answerID = answerdetails.getTabID();
                } else {
                    answerID = bundle.getInt("AnswerID");
                }
            }
        }
    }

    /**
     * 保存可能意外销毁的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QuestionIndexItem", question);
        outState.putSerializable("Answer", answer);
        outState.putInt("AnswerID", answerID);
        outState.putInt("QuestionID", questionID);
        outState.putSerializable("answerdetails", answerdetails);
        outState.putInt("IsAnswer", isAnswer);
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        setContentLayout(R.layout.activity_qiuzhi_question_answerdetails);
        mContext = this;
        ACache.get(getApplicationContext(), "qiuzhi").put("isNewQuestion",
                "false");
        initPopWindowComment();
        QiuZhiQuestionAnswerDetailsActivityController.getInstance().setContext(
                this);
        WatchedEntityIndexItem.getInstance().addWatcher(this);

        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        tv_answerHead = (TextView) findViewById(R.id.tv_answer_head);
        img_answerHead = (TextView) findViewById(R.id.img_answerHead);
        img_answerContent = (TextView) findViewById(R.id.img_answerContent);
        tv_question = (TextView) findViewById(R.id.qiuzhi_answer_tv_question);
        tv_topic = (TextView) findViewById(R.id.qiuzhi_answer_tv_topic);
        tv_answernum = (TextView) findViewById(R.id.qiuzhi_answer_tv_answernum);
        tv_attnum = (TextView) findViewById(R.id.qiuzhi_answer_tv_attnum);
        tv_clicknum = (TextView) findViewById(R.id.qiuzhi_answer_tv_clicknum);
        CusListView listView;// 列表
        listView = (CusListView) findViewById(R.id.qiuzhi_answer_listview);
        iv_photo = (ImageView) findViewById(R.id.qiuzhi_answer_iv_photo);
        tv_likenum = (TextView) findViewById(R.id.qiuzhi_answer_tv_likenum);
        iv_like = (ImageView) findViewById(R.id.qiuzhi_answer_iv_like);
        tv_name = (TextView) findViewById(R.id.qiuzhi_answer_tv_name);
        layout_web = (LinearLayout) findViewById(R.id.qiuzhi_answer_layout_web);
        tv_detailstime = (TextView) findViewById(R.id.qiuzhi_answer_tv_detailstime);
        tv_report = (TextView) findViewById(R.id.qiuzhi_answer_tv_report);
        tv_commit = (TextView) findViewById(R.id.qiuzhi_answer_tv_commit);
        tv_against = (TextView) findViewById(R.id.qiuzhi_answer_tv_against);

        tv_details = (TextView) findViewById(R.id.qiuzhi_answer_tv_question1);

        tv_details.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        tv_against.setOnClickListener(this);
        tv_question.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        tv_likenum.setOnClickListener(this);
        mPullRefreshScrollView.setOnRefreshListener(this);
        adapter = new QiuZhiAnswerCommentsListAdapter(mContext);
        adapter.setData(commentsList);
        adapter.setRefcomments(refcommentsList);
        listView.setAdapter(adapter);
        web_detailsgist = new WebView(mContext);
        WebSetUtils.getWebSetting(this, web_detailsgist);
        //
        if (answerdetails == null) {
            QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                    .AnswerDetail(answerID);
        } else {
            initAnswerDetails();
        }
        if (null != question) {
            setActionBarTitle(question.getTitle());
            tv_question.setText(question.getTitle());
            tv_topic.setText(question.getCategorySuject());
            tv_answernum.setText(String.valueOf(question.getAnswersCount()));
            tv_attnum.setText(String.valueOf(question.getAttCount()));
            tv_clicknum.setText(String.valueOf(question.getViewCount()));
        } else {
            QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                    .QuestionDetail(questionID);
        }

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UserInfo userInfo = (UserInfo) ACache.get(
                        getApplicationContext()).getAsObject("userInfo");
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
                AnswerComment ac = commentsList.get(position);
                commentDialog(ac);

            }
        });

    }

    @SuppressWarnings("deprecation")
    private void initAnswerDetails() {
        if (null != answerdetails) {
            QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                    .CommentsList(answerdetails.getTabID(), pageNum, RowCount);
            if (TextUtils.isEmpty(answerdetails.getIdFlag())) {
                tv_name.setText("匿名");
                JSYApplication.getInstance().bitmap.display(iv_photo, "");
            } else {
                String url = ACache.get(mContext.getApplicationContext())
                        .getAsString("MainUrl")
                        + ConstantUrl.photoURL
                        + "?AccID=" + answerdetails.getJiaoBaoHao();
                JSYApplication.getInstance().bitmap.display(iv_photo, url);
                tv_name.setText(answerdetails.getIdFlag());
            }
            tv_likenum.setText(String.valueOf(answerdetails.getLikeCount()));
            tv_against.setText(mContext.getString(R.string.against, answerdetails.getCaiCount()));
            tv_detailstime
                    .setText(answerdetails.getRecDate().replace("T", " "));
            String con = answerdetails.getAContent();

            String tit = answerdetails.getATitle();
            tv_answerHead.setText(tit);

            // 0无内容1有内容2有证据
            if (answerdetails.getFlag() == 1) {
                img_answerHead.setBackground(getResources().getDrawable(
                        R.drawable.icon_qiuzhi_answer_));
                img_answerContent.setBackground(getResources().getDrawable(
                        R.drawable.icon_qiuzhi_content_));
            } else if (answerdetails.getFlag() == 2) {
                if (TextUtils.isEmpty(con) || con.equals("<p></p>")) {
                    img_answerContent.setBackground(getResources().getDrawable(
                            R.drawable.icon_qiuzhi_nocontent_));
                } else {
                    img_answerContent.setBackground(getResources().getDrawable(
                            R.drawable.icon_qiuzhi_gist_));
                }
            } else {
                img_answerContent.setBackground(getResources().getDrawable(
                        R.drawable.icon_qiuzhi_nocontent_));
            }

            web_detailsgist.loadUrl(con);
            tv_commit.setText(mContext.getString(R.string.comment_number, answerdetails.getCCount()));

            if (layout_web.getChildCount() == 0) {
                layout_web.addView(web_detailsgist);
            }
        } else {
            tv_commit.setEnabled(false);
            tv_against.setEnabled(false);
            tv_report.setEnabled(false);
            tv_likenum.setEnabled(false);
            iv_like.setEnabled(false);
            DialogUtil.getInstance().cannleDialog();
            ACache.get(getApplicationContext(), "qiuzhi").put("isOld", "false");
            ToastUtil.showMessage(mContext, R.string.record_isDel_orShielded);
            mPullRefreshScrollView.setVisibility(View.GONE);
        }
    }

    private void commentDialog(final AnswerComment ac) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(android.R.drawable.ic_menu_help).setTitle("请选择")
                .setPositiveButton("回复", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        str_clickComment = ac.getNumber();
                        pp_comment.setTag(ac.getTabID());
                        String str = "回复" + str_clickComment + ":";
                        showPopComment();
                        BaseUtils.openpan(pp_input);
                        pp_input.setText(str);
                        pp_input.requestFocus();
                        pp_input.setSelection(str.length());
                    }
                })
                .setNegativeButton("举报", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BaseUtils.createDialog(mContext,
                                R.string.report_comment, R.string.report_orNot,
                                android.R.drawable.ic_dialog_alert,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        QiuZhiQuestionAnswerDetailsActivityController
                                                .getInstance().ReportAns(
                                                ac.getTabID(), 2);
                                    }
                                }).show();
                    }
                }).show();
    }

    String str_clickComment;


    @Override
    public void onResume() {
        EventBusUtil.register(this);
        super.onResume();
        String isNewQuestion = ACache.get(getApplicationContext(), "qiuzhi")
                .getAsString("isNewQuestion");
        if ("true".equals(isNewQuestion)) {
            QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                    .QuestionDetail(questionID);
        }
    }

    @Override
    public void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WatchedEntityIndexItem.getInstance().removeWatcher(this);
    }

    AnswerDetails answerdetails;

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_qiuzhi_AddScoreKnl:
                DialogUtil.getInstance().cannleDialog();
                AnswerComment comment = (AnswerComment) list.get(1);
                if (comment != null) {
                    int yn = (Integer) list.get(2);
                    if (yn == 1) {
                        ToastUtil.showMessage(mContext, "谢谢支持");
                    } else {
                        ToastUtil.showMessage(mContext, "你的意见是我前进的最大动力");
                    }
                    for (int i = 0; i < commentsList.size(); i++) {
                        if (comment.getTabID() == commentsList.get(i).getTabID()) {
                            commentsList.set(i, comment);
                            break;
                        }
                    }
                } else {
                    ToastUtil.showMessage(mContext, "请求失败");
                }
                adapter.notifyDataSetChanged();
                break;
            case Constant.msgcenter_qiuzhi_AnswerDetail:
                answerdetails = (AnswerDetails) list.get(1);
                initAnswerDetails();
                break;
            case Constant.msgcenter_qiuzhi_CommentsList:
                mPullRefreshScrollView.onRefreshComplete();
                GetAnswerComments comments = (GetAnswerComments) list.get(1);
                if (null != comments) {
                    RowCount = comments.getRowCount();
                    ArrayList<AnswerComment> commentslist = comments
                            .getCommentsList();
                    ArrayList<AnswerRefcomment> refcommentslist = comments
                            .getRefcomments();
                    if (null != commentslist) {
                        commentsList.addAll(commentslist);
                    }
                    if (null != refcommentslist) {
                        refcommentsList.addAll(refcommentslist);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case Constant.msgcenter_qiuzhi_ReportAns:
                String r = (String) list.get(1);
                if (r.equals("1")) {
                    ToastUtil.showMessage(mContext,
                            R.string.this_user_hasBeenReported);
                } else {
                    ToastUtil.showMessage(mContext, R.string.fuck_success);
                }
                break;
            case Constant.msgcenter_qiuzhi_SetYes:
                String r1 = (String) list.get(1);
                if (TextUtils.isEmpty(r1)) {
                    ToastUtil.showMessage(mContext,
                            getResources().getString(R.string.public_syserr));
                } else if ("-1".equals(r1)) {
                    ToastUtil.showMessage(mContext, R.string.viewpoint_reported);
                } else if ("1".equals(r1)) {
                    ToastUtil.showMessage(mContext,
                            getResources().getString(R.string.public_sysright));
                    answerdetails.setLikeCount(answerdetails.getLikeCount() + 1);
                    tv_likenum
                            .setText(String.valueOf(answerdetails.getLikeCount()));
                    if (answer != null) {
                        answer.setLikeCount(answer.getLikeCount() + 1);
                        WatchedEntityIndexItem.getInstance().notifyAnswerList(
                                answer);
                    }
                } else if ("-2".equals(r1)) {
                    ToastUtil.showMessage(mContext,
                            R.string.record_isDel_orShielded);
                }
                break;
            case Constant.msgcenter_qiuzhi_SetNo:
                String r11 = (String) list.get(1);
                if (TextUtils.isEmpty(r11)) {
                    ToastUtil.showMessage(mContext,
                            getResources().getString(R.string.public_syserr));
                } else if ("-1".equals(r11)) {
                    ToastUtil.showMessage(mContext, R.string.viewpoint_reported);
                } else if ("1".equals(r11)) {
                    answerdetails.setCaiCount(answerdetails.getCaiCount() + 1);
                    tv_against.setText(mContext.getString(R.string.against_number, answerdetails.getCaiCount()));
                    if (answer != null) {
                        answer.setCaiCount(answer.getCaiCount() + 1);
                        WatchedEntityIndexItem.getInstance().notifyAnswerList(
                                answer);
                    }
                    ToastUtil.showMessage(mContext,
                            getResources().getString(R.string.public_sysright));
                } else if ("-2".equals(r11)) {
                    ToastUtil.showMessage(mContext,
                            R.string.record_isDel_orShielded);
                }
                break;
            case Constant.msgcenter_qiuzhi_AddComment:
                pp_comment.setEnabled(true);
                String r111 = (String) list.get(1);
                if (TextUtils.isEmpty(r111)) {
                    DialogUtil.getInstance().cannleDialog();
                } else if ("-2".equals(r111)) {
                    DialogUtil.getInstance().cannleDialog();
                    ToastUtil.showMessage(mContext,
                            R.string.record_isDel_orShielded);
                } else {
                    popupWindowComment.dismiss();
                    answerdetails.setCCount(answerdetails.getCCount() + 1);
                    tv_commit.setText(getString(R.string.comment_number, answerdetails.getCCount()));
                    pp_input.setText("");
                    pageNum = 1;
                    RowCount = 0;
                    commentsList.clear();
                    refcommentsList.clear();
                    QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                            .CommentsList(answerdetails.getTabID(), pageNum,
                                    RowCount);
                    if (answer != null) {
                        answer.setCCount(answer.getCCount() + 1);
                        WatchedEntityIndexItem.getInstance().notifyAnswerList(
                                answer);
                    }
                }
                break;
            case Constant.msgcenter_qiuzhi_QuestionDetail:
                questiondetails = (QuestionDetails) list.get(1);
                if (questiondetails == null) {
                    tv_commit.setEnabled(false);
                    tv_against.setEnabled(false);
                    tv_report.setEnabled(false);
                    tv_likenum.setEnabled(false);
                    iv_like.setEnabled(false);
                    DialogUtil.getInstance().cannleDialog();
                    ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
                            "false");
                    ToastUtil.showMessage(mContext,
                            R.string.record_isDel_orShielded);
                    mPullRefreshScrollView.setVisibility(View.GONE);
                    break;
                }
                setActionBarTitle(questiondetails.getTitle());
                tv_question.setText(questiondetails.getTitle());
                tv_topic.setText(questiondetails.getCategory());
                tv_answernum.setText(String.valueOf(questiondetails
                        .getAnswersCount()));
                tv_attnum.setText(String.valueOf(questiondetails.getAttCount()));
                tv_clicknum.setText(String.valueOf(questiondetails.getViewCount()));

                if (question == null) {
                    question = new QuestionIndexItem();
                    question.setTabID(questiondetails.getTabID());
                    question.setTitle(questiondetails.getTitle());
                    question.setCategorySuject(questiondetails.getCategory());
                    question.setAnswersCount(questiondetails.getAnswersCount());
                    question.setViewCount(questiondetails.getViewCount());
                    question.setAttCount(questiondetails.getAttCount());
                }
                break;
            default:
                break;
        }
    }

    QuestionDetails questiondetails;

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pageNum = 1;
        RowCount = 0;
        commentsList.clear();
        refcommentsList.clear();
        QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                .CommentsList(answerdetails.getTabID(), pageNum, RowCount);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (RowCount == commentsList.size()) {
            ToastUtil.showMessage(mContext, R.string.no_more);
            mPullRefreshScrollView.onRefreshComplete();
        } else {
            pageNum++;
            QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                    .CommentsList(answerdetails.getTabID(), pageNum, RowCount);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qiuzhi_answer_tv_question1:
            case R.id.qiuzhi_answer_tv_question:
                Intent intent = new Intent(mContext,
                        QiuZhiQuestionDetailsActivity.class);

                intent.putExtra("QuestionIndexItem", question);
                intent.putExtra("QuestionDetails", questiondetails);
                startActivity(intent);
                break;
            case R.id.qiuzhi_answer_tv_report:
                UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
                        .getAsObject("userInfo");
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
                BaseUtils.createDialog(mContext, R.string.report_answer,
                        R.string.report_orNot, android.R.drawable.ic_dialog_alert,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                QiuZhiQuestionAnswerDetailsActivityController
                                        .getInstance().ReportAns(answerID, 0);
                            }
                        }).show();
                break;
            case R.id.qiuzhi_answer_tv_against:
                UserInfo userInfo1 = (UserInfo) ACache.get(getApplicationContext())
                        .getAsObject("userInfo");
                if (userInfo1.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo1.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                // if ("0".equals(answerdetails.getLikeList())) {
                // ToastUtil.showMessage(mContext, "重复操作");
                // }else{
                QiuZhiQuestionAnswerDetailsActivityController.getInstance().SetNo(
                        answerID);
                // }
                break;
            case R.id.qiuzhi_answer_tv_commit:
                UserInfo userInfo11 = (UserInfo) ACache
                        .get(getApplicationContext()).getAsObject("userInfo");
                if (userInfo11.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo11.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                if (userInfo11.getNickName() == null
                        || userInfo11.getNickName().equals("")) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nonick));
                    return;
                }
                int tag = (Integer) pp_comment.getTag();
                if (tag != 0) {

                    pp_input.setText("");
                }
                pp_comment.setTag(0);
                showPopComment();
                break;
            case R.id.qiuzhi_answer_iv_like:
            case R.id.qiuzhi_answer_tv_likenum:
                UserInfo userInfo111 = (UserInfo) ACache.get(
                        getApplicationContext()).getAsObject("userInfo");
                if (userInfo111.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return;
                }
                if (userInfo111.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return;
                }
                // if ("0".equals(answerdetails.getLikeList())) {
                // ToastUtil.showMessage(mContext, "重复操作");
                // }else{
                QiuZhiQuestionAnswerDetailsActivityController.getInstance().SetYes(
                        answerID);
                // }
                break;

            default:
                break;
        }
    }

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
    private ImageView pp_comment;
    private IEditText pp_input;

    /**
     * 初始化popWindow
     */
    private void initPopWindowComment() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View popView = inflater.inflate(R.layout.popup_artlistcomment,
                mPullRefreshScrollView, false);
        popupWindowComment = new PopupWindow(popView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pp_comment = (ImageView) popView
                .findViewById(R.id.item_show2_iv_commit);
        pp_comment.setTag(0);
        pp_input = (IEditText) popView.findViewById(R.id.item_show2_edt_commit);
        pp_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UserInfo userInfo = (UserInfo) ACache.get(
                        getApplicationContext()).getAsObject("userInfo");
                if (userInfo.getNickName() == null
                        || userInfo.getNickName().equals("")) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nonick));
                    return;
                }
                pp_comment.setEnabled(false);
                String str_comm = pp_input.getText().toString();
                String[] str_comms = str_comm.split(getResources().getString(
                        R.string.reply)
                        + str_clickComment + ":");
                if (str_comms.length == 2) {
                    str_comm = slect(str_comms[1]);
                }
                int RefID = (Integer) v.getTag();
                if (TextUtils.isEmpty(str_comm.trim())
                        || (getResources().getString(R.string.reply)
                        + str_clickComment + ":").equals(str_comm)) {
                    ToastUtil.showMessage(mContext,
                            R.string.please_input_content);
                    pp_comment.setEnabled(true);
                } else {
                    QiuZhiQuestionAnswerDetailsActivityController.getInstance()
                            .AddComment(answerID, str_comm, RefID);

                }
            }
        });
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

    @Override
    public void update(QuestionIndexItem qEntity) {
        if (null != qEntity) {
            tv_answernum.setText(String.valueOf(qEntity.getAnswersCount()));
            tv_attnum.setText(String.valueOf(qEntity.getAttCount()));
            tv_clicknum.setText(String.valueOf(qEntity.getViewCount()));
        }
    }

    @Override
    public void update(AnswerItem answer) {

    }

    @Override
    public void onPullPageChanging(boolean isChanging) {
        System.out.println("----:" + isChanging);
    }

}
