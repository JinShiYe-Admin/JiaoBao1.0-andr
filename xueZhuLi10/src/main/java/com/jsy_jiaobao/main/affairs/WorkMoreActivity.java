package com.jsy_jiaobao.main.affairs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.XListView;
import com.jsy_jiaobao.customview.XListView.IXListViewListener;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.CommMsg;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  `. . __
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       永无BUG
 */
public class WorkMoreActivity extends BaseActivity implements IXListViewListener, PublicMethod {
    private Work2ListAdapter listAdapter;
    public final static int CLICK_POSITION = 1;
    @ViewInject(R.id.layout_xlistview)
    XListView listView;
    private Context mContext;
    private SharedPreferences sp_sys;
    private String msgType;
    private boolean fromme = false;
    private String timeFlag;
    private int currPage = 1;
    private boolean havemore = true;
    private String title = "更多";
    private List<CommMsg> list = new ArrayList<>();
//	private List<FeeBack> list2 = new ArrayList<>();

    private ProgressDialog dialog;

//	private BitmapUtils bitmapUtils;
//	private RadioGroup radioGroup ;

    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            msgType = savedInstanceState.getString("msgType");
            timeFlag = savedInstanceState.getString("time");
            fromme = savedInstanceState.getBoolean("fromme");
            title = savedInstanceState.getString("title");
        } else {
            initPassData();
        }
        initViews();
        initDeatilsData();
        initListener();
    }

    @Override
    public void initPassData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                msgType = bundle.getString("msgType");
                timeFlag = bundle.getString("time");
                fromme = bundle.getBoolean("fromme");
                title = bundle.getString("title");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("msgType", msgType);
        outState.putString("time", timeFlag);
        outState.putString("title", title);
        outState.putBoolean("fromme", fromme);
    }

    @Override
    public void initViews() {
        setContentLayout(R.layout.layout_xlistview);
        ViewUtils.inject(this);
        mContext = this;
        LoginActivityController.getInstance().setContext(this);

        setActionBarTitle(title);

        dialog = BaseUtils.showDialog(mContext, getResources().getString(R.string.public_loading));
        sp_sys = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);

        listAdapter = new Work2ListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this);
        listView.setDivider(null);
        mCache = ACache.get(getApplicationContext());
    }

    @Override
    public void initDeatilsData() {
        dialog.show();

        HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, msgType + currPage, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                if (!isFinishing()) {
                    ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect) + "r1001");
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    dialog.dismiss();
                    if (currPage > 1) {
                        currPage--;
                    }
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (!isFinishing()) {
                    dialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(arg0.result);
                        String ResultCode = jsonObj.getString("ResultCode");

                        if ("0".equals(ResultCode)) {
                            String data = Des.decrypt(jsonObj.getString("Data"), sp_sys.getString("ClientKey", ""));
                            List<CommMsg> a = GsonUtil.GsonToList(data, new TypeToken<ArrayList<CommMsg>>() {
                            }.getType());

                            list.addAll(a);
                            havemore = a.size() >= 20;
                            listAdapter.setData(list);
                        } else if ("8".equals(ResultCode)) {
                            LoginActivityController.getInstance().helloService(mContext);
                        } else {
                            if (currPage > 1) {
                                currPage--;
                            }
                            ToastUtil.showMessage(mContext, jsonObj.getString("ResultDesc"));
                        }
                    } catch (Exception e) {
                        if (currPage > 1) {
                            currPage--;
                        }
                        ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect) + "r1002");
                    }
                    listView.setRefreshTime(mCache.getAsString(timeFlag));
//					listAdapter.setFromMe(fromme);
                    listAdapter.notifyDataSetChanged();
                    listView.stopRefresh();
                    listView.stopLoadMore();
                    Date date = Calendar.getInstance().getTime();
                    String str_time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
                    mCache.put(timeFlag, str_time);
                }
            }
        });
    }

    @Override
    public void initListener() {
    }


    @Override
    public void onRefresh() {
        list.clear();
        currPage = 1;
        initDeatilsData();
    }

    @Override
    public void onLoadMore() {
        if (havemore) {
            currPage++;
            initDeatilsData();
        } else {
            listView.stopRefresh();
            listView.stopLoadMore();
            ToastUtil.showMessage(mContext, R.string.no_more);
        }
    }

    @Override
    public void onResume() {
        EventBusUtil.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case CLICK_POSITION://本单位文章详情
                if (timeFlag.equals("more_1") || timeFlag.equals("more_2")) {
                    CommMsg msg = (CommMsg) list.get(1);
                    listAdapter.getData().remove(msg);
                    listAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return false;
    }
}
