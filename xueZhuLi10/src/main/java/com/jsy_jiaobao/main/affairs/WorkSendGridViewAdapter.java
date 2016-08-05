package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.GenRevicer;
import com.jsy_jiaobao.po.sys.Selit;

public class WorkSendGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<Boolean> mChecked;
	private ArrayList<CommMsgRevicerUnitClass> unitClass;
	public WorkSendGridViewAdapter(Context context){
		this.context = context;
        mChecked = new ArrayList<Boolean>();

	}

	public void setData(ArrayList<CommMsgRevicerUnitClass> unitClass) {
		this.unitClass = unitClass;
		mChecked.clear();
		for (int i = 0; i < unitClass.size(); i++) {
			mChecked.add(i,false);
		}
	}
	public void setAllCheck(boolean check){
		for (int i = 0; i < unitClass.size(); i++) {
			mChecked.set(i,check);
		}
	}
	public ArrayList<CommMsgRevicerUnitClass> getCheckedList(){
		ArrayList<CommMsgRevicerUnitClass> unitclass = new ArrayList<CommMsgRevicerUnitClass>();
		for (int i = 0; i < mChecked.size(); i++) {
			if (mChecked.get(i)) {
				unitclass.add(unitClass.get(i));
			}
		}
		return unitclass;
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
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,R.layout.item_worksend_gen_gridview, position);
		CheckBox cb = viewHolder.getView(R.id.item_worksend_gen_cb);
		try {
			CommMsgRevicerUnitClass unitClass = (CommMsgRevicerUnitClass) getItem(position);
			cb.setOnCheckedChangeListener(null);
			cb.setText(unitClass.getClsName().trim());
			GenRevicer genRevicer = unitClass.getUserList();
			ArrayList<Selit> list = null;
			if (genRevicer != null) {
				list = genRevicer.getGenselit();
			}
			if (list == null || list.size() == 0) {
				cb.setChecked(false);
				cb.setClickable(false);
				mChecked.set(position,false);
			}else{
				cb.setChecked(mChecked.get(position));
				cb.setClickable(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mChecked.set(position, isChecked);
			}
		});
		return viewHolder.getConvertView();
	}
}
