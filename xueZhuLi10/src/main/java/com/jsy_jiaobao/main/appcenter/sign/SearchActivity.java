package com.jsy_jiaobao.main.appcenter.sign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.DateWidgetDayCell;
import com.jsy_jiaobao.customview.DateWidgetDayHeader;
import com.jsy_jiaobao.customview.DayStyle;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sign.search.GetDaySign;
import com.jsy_jiaobao.po.sign.search.GetSignInfo;
import com.jsy_jiaobao.po.sign.search.SignInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Android实现日历控件
 * @Description: Android实现日历控件

 * @File: MainActivity.java

 * @Package com.calendar.demo

 * @Author Hanyonglu

 * @Date 2012-6-21 上午11:42:32

 * @Version V1.0
 */
public class SearchActivity extends BaseActivity implements OnRefreshListener2<ScrollView>{
	
	@ViewInject(R.id.pull_refresh_scrollview)private PullToRefreshScrollView mPullRefreshScrollView;
	// 生成日历，外层容器
	private LinearLayout layContent = null;
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();

	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// 当前操作日期
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;
	
	// 页面控件
	@ViewInject(R.id.search_Top_Date)TextView Top_Date;
	@ViewInject(R.id.search_btn_pre_month)Button btn_pre_month;
	@ViewInject(R.id.search_btn_pre_year)Button btn_pre_year;
	@ViewInject(R.id.search_btn_next_month)Button btn_next_month;
	@ViewInject(R.id.search_btn_next_year)Button btn_next_year;
	private Context mContext;
	CusListView signlist = null;
	LinearLayout mainLayout = null;
	LinearLayout arrange_layout = null;

	// 数据源
	ArrayList<String> Calendar_Source = null;
	Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<Integer, Integer>();
	Boolean[] flag = null;
	Calendar startDate = null;
	Calendar endDate = null;
	int dayvalue = -1;

	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;
	public static int isNormal_backColor = 0;

	String UserName = "";
	private int UserId;
	private SharedPreferences sp;
	private List<GetDaySign> list_month;
	
	private SignListAdapter signAdapter;
	
	private boolean isClick = false;
	
	private ProgressDialog dialog;
	@OnClick({R.id.search_btn_pre_month,R.id.search_btn_pre_year,
		R.id.search_btn_next_month,R.id.search_btn_next_year})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.search_btn_pre_month:
			Pre_MonthOnClick();
			httpConnect();
			break;
		case R.id.search_btn_pre_year:
			Pre_YearOnClick();
			httpConnect();
			break;
		case R.id.search_btn_next_month:
			Next_MonthOnClick();
			httpConnect();
			break;
		case R.id.search_btn_next_year:
			Next_YearOnClick();
			httpConnect();
			break;

