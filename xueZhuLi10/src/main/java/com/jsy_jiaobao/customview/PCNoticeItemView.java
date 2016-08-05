package com.jsy_jiaobao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;

/**
 * 三级列表
 * 
 * @author Administrator
 *
 */
public class PCNoticeItemView extends LinearLayout {
	private LinearLayout layout_child;
	private RelativeLayout layout_parent, layout_child1, layout_child2;
	public ImageView parent_icon, parent_more;
	public TextView parent_text, child1_text, child2_text;
	public ImageView parent_flagview, child1_flagview, child2_flagview;
	private ImageView img_child1more, img_child2more;
	public CusListView listview1;
	private TextView parent_badge, child1_badge, child2_badge;

	public PCNoticeItemView(Context context, AttributeSet attrs) {
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
		parent_badge = (TextView) findViewById(R.id.parent_badge);
		parent_flagview.setVisibility(4);

		child1_flagview = (ImageView) findViewById(R.id.child1_flag);
		child2_flagview = (ImageView) findViewById(R.id.child2_flag);
		child1_flagview.setVisibility(0);
		child2_flagview.setVisibility(4);
		img_child1more = (ImageView) findViewById(R.id.child1_more);
		img_child2more = (ImageView) findViewById(R.id.child2_more);

		child1_text = (TextView) findViewById(R.id.child1_text);
		child2_text = (TextView) findViewById(R.id.child2_text);
		child1_badge = (TextView) findViewById(R.id.child1_badge);
		child2_badge = (TextView) findViewById(R.id.child2_badge);

		listview1 = (CusListView) findViewById(R.id.child1_list);
		setExpanListView1(8);
	}

	OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == layout_child1) {
				if (listview1.getVisibility() == View.VISIBLE) {
					setExpanListView1(8);
				} else {
					setExpanListView1(0);
				}
			}
		}
	};

	public void setParentBadge(int number){
		if (number<1) {
			parent_badge.setVisibility(4);
		}else{
			parent_badge.setText(String.valueOf(number));
			parent_badge.setVisibility(0);
		}
	}
	public void setChild1Badge(int number){
		if (number<1) {
			child1_badge.setVisibility(4);
		}else{
			child1_badge.setText(String.valueOf(number));
			child1_badge.setVisibility(0);
		}
	}
	public void setChild2Badge(int number){
		if (number<1) {
			child2_badge.setVisibility(4);
		}else{
			child2_badge.setText(String.valueOf(number));
			child2_badge.setVisibility(0);
		}
	}
	public void setExpanListView1(int visibility) {
		listview1.setVisibility(visibility);
		if (visibility == 0) {
			child1_flagview.setBackgroundResource(R.drawable.img_expan);
		} else {
			child1_flagview.setBackgroundResource(R.drawable.img_unexpan);
		}

	}

	public void hideChild1Expan(int visibility) {
		child1_flagview.setVisibility(visibility);
	}

	public void setParentMoreVisiblity(int visibility) {
		parent_more.setVisibility(visibility);
	}

	/**
	 * 两个孩子列表
	 * 
	 * @param icon
	 * @param parent
	 * @param child1
	 * @param child2
	 */
	public void create(int icon, String parent, String child1, String child2) {
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setText(child1);
		child2_text.setText(child2);
	}

	/**
	 * 一个孩子列表
	 * 
	 * @param icon
	 * @param parent
	 * @param child1
	 */
	public void create(int icon, String parent, String child1) {
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setText(child1);
		layout_child2.setVisibility(8);
	}

	public void create(int icon, String parent) {
		parent_icon.setBackgroundResource(icon);
		parent_text.setText(parent);
		child1_text.setVisibility(8);
		layout_child2.setVisibility(8);
	}

	public ListView getListView1() {
		if (listview1 != null) {
			return listview1;
		}
		return null;
	}


	/**
	 * @return 0:layout_parent, 1:layout_child1, 2:layout_child2, 3:listview1,
	 *         4:img_child1more, 5:img_child2more
	 */
	public View[] getViews() {
		return new View[] { layout_parent, layout_child1, layout_child2,
				listview1, img_child1more, img_child2more };
	}

}
