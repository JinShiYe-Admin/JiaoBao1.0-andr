package com.jsy_jiaobao.main.appcenter.sign;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.jsy_jiaobao.main.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Created by rocka on 2017/11/4.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_START_DATE = "startDate";
    private static final String ARG_END_DATE = "endDate";
    public static final String EXTRA_DATE = "com.jsy_jiaobao.main.appcenter.sign.date";
    DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date startDate, Date endDate) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_START_DATE, startDate);
        args.putSerializable(ARG_END_DATE, endDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date startDate = (Date) getArguments().getSerializable(ARG_START_DATE);
        Date endDate = (Date) getArguments().getSerializable(ARG_END_DATE);
        Calendar calendar = Calendar.getInstance();
        if (getTargetRequestCode() == 1) {
            calendar.setTime(endDate);
        } else {
            calendar.setTime(startDate);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.setMaxDate(getMaxDate(getTargetRequestCode(), startDate, endDate).getTime());
        Date minDate = getMinDate(getTargetRequestCode(), startDate, endDate);
        if (minDate != null) {
            mDatePicker.setMinDate(minDate.getTime());
        }
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setTitle("日期选择")
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date1 = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date1);

                    }
                }).create();
    }

    private Date getShowEndDate(Date start, Date end) {
        if (isInSameMonth(start, end) && isDayBefore(start, end)) {
            return end;
        }
        return start;
    }

    /**
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    private Date getMaxDate(int type, Date startDate, Date endDate) {
        if (type == 0) {//开始时间
            return new Date();
        } else {//结束时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            int endDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
            calendar.set(endYear, endMonth, endDay);
            if (new Date().before(calendar.getTime())) {
                return new Date();
            }
            return calendar.getTime();
        }
    }

    private Date getMinDate(int type, Date startDate, Date endDate) {
        if (type == 0) {//开始时间
//            try {
//                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2017-10-10");
//            } catch (Exception e) {
            return null;
//            }
        } else {//结束时间
            return startDate;
        }
    }

    /**
     * @return
     */
    private boolean isDayBefore(Date mStartDate, Date mEndDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(mEndDate);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        return startDay <= endDay;
    }

    private boolean isInSameMonth(Date mStartDate, Date mEndDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        int startMonth = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(mEndDate);
        int endMonth = calendar.get(Calendar.MONTH);
        int endYear = calendar.get(Calendar.YEAR);
        return startYear == endYear && startMonth == endMonth;
    }

    private void sendResult(int code, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), code, intent);
    }
}
