package com.jsy_jiaobao.main.studentrecord;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.StuRec_GenWord;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class WordsListAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;

	// private BitmapUtils bitmap;
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

	private void notifyItem() {
		notifyDataSetChanged();
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

	ImageGetter imgGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			String fileString = JSYApplication.getInstance().FILE_PATH
					+ String.valueOf(source.hashCode());
			// 判断SD卡里面是否存在图片文件
			if (new File(fileString).exists()) {
				// 获取本地文件返回Drawable
				drawable = Drawable.createFromPath(fileString);
				// 设置图片边界
				int with = drawable.getIntrinsicWidth();
				int height = drawable.getIntrinsicHeight();
				System.out.println();
				float scan = height / with;
				if (with > Constant.ScreenWith) {
					height = (int) (Constant.ScreenWith * scan);
					with = Constant.ScreenWith;
				}

				drawable.setBounds(0, 0, with, height);
				return drawable;
			} else {
				// 启动新线程下载
				HttpUtil.getInstance().download(
						source,
						JSYApplication.getInstance().FILE_PATH
								+ String.valueOf(source.hashCode()), callBack);
				return drawable;
			}
		}
	};
	RequestCallBack<File> callBack = new RequestCallBack<File>() {

		@Override
		public void onFailure(HttpException arg0, String arg1) {

		}

		@Override
		public void onSuccess(ResponseInfo<File> arg0) {
			notifyItem();
		}

	};
}