		default:
			break;
		}
		
	}
	/**
	 * 获取用户签到记录
	 */
	private void httpConnect() {
		try {
			dialog.show();
			if (list_month != null) {
				list_month.clear();
			}
			RequestParams params = new RequestParams();
			params.addBodyParameter("UserID",UserId+"");
			params.addBodyParameter("Year",iMonthViewCurrentYear+"");
			params.addBodyParameter("Month",(iMonthViewCurrentMonth+1)+"");
			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.POST, ACache.get(mContext.getApplicationContext()).getAsString("KaoQUrl")+user_select, params, new RequestCallBack<String>() {
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					if (dialog != null) {
						dialog.dismiss();
					}
					BaseUtils.shortToast(mContext, getResources().getString(R.string.error_serverconnect));
					mPullRefreshScrollView.onRefreshComplete();
				}
				
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						mPullRefreshScrollView.onRefreshComplete();
						if (dialog != null) {
							dialog.dismiss();
						}
						JSONObject jsonObj = new JSONObject(arg0.result);
						String result = jsonObj.getString("ResultCode");
						if ("0".equals(result)) {
							GetSignInfo getListMonth = GsonUtil.GsonToObject(arg0.result, GetSignInfo.class);
							list_month = getListMonth.getData();
							updateCalendar();
						} else {
						}
					} catch (JSONException e) {
						BaseUtils.shortToast(mContext, getResources().getString(R.string.error_serverconnect));
					} 
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;

		// 制定布局文件，并设置属性
		// mainLayout.setPadding(2, 0, 2, 0);
		setContentLayout(R.layout.activity_calendar);
		ViewUtils.inject(this);
		mainLayout = (LinearLayout) findViewById(R.id.layout_calendar);
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);
		mPullRefreshScrollView.setOnRefreshListener(this);
		mContext = this;
		dialog= BaseUtils.showDialog(mContext, R.string.loading);
		setActionBarTitle(R.string.sign_record);
		sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
		UserId = sp.getInt("UserID", 0);
		// 计算本月日历中的第一天(一般是上月的某天)，并更新日历
		calStartDate = getCalendarStartDate();
		mainLayout.addView(generateCalendarMain());
		DateWidgetDayCell daySelected = updateCalendar();

		if (daySelected != null)
			daySelected.requestFocus();

		LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		ScrollView view = new ScrollView(this);
		arrange_layout = createLayout(LinearLayout.VERTICAL);
		arrange_layout.setPadding(5, 2, 0, 0);
		signlist = new CusListView(this, null);
		mainLayout.setBackgroundColor(Color.WHITE);
		arrange_layout.addView(signlist);

		startDate = GetStartDate();
		calToday = GetTodayDate();
		
		endDate = GetEndDate(startDate);
		view.addView(arrange_layout, Param1);
		mainLayout.addView(view);
		signAdapter = new SignListAdapter(this);
		signlist.setAdapter(signAdapter);
		// 新建线程
		new Thread() {
			@Override
			public void run() {
				int day = GetNumFromDate(calToday, startDate);
				
				if (calendar_Hashtable != null && calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
			
		}.start();
		httpConnect();
		Calendar_WeekBgColor = this.getResources().getColor(R.color.Calendar_WeekBgColor);
		Calendar_DayBgColor = this.getResources().getColor(R.color.Calendar_DayBgColor);
		isHoliday_BgColor = this.getResources().getColor(R.color.isHoliday_BgColor);
		unPresentMonth_FontColor = this.getResources().getColor(R.color.unPresentMonth_FontColor);
		isPresentMonth_FontColor = this.getResources().getColor(R.color.isPresentMonth_FontColor);
		isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
		special_Reminder = this.getResources().getColor(R.color.specialReminder);
		common_Reminder = this.getResources().getColor(R.color.commonReminder);
		Calendar_WeekFontColor = this.getResources().getColor(R.color.Calendar_WeekFontColor);
		isNormal_backColor = this.getResources().getColor(R.color.mediumvioletred);
	}

	protected String GetDateShortString(Calendar date) {
		String returnString = date.get(Calendar.YEAR) + "/";
		returnString += date.get(Calendar.MONTH) + 1 + "/";
		returnString += date.get(Calendar.DAY_OF_MONTH);
		
		return returnString;
	}

	//得到当天在日历中的序号
	private int GetNumFromDate(Calendar now, Calendar returnDate) {
		Calendar cNow = (Calendar) now.clone();
		Calendar cReturnDate = (Calendar) returnDate.clone();
		setTimeToMidnight(cNow);
		setTimeToMidnight(cReturnDate);
		
		long todayMs = cNow.getTimeInMillis();
		long returnMs = cReturnDate.getTimeInMillis();
		long intervalMs = todayMs - returnMs;
		int index = millisecondsToDays(intervalMs);
		
		return index;
	}

	private int millisecondsToDays(long intervalMs) {
		return Math.round((intervalMs / (1000 * 86400)));
	}

	private void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	//生成布局
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		
		return lay;
	}

	// 生成日历头部
	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		// layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));
		
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this, Cell_Width,35);
			
			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		
		return layRow;
	}

	//  生成日历主体
	private View generateCalendarMain() {
		layContent = createLayout(LinearLayout.VERTICAL);
		// layContent.setPadding(1, 0, 1, 0);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
		layContent.addView(generateCalendarHeader());
		days.clear();
		
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}
		
		return layContent;
	}

	// 生成日历中的一行，仅画矩形
	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this, Cell_Width,Cell_Width);
			dayCell.setItemClick(mOnDayCellClick);
			days.add(dayCell);
			layRow.addView(dayCell);
		}
		
		return layRow;
	}

	//设置当天日期和被选中日期
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}
		
		UpdateStartDateForMonth();
		return calStartDate;
	}

	//  由于本日历上的日期都是从周一开始的，此方法可推算出上月在本月日历中显示的天数
	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		// update days for week
		UpdateCurrentMonthDisplay();
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;
		
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	// 更新日历
	private DateWidgetDayCell updateCalendar() {
		boolean begin = false;
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		int j =0;
		int daynumber = BaseUtils.getDayNumber(iMonthViewCurrentYear, iMonthViewCurrentMonth+1);
		for (int i = 0; i < days.size(); i++) {
			
			List<SignInfo> list_day =null;
			String isNormal = "2";//1:前。2：后。3：没有数据

			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell = days.get(i);
			if (iDay == 1 && j == 0 ) {
				begin = true;
			}
			if (begin) {
				if(iDay == 1 && j > 0){
					begin = false;
				}
			}
			if (begin) {
				if (iDay>j) {
					isNormal = "1";
					if (list_month != null) {
						if (list_month.size()!=0) {
							try {
								if (null != list_month.get(j)) {
									
									list_day = list_month.get(j).getDaylist();
									if (list_day != null && list_day.size()>0) {
										isNormal = list_month.get(j).getHandleResult();
									}else{
										isNormal = "3";
									}
									
								}
							} catch (Exception e) {
								list_day = null;
								isNormal = "1";
							}
						}
					}
					j++;
				}else{ 
					begin = false;
				}
			}else{
				isNormal = "2";
			}
			// 判断是否当天
			boolean bToday = false;
			
			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						bToday = true;
						if (!isClick) {
							if (list_day == null) {
								if (signAdapter != null) {
									signAdapter.setListData(null);
									signAdapter.notifyDataSetChanged();
								}
							}else{
								signAdapter.setListData(list_day);
								signAdapter.notifyDataSetChanged();
							}
						}
					}
				}
			}

			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			//  是否被选中
			bSelected = false;
			
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth) && (iSelectedYear == iYear)) {
					bSelected = true;
				}
			
			dayCell.setSelected(bSelected);

			//是否有记录
			boolean hasRecord = false;
			
			if (flag != null && flag[i] == true && calendar_Hashtable != null&& calendar_Hashtable.containsKey(i)) {
				// hasRecord = flag[i];
				hasRecord = Calendar_Source.get(calendar_Hashtable.get(i)).contains(UserName);
			}

			if (bSelected)
				daySelected = dayCell;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					iMonthViewCurrentMonth, hasRecord,list_day,isNormal);

			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return daySelected;
	}
	//  更新日历标题上显示的年月
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + getResources().getString(R.string.public_year)
				+ (calStartDate.get(Calendar.MONTH) + 1) + getResources().getString(R.string.public_month);
		Top_Date.setText(date);
	}
	// 点击上年按钮，触发事件
	private void Pre_YearOnClick(){
		signAdapter.setListData(null);
		signAdapter.notifyDataSetChanged();
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentYear--;
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		calStartDate.set(Calendar.MILLISECOND, 0);
		UpdateStartDateForMonth();

		startDate = (Calendar) calStartDate.clone();
		endDate = GetEndDate(startDate);

		// 新建线程
		new Thread() {
			@Override
			public void run() {

				int day = GetNumFromDate(calToday, startDate);
				
				if (calendar_Hashtable != null && calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
		}.start();

		updateCalendar();
	}
	// 点击上月按钮，触发事件
	private void Pre_MonthOnClick(){
		signAdapter.setListData(null);
		signAdapter.notifyDataSetChanged();
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentMonth--;
		
		if (iMonthViewCurrentMonth == -1) {
			iMonthViewCurrentMonth = 11;
			iMonthViewCurrentYear--;
		}
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		calStartDate.set(Calendar.MILLISECOND, 0);
		UpdateStartDateForMonth();

		startDate = (Calendar) calStartDate.clone();
		endDate = GetEndDate(startDate);

		new Thread() {
			@Override
			public void run() {

				int day = GetNumFromDate(calToday, startDate);
				
				if (calendar_Hashtable != null && calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
		}.start();

		updateCalendar();
	}

	// 点击下月按钮，触发事件
	private void Next_MonthOnClick(){
		signAdapter.setListData(null);
		signAdapter.notifyDataSetChanged();
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentMonth++;
		
		if (iMonthViewCurrentMonth == 12) {
			iMonthViewCurrentMonth = 0;
			iMonthViewCurrentYear++;
		}
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		UpdateStartDateForMonth();

		startDate = (Calendar) calStartDate.clone();
		endDate = GetEndDate(startDate);

		new Thread() {
			@Override
			public void run() {
				int day = 5;
				
				if (calendar_Hashtable != null&& calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
		}.start();

		updateCalendar();
	}
	// 点击下年按钮，触发事件
	private void Next_YearOnClick(){
		signAdapter.setListData(null);
		signAdapter.notifyDataSetChanged();
		calSelected.setTimeInMillis(0);
		iMonthViewCurrentYear++;
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
		UpdateStartDateForMonth();
		
		startDate = (Calendar) calStartDate.clone();
		endDate = GetEndDate(startDate);
		
		new Thread() {
			@Override
			public void run() {
				int day = 5;
				
				if (calendar_Hashtable != null && calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
		}.start();
		
		updateCalendar();
	}

	// 点击日历，触发事件
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			
			isClick = true;
			List<SignInfo> list_daysignInfo = item.getSignInfo();
			signAdapter.setListData(list_daysignInfo);
			signAdapter.notifyDataSetChanged();
			if (list_daysignInfo == null) {
				ToastUtil.showMessage(mContext, R.string.no_successSignRecord);
			}
			
			
			item.setSelected(true);
			updateCalendar();
		}
	};

	public Calendar GetTodayDate() {
		Calendar cal_Today = Calendar.getInstance();
		cal_Today.set(Calendar.HOUR_OF_DAY, 0);
		cal_Today.set(Calendar.MINUTE, 0);
		cal_Today.set(Calendar.SECOND, 0);
		cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

		return cal_Today;
	}

	// 得到当前日历中的第一天
	public Calendar GetStartDate() {
		int iDay = 0;
		Calendar cal_Now = Calendar.getInstance();
		cal_Now.set(Calendar.DAY_OF_MONTH, 1);
		cal_Now.set(Calendar.HOUR_OF_DAY, 0);
		cal_Now.set(Calendar.MINUTE, 0);
		cal_Now.set(Calendar.SECOND, 0);
		cal_Now.setFirstDayOfWeek(Calendar.MONDAY);

		iDay = cal_Now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		
		if (iDay < 0) {
			iDay = 6;
		}
		
		cal_Now.add(Calendar.DAY_OF_WEEK, -iDay);
		
		return cal_Now;
	}

	public Calendar GetEndDate(Calendar startDate) {
		// Calendar end = GetStartDate(enddate);
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	finish();
        	break;
		default:
			break;
		}
        return true;
    }
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		httpConnect();
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
	}
	@Override
	public void onPullPageChanging(boolean isChanging) {
		// TODO Auto-generated method stub
		
	}
}