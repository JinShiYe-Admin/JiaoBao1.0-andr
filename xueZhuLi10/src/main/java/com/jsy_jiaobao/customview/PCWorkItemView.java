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
import com.jsy_jiaobao.po.personal.FeeBack;
/**
 * 三级列表
 * @author Administrator
 *
 */
public class PCWorkItemView extends LinearLayout{
	private LinearLayout layout_child;
	private RelativeLayout layout_parent,layout_child1,layout_child2;
	public ImageView parent_icon,parent_more;
	public TextView parent_text,child1_text,child2_text;
	public ImageView parent_flagview,child1_flagview,child2_flagview;
	private ImageView img_child1more,img_child2more;
	public CusListView listview1,listview2;

	public PCWorkItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_pcwork_item, this);

		layout_parent = (RelativeLayout) findViewById(R.id.layout_parent);
		layout_child1 = (RelativeLayout) findViewById(R.id.layout_child1);
		layout_child2 = (RelativeLayout) findViewById(R.id.layout_child2);
		
		layout_parent.setOnClickListener(onclickListener);
		layout_child1.setOnClickListener(onclickListener);
		layout_child2.setOnClickListener(onclickListener);
		
		layout_child = (LinearLayout) findViewById(R.id.layout_child);

		parent_icon = (ImageView) findViewById(R.id.parent_icon);
		parent_flagview = (ImageView) findViewById(R.id.parent_flag);
		parent_more = (ImageView) findViewById(R.id.parent_more);
		parent_text = (TextView) findViewById(R.id.parent_text);

		child1_flagview = (ImageView) findViewById(R.id.child1_flag);
		child2_flagview = (ImageView) findViewById(R.id.child2_flag);
		
		img_child1more = (ImageView) findViewById(R.id.child1_more);
		img_child2more = (ImageView) findViewById(R.id.child2_more);

		child1_text = (TextView) findViewById(R.id.child1_text);
		child2_text = (TextView) findViewById(R.id.child2_text);

		listview1 = (CusListView) findViewById(R.id.child1_list);
		listview2 = (CusListView) findViewById(R.id.child2_list);
		
		setExpanChild(8);
		setExpanListView1(8);
		setExpanListView2(8);
	}

	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == layout_child1) {
				if (listview1.getVisibility() == View.VISIBLE) {
					setExpanListView1(8);
				}else{
					setExpanListView1(0);
				}
			}else if(v == layout_child2){
				if (listview2.getVisibility() == View.VISIBLE) {
					setExpanListView2(8);
				}else{
					setExpanListView2(0);
				}
			}else if(v == layout_parent){
				if (layout_child.getVisibility() == View.VISIBLE) {
					setExpanChild(8);
				}else{
					setExpanChild(0);
				}
			}
		}
	};
	public void setExpanChild(int visibility){
		layout_child.setVisibility(visibility);
		if (visibility == 0) {
			parent_flagview.setBackgroundResource(R.drawable.img_expan);
		}else{
			parent_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}
	}
	public void setExpanListView1(int visibility){
		listview1.setVisibility(visibility);
		if (visibility == 0) {
			child1_flagview.setBackgroundResource(R.drawable.img_expan);
		}else{
			child1_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}
		
	}
	public void setExpanListView2(int visibility){
		listview2.setVisibility(visibility);
		if (visibility == 0) {
			child2_flagview.setBackgroundResource(R.drawable.img_expan);
		}else{
			child2_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}
	}
	public void setParentMoreVisiblity(int visibility){
		parent_more.setVisibility(visibility);
	}
	/**
	 * 两个孩子列表
	 * @param icon
	 * @param parent
	 * @param child1
	 * @param child2
	 */
	public void create(int icon,String parent,String child1,String child2){
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setText(child1);
		child2_text.setText(child2);
	}
	public void create(int icon,int parentId,int child1Id,int child2Id){
		parent_icon.setBackgroundResource(icon);
		
		parent_text.setText(getResources().getString(parentId));
		child1_text.setText(getResources().getString(child1Id));
		child2_text.setText(getResources().getString(child2Id));
	}
	/**
	 * 一个孩子列表
	 * @param icon
	 * @param parent
	 * @param child1
	 */
	public void create(int icon,String parent,String child1){
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setText(child1);
		layout_child2.setVisibility(8);
	}
	public void create(int icon,String parent){
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setVisibility(8);
		layout_child2.setVisibility(8);
	}
	
	public ListView getListView1(){
		if (listview1 != null) {
			return listview1;
		}
		return null;
	}
	public ListView getListView2(){
		if (listview2 != null) {
			return listview2;
		}
		return null;
	}
	public void setListData(List<CommMsg> list, WorkListAdapter adapter) {
		adapter.setData(list);
		adapter.notifyDataSetChanged();
	}
	public void setListFBData(List<FeeBack> list,WorkListAdapter adapter) {
		adapter.setFBData(list);
		adapter.notifyDataSetChanged();
	}
	public void setList1Adapter(BaseAdapter adapter){
		listview1.setAdapter(adapter);
	}
	public void setList2Adapter(BaseAdapter adapter){
		listview2.setAdapter(adapter);
	}
	/**
	 * @return 0:layout_parent,
	 * 		   1:layout_child1,
	 * 		   2:layout_child2,
	 * 		   3:listview1,
	 * 		   4:listview2;
	 * 		   5img_child1more,6img_child2more
	 */
	public View[] getViews(){
		return new View[]{layout_parent,layout_child1,layout_child2,listview1,listview2,img_child1more,img_child2more};
	}

}
