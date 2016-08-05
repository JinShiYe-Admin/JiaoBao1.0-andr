package com.jsy.xuezhuli.utils;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jsy_jiaobao.main.R;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//一些修改 2016-4-27 MSL
//1.增加-部分机型在生成缩略图的时候，路径设置会抛出异常，所以增加了捕获异常

public class BaseUtils {

    /**
     * 显示OK,Cancel对话框
     */
    public static Dialog createDialog(Context context, String strTitle,
                                      String strText, int icon, OnClickListener onClickListener) {
        AlertDialog.Builder builder = initDialog(context, strTitle, strText, icon);
        builder.setPositiveButton(R.string.sure, onClickListener);
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    public static Dialog createDialog(Context context, int titleId,
                                      int textId, int icon, OnClickListener onClickListener) {
        String strTitle = context.getResources().getString(titleId);
        String strText = context.getResources().getString(textId);
        AlertDialog.Builder builder = initDialog(context, strTitle, strText, icon);
        builder.setPositiveButton(R.string.sure, onClickListener);
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }


    /**
     * 初始化对话框参数
     */
    public static Builder initDialog(Context context, String strTitle,
                                     String strText, int icon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(icon);
        builder.setTitle(strTitle);
        builder.setMessage(strText);
        return builder;
    }

    public static ProgressDialog showDialog(Context context, int id) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getResources().getString(id));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static ProgressDialog showDialog(Context context, String text) {
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
     * 获取版本号
     */
    public static String getVersion(Context context) {
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
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Date getDate() throws Exception {
        URL url = new URL("http://www.bjtime.cn");
        URLConnection uc = url.openConnection();
        uc.connect();
        uc.setConnectTimeout(3000);
        long ld = uc.getDate();
        return new Date(ld);
    }

    //获取当月天数
    public static int getDayNumber(int year, int month) {
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
                } else {
                    number = 28;
                }
                break;
            default:
                break;
        }
        return number;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "ABCEDFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void hidepan(EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            System.out.println("----close SoftInput fail");
        }
    }

    public static void openpan(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                } catch (Exception e) {
                    System.out.println("-----open SoftInput fail");
                    e.printStackTrace();
                }
            }
        }, 100);

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
        String result;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap createVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
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
        return bitmap;
    }

    /**
     * 字符串转换成日期
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
     */
    public static void insetImage(EditText edt, String imageName, Bitmap image) {
        String txt = edt.getText().toString();
        if (!TextUtils.isEmpty(txt) && !txt.endsWith("<br>")) {
            imageName = "<br>" + imageName;
        }
        Editable eb = edt.getEditableText();
        // 获得光标所在位置
        int qqPosition = edt.getSelectionStart();
        SpannableString ss = new SpannableString(imageName);
        // 定义插入图片
        Drawable drawable = new BitmapDrawable(image);
        ss.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        drawable.setBounds(2, 0, drawable.getIntrinsicWidth() + 20, drawable.getIntrinsicHeight() + 20);
        // 插入图片
        if (qqPosition < 0 || qqPosition >= eb.length()) {
            eb.append(ss);
        } else {
            eb.insert(qqPosition, ss);
        }

    }

    /**
     * 去除开头结尾空格
     */
    public static String startendNospacing(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (content.startsWith(" ")) {
                content = content.substring(1);
                content = startendNospacing(content);
            }
            if (content.endsWith(" ")) {
                content = content.substring(0, content.length() - 1);
                content = startendNospacing(content);
            }
        }
        return content;

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return true;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}