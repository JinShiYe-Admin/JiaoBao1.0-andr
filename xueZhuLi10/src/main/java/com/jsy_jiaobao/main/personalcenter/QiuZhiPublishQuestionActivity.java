package com.jsy_jiaobao.main.personalcenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.UpFiles;
import com.jsy_jiaobao.po.qiuzhi.AtMeUser;
import com.jsy_jiaobao.po.qiuzhi.CityMessage;
import com.jsy_jiaobao.po.qiuzhi.Subject;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.lidroid.xutils.http.RequestParams;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 求知发布问题
 * 
 * @author admin
 * 
 */
public class QiuZhiPublishQuestionActivity extends BaseActivity implements
		OnClickListener {
	private static Context mContext;
	private Uri photoUri;

	private IEditText edt_title;// 标题输入框
	private IEditText edt_conent;// 内容输入框

	private TextView tv_clazz;
	private IEditText edt_invite;// 邀请回答

	private Spinner sp_city;// 城市下拉框

	// 省数据
	private ArrayList<Map<String, String>> provinceData = new ArrayList<>();
	// 城市数据
	private ArrayList<Map<String, String>> cityData = new ArrayList<>();
	// 地区数据
	private ArrayList<Map<String, String>> countyData = new ArrayList<>();
	// 省Spinner的Adapter
	private SimpleAdapter provinceAdapter;
	// 城市Spinner的Adapter
	private SimpleAdapter cityAdapter;
	// 地区Spinner的Adapter
	private SimpleAdapter countyAdapter;
	// 发布问腿
	private TextView tv_publish;
	// 选项
	private CheckBox checkBox;
	private String AreaCode = "";

//	// 图片路径列表
//	private List<Object> photoPathList;
	// 图片路径
	private String photoPath;
	/** 被邀请人 */
	private int invitedAccId;
	/** 被邀请人 */
	private String username;
	private String mInvitedPerson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
		} else {
			initPassData();
		}
//		photoPathList = new ArrayList<Object>();
		initViews();
	}

	public void initPassData() {
//		Intent getPass = getIntent();
//		if (getPass != null) {
//			Bundle bundle = getPass.getExtras();
//			if (bundle != null) {
//			}
//		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
	}

	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_publish);
		mContext = this;
		QiuZhiPublishQuestionActivityController.getInstance().setContext(this);
		QiuZhiPublishQuestionActivityController.getInstance().GetProvice();
		setActionBarTitle(R.string.release_question);
		TextView tv_questionStar;
		LinearLayout layout_root;
		tv_questionStar = (TextView) findViewById(R.id.tv_questionStar);
		layout_root = (LinearLayout) findViewById(R.id.qiuzhi_publish_root);
		edt_title = (IEditText) findViewById(R.id.qiuzhi_publish_edt_title);
		edt_conent = (IEditText) findViewById(R.id.qiuzhi_publish_edt_content);
		TextView tv_takpic;// 插入图片
		tv_takpic = (TextView) findViewById(R.id.qiuzhi_publish_tv_takepic);
		tv_clazz = (TextView) findViewById(R.id.qiuzhi_publish_tv_clazz);
		edt_invite = (IEditText) findViewById(R.id.qiuzhi_publish_edt_invite);
		Spinner sp_province;// 省份下拉框
		sp_province = (Spinner) findViewById(R.id.qiuzhi_publish_sp_province);
		sp_city = (Spinner) findViewById(R.id.qiuzhi_publish_sp_city);
		Spinner sp_county;// 地区下拉框
		sp_county = (Spinner) findViewById(R.id.qiuzhi_publish_sp_county);
		tv_publish = (TextView) findViewById(R.id.qiuzhi_publish_tv_publish);
		checkBox = (CheckBox) findViewById(R.id.qiuzhi_publish_cb);
		tv_questionStar.requestFocus();
		layout_root.setOnClickListener(this);
		tv_takpic.setOnClickListener(this);
		tv_publish.setOnClickListener(this);
		tv_clazz.setOnClickListener(this);
		edt_title.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					edt_title.append(" ");
					return true;
				}
				return false;
			}
		});
		Map<String, String> map1 = new HashMap<>();
		map1.put("CityCode", "-1");
		map1.put("CnName", getResources().getString(R.string.please_choose));
		provinceData.add(map1);
		cityData.add(map1);
		countyData.add(map1);
		/**
		 * 设置监听，当触摸。。edt_content时，父ViewGroup禁止滚动
		 */
		edt_conent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.qiuzhi_publish_edt_content) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});
		// 省份迭代器
		provinceAdapter = new SimpleAdapter(this, provinceData,
				R.layout.define_simple_spinner, new String[] { "CnName" },
				new int[] { android.R.id.text1 });
		sp_province.setAdapter(provinceAdapter);
		// 城市迭代器
		cityAdapter = new SimpleAdapter(this, cityData,
				R.layout.define_simple_spinner, new String[] { "CnName" },
				new int[] { android.R.id.text1 });
		sp_city.setAdapter(cityAdapter);
		// 地区迭代器
		countyAdapter = new SimpleAdapter(this, countyData,
				R.layout.define_simple_spinner, new String[] { "CnName" },
				new int[] { android.R.id.text1 });
		sp_county.setAdapter(countyAdapter);
		// 省份下拉菜单选择监听事件
		sp_province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = provinceData.get(position);
				if ("-1".equals(map.get("CityCode"))) {
					cityData.clear();
					countyData.clear();
					Map<String, String> map1 = new HashMap<>();
					map1.put("CityCode", "-1");
					map1.put("CnName",
							getResources().getString(R.string.please_choose));
					cityData.add(map1);
					countyData.add(map1);
					cityAdapter.notifyDataSetChanged();
					countyAdapter.notifyDataSetChanged();

				} else {
					AreaCode = map.get("CityCode");
					// 请求省份数据
					QiuZhiPublishQuestionActivityController.getInstance()
							.GetCity(AreaCode);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (provinceData.size() > 0) {
					Map<String, String> map = provinceData.get(0);
					if ("-1".equals(map.get("CityCode"))) {
						Map<String, String> map1 = new HashMap<>();
						map1.put("CityCode", "-1");
						map1.put("CnName",
								getResources()
										.getString(R.string.please_choose));
						cityData.clear();
						countyData.clear();
						cityData.add(map1);
						countyData.add(map1);
						cityAdapter.notifyDataSetChanged();
						countyAdapter.notifyDataSetChanged();

					} else {
						AreaCode = map.get("CityCode");
						// 请求省份数据
						QiuZhiPublishQuestionActivityController.getInstance()
								.GetCity(AreaCode);
					}
				}
			}
		});
		// 城市下拉菜单选择事件监听
		sp_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = cityData.get(position);
				if ("-1".equals(map.get("CityCode"))) {
					countyData.clear();
					Map<String, String> map1 = new HashMap<>();
					map1.put("CityCode", "-1");
					map1.put("CnName", "请选择");
					countyData.add(map1);
					countyAdapter.notifyDataSetChanged();
				} else {
					AreaCode = map.get("CityCode");
					// 请求城市数据
					QiuZhiPublishQuestionActivityController.getInstance()
							.GetCounty(AreaCode);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (cityData.size() > 0) {
					Map<String, String> map = cityData.get(0);

					if ("-1".equals(map.get("CityCode"))) {
						Map<String, String> map1 = new HashMap<>();
						map1.put("CityCode", "-1");
						map1.put("CnName",
								getResources()
										.getString(R.string.please_choose));
						countyData.clear();
						countyData.add(map1);
						countyAdapter.notifyDataSetChanged();

					} else {
						// 获取城市数据
						QiuZhiPublishQuestionActivityController.getInstance()
								.GetCounty(map.get("CityCode"));
					}
				}
			}
		});
		// 地区下拉菜单选择监听事件
		sp_county.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = countyData.get(position);
				AreaCode = map.get("CityCode");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (countyData.size() > 0) {
					countyData.get(0);
				}
			}
		});
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	/**
	 * EventBus功能模块
	 * 
	 * @功能 获取到数据并做处理
	 * @param list list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_GetCounty:
			// 取指定省份的地市数据或取指定地市的区县数据
			@SuppressWarnings("unchecked")
			ArrayList<CityMessage> countylist = (ArrayList<CityMessage>) list
					.get(1);
			if (null != countylist && countylist.size() > 0) {
				countyData.clear();
				Map<String, String> map1 = new HashMap<>();
				map1.put("CityCode", "-1");
				map1.put("CnName",
						getResources().getString(R.string.please_choose));
				countyData.add(map1);
				for (int i = 0; i < countylist.size(); i++) {
					Map<String, String> map = new HashMap<>();
					map.put("CityCode", countylist.get(i).getCityCode());
					map.put("CnName", countylist.get(i).getCnName());
					countyData.add(map);
				}
				countyAdapter.notifyDataSetChanged();
			}
			break;
		case Constant.msgcenter_qiuzhi_GetCity:
			// 取指定省份的地市数据
			@SuppressWarnings("unchecked")
			ArrayList<CityMessage> citylist = (ArrayList<CityMessage>) list
					.get(1);
			if (null != citylist && citylist.size() > 0) {
				cityData.clear();
				Map<String, String> map1 = new HashMap<>();
				map1.put("CityCode", "-1");
				map1.put("CnName",
						getResources().getString(R.string.please_choose));
				cityData.add(map1);
				for (int i = 0; i < citylist.size(); i++) {
					Map<String, String> map = new HashMap<>();
					map.put("CityCode", citylist.get(i).getCityCode());
					map.put("CnName", citylist.get(i).getCnName());
					cityData.add(map);
				}
				cityAdapter.notifyDataSetChanged();

				Map<String, String> map = cityData.get(sp_city
						.getSelectedItemPosition());
				if (!"-1".equals(map.get("CityCode"))) {
					QiuZhiPublishQuestionActivityController.getInstance()
							.GetCounty(map.get("CityCode"));
				}
			}
			break;
		case Constant.msgcenter_qiuzhi_GetProvice:
			// 取系统中省份信息
			@SuppressWarnings("unchecked")
			ArrayList<CityMessage> provicelist = (ArrayList<CityMessage>) list
					.get(1);
			if (null != provicelist && provicelist.size() > 0) {
				provinceData.clear();
				Map<String, String> map1 = new HashMap<>();
				map1.put("CityCode", "-1");
				map1.put("CnName", "选择省");
				provinceData.add(map1);
				for (int i = 0; i < provicelist.size(); i++) {
					Map<String, String> map = new HashMap<>();
					map.put("CityCode", provicelist.get(i).getCityCode());
					map.put("CnName", provicelist.get(i).getCnName());
					provinceData.add(map);
				}
				provinceAdapter.notifyDataSetChanged();
			}
			break;
		case Constant.msgcenter_qiuzhi_AtMeForAnswer:
			// 邀请指定的用户回答问题
			String atmeResult = (String) list.get(1);
			username = (username != null ? username : mInvitedPerson);
			if (TextUtils.isEmpty(atmeResult)) {
				Toast.makeText(
						mContext,
						getResources().getString(R.string.invite) + username
								+ getResources().getString(R.string.failure),
						Toast.LENGTH_SHORT).show();
			} else if ("0".equals(atmeResult)) {
				Toast.makeText(
						mContext,
						getResources().getString(R.string.invite) + username
								+ getResources().getString(R.string.success),
						Toast.LENGTH_SHORT).show();
				edt_invite.setText("");
			} else if ("-1".equals(atmeResult)) {
				Toast.makeText(
						mContext,
						username
								+ getResources().getString(
										R.string.has_beenInvited),
						Toast.LENGTH_SHORT).show();
			} else if ("-2".equals(atmeResult)) {
				Toast.makeText(
						mContext,
						username
								+ getResources().getString(
										R.string.has_answered_thisQuestion),
						Toast.LENGTH_SHORT).show();
			}
			finish();
			break;
		case Constant.msgcenter_qiuzhi_NewQuestion:
			// 发布问题的结果
			tv_publish.setEnabled(true);
			String publishedQid;
			publishedQid = (String) list.get(1);
			if ("".equals(publishedQid)) {
				ToastUtil.showMessage(mContext,
						getResources().getString(R.string.public_syserr));
				DialogUtil.getInstance().cannleDialog();
				ToastUtil.showMessage(mContext, "发布失败");
			} else if ("0".equals(publishedQid)) {
				DialogUtil.getInstance().cannleDialog();
				ToastUtil.showMessage(mContext, "发布失败");
			} else {
				edt_title.setText("");
				edt_conent.setText("");
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				if (invitedAccId > 0) {
					QiuZhiPublishQuestionActivityController.getInstance()
							.AtMeForAnswer(invitedAccId, publishedQid);
				} else {
					DialogUtil.getInstance().cannleDialog();
					finish();
				}
			}
			break;
		case Constant.msgcenter_qiuzhi_uploadSectionImg:
			// 上传插入图片的结果
			DialogUtil.getInstance().cannleDialog();
			UpFiles upFile = (UpFiles) list.get(1);
			if (upFile == null) {
				return;
			}
			if (bitmap != null) {

				if (bitmap.getWidth() > 120) {
					bitmap = ResizeBitmap(bitmap, 120);
				}
				String img = "<img src=\"" + upFile.getUrl() + "\"/><br>";
//				Map<String, String> map = new HashMap<>();
//				map.put(upFile.getUrl(), photoPath);
				int length = img.length() + edt_conent.getTextString().length();
				if (length > 4000) {
					ToastUtil.showMessage(mContext, "字数超出限制,请删除"
							+ (length - 4000) + "字");
					// 回收图片
					bitmap.recycle();
				} else {
					// 在edt_content中插入图片Span
					BaseUtils.insetImage(edt_conent, img, bitmap);
				}
			}
			break;
		case Constant.msgcenter_qiuzhi_GetAtMeUsers:
			// 邀请人回答时，获取回答该话题问题最多的用户列表
			@SuppressWarnings("unchecked")
			ArrayList<AtMeUser> users = (ArrayList<AtMeUser>) list.get(1);
			if (users != null && users.size() > 0) {
				invitedAccId = users.get(0).getJiaoBaoHao();
				username = users.get(0).getNickName();
				String str_title = edt_title.getTextString();
				String str_describe = edt_conent.getTextString();
				QiuZhiPublishQuestionActivityController.getInstance()
						.NewQuestion(CategoryId, str_title, str_describe,
								str_title, AreaCode,
								checkBox.isChecked() ? 1 : 0);
			} else {
				tv_publish.setEnabled(true);
				ToastUtil.showMessage(mContext, R.string.no_this_user);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 自定义方法
	 * 
	 * @功能 判断图片数量 如果小于20 返回真 否则返回false
	 * @return f
	 */
	private boolean CheckPicEnough() {
		String xml = edt_conent.getText().toString();
		int size = StringUtils.getImgStr(xml).size();
		return size <= 20 ;
	}

	/**
	 * view监听接口的重写
	 * 
	 * @功能 各个view的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qiuzhi_publish_root:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		case R.id.qiuzhi_publish_tv_takepic:
			// 获取图片
//			List<String> list = StringUtils.getImgStr1(edt_conent.getText()
//					.toString());
			if (CheckPicEnough()) {
				if (edt_conent.getTextString().length() >= 4000) {
					ToastUtil.showMessage(mContext, "已达四千字上限,请删除多余内容后上传");
					return;
				}
				// 创建对话框
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setIcon(android.R.drawable.ic_menu_gallery);
				builder.setTitle(getResources().getString(
						R.string.choose_source));
				// 相机
				builder.setPositiveButton(R.string.camera,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									photoPath = JSYApplication.getInstance().FILE_PATH;
									File photoFile = PictureUtils
											.createImageFile(photoPath);
									if (photoFile != null) {
										photoPath = photoFile.getAbsolutePath();
										PictureUtils
												.dispatchTakePictureIntent(
														QiuZhiPublishQuestionActivity.this,
														photoFile, 1);
									}

								} catch (Exception e) {
									ToastUtil.showMessage(mContext,
											R.string.open_camera_abnormal);
									e.printStackTrace();
								}
							}
						});
				// 相册
				builder.setNegativeButton(R.string.album,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent ii = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
								startActivityForResult(ii, 2);
							}
						});
				builder.create().show();
			} else {
				ToastUtil.showMessage(mContext, R.string.noMoreThan_20ToUpLoad);
			}
			break;

		case R.id.qiuzhi_publish_tv_publish:
			// 发布问题
			UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
					.getAsObject("userInfo");
			if (userInfo.isIsKnlFeezeUser()) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_user));
				return;
			}
			if (userInfo.getDUnitId() == 0) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nounit));
				return;
			}
			if (userInfo.getNickName() == null
					|| userInfo.getNickName().equals("")) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nonick));
				return;
			}
			if (StringUtils.getImgStr(edt_conent.getText().toString()).size() > 20) {
				ToastUtil.showMessage(mContext, R.string.noMoreThan_20ToUpLoad);
				return;
			}
			String str_title = edt_title.getTextString();
			String str_describe = edt_conent.getTextString();
			String str_clazz = tv_clazz.getText().toString();
			if (TextUtils.isEmpty(str_title)) {
				ToastUtil.showMessage(mContext, "请填写标题");
			} else if (str_title.length() < 6 || str_title.length() > 100) {
				ToastUtil.showMessage(mContext, "请确认标题长度不小于6,不大于100");
			} else if (TextUtils.isEmpty(str_clazz)) {
				ToastUtil.showMessage(mContext, "请选择分类");
			} else {
				tv_publish.setEnabled(false);
				String str_in = edt_invite.getTextString();
				if (!TextUtils.isEmpty(str_describe)) {
					str_describe = str_describe.replaceAll("\\\n", "<br>")
							.replaceAll("<img src", "<br><img src")
							.replaceAll("<br><br>", "<br>");
				}
				if (TextUtils.isEmpty(str_in)) {
					invitedAccId = 0;
					username = "";
					QiuZhiPublishQuestionActivityController.getInstance()
							.NewQuestion(CategoryId, str_title, str_describe,
									str_title, AreaCode,
									checkBox.isChecked() ? 1 : 0);
				} else {
					mInvitedPerson = str_in;
					QiuZhiPublishQuestionActivityController.getInstance()
							.GetAtMeUsers(str_in, CategoryId);
				}
			}
			break;
		case R.id.qiuzhi_publish_tv_clazz:
			// 选择话题
			Intent ii = new Intent(mContext, QiuZhiChoseClazzActivity.class);
			startActivityForResult(ii, 3);
			break;
		default:
			break;
		}
	}

	Bitmap bitmap = null;
	protected static File file;
	protected static Thread thread;
	private final static String TAG = "QiuZhiPublishQuestionActivity";

	/**
	 * 启动其他组建后返回数据
	 * 
	 * @功能 返回其他组建 的数据 并作处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			// 从相机返回的数据
			try {
				DialogUtil.getInstance().getDialog(mContext, R.string.loading);
				// 创建线程 获取图片
				createThread();
			} catch (Exception e1) {
				bitmap = null;
				e1.printStackTrace();
			}

			break;
		case 2:
			try {
				if (data != null) {
					Uri uri1 = data.getData();
					Cursor cursor = this.getContentResolver().query(uri1, null,
							null, null, null);
					if(cursor!=null&&cursor.moveToFirst()){
						photoPath = cursor.getString(1);
						DialogUtil.getInstance().getDialog(mContext,
								R.string.loading);
						// 创建线程 获取图片
						createThread();
						cursor.close();
					}
				}
			} catch (Exception e) {
				bitmap = null;
				e.printStackTrace();
			}

			break;
		case 3:
			try {
				Bundle b = data.getExtras(); // data为B中回传的Intent
				Subject subject = (Subject) b.getSerializable("Subject");
				if(subject!=null){
					String str_name = subject.getSubject().trim();
					CategoryId = subject.getTabID();
					tv_clazz.setText(str_name);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 创建线程
	 * 
	 * @功能 获取图片
	 */
	private void createThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					bitmap = PictureUtils.getbitmapFromURL(photoPath);
					if (bitmap == null || bitmap.getWidth() == 0) {
						DialogUtil.getInstance().cannleDialog();
						// 删除图片
						PictureUtils.DeleteImage(mContext, photoPath);
						file = null;
					} else {
						// 保存图片文件
						file = PictureUtils.saveBitmapFile(bitmap, photoPath);
					}
					handler.sendEmptyMessage(789);
				}
			});
		}
		thread.start();
	}

	IncomingHandler handler = new IncomingHandler(this);

	/**
	 *
	 * @param file file
     */
	private static void uploadFile(File file) {
		DialogUtil.getInstance().getDialog(mContext, R.string.uploading);
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", file);
		QiuZhiPublishQuestionActivityController.getInstance().uploadSectionImg(
				params);
	}

	private int CategoryId;

	/**
	 *
	 * @param bitmap bit
	 * @param newWidth w
     * @return bt
     */

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		Bitmap resizedBitmap = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float temp = ((float) height) / ((float) width);
			int newHeight = (int) ((newWidth) * temp);
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			// resize the bit map
			matrix.postScale(scaleWidth, scaleHeight);
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);
			bitmap.recycle();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resizedBitmap;
	}

	/**
	 * 重写系统返回键
	 * 
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 重写生命周期事件
	 * 
	 * @功能 当当前activity要销毁时，回收bitmap
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != bitmap) {
			bitmap.recycle();
		}
	}

	/**
	 * Handler自定义类 当前Activity的弱引用
	 * 
	 * @author admin
	 * 
	 */
	static class IncomingHandler extends Handler {
		private final WeakReference<QiuZhiPublishQuestionActivity> mService;

		IncomingHandler(QiuZhiPublishQuestionActivity service) {
			mService = new WeakReference<>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			QiuZhiPublishQuestionActivity service = mService.get();
			if (service != null) {
				switch (msg.what) {
				case 789:
					DialogUtil.getInstance().cannleDialog();
					thread.interrupt();
					thread = null;
					if (file != null) {
						// 上传图片
						uploadFile(file);
					}
					break;

				default:
					break;
				}
			}
		}
	}

}
