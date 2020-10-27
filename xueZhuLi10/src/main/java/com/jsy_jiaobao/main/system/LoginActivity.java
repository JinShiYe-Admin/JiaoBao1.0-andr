package com.jsy_jiaobao.main.system;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Base64Helper;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Coder;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.NetManager;
import com.jsy.xuezhuli.utils.RsaHelper;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.CommonDialog;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.UpdateService;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.po.push.AliasType;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.sys.URLs;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.jsy_jiaobao.po.sys.VersionInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.jsy.xuezhuli.utils.Constant.listUserIdentity;

/**
 * 登陆界面
 */
public class LoginActivity extends SherlockActivity implements ConstantUrl,
        OnClickListener {
    private static final String TAG = "LoginActivity";
    private Button btn_login;// 登陆按钮
    private IEditText edt_pwd;// 密码输入框
    private IEditText edt_username;// 用户名输入框
    private TextView tv_isAuto;// 是否自动登陆
    private TextView cb_isAuto;
    private TextView tv_version;
    private LinearLayout layout_parent;
    private ImageView iv_register;// 注册
    private ImageView iv_resetpwd;// 重置密码
    private String str_time;
    private String str_pwd;
    private String str_username;// 用户名
    private Context mContext;
    private ProgressDialog dialog;// 提示
    private SharedPreferences sp_sys;
    private SharedPreferences sp;
    private Editor editor, editor_sys;
    private boolean isAuto = false;
    private boolean isFirst = true;
    private boolean isClick = false;
    private int failure = 1;
    private String hellostr;
    private int mVisibleHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        MobclickAgent.openActivityDurationTrack(false);

        findViews();
        mContext = this;
        JSYApplication.getInstance().addActivity(this);
        VisitPublicHttp.getInstance().setContext(this);
        dialog = BaseUtils.showDialog(mContext, R.string.login_loginning);
        sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
        sp_sys = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);
        editor = sp.edit();
        editor_sys = sp_sys.edit();
        isAuto = sp.getBoolean("isAuto", true);
        setIsAutoBackgound();
        edt_username.setText(sp.getString("str_username", ""));
        isFirst = sp_sys.getBoolean("isFirst", true);
        if (isAuto) {
            edt_pwd.setText(sp.getString("UserPW", ""));

        }
        if (isAuto && !sp.getString("str_username", "").equals("")
                && !sp.getString("UserPW", "").equals("")) {
            str_pwd = edt_pwd.getTextString();
            str_username = edt_username.getTextString();
            if ("".equals(str_pwd) || "".equals(str_username)) {
                BaseUtils.shortToast(mContext,
                        getResources().getString(R.string.login_error_empty));
            } else {
                if (!str_username.equals(sp.getString("JiaoBaoHao", ""))) {
                    editor.remove("UnitID");
                    editor.remove("UnitName");
                    editor.commit();
                }
                editor.putBoolean("isAuto", isAuto).commit();
                editor.putString("str_username", str_username).commit();
                editor.putString("UserPW", str_pwd).commit();
                dialog.show();
                isClick = true;
                getTime();
            }
        }
        if ("".equals(sp_sys.getString("IAMSCID", ""))) {
            String uuid = "M" + UUID.randomUUID();
            editor_sys.putString("IAMSCID", uuid).commit();
        }
        if ("".equals(sp_sys.getString("ClientKey", ""))) {
            String ClientKey = BaseUtils.getRandomString(8);
            editor_sys.putString("ClientKey", ClientKey).commit();
            getTime();
        }
        String currCode = BaseUtils.getVersion(getApplicationContext());
        tv_version.setText(currCode);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ScreenWith = dm.widthPixels;// 宽度
        Constant.ScreenHeight = dm.heightPixels;// 高度
        getSupportActionBar().hide();
        layout_parent.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getKeyboardHeight();
                        System.out.println("------------"
                                + (mVisibleHeight - getResources()
                                .getDimensionPixelSize(
                                        R.dimen.px_to_dip_60)));
                        if (mVisibleHeight < sp.getInt("KeyboardHeight",
                                Constant.ScreenHeight / 2)) {
                            editor.putInt(
                                    "KeyboardHeight",
                                    mVisibleHeight
                                            - getResources()
                                            .getDimensionPixelSize(
                                                    R.dimen.px_to_dip_60))
                                    .commit();
                        }
                    }
                });
        initDialog();
    }

    private void test(){
        String url = "http://www.jiaobao.net/dl/JSY_JiaoBao.apk";
        Intent updateIntent = new Intent(mContext,
                UpdateService.class);
        updateIntent.putExtra("titleId",
                mContext.getString(R.string.app_name)
                        + "5.3.3");
        updateIntent.putExtra("url", url);
        mContext.startService(updateIntent);
    }

    private void initDialog() {
        boolean firstLogin= sp_sys.getBoolean("firstLogin",true);
        if(firstLogin){
            final CommonDialog dialog = new CommonDialog(this);
            dialog.setMessage(Constant.KNOWN)
//                .setImageResId(R.drawable.ic_launcher)
                    .setTitle("用户须知")
                    .setNegtive("不同意")
                    .setPositive("同意")
                    .setSingle(false).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                    editor_sys.putBoolean("firstLogin", false).commit();
                }

                @Override
                public void onNegtiveClick() {
                    final CommonDialog dialog2 = new CommonDialog(mContext);
                    dialog2.setMessage(Constant.NO_PASS_TEXT)
                            .setPositive("返回")
                            .setSingle(true).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            dialog2.dismiss();
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog2.dismiss();
                        }
                    }).show();
                }
            }).show();
        }
    }

    /**
     * @功能 初始化界面 加载监听事件
     */
    private void findViews() {
        btn_login = (Button) findViewById(R.id.login_btn_take);
        edt_pwd = (IEditText) findViewById(R.id.login_edt_pwd);
        edt_username = (IEditText) findViewById(R.id.login_edt_username);
        tv_isAuto = (TextView) findViewById(R.id.login_tv_autologin);
        cb_isAuto = (TextView) findViewById(R.id.login_cb_autologin);
        tv_version = (TextView) findViewById(R.id.login_tv_version);
        layout_parent = (LinearLayout) findViewById(R.id.login_layout_parent);
        iv_register = (ImageView) findViewById(R.id.login_iv_regeit);
        iv_resetpwd = (ImageView) findViewById(R.id.login_iv_resetpwd);
        tv_isAuto.setOnClickListener(this);
        cb_isAuto.setOnClickListener(this);
        iv_resetpwd.setOnClickListener(this);
        iv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    /**
     * 获取软键盘高度
     */
    private void getKeyboardHeight() {
        Rect r = new Rect();
        layout_parent.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }
        if (mVisibleHeight == visibleHeight) {
            return;
        }
        mVisibleHeight = visibleHeight;

    }

    /**
     * @功能 根据用户设置是否自动登陆，更新UI
     */
    private void setIsAutoBackgound() {
        if (isAuto) {
            cb_isAuto.setBackgroundResource(R.drawable.login_checked);
        } else {
            cb_isAuto.setBackgroundResource(R.drawable.login_uncheck);
        }
    }

    /**
     * @功能 界面的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_take:// 登陆
                MobclickAgent.onEvent(this,
                        getResources().getString(R.string.login_button));
                str_pwd = edt_pwd.getTextString();
                str_username = edt_username.getTextString();
                if ("".equals(str_pwd) || "".equals(str_username)) {
                    BaseUtils.shortToast(mContext,
                            getResources().getString(R.string.login_error_empty));
                } else {
                    if (!str_username.equals(sp.getString("JiaoBaoHao", ""))) {
                        editor.remove("UnitID");
                        editor.remove("UnitName");
                        editor.commit();
                    }
                    editor.putBoolean("isAuto", isAuto).commit();
                    editor.putString("str_username", str_username).commit();
                    editor.putString("UserPW", str_pwd).commit();
                    dialog.show();
                    isClick = true;
                    getTime();
                }
                break;
            case R.id.login_cb_autologin:// 自动登陆
            case R.id.login_tv_autologin:// 自动登陆
                MobclickAgent.onEvent(this,
                        getResources().getString(R.string.auto_login));
                if (isAuto) {
                    isAuto = false;
                } else {
                    isAuto = true;
                }
                setIsAutoBackgound();
                break;
            case R.id.login_iv_regeit:// 注册
                MobclickAgent.onEvent(this,
                        getResources().getString(R.string.login_register));
                Intent intent = new Intent(mContext, RegistActivity.class);
                intent.putExtra("page", "regeit");
                startActivity(intent);
                break;
            case R.id.login_iv_resetpwd:// 重置密码
                MobclickAgent.onEvent(this,
                        getResources().getString(R.string.login_reset_passwords));
                Intent intent1 = new Intent(mContext, RegistActivity.class);
                intent1.putExtra("page", "reset");
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    /**
     * 系统返回键的点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 用户登录
     */

    public void httpUserLogin() {
        try {
            LogUtils.e("----------httpUserLogin");
            String loginClass = "{\"UserName\":\"" + str_username
                    + "\",\"UserPW\":\"" + str_pwd + "\",\"TimeStamp\":\""
                    + str_time + "\"}";
            String rsaLoginstr = RsaHelper.encryptDataFromStr(loginClass);
            String sign = BaseUtils.getVersion(mContext) + rsaLoginstr
                    + sp_sys.getString("ClientKey", "") + str_time;
            String enMD5 = Base64Helper
                    .encode(Coder.encryptMD5(sign.getBytes()));
            RequestParams params = new RequestParams();
            params.addBodyParameter("CliVer", BaseUtils.getVersion(mContext));
            params.addBodyParameter("Loginstr", rsaLoginstr);
            params.addBodyParameter("TimeStamp", str_time);
            params.addBodyParameter("Sign", enMD5);
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                    user_login, params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");
                                if ("0".equals(ResultCode)) {
                                    String data = Des.decrypt(
                                            jsonObj.getString("Data"),
                                            sp_sys.getString("ClientKey", ""));
                                    JSONObject jsonData = new JSONObject(data);
                                    String nickname = jsonData
                                            .getString("Nickname");
                                    if ("null".equals(nickname)) {
                                        nickname = "";
                                        editor.putString("UserName", nickname);
                                    }
                                    editor.putString("Nickname", nickname);
                                    editor.putString("JiaoBaoHao",
                                            jsonData.getString("JiaoBaoHao"));
                                    String truename = TextUtils
                                            .isEmpty(jsonData
                                                    .getString("TrueName")) ? ""
                                            : jsonData.getString("TrueName");
                                    if ("null".equals(truename)) {
                                        truename = "";
                                        editor.putString("UserName", nickname);
                                    }
                                    editor.putString("TrueName", truename);
                                    editor.commit();
                                    setAlias(jsonData.getString("JiaoBaoHao"));
                                    httpGetUserInfo();
                                } else if ("8".equals(ResultCode)) {
                                    helloService();
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_login)
                                                            + "\n"
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources().getString(
                                                        R.string.login_login)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlias(String jiaobaohao) {
        PushAgent mPushAgent = PushAgent.getInstance(getApplication());
        mPushAgent.setAlias(jiaobaohao, AliasType.JINSHIYE,
                new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                        Log.d(TAG, "添加alias" + isSuccess + message);
                    }
                });
    }


    /**
     * 获取用户主系统下的信息
     */
    private void httpGetUserInfo() {
        try {
            LogUtils.e("----------httpGetUserInfo");
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                    user_getRoleIdentity, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");
                                if ("0".equals(ResultCode)) {
                                    String data = Des.decrypt(
                                            jsonObj.getString("Data"),
                                            sp_sys.getString("ClientKey", ""));
                                    listUserIdentity = GsonUtil
                                            .GsonToList(
                                                    data,
                                                    new TypeToken<ArrayList<UserIdentity>>() {
                                                    }.getType());

                                    try {
                                        if (listUserIdentity.get(
                                                listUserIdentity.size() - 1)
                                                .getRoleIdentity() == 5) {
                                            listUserIdentity
                                                    .remove(listUserIdentity
                                                            .size() - 1);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    editor.putInt("RoleIdentity", 1);
                                    editor.putInt("UnitID", 0);
                                    editor.putInt("UnitType", 1);
                                    editor.putString("UnitName", "");
                                    editor.putString("TabIDStr", "");
                                    editor.commit();
                                    if (listUserIdentity != null
                                            && listUserIdentity.size() > 0) {
                                        if (listUserIdentity.get(0)
                                                .getRoleIdentity() == 3) {
                                            List<UserUnit> units = listUserIdentity
                                                    .get(0).getUserUnits();
                                            if (units == null
                                                    || units.size() == 0) {
                                                List<UserClass> UserClasses = listUserIdentity
                                                        .get(0)
                                                        .getUserClasses();
                                                if (UserClasses != null
                                                        && UserClasses.size() > 0) {
                                                    editor.putInt(
                                                            "RoleIdentity", 3);
                                                    editor.putInt(
                                                            "UnitID",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getSchoolID());
                                                    editor.putInt("UnitType", 2);
                                                    editor.putString(
                                                            "UnitName",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getClassName());
                                                    editor.putString(
                                                            "TabIDStr",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getTabIDStr());
                                                    editor.commit();
                                                }
                                            }
                                        } else if (listUserIdentity.get(0)
                                                .getRoleIdentity() == 4) {
                                            List<UserUnit> units = listUserIdentity
                                                    .get(0).getUserUnits();
                                            if (units == null
                                                    || units.size() == 0) {
                                                List<UserClass> UserClasses = listUserIdentity
                                                        .get(0)
                                                        .getUserClasses();
                                                if (UserClasses != null
                                                        && UserClasses.size() > 0) {
                                                    editor.putInt(
                                                            "RoleIdentity", 4);
                                                    editor.putInt(
                                                            "UnitID",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getSchoolID());
                                                    editor.putInt("UnitType", 2);
                                                    editor.putString(
                                                            "UnitName",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getClassName());
                                                    editor.putString(
                                                            "TabIDStr",
                                                            UserClasses
                                                                    .get(0)
                                                                    .getTabIDStr());
                                                    editor.commit();
                                                }
                                            }
                                        } else {
                                            int uid;
                                            x:
                                            for (int i = 0; i < listUserIdentity
                                                    .size(); i++) {
                                                UserIdentity userIdentity = listUserIdentity
                                                        .get(i);
                                                uid = userIdentity
                                                        .getDefaultUnitId();

                                                for (int j = 0; j < userIdentity
                                                        .getUserUnits().size(); j++) {
                                                    UserUnit userUnit = userIdentity
                                                            .getUserUnits()
                                                            .get(j);
                                                    if (uid == userUnit
                                                            .getUnitID()
                                                            || uid == 0) {
                                                        editor.putInt(
                                                                "RoleIdentity",
                                                                userIdentity
                                                                        .getRoleIdentity());
                                                        editor.putInt(
                                                                "UnitID",
                                                                userUnit.getUnitID());
                                                        editor.putInt(
                                                                "UnitType",
                                                                userUnit.getUnitType());
                                                        editor.putString(
                                                                "UnitName",
                                                                userUnit.getUnitName());
                                                        editor.putString(
                                                                "TabIDStr",
                                                                userUnit.getTabIDStr());
                                                        editor.commit();
                                                        break x;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    VisitPublicHttp.getInstance()
                                            .httpGetLeaveSetting();
                                    VisitPublicHttp.getInstance()
                                            .changeCurUnit(false);
                                    getSRVUrl();
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_get_roleIdentity)
                                                            + "\n"
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_get_roleIdentity)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取各个应用主url
     */
    private void getSRVUrl() {
        try {
            LogUtils.e("----------getSRVUrl");
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                    sys_SRVUrl, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");
                                if ("0".equals(ResultCode)) {
                                    String data = Des.decrypt(
                                            jsonObj.getString("Data"),
                                            sp_sys.getString("ClientKey", ""));
                                    List<URLs> urlList = GsonUtil.GsonToList(
                                            data,
                                            new TypeToken<ArrayList<URLs>>() {
                                            }.getType());
                                    for (int i = 0; i < urlList.size(); i++) {// MainUrl,RiCUrl,KaoQUrl;http://www.jiaobao.net/JBApp2
                                        URLs urls = urlList.get(i);
                                        ACache.get(getApplicationContext())
                                                .put(urls.getName(),
                                                        urls.getValue());
                                    }
                                    getToken();
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_get_webUrl)
                                                            + "\n"
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_get_webUrl)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取云Token
     */
    protected void getToken() {
        try {
            LogUtils.e("----------getToken");
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getToken,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");

                                if ("0".equals(ResultCode)) {
                                    String data = Des.decrypt(
                                            jsonObj.getString("Data"),
                                            sp_sys.getString("ClientKey", ""));
                                    loginJBApp(data);
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_get_token)
                                                            + "\n"
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_get_token)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆JBApp
     */
    protected void loginJBApp(String data) {
        try {
            LogUtils.e("----------loginJBApp");
            RequestParams params = new RequestParams();
            params.addBodyParameter("cliToken", data);
            HttpUtil.getInstanceNew().send(
                    HttpRequest.HttpMethod.POST,
                    ACache.get(mContext.getApplicationContext()).getAsString(
                            "MainUrl")
                            + checkToken, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");

                                if ("0".equals(ResultCode)) {
                                    GetUserInfo();
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_get_webUrl)
                                                            + "\n"
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_get_webUrl)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息
     */
    protected void GetUserInfo() {
        try {
            LogUtils.e("----------GetUserInfo");
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                    GetUserInfo, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            BaseUtils.shortToast(mContext, getResources()
                                    .getString(R.string.error_serverconnect));
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String ResultCode = jsonObj
                                        .getString("ResultCode");

                                if ("0".equals(ResultCode)) {
                                    UserInfo userInfo = GsonUtil.GsonToObject(
                                            jsonObj.getString("Data"),
                                            UserInfo.class);
                                    ACache.get(getApplicationContext()).put(
                                            "userInfo", userInfo);
                                    if (userInfo.isIsKnlFeezeUser()) {
                                        ToastUtil
                                                .showMessage(
                                                        mContext,
                                                        mContext.getResources()
                                                                .getString(
                                                                        R.string.public_error_user));
                                    }
                                    if (userInfo.getDUnitId() == 0) {
                                        ToastUtil
                                                .showMessage(
                                                        mContext,
                                                        mContext.getResources()
                                                                .getString(
                                                                        R.string.public_error_nounit));
                                    }
                                    BaseUtils.hidepan(edt_pwd);
                                    BaseUtils.hidepan(edt_username);
                                    //登陆成功
                                    startActivity(new Intent(mContext,
                                            MessageCenterActivity.class));
                                    dialog.dismiss();
                                    LoginActivity.this.finish();
                                } else {
                                    dialog.dismiss();
                                    BaseUtils
                                            .shortToast(
                                                    mContext,
                                                    getResources()
                                                            .getString(
                                                                    R.string.login_get_userInfo)
                                                            + jsonObj
                                                            .getString("ResultDesc"));
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_get_userInfo)
                                                        + arg0.result
                                                        + "\n"
                                                        + getResources()
                                                        .getString(
                                                                R.string.login_error_login));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端注册
     */
    private void reg() {
        LogUtils.e("----------reg");
        String reg = "{\"AppID\":\"101001\",\"ID\":\""
                + sp_sys.getString("IAMSCID", "") + "\",\"Key\":\""
                + sp_sys.getString("ClientKey", "") + "\",\"TimeStamp\":\""
                + str_time + "\"}";
        String regstr = RsaHelper.encryptDataFromStr(reg);
        RequestParams params = new RequestParams();
        params.addBodyParameter("CliVer",
                BaseUtils.getVersion(getApplicationContext()));
        params.addBodyParameter("regstr", regstr);
        HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, sys_regClient,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dialog.dismiss();
                        ToastUtil.showMessage(
                                mContext,
                                getResources().getString(
                                        R.string.login_registerClient_failed));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        System.out.println(arg0);
                        try {
                            JSONObject jsonObj = new JSONObject(arg0.result);
                            String result = jsonObj.getString("ResultCode");
                            if ("0".equals(result)) {
                                String data = Des.decrypt(
                                        jsonObj.getString("Data"),
                                        sp_sys.getString("ClientKey", ""));
                                if (data.equals(str_time)) {
                                    editor_sys.putBoolean("isFirst", false)
                                            .commit();
                                    if (isClick) {
                                        helloService();
                                    }
                                }
                                isFirst = false;
                                System.out.println(data);
                            } else {
                                dialog.dismiss();
                                String ClientKey = BaseUtils.getRandomString(8);
                                editor_sys.putString("ClientKey", ClientKey)
                                        .commit();
                                BaseUtils
                                        .longToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_registerClient)
                                                        + "\n"
                                                        + jsonObj
                                                        .getString("ResultDesc"));
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            BaseUtils
                                    .shortToast(
                                            mContext,
                                            getResources()
                                                    .getString(
                                                            R.string.login_registerClient)
                                                    + arg0.result
                                                    + "\n"
                                                    + getResources()
                                                    .getString(
                                                            R.string.error_serverconnect));
                        }
                    }
                });
    }

    /**
     * 通讯握手 base64(MD5(Ver + IAMSCID + hellostr + ClientKey))
     */
    private void helloService() {
        try {
            LogUtils.e("----------helloService");
            hellostr = BaseUtils.getRandomString(4);
            editor.putString("hellostr", hellostr).commit();
            String sign = BaseUtils.getVersion(getApplicationContext())
                    + sp_sys.getString("IAMSCID", "") + hellostr
                    + sp_sys.getString("ClientKey", "");
            RequestParams params = new RequestParams();
            params.addBodyParameter("CliVer",
                    BaseUtils.getVersion(getApplicationContext()));
            params.addBodyParameter("hellostr", hellostr);
            params.addBodyParameter("IAMSCID", sp_sys.getString("IAMSCID", ""));
            params.addBodyParameter("Sign",
                    Base64Helper.encode(Coder.encryptMD5(sign.getBytes())));
            HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, sys_hello,
                    params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialog.dismiss();
                            System.out.println(arg1);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            System.out.println(arg0);
                            try {
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                String result = jsonObj.getString("ResultCode");
                                if ("0".equals(result)) {
                                    String data = Des.decrypt(
                                            jsonObj.getString("Data"),
                                            sp_sys.getString("ClientKey", ""));
                                    if (data.equals(hellostr)) {
                                        isFirst = false;
                                        if (isClick) {
                                            httpUserLogin();
                                        }
                                    }
                                } else {
                                    failure++;
                                    if (failure > 2) {
                                        String ClientKey = BaseUtils
                                                .getRandomString(8);
                                        editor_sys.putString("ClientKey",
                                                ClientKey).commit();
                                        reg();
                                    } else {
                                        helloService();
                                    }
                                }
                            } catch (Exception e) {
                                dialog.dismiss();
                                BaseUtils
                                        .shortToast(
                                                mContext,
                                                getResources()
                                                        .getString(
                                                                R.string.login_communication_handshake)
                                                        + getResources()
                                                        .getString(
                                                                R.string.error_serverconnect));
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean getInternet = false;

    // 获取网络时间
    private void getTime() {
        LogUtils.e("----------getTime");
        if (-10 == NetManager.checkNetwork(mContext)) {
            NetManager.alertNetError(mContext);
        }
        getInternet = false;
        HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
                "http://www.jiaobao.net/jbclient/Account/getcurTime",
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        try {
                            if (!getInternet) {
                                getInternet = true;
                                Date date = new Date(System.currentTimeMillis());
                                str_time = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss", Locale
                                        .getDefault()).format(date);

                                if (isFirst) {
                                    reg();
                                } else {
                                    helloService();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            if (!getInternet) {
                                getInternet = true;
                                JSONObject jsonObj = new JSONObject(arg0.result);
                                str_time = jsonObj.getString("Data");
                                if (isFirst) {
                                    reg();
                                } else {
                                    helloService();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBusUtil.register(this);
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_checkversion:// 得到最新版本
                VersionInfo versionInfo = (VersionInfo) list.get(1);
                int currCode = BaseUtils.getVersionCode(getApplicationContext());
                if (currCode < versionInfo.getVersionCode()) {
                    dialog_version(versionInfo);
                }
                break;
            case Constant.msgcenter_updataversion:
                DialogUtil.getInstance().cannleDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 提示框
     */
    protected void dialog_version(final VersionInfo versionInfo) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage(versionInfo.getIntroduce());
        MobclickAgent.onEvent(this,
                getResources().getString(R.string.login_new_version));
        builder.setTitle(R.string.detected_new_version);
        if (!versionInfo.getUpdata_1().equals("0")) {
            btn_login.setEnabled(false);
            builder.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog_version(versionInfo);
                }
            });
        }
        builder.setNeutralButton(R.string.up_grade,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = versionInfo.getUrl();
                        Intent updateIntent = new Intent(mContext,
                                UpdateService.class);
                        updateIntent.putExtra("titleId",
                                mContext.getString(R.string.app_name)
                                        + versionInfo.getVersionCode());
                        updateIntent.putExtra("url", url);
                        mContext.startService(updateIntent);
                        if (!versionInfo.getUpdata_1().equals("0")) {
                            DialogUtil.getInstance().getDialog(
                                    mContext,
                                    getResources().getString(
                                            R.string.public_loading));
                            DialogUtil.getInstance().setCanCancel(false);
                        }
                    }

                });
        if (versionInfo.getUpdata_1().equals("0")) {
            builder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        builder.create().show();
    }
}