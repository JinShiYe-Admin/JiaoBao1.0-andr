package com.jsy_jiaobao.main.appcenter.workplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.DateWidgetDayCellWP;
import com.jsy_jiaobao.customview.DateWidgetDayHeaderWP;
import com.jsy_jiaobao.customview.DayStyle;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.app.workplan.GetWorkPlanInfo;
import com.jsy_jiaobao.po.app.workplan.WorkPlanInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * 日程记录
 */
public class WorkPlanActivity extends BaseActivity implements View.OnClickListener {
    // 生成日历，外层容器
    private LinearLayout layContent = null;
    private ArrayList<DateWidgetDayCellWP> days = new ArrayList<>();
    // 日期变量
    public static Calendar calStartDate = Calendar.getInstance();
    private Calendar calToday = Calendar.getInstance();
    private Calendar calCalendar = Calendar.getInstance();
    private Calendar calSelected = Calendar.getInstance();

    // 当前操作日期
    private int iMonthViewCurrentMonth = 0;
    private int iMonthViewCurrentYear = 0;
    private int iFirstDayOfWeek = Calendar.MONDAY;
    private int Cell_Width = 0;

    // 页面控件
    private TextView Top_Date;
    LinearLayout mainLayout = null;
    // 数据源
    ArrayList<String> Calendar_Source = new ArrayList<>();
    Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<>();

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

//    String UserName = "";

    private int difference = 3;
    private double between_days = 0;
    private String str_selecttime;

    private Context mContext;
    private int UnitID;
    private int UserID;
    //    private String UnitName;
    private SharedPreferences sp;
    private Intent i = new Intent();
    private WorkPlanListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int   Calendar_Width = size.x;
        Cell_Width = Calendar_Width / 7 + 1;
        setContentLayout(R.layout.calendar_main);
        // 制定布局文件，并设置属性
        mainLayout = (LinearLayout) findViewById(R.id.layout_calendar);
        injectViews();
        mContext = this;
        sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
        UnitID = sp.getInt("UnitID", 0);
        UserID = sp.getInt("UserID", 0);
//        WorkPlanActivityController.getInstance().setContext(this);
        str_selecttime = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
                .format(System.currentTimeMillis());
        // 计算本月日历中的第一天(一般是上月的某天)，并更新日历
        calStartDate = getCalendarStartDate();
        mainLayout.addView(generateCalendarMain());

        CusListView listView = new CusListView(mContext, null);
        mainLayout.addView(listView);
        adapter = new WorkPlanListAdapter(mContext);
        listView.setAdapter(adapter);
        DateWidgetDayCellWP daySelected = updateCalendar();
        if (daySelected != null)
            daySelected.requestFocus();
        startDate = GetStartDate();
        calToday = GetTodayDate();
        endDate = GetEndDate(startDate);

        // 新建线程
        new Thread() {
            @Override
            public void run() {
                int day = GetNumFromDate(calToday, startDate);

                if (calendar_Hashtable != null
                        && calendar_Hashtable.containsKey(day)) {
                    dayvalue = calendar_Hashtable.get(day);
                }
            }

        }.start();

        initColor();

        getDifference();

        setTitleText();

        getRecord(GetDateShortString(calToday));
    }

    private void injectViews() {
        Top_Date = (TextView) findViewById(R.id.search_Top_Date);
        findViewById(R.id.search_btn_pre_month).setOnClickListener(this);
        findViewById(R.id.search_btn_pre_year).setOnClickListener(this);
        findViewById(R.id.search_btn_next_month).setOnClickListener(this);
        findViewById(R.id.search_btn_next_year).setOnClickListener(this);

    }

    private void setTitleText() {
        try {
            String nick = sp.getString("UserName", "");
            if (TextUtils.isEmpty(nick)) {
                editor.putString("UserName", sp.getString("TrueName", ""))
                        .commit();
                nick = sp.getString("TrueName", "");
                if (TextUtils.isEmpty(nick)) {
                    editor.putString("UserName", sp.getString("Nickname", ""))
                            .commit();
                    nick = sp.getString("Nickname", "");
                    if (TextUtils.isEmpty(nick)) {
                        nick = "未知";
                    }
                }
            }
            String unit = sp.getString("UnitName", "");
            if (TextUtils.isEmpty(unit)) {
                unit = "";
            } else {
                unit += ":";
            }
            setActionBarTitle(unit + nick);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getRecord(String selectdate) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("UnitID", UnitID + "");
        params.addBodyParameter("UserID", UserID + "");
        params.addBodyParameter("strSelectDate", selectdate);
        HttpUtils http = new HttpUtils();
        http.send(
                HttpRequest.HttpMethod.POST,
                ACache.get(mContext.getApplicationContext()).getAsString(
                        "RiCUrl")
                        + selectWorkPlanMonth, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        BaseUtils.shortToast(mContext, getResources()
                                .getString(R.string.error_serverconnect));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObj = new JSONObject(arg0.result);
                            String ResultCode = jsonObj.getString("ResultCode");
                            if ("0".equals(ResultCode)) {
                                Calendar_Source.clear();
                                String[] data = jsonObj.getString("Data")
                                        .split(",");
                                Calendar_Source.addAll(Arrays.asList(data));
                                updateCalendar();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
//        EventBusUtil.register(this);
        getRecord(GetDateShortString(calToday));
        super.onResume();
    }

    @Override
    protected void onPause() {
//        EventBusUtil.unregister(this);
        super.onPause();
    }

    private void getDifference() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("UnitID", UnitID + "");
        HttpUtils http = new HttpUtils();
        http.send(
                HttpRequest.HttpMethod.POST,
                ACache.get(mContext.getApplicationContext()).getAsString(
                        "RiCUrl")
                        + getDiffence, params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        BaseUtils.shortToast(mContext, getResources()
                                .getString(R.string.error_serverconnect));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObj = new JSONObject(arg0.result);
                            String ResultCode = jsonObj.getString("ResultCode");
                            if ("0".equals(ResultCode)) {
                                difference = Integer.parseInt(jsonObj
                                        .getString("Data"));
                            } else {
                                BaseUtils.longToast(mContext, "获取用户可延时天数失败");
                            }
                        } catch (Exception e) {
                            BaseUtils.longToast(mContext, "获取用户可延时天数失败");
                        }

                    }
                });
    }

    private void initColor() {
        Calendar_WeekBgColor = this.getResources().getColor(
                R.color.Calendar_WeekBgColor);
        Calendar_DayBgColor = this.getResources().getColor(
                R.color.Calendar_DayBgColor);
        isHoliday_BgColor = this.getResources().getColor(
                R.color.isHoliday_BgColor);
        unPresentMonth_FontColor = this.getResources().getColor(
                R.color.unPresentMonth_FontColor);
        isPresentMonth_FontColor = this.getResources().getColor(
                R.color.isPresentMonth_FontColor);
        isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
        special_Reminder = this.getResources()
                .getColor(R.color.specialReminder);
        common_Reminder = this.getResources().getColor(R.color.commonReminder);
        Calendar_WeekFontColor = this.getResources().getColor(
                R.color.Calendar_WeekFontColor);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn_pre_month:
                Pre_MonthOnClick();
                break;
            case R.id.search_btn_pre_year:
                Pre_YearOnClick();
                break;
            case R.id.search_btn_next_month:
                Next_MonthOnClick();
                break;
            case R.id.search_btn_next_year:
                Next_YearOnClick();
                break;

            default:
                break;
        }
        getRecord(iMonthViewCurrentYear + "-" + iMonthViewCurrentMonth + "-1");
    }

    protected String GetDateShortString(Calendar date) {
        String returnString = date.get(Calendar.YEAR) + "-";
        returnString += date.get(Calendar.MONTH) + 1 + "-";
        returnString += date.get(Calendar.DAY_OF_MONTH);

        return returnString;
    }

    // 得到当天在日历中的序号
    private int GetNumFromDate(Calendar now, Calendar returnDate) {
        Calendar cNow = (Calendar) now.clone();
        Calendar cReturnDate = (Calendar) returnDate.clone();
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);

        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;
        return millisecondsToDays(intervalMs);
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

    // 生成布局
    private LinearLayout createLayout(int iOrientation) {
        LinearLayout lay = new LinearLayout(this);
        lay.setLayoutParams(new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        lay.setOrientation(iOrientation);

        return lay;
    }


    // 生成日历头部
    private View generateCalendarHeader() {
        LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
        // layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));

        for (int iDay = 0; iDay < 7; iDay++) {
            DateWidgetDayHeaderWP day = new DateWidgetDayHeaderWP(this,
                    Cell_Width, 35);

            final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
            day.setData(iWeekDay);
            layRow.addView(day);
        }

        return layRow;
    }

    // 生成日历主体
    private View generateCalendarMain() {
        layContent = createLayout(LinearLayout.VERTICAL);
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
            DateWidgetDayCellWP dayCell = new DateWidgetDayCellWP(this,
                    Cell_Width, Cell_Width);
            dayCell.setItemClick(mOnDayCellClick);
            days.add(dayCell);
            layRow.addView(dayCell);
        }
        return layRow;
    }

    // 设置当天日期和被选中日期
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
    // 由于本日历上的日期都是从周一开始的，此方法可推算出上月在本月日历中显示的天数
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
    private DateWidgetDayCellWP updateCalendar() {
        DateWidgetDayCellWP daySelected = null;
        boolean bSelected ;
        final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
        final int iSelectedYear = calSelected.get(Calendar.YEAR);
        final int iSelectedMonth = calSelected.get(Calendar.MONTH);
        final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
        calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());

        for (int i = 0; i < days.size(); i++) {
            final int iYear = calCalendar.get(Calendar.YEAR);
            final int iMonth = calCalendar.get(Calendar.MONTH);
            final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
            final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
            DateWidgetDayCellWP dayCell = days.get(i);

            // 判断是否当天
            boolean bToday = false;

            if (calToday.get(Calendar.YEAR) == iYear) {
                if (calToday.get(Calendar.MONTH) == iMonth) {
                    if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
                        bToday = true;
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

            // 是否被选中
            bSelected = false;

            if (bIsSelection)
                if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
                        && (iSelectedYear == iYear)) {
                    bSelected = true;
                }

            dayCell.setSelected(bSelected);

            // 是否有记录
            boolean hasRecord = false;
            try {
                if (iYear == calToday.get(Calendar.YEAR)) {
                    if (iMonth == calToday.get(Calendar.MONTH)) {
                        for (String d : Calendar_Source) {
                            if (d.equals(String.valueOf(iDay))) {
                                hasRecord = true;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (bSelected)
                daySelected = dayCell;

            dayCell.setData(iYear, iMonth, iDay, bToday,
                    iMonthViewCurrentMonth, hasRecord);

            calCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        layContent.invalidate();

        return daySelected;
    }

    // 更新日历标题上显示的年月
    private void UpdateCurrentMonthDisplay() {
        String date = calStartDate.get(Calendar.YEAR) + "年"
                + (calStartDate.get(Calendar.MONTH) + 1) + "月";
        Top_Date.setText(date);
    }

    // 点击上年按钮，触发事件
    private void Pre_YearOnClick() {
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

                if (calendar_Hashtable != null
                        && calendar_Hashtable.containsKey(day)) {
                    dayvalue = calendar_Hashtable.get(day);
                }
            }
        }.start();
        updateCalendar();

    }

    // 点击上月按钮，触发事件
    private void Pre_MonthOnClick() {
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

                if (calendar_Hashtable != null
                        && calendar_Hashtable.containsKey(day)) {
                    dayvalue = calendar_Hashtable.get(day);
                }
            }
        }.start();

        updateCalendar();
    }

    // 点击下月按钮，触发事件
    private void Next_MonthOnClick() {
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

                if (calendar_Hashtable != null
                        && calendar_Hashtable.containsKey(day)) {
                    dayvalue = calendar_Hashtable.get(day);
                }
            }
        }.start();

        updateCalendar();
    }

    // 点击下年按钮，触发事件
    private void Next_YearOnClick() {
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

                if (calendar_Hashtable != null
                        && calendar_Hashtable.containsKey(day)) {
                    dayvalue = calendar_Hashtable.get(day);
                }
            }
        }.start();

        updateCalendar();
    }

    // 点击日历，触发事件
    private DateWidgetDayCellWP.OnItemClick mOnDayCellClick = new DateWidgetDayCellWP.OnItemClick() {
        public void OnClick(DateWidgetDayCellWP item) {

            long stime = item.getDate().getTimeInMillis();
            long ttime = calToday.getTimeInMillis();

            between_days = (double) (ttime - stime) / (1000 * 3600 * 24);// 负数代表计划日程
            str_selecttime = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(stime);
            calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
            item.setSelected(true);
            updateCalendar();
            adapter.setListData(null);
            adapter.notifyDataSetChanged();
            if (item.hasRecord) {
                getWorkPlan();
            }
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
        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DAY_OF_MONTH, 41);
        return endDate;
    }

