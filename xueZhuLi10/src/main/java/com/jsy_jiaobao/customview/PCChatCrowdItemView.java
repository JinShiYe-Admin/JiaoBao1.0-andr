package com.jsy_jiaobao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
/**
 * 1级列表
 * @author Administrator
 *
 */
public class PCChatCrowdItemView extends LinearLayout{
	public RelativeLayout layout_parent;
	public ImageView parent_icon;
	public TextView parent_text,parent_art;
	public ImageView parent_flagview;
	public ExpandableListView listview;

	public PCChatCrowdItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_pcchat_crowditem, this);

		layout_parent = (RelativeLayout) findViewById(R.id.crowd_layout_parent);
		
		this.setOnClickListener(onclickListener);
		
		parent_icon = (ImageView) findViewById(R.id.crowd_parent_icon);
		parent_flagview = (ImageView) findViewById(R.id.crowd_parent_flag);
		parent_text = (TextView) findViewById(R.id.crowd_parent_text);
		parent_art = (TextView) findViewById(R.id.crowd_parent_art);


		listview = (ExpandableListView) findViewById(R.id.crowd_child_listview);
		listview.setGroupIndicator(null);
		
		setExpanChild(8);
	}

	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (listview.getVisibility() == View.VISIBLE) {
				setExpanChild(8);
			}else{
				setExpanChild(0);
			}
		}
	};
	public void setExpanChild(int visibility){
		listview.setVisibility(visibility);
		if (visibility == 0) {
			parent_flagview.setBackgroundResource(R.drawable.icon_crowd_minus);
		}else{
			parent_flagview.setBackgroundResource(R.drawable.icon_crowd_plus);
		}
	}
	public void create(int icon,String parent){
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
	}
	public void hideFlagIcon(int visibility){
		parent_flagview.setVisibility(visibility);
	}
	public void hideExpanIcon(int visibility){
		parent_flagview.setVisibility(visibility);
	}
	/**
	 * @return 0:layout_parent,
	 * 		   1:listview.
	 * 		   2:more;
	 */
	public View[] getViews(){
		return new View[]{layout_parent,listview};
	}
}
