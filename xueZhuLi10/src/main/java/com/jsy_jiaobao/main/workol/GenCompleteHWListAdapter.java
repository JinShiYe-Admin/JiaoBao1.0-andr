package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.HWStatus;

/**
 * 作业完成情况Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class GenCompleteHWListAdapter<T> extends BaseAdapter {
	private final int TYPE_TITLE = 0;
	private final int TYPE_CURR = 1;
	private ArrayList<T> mData;
	private Context mContext;

	public GenCompleteHWListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_TITLE;
		} else {
			return TYPE_CURR;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	/**
	 * Item的布局
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_workolgen_overlist, position);
		RelativeLayout layout = viewHolder.getView(R.id.workol_stuhw_layout);
		TextView name = viewHolder.getView(R.id.workol_stuhw_tv_name);// 名称
		TextView num = viewHolder.getView(R.id.workol_stuhw_tv_num);// 数量
		switch (getItemViewType(position)) {
		// 第一个Item
		case TYPE_TITLE:
			// 设置控件颜色
			num.setTextColor(mContext.getResources().getColor(
					R.color.color_21292b));
			layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.color_ebebeb));
			break;
		// 其他Item
		case TYPE_CURR:
			// 设置控件颜色和数据
			HWStatus stuHW = (HWStatus) getItem(position);
			if (stuHW != null) {
				layout.setBackgroundColor(mContext.getResources().getColor(
						R.color.white));
				num.setTextColor(mContext.getResources().getColor(
						R.color.color_e67215));
				name.setText(stuHW.getName());
				num.setText(stuHW.getIsF() + "/" + stuHW.getUnF() + "/"
						+ stuHW.getTotal());
			}
			break;
		default:
			break;
		}
		return viewHolder.getConvertView();
	}

	// 获取列表数据
	public ArrayList<T> getmData() {
		return mData;
	}

	// 设置列表数据
	public void setmData(ArrayList<T> mData) {
		this.mData = mData;
	}
}
