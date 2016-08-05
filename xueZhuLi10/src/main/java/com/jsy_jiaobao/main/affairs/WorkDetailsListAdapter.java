package com.jsy_jiaobao.main.affairs;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.FeeBack;

public class WorkDetailsListAdapter extends BaseAdapter {
	private Context context;
	private List<FeeBack> feebackList;
	public WorkDetailsListAdapter(Context context){
		this.context = context;
	}
	
	public void setData(List<FeeBack> feebackList) {
		this.feebackList = feebackList;
	}
	@Override
	public int getCount() {
		if (feebackList != null) {
			return feebackList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return feebackList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,R.layout.msgdetails_listview_item, position);
		FeeBack item = (FeeBack) getItem(position);
		viewHolder.setText(R.id.msgdetails_listview_tv_author,item.getUserName());
		viewHolder.setText(R.id.msgdetails_listview_tv_content,item.getFeeBackMsg());
		return viewHolder.getConvertView();
	}


}
