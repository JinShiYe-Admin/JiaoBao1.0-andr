package com.jsy_jiaobao.main.appcenter.sign;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速签到页面
 */
public class QuickSignInActivity extends BaseActivity {
    private static final String TAG = "QuickSigninActivity";
    private Context mContext;
    private List<String> data_list = new ArrayList<String>();
    private ArrayAdapter<String> arr_adapter;
    private Button btn_sure;//确定按钮
    private Spinner spinner;//下拉列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        Log.d(TAG, "initView");
        setContentView(R.layout.activity_quick_sign_in);
        setActionBarTitle(R.string.function_signinquick);
        btn_sure = (Button) findViewById(R.id.btn_submit);
        spinner = (Spinner) findViewById(R.id.spinner);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null) {
                    String text = spinner.getSelectedItem().toString();
                    Log.d(TAG, "确定 " + text);
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
        Log.d(TAG, "initData");
        Log.d(TAG, "listUserIdentity:" + Constant.listUserIdentity.size());
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
                            data_list.add(unit.getUnitName());
                        }
                    }
                }
            }
        }
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
    }

}
