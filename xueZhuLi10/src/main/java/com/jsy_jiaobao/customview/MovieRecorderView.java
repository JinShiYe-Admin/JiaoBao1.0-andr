package com.jsy_jiaobao.customview;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OutputFormat;
import android.media.MediaRecorder.VideoEncoder;
import android.media.MediaRecorder.VideoSource;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.R.*;

/**
 * 一些修改1 2016-4-27 MSL 
 * 1.修改-注释一些摄像头的参数
 * 
 * 一些修改2 2016-5-3 MSL 
 * 1.修改-录制视频最大时间由60s改为10s  
 */
/**
 * 视频录制控件
 * 
 * @author lip
 * 
 * @date 2015-3-16
 */
public class MovieRecorderView extends LinearLayout implements OnErrorListener {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private TextView mProgressBar;

	private MediaRecorder mMediaRecorder;
	private Camera mCamera;
	private Timer mTimer;// 计时器
	private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口

	private int mWidth;// 视频分辨率宽度
	private int mHeight;// 视频分辨率高度
	private boolean isOpenCamera;// 是否一开始就打开摄像头
	private int mRecordMaxTime;// 一次拍摄最长时间
	private int mTimeCount;// 时间计数
	private File mVecordFile = null;// 文件
	private boolean isFinish = true;
	private Handler mHandler;

	public MovieRecorderView(Context context) {
		this(context, null);
	}

	public MovieRecorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public MovieRecorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mWidth = 800;
		mHeight = 480;
		isOpenCamera = true;
		mRecordMaxTime = 10;// 60S改为10S

		LayoutInflater.from(context)
				.inflate(R.layout.moive_recorder_view, this);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		mProgressBar = (TextView) findViewById(R.id.progressBar);
		mProgressBar.setText(mTimeCount + "s/" + mRecordMaxTime + "s");// 设置进度条最大量

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(new CustomCallBack());
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	/**
	 * 
	 * @author liuyinjun
	 * 
	 * @date 2015-2-5
	 */
	private class CustomCallBack implements Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (!isOpenCamera)
				return;
			try {
				initCamera();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (!isOpenCamera)
				return;
			freeCameraResource();
		}

	}

	/**
	 * 更新相机参数
	 */
	private void updateCameraParameters() {
		if (mCamera != null) {
			Camera.Parameters p = mCamera.getParameters();
			long time = new Date().getTime();
			p.setGpsTimestamp(time);
			Size previewSize = p.getPreviewSize();
			mCamera.setParameters(p);

			int supportPreviewWidth = previewSize.width;
			int supportPreviewHeight = previewSize.height;
			int srcWidth = getScreenWH().widthPixels;
			int srcHeight = getScreenWH().heightPixels;
			int width = Math.min(srcWidth, srcHeight);
			int height = width * supportPreviewWidth / supportPreviewHeight;
			this.surfaceHeight = height;
			this.surfaceWidth = width;
			if ((srcHeight - height) > 200) {
				Message msg = new Message();
				msg.what = 10;
				msg.arg1 = srcHeight - height;
				layoutHandler.sendMessage(msg);
			}
			mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(width,
					height));
		}
	}

	private int surfaceWidth;
	private int surfaceHeight;

	protected DisplayMetrics getScreenWH() {
		DisplayMetrics dMetrics = new DisplayMetrics();
		dMetrics = this.getResources().getDisplayMetrics();
		return dMetrics;
	}

	/**
	 * 初始化摄像头
	 * 
	 * @author lip
	 * @date 2015-3-16
	 * @throws IOException
	 */
	public void initCamera() throws IOException {
		if (mCamera != null) {
			freeCameraResource();
		}
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
			freeCameraResource();
			mSurfaceView.setVisibility(View.INVISIBLE);// 设为不可见
			ToastUtil.showMessage(getContext(), "启动摄像头失败，录制视频可能无效");
		}
		if (mCamera == null)
			return;
		setCameraParams();
		mCamera.setDisplayOrientation(90);
		mCamera.setPreviewDisplay(mSurfaceHolder);
		mCamera.startPreview();
		mCamera.unlock();
	}

	/**
	 * 设置摄像头为竖屏
	 * 
	 * @author lip
	 * @date 2015-3-16
	 */
	private void setCameraParams() {
		if (mCamera != null) {
			Parameters params = mCamera.getParameters();
			params.set("orientation", "portrait");
			mCamera.setParameters(params);
			updateCameraParameters();
		}
	}

	/**
	 * 释放摄像头资源
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 */
	private void freeCameraResource() {
		if (mCamera != null) {
			try {
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				mCamera.lock();
				mCamera.release();
				mCamera = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createRecordDir(String filePath, String fileName) {
		try {
			mVecordFile = new File(filePath + fileName);
			if (!mVecordFile.exists()) {
				mVecordFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// File vecordDir = sampleDir;
		// // 创建文件
		// try {
		// mVecordFile = File.c(fileName, ".mp4", vecordDir);//mp4格式
		// //LogUtils.i(mVecordFile.getAbsolutePath());
		// Log.d("Path:",mVecordFile.getAbsolutePath());
		// } catch (IOException e) {
		// }
	}

	/**
	 * 初始化
	 * 
	 * @author lip
	 * @date 2015-3-16
	 * @throws IOException
	 */
	private void initRecord() throws IOException {
		mMediaRecorder = new MediaRecorder();
		mMediaRecorder.reset();
		if (mCamera != null)
			mMediaRecorder.setCamera(mCamera);
		mMediaRecorder.setOnErrorListener(this);
		mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 视频输出格式
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// 视频录制格式
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 音频格式
		mMediaRecorder.setVideoSize(640, 480);// 设置分辨率：
		mMediaRecorder.setVideoFrameRate(30);//
		mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 512);//
		// 设置帧频率，然后就清晰了
		// mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));//
		// 清晰度
		mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
		// mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
		mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始录制视频
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 * @param fileName
	 *            视频储存位置
	 * @param onRecordFinishListener
	 *            达到指定时间之后回调接口
	 */
	public void record(final OnRecordFinishListener onRecordFinishListener,
			String filePath, String fileName) {
		this.mOnRecordFinishListener = onRecordFinishListener;
		isFinish = false;
		createRecordDir(filePath, fileName);
		try {
			if (!isOpenCamera)// 如果未打开摄像头，则打开
				initCamera();
			initRecord();
			mTimeCount = 0;// 时间计数器重新赋值
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					mTimeCount++;
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mProgressBar.setText(mTimeCount + "s/"
									+ mRecordMaxTime + "s");
						}
					});
					if (mTimeCount == mRecordMaxTime) {// 达到指定时间，停止拍摄
						isFinish = true;
						stop();
						if (mOnRecordFinishListener != null)
							mOnRecordFinishListener.onRecordFinish();
					}
				}
			}, 0, 1000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止拍摄
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 */
	public void stop() {
		try {
			stopRecord();
			releaseRecord();
			freeCameraResource();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止录制
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 */
	public void stopRecord() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mProgressBar.setText("时间:" + mTimeCount + "s");
			}
		});
		if (mTimer != null)
			mTimer.cancel();
		if (mMediaRecorder != null) {
			// 设置后不会崩
			mMediaRecorder.setOnErrorListener(null);
			mMediaRecorder.setPreviewDisplay(null);
			try {
				mMediaRecorder.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放资源
	 * 
	 * @author liuyinjun
	 * @date 2015-2-5
	 */
	private void releaseRecord() {
		if (mMediaRecorder != null) {
			mMediaRecorder.setOnErrorListener(null);
			try {
				mMediaRecorder.reset();
				mMediaRecorder.release();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mMediaRecorder = null;
	}

	public int getTimeCount() {
		return mTimeCount;
	}

	/**
	 * @return the mVecordFile
	 */
	public File getmVecordFile() {
		return mVecordFile;
	}

	/**
	 * 录制完成回调接口
	 * 
	 * @author lip
	 * 
	 * @date 2015-3-16
	 */
	public interface OnRecordFinishListener {
		public void onRecordFinish();
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		try {
			if (mr != null)
				mr.reset();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	Handler layoutHandler;

	public void setHandler(Handler mHandler, Handler layoutHandler) {
		this.mHandler = mHandler;
		this.layoutHandler = layoutHandler;
	}

	public int getSurfaceWidth() {
		return surfaceWidth;
	}

	public void setSurfaceWidth(int surfaceWidth) {
		this.surfaceWidth = surfaceWidth;
	}

	public int getSurfaceHeight() {
		return surfaceHeight;
	}

	public void setSurfaceHeight(int surfaceHeight) {
		this.surfaceHeight = surfaceHeight;
	}
}