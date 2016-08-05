package com.jsy_jiaobao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.webkit.WebView;

public class MyWebView extends WebView {
	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
