package com.jsy.xuezhuli.utils;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class URLDrawable extends BitmapDrawable {

	private Drawable drawable;
	public URLDrawable(Drawable defaultDraw) {
		setDrawable(defaultDraw);
	}

	public void setDrawable(Drawable ndrawable) {
		drawable = ndrawable;
		if (drawable != null) {
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();
			
			if (w>0 && h>0) {
				int width = Constant.ScreenWith/3*2;
				int height = Constant.ScreenWith/3*2*h/w;
				drawable.setBounds(0, 0, width,height);
				setBounds(0, 0, width,height);
			}else{
				drawable.setBounds(0, 0, w,h);
				setBounds(0, 0, w,h);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (drawable != null) {
			drawable.draw(canvas);
		}
	}
}