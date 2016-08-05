package com.jsy_jiaobao.main.affairs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 查看事务列表的Adapter<br>
 * 友情提示：1.布局分为左侧头像区域和右侧内容区域，点击头像和内容是两个功能 --ShangLin Mo
 */
public class Work2ListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private String mainURL;
	private String JiaoBaoHao;
	private String str_todaytime;
	// private String selectTargetID;
	private Date today;
	private String[] str_todaytimes;
	private List<T> mData;
	private BitmapUtils bitmap;
	private SimpleDateFormat dateFormat;

	public Work2ListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
		JiaoBaoHao = BaseActivity.sp.getString("JiaoBaoHao", "");
	}

	public void setData(List<T> mData) {
		this.mData = mData;
	}

	public List<T> getData() {
		return mData;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_work2, position);
		ImageView photo = viewHolder.getView(android.R.id.icon);// 头像
		TextView new_message = viewHolder.getView(R.id.rc_new_message);// 这个好像没用上
		TextView message = viewHolder.getView(android.R.id.message);// 部分信息
		TextView author = viewHolder.getView(android.R.id.text1);// 作者
		TextView time = viewHolder.getView(android.R.id.text2);// 时间
		try {
			final CommMsg item = (CommMsg) getItem(position);
			String url;
			int jiaobaohao = item.getJiaoBaoHao();
			if (jiaobaohao == 0) {
				url = mainURL + ConstantUrl.photoURL + "?AccID=" + JiaoBaoHao;
			} else {
				url = mainURL + ConstantUrl.photoURL + "?AccID=" + jiaobaohao;
			}
			JSYApplication.getInstance().bitmap.display(photo, url);
			new_message.setVisibility(8);
			message.setText(item.getMsgContent());
			author.setText(item.getUserName());
			String[] str_times = item.getRecDate().split(" ");
			if (str_times[0].equals(str_todaytimes[0])) {
				time.setText(str_times[1]);
			} else {
				time.setText(str_times[0]);
			}
			photo.setOnClickListener(new OnClickListener() {// 列表中的头像图片区域
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					if (item.getJiaoBaoHao() == 0) {// 我发的查询类型
						intent.setClass(mContext,
								Work2MineDetailsListActivity.class);
						intent.putExtra("TabIDStr", item.getTabIDStr());//加密后的标识字段
						intent.putExtra("MsgRecDate", item.getRecDate());// 发送时间
						intent.putExtra("ReadFlag", 0);//未读数量
					} else {// 全部，未读，未回复，已回复查询类型
						intent.setClass(mContext,
								Work2OthersDetailsListActivity.class);
						intent.putExtra("TabIDStr", item.getTabIDStr());//加密后的标识字段
						intent.putExtra("MsgRecDate", item.getRecDate());// 发送时间
						intent.putExtra("senderAccId", item.getJiaoBaoHao());// 发送者AccID
						intent.putExtra("UserName", item.getUserName());// 发送者姓名
					}
					mContext.startActivity(intent);
				}
			});
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {// 列表头像的右侧区域
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setClass(mContext,
									Work2DetailsListItemActivity.class);
							intent.putExtra("type", 31);
							intent.putExtra("TabIDStr", item.getTabIDStr());//加密后的标识字段
							intent.putExtra("MsgRecDate", item.getRecDate());// 发送时间
							intent.putExtra("UserName", item.getUserName());// 发送者姓名
							mContext.startActivity(intent);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			message.setText(R.string.this_message_wrong);// 此条信息有误
			author.setText("");
			time.setText(str_todaytimes[0]);
		}
		return viewHolder.getConvertView();
	}
}
