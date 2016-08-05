package com.jsy.xuezhuli.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;

public class WebSetUtils {
	public static void getWebSetting(Activity activity, WebView webView) {
		webView.setScrollBarStyle(0);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}
		});
		 webView.setOnTouchListener(new View.OnTouchListener() {
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			      return (true);
			    }
			  });
		WebSettings webSetting = webView.getSettings();
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setDefaultTextEncodingName("utf-8");// 避免中文乱码
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setNeedInitialFocus(false);
		webSetting.setUseWideViewPort(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT
				| WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSetting.setSupportZoom(false);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setUseWideViewPort(true);
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		if (mDensity <= 120) {
			webSetting.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity >= 160 && mDensity < 240) {
			webSetting.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity >= 240) {
			webSetting.setDefaultZoom(ZoomDensity.FAR);
		}
		webView.setVerticalScrollBarEnabled(false);
		webView.setVerticalScrollbarOverlay(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setHorizontalScrollbarOverlay(false);
	}
}
