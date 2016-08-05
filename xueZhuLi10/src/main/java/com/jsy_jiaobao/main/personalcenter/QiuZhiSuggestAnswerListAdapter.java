package com.jsy_jiaobao.main.personalcenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DestroyInterface;
import com.jsy.xuezhuli.utils.HtmlImageGetter;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexAnswer;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.lidroid.xutils.BitmapUtils;

/**
 * 土建列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class QiuZhiSuggestAnswerListAdapter<T> extends BaseAdapter implements
		Watcher, DestroyInterface {
	private RecommentIndexItem question;// 问题数据
	private Context mContext;
	private List<T> mData;// 数据
	private BitmapUtils bitmap;
	private SimpleDateFormat dateFormat;
	private Date today;
	private String str_todaytime;
	private String[] str_todaytimes;
	private String mainURL;
	private Drawable defaultDrawable;

	public QiuZhiSuggestAnswerListAdapter(Context mContext, CusListView listView) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		Constant.ScreenWith = dm.widthPixels;// 宽度
		Constant.ScreenHeight = dm.heightPixels;// 高度

		defaultDrawable = mContext.getResources().getDrawable(R.drawable.photo);

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

	/**
	 * 推荐列表cell的界面
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_qiuzhi_suggest_answer_listview, position);
		LinearLayout layout_gist = viewHolder
				.getView(R.id.qiuzhi_item_layout_gist);
		RelativeLayout layout_bottom = viewHolder
				.getView(R.id.qiuzhi_item_layout_bottom);
		RelativeLayout layout_author = viewHolder
				.getView(R.id.qiuzhi_item_layout_author);
		TextView tv_likenum = viewHolder.getView(R.id.qiuzhi_item_tv_likenum);

		ImageView iv_photo = viewHolder.getView(R.id.qiuzhi_item_iv_photo);
		TextView tv_author = viewHolder.getView(R.id.qiuzhi_item_tv_author);
		TextView tv_answer = viewHolder.getView(R.id.qiuzhi_item_tv_answer);
		TextView tv_answer_ = viewHolder.getView(R.id.qiuzhi_item_tv_answer_);
		TextView tv_gist = viewHolder.getView(R.id.qiuzhi_item_tv_gist);
		ImageView tv_gist_ = viewHolder.getView(R.id.qiuzhi_item_tv_gist_);
		GridView gv_gallery = viewHolder.getView(R.id.qiuzhi_item_gv_gallery);
		TextView tv_time = viewHolder.getView(R.id.qiuzhi_item_tv_time);
		TextView tv_commentnum = viewHolder
				.getView(R.id.qiuzhi_item_tv_commentnum);
		try {
			final RecommentIndexAnswer answer = (RecommentIndexAnswer) mData
					.get(position);
			if (null == answer) {
				// 答案为空
				layout_author.setVisibility(View.GONE);
				layout_bottom.setVisibility(View.GONE);
				layout_gist.setVisibility(View.GONE);
				tv_answer_.setVisibility(View.GONE);
				tv_answer.setText(mContext.getResources().getString(
						R.string.oldAnswer_isDel_orIsShielded));
			} else {
				tv_answer_.setVisibility(View.VISIBLE);
				layout_author.setVisibility(View.VISIBLE);
				layout_gist.setVisibility(View.VISIBLE);
				layout_bottom.setVisibility(View.VISIBLE);
				tv_likenum.setText(String.valueOf(answer.getLikeCount()) + "赞");
				// 用户昵称或姓名，匿名为空字符串
				if (TextUtils.isEmpty(answer.getIdFlag())) {
					tv_author.setText(mContext.getResources().getString(
							R.string.anonymity));
					bitmap.display(iv_photo, "");
				} else {
					tv_author.setText(answer.getIdFlag());
					String url = mainURL + ConstantUrl.photoURL + "?AccID="
							+ answer.getJiaoBaoHao();
					bitmap.display(iv_photo, url);
				}
				String[] str_times = answer.getRecDate().split("T");
				if (str_times[0].equals(str_todaytimes[0])) {
					tv_time.setText(str_times[1]);
				} else {
					tv_time.setText(str_times[0]);
				}
				tv_commentnum.setText(String.valueOf(answer.getCCount()));

				tv_answer.setText(answer.getATitle());
				// 0=无内容，1=有内容，2=有证据
				int flag = answer.getFlag();
				String start = "<noscript>";
				String end = "</noscript>";
				String aContent = answer.getAContent().replaceAll("\n", "<br>")
						.replaceAll("</br>", "<br>")
						.replaceAll("<br/>", "<br>");
				if (flag == 0) {
					tv_gist.setText("");
					tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
				} else if (flag == 1) {

					if (TextUtils.isEmpty(answer.getAContent())) {
						tv_gist.setText("");
						tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
					} else {
						tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
						aContent = SubNewString(aContent, start, end);
						HtmlImageGetter htmlImageGetter = new HtmlImageGetter(
								tv_gist, "/esun", defaultDrawable);
						Spanned sp = Html.fromHtml(aContent, htmlImageGetter,
								null);
						tv_gist.setText(sp);

					}
				} else if (flag == 2) {

					tv_gist_.setImageResource(R.drawable.icon_qiuzhi_gist_);
					if (TextUtils.isEmpty(answer.getAContent())) {
						tv_gist.setText(mContext.getResources().getString(
								R.string.qiuzhi_gist_revamp));
					} else {
						aContent = SubNewString(aContent, start, end);
						HtmlImageGetter htmlImageGetter = new HtmlImageGetter(
								tv_gist, "/esun", defaultDrawable);
						Spanned sp = Html.fromHtml(aContent, htmlImageGetter,
								null);
						tv_gist.setText(sp);
					}
				}
				// 图片url,字符串（url)json数组
				String thumbnail = answer.getThumbnail();
				if (TextUtils.isEmpty(thumbnail)) {
					gv_gallery.setVisibility(View.GONE);
				} else {
					gv_gallery.setVisibility(View.VISIBLE);
					JSONArray jsonarray = new JSONArray(thumbnail);
					PictureAdapter adapter = new PictureAdapter(mContext,
							jsonarray);
					gv_gallery.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}

	@Override
	public void Destroy() {
		if (defaultDrawable != null) {
			defaultDrawable = null;
		}
	}
	/**
	 * @功能 获取start end 之间的String
	 * @param html
	 * @param start
	 * @param end
	 * @return
	 */
	private String SubNewString(String html, String start, String end) {
		boolean is = true;
		while (is) {
			try {
				if (html.contains(start)) {
					int a = html.indexOf(start);// 第一个字符串的起始位置
					int b = html.indexOf(end);// 第二个字符串的起始位置
					html = html.substring(0, a)
							+ html.substring(b + end.length(), html.length());// 利用substring进行字符串截取
				} else {
					is = false;
				}
			} catch (Exception e) {
				is = false;
			}
		}
		return html;
	}

	public RecommentIndexItem getQuestion() {
		return question;
	}

	public void setQuestion(RecommentIndexItem question) {
		this.question = question;
	}


	@Override
	public void update(QuestionIndexItem qEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(AnswerItem answer) {
		// TODO Auto-generated method stub
		if (answer != null) {
			for (int i = 0; i < mData.size(); i++) {
				RecommentIndexAnswer item = (RecommentIndexAnswer) mData.get(i);
				if (item.getTabID() == answer.getTabID()) {
					item.setCaiCount(answer.getCaiCount());
					item.setCCount(answer.getCCount());
					item.setLikeCount(answer.getLikeCount());
					mData.set(i, (T) item);
				}
			}
		}
		QiuZhiSuggestAnswerListAdapter.this.notifyDataSetChanged();
	}

}
