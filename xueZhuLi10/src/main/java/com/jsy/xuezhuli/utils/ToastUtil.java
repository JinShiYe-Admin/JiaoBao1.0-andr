package com.jsy.xuezhuli.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

/**
 *         Toast快速消失
 */
public class ToastUtil {

	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;
	private static final Object synObj = new Object();
	private static View view;

	public static void showMessage(final Context context, final String msg) {
		try {
			showMessage(context, msg, Toast.LENGTH_SHORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void showMessage(final Context context,final int id){
		try {
			showMessage(context, context.getResources().getString(id),Toast.LENGTH_SHORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showMessage(final Context act, final String msg,
			final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								// toast.cancel();
								toast.setView(view);
								toast.setText(msg);
								toast.setDuration(len);
							} else {
								if (act != null) {
									toast = Toast.makeText(act, msg, len);
									view = toast.getView();
								}
							}
							if (toast != null) {
								toast.show();
							}
						}
					}
				});
			}
		}).start();
	}
}