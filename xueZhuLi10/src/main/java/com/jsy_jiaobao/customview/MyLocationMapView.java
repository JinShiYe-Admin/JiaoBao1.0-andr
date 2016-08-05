package com.jsy_jiaobao.customview;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class MyLocationMapView extends MapView {
	static PopupOverlay pop = null;// 

	public MyLocationMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyLocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// 
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
}
