package com.jsy_jiaobao.main.studentrecord;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.StuRec_GenWord;

import java.util.List;

public class WordsListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;

	public WordsListAdapter(Context mContext) {
		this.mContext = mContext;
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
				R.layout.layout_words_item, position);
		TextView title = viewHolder.getView(R.id.words_tv_title);
		WebView content = viewHolder.getView(R.id.words_web_content);
		TextView author = viewHolder.getView(R.id.words_tv_author);
		try {
			final StuRec_GenWord GenWord = (StuRec_GenWord) getItem(position);
			title.setText(GenWord.getQP_TITLE());
			author.setText(GenWord.getREC_USERTNAME());
			String ht = "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /> <style type=\"text/css\">img{text-align:left;} body { max-width: 100%;}</style></head>";
			String body = "<body>" + GenWord.getQP_CONTENT() + "</body>";
			content.loadDataWithBaseURL(null, ht + body, "text/html", "utf-8",
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}
}