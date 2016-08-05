package com.jsy_jiaobao.main;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.NetManager;
import com.jsy_jiaobao.main.system.LoginActivity;

public class SplashActivity extends Activity {
	
	private SharedPreferences sp_sys;
	private Editor editor;
	private Intent i;
	private String str_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_splash);
        sp_sys = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);
		editor = sp_sys.edit();
		if ("".equals(sp_sys.getString("IAMSCID", ""))) {
			String uuid = "M"+UUID.randomUUID();
			editor.putString("IAMSCID",uuid).commit();
		}
		System.out.println("------------"+sp_sys.getString("IAMSCID", ""));

    }

    @Override
    protected void onResume() {
    	super.onResume();
    	if (NetManager.checkNetwork(this) == -10) {
    		NetManager.alertNetError(this);
		} else {
			new Thread(){
				public void run(){
					try {
						sleep(2000);
						i = new Intent(SplashActivity.this,LoginActivity.class);
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
