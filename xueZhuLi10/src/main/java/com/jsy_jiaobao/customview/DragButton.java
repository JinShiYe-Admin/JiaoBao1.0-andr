package com.jsy_jiaobao.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class DragButton extends Button {
	// a array for save the drag position
	public int[] location = new int[4];
	public DragButton(Context context, AttributeSet attribute) {
		this(context, attribute, 0);
	}

	public DragButton(Context context, AttributeSet attribute, int style) {
		super(context, attribute, style);
	}
}
