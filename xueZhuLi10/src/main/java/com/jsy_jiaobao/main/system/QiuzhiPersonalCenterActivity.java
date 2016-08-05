package com.jsy_jiaobao.main.system;

import java.io.File;
import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

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
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.sys.QiuZhiPoint;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

/**
 * 求知个人中心
 * 
 * @author admin
 * 
 */
public class QiuzhiPersonalCenterActivity extends BaseActivity implements
		OnClickListener {
	final static int Q_TYPE_MYATT = 1;// 我关注的问题
	final static int Q_TYPE_MYQUESTION = 2;// 我提问过的问题
	final static int Q_TYPE_MYANSWER = 3;// 我回答过的问题
	final static int Q_TYPE_ATME = 4;// /邀请我回答的问题
	final static int Q_TYPE_ATTP = 5;// 我关注的人
	final static int Q_TYPE_ATTC = 6;// 我关注的话题
	final static int Q_TYPE_ATTM = 7;// 关注我的人
	final static int Q_TYPE_MYCOMMENT = 8;// 我做出的评论
	Intent intent = new Intent();
	private Context mContext;
	private ImageView iv_photo;// 头像
	private TextView tv_name;// 姓名
	private TextView tv_score;// 积分
	private TextView tv_attc;// 我关注的话题
	private TextView tv_attp;// 我关注的人
	private TextView tv_attm;// 关注我的人
	private TextView tv_answeredq;// 我回答过的问题
	private TextView tv_question;// 我提问过的问题
	private TextView tv_attquestion;// 我关注的问题
	private TextView tv_atme;// 邀请我回答的问题
	private TextView tv_mycomment;// 我做出的评论
	private Uri photoUri;
	private String photourl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
		} else {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
			}
		}
		initView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
	}

	private void initView() {
		setContentLayout(R.layout.activity_qiuzhi_personal);
		mContext = this;
		iv_photo = (ImageView) findViewById(R.id.qiuzhip_iv_photo);
		tv_score = (TextView) findViewById(R.id.qiuzhip_tv_score);
		tv_name = (TextView) findViewById(R.id.qiuzhip_tv_name);
		tv_answeredq = (TextView) findViewById(R.id.qiuzhip_tv_answeredq);
		tv_attm = (TextView) findViewById(R.id.qiuzhip_tv_attm);
		// tv_attmnum = (TextView) findViewById(R.id.qiuzhip_tv_attmnum);
		tv_attp = (TextView) findViewById(R.id.qiuzhip_tv_attp);
		// tv_attpnum = (TextView) findViewById(R.id.qiuzhip_tv_attpnum);
		tv_attc = (TextView) findViewById(R.id.qiuzhip_tv_attc);
		// tv_attcnum = (TextView) findViewById(R.id.qiuzhip_tv_attcnum);
		tv_question = (TextView) findViewById(R.id.qiuzhip_tv_question);
		tv_attquestion = (TextView) findViewById(R.id.qiuzhip_tv_attquestion);
		tv_atme = (TextView) findViewById(R.id.qiuzhip_tv_atme);
		tv_mycomment = (TextView) findViewById(R.id.qiuzhip_tv_mycomment);
		tv_question.setOnClickListener(this);
		tv_answeredq.setOnClickListener(this);
		tv_attquestion.setOnClickListener(this);
		tv_atme.setOnClickListener(this);
		tv_mycomment.setOnClickListener(this);
		tv_attc.setOnClickListener(this);
		tv_attp.setOnClickListener(this);
		tv_attm.setOnClickListener(this);
		iv_photo.setOnClickListener(this);
		setActionBarTitle(R.string.qiuZhi_personal_center);
		intent.setClass(mContext, QiuzhiQuestionListActivity.class);
		tv_name.setText(BaseActivity.sp.getString("Nickname", getResources()
				.getString(R.string.new_user)));
		photourl = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl")
				+ ConstantUrl.photoURL
				+ "?AccID="
				+ BaseActivity.sp.getString("JiaoBaoHao", "");
		JSYApplication.getInstance().bitmap.display(iv_photo, photourl);
		GetMyPointsMonth();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 我关注的人
		case R.id.qiuzhip_tv_attp:
			ToastUtil.showMessage(mContext, "该功能暂未开放");
			// intent.putExtra("Q_TYPE", Q_TYPE_ATTP);
			// startActivity(intent);
			break;
		// 我关注的话题
		case R.id.qiuzhip_tv_attc:
			intent.putExtra("Q_TYPE", Q_TYPE_ATTC);
			startActivity(intent);
			break;
		// 关注我的人
		case R.id.qiuzhip_tv_attm:
			ToastUtil.showMessage(mContext, "该功能暂未开放");
			// intent.putExtra("Q_TYPE", Q_TYPE_ATTM);
			// startActivity(intent);
			break;
		// 我提问过的问题
		case R.id.qiuzhip_tv_question:
			intent.putExtra("Q_TYPE", Q_TYPE_MYQUESTION);
			startActivity(intent);
			break;
		// 我回答过的问题
		case R.id.qiuzhip_tv_answeredq:
			intent.putExtra("Q_TYPE", Q_TYPE_MYANSWER);
			startActivity(intent);
			break;
		// 我关注的问题
		case R.id.qiuzhip_tv_attquestion:
			intent.putExtra("Q_TYPE", Q_TYPE_MYATT);
			startActivity(intent);
			break;
		// 邀请我回答的问题
		case R.id.qiuzhip_tv_atme:
			intent.putExtra("Q_TYPE", Q_TYPE_ATME);
			startActivity(intent);
			break;
		// 我做出的评论
		case R.id.qiuzhip_tv_mycomment:
			intent.putExtra("Q_TYPE", Q_TYPE_MYCOMMENT);
			startActivity(intent);
			break;
		case R.id.qiuzhip_iv_photo:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_menu_gallery);
			builder.setTitle(R.string.choose_source);
			builder.setPositiveButton(R.string.camera,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								// 设置photoPath
								photoPath = JSYApplication.getInstance().FILE_PATH;
								// 获取文件
								File photoFile = PictureUtils
										.createImageFile(photoPath);
								if (photoFile != null) {
									photoPath = photoFile.getAbsolutePath();
									// 请iu
									PictureUtils.dispatchTakePictureIntent(
											QiuzhiPersonalCenterActivity.this,
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
		EventBusUtil.register(this);
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.user_regist_checkAccN:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void GetMyPointsDay() {
		UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
				.getAsObject("userInfo");
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId",
				String.valueOf(userInfo.getJiaoBaoHao()));
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.GET, GetMyPointsDay,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (mContext != null) {
							LogUtils.e(arg1);
							if (BaseUtils.isNetworkAvailable(mContext)) {
								ToastUtil.showMessage(mContext,
										R.string.phone_no_web);
							}
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject jsonObj = new JSONObject(arg0.result);
							String ResultCode = jsonObj.getString("ResultCode");

							if ("0".equals(ResultCode)) {
								QiuZhiPoint point = GsonUtil.GsonToObject(
										jsonObj.getString("Data"),
										QiuZhiPoint.class);
								int todayPoint = point.getPoint()
										+ point.getDelPoint();
								tv_score.setText("本月积分:"
										+ (monthPoint + todayPoint) + "\n本日积分:"
										+ todayPoint);
							}
						} catch (Exception e) {
						}
					}
				});
	}

	int monthPoint = 0;

	private void GetMyPointsMonth() {
		monthPoint = 0;
		UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
				.getAsObject("userInfo");
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId",
				String.valueOf(userInfo.getJiaoBaoHao()));
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
				GetMyPointsMonth, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.e(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject jsonObj = new JSONObject(arg0.result);
							String ResultCode = jsonObj.getString("ResultCode");

							if ("0".equals(ResultCode)) {
								QiuZhiPoint point = GsonUtil.GsonToObject(
										jsonObj.getString("Data"),
										QiuZhiPoint.class);
								monthPoint = point.getPoint();
								GetMyPointsDay();
							}
						} catch (Exception e) {
						}
					}
				});
	}

	private String photoPath;
	private Bitmap bitmap;
	private Thread thread;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			try {
				DialogUtil.getInstance().getDialog(mContext, R.string.loading);
				createThread();

			} catch (Exception e1) {
				bitmap = null;
				e1.printStackTrace();
			}

			break;
		case 2:
			try {
				// 判断是否为空
				if (data != null) {
					Uri uri1 = data.getData();
					Cursor cursor = this.getContentResolver().query(uri1, null,
							null, null, null);
					cursor.moveToFirst();
					photoPath = cursor.getString(1);
					DialogUtil.getInstance().getDialog(mContext,
							R.string.loading);
					createThread();
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
	 */
	private void createThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
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

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 789:
				DialogUtil.getInstance().cannleDialog();
				thread.interrupt();
				thread = null;
				if (file != null) {
					uploadFile(file);
					// /*}else{
					// ToastUtil.showMessage(mContext, "文件为空");*/
				}
				break;
			default:
				break;
			}
		}

	};

	private void uploadFile(File file) {
		DialogUtil.getInstance().getDialog(mContext, R.string.uploading);
		// TODO Auto-generated method stub
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

	// 上传图片

	private File file;

}
