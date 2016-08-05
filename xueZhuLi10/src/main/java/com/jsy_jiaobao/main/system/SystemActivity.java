package com.jsy_jiaobao.main.system;

import java.io.File;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SystemActivity extends BaseActivity implements PublicMethod{
	private Context mContext;
	@ViewInject(R.id.system_btn_exit) private Button btn_exit;
	@ViewInject(R.id.system_btn_loginout) private Button btn_loginout;
	@ViewInject(R.id.system_btn_upphoto) private Button btn_upphoto;
	@ViewInject(R.id.system_img_photo) private ImageView img_photo;
	
	private BitmapUtils bitmapUtils;
	private SharedPreferences sp;
	private File file ;
	private ProgressDialog dialog;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initParentView();
		
		initPassData();
		initViews();
		initDeatilsData();
		initListener();

	}
	@OnClick({R.id.system_btn_exit,R.id.system_btn_loginout
		,R.id.system_img_photo,R.id.system_btn_upphoto
		})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.system_btn_exit:
			finish();
			break;
		case R.id.system_btn_loginout:
			startActivity(new Intent(mContext,LoginActivity.class));
			finish();
			break;
		case R.id.system_img_photo:
			Intent ii = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库 
			startActivityForResult(ii, 2); 
			break;
		case R.id.system_btn_upphoto:
			if (bitmap != null) {
				dialog.show();
				upLoadImage();
			}else{
				ToastUtil.showMessage(mContext, R.string.choose_picture_please);
			}
			break;
		}
	}
	@Override
	public void initPassData() {
		
	}
	@Override
	public void initViews() {
		setContentLayout(R.layout.ui_system);
		ViewUtils.inject(this);
		mContext = this;
		dialog = BaseUtils.showDialog(mContext, R.string.changing_unit_waiting);
		dialog.setCanceledOnTouchOutside(false);
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.clearCache();
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.photo);
		bitmapUtils.configDefaultLoadingImage(R.drawable.photo);
		sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
//		sys_sp = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);
		bitmapUtils.display(img_photo, ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+ConstantUrl.photoURL+"?AccID="+sp.getString("JiaoBaoHao", ""));
	
		
	}
	@Override
	public void initDeatilsData() {
		
	}
	@Override
	public void initListener() {
		
	}
	private void initParentView() {
//		setTitle(R.string.function_system);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setTitle(getResources().getString(R.string.function_system));
//		getRadio4().setChecked(true);
	}

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			
			super.onActivityResult(requestCode, resultCode, data);  
			switch (requestCode) {
			case 1:
				if (data != null) {
					Bundle bundle = data.getExtras(); 
					if (bundle != null) {
						bitmap = (Bitmap) bundle.get("data");// 
					}
				}
				break;
			case 2:
				if (data != null) {
					Uri uri = data.getData(); 
					Cursor cursor = this.getContentResolver().query(uri, null,null, null, null); 
					cursor.moveToFirst(); 
					String imgPath = cursor.getString(1); // 图片文件路径 
					cursor.close(); 
					Options options = new BitmapFactory.Options(); 
					options.inJustDecodeBounds = false; 
					options.inSampleSize = 2; 
					bitmap = BitmapFactory.decodeFile(imgPath, options); 
				}
				break;
			default:
				break;
			}
			if (bitmap != null) {
				img_photo.setImageBitmap(bitmap);
				file = PictureUtils.saveBitmapFile(bitmap, JSYApplication.getInstance().DB_PATH+"/photo.png");
			}
		}
	} 

	//上传图片
	private void upLoadImage() {
		try {
			RequestParams params = new RequestParams();
	        params.addBodyParameter("file", file);

	        HttpUtil.getInstanceNew().send(HttpRequest.HttpMethod.POST, 
	        		ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+updatefaceimg, params, 
	        		new RequestCallBack<String>() {
	        	
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					if(mContext!=null){
						dialog.dismiss();
						if(BaseUtils.isNetworkAvailable(mContext)){
							ToastUtil.showMessage(mContext,R.string.phone_no_web);
						}else if (!isFinishing()) {
							ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_internet));
						}
					}
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					if (!isFinishing()) {
						dialog.dismiss();
						try {
							JSONObject jsonObj = new JSONObject(arg0.result);
							ToastUtil.showMessage(mContext, jsonObj.getString("ResultDesc"));
						} catch (Exception e) {
							dialog.dismiss();
							BaseUtils.shortToast(mContext, mContext.getResources().getString(R.string.error_serverconnect)+"r1002");
						} 
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
			finish();
            return true;   
		}
        return super.onKeyDown(keyCode, event);
	}
}
