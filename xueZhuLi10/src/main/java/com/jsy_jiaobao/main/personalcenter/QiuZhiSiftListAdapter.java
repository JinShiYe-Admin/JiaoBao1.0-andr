package com.jsy_jiaobao.main.personalcenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.PickedItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.lidroid.xutils.BitmapUtils;

/**
 * 往期精选列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */

public class QiuZhiSiftListAdapter<T> extends BaseAdapter {
	private QuestionIndexItem question;// 问题数据
	private Context mContext;
	private List<T> mData;// 数据
	private BitmapUtils bitmap;
	private SimpleDateFormat dateFormat;
	private Date today;
	private String str_todaytime;
	private String[] str_todaytimes;
	private String mainURL;

	public QiuZhiSiftListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainURL = ConstantUrl.jsyoa.replaceAll("jbclient", "");
	}

	public void setData(List<T> mData) {
		this.mData = mData;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_qiuzhi_siftlist, position);
		TextView tv_title = viewHolder.getView(R.id.qiuzhi_sift_tv_title);
		TextView tv_time = viewHolder.getView(R.id.qiuzhi_sift_tv_time);
		ImageView iv_cover = viewHolder.getView(R.id.qiuzhi_sift_iv_cover);
		if (mData.get(position) instanceof PickedItem) {
			try {
				final PickedItem data = (PickedItem) mData.get(position);
				tv_title.setText(data.getPTitle());
				String imgs = data.getImgContent();
				if (!TextUtils.isEmpty(imgs)) {
					final JSONArray jsonarray = new JSONArray(imgs);
					String url = "";
					if (jsonarray.length() > 0) {
						for (int i = 0; i < jsonarray.length(); i++) {
							jsonarray.put(i, mainURL + data.getBaseImgUrl()
									+ jsonarray.getString(i));
						}
						url = jsonarray.getString(0);
					}
					bitmap.display(iv_cover, url);
				}
				String[] str_times = data.getRecDate().split("T");
				if (str_times[0].equals(str_todaytimes[0])) {
					tv_time.setText(str_times[1]);
				} else {
					tv_time.setText(str_times[0]);
				}
				// list Item的点击事件
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext,
										QiuZhiPickedDetailsActivity.class);
								intent.putExtra("PickedItem", data);
								mContext.startActivity(intent);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return viewHolder.getConvertView();
	}

	public QuestionIndexItem getQuestion() {
		return question;
	}

	public void setQuestion(QuestionIndexItem question) {
		this.question = question;
	}
}
