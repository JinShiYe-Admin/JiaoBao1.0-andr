package com.jsy_jiaobao.main.workol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.HwPack;
import com.jsy_jiaobao.po.workol.QsPack;
import com.jsy_jiaobao.po.workol.StuSubQs;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 作业详情界面
 *
 * @author admin
 */

public class WorkDetailsActivity extends BaseActivity implements
        OnClickListener {
    // private final static String TAG="WorkDetailsActivity";
    private Context mContext;// 上下文
    private TextView tv_title;// 题目
    private Chronometer tv_used_time;// 计时器
    private TextView tv_totalnum;// 题目总数
    private Spinner spinner;// 题号范围下拉菜单
    private GridView gridView;// gridView 加载题号
    private WebView webView;// webView 加载题目详情
    // private TextView continueEx;
    private TextView tv_last;// 上一题按钮
    private TextView tv_next;// 下一题按钮
    private int TabID;
    private boolean isFinish;// 是否完成
    private boolean isDoOver = false;// 是否做过
    private ArrayList<Boolean> post;
    private SpinnerAdapter spAdapter;// spinner的Adapter
    private GridAdapter gridAdapter;// gridView的Adapter
    /**
     * gridview 数据
     */
    private ArrayList<QT> QTList = new ArrayList<>();
    private boolean isLastSPPage = true;// 是否最后一页
    private boolean isFirstSPPage = true;// 是否第一页
    private int spPosition = 0;// Spinner位置
    /**
     * 有无题目
     */
    private boolean haveHtml = false;
    private int FLIP_TYPE_LAST = 0;// 点击上一题
    private int FLIP_TYPE_NEXT = 1;// 点击下一题
    private int FLIP_TYPE_CLICK = 2;// 点击题号
    private int FLIP_TYPE = FLIP_TYPE_NEXT;// 默认为下一题

    private boolean isSpinnerScroll = false;// Spinner是否滚动
    // private int scrollPosition;
    private String actionBarName;// ActionBar Title 内容
    private boolean isGenLook;// 是否家长查看
    private int isSelf;// 是否自己
    private String startTime;// 开始时间
    private int longTime;// 作业时长

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            TabID = savedInstanceState.getInt("TabID");
            actionBarName = savedInstanceState.getString("Name");
            isSelf = savedInstanceState.getInt("isSelf");
            post = (ArrayList<Boolean>) savedInstanceState
                    .getSerializable("isFinish");
            if (post != null && post.size() > 1) {
                isFinish = post.get(0);
                isGenLook = post.get(1);
            }
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                actionBarName = bundle.getString("Name");
                TabID = bundle.getInt("TabID");
                isSelf = bundle.getInt("isSelf");
                post = (ArrayList<Boolean>) bundle.getSerializable("isFinish");
                if (post != null && post.size() > 1) {
                    isFinish = post.get(0);
                    isGenLook = post.get(1);
                }
            }
        }
        // 初始化界面
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Name", actionBarName);
        outState.putInt("TabID", TabID);
        outState.putInt("isSelf", isSelf);
        outState.putSerializable("isFinish", post);
    }

    /**
     * 初始化界面
     */
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    @SuppressWarnings("deprecation")
    private void initView() {
        setContentLayout(R.layout.activity_workol_do);
        mContext = this;
        // 如果ActionBarTitle内容为空
        if (TextUtils.isEmpty(actionBarName)) {
            actionBarName = getResources().getString(R.string.home_work);
        }
        // 设置ActionBar的名称
        setActionBarTitle(actionBarName
                + getResources().getString(R.string.details));
        // 初始化Adapter
        spAdapter = new SpinnerAdapter();
        gridAdapter = new GridAdapter();
        // Controller注册在此
        WorkDetailsActivityController.getInstance().setContext(this);
        tv_title = (TextView) findViewById(R.id.workol_tv_title);
        // 计时器
        tv_used_time = (Chronometer) findViewById(R.id.workol_used_time);
        tv_used_time.setVisibility(View.GONE);
        tv_totalnum = (TextView) findViewById(R.id.workol_tv_totalnum);
        tv_last = (TextView) findViewById(R.id.workol_tv_last);
        tv_next = (TextView) findViewById(R.id.workol_tv_next);
        spinner = (Spinner) findViewById(R.id.workol_sp_chose);
        gridView = (GridView) findViewById(R.id.workol_gridview);
        webView = (WebView) findViewById(R.id.workol_webview);
        // 设置WebView各种属性
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setDefaultTextEncodingName("utf-8");// 避免中文乱码
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setNeedInitialFocus(false);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setSupportZoom(false);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        /**
         * 根据屏幕分辨率不同设置WebView的显示模式
         */
        int mDensity = metrics.densityDpi;
        if (mDensity <= 120) {
            webSetting.setDefaultZoom(ZoomDensity.CLOSE);
        } else if (mDensity >= 160 && mDensity < 240) {
            webSetting.setDefaultZoom(ZoomDensity.MEDIUM);
        } else if (mDensity >= 240) {
            webSetting.setDefaultZoom(ZoomDensity.FAR);
        }
        JavaScriptInterface myJavaScriptInterface;
        myJavaScriptInterface = new JavaScriptInterface();
        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
        // 控件设置各种监听事件
        tv_last.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        // 控件加载各种Adapter
        spinner.setAdapter(spAdapter);
        gridView.setAdapter(gridAdapter);
        // 根据作业Id,去请求服务器 获取作业详情
        WorkDetailsActivityController.getInstance().GetStuHW(TabID, isGenLook);
        spinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerScroll = true;
                return false;
            }
        });
        /**
         * 题号范围的Spinner中的Item监听事件
         */
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.GenCheckWorkActivity_screen));
                if (isSpinnerScroll) {
                    FLIP_TYPE = FLIP_TYPE_CLICK;
                    int p = position * 20;
                    QT qt = QTList.get(p);
                    clickQuetionID = qt.qid;
                    for (int i = 0; i < QTList.size(); i++) {
                        QTList.get(i).isCheck = false;
                    }
                    if (isFinish || isGenLook) {// 查看的

                        currQuetionID = qt.qid;
                        QTList.get(p).isCheck = true;
                        gridAdapter.notifyDataSetChanged();
                        WorkDetailsActivityController.getInstance().GetStuHWQs(
                                TabID, currQuetionID);
                    } else {// 做作业
                        if (haveHtml) {// 不是空题,提交
                            webView.loadUrl("javascript:showOutPuts("
                                    + FLIP_TYPE + ")");
                        } else {
                            currQuetionID = qt.qid;
                            QTList.get(p).isCheck = true;
                            gridAdapter.notifyDataSetChanged();
                            WorkDetailsActivityController.getInstance()
                                    .GetStuHWQs(TabID, currQuetionID);
                        }
                    }
                }
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSpinnerScroll) {
                            gridView.setSelection(position * 20);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**
         * webView加载数据
         */
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                // 答案不为空
                if (myAnswer != null) {
                    // 家长查看
                    if (isGenLook) {
                        webView.loadUrl("javascript:getInputs('" + myAnswer
                                + "'," + true + ")");
                        // 自己查看
                    } else {
                        webView.loadUrl("javascript:getInputs('" + myAnswer
                                + "'," + isFinish + ")");
                    }
                    // 答案为空
                } else {
                    // 家长产看
                    if (isGenLook) {
                        webView.loadUrl("javascript:getInputs(''," + true + ")");
                    } else {
                        // 自己查看
                        webView.loadUrl("javascript:getInputs(''," + isFinish
                                + ")");
                    }
                }
            }

        });
    }

    // 将String型日期转换成long型
    private long getDayTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date dt2;
        try {
            dt2 = sdf.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            dt2 = new Date();
        }
        return dt2.getTime();

    }

    /**
     * JavaScriptInterface
     *
     * @author admin
     */
    public class JavaScriptInterface {
        public JavaScriptInterface() {

        }

        @JavascriptInterface
        public void getInput(String input) {

        }

        @JavascriptInterface
        public void showOutPuts(String output, int type) {// 提交答案,>若做题并且不是第一页则获取下一题,把下一题设为选中
            // 输出为空时 提示不能提交
            if (!TextUtils.isEmpty(output)) {
                // 半角换成全角
                output = output.replaceAll("<", "＜").replaceAll(">", "＞");
                // 输出有不为空的就可以提交
                boolean could = false;
                if (!isFinish && !isGenLook) {
                    String[] outputs = output.split("\\,");
                    if (outputs.length == 0) {
                        could = false;
                    } else {
                        output = output.substring(0, output.length() - 1);

                        for (int i = 0; i < outputs.length; i++) {
                            if (!TextUtils.isEmpty(outputs[i])) {
                                could = true;
                                break;
                            }
                        }
                    }
                }
                // 题号点击
                if (FLIP_TYPE == FLIP_TYPE_CLICK) {
                    for (int i = 0; i < QTList.size(); i++) {
                        QT qt = QTList.get(i);
                        if (qt.qid.equals(currQuetionID)) {
                            if (i == QTList.size() - 1) {
                                could = false;
                            }
                            break;
                        }
                    }
                    if (could) {// 能提交,提交本题----------->获取下一题,把下一题设为选中
                        WorkDetailsActivityController.getInstance().StuSubQs(
                                TabID, currQuetionID, output);
                    } else {
                        WorkDetailsActivityController.getInstance().GetStuHWQs(
                                TabID, clickQuetionID);
                    }

                } else {
                    if (could) {// 能提交,提交本题----------->获取下一题,把下一题设为选中
                        WorkDetailsActivityController.getInstance().StuSubQs(
                                TabID, currQuetionID, output);
                    } else {
                        ToastUtil.showMessage(mContext,
                                R.string.please_input_answer);
                    }
                }
            } else {
                // 点击题号
                if (FLIP_TYPE == FLIP_TYPE_CLICK) {
                    WorkDetailsActivityController.getInstance().GetStuHWQs(
                            TabID, clickQuetionID);
                } else {
                    ToastUtil.showMessage(mContext,
                            R.string.please_input_answer);
                }
            }
        }
    }

    /**
     * 各种控件的点击事件
     */
    @Override
    public void onClick(View v) {
        int position = 0;
        QT qt = null;
        switch (v.getId()) {
            // 上一题
            case R.id.workol_tv_last:
                MobclickAgent
                        .onEvent(
                                mContext,
                                getResources().getString(
                                        R.string.WorkDetailsActivity_last));
                FLIP_TYPE = FLIP_TYPE_LAST;
                // 判断是否要提交当前题
                for (int i = 0; i < QTList.size(); i++) {
                    qt = QTList.get(i);
                    if (qt.qid.equals(currQuetionID)) {
                        position = i;
                        break;
                    }
                }
                if (isFinish || isGenLook) {// 查看的
                    if (position > 0) {// 不是第一条则直接获取上一条的信息
                        QTList.get(position).isCheck = false;
                        qt = QTList.get(position - 1);
                        QTList.get(position - 1).isCheck = true;
                        gridAdapter.notifyDataSetChanged();
                        currQuetionID = qt.qid;
                        WorkDetailsActivityController.getInstance().GetStuHWQs(
                                TabID, currQuetionID);
                    } else {// 是第一条
                        if (isFirstSPPage) {// 第一页的第一条提交一下获取分数
                            // WorkDetailsActivityController.getInstance().StuSubQs(TabID,
                            // currQuetionID,"");
                            ToastUtil.showMessage(mContext,
                                    R.string.this_first_topic);
                        } else {// 不是第一条,要翻到上一页的最后一条
                            spinner.setSelection(spPosition - 1);
                        }
                    }
                } else {// 做作业的
                    if (position > 0) {// 不是第一条则 提交答案后获取下一条
                        if (haveHtml) {// 有内容
                            QTList.get(position).isCheck = false;
                            qt = QTList.get(position - 1);
                            QTList.get(position - 1).isCheck = true;
                            gridAdapter.notifyDataSetChanged();
                            currQuetionID = qt.qid;
                            WorkDetailsActivityController.getInstance().GetStuHWQs(
                                    TabID, currQuetionID);
                        } else {// 无内容直接获取下一条
                            QTList.get(position).isCheck = false;
                            qt = QTList.get(position - 1);
                            QTList.get(position - 1).isCheck = true;
                            gridAdapter.notifyDataSetChanged();
                            currQuetionID = qt.qid;
                            WorkDetailsActivityController.getInstance().GetStuHWQs(
                                    TabID, currQuetionID);
                        }
                    } else {// 是第一条
                        if (isFirstSPPage) {// 第一页的第一条提交一下获取分数

                            ToastUtil.showMessage(mContext, R.string.no_topic);
                            // }
                        } else {// 不是第一条,要翻到上一页的最后一条
                            if (haveHtml) {
                                webView.loadUrl("javascript:showOutPuts("
                                        + FLIP_TYPE + ")");// --->提交答案后获取下一条
                            } else {
                                spinner.setSelection(spPosition - 1);
                            }
                        }

                    }
                }
                break;
            // 下一题
            case R.id.workol_tv_next:
                MobclickAgent
                        .onEvent(
                                mContext,
                                getResources().getString(
                                        R.string.WorkDetailsActivity_last));
                FLIP_TYPE = FLIP_TYPE_NEXT;
                // 判断是否要提交当前题
                for (int i = 0; i < QTList.size(); i++) {
                    qt = QTList.get(i);
                    if (qt.qid.equals(currQuetionID)) {
                        position = i;
                        break;
                    }
                }
                if (isFinish || isGenLook) {// 查看的

                    if (position < QTList.size() - 1) {// 不是最后一条则直接获取下一条的信息
                        QTList.get(position).isCheck = false;
                        qt = QTList.get(position + 1);
                        QTList.get(position + 1).isCheck = true;
                        gridAdapter.notifyDataSetChanged();
                        currQuetionID = qt.qid;
                        WorkDetailsActivityController.getInstance().GetStuHWQs(
                                TabID, currQuetionID);
                    } else {// 是第一条
                        if (isLastSPPage) {// 最后一页的最后一条提交一下获取分数
                            WorkDetailsActivityController.getInstance().StuSubQs(
                                    TabID, currQuetionID, "");
                        } else {// 不是第一条,要翻到上一页的最后一条
                            spinner.setSelection(spPosition + 1);
                        }
                    }
                } else {// 做作业的

                    if (position < QTList.size() - 1) {// 不是最后一条则 提交答案后获取下一条
                        if (haveHtml) {// 有内容
                            webView.loadUrl("javascript:showOutPuts(" + FLIP_TYPE
                                    + ")");// --->提交答案后获取下一条
                        } else {// 无内容直接获取下一条
                            QTList.get(position).isCheck = false;
                            qt = QTList.get(position + 1);
                            QTList.get(position + 1).isCheck = true;
                            gridAdapter.notifyDataSetChanged();
                            currQuetionID = qt.qid;
                            WorkDetailsActivityController.getInstance().GetStuHWQs(
                                    TabID, currQuetionID);
                        }
                    } else {// 是最后一条
                        if (isLastSPPage) {// 最后一页的最后一条提交一下获取分数
                            if (haveHtml) {
                                webView.loadUrl("javascript:showOutPuts("
                                        + FLIP_TYPE + ")");// --->提交答案
                            } else {
                                ToastUtil.showMessage(mContext, R.string.no_topic);
                            }
                        } else {// 不是第一条,要翻到上一页的第一条
                            if (haveHtml) {
                                webView.loadUrl("javascript:showOutPuts("
                                        + FLIP_TYPE + ")");// --->提交答案后获取下一条
                            } else {
                                spinner.setSelection(spPosition + 1);
                            }
                        }

                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        EventBusUtil.register(this);
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    ArrayList<SD> sdlist = new ArrayList<>();
    // ArrayList<QT> qtlist = new ArrayList<QT>();
    /**
     * 我回答过的本题答案
     */
    private String myAnswer;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 99:
                    if (isFinish || isGenLook || isSelf == 1) {
                        tv_used_time.setVisibility(View.GONE);
                    } else {
                        tv_used_time.setVisibility(View.VISIBLE);
                    }
                    thread.interrupt();
                    thread = null;
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * 由于计时器响应慢，休息一段时间再显示
     */
    private Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                Thread.sleep(999);
                handler.sendEmptyMessage(99);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });

    /**
     * 获取数据
     *
     * @param list li
     */
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constants.WORKOL_StuSubQs:// 提交结果
                StuSubQs subqs = (StuSubQs) list.get(1);
                if (subqs != null) {
                    int renum = subqs.getReNum();
                    if (renum > 0) {// 获取下一题
                        tv_last.setVisibility(View.VISIBLE);
                        tv_next.setVisibility(View.VISIBLE);
                        int position = 0;
                        for (int i = 0; i < QTList.size(); i++) {
                            QT qt = QTList.get(i);
                            if (qt.qid.equals(currQuetionID)) {
                                position = i;
                                try {
                                    QTList.get(i).commflag = "1";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        QT qt;
                        // 上一题按钮
                        if (FLIP_TYPE == FLIP_TYPE_LAST) {
                            if (position == 0) {
                                // 第一页
                                if (isFirstSPPage) {
                                    ToastUtil.showMessage(mContext, "当前为第一题");
                                } else {
                                    spinner.setSelection(spPosition - 1);
                                }
                            } else {
                                QTList.get(position).isCheck = false;
                                qt = QTList.get(position - 1);
                                QTList.get(position - 1).isCheck = true;
                                currQuetionID = qt.qid;
                                WorkDetailsActivityController.getInstance()
                                        .GetStuHWQs(TabID, currQuetionID);
                            }
                            // 下一页按钮
                        } else if (FLIP_TYPE == FLIP_TYPE_NEXT) {
                            if (position == QTList.size() - 1) {
                                if (!isLastSPPage) {
                                    spinner.setSelection(spPosition + 1);
                                }
                            } else {
                                QTList.get(position).isCheck = false;
                                qt = QTList.get(position + 1);
                                QTList.get(position + 1).isCheck = true;
                                currQuetionID = qt.qid;
                                WorkDetailsActivityController.getInstance()
                                        .GetStuHWQs(TabID, currQuetionID);
                            }
                        } else if (FLIP_TYPE == FLIP_TYPE_CLICK) {
                            // 点击的是题号
                            QTList.get(position).isCheck = false;
                            for (int i = 0; i < QTList.size(); i++) {
                                QT qt1 = QTList.get(i);
                                if (qt1.qid.equals(clickQuetionID)) {
                                    QTList.get(i).isCheck = true;
                                    break;
                                }
                            }
                            WorkDetailsActivityController.getInstance().GetStuHWQs(
                                    TabID, clickQuetionID);
                        }
                        gridAdapter.notifyDataSetChanged();

                    } else {// 展示成绩
                        isDoOver = true;
                        isFinish = true;
                        tv_used_time.setVisibility(View.GONE);
                        tv_last.setVisibility(View.GONE);
                        tv_next.setVisibility(View.GONE);
                        DialogUtil.getInstance().cannleDialog();
                        webView.loadDataWithBaseURL(
                                null,
                                StringUtils.xml2webview(subqs.getHWHTML()
                                        .replaceAll("width", "")
                                        .replaceAll("height", "")), "text/html",
                                "utf-8", null);
                        WorkDetailsActivityController.getInstance().GetStuHW(TabID,
                                isGenLook);
                    }
                }
                break;
            /**
             * 已完成作业查看
             */
            case Constants.WORKOL_GetStuHWQs:
                tv_last.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.VISIBLE);
                // Dialog消失
                DialogUtil.getInstance().cannleDialog();
                QsPack qsPack = (QsPack) list.get(1);
                if (qsPack != null) {
                    int position = 0;
                    for (int i = 0; i < QTList.size(); i++) {
                        QT qt1 = QTList.get(i);
                        if (qt1.qid.equals(currQuetionID)) {
                            position = i;
                            break;
                        }
                    }
                    int spP = spinner.getSelectedItemPosition();
                    // 点击题号 GridView与SpinnerView的关联
                    if (FLIP_TYPE == FLIP_TYPE_CLICK) {
                        int position1 = 0;
                        for (int i = 0; i < QTList.size(); i++) {
                            QT qt1 = QTList.get(i);
                            if (qt1.qid.equals(clickQuetionID)) {
                                position1 = i;
                                break;
                            }
                        }
                        currQuetionID = clickQuetionID;
                        QTList.get(position1).isCheck = true;
                        gridAdapter.notifyDataSetChanged();
                        isSpinnerScroll = false;
                        final int s = position1 / 20;
                        gridView.post(new Runnable() {

                            @Override
                            public void run() {
                                gridView.setSelection(s * 20);
                            }
                        });
                        if (Math.abs(s - spP) > 0) {
                            spinner.setSelection(s);
                        }
                        // 上一题 gridView与SpinnerView的关联
                    } else if (FLIP_TYPE == FLIP_TYPE_LAST) {
                        isSpinnerScroll = false;
                        final int s = position / 20;
                        gridView.post(new Runnable() {
                            @Override
                            public void run() {
                                gridView.setSelection(s * 20);
                            }
                        });
                        if (Math.abs(s - spP) > 0) {
                            spinner.setSelection(s);
                        }
                        // 点击下一题 gridView与SpinnerView的关联
                    } else if (FLIP_TYPE == FLIP_TYPE_NEXT) {
                        isSpinnerScroll = false;
                        final int s = position / 20;
                        gridView.post(new Runnable() {

                            @Override
                            public void run() {
                                gridView.setSelection(s * 20);
                            }
                        });
                        if (Math.abs(s - spP) > 0) {
                            spinner.setSelection(s);
                        }
                    }
                    // 第一页 且 当且Id为1
                    if (isFirstSPPage && currQuetionID.equals("1")) {
                        // 上一题按钮隐藏
                        tv_last.setVisibility(View.INVISIBLE);
                    } else {
                        // 否则显示上一题按钮
                        tv_last.setVisibility(View.VISIBLE);
                    }
                    tv_next.setEnabled(true);
                    // 最后一题
                    if (isLastSPPage
                            && currQuetionID.equals(String.valueOf(QTList.size()))) {
                        if (isFinish || isGenLook) {
                            // 如果已完成，或者家长查看，tv_next不可点击
                            tv_next.setEnabled(false);
                        } else {
                            tv_next.setEnabled(true);
                        }
                        tv_next.setText(mContext.getString(R.string.commit_workName, actionBarName));
                    } else {
                        // 不是最后一题,控件显示下一题
                        tv_next.setText(getResources().getString(
                                R.string.next_topic));
                    }
                    // 获取我的回答
                    myAnswer = qsPack.getQsAns();
                    // 替换
                    String html = qsPack.getQsCon().replaceAll("width", "")
                            .replaceAll("height", "");
                    if (TextUtils.isEmpty(html)) {
                        haveHtml = false;
                        html = getResources().getString(R.string.no_topic);
                    } else {
                        haveHtml = true;
                    }
                    // 已完成
                    if (isFinish) {
                        String qsAns = TextUtils.isEmpty(qsPack.getQsAns()) ? ""
                                : qsPack.getQsAns();
                        String qsCoretAnswer = TextUtils.isEmpty(qsPack
                                .getQsCorectAnswer()) ? "" : qsPack
                                .getQsCorectAnswer();
                        if (qsAns.equals(qsCoretAnswer)) {
                            html += "<br>作答:<font color='green'>" + qsAns
                                    + "</font>";
                        } else {
                            html += "<br>作答:<font color='red'>" + qsAns + "</font>";
                        }
                        html += "<br>正确答案:" + qsCoretAnswer;
                        html += "<br><font color='red'>"
                                + (TextUtils.isEmpty(qsPack.getQsExplain()) ? ""
                                : qsPack.getQsExplain()) + "</font>";
                    }

                    String js = "<script> "
                            + "function showOutPuts(type){"
                            + "	var outputs = '';"
                            + "	var ps = document.getElementsByTagName('input');"
                            + "	for(i =0;i<ps.length;i++){"
                            + "		if(ps[i].type =='text'){"
                            + "			outputs += ps[i].value.replace(/,/g,'，').replace(/'/g,'’')+',';"
                            + "		}else if(ps[i].type =='radio'){"
                            + "			if(ps[i].checked){"
                            + "				outputs = ps[i].value+',';" + "				break;"
                            + "			}" + "		}else if(ps[i].type =='checkbox'){"
                            + "		}" + "	}"
                            + "	window.AndroidFunction.showOutPuts(outputs,type);"
                            + "}" + "	function getInputs(inputs,isFinish){"
                            + "		var ds = new Array();"
                            + "		var ps = document.getElementsByTagName('input');"
                            + "		for(i=0;i<ps.length;i++){"
                            + "         ps[i].disabled =isFinish;"
                            + "			if(ps[i].type !='hidden'){"
                            + "				ds.push(ps[i]);" + "			}" + "		}"
                            + "		var strs= new Array();"
                            + "		strs=inputs.split(',');"
                            + "		for(i =0;i<ds.length;i++){"
                            + "			if(ds[i].type =='text'){"
                            + "             if(strs[i] != null){"
                            + "					ds[i].value = strs[i];" + "				}" + "			}"
                            + "			else if(ds[i].type =='radio'){"
                            + "				if(ds[i].value == strs[0]){"
                            + "					ds[i].checked = true;" + "				}" + "				else{"
                            + "					ds[i].checked = false;" + "				}" + "			}"
                            + "			else if(ds[i].type =='checkbox'){" + "			}"
                            + "		}" + "	}" + "</script>";
                    // webView加载数据
                    webView.loadDataWithBaseURL(null,
                            StringUtils.xml2webview(html + js), "text/html",
                            "utf-8", null);
                } else {
                    // webView加载空
                    webView.loadDataWithBaseURL(null, "", "text/html", "utf-8",
                            null);
                }
                break;
            // 获取服务器时间
            case Constants.WORKOL_GetSQLDateTIme:
                String time = (String) list.get(1);// 2016-01-23 11:22:54
                // 开始时间
                String startTimeR = startTime.replace("T", " ");
                // 开始时间+作业时长
                long startTimeL = getDayTime(startTimeR) + (longTime * 60 * 1000);
                // 系统时间
                final long systemTime = getDayTime(time);
                // 校对时间
                final long diffrentTime = systemTime - System.currentTimeMillis();
                // 设置初始时间
                tv_used_time.setBase(startTimeL);
                // 开始计时
                tv_used_time.start();
                // 时间监听
                tv_used_time
                        .setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer chronometer) {

                                long leftLongTime = chronometer.getBase()
                                        - (System.currentTimeMillis() + diffrentTime);
                                SimpleDateFormat dateFormat = new SimpleDateFormat(
                                        "ss", Locale.getDefault());
                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                // 展示倒计时，
                                if (leftLongTime > 0) {
                                    String s = dateFormat.format(leftLongTime);
                                    chronometer.setText(getString(R.string.count_down_time, leftLongTime / (60 * 1000), s));

                                } else {
                                    // 显示超时时间
                                    long timeOut = 0 - leftLongTime;
                                    String s = dateFormat.format(timeOut);
                                    chronometer.setText(getString(R.string.overred_time, timeOut / (60 * 1000), s));
                                }
                            }
                        });
                thread.start();
                break;
            // 获取作业
            case Constants.WORKOL_GetStuHW:
                HwPack hwPack = (HwPack) list.get(1);
                String title = hwPack.getHomeworkname();
                // 作业时长
                longTime = hwPack.getLongTime();
                // 作业开始时间
                startTime = hwPack.getHWStartTime();
                // 作业名称
                tv_title.setText(TextUtils.isEmpty(title) ? "" : title);
                // 获取服务器当前时间
                WorkDetailsActivityController.getInstance().GetSQLDateTIme();
                // 清空数据
                sdlist.clear();
                QTList.clear();
                /**
                 * 获取题目数量，设置题号Spinner和GridView数据
                 */
                try {
                    String[] qsidqids = hwPack.getQsIdQId().split("\\|");
                    tv_totalnum.setText(getString(R.string.common_questions, qsidqids[qsidqids.length - 1].split("\\_")[0]));
                    int a = 1;
                    for (int i = 1; i <= qsidqids.length; i++) {
                        String[] qids = qsidqids[i - 1].split("\\_");
                        QT qt = new QT();
                        if (qids.length > 0) {
                            qt.name = qids[0];
                            qt.qid = qids[0];
                        }
                        if (qids.length > 2) {
                            qt.commflag = qids[2];
                        }
                        if (qids.length > 3) {
                            // 0==没做,1==正确,2==错误
                            qt.errflag = qids[3];
                        }
                        QTList.add(qt);
                        if (i % 20 == 0) {
                            String name = (a * 20 - 19) + "—" + a * 20;
                            SD sd = new SD();
                            sd.name = name;
                            // sd.list = (ArrayList<QT>) QTList.clone();
                            sdlist.add(sd);
                            // qtlist.clear();
                            a++;
                        } else if (i == qsidqids.length) {
                            String name = (a * 20 - 19) + "—" + i;
                            SD sd = new SD();
                            sd.name = name;

                            // sd.list = QTList;
                            sdlist.add(sd);
                        }
                    }
                    // 做过的题目
                    if (!isDoOver) {
                        if (QTList.size() > 0) {
                            QTList.get(0).isCheck = true;
                            currQuetionID = QTList.get(0).qid;
                            WorkDetailsActivityController.getInstance().GetStuHWQs(
                                    TabID, currQuetionID);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 更新加载界面
                spAdapter.notifyDataSetChanged();
                gridAdapter.notifyDataSetChanged();
                float f = getResources().getDimensionPixelSize(
                        R.dimen.px_to_dip_152);
                // gridView的大小控制
                LayoutParams params = gridView.getLayoutParams();
                if (QTList.size() > 10) {
                    f = f / 4 * 3;
                } else if (QTList.size() > 5) {
                    f = f / 2;
                } else if (QTList.size() > 0) {
                    f = f / 4;
                }
                params.height = (int) f;
                gridView.setLayoutParams(params);
                break;
            default:
                break;
        }
    }

    private class QT {
        String name;
        String qid;
        String commflag;
        String errflag;
        boolean isCheck;
    }

    private class SD {
        String name;
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
     * 下拉框
     *
     * @author admin
     */

    private class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sdlist == null ? 0 : sdlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
                    parent, R.layout.item_workol_spinner, position);
            TextView txt = viewHolder.getView(R.id.item_workol_spinner);
            SD genInfo = sdlist.get(position);
            if (genInfo != null) {
                String name = genInfo.name;
                txt.setText(TextUtils.isEmpty(name) ? "" : name);
            }
            return viewHolder.getConvertView();
        }
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return QTList == null ? 0 : QTList.size();
        }

        @Override
        public Object getItem(int position) {
            return QTList == null ? null : QTList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 子控件布局
         */
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
                    parent, R.layout.item_workol_spinner, position);
            TextView txt = viewHolder.getView(R.id.item_workol_spinner);
            txt.setGravity(Gravity.CENTER);
            txt.setBackgroundColor(getResources()
                    .getColor(R.color.color_ebebeb));
            txt.setTextColor(getResources().getColor(R.color.black));
            final QT qt = QTList.get(position);
            if (qt != null) {
                String name = qt.name;
                txt.setText(TextUtils.isEmpty(name) ? "" : name);
                // qt.errflag :0==没做,1==正确,2==错误;
                if (qt.isCheck) {
                    txt.setBackgroundColor(getResources().getColor(
                            R.color.color_0ba935));
                    txt.setTextColor(getResources().getColor(R.color.white));
                    if (isFinish) {
                        if (qt.errflag.equals("0") || qt.errflag.equals("2")) {
                            txt.setTextColor(getResources().getColor(
                                    R.color.red));
                        } else {
                            txt.setTextColor(getResources().getColor(
                                    R.color.black));
                        }
                    }
                } else {
                    if (qt.commflag.equals("1")) {
                        txt.setBackgroundColor(getResources().getColor(
                                R.color.color_a4eab7));
                    }
                    if (isFinish) {
                        if (qt.errflag.equals("0") || qt.errflag.equals("2")) {
                            txt.setTextColor(getResources().getColor(
                                    R.color.red));
                        } else {
                            txt.setTextColor(getResources().getColor(
                                    R.color.black));
                        }
                    }
                }
                txt.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.WorkDetailsActivity_clickGride));
                        FLIP_TYPE = FLIP_TYPE_CLICK;
                        clickQuetionID = qt.qid;
                        for (int i = 0; i < QTList.size(); i++) {
                            QTList.get(i).isCheck = false;
                        }
                        if (isFinish || isGenLook) {// 查看的
                            currQuetionID = qt.qid;
                            QTList.get(position).isCheck = true;
                            gridAdapter.notifyDataSetChanged();
                            WorkDetailsActivityController.getInstance()
                                    .GetStuHWQs(TabID, currQuetionID);
                        } else {// 做作业
                            if (haveHtml) {// 不是空题,提交
                                webView.loadUrl("javascript:showOutPuts("
                                        + FLIP_TYPE + ")");
                            } else {
                                currQuetionID = qt.qid;
                                QTList.get(position).isCheck = true;
                                gridAdapter.notifyDataSetChanged();
                                WorkDetailsActivityController.getInstance()
                                        .GetStuHWQs(TabID, currQuetionID);
                            }
                        }
                    }
                });
            }
            return viewHolder.getConvertView();
        }
    }

    private String currQuetionID;
    private String clickQuetionID;
}
