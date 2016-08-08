package com.jsy_jiaobao.main.personalcenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.lidroid.xutils.BitmapUtils;

/**
 * 求知答案列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class QiuZhiAnswerListAdapter<T> extends BaseAdapter implements Watcher {
	private QuestionIndexItem question;
	private Context mContext;
	private List<T> mData;
	private BitmapUtils bitmap;// 图片
	private String[] str_todaytimes;// 日期 和时间 分开
	private String mainURL;

	public QiuZhiAnswerListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		SimpleDateFormat dateFormat;// 日期格式
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date today;// 今天
		String str_todaytime;// 今天日期格式化
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
		WatchedEntityIndexItem.getInstance().addWatcher(this);
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
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * cell的视图
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_qiuzhi_answer_listview, position);
		LinearLayout layout_answer = viewHolder
				.getView(R.id.qiuzhi_item_layout_answer);
		LinearLayout layout_answercontent = viewHolder
				.getView(R.id.qiuzhi_item_layout_answercontent);
		TextView tv_likenum = viewHolder.getView(R.id.qiuzhi_item_tv_likenum);
		// 回答者的头像
		ImageView iv_photo = viewHolder.getView(R.id.qiuzhi_item_iv_photo);
		// 回答者
		TextView tv_author = viewHolder.getView(R.id.qiuzhi_item_tv_author);
		// 回答内容
		TextView tv_answer = viewHolder.getView(R.id.qiuzhi_item_tv_answer);
		TextView tv_gist = viewHolder.getView(R.id.qiuzhi_item_tv_gist);
		TextView tv_gist_ = viewHolder.getView(R.id.qiuzhi_item_tv_gist_);
		// 回答中的图片列表
		GridView gv_gallery = viewHolder.getView(R.id.qiuzhi_item_gv_gallery);
		// 回答时间
		TextView tv_time = viewHolder.getView(R.id.qiuzhi_item_tv_time);
		// 评论数
		TextView tv_commentnum = viewHolder
				.getView(R.id.qiuzhi_item_tv_commentnum);
		/**
		 * 控件加载数据
		 */
		if (mData.get(position) instanceof AnswerItem) {
			try {
				final AnswerItem answer = (AnswerItem) mData.get(position);
				if (null == answer) {
					// 答案为空
					LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout_answer
							.getLayoutParams(); // 取控件textView当前的布局参数
					linearParams.height = 0;// 控件的高强制设0
					layout_answer.setLayoutParams(linearParams);
				} else {// 答案不为空
					layout_answer
							.setLayoutParams(new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT));
					tv_likenum.setText(mContext.getString(R.string.number_like,answer.getLikeCount()));
					if (TextUtils.isEmpty(answer.getIdFlag())) {// 匿名
						tv_author.setText(R.string.anonymity);
						iv_photo.setImageResource(R.drawable.photo);
					} else {// 署名
						tv_author.setText(answer.getIdFlag());
						String url = mainURL + ConstantUrl.photoURL + "?AccID="
								+ answer.getJiaoBaoHao();
						bitmap.display(iv_photo, url);
					}
					String[] str_times = answer.getRecDate().split("T");
					if (str_times[0].equals(str_todaytimes[0])) {
						// 如果日期是今天 只显示时间
						tv_time.setText(str_times[1]);
					} else {
						// 否，则显示日期
						tv_time.setText(str_times[0]);
					}
					// 加载评论数
					tv_commentnum.setText(String.valueOf(answer.getCCount()));
					// 答案加载标题
					tv_answer.setText(answer.getATitle());
					String thumbnail = answer.getThumbnail();
					// 0无内容1有内容2有证据
					int flag = answer.getFlag();
					tv_gist.setTextColor(mContext.getResources().getColor(
							R.color.black));

					if (flag == 0) {
						tv_gist.setText("");
						if (TextUtils.isEmpty(thumbnail)) {
							// 无内容
							tv_gist_.setBackgroundResource(R.drawable.icon_qiuzhi_nocontent_);
						} else {
							// 内容
							tv_gist_.setBackgroundResource(R.drawable.icon_qiuzhi_content_);
						}
					} else if (flag == 1) {
						// 内容 加载内容
						tv_gist_.setBackgroundResource(R.drawable.icon_qiuzhi_content_);
						tv_gist.setText(answer.getAbstracts());
					} else if (flag == 2) {
						// 有证据
						String abs = answer.getAbstracts();
						if (TextUtils.isEmpty(thumbnail)
								&& TextUtils.isEmpty(abs)) {
							// 此答案已修改
							tv_gist.setText(mContext.getResources().getString(
									R.string.qiuzhi_gist_revamp));
						} else {
							tv_gist.setText(abs);
						}
						tv_gist_.setBackgroundResource(R.drawable.icon_qiuzhi_gist_);
						tv_gist.setTextColor(mContext.getResources().getColor(
								R.color.color_e67215));
					}
					if (TextUtils.isEmpty(thumbnail)) {
						// 无图片
						gv_gallery.setVisibility(View.GONE);
					} else {
						// 有图片
						gv_gallery.setVisibility(View.VISIBLE);
						JSONArray jsonarray = new JSONArray(thumbnail);
						PictureAdapter adapter = new PictureAdapter(mContext,
								jsonarray);
						// 加载图片
						gv_gallery.setAdapter(adapter);
					}
					// 答案的点击事件监听
					layout_answercontent
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											mContext,
											QiuZhiQuestionAnswerDetailsActivity.class);
									Bundle args = new Bundle();
									args.putSerializable("Answer", answer);
									args.putSerializable("QuestionIndexItem",
											getQuestion());
									args.putSerializable("IsAnswer", 1);
									intent.putExtras(args);
									mContext.startActivity(intent);
								}
							});
				}
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

	@Override
	public void update(QuestionIndexItem qEntity) {

	}

	/**
	 * 更新
	 */
	@Override
	public void update(AnswerItem answer) {
		if (answer != null) {
			for (int i = 0; i < mData.size(); i++) {
				AnswerItem a = (AnswerItem) mData.get(i);
				if (a.getTabID() == answer.getTabID()) {
					mData.set(i, (T) answer);
				}
			}
			QiuZhiAnswerListAdapter.this.notifyDataSetChanged();
		}
	}
}
