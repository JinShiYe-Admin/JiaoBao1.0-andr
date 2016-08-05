package com.jsy_jiaobao.main.appcenter.sign;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sign.search.SignInfo;

public class SignListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private List<SignInfo> list_daysignInfo;
	public SignListAdapter(Context context){
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}
	public void setListData(List<SignInfo> list_daysignInfo){
		this.list_daysignInfo = list_daysignInfo;	
	}
	@Override
	public int getCount() {
		if (list_daysignInfo != null) {
			return list_daysignInfo.size()+1;
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		
		if (list_daysignInfo != null && arg0>0) {
			return list_daysignInfo.get(arg0-1);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		}
		return 1;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, arg2,R.layout.listview_sign_item, position);
		TextView tv_time = viewHolder.getView(R.id.sign_item_tv_time);
		TextView tv_type = viewHolder.getView(R.id.sign_item_tv_type);
		TextView tv_place = viewHolder.getView(R.id.sign_item_tv_place);
		SignInfo signInfo = (SignInfo) getItem(position);
		switch (getItemViewType(position)) {
		case 0:
			tv_time.setText(R.string.time);
			tv_type.setText(R.string.way);
			tv_place.setText(R.string.address);
			break;

		case 1:

			if (list_daysignInfo != null) {
				
				tv_time.setText(signInfo.getSignInDateTime());
				String opFlag = signInfo.getSignInFlag();
				String opTimeSlot = signInfo.getSignInTypeName();
				String signInGroupName = signInfo.getSignInGroupName();
				 
				tv_type.setText(opTimeSlot+":\n"+signInGroupName+"\n"+opFlag);
				tv_place.setText(signInfo.getPlace());
			}
			break;
		}

		return viewHolder.getConvertView();
	}

}
