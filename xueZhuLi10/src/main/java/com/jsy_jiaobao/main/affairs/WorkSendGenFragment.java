package com.jsy_jiaobao.main.affairs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusExpandableListView;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitList;
import com.jsy_jiaobao.po.personal.GenRevicer;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.jsy_jiaobao.po.sys.Selit;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 家校互动 Fragment
 */
public class WorkSendGenFragment extends Fragment implements OnClickListener {
    public static final String TAG = "WorkSendGenFragment";
    private static final int TYPE_CLASS = 1;// 班级通知
    private static final int TYPE_PERSON = 2;// 个性展示
    private static final int TYPE_SCHOOL = 3;// 校园通知
    private static final int TYPE_GEN = 4;// 多校家长
    private static int TYPE_SELECT = 1;
    private Uri photoUri;// 图片路径
    private RequestParams params;//发送数据
    private List<NameValuePair> reciverList = new ArrayList<>();
    private ArrayList<CommMsgRevicerUnitClass> unitClass = new ArrayList<>();// 班级
    private List<SMSTreeUnitID> schoolList = new ArrayList<>();// 学校

    private Activity mActivity;
    private LinearLayout layout_class;// 班级通知布局
    private LinearLayout layout_person;// 个性展示布局
    private LinearLayout layout_school;// 校园通知布局
    private LinearLayout layout_gen;// 多校家长布局
    private RelativeLayout layout_gen_top;// 多校家长布局的顶部
    private ImageView iv_class;// 标题班级通知
    private ImageView iv_person;// 标题个性展示
    private ImageView iv_school;// 标题校园通知
    private ImageView iv_gen;// 标题多校家长
    private CheckBox cb_all;// 全选
    private CheckBox cb_invert;// 反选
    private CusGridView gridView_class;// 班级通知列表
    private CusExpandableListView listView_person;// 个性展示列表
    private CusListView listView_school;// 校园通知列表
    private CusListView listView_gen;// 多校家长列表
    private WorkSendGridViewAdapter gridAdapter_class;
    private WorkSendListViewAdapter listAdapter_gen;
    private WorkSendListViewAdapter listAdapter_school;
    private WorkSendGenExpanListAdapter expanAdapter_person;

