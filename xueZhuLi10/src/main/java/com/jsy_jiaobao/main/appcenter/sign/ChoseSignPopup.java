package com.jsy_jiaobao.main.appcenter.sign;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy_jiaobao.customview.wheel.OnWheelChangedListener;
import com.jsy_jiaobao.customview.wheel.OnWheelScrollListener;
import com.jsy_jiaobao.customview.wheel.WheelView;
import com.jsy_jiaobao.customview.wheel.adapter.AbstractWheelTextAdapter;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sign.ChildSignWay;
import com.jsy_jiaobao.po.sign.ParentSignWay;

public class ChoseSignPopup {
	// Scrolling flag
	private boolean scrolling = false;
	private PopupWindow ppw;
	private Context mcontext;
	private WheelView parent, child;
	private int parentLocation;
	private List<ChildSignWay> children =new ArrayList<ChildSignWay>();
	private List<ParentSignWay> childrens = Constant.listParentSignWay;
	public ChoseSignPopup(Context context) {
		this.mcontext = context;
	}
	public void showPop(View layout,int y,final TextView tv_parent,final TextView tv_child) {
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		final View popupLayout = inflater.inflate(R.layout.popup_chosesign, null);
		parent = (WheelView)popupLayout.findViewById(R.id.popup_chosesign_parent);
		child = (WheelView)popupLayout.findViewById(R.id.popup_chosesign_child);
		
		ppw = new PopupWindow(mcontext);
		ppw.setBackgroundDrawable(new BitmapDrawable());
		ppw.setWidth(LayoutParams.MATCH_PARENT);
		ppw.setHeight( LayoutParams.WRAP_CONTENT);
		ppw.setOutsideTouchable(false);
		ppw.setFocusable(true);
		ppw.setContentView(popupLayout);
		ppw.showAtLocation(layout, Gravity.CENTER|Gravity.TOP, 0, y);
		
		child.setVisibleItems(5);
		parent.setVisibleItems(3);
		parent.setViewAdapter(new ParentAdapter(mcontext));
		
		
		parent.setCurrentItem(0);
		children = childrens.get(0).getGroupItems();
		parentLocation = 0;
		tv_child.setText(children.get(0).getGroupName());
		updateCities(child, children);
		Constant.SIGNWAY_C = children.get(0);
		Constant.SIGNWAY_P = childrens.get(0);//--------------- 
		parent.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
//					children.clear();
					children = childrens.get(newValue).getGroupItems();
					parentLocation = newValue;
					tv_parent.setText(childrens.get(parentLocation).getGroupTypeName());
					tv_child.setText(children.get(0).getGroupName());
					Constant.SIGNWAY_C = childrens.get(parentLocation).getGroupItems().get(0);
					Constant.SIGNWAY_P = childrens.get(parentLocation);//--------------- 
					updateCities(child, children);
				}
			}
		});

		parent.addScrollingListener( new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
//				children.clear();
				parentLocation = parent.getCurrentItem();
				children = childrens.get(parentLocation).getGroupItems();
				tv_parent.setText(childrens.get(parentLocation).getGroupTypeName());
				tv_child.setText(children.get(0).getGroupName());
				Constant.SIGNWAY_C = childrens.get(parentLocation).getGroupItems().get(0);
				Constant.SIGNWAY_P = childrens.get(parentLocation);//--------------- 
				scrolling = false;
				updateCities(child, children);
			}
		});
		child.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					tv_child.setText(children.get(newValue).getGroupName());
					Constant.SIGNWAY_C = childrens.get(parentLocation).getGroupItems().get(newValue);
					System.out.println("-----签到方式id"+Constant.SIGNWAY_C);
				}
			}
		});
		
		child.addScrollingListener( new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				tv_child.setText(children.get(child.getCurrentItem()).getGroupName());
				Constant.SIGNWAY_C = childrens.get(parentLocation).getGroupItems().get(child.getCurrentItem());
				scrolling = false;
				System.out.println("-----签到方式id"+Constant.SIGNWAY_C);
			}
		});


	}
	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView child, List<ChildSignWay> children) {
		ChildAdapter adapter = new ChildAdapter(mcontext,children);
		adapter.setTextSize(18);
		child.setViewAdapter(adapter);
		child.setCurrentItem(0);
	}
	/**
	 * Adapter for countries
	 */
	private class ChildAdapter extends AbstractWheelTextAdapter {
		
		private List<ChildSignWay> children;
		/**
		 * Constructor
		 */
		protected ChildAdapter(Context context,List<ChildSignWay> children) {
			super(context, R.layout.popup_chosesign_parent, NO_RESOURCE);
			setItemTextResource(R.id.parent_name);
			this.children = children;
		}
		
		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
//			ImageView img = (ImageView) view.findViewById(R.id.parent_flag);
//			img.setImageResource(flags[index]);
			return view;
		}
		
		@Override
		public int getItemsCount() {
			return children.size();
		}
		
		@Override
		protected CharSequence getItemText(int index) {
			return children.get(index).getGroupName();
		}
	}
	/**
	 * Adapter for countries
	 */
	private class ParentAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		protected ParentAdapter(Context context) {
			super(context, R.layout.popup_chosesign_parent, NO_RESOURCE);
			setItemTextResource(R.id.parent_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
//			ImageView img = (ImageView) view.findViewById(R.id.parent_flag);
//			img.setImageResource(flags[index]);
			return view;
		}

		@Override
		public int getItemsCount() {
			return Constant.listParentSignWay.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return Constant.listParentSignWay.get(index).getGroupTypeName();
		}
	}
}
