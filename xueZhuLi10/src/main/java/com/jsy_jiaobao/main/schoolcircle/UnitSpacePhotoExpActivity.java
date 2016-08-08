package com.jsy_jiaobao.main.schoolcircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Coder;
import com.jsy_jiaobao.customview.MyViewPager;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * 相册图片列表界面
 */
public class UnitSpacePhotoExpActivity extends BaseActivity {

	private Context mContext;
	private ArrayList<Photo> getPgroupList;
	private ArrayList<String> photoUrlList = new ArrayList<>();
	private int position;
	private String nameStr;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			try {
				position = savedInstanceState.getInt("position");
				getPgroupList = (ArrayList<Photo>) savedInstanceState
						.getSerializable("photoList");
				nameStr = savedInstanceState.getString("NameStr");
				for (int i = 0; i < getPgroupList.size(); i++) {
					try {
						String[] urls = getPgroupList.get(i).getBIGPhotoPath()
								.split("/");
						String[] names = urls[urls.length - 1].split("\\.");
						String formt = names[names.length - 1];
						String str = "";
						for (int i1 = 0; i1 < names.length - 1; i1++) {
							str = str + names[i1] + ".";
						}
						String url = "";// http://www.jb.edu8800.com/JBApp2/UploadPhotoOfUnit/20141125/5150059/20141125091852abe0_2013-03-03
										// 12.49.44.jpg
						for (int i1 = 0; i1 < urls.length - 1; i1++) {
							url = url + urls[i1] + "/";
						}
						String n = Coder.encodeURL(str).replace("+", "%20");
						url = url + "" + n + formt;
						photoUrlList.add(url);
					} catch (Exception e) {
						photoUrlList
								.add(getPgroupList.get(i).getBIGPhotoPath());
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			initPass();
		}
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		initViews();
	}

	@SuppressWarnings("unchecked")
	private void initPass() {

		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				try {
					position = bundle.getInt("position");
					getPgroupList = (ArrayList<Photo>) bundle
							.getSerializable("photoList");
					nameStr = bundle.getString("NameStr");
					for (int i = 0; i < getPgroupList.size(); i++) {
						try {
							String[] urls = getPgroupList.get(i)
									.getBIGPhotoPath().split("\\/");
							String[] names = urls[urls.length - 1].split("\\.");
							String formt = names[names.length - 1];
							String str = "";
							for (int i1 = 0; i1 < names.length - 1; i1++) {
								str = str + names[i1] + ".";
							}
							String url = "";// http://www.jb.edu8800.com/JBApp2/UploadPhotoOfUnit/20141125/5150059/20141125091852abe0_2013-03-03
											// 12.49.44.jpg
							for (int i1 = 0; i1 < urls.length - 1; i1++) {
								url = url + urls[i1] + "/";
							}
							String n = Coder.encodeURL(str).replace("+", "%20");
							url = url + "" + n + formt;
							photoUrlList.add(url);
						} catch (Exception e) {
							photoUrlList.add(getPgroupList.get(i)
									.getBIGPhotoPath());
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			outState.putInt("position", position);
			outState.putSerializable("photoList", getPgroupList);
			outState.putString("NameStr", nameStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_unitspace_photo);
		LinearLayout layout_body = (LinearLayout) findViewById(R.id.unitspace_layout_body);
		mContext = this;
		mCache = ACache.get(getApplicationContext(), "chat");
		UnitSpaceExpActivityController.getInstance().setContext(this);
		setActionBarTitle(nameStr);
		MyViewPager mViewPager = new MyViewPager(mContext);
		ImageAdapter imageAdapter = new ImageAdapter(mContext);
		imageAdapter.setIMAGE_URLS(photoUrlList);
		mViewPager.setAdapter(imageAdapter);
		layout_body.addView(mViewPager);
		mViewPager.setCurrentItem(position);
	}

	// 图片的Adapter
	private static class ImageAdapter extends PagerAdapter {

		private ArrayList<String> IMAGE_URLS;
		private LayoutInflater inflater;
		private DisplayImageOptions options;

		public void setIMAGE_URLS(ArrayList<String> iMAGE_URLS) {
			IMAGE_URLS = iMAGE_URLS;
		}

		ImageAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.rc_image_download_failure)
					.showImageOnFail(R.drawable.rc_image_download_failure)
					.resetViewBeforeLoading(true).cacheOnDisk(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true)
					.displayer(new FadeInBitmapDisplayer(300)).build();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return IMAGE_URLS == null ? 0 : IMAGE_URLS.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.imageView);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			ImageLoader.getInstance().displayImage(IMAGE_URLS.get(position),
					imageView, options, new SimpleImageLoadingListener() {
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
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 系统返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}