package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.ErrorModel;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 错题本的Adapter
 * 
 * @author admin
 */

public class ErrorCheckAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ErrorModel> mErrorModels;

	public ErrorCheckAdapter(Context context) {
		mContext = context;
	}

	// item数量
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mErrorModels == null ? 0 : mErrorModels.size();
	}

	// item
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mErrorModels == null ? null : mErrorModels.get(position);
	}

	// item Id
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 布置子控件
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ErrorModel errorModel = mErrorModels.get(position);

		ViewHolder0 viewHolder0 = new ViewHolder0();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.workol_list_item_error,
					parent, false);
			// 题号
			viewHolder0.tv_no = (TextView) convertView
					.findViewById(R.id.tv_questionNo);
			// 星星
			viewHolder0.tv_star = (TextView) convertView
					.findViewById(R.id.tv_questionRe);
			// 困难度
			viewHolder0.tv_diff = (TextView) convertView
					.findViewById(R.id.tv_diff);
			// 内容
			viewHolder0.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			// 答案
			viewHolder0.tv_answer = (TextView) convertView
					.findViewById(R.id.tv_answer);
			// 正确答案
			viewHolder0.tv_correctAn = (TextView) convertView
					.findViewById(R.id.tv_correctAn);
			// 解释
			viewHolder0.tv_annotation = (TextView) convertView
					.findViewById(R.id.tv_annotation);
			// 返回View
			convertView.setTag(viewHolder0);
		} else {
			viewHolder0 = (ViewHolder0) convertView.getTag();
		}
		String html = errorModel.getQsCon();
		// 如果问题内容为空
		if (html == null || html.length() == 0) {
			convertView.setLayoutParams(new ListView.LayoutParams(-1, 1));
			convertView.setVisibility(View.GONE);
			return convertView;

		} else {
			// 存在问题内容
			if (convertView.getVisibility() == View.GONE) {
				convertView.setVisibility(View.VISIBLE);
				convertView
						.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
			}
			viewHolder0.tv_no.setText(String.valueOf(errorModel.getTabid()));
			String star = "";
			int i = errorModel.getDoC();
			for (int j = 0; j < i; j++) {
				star = star + "*";
			}
			// 星星
			viewHolder0.tv_star.setText(star);
			// 困难度
			viewHolder0.tv_diff.setText(String.valueOf(errorModel.getQsLv()));
			// 答案
			String qsAns = TextUtils.isEmpty(errorModel.getAnswer()) ? "未作答"
					: errorModel.getAnswer();
			viewHolder0.tv_answer.setText(qsAns);
			// 正确答案
			String qsCoretAnswer = errorModel.getQsCorectAnswer();
			viewHolder0.tv_correctAn.setText(qsCoretAnswer);

			String qsExplain = errorModel.getQsExplain();
			if (qsExplain != null && qsExplain.length() > 0) {
				// 问题解释存在
				viewHolder0.tv_annotation.setVisibility(View.VISIBLE);
				viewHolder0.tv_annotation.setText(qsExplain);
			} else {
				// 问题解释为空
				viewHolder0.tv_annotation.setVisibility(View.GONE);
			}

			viewHolder0.tv_content.setText(html);

			return convertView;
		}
	}

	// 获取上下文
	public Context getContext() {
		return mContext;
	}

	// 设置Context
	public void setContext(Context context) {
		mContext = context;
	}

	// 放置error数据
	public void setErrorModels(ArrayList<ErrorModel> errorModels) {
		mErrorModels = errorModels;
	}

	private class ViewHolder0 {
		TextView tv_no;// 问题题号
		TextView tv_star;// 星号标记
		TextView tv_diff;// 难度
		TextView tv_content;// 内容
		TextView tv_answer;// 答案
		TextView tv_correctAn;// 正确答案
		TextView tv_annotation;// 说明
	}

}
