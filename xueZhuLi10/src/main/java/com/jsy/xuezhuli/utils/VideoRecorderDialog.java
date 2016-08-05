package com.jsy.xuezhuli.utils;

import java.io.File;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsy_jiaobao.customview.MovieRecorderView;
import com.jsy_jiaobao.customview.MovieRecorderView.OnRecordFinishListener;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;

/**
 *一些修改1 2016-4-26 MSL<br>
 *1.增加-捕获启动摄像头失败的异常后返回
 */

/**
 * 录制视频
 */
public class VideoRecorderDialog extends Dialog {

	private Context mContext;
	private ClickListenerInterface clickListenerInterface;
	private Handler mHandler;

	public interface ClickListenerInterface {

		public void doConfirm();

		public void doCancel(String filePath);
	}

	public VideoRecorderDialog(Context context, Handler mHandler) {
		super(context, R.style.videodialog);
		this.mContext = context;
		this.mHandler = mHandler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	MovieRecorderView movieRecorderView;
	TextView tvConfirm;//开始录制和停止录制按钮
	TextView tvCancel;//退出按钮
	boolean startRecorder = false;//这个好像没用上

	protected DisplayMetrics getScreenWH() {
		DisplayMetrics dMetrics = new DisplayMetrics();
		dMetrics = mContext.getResources().getDisplayMetrics();
		return dMetrics;
	}

	private Handler layoutHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 10) {
				int h = msg.arg1;
				RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(
						getScreenWH().widthPixels, h);
				bottom.setLayoutParams(l);
			}
		}

	};
	RelativeLayout bottom;//底部布局

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_videorecorder, null);
		setContentView(view);
		movieRecorderView = (MovieRecorderView) view
				.findViewById(R.id.movierecorderview);
		movieRecorderView.setHandler(mHandler, layoutHandler);
		tvConfirm = (TextView) view.findViewById(R.id.confirm);
		tvCancel = (TextView) view.findViewById(R.id.cancel);
		bottom = (RelativeLayout) view.findViewById(R.id.layout_bottom);

		tvConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag == 1) {// 开始录制视频
					tvConfirm
							.setBackgroundResource(R.drawable.voice_rcd_btn_ing);
					tvCancel.setVisibility(View.VISIBLE);
					startVoiceT = System.currentTimeMillis();
					voiceName = startVoiceT + ".mp4";
					try {
						movieRecorderView.record(new OnRecordFinishListener() {

							@Override
							public void onRecordFinish() {
								clickListenerInterface.doCancel(JSYApplication
										.getInstance().AV_PATH + voiceName);
							}
						}, JSYApplication.getInstance().AV_PATH, voiceName);
					} catch (Exception e) {
						ToastUtil.showMessage(mContext, "启动摄像头失败，录制视频可能无效");
						onBackPressed();// 启动摄像头失败后退出
					}
					flag = 2;
				} else if (flag == 2) {// 停止录制视频
					tvConfirm
							.setBackgroundResource(R.drawable.voice_rcd_btn_no);
					tvCancel.setVisibility(View.INVISIBLE);
					tvConfirm.setEnabled(false);
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							tvConfirm.setEnabled(true);
						}
					}, 2000);
					movieRecorderView.stop();
					try {
						movieRecorderView.initCamera();
					} catch (Exception e) {
						e.printStackTrace();
					}
					flag = 1;
					endVoiceT = System.currentTimeMillis();
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					if (time < 3) {
						ToastUtil.showMessage(mContext, "录制时间太短");
						File file = new File(
								JSYApplication.getInstance().AV_PATH
										+ voiceName);
						if (file.exists()) {
							file.delete();
						}
					} else {
						if (!movieRecorderView.isFinish()) {
							clickListenerInterface.doCancel(JSYApplication
									.getInstance().AV_PATH + voiceName);
						}
					}
				}
			}
		});
		tvCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {//退出
				tvConfirm.setBackgroundResource(R.drawable.voice_rcd_btn_no);
				tvCancel.setVisibility(View.INVISIBLE);
				tvConfirm.setEnabled(false);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						tvConfirm.setEnabled(true);
					}
				}, 2000);
				movieRecorderView.stop();
				try {
					movieRecorderView.initCamera();
				} catch (Exception e) {
					e.printStackTrace();
				}
				flag = 1;
				File file = new File(JSYApplication.getInstance().AV_PATH
						+ voiceName);
				if (file.exists()) {
					file.delete();
				}
			}
		});
		if (movieRecorderView == null) {
			clickListenerInterface.doCancel("");
		}
	}

	private int flag = 1;
	private long startVoiceT, endVoiceT;
	String voiceName;

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// int[] location = new int[2];
	// tvConfirm.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
	// int btn_rc_Y = location[1];
	// int btn_rc_X = location[0];
	// int[] del_location = new int[2];
	// tvCancel.getLocationInWindow(del_location);
	// int del_Y = del_location[1];
	// int del_x = del_location[0];
	// if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
	// if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {
	// tvConfirm.setText("松开保存");
	// tvConfirm.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
	// tvCancel.setVisibility(View.VISIBLE);
	// startVoiceT = System.currentTimeMillis();
	// voiceName = startVoiceT + ".mp4";
	// movieRecorderView.record(new OnRecordFinishListener() {
	//
	// @Override
	// public void onRecordFinish() {
	// clickListenerInterface.doCancel(JSYApplication.getInstance().AV_PATH+voiceName);
	// }
	// },JSYApplication.getInstance().AV_PATH,voiceName);
	// flag = 2;
	// }
	// } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2)
	// {//松开手势时执行录制完成
	// tvConfirm.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
	// tvCancel.setVisibility(View.INVISIBLE);
	// tvTouch.setEnabled(false);
	// tvConfirm.setText("数据处理中");
	// mHandler.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// tvTouch.setEnabled(true);
	// tvConfirm.setText("按住开始");
	// }
	// }, 2000);
	// movieRecorderView.stop();
	// try {
	// movieRecorderView.initCamera();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// flag = 1;
	// if (event.getY() >= del_Y && event.getY() <= del_Y + tvCancel.getHeight()
	// && event.getX() >= del_x && event.getX() <= del_x + tvCancel.getWidth())
	// {
	// File file = new File(JSYApplication.getInstance().AV_PATH+voiceName);
	// if (file.exists()) {
	// file.delete();
	// }
	// } else {
	// endVoiceT = System.currentTimeMillis();
	// int time = (int) ((endVoiceT - startVoiceT) / 1000);
	// if (time < 3) {
	// ToastUtil.showMessage(mContext, "录制时间太短");
	// File file = new File(JSYApplication.getInstance().AV_PATH+voiceName);
	// if (file.exists()) {
	// file.delete();
	// }
	// }else{
	// if (!movieRecorderView.isFinish()) {
	// clickListenerInterface.doCancel(JSYApplication.getInstance().AV_PATH+voiceName);
	// }
	// }
	// }
	//
	// }
	// return true;
	// }

	public void setClickListenerInterface(
			ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	@Override
	public void onBackPressed() {
		if (flag == 1) {
			super.onBackPressed();
		}
	};

}
