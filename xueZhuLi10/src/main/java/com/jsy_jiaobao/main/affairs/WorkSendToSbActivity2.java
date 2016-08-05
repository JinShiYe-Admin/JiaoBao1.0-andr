package com.jsy_jiaobao.main.affairs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.filechoser.grivider.FileChooseActivity;
import com.filechoser.grivider.FilePerate;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.SelectFilePopupWindow;
import com.jsy.xuezhuli.utils.SoundMeter;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.VideoRecorderDialog;
import com.jsy.xuezhuli.utils.VideoRecorderDialog.ClickListenerInterface;
import com.jsy.xuezhuli.utils.WatchUtils;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnit;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitList;
import com.jsy_jiaobao.po.personal.MyUnit;
import com.jsy_jiaobao.po.sys.GetCommPerm;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 发布事务有关的代码不是我写的，但是注释是我补充的，写得对不对我也说不清<br>
 * 为什么不由写代码的加注释呢，因为他已经离职了<br>
 * 发布事务没有需求，说改就改<br>
 * ShangLin Mo 2016-6-24 16:37:21
 * 
 * 一些修改1 2016-4-25 MSL <br>
 * 1.修改-添加附件增加图片后，点击图片区域不删除图片 <br>
 * 2.修改-修改添加图片的缩略图大小 <br>
 * 3.增加-点击删除按钮，弹出询问确定删除<br>
 * 
 * 一些修改2 2016-4-26 MSL <br>
 * 1.修改-修改点击视频，弹出的活动设置的路径中小米手机无效
 * 
 * 一些修改3 2016-4-29 MSL <br>
 * 1.修改附件如果是图片显示缩略图的部分逻辑
 * 
 * 一些修改4 2016-4-29 MSL <br>
 * 1.增加-记录录音时麦克风获取的分贝数，如果持续为0.0。录音结束后有提示
 * 
 * 一些修改5 2016-5-11 MSL <br>
 * 1.修改-录音时获取麦克风分贝数，第二秒为0.0分贝，停止录音弹出提示<br>
 * 2.增加-录音权限被禁后，一些华为手机的麦克风还是能获取到声音，在录音结束后，若录音文件大小为0，删掉文件并弹出提示
 * 
 * 一些修改6 2016-5-14 MSL <br>
 * 1.增加-增加附件最多9个的限制
 * 
 * 一些修改7 2016-5-17 MSL <br>
 * 1.将layout_photofile的findViewById改为R.id.worksend_layout_file
 * 
 * 一些修改8 2016-5-25 MSL <br>
 * 1.附件限制10个
 */
enum Function {
	UNIT, GEN, OTHER;
}

/**
 * 功能说明：发布事务（内部事务，家校互动，多单位事务）
 */
public class WorkSendToSbActivity2 extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "WorkSendToSbActivity2";
	private static final int POLL_INTERVAL = 300;// 间隔时间
	public static MyUnit myUnit;// 当前所在单位model
	public static ArrayList<CommMsgRevicerUnit> ParentUnits;// 上级单位数组可能有多个
	public static ArrayList<CommMsgRevicerUnit> SubUnits;// 下级单位数组可能有多个
	public static ArrayList<CommMsgRevicerUnitClass> UnitClass;//  班级数组，我所执教的班级，数组，如果是教育局，这个对象为null
	public static List<SMSTreeUnitID> SMSList;
	public static boolean UnitCommRight = true;// 同单位事务

	private int width;// 屏幕宽度
	protected long size = 0;
	private boolean rcdPressed = false;// 是否点击了开始录音按钮
	private boolean commMsg = false;// 获取到取接收人列表的回调
	private boolean SMSComm = false;// 获取到短信直通车权限数据的回调
	private String photoPath;// 图片路径
	private Uri photoUri;// 图片路径
	private GetCommPerm getCommPerm;// 新建事务权限
	private Thread thread;
	protected Bitmap bitmap;
	protected File file;
	private Function function = Function.UNIT;// 标题，内部事务，家校互动，多单位事务
	private ArrayList<String> selectFileList = new ArrayList<String>();// 附件文件路径

	private Context mContext;
	private LinearLayout layout_body;// 整个界面
	private LinearLayout layout_file;// 附件显示布局
	// private LinearLayout layout_photofile;
	private TextView tv_left;// 显示可输入的剩余字数
	private TextView tv_takefile;// 附件按钮
	private IEditText edt_input;// 内容编辑框
	private ImageView iv_send;// 发布按钮
	private ImageView unitFragment;// 内容事务
	private ImageView genFragment;// 家校互动
	private ImageView otherUnitFragment;// 多单位事务
	private CheckBox cb_ismsg;// 是否发送短信
	private SelectFilePopupWindow menuWindow;// 显示上传附件的方式

	/* 录音 start */
	private int voice_rc_time;// 录音持续时间
	private boolean isShosrt = false;// 录音时间过短
	private long startVoiceT;// 录音开始的时间
	private long endVoiceT;// 录音结束的时间
	private String mVoice;// 分贝数
	private String voiceName;// 录音文件名称
	private String playingMediaName;// 多媒体名称
	private SoundMeter mSensor;// 声音管理
	private Timer timer;
	private MediaPlayer mMediaPlayer = new MediaPlayer();// 多媒体播放器

	private View rcChat_popup;// 录音显示UI层
	private LinearLayout voice_rcd_hint_rcding;// 录音中
	private LinearLayout voice_rcd_hint_loading;// 加载中
	private LinearLayout voice_rcd_hint_tooshort;// 录音时间太短
	private TextView voice_time;// 录音持续时间
	private TextView btn_recvoice;// 开始录音
	private ImageView img1;// 中止录音
	private ImageView volume;// 显示分贝

	/* 录音end */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPassData(savedInstanceState);
		initViews();
	}

	private void initPassData(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
	}

	private void initViews() {
		mContext = this;
		setContentLayout(R.layout.activity_worksendsb2);
		setActionBarTitle(R.string.send_work);
		commMsg = false;
		SMSComm = false;
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		WorkSendToSbActivity2Controller.getInstance().setContext(this);
		WorkSendToSbActivity2Controller.getInstance().GetCommPerm();// 取发送事务权限
		WorkSendToSbActivity2Controller.getInstance().SMSCommIndex();// 获取短信直通车接收单位树数据源
		findView();
		initData();
	}

	private void findView() {
		layout_body = (LinearLayout) findViewById(R.id.layout_worksendsb2);// 整个界面布局
		layout_file = (LinearLayout) findViewById(R.id.worksend_layout_file);// 附件显示布局
		// layout_photofile = (LinearLayout)
		// findViewById(R.id.worksend_layout_file);
		tv_left = (TextView) findViewById(R.id.tv_leftWords);// 显示可输入字数
		tv_takefile = (TextView) findViewById(R.id.worksend_tv_takefile);// 附件按钮
		edt_input = (IEditText) findViewById(R.id.worksend_edt_input);// 内容编辑框
		unitFragment = (ImageView) findViewById(R.id.worksend_iv_unitfragment);// 内部事务
		genFragment = (ImageView) findViewById(R.id.worksend_iv_genfragment);// 家校互动
		otherUnitFragment = (ImageView) findViewById(R.id.worksend_iv_otherunitfragment);// 多单位事务
		iv_send = (ImageView) findViewById(R.id.worksend_iv_send);// 发布按钮
		cb_ismsg = (CheckBox) findViewById(R.id.worksend_cb_ismsg);// 是否发送短信
		/* 录音start */
		voice_rcd_hint_rcding = (LinearLayout) findViewById(R.id.voice_rcd_hint_rcding);// 录音中
		voice_rcd_hint_loading = (LinearLayout) findViewById(R.id.voice_rcd_hint_loading);// 加载中
		voice_rcd_hint_tooshort = (LinearLayout) findViewById(R.id.voice_rcd_hint_tooshort);// 录像时间太短
		btn_recvoice = (TextView) findViewById(R.id.btn_rcd);// 开始录音
		voice_time = (TextView) findViewById(R.id.voice_time);// 录音持续时间
		img1 = (ImageView) findViewById(R.id.img1);// 中止录音
		volume = (ImageView) findViewById(R.id.volume);// 显示分贝
		rcChat_popup = findViewById(R.id.rcChat_popup);// 录音显示UI层
		/* 录音end */
	}

	private void initData() {
		unitFragment
				.setImageResource(R.drawable.icon_worksend_unitfragment_selected);
		genFragment
				.setImageResource(R.drawable.icon_worksend_genfragment_unselect);
		otherUnitFragment
				.setImageResource(R.drawable.icon_worksend_otherunitfragment_unselect);
		unitFragment.setOnClickListener(this);
		genFragment.setOnClickListener(this);
		otherUnitFragment.setOnClickListener(this);
		tv_takefile.setOnClickListener(this);
		iv_send.setOnClickListener(this);
		WatchUtils.edtviewAddWarcher(mContext, edt_input, 350, tv_left);// 显示可输入的剩余字数
		/* 录音start */
		mSensor = new SoundMeter();
		btn_recvoice.setOnClickListener(this);
		img1.setOnClickListener(this);
		/* 录音end */
		/**
		 * 设置监听，当触摸。。edt_input时，父ViewGroup禁止滚动
		 */
		edt_input.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.worksend_edt_input) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});
		// 获取手机宽度,用于录音按时间长短显示时，按比例显示
		WindowManager manager = this.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		width = outMetrics.widthPixels;
	}

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	/**
	 * 麦克风的分贝数
	 */
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
			mVoice = amp + "";
			// System.out.println("分贝："+mVoice+"-"+voice_rc_time);
			if (voice_rc_time > 1) {
				if ("0.0".equals(mVoice)) {
					Builder deleDialog = new AlertDialog.Builder(mContext);
					deleDialog.setTitle("提示");
					deleDialog.setMessage("麦克风没有声音，可能是录音权限被禁！请在权限管理中将录音设为允许");
					deleDialog.setPositiveButton("我知道了",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					deleDialog.show();
					btn_recvoice.setText(R.string.start_toRecord);
					rcdPressed = false;
					btn_recvoice.setCompoundDrawablesWithIntrinsicBounds(
							android.R.drawable.presence_audio_online, 0, 0, 0);
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					stop();
					File file = new File(
							android.os.Environment
									.getExternalStorageDirectory()
									+ "/"
									+ voiceName);
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
	};

	/**
	 * 开始录音
	 * 
	 * @param
	 */
	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						voice_rc_time++;
						voice_time.setText(voice_rc_time + "s");// 显示录音持续的时间
					}
				});
			}
		}, 0, 1000);
	}

	/**
	 * 停止录音
	 */
	private void stop() {
		timer.cancel();
		voice_rc_time = 0;
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		try {
			mSensor.stop();
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.showMessage(mContext, "录音功能异常");
		}
		volume.setImageResource(R.drawable.amp1);
		tv_takefile.setClickable(true);// 附件按钮可点击
		edt_input.setEnabled(true);// 输入框可输入
	}

	/**
	 * 根据分贝数显示录音的声音
	 * 
	 * @param signalEMA
	 *            分贝数
	 */
	private void updateDisplay(double signalEMA) {
		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);
			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				break;
			case 101:
				break;
			case 102:
				if (commMsg && SMSComm) {
					DialogUtil.getInstance().cannleDialog();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_rcd:// 开始录音和结束录音按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_btn_rcd));
			boolean b = noMoreThanTen();
			if (b) {
				if (!rcdPressed) {// 开始录音
					tv_takefile.setClickable(false);// 附件按钮不可点击
					edt_input.setEnabled(false);// 编辑框不允许输入
					btn_recvoice.setText(R.string.click_toSave);
					btn_recvoice.setCompoundDrawablesWithIntrinsicBounds(
							android.R.drawable.presence_audio_busy, 0, 0, 0);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding
										.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					startVoiceT = System.currentTimeMillis();
					voiceName = startVoiceT + ".aac";
					start(voiceName);
				} else {// 结束录音
					btn_recvoice.setText(R.string.start_toRecord);
					btn_recvoice.setCompoundDrawablesWithIntrinsicBounds(
							android.R.drawable.presence_audio_online, 0, 0, 0);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = System.currentTimeMillis();
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					// if (time < 1) {
					// isShosrt = true;
					// voice_rcd_hint_loading.setVisibility(View.GONE);
					// voice_rcd_hint_rcding.setVisibility(View.GONE);
					// voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
					// mHandler.postDelayed(new Runnable() {
					// public void run() {
					// voice_rcd_hint_tooshort
					// .setVisibility(View.GONE);
					// rcChat_popup.setVisibility(View.GONE);
					// isShosrt = false;
					// }
					// }, 500);
					// }
					rcChat_popup.setVisibility(View.GONE);
					// 把文件显示出来就行了。
					final String path = JSYApplication.getInstance().AV_PATH
							+ voiceName;
					File file = new File(path);
					size += file.length();
					if (file.length() == 0 || time == 0) {// 录音文件大小为0或者录音时间为0
						if (file.length() == 0) {
							ToastUtil.showMessage(mContext,
									"录音文件大小为0，请检查麦克风设置！");
						} else {
							ToastUtil.showMessage(mContext, "录音时间过短");
						}
						File file2 = new File(
								android.os.Environment
										.getExternalStorageDirectory()
										+ "/"
										+ voiceName);
						if (file2.exists()) {
							file2.delete();
						}
						size -= file.length();
					} else if (size > 10 * 1024 * 1024) {
						ToastUtil.showMessage(mContext,
								R.string.noMoreThan_10M_content);
						size -= file.length();
					} else {
						LinearLayout layout = new LinearLayout(mContext);
						ImageView delete = new ImageView(mContext);
						delete.setImageResource(R.drawable.btn_sendmsg_delete);
						TextView item = new TextView(mContext);
						item.setText(time + "\"");
						item.setMaxWidth(width);
						// Log.i("width", width + "");
						int i = mtime(time);
						item.setMinimumWidth(126 + (width / 30) * i); // 录音图标按照时间长短显示（最长120S，每6秒增加一个长度）
						item.setBackgroundResource(R.drawable.chatto_bg);
						item.setPadding(5, 5, 5, 5);
						item.setCompoundDrawablesWithIntrinsicBounds(
								R.drawable.chatto_voice_playing, 0, 0, 0);
						item.setTag(path);
						item.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								if (path.equals(playingMediaName)) {
									try {
										if (mMediaPlayer.isPlaying()) {
											mMediaPlayer.stop();
										} else {
											playMusic(path);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									playMusic(path);
								}
							}
						});
						ArrayList<Object> tag = new ArrayList<Object>();
						tag.add(0, layout);
						tag.add(1, path);
						delete.setTag(tag);
						item.setTag(tag);
						layout.addView(delete);
						layout.addView(item);
						layout_file.addView(layout);
						selectFileList.add(path);
						delete.setOnClickListener(deleteListener);
					}
				}
				rcdPressed = !rcdPressed;
			}
			break;
		case R.id.img1:// 中止录音
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_stop_rcd));
			btn_recvoice.setText(R.string.start_toRecord);
			rcdPressed = false;
			btn_recvoice.setCompoundDrawablesWithIntrinsicBounds(
					android.R.drawable.presence_audio_online, 0, 0, 0);
			rcChat_popup.setVisibility(View.GONE);
			img1.setVisibility(View.VISIBLE);
			stop();
			File file = new File(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ voiceName);
			if (file.exists()) {
				file.delete();
			}
			break;
		case R.id.worksend_iv_unitfragment:// 内部事务
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_unitfragment));
			unitFragment
					.setImageResource(R.drawable.icon_worksend_unitfragment_selected);
			genFragment
					.setImageResource(R.drawable.icon_worksend_genfragment_unselect);
			otherUnitFragment
					.setImageResource(R.drawable.icon_worksend_otherunitfragment_unselect);
			function = Function.UNIT;
			WorkSendToSbActivity2Controller.getInstance().switchUnitFragment();
			break;
		case R.id.worksend_iv_genfragment:// 家校互动
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_genfragment));
			unitFragment
					.setImageResource(R.drawable.icon_worksend_unitfragment_unselect);
			genFragment
					.setImageResource(R.drawable.icon_worksend_genfragment_selected);
			otherUnitFragment
					.setImageResource(R.drawable.icon_worksend_otherunitfragment_unselect);
			function = Function.GEN;
			WorkSendToSbActivity2Controller.getInstance().switchGenFragment();
			break;
		case R.id.worksend_iv_otherunitfragment:// 多单位事务
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_otherunitfragment));
			unitFragment
					.setImageResource(R.drawable.icon_worksend_unitfragment_unselect);
			genFragment
					.setImageResource(R.drawable.icon_worksend_genfragment_unselect);
			otherUnitFragment
					.setImageResource(R.drawable.icon_worksend_otherunitfragment_selected);
			function = Function.OTHER;
			WorkSendToSbActivity2Controller.getInstance()
					.switchOtherUnitFragment();
			break;
		case R.id.worksend_tv_takefile:// 添加附件按钮
			boolean b2 = noMoreThanTen();
			if (b2) {
				MobclickAgent.onEvent(
						mContext,
						getResources().getString(
								R.string.WorkSendToSbActivity2_takefile));
				// 实例化SelectPicPopupWindow
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
				}
				menuWindow = new SelectFilePopupWindow(
						WorkSendToSbActivity2.this, WorkSendToSbActivity2.this);
				// 显示窗口
				menuWindow.showAtLocation(layout_body, Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			}
			break;
		case R.id.btn_pick_file:// 附件，从本地选择
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_btn_pick_file));

			menuWindow.dismiss();
			if (FilePerate.getRootFolder() == null) {// 如果没有SD卡，则输出提示
				Toast.makeText(mContext, R.string.noSDCard, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// 创建一个自定义的对话框
			Intent intent1 = new Intent(mContext, FileChooseActivity.class);
			startActivityForResult(intent1, 10);
			break;
		case R.id.btn_take_photo:// 附件，从相机拍照
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_btn_take_photo));
			menuWindow.dismiss();
			try {
				photoPath = JSYApplication.getInstance().FILE_PATH;
				File photoFile = PictureUtils.createImageFile(photoPath);
				if (photoFile != null) {
					photoPath = photoFile.getAbsolutePath();
					PictureUtils.dispatchTakePictureIntent(
							WorkSendToSbActivity2.this, photoFile, 11);
				}
			} catch (Exception e) {
				ToastUtil.showMessage(mContext, R.string.open_camera_abnormal);
			}
			break;
		case R.id.btn_take_video:// 附件，录制视频
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_btn_take_video));
			menuWindow.dismiss();
			final VideoRecorderDialog dialog = new VideoRecorderDialog(
					mContext, mHandler);
			dialog.setClickListenerInterface(new ClickListenerInterface() {
				@Override
				public void doConfirm() {
				}

				@Override
				public void doCancel(final String filePath) {
					dialog.cancel();
					mHandler.post(new Runnable() {
						public void run() {
							// size = 0;
							// for (int i = 0; i < selectFileList.size();
							// i++) {
							// File file = new File(selectFileList.get(i));
							// size += file.length();
							// }
							File file = new File(filePath);
							size += file.length();
							if (size == 0) {// 文件大小为0
								if (file.exists()) {
									file.delete();
								}
							} else {
								if (size > 10 * 1024 * 1024) {// 大于10M
									ToastUtil.showMessage(mContext,
											R.string.noMoreThan_10M_content);
									size -= file.length();
								} else {
									LinearLayout layout = new LinearLayout(
											mContext);
									ImageView delete = new ImageView(mContext);
									delete.setImageResource(R.drawable.btn_sendmsg_delete);
									ImageView item = new ImageView(mContext);
									item.setLayoutParams(new LinearLayout.LayoutParams(
											(int) getResources().getDimension(
													R.dimen.px_to_dip_100),
											(int) getResources().getDimension(
													R.dimen.px_to_dip_100)));
									Bitmap bg = BaseUtils.createVideoThumbnail(
											filePath, 100, 100);// 生成缩略图
									if (bg == null) {// 生成缩略图失败
										item.setImageResource(R.drawable.pic_video_no);
									} else {
										item.setImageBitmap(bg);
									}
									item.setTag(filePath);
									item.setOnClickListener(new OnClickListener() {

										public void onClick(View v) {
											File file2 = new File(filePath);
											Intent intent = new Intent(
													Intent.ACTION_VIEW);
											intent.setDataAndType(
													Uri.fromFile(file2),
													"video/mp4");// 设置路径
											startActivity(intent);
										}
									});
									ArrayList<Object> tag = new ArrayList<Object>();
									tag.add(0, layout);
									tag.add(1, filePath);
									delete.setTag(tag);
									item.setTag(tag);
									layout.addView(delete);
									layout.addView(item);
									layout_file.addView(layout);
									selectFileList.add(filePath);
									delete.setOnClickListener(deleteListener);
								}
							}
						}
					});
				}
			});
			dialog.show();
			break;
		case R.id.worksend_iv_send:// 发布按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.WorkSendToSbActivity2_send));
			String str_msgcontent = edt_input.getTextString();
			Log.d(TAG, size / (1024 * 1024) + "");
			if ("".equals(str_msgcontent)) {
				ToastUtil.showMessage(mContext, R.string.content_cannot_empty);
			} else {
				if (selectFileList.size() > 10) {
					ToastUtil.showMessage(mContext,
							R.string.noMoreThan_10_attachment);// 附件限制10个
				} else if (size > 10 * 1024 * 1024) {
					ToastUtil.showMessage(mContext,
							R.string.noMoreThan_10M_content);// 附件大小限制10M
				} else {
					RequestParams params = new RequestParams();
					params.addBodyParameter("unitclassgenCount", "0");
					params.addBodyParameter("talkcontent", str_msgcontent);
					params.addBodyParameter("SMSFlag",
							cb_ismsg.isChecked() ? "1" : "0");
					params.addBodyParameter("curunitid", myUnit.getTabIDStr());

					for (int i = 0; i < selectFileList.size(); i++) {
						params.addBodyParameter("ATTfileList" + i, new File(
								selectFileList.get(i)));
					}
					ArrayList<Object> post = new ArrayList<Object>();
					post.add(Constant.msgcenter_worksend_SendBtnClicked);
					post.add(function);// 选择的标题不同，发送不同的信息
					post.add(params);
					EventBusUtil.post(post);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 附件限制10个&&大小限制10M
	 * 
	 * @return true 在限制范围内，false 超出限制
	 */
	private boolean noMoreThanTen() {
		if (selectFileList.size() >= 10) {
			ToastUtil.showMessage(mContext, R.string.noMoreThan_10_attachment);// 附件限制10个
			return false;
		} else if (size > 10 * 1024 * 1024) {
			ToastUtil.showMessage(mContext, R.string.noMoreThan_10M_content);// 附件大小限制10M
			return false;
		}
		return true;
	}

	/**
	 * 比较数值current是否属于min-max之间
	 * 
	 * @param current
	 *            被比较的数
	 * @param min
	 *            小的数值
	 * @param max
	 *            大的数值
	 * @return 1 属于min-max之间；0否
	 */
	private boolean rangeInDefined(int current, int min, int max) {
		return Math.max(min, current) == Math.min(current, max);
	}

	/**
	 * 判断录音时间属于哪个区间，显示不同长度
	 * 
	 * @param time
	 *            时间
	 * @return 录音时间应该显示的宽度比例
	 */
	private int mtime(int time) {
		int a = 20;
		for (int i = 0; i < 20; i++) {
			boolean a1 = rangeInDefined(time, 1 * i, 6 * i);// 比较录音时间，最大设定为120S
			if (a1) {
				a = i;
				break;
			}
		}
		// Log.i("mtime", a + "");
		return a;
	}

	/**
	 * 播放录音
	 * 
	 * @param name 录音文件的路径
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			playingMediaName = name;
			try {
				mMediaPlayer.setDataSource(name);
				// mMediaPlayer.prepareAsync();
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showMessage(mContext, "播放异常,该录音可能无效");
			}
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work_change:// 切换单位
			DialogUtil.getInstance().cannleDialog();
			boolean changeUnit = (Boolean) list.get(1);
			if (changeUnit) {
				commMsg = false;
				SMSComm = false;
				getCommPerm = null;
				DialogUtil.getInstance().getDialog(
						mContext,
						mContext.getResources().getString(
								R.string.public_loading));
				DialogUtil.getInstance().setCanCancel(false);
				WorkSendToSbActivity2Controller.getInstance()
						.CommMsgRevicerUnitList();
				WorkSendToSbActivity2Controller.getInstance().SMSCommIndex();
			}
			break;
		case Constant.msgcenter_work_GetCommPerm:// 取发送事务权限
			DialogUtil.getInstance().cannleDialog();
			getCommPerm = (GetCommPerm) list.get(1);
			UnitCommRight = getCommPerm.isUnitCommRight();// 同级单位发送事务 权限
			if (getCommPerm.isUnitCommRight()) {// 有同级单位发送事务权限，显示内部事务
				unitFragment
						.setImageResource(R.drawable.icon_worksend_unitfragment_selected);
				genFragment
						.setImageResource(R.drawable.icon_worksend_genfragment_unselect);
				otherUnitFragment
						.setImageResource(R.drawable.icon_worksend_otherunitfragment_unselect);
				function = Function.UNIT;
				WorkSendToSbActivity2Controller.getInstance()
						.switchUnitFragment();
				unitFragment.setVisibility(0);
			} else {// 无同级单位发送事务权限，隐藏内部事务
				unitFragment.setVisibility(8);
				genFragment
						.setImageResource(R.drawable.icon_worksend_genfragment_selected);
				otherUnitFragment
						.setImageResource(R.drawable.icon_worksend_otherunitfragment_unselect);
				function = Function.GEN;
				WorkSendToSbActivity2Controller.getInstance()
						.switchGenFragment();
			}
			if (getCommPerm.isParentCommRight()
					|| getCommPerm.isSubUnitCommRight()) {// 有上级或下级单位发送事务权限，显示多单位事务
				otherUnitFragment.setVisibility(0);
			} else {
				otherUnitFragment.setVisibility(8);
			}
			WorkSendToSbActivity2Controller.getInstance()
					.CommMsgRevicerUnitList();
			break;
		case Constant.msgcenter_work_CommMsgRevicerUnitList:// 获取事务信息接收单位列表
			DialogUtil.getInstance().cannleDialog();
			commMsg = true;// 获取到取接收人列表的回调
			mHandler.sendEmptyMessage(102);
			String commMsgRevicerUnitList_str = (String) list.get(1);
			CommMsgRevicerUnitList commMsgRevicerUnitList = GsonUtil
					.GsonToObject(commMsgRevicerUnitList_str,
							CommMsgRevicerUnitList.class);
			if (getCommPerm.isParentCommRight()) {// 有上级单位
				ParentUnits = commMsgRevicerUnitList.getUnitParents();
			} else {
				ParentUnits = null;
			}
			if (getCommPerm.isSubUnitCommRight()) {// 有下级单位
				SubUnits = commMsgRevicerUnitList.getSubUnits();
			} else {
				SubUnits = null;
			}
			if (getCommPerm.isUnitCommRight()) {// 有同级单位
				myUnit = commMsgRevicerUnitList.getMyUnit();
			} else {
				myUnit = null;
			}
			UnitClass = commMsgRevicerUnitList.getUnitClass();
			Log.i("UnitClass-CommMsgRevicerUnitList", UnitClass + "");
			noClassChangeRole();
			if (myUnit != null) {// 有同级单位
				RequestParams params = new RequestParams();
				params.addBodyParameter("unitId",
						String.valueOf(myUnit.getTabID()));// 单位ID
				params.addBodyParameter("flag",
						String.valueOf(myUnit.getFlag()));// 单位标志
				WorkSendToSbActivity2Controller.getInstance().GetUnitRevicer(
						params);
			}
			break;
		case Constant.msgcenter_work_SMSCommIndex:// 获取短信直通车权限数据
			DialogUtil.getInstance().cannleDialog();
			SMSComm = true;// 获取到短信直通车权限数据的回调
			mHandler.sendEmptyMessage(102);
			SMSList = (List<SMSTreeUnitID>) list.get(1);
			Log.i("sucSMS-SMSCommIndex", SMSComm + "");
			noClassChangeRole();
			break;
		case Constant.msgcenter_work_CreateCommMsg:
			DialogUtil.getInstance().cannleDialog();
			int result = (Integer) list.get(1);
			if (result == 0) {
				ToastUtil.showMessage(mContext, R.string.send_failed);
			} else if (result == 1) {// 发送成功
				edt_input.setText("");
				try {
					selectFileList.clear();
					size = 0;
					layout_file.removeAllViews();
					// layout_photofile.removeAllViews();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ToastUtil.showMessage(mContext, R.string.send_success);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取了单位接收人，短信直通车的回调后，单位接收人或者短信直通车无相应数据，提示‘当前身份无班级，请切换身份’
	 */
	private void noClassChangeRole() {
		if (commMsg && SMSComm && !getCommPerm.isUnitCommRight()) {
			if (SMSList != null || UnitClass != null) {
				// SMSList 校园通知，多校家长；UnitClass 班级通知，个性表现
				if (UnitClass != null) {
					if (UnitClass.size() == 0 && SMSList == null) {// 有可能出现设置了权限，但是未分配班级或者接收人的情况
						ToastUtil.showMessage(mContext,
								R.string.currentRole_hasNoClass_changeRole);
					}
				}
				if (SMSList != null) {
					if (SMSList.size() == 0 && UnitClass == null) {// 有可能出现设置了权限，但是未分配班级或者接收人的情况
						ToastUtil.showMessage(mContext,
								R.string.currentRole_hasNoClass_changeRole);
					}
				}
			} else {
				ToastUtil.showMessage(mContext,
						R.string.currentRole_hasNoClass_changeRole);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 10:// 附件从本地选择
			if (data != null) {
				String path = data.getExtras().getString("path");
				// size = 0;
				// for (int i = 0; i < selectFileList.size(); i++) {
				// File file = new File(selectFileList.get(i));
				// size += file.length();
				// }
				File file = new File(path);
				size += file.length();
				System.out.println("------------size:" + size);
				if (size > 10 * 1024 * 1024) {
					ToastUtil.showMessage(mContext,
							R.string.noMoreThan_10M_content);
				} else {
					LinearLayout layout = new LinearLayout(mContext);
					ImageView delete = new ImageView(mContext);
					delete.setPadding(5, 5, 5, 5);
					delete.setImageResource(R.drawable.btn_sendmsg_delete);
					TextView item = new TextView(mContext);
					String[] paths = path.split("\\/");
					String[] names = paths[paths.length - 1].split("\\.");
					// if (names.length == 2) {
					if (names[names.length - 1].equals("jpg")
							|| names[names.length - 1].equals("jpeg")
							|| names[names.length - 1].equals("png")
							|| names[names.length - 1].equals("bmp")) {
						item.setLayoutParams(new LinearLayout.LayoutParams(
								(int) getResources().getDimension(
										R.dimen.px_to_dip_45),
								(int) getResources().getDimension(
										R.dimen.px_to_dip_45)));
						JSYApplication.getInstance().bitmapUtils.display(item,
								path);
					} else {
						item.setText(paths[paths.length - 1]);
					}
					// }else{
					// item.setText(paths[paths.length-1]);
					// }
					item.setTag(path);
					ArrayList<Object> tag = new ArrayList<Object>();
					tag.add(0, layout);
					tag.add(1, path);
					delete.setTag(tag);
					item.setTag(tag);
					layout.addView(delete);
					layout.addView(item);
					layout_file.addView(layout);
					selectFileList.add(path);

					delete.setOnClickListener(deleteListener);
					// item.setOnClickListener(deleteListener);//添加附件，点击本地图片删除
				}
			}
			break;
		case 11:// 相机拍照
			// size = 0;
			// for (int i = 0; i < selectFileList.size(); i++) {
			// File file = new File(selectFileList.get(i));
			// size += file.length();
			// }
			file = null;
			try {
				DialogUtil.getInstance().getDialog(mContext, R.string.loading);
				createThread();
			} catch (Exception e) {
				e.printStackTrace();
				PictureUtils.DeleteImage(mContext, photoPath);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 创建线程，处理图片
	 */
	private void createThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					bitmap = PictureUtils.getbitmapFromURL(photoPath);
					if (bitmap == null) {
						PictureUtils.DeleteImage(mContext, photoPath);
						DialogUtil.getInstance().cannleDialog();
						file = null;
					} else {
						file = PictureUtils.saveBitmapFile(bitmap, photoPath);
					}
					handler.sendEmptyMessage(789);
				}
			});
		}
		thread.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 789) {
				DialogUtil.getInstance().cannleDialog();
				thread.interrupt();
				thread = null;
				if (file != null) {
					updateFile(file);
					// } else {
					// ToastUtil.showMessage(mContext, "文件为空");
				}
			}
		}

		private void updateFile(File file) {
			if (file != null && file.length() > 0) {
				size += file.length();
				if (size > 10 * 1024 * 1024) {
					ToastUtil.showMessage(mContext,
							R.string.noMoreThan_10M_content);
					size -= file.length();
				} else {
					LinearLayout layout = new LinearLayout(mContext);
					ImageView delete = new ImageView(mContext);
					delete.setPadding(5, 5, 5, 5);
					delete.setImageResource(R.drawable.btn_sendmsg_delete);
					ImageView item = new ImageView(mContext);
					item.setLayoutParams(new LinearLayout.LayoutParams(
							(int) getResources().getDimension(
									R.dimen.px_to_dip_45), (int) getResources()
									.getDimension(R.dimen.px_to_dip_45)));// 图片缩略图大小
					JSYApplication.getInstance().bitmap
							.display(item, photoPath);
					item.setTag(photoPath);
					ArrayList<Object> tag = new ArrayList<Object>();
					tag.add(0, layout);
					tag.add(1, photoPath);
					delete.setTag(tag);
					item.setTag(tag);
					layout.addView(delete);
					layout.addView(item);
					layout_file.addView(layout);
					// layout_photofile.addView(layout);
					selectFileList.add(photoPath);
					delete.setOnClickListener(deleteListener);
					// item.setOnClickListener(deleteListener);//添加附件，点击拍照图片删除
				}
			}
		}

	};
	/**
	 * 删除附件
	 */
	OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			@SuppressWarnings("unchecked")
			ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
			final LinearLayout layout = (LinearLayout) tag.get(0);
			final String path = (String) tag.get(1);
			Builder deleDialog = new AlertDialog.Builder(mContext);// 删除时增加了询问
			deleDialog.setTitle("提示");
			deleDialog.setMessage("确定删除？");
			deleDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							layout.removeAllViews();
							for (int i = 0; i < selectFileList.size(); i++) {
								if (path.equals(selectFileList.get(i))) {
									selectFileList.remove(i);
									size -= new File(path).length();
								}
							}
							if (path.equals(playingMediaName)) {
								mMediaPlayer.stop();
							}
							ToastUtil.showMessage(mContext, "删除成功");
						}
					});
			deleDialog.setNegativeButton("取消", null);
			deleDialog.show();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
