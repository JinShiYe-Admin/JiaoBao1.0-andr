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
import com.jsy_jiaobao.po.workol.EduLevel;

/***
 * 学力值adapter
 * 
 * @author admin
 * 
 * @param <T>
 */

public class GenHWEduLevelAdapter<T> extends BaseAdapter {
	private final int TYPE_TITLE = 0;// 第一行
	private final int TYPE_CURR = 1;// 其他行
	private ArrayList<T> mData;// 学力值数据
	private Context mContext;// 上下文

	public GenHWEduLevelAdapter(Context mContext) {
		this.mContext = mContext;
	}

	// 数量大小
	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size() + 1;
	}

	// Item
	@Override
	public Object getItem(int position) {
		return mData.get(position - 1);
	}

	// item的Id
	@Override
	public long getItemId(int position) {
		return position;
	}

	// item的控件类型
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_TITLE;
		} else {
			return TYPE_CURR;
		}
	}

	// item的控件类型数量
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	// item的控件布局，及所显示的状态
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_workolgen_levellist, position);
		RelativeLayout layout = viewHolder.getView(R.id.workol_stuhw_layout);
		// 名字
		TextView name = viewHolder.getView(R.id.workol_stuhw_tv_title);
		// 分数
		TextView score = viewHolder.getView(R.id.workol_stuhw_tv_score);
		TextView flag = viewHolder.getView(R.id.workol_stuhw_tv_flag);
		flag.setVisibility(View.VISIBLE);
		switch (getItemViewType(position)) {
		// 第一个Item
		case TYPE_TITLE:
			// 顶格
			name.setPadding(0, 0, 0, 0);
			flag.setVisibility(View.INVISIBLE);// 控件消失，但占用地方
			score.setTextColor(mContext.getResources().getColor(
					R.color.color_21292b));// 分数颜色
			layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.color_ebebeb));// 背景颜色
			break;
		case TYPE_CURR:
			// 数据加载至Item
			EduLevel level = (EduLevel) getItem(position);
			if (level != null) {
				// 如果不为空，则设置背景色、颜色
				layout.setBackgroundColor(mContext.getResources().getColor(
						R.color.white));
				score.setTextColor(mContext.getResources().getColor(
						R.color.color_e67215));
				name.setText(level.getName());
				// 退格20
				name.setPadding(level.getPadding() * 20, 0, 0, 0);
				score.setText(String.valueOf(level.getLevel()));
				if (!level.isHaveChild()) {
					// 如果没有子内容
					flag.setVisibility(View.INVISIBLE);// 小旗子消失，但占用位置
				} else {
					flag.setVisibility(View.VISIBLE);// 小旗子显现
					// 是否展开，显示不同状态
					if (level.isExpanded()) {
						flag.setBackgroundResource(R.drawable.icon_workol_open);
					} else {
						flag.setBackgroundResource(R.drawable.icon_workol_close);
					}
				}
			}
			break;
		}
		return viewHolder.getConvertView();
	}

	// 获取数据
	public ArrayList<T> getmData() {
		return mData;
	}

	// 设置数据
	public void setmData(ArrayList<T> mData) {
		this.mData = mData;
	}
}
