package com.jsy_jiaobao.main.schoolcircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建相册
 */
public class SpacePhotoCreateActivity extends BaseActivity {

	@ViewInject(R.id.createphoto_edt_name)
	private IEditText edt_name;
	@ViewInject(R.id.createphoto_spinner)
	private Spinner spinner;
	private Context mContext;
	private String UnitID;
	private int UnitType;
	private String Viewtype;
	private boolean create = false;
	private ArrayList<Map<String, String>> spdata = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			UnitType = savedInstanceState.getInt("UnitType");
			UnitID = savedInstanceState.getString("UnitID");
		} else {
			initPass();
		}
		initViews();
	}

	/**
	 * 获取Intent携带的数据
	 */
	private void initPass() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				UnitType = bundle.getInt("UnitType");
				UnitID = bundle.getString("UnitID");
			}
		}
	}

	/**
	 * 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("UnitID", UnitID);
		outState.putInt("UnitType", UnitType);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_createspacephoto);
		ViewUtils.inject(this);
		mContext = this;
		SpacePhotoCreateActivityController.getInstance().setContext(this);
		setActionBarTitle("创建相册");
		initViewType();
		getActionBarBackView().setOnClickListener(backListener);
		getActionBarTitleView().setOnClickListener(backListener);
	}

	/**
	 * ActionBar的监听事件
	 *
	 * @功能 存放数据 结束当前Activity
	 */
	OnClickListener backListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent result = new Intent(mContext,
					UnitSpacePhotoGroupActivity.class);
			result.putExtra("create", create);
			setResult(UnitType, result);
			finish();
		}
	};

	/**
	 * 设置相册权限数据
	 */
	private void initViewType() {
		if (UnitType == 9) {
			Map<String, String> map = new HashMap<>();
			map.put("ID", "0");// Viewtype 相册访问权限:0:私有；1：好友可访问；2：关注可访问；3：游客可访问
			map.put("Viewtype", "私有");
			spdata.add(map);
			Map<String, String> map1 = new HashMap<>();
			map1.put("ID", "1");// Viewtype 相册访问权限:0:私有；1：好友可访问；2：关注可访问；3：游客可访问
			map1.put("Viewtype", "好友可访问");
			spdata.add(map1);
			Map<String, String> map11 = new HashMap<>();
			map11.put("ID", "2");// Viewtype 相册访问权限:0:私有；1：好友可访问；2：关注可访问；3：游客可访问
			map11.put("Viewtype", "关注可访问");
			spdata.add(map11);
			Map<String, String> map111 = new HashMap<>();
			map111.put("ID", "3");// Viewtype
									// 相册访问权限:0:私有；1：好友可访问；2：关注可访问；3：游客可访问
			map111.put("Viewtype", "游客可访问");
			spdata.add(map111);
		} else {
			Map<String, String> map = new HashMap<>();
			map.put("ID", "0");// Viewtype 0无限制，1单位内可见
			map.put("Viewtype", "无限制");
			spdata.add(map);
			Map<String, String> map1 = new HashMap<>();
			map1.put("ID", "1");// Viewtype 0无限制，1单位内可见
			map1.put("Viewtype", "单位内可见");
			spdata.add(map1);
		}
		Viewtype = spdata.get(0).get("ID");
		SimpleAdapter spAdapter = new SimpleAdapter(this, spdata,
				android.R.layout.simple_list_item_1,
				new String[] { "Viewtype" }, new int[] { android.R.id.text1 });
		spinner.setAdapter(spAdapter);
		// 相册权限选择监听
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Viewtype = spdata.get(position).get("ID");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
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
	 * @功能 网络请求数据的接收和处理
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_unitspace_AddPhotoGroup:// 创建相册
			create = true;
			ToastUtil.showMessage(mContext, "创建成功");
			Intent result = new Intent();
			result.putExtra("create", create);
			setResult(UnitType, result);
			finish();
			break;
		case Constant.msgcenter_unitspace_CreateUnitPhotoGroup:// 创建单位相册
			create = true;
			ToastUtil.showMessage(mContext, "创建成功");
			result = new Intent();
			result.putExtra("create", create);
			setResult(UnitType, result);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * ActionBar的按钮 相册创建提交
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu sub_menu = menu.addSubMenu("完成");
		sub_menu.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub_menu.getItem().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						String name = edt_name.getTextString();
						RequestParams params = new RequestParams();
						if (TextUtils.isEmpty(name)) {
							ToastUtil.showMessage(mContext, "相册未命名");
						} else {
							String desInfo = "来自手机";
							if (UnitType == 9) {
								params.addBodyParameter("JiaoBaoHao",
										BaseActivity.sp.getString("JiaoBaoHao",
												""));
								params.addBodyParameter("PhotoGroupName", name);
								params.addBodyParameter("viewType", Viewtype);
								SpacePhotoCreateActivityController
										.getInstance().AddPhotoGroup(params);
							} else {
								params.addBodyParameter("CreateBy",
										BaseActivity.sp.getString("JiaoBaoHao",
												""));
								params.addBodyParameter("UnitID", UnitID);
								params.addBodyParameter("nameStr", name);
								params.addBodyParameter("DesInfo", desInfo);
								params.addBodyParameter("ViewType", Viewtype);
								SpacePhotoCreateActivityController
										.getInstance().CreateUnitPhotoGroup(
												params);
							}
						}
						return false;
					}
				});
		return true;
	}

	/**
	 * 系统返回键
	 *
	 * @功能 保存数据 关闭当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent result = new Intent(mContext,
					UnitSpacePhotoGroupActivity.class);
			result.putExtra("create", create);
			setResult(UnitType, result);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}