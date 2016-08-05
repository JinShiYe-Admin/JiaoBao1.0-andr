package com.jsy_jiaobao.main.leave;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.LeaveUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveDetail;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 查询请假详情
 * 
 * @author admin
 * 
 */

public class LeaveDetailsActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ScrollView> {
	private final static int TYPE_STU = 0;
	private final static int TYPE_TEA = 1;
	private int mType;
	private int leaveId;// 假条记录Id
	private int position;
	private int leaveRole;// 1一审
	private int leaveType;// 0查看1审核

	private boolean del = false;
	private boolean change = false;
	private boolean reflash = false;
	private String mUserName;
	private LeaveDetail choseLeave;
	private ArrayList<String> leaveNoteStu;
	private ArrayList<String> leaveNote;

	private Context mContext;
	private LinearLayout ly_leave_details;
	private RelativeLayout rl_buttons;
	private PullToRefreshScrollView refreshView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		leaveId = getIntent().getIntExtra("LeaveID", 0);
		mContext = this;
		mUserName = sp.getString("UserName", null);
		leaveRole = getIntent().getIntExtra("CheckRole", 0);
		leaveType = getIntent().getIntExtra("CheckType", 0);
		position = getIntent().getIntExtra("Position", 0);
		setActionBarTitle("请假详情");
		initViews();
	}

	private void initViews() {
		setContentLayout(R.layout.leave_activity_leavedetails);
		LeaveDetailsActivityControl.getInstance().setContext(this);
		ly_leave_details = (LinearLayout) findViewById(R.id.ly_leave_details);
		rl_buttons = (RelativeLayout) findViewById(R.id.ry_buttons);
		Button btn_delete;
		btn_delete = (Button) findViewById(R.id.btn_del);
		Button btn_updata;
		btn_updata = (Button) findViewById(R.id.btn_change);
		refreshView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		refreshView.setOnRefreshListener(this);
		refreshView.setMode(Mode.PULL_FROM_START);

		leaveNote = new ArrayList<>();
		leaveNoteStu = new ArrayList<>();
		btn_delete.setOnClickListener(this);
		btn_updata.setOnClickListener(this);
		if (leaveType == 1) {
			rl_buttons.setVisibility(View.GONE);
		} else {
			rl_buttons.setVisibility(View.VISIBLE);
		}
		LeaveDetailsActivityControl.getInstance().GetLeaveModel(leaveId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		LeaveDetailsActivityControl.getInstance().GetLeaveModel(leaveId);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		del = false;
		change = false;
		super.onPause();
		MobclickAgent.onPause(mContext);
	}

	private void initData() {
		ly_leave_details.removeAllViews();
		int ApproveLevelStd = sp.getInt("ApproveLevelStd", 0);
		int ApproveLevel = sp.getInt("ApproveLevel", 0);
		String LevelNoteStdA = sp.getString("LevelNoteStdA", null) == null ? "一审"
				: sp.getString("LevelNoteStdA", null);
		String LevelNoteStdB = sp.getString("LevelNoteStdB", null) == null ? "二审"
				: sp.getString("LevelNoteStdB", null);
		String LevelNoteStdC = sp.getString("LevelNoteStdC", null) == null ? "三审"
				: sp.getString("LevelNoteStdC", null);
		String LevelNoteStdD = sp.getString("LevelNoteStdD", null) == null ? "四审"
				: sp.getString("LevelNoteStdD", null);
		String LevelNoteStdE = sp.getString("LevelNoteStdE", null) == null ? "五审"
				: sp.getString("LevelNoteStdE", null);
		leaveNoteStu.add(LevelNoteStdA);
		leaveNoteStu.add(LevelNoteStdB);
		leaveNoteStu.add(LevelNoteStdC);
		leaveNoteStu.add(LevelNoteStdD);
		leaveNoteStu.add(LevelNoteStdE);
		String LevelNoteA = sp.getString("LevelNoteA", null) == null ? "一审"
				: sp.getString("LevelNoteA", null);
		String LevelNoteB = sp.getString("LevelNoteB", null) == null ? "二审"
				: sp.getString("LevelNoteB", null);
		String LevelNoteC = sp.getString("LevelNoteC", null) == null ? "三审"
				: sp.getString("LevelNoteC", null);
		String LevelNoteD = sp.getString("LevelNoteD", null) == null ? "四审"
				: sp.getString("LevelNoteD", null);
		String LevelNoteE = sp.getString("LevelNoteE", null) == null ? "五审"
				: sp.getString("LevelNoteE", null);
		leaveNote.add(LevelNoteA);
		leaveNote.add(LevelNoteB);
		leaveNote.add(LevelNoteC);
		leaveNote.add(LevelNoteD);
		leaveNote.add(LevelNoteE);
		leaveNoteStu.subList(0, ApproveLevelStd);
		leaveNote.subList(0, ApproveLevel);
		switch (mType) {
		case TYPE_STU://审核学生
			LeaveUtils.addLeaveDetails(mContext, ly_leave_details, choseLeave,
					leaveType, leaveRole, leaveId, leaveNoteStu);
			break;
		case TYPE_TEA://审核教职工
			LeaveUtils.addLeaveDetails(mContext, ly_leave_details, choseLeave,
					leaveType, leaveRole, leaveId, leaveNote);
			break;
		default:
			break;
		}
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case LeaveConstant.leave_GetLeaveModel:
			DialogUtil.getInstance().cannleDialog();
			refreshView.onRefreshComplete();
			choseLeave = (LeaveDetail) list.get(1);
			mType = choseLeave.getManType();
			if (choseLeave != null ) {
				for (int i = 0; i < choseLeave.getTimeList().size(); i++) {
					String startTime = choseLeave.getTimeList().get(i)
							.getSdate().replace("T", " ");
					String endTime = choseLeave.getTimeList().get(i).getEdate()
							.replace("T", " ");
					startTime = startTime.substring(0, startTime.length() - 3);
					endTime = endTime.substring(0, endTime.length() - 3);
					choseLeave.getTimeList().get(i).setSdate(startTime);
					choseLeave.getTimeList().get(i).setEdate(endTime);
				}
			}
			boolean state;// 判断假条状态，等待中则为true
			if (choseLeave != null && choseLeave.getStatusStr().equals("等待中")
					&& leaveType == 0) {// 请假记录不为空并且记录状态为等待中则是可见的

				state = true;// 状态为等待中
				rl_buttons.setVisibility(View.VISIBLE);
			} else {
				state = false;// 状态不是等待中
				rl_buttons.setVisibility(View.GONE);
			}
			if (del) {
				if (state) {
					LeaveDetailsActivityControl.getInstance().DeleteLeaveModel(
							leaveId);
				} else {
					ToastUtil.showMessage(mContext, "已进入审核状态");
					initData();
				}
			}
			if (change) {// 修改假条
				if (state) {// 状态是否是等待中
					Intent intent = new Intent(mContext,
							ModifyLeaveActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("ChoseLeave", choseLeave);
					intent.putExtras(mBundle);
					startActivity(intent);
				} else {
					ToastUtil.showMessage(mContext, "已进入审核状态");
					initData();
					rl_buttons.setVisibility(View.GONE);
				}
			}
			initData();
			break;
		case LeaveConstant.leave_DeleteLeaveTime:
			DialogUtil.getInstance().cannleDialog();
			boolean f = (Boolean) list.get(1);
			if (f) {
				ToastUtil.showMessage(mContext, "删除成功");

			} else {
				ToastUtil.showMessage(mContext, "删除失败");
			}
			break;
		case LeaveConstant.leave_GateWriteLeaveTime:
			DialogUtil.getInstance().cannleDialog();
			int type = (Integer) list.get(1);
			LeaveTime lTime = (LeaveTime) list.get(2);
			LeaveDetailsActivityControl.getInstance().UpdateGateInfol(
					lTime.getTabID(), mUserName, type);
			break;
		case LeaveConstant.leave_UpdateGateInfo:
			DialogUtil.getInstance().cannleDialog();
			boolean d = (Boolean) list.get(1);
			if (d) {
				ToastUtil.showMessage(mContext, "门卫已签");
			} else {
				ToastUtil.showMessage(mContext, "签字失败，请重签");
			}
			break;
		case LeaveConstant.leave_DeleteLeaveModel:
			boolean b = (Boolean) list.get(1);
			if (b) {
				ToastUtil.showMessage(mContext, "删除假条成功");
				Intent i = new Intent();
				i.putExtra("Position", position);
				setResult(LeaveConstant.leave_postDelelteLeaveId, i);
				finish();
			} else {
				ToastUtil.showMessage(mContext, "删除假条失败");
			}
		case 999:
			int a = (Integer) list.get(1);
			if (a == 999) {
				DialogUtil.getInstance().cannleDialog();
				refreshView.onRefreshComplete();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_del:// 删除按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.LeaveDetailsActivity_btn_del));
			Builder delDialog = new AlertDialog.Builder(this);
			delDialog.setTitle("提示");
			delDialog.setMessage("确定删除？");
			delDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							LeaveDetailsActivityControl.getInstance()
									.GetLeaveModel(leaveId);
							del = true;
						}
					});
			delDialog.setNegativeButton("取消", null);
			delDialog.show();
			break;
		case R.id.btn_change:// 修改按钮
			MobclickAgent.onEvent(
					mContext,
					getResources().getString(
							R.string.LeaveDetailsActivity_btn_change));
			LeaveDetailsActivityControl.getInstance().GetLeaveModel(leaveId);
			change = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		LeaveDetailsActivityControl.getInstance().GetLeaveModel(leaveId);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
	}

	@Override
	public void onPullPageChanging(boolean isChanging) {
	}

	/**
	 * 审核后返回true，刷新列表
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		Log.i("onActivityResult", arg0 + "-" + arg1);
		if (arg1 == 998) {
			reflash = true;
		}
	}

	/**
	 * 审核后返回true，刷新列表
	 */
	@Override
	public void setResultForLastActivity() {
		super.setResultForLastActivity();
		if (reflash) {
			Intent i = new Intent();
			setResult(998, i);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (reflash) {
				Intent i = new Intent();
				setResult(998, i);
			}
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}