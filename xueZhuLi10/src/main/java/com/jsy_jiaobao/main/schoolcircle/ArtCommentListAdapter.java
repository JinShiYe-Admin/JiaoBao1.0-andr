package com.jsy_jiaobao.main.schoolcircle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EmojiUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.PersonalSpaceActivity;
import com.jsy_jiaobao.po.personal.Comment;
import com.jsy_jiaobao.po.personal.RefComment;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 文章回复列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class ArtCommentListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private BitmapUtils bitmap;
	private String str_todaytime;
	private String[] str_todaytimes;
	private SimpleDateFormat dateFormat;
	private Date today;
	private ArrayList<Comment> commentsList;
	private ArrayList<RefComment> refcomments;
	private String mainURL;

	public ArtCommentListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
	}

	public void setCommentsData(ArrayList<Comment> commentsList) {
		this.commentsList = commentsList;
	}

	public void setRefCommentsData(ArrayList<RefComment> refcomments) {
		this.refcomments = refcomments;
	}

	public ArrayList<Comment> getCommentsList() {
		return commentsList;
	}

	public ArrayList<RefComment> getRefcomments() {
		return refcomments;
	}

	@Override
	public int getCount() {
		return commentsList == null ? 0 : commentsList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentsList.get(position);
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
				R.layout.layout_artcomment_listitem, position);
		LinearLayout layout_refcomments = viewHolder
				.getView(R.id.artcomment_item_layout_refcomments);
		TextView tv_number = viewHolder.getView(R.id.artcomment_item_tv_number);
		TextView tv_time = viewHolder.getView(R.id.artcomment_item_tv_recdate);
		TextView tv_author = viewHolder.getView(R.id.artcomment_item_tv_author);
		TextView tv_content = viewHolder
				.getView(R.id.artcomment_item_tv_content);
		TextView tv_like = viewHolder.getView(R.id.artcomment_item_tv_like);
		TextView tv_cai = viewHolder.getView(R.id.artcomment_item_tv_cai);
		TextView tv_reply = viewHolder.getView(R.id.artcomment_item_tv_reply);
		ImageView img_photo = viewHolder
				.getView(R.id.artcomment_item_img_photo);

		final Comment comment = (Comment) getItem(position);
		layout_refcomments.removeAllViews();
		tv_number.setText(comment.getNumber());
		String[] str_times = comment.getRecDate().split("T");
		if (str_times[0].equals(str_todaytimes[0])) {
			tv_time.setText(str_times[1]);
		} else {
			tv_time.setText(str_times[0]);
		}
		tv_author.setText(comment.getUserName() + "@"
				+ comment.getUnitShortname());
		tv_content.setText(EmojiUtil.filterEmoji(comment.getCommnets()));
		String url = mainURL + ConstantUrl.photoURL + "?AccID="
				+ comment.getJiaoBaoHao();
		bitmap.display(img_photo, url);
		tv_like.setText("顶(" + comment.getLikeCount() + ")");
		tv_cai.setText("踩(" + comment.getCaiCount() + ")");
		if (!TextUtils.isEmpty(comment.getRefID())) {
			String[] refIDs = comment.getRefID().split(",");
			for (int i = 0; i < refIDs.length; i++) {
				for (int j = 0; j < refcomments.size(); j++) {
					RefComment refComment = refcomments.get(j);
					if (refIDs[i].equals(String.valueOf(refComment.getTabID()))) {
						layout_refcomments.addView(creatRefView(refComment,
								parent));
					}
				}
			}
		}

		img_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.ArticleDetailsActivity_listPhoto));
				Intent intent = new Intent(mContext,
						PersonalSpaceActivity.class);
				intent.putExtra("JiaoBaoHao", comment.getJiaoBaoHao() + "");
				intent.putExtra("UserName", comment.getUserName());
				mContext.startActivity(intent);
			}
		});
		tv_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.ArticleDetailsActivity_listLike));
				ArrayList<Object> post = new ArrayList<Object>();
				post.add(Constant.msgcenter_article_AddScore_like);
				post.add(comment);
				EventBusUtil.post(post);
			}
		});
		tv_cai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent
						.onEvent(
								mContext,
								mContext.getResources()
										.getString(
												R.string.ArticleDetailsActivity_listAgainst));
				ArrayList<Object> post = new ArrayList<Object>();
				post.add(Constant.msgcenter_article_AddScore_cai);
				post.add(comment);
				EventBusUtil.post(post);
			}
		});
		tv_reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.ArticleDetailsActivity_listReply));
				ArrayList<Object> post = new ArrayList<Object>();
				post.add(Constant.msgcenter_article_click_reply);
				post.add(comment);
				EventBusUtil.post(post);
			}
		});
		return viewHolder.getConvertView();
	}

	private View creatRefView(RefComment refComment, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.layout_artcomment_refcomitem,
				parent, false);

		TextView tv_content = (TextView) layout
				.findViewById(R.id.artcomment_refcom_tv_content);
		TextView tv_cai = (TextView) layout
				.findViewById(R.id.artcomment_refcom_tv_cai);
		TextView tv_like = (TextView) layout
				.findViewById(R.id.artcomment_refcom_tv_like);
		tv_like.setText("顶(" + refComment.getLikeCount() + ")");
		tv_cai.setText("踩(" + refComment.getCaiCount() + ")");
		tv_content.setText(refComment.getUserName() + "@"
				+ refComment.getUnitShortname() + ":"
				+ EmojiUtil.filterEmoji(refComment.getCommnets()));
		tv_like.setTag(refComment);
		tv_cai.setTag(refComment);
		tv_cai.setOnClickListener(onClickListener);
		tv_like.setOnClickListener(onClickListener);
		return layout;
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			RefComment refComment = (RefComment) v.getTag();
			switch (v.getId()) {
			case R.id.artcomment_refcom_tv_like:
				ArrayList<Object> post = new ArrayList<Object>();
				post.add(Constant.msgcenter_article_AddScore_like_ref);
				post.add(refComment);
				EventBusUtil.post(post);
				break;
			case R.id.artcomment_refcom_tv_cai:
				ArrayList<Object> post1 = new ArrayList<Object>();
				post1.add(Constant.msgcenter_article_AddScore_cai_ref);
				post1.add(refComment);
				EventBusUtil.post(post1);
			default:
				break;
			}
		}
	};

}
