package com.jsy.xuezhuli.utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.jsy_jiaobao.main.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

//一些修改 2016-4-27 MSL
//1.增加-部分机型在生成缩略图的时候，路径设置会抛出异常，所以增加了捕获异常

public class BaseUtils {
	
	/**
	 * 显示OK,Cancel对话框
	 * 
	 * @param context
	 * @param strTitle
	 * @param strText
	 * @param icon
	 */
	public static Dialog createDialog(Context context, String strTitle,
			String strText, int icon, OnClickListener onClickListener) {
		AlertDialog.Builder builder = initDialog(context, strTitle, strText,icon);
		builder.setPositiveButton(R.string.sure, onClickListener);
		builder.setNegativeButton(R.string.cancel, null);
		return builder.create();
	}
	public static Dialog createDialog(Context context, int titleId,
			int textId, int icon, OnClickListener onClickListener) {
		String strTitle=context.getResources().getString(titleId);
		String strText=context.getResources().getString(textId);
		AlertDialog.Builder builder = initDialog(context, strTitle, strText,icon);
		builder.setPositiveButton(R.string.sure, onClickListener);
		builder.setNegativeButton(R.string.cancel, null);
		return builder.create();
	}
	

	/**
	 * 初始化对话框参数
	 * 
	 * @param context
	 * @param strTitle
	 * @param strText
	 * @param icon
	 * @return
	 */
	public static Builder initDialog(Context context, String strTitle,
			String strText, int icon) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(icon);
		builder.setTitle(strTitle);
		builder.setMessage(strText);
		return builder;
	}

	// show the progress bar.
	public static ProgressDialog showHorizontalProgress(Context context,
			CharSequence title, CharSequence message, boolean indeterminate,
			boolean cancelable) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(indeterminate);
		dialog.setCancelable(cancelable);

		dialog.show();
		return dialog;
	}

	public static ProgressDialog showSpinnerProgress(Context context) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(context.getResources().getString(R.string.public_loading));
		return dialog;
	}
	public static ProgressDialog showDialog(Context context,int id){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(context.getResources().getString(id));
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	public static ProgressDialog showDialog(Context context,String text) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(text);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}


	public static void shortToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void shortToast(Context context, int resId) {
		Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();
	}


	public static void longToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	public static void longToast(Context context, int textId) {
		Toast.makeText(context, context.getResources().getString(textId), Toast.LENGTH_LONG).show();
	}

	/**
	 * 空的判断
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNull(String text) {
		if (text == null || "".equals(text.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 获取版本号
	 */
	public static String getVersion(Context context){
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "Unknown";
		}
	}
	/**
	 * 获取版本号
	 */
	public static int getVersionCode(Context context){
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static Date getDate() throws Exception {
	       URL url=new URL("http://www.bjtime.cn");// 
	       URLConnection uc=url.openConnection();// 
	       uc.connect(); // 
	       uc.setConnectTimeout(3000);
	       long ld=uc.getDate(); // 
	       Date date=new Date(ld); // 
	       // 
	       return date;
	 }
	//获取当月天数
	public static int getDayNumber(int year,int month){
		int number = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			number = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			number = 31;
			break;
		case 2:
			if (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)) {
				number = 29;
			}else{
				number = 28;
			}
			break;
		default:
			break;
		}
		return number;
	}
	/** 
	 * google maps的脚本里代码 
	 */    
	private static double rad(double d) { 
	     return d * Math.PI / 180.0; 
	}  

	/** 
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
	 */ 
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2){ 
	    double radLat1 = rad(lat1); 
	    double radLat2 = rad(lat2); 
	    double a = radLat1 - radLat2; 
	    double b = rad(lng1) - rad(lng2); 
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
	    s = s * 6378.137; 
	    s = Math.round(s * 10000) / 10000; 
	    return s; 
	} 
	/**
	 * 登录
	 */
	public static void Login(Context context){
//		context.startActivity(new Intent(context,LoginActivity.class));
	}
	/**
	 * 跳转
	 */
	public static void InToActivity(Context context,Class<?> activity){
		context.startActivity(new Intent(context,activity));
	}
	/**
	 * 判断是否登录
	 */
	public static boolean isLanding(Context context){
		SharedPreferences sp = context.getSharedPreferences(Constant.SP_TB_USER, context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		if (!"".equals(uid)) {
			return true;
		}
		return false;
	}
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "ABCEDFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }   

//	public static String RandomNumber(int length) {
//		Random random = new Random();
//		String str = "";
//		for (int i = 1; i <= length; i++) {
//			int ma = random.nextInt(93)+33; // 33-126
//			char ca = (char) ma;
//			str += ca + "";
//		}
//		return str;
//	}
	public static void hidepan(EditText editText){
		try {
			InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE); 
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
		} catch (Exception e) {
			System.out.println("----close SoftInput fail");
		}  
	}

	public static void openpan(final EditText editText) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				try {
					InputMethodManager inputManager =(InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(editText, 0);
				} catch (Exception e) {
					System.out.println("-----open SoftInput fail");
					e.printStackTrace();
				}
			}
		},100);
