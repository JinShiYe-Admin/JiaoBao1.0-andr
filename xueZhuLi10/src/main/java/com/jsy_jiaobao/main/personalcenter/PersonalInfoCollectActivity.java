package com.jsy_jiaobao.main.personalcenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2020/10/30.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */
public class PersonalInfoCollectActivity extends BaseActivity {
	private Context mContext;
	private List<String> list = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_personalinfocollect);
		mContext = this;
		setActionBarTitle(getResources().getString(R.string.function_personal_infocollect));
		list.add(Permission.RECORD_AUDIO);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e("教宝校园", "Activity重启了");
	}


	Handler permissionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 10010://获取手机状态

					break;
				case 10011://存储空间信息

					break;
				case 10012://音视频信息（摄像头）

					break;
				case 10013://音视频信息（麦克风）

					break;
				default:
					break;
			}
		}
	};

	private void checkPermission_audio(final Handler permissionHandler){

	}

	private void checkPermission_video(final Handler permissionHandler){

	}

	private void checkPermission_readwrite(final Handler permissionHandler){

	}

	private void checkPermission_phone(final Handler permissionHandler){
		PackageManager pm = getPackageManager();
		boolean permission_readStorage = (PackageManager.PERMISSION_GRANTED ==
				pm.checkPermission("android.permission.READ_EXTERNAL_STORAGE", "packageName"));
		boolean permission_writeStorage = (PackageManager.PERMISSION_GRANTED ==
				pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "packageName"));
		boolean permission_caremera = (PackageManager.PERMISSION_GRANTED ==
				pm.checkPermission("android.permission.RECORD_AUDIO", "packageName"));

		if (!(permission_readStorage && permission_writeStorage && permission_caremera)) {
//            mContext.reqye(this, new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.RECORD_AUDIO,
//            }, 0x01);
		}
	}

	private void getPermission_audio(final Handler permissionHandler) {
		XXPermissions.with(this)
				// 申请单个权限
				.permission(Permission.RECORD_AUDIO)
				.request(new OnPermission() {
					@Override
					public void hasPermission(List<String> granted, boolean all) {
						if (all) {
//							ToastUtil.showMessage(activity,"获取录音和拍照权限成功");
							Message msg = Message.obtain(); // 实例化消息对象
							msg.what = 10013; // 消息标识
							msg.obj = "AA"; // 消息内容存放
							permissionHandler.sendMessage(msg);
						} else {
//                            ToastUtil.showMessage(activity,"获取权限成功，部分权限未正常授予，事务功能无法完整使用");
						}
					}

					@Override
					public void noPermission(List<String> denied, boolean never) {
						if (never) {
//                            ToastUtil.showMessage(activity,"录音未授权，您仅能发送事务文字，如果需要发送录音，请手动授予录音和拍照权限");
							// 如果是被永久拒绝就跳转到应用权限系统设置页面
							XXPermissions.startPermissionActivity(mContext, denied);
						} else {
//                            ToastUtil.showMessage(activity,"获取录音权限失败");
						}
					}
				});
	}

	private void getPermission_video(final Handler permissionHandler) {
		XXPermissions.with(this)
				// 申请单个权限
				.permission(Permission.CAMERA)
				.request(new OnPermission() {
					@Override
					public void hasPermission(List<String> granted, boolean all) {
						if (all) {
//							ToastUtil.showMessage(activity,"获取录音和拍照权限成功");
							Message msg = Message.obtain(); // 实例化消息对象
							msg.what = 10012; // 消息标识
							msg.obj = "AA"; // 消息内容存放
							permissionHandler.sendMessage(msg);
						} else {
//                            ToastUtil.showMessage(activity,"获取权限成功，部分权限未正常授予，事务功能无法完整使用");
						}
					}

					@Override
					public void noPermission(List<String> denied, boolean never) {
						if (never) {
//                            ToastUtil.showMessage(activity,"录音未授权，您仅能发送事务文字，如果需要发送录音，请手动授予录音和拍照权限");
							// 如果是被永久拒绝就跳转到应用权限系统设置页面
							XXPermissions.startPermissionActivity(mContext, denied);
						} else {
//                            ToastUtil.showMessage(activity,"获取录音权限失败");
						}
					}
				});

	}

	private void getPermission_readwrite(final Handler permissionHandler) {
		XXPermissions.with(this)
				// 申请单个权限
				.permission(Permission.READ_EXTERNAL_STORAGE)
				.permission(Permission.WRITE_EXTERNAL_STORAGE)
				.request(new OnPermission() {
					@Override
					public void hasPermission(List<String> granted, boolean all) {
						if (all) {
//							ToastUtil.showMessage(activity,"获取录音和拍照权限成功");
							Message msg = Message.obtain(); // 实例化消息对象
							msg.what = 10011; // 消息标识
							msg.obj = "AA"; // 消息内容存放
							permissionHandler.sendMessage(msg);
						} else {
//                            ToastUtil.showMessage(activity,"获取权限成功，部分权限未正常授予，事务功能无法完整使用");
						}
					}

					@Override
					public void noPermission(List<String> denied, boolean never) {
						if (never) {
//                            ToastUtil.showMessage(activity,"录音未授权，您仅能发送事务文字，如果需要发送录音，请手动授予录音和拍照权限");
							// 如果是被永久拒绝就跳转到应用权限系统设置页面
							XXPermissions.startPermissionActivity(mContext, denied);
						} else {
//                            ToastUtil.showMessage(activity,"获取录音权限失败");
						}
					}
				});
	}

	private void getPermission_phone(final Handler permissionHandler) {
		XXPermissions.with(this)
				// 申请单个权限
				.permission(Permission.READ_PHONE_STATE)
				.request(new OnPermission() {
					@Override
					public void hasPermission(List<String> granted, boolean all) {
						if (all) {
//							ToastUtil.showMessage(activity,"获取录音和拍照权限成功");
							Message msg = Message.obtain(); // 实例化消息对象
							msg.what = 10010; // 消息标识
							msg.obj = "AA"; // 消息内容存放
							permissionHandler.sendMessage(msg);
						} else {
//                            ToastUtil.showMessage(activity,"获取权限成功，部分权限未正常授予，事务功能无法完整使用");
						}
					}

					@Override
					public void noPermission(List<String> denied, boolean never) {
						if (never) {
//                            ToastUtil.showMessage(activity,"录音未授权，您仅能发送事务文字，如果需要发送录音，请手动授予录音和拍照权限");
							// 如果是被永久拒绝就跳转到应用权限系统设置页面
							XXPermissions.startPermissionActivity(mContext, denied);
						} else {
//                            ToastUtil.showMessage(activity,"获取录音权限失败");
						}
					}
				});
	}


}
