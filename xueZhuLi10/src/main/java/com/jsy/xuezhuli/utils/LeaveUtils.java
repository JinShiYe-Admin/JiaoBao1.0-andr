package com.jsy.xuezhuli.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.leave.CheckerAgreeOrActivity;
import com.jsy_jiaobao.po.leave.DateTimePickerDialog;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveDetail;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.jsy_jiaobao.po.sys.GetRevicerList;
import com.umeng.analytics.MobclickAgent;

public class LeaveUtils {
	private static String defaultStartTime;
	private static String defaultEndTime;
	private static Context mContext;
	private final static int MAN_STU = 0;
	private final static int MAN_TEA = 1;

	/**
	 * 请假详情
	 * 
	 * @param context
	 * @param leaveDetailLayout
	 * @param mData
	 * @param leaveFlag
	 * @param leaveRole
	 * @param leaveId
	 * @param leaveNoteList
	 */
	public static void addLeaveDetails(final Context context,
			final LinearLayout leaveDetailLayout, final LeaveDetail mData,
			final int leaveFlag, final int leaveRole, final int leaveId,
			final ArrayList<String> leaveNoteList) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		mContext = context;
		final View view = layoutInflater.inflate(
				R.layout.leave_layout_leave_details, null);
		TextView tv_leaver = (TextView) view.findViewById(R.id.tv_leaver);
		TextView tv_writer = (TextView) view.findViewById(R.id.tv_writer);
		TextView tv_createTime = (TextView) view
				.findViewById(R.id.tv_createTime);
		TextView tv_reasonType = (TextView) view
				.findViewById(R.id.tv_reasonType);
		TextView tv_reasonDetail = (TextView) view.findViewById(R.id.tv_reason);
		LinearLayout ly_time = (LinearLayout) view.findViewById(R.id.ly_time);
		LinearLayout ly_approval = (LinearLayout) view
				.findViewById(R.id.ly_approval);
		tv_leaver.setText(mData.getManName());
		tv_writer.setText(mData.getWriter());
		String writeDate = mData.getWriteDate();
		writeDate = writeDate.substring(0, writeDate.length() - 3);
		tv_createTime.setText(replaceDate(writeDate));
		tv_reasonType.setText(mData.getLeaveType());
		tv_reasonDetail.setText(mData.getLeaveReason());
		ArrayList<LeaveTime> timeList = mData.getTimeList();
		if (timeList != null) {
			LeaveUtils.addAllTimeLayout(mContext, ly_time, timeList, leaveRole);
		}
		int ApproveStatus = mData.getApproveStatus();
		int ApproveStatus1 = mData.getApproveStatus1();
		int ApproveStatus2 = mData.getApproveStatus2();
		int ApproveStatus3 = mData.getApproveStatus3();
		int ApproveStatus4 = mData.getApproveStatus4();
		ArrayList<Integer> statusList = new ArrayList<Integer>();
		try {
			statusList.add(ApproveStatus);
			statusList.add(ApproveStatus1);
			statusList.add(ApproveStatus2);
			statusList.add(ApproveStatus3);
			statusList.add(ApproveStatus4);
			LeaveUtils.addApprovalLayout(mContext, ly_approval, statusList,
					leaveFlag, leaveRole, leaveId, mData, leaveNoteList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		leaveDetailLayout.addView(view);
	}

	/**
	 * 添加默认时间段
	 * 
	 * @param context
	 * @param layoutTime
	 * @param timeList
	 */
	public static void addTimeLayout(final Context context,
			final LinearLayout layoutTime, final List<LeaveTime> timeList) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		mContext = context;
		final View view = layoutInflater.inflate(
				R.layout.leave_layout_leave_time, null);
		final TextView start = (TextView) view
				.findViewById(R.id.leave_time_start);
		final TextView end = (TextView) view.findViewById(R.id.leave_time_end);
		final LeaveTime map = new LeaveTime();
		getDefaultTime();
		start.setText(defaultStartTime);
		end.setText(defaultEndTime);
		map.setEdate(defaultEndTime);
		map.setSdate(defaultStartTime);
		timeList.add(map);
		ImageView delete = (ImageView) view
				.findViewById(R.id.leave_time_delete);
		start.setOnClickListener(new View.OnClickListener() {// 修改开始时间
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.addTimeLayout_start));
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
						(Activity) context, map.getSdate());
				dateTimePicKDialog.dateTimePicKDialog(start, map, 1);
			}
		});
		end.setOnClickListener(new View.OnClickListener() {// 修改结束时间
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.addTimeLayout_end));
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
						(Activity) context, map.getEdate());
				dateTimePicKDialog.dateTimePicKDialog(end, map, 2);
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {// 删除时间段
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.addTimeLayout_delete));
				try {
					Builder delDialog = new AlertDialog.Builder(context);
					delDialog.setTitle("提示");
					delDialog.setMessage("确定删除？");
					delDialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									if (timeList.size() == 1) {
										ToastUtil.showMessage(mContext,
												"必须有一个请假时间段");
									} else {
										timeList.remove(map);
										layoutTime.removeView(view);
									}
								}
							});
					delDialog.setNegativeButton("取消", null);
					delDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		layoutTime.addView(view);
	}

	/**
	 * 请假时间段区域
	 * 
	 * @param context
	 * @param layoutTime
	 * @param timeList
	 * @param leaveRole
	 */
	public static void addAllTimeLayout(final Context context,
			final LinearLayout layoutTime, final ArrayList<LeaveTime> timeList,
			final int leaveRole) {
		mContext = context;
		for (int i = 0; i < timeList.size(); i++) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			mContext = context;
			try {
				final View view = layoutInflater.inflate(
						R.layout.leave_layout_leavedetails_time, null);
				final TextView tv_start = (TextView) view
						.findViewById(R.id.tv_startTime);
				final TextView tv_end = (TextView) view
						.findViewById(R.id.tv_endTime);
				final TextView tv_leave = (TextView) view
						.findViewById(R.id.tv_leaveTime);
				final TextView tv_back = (TextView) view
						.findViewById(R.id.tv_backTime);
				final TextView tv_leaveGate = (TextView) view
						.findViewById(R.id.tv_leave_gateGuard);
				final TextView tv_backGate = (TextView) view
						.findViewById(R.id.tv_backGateGuard);
				// final Button btn_leave = (Button) view
				// .findViewById(R.id.btn_leave_sign);
				// final Button btn_back = (Button) view
				// .findViewById(R.id.btn_back_sign);
				final LeaveTime leaveTime = timeList.get(i);
				String startDate = leaveTime.getSdate();
				String endDate = leaveTime.getEdate();
				tv_start.setText(replaceDate(startDate));
				tv_end.setText(replaceDate(endDate));
				if (leaveTime.getLeaveTime() != null
						&& leaveTime.getLeaveTime().length() != 0) {
					String mLeaveTime = leaveTime.getLeaveTime();
					mLeaveTime = replaceDate(mLeaveTime);
					mLeaveTime = mLeaveTime.substring(0,
							mLeaveTime.length() - 3);
					tv_leave.setText(mLeaveTime);
					tv_leaveGate.setText(leaveTime.getLWriterName());
				}

				if (leaveTime.getComeTime() != null
						&& leaveTime.getComeTime().length() != 0) {
					String mComeTime = leaveTime.getComeTime();
					mComeTime = replaceDate(mComeTime);
					mComeTime = mComeTime.substring(0, mComeTime.length() - 3);
					tv_back.setText(mComeTime);
					tv_backGate.setText(leaveTime.getCWriterName());
				}
				// btn_leave.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// ArrayList<Object> post = new ArrayList<Object>();
				// post.add(LeaveConstant.leave_GateWriteLeaveTime);
				// post.add(0);
				// post.add(leaveTime);
				// EventBusUtil.post(post);
				// DialogUtil.getInstance().getDialog(context,
				// "正向服务器发送请求,请稍候");
				// }
				// });
				// btn_back.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// ArrayList<Object> post = new ArrayList<Object>();
				// post.add(LeaveConstant.leave_GateWriteLeaveTime);
				// post.add(1);
				// post.add(leaveTime);
				// EventBusUtil.post(post);
				// DialogUtil.getInstance().getDialog(context,
				// "正向服务器发送请求,请稍候");
				// }
				// });
				layoutTime.addView(view);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 审核状态区域
	 * 
	 * @param context
	 * @param layoutAproval
	 * @param list
	 * @param leaveType
	 * @param leaveRole
	 * @param leaveId
	 * @param leaveNoteList
	 */
	public static void addApprovalLayout(final Context context,
			final LinearLayout layoutAproval, final ArrayList<Integer> list,
			int leaveType, final int leaveRole, final int leaveId,
			final LeaveDetail mData, ArrayList<String> leaveNoteList) {
		// final String[] rag = { "一审:", "二审:", "三审:", "四审:", "五审:" };
		for (int i = 0; i < list.size(); i++) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			mContext = context;
			final View view = layoutInflater.inflate(
					R.layout.leave_layout_approval, null);
			final TextView name = (TextView) view
					.findViewById(R.id.approval_name);
			final TextView statusContent = (TextView) view
					.findViewById(R.id.approval_content);
			final TextView note = (TextView) view
					.findViewById(R.id.approval_note);
			final Button btn_check = (Button) view.findViewById(R.id.btn_check);
			btn_check.setVisibility(View.VISIBLE);
			statusContent.setVisibility(View.VISIBLE);
			btn_check.setOnClickListener(new OnClickListener() {//审核
				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, mContext.getResources()
							.getString(R.string.addApprovalLayout_btn_check));
					Intent i = new Intent(mContext,
							CheckerAgreeOrActivity.class);
					i.putExtra("CheckRole", leaveRole);
					i.putExtra("LeaveId", leaveId);
					i.putExtra("ManName", mData.getManName());
					((Activity) mContext).startActivityForResult(i, 0);
				}
			});
			name.setText(leaveNoteList.get(i));
			switch (list.get(i)) {//显示状态
			case 0:
				statusContent.setText("等待中");
				if (leaveType == 0) {
					btn_check.setVisibility(View.GONE);
				} else if (leaveType == 1) {
					if (leaveRole == i + 1) {
						statusContent.setVisibility(View.GONE);
						btn_check.setVisibility(View.VISIBLE);
					} else {
						statusContent.setVisibility(View.VISIBLE);
						btn_check.setVisibility(View.GONE);
					}
				}
				layoutAproval.addView(view);
				break;
			case 1:
				statusContent.setText("通过");
				layoutAproval.addView(view);
				btn_check.setVisibility(View.GONE);
				break;
			case 2:
				statusContent.setText("拒绝");
				btn_check.setVisibility(View.GONE);
				layoutAproval.addView(view);
			default:
				break;
			}
			switch (i) {//显示批注
			case 0:
				if (mData.getApproveNote() != null) {
					note.setText(mData.getApproveNote());
				}
				break;
			case 1:
				if (mData.getApproveNote1() != null) {
					note.setText(mData.getApproveNote1());
				}
				break;
			case 2:
				if (mData.getApproveNote2() != null) {
					note.setText(mData.getApproveNote2());
				}
				break;
			case 3:
				if (mData.getApproveNote3() != null) {
					note.setText(mData.getApproveNote3());
				}
				break;
			case 4:
				if (mData.getApproveNote4() != null) {
					note.setText(mData.getApproveNote4());
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 修改假条，生成记录中的时间段
	 * 
	 * @param context
	 * @param layoutTime
	 * @param timeList
	 */
	public static void modfiyTimeLayout(final Context context,
			final LinearLayout layoutTime, final ArrayList<LeaveTime> timeList) {

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		mContext = context;
		final View view = layoutInflater.inflate(
				R.layout.leave_layout_leave_time, null);
		final TextView start = (TextView) view
				.findViewById(R.id.leave_time_start);
		final TextView end = (TextView) view.findViewById(R.id.leave_time_end);
		final LeaveTime map = new LeaveTime();
		start.setText(timeList.get(timeList.size() - 1).getSdate());
		end.setText(timeList.get(timeList.size() - 1).getEdate());
		map.setEdate(timeList.get(timeList.size() - 1).getEdate());
		map.setSdate(timeList.get(timeList.size() - 1).getSdate());
		map.setTabID(timeList.get(timeList.size() - 1).getTabID());
		timeList.add(map);
		timeList.remove(timeList.size() - 2);
		ImageView delete = (ImageView) view
				.findViewById(R.id.leave_time_delete);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.modfiyTimeLayout_start));
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
						(Activity) context, map.getSdate());
				dateTimePicKDialog.dateTimePicKDialog(start, map, 1);
			}
		});
		end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.modfiyTimeLayout_end));
				DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
						(Activity) context, map.getEdate());
				dateTimePicKDialog.dateTimePicKDialog(end, map, 2);
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.modfiyTimeLayout_delete));
				try {
					Builder delDialog = new AlertDialog.Builder(context);
					delDialog.setTitle("提示");
					delDialog.setMessage("确定删除？");
					delDialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									if (timeList.size() == 1) {
										ToastUtil.showMessage(mContext,
												"必须有一个请假时间段");
									} else {
										timeList.remove(map);
										layoutTime.removeView(view);
									}

								}
							});
					delDialog.setNegativeButton("取消", null);
					delDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		layoutTime.addView(view);
	}

	/**
	 * 设置默认时间段
	 */
	private static void getDefaultTime() {
		Long time = System.currentTimeMillis();
		Long tomorrowDate = time + 24 * 60 * 60 * 1000;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		String tomorrow = dateFormat.format(tomorrowDate);
		defaultStartTime = tomorrow + " 08:30";
		defaultEndTime = tomorrow + " 17:30";
	}

	/**
	 * 替换日期格式
	 * 
	 * @param date
	 * @return
	 */
	private static String replaceDate(String date) {
		String reDate = date;
		reDate = reDate.replaceFirst(" ", "号" + " ");
		reDate = reDate.replaceFirst("T", "号" + " ");
		reDate = reDate.replaceFirst("-", "年");
		reDate = reDate.replaceFirst("-", "月");
		return reDate;
	}
}
