package com.jsy_jiaobao.main.personalcenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.PersonalSpaceActivity;
import com.jsy_jiaobao.main.schoolcircle.ArticleDetailsActivity;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人空间的文章列表
 * 
 * @author admin
 * 
 * @param <T>
 */
public class NoticeArtListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;
	private BitmapUtils bitmap;
	private String str_todaytime;
	private String[] str_todaytimes;
	private SimpleDateFormat dateFormat;
	private Date today;
	private String mainURL;
	private boolean ispc = false;

	/**
	 * 
	 * @param mContext
	 * @param ispc
	 *            是否是个人空间的文章列表
	 */
	public NoticeArtListAdapter(Context mContext, boolean ispc) {
		this.mContext = mContext;
		this.ispc = ispc;
		bitmap = JSYApplication.getInstance().bitmap;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
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
		// TextView click = viewHolder.getView(R.id.artlist_item_clickcount);
		TextView view = viewHolder.getView(R.id.artlist_item_viewcount);
		TextView like = viewHolder.getView(R.id.artlist_item_likecount);
		ImageView likeview = viewHolder.getView(R.id.artlist_item_likeview);
		ImageView pic = viewHolder.getView(R.id.artlist_item_pic);
		RelativeLayout body = viewHolder.getView(R.id.artlist_item_body);
		try {
			final ArthInfo arthInfo = (ArthInfo) getItem(position);
			String url = mainURL + ConstantUrl.photoURL + "?AccID="
					+ arthInfo.getJiaoBaoHao();
			bitmap.display(pic, url);
			// viewHolder.setImageByUrl(R.id.artlist_item_pic,url);
			// ImageLoader.getInstance().loadImage(url, pic);

			String[] str_times = arthInfo.getRecDate().split(" ");
			if (str_times[0].equals(str_todaytimes[0])) {
				time.setText(str_times[1]);
			} else {
				time.setText(str_times[0]);
			}

			title.setText(arthInfo.getTitle());
			author.setText(arthInfo.getUserName());
			title.setText(arthInfo.getTitle());
			view.setText(String.valueOf(arthInfo.getViewCount()));
			like.setText(String.valueOf(arthInfo.getLikeCount()));

			if (arthInfo.getLikeflag() == 0) {
				likeview.setBackgroundResource(R.drawable.icon_art3);
			} else {
				likeview.setBackgroundResource(R.drawable.icon_art3_2);
			}

			body.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MobclickAgent.onEvent(mContext, mContext.getResources()
							.getString(R.string.PersonalSpaceActivity_body));
					Intent intent = new Intent(mContext,
							ArticleDetailsActivity.class);
					intent.putExtra("arthInfo", arthInfo);
					mContext.startActivity(intent);
				}
			});
			pic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!ispc) {
						Intent intent = new Intent(mContext,
								PersonalSpaceActivity.class);
						intent.putExtra("JiaoBaoHao", arthInfo.getJiaoBaoHao()
								+ "");
						intent.putExtra("UserName", arthInfo.getUserName());
						mContext.startActivity(intent);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}

}
