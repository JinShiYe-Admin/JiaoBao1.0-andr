package com.jsy_jiaobao.main.schoolcircle;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 单张照片展示Activity
 * 
 * @author admin
 * 
 */
public class SinglePictureDisplayActivity extends BaseActivity {

	private ImageView imageView;
	private DisplayImageOptions options;
	private String photoPath;
	private ProgressBar spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			photoPath = (String) getIntent().getStringExtra("PhotoPath");
		} else {
			photoPath = (String) savedInstanceState.getString("PhotoPath");
		}
		setImageLoader();
		initViews();
	}

	/**
	 * 保存可能意外销毁的数据
	 * 
	 * @功能 保存照片路径
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("PhotoPath", photoPath);
		super.onSaveInstanceState(outState);
	}

	private void setImageLoader() {
		// TODO Auto-generated method stub
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.rc_image_download_failure)
				.showImageOnFail(R.drawable.rc_image_download_failure)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		setContentLayout(R.layout.item_pager_image);
		imageView = (ImageView) findViewById(R.id.imageView);
		spinner = (ProgressBar) findViewById(R.id.loading);
		photoPath = "file://" + photoPath;
		ImageLoader.getInstance().displayImage(photoPath, imageView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						spinner.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
						}
						Toast.makeText(view.getContext(), message,
								Toast.LENGTH_SHORT).show();

						spinner.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						spinner.setVisibility(View.GONE);
					}
				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 系统返回按键
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
