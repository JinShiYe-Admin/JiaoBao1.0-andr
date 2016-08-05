package com.jsy_jiaobao.main.studentrecord;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
	private String[] TITLE;
	final private static int NewUpFragmentP = 0;
	final private static int SchoolNoticeP = 1;
	final private static int ClassNoticeP = 2;
	final private static int DailyExpressionP = 3;
	final private static int QipingRecordP = 4;
	final private static int TeacherWordsP = 5;
	final private static int GenWordsP = 6;
	public TabPageIndicatorAdapter(FragmentManager fm,String[] TITLE) {
		super(fm);
		this.TITLE = TITLE;
	}

	@Override
	public Fragment getItem(int position) {
		// 新建一个Fragment来展示ViewPager item的内容，并传递参数
		switch (position) {
		case NewUpFragmentP:return NewUpFragment.newInstance();
		case SchoolNoticeP:return SchoolNoticeFragment.newInstance();
		case ClassNoticeP:return ClassNoticeFragment.newInstance();
		case DailyExpressionP:return DailyExpressionFragment.newInstance();
		case QipingRecordP:return QipingRecordFragment.newInstance();
		case TeacherWordsP:return TeacherWordsFragment.newInstance();
		case GenWordsP:return GenWordsFragment.newInstance();
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
}
