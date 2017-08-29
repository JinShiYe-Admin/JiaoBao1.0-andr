package com.jsy_jiaobao.main.system;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.adBaseActivity;

public class adActivity extends adBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        WebView mWebView = (WebView)this.findViewById(R.id.webview);
        mWebView.loadUrl("https://mp.weixin.qq.com/s/7NJEfy05F1ucBv_YeLg0tQ");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            Intent intent = new Intent(adActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;

    }
}