//    Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 10:
//                    String title = (String) msg.obj;
//                    setActionBarTitle(title);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 查询日程
     */
    private void getWorkPlan() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("UnitID", sp.getInt("UnitID", 0) + "");
        params.addBodyParameter("UserID", sp.getInt("UserID", 0) + "");
        params.addBodyParameter("WorkPlanDate", str_selecttime);
        HttpUtils http = new HttpUtils();
        http.send(
                HttpRequest.HttpMethod.POST,
                ACache.get(mContext.getApplicationContext()).getAsString(
                        "RiCUrl")
                        + selectWorkPlanDay, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        BaseUtils.shortToast(mContext, getResources()
                                .getString(R.string.error_serverconnect));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            JSONObject jsonObj = new JSONObject(arg0.result);
                            String ResultCode = jsonObj.getString("ResultCode");
                            if ("0".equals(ResultCode)) {
                                GetWorkPlanInfo getWorkPlanList = GsonUtil
                                        .GsonToObject(arg0.result,
                                                GetWorkPlanInfo.class);
                                List<WorkPlanInfo> list_workplan = getWorkPlanList
                                        .getData();
                                adapter.setListData(list_workplan);
                                adapter.notifyDataSetChanged();
                            } else {
                                BaseUtils.shortToast(mContext, "获取失败");
                            }
                        } catch (Exception e) {
                            BaseUtils.shortToast(mContext, "获取失败");
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu sub_search = menu.addSubMenu("提交").setIcon(
                R.drawable.top_btn_more);
        sub_search.getItem().setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (between_days >= 0) {
                            if (between_days <= difference) {
                                i.setClass(mContext, WriteInfoActivity.class);
                                i.putExtra("selecttime", str_selecttime);
                                i.putExtra("between_days", between_days);
                                startActivity(i);
                            } else {
                                BaseUtils.shortToast(mContext, "超出期限!");
                            }

                        } else {
                            BaseUtils.shortToast(mContext, "不能提前填报日程!");
                        }
                        return false;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}