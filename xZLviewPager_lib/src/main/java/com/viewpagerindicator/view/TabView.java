package com.viewpagerindicator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.R;

public class TabView extends LinearLayout {

	public ImageView icon;
	public ImageView notice;
	public TextView title;
	public int index;
	private Context mContext;
	public TabView(Context context) {
		super(context);
	}

	public TabView(Context context,AttributeSet attrs){
		super(context,attrs);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_tabview, this);
		icon = (ImageView) findViewById(R.id.tabview_icon);
		notice = (ImageView) findViewById(R.id.tabview_notice);
		title = (TextView) findViewById(R.id.tabview_title);
	}
	public TabView(Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
	}
	public void setIndex(int index){
		this.index = index;
		switch (index) {
		case 0:
			title.setText(R.string.bottom_title_qiuzhi);
			icon.setBackgroundResource(R.drawable.bottom_topical_icon);
			break;
//		case 1:
//			title.setText(R.string.bottom_title_notice);
//			icon.setBackgroundResource(R.drawable.bottom_notice_icon);
//			break;
		case 1:
			title.setText(R.string.bottom_title_article);
			icon.setBackgroundResource(R.drawable.bottom_show_icon);
			break;
		case 2:
			title.setText(R.string.bottom_title_work);
			icon.setBackgroundResource(R.drawable.bottom_work_icon);
			break;
//		case 0:
//			title.setText(R.string.bottom_title_chat);
//			icon.setBackgroundResource(R.drawable.bottom_chat_icon);
//			break;
//		case 1:
//			title.setText(R.string.bottom_title_work);
//			icon.setBackgroundResource(R.drawable.bottom_work_icon);
//			break;
//		case 2:
//			title.setText(R.string.bottom_title_notice);
//			icon.setBackgroundResource(R.drawable.bottom_notice_icon);
//			break;
//		case 3:
//			title.setText(R.string.bottom_title_show);
//			icon.setBackgroundResource(R.drawable.bottom_show_icon);
//			break;
//		case 4:
//			title.setText(R.string.bottom_title_topical);
//			icon.setBackgroundResource(R.drawable.bottom_topical_icon);
//			break;

		default:
			break;
		}
	}
	public int getIndex() {
		return index;
	}
}
