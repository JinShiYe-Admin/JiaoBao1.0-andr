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
 * 单位成员界面
 * 
 * @author admin
 * 
 */
public class UnitSpaceExpActivity extends BaseActivity {

	private LinearLayout layout_body;
	private Context mContext;
	private String item = "单位成员";
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
	 * 获取Intent携带过来的数据
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
	 * 保存可能意外销毁的数据
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
		setContentLayout(R.layout.activity_unitspace_photo);
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
			String f = BaseActivity.sp.getString("JiaoBaoHao", "") + UnitID
					+ "0";
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) mCache
					.getAsObject(f);
			if (null != map) {
				ExpandableListView listview = new ExpandableListView(mContext);
				ChatCrowdAdapter adapter = new ChatCrowdAdapter(this);
				adapter.setPagePosition(2);
				listview.setAdapter(adapter);
				@SuppressWarnings("unchecked")
				ArrayList<UnitGroupInfo> UnitGroupsList = (ArrayList<UnitGroupInfo>) map
						.get("group");// 组列表
				@SuppressWarnings("unchecked")
				ArrayList<ArrayList<Human>> childList = (ArrayList<ArrayList<Human>>) map
						.get("human");
				adapter.setData(UnitGroupsList, childList);
				layout_body.addView(listview);
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					listview.expandGroup(i);
				}
			} else {
				UnitSpaceExpActivityController.getInstance().getUnitGroups(
						UnitID);
			}
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
	 * EventBus功能模块
	 * 
	 * @功能 获取网络请求返回的数据 并作处理
	 * @param list
	 */
	@SuppressWarnings("unchecked")
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
		case Constant.msgcenter_chat_getUnitGroups:
			// 取单位内所有组
			UnitGroupInfoList = (ArrayList<UnitGroupInfo>) list.get(1);
			UnitSpaceExpActivityController.getInstance().getUserInfoByUnitID(
					UnitID);
			break;
		case Constant.msgcenter_chat_getUserInfoByUnitID:
			// 取单位内所有人员
			ArrayList<Userinfo> list1 = (ArrayList<Userinfo>) list.get(1);
			ArrayList<ArrayList<Human>> childList = new ArrayList<ArrayList<Human>>();
			String mainURL = ACache.get(mContext.getApplicationContext())
					.getAsString("MainUrl");
			for (UnitGroupInfo groupInfo : UnitGroupInfoList) {
				ArrayList<Human> child = new ArrayList<Human>();
				for (int i = 0; i < list1.size(); i++) {
					Userinfo userinfo = list1.get(i);
					editor.putInt("UserID", userinfo.getUserID());
					String[] groups = userinfo.getGroupFlag().split(",");
					for (int j = 0; j < groups.length; j++) {
						if (groups[j].equals(String.valueOf(groupInfo
								.getGroupID()))) {
							Human human = new Human(String.valueOf(userinfo
									.getAccID()), userinfo.getUserName(),
									mainURL);
							child.add(human);
						}
					}
				}
				childList.add(child);
			}
			ExpandableListView listview = new ExpandableListView(mContext);
			listview.setGroupIndicator(null);
			ChatCrowdAdapter adapter = new ChatCrowdAdapter(this);
			adapter.setPagePosition(2);
			listview.setAdapter(adapter);
			adapter.setData(UnitGroupInfoList, childList);
			layout_body.addView(listview);
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				listview.expandGroup(i);
			}
			String f = BaseActivity.sp.getString("JiaoBaoHao", "") + UnitID
					+ "0";
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("group", UnitGroupInfoList);
			map.put("human", childList);
			mCache.put(f, map, 60 * 60 * 24);
			break;
		case Constant.msgcenter_notice_getMySubUnitInfo:// 下级单位
			NoticeGetUnitInfo getMySubUnitInfo = (NoticeGetUnitInfo) list
					.get(1);
			List<UnitInfo> list11 = getMySubUnitInfo.getList();
			ShowMyUnitsAdapter myunitAdapter = new ShowMyUnitsAdapter(mContext);
			myunitAdapter.setData(list11);
			ListView listView = new ListView(mContext);
			listView.setAdapter(myunitAdapter);
			layout_body.addView(listView);
			break;
		case Constant.msgcenter_show_getSchoolClassInfo:
			// 获取指定学校的所有班级基础数据
			// NoticeGetUnitClass getUnitClass = (NoticeGetUnitClass)
			// list.get(1);
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
