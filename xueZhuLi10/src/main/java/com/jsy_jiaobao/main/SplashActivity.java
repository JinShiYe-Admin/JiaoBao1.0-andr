package com.jsy_jiaobao.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.NetManager;
import com.jsy_jiaobao.main.system.LoginActivity;

import java.util.UUID;

public class SplashActivity extends Activity {


    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_splash);
        SharedPreferences sp_sys;
        sp_sys = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);
        Editor editor;
        editor = sp_sys.edit();
        if ("".equals(sp_sys.getString("IAMSCID", ""))) {
            String uuid = "M" + UUID.randomUUID();
            editor.putString("IAMSCID", uuid).apply();
        }
        System.out.println("------------" + sp_sys.getString("IAMSCID", ""));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetManager.checkNetwork(this) == -10) {
            NetManager.alertNetError(this);
        } else {
            new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                        i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }
}
