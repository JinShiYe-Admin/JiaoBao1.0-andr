package com.jsy.xuezhuli.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetManager {

	public static int checkNetwork(Context context) {
		int state = -10;
		ConnectivityManager cManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			// netTypeText.setText("无网络");
			state = -10;
		} else {
			switch (networkInfo.getType()) {
			case ConnectivityManager.TYPE_WIFI: // wifi
				// netTypeText.setText("WIFI网络");
				state = 2;
				break;
			case ConnectivityManager.TYPE_MOBILE:// 手机网络
				switch (networkInfo.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_CDMA:
					// netTypeText.setText("电信2G网络");
					state = 0;
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					// netTypeText.setText("联通3G网络");
					state = 1;
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
					// netTypeText.setText("移动或联通2G网络");
					state = 0;
					break;
				default:
					// netTypeText.setText("其他网络");
					state = 0;
					break;
				}
				break;
			default:
				break;
			}
		}
		return state;
	}

	public static void alertNetError(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("网络不可用");
		builder.setNegativeButton("退出程序", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Dialog) dialog).setCanceledOnTouchOutside(false);
				dialog.cancel();
				System.exit(0);
			}
		});
		builder.setPositiveButton("设置网络", new OnClickListener() { // 设置网络

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent intent;
//						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_SETTINGS);
//						} else {
//							intent = new Intent();
//							ComponentName component = new ComponentName(
//									"com.android.settings",
//									"com.android.settings.WirelessSettings");
//							intent.setComponent(component);
//							intent.setAction("android.intent.action.VIEW");
//						}
						context.startActivity(intent);
					}
				});
		builder.create().show();
	}
}
