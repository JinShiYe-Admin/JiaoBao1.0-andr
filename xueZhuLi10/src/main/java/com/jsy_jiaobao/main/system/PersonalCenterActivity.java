package com.jsy_jiaobao.main.system;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.CommonDialog;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.PersonalInfoCollectActivity;
import com.jsy_jiaobao.po.push.AliasType;
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 个人中心界面
 */
public class PersonalCenterActivity extends BaseActivity implements
		OnClickListener {
	private Context mContext;
	private ImageView iv_photo;// 头像
	private TextView tv_nickname;// 昵称
	private TextView tv_truename;
	private TextView tv_myunits;// 我的单位
	private String photourl;
	private Intent intent = new Intent();
	private Uri photoUri;
	private TextView personal_tv_yinsi,personal_tv_known,personal_tv_fankui_detail,cancellation,personalInfoCollect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
		}
		initView();
	}

	/**
	 * 保存可能意外销毁的数据
	 * @功能 保存图片URI
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentLayout(R.layout.activity_personalcenter);
		mContext = this;
		setActionBarTitle(getResources().getString(R.string.function_pccenter));
		iv_photo = (ImageView) findViewById(R.id.personal_iv_photo);
		tv_nickname = (TextView) findViewById(R.id.personal_tv_nickname);
		tv_truename = (TextView) findViewById(R.id.personal_tv_truename);
		TextView tv_jiaobaohao = (TextView) findViewById(R.id.personal_tv_jiaobaohao);
		TextView tv_qiuzhi = (TextView) findViewById(R.id.personal_tv_qiuzhi);
		TextView tv_pwd = (TextView) findViewById(R.id.personal_tv_pwd);
		TextView tv_getunit = (TextView) findViewById(R.id.personal_tv_getunit);
		tv_myunits = (TextView) findViewById(R.id.personal_tv_units);
		personal_tv_yinsi= (TextView) findViewById(R.id.personal_tv_yinsi);
		personal_tv_known= (TextView) findViewById(R.id.personal_tv_known);
		personal_tv_fankui_detail= (TextView) findViewById(R.id.personal_tv_fankui_detail);
		cancellation= (TextView) findViewById(R.id.cancellation);
		personalInfoCollect = (TextView) findViewById(R.id.personalInfoCollect);
		personal_tv_fankui_detail.setText(Constant.FANKUI);
		personal_tv_yinsi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.YINSI_URL));
				it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				mContext.startActivity(it);
			}
		});
		personal_tv_known.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.KNOWN_URL));
				it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				mContext.startActivity(it);
			}
		});

		personalInfoCollect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PersonalInfoCollectActivity.class);
				mContext.startActivity(intent);
			}
		});
		cancellation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CommonDialog dialog = new CommonDialog(mContext);
				dialog.setMessage(Constant.CANCELLATION)
//                .setImageResId(R.drawable.ic_launcher)
						.setTitle("提醒")
						.setNegtive("取消")
						.setPositive("确定")
						.setSingle(false).setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
					@Override
					public void onPositiveClick() {
						delUser(dialog);
					}

					@Override
					public void onNegtiveClick() {
						dialog.dismiss();
					}
				}).show();
			}
		});
		photourl = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl")
				+ ConstantUrl.photoURL
				+ "?AccID="
				+ BaseActivity.sp.getString("JiaoBaoHao", "");
		JSYApplication.getInstance().bitmap.clearCache(photourl);
		tv_jiaobaohao.setText(BaseActivity.sp.getString("JiaoBaoHao", ""));
		// view加入监听
		tv_pwd.setOnClickListener(this);
		tv_getunit.setOnClickListener(this);
		tv_nickname.setOnClickListener(this);
		tv_truename.setOnClickListener(this);
		iv_photo.setOnClickListener(this);
		tv_qiuzhi.setOnClickListener(this);
		setUnits();// 获取单位
	}

	/**
	 * 退出系统
	 */
	private void exit() {
		MobclickAgent.onEvent(mContext, getResources().getString(R.string.MessageCenterActivity_quiteSystem));
		sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
		PushAgent mPushAgent = PushAgent.getInstance(getApplication());
		mPushAgent.deleteAlias(sp.getString("JiaoBaoHao", ""), AliasType.JINSHIYE, new UTrack.ICallBack() {

			@Override
			public void onMessage(boolean isSuccess, String message) {
				Log.d("PersonalCenterActivity", "刪除alias " + isSuccess + ":" + message);
			}

		});
		httpLogout();
		JSYApplication.getInstance().finishActivities();

	}

	/**
	 * 注销协议
	 */

	public void delUser(CommonDialog dialog) {
		DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		String JiaoBaoHao=BaseActivity.sp.getString("JiaoBaoHao", "");
		if(TextUtils.isEmpty(JiaoBaoHao)){
			ToastUtil.showMessage(mContext,"教包号为空，无法执行此操作");
		}else{
			params.addBodyParameter("accID", JiaoBaoHao);
			CallBack callback = new CallBack(dialog);
			callback.setUserTag(Constant.del_user);
			HttpUtil.InstanceSend(DelUser, params, callback);
		}

	}

	private class CallBack extends RequestCallBack<String> {

		private CommonDialog dialog;

		public CallBack (){
			super();
		}

		public CallBack (CommonDialog dialog){
			this.dialog=dialog;
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			DialogUtil.getInstance().cannleDialog();
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			DialogUtil.getInstance().cannleDialog();
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						dialog.dismiss();
						ToastUtil.showMessage(mContext,"账户注销成功，稍后将退出系统");
						Timer timer = new Timer();
						TimerTask task = new TimerTask() {
							@Override
							public void run() {
								exit();
							}
						};
						timer.schedule(task,3000,1000000);
					} else if ("8".equals(ResultCode)) {
						dealResponseInfo("", this.getUserTag());
						LoginActivityController.getInstance().helloService(mContext);
					} else {
						ToastUtil.showMessage(mContext, jsonObj.getString("ResultDesc"));
						dealResponseInfo("", this.getUserTag());
					}
				} catch (Exception e) {
					dealResponseInfo("", this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
			case Constant.del_user:
				DialogUtil.getInstance().cannleDialog();
				GetPicked getPicked = GsonUtil
						.GsonToObject(result, GetPicked.class);
				post.add(getPicked);
				break;
			default:
				break;
		}
		EventBusUtil.post(post);
	}


	/*
	 * 获取所在单位
	 *
	 * @ 1 教育局2老师3家长4学生
	 */
	private void setUnits() {
		String myunits = "";
		if (Constant.listUserIdentity != null) {
			for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
				UserIdentity userIdentity = Constant.listUserIdentity.get(i);
				if (userIdentity.getRoleIdentity() == 1) {// 教育局
					List<UserUnit> UserUnits = userIdentity.getUserUnits();
					if (UserUnits != null) {
						for (int j = 0; j < UserUnits.size(); j++) {
							UserUnit unit = UserUnits.get(j);
							myunits += getResources()
									.getString(
											R.string.personal_roleIdentity_boardOfEducation)
									+ "-" + unit.getUnitName() + "\n";
						}
					}
				} else if (userIdentity.getRoleIdentity() == 2) {// 老师
					List<UserUnit> UserUnits = userIdentity.getUserUnits();
					if (UserUnits != null) {
						for (int j = 0; j < UserUnits.size(); j++) {
							UserUnit unit = UserUnits.get(j);
							myunits += getResources().getString(
									R.string.personal_roleIdentity_teacher)
									+ "-" + unit.getUnitName() + "\n";
						}
					}
					List<UserClass> userClasses = userIdentity.getUserClasses();
					if (userClasses != null) {
						for (int j = 0; j < userClasses.size(); j++) {
							UserClass unit = userClasses.get(j);
							myunits += getResources().getString(
									R.string.personal_roleIdentity_teacher)
									+ "-" + unit.getClassName() + "\n";
						}
					}
				} else if (userIdentity.getRoleIdentity() == 3) {// 家长
					List<UserClass> userClasses = userIdentity.getUserClasses();
					if (userClasses != null) {
						for (int j = 0; j < userClasses.size(); j++) {
							UserClass unit = userClasses.get(j);
							myunits += getResources().getString(
									R.string.personal_roleIdentity_parent)
									+ "-" + unit.getClassName() + "\n";
						}
					}
				} else if (userIdentity.getRoleIdentity() == 4) {// 学生
					List<UserClass> userClasses = userIdentity.getUserClasses();
					if (userClasses != null) {
						for (int j = 0; j < userClasses.size(); j++) {
							UserClass unit = userClasses.get(j);
							myunits += getResources().getString(
									R.string.personal_roleIdentity_student)
									+ unit.getClassName() + "\n";
						}
					}
				}
			}
		}
		if (TextUtils.isEmpty(myunits)) {
			myunits = getResources().getString(
					R.string.temporarily_notTo_joinTheUnit);
		} else {
			myunits = getResources().getString(R.string.relation_unit) + "\n"
					+ myunits;

		}
		tv_myunits.setText(myunits);
	}

	/**
	 * 点击事件接口
	 * @功能 view点击事件监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.personal_tv_qiuzhi:// 求知个人中心
				intent.setClass(mContext, QiuzhiPersonalCenterActivity.class);
				startActivity(intent);
				break;
			case R.id.personal_tv_pwd:// 重置密码
				intent.setClass(mContext, PersonalCenterChangeActivity.class);
				intent.putExtra("way", "pwd");
				startActivity(intent);
				break;
			case R.id.personal_tv_getunit:// 加入单位
				intent.setClass(mContext, PersonalCenterChangeActivity.class);
				intent.putExtra("way", "unit");
				startActivity(intent);
				break;
			case R.id.personal_tv_truename:// 姓名
				intent.setClass(mContext, PersonalCenterChangeActivity.class);
				intent.putExtra("way", "name");
				startActivity(intent);
				break;
			case R.id.personal_tv_nickname:// 昵称
				intent.setClass(mContext, PersonalCenterChangeActivity.class);
				intent.putExtra("way", "name");
				startActivity(intent);
				break;
			case R.id.personal_iv_photo:// 头像
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setIcon(android.R.drawable.ic_menu_gallery);
				builder.setTitle(R.string.choose_source);
				builder.setPositiveButton(R.string.camera,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								try {
									photoPath = JSYApplication.getInstance().FILE_PATH;
									File photoFile = PictureUtils
											.createImageFile(photoPath);
									if (photoFile != null) {
										photoPath = photoFile.getAbsolutePath();
										PictureUtils.dispatchTakePictureIntent(
												PersonalCenterActivity.this,
												photoFile, 1);
									}
								} catch (Exception e) {
									ToastUtil.showMessage(mContext,
											R.string.open_camera_abnormal);
								}
							}
						});
				builder.setNegativeButton(R.string.album,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent ii = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
								startActivityForResult(ii, 2);
							}
						});
				builder.create().show();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		tv_nickname.setText(BaseActivity.sp.getString("Nickname",
				getResources().getString(R.string.new_user)));
		tv_truename.setText(BaseActivity.sp.getString("TrueName",
				getResources().getString(R.string.new_user)));
		JSYApplication.getInstance().bitmap.clearCache(photourl);
		JSYApplication.getInstance().bitmap.display(iv_photo, photourl);
		setUnits();
	}

	private String photoPath;
	private Bitmap bitmap;
	private File file;
	private Thread thread;

	/**
	 * 接收上个组件留存的数据
	 * @功能 从相机或相册获取到出片并处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:// 相机
				try {
					DialogUtil.getInstance().getDialog(mContext, R.string.loading);
					creatThread();
				} catch (Exception e1) {
					bitmap = null;
					e1.printStackTrace();
				}
				break;
			case 2:// 相册
				try {
					if (data != null) {
						Uri uri1 = data.getData();
						Cursor cursor = this.getContentResolver().query(uri1, null,
								null, null, null);
						cursor.moveToFirst();
						photoPath = cursor.getString(1);
						DialogUtil.getInstance().getDialog(mContext,
								R.string.loading);
						creatThread();
					}
				} catch (Exception e) {
					bitmap = null;
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 创建线程
	 * @功能 处理图片
	 */
	private void creatThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					bitmap = PictureUtils.getbitmapFromURL(photoPath);
					if (bitmap == null) {
						file = null;
						DialogUtil.getInstance().cannleDialog();
					} else {
						file = PictureUtils.saveBitmapFile(bitmap, photoPath);
					}
					handler.sendEmptyMessage(789);
				}
			});
		}
		thread.start();
	}

	/**
	 * 获取到的图片文件上传
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 789:
					DialogUtil.getInstance().cannleDialog();
					thread.interrupt();
					thread = null;
					if (file != null) {
						uploadFile(file);
					}
					break;
				default:
					break;
			}
		}
	};

	/**
	 * @method 自定义方法
	 * @功能 上传图片文件
	 */
	private void uploadFile(File file) {
		DialogUtil.getInstance().getDialog(mContext, R.string.uploading);
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", file);
		HttpUtil.getInstanceNew().send(
				HttpRequest.HttpMethod.POST,
				ACache.get(mContext.getApplicationContext()).getAsString(
						"MainUrl")
						+ updatefaceimg, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (mContext != null) {
							if (BaseUtils.isNetworkAvailable(mContext)) {
								ToastUtil.showMessage(mContext,
										R.string.phone_no_web);
							}
							if (!isFinishing()) {
								DialogUtil.getInstance().cannleDialog();
								ToastUtil.showMessage(
										mContext,
										mContext.getResources().getString(
												R.string.error_internet));
							}
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (!isFinishing()) {
							DialogUtil.getInstance().cannleDialog();
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								JSYApplication.getInstance().bitmap
										.clearCache(photourl);
								JSYApplication.getInstance().bitmap.display(
										iv_photo, photoPath);
								ToastUtil.showMessage(mContext,
										jsonObj.getString("ResultDesc"));
							} catch (Exception e) {
								ToastUtil.showMessage(
										mContext,
										mContext.getResources().getString(
												R.string.error_serverconnect));
							}
						}
					}
				});
	}

	/**
	 * 系统返回按键
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}