package com.jsy_jiaobao.main.workol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusExpandableListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.workol.DesHW;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaGroupData;
import com.jsy_jiaobao.po.workol.TeaMakeHW;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.umeng.analytics.MobclickAgent;

/**
 * 老师布置作业二级列表
 */
public class TeaExpandableListViewAdapter extends BaseExpandableListAdapter {
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
    /* 主观题？ */
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
    /**
     * 作业模式
     */
    final static int MTYPE_individual = 1;// 个性
    final static int MTYPE_collective = 4;// 统一
    final static int MTYPE_ab = 2;// AB卷
    final static int MTYPE_custom = 3;// 自定义作业
    private CusExpandableListView listView;
    // 作业类型 1为个性作业，2为AB卷，3为自定义作业，4统一作业（所有班级统一）
    /**
     * 个性作业= 1,统一作业 = 2,自定义作业 = 4,ab卷 = 3
     */
    private int TYPE_MODE = MTYPE_individual;
    /**
     * 已选年级
     */
    private int GradeCode;
    /**
     * 已选科目
     */
    private int SubjectCode;
    /**
     * 已选教版
     */
    private int uidCode;

    private ArrayList<TeaMakeHW> teaMakeHWList = new ArrayList<>();
    public ArrayList<TeaGroupData> groupData = new ArrayList<>();
    public ArrayList<Object> childData = new ArrayList<>();
    Activity mContext;
    String[] mItems = new String[]{"1", "2", "3", "4", "5"};
    ArrayAdapter<String> _Adapter;

    private String work_title;
    public String work_time;
    // 日期格式
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());
    SimpleDateFormat formatterTitle = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());
    private boolean haveTecQs;
    // 默认时长20
    protected int LongTime = 20;

    @SuppressWarnings("deprecation")
    public TeaExpandableListViewAdapter(Activity context,
                                        CusExpandableListView listView) {
        this.mContext = context;
        this.listView = listView;
        _Adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, mItems);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        curDate.setHours(23);
        curDate.setMinutes(0);
        curDate.setSeconds(0);
        work_time = formatter.format(curDate);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childData.size() > 0) {
            Object child = childData.get(groupPosition);
            if (child == null) {
                return 0;
            } else {
                if (child instanceof List) {
                    return ((List) child).size();
                } else {
                    return 1;
                }
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_workol_tree_item, parent, false);
            mViewChild.layout_grade = (LinearLayout) convertView
                    .findViewById(R.id.tree_workol_layout_grade);
            mViewChild.layout_txt = (RelativeLayout) convertView
                    .findViewById(R.id.tree_workol_layout_txt);
            mViewChild.layout_clazz = (LinearLayout) convertView
                    .findViewById(R.id.tree_workol_layout_clazzb);
            mViewChild.layout_others = (LinearLayout) convertView
                    .findViewById(R.id.tree_workol_layout_others);
            mViewChild.clazz_checkBox = (CheckBox) convertView
                    .findViewById(R.id.tree_workol_clazz_cb);
            mViewChild.clazz_radioGroup = (RadioGroup) convertView
                    .findViewById(R.id.tree_workol_clazz_radiogroup);
            mViewChild.layout_clazz_radiogroup = (LinearLayout) convertView
                    .findViewById(R.id.tree_workol_clazzb_layout_chose);
            mViewChild.grade_name = (TextView) convertView
                    .findViewById(R.id.tree_workol_grade_tv_name);
            mViewChild.grade_radio = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_grade_radiobutton);
            mViewChild.txt_name = (TextView) convertView
                    .findViewById(R.id.tree_workol_txt_name);
            mViewChild.txt_toggle = (ImageView) convertView
                    .findViewById(R.id.tree_workol_iv_toggle);
            mViewChild.txt_radio = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_txt_radiobutton);
            mViewChild.others_title = (IEditText) convertView
                    .findViewById(R.id.tree_workol_others_edtname);
            mViewChild.others_time = (IEditText) convertView
                    .findViewById(R.id.tree_workol_others_edttime);
            mViewChild.others_radiogroup = (com.jsy_jiaobao.customview.RadioGroup) convertView
                    .findViewById(R.id.tree_workol_others_radiogroup);
            mViewChild.radio10 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio10);
            mViewChild.radio20 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio20);
            mViewChild.radio30 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio30);
            mViewChild.radio40 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio40);
            mViewChild.radio60 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio60);
            mViewChild.radio90 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio90);
            mViewChild.radio120 = (RadioButton) convertView
                    .findViewById(R.id.tree_workol_others_radio120);
            convertView.setTag(mViewChild);

        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        mViewChild.layout_txt.setPadding(0, 0, 0, 0);
        mViewChild.txt_toggle.setVisibility(View.VISIBLE);
        final int type = getGroupType(groupPosition);
        switch (type) {
            // case GTYPE_ MODE:
            // break;
            case GTYPE_CLAZZ:
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.GONE);
                mViewChild.layout_clazz.setVisibility(View.VISIBLE);
                mViewChild.clazz_checkBox.setOnCheckedChangeListener(null);
                mViewChild.clazz_radioGroup.setOnCheckedChangeListener(null);
                Object child = childData.get(type);
                if (child instanceof ArrayList) {
                    final UserClass userClass = (UserClass) ((ArrayList) child)
                            .get(childPosition);

                    for (int i = 0; i < teaMakeHWList.size(); i++) {
                        if (teaMakeHWList.get(i).getClassID() == userClass
                                .getClassID()) {
                            TeaMakeHW teaMakeHW = teaMakeHWList.get(i);
                            mViewChild.clazz_checkBox.setChecked(teaMakeHW
                                    .isChecked());
                            switch (teaMakeHW.getDoLv()) {
                                case 1:
                                    mViewChild.clazz_radioGroup
                                            .check(R.id.tree_workol_clazz_radio1);
                                    break;
                                case 2:
                                    mViewChild.clazz_radioGroup
                                            .check(R.id.tree_workol_clazz_radio2);
                                    break;
                                case 3:
                                    mViewChild.clazz_radioGroup
                                            .check(R.id.tree_workol_clazz_radio3);
                                    break;
                                case 4:
                                    mViewChild.clazz_radioGroup
                                            .check(R.id.tree_workol_clazz_radio4);
                                    break;
                                case 5:
                                    mViewChild.clazz_radioGroup
                                            .check(R.id.tree_workol_clazz_radio5);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                    }
                    mViewChild.clazz_checkBox.setText(userClass.getClassName()
                            .trim() + "[" + userClass.getSchoolName() + "]");
                    mViewChild.clazz_radioGroup
                            .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(RadioGroup group,
                                                             int checkedId) {
                                    TeaMakeHW teaMakeHW = null;
                                    int position = 0;
                                    for (int i = 0; i < teaMakeHWList.size(); i++) {
                                        if (teaMakeHWList.get(i).getClassID() == userClass
                                                .getClassID()) {
                                            teaMakeHW = teaMakeHWList.get(i);
                                            position = i;
                                            break;
                                        }
                                    }
                                    if (teaMakeHW != null) {
                                        switch (checkedId) {
                                            case R.id.tree_workol_clazz_radio1:
                                                teaMakeHW.setDoLv(1);
                                                break;
                                            case R.id.tree_workol_clazz_radio2:
                                                teaMakeHW.setDoLv(2);
                                                break;
                                            case R.id.tree_workol_clazz_radio3:
                                                teaMakeHW.setDoLv(3);
                                                break;
                                            case R.id.tree_workol_clazz_radio4:
                                                teaMakeHW.setDoLv(4);
                                                break;
                                            case R.id.tree_workol_clazz_radio5:
                                                teaMakeHW.setDoLv(5);
                                                break;
                                            default:
                                                break;
                                        }
                                        teaMakeHWList.set(position, teaMakeHW);
                                    }
                                }
                            });
                    mViewChild.clazz_checkBox
                            .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(
                                        CompoundButton buttonView, boolean isChecked) {
                                    MobclickAgent
                                            .onEvent(
                                                    mContext,
                                                    mContext.getResources()
                                                            .getString(
                                                                    R.string.TeacherPublishWorkActivity_choseClass));
                                    try {
                                        TeaGroupData cdata = groupData.get(type);
                                        String cname = cdata.getCurrName();
                                        int num = Integer.parseInt(cname);
                                        if (isChecked) {
                                            num++;
                                        } else {
                                            num--;
                                        }
                                        groupData.get(type).setCurrName(
                                                String.valueOf(num));
                                        TeaExpandableListViewAdapter.this
                                                .notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    TeaMakeHW teaMakeHW = null;
                                    int position = 0;
                                    for (int i = 0; i < teaMakeHWList.size(); i++) {
                                        if (teaMakeHWList.get(i).getClassID() == userClass
                                                .getClassID()) {
                                            teaMakeHW = teaMakeHWList.get(i);
                                            position = i;
                                            break;
                                        }
                                    }
                                    if (teaMakeHW != null) {
                                        teaMakeHW.setChecked(isChecked);
                                        teaMakeHWList.set(position, teaMakeHW);
                                    }
                                }
                            });
                }
                switch (TYPE_MODE) {
                    case MTYPE_individual:
                        mViewChild.layout_clazz_radiogroup.setVisibility(View.VISIBLE);
                        break;
                    case MTYPE_collective:
                    case MTYPE_ab:
                        mViewChild.layout_clazz_radiogroup.setVisibility(View.GONE);
                        break;
                    case MTYPE_custom:
                        mViewChild.layout_clazz_radiogroup.setVisibility(View.GONE);

                        break;

                    default:
                        break;
                }
                break;
            case GTYPE_GRADE:
                mViewChild.layout_grade.setVisibility(View.VISIBLE);
                mViewChild.layout_txt.setVisibility(View.GONE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.grade_name.setText("");
                Object gradeData = childData.get(type);
                if (gradeData != null && gradeData instanceof ArrayList) {
                    final TeaGrade userGrade = (TeaGrade) ((ArrayList) gradeData)
                            .get(childPosition);
                    mViewChild.grade_name.setText(userGrade.getGradeName().trim());
                    int id = groupData.get(type).getCurrID();
                    mViewChild.grade_radio.setChecked(false);
                    if (userGrade.getGradeCode() == id) {
                        mViewChild.grade_radio.setChecked(true);
                    }
                    mViewChild.grade_radio
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    MobclickAgent
                                            .onEvent(
                                                    mContext,
                                                    mContext.getResources()
                                                            .getString(
                                                                    R.string.TeacherPublishWorkActivity_choseGrade));
                                    GradeCode = userGrade.getGradeCode();
                                    groupData.get(type).setCurrID(GradeCode);
                                    groupData.get(type).setCurrName(
                                            userGrade.getGradeName());
                                    TeacherPublishWorkActivityController
                                            .getInstance().GetUnionChapterList(
                                            TYPE_MODE, GTYPE_GRADE,
                                            GradeCode, 0, 0, 0);
                                }
                            });
                    mViewChild.grade_name.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            GradeCode = userGrade.getGradeCode();
                            groupData.get(type).setCurrID(GradeCode);
                            groupData.get(type).setCurrName(
                                    userGrade.getGradeName());
                            TeacherPublishWorkActivityController.getInstance()
                                    .GetUnionChapterList(TYPE_MODE, GTYPE_GRADE,
                                            GradeCode, 0, 0, 0);
                        }
                    });
                }
                break;
            case GTYPE_SUBJECT:
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.VISIBLE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.txt_name.setText("");
                Object txtData = childData.get(type);
                if (txtData != null && txtData instanceof ArrayList) {
                    mViewChild.txt_toggle.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewChild.layout_txt
                            .getLayoutParams();
                    lp.height = LayoutParams.WRAP_CONTENT;
                    mViewChild.layout_txt.setLayoutParams(lp);
                    final TeaMode userGrade = (TeaMode) ((ArrayList) txtData)
                            .get(childPosition);
                    if (userGrade.getSubjectCode() == 0) {
                        mViewChild.txt_name.setText("");
                    } else {
                        int id = groupData.get(type).getCurrID();
                        mViewChild.txt_radio.setChecked(false);
                        if (userGrade.getSubjectCode() == id) {
                            mViewChild.txt_radio.setChecked(true);
                        }
                        // 显示选中项
                        mViewChild.txt_name.setText(userGrade.getSubjectName()
                                .trim());
                        // 选项监听事件
                        mViewChild.txt_radio
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        MobclickAgent
                                                .onEvent(
                                                        mContext,
                                                        mContext.getResources()
                                                                .getString(
                                                                        R.string.TeacherPublishWorkActivity_choseSubject));
                                        SubjectCode = userGrade.getSubjectCode();
                                        groupData.get(type).setCurrID(SubjectCode);
                                        groupData.get(type).setCurrName(
                                                userGrade.getSubjectName());
                                        TeacherPublishWorkActivityController
                                                .getInstance().GetUnionChapterList(
                                                TYPE_MODE, GTYPE_SUBJECT,
                                                GradeCode, SubjectCode, 0,
                                                1);
                                    }
                                });
                        // 节点名称的点击监听事件
                        mViewChild.txt_name
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        SubjectCode = userGrade.getSubjectCode();
                                        groupData.get(type).setCurrID(SubjectCode);
                                        groupData.get(type).setCurrName(
                                                userGrade.getSubjectName());
                                        TeacherPublishWorkActivityController
                                                .getInstance().GetUnionChapterList(
                                                TYPE_MODE, GTYPE_SUBJECT,
                                                GradeCode, SubjectCode, 0,
                                                1);
                                    }
                                });
                    }
                }
                break;
            case GTYPE_GOV:
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.VISIBLE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.txt_name.setText("");
                Object txtData1 = childData.get(type);
                if (txtData1 != null && txtData1 instanceof ArrayList) {
                    mViewChild.txt_toggle.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewChild.layout_txt
                            .getLayoutParams();
                    lp.height = LayoutParams.WRAP_CONTENT;
                    mViewChild.layout_txt.setLayoutParams(lp);
                    final TeaSubject userGrade = (TeaSubject) ((ArrayList) txtData1)
                            .get(childPosition);
                    int id = groupData.get(type).getCurrID();
                    mViewChild.txt_radio.setChecked(false);
                    if (userGrade.getTabID() == id) {
                        mViewChild.txt_radio.setChecked(true);
                    }
                    // 显示选中项
                    mViewChild.txt_name.setText(userGrade.getVersionName().trim());
                    // 选项设置监听事件
                    mViewChild.txt_radio.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            MobclickAgent
                                    .onEvent(
                                            mContext,
                                            mContext.getResources()
                                                    .getString(
                                                            R.string.TeacherPublishWorkActivity_choseVersion));
                            uidCode = userGrade.getTabID();
                            groupData.get(type).setCurrID(uidCode);
                            // groupData.get(type).setVersionCode(userGrade.getVersionCode());
                            groupData.get(type).setCurrName(
                                    userGrade.getVersionName());
                            TeacherPublishWorkActivityController.getInstance()
                                    .GetUnionChapterList(TYPE_MODE, GTYPE_GOV,
                                            GradeCode, SubjectCode, uidCode, 2);
                        }
                    });
                    // 设置监听事件
                    mViewChild.txt_name.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            uidCode = userGrade.getTabID();
                            groupData.get(type).setCurrID(uidCode);
                            // groupData.get(type).setVersionCode(userGrade.getVersionCode());
                            groupData.get(type).setCurrName(
                                    userGrade.getVersionName());
                            TeacherPublishWorkActivityController.getInstance()
                                    .GetUnionChapterList(TYPE_MODE, GTYPE_GOV,
                                            GradeCode, SubjectCode, uidCode, 2);
                        }
                    });
                }
                break;
            // 章节
            case GTYPE_SESSION:
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.VISIBLE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.txt_name.setText("");
                Object txtData11 = childData.get(type);
                if (txtData11 != null && txtData11 instanceof ArrayList) {
                    @SuppressWarnings("unchecked")
                    final ArrayList<TeaSession> sessionList = (ArrayList<TeaSession>) txtData11;
                    final TeaSession userGrade = sessionList.get(childPosition);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewChild.layout_txt
                            .getLayoutParams();
                    if (userGrade.isVisible()) {
                        lp.height = LayoutParams.WRAP_CONTENT;
                    } else {
                        lp.height = 0;
                    }
                    mViewChild.layout_txt.setLayoutParams(lp);
                    int id = groupData.get(type).getCurrID();
                    mViewChild.txt_radio.setChecked(false);
                    if (userGrade.getTabID() == id) {
                        mViewChild.txt_radio.setChecked(true);
                    }
                    // 选中的章节名称
                    mViewChild.txt_name.setText(userGrade.getChapterName().trim());
                    // 退格
                    mViewChild.layout_txt.setPadding(userGrade.getLevel() * 30, 0,
                            0, 0);
                    // 根据是否有子节点 ，更新UI
                    if (userGrade.isHaveChild()) {
                        mViewChild.txt_toggle.setVisibility(View.VISIBLE);
                    } else {
                        mViewChild.txt_toggle.setVisibility(View.INVISIBLE);
                    }
                    final boolean isOpened = userGrade.isChildrenOpened();
                    // 是否展开子章节，改变UI
                    if (isOpened) {
                        mViewChild.txt_toggle
                                .setImageResource(R.drawable.icon_worksend_selit_minus);
                    } else {
                        mViewChild.txt_toggle
                                .setImageResource(R.drawable.icon_worksend_selit_plus);
                    }
                    // 展开 闭合 按钮的点击监听事件
                    mViewChild.txt_toggle.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((TeaSession) ((ArrayList) childData.get(type))
                                    .get(childPosition))
                                    .setChildrenOpened(!isOpened);
                            setVisible(userGrade, sessionList, !isOpened);
                            TeaExpandableListViewAdapter.this
                                    .notifyDataSetChanged();
                        }
                    });
                    // 章节选项点击事件
                    mViewChild.txt_radio.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            MobclickAgent
                                    .onEvent(
                                            mContext,
                                            mContext.getResources()
                                                    .getString(
                                                            R.string.TeacherPublishWorkActivity_choseSession));
                            groupData.get(type).setCurrID(userGrade.getTabID());
                            groupData.get(type).setCurrName(
                                    userGrade.getChapterName());
                            work_title = formatterTitle.format(new Date())
                                    + groupData.get(GTYPE_SUBJECT).getCurrName()
                                    + userGrade.getChapterName() + "作业";
                            groupData.get(GTYPE_OTHERS).setCurrName(work_title);
                            for (int i = 0; i < teaMakeHWList.size(); i++) {
                                teaMakeHWList.get(i).setChapterID(
                                        userGrade.getTabID());
                            }
                            TeaExpandableListViewAdapter.this
                                    .notifyDataSetChanged();
                            TeacherPublishWorkActivityController.getInstance()
                                    .TecQs(userGrade.getTabID());
                            if (TYPE_MODE == MTYPE_custom) {
                                TeacherPublishWorkActivityController.getInstance()
                                        .GetDesHWList(userGrade.getTabID());
                            }
                        }
                    });
                    // 选中章节的点击事件
                    mViewChild.txt_name.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            groupData.get(type).setCurrID(userGrade.getTabID());
                            groupData.get(type).setCurrName(
                                    userGrade.getChapterName());
                            work_title = formatterTitle.format(new Date())
                                    + groupData.get(GTYPE_SUBJECT).getCurrName()
                                    + userGrade.getChapterName() + "作业";
                            groupData.get(GTYPE_OTHERS).setCurrName(work_title);
                            for (int i = 0; i < teaMakeHWList.size(); i++) {
                                teaMakeHWList.get(i).setChapterID(
                                        userGrade.getTabID());
                            }
                            TeaExpandableListViewAdapter.this
                                    .notifyDataSetChanged();
                            if (TYPE_MODE == MTYPE_custom) {
                                TeacherPublishWorkActivityController.getInstance()
                                        .GetDesHWList(userGrade.getTabID());
                            } else {
                                TeacherPublishWorkActivityController.getInstance()
                                        .TecQs(userGrade.getTabID());

                            }
                        }
                    });

                }
                break;
            /**
             * custom
             */
            case GTYPE_CUSTOM:
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.VISIBLE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.GONE);
                mViewChild.txt_name.setText("");
                Object txtData111 = childData.get(type);
                if (txtData111 != null && txtData111 instanceof ArrayList) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mViewChild.layout_txt
                            .getLayoutParams();
                    lp.height = LayoutParams.WRAP_CONTENT;
                    mViewChild.layout_txt.setLayoutParams(lp);
                    final DesHW userGrade = (DesHW) ((ArrayList) txtData111)
                            .get(childPosition);
                    if (userGrade.getTabID() == 0) {
                        mViewChild.txt_name.setText("");
                    } else {
                        int id = groupData.get(type).getCurrID();
                        mViewChild.txt_radio.setChecked(false);
                        if (userGrade.getTabID() == id) {
                            mViewChild.txt_radio.setChecked(true);
                        }
                        // 隐藏
                        mViewChild.txt_toggle.setVisibility(View.INVISIBLE);
                        // 显示名称
                        mViewChild.txt_name.setText(userGrade.getHomeworkName()
                                .trim());
                        // 点击事件
                        mViewChild.txt_name
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        groupData.get(type).setCurrID(
                                                userGrade.getTabID());
                                        groupData.get(type).setCurrName(
                                                userGrade.getHomeworkName());
                                        for (int i = 0; i < teaMakeHWList.size(); i++) {
                                            teaMakeHWList.get(i).setDesId(
                                                    userGrade.getTabID());
                                        }
                                        TeaExpandableListViewAdapter.this
                                                .notifyDataSetChanged();
                                    }
                                });
                        // 选项选择
                        mViewChild.txt_radio
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        groupData.get(type).setCurrID(
                                                userGrade.getTabID());
                                        groupData.get(type).setCurrName(
                                                userGrade.getHomeworkName());
                                        for (int i = 0; i < teaMakeHWList.size(); i++) {
                                            teaMakeHWList.get(i).setDesId(
                                                    userGrade.getTabID());
                                        }
                                        TeaExpandableListViewAdapter.this
                                                .notifyDataSetChanged();
                                    }
                                });
                    }
                }
                break;
            // 其他界面
            case GTYPE_OTHERS:
                mViewChild.layout_grade.setVisibility(View.GONE);
                mViewChild.layout_txt.setVisibility(View.GONE);
                mViewChild.layout_clazz.setVisibility(View.GONE);
                mViewChild.layout_others.setVisibility(View.VISIBLE);
                // oldTitle=groupData.get(GTYPE_OTHERS).getCurrName();
                // 监听作业标题的输入事件
                mViewChild.others_title.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        groupData.get(GTYPE_OTHERS).setCurrName(s.toString());
                        work_title = s.toString();
                    }
                });
                // 时间文本的监听事件
                mViewChild.others_time.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        work_time = s.toString();
                    }
                });
                mViewChild.others_title.setText(work_title);
                mViewChild.others_time.setText(work_time);
                // 时间的点击事件
                mViewChild.others_time.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        mContext.getResources()
                                                .getString(
                                                        R.string.TeacherPublishWorkActivity_choseOverTime));
                        // 获取系统当前时间
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        // 格式化
                        String initEndDateTime = formatter.format(curDate);
                        // 日期时间选择控件
                        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                                mContext, initEndDateTime);
                        dateTimePicKDialog
                                .dateTimePicKDialog(TeaExpandableListViewAdapter.this);
                    }
                });
                /**
                 * 作业时长选择，默认都不选
                 */
                getDefaultCheck();
                // 如果未赋值longTime，则默认为20min
                if (LongTime == 0) {
                    LongTime = 20;
                }
                // 当LongTime 赋值时，对应各个选项，改变选择状态
                switch (LongTime) {
                    case 10:
                        mViewChild.radio10.setChecked(true);
                        break;
                    case 20:
                        mViewChild.radio20.setChecked(true);
                        break;
                    case 30:
                        mViewChild.radio30.setChecked(true);
                        break;
                    case 40:
                        mViewChild.radio40.setChecked(true);
                        break;
                    case 60:
                        mViewChild.radio60.setChecked(true);
                        break;
                    case 90:
                        mViewChild.radio90.setChecked(true);
                        break;
                    case 120:
                        mViewChild.radio120.setChecked(true);
                        break;

                    default:
                        break;
                }
                // mViewChild.others_scroll.setHorizontalScrollBarEnabled(false);
                // 作业时长选择监听事件
                mViewChild.others_radiogroup
                        .setOnCheckedChangeListener(new com.jsy_jiaobao.customview.RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(
                                    com.jsy_jiaobao.customview.RadioGroup group,
                                    int checkedId) {
                                MobclickAgent
                                        .onEvent(
                                                mContext,
                                                mContext.getResources()
                                                        .getString(
                                                                R.string.TeacherPublishWorkActivity_choseLongTime));
                                switch (checkedId) {
                                    // 选择的为10min
                                    case R.id.tree_workol_others_radio10:
                                        LongTime = 10;
                                        break;
                                    // 选择的为20min
                                    case R.id.tree_workol_others_radio20:
                                        LongTime = 20;
                                        break;
                                    // 选择的为30min
                                    case R.id.tree_workol_others_radio30:
                                        LongTime = 30;
                                        break;
                                    // 选择的为40min
                                    case R.id.tree_workol_others_radio40:
                                        LongTime = 40;
                                        break;
                                    // 选择的为60min
                                    case R.id.tree_workol_others_radio60:
                                        LongTime = 60;
                                        break;
                                    // 选择的为90min
                                    case R.id.tree_workol_others_radio90:
                                        LongTime = 90;
                                        break;
                                    // 选择的为120min
                                    case R.id.tree_workol_others_radio120:
                                        LongTime = 120;
                                        break;
                                    default:
                                        break;
                                }
                                // 数据保存为当前选中时长
                                groupData.get(GTYPE_OTHERS).setLongTime(LongTime);
                                // 魅族数据都添加选中的时长
                                for (int i = 0; i < teaMakeHWList.size(); i++) {
                                    teaMakeHWList.get(i).setLongTime(LongTime);
                                }
                            }
                        });
                break;
            default:
                convertView = null;
                break;

        }

        return convertView;
    }

    /**
     * 时长选择 默认都不选
     */
    private void getDefaultCheck() {
        // TODO Auto-generated method stub
        mViewChild.radio10.setChecked(false);
        mViewChild.radio20.setChecked(false);
        mViewChild.radio30.setChecked(false);
        mViewChild.radio40.setChecked(false);
        mViewChild.radio60.setChecked(false);
        mViewChild.radio90.setChecked(false);
        mViewChild.radio120.setChecked(false);
    }

    @Override
    public Object getGroup(int groupPosition) {

        return null;
    }

    @Override
    public int getGroupCount() {
        return groupData == null ? 0 : groupData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getChildTypeCount() {
        // TODO Auto-generated method stub
        return 11;
    }

    @Override
    public int getGroupTypeCount() {
        // TODO Auto-generated method stub
        return 11;
    }

    @Override
    public int getGroupType(int groupPosition) {
        // TODO Auto-generated method stub
        return groupData == null ? 0 : groupData.get(groupPosition).getType();
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        RadioGroup radioGroup;
        ImageView toggle;
        TextView name;
        TextView lable;
        LinearLayout layout_chose;
        RadioGroup rg_chose;
        Spinner sp_level;
        CheckBox cb_parent;
        CheckBox cb_feed;
        LinearLayout layout;
        int type = getGroupType(groupPosition);

        switch (type) {

            case GTYPE_MODE:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_modechose, parent, false);
                radioGroup = (RadioGroup) convertView
                        .findViewById(R.id.tree_modechose_radiogroup);
                work_title = groupData.get(GTYPE_OTHERS).getCurrName();
                /**
                 * 根据不同模式，选中的不同选项
                 */
                switch (TYPE_MODE) {
                    case MTYPE_individual:
                        radioGroup.check(R.id.tree_modechose_radio1);
                        break;
                    case MTYPE_collective:
                        radioGroup.check(R.id.tree_modechose_radio2);

                        break;
                    case MTYPE_ab:
                        radioGroup.check(R.id.tree_modechose_radio3);

                        break;
                    case MTYPE_custom:
                        radioGroup.check(R.id.tree_modechose_radio4);
                        break;

                    default:
                        break;
                }
                /**
                 * RadioGroup的选择事件 按钮组的选择事件监听
                 */

                radioGroup
                        .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group,
                                                         int checkedId) {
                                work_title = formatterTitle.format(new Date())
                                        + groupData.get(GTYPE_SUBJECT)
                                        .getCurrName()
                                        + groupData.get(GTYPE_SESSION)
                                        .getCurrName() + "作业";
                                groupData.get(GTYPE_OTHERS).setCurrName(work_title);
                                // if(oldTitle!=null){
                                // mViewChild.others_title.setText(oldTitle);
                                // }
                                switch (checkedId) {
                                    case R.id.tree_modechose_radio1:
                                        TYPE_MODE = MTYPE_individual;
                                        listView.collapseGroup(GTYPE_CUSTOM);
                                        break;
                                    case R.id.tree_modechose_radio2:
                                        TYPE_MODE = MTYPE_collective;
                                        listView.collapseGroup(GTYPE_CUSTOM);
                                        break;
                                    case R.id.tree_modechose_radio3:
                                        TYPE_MODE = MTYPE_ab;
                                        listView.collapseGroup(GTYPE_CUSTOM);
                                        break;
                                    case R.id.tree_modechose_radio4:
                                        TYPE_MODE = MTYPE_custom;
                                        int chapterID = groupData.get(GTYPE_SESSION)
                                                .getCurrID();
                                        if (chapterID != 0) {
                                            groupData.get(GTYPE_CUSTOM).setCurrID(0);
                                            groupData.get(GTYPE_CUSTOM).setCurrName("");
                                            groupData.get(GTYPE_CUSTOM).setData(null);
                                            TeacherPublishWorkActivityController
                                                    .getInstance().GetDesHWList(
                                                    chapterID);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                for (TeaMakeHW item : teaMakeHWList) {
                                    item.setHwType(TYPE_MODE);
                                }
                                TeaExpandableListViewAdapter.this
                                        .notifyDataSetChanged();
                            }
                        });
                break;
            // 班级
            case GTYPE_CLAZZ:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
//			lable = (TextView) convertView
//					.findViewById(R.id.tree_workol_clazz_tv_1);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                layout_chose = (LinearLayout) convertView
                        .findViewById(R.id.tree_workol_clazz_layout_chose);
                sp_level = (Spinner) convertView
                        .findViewById(R.id.tree_workol_clazz_spinner);
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ArrayList<Object> list = new ArrayList<>();
                        list.add(Constants.WORKOL_TecMakeHWClick);
                        list.add(groupPosition);
                        list.add(isExpanded);
                        EventBusUtil.post(list);
                    }
                });
                TeaGroupData cdata = groupData.get(type);
                String cname = cdata.getCurrName();
                if ("0".equals(cname)) {
                    name.setText(mContext.getResources().getString(
                            R.string.workol_select_class));
                } else {
                    name.setText(mContext.getString(R.string.had_chose_classes, cname));
                }
                /**
                 * 不同的试卷模式
                 */
                switch (TYPE_MODE) {
                    // 个性
                    case MTYPE_individual:
                        layout_chose.setVisibility(View.GONE);

                        break;
                    // 统一
                    case MTYPE_collective:
                        // ab卷
                    case MTYPE_ab:
                        layout_chose.setVisibility(View.VISIBLE);
                        sp_level.setAdapter(_Adapter);

                        int dolv1 = teaMakeHWList.get(0).getDoLv();
                        boolean isEqual1 = true;
                        for (int i = 0; i < teaMakeHWList.size(); i++) {
                            TeaMakeHW teaMakeHW = teaMakeHWList.get(i);
                            if (dolv1 != teaMakeHW.getDoLv()) {
                                isEqual1 = false;
                                break;
                            }
                        }
                        if (isEqual1) {
                            sp_level.setSelection(dolv1 - 1);
                        } else {
                            sp_level.setSelection(1);
                            for (int i = 0; i < teaMakeHWList.size(); i++) {
                                TeaMakeHW teaMakeHW = teaMakeHWList.get(i);
                                teaMakeHW.setDoLv(2);
                                teaMakeHWList.set(i, teaMakeHW);
                            }
                        }
                        // 难度选择
                        sp_level.setOnItemSelectedListener(new OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int position, long id) {
                                for (int i = 0; i < teaMakeHWList.size(); i++) {
                                    TeaMakeHW teaMakeHW = teaMakeHWList.get(i);
                                    teaMakeHW.setDoLv(position + 1);
                                    teaMakeHWList.set(i, teaMakeHW);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        break;
                    // 自定义试卷
                    case MTYPE_custom:
                        layout_chose.setVisibility(View.GONE);
                        for (int i = 0; i < teaMakeHWList.size(); i++) {
                            TeaMakeHW teaMakeHW = teaMakeHWList.get(i);
                            teaMakeHW.setDoLv(3);
                            teaMakeHWList.set(i, teaMakeHW);
                        }
                        break;

                    default:
                        break;
                }
                break;
            // 年级
            case GTYPE_GRADE:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                lable.setText(mContext.getResources().getString(
                        R.string.workol_select_grade));
                TeaGroupData gdata = groupData.get(type);
                String gname = gdata.getCurrName();
                name.setText(TextUtils.isEmpty(gname) ? "无年级" : gname);
                break;
            // 科目
            case GTYPE_SUBJECT:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                // 是否已展开子选项
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                lable.setText(mContext.getResources().getString(
                        R.string.workol_select_mode));
                TeaGroupData sdata = groupData.get(type);
                String sname = sdata.getCurrName();
                name.setText(TextUtils.isEmpty(sname) ? "无科目" : sname);
                break;
            // 教版选择
            case GTYPE_GOV:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                /**
                 * 是否展开选项
                 */
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                lable.setText(mContext.getResources().getString(
                        R.string.workol_select_subject));
                TeaGroupData govdata = groupData.get(type);
                String govname = govdata.getCurrName();
                name.setText(TextUtils.isEmpty(govname) ? "无教版" : govname);
                break;
            /**
             * 章节选项
             */
            case GTYPE_SESSION:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                /**
                 * 是否已展开
                 */
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                lable.setText(mContext.getResources().getString(
                        R.string.workol_select_session));
                TeaGroupData sedata = groupData.get(type);
                String sename = sedata.getCurrName();
                name.setText(TextUtils.isEmpty(sename) ? "无章节" : sename);
                break;
            case GTYPE_IMPERSONALITY:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_impersonality, parent, false);
                layout = (LinearLayout) convertView
                        .findViewById(R.id.tree_workol_impersonality_layout);
                rg_chose = (RadioGroup) convertView
                        .findViewById(R.id.tree_workol_impersonality_chose);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_impersonality_lable);
                layout.setVisibility(View.VISIBLE);
                lable.setText("选择题");
                switch (TYPE_MODE) {
                    case MTYPE_custom:
                        layout.setVisibility(View.GONE);

                        break;

                    default:
                        // 选择题题量
                        rg_chose.setOnCheckedChangeListener(null);
                        if (teaMakeHWList.size() > 0) {
                            switch (teaMakeHWList.get(0).getSelNum()) {
                                case 5:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio5);
                                    break;
                                case 10:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio10);
                                    break;
                                case 20:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio20);
                                    break;
                                case 40:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio40);
                                    break;

                                default:
                                    break;
                            }
                        }
                        // 填空题题量
                        rg_chose.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                int SelNum = 5;
                                switch (checkedId) {
                                    case R.id.tree_workol_impresonality_radio5:
                                        SelNum = 5;
                                        break;
                                    case R.id.tree_workol_impresonality_radio10:
                                        SelNum = 10;
                                        break;
                                    case R.id.tree_workol_impresonality_radio20:
                                        SelNum = 20;
                                        break;
                                    case R.id.tree_workol_impresonality_radio40:
                                        SelNum = 40;
                                        break;
                                    default:
                                        break;
                                }
                                for (int i = 0; i < teaMakeHWList.size(); i++) {
                                    teaMakeHWList.get(i).setSelNum(SelNum);
                                }
                            }
                        });
                        break;
                }
                break;
            case GTYPE_IMPERSONALITY2:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_impersonality, parent, false);
                layout = (LinearLayout) convertView
                        .findViewById(R.id.tree_workol_impersonality_layout);
                rg_chose = (RadioGroup) convertView
                        .findViewById(R.id.tree_workol_impersonality_chose);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_impersonality_lable);
                layout.setVisibility(View.VISIBLE);
                lable.setText("填空题");
                switch (TYPE_MODE) {
                    case MTYPE_custom:
                        layout.setVisibility(View.GONE);
                        break;
                    default:
                        // 填空题题量选择
                        rg_chose.setOnCheckedChangeListener(null);
                        if (teaMakeHWList.size() > 0) {
                            switch (teaMakeHWList.get(0).getInpNum()) {
                                case 5:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio5);
                                    break;
                                case 10:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio10);
                                    break;
                                case 20:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio20);
                                    break;
                                case 40:
                                    rg_chose.check(R.id.tree_workol_impresonality_radio40);
                                    break;

                                default:
                                    break;
                            }
                        }
                        rg_chose.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                int SelNum = 5;
                                switch (checkedId) {
                                    case R.id.tree_workol_impresonality_radio5:
                                        SelNum = 5;
                                        break;
                                    case R.id.tree_workol_impresonality_radio10:
                                        SelNum = 10;
                                        break;
                                    case R.id.tree_workol_impresonality_radio20:
                                        SelNum = 20;
                                        break;
                                    case R.id.tree_workol_impresonality_radio40:
                                        SelNum = 40;
                                        break;
                                    default:
                                        break;
                                }
                                for (int i = 0; i < teaMakeHWList.size(); i++) {
                                    teaMakeHWList.get(i).setInpNum(SelNum);
                                }
                            }
                        });
                        break;
                }
                break;
            case GTYPE_CUSTOM:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                RelativeLayout layout1 = (RelativeLayout) convertView
                        .findViewById(R.id.tree_workol_clazz_layout);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);
                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                layout1.setVisibility(View.GONE);
                switch (TYPE_MODE) {
                    case MTYPE_custom:
                        layout1.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                lable.setText("自定义:");
                name.setText(groupData.get(type).getCurrName());
                convertView.setTag(R.id.groupId, GTYPE_CUSTOM);
                break;
            /**
             * 其他选项
             */
            case GTYPE_OTHERS:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_clazza, parent, false);
                lable = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_1);
                name = (TextView) convertView
                        .findViewById(R.id.tree_workol_clazz_tv_name);

                toggle = (ImageView) convertView
                        .findViewById(R.id.tree_workol_clazz_iv_toggle);
                if (isExpanded) {
                    toggle.setImageResource(R.drawable.icon_workol_open);
                } else {
                    toggle.setImageResource(R.drawable.icon_workol_close);
                }
                lable.setText("其他选项:");
                name.setText("");
                break;
            /**
             * 短信勾选
             */
            case GTYPE_SMSG:
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_workol_tree_smsg, parent, false);
                cb_parent = (CheckBox) convertView
                        .findViewById(R.id.tree_workol_smsg_parent);
                cb_feed = (CheckBox) convertView
                        .findViewById(R.id.tree_workol_smsg_feed);
                cb_parent.setOnCheckedChangeListener(null);
                cb_feed.setOnCheckedChangeListener(null);

                if (teaMakeHWList.size() > 0) {
                    cb_parent.setChecked(teaMakeHWList.get(0).getIsQsSms());
                    cb_feed.setChecked(teaMakeHWList.get(0).getIsRep());
                }
                cb_parent
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         boolean isChecked) {
                                for (int i = 0; i < teaMakeHWList.size(); i++) {
                                    teaMakeHWList.get(i).setIsQsSms(isChecked);
                                }
                            }
                        });
                cb_feed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        for (int i = 0; i < teaMakeHWList.size(); i++) {
                            teaMakeHWList.get(i).setIsRep(isChecked);
                        }

                    }
                });
                break;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    ViewChild mViewChild;

    static class ViewChild {

        LinearLayout layout_others;
        IEditText others_title;
        IEditText others_time;
        com.jsy_jiaobao.customview.RadioGroup others_radiogroup;
        // HorizontalScrollView others_scroll;
        LinearLayout layout_clazz;
        LinearLayout layout_clazz_radiogroup;
        RadioGroup clazz_radioGroup;
        CheckBox clazz_checkBox;
        LinearLayout layout_grade;
        TextView grade_name;
        RadioButton grade_radio;
        RelativeLayout layout_txt;
        TextView txt_name;
        ImageView txt_toggle;
        RadioButton txt_radio;
        RadioButton radio10;
        RadioButton radio20;
        RadioButton radio30;
        RadioButton radio40;
        RadioButton radio60;
        RadioButton radio90;
        RadioButton radio120;

    }

    public void createTeaHWList() {
        teaMakeHWList.clear();
        @SuppressWarnings("unchecked")
        ArrayList<UserClass> list = (ArrayList<UserClass>) groupData.get(
                GTYPE_CLAZZ).getData();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TeaMakeHW teaMakeHW = new TeaMakeHW();
                teaMakeHW.setHwType(TYPE_MODE);
                teaMakeHW.setClassID(list.get(i).getClassID());
                teaMakeHW.setClassName(list.get(i).getClassName());
                teaMakeHW.setTeacherJiaobaohao(Integer.parseInt(BaseActivity.sp
                        .getString("JiaoBaoHao", "")));
                teaMakeHW.setSelNum(10);
                teaMakeHW.setInpNum(10);
                teaMakeHW.setDoLv(2);
                teaMakeHW.setLongTime(20);
                teaMakeHW.setIsRep(true);
                teaMakeHW.setIsQsSms(true);
                teaMakeHW.setTecName(BaseActivity.sp.getString("UserName", ""));
                teaMakeHW.setSchoolName(list.get(i).getSchoolName());
                teaMakeHW
                        .setChapterID(groupData.get(GTYPE_SESSION).getCurrID());
                teaMakeHW.setDesId(groupData.get(GTYPE_CUSTOM).getCurrID());
                teaMakeHWList.add(teaMakeHW);
            }
        }
    }

    /**
     * 自定义作业,更新数据
     */
    public void updataDes() {
        for (int i = 0; i < teaMakeHWList.size(); i++) {
            teaMakeHWList.get(i).setDesId(
                    groupData.get(GTYPE_CUSTOM).getCurrID());
        }
    }

    public void setGroupData(ArrayList<TeaGroupData> groupData) {
        this.groupData = groupData;
        childData.clear();
        for (int i = 0; i < groupData.size(); i++) {
            childData.add(i, groupData.get(i).getData());
        }
        for (int i = 0; i < teaMakeHWList.size(); i++) {
            teaMakeHWList.get(i).setChapterID(
                    groupData.get(GTYPE_SESSION).getCurrID());
        }
        GradeCode = groupData.get(GTYPE_GRADE).getCurrID();
        SubjectCode = groupData.get(GTYPE_SUBJECT).getCurrID();
        uidCode = groupData.get(GTYPE_GOV).getCurrID();
        work_title = groupData.get(GTYPE_OTHERS).getCurrName();
        LongTime = groupData.get(GTYPE_OTHERS).getLongTime();

    }

    public void GetDesHWList() {
        if (TYPE_MODE == MTYPE_custom) {
            try {
                @SuppressWarnings("unchecked")
                ArrayList<TeaSession> list = (ArrayList<TeaSession>) groupData
                        .get(GTYPE_SESSION).getData();
                if (list != null && list.size() > 0) {
                    TeacherPublishWorkActivityController.getInstance()
                            .GetDesHWList(
                                    groupData.get(GTYPE_SESSION).getCurrID());
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    private String slect(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (content.startsWith(" ")) {
                content = content.substring(1);
                content = slect(content);
            }
            if (content.endsWith(" ")) {
                content = content.substring(0, content.length() - 1);
                content = slect(content);
            }
        }
        return content;

    }

    public ArrayList<TeaMakeHW> getResult() {
        if (haveTecQs) {
            if (!TextUtils.isEmpty(work_time)) {
                Date currTime = new Date();
                Date d ;
                try {
                    d = formatter.parse(work_time);
                } catch (ParseException e) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.workol_hw_errortime));
                    return null;
                }
                if (currTime.before(d)) {
                    ArrayList<TeaMakeHW> list = new ArrayList<>();
                    for (int i = 0; i < teaMakeHWList.size(); i++) {
                        if (teaMakeHWList.get(i).isChecked()) {
                            list.add(teaMakeHWList.get(i));
                        }
                    }
                    work_title = slect(groupData.get(GTYPE_OTHERS)
                            .getCurrName());
                    if (list.size() > 0) {
                        if (!TextUtils.isEmpty(work_time)) {
                            if (!TextUtils.isEmpty(work_title.trim())) {
                                if (work_title.length() > 5
                                        && work_title.length() < 50) {
                                    for (int i = 0; i < list.size(); i++) {
                                        list.get(i).setHomeworkName(work_title);
                                        list.get(i).setExpTime(work_time);
                                    }
                                } else {
                                    ToastUtil
                                            .showMessage(
                                                    mContext,
                                                    mContext.getResources()
                                                            .getString(
                                                                    R.string.workol_hw_errtitle));
                                    return null;
                                }
                            } else {
                                ToastUtil.showMessage(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.workol_hw_notitle));
                                return null;
                            }
                        } else {
                            ToastUtil.showMessage(
                                    mContext,
                                    mContext.getResources().getString(
                                            R.string.workol_hw_notime));
                            return null;
                        }
                        TeaMakeHW item = list.get(0);
                        if (!item.check()) {
                            ToastUtil.showMessage(
                                    mContext,
                                    mContext.getResources().getString(
                                            R.string.workol_hw_nocontent));
                            return null;
                        }
                    } else {
                        ToastUtil.showMessage(mContext, mContext.getResources()
                                .getString(R.string.workol_select_class));
                        return null;
                    }
                    switch (TYPE_MODE) {
                        case MTYPE_custom:
                            for (int i = 0; i < list.size(); i++) {
                                int id = list.get(i).getDesId();
                                if (id == 0) {
                                    ToastUtil.showMessage(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.workol_select_desid));
                                    return null;
                                }
                            }
                            return list;

                        default:
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setDesId(0);
                            }
                            return list;
                    }
                } else {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.workol_hw_error_time));
                    return null;
                }
            } else {
                ToastUtil.showMessage(mContext, mContext.getResources()
                        .getString(R.string.workol_hw_notime));
                return null;
            }
        } else {
            ToastUtil.showMessage(
                    mContext,
                    mContext.getResources().getString(
                            R.string.workol_session_nocontent));
            return null;
        }
    }


    public void setHaveTecQs(boolean haveTecQs) {
        this.haveTecQs = haveTecQs;
    }

    private void setVisible(TeaSession parent, ArrayList<TeaSession> list,
                            boolean isVisible) {
        for (TeaSession session : list) {
            if (session.getPid() == parent.getTabID()) {
                session.setVisible(isVisible);
                if (!isVisible) {
                    session.setChildrenOpened(isVisible);
                    setVisible(session, list, isVisible);
                }
            }
        }
    }
}