package com.jsy_jiaobao.main.leave;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.LeaveUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LeaveDetail;
import com.jsy_jiaobao.po.leave.LeaveTime;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.jsy_jiaobao.po.leave.UpdateLeaveModelPost;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 修改假条功能
 *
 * @author ShangLin Mo
 */
public class ModifyLeaveActivity extends BaseActivity implements
        OnClickListener, OnItemSelectedListener, TextWatcher {
    private Context mContext;
    private int post = 0;//记录修改假条成功后获取回调次数
    private int number = 0;//记录需要发送数据的次数
    private boolean commit = false;//确定提交修改，0 否；1 是
    private String typeChose;// 选择的请假类型

    private LeaveDetail mLeaveDetail;// 修改的假条

    private ArrayList<String> typeList;// 请假类型
    private ArrayList<LeaveTime> mLeaveTime;// 请假时间段
    private ArrayList<LeaveTime> originalLeaveTime;// 假条中原始的时间段

    private LinearLayout lLayout_stu;// 学生姓名布局
    private LinearLayout lLayout_time;// 时间段布局
    private TextView tv_stu;// 学生

    private EditText edt_reason;// 理由描述

    private Spinner spn_type;// 请假类型
    private SpinnerAdapter spnAdapter_type;// 请假类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ModifyLeaveActivityController.getInstance().setContext(this);
        if (savedInstanceState == null) {
            mLeaveDetail = (LeaveDetail) getIntent().getSerializableExtra(
                    "ChoseLeave");
        } else {
            mLeaveDetail = (LeaveDetail) savedInstanceState
                    .getSerializable("ChoseLeave");
        }
        EventBusUtil.register(this);
        initViews();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private void initViews() {
        setContentLayout(R.layout.leave_activity_modify_leave);
        setActionBarTitle(R.string.leave_modifyleave);
        lLayout_stu = (LinearLayout) findViewById(R.id.leave_llayout_stu);
        lLayout_time = (LinearLayout) findViewById(R.id.leave_llayout_time);
        tv_stu = (TextView) findViewById(R.id.leave_tv_name);
        TextView tv_timeAdd;// 增加时间段
        tv_timeAdd = (TextView) findViewById(R.id.leave_tv_timeadd);
        edt_reason = (EditText) findViewById(R.id.leave_edt_reason);
        Button btn_commit;// 提交按钮
        btn_commit = (Button) findViewById(R.id.leave_btn_commit);
        spn_type = (Spinner) findViewById(R.id.leave_spn_type);
        tv_timeAdd.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        edt_reason.addTextChangedListener(this);
        typeList = new ArrayList<>();
        mLeaveTime = new ArrayList<>();
        originalLeaveTime = new ArrayList<>();
        spnAdapter_type = new SpinnerAdapter(mContext, typeList);
        String[] type = {"补课", "事假", "病假", "其他"};
        typeList.addAll(Arrays.asList(type));
        spn_type.setAdapter(spnAdapter_type);
        spn_type.setOnItemSelectedListener(this);
        initData();
    }

    private void initData() {
        if (mLeaveDetail.getManType() == 0) {// 修改学生
            lLayout_stu.setVisibility(View.VISIBLE);
            tv_stu.setText(mLeaveDetail.getManName());
        } else {// 修改老师
            typeList.remove(0);// 去掉‘补课’选项
            spnAdapter_type.notifyDataSetChanged();
        }
        for (int i = 0; i < typeList.size(); i++) {
            if (mLeaveDetail.getLeaveType().equals(typeList.get(i))) {
                spn_type.setSelection(i);
                break;
            }
        }
        if (mLeaveDetail.getLeaveReason() != null) {
            edt_reason.setText(mLeaveDetail.getLeaveReason());
        }
        originalLeaveTime = mLeaveDetail.getTimeList();// 复制原始请假时间段，用于时间段比较
        for (int i = 0; i < mLeaveDetail.getTimeList().size(); i++) {
            mLeaveTime.add(mLeaveDetail.getTimeList().get(i));
            LeaveUtils.modfiyTimeLayout(mContext, lLayout_time, mLeaveTime);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_tv_timeadd:// 增加时间段
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.ModifyLeaveActivity_add_time));
                if (mLeaveTime.size() > 4) {
                    ToastUtil.showMessage(mContext,
                            R.string.leave_time_noMoreThanFive);
                } else {
                    LeaveUtils.addTimeLayout(mContext, lLayout_time, mLeaveTime);
                }
                break;
            case R.id.leave_btn_commit:// 提交按钮
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.ModifyLeaveActivity_btn_commit));
                if (mLeaveTime.size() > 0 && mLeaveTime != null) {
                    Builder commitDialog = new AlertDialog.Builder(mContext);
                    commitDialog.setTitle(R.string.hint);
                    commitDialog.setMessage(R.string.decided_to_submit);
                    commitDialog.setPositiveButton(R.string.sure,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    commit = true;
                                    LeaveDetailsActivityControl.getInstance()
                                            .GetLeaveModel(mLeaveDetail.getTabID());//判断是否是未审核状态
                                }
                            });
                    commitDialog.setNegativeButton(R.string.cancel, null);
                    commitDialog.show();
                } else {
                    ToastUtil.showMessage(mContext, R.string.leave_add_time);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 提交数据
     */
    private void commitData() {
        if (!typeChose.equals(mLeaveDetail.getLeaveType())
                || !edt_reason.getText().toString()
                .equals(mLeaveDetail.getLeaveReason())) {// 请假类型或者请假描述修改
            setUpdateLeaveModel();// 修改请假记录信息，不包括时间段
        }
        compareLeaveTime();
    }

    /**
     * 修改请假记录信息，不包括时间段
     */
    private void setUpdateLeaveModel() {
        number = number + 1;//设置修改成功post回调次数
        UpdateLeaveModelPost updatePost;// 更新假条信息
        updatePost = new UpdateLeaveModelPost();
        updatePost.setManId(mLeaveDetail.getManId());
        updatePost.setTabId(mLeaveDetail.getTabID());
        updatePost.setManName(mLeaveDetail.getManName());
        updatePost.setLeaveType(typeChose);
        updatePost.setClassStr(mLeaveDetail.getClassStr());// 修改假条必须上传班级年级
        updatePost.setGradeStr(mLeaveDetail.getGradeStr());// 修改假条必须上传班级年级
        updatePost.setManType(mLeaveDetail.getManType());
        if (edt_reason != null) {
            updatePost.setLeaveReason(edt_reason.getText().toString());
        }
        ModifyLeaveActivityController.getInstance()
                .UpdateLeaveModel(updatePost);
    }

    /**
     * 修改时间段： 1.最终时间段比原有时间段多或相等，则修改时间段并添加多的时间段 ； 2.最终时间段比原有时间段少，则修改时间段并删除多的时间段；
     */
    private void compareLeaveTime() {
        int a = originalLeaveTime.size();// 原有时间段
        int b = mLeaveTime.size();// 最终时间段
        if (a <= b) {// 最终时间段比原有时间段多或相等
            for (int i = 0; i < a; i++) {// 更新时间段
                number = number + 1;//设置修改成功post回调次数
                ModifyLeaveActivityController.getInstance().UpdateLeaveTime(
                        originalLeaveTime.get(i).getTabID(),
                        mLeaveTime.get(i).getSdate(),
                        mLeaveTime.get(i).getEdate());
            }
            int c = b - a;
            for (int i = c; i > 0; i--) {// 添加时间段
                number = number + 1;//设置修改成功post回调次数
                ModifyLeaveActivityController.getInstance().AddLeaveTime(
                        mLeaveDetail.getTabID(), mLeaveDetail.getUnitId(),
                        mLeaveTime.get(b - i).getSdate(),
                        mLeaveTime.get(b - i).getEdate());
            }
        } else {// 原有时间段比最终时间段多
            for (int i = 0; i < b; i++) {// 更新时间段
                number = number + 1;//设置修改成功post回调次数
                ModifyLeaveActivityController.getInstance().UpdateLeaveTime(
                        originalLeaveTime.get(i).getTabID(),
                        mLeaveTime.get(i).getSdate(),
                        mLeaveTime.get(i).getEdate());
            }
            int c = a - b;
            for (int i = c; i > 0; i--) {// 删除时间段
                number = number + 1;//设置修改成功post回调次数
                ModifyLeaveActivityController.getInstance().DeleteLeaveTime(
                        originalLeaveTime.get(a - i).getTabID());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (parent.getId() == R.id.leave_spn_type) {//请假类型
            MobclickAgent.onEvent(
                    mContext,
                    getResources().getString(
                            R.string.ModifyLeaveActivity_leave_type));
            typeChose = typeList.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case LeaveConstant.leave_GetLeaveModel://获取假条，判断是否是等待中
                DialogUtil.getInstance().cannleDialog();
                LeaveDetail choseLeaveCommit;
                choseLeaveCommit = (LeaveDetail) list.get(1);
                if (commit) {
                    if (choseLeaveCommit != null
                            && choseLeaveCommit.getStatusStr().equals("等待中")) {
                        commitData();//提交数据
                    } else {
                        ToastUtil.showMessage(mContext, R.string.leave_entered_audit);//提示已进入审核
                        finish();
                    }
                }
                break;
            case LeaveConstant.leave_UpdateLeaveModel:
            case LeaveConstant.leave_UpdateLeaveTime:
            case LeaveConstant.leave_DeleteLeaveTime:
            case LeaveConstant.leave_AddLeaveTime:
                post = post + 1;
                if (post == number) {//等待所有数据发送完成
                    ToastUtil.showMessage(mContext, R.string.leave_modifyleave_success);
                    DialogUtil.getInstance().cannleDialog();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 请假理由描述屏蔽回车
     */
    @Override
    public void afterTextChanged(Editable s) {
        for (int i = s.length(); i > 0; i--) {
            if (s.subSequence(i - 1, i).toString().equals("\n"))
                s.replace(i - 1, i, "");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
