package com.jsy_jiaobao.main.appcenter.sign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 快速签到页面
 */
public class QuickSignInActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "QuickSignInActivity";
    private Context mContext;
    private List<QuickSignIn> dataList = new ArrayList<QuickSignIn>();//单位数组
    private ArrayAdapter<QuickSignIn> arr_adapter;
    private String photoPath;//图片路径
    private Button btn_sure;//确定按钮
    private Button btn_camera;//拍照按钮
    private Spinner spn_spinner;//下拉列表
    private ImageView img_camera;//拍照后的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_quick_sign_in);
        setActionBarTitle(R.string.function_signinquick);
        btn_sure = (Button) findViewById(R.id.btn_submit);
        btn_camera = (Button) findViewById(R.id.btn_camera);
        img_camera = (ImageView) findViewById(R.id.img_camera);
        spn_spinner = (Spinner) findViewById(R.id.spinner);
        btn_sure.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
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
                            dataList.add(qsi);
                        }
                    }
                }
            }
        }
        if (dataList.size() == 0) {
            ToastUtil.showMessage(mContext, "没有允许签到的单位");
        } else {
            spn_spinner.setVisibility(View.VISIBLE);
            btn_sure.setVisibility(View.VISIBLE);
            //隐藏拍照按钮和水印图片
            //btn_camera.setVisibility(View.VISIBLE);
            //img_camera.setVisibility(View.VISIBLE);
            //适配器
            arr_adapter = new ArrayAdapter<QuickSignIn>(this, android.R.layout.simple_spinner_item, dataList);
            //设置样式
            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //加载适配器
            spn_spinner.setAdapter(arr_adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit://点击签到
                if (spn_spinner.getSelectedItem() != null) {
                    QuickSignIn qsi = (QuickSignIn) spn_spinner.getSelectedItem();
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
                break;
            case R.id.btn_camera://点击拍照
                try {
                    photoPath = JSYApplication.getInstance().FILE_PATH;
                    File photoFile = PictureUtils
                            .createImageFile(photoPath);
                    if (photoFile != null) {
                        photoPath = photoFile.getAbsolutePath();
                        PictureUtils
                                .dispatchTakePictureIntent(
                                        QuickSignInActivity.this,
                                        photoFile, 1);
                    }
                } catch (Exception e) {
                    ToastUtil.showMessage(mContext,
                            R.string.open_camera_abnormal);
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Bitmap photo = PictureUtils.getbitmapFromURL(photoPath);
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String text = sdf.format(d);
                Bitmap bm = WaterMark(photo, text);
                img_camera.setImageBitmap(bm);
                break;
        }
    }

    /**
     * 水印
     *
     * @param bitmap 添加水印的图
     * @param text   添加的文字
     * @return
     */
    private Bitmap WaterMark(Bitmap bitmap, String text) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);//设置抗锯齿
        textPaint.setColor(Color.GREEN);//设置色值
        textPaint.setTextSize(w / 15);
        StaticLayout sl = new StaticLayout(text, textPaint, newBitmap.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        sl.draw(canvas);
        return newBitmap;
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
                Intent i = new Intent(mContext,
                        RecordActivity.class);
                startActivity(i);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            //手机硬件返回按键
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
