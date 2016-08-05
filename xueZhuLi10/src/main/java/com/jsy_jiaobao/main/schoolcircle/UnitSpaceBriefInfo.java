package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
import com.jsy_jiaobao.po.personal.UnitInfo;
import com.jsy_jiaobao.po.personal.Userinfo;
import com.jsy_jiaobao.po.sys.Human;
import com.jsy_jiaobao.po.sys.UnitGroupInfo;

/**
 * 单位简介
 * 
 * @author admin
 * 
 */
public class UnitSpaceBriefInfo extends BaseActivity {

	private LinearLayout layout_body;
	private Context mContext;
	private String item = "单位简介";
	private String ClickName;
	private String UnitName;
	private int UnitID;
	private int UnitType;
	private ACache mCache;
	private ArrayList<UnitGroupInfo> UnitGroupInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {

			ClickName = savedInstanceState.getString("ClickName");
			UnitName = savedInstanceState.getString("UnitName");
			UnitID = savedInstanceState.getInt("UnitID");
			UnitType = savedInstanceState.getInt("UnitType");
		} else {
			initPass();
		}
		initViews();
		initDatas();
	}

	/**
	 * 获取Intent携带的数据
	 */
	private void initPass() {

		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				ClickName = bundle.getString("ClickName");
				UnitName = bundle.getString("UnitName");
				UnitID = bundle.getInt("UnitID");
				UnitType = bundle.getInt("UnitType");
			}
		}
	}

	/**
	 * 保存可能销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("ClickName", ClickName);
		outState.putString("UnitName", UnitName);
		outState.putInt("UnitID", UnitID);
		outState.putInt("UnitType", UnitType);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_unitspaceexp);
		layout_body = (LinearLayout) findViewById(R.id.unitspace_layout_body);
		mContext = this;
		mCache = ACache.get(getApplicationContext(), "chat");
		UnitSpaceExpActivityController.getInstance().setContext(this);
		setActionBarTitle(ClickName);
	}

	/**
	 * 加载数据
	 */
	private void initDatas() {
		if (item.equals(ClickName)) {
			UnitSpaceExpActivityController.getInstance().getintroduce(
					String.valueOf(UnitID), String.valueOf(UnitType));
		}
	}

	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	/**
	 * EventBus 功能模块
	 * 
	 * @功能 获取数据并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_show_getintroduce:
			// 单位介绍
			String introduce = (String) list.get(1);
			if (!introduce.equals("")) {
				String start = introduce.substring(0, 1);
				if ("\"".equals(start)) {
					introduce = introduce.substring(1);
				}
				if (introduce.endsWith("\"")) {
					introduce = introduce.substring(0, introduce.length() - 1);
				}
			}
			WebView web_introduce = new WebView(mContext);
			WebSetUtils.getWebSetting(this, web_introduce);
			String content = introduce.replace("nowrap", "no wrap");
			String ht = "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /> <style type=\"text/css\">img{text-align:left;} body { max-width: 100%;}</style></head>";
			String body = "<body>"
					+ content.replace("\\\"", "\"").replace("\\n", "<br/>")
					+ "</body>";
			web_introduce.loadDataWithBaseURL(null, ht + body, "text/html",
					"utf-8", null);
			layout_body.addView(web_introduce);
			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回按键
	 * 
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
