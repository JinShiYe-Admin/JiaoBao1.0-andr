package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;

public class WorkSendListViewAdapter extends BaseAdapter {
	private Context context;
	public List<Boolean> mChecked;
	private List<SMSTreeUnitID> unitClass;
	private Handler mHandler;
	public WorkSendListViewAdapter(Context context, Handler mHandler){
		this.context = context;
        mChecked = new ArrayList<Boolean>();
        this.mHandler = mHandler;
	}

	public void setData(List<SMSTreeUnitID> sMSList) {
		this.unitClass = sMSList;
		mChecked.clear();
		for (int i = 0; i < sMSList.size(); i++) {
			mChecked.add(i,false);
		}
	}
	public ArrayList<SMSTreeUnitID> getCheckedList(){
		ArrayList<SMSTreeUnitID> unitclass = new ArrayList<SMSTreeUnitID>();
		for (int i = 0; i < mChecked.size(); i++) {
			if (mChecked.get(i)) {
				unitclass.add(unitClass.get(i));
			}
		}
		return unitclass;
	}
	/**
	 * 全选或全不选
	 * @param isChecked
	 */
	public void setAllSelect(boolean isChecked){
		for (int i = 0; i < mChecked.size(); i++) {
			mChecked.set(i, isChecked);
		}
	}
	/**
	 * 反选
	 */
	public boolean setInvert(){
		boolean isall = true;
		for (int i = 0; i < mChecked.size(); i++) {
			mChecked.set(i, !mChecked.get(i));
			if (!mChecked.get(i)) {
				isall = false;
			}
		}
		return isall;
	}
	@Override
	public int getCount() {
		return unitClass == null? 0 : unitClass.size();
	}

	@Override
	public Object getItem(int arg0) {
		return unitClass == null? null : unitClass.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,R.layout.item_worksend_gen_genlist, position);
		CheckBox cb = viewHolder.getView(R.id.item_worksend_gen_gen_cb);
		TextView name = viewHolder.getView(R.id.item_worksend_gen_gen_name);
		SMSTreeUnitID unitClass = (SMSTreeUnitID) getItem(position);
		name.setText(unitClass.getName().trim());
		cb.setOnCheckedChangeListener(null);
		cb.setChecked(mChecked.get(position));
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mChecked.set(position, isChecked);
				Message msg = new Message();
				msg.what = 101;
				mHandler.sendMessage(msg);
			}
		});
//		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
		return viewHolder.getConvertView();
	}
}
