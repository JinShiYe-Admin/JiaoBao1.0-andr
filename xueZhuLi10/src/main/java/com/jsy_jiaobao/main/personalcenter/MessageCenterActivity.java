package com.jsy_jiaobao.main.personalcenter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.UpdateService;
import com.jsy_jiaobao.main.affairs.WorkFragment2;
import com.jsy_jiaobao.main.affairs.WorkSendToSbActivity2;
import com.jsy_jiaobao.main.appcenter.sign.LocationActivity;
import com.jsy_jiaobao.main.appcenter.sign.QuickSignInActivity;
import com.jsy_jiaobao.main.appcenter.sign.ShowPopup;
import com.jsy_jiaobao.main.appcenter.workplan.WorkPlanActivity;
import com.jsy_jiaobao.main.leave.CheckerActivity;
import com.jsy_jiaobao.main.leave.GenActivity;
import com.jsy_jiaobao.main.leave.HeadTeacherActivity;
import com.jsy_jiaobao.main.leave.TeacherActivity;
import com.jsy_jiaobao.main.schoolcircle.ShowFragment2;
import com.jsy_jiaobao.main.system.LoginActivity;
import com.jsy_jiaobao.main.system.PersonalCenterActivity;
import com.jsy_jiaobao.main.system.VisitPublicHttp;
import com.jsy_jiaobao.main.workol.Constants;
import com.jsy_jiaobao.main.workol.GenCheckWorkActivity;
import com.jsy_jiaobao.main.workol.StudentWorkActivity;
import com.jsy_jiaobao.main.workol.TeacherPublishWorkActivity;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.personal.PublishPermission;
import com.jsy_jiaobao.po.push.AliasType;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.VersionInfo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.view.TabView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * 主界面
 *
 * @author admin
 */

public class MessageCenterActivity extends BaseActivity implements PublicMethod {
    private LinearLayout layout_ui;
    private static String[] TITLE;
    public static String MessageCenterTitle;
    private TabPageIndicator indicator;
    private Context mContext;
    private final static String TAG = "MessageCenterActivity";
    private PopupWindow ppw;
    private TextView title;
    private TabView[] titles = new TabView[3];
    private int position = 0;
    public static final String NEWAFFAIRNOTICE = "new affair notice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "*****onCreate******");
        MobclickAgent.openActivityDurationTrack(false);
        mContext = this;
        initPassData();
        initViews();
        initDeatilsData();
        initListener();
        setAlias();
    }

    private void setNoticeListener() {
        Log.e(TAG, "*****setNoticeListener******");
        Intent intent = getIntent();
        boolean isNotice = intent.getBooleanExtra(NEWAFFAIRNOTICE, false);
        Log.e(TAG, "是否是通知：" + isNotice);
        if (isNotice) {
            indicator.setCurrentItem(2);
        }
    }

    @Override
    public void initPassData() {

    }

    /**
     * 初始化界面
     */
    @Override
    public void initViews() {
        setContentLayout(R.layout.tabpageindicatorviewpager);
//        clearNotification();
        ShortcutBadger.removeCount(this);
        SharedPreferences.Editor editor = getSharedPreferences("messageNum", MODE_PRIVATE).edit();
        editor.putString("num","0");
        editor.commit();
        layout_ui = (LinearLayout) findViewById(R.id.base_messagecenter_layout);
        mContext = this;
        MessageCenterActivityController.getInstance().setContext(this);
        getSupportActionBar().setCustomView(R.layout.actionbar_titile);
        title = (TextView) findViewById(R.id.actionbar_title);
        // 选项卡 字符串
        TITLE = new String[]{
                getResources().getString(R.string.messagecenter_title_qiuzhi),
                getResources().getString(R.string.messagecenter_title_show),
                getResources().getString(R.string.messagecenter_title_work)
        };
        // ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(
                getSupportFragmentManager());
        NoScrollViewPager pager = (NoScrollViewPager) findViewById(R.id.base_layout_pager);
        pager.setAdapter(adapter);
        // 实例化TabPageIndicator然后设置ViewPager与之关联
        indicator = (TabPageIndicator) findViewById(R.id.base_tab_indicator);
        indicator.setViewPager(pager);

        titles[0] = (TabView) indicator.findViewWithTag(0);
        titles[1] = (TabView) indicator.findViewWithTag(1);
        titles[2] = (TabView) indicator.findViewWithTag(2);
        titles[0].setVisibility(View.GONE);
        titles[1].setVisibility(View.GONE);
        indicator.setCurrentItem(2);
    }


    @Override
    public void initDeatilsData() {
        // notice_gridList.clear();
        Log.d("sp.getInt", sp.getInt("UnitID", 0) + "");
    }

    /**
     * ViewPager的页面更换监听事件
     */
    @Override
    public void initListener() {
        // 如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                MessageCenterTitle = TITLE[arg0];
                position = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * // * search的PopWindow //
     */

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // 求知界面
                    MobclickAgent.onEvent(
                            mContext,
                            getResources().getString(
                                    R.string.MessageCenterActivity_qiuzhi));
                    return QiuZhiFragment.newInstance();
                case 1:
                    // 学校圈
                    MobclickAgent.onEvent(
                            mContext,
                            getResources().getString(
                                    R.string.MessageCenterActivity_xuexiao));
                    return ShowFragment2.newInstance();
                case 2:
                    // 事务
                    MobclickAgent.onEvent(
                            mContext,
                            getResources().getString(
                                    R.string.MessageCenterActivity_shiwu));
                    return WorkFragment2.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
        }
    }

    /**
     * Activity的生命周期事件
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "*****onResume******");
//        clearNotification();
        ShortcutBadger.removeCount(MessageCenterActivity.this);

        SharedPreferences.Editor editor = getSharedPreferences("messageNum", MODE_PRIVATE).edit();
        editor.putString("num","0");
        editor.commit();
        EventBusUtil.register(this);
        MobclickAgent.onResume(this);
        setTitleText();
        if (position != 0) {
            ACache.get(getApplicationContext(), "qiuzhi").put("isOld", "true");
        }

        setNoticeListener();
    }

    /**
     * 生命周期事件
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "*****onPause******");
        EventBusUtil.unregister(this);
        MobclickAgent.onPause(this);
        setAlias();
        super.onPause();
    }

    /**
     * ActionBar的Title设置
     */
    private void setTitleText() {
        try {
            String nick = sp.getString("UserName", "");
            if (TextUtils.isEmpty(nick)) {
                editor.putString("UserName", sp.getString("TrueName", ""))
                        .commit();
                nick = sp.getString("TrueName", "");
                if (TextUtils.isEmpty(nick)) {
                    editor.putString("UserName", sp.getString("Nickname", ""))
                            .commit();
                    nick = sp.getString("Nickname", "");
                    if (TextUtils.isEmpty(nick)) {
                        nick = getResources().getString(R.string.unknown);
                    }
                }
            }
            String unit = sp.getString("UnitName", "");
            if (TextUtils.isEmpty(unit)) {
                unit = "";
            } else {
                unit += ":";
            }
            title.setText(unit + nick);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // private ArrayList<UnitSectionMessage> notice_gridList = new
    // ArrayList<UnitSectionMessage>();

    /**
     * 返回数据的处理
     *
     * @param list
     */
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            /**
             * 切换单位 根据切换后的角色权限 变更按钮
             */
            case Constant.msgcenter_work_change:
                setTitleText();
                if (sp.getInt("RoleIdentity", 1) > 2) {
                    menu.findItem(1003).setVisible(false);
                    menu.findItem(1003).setEnabled(false);
                    menu.findItem(1004).setVisible(false);
                    menu.findItem(1004).setEnabled(false);
                    menu.findItem(1005).setVisible(false);//快速签到
                    menu.findItem(1005).setEnabled(false);//快速签到
                    menu.findItem(1009).setVisible(false);
                    menu.findItem(1009).setEnabled(false);
                } else {
                    menu.findItem(1003).setVisible(false);
                    menu.findItem(1003).setEnabled(false);
                    menu.findItem(1004).setVisible(false);
                    menu.findItem(1004).setEnabled(false);
                    menu.findItem(1005).setVisible(true);//快速签到
                    menu.findItem(1005).setEnabled(true);//快速签到
                    menu.findItem(1009).setVisible(true);
                    menu.findItem(1009).setEnabled(true);
                }
                if (BaseActivity.sp.getInt("UnitID", 0) == 0) {
                    menu.findItem(1009).setVisible(false);
                    menu.findItem(1009).setEnabled(false);
                } else {
                    menu.findItem(1009).setVisible(true);
                    menu.findItem(1009).setEnabled(true);
                }
                if (sp.getInt("RoleIdentity", 1) == 2) {
                    menu.findItem(1006).setVisible(false);
                    menu.findItem(1007).setVisible(false);
                    menu.findItem(1008).setVisible(false);
                } else if (sp.getInt("RoleIdentity", 1) == 3) {
                    menu.findItem(1006).setVisible(false);
                    menu.findItem(1007).setVisible(false);
                    menu.findItem(1008).setVisible(false);
                } else if (sp.getInt("RoleIdentity", 1) == 4) {
                    menu.findItem(1006).setVisible(false);
                    menu.findItem(1007).setVisible(false);
                    menu.findItem(1008).setVisible(false);
                    menu.findItem(888).setVisible(false);
                    menu.findItem(999).setVisible(false);
                } else {
                    menu.findItem(1006).setVisible(false);
                    menu.findItem(1007).setVisible(false);
                    menu.findItem(1008).setVisible(false);
                }
                menu.findItem(1006).setVisible(false);
                menu.findItem(1007).setVisible(false);
                menu.findItem(1008).setVisible(false);
                break;
            // 获取家长学生信息，然后设置ActionBarTitle
            case Constants.WORKOL_getGenInfo:
            case Constants.WORKOL_getStuInfo:
                setTitleText();
                break;
            case Constant.msgcenter_train_getUnitNotics:// 得到单位通知文章
                setTitleText();
                break;
            case Constant.msgcenter_checkversion:// 得到最新版本
                VersionInfo versionInfo = (VersionInfo) list.get(1);
                int currCode = BaseUtils.getVersionCode(getApplicationContext());
                if (currCode < versionInfo.getVersionCode()) {
                    dialog_version(versionInfo);
                }
                break;
            // 更新版本
            case Constant.msgcenter_updataversion:
                DialogUtil.getInstance().cannleDialog();
                break;
            // 判断是有未回复或未查看的信息，如果有 显示红点
            case Constant.msgcenter_work_notice:
                int number = (Integer) list.get(1);
                if (number > 0)
                    titles[2].notice.setVisibility(View.VISIBLE);
                else
                    titles[2].notice.setVisibility(View.INVISIBLE);
                break;
            case Constant.msgcenter_show_notice:
                // int number11 = (Integer) list.get(1);
                // if (number11>0){
                // titles[3].notice.setVisibility(0);
                // }else{
                // titles[3].notice.setVisibility(4);
                // }
                // break;
                // case Constant.msgcenter_show_getUnitSectionMessages:
                // GetUnitSectionMessage getUnitSectionMessage1 =
                // (GetUnitSectionMessage) list.get(1);
                // ArrayList<UnitSectionMessage> UnitSectionMessageList1 =
                // getUnitSectionMessage1.getList();
                // show_gridList.addAll(UnitSectionMessageList1);
                // UnitSectionMessage junior1 = new UnitSectionMessage();
                // junior1.UnitName =
                // getResources().getString(R.string.messagecenter_juniorunit);
                // show_gridList.add(junior1);
                //
                // ArrayList<UnitSectionMessage> myunitinfo1 = new
                // ArrayList<UnitSectionMessage>();
                // for (UnitSectionMessage unititem : UnitSectionMessageList1) {
                // if (unititem.getIsMyUnit() == 1 ) {
                // show_artnum += unititem.MessageCount;
                // myunitinfo1.add(unititem);
                // }
                // }
                // artCache.put("show_all", show_gridList);
                // artCache.put("show_mine", myunitinfo1);
                // ArrayList<Object> post1 = new ArrayList<Object>();
                // post1.add(Constant.msgcenter_show_notice);
                // post1.add(show_artnum);
                // EventBusUtil.post(post1);
                //
                break;
            case Constant.msgcenter_publish_permission:// 发布单位动态，通过取我可发动态的单位判断
                DialogUtil.getInstance().cannleDialog();
                @SuppressWarnings("unchecked")
                ArrayList<PublishPermission> permissionlist = (ArrayList<PublishPermission>) list
                        .get(1);
                if (permissionlist == null || permissionlist.size() == 0) {
                    ToastUtil.showMessage(mContext, "无权限");
                } else {
                    Intent publish = new Intent(mContext,
                            PublishArticaleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SectionFlag", "2");
                    bundle.putSerializable("UnitList", permissionlist);
                    publish.putExtras(bundle);
                    startActivity(publish);
                }
                break;
            // 获取我的班级
            case Constant.msgcenter_publish_getmyUserClass:
                DialogUtil.getInstance().cannleDialog();
                @SuppressWarnings("unchecked")
                ArrayList<UserClass> list1 = (ArrayList<UserClass>) list.get(1);
                Intent publish = new Intent(mContext, PublishArticaleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("SectionFlag", "1");
                bundle.putSerializable("UnitList", list1);
                publish.putExtras(bundle);
                startActivity(publish);
                break;
            /**
             * 请假详情
             */
            case LeaveConstant.leave_GetLeaveSetting:
                getLeaveSetting();
                break;
            default:
                break;
        }
    }

    /**
     * 获取leave权限
     */
    private void getLeaveSetting() {
        int role = sp.getInt("RoleIdentity", 1);
        boolean isHasStdLeave = sp.getBoolean("StatusStd", false);
        boolean isHasTeaLeave = sp.getBoolean("Status", false);
        boolean isGateGuard = sp.getBoolean("GateGuardList", false);
        boolean hasApprovalStdA = sp.getBoolean("ApproveListStdA", false);
        boolean hasApprovalStdB = sp.getBoolean("ApproveListStdB", false);
        boolean hasApprovalStdC = sp.getBoolean("ApproveListStdC", false);
        boolean hasApprovalStdD = sp.getBoolean("ApproveListStdD", false);
        boolean hasApprovalStdE = sp.getBoolean("ApproveListStdE", false);
        boolean hasApprovalA = sp.getBoolean("ApproveListA", false);
        boolean hasApprovalB = sp.getBoolean("ApproveListB", false);
        boolean hasApprovalC = sp.getBoolean("ApproveListC", false);
        boolean hasApprovalD = sp.getBoolean("ApproveListD", false);
        boolean hasApprovalE = sp.getBoolean("ApproveListE", false);
        Boolean[] bo = {hasApprovalStdA, hasApprovalStdB, hasApprovalStdC,
                hasApprovalStdD, hasApprovalStdE, hasApprovalA, hasApprovalB,
                hasApprovalC, hasApprovalD, hasApprovalE};
        Log.d(TAG, "isHasStdLeave" + isHasStdLeave);
        Log.d(TAG, "isHasTeaLeave" + isHasTeaLeave);
        Log.d(TAG, "role" + role);
        if (isHasStdLeave && isHasTeaLeave) {
            if (role == 3 || role == 2) {
                menu.findItem(888).setVisible(false);
            } else {
                menu.findItem(888).setVisible(false);
            }
        } else if (isHasStdLeave) {
            if (role == 3) {
                menu.findItem(888).setVisible(false);
            } else {
                menu.findItem(888).setVisible(false);
            }

        } else if (isHasTeaLeave) {
            if (role == 2) {
                menu.findItem(888).setVisible(false);
            } else {
                menu.findItem(888).setVisible(false);
            }
        } else {
            menu.findItem(888).setVisible(false);
        }

        boolean isHasCheck = false;
        if (isHasStdLeave || isHasTeaLeave) {
            for (boolean b : bo) {
                if (b) {
                    isHasCheck = true;
                }
            }
            if (role == 2) {
                if (sp.getInt("isAdmin", 0) == 2
                        || sp.getInt("isAdmin", 0) == 3) {
                    isHasCheck = true;
                }
            }
        }
        Log.d(TAG, "isHasCHeck" + isHasCheck);
        if (isHasCheck) {
            menu.findItem(999).setVisible(false);
        } else {
            menu.findItem(999).setVisible(false);
        }
        Log.d(TAG, "isGateGuard" + isGateGuard);
        if (isGateGuard) {
            menu.findItem(888).setVisible(false);
            menu.findItem(999).setVisible(false);
        }
    }

    /**
     * 提示框
     */
    protected void dialog_version(final VersionInfo versionInfo) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage(versionInfo.getIntroduce());
        builder.setTitle("检测到新版本");
        if (!versionInfo.getUpdata_1().equals("0")) {
            builder.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog_version(versionInfo);
                }
            });
        }
        builder.setNeutralButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String url = versionInfo.getUrl();
                Intent updateIntent = new Intent(mContext, UpdateService.class);
                updateIntent.putExtra(
                        "titleId",
                        mContext.getString(R.string.app_name)
                                + versionInfo.getVersionCode());
                updateIntent.putExtra("url", url);
                mContext.startService(updateIntent);
                if (!versionInfo.getUpdata_1().equals("0")) {
                    DialogUtil.getInstance().getDialog(mContext,
                            getResources().getString(R.string.public_loading));
                    DialogUtil.getInstance().setCanCancel(false);
                }
            }

        });
        if (versionInfo.getUpdata_1().equals("0")) {
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        builder.create().show();
    }

    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        SubMenu sub_more = menu.addSubMenu(0, 1, 0, "更多").setIcon(
                R.drawable.bar_more);
        sub_more.add(0, 1003, 0,
                getResources().getString(R.string.function_signin));
        sub_more.add(0, 1004, 0,
                getResources().getString(R.string.function_dailywork));

        sub_more.add(0, 1006, 0,
                getResources().getString(R.string.function_workol));
        sub_more.add(0, 1007, 0,
                getResources().getString(R.string.function_workol_gen));
        sub_more.add(0, 1008, 0,
                getResources().getString(R.string.function_workol_stu));
        sub_more.add(0, 888, 0,
                getResources().getString(R.string.function_leave));
        sub_more.add(0, 999, 0,
                getResources().getString(R.string.function_leave_approval));
        sub_more.add(0, 1005, 0,
                getResources().getString(R.string.function_signinquick));
        sub_more.getItem().setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_more.getItem(0).setIcon(R.drawable.bar_menu_signin);
        sub_more.getItem(1).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(2).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(3).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(4).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(5).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(6).setIcon(R.drawable.bar_menu_dailywork);
        sub_more.getItem(7).setIcon(R.drawable.bar_menu_dailywork);
        getLeaveSetting();
        //快速签到入口
        if (sp.getInt("RoleIdentity", 1) > 2) {
            menu.findItem(1005).setVisible(false);
        } else {
            // 角色为教育局人员或者老师
            menu.findItem(1005).setVisible(true);
        }
        if (sp.getInt("RoleIdentity", 1) == 2) {
            // 角色为老师
            menu.findItem(1006).setVisible(false);
            menu.findItem(1007).setVisible(false);
            menu.findItem(1008).setVisible(false);
        } else if (sp.getInt("RoleIdentity", 1) == 3) {
            // 角色为家长
            menu.findItem(1006).setVisible(false);
            menu.findItem(1007).setVisible(false);
            menu.findItem(1008).setVisible(false);
        } else if (sp.getInt("RoleIdentity", 1) == 4) {
            // 角色为学生
            menu.findItem(1006).setVisible(false);
            menu.findItem(1007).setVisible(false);
            menu.findItem(1008).setVisible(false);
            menu.findItem(888).setVisible(false);
        } else {
            // 其他
            menu.findItem(1006).setVisible(false);
            menu.findItem(1007).setVisible(false);
            menu.findItem(1008).setVisible(false);
        }
        menu.findItem(1006).setVisible(false);
        menu.findItem(1007).setVisible(false);
        menu.findItem(1008).setVisible(false);
        menu.findItem(1003).setVisible(false);
        menu.findItem(1004).setVisible(false);
        menu.findItem(888).setVisible(false);
        menu.findItem(999).setVisible(false);
        // 签到页面
        sub_more.getItem(0).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(mContext,
                                LocationActivity.class);
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_location));
                        startActivity(intent1);
                        return false;
                    }
                });
        // 日程记录
        sub_more.getItem(1).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(mContext,
                                WorkPlanActivity.class);
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_workPlan));
                        startActivity(intent1);
                        return false;
                    }
                });
        // 作业布置
        sub_more.getItem(2).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(mContext,
                                TeacherPublishWorkActivity.class);
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_publishWork));
                        startActivity(intent1);
                        return false;
                    }
                });
        // 家长查询
        sub_more.getItem(3).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(mContext,
                                GenCheckWorkActivity.class);
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_genCheck));
                        startActivity(intent1);
                        return false;
                    }
                });
        // 学生作业
        sub_more.getItem(4).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent1 = new Intent(mContext,
                                StudentWorkActivity.class);
                        MobclickAgent.onEvent(
                                mContext,
                                getResources().getString(
                                        R.string.MessageCenterActivity_myWork));
                        startActivity(intent1);
                        return false;
                    }
                });
        // 请假申请
        sub_more.getItem(5).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_leaveApply));
                        switch (sp.getInt("RoleIdentity", 1)) {
                            // 教师
                            case 2:
                                if (sp.getInt("isAdmin", 0) == 2
                                        || sp.getInt("isAdmin", 0) == 3) {
                                    Log.d(TAG, sp.getInt("isAdmin", 0) + "");
                                    Intent intent = new Intent(mContext,
                                            HeadTeacherActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(mContext,
                                            TeacherActivity.class);
                                    startActivity(intent);
                                }
                                return false;
                            // 家长
                            case 3:
                                Intent intent1 = new Intent(mContext,
                                        GenActivity.class);
                                startActivity(intent1);
                                return false;
                            default:
                                return false;
                        }

                    }
                });
        // 请假审核
        sub_more.getItem(6).setOnMenuItemClickListener(
                new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        Intent i = new Intent(mContext, CheckerActivity.class);
                        MobclickAgent
                                .onEvent(
                                        mContext,
                                        getResources()
                                                .getString(
                                                        R.string.MessageCenterActivity_leaveCheck));
                        startActivity(i);
                        return false;
                    }
                });
        SubMenu sub_menu = menu.addSubMenu(R.string.system).setIcon(
                R.drawable.bar_menu);
        sub_menu.add(1, 1020, 0,
                getResources().getString(R.string.function_publishquestion));
        sub_menu.add(1, 1009, 0,
                getResources().getString(R.string.function_publish_work));
        sub_menu.add(1, 1010, 0,
                getResources().getString(R.string.function_publish_show));
        sub_menu.add(1, 1011, 0,
                getResources().getString(R.string.function_publish_notice));
        sub_menu.add(1, 1012, 0,
                getResources().getString(R.string.function_pccenter));
        sub_menu.add(1, 1013, 0,
                getResources().getString(R.string.function_changeunit));
        sub_menu.add(1, 1014, 0,
                getResources().getString(R.string.function_changeuser));
        sub_menu.add(1, 1015, 0,
                getResources().getString(R.string.function_exit));
        sub_menu.getItem(0).setIcon(R.drawable.bar_menu_work);
        sub_menu.getItem(1).setIcon(R.drawable.bar_menu_work);
        sub_menu.getItem(2).setIcon(R.drawable.bar_menu_dt);
        sub_menu.getItem(3).setIcon(R.drawable.bar_menu_fx);
        sub_menu.getItem(4).setIcon(R.drawable.bar_menu_changejiaobao);
        sub_menu.getItem(5).setIcon(R.drawable.bar_menu_changeunit);
        sub_menu.getItem(6).setIcon(R.drawable.bar_menu_changejiaobao);
        sub_menu.getItem(7).setIcon(R.drawable.bar_menu_exit);
        sub_menu.getItem().setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        if (sp.getInt("RoleIdentity", 1) > 2) {
            // 教师
            menu.findItem(1003).setVisible(false);
            menu.findItem(1003).setEnabled(false);
            menu.findItem(1004).setVisible(false);
            menu.findItem(1004).setEnabled(false);
            menu.findItem(1009).setVisible(false);
            menu.findItem(1009).setEnabled(false);
        } else {
            // 非教师
            menu.findItem(1003).setVisible(false);
            menu.findItem(1003).setEnabled(false);
            menu.findItem(1004).setVisible(false);
            menu.findItem(1004).setEnabled(false);
            menu.findItem(1009).setVisible(true);
            menu.findItem(1009).setEnabled(true);
            if (BaseActivity.sp.getInt("UnitID", 0) == 0) {
                // 单位为空，未加入单位
                menu.findItem(1009).setVisible(false);
                menu.findItem(1009).setEnabled(false);
            } else {
                // 已加入单位
                menu.findItem(1009).setVisible(true);
                menu.findItem(1009).setEnabled(true);
            }
        }
        menu.findItem(1020).setVisible(false);
        menu.findItem(1020).setEnabled(false);
        menu.findItem(1010).setVisible(false);
        menu.findItem(1010).setEnabled(false);
        menu.findItem(1011).setVisible(false);
        menu.findItem(1011).setEnabled(false);
        return true;
    }

    /**
     * menu的响应事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ppw.dismiss();
                break;
            // 发布问题
            case 1020:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_publishQuestion));
                UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
                        .getAsObject("userInfo");
                if (userInfo.isIsKnlFeezeUser()) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_user));
                    return false;
                }
                if (userInfo.getDUnitId() == 0) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nounit));
                    return false;
                }
                if (userInfo.getNickName() == null
                        || userInfo.getNickName().equals("")) {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_error_nonick));
                    return false;
                }
                Intent intent1 = new Intent(mContext,
                        QiuZhiPublishQuestionActivity.class);
                startActivity(intent1);
                break;
            // 发布事务
            case 1009:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_publishMyWork));
                if (sp.getInt("RoleIdentity", 1) < 3) {
                    startActivity(new Intent(mContext, WorkSendToSbActivity2.class));
                } else {
                    ToastUtil.showMessage(mContext, R.string.no_permission);
                }
                break;
            // 发布单位动态
            case 1010:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_publishUnitNews));
                MessageCenterActivityController.getInstance().GetReleaseNewsUnits();

                break;
            // 发布分享
            case 1011:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_publishNotice));
                if (sp.getInt("RoleIdentity", 1) < 5) {
                    if (sp.getInt("RoleIdentity", 1) == 2) {
                        MessageCenterActivityController.getInstance()
                                .getmyUserClass(sp.getInt("UnitID", 0));
                    } else {
                        Intent publish1 = new Intent(mContext,
                                PublishArticaleActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("SectionFlag", "1");
                        publish1.putExtras(bundle1);
                        startActivity(publish1);
                    }
                }
                break;
            // 个人中心
            case 1012:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_personalCenter));
                Intent publish1 = new Intent(mContext, PersonalCenterActivity.class);
                startActivity(publish1);
                break;
            // 切换单位
            case 1013:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_changeUnit));
                if (Constant.listUserIdentity != null
                        && Constant.listUserIdentity.size() > 0) {
                    Rect frame = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                    int statusBarHeight = frame.top;
                    ShowPopup showPopup = new ShowPopup(mContext);
                    showPopup.showPop(layout_ui,
                            statusBarHeight + title.getBottom() + 2,
                            Constant.listUserIdentity, null);
                } else {
                    VisitPublicHttp.getInstance().setContext(this);
                    VisitPublicHttp.getInstance().getRoleIdentity();
                }

                break;
            // 切换用户
            case 1014:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_changeUser));
                httpLogout();
                BaseActivity.editor.putString("str_username", "");
                BaseActivity.editor.putString("UserPW", "").apply();
                delAlias();
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
            // 退出系统
            case 1015:
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.MessageCenterActivity_quiteSystem));
                delAlias();
                httpLogout();
                JSYApplication.getInstance().finishActivities();

                break;
            //快速签到
            case 1005:
                Intent i = new Intent(mContext, QuickSignInActivity.class);
                MobclickAgent
                        .onEvent(
                                mContext,
                                getResources()
                                        .getString(
                                                R.string.MessageCenterActivity_quickSignIn));
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    private void setAlias() {
        sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
        String account=sp.getString("JiaoBaoHao", "");
        PushAgent mPushAgent = PushAgent.getInstance(getApplication());
        mPushAgent.setAlias(account, AliasType.JINSHIYE,
                new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {
                        Log.d(TAG, "添加alias::::" + isSuccess + message);
                    }
                });
    }

    private void delAlias() {
        sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
        PushAgent mPushAgent = PushAgent.getInstance(getApplication());
        mPushAgent.deleteAlias(sp.getString("JiaoBaoHao", ""), AliasType.JINSHIYE, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean isSuccess, String message) {
                Log.d(TAG, "刪除alias " + isSuccess + ":" + message);
            }

        });
    }

    /**
     * 系统返回键的点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Activity生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "*****onDestroy******");
        ACache artCache = ACache.get(mContext, "noticefragmentarthlist");
        artCache.clear();
    }
}
