package com.jsy_jiaobao.po.sign.search;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocka on 2017/11/6.
 */

public class SignRecordLab {
    private static SignRecordLab sSignRecordLab;
    private List<SignRecord> mSignRecords;

    public static SignRecordLab get(Context context) {
        if (sSignRecordLab == null) {
            sSignRecordLab = new SignRecordLab(context);
        }
        return sSignRecordLab;
    }

    private SignRecordLab(Context context) {
        mSignRecords = new ArrayList<>();
    }

    public List<SignRecord> getSignRecords() {
        return mSignRecords;
    }
    public void addSignRecords(List<SignRecord> records){
        mSignRecords.addAll(records);
    }

    public void clearSignRecords() {
        mSignRecords.clear();
    }
}
