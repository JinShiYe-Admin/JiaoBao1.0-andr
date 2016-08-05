package com.jsy_jiaobao.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private GestureDetector mGestureDetector;
	public MyViewPager(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context, onGestureListener);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, onGestureListener);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		getParent().requestDisallowInterceptTouchEvent(true); // 让父类不拦截触摸事件

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (Exception e) {
			// ignore it
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		try {
//			return super.onTouchEvent(ev);
//		} catch (IllegalArgumentException ex) {
//			// ignore it
//		}
//		return true;
        mGestureDetector.onTouchEvent(ev);
        return super.onTouchEvent(ev);
	}
    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY != 0 && distanceX != 0) {
            }
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
            	setParentScrollAble(true);
                return true;
            }
            //当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview 停住不能滚动
            setParentScrollAble(false);
            return false;
        }
    };

    /**
     * 是否把滚动事件交给父scrollview
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        //这里的parentScrollView就是listview外面的那个scrollview
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}
