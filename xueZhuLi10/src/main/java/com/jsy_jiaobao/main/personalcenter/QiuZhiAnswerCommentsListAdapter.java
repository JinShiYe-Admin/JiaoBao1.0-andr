package com.jsy_jiaobao.main.personalcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerComment;
import com.jsy_jiaobao.po.qiuzhi.AnswerRefcomment;

/**
 * 求知答案评论列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */

public class QiuZhiAnswerCommentsListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;// 数据
	private ArrayList<AnswerRefcomment> refcomments;// 答案评论列表


	private String[] str_todaytimes;// 日期的Array
	private String mainURL;// 主Url

	public QiuZhiAnswerCommentsListAdapter(Context mContext) {
		this.mContext = mContext;
		// bitmap = JSYApplication.getInstance().bitmap;
		// 日期格式
		SimpleDateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		// 当前日期
		Date today;// 日期
		String str_todaytime;// 日期的格式化
		today = new Date();
		// 格式化
		str_todaytime = dateFormat.format(today);
		// 日期和时间分开
		str_todaytimes = str_todaytime.split(" ");
		// 主Url
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
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
		return mData == null ? null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * cell界面
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_qiuzhi_answer_comment, position);
		LinearLayout layout_ref = viewHolder
				.getView(R.id.qiuzhi_answer_comment_layout_ref);
		// 评论者头像
		ImageView photo = viewHolder.getView(android.R.id.icon);
		TextView new_message = viewHolder.getView(R.id.rc_new_message);
		TextView message = viewHolder
				.getView(R.id.qiuzhi_answer_comment_tv_msg);
		// 评论者
		TextView author = viewHolder
				.getView(R.id.qiuzhi_answer_comment_tv_author);
		// 评论时间
		TextView time = viewHolder.getView(R.id.qiuzhi_answer_comment_tv_time);
		// 赞
		final TextView like = viewHolder
				.getView(R.id.qiuzhi_answer_comment_tv_like);
		// 反对
		final TextView against = viewHolder
				.getView(R.id.qiuzhi_answer_comment_tv_against);
		like.setClickable(true);
		against.setClickable(true);
		layout_ref.removeAllViews();
		layout_ref.setVisibility(View.GONE);
		/**
		 * 根据数据 更新cell的UI
		 */
		try {
			final AnswerComment item = (AnswerComment) getItem(position);
			int jiaobaohao = item.getJiaoBaoHao();
			String url = mainURL + ConstantUrl.photoURL + "?AccID="
					+ jiaobaohao;
			JSYApplication.getInstance().bitmap.display(photo, url);
			new_message.setText(item.getNumber());
			message.setText(item.getWContent());
			message.setTextColor(mContext.getResources()
					.getColor(R.color.black));
			author.setText(item.getUserName() == null ? item.getJiaoBaoHao()
					+ ":" : item.getUserName() + ":");
			against.setText(mContext.getString(R.string.against, (int)item.getCaiCount()));
			like.setText(mContext.getString(R.string.like,item.getLikeCount()));
			against.setEnabled(!item.isAddScore());
			like.setEnabled(!item.isAddScore());
			String[] str_times = item.getRecDate().split("T");
			if (str_times[0].equals(str_todaytimes[0])) {
				time.setText(str_times[1]);
			} else {
				time.setText(str_times[0]);
			}
			if (!TextUtils.isEmpty(item.getRefIds())) {
				layout_ref.setVisibility(View.VISIBLE);
				String[] ids = item.getRefIds().split("\\,");
				for (String id : ids) {
					boolean have = false;
					for (AnswerRefcomment ref : refcomments) {
						if (id.equals(String.valueOf(ref.getTabID()))) {
							have = true;
							TextView tv = new TextView(mContext);

							String str = ref.getUserName();
							if (TextUtils.isEmpty(str)) {
								str = ref.getJiaoBaoHao() + ":\n";
							}
							tv.setText(str +String.valueOf(ref.getWContent()) );
							tv.setTextSize(12);
							tv.setPadding(0, 10, 0, 10);
							layout_ref.addView(tv);
						}
					}
					if (!have) {
						TextView tv = new TextView(mContext);
						tv.setText("该评论已被屏蔽或删除");
						tv.setPadding(0, 10, 0, 10);
						layout_ref.addView(tv);
					}
				}
				int cc = layout_ref.getChildCount();
				if (cc == 0) {
					layout_ref.setVisibility(View.GONE);
				}
			}
			// 赞的点击事件监听
			like.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					like.setClickable(false);
					QiuZhiQuestionAnswerDetailsActivityController.getInstance()
							.AddScore(item, 1);
				}
			});
			// 反对的点击事件监听
			against.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					against.setClickable(false);
					QiuZhiQuestionAnswerDetailsActivityController.getInstance()
							.AddScore(item, 0);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			message.setText(R.string.this_message_wrong);
			author.setText("");
			time.setText(str_todaytimes[0]);
		}
		return viewHolder.getConvertView();
	}


	public void setRefcomments(ArrayList<AnswerRefcomment> refcomments) {
		this.refcomments = refcomments;
	}

}