    public static WorkSendGenFragment newInstance() {
        return new WorkSendGenFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_worksend_gen, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            photoUri = savedInstanceState.getParcelable("photoUri");
        }
        WorkSendFragmentController.getInstance().setContext(this);
        mActivity = getActivity();
        initViews();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("photoUri", photoUri);
    }

    private void initViews() {
        findView();
        gridAdapter_class = new WorkSendGridViewAdapter(mActivity);
        expanAdapter_person = new WorkSendGenExpanListAdapter(mActivity,
                mHandler);
        listAdapter_school = new WorkSendListViewAdapter(mActivity, mHandler);
        listAdapter_gen = new WorkSendListViewAdapter(mActivity, mHandler);
        iv_class.setOnClickListener(this);
        iv_person.setOnClickListener(this);
        iv_school.setOnClickListener(this);
        iv_gen.setOnClickListener(this);
        cb_all.setChecked(false);
        cb_all.setOnClickListener(this);
        cb_invert.setOnClickListener(this);
        gridView_class.setAdapter(gridAdapter_class);
        listView_person.setGroupIndicator(null);
        listView_person.setAdapter(expanAdapter_person);
        listView_school.setAdapter(listAdapter_school);
        listView_gen.setAdapter(listAdapter_gen);
        initData();
    }

    private void findView() {
        layout_class = (LinearLayout) mActivity
                .findViewById(R.id.worksend_gen_class_layout);
        layout_person = (LinearLayout) mActivity
                .findViewById(R.id.worksend_gen_person_layout);
        layout_school = (LinearLayout) mActivity
                .findViewById(R.id.worksend_gen_school_layout);
        layout_gen = (LinearLayout) mActivity
                .findViewById(R.id.worksend_gen_gen_layout);
        layout_gen_top = (RelativeLayout) mActivity
                .findViewById(R.id.worksend_gen_gen_layout_top);
        iv_class = (ImageView) mActivity
                .findViewById(R.id.worksend_gen_iv_class);
        iv_person = (ImageView) mActivity
                .findViewById(R.id.worksend_gen_iv_person);
        iv_school = (ImageView) mActivity
                .findViewById(R.id.worksend_gen_iv_school);
        iv_gen = (ImageView) mActivity.findViewById(R.id.worksend_gen_iv_gen);
        cb_all = (CheckBox) mActivity
                .findViewById(R.id.worksend_gen_gen_cb_selectall);
        cb_invert = (CheckBox) mActivity
                .findViewById(R.id.worksend_gen_gen_cb_invert);
        listView_gen = (CusListView) mActivity
                .findViewById(R.id.worksend_gen_gen_listview);
        listView_school = (CusListView) mActivity
                .findViewById(R.id.worksend_gen_school_listview);
        listView_person = (CusExpandableListView) mActivity
                .findViewById(R.id.worksend_gen_person_expanlistview);
        gridView_class = (CusGridView) mActivity
                .findViewById(R.id.worksend_gen_class_gridview);
    }

    private void initData() {
        changeSelectType(TYPE_CLASS);
        if (BaseActivity.sp.getInt("RoleIdentity", 1) > 1) {
            if (WorkSendToSbActivity2.UnitClass != null) {//有班级数组
                if (WorkSendToSbActivity2.UnitCommRight) { // 有同级单位
                    unitClass.clear();
                    for (int i = 0; i < WorkSendToSbActivity2.UnitClass.size(); i++) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("unitclassId", String
                                .valueOf(WorkSendToSbActivity2.UnitClass.get(i)
                                        .getTabID()));
                        params.addBodyParameter("flag", String
                                .valueOf(WorkSendToSbActivity2.UnitClass.get(i)
                                        .getFlag()));
                        WorkSendFragmentController.getInstance()
                                .GetUnitRevicer(params,
                                        WorkSendToSbActivity2.UnitClass.get(i));
                    }
                }
            }
        }
        if (WorkSendToSbActivity2.SMSList != null) {
            layout_gen_top.setVisibility(View.VISIBLE);
            listAdapter_gen.setData(WorkSendToSbActivity2.SMSList);
            listAdapter_gen.notifyDataSetChanged();
            if (BaseActivity.sp.getInt("RoleIdentity", 1) == 2) {// 1==教育局;2==老师;3==家长;4==学生;5==游客
                schoolList.clear();
                int UnitID = BaseActivity.sp.getInt("UnitID", 0);
                for (int i = 0; i < WorkSendToSbActivity2.SMSList.size(); i++) {
                    SMSTreeUnitID unit = WorkSendToSbActivity2.SMSList.get(i);
                    if (UnitID == unit.getId()) {
                        schoolList.add(unit);
                    }
                }
                listAdapter_school.setData(schoolList);
                listAdapter_school.notifyDataSetChanged();
            }
        } else {
            layout_gen_top.setVisibility(View.GONE);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if ((Boolean) msg.obj) {
                        listView_person.collapseGroup(msg.arg1);
                    } else {
                        listView_person.expandGroup(msg.arg1);
                    }
                    break;
                case 101:
                    cb_all.setChecked(true);
                    x:
                    for (int i = 0; i < listAdapter_gen.mChecked.size(); i++) {
                        boolean childList = listAdapter_gen.mChecked.get(i);
                        if (!childList) {
                            cb_all.setChecked(false);
                            break x;
                        }
                    }
                    break;
                case 102:
                    expanAdapter_person.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 不同的标题，切换不同的布局显示
     *
     * @param type t
     */
    private void changeSelectType(int type) {
        TYPE_SELECT = type;
        layout_class.setVisibility(View.GONE);
        layout_person.setVisibility(View.GONE);
        layout_school.setVisibility(View.GONE);
        layout_gen.setVisibility(View.GONE);
        iv_class.setImageResource(R.drawable.icon_worksend_gen_class_unselect);
        iv_person
                .setImageResource(R.drawable.icon_worksend_gen_person_unselect);
        iv_school
                .setImageResource(R.drawable.icon_worksend_gen_school_unselect);
        iv_gen.setImageResource(R.drawable.icon_worksend_gen_gen_unselect);
        switch (type) {
            case TYPE_CLASS:// 班级通知
                layout_class.setVisibility(View.VISIBLE);
                iv_class.setImageResource(R.drawable.icon_worksend_gen_class_selected);
                break;
            case TYPE_PERSON:// 个性展示
                layout_person.setVisibility(View.VISIBLE);
                iv_person
                        .setImageResource(R.drawable.icon_worksend_gen_person_selected);
                break;
            case TYPE_SCHOOL:// 校园通知
                layout_school.setVisibility(View.VISIBLE);
                iv_school
                        .setImageResource(R.drawable.icon_worksend_gen_school_selected);
                break;
            case TYPE_GEN:// 多校家长
                layout_gen.setVisibility(View.VISIBLE);
                iv_gen.setImageResource(R.drawable.icon_worksend_gen_gen_selected);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.worksend_gen_gen_cb_selectall:// 多校家长，全选
                MobclickAgent.onEvent(
                        mActivity,
                        getResources().getString(
                                R.string.WorkSendGenFragment_selectall));
                listAdapter_gen.setAllSelect(cb_all.isChecked());
                listAdapter_gen.notifyDataSetChanged();
                break;
            case R.id.worksend_gen_gen_cb_invert:// 多校家长，反选
                MobclickAgent.onEvent(
                        mActivity,
                        getResources().getString(
                                R.string.WorkSendGenFragment_invert));
                boolean isAll = listAdapter_gen.setInvert();
                cb_all.setChecked(isAll);
                listAdapter_gen.notifyDataSetChanged();

                break;
            case R.id.worksend_gen_iv_class:// 班级通知
                MobclickAgent.onEvent(mActivity,
                        getResources()
                                .getString(R.string.WorkSendGenFragment_class));
                changeSelectType(TYPE_CLASS);
                if (gridAdapter_class.getCount() == 0) {
                    ToastUtil.showMessage(mActivity, R.string.no_permission);// 提示无权限
                }
                break;
            case R.id.worksend_gen_iv_person:// 个性表现
                MobclickAgent.onEvent(
                        mActivity,
                        getResources().getString(
                                R.string.WorkSendGenFragment_person));
                changeSelectType(TYPE_PERSON);
                if (expanAdapter_person.getGroupCount() == 0) {
                    ToastUtil.showMessage(mActivity, R.string.no_permission);// 提示无权限
                }
                break;
            case R.id.worksend_gen_iv_school:// 校园通知
                MobclickAgent.onEvent(
                        mActivity,
                        getResources().getString(
                                R.string.WorkSendGenFragment_school));
                changeSelectType(TYPE_SCHOOL);
                if (listAdapter_school.getCount() == 0) {
                    ToastUtil.showMessage(mActivity, R.string.no_permission);// 提示无权限
                }
                break;
            case R.id.worksend_gen_iv_gen:// 多校家长
                MobclickAgent.onEvent(mActivity,
                        getResources().getString(R.string.WorkSendGenFragment_gen));
                changeSelectType(TYPE_GEN);
                if (listAdapter_gen.getCount() == 0) {
                    ToastUtil.showMessage(mActivity, R.string.no_permission);// 提示无权限
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送时弹出确认提示框（班级通知，个性表现）
     *
     * @param count 人数
     */
    protected void dialog_send(int count) {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setMessage("确定要向" + count + "人发送信息吗?");
        builder.setTitle(R.string.hint);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent
                                .onEvent(
                                        mActivity,
                                        getResources()
                                                .getString(
                                                        R.string.WorkSendGenFragment_send_sure));
                        dialog.dismiss();
                        sendMessage();
                    }

                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent
                                .onEvent(
                                        mActivity,
                                        getResources()
                                                .getString(
                                                        R.string.WorkSendGenFragment_send_cancel));
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 发送时弹出确认提示框（校园通知，多校家长）（短信直通车）
     *
     * @param count 人数
     */
    protected void dialog_sendSMS(int count) {
        AlertDialog.Builder builder = new Builder(getActivity());
        builder.setMessage("确定要向" + count + "个单位发送短信直通车?");
        builder.setTitle(R.string.hint);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent
                                .onEvent(
                                        mActivity,
                                        getResources()
                                                .getString(
                                                        R.string.WorkSendGenFragment_sendSMS_sure));
                        dialog.dismiss();
                        sendMessageSMS();
                    }

                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MobclickAgent
                                .onEvent(
                                        mActivity,
                                        getResources()
                                                .getString(
                                                        R.string.WorkSendGenFragment_sendSMS_cancel));
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 发送信息（班级通知，个性表现）
     */
    private void sendMessage() {
        params.addBodyParameter(reciverList);
        params.addBodyParameter("grsms", String.valueOf(false));
        WorkSendFragmentController.getInstance().CreateCommMsg(params);
    }

    /**
     * 发送信息（校园通知，多校家长）（短信直通车）
     */
    private void sendMessageSMS() {
        params.addBodyParameter(reciverList);
        params.addBodyParameter("grsms", String.valueOf(true));
        WorkSendFragmentController.getInstance().CreateCommMsg(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDetach() {
        EventBusUtil.unregister(this);
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        EventBusUtil.register(this);
        super.onAttach(activity);
    }


    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);

        switch (tag) {
            case Constant.msgcenter_worksend_SendBtnClicked://点击‘发布’按钮
                DialogUtil.getInstance().cannleDialog();
                Function function = (Function) list.get(1);
                params = (RequestParams) list.get(2);
                if (function == Function.GEN) {//发送的类型为家校互动
                    reciverList.clear();
                    switch (TYPE_SELECT) {
                        case TYPE_CLASS://班级通知
                            ArrayList<CommMsgRevicerUnitClass> classselectList = gridAdapter_class
                                    .getCheckedList();
                            if (classselectList != null) {
                                for (int i = 0; i < classselectList.size(); i++) {
                                    CommMsgRevicerUnitClass itemClass = classselectList
                                            .get(i);
                                    if (itemClass != null) {
                                        GenRevicer revicer = itemClass.getUserList();
                                        ArrayList<Selit> groupList = revicer
                                                .getGenselit();
                                        if (groupList != null) {
                                            if (groupList.size() > 0) {
                                                for (int k = 0; k < groupList.size(); k++) {
                                                    BasicNameValuePair item = new BasicNameValuePair(
                                                            "genselit", groupList
                                                            .get(k).getSelit());
                                                    reciverList.add(item);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (reciverList == null || reciverList.size() == 0) {
                                ToastUtil.showMessage(mActivity,
                                        R.string.please_choose_receiver);
                            } else {
                                dialog_send(reciverList.size());
                            }
                            break;
                        case TYPE_PERSON://个性表现
                            List<Selit> list1 = expanAdapter_person.getSelectSelit();
                            for (int i = 0; i < list1.size(); i++) {
                                BasicNameValuePair item = new BasicNameValuePair(
                                        "genselit", list1.get(i).getSelit());
                                reciverList.add(item);
                            }
                            if (reciverList == null || reciverList.size() == 0) {
                                ToastUtil.showMessage(mActivity,
                                        R.string.please_choose_receiver);
                            } else {
                                dialog_send(reciverList.size());
                            }
                            break;
                        case TYPE_SCHOOL://校园通知
                            List<SMSTreeUnitID> SMSList_s = listAdapter_school
                                    .getCheckedList();
                            if (SMSList_s != null) {
                                for (int i = 0; i < SMSList_s.size(); i++) {
                                    BasicNameValuePair item = new BasicNameValuePair(
                                            "GenUnit", String.valueOf(SMSList_s.get(i)
                                            .getId()));
                                    reciverList.add(item);
                                }
                            }
                            if (reciverList == null || reciverList.size() == 0) {
                                ToastUtil.showMessage(mActivity,
                                        R.string.please_choose_receiver);
                            } else {
                                dialog_sendSMS(reciverList.size());
                            }
                            break;
                        case TYPE_GEN://多校家长
                            List<SMSTreeUnitID> SMSList = listAdapter_gen
                                    .getCheckedList();
                            if (SMSList != null) {
                                for (int i = 0; i < SMSList.size(); i++) {
                                    BasicNameValuePair item = new BasicNameValuePair(
                                            "GenUnit", String.valueOf(SMSList.get(i)
                                            .getId()));
                                    reciverList.add(item);
                                }
                            }
                            if (reciverList == null || reciverList.size() == 0) {
                                ToastUtil.showMessage(mActivity,
                                        R.string.please_choose_receiver);
                            } else {
                                dialog_sendSMS(reciverList.size());
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
            case Constant.msgcenter_select_position:
                DialogUtil.getInstance().cannleDialog();
                break;
            case Constant.msgcenter_work_CommMsgRevicerUnitList://获取接收人列表
                DialogUtil.getInstance().cannleDialog();
                String commMsgRevicerUnitList_str = (String) list.get(1);
                CommMsgRevicerUnitList commMsgRevicerUnitList = GsonUtil
                        .GsonToObject(commMsgRevicerUnitList_str,
                                CommMsgRevicerUnitList.class);
                WorkSendToSbActivity2.myUnit = commMsgRevicerUnitList.getMyUnit();
                ArrayList<CommMsgRevicerUnitClass> UnitClass = commMsgRevicerUnitList
                        .getUnitClass();
                if (BaseActivity.sp.getInt("RoleIdentity", 1) > 1) {
                    unitClass.clear();
                    for (int i = 0; i < UnitClass.size(); i++) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("unitclassId",
                                String.valueOf(UnitClass.get(i).getTabID()));
                        params.addBodyParameter("flag",
                                String.valueOf(UnitClass.get(i).getFlag()));
                        WorkSendFragmentController.getInstance().GetUnitRevicer(
                                params, UnitClass.get(i));
                    }
                } else {
                    ToastUtil.showMessage(mActivity,
                            R.string.currentRole_hasNoClass_changeRole);
                }
                break;
            case Constant.msgcenter_worksend_GetUnitClassRevicer://获取班级
                DialogUtil.getInstance().cannleDialog();
                ArrayList<CommMsgRevicerUnitClass> classList = new ArrayList<>();
                GenRevicer UserList = (GenRevicer) list.get(1);
                CommMsgRevicerUnitClass ClassItem = (CommMsgRevicerUnitClass) list
                        .get(2);
                ClassItem.setUserList(UserList);
                unitClass.add(ClassItem);
                // 当所有班级请求完毕，排序，加载数据
                if (unitClass.size() == WorkSendToSbActivity2.UnitClass.size()
                        && unitClass != null) {
                    String[] classes = new String[unitClass.size()];
                    for (int i = 0; i < classes.length; i++) {
                        classes[i] = unitClass.get(i).getClsName();
                    }
                    Comparator c = Collator.getInstance(Locale.CHINA);
                    Arrays.sort(classes, c);
                    for (int i = 0; i < classes.length; i++) {
                        for (int j = 0; j < unitClass.size(); j++) {
                            if (classes[i].equals(unitClass.get(j).getClsName())) {
                                classList.add(unitClass.get(j));
                            }
                        }
                    }
                    gridAdapter_class.setData(classList);
                    gridAdapter_class.notifyDataSetChanged();

                    expanAdapter_person.setData(classList);
                    expanAdapter_person.notifyDataSetChanged();
                }
                break;
            case Constant.msgcenter_work_CreateCommMsg://发送信息成功
                DialogUtil.getInstance().cannleDialog();
                int result = (Integer) list.get(1);
                if (result == 1) {
                    try {
                        switch (TYPE_SELECT) {
                            case TYPE_CLASS://班级通知
                                gridAdapter_class.setAllCheck(false);
                                gridAdapter_class.notifyDataSetChanged();
                                break;
                            case TYPE_PERSON://个性表现
                                expanAdapter_person.setAllSelitCheckFlag(false);
                                expanAdapter_person.notifyDataSetChanged();
                                break;
                            case TYPE_SCHOOL://校园通知
                                listAdapter_school.setAllSelect(false);
                                listAdapter_school.notifyDataSetChanged();
                                break;
                            case TYPE_GEN://多校家长
                                listAdapter_gen.setAllSelect(false);
                                listAdapter_gen.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constant.msgcenter_work_SMSCommIndex://获取短信直通车数据
                DialogUtil.getInstance().cannleDialog();
                WorkSendToSbActivity2.SMSList = (List<SMSTreeUnitID>) list.get(1);
                if (WorkSendToSbActivity2.SMSList != null) {
                    layout_gen_top.setVisibility(View.VISIBLE);
                    listAdapter_gen.setData(WorkSendToSbActivity2.SMSList);
                    listAdapter_gen.notifyDataSetChanged();
                } else {
                    layout_gen_top.setVisibility(View.GONE);
                }
                if (BaseActivity.sp.getInt("RoleIdentity", 1) == 2) {
                    schoolList.clear();
                    int UnitID = BaseActivity.sp.getInt("UnitID", 0);
                    for (int i = 0; i < WorkSendToSbActivity2.SMSList.size(); i++) {
                        SMSTreeUnitID unit = WorkSendToSbActivity2.SMSList.get(i);
                        if (UnitID == unit.getId()) {
                            schoolList.add(unit);
                        }
                    }
                    listAdapter_school.setData(schoolList);
                    listAdapter_school.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
}