package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Coder;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Photo;
import com.lidroid.xutils.BitmapUtils;

/**
 * 单位空间
 */
public class UnitSpaceActivity extends BaseActivity {
	public static String ActivityTag = "UnitSpaceActivity";
	private GridView gridView;
	private ViewPager mViewPager;
	private LinearLayout slider_viewGroup;
	private Context mContext;
	private SimpleAdapter adapter;
	private String[] items = { "单位简介", "展示文章", "分享文章", "单位成员", "下级单位", "单位相册" };
	private int[] drawables = { R.drawable.icon_unit_info,
			R.drawable.icon_unit_show, R.drawable.icon_unit_share,
			R.drawable.icon_unit_member, R.drawable.icon_unit_juniorunit,
			R.drawable.icon_unit_gallery };
	private int UnitID;
	private int UnitType = 1;
	private String UnitName;
	private int IsMyUnit;
	private BitmapUtils bitmap;
	private SamplePagerAdapter pagerAdapter;
	private ArrayList<String> photoUrlList = new ArrayList<String>();
	private TextView[] textViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			UnitID = savedInstanceState.getInt("UnitID");
			UnitType = savedInstanceState.getInt("UnitType");
			IsMyUnit = savedInstanceState.getInt("IsMyUnit");
			UnitName = savedInstanceState.getString("UnitName");
			if (TextUtils.isEmpty(UnitName)) {
				UnitName = getResources().getString(R.string.unknown_unit);
			}
			if (UnitType == 3) {
				if (UnitID > 0) {
					UnitID = UnitID * -1;
				}
			}
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
				UnitID = bundle.getInt("UnitID");
				UnitType = bundle.getInt("UnitType");
				IsMyUnit = bundle.getInt("IsMyUnit");
				UnitName = bundle.getString("UnitName");
				if (TextUtils.isEmpty(UnitName)) {
					UnitName = getResources().getString(R.string.unknown_unit);
				}
				if (UnitType == 3) {
					if (UnitID > 0) {
						UnitID = UnitID * -1;
					}
				}
			}
		}
	}

	/**
	 * 保存可能意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			outState.putString("UnitName", UnitName);
			outState.putInt("UnitID", UnitID);
			outState.putInt("UnitType", UnitType);
			outState.putInt("IsMyUnit", IsMyUnit);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentView(R.layout.activity_unitspace);
		gridView = (GridView) findViewById(R.id.unitspace_gridview);
		// layout_top = (FrameLayout) findViewById(R.id.unitspace_layout_top);
		mViewPager = (ViewPager) findViewById(R.id.unitspace_slider_viewpager);
		slider_viewGroup = (LinearLayout) findViewById(R.id.unitspace_slider_viewGroup);
		mContext = this;
		UnitSpaceActivityController.getInstance().setContext(this);
		UnitSpaceActivityController.getInstance().GetUnitNewPhoto(
				String.valueOf(UnitID));
		mCache = ACache.get(getApplicationContext());
		setActionBarTitle(UnitName + "单位空间");
		bitmap = new BitmapUtils(mContext);
		bitmap.configDefaultLoadFailedImage(R.drawable.rc_image_download_failure);
		bitmap.configDefaultLoadingImage(R.drawable.img_downloading);
		bitmap.configDiskCacheEnabled(true);
		bitmap.configMemoryCacheEnabled(true);
		@SuppressWarnings("deprecation")
		int height = (int) (getWindowManager().getDefaultDisplay().getHeight() / 3);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height);
		lp.gravity = Gravity.TOP;
		mViewPager.setLayoutParams(lp);
		mViewPager.setOffscreenPageLimit(3);
		pagerAdapter = new SamplePagerAdapter();
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < textViews.length; i++) {
					textViews[i].setBackgroundResource(R.drawable.radio);
				}
				textViews[arg0].setBackgroundResource(R.drawable.radio_sel);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < items.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("item_image", drawables[i]);
			data.add(map);
		}

		adapter = new SimpleAdapter(mContext, data,
				R.layout.appcenter_gridview_item,
				new String[] { "item_image" }, new int[] { R.id.item_image });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				switch (position) {
				// 单位简介
				case 0:
					intent.setClass(mContext, UnitSpaceBriefInfo.class);
					bundle.putInt("UnitID", UnitID);
					bundle.putInt("UnitType", UnitType);
					bundle.putString("UnitName", UnitName);
					bundle.putString("ClickName", items[position]);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				// 展示文章
				case 1:
					intent.setClass(mContext, NoticeArtListActivity.class);
					bundle.putInt("UnitID", UnitID);
					bundle.putInt("UnitType", UnitType);
					bundle.putString("UnitName", UnitName);
					bundle.putString("SectionFlag", "-2");
					if (IsMyUnit == 1) {
						bundle.putBoolean("myunit", true);
					} else {
						bundle.putBoolean("myunit", false);
					}
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				// 分享文章
				case 2:
					intent.setClass(mContext, NoticeArtListActivity.class);

					bundle.putInt("UnitID", UnitID);
					bundle.putInt("UnitType", UnitType);
					bundle.putString("UnitName", UnitName);
					bundle.putString("SectionFlag", "-1");
					if (IsMyUnit == 1) {
						bundle.putBoolean("myunit", true);
					} else {
						bundle.putBoolean("myunit", false);
					}
					intent.putExtras(bundle);
					startActivity(intent);

					break;
				// 单位成员
				case 3:
					if (UnitType < 3) {
						intent.setClass(mContext, UnitSpaceExpActivity.class);
						bundle.putInt("UnitID", UnitID);
						bundle.putInt("UnitType", UnitType);
						bundle.putString("UnitName", UnitName);
						bundle.putString("ClickName", items[position]);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						ToastUtil.showMessage(mContext,
								R.string.class_notContain_unitMember);
					}
					break;
				// 下级单位
				case 4:
					if (UnitType < 3) {
						intent.setClass(mContext,
								NoticeJuniorListActivity.class);
						bundle.putInt("UnitID", UnitID);
						bundle.putInt("UnitType", UnitType);
						bundle.putString("UnitName", UnitName);
						bundle.putString("ClickName", items[position]);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						ToastUtil.showMessage(mContext, R.string.noLower_unit);
					}
					break;
				// 单位相册
				case 5:
					intent.setClass(mContext, UnitSpacePhotoGroupActivity.class);
					bundle.putString("UnitID", "" + UnitID);
					bundle.putInt("UnitType", UnitType);
					bundle.putString("UnitName", UnitName);
					bundle.putString("ClickName", items[position]);
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	private class SamplePagerAdapter extends PagerAdapter {
		private ArrayList<String> photoUrlList;

		public void setData(ArrayList<String> photoUrlList) {
			this.photoUrlList = photoUrlList;
		}

		@Override
		public int getCount() {
			if (photoUrlList != null) {
				return photoUrlList.size();
			}
			return 0;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			ImageView photoView = new ImageView(container.getContext());
			photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			bitmap.display(photoView, photoUrlList.get(position));
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (null != null) {
				container.removeView((View) object);
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public void onResume() {
		UnitSpaceActivityController.getInstance().setContext(this);
		EventBusUtil.register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_unitspace_GetUnitNewPhoto:
			@SuppressWarnings("unchecked")
			ArrayList<Photo> getPhotoList111 = (ArrayList<Photo>) list.get(1);
			if (null == getPhotoList111) {
				getPhotoList111 = new ArrayList<Photo>();
				ToastUtil.showMessage(mContext, R.string.no_photo);
			}
			for (int i = 0; i < getPhotoList111.size(); i++) {
				String[] urls = getPhotoList111.get(i).getSMPhotoPath()
						.split("\\/");
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
			}
			if (photoUrlList.size() == 0) {
				photoUrlList.add("assets/drawable/pic_no.png");
			}
			pagerAdapter.setData(photoUrlList);
			pagerAdapter.notifyDataSetChanged();

			FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(15, 15);
			l.gravity = Gravity.CENTER;
			textViews = new TextView[photoUrlList.size()];
			for (int j = 0; j < photoUrlList.size(); j++) {
				TextView textView = new TextView(mContext);
				textView.setLayoutParams(l);
				textView.setPadding(0, 0, 20, 0);
				textViews[j] = textView;
				if (j == 0) {
					// 默认进入程序后第一张图片被选中;
					textViews[j].setBackgroundResource(R.drawable.radio_sel);
				} else {
					textViews[j].setBackgroundResource(R.drawable.radio);
				}
				slider_viewGroup.addView(textViews[j]);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
