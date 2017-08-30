package com.jsy_jiaobao.main.system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.jsy_jiaobao.main.R;

public class SplashActivity extends Activity {
    boolean click=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!click) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void click(View v){
        click=true;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("https://mp.weixin.qq.com/s/7NJEfy05F1ucBv_YeLg0tQ");
        intent.setData(content_url);
        startActivityForResult(intent,998);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
