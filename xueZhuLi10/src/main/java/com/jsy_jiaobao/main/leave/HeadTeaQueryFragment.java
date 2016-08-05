package com.jsy_jiaobao.main.leave;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.leave.AdminClassModel;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.MonPickerDialog;
import com.jsy_jiaobao.po.leave.MyAdminClasses;
import com.jsy_jiaobao.po.leave.MyLeaveModel;
import com.jsy_jiaobao.po.leave.MyLeaves;
import com.jsy_jiaobao.po.leave.MyLeavesPost;
import com.jsy_jiaobao.po.leave.SpinnerAdapter;
import com.jsy_jiaobao.po.leave.UnitClassLeaveModel;
import com.jsy_jiaobao.po.leave.UnitClassLeaves;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 功能说明：班主任查询（包括查询本人、查询学生）
 *
 * @author MSL
 */
public class HeadTeaQueryFragment extends Fragment implements
        OnCheckedChangeListener, OnItemSelectedListener, OnClickListener,
        OnRefreshListener2<ScrollView> {
    private final static String TAG = "HeadTeaQueryFragment";
    private final static int TYPE_SELF = 1;// 本人
    private final static int TYPE_STU = 0;// 学生
    private View view;
    private Context mContext;
    private int mType;// 本人或学生查询
    private int myPageNum = 1;// (本人)第几页，默认为1
    private int stuPageNum = 1;// (学生)第几页，默认为1
    private int myRowCount = 0;// (本人)pageNum=1为0，第二页起从前一页的返回结果中获得
    private int stuRowCount = 0;// (学生)pageNum=1为0，第二页起从前一页的返回结果中获得
    private String timeChose;// 选择的时间
    private boolean query = true;// 是否是第一次点击查询 0 否,1 是
    private boolean newLeaveModel = false;// // 生成了新的请假条，0 否,1 是
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM",
            Locale.getDefault());// 时间格式
    private MyLeavesPost myLeavesPost;// 查询假条

    private AdminClassModel choseClass;// 班级

    private ArrayList<String> classNameList;// 班级名称
    private ArrayList<MyLeaveModel> myLeaveList;// 本人请假记录列表
    private ArrayList<MyLeaveModel> stuLeaveList;// 学生请假记录列表
    private ArrayList<AdminClassModel> classModelList;// 班级列表


    private LinearLayout lLayout_class;// 班级选择区域
    private TextView tv_time;// 选择时间

    private TextView tv_stuName;// 学生（列表标题）

    private RadioGroup rg_identity;// 本人、学生切换区域
    private RadioButton rb_self;// 本人

    private SpinnerAdapter classAdapter;// 选择班级
    private UnitClassLeavesAdapter<Object> uclAdapter_leave;// 假条列表
    private PullToRefreshScrollView ptrScrollView;// 上拉加载更多，下拉刷新的控件

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.leave_fragment_head_query, container, false);
        LeaveFragmentControl.getInstance().setContext(this);
        initViews();
        return view;
    }

    private void initViews() {
        lLayout_class = (LinearLayout) view
                .findViewById(R.id.leave_llayout_class);
        tv_time = (TextView) view.findViewById(R.id.leave_tv_time);
        TextView tv_symbol;// 选择时间
        tv_symbol = (TextView) view.findViewById(R.id.leave_tv_symbol);
        tv_stuName = (TextView) view.findViewById(R.id.leave_item_name);
        rg_identity = (RadioGroup) view.findViewById(R.id.leave_rg);
        rb_self = (RadioButton) view.findViewById(R.id.leave_rb_self);
        Spinner spn_class;// 选择班级
        spn_class = (Spinner) view.findViewById(R.id.leave_spn_class);
        CusListView clv_leave;// 假条列表
        clv_leave = (CusListView) view.findViewById(R.id.leave_clv_leave);
        ptrScrollView = (PullToRefreshScrollView) view
                .findViewById(R.id.leave_ptrscrollview);
        tv_time.setOnClickListener(this);// 切换时间查询监听
        tv_symbol.setOnClickListener(this);// 切换时间查询监听
        rg_identity.setOnCheckedChangeListener(this);// 本人、学生之间切换监听
        ptrScrollView.setOnRefreshListener(this);
        classNameList = new ArrayList<>();
        myLeaveList = new ArrayList<>();
        stuLeaveList = new ArrayList<>();
        uclAdapter_leave = new UnitClassLeavesAdapter<>(this);
        classAdapter = new SpinnerAdapter(mContext, classNameList);
        clv_leave.setAdapter(uclAdapter_leave);
        spn_class.setAdapter(classAdapter);
        spn_class.setOnItemSelectedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lLayout_class.setVisibility(View.GONE);
        rg_identity.clearCheck();// 本人和学生都不选
        String time = sf.format(new Date());
        timeChose = time + "-01";// 特殊要求增加“-01”，我也不知道为什么
        tv_time.setText(time);
        mType = TYPE_SELF;
        String jiaobaohao = BaseActivity.sp.getString("JiaoBaoHao", null);
        if (jiaobaohao != null) {
            LeaveFragmentControl.getInstance().GetMyAdminClass(jiaobaohao);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBusUtil.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusUtil.unregister(this);
        MobclickAgent.onPageEnd(TAG);
    }

    /**
     * 切换fragment，用户看到
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (query) {
                rb_self.setChecked(true);
                query = false;
                getDefaultPost();
                LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
            } else if (newLeaveModel) {
                getDefaultPost();
                LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                newLeaveModel = false;
            }
        } else {
            newLeaveModel = false;
        }
    }

    /**
     * 根据选择身份不同更改界面
     *
     * @param type 自己/学生
     */
    private void changeData(int type) {
        switch (type) {
            case TYPE_SELF:
                uclAdapter_leave.setType(mType);
                uclAdapter_leave.notifyDataSetChanged();
                myLeaveList.clear();
                tv_stuName.setVisibility(View.GONE);
                lLayout_class.setVisibility(View.GONE);
                break;
            case TYPE_STU:
                uclAdapter_leave.setType(mType);
                uclAdapter_leave.notifyDataSetChanged();
                stuLeaveList.clear();
                tv_stuName.setVisibility(View.VISIBLE);
                lLayout_class.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void changeAdapterData() {
        switch (mType) {
            case TYPE_SELF:
                uclAdapter_leave.setData(myLeaveList);
                break;
            case TYPE_STU:
                uclAdapter_leave.setData(stuLeaveList);
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case LeaveConstant.leave_GetMyAdminClass:// 获取班级
                MyAdminClasses myAdminClasses;// 班级
                myAdminClasses = (MyAdminClasses) list.get(1);
                classModelList = myAdminClasses.getList();
                if (classModelList.size() > 0) {
                    classNameList.clear();
                    if (classModelList.size() == 1) {
                        choseClass = classModelList.get(0);
                        classNameList.add(choseClass.getClsName());
                    } else {
                        for (int i = 0; i < classModelList.size(); i++) {
                            classNameList.add(classModelList.get(i).getClsName());
                        }
                    }
                    classAdapter.notifyDataSetChanged();
                }
                break;
            case LeaveConstant.leave_GetClassLeaves:// 获取班级请假记录
                ptrScrollView.onRefreshComplete();
                UnitClassLeaves unitClassLeaves;// 班级请假记录
                unitClassLeaves = (UnitClassLeaves) list.get(1);
                ArrayList<UnitClassLeaveModel> leaveList = unitClassLeaves
                        .getList();
                ArrayList<UnitClassLeaveModel> leaveLists = new ArrayList<>();
                leaveLists.addAll(leaveList);
                uclAdapter_leave.setData(leaveLists);
                uclAdapter_leave.notifyDataSetChanged();
                break;
            case LeaveConstant.leave_GetMyLeaves:// 获取本人请假记录
                ptrScrollView.onRefreshComplete();
                MyLeaves myLeaves = (MyLeaves) list.get(1);
                int resultType = (Integer) list.get(2);
                ArrayList<MyLeaveModel> leaveList1 = myLeaves.getList();
                addList(resultType, leaveList1);
                changeAdapterData();
                uclAdapter_leave.notifyDataSetChanged();
                break;
            case LeaveConstant.leave_NewLeaveModel:
                newLeaveModel = (Boolean) list.get(1);// 请假成功后返回查询界面，必须刷新列表，不然进行撤回会出错
            default:
                break;
        }
    }

    private void addList(int resultType, ArrayList<MyLeaveModel> leaveList) {
        switch (resultType) {
            case TYPE_SELF:
                if (leaveList.size() == 0) {// 获得的数据为空或者没有数据
                    if (myLeaveList.size() == 0) {// 假条列表为空。因为删除一条假条后会获取一条新的记录补充列表，需要增加这个判断
                        ToastUtil.showMessage(mContext, R.string.leave_no_myleaves);
                    }
                } else {
                    myRowCount = leaveList.get(0).getRowCount();
                    myLeaveList.addAll(leaveList);
                }
                break;
            case TYPE_STU:
                if (leaveList.size() == 0 ) {// 获得的数据为空或者没有数据
                    if (stuLeaveList.size() == 0) {// 假条列表为空。因为删除一条假条后会获取一条新的记录补充列表，需要增加这个判断
                        ToastUtil.showMessage(mContext, R.string.leave_no_myleaves);
                    }
                } else {
                    stuRowCount = leaveList.get(0).getRowCount();
                    stuLeaveList.addAll(leaveList);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_self:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.HeadTeaQueryFragment_rb_self));
                mType = TYPE_SELF;
                changeData(mType);
                getDefaultPost();
                LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                break;
            case R.id.rb_stu:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.HeadTeaQueryFragment_rb_stu));
                mType = TYPE_STU;
                changeData(mType);
                getDefaultPost();
                LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_time || v.getId() == R.id.tv_symbol) {// 切换时间
            MobclickAgent.onEvent(
                    mContext,
                    getResources().getString(
                            R.string.HeadTeaQueryFragment_tv_time));
            String textTime = tv_time.getText().toString();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getDayTime(textTime));
            new MonPickerDialog(mContext, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    tv_time.setText(year + "-"
                            + String.format("%02d", (monthOfYear + 1)));
                    timeChose = tv_time.getText().toString() + "-01";// 特殊要求增加“-01”，我也不知道为什么
                    Log.e("chose_time", timeChose);
                    getDefaultPost();
                    LeaveFragmentControl.getInstance()
                            .GetMyLeaves(myLeavesPost);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private long getDayTime(String date) {
        Date dt2 ;
        try {
            dt2 = sf.parse(date);
        } catch (ParseException e) {
            dt2 = new Date();
        }
        return dt2.getTime();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (parent.getId() == R.id.spn_class) {// 选择班级
            MobclickAgent.onEvent(
                    mContext,
                    getResources().getString(
                            R.string.HeadTeaQueryFragment_spn_class));
            choseClass = classModelList.get(position);
            Log.e(TAG, choseClass.getTabID() + "所选");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LeaveConstant.leave_postDelelteLeaveId:
                int po = data.getIntExtra("Position", -1);
                removeListItem(po);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 删除一条请假记录，补数据
     *
     * @param po 是
     */
    private void removeListItem(int po) {
        switch (mType) {
            case TYPE_SELF:
                int size = myLeaveList.size();
                myLeaveList.remove(po);

                if (myRowCount == size || myRowCount == 0) {
                    uclAdapter_leave.setData(myLeaveList);
                    uclAdapter_leave.notifyDataSetChanged();
                } else {
                    myLeavesPost.setNumPerPage(1);
                    myLeavesPost.setPageNum(size);
                    myLeavesPost.setRowCount(myRowCount - 1);
                    LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                }
                break;
            case TYPE_STU:
                int size1 = stuLeaveList.size();
                stuLeaveList.remove(po);
                if (stuRowCount == size1 || stuRowCount == 0) {
                    uclAdapter_leave.setData(stuLeaveList);
                    uclAdapter_leave.notifyDataSetChanged();
                } else {
                    myLeavesPost.setNumPerPage(1);
                    myLeavesPost.setPageNum(size1);
                    myLeavesPost.setRowCount(stuRowCount - 1);
                    LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        changeData(mType);
        getDefaultPost();
        LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
    }

    /**
     * 初始化列表参数
     */
    private void getDefaultPost() {
        myLeavesPost = new MyLeavesPost(mContext);
        myLeavesPost.setManType(mType);
        myLeavesPost.setsDateTime(timeChose);
        myPageNum = 1;
        myRowCount = 0;
        stuPageNum = 1;
        myRowCount = 0;
        myLeaveList.clear();
        stuLeaveList.clear();
        myLeavesPost.setRowCount(0);
        myLeavesPost.setPageNum(1);
        myLeavesPost.setNumPerPage(20);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        loadMore();
    }

    private void loadMore() {
        switch (mType) {
            case TYPE_SELF:
                if (myRowCount == myLeaveList.size() || myRowCount == 0) {
                    // 只有第一页有数据，或者通过第二页以后返回的值为0
                    ToastUtil.showMessage(mContext, R.string.no_more);
                    ptrScrollView.onRefreshComplete();
                } else if (myLeaveList.size() % 20 == 0) {
                    myPageNum = myPageNum + 1;
                    myLeavesPost.setPageNum(myPageNum);
                    LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                } else {
                    ToastUtil.showMessage(mContext, R.string.no_more);// 提示没有更多了
                    ptrScrollView.onRefreshComplete();
                }
                break;
            case TYPE_STU:
                if (stuRowCount == stuLeaveList.size() || stuRowCount == 0) {
                    ToastUtil.showMessage(mContext, R.string.no_more);
                    ptrScrollView.onRefreshComplete();
                } else if (stuLeaveList.size() % 20 == 0) {
                    stuPageNum = stuPageNum + 1;
                    myLeavesPost.setPageNum(stuPageNum);
                    LeaveFragmentControl.getInstance().GetMyLeaves(myLeavesPost);
                } else {
                    ToastUtil.showMessage(mContext, R.string.no_more);// 提示没有更多了
                    ptrScrollView.onRefreshComplete();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullPageChanging(boolean isChanging) {
    }

    public static HeadTeaQueryFragment newInstance() {
        return new HeadTeaQueryFragment();
    }
}
