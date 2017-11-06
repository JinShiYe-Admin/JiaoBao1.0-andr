package com.jsy_jiaobao.main.appcenter.sign;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.sign.QuickSignIn;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速签到页面
 */
public class QuickSignInActivity extends BaseActivity {
    private static final String TAG = "QuickSignInActivity";
    private Context mContext;
    private List<QuickSignIn> data_list = new ArrayList<QuickSignIn>();
    private ArrayAdapter<QuickSignIn> arr_adapter;
    private Button btn_sure;//确定按钮
    private Spinner spinner;//下拉列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //导航右上角查询按钮
        SubMenu sub_search = menu.addSubMenu("查询");
        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "查询");
//                Intent i = new Intent(mContext,
//                        WorkPlanActivity.class);
//                startActivity(i);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        setContentView(R.layout.activity_quick_sign_in);
        setActionBarTitle(R.string.function_signinquick);
        btn_sure = (Button) findViewById(R.id.btn_submit);
        spinner = (Spinner) findViewById(R.id.spinner);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null) {
                    QuickSignIn qsi = (QuickSignIn) spinner.getSelectedItem();
                    int UnitID = qsi.getUnitID();
                    String UnitName = qsi.getUnitName();
                    Log.d(TAG, UnitID + "-" + UnitName);
                    quickSignIn(String.valueOf(UnitID));
                } else {
                    BaseUtils
                            .shortToast(
                                    mContext,
                                    "请先选取签到的单位");
                }
            }
        });
    }

    private void initData() {
        if (Constant.listUserIdentity != null) {
            for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
                UserIdentity userIdentity = Constant.listUserIdentity.get(i);
                int role = userIdentity.getRoleIdentity();
                if (role == 1 || role == 2) {
                    // 教育局或者老师
                    List<UserUnit> UserUnits = userIdentity.getUserUnits();
                    if (UserUnits != null) {
                        for (int j = 0; j < UserUnits.size(); j++) {
                            UserUnit unit = UserUnits.get(j);
                            QuickSignIn qsi = new QuickSignIn(unit.getUnitID(), unit.getUnitName());
                            data_list.add(qsi);
                        }
                    }
                }
            }
        }
        if (data_list.size() == 0) {
            ToastUtil.showMessage(mContext, "没有允许签到的单位");
        } else {
            spinner.setVisibility(View.VISIBLE);
            btn_sure.setVisibility(View.VISIBLE);
            //适配器
            arr_adapter = new ArrayAdapter<QuickSignIn>(this, android.R.layout.simple_spinner_item, data_list);
            //设置样式
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //加载适配器
            spinner.setAdapter(arr_adapter);
        }

    }

    /**
     * 快速签到
     *
     * @param unitId 单位ID
     */
    private void quickSignIn(String unitId) {
        String accId = BaseActivity.sp.getString("JiaoBaoHao", "");
        Log.d(TAG, "accId" + accId);
        Log.d(TAG, "unitId" + unitId);
        DialogUtil.getInstance().getDialog(mContext, "正在签到");
        RequestParams params = new RequestParams();
        params.addBodyParameter("accId", accId);
        //params.addBodyParameter("sDate", "2017-11-1");
        //params.addBodyParameter("eDate", "2017-11-30");
        //params.addBodyParameter("RowCount", String.valueOf(0));
        params.addBodyParameter("unitId", unitId);
        HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                QuickSignIn, params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        DialogUtil.getInstance().cannleDialog();
                        if (null != mContext) {
                            if (BaseUtils.isNetworkAvailable(mContext)) {
                                ToastUtil.showMessage(mContext, R.string.phone_no_web);
                            } else {
                                ToastUtil.showMessage(mContext, R.string.error_internet);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        DialogUtil.getInstance().cannleDialog();
                        if (null != mContext) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj.getString("ResultCode");
                                String ResultDesc = jsonObj.getString("ResultDesc");
                                String Data = jsonObj.getString("Data");
                                Log.d(TAG, "ResultCode" + ResultCode);
                                Log.d(TAG, "ResultDesc" + ResultDesc);
                                Log.d(TAG, "Data" + Data);
                                if (!TextUtils.isEmpty(ResultCode)) {
                                    if ("0".equals(ResultCode)) {
                                        Log.d(TAG, "成功快速签到");
                                        ToastUtil.showMessage(mContext,
                                                R.string.fuck_success);
                                    } else if ("8".equals(ResultCode)) {
                                        LoginActivityController.getInstance().helloService(
                                                mContext);
                                    } else {
                                        ToastUtil.showMessage(mContext,
                                                jsonObj.getString("ResultDesc"));
                                    }
                                }
                            } catch (Exception e) {
                                ToastUtil.showMessage(mContext, mContext.getResources()
                                        .getString(R.string.error_serverconnect) + "r1002");
                            }
                        }
                    }
                }
        );
    }

}