//		try {
//			// 显示键盘
//			InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//			m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		} catch (Exception e) {
//			System.out.println("-----open SoftInput fail");
//		}
	}
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
	// 播放默认铃声  
    // 返回Notification id  
	public static void PlaySound(Context context) {  
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
        Notification nt = new Notification();  
        nt.defaults = Notification.DEFAULT_SOUND;  
//        int soundId = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);  
        mgr.notify(1000, nt);  
    }  


	/**
	 * 获取IMEI号，IESI号，手机型号
	 */
	public static String getInfo(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		String imsi = mTm.getSubscriberId();
		return "手机IMEI号：" + imei + "手机IESI号：" + imsi;
	}

	/**
	 * .获取手机MAC地址 只有手机开启wifi才能获取到mac地址
	 */
	public static String getMacAddress(Context context) {
		String result = "";
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}
	/**
	 * 获取视频缩略图
	 * @param filePath
	 * @return
	 */
	@SuppressLint("NewApi") 
	public static Bitmap getVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(filePath);
			bitmap = retriever.getFrameAtTime();
		} 
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (RuntimeException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				retriever.release();
			} 
			catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
    /** 
     * 获取视频的缩略图 
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
     * @param videoPath 视频的路径 
     * @param width 指定输出视频缩略图的宽度 
     * @param height 指定输出视频缩略图的高度度 
     * @param kind 参照{@link}{MediaStore.Images.Thumbnails}类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
     * @return 指定大小的视频缩略图 
     */  
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {  
        Bitmap bitmap = null;  
        // 获取视频的缩略图  
//        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
//        System.out.println("w"+bitmap.getWidth());  
//        System.out.println("h"+bitmap.getHeight());  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static Bitmap createVideoThumbnail(String url, int width, int height) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		int kind = MediaStore.Video.Thumbnails.MINI_KIND;			
		try {
			if (Build.VERSION.SDK_INT >= 14) {
				try {
					retriever.setDataSource(url, new HashMap<String, String>());
				} catch (Exception e) {
					//如果手机的安卓系统被修改了，if条件不合适，会抛出异常
					retriever.setDataSource(url);
				}				
			} else {
				retriever.setDataSource(url);
			}
			bitmap = retriever.getFrameAtTime();
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			// Assume this is a corrupt video file
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			// Assume this is a corrupt video file.
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
				ex.printStackTrace();
				// Ignore failures while cleaning up.
			}
		}
		if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}

	/**
	 * 日期转换成字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		return str;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * EditText插入图片
	 * @param edt
	 * @param imageName
	 * @param image
	 */
	public static void insetImage(Context context,EditText edt,String imageName, Bitmap image) {
		String txt = edt.getText().toString();
		if (!TextUtils.isEmpty(txt)&&!txt.endsWith("<br>")) {
			imageName ="<br>"+imageName;
		}
		Editable eb = edt.getEditableText();
		// 获得光标所在位置
		int qqPosition = edt.getSelectionStart();
		 	SpannableString ss = new SpannableString(imageName);
		// 定义插入图片
		Drawable drawable = new BitmapDrawable(image);
		ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0,ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		drawable.setBounds(2, 0, drawable.getIntrinsicWidth() + 20,drawable.getIntrinsicHeight() + 20);
		// 插入图片
		if(qqPosition<0 || qqPosition >= eb.length()){
			eb.append(ss);
        }else{
        	eb.insert(qqPosition, ss);
        }
		
    }
	
	/**
	 * 去除开头结尾空格
	 * @param content
	 * @return
	 */
	public static String startendNospacing(String content){
		if (!TextUtils.isEmpty(content)) {
			if (content.startsWith(" ")) {
				content = content.substring(1);
				content = startendNospacing(content);
			}
			if (content.endsWith(" ")) {
				content = content.substring(0,content.length()-1);
				content = startendNospacing(content);
			}
		}
		return content;
			
	}
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
//			Log.i("NetWorkState", "Unavailabel");
			return true;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//						Log.i("NetWorkState", "Availabel");
						return false;
					}
				}
			}
		}
		return true;
	}
}