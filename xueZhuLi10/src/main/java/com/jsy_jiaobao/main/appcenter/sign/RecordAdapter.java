package com.jsy_jiaobao.main.appcenter.sign;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.SignRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocka on 2017/11/8.
 */

public class RecordAdapter extends BaseAdapter {
    private Activity mContext;
    private List<SignRecord> mRecords;
    public RecordAdapter(Activity context) {
        mContext = context;
        mRecords = new ArrayList<>();
    }

    public void setRecords(List<SignRecord> records) {
        mRecords = records;
    }


    @Override
    public int getCount() {
        return mRecords.size();
    }

    @Override
    public Object getItem(int i) {
        return mRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecordHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_recycler_sign_record, null, false);
            holder = new RecordHolder();
            holder.mUserName = (TextView) view.findViewById(R.id.user_name);
            holder.mUnitName = (TextView) view.findViewById(R.id.unit_name);
            holder.mSignDate = (TextView) view.findViewById(R.id.sign_date);
            view.setTag(holder);
        } else {
            holder = (RecordHolder) view.getTag();
        }
        SignRecord item = mRecords.get(i);
        holder.mUserName.setText(item.getUserName());
        holder.mUnitName.setText(item.getUserShortName());
        holder.mSignDate.setText(item.getRecDate());
        return view;
    }

    private class RecordHolder {
        TextView mUserName;
        TextView mUnitName;
        TextView mSignDate;
    }
}
