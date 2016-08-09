package com.jsy_jiaobao.main.studentrecord;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class NoticeListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;
	private BitmapUtils bitmap;
	private String mainURL;

	public NoticeListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
	}

	public void setData(List<T> mData) {
		this.mData = mData;
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
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
				R.layout.layout_artlist_item, position);
		TextView title = viewHolder.getView(R.id.artlist_item_title);
		TextView time = viewHolder.getView(R.id.artlist_item_time);
		TextView author = viewHolder.getView(R.id.artlist_item_author);
		TextView view = viewHolder.getView(R.id.artlist_item_viewcount);
		TextView like = viewHolder.getView(R.id.artlist_item_likecount);
		ImageView pic = viewHolder.getView(R.id.artlist_item_pic);
		try {
			final ArthInfo arthInfo = (ArthInfo) getItem(position);
			String url = mainURL + ConstantUrl.photoURL + "?AccID="
					+ arthInfo.getJiaoBaoHao();
			bitmap.display(pic, url);
			String[] times = arthInfo.getRecDate().split(" ");
			title.setText(arthInfo.getTitle());
			time.setText(times[0]);
			author.setText(arthInfo.getUserName());
			view.setText(String.valueOf(arthInfo.getViewCount()));
			like.setText(String.valueOf(arthInfo.getLikeCount()));
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}
}
