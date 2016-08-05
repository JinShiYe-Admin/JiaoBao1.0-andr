package com.jsy_jiaobao.main.leave;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivityController;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator2;
import com.viewpagerindicator.view.TabView;

/**
 * 功能说明：班主任身份使用请假系统
 * 
 * @author 
 * 
 */
public class HeadTeacherActivity extends BaseActivity {
	private static String[] TITLE;
	public static String MessageCenterTitle;
	private TabPageIndicator2 indicator;
	private TabView[] titles = new TabView[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	private void initViews() {
		setContentLayout(R.layout.leave_activity_gen);
		setActionBarTitle(R.string.leave_headteacher);
		MessageCenterActivityController.getInstance().setContext(this);
		TITLE = new String[] {
				getResources().getString(R.string.leave_gen_make),
				getResources().getString(R.string.leave_query), };
		// ViewPager的adapter
		FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(
				getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.base_layout_pager);
		pager.setAdapter(adapter);

		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator = (TabPageIndicator2) findViewById(R.id.base_tab_indicator);
		indicator.setViewPager(pager);

		titles[0] = (TabView) indicator.findViewWithTag(0);
		titles[1] = (TabView) indicator.findViewWithTag(1);
	}

	class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		public TabPageIndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return HeadTeaLeaveFeagment.newInstance();// 班主任请假
			} else if (position == 1) {
				return HeadTeaQueryFragment.newInstance();// 班主任请假记录查询
			}

			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLE[position % TITLE.length];
		}

		@Override
		public int getCount() {
			return TITLE.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
