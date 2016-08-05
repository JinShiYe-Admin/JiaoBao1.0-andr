package com.jsy_jiaobao.main.appcenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoGroupActivity;
import com.jsy_jiaobao.po.app.gallery.Gallery;
import com.jsy_jiaobao.po.app.gallery.GetGallery;
import com.jsy_jiaobao.po.personal.UnitPGroup;
import com.lidroid.xutils.http.RequestParams;

/**
 * 上传相片界面
 * 
 * @author admin
 * 
 */
public class GalleryActivity extends BaseActivity implements PublicMethod,
		OnClickListener {

	private Button btn_camera;
	private Button btn_gallery;
	private GridView gridView;
	private Spinner sp_chose;
	private ArrayList<File> fileList = new ArrayList<File>();
	private ArrayList<String> fileNameList = new ArrayList<String>();
	private GalleryGridViewAdapter adapter;
	private String GroupID;
	private SimpleAdapter spAdapter;
	private Context mContext;
	private String UnitID;
	private int UnitType;
	private ArrayList<UnitPGroup> getPgroupList = new ArrayList<UnitPGroup>();
	private boolean create = false;
	private Uri photoUri;
	private String photoPath;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
			UnitType = savedInstanceState.getInt("UnitType");
			UnitID = savedInstanceState.getString("UnitID");
			getPgroupList = (ArrayList<UnitPGroup>) savedInstanceState
					.getSerializable("groupList");
		} else {
			initPassData();
		}
		initViews();
		initDeatilsData();
		initListener();
	}

	/**
	 * @功能 获取Intent携带的数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				UnitType = bundle.getInt("UnitType");
				UnitID = bundle.getString("UnitID");
				getPgroupList = (ArrayList<UnitPGroup>) bundle
						.getSerializable("groupList");
			}
		}
	}

	/**
	 * @功能 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("UnitID", UnitID);
		outState.putInt("UnitType", UnitType);
		outState.putSerializable("groupList", getPgroupList);
		outState.putParcelable("photoUri", photoUri);
	}

	/**
	 * @功能 初始化界面
	 */
	@Override
	public void initViews() {
		setContentLayout(R.layout.ui_gallery);
		findViews();
		mContext = this;
		getSupportActionBar().show();
		setActionBarTitle("上传照片");
		GalleryActivityController.getInstance().setContext(this);
		adapter = new GalleryGridViewAdapter(this, fileList);
		gridView.setAdapter(adapter);

	}

	/**
	 * view的赋值 和点击事件注册
	 */
	private void findViews() {
		// TODO Auto-generated method stub
		btn_camera = (Button) findViewById(R.id.gallery_btn_camera);
		btn_gallery = (Button) findViewById(R.id.gallery_btn_gallery);
		gridView = (GridView) findViewById(R.id.gallery_gridview);
		sp_chose = (Spinner) findViewById(R.id.gallery_sp_chose);
		btn_camera.setOnClickListener(this);
		btn_gallery.setOnClickListener(this);
	}

	/**
	 * @功能 加载数据
	 */
	@Override
	public void initDeatilsData() {
		if (UnitType == 9) {
			GalleryActivityController.getInstance().GetPhotoList();
		} else {
			if (getPgroupList.size() > 0) {
				spdata = new ArrayList<Map<String, String>>();
				for (UnitPGroup gallery : getPgroupList) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("ID", String.valueOf(gallery.getTabID()));
					map.put("GroupName", gallery.getNameStr());
					spdata.add(map);
				}
				GroupID = spdata.get(0).get("ID");
				spAdapter = new SimpleAdapter(this, spdata,
						android.R.layout.simple_list_item_1,
						new String[] { "GroupName" },
						new int[] { android.R.id.text1 });
				sp_chose.setAdapter(spAdapter);
			}
		}
	}

	/**
	 * 下拉菜单的选择事件监听
	 */
	@Override
	public void initListener() {
		sp_chose.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				GroupID = spdata.get(position).get("ID");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 界面按钮的监听事件
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.gallery_btn_camera:
			// 相机
			try {
				try {
					photoPath = JSYApplication.getInstance().FILE_PATH;
					File photoFile = PictureUtils.createImageFile(photoPath);
					if (photoFile != null) {
						photoPath = photoFile.getAbsolutePath();
						PictureUtils.dispatchTakePictureIntent(
								GalleryActivity.this, photoFile, 1);
					}

				} catch (Exception e) {
					ToastUtil.showMessage(mContext,
							R.string.open_camera_abnormal);
					e.printStackTrace();
				}
			} catch (Exception e) {
				ToastUtil.showMessage(mContext, R.string.open_camera_abnormal);
			}
			break;
		case R.id.gallery_btn_gallery:
			// 相册
			Intent ii = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			startActivityForResult(ii, 2);
			break;

		default:
			break;
		}
	}

	private String fileName;
	private File file;
	private Thread thread;
	private Bitmap bitmap;

	/**
	 * 上个组件返回数据的处理
	 * 
	 * @功能 相机 相册 返回数据的处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:// 相机
			try {
				if (resultCode == Activity.RESULT_OK) {
					DialogUtil.getInstance().getDialog(mContext,
							R.string.loading);
					createThread();
				} else {
					PictureUtils.DeleteImage(mContext, photoPath);
				}
			} catch (Exception e) {
				bitmap = null;
				e.printStackTrace();
				PictureUtils.DeleteImage(mContext, photoPath);
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
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 创建线程
	 * 
	 * @功能 获取照片
	 */
	private void createThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// 照片
					bitmap = PictureUtils.getbitmapFromURL(photoPath);
					if (bitmap == null || bitmap.getWidth() == 0) {
						DialogUtil.getInstance().cannleDialog();
						// 不存在 删除图片
						PictureUtils.DeleteImage(mContext, photoPath);
						file = null;
					} else {
						file = PictureUtils.saveBitmapFile(bitmap, photoPath);
					}
					handler.sendEmptyMessage(189);
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
			case 189:
				thread.interrupt();
				thread = null;
				DialogUtil.getInstance().cannleDialog();
				if (file != null) {
					String[] paths = photoPath.split("/");
					fileName = paths[paths.length - 1];
					fileNameList.add(fileName);
					fileList.add(file);
					adapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		}

	};

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

	private List<Map<String, String>> spdata;
	protected int i;

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
		case Constant.appcenter_gallery_GetPhotoList:// 本单位文章详情
			GetGallery getArthInfo = (GetGallery) list.get(1);
			if (getArthInfo.getList().size() > 0) {
				spdata = new ArrayList<Map<String, String>>();

				for (Gallery gallery : getArthInfo.getList()) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("ID", gallery.getID());
					map.put("GroupName", gallery.getGroupName());
					spdata.add(map);
				}
				GroupID = spdata.get(0).get("ID");
				spAdapter = new SimpleAdapter(this, spdata,
						android.R.layout.simple_list_item_1,
						new String[] { "GroupName" },
						new int[] { android.R.id.text1 });
				sp_chose.setAdapter(spAdapter);
			}
			break;
		case Constant.appcenter_gallery_UpLoadPhoto:// 上传照片
			create = true;
			ToastUtil.showMessage(mContext, R.string.upload_success);
			File tagBmp = (File) list.get(1);
			if (tagBmp == fileList.get(0)) {
				fileNameList.remove(0);
				fileList.remove(0);
			}
			adapter.notifyDataSetChanged();
			uploadFile(0);
			break;
		case Constant.appcenter_gallery_UpLoadPhotofailed:// 上传照片失败
			ToastUtil.showMessage(mContext, R.string.update_failed);
			DialogUtil.getInstance().cannleDialog();
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * ActionBar的menu
	 * 
	 * @功能 上传照片
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu sub_menu = menu.addSubMenu(R.string.commit).setIcon(
				R.drawable.btn_updata);
		sub_menu.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub_menu.getItem().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						if (!TextUtils.isEmpty(GroupID) && fileList.size() > 0) {
							DialogUtil.getInstance().getDialog(mContext,
									R.string.please_waiting);
							uploadFile(0);
						} else {
							DialogUtil.getInstance().cannleDialog();
							ToastUtil.showMessage(mContext,
									R.string.please_choose_photo);
						}
						return false;
					}
				});
		return true;
	}

	/**
	 * 上传图片文件
	 * 
	 * @param i
	 */
	private void uploadFile(int i) {
		if (fileList.size() > 0) {
			RequestParams params = new RequestParams();
			if (UnitType == 9) {
				params.addBodyParameter("JiaoBaoHao",
						BaseActivity.sp.getString("JiaoBaoHao", ""));
				params.addBodyParameter("FileName", fileNameList.get(i));
				params.addBodyParameter("FileInfo", fileList.get(i));
				params.addBodyParameter("PhotoDescribe", fileNameList.get(i));
				params.addBodyParameter("GroupID", GroupID);
				GalleryActivityController.getInstance().uploadSectionImg(
						fileList.get(i), params);
			} else {
				params.addBodyParameter("GroupID", GroupID);
				params.addBodyParameter("UnitID", UnitID);
				params.addBodyParameter("file", fileList.get(i));
				params.addBodyParameter("CreateBy",
						BaseActivity.sp.getString("JiaoBaoHao", ""));
				GalleryActivityController.getInstance().UpLoadPhotoUnit(
						fileList.get(i), params);
			}
		} else {
			DialogUtil.getInstance().cannleDialog();
		}
	}

	/**
	 * 系统返回键
	 * 
	 * @功能 设置返回数据 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bundle args = new Bundle();
			Intent result = new Intent();
			args.putBoolean("create", create);
			result.putExtras(args);
			setResult(UnitType, result);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 按键选择事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Bundle args = new Bundle();
			Intent result = new Intent(mContext,
					UnitSpacePhotoGroupActivity.class);
			args.putBoolean("create", create);
			result.putExtras(args);
			setResult(UnitType, result);
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * ActioBar的返回键
	 * 
	 * @功能 记录返回数据 结束当前Activity
	 */
	@Override
	public void setResultForLastActivity() {
		// TODO Auto-generated method stub
		Bundle args = new Bundle();
		Intent result = new Intent(mContext, UnitSpacePhotoGroupActivity.class);
		args.putBoolean("create", create);
		result.putExtras(args);
		setResult(UnitType, result);
		super.setResultForLastActivity();
	}

}
