package com.jsy_jiaobao.main.workol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.StuHW;

/**
 * 作业列表Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class GenHWListAdapter<T> extends BaseAdapter {
	private ArrayList<T> mData;// 日期
	private Context mContext;
	private Boolean isHW = true;// 是否作业
	private SimpleDateFormat simpleDateFormat;

	public GenHWListAdapter(Context mContext) {
		this.mContext = mContext;
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * Item的布局
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_workolstu_hwlist, position);
		// icon
		TextView icon = viewHolder.getView(R.id.workol_stuhw_tv_icon);
		// 标题
		TextView title = viewHolder.getView(R.id.workol_stuhw_tv_title);
		// 数目
		TextView num = viewHolder.getView(R.id.workol_stuhw_tv_num);
		// 过期时间 开始时间 分数 学力值
		TextView time0 = viewHolder.getView(R.id.workol_stuhw_tv_time0);
		TextView time = viewHolder.getView(R.id.workol_stuhw_tv_time);
		TextView start = viewHolder.getView(R.id.workol_stuhw_tv_start);
		TextView score = viewHolder.getView(R.id.workol_stuhw_tv_score);
		TextView edulevel = viewHolder.getView(R.id.workol_stuhw_tv_edulevel);
		LinearLayout ll_flag = viewHolder.getView(R.id.workol_stuhw_ll_flag);
		final StuHW stuHW = (StuHW) getItem(position);
		final ArrayList<Boolean> post = new ArrayList<>();
		final String name;
		post.add(true);
		post.add(true);
		if (stuHW != null) {
			if (isHW) {
				// 作业
				name = mContext.getResources().getString(R.string.home_work);
			} else {
				// 练习
				name = mContext.getResources().getString(R.string.exercise);
			}
			// 作业名称
			title.setText(stuHW.getHomeworkName());
			// 题量
			num.setText(String.valueOf(stuHW.getItemNumber()));
			String str_time ;
			// 过期时间
			time0.setText(R.string.expiration);
			// 过期时间
			str_time = stuHW.getEXPIRYDATE();
			// 如果过期时间不为空
			if (!TextUtils.isEmpty(str_time)) {
				str_time = str_time.replace("T", " ");
				Date date;
				try {
					date = simpleDateFormat.parse(str_time);
				} catch (Exception e) {
					// TODO: handle exception
					date = new Date();
				}
				str_time = simpleDateFormat.format(date);
			} else {
				// 否则 过期时间显示为空
				str_time = "";
			}
			time.setText(str_time);
			if (stuHW.getIsHaveAdd() == 1) {// 主观题
				icon.setVisibility(View.VISIBLE);
			} else {
				icon.setVisibility(View.GONE);
			}
			if (stuHW.getIsHWFinish() == 1) {
				start.setVisibility(View.GONE);
				ll_flag.setVisibility(View.VISIBLE);
				score.setText(String.valueOf(stuHW.getHWScore()));
				edulevel.setText(String.valueOf(stuHW.getEduLevel()));
				post.set(0, true);
			} else {
				// 未完成
				start.setText(R.string.unfinish);
				start.setVisibility(View.VISIBLE);
				ll_flag.setVisibility(View.GONE);
				post.set(0, false);
			}
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									WorkDetailsActivity.class);
							intent.putExtra("Name", name);
							intent.putExtra("TabID", stuHW.getTabID());
							intent.putExtra("isFinish", post);
							mContext.startActivity(intent);
						}
					});
		}
		return viewHolder.getConvertView();
	}


	// 设置是否作业
	public void setIsHW(Boolean isHW) {
		this.isHW = isHW;
	}

	public ArrayList<T> getmData() {
		return mData;
	}

	public void setmData(ArrayList<T> mData) {
		this.mData = mData;
	}

}
