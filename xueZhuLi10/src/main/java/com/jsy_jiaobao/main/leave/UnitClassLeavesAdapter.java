package com.jsy_jiaobao.main.leave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.GateQueryLeaveModel;
import com.jsy_jiaobao.po.leave.MyLeaveModel;
import com.jsy_jiaobao.po.leave.UnitClassLeaveModel;
import com.umeng.analytics.MobclickAgent;

/**
 * Adapter
 */
public class UnitClassLeavesAdapter<T> extends BaseAdapter {
	private Activity mActivity;
	private Context mContext;
	private int type = 0;// 0可见姓名 1不可见姓名
	private List<?> Data;
	private int checkType = 0;// 查看 1审核
	private int checkRole = 0;// 1一审 2 二审 3三审 4审 6门卫
	private Fragment fragment;

	public UnitClassLeavesAdapter(Fragment fragment) {
		this.fragment = fragment;
		mContext = fragment.getActivity();
	}

	public UnitClassLeavesAdapter(Activity activity) {
		mActivity = activity;
		mContext = activity;
	}

	public List<?> getData() {
		return Data;
	}

	public void setData(List<?> data) {
		Data = data;
	}

	@Override
	public int getCount() {
		return Data == null ? 0 : Data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		 if (Data.get(position) instanceof MyLeaveModel) {//我的请假列表
			return 0;
		} else if (Data.get(position) instanceof UnitClassLeaveModel) {//未审核，已审核
			return 1;
		} else if (Data.get(position) instanceof GateQueryLeaveModel) {//门卫查询
			return 2;
		}

		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.leave_item_teacherselect_listview, position);
		TextView time = viewHolder.getView(R.id.item_checkedcommited_time);
		TextView name = viewHolder.getView(R.id.item_checkedcommited_name);
		TextView reason = viewHolder.getView(R.id.item_checkedcommited_reason);
		TextView flag = viewHolder.getView(R.id.item_checkedcommited_flag);
		name.setVisibility(type == 1 ? View.GONE : View.VISIBLE);

		if (getItemViewType(position) == 0) {//我的请假列表		
				final int sPosition = position;
				final MyLeaveModel leave = (MyLeaveModel) Data.get(position);
				time.setText(replaceDate(leave.getWriteDate()));
				name.setText(leave.getManName());
				Log.e("Adapter" + (position), leave.getWriteDate());
				reason.setText(leave.getLeaveType());
				flag.setText(replaceflag(leave.getStatusStr()));
				viewHolder.getConvertView().setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								MobclickAgent.onEvent(mContext, mContext.getResources().getString(R.string.UnitClassLeavesAdapter_MyLeaveModel));
								Intent intent = new Intent(mContext,
										LeaveDetailsActivity.class);
								intent.putExtra("CheckType", checkType);
								intent.putExtra("CheckRole", checkRole);
								intent.putExtra("LeaveID", leave.getTabID());
								intent.putExtra("Position", sPosition);
								if (fragment != null) {
									fragment.startActivityForResult(intent, 0);
								} else {
									mActivity.startActivityForResult(intent, 0);
								}
							}
						});		
		} else if (getItemViewType(position) == 1) {//未审核，已审核
				final int sPosition = position;
				final UnitClassLeaveModel leave = (UnitClassLeaveModel) Data
						.get(position);
				time.setText(replaceDate(leave.getWriteDate()));
				name.setText(leave.getManName());				
				reason.setText(leave.getLeaveType());
				flag.setText(replaceflag(leave.getStatusStr()));
				try {
					name.setText(leave.getManName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewHolder.getConvertView().setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								MobclickAgent.onEvent(mContext, mContext.getResources().getString(R.string.UnitClassLeavesAdapter_UnitClassLeaveModel));
								Intent intent = new Intent(mContext,
										LeaveDetailsActivity.class);
								intent.putExtra("CheckType", checkType);
								intent.putExtra("CheckRole", checkRole);
								System.out.println(checkType+"-"+checkRole);
								intent.putExtra("LeaveID", leave.getTabID());
								intent.putExtra("Position", sPosition);
								if (fragment != null) {
									fragment.startActivityForResult(intent, 0);
								} else {
									mActivity.startActivityForResult(intent, 0);
								}								
							}
						});
		} else {//门卫查询		
				final int sPosition = position;
				final GateQueryLeaveModel leave = (GateQueryLeaveModel) Data
						.get(position);
				time.setText(replaceDate(leave.getWriteDate()));
				name.setText(leave.getManName());				
				reason.setText(leave.getLeaveType());
				flag.setVisibility(View.GONE);
				try {
					name.setText(leave.getManName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewHolder.getConvertView().setOnClickListener(
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								MobclickAgent.onEvent(mContext, mContext.getResources().getString(R.string.UnitClassLeavesAdapter_GateQueryLeaveModel));
								Intent intent = new Intent(mContext,
										GateCheckDetails.class);
								intent.putExtra("CheckType", checkType);
								intent.putExtra("CheckRole", checkRole);
								intent.putExtra("Position", sPosition);
								Bundle mBundle = new Bundle();
								mBundle.putSerializable("ChoseLeave", leave);
								intent.putExtras(mBundle);
								if (fragment != null) {
									fragment.startActivityForResult(intent, 0);
								} else {
									mActivity.startActivityForResult(intent, 0);
								}
							}
						});
			
		}
		return viewHolder.getConvertView();
	}

	/**
	 * 0可见 1不可见
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	public int getCheckType() {
		return checkType;
	}

	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}

	public int getCheckRole() {
		return checkRole;
	}

	public void setCheckRole(int checkRole) {
		this.checkRole = checkRole;
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
		reDate = reDate.replaceFirst("T", "号" + "T");
		reDate = reDate.substring(0, reDate.indexOf("T"));
		return reDate;
	}

	/**
	 * 替换状态文字
	 * 
	 * @param flag
	 * @return
	 */
	private static String replaceflag(String flag) {
		String reDate;
		if ("等待中".equals(flag)) {
			reDate = "未审核";
		} else if ("审批中".equals(flag)) {
			reDate = "审核中";
		} else if ("审批拒绝".equals(flag)) {
			reDate = "拒绝";
		} else {
			reDate = "同意";
		}
		return reDate;
	}
}
