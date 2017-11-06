package com.jsy_jiaobao.main.appcenter.sign;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.SignRecord;
import com.jsy_jiaobao.po.personal.SignRecordLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordFragment extends Fragment implements View.OnClickListener {
    private static final String DIALOG_DATE = "DialogDate";
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSureButton;
    private Date mStartDate;
    private Date mEndDate;
    private RecyclerView mRecyclerView;
    private RecordAdapter mRecordAdapter;
    private static final int START_DATE_CODE = 0;
    private static final int END_DATE_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        setContentView(v);
        return v;
    }


    public RecordFragment() {
    }

    private void setContentView(View v) {
        mStartTimeButton = (Button) v.findViewById(R.id.button_startTime);
        mEndTimeButton = (Button) v.findViewById(R.id.button_endTime);
        mSureButton = (Button) v.findViewById(R.id.button_sure);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_sign_record);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setList();
        mStartDate = new Date();
        mEndDate = new Date();
        updateView();
        mStartTimeButton.setOnClickListener(this);
        mEndTimeButton.setOnClickListener(this);
    }

    private void updateView() {
        mStartTimeButton.setText(formatDate(mStartDate));
        mEndTimeButton.setText(formatDate(mEndDate));
    }

    private void setList() {
        SignRecordLab recordLab = SignRecordLab.get(getActivity());
        List<SignRecord> records = recordLab.getSignRecords();
        mRecordAdapter = new RecordAdapter(records);
        mRecyclerView.setAdapter(mRecordAdapter);
    }

    private void updateList() {
        mRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_startTime:
                showDialog(mStartDate, START_DATE_CODE);
                break;
            case R.id.button_endTime:
                showDialog(mEndDate, END_DATE_CODE);
                break;
            case R.id.button_sure:
                //TODO 请求数据
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case START_DATE_CODE:
                mStartDate = getChoseDate(data);
                break;
            case END_DATE_CODE:
                mEndDate = getChoseDate(data);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
        updateView();
    }

    private Date getChoseDate(Intent data) {
        return (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
    }

    private void showDialog(Date date, int code) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(date);
        dialog.setTargetFragment(RecordFragment.this, code);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }

    private String formatDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return formatter.format(s);
    }

    private class RecordHolder extends RecyclerView.ViewHolder {
        private TextView mUserName;
        private TextView mUnitName;
        private TextView mDateText;
        private SignRecord mRecord;

        private RecordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_recycler_sign_record, parent, false));
            mUserName = (TextView) itemView.findViewById(R.id.user_name);
            mUnitName = (TextView) itemView.findViewById(R.id.unit_name);
            mDateText = (TextView) itemView.findViewById(R.id.date_text);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        void bind(SignRecord record) {
            mRecord = record;
            mDateText.setText(formatDate(mRecord.getRecDate()));
            mUnitName.setText(mRecord.getUserShortName());
            mUserName.setText(mRecord.getUserName());
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder> {
        private List<SignRecord> mRecordList;
        private RecordHolder mHolder;

        public RecordAdapter(List<SignRecord> recordList) {
            mRecordList = recordList;
        }

        @Override
        public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mHolder = new RecordHolder(LayoutInflater.from(getActivity()), parent);
            return mHolder;
        }

        @Override
        public void onBindViewHolder(RecordHolder holder, int position) {
            mHolder.bind(mRecordList.get(position));
        }

        @Override
        public int getItemCount() {
            return mRecordList.size();
        }
    }
}
