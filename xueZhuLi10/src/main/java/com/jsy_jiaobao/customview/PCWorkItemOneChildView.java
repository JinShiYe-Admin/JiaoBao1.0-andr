package com.jsy_jiaobao.customview;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.affairs.WorkListAdapter;
import com.jsy_jiaobao.po.personal.CommMsg;
/**
 * 1级列表
 * @author Administrator
 *
 */
public class PCWorkItemOneChildView extends LinearLayout{
	public RelativeLayout layout_parent;
	public ImageView parent_icon;
	public TextView parent_text,parent_art;
	public ImageView parent_flagview,more;
	public CusListView listview1;

	public PCWorkItemOneChildView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_pcwork_item_1child, this);

		layout_parent = (RelativeLayout) findViewById(R.id.child1_layout_parent);
		
		layout_parent.setOnClickListener(onclickListener);
		
		parent_icon = (ImageView) findViewById(R.id.child1_parent_icon);
		parent_flagview = (ImageView) findViewById(R.id.child1_parent_flag);
		more = (ImageView) findViewById(R.id.child1_parent_more);
		parent_text = (TextView) findViewById(R.id.child1_parent_text);
		parent_art = (TextView) findViewById(R.id.child1_parent_art);


		listview1 = (CusListView) findViewById(R.id.child1_child1_list);
		setExpanChild(8);
	}

	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (listview1.getVisibility() == View.VISIBLE) {
				setExpanChild(8);
			}else{
				setExpanChild(0);
			}
		}
	};
	public void setExpanChild(int visibility){
		listview1.setVisibility(visibility);
		if (visibility == 0) {
			parent_flagview.setBackgroundResource(R.drawable.img_expan);
		}else{
			parent_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}
	}
	public void create(int icon,String parent){
		if (icon > 0) {
			parent_icon.setImageResource(icon);
		}
		parent_text.setText(parent);
	}
	public void create(int icon,int parentId){
		String parent=getResources().getString(parentId);
		if (icon > 0) {
			parent_icon.setImageResource(icon);
		}
		parent_text.setText(parent);
	}
	public void hideFlagIcon(int visibility){
		parent_flagview.setVisibility(visibility);
		more.setVisibility(visibility);
	}
	public void hideExpanIcon(int visibility){
		parent_flagview.setVisibility(visibility);
	}
	public void hideMoreIcon(int visibility){
		more.setVisibility(visibility);
	}
	public ListView getListView1(){
		if (listview1 != null) {
			return listview1;
		}
		return null;
	}
	public void setListData(List<CommMsg> list, WorkListAdapter adapter) {
		adapter.setData(list);
		adapter.notifyDataSetChanged();
	}
	public void setListAdapter(BaseAdapter adapter){
		listview1.setAdapter(adapter);
	}
	/**
	 * @return 0:layout_parent,
	 * 		   1:listview.
	 * 		   2:more;
	 */
	public View[] getViews(){
		return new View[]{layout_parent,listview1,more};
	}
}
