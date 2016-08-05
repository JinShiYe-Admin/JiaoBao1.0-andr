package com.jsy_jiaobao.main.leave;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.LeaveUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.GateQueryLeaveModel;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveDetail;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.umeng.analytics.MobclickAgent;

/**
 * 功能说明：请假系统，门卫签字
 * 
 * @author MSL
 * 
 */
public class GateCheckDetails extends BaseActivity implements OnClickListener {
	private int tabId;// 时间段id
	private String userName;// 签字人姓名
	private GateQueryLeaveModel gateLeave;// 门卫审核的假条

	private Context mContext;// 上下文
	private TextView tv_WriteTime;// 发起时间
	private TextView tv_ManName;// 请假人姓名
	private TextView tv_LeaveType;// 请假类型
	private TextView tv_Sdate;// 请假开始时间
	private TextView tv_Edate;// 请假结束时间
	private TextView tv_LWriterName;// 离校登记人（门卫）
	private TextView tv_LeaveTime;// 离校时间
	private TextView tv_CWriterName;// 返校登记人(门卫）
	private TextView tv_ComeTime;// 返校时间
	private Button bt_GateSignLeave;// 离校门卫签字
	private Button bt_GateSignBack;// 回校门卫签字

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		gateLeave = (GateQueryLeaveModel) getIntent().getSerializableExtra(
				"ChoseLeave");
		setActionBarTitle("门卫签字");
		initViews();
		EventBusUtil.register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
		MobclickAgent.onPause(mContext);
	}

	private void initViews() {
		CheckerActivityControler.getInstance().setContext(this);
		setContentLayout(R.layout.leave_gate_checkdetails);
		tv_WriteTime = (TextView) findViewById(R.id.tv_writeTime);
		tv_ManName = (TextView) findViewById(R.id.tv_manName);
		tv_LeaveType = (TextView) findViewById(R.id.tv_leaveType);
		tv_Sdate = (TextView) findViewById(R.id.tv_startTime);
		tv_Edate = (TextView) findViewById(R.id.tv_endTime);
		tv_LWriterName = (TextView) findViewById(R.id.tv_leaveSign);
		tv_LeaveTime = (TextView) findViewById(R.id.tv_leaveTime);
		tv_CWriterName = (TextView) findViewById(R.id.tv_backSign);
		tv_ComeTime = (TextView) findViewById(R.id.tv_backTime);
		bt_GateSignLeave = (Button) findViewById(R.id.bt_gateSignLeave);
		bt_GateSignBack = (Button) findViewById(R.id.bt_gateSignBack);
		bt_GateSignLeave.setOnClickListener(this);
		bt_GateSignBack.setOnClickListener(this);
		initData();
	}

	private void initData() {
		tv_WriteTime.setText(replaceDate(gateLeave.getWriteDate()));// 发起时间
		tv_ManName.setText(gateLeave.getManName());// 请假人姓名
		tv_LeaveType.setText(gateLeave.getLeaveType());// 请假类型
		tv_Sdate.setText(replaceDate(gateLeave.getSdate()));// 开始时间
		tv_Edate.setText(replaceDate(gateLeave.getEdate()));// 结束时间
		tabId = gateLeave.getTabID();
		userName = BaseActivity.sp.getString("UserName", null);
		if (gateLeave.getLeaveTime() != null
				&& !gateLeave.getLeaveTime().equals("")) {// 有离校时间
			if (gateLeave.getComeTime() != null
					&& !gateLeave.getComeTime().equals("")) {// 有返校时间
				tv_LeaveTime.setText(replaceDate(gateLeave.getLeaveTime()));
				tv_LWriterName.setText(gateLeave.getLWriterName());
				tv_ComeTime.setText(replaceDate(gateLeave.getComeTime()));
				tv_CWriterName.setText(gateLeave.getCWriterName());
				bt_GateSignLeave.setVisibility(View.GONE);
				bt_GateSignBack.setVisibility(View.GONE);
			} else {
				tv_LeaveTime.setText(replaceDate(gateLeave.getLeaveTime()));
				tv_LWriterName.setText(gateLeave.getLWriterName());
				bt_GateSignLeave.setVisibility(View.GONE);
			}
		} else {
			bt_GateSignBack.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_gateSignLeave:// 离校门卫签字
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.GateCheckDetails_Leave));
			CheckerActivityControler.getInstance().UpdateGateInfo(tabId,
					userName, 0);
			break;
		case R.id.bt_gateSignBack:// 回校门卫签字
			MobclickAgent.onEvent(mContext,
					getResources().getString(R.string.GateCheckDetails_Back));
			CheckerActivityControler.getInstance().UpdateGateInfo(tabId,
					userName, 1);
			break;
		default:
			break;
		}
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		if (tag == LeaveConstant.leave_UpdateGateInfo) {// 门卫签字成功
			DialogUtil.getInstance().cannleDialog();
			ToastUtil.showMessage(mContext, "签字成功");
			Intent i = new Intent();
			setResult(997, i);
			finish();
		}
	}

	/**
	 * 替换日期格式
	 * 
	 * @param date
	 * @return
	 */
	private static String replaceDate(String date) {
		String reDate = date;
		reDate = reDate.replaceFirst("-", "年");
		reDate = reDate.replaceFirst("-", "月");
		reDate = reDate.replaceFirst("T", "号 ");
		reDate = reDate.substring(0, reDate.length() - 3);
		return reDate;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}