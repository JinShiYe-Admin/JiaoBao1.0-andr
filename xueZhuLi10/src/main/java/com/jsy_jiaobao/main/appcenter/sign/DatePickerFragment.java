package com.jsy_jiaobao.main.appcenter.sign;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Date;


/**
 * Created by rocka on 2017/11/4.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
