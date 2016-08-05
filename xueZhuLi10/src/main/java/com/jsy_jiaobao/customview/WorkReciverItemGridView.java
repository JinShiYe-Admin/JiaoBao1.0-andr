package com.jsy_jiaobao.customview;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.affairs.GridViewAdapter;
import com.jsy_jiaobao.po.sys.Selit;
/**
 * 1级列表
 * @author Administrator
 *
 */
public class WorkReciverItemGridView extends LinearLayout{
	private RelativeLayout layout_parent;
	public ImageView parent_icon;
	public TextView parent_text;
	public ImageView parent_flagview;
	public CusGridView gridview;

	public WorkReciverItemGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_work_reciveritem_gridview, this);

		layout_parent = (RelativeLayout) findViewById(R.id.reciveritem_layout_parent);
		
		layout_parent.setOnClickListener(onclickListener);
		
		parent_icon = (ImageView) findViewById(R.id.reciveritem_parent_icon);
		parent_flagview = (ImageView) findViewById(R.id.reciveritem_parent_flag);
		parent_text = (TextView) findViewById(R.id.reciveritem_parent_text);


		gridview = (CusGridView) findViewById(R.id.reciveritem_child_gridview);
		
		setExpanChild(8);
	}

	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (gridview.getVisibility() == View.VISIBLE) {
				setExpanChild(8);
			}else{
				setExpanChild(0);
			}
		}
	};
	public void setExpanChild(int visibility){
		gridview.setVisibility(visibility);
		if (visibility == 0) {
			parent_flagview.setBackgroundResource(R.drawable.img_expan);
		}else{
			parent_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}
	}
	public void create(int icon,String parent){
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
	}
	public void hideFlagIcon(int visibility){
		parent_flagview.setVisibility(visibility);
	}
	public CusGridView getGridView(){
		if (gridview != null) {
			return gridview;
		}
		return null;
	}
	public void setListData(List<Selit> selitList, GridViewAdapter adapter) {
		adapter.setReciver(selitList);
		adapter.notifyDataSetChanged();
	}
	public void setListAdapter(BaseAdapter adapter){
		gridview.setAdapter(adapter);
	}
	/**
	 * @return 0:layout_parent,
	 * 		   1:listview2.
	 * 		   2:parent_flagview;
	 */
	public View[] getViews(){
		return new View[]{layout_parent,gridview,parent_flagview};
	}
}
