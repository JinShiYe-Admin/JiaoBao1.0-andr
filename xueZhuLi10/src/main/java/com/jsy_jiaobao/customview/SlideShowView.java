package com.jsy_jiaobao.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 */

public class SlideShowView extends FrameLayout {

	// 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
	private BitmapUtils bitmapUtils;
	// 轮播图图片数量
	private static int IMAGE_COUNT = 5;
	// 自动轮播的时间间隔
	private final static int TIME_INTERVAL = 5;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = true;

	// 自定义轮播图的资源
	private String[] imageUrls;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList = new ArrayList<View>();

	private int position;

	private Boolean autoPlay = true;

	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;
	private SlideShowItemClick slideShowItemClick;

	// Handler
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
	}

	public void setPosition(int Position) {
		this.position = Position;
	}

	public void setImageUrls(List<String> imageList) {
		IMAGE_COUNT = imageList.size();
		this.imageUrls = new String[imageList.size()];
		for (int i = 0; i < imageList.size(); i++) {
			imageUrls[i] = imageList.get(i);
		}
		initData();
	}

	/**
	 * 开始轮播图切换
	 */
	public void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		imageViewsList = new ArrayList<>();
		dotViewsList = new ArrayList<>();
		initUI(context);
	}

	/**
	 * 初始化views等ui
	 */
	private void initUI(Context context) {
		if (imageUrls == null || imageUrls.length == 0)
			return;

		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);
		// 热点个数与图片特殊相等
		for (int i = 0; i < imageUrls.length; i++) {
			final ImageView view = new ImageView(context);
			view.setTag(imageUrls[i]);
			// if (i == 0)//给一个默认图
			// view.setBackgroundResource(R.drawable.default_page);
			view.setScaleType(ScaleType.FIT_XY);
			final int finalI = i;
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (slideShowItemClick != null)
						slideShowItemClick.onImageClick(finalI, view);
				}
			});
			imageViewsList.add(view);
		}
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setFocusable(true);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	public void setCurrentItem(int j) {
		if (dotViewsList.size() > 0) {
			currentItem = j;
			viewPager.setCurrentItem(currentItem);
		}
	}

	/**
	 * 填充ViewPager的页面适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView imageView = imageViewsList.get(position);
			bitmapUtils.display(imageView, imageView.getTag() + "");
			((ViewPager) container).addView(imageViewsList.get(position));
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int pos) {
			currentItem = pos;
		}

	}

	/**
	 * 执行轮播图切换任务
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	public void setOnItemClick(SlideShowItemClick slideShowItemClick) {
		this.slideShowItemClick = slideShowItemClick;
	}

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public interface SlideShowItemClick {
		void onImageClick(int postion, View imageView);
	}

}