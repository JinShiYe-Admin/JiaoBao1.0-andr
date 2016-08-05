package com.jsy.xuezhuli.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.StuErrDetailModel;
import com.jsy_jiaobao.po.workol.StuErrorModel;

public class WorkolUtils {
	private Context mContext;
	private String TAG="WorkolUtils";

	public WorkolUtils(Context context) {
		mContext = context;
	}

	public View getView(StuErrDetailModel qsPack, StuErrorModel stuErrorModel) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.workol_item_error, null);
		TextView tv_no = (TextView) view.findViewById(R.id.tv_questionNo);
		TextView tv_star = (TextView) view.findViewById(R.id.tv_questionRe);
		TextView tv_diff = (TextView) view.findViewById(R.id.tv_diff);
		LinearLayout ly_addWeb = (LinearLayout) view
				.findViewById(R.id.ly_addWeb);
		tv_no.setText(String.valueOf(stuErrorModel.getTabid()));
		String star = "";
		int i = stuErrorModel.getDoC();
		for (int j = 0; j < i; j++) {
			star = star + "*";
		}
		tv_star.setText(star);
		tv_diff.setText(String.valueOf(stuErrorModel.getQsLv()));
		String html = TextUtils.isEmpty(qsPack.getQsCon()) ? "" : qsPack
				.getQsCon();
		Log.d(TAG, html);
		// .replaceAll("width", "").replace("height", "");
		String qsAns = TextUtils.isEmpty(stuErrorModel.getAnswer()) ? ""
				: stuErrorModel.getAnswer();
		if (html == null || html.length() == 0) {

		} else {
			if (qsAns == null || qsAns.length() == 0) {

				html += "<br>作答:" + qsAns + "<font color='red'>" + "未作答"
						+ "</font>";
			} else {
				html += "<br>作答:" + qsAns;
			}
			String qsCoretAnswer = TextUtils
					.isEmpty(qsPack.getQsCorectAnswer()) ? "" : qsPack
					.getQsCorectAnswer();
			html += "<br>正确答案:" + qsCoretAnswer;
			html += "<br><font color='red'>"
					+ (TextUtils.isEmpty(qsPack.getQsExplain()) ? "" : qsPack
							.getQsExplain()) + "</font></br>";
			try {

				WebView webView = new WebView(mContext);
				webView.setId(R.id.list_item_webView1);
				webView.setFocusable(false);
				webView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				WebSettings mWebSettings = webView.getSettings();
				mWebSettings.setLoadWithOverviewMode(true);
				mWebSettings.setJavaScriptEnabled(true);
				mWebSettings.setSupportZoom(false);
				mWebSettings.setDefaultFixedFontSize(18);
				mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
				mWebSettings.setDefaultTextEncodingName("utf-8");// 避免中文乱码
				mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT
						| WebSettings.LOAD_CACHE_ELSE_NETWORK);
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						// TODO Auto-generated method stub
						view.loadUrl(url);
						return true;
					}
				});
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",
						null);
				ly_addWeb.addView(webView);
				return view;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return null;
	}

	public static WorkolUtils newInstance(Context mContext) {
		WorkolUtils workolUtils = new WorkolUtils(mContext);
		return workolUtils;
	}
}
