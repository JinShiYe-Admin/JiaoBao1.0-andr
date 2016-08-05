package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;

public class WorkSendUnitExpanListAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private ArrayList<GroupUserList> groupList;
	private Handler mHandler;
	public ArrayList<ArrayList<Boolean>> mChecked = new ArrayList<>();
	public WorkSendUnitExpanListAdapter(Context fragmentActivity, Handler mHandler){
		this.mContext = fragmentActivity;
		this.mHandler = mHandler;
	}
	public void setData(ArrayList<GroupUserList> selitList) {
		this.groupList = selitList;
		mChecked.clear();
		for(GroupUserList groupList : selitList){
			ArrayList<Boolean> childList = new ArrayList<>();
			for (int i = 0; i < groupList.getGroupselit_selit().size(); i++) {
				childList.add(i,false);
			}
			mChecked.add(childList);
		}
	}
	 //-----------------Child----------------//  
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (null != groupList) {
			return groupList.get(groupPosition).getGroupselit_selit().get(childPosition);
		}
		return null;
	}
	/**
	 * 获取一条人员信息选中状态
	 * @param groupPosition g
	 * @param childPosition c
	 * @return boolean
	 */
	private boolean getSelitCheckFlag(int groupPosition, int childPosition){
		try {
			if (null != mChecked) {
				return mChecked.get(groupPosition).get(childPosition);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 设置一条人员选中状态
	 * @param groupPosition g
	 * @param childPosition c
	 * @param checkFlag c
	 */
	public void setSelitCheckFlag(int groupPosition, int childPosition,boolean checkFlag){
		try {
			mChecked.get(groupPosition).set(childPosition,checkFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 操作全部
	 * @param checkFlag c
	 */
	public void setAllSelitCheckFlag(boolean checkFlag){
		for (int i = 0; i < mChecked.size(); i++) {
			ArrayList<Boolean> childList = mChecked.get(i);
			for (int j = 0; j < childList.size(); j++) {
				childList.set(j, checkFlag);
			}
		}
	}
	/**
	 * 全部反选
	 */
	public boolean setAllSelitInvert(){
		boolean selectall = true;
		for (int i = 0; i < mChecked.size(); i++) {
			ArrayList<Boolean> childList = mChecked.get(i);
			for (int j = 0; j < childList.size(); j++) {
				boolean flag = !getSelitCheckFlag(i,j);
				childList.set(j,flag);
				if (!flag) {
					selectall = false;
				}
			}
		}
		return selectall;
	}
	/**
	 * 返回被选中的人员列表
	 * @return arrayList
	 */
	public ArrayList<Selit> getSelectSelit(){
		ArrayList<Selit> list = new ArrayList<>();
		for (int i = 0; i < mChecked.size(); i++) {
			ArrayList<Boolean> childList = mChecked.get(i);
			for (int j = 0; j < childList.size(); j++) {
				if (childList.get(j)) {
					try {
						list.add(groupList.get(i).getGroupselit_selit().get(j));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		if (null != groupList && groupList.size()>0) {
			ArrayList<Selit> list = groupList.get(groupPosition).getGroupselit_selit();
			if (null != list) {
				int size = list.size();
				return size%2==1?size/2+1:size/2;
			}
		}
		return 0;
	}
	@Override
	public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.item_expan_worksend_child, childPosition);
		CheckBox checkBox1 = viewHolder.getView(R.id.item_expan_worksend_child_cb1);
		CheckBox checkBox2 = viewHolder.getView(R.id.item_expan_worksend_child_cb2);
		Selit selit1 = (Selit) getChild(groupPosition, childPosition*2);
		checkBox1.setText(selit1.getName());
		checkBox1.setOnCheckedChangeListener(null);
		checkBox2.setOnCheckedChangeListener(null);
		checkBox1.setChecked(getSelitCheckFlag(groupPosition, childPosition*2));
		if (null != groupList && groupList.size()>0) {
			ArrayList<Selit> list = groupList.get(groupPosition).getGroupselit_selit();
			if (null != list) {
				int size = list.size();
				if (childPosition*2+1>=size) {
					checkBox2.setVisibility(View.GONE);
				}else{
					checkBox2.setVisibility(View.VISIBLE);
					Selit selit2 = (Selit) getChild(groupPosition, childPosition*2+1);
					checkBox2.setText(selit2.getName());
					checkBox2.setChecked(getSelitCheckFlag(groupPosition, childPosition*2+1));
					checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							setSelitCheckFlag(groupPosition, childPosition*2+1,isChecked);
							Message msg = new Message();
							msg.what = 101;
							mHandler.sendMessage(msg);
						}
					});
				}
			}
		}
		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setSelitCheckFlag(groupPosition, childPosition*2,isChecked);
				Message msg = new Message();
				msg.what = 101;
				mHandler.sendMessage(msg);
			}
		});

		return viewHolder.getConvertView();
	}
	 //----------------Group----------------//  

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public int getGroupCount() {
		if (null != groupList) {
			return groupList.size();
		}
		return 0;
	}
	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.item_expan_worksend, groupPosition);
		TextView name = viewHolder.getView(R.id.item_expan_worksend_name);
		CheckBox invert = viewHolder.getView(R.id.item_expan_worksend_cb_invert);
		CheckBox cb_all = viewHolder.getView(R.id.item_expan_worksend_cb_all);
		ImageView flag = viewHolder.getView(R.id.item_expan_worksend_flag);
		ImageView expan = viewHolder.getView(R.id.item_expan_worksend_expan);
		GroupUserList string = groupList.get(groupPosition);
		name.setText(string.getGroupName());
		if (isExpanded) {
			expan.setImageResource(R.drawable.icon_worksend_selit_minus);
			flag.setImageResource(R.drawable.icon_worksend_selit_minus_m);
		}else{
			expan.setImageResource(R.drawable.icon_worksend_selit_plus);
			flag.setImageResource(R.drawable.icon_worksend_selit_plus_p);
		}
		cb_all.setOnCheckedChangeListener(null);
		
		cb_all.setChecked(true);
		x:for (int i = 0; i < mChecked.get(groupPosition).size(); i++) {
			if (!getSelitCheckFlag(groupPosition, i)) {
				cb_all.setChecked(false);
				break x;
			}
		}
		cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for (int i = 0; i < mChecked.get(groupPosition).size(); i++) {
					setSelitCheckFlag(groupPosition, i,isChecked);
				}
				Message msg = new Message();
				msg.what = 101;
				mHandler.sendMessage(msg);
			}
		});
		invert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < mChecked.get(groupPosition).size(); i++) {
					setSelitCheckFlag(groupPosition, i,!getSelitCheckFlag(groupPosition, i));
				}
				Message msg = new Message();
				msg.what = 101;
				mHandler.sendMessage(msg);
			}
		});
		viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 100;
				msg.arg1 = groupPosition;
				msg.obj = isExpanded;
				mHandler.sendMessage(msg);
			}
		});
        return viewHolder.getConvertView(); 
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
//	//创建组/子视图    
//    public TextView getGenericView(String s) {    
//        // Layout parameters for the ExpandableListView    
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 70);  
//
//        TextView text = new TextView(mContext);    
//        text.setLayoutParams(lp); 
//        text.setTextSize(16);
//        text.setBackgroundColor(mContext.getResources().getColor(R.color.sendmessage_bg));
//        // Center the text vertically    
//        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);    
//        // Set the text starting position    
//        text.setPadding(50, 2, 0, 2);    
//            
//        text.setText(s);    
//        return text;    
//    }



}
