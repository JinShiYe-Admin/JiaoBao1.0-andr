package com.jsy_jiaobao.po.leave;

import java.util.ArrayList;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Spinner的Adapter
 * 
 * @author admin
 * 
 */
public class SpinnerAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> list;

	public SpinnerAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public SpinnerAdapter(Context context, ArrayList<String> list) {
		// TODO Auto-generated constructor stub\
		this.list = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String name = list.get(position);
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.define_simple_spinner, position);
		TextView txt = viewHolder.getView(android.R.id.text1);
		txt.setText(name);
		return viewHolder.getConvertView();
	}

}
