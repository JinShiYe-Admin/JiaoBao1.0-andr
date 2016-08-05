package com.jsy_jiaobao.main.leave;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.jsy_jiaobao.po.leave.LeaveTime;

import android.util.Log;

/**
 * 比较请假时间是否重叠
 * @author ShangLin Mo
 *
 */
public class CompareLeaveTimeList {
	public static Boolean compare(ArrayList<LeaveTime> leaveTimeList) {		
		ArrayList<LeaveTime> mLeaveTimeList = new ArrayList<LeaveTime>();
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < leaveTimeList.size(); i++) {
			Log.i("leaveTimeList:" + i, "Sdate:"
					+ leaveTimeList.get(i).getSdate() + "-Edate:"
					+ leaveTimeList.get(i).getEdate());
			list.add(leaveTimeList.get(i).getSdate());//将所有开始时间添加到一个List
		}
		Collections.sort(list);//开始时间排序
		for (int i = 0; i < list.size(); i++) {
			Log.i("list:" + i, list.get(i));
			for (int j = 0; j < leaveTimeList.size(); j++) {
				if (list.get(i).equals(leaveTimeList.get(j).getSdate())) {//通过比较开始时间，将时间段排序
					mLeaveTimeList.add(leaveTimeList.get(j));//时间段排序
				}
			}
		}
		for (int i = 0; i < mLeaveTimeList.size(); i++) {
			Log.i("mLeaveTimeList:" + i, "Sdate:"
					+ mLeaveTimeList.get(i).getSdate() + "-Edate:"
					+ mLeaveTimeList.get(i).getEdate());
		}
		Log.i("", "---------------------------------");
		if (mLeaveTimeList.size() > leaveTimeList.size()) {
			//开始时间有重复
			Log.e("leaveTimeList", "时间段有重叠(开始时间)");
			return false;
		} else if (mLeaveTimeList.size() == leaveTimeList.size()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for (int i = 0; i < mLeaveTimeList.size() - 1; i++) {
				try {
					//前一个时间段的结束时间与后一个时间段的开始时间比较
					Date dateTime1 = dateFormat.parse(mLeaveTimeList.get(i)
							.getEdate());//前一个时间段的结束时间
					Date dateTime2 = dateFormat.parse(mLeaveTimeList
							.get(i + 1).getSdate());//后一个时间段的开始时间
					Boolean b = dateTime1.before(dateTime2);//true,dateTime1 before dateTime2
					if (b) {
						Log.e("leaveTimeList", "第"+i+"次比时间段未重叠");
					}else {
						Log.e("leaveTimeList", "第"+i+"次比时间段有重叠");
						return false;						
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else {
			Log.e("leaveTimeList", "这不科学！！！");
		}
		return true;

	}
}
