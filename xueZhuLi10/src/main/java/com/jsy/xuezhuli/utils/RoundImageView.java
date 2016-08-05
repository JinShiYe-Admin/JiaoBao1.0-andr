package com.jsy.xuezhuli.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class RoundImageView {
	private PorterDuffXfermode srcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	private Paint mPaint = null;
	//
	public Bitmap roundBitmap(final Bitmap bitmap, final int radius) { // ��

		final int w = bitmap.getWidth(), h = bitmap.getHeight();

		final int v = Math.abs(w - h) / 2;

		Rect src = null, dst = null;

		if (w >= h) {

			src = new Rect(v, 0, w - v, h);

			dst = new Rect(0, 0, h, h);

		} else {

			src = new Rect(0, v, w, h - v);

			dst = new Rect(0, 0, w, w);

		}

		Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(output);

		canvas.drawRoundRect(new RectF(dst), radius, radius, getPaint());

		mPaint.setXfermode(srcIn);

		canvas.drawBitmap(bitmap, src, dst, mPaint);

		return output;

	}

	private Paint getPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		return mPaint;
	}
}
