package com.jsy_jiaobao.main.appcenter.sign;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.sign.search.SignRecord;
import com.jsy_jiaobao.po.sign.search.SignRecordLab;
import com.jsy_jiaobao.po.sign.search.SignRecordModel;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordFragment extends Fragment implements View.OnClickListener, OnRefreshListener2 {
    private static final String DIALOG_DATE = "DialogDate";
    private PullToRefreshScrollView mRefreshScrollView;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mSureButton;
    private Date mStartDate;
    private Date mEndDate;
    private Context mContext;
    private ListView mListView;
    private RecordAdapter mRecordAdapter;
    private static final int START_DATE_CODE = 0;
    private static final int END_DATE_CODE = 1;
    private int pageNum = 1;
    private int RowCount;
    private int numPerPage = 10;
    private static final String TAG = "RecordFragment";
    private List<SignRecord> mSignRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);
        setContentView(v);
        mContext = getActivity();
        return v;
    }


    public RecordFragment() {
    }

    private void setContentView(View v) {
        mRefreshScrollView = (PullToRefreshScrollView) v.findViewById(R.id.pull_refresh_view);
        mRefreshScrollView.setOnRefreshListener(this);
        mStartTimeButton = (Button) v.findViewById(R.id.button_startTime);
        mEndTimeButton = (Button) v.findViewById(R.id.button_endTime);
        mSureButton = (Button) v.findViewById(R.id.button_sure);
        mListView = (ListView) v.findViewById(R.id.list_sign_record);
        mRecordAdapter = new RecordAdapter(getActivity());
        mListView.setAdapter(mRecordAdapter);
        setList();
        mStartDate = new Date();
        mEndDate = new Date();
        updateView();
        mStartTimeButton.setOnClickListener(this);
        mEndTimeButton.setOnClickListener(this);
        mSureButton.setOnClickListener(this);
    }

    private void updateView() {
        mStartTimeButton.setText(formatDate(mStartDate));
        mEndTimeButton.setText(formatDate(mEndDate));
    }

    private void setList() {
        SignRecordLab recordLab = SignRecordLab.get(getActivity());
        mSignRecords = recordLab.getSignRecords();
    }

    private void updateList() {
        mRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_startTime:
                showDialog(START_DATE_CODE);
                break;
            case R.id.button_endTime:
                showDialog(END_DATE_CODE);
                break;
            case R.id.button_sure:
                requestListData();
                break;
            default:
                break;
        }
    }

    private void requestListData() {
        if (!isDateLegal()) {
            Toast.makeText(getActivity(), "请确保结束日期在开始日期之后，且在同一月", Toast.LENGTH_LONG).show();
        } else {
            requestData();
        }
    }

    private void requestData() {
        if (!DialogUtil.getInstance().isDialogShowing()) {
            DialogUtil.getInstance().getDialog(getActivity(), getActivity().getResources().getString(R.string.public_loading));
            DialogUtil.getInstance().setCanCancel(false);
        }

        String accId = BaseActivity.sp.getString("JiaoBaoHao", "");
        RequestParams params = new RequestParams();
        params.addBodyParameter("accId", accId);
        params.addBodyParameter("sDate", formatDate(mStartDate));
        params.addBodyParameter("eDate", formatDate(mEndDate));
        params.addBodyParameter("numPerPage", String.valueOf(numPerPage));
        params.addBodyParameter("pageNum", String.valueOf(pageNum));
        params.addBodyParameter("RowCount", String.valueOf(RowCount));
        HttpUtil.getInstance().send(POST, ConstantUrl.GetMySignInfo, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DialogUtil.getInstance().cannleDialog();
                if (mContext != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(responseInfo.result);
                        String ResultCode = jsonObj.getString("ResultCode");
                        String ResultDesc = jsonObj.getString("ResultDesc");
                        String Data = jsonObj.getString("Data");
                        Log.d(TAG, "ResultCode" + ResultCode);
                        Log.d(TAG, "ResultDesc" + ResultDesc);
                        Log.d(TAG, "Data" + Data);
                        if (!TextUtils.isEmpty(ResultCode)) {
                            if ("0".equals(ResultCode)) {
                                SignRecordModel signRecordModel = GsonUtil.GsonToObject(Data, SignRecordModel.class);
                                Log.d(TAG, signRecordModel.toString());
                                RowCount = signRecordModel.getRowCount();
                                if (pageNum == 1) {
                                    SignRecordLab.get(getActivity()).clearSignRecords();
                                }
                                SignRecordLab.get(getActivity()).addSignRecords(signRecordModel.getInfolist());
                                mRecordAdapter.setRecords(mSignRecords);
                                mRecordAdapter.notifyDataSetChanged();
                                mRefreshScrollView.onRefreshComplete();
                            } else if ("8".equals(ResultCode)) {
                                LoginActivityController.getInstance().helloService(
                                        mContext);
                            } else {
                                ToastUtil.showMessage(mContext,
                                        jsonObj.getString("ResultDesc"));
                            }
                        }
                    } catch (Exception e) {
                        ToastUtil.showMessage(mContext, mContext.getResources()
                                .getString(R.string.error_serverconnect) + "r1002");
                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                DialogUtil.getInstance().cannleDialog();

                if (BaseUtils.isNetworkAvailable(getActivity())) {
                    ToastUtil.showMessage(getActivity(), R.string.phone_no_web);
                } else {
                    ToastUtil.showMessage(getActivity(), R.string.error_internet);
                }
            }
        });
    }

    private boolean isDateLegal() {
        return isInSameMonth() && isDayBefore();
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

    private void showDialog(int code) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mStartDate, mEndDate);
        dialog.setTargetFragment(RecordFragment.this, code);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    /**
     * @return
     */
    private boolean isDayBefore() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(mEndDate);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        return startDay <= endDay;
    }

    private boolean isInSameMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        int startMonth = calendar.get(Calendar.MONTH);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(mEndDate);
        int endMonth = calendar.get(Calendar.MONTH);
        int endYear = calendar.get(Calendar.YEAR);
        return startYear == endYear && startMonth == endMonth;
    }

    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }

    private String formatDate(String s) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return formatter.format(s);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageNum = 1;
        requestListData();

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (pageNum < Math.ceil(RowCount / 10.0)) {
            pageNum++;
            requestListData();
        } else {
            mRefreshScrollView.onRefreshComplete();
        }
    }

    @Override
    public void onPullPageChanging(boolean isChanging) {

    }
}
