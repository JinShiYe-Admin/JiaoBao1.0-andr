package com.jsy_jiaobao.main.system;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.GetAllCategory;
import com.jsy_jiaobao.po.qiuzhi.MyAttQItem;
import com.jsy_jiaobao.po.qiuzhi.MyComms;
import com.jsy_jiaobao.po.qiuzhi.Subject;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * 求知我关注的问题等的界面
 */
public class QiuzhiQuestionListActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView> {
	private final static int Q_TYPE_MYATT = 1;// 我关注的问题
	private final static int Q_TYPE_MYQUESTION = 2;// 我提问过的问题
	private final static int Q_TYPE_MYANSWER = 3;// 我回答过的问题
	private final static int Q_TYPE_ATME = 4;// 邀请我回答的问题
	private final static int Q_TYPE_ATTP = 5;// 我关注的人
	private final static int Q_TYPE_ATTC = 6;// 我关注的话题
	private final static int Q_TYPE_ATTM = 7;// 关注我的人
	private final static int Q_TYPE_MYCOMMENT = 8;// 我做出的评论
	private int q_type = 1;
	private int pageNum = 1;
	private int RowCount = 0;
	private Context mContext;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private TextView tv_hidden;
	private QiuzhiQuestionListAdapter adapter;
	private ArrayList<Object> myattqList = new ArrayList<>();
	private int numPerPage = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBusUtil.register(this);
		if (savedInstanceState != null) {
			q_type = savedInstanceState.getInt("Q_TYPE");
		} else {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				q_type = bundle.getInt("Q_TYPE");
			}
		}
		initView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("Q_TYPE", q_type);
	}

	private void initView() {
		mContext = this;
		setContentLayout(R.layout.activity_qiuzhi_siftlist);
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		CusListView listView = (CusListView) findViewById(R.id.qiuzhi_siftlist_listview);
		tv_hidden = (TextView) findViewById(R.id.qiuzhi_siftlist_hidden);
		mPullRefreshScrollView.setOnRefreshListener(this);
		QiuzhiQuestionListActivityController.getInstance().setContext(this);
		adapter = new QiuzhiQuestionListAdapter(mContext);
		adapter.setData(myattqList);
		adapter.setqType(q_type);
		listView.setAdapter(adapter);
        switch (q_type) {
            case Q_TYPE_MYATT:// 我关注的问题
                setActionBarTitle(R.string.question_has_myAttention);
                break;
            case Q_TYPE_MYQUESTION:// 我提问过的问题
                setActionBarTitle(R.string.question_i_raised);
                break;
            case Q_TYPE_MYANSWER: // 我回答过的问题
                setActionBarTitle(R.string.question_i_answered);
                break;
            case Q_TYPE_ATME:// 邀请我回答的问题
                setActionBarTitle(R.string.question_invitedMe_toAnswer);
                break;
            case Q_TYPE_ATTP:// 我关注的人
                setActionBarTitle(R.string.qiuZhi_personal_center);
                break;
            case Q_TYPE_ATTC:// 我关注的话题
                setActionBarTitle(R.string.topic_has_myAttention);
                mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
                listView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
                        final Subject subject = (Subject) myattqList.get(position);
                        BaseUtils
                                .createDialog(
                                        mContext,
                                        getResources().getString(
                                                R.string.cancel_attention),
                                        getResources().getString(
                                                R.string.beSure_toCancel_attention)
                                                + subject.getSubject().trim() + "?",
                                        android.R.drawable.ic_dialog_alert,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                String uid = "";
                                                for (int i = 0; i < myattqList
                                                        .size(); i++) {
                                                    Subject item = (Subject) myattqList
                                                            .get(i);
                                                    if (item.getTabID() != subject
                                                            .getTabID()) {
                                                        uid += item.getTabID()
                                                                + ",";
                                                    }
                                                }
                                                if (!TextUtils.isEmpty(uid)) {
                                                    uid = uid.substring(0,
                                                            uid.length() - 1);
                                                }
                                                QiuzhiQuestionListActivityController
                                                        .getInstance()
                                                        .AddMyattCate(uid);
                                            }
                                        }).show();
                    }
                });
                break;
            case Q_TYPE_ATTM:// 关注我的人
                setActionBarTitle(R.string.qiuZhi_personal_center);
                break;
            case Q_TYPE_MYCOMMENT:// 我做出的评论
                setActionBarTitle(R.string.commit_i_made);
                break;
            default:
                break;
        }
		getData();
	}

	private void getData() {
		switch (q_type) {
            case Q_TYPE_ATTC:// 我关注的话题
                QiuzhiQuestionListActivityController.getInstance().GetMyattCate();
                break;
            case Q_TYPE_ATTP:// 我关注的人
                break;
            case Q_TYPE_ATTM:// 关注我的人
                break;
            case Q_TYPE_MYATT:// 我关注的问题
                QiuzhiQuestionListActivityController.getInstance().MyAttQIndex(
                        pageNum, RowCount, numPerPage);
                break;
            case Q_TYPE_ATME:// 邀请我回答的问题
                QiuzhiQuestionListActivityController.getInstance().AtMeQIndex(
                        pageNum, RowCount);
                break;
            case Q_TYPE_MYANSWER: // 我回答过的问题
                QiuzhiQuestionListActivityController.getInstance().MyAnswerIndex(
                        pageNum, RowCount);
                break;
            case Q_TYPE_MYQUESTION:// 我提问过的问题
                QiuzhiQuestionListActivityController.getInstance().MyQuestionIndex(
                        pageNum, RowCount);
                break;
            case Q_TYPE_MYCOMMENT: // 我做出的评论
                QiuzhiQuestionListActivityController.getInstance().GetMyComms(
                        pageNum, RowCount);
                break;

            default:
                break;
		}
	}

	@Override
	protected void onDestroy() {
		EventBusUtil.unregister(this);
		super.onDestroy();
	}

	int hidden = 0;

	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		int showNum = 0;
		switch (tag) {
            case Constant.msgcenter_qiuzhi_AddMyattCate:// 更新我关注的话题
                String result = (String) list.get(1);
                if (result.equals("1")) {
                    ToastUtil.showMessage(mContext, R.string.update_success);
                    pageNum = 1;
                    RowCount = 0;
                    numPerPage = 10;
                    myattqList.clear();
                    getData();
                } else {
                    ToastUtil.showMessage(mContext, R.string.update_failed);
                }
                break;
            case Constant.msgcenter_qiuzhi_RemoveMyAttQ:// 取消关注问题
                String result1 = (String) list.get(1);
                if (TextUtils.isEmpty(result1)) {
                    ToastUtil.showMessage(mContext, R.string.fuck_failed);
                } else {
                    ToastUtil.showMessage(mContext,
                            R.string.cancel_attention_success);
                    int s = Integer.valueOf(result1);
                    int f = myattqList.size();
                    myattqList.remove(s);
                    adapter.notifyDataSetChanged();
                    if (RowCount > f) {
                        QiuzhiQuestionListActivityController.getInstance()
                                .MyAttQIndex(f, RowCount, 1);
                    }

                }
                break;
            case Constant.msgcenter_qiuzhi_AnswerDetail:// 回答详情
                try {
                    MyComms com = (MyComms) list.get(1);
                    if (com != null) {
                        myattqList.remove(com);
                    } else {
                        myattqList.add(null);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                break;
            /**
             * 我关注的问题
             */
            case Constant.msgcenter_qiuzhi_MyAttQIndex:
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<MyAttQItem> myattqs = (ArrayList<MyAttQItem>) list.get(1);
                if (myattqs != null && myattqs.size() > 0) {
                    RowCount = myattqs.get(0).getRowCount();
                    if (myattqs.get(0).getTabID() != -1) {
                        myattqList.addAll(myattqs);
                        showNum++;
                    }
                }
                adapter.notifyDataSetChanged();
                int num = (Integer) list.get(2);
                if (num == 10) {
                    dataLoadMore();
                } else {
                    DialogUtil.getInstance().cannleDialog();
                }
                hidden += num;
                if (hidden > 0) {
                    tv_hidden.setVisibility(View.VISIBLE);
                    tv_hidden.setText("有" + hidden + "条记录被隐藏");
                }
                if (RowCount == 0) {
                    ToastUtil
                            .showMessage(mContext, R.string.temporarily_noCatainer);
                } else {
                    if (RowCount <= myattqList.size() + hidden) {
                        if (RowCount == hidden) {
                            ToastUtil.showMessage(mContext,
                                    R.string.temporarily_noCatainer);
                        } else {
                            if (showNum == 0) {
                                ToastUtil.showMessage(mContext,
                                        R.string.have_no_more);
                            }
                        }
                    }
                }
                break;

            case Constant.msgcenter_qiuzhi_AtMeQIndex:// 获取邀请我回答的问题列表
            case Constant.msgcenter_qiuzhi_MyQuestionIndex:// 获获取我提出的问题列表
            case Constant.msgcenter_qiuzhi_MyAnswerIndex:// 获取我提出的问题列表
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<MyAttQItem> myattqs1 = (ArrayList<MyAttQItem>) list
                        .get(1);
                if (null != myattqs1 && myattqs1.size() > 0) {
                    RowCount = myattqs1.get(0).getRowCount();
                    if (myattqs1.get(0).getTabID() != -1) {
                        myattqList.addAll(myattqs1);
                        showNum++;
                    }
                }
                int num1 = (Integer) list.get(2);
                if (num1 == 10) {
                    dataLoadMore();
                } else if (myattqList.size() == num1) {
                    DialogUtil.getInstance().cannleDialog();
                } else {
                    DialogUtil.getInstance().cannleDialog();
                }
                hidden += num1;
                if (hidden > 0) {
                    tv_hidden.setVisibility(View.VISIBLE);
                    tv_hidden.setText("有" + hidden + "条记录被隐藏");
                }
                if (RowCount == 0) {
                    ToastUtil
                            .showMessage(mContext, R.string.temporarily_noCatainer);
                } else {
                    if (RowCount <= myattqList.size() + hidden) {
                        if (RowCount == hidden) {
                            ToastUtil.showMessage(mContext,
                                    R.string.temporarily_noCatainer);
                        } else {
                            if (showNum == 0) {
                                ToastUtil.showMessage(mContext, R.string.no_more);
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                break;
            case Constant.msgcenter_qiuzhi_GetMyComms:// 获取我的评论
                mPullRefreshScrollView.onRefreshComplete();
                ArrayList<MyComms> mycomms = (ArrayList<MyComms>) list.get(1);
                if (null != myattqList && mycomms.size() > 0) {
                    RowCount = mycomms.get(0).getRowCount();
                    myattqList.addAll(mycomms);
                }
                if (RowCount == 0) {
                    ToastUtil
                            .showMessage(mContext, R.string.temporarily_noCatainer);
                } else {
                    if (RowCount <= myattqList.size()) {
                        if (mycomms.size() == 0) {
                            ToastUtil.showMessage(mContext, R.string.have_no_more);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case Constant.msgcenter_qiuzhi_GetMyattCate:// 获取我关注的话题ID数组
                mPullRefreshScrollView.onRefreshComplete();
                String ids = (String) list.get(1);
                if ("[]".equals(ids)) {
                    ToastUtil.showMessage(mContext, "无关注的话题");
                    adapter.notifyDataSetChanged();
                    return;
                }
                try {
                    JSONArray jsonarray = new JSONArray(ids);
                    ArrayList<GetAllCategory> allCategory = (ArrayList<GetAllCategory>) ACache
                            .get(getApplicationContext(), "qiuzhi").getAsObject(
                                    "GetAllCategory");
                    if (null != allCategory) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String id = jsonarray.getString(i);
                            x: for (int j = 0; j < allCategory.size(); j++) {
                                Subject parent = allCategory.get(j).getItem();
                                if (id.equals(String.valueOf(parent.getTabID()))) {
                                    myattqList.add(parent);
                                    break;
                                }
                                ArrayList<Subject> children = allCategory.get(j)
                                        .getSubitem();
                                for (int k = 0; k < children.size(); k++) {
                                    Subject child = children.get(k);
                                    if (id.equals(String.valueOf(child.getTabID()))) {
                                        myattqList.add(child);
                                        break x;
                                    }
                                }
                            }
                        }
                    } else {
                        ToastUtil.showMessage(mContext,
                                R.string.temporarily_noCatainer);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
		}
	}

	/**
	 * 系统返回键
	 *
	 * @功能 结束当前Activity
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
		hidden = 0;
		pageNum = 1;
		RowCount = 0;
		numPerPage = 10;
		myattqList.clear();
		getData();
	}

	/**
	 * 加载下页数据
	 */
	private void dataLoadMore() {
		if (RowCount <= myattqList.size() + hidden) {
			if (RowCount == hidden) {
				ToastUtil.showMessage(mContext, R.string.no_content);
			} else {
				ToastUtil.showMessage(mContext, R.string.have_no_more);
			}
			mPullRefreshScrollView.onRefreshComplete();
		} else {
			pageNum++;
			numPerPage = 10;
			getData();
		}
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		dataLoadMore();
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}
}