package com.jsy_jiaobao.main.system;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.QiuZhiQuestionAnswerDetailsActivity;
import com.jsy_jiaobao.main.personalcenter.QiuZhiQuestionAnswerListActivity;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.MyAttQItem;
import com.jsy_jiaobao.po.qiuzhi.MyComms;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Subject;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 求知个人中心问题列表的Adapter
 */
public class QiuzhiQuestionListAdapter<T> extends BaseAdapter implements
		ConstantUrl {
	private final static int Q_TYPE_MYATT = 1;// 我关注的问题
	private final static int Q_TYPE_MYQUESTION = 2;// 我提问过的问题
	private final static int Q_TYPE_ATTC = 6;// 我关注的话题
	private final static int Q_TYPE_MYCOMMENT = 8;// 我做出的评论
	private int qType;
	private Context mContext;
	private List<T> mData = new ArrayList<>();

	public QiuzhiQuestionListAdapter(Context mContext) {
		this.mContext = mContext;
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
	 * Cell界面类型
	 */
	@Override
	public int getItemViewType(int position) {
		if (getItem(position) instanceof MyAttQItem) {
			return Q_TYPE_MYATT;
		} else if (getItem(position) instanceof Subject) {
			return Q_TYPE_ATTC;
		} else if (getItem(position) instanceof MyComms) {
			return Q_TYPE_MYCOMMENT;
		}
		return super.getItemViewType(position);
	}

	/**
	 * 生成Cell界面
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		switch (getItemViewType(position)) {
			case Q_TYPE_ATTC:// 我关注的话题
				final Subject subject = (Subject) getItem(position);
				viewHolder = ViewHolder.get(mContext, convertView, parent,
						R.layout.item_qiuzhip_subjectlist, position);
				TextView subname = viewHolder.getView(R.id.qiuzhip_item_tv_subname);
				subname.setText(subject.getSubject().trim());
				break;
			case Q_TYPE_MYATT:// 我关注的问题
				final MyAttQItem myattqitem = (MyAttQItem) getItem(position);
				viewHolder = ViewHolder.get(mContext, convertView, parent,
						R.layout.item_qiuzhip_questionlist, position);
				TextView question = viewHolder
						.getView(R.id.qiuzhip_item_tv_question);
				TextView att = viewHolder.getView(R.id.qiuzhip_item_tv_att);
				TextView answernum = viewHolder
						.getView(R.id.qiuzhip_item_tv_answernum);
				TextView attnum = viewHolder.getView(R.id.qiuzhip_item_tv_attnum);
				TextView tv_q = viewHolder.getView(R.id.qiuzhip_item_tv_q);
				LinearLayout layout_num = viewHolder
						.getView(R.id.qiuzhip_item_layout_num);
				TextView subjectName = viewHolder
						.getView(R.id.qiuzhip_item_tv_subject);
				TextView opennum = viewHolder.getView(R.id.qiuzhip_item_tv_opennum);
				int tabid = myattqitem.getTabID();
				if (tabid == 0) {
					layout_num.setVisibility(View.GONE);
					att.setVisibility(View.GONE);
					tv_q.setVisibility(View.GONE);
					question.setText(myattqitem.getTitle());
					break;
				} else {
					layout_num.setVisibility(View.VISIBLE);
					att.setVisibility(View.VISIBLE);
					tv_q.setVisibility(View.VISIBLE);
				}
				question.setText(myattqitem.getTitle());
				if (this.qType == Q_TYPE_MYATT) {
					att.setVisibility(View.VISIBLE);
					att.setText(mContext.getResources()
							.getString(R.string.unfollow));
					att.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							UserInfo userInfo = (UserInfo) ACache.get(
									mContext.getApplicationContext()).getAsObject(
									"userInfo");

							if (userInfo.isIsKnlFeezeUser()) {
								ToastUtil.showMessage(
										mContext,
										mContext.getResources().getString(
												R.string.public_error_user));
								return;
							}
							if (userInfo.getDUnitId() == 0) {
								ToastUtil.showMessage(
										mContext,
										mContext.getResources().getString(
												R.string.public_error_nounit));
								return;
							}
							Builder builder = new Builder(mContext);

							builder.setTitle(R.string.beSureCancelAttention);
							builder.setMessage(myattqitem.getTitle());
							builder.setNegativeButton(R.string.cancel,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}

							);
							builder.setPositiveButton(R.string.sure,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											QiuzhiQuestionListActivityController
													.getInstance().RemoveMyAttQ(
															myattqitem.getTabID(),
															position);
										}
									});
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					});
				} else if (this.qType == Q_TYPE_MYQUESTION) {// 我回答过的问题
					if (myattqitem.getState() == 0) {
						att.setVisibility(View.VISIBLE);
						Spanned q = Html
								.fromHtml("<font color='#999999'>[屏蔽]</font>");
						att.setText(q);
					} else {
						att.setVisibility(View.GONE);
					}
					question.setText(Html.fromHtml("<font color='#4c4c4c'>"
							+ myattqitem.getTitle() + "</font>"));
				} else {
					att.setVisibility(View.GONE);
				}
				answernum.setText(myattqitem.getAnswersCount() + "个答案");
				attnum.setText(myattqitem.getAttCount() + "人关注");
				subjectName.setText(myattqitem.getCategorySuject().trim());
				opennum.setText(myattqitem.getViewCount() + "人浏览");
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								QuestionIndexItem data = new QuestionIndexItem();
								data.setAnswersCount(myattqitem.getAnswersCount());
								data.setAttCount(myattqitem.getAttCount());
								data.setCategoryId(myattqitem.getCategoryId());
								data.setTabID(myattqitem.getTabID());
								data.setTitle(myattqitem.getTitle());
								data.setViewCount(myattqitem.getViewCount());
								data.setCategorySuject(myattqitem
										.getCategorySuject());
								Intent intent = new Intent(mContext,
										QiuZhiQuestionAnswerListActivity.class);
								intent.putExtra("QuestionIndexItem", data);
								mContext.startActivity(intent);
							}
						});
				break;
			case Q_TYPE_MYCOMMENT:// 我的回复
				final MyComms myComms = (MyComms) getItem(position);
				viewHolder = ViewHolder.get(mContext, convertView, parent,
						R.layout.item_qiuzhip_commlist, position);
				TextView answer = viewHolder.getView(R.id.qiuzhip_comm_answer);
				TextView comment = viewHolder.getView(R.id.qiuzhip_comm_comm);
				comment.setText(myComms.getWContent());
				final AnswerDetails answerDetails = myComms.getAnswerDetails();
				if (answerDetails != null) {
					answer.setText(answerDetails.getATitle());
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext,
										QiuZhiQuestionAnswerDetailsActivity.class);
								Bundle args = new Bundle();
								if (answerDetails != null) {
									args.putSerializable("answerDetails",
											answerDetails);
								} else {
									args.putInt("AnswerID", myComms.getAId());

								}
								args.putInt("QuestionID", myComms.getQId());
								intent.putExtras(args);
								mContext.startActivity(intent);
							}
						});
				break;
			default:
				break;
		}
		return viewHolder.getConvertView();
	}

	public int getqType() {
		return qType;
	}

	public void setqType(int qType) {
		this.qType = qType;
	}
}