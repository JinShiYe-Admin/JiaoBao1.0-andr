package com.jsy_jiaobao.main.appcenter.workplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ShowPopup;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class WriteInfoActivity extends BaseActivity {

    private IEditText edt_place;
    private TextView tv_beginTime;
    private TextView tv_endTime;
    private IEditText edt_workPlan;
    private TextView tv_commitTime;
    private TextView tv_selectTime;
    private Spinner sp_unit;
    private Context mContext;
    private String str_begintime;
    private String str_endtime;
    private String str_place;
    private String str_workplan;
    private String str_committime;
    private String str_selecttime;
    private long between_days = 0;
    private String FlagName = "正常";

    private ShowPopup showPop;

    private View layout;

    private SharedPreferences sp;
    ArrayList<Map<String, String>> spdata = new ArrayList<>();
    int UnitID;
    int UnitType;
    String UnitName;
    int UserID;
    String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            str_selecttime = savedInstanceState.getString("selecttime");
            between_days = savedInstanceState.getLong("between_days");
            if (between_days > 0) {
                between_days = 1;
                FlagName = "晚报";
            }
        } else {
            Intent getInfo = getIntent();
            if (getInfo != null) {
                Bundle bundle = getInfo.getExtras();
                if (bundle != null) {
                    str_selecttime = bundle.getString("selecttime");
                    between_days = bundle.getLong("between_days");
                    if (between_days > 0) {
                        between_days = 1;
                        FlagName = "晚报";
                    }
                }
            }
        }
        setContentLayout(R.layout.ui_writeinfo);
        ViewUtils.inject(this);
        injectViews();
        mContext = this;
        initUI();
    }

    private void injectViews() {
        edt_place = (IEditText) findViewById(R.id.write_edt_place);
        tv_beginTime = (TextView) findViewById(R.id.write_tv_begintime);
        tv_endTime = (TextView) findViewById(R.id.write_tv_endtime);
        edt_workPlan = (IEditText) findViewById(R.id.write_edt_workplan);
        tv_commitTime = (TextView) findViewById(R.id.write_tv_committime);
        tv_selectTime = (TextView) findViewById(R.id.write_tv_selecttime);
        sp_unit = (Spinner) findViewById(R.id.write_sp_unit);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selecttime", str_selecttime);
        outState.putLong("between_days", between_days);
    }

    private void initUI() {
        layout = findViewById(R.id.layout_writeinfo);
        showPop = new ShowPopup(this);
        sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
        setActionBarTitle("填写信息");

        getTime(0);

        tv_selectTime.setText(str_selecttime);
        edt_place.setText(sp.getString("LOCATION_NAME", ""));

        if (Constant.listUserIdentity != null && Constant.listUserIdentity.size() > 0) {
            for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
                UserIdentity userIdentity = Constant.listUserIdentity.get(i);
                if (userIdentity.getRoleIdentity() == 1) {
                    List<UserUnit> UserUnits = userIdentity.getUserUnits();
                    if (UserUnits != null) {
                        for (int j = 0; j < UserUnits.size(); j++) {
                            UserUnit unit = UserUnits.get(j);
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", String.valueOf(unit.getUnitID()));
                            map.put("UnitName", unit.getUnitName());
                            map.put("TabIDStr", unit.getTabIDStr());
                            map.put("Type", String.valueOf(unit.getUnitType()));
                            map.put("Name", "教育局-" + unit.getUnitName());
                            spdata.add(map);
                        }
                    }
                } else if (userIdentity.getRoleIdentity() == 2) {
                    List<UserUnit> UserUnits = userIdentity.getUserUnits();
                    if (UserUnits != null) {
                        for (int j = 0; j < UserUnits.size(); j++) {
                            UserUnit unit = UserUnits.get(j);
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", String.valueOf(unit.getUnitID()));
                            map.put("Type", String.valueOf(unit.getUnitType()));
                            map.put("UnitName", unit.getUnitName());
                            map.put("TabIDStr", unit.getTabIDStr());
                            map.put("Name", "老师-" + unit.getUnitName());
                            spdata.add(map);
                        }
                    }
                    List<UserClass> userClasses = userIdentity.getUserClasses();
                    if (userClasses != null) {
                        for (int j = 0; j < userClasses.size(); j++) {
                            UserClass unit = userClasses.get(j);
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", String.valueOf(unit.getSchoolID()));
                            map.put("UnitName", unit.getClassName());
                            map.put("Type", "3");
                            map.put("TabIDStr", unit.getTabIDStr());
                            map.put("Name", "老师-" + unit.getClassName());
                            spdata.add(map);
                        }
                    }
                } else if (userIdentity.getRoleIdentity() == 3) {
                    List<UserClass> userClasses = userIdentity.getUserClasses();
                    if (userClasses != null) {
                        for (int j = 0; j < userClasses.size(); j++) {
                            UserClass unit = userClasses.get(j);
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", String.valueOf(unit.getSchoolID()));
                            map.put("Type", "3");
                            map.put("TabIDStr", unit.getTabIDStr());
                            map.put("UnitName", unit.getClassName());
                            map.put("Name", "家长-" + unit.getClassName());
                            spdata.add(map);
                        }
                    }
                } else if (userIdentity.getRoleIdentity() == 4) {
                    List<UserClass> userClasses = userIdentity.getUserClasses();
                    if (userClasses != null) {
                        for (int j = 0; j < userClasses.size(); j++) {
                            UserClass unit = userClasses.get(j);
                            Map<String, String> map = new HashMap<>();
                            map.put("ID", String.valueOf(unit.getSchoolID()));
                            map.put("Type", "3");
                            map.put("TabIDStr", unit.getTabIDStr());
                            map.put("UnitName", unit.getClassName());
                            map.put("Name", "学生-" + unit.getClassName());
                            spdata.add(map);
                        }
                    }
                }
            }
        }
        SimpleAdapter spAdapter = new SimpleAdapter(this, spdata, R.layout.textview_spinner, new String[]{"Name"}, new int[]{android.R.id.text1});
        sp_unit.setAdapter(spAdapter);
        if (spdata.size() > 0) {
            UnitID = Integer.parseInt(spdata.get(0).get("ID"));
            UnitType = Integer.parseInt(spdata.get(0).get("Type"));
            UnitName = spdata.get(0).get("UnitName");
        }
        sp_unit.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UnitID = Integer.parseInt(spdata.get(position).get("ID"));
                UnitType = Integer.parseInt(spdata.get(position).get("Type"));
                UnitName = spdata.get(position).get("UnitName");
                httpGetUserInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void httpGetUserInfo() {
        try {
            RequestParams params = new RequestParams();
            params.addBodyParameter("AccID", sp.getString("JiaoBaoHao", ""));
            params.addBodyParameter("UID", UnitID + "");
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, user_getUserInfo, params, new RequestCallBack<String>() {

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    if (mContext != null) {
                        try {
                            DialogUtil.getInstance().cannleDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        BaseUtils.shortToast(mContext, mContext.getResources().getString(R.string.error_serverconnect));
                    }
                }

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    if (mContext != null) {
                        try {
                            DialogUtil.getInstance().cannleDialog();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        try {
                            JSONObject jsonObj = new JSONObject(arg0.result);
                            String ResultCode = jsonObj.getString("ResultCode");
                            if ("0".equals(ResultCode)) {
                                String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
                                JSONObject jsonData = new JSONObject(data);
                                UserID = jsonData.getInt("UserID");
                                UserName = jsonData.getString("UserName");
//								System.out.println("----"+UnitID+";"+UserID);
                                editor.commit();

                            } else if ("8".equals(ResultCode)) {
                                LoginActivityController.getInstance().helloService(mContext);
                            } else {
                                BaseUtils.shortToast(mContext, jsonObj.getString("ResultDesc"));
                            }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取网络时间
    private void getTime(final int way) {
        HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getcurTime, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                DialogUtil.getInstance().cannleDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    JSONObject jsonObj = new JSONObject(arg0.result);
                    str_committime = jsonObj.getString("Data");
                    mHandler.sendEmptyMessage(way);
                } catch (JSONException e) {
                    DialogUtil.getInstance().cannleDialog();
                }
            }
        });
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv_commitTime.setText(str_committime);
                    break;
                case 10:
                    tv_commitTime.setText(str_committime);
                    commitHttp();
                    break;
                default:
                    break;
            }
        }

    };

    @OnClick({R.id.write_tv_begintime, R.id.write_tv_endtime})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_tv_begintime:
                showPop.showPop(layout, tv_beginTime);
                break;
            case R.id.write_tv_endtime:
                showPop.showPop(layout, tv_endTime);
                break;
            default:
                break;
        }
    }

    /**
     * 提交数据
     */
    private void commitHttp() {
        RequestParams params = new RequestParams();
        String Data = "{\"dSdate\":\"%s\",\"dEdate\":\"%s\",\"sWorkPlace\":\"%s\",\"sSubject\":\"%s\",\"allday\":\"%s\",\"dRecDate\":\"%s\",\"dUpdateDate\":\"%s\",\"UnitID\":\"%s\",\"UnitName\":\"%s\",\"UnitType\":\"%s\",\"UnitTypeName\":\"%s\",\"DetptID\":\"%s\",\"DetptName\":\"%s\",\"Checked\":\"%s\",\"Checker\":\"%s\",\"sRecorder\":\"%s\",\"RecodrderName\":\"%s\",\"Flag\":\"%s\",\"FlagName\":\"%s\"}";
        String SignInJsonData = String.format(Data, str_selecttime + " " + str_begintime, str_selecttime + " " + str_endtime, str_place, str_workplan, "0", str_committime, str_committime, UnitID, UnitName, UnitType, UnitName, 0, "", "0", "未审核", UserID, UserName, between_days, FlagName);
        params.addBodyParameter("Data", SignInJsonData);

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, ACache.get(mContext.getApplicationContext()).getAsString("RiCUrl") + commitWorkPlan, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                DialogUtil.getInstance().cannleDialog();
                BaseUtils.shortToast(mContext, getResources().getString(R.string.error_serverconnect));
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                try {
                    DialogUtil.getInstance().cannleDialog();
                    JSONObject jsonObj = new JSONObject(arg0.result);
                    String ResultCode = jsonObj.getString("ResultCode");
                    if ("0".equals(ResultCode)) {
                        BaseUtils.shortToast(mContext, "提交成功");
                        sp.edit().putString("LOCATION_NAME", str_place).commit();
                        tv_beginTime.setText("");
                        tv_endTime.setText("");
                        edt_workPlan.setText("");
                    } else {
                        BaseUtils.shortToast(mContext, "提交失败");
                    }
                } catch (Exception e) {
                    DialogUtil.getInstance().cannleDialog();
                    BaseUtils.shortToast(mContext, "提交失败1002");
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu sub_search = menu.addSubMenu("提交");
        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                str_place = edt_place.getTextString();
                str_begintime = tv_beginTime.getText().toString();
                str_endtime = tv_endTime.getText().toString();
                str_workplan = edt_workPlan.getTextString();
                try {
                    String[] begins = str_begintime.split(":");
                    String[] ends = str_endtime.split(":");
                    if (Integer.parseInt(ends[0]) < Integer.parseInt(begins[0])) {
                        ToastUtil.showMessage(mContext, "请填写正确的时间！");
                        return false;
                    } else if (Integer.parseInt(ends[0]) == Integer.parseInt(begins[0])) {
                        if (Integer.parseInt(ends[1]) == Integer.parseInt(begins[1])) {
                            if (Integer.parseInt(ends[2]) <= Integer.parseInt(begins[2])) {
                                ToastUtil.showMessage(mContext, "请填写正确的时间！");
                                return false;
                            }
                        } else if (Integer.parseInt(ends[1]) < Integer.parseInt(begins[1])) {
                            ToastUtil.showMessage(mContext, "请填写正确的时间！");
                            return false;
                        }
                    }
                    if (!TextUtils.isEmpty(str_begintime) && !TextUtils.isEmpty(str_endtime) && !TextUtils.isEmpty(str_place) && !TextUtils.isEmpty(str_workplan) && !TextUtils.isEmpty(str_committime)) {
                        DialogUtil.getInstance().getDialog(mContext, "数据上传中...请稍候");
                        DialogUtil.getInstance().setCanCancel(false);
                        getTime(10);
                    } else {
                        BaseUtils.shortToast(mContext, "不能为空!");
                    }
                } catch (NumberFormatException e) {
                    ToastUtil.showMessage(mContext, "请填写正确的时间！");
                    e.printStackTrace();
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
