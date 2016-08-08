package com.jsy_jiaobao.main.schoolcircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.appcenter.GalleryActivity;
import com.jsy_jiaobao.po.app.gallery.Gallery;
import com.jsy_jiaobao.po.personal.UnitPGroup;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 相册列表界面
 * @author admin
 */
public class UnitSpacePhotoGroupActivity extends BaseActivity {

	private LinearLayout layout_body;
	private Context mContext;
	private String ClickName;
	private String UnitName;
	/** 个人相册时为教宝号 */
	private String UnitID;
	/** 个人相册时为9 */
	private int UnitType;
	private UnitSpacePhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			UnitType = savedInstanceState.getInt("UnitType");
			ClickName = savedInstanceState.getString("ClickName");
			UnitName = savedInstanceState.getString("UnitName");
			UnitID = savedInstanceState.getString("UnitID");
		} else {
			initPass();// 获取Intent携带的数据
		}
		initViews();// 初始化界面
		initDatas();// 加载数据
	}

	/***
	 * @method 自定义方法
	 * @功能 获取Intent 携带的数据
	 */
	private void initPass() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				UnitType = bundle.getInt("UnitType");
				ClickName = bundle.getString("ClickName");
				UnitName = bundle.getString("UnitName");
				UnitID = bundle.getString("UnitID");
			}
		}
	}

	/**
	 * 重写方法
	 * 
	 * @功能 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("UnitType", UnitType);
		outState.putString("ClickName", ClickName);
		outState.putString("UnitName", UnitName);
		outState.putString("UnitID", UnitID);
	}

	/**
	 * @功能 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_unitspace_photo);
		layout_body = (LinearLayout) findViewById(R.id.unitspace_layout_body);
		mContext = this;
		mCache = ACache.get(getApplicationContext(), "chat");
		UnitSpaceActivityController.getInstance().setContext(this);
		setActionBarTitle(ClickName);
		GridView gridView = new GridView(this);
		gridView.setNumColumns(3);
		layout_body.addView(gridView);
		adapter = new UnitSpacePhotoAdapter(mContext);
		gridView.setAdapter(adapter);
	}

	boolean myunit = false;

	/**
	 * @功能 加载数据
	 */
	private void initDatas() {
		if (Constant.listUserIdentity != null) {
			for (int i1 = 0; i1 < Constant.listUserIdentity.size(); i1++) {
				UserIdentity userIdentity = Constant.listUserIdentity.get(i1);
				for (int j = 0; j < userIdentity.getUserUnits().size(); j++) {
					UserUnit userUnit = userIdentity.getUserUnits().get(j);
					if (String.valueOf(userUnit.getUnitID()).equals(UnitID)) {
						myunit = true;
						break;
					}
				}
				for (int j = 0; j < userIdentity.getUserClasses().size(); j++) {
					UserClass userUnit = userIdentity.getUserClasses().get(j);
					if (String.valueOf("-" + userUnit.getClassID()).equals(
							UnitID)) {
						myunit = true;
						break;
					}
				}
			}
		}
		if (UnitType == 9) {
			UnitSpaceActivityController.getInstance().GetPhotoList(UnitID);
		} else {
			UnitSpaceActivityController.getInstance().GetUnitPGroup(UnitID);
			UnitSpaceActivityController.getInstance().getmyUserClass(
					sp.getInt("UnitID", 0));
		}
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		UnitSpaceActivityController.getInstance().setContext(this);
		EventBusUtil.register(this);
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		MobclickAgent.onPause(this);
		super.onPause();
	}

	private ArrayList<UnitPGroup> getPgroupList;

	/**
	 * EventBus 功能模块
	 * 
	 * @功能 获取网络请求返回数据 并处理
	 */
	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_unitspace_GetUnitPGroup:// 获取相册数组
			getPgroupList = (ArrayList<UnitPGroup>) list.get(1);
			adapter.setData(getPgroupList);
			adapter.notifyDataSetChanged();
			break;
		case Constant.msgcenter_personalspace_GetPhotoList:// 获取照片列表
			ArrayList<Gallery> getMyList = (ArrayList<Gallery>) list.get(1);
			adapter.setData(getMyList);
			adapter.setJiaoBaoHao(UnitID);
			adapter.notifyDataSetChanged();
			break;
		case Constant.msgcenter_publish_getmyUserClass:// 获取我的班级
			ArrayList<UserClass> list1 = (ArrayList<UserClass>) list.get(1);
			for (int i = 0; i < list1.size(); i++) {
				if (UnitID.equals(String
						.valueOf(list1.get(i).getClassID() * -1))) {
					myunit = true;
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回键
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

	/**
	 * ActionBar上的按钮
	 * @功能 创建相册 上传照片
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub_menu = menu.addSubMenu(R.string.system).setIcon(
				R.drawable.top_btn_menu);
		sub_menu.add(1, 1011, 0, R.string.new_album);
		sub_menu.add(1, 1012, 0, R.string.upload_photo);
		sub_menu.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		// 上传照片
		sub_menu.getItem(1).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						MobclickAgent
								.onEvent(
										mContext,
										mContext.getResources()
												.getString(
														R.string.UnitSpacePhotoGroupActivity_uploadPhoto));
						Intent intent = new Intent(mContext,
								GalleryActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("UnitType", UnitType);
						bundle.putString("UnitID", UnitID);
						if (UnitType == 9) {// 个人相册验证教宝号是不是自己的
							if (UnitID.equals(BaseActivity.sp.getString(
									"JiaoBaoHao", ""))) {
								intent.putExtras(bundle);
								startActivityForResult(intent, UnitType);
							} else {
								ToastUtil.showMessage(mContext,
										R.string.no_permission);
							}
						} else {
							ArrayList<UnitPGroup> list = new ArrayList<UnitPGroup>();
							if (null != getPgroupList) {
								for (int i = 0; i < getPgroupList.size(); i++) {
									if (BaseActivity.sp.getString("JiaoBaoHao",
											"").equals(
											String.valueOf(getPgroupList.get(i)
													.getCreateByjiaobaohao()))) {
										list.add(getPgroupList.get(i));
									}
								}
							}
							if (list.size() == 0) {
								ToastUtil.showMessage(mContext,
										R.string.no_permission);
							} else {
								bundle.putSerializable("groupList", list);
								intent.putExtras(bundle);
								startActivityForResult(intent, UnitType);
							}

						}
						return false;
					}
				});
		// 创建相册
		sub_menu.getItem(0).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						MobclickAgent
								.onEvent(
										mContext,
										mContext.getResources()
												.getString(
														R.string.UnitSpacePhotoGroupActivity_createAlbum));
						Intent intent = new Intent(mContext,
								SpacePhotoCreateActivity.class);
						Bundle args = new Bundle();
						args.putInt("UnitType", UnitType);
						args.putString("UnitID", UnitID);
						intent.putExtras(args);
						if (UnitType == 9) {
							if (UnitID.equals(BaseActivity.sp.getString(
									"JiaoBaoHao", ""))) {
								startActivityForResult(intent, 9);
							} else {
								ToastUtil.showMessage(mContext,
										R.string.no_permission);
							}
						} else {
							if (!myunit) {
								ToastUtil.showMessage(mContext,
										R.string.no_permission);
							} else {
								startActivityForResult(intent, UnitType);
							}
						}
						return false;
					}
				});
		return true;
	}

	boolean create = false;

	/**
	 * 处理上个组建返回的值
	 * 
	 * @功能 创建相册
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 9:
			if (data != null) {
				create = data.getExtras().getBoolean("create");
				if (create) {
					UnitSpaceActivityController.getInstance().GetPhotoList(
							UnitID);
				}
			}
			break;
		default:
			if (data != null) {
				create = data.getExtras().getBoolean("create");
				if (create) {
					UnitSpaceActivityController.getInstance().GetUnitPGroup(
							UnitID);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}