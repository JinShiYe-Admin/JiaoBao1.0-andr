package com.jsy_jiaobao.main.workol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusExpandableListView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.jsy_jiaobao.po.workol.DesHW;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaGroupData;
import com.jsy_jiaobao.po.workol.TeaMakeHW;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.jsy_jiaobao.po.workol.UnionChapterList;
import com.umeng.analytics.MobclickAgent;

public class TeacherPublishWorkActivity extends BaseActivity implements
        OnClickListener {
    // private final static String TAG="TeacherPublishWorkActivity";
    /**
     * 模式
     */
    final static int GTYPE_MODE = 0;
    /**
     * 班级
     */
    final static int GTYPE_CLAZZ = 1;
    /**
     * 年级
     */
    final static int GTYPE_GRADE = 2;
    /**
     * 科目
     */
    final static int GTYPE_SUBJECT = 3;
    /**
     * 教版
     */
    final static int GTYPE_GOV = 4;
    /**
     * 章节
     */
    final static int GTYPE_SESSION = 5;
    /**
     * 客观题
     */
    final static int GTYPE_IMPERSONALITY = 6;
    final static int GTYPE_IMPERSONALITY2 = 7;
    /**
     * 自定义作业列表
     */
    final static int GTYPE_CUSTOM = 8;
    /**
     * 其他选项
     */
    final static int GTYPE_OTHERS = 9;
    /**
     * 短信勾选 :家长通知,反馈
     */
    final static int GTYPE_SMSG = 10;
    // 个性作业
//    final static int MTYPE_individual = 1;
//    // 统一
//    final static int MTYPE_collective = 4;
//    // AB卷
//    final static int MTYPE_ab = 2;
    // 自定义
    final static int MTYPE_custom = 3;
    private Context mContext;// 上下文
    private CusExpandableListView listView;
    private Button btn_publish;// 发布列表
    private TeaExpandableListViewAdapter adapter;
    // 数据列表
    private ArrayList<TeaGroupData> adapterData = new ArrayList<>();
    SimpleDateFormat formatterTitle = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());// 时间格式
    private String toastMsg;// 消息
    private int MyClassNum;// 班级数目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//        } else {
//            Bundle bundle = getIntent().getExtras();
//            if (bundle != null) {
//            }
//        }
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setContentLayout(R.layout.activity_teacherpublish);
        mContext = this;
        setActionBarTitle(R.string.homework_handout);
        TeacherPublishWorkActivityController.getInstance().setContext(this);
        btn_publish = (Button) findViewById(R.id.workol_btn_publish);
        btn_publish.setVisibility(View.GONE);
        listView = (CusExpandableListView) findViewById(R.id.workol_tealistview);
        btn_publish.setOnClickListener(this);
        adapter = new TeaExpandableListViewAdapter(this, listView);
        listView.setAdapter(adapter);
        listView.setGroupIndicator(null);
        initData();
        // 长按更新自定义作业
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (view.getTag(R.id.groupId) != null) {
                    int groupId = (Integer) view.getTag(R.id.groupId);
                    switch (groupId) {
                        case GTYPE_CUSTOM:
                            TeacherPublishWorkActivityController.getInstance()
                                    .GetDesHWList(
                                            adapterData.get(GTYPE_SESSION)
                                                    .getCurrID());
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }

    // 初始化数据
    private void initData() {
        TeaGroupData mode = new TeaGroupData();
        mode.setType(GTYPE_MODE);
        adapterData.add(GTYPE_MODE, mode);

        TeaGroupData g1 = new TeaGroupData();
        g1.setType(GTYPE_CLAZZ);
        g1.setCurrName("0");
        adapterData.add(GTYPE_CLAZZ, g1);

        TeaGroupData g2 = new TeaGroupData();
        g2.setType(GTYPE_GRADE);
        adapterData.add(GTYPE_GRADE, g2);

        TeaGroupData gs = new TeaGroupData();
        gs.setType(GTYPE_SUBJECT);
        adapterData.add(GTYPE_SUBJECT, gs);

        TeaGroupData gg = new TeaGroupData();
        gg.setType(GTYPE_GOV);
        adapterData.add(GTYPE_GOV, gg);

        TeaGroupData gse = new TeaGroupData();
        gse.setType(GTYPE_SESSION);
        adapterData.add(GTYPE_SESSION, gse);

        TeaGroupData gi1 = new TeaGroupData();
        gi1.setType(GTYPE_IMPERSONALITY);
        adapterData.add(GTYPE_IMPERSONALITY, gi1);

        TeaGroupData gi2 = new TeaGroupData();
        gi2.setType(GTYPE_IMPERSONALITY2);
        adapterData.add(GTYPE_IMPERSONALITY2, gi2);

        TeaGroupData gc = new TeaGroupData();
        gc.setType(GTYPE_CUSTOM);
        adapterData.add(GTYPE_CUSTOM, gc);

        TeaGroupData go = new TeaGroupData();
        go.setType(GTYPE_OTHERS);
        go.setData(0);
        adapterData.add(GTYPE_OTHERS, go);

        TeaGroupData gsm = new TeaGroupData();
        gsm.setType(GTYPE_SMSG);
        adapterData.add(GTYPE_SMSG, gsm);
        // 根据老师身份 获取其负责的班级
        if (Constant.listUserIdentity != null) {
            for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
                UserIdentity userIdentity = Constant.listUserIdentity.get(i);
                if (userIdentity.getRoleIdentity() == 2) {
                    List<UserUnit> list = userIdentity.getUserUnits();
                    if (list != null && list.size() > 0) {
                        MyClassNum = list.size();
                        myUserClassList.clear();
                        currClassNum = 0;
                        for (int j = 0; j < list.size(); j++) {
                            TeacherPublishWorkActivityController.getInstance()
                                    .getmyUserClass(list.get(j));
                        }

                    } else {
                        ToastUtil.showMessage(mContext,
                                R.string.no_class_no_publish);
                        btn_publish.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 发布作业点击事件
            case R.id.workol_btn_publish:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.TeacherPublishWorkActivity_publishWork));
                final ArrayList<TeaMakeHW> list = adapter.getResult();
                if (list != null && list.size() > 0) {
                    String ExpTime =  list.get(0).getExpTime();
                    // 判断字符串是否为时间格式
                    if (isValidDate(ExpTime)) {
                        String title = list.get(0).getHomeworkName();
                        // 创建Dialog
                        BaseUtils.createDialog(mContext,
                                getResources().getString(R.string.release_work),
                                "确定要发" + title + "?",
                                android.R.drawable.ic_dialog_alert,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        TecMakeHWSize = list.size();
                                        TecMakeHWNum = 0;
                                        DialogUtil.getInstance().getDialog(
                                                mContext,
                                                R.string.releasing_waiting);
                                        DialogUtil.getInstance()
                                                .setCanCancel(false);
                                        toastMsg = "";
                                        for (int i = 0; i < list.size(); i++) {
                                            TeacherPublishWorkActivityController
                                                    .getInstance().TecMakeHW(
                                                    list.get(i));
                                        }
                                    }
                                }).show();
                    } else {
                        ToastUtil.showMessage(mContext, "截止时间格式错误,请修改后再发布");
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 判断字符串是否为时间格式
     *
     * @param expTime shi
     * @return bool
     */
    public boolean isValidDate(String expTime) {
        if (expTime == null) {
            return false;
        }
        try {
            // 指定日期格式为四位年/两位月份/两位日期，
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2004/02/29会被接受，并转换成2004/03/01
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(expTime);
            String newExpTime = dateFormat.format(date);
            return newExpTime.equals(expTime);

        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        EventBusUtil.register(this);
    }

    @Override
    protected void onPause() {
        EventBusUtil.unregister(this);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    /**
     * Toast信息
     *
     * @param name f
     * @param b f
     * @return f
     */
    private String getToastPublishInfo(String name, Boolean b) {
        String a;
        if (b) {
            a = name + getResources().getString(R.string.workol_makehw_success);
        } else {
            a = name + getResources().getString(R.string.workol_makehw_faild);
        }
        return a;
    }

    private int TecMakeHWNum = 0;
    private int TecMakeHWSize = 0;
    boolean haveTecQs;

    /**
     * 获取数据处理
     *
     * @param list list
     */

    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            // 是否有题
            case Constants.WORKOL_TecQs:
                String qs = (String) list.get(1);
                if ("true".equals(qs)) {
                    haveTecQs = true;
                } else if ("ConnectFailed".equals(qs)) {
                    ToastUtil.showMessage(mContext, "网络发送请求失败");
                } else {
                    ToastUtil.showMessage(
                            mContext,
                            getResources().getString(
                                    R.string.workol_session_nocontent));
                    haveTecQs = false;
                }
                adapter.setHaveTecQs(haveTecQs);
                break;

            case Constants.WORKOL_TecMakeHWClick:
                int groupPosition = (Integer) list.get(1);
                boolean isExpanded = (Boolean) list.get(2);
                if (isExpanded) {
                    listView.collapseGroup(groupPosition);
                } else {
                    listView.expandGroup(groupPosition);
                }
                break;
            // 发布作业结果
            case Constants.WORKOL_TecMakeHW:
                TecMakeHWNum++;
                String result = (String) list.get(1);
                String className = (String) list.get(2);
                toastMsg +=  getToastPublishInfo(className,
                        Boolean.valueOf(result))
                        + "\n";
                if (TecMakeHWSize == TecMakeHWNum) {
                    DialogUtil.getInstance().cannleDialog();
                    ToastUtil.showMessage(mContext, toastMsg);
                }
                break;
            /**
             * <pre>
             * 取出联动效果
             *
             * "gCode">年级代码
             * "subCode">科目代码
             * "uId">教版联动代码
             * "flag">0： 根据年级获取科目，1：根据科目获取教版，2： 根据所有获取UID
             * return
             * Args1 为科目列表数据
             * Args2为教版列表
             * Args3为章节列表
             * statusCode=200表示成功
             */
            case Constants.WORKOL_GetUnionChapterList:
                DialogUtil.getInstance().cannleDialog();
                btn_publish.setVisibility(View.VISIBLE);
                UnionChapterList union = (UnionChapterList) list.get(1);
                int position = (Integer) list.get(2);
                int typeMode = (Integer) list.get(3);
                btn_publish.setVisibility(View.VISIBLE);
                switch (position) {
                    // 年级 根据年级获得科目
                    case GTYPE_GRADE:
                        TeaGroupData gs = adapterData.get(GTYPE_SUBJECT);
                        TeaGroupData gg = adapterData.get(GTYPE_GOV);
                        TeaGroupData gse = adapterData.get(GTYPE_SESSION);
                        if (union == null) {
                            gs.setData(null);
                            gs.setCurrID(0);
                            gs.setCurrName("");
                            adapterData.set(GTYPE_SUBJECT, gs);

                            gg.setCurrID(0);
                            gg.setCurrName("");
                            gg.setData(null);
                            adapterData.set(GTYPE_GOV, gg);

                            gse.setData(null);
                            gse.setCurrID(0);
                            gse.setCurrName("");
                            adapterData.get(GTYPE_OTHERS).setCurrName(
                                    formatterTitle.format(new Date())
                                            + adapterData.get(GTYPE_SUBJECT)
                                            .getCurrName() + "无章节作业");
                            adapterData.set(GTYPE_SESSION, gse);
                        } else {
                            // 获取科目数据
                            ArrayList<TeaMode> modeList = union.getArgs1();
                            gs.setData(modeList);
                            if (modeList != null && modeList.size() > 0) {
                                gs.setCurrID(modeList.get(0).getSubjectCode());
                                gs.setCurrName(modeList.get(0).getSubjectName());
                            } else {
                                gs.setCurrID(0);
                                gs.setCurrName("");
                            }
                            // 保存科目数据
                            adapterData.set(GTYPE_SUBJECT, gs);
                            // 获取科目数据
                            ArrayList<TeaSubject> subList = union.getArgs2();
                            if (subList != null && subList.size() > 0) {
                                gg.setCurrID(subList.get(0).getTabID());
                                gg.setVersionCode(subList.get(0).getVersionCode());
                                gg.setCurrName(subList.get(0).getVersionName());
                            } else {
                                gg.setCurrID(0);
                                gg.setCurrName("");
                            }
                            gg.setData(subList);
                            // 保存教版数据
                            adapterData.set(GTYPE_GOV, gg);
                            ArrayList<TeaSession> grList = sortSession(union.getArgs3());
                            for (int i = 0; i < grList.size(); i++) {
                                TeaSession session = grList.get(i);
                                if (session.getPid() == 0) {
                                    session.setVisible(true);
                                }
                                session.setHaveChild(getSessionChild(session, grList));
                            }
                            gse.setData(grList);
                            if ( grList.size() > 0) {
                                gse.setCurrID(grList.get(0).getTabID());
                                gse.setCurrName(grList.get(0).getChapterName());
                                adapterData.get(GTYPE_OTHERS)
                                        .setCurrName(
                                                formatterTitle.format(new Date())
                                                        + adapterData
                                                        .get(GTYPE_SUBJECT)
                                                        .getCurrName()
                                                        + grList.get(0)
                                                        .getChapterName()
                                                        + "作业");
                            } else {
                                gse.setCurrID(0);
                                gse.setCurrName("");
                                adapterData.get(GTYPE_OTHERS).setCurrName(
                                        formatterTitle.format(new Date())
                                                + adapterData.get(GTYPE_SUBJECT)
                                                .getCurrName() + "无章节作业");
                            }
                            adapterData.set(GTYPE_SESSION, gse);
                        }
                        break;
                    // 科目 根据科目获得教版
                    case GTYPE_SUBJECT:
                        TeaGroupData gg1 = adapterData.get(GTYPE_GOV);
                        TeaGroupData gse1 = adapterData.get(GTYPE_SESSION);
                        if (union == null) {
                            gg1.setCurrID(0);
                            gg1.setCurrName("");
                            gg1.setData(null);
                            adapterData.set(GTYPE_GOV, gg1);

                            gse1.setData(null);
                            gse1.setCurrID(0);
                            gse1.setCurrName("");
                            adapterData.get(GTYPE_OTHERS).setCurrName(
                                    formatterTitle.format(new Date())
                                            + adapterData.get(GTYPE_SUBJECT)
                                            .getCurrName() + "无章节作业");
                            adapterData.set(GTYPE_SESSION, gse1);

                        } else {
                            // 获取教版数据
                            ArrayList<TeaSubject> subList1 = union.getArgs2();
                            if (subList1 != null && subList1.size() > 0) {
                                gg1.setCurrID(subList1.get(0).getTabID());
                                gg1.setVersionCode(subList1.get(0).getVersionCode());
                                gg1.setCurrName(subList1.get(0).getVersionName());
                            } else {
                                gg1.setCurrID(0);
                                gg1.setCurrName("");
                            }
                            gg1.setData(subList1);
                            // 保存教版数据
                            adapterData.set(GTYPE_GOV, gg1);
                            // 获取章节数据
                            ArrayList<TeaSession> grList1 = sortSession(union
                                    .getArgs3());
                            for (int i = 0; i < grList1.size(); i++) {
                                TeaSession session = grList1.get(i);
                                if (session.getPid() == 0) {
                                    session.setVisible(true);
                                }
                                session.setHaveChild(getSessionChild(session, grList1));
                            }
                            // 更新数据
                            gse1.setData(grList1);
                            if ( grList1.size() > 0) {
                                gse1.setCurrID(grList1.get(0).getTabID());
                                gse1.setCurrName(grList1.get(0).getChapterName());
                                adapterData.get(GTYPE_OTHERS).setCurrName(
                                        formatterTitle.format(new Date())
                                                + adapterData.get(GTYPE_SUBJECT)
                                                .getCurrName()
                                                + grList1.get(0).getChapterName()
                                                + "作业");
                            } else {
                                gse1.setCurrID(0);
                                gse1.setCurrName("");
                                adapterData.get(GTYPE_OTHERS).setCurrName(
                                        formatterTitle.format(new Date())
                                                + adapterData.get(GTYPE_SUBJECT)
                                                .getCurrName() + "无章节作业");
                            }
                            // 保存章节数据
                            adapterData.set(GTYPE_SESSION, gse1);
                        }

                        break;
                    // 教版 根据教版获得章节
                    case GTYPE_GOV:
                        TeaGroupData gse11 = adapterData.get(GTYPE_SESSION);
                        if (union == null) {
                            gse11.setData(null);
                            gse11.setCurrID(0);
                            gse11.setCurrName("");
                            adapterData.get(GTYPE_OTHERS).setCurrName(
                                    formatterTitle.format(new Date())
                                            + adapterData.get(GTYPE_SUBJECT)
                                            .getCurrName() + "无章节作业");
                            adapterData.set(GTYPE_SESSION, gse11);
                        } else {
                            ArrayList<TeaSession> grList11 = sortSession(union
                                    .getArgs3());
                            for (int i = 0; i < grList11.size(); i++) {
                                TeaSession session = grList11.get(i);
                                if (session.getPid() == 0) {
                                    session.setVisible(true);
                                }
                                session.setHaveChild(getSessionChild(session, grList11));
                            }
                            gse11.setData(grList11);
                            if ( grList11.size() > 0) {
                                gse11.setCurrID(grList11.get(0).getTabID());
                                gse11.setCurrName(grList11.get(0).getChapterName());
                                adapterData.get(GTYPE_OTHERS).setCurrName(
                                        formatterTitle.format(new Date())
                                                + adapterData.get(GTYPE_SUBJECT)
                                                .getCurrName()
                                                + grList11.get(0).getChapterName()
                                                + "作业");
                            } else {
                                gse11.setCurrID(0);
                                gse11.setCurrName("");
                                adapterData.get(GTYPE_OTHERS).setCurrName(
                                        formatterTitle.format(new Date())
                                                + adapterData.get(GTYPE_SUBJECT)
                                                .getCurrName() + "无章节作业");
                            }
                            adapterData.set(GTYPE_SESSION, gse11);
                        }
                        break;

                    default:
                        break;
                }
                // 获取章节ID
                int chapterID = adapterData.get(GTYPE_SESSION).getCurrID();
                if (typeMode == MTYPE_custom) {
                    if (chapterID != 0) {
                        TeaGroupData gse11 = adapterData.get(GTYPE_SESSION);
                        @SuppressWarnings("unchecked")
                        ArrayList<TeaSession> grList11 = (ArrayList<TeaSession>) gse11
                                .getData();
                        if (grList11 != null && grList11.size() > 0) {
                            TeacherPublishWorkActivityController.getInstance()
                                    .GetDesHWList(chapterID);
                        }
                    } else {
                        adapterData.get(GTYPE_CUSTOM).setData(null);
                        adapterData.get(GTYPE_CUSTOM).setCurrID(0);
                        adapterData.get(GTYPE_CUSTOM).setCurrName(
                                getResources().getString(R.string.have_noSelfWork));
                    }
                } else {
                    TeacherPublishWorkActivityController.getInstance().TecQs(
                            chapterID);
                }
                adapter.setGroupData(adapterData);
                adapter.notifyDataSetChanged();
                adapter.GetDesHWList();
                for (int i = 0; i < adapterData.size(); i++) {
                    listView.collapseGroup(i);
                }
                break;
            // 获取到我的班级
            case Constant.msgcenter_publish_getmyUserClass:
                DialogUtil.getInstance().cannleDialog();
                @SuppressWarnings("unchecked")
                ArrayList<UserClass> list1 = (ArrayList<UserClass>) list.get(1);
                String schoolName = (String) list.get(2);
                if (list1 != null && list1.size() > 0) {
                    for (int i = 0; i < list1.size(); i++) {
                        UserClass uc = list1.get(i);
                        uc.setSchoolName(schoolName);
                    }
                }
                myUserClassList.addAll(list1);
                currClassNum++;
                if (currClassNum == MyClassNum) {
                    adapterData.get(GTYPE_CLAZZ).setData(myUserClassList);
                    TeacherPublishWorkActivityController.getInstance()
                            .GetGradeList();
                }
                adapter.setGroupData(adapterData);
                adapter.createTeaHWList();
                break;
            // 获取到我的年级
            case Constants.WORKOL_GetGradeList:
                @SuppressWarnings("unchecked")
                ArrayList<TeaGrade> glist = (ArrayList<TeaGrade>) list.get(1);
                TeaGroupData g2 = adapterData.get(GTYPE_GRADE);
                g2.setData(glist);
                if (glist != null && glist.size() > 0) {
                    g2.setCurrID(glist.get(0).getGradeCode());
                    g2.setCurrName(glist.get(0).getGradeName());
                } else {
                    g2.setCurrID(0);
                    g2.setCurrName("");
                }
                adapterData.set(GTYPE_GRADE, g2);
                if (glist != null && glist.size() > 0) {
                    TeacherPublishWorkActivityController.getInstance()
                            .GetUnionChapterList(1, GTYPE_GRADE,
                                    glist.get(0).getGradeCode(), 0, 0, 0);
                } else {
                    adapter.setGroupData(adapterData);
                    adapter.notifyDataSetChanged();
                }
                break;
            // 自定义作业列表
            case Constants.WORKOL_GetDesHWList:
                @SuppressWarnings("unchecked")
                ArrayList<DesHW> list11 = (ArrayList<DesHW>) list.get(1);
                adapterData.get(GTYPE_CUSTOM).setData(list11);
                if (list11 != null && list11.size() > 0) {
                    adapterData.get(GTYPE_CUSTOM).setCurrID(
                            list11.get(0).getTabID());
                    adapterData.get(GTYPE_CUSTOM).setCurrName(
                            list11.get(0).getHomeworkName());
                } else {
                    adapterData.get(GTYPE_CUSTOM).setCurrID(0);
                    adapterData.get(GTYPE_CUSTOM).setCurrName(
                            getResources().getString(R.string.have_noSelfWork));
                }
                adapter.setGroupData(adapterData);
                adapter.updataDes();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    // 获取章节子菜单
    private boolean getSessionChild(TeaSession session,
                                    ArrayList<TeaSession> list) {
        for (TeaSession child : list) {
            if (session.getTabID() == child.getPid()) {
                return true;
            }
        }
        return false;
    }

    // 获取章节列表
    private ArrayList<TeaSession> sortSession(ArrayList<TeaSession> grList) {
        ArrayList<TeaSession> list = new ArrayList<>();
        if (grList != null) {
            for (int i = 0; i < grList.size(); i++) {
                TeaSession session = grList.get(i);
                if (session.getPid() == 0) {
                    list.add(session);
                    sortChildSession(grList, session, list, 1);
                }
            }
        }
        return list;
    }

    private void sortChildSession(ArrayList<TeaSession> grList,
                                  TeaSession session, ArrayList<TeaSession> list, int level) {
        for (int i = grList.size() - 1; i >= 0; i--) {
            TeaSession child = grList.get(i);
            if (child.getPid() == session.getTabID()) {
                child.setLevel(level);
                list.add(list.indexOf(session) + 1, child);
                sortChildSession(grList, child, list, level + 1);
            }
        }
    }

    ArrayList<UserClass> myUserClassList = new ArrayList<>();
    private int currClassNum = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
    }
}
