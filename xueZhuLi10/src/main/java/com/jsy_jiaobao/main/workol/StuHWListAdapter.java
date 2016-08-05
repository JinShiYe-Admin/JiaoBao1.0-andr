package com.jsy_jiaobao.main.workol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.StuHW;

/**
 * 学生作业Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class StuHWListAdapter<T> extends BaseAdapter {
	private ArrayList<T> mData;// 数据
	private Context mContext;// 上下文
	private int IsSelf;// 练习
	// private int longTime;
	private boolean IsLook = false;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			Locale.getDefault());// 时间格式

	public StuHWListAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * item数量
	 */
	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	/**
	 * Item数据
	 */
	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	/**
	 * Item Id
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 是否完成
	 * 
	 * @return
	 */
	public boolean getFinishFlag() {
		boolean isFinish = true;
		if (mData != null) {
			for (int i = 0; i < mData.size(); i++) {
				StuHW stuHW = (StuHW) mData.get(i);
				// 如果做完了
				if (stuHW.getIsHWFinish() == 1) {
					isFinish = true;
				} else {
					isFinish = false;
					break;
				}
			}
		} else {
			isFinish = true;
		}
		return isFinish;
	}

	/**
	 * Item布局
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_workolstu_hwlist, position);
		TextView icon = viewHolder.getView(R.id.workol_stuhw_tv_icon);
		TextView title = viewHolder.getView(R.id.workol_stuhw_tv_title);
		TextView num = viewHolder.getView(R.id.workol_stuhw_tv_num);
		TextView time0 = viewHolder.getView(R.id.workol_stuhw_tv_time0);
		TextView time = viewHolder.getView(R.id.workol_stuhw_tv_time);
		TextView start = viewHolder.getView(R.id.workol_stuhw_tv_start);
		TextView score = viewHolder.getView(R.id.workol_stuhw_tv_score);
		TextView edulevel = viewHolder.getView(R.id.workol_stuhw_tv_edulevel);
		LinearLayout ll_flag = viewHolder.getView(R.id.workol_stuhw_ll_flag);
		final StuHW stuHW = (StuHW) getItem(position);
		final ArrayList<Boolean> post = new ArrayList<Boolean>();
		post.add(false);
		post.add(IsLook);
		boolean isFinish = false;
		if (stuHW != null) {
			num.setText(String.valueOf(stuHW.getItemNumber()));
			String str_time = "";
			final String name;
			title.setText(stuHW.getHomeworkName());
			// longTime = stuHW.getLongTime();
			if (getIsSelf() == 0) {
				// 过期时间
				time0.setText(mContext.getResources().getString(
						R.string.past_due_time));
				name = mContext.getResources().getString(R.string.home_work);
				str_time = stuHW.getEXPIRYDATE();
			} else {
				// 发布时间
				time0.setText(mContext.getResources().getString(
						R.string.release_time));
				name = mContext.getResources().getString(R.string.exercise);
				str_time = stuHW.getCreateDate();
			}
			if (!TextUtils.isEmpty(str_time)) {
				str_time = formatter.format(getDayTime(str_time.replace("T",
						" ")));
			} else {
				str_time = "";
			}
			time.setText(str_time);
			if (stuHW.getIsHaveAdd() == 1) {// 主观题
				icon.setVisibility(View.VISIBLE);
			} else {
				icon.setVisibility(View.GONE);
			}
			if (stuHW.getIsHWFinish() == 1) {
				// 已完成
				isFinish = true;
				start.setVisibility(View.GONE);
				ll_flag.setVisibility(View.VISIBLE);
				// 显示分数和学力值
				score.setText(String.valueOf(stuHW.getHWScore()));
				edulevel.setText(String.valueOf(stuHW.getEduLevel()));
			} else {
				// 未完成
				// 获取开始时间
				String flag = stuHW.getHWStartTime();
				// 未开始
				if ("1970-01-01T00:00:00".equals(flag)) {
					start.setText(mContext.getResources().getString(
							R.string.work_start)
							+ name);

				} else {
					// 已开始
					start.setText(mContext.getResources().getString(
							R.string.work_continue)
							+ name);
				}
				// 完成状态 未完成
				isFinish = false;
				start.setVisibility(0);
				ll_flag.setVisibility(8);
			}
			post.set(0, isFinish);
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									WorkDetailsActivity.class);
							intent.putExtra("TabID", stuHW.getTabID());
							intent.putExtra("isFinish", post);
							intent.putExtra("isSelf", IsSelf);
							intent.putExtra("Name", name);
							mContext.startActivity(intent);
						}
					});
		}
		return viewHolder.getConvertView();
	}

	public ArrayList<T> getmData() {
		return mData;
	}

	public void setmData(ArrayList<T> mData) {
		this.mData = mData;
	}

	public int getIsSelf() {
		return IsSelf;
	}

	public void setIsSelf(int isSelf) {
		IsSelf = isSelf;
	}

	/**
	 * 时间String转换成long
	 * 
	 * @param date
	 * @return
	 */
	private long getDayTime(String date) {
		// 时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date dt2 = null;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dt2.getTime();

	}

	/**
	 * 
	 * @return isLook判断是否被查看
	 */

	public boolean isIsLook() {
		return IsLook;
	}

	// 设置是否是查看

	public void setIsLook(boolean isLook) {
		IsLook = isLook;
	}

}
