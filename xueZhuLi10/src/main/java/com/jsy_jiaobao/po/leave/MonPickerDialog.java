package com.jsy_jiaobao.po.leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

/**
 * 重写datePicker 1.只显示 年-月 2.title 只显示 年-月
 * 
 * @author lmw
 */
public class MonPickerDialog extends DatePickerDialog {
	public MonPickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.setTitle(year + "年" + (monthOfYear + 1) + "月");

		/*
		 * ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
		 * .getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
		 */
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(year + "年" + (month + 1) + "月");
	}

	/**
	 * 返回键不调用onDateSet方法
	 */
	@Override
	protected void onStop() {
		// super.onStop();
	}

	@Override
	public void show() {
		super.show();
		int SDKVersion = this.getSDKVersionNumber();// 获取系统版本
		System.out.println("SDKVersion = " + SDKVersion);
		DatePicker dp = this.getDatePicker();
		NumberPicker view0 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp
				.getChildAt(0)).getChildAt(0)).getChildAt(0); // 获取最前一位的宽度
		NumberPicker view1 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp
				.getChildAt(0)).getChildAt(0)).getChildAt(1); // 获取中间一位的宽度
		NumberPicker view2 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp
				.getChildAt(0)).getChildAt(0)).getChildAt(2);// 获取最后一位的宽度

		// 年的最大值为2100
		// 月的最大值为11
		// 日的最大值为28,29,30,31
		int value0 = view0.getMaxValue();// 获取第一个View的最大值
		int value1 = view1.getMaxValue();// 获取第二个View的最大值
		int value2 = view2.getMaxValue();// 获取第三个View的最大值
		if (value0 >= 28 && value0 <= 31) {
			view0.setVisibility(View.GONE);
		} else if (value1 >= 28 && value1 <= 31) {
			view1.setVisibility(View.GONE);
		} else if (value2 >= 28 && value2 <= 31) {
			view2.setVisibility(View.GONE);
		}
	}

	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String dateToStr(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}

	/**
	 * 获取DatePicker
	 * 
	 * @param group
	 * @return
	 */
	private DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * 获取系统SDK版本
	 * 
	 * @return
	 */
	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}
}
