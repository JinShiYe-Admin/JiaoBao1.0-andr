package com.jsy_jiaobao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class IRelativeLayout extends RelativeLayout {

	public int layoutHeight;
	private DragButton button;
	public IRelativeLayout(Context context,AttributeSet attrs) {
		super(context,attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	
	public DragButton getButton() {
		return button;
	}

	public void setButton(DragButton button) {
		this.button = button;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		layoutHeight = b;
		if (button != null) {
			button.layout(button.location[0], button.location[1], button.location[2], button.location[3]);
		}
	}

}
