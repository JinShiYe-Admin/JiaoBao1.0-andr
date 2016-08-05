package com.jsy_jiaobao.main.leave;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.ClassSumLeaveModel;
import com.jsy_jiaobao.po.leave.StuSumLeavesModel;
import com.jsy_jiaobao.po.leave.SumLeavesModel;
import com.umeng.analytics.MobclickAgent;

/**
 * 功能说明：审核界面，统计查询Adapter
 * 
 * @author ShangLin Mo
 */
public class CheckerQueryAdapter<T> extends BaseAdapter {
	private List<?> mData;
	private Context mContext;
	private int createYear;
	private int createMonth;
	private String sDateTime;

	public String getsDateTime() {
		return sDateTime;
	}

	public void setsDateTime(String sDateTime) {
		this.sDateTime = sDateTime;
	}

	public CheckerQueryAdapter(Context context) {
		mContext = context;
	}

	public List<?> getData() {
		return mData;
	}

	public void setData(List<?> data) {
		mData = data;
	}

	@Override
	public int getItemViewType(int position) {
		if (mData.get(position) instanceof SumLeavesModel) {// 教职工请假统计
			return 0;
		} else if (mData.get(position) instanceof StuSumLeavesModel) {// 学生请假统计
			return 1;
		} else if (mData.get(position) instanceof ClassSumLeaveModel) {// 班级请假统计
			return 2;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	public int getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(int createMonth) {
		this.createMonth = createMonth;
	}

	public int getCreateYear() {
		return createYear;
	}

	public void setCreateYear(int createYear) {
		this.createYear = createYear;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.leave_item_checkedselect_listview, position);
		TextView name = viewHolder.getView(R.id.item_checkedselect_name);
		TextView clazz = viewHolder.getView(R.id.item_checkedselect_class);
		TextView other = viewHolder.getView(R.id.item_checkedselect_other);
		if (getItemViewType(position) == 0) {// 教职工
			final SumLeavesModel s = (SumLeavesModel) mData.get(position);
			name.setText(s.getManName());
			clazz.setText("0");
			other.setText(s.getAmount() + "");
			viewHolder.getConvertView().setOnClickListener(null);
		} else if (getItemViewType(position) == 1) {// 学生
			final StuSumLeavesModel ss = (StuSumLeavesModel) mData
					.get(position);
			name.setText(ss.getManName());
			clazz.setText(ss.getAmount() + "");
			other.setText(ss.getAmount2() + "");
			viewHolder.getConvertView().setOnClickListener(null);
		} else if (getItemViewType(position) == 2) {// 班级
			final ClassSumLeaveModel cs = (ClassSumLeaveModel) mData
					.get(position);
			name.setText(cs.getClassStr());
			clazz.setText(cs.getAmount() + "");
			other.setText(cs.getAmount2() + "");
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							MobclickAgent.onEvent(
									mContext,
									mContext.getString(R.string.CheckerQueryAdapter_ClassSumLeaveModel));
							Intent intent = new Intent(mContext,
									SumStudentLeavesActivity.class);
							intent.putExtra("UnitClassId", cs.getUnitClassId());
							intent.putExtra("sDateTime", sDateTime);
							mContext.startActivity(intent);
						}
					});
		}
		return viewHolder.getConvertView();
	}
}
