package com.jsy_jiaobao.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class UpdateService extends Service {
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

//	private String url = "http://www.jiaobao.net/dl/JSY_JiaoBao.apk";
//	//标题
//	private String titleId = "jiaobaoapp";
//	private final static int DOWNLOAD_COMPLETE = 0;
//	private final static int DOWNLOAD_FAIL = 1;
//	//文件存储
//	private File updateDir = null;
//	private File updateFile = null;
//
//	//通知栏
//	private NotificationManager updateNotificationManager = null;
//	private Notification updateNotification = null;
//	//通知栏跳转Intent
//	private Intent updateIntent = null;
//	private PendingIntent updatePendingIntent = null;
//	@Override
//	public IBinder onBind(Intent intent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//
//		super.onCreate();
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		if (intent != null) {
//			Bundle bundle = intent.getExtras();
//			if (bundle != null) {
//				//获取传值
//				titleId = bundle.getString("titleId");
//				url = bundle.getString("url");
//			}
//		}
//	    //创建文件
//	    if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){
//	        updateDir = new File(JSYApplication.getInstance().DB_PATH);
//	        updateFile = new File(updateDir.getPath(),titleId+".apk");
//	    }
//
//	    this.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//	    this.updateNotification = new Notification();
//
//	    //设置下载过程中，点击通知栏，回到主界面
//	    updateIntent = new Intent(this, LoginActivity.class);
//	    updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
//	    //设置通知栏显示内容
//	    updateNotification.icon = R.drawable.logo72;
//	    updateNotification.tickerText = "开始下载";
//	    updateNotification.setLatestEventInfo(this,getString(R.string.app_name),"0%",updatePendingIntent);
//	    //发出通知
//	    updateNotificationManager.notify(0,updateNotification);
//
//	    //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
//	    new Thread(new updateRunnable()).start();//这个是下载的重点，是下载的过程
//
//	    return super.onStartCommand(intent, flags, startId);
//	}
//	private Handler updateHandler = new  Handler(){
//	    @Override
//	    public void handleMessage(Message msg) {
//	    	 switch(msg.what){
//	            case DOWNLOAD_COMPLETE:
//	            	updateNotification.flags|=updateNotification.FLAG_AUTO_CANCEL;
//	                //点击安装PendingIntent
//	                Uri uri = Uri.fromFile(updateFile);
//	                Intent installIntent = new Intent(Intent.ACTION_VIEW);
//	                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
//	                updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);
//
//	                updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒
//	                updateNotification.setLatestEventInfo(UpdateService.this, getString(R.string.app_name), "下载完成,点击安装。", updatePendingIntent);
//	                updateNotificationManager.notify(0, updateNotification);
//	                ArrayList<Object> post = new ArrayList<>();
//	                post.add(Constant.msgcenter_updataversion);
//	                EventBusUtil.post(post);
//	                //停止服务
//	                stopService(updateIntent);
//	                break;
//	            case DOWNLOAD_FAIL:
//	                //下载失败
//	                updateNotification.setLatestEventInfo(UpdateService.this, getString(R.string.app_name), "下载失败!。", updatePendingIntent);
//	                updateNotificationManager.notify(0, updateNotification);
//	                ArrayList<Object> post1 = new ArrayList<>();
//	                post1.add(Constant.msgcenter_updataversion);
//	                EventBusUtil.post(post1);
//	                break;
//	            default:
//	                stopService(updateIntent);
//	        }
//	    }
//	};
//
//
//	class updateRunnable implements Runnable {
//        Message message = updateHandler.obtainMessage();
//        public void run() {
//            message.what = DOWNLOAD_COMPLETE;
//            try{
//                //增加权限<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
//                if(!updateDir.exists()){
//                    updateDir.mkdirs();
//                }
//                if(!updateFile.exists()){
//                    updateFile.createNewFile();
//                }
//                //下载函数，以QQ为例子
//                //增加权限<uses-permission android:name="android.permission.INTERNET">;
//                long downloadSize = downloadUpdateFile(url,updateFile);
//                if(downloadSize>0){
//                    //下载成功
//                    updateHandler.sendMessage(message);
//                }
//            }catch(Exception ex){
//                ex.printStackTrace();
//                message.what = DOWNLOAD_FAIL;
//                //下载失败
//                updateHandler.sendMessage(message);
//            }
//        }
//    }
//
//
//
//	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
//        //这样的下载代码很多，我就不做过多的说明
//        int downloadCount = 0;
//        long totalSize = 0;
//        int updateTotalSize;
//
//        HttpURLConnection httpConnection = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//
//        try {
//            URL url = new URL(downloadUrl);
//            httpConnection = (HttpURLConnection)url.openConnection();
//            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
////            if(currentSize > 0) {
////                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
////            }
//            httpConnection.setConnectTimeout(10000);
//            httpConnection.setReadTimeout(20000);
//            updateTotalSize = httpConnection.getContentLength();
//            if (httpConnection.getResponseCode() == 404) {
//                throw new Exception("fail!");
//            }
//            is = httpConnection.getInputStream();
//            fos = new FileOutputStream(saveFile, false);
//            byte buffer[] = new byte[4096];
//            int readsize ;
//            while((readsize = is.read(buffer)) > 0){
//                fos.write(buffer, 0, readsize);
//                totalSize += readsize;
//                //为了防止频繁的通知导致应用吃紧，百分比增加1才通知一次
//                if((downloadCount == 0)||(int) (totalSize*100/updateTotalSize)-1>downloadCount){
//                    downloadCount += 1;
//                    updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int)totalSize*100/updateTotalSize+"%", updatePendingIntent);
//                    updateNotificationManager.notify(0, updateNotification);
//                }
//            }
//        } finally {
//            if(httpConnection != null) {
//                httpConnection.disconnect();
//            }
//            if(is != null) {
//                is.close();
//            }
//            if(fos != null) {
//                fos.close();
//            }
//        }
//        return totalSize;
//    }
//
}
