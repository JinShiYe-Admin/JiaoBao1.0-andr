package com.jsy.xuezhuli.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

public class BaseBitmapLoadCallBack {

	public static BitmapLoadCallBack<ImageView> handleCallBack() {
		return new BitmapLoadCallBack<ImageView>() {
			@Override
			public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
			}

			@Override
			public void onLoadCompleted(ImageView arg0, String arg1,
					Bitmap arg2, BitmapDisplayConfig arg3, BitmapLoadFrom arg4) {
				RoundImageView roundImage = new RoundImageView();
				Bitmap bitmap = roundImage.roundBitmap(arg2, 100);
				arg0.setImageBitmap(bitmap);
			}
		};
	}
}
