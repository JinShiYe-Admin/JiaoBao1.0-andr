package com.jsy_jiaobao.main.affairs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.filechoser.grivider.FileChooseActivity;
import com.filechoser.grivider.FilePerate;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.appcenter.sign.ShowPopup;
import com.jsy_jiaobao.main.system.VisitPublicHttp;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnit;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitList;
import com.jsy_jiaobao.po.personal.GetUnitClassRevicer;
import com.jsy_jiaobao.po.personal.GetUnitRevicer;
import com.jsy_jiaobao.po.personal.MyUnit;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布事务活动
 *
 * @author MSL
 */
public class WorkSendToSbActivity extends BaseActivity implements PublicMethod, OnClickListener {
    private static String TAG = WorkSendToSbActivity.class.getName();
    private ScrollView layout_ui;
    private HorizontalScrollView layout_lowermyclass;//执教班级
    private LinearLayout layout_reciver;
    private LinearLayout layout_lowerunit;//
    private LinearLayout layout_lowerclass;//执教班级
    private Button btn_currunit;//当前单位
    private LinearLayout layout_higherunit;//上级单位
    private IEditText edt_content;//内容框
    private CheckBox cb_shotmsg;//是否是短信
    private Button btn_send;//发布
    private TextView tv_lowerunit;//下级单位or所有班级
    private TextView tv_lowerclass;//执教班级
    private TextView tv_curr;//当前单位名
    private LinearLayout layout_file;//
    private LinearLayout layout_photofile;//
    private Context mContext;
    private CommMsgRevicerUnitList commMsgRevicerUnitList;
    private List<CusGridView> grids = new ArrayList<>();
    private String str_msgcontent;
    private List<NameValuePair> reciverList = new ArrayList<>();
    private String curunitid;//当前所在单位加密id
    private List<View> viewNames = new ArrayList<>();
    private String cache_commMsgRevicerUnitList;
    private String cache_UnitRevicer;
    public static String filePath = null;//文件路径，用于接收文件选择器返回的路径

    @SuppressWarnings("unchecked")
    OnClickListener deleteListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
            LinearLayout layout = (LinearLayout) tag.get(0);
            String path = (String) tag.get(1);
            layout.removeAllViews();
            for (int i = 0; i < selectFileList.size(); i++) {
                if (path.equals(selectFileList.get(i))) {
                    selectFileList.remove(i);
                }
            }
        }
    };
    ArrayList<String> selectFileList = new ArrayList<>();
    public static String clickPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initViews();
        initDeatilsData();
        initListener();
    }

    @Override
    public void initPassData() {

    }

    @Override
    public void initViews() {
        setContentLayout(R.layout.fragment_msgcenter_sendmessage);
        ViewUtils.inject(this);
        mContext = this;
        layout_ui = (ScrollView) findViewById(R.id.ui_sendmessage);
        LinearLayout layout_content;
        layout_content = (LinearLayout) findViewById(R.id.sendmessage_layout_content);
        layout_lowermyclass = (HorizontalScrollView) findViewById(R.id.sendmessage_layout_lower_class);
        layout_reciver = (LinearLayout) findViewById(R.id.sendmessage_layout_reciverlist);
        layout_lowerunit = (LinearLayout) findViewById(R.id.sendmessage_layout_lowerunit);
        layout_lowerclass = (LinearLayout) findViewById(R.id.sendmessage_layout_lowerclass);
        btn_currunit = (Button) findViewById(R.id.sendmessage_btn_currunit);
        layout_higherunit = (LinearLayout) findViewById(R.id.sendmessage_layout_higherunit);
        edt_content = (IEditText) findViewById(R.id.sendmessage_edt_content);
        cb_shotmsg = (CheckBox) findViewById(R.id.sendmessage_checkBox_shotmsg);
        Button btn_toall;//全选
        btn_toall = (Button) findViewById(R.id.sendmessage_btn_toall);
        Button btn_invert;//反选
        btn_invert = (Button) findViewById(R.id.sendmessage_btn_invert);
        btn_send = (Button) findViewById(R.id.sendmessage_btn_send);
        tv_lowerunit = (TextView) findViewById(R.id.sendmessage_tv_lowerunit);
        tv_lowerclass = (TextView) findViewById(R.id.sendmessage_tv_lowerclass);
        tv_curr = (TextView) findViewById(R.id.sendmessage_tv_curr);
        TextView img_file;//当前单位名
        img_file = (TextView) findViewById(R.id.sendmessage_img_file);
        layout_file = (LinearLayout) findViewById(R.id.sendmessage_layout_file);
        TextView img_photo;//当前单位名
        img_photo = (TextView) findViewById(R.id.sendmessage_img_photo);
        layout_photofile = (LinearLayout) findViewById(R.id.sendmessage_layout_photofile);
        WorkSendMessageActivityController.getInstance().setContext(this);
        VisitPublicHttp.getInstance().setContext(this);
        cache_commMsgRevicerUnitList = TAG + sp.getString("JiaoBaoHao", "") + sp.getInt("UnitID", 0) + sp.getInt("RoleIdentity", 1) + "CommMsgRevicerUnitList";
        cache_UnitRevicer = TAG + sp.getString("JiaoBaoHao", "") + sp.getInt("RoleIdentity", 1) + "UnitRevicer";
        setActionBarTitle(R.string.send_message);
        layout_content.setFocusable(true);
        layout_content.setFocusableInTouchMode(true);
        layout_content.requestFocus();
        btn_currunit.setText(sp.getString("UnitName", getResources().getString(R.string.noUnit_pleaseChangeUnit)));
        cb_shotmsg.setChecked(true);
        VisitPublicHttp.getInstance().changeCurUnit(true);
        btn_toall.setOnClickListener(this);
        btn_invert.setOnClickListener(this);
        btn_currunit.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        img_photo.setOnClickListener(this);
        img_file.setOnClickListener(this);
    }

    @Override
    public void initDeatilsData() {

    }

    @Override
    public void initListener() {
    }

    /**
     * 切换单位
     */
    private void choseUnit() {
        try {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            ShowPopup showPopup = new ShowPopup(mContext);
            showPopup.showPop(layout_ui, statusBarHeight + getSupportActionBar().getHeight() + 2, Constant.listUserIdentity, mHandler);
        } catch (Exception e) {
            e.printStackTrace();
            VisitPublicHttp.getInstance().setContext(this);
            VisitPublicHttp.getInstance().getRoleIdentity();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    String title = (String) msg.obj;
                    btn_currunit.setText(title);
                    break;
                default:
                    break;
            }
        }
    };
    String photoPath;

    @SuppressWarnings("unchecked")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendmessage_btn_toall:
                for (int i = 0; i < viewNames.size(); i++) {
                    View view = viewNames.get(i);
                    if (view.getVisibility() == View.VISIBLE) {
                        ArrayList<Object> tag = (ArrayList<Object>) view.getTag();
                        ArrayList<Object> adapterList = (ArrayList<Object>) tag.get(1);
                        if (adapterList != null) {
                            for (int j = 0; j < adapterList.size(); j++) {
                                GridViewAdapter adapter = (GridViewAdapter) adapterList.get(j);
                                adapter.setCheckAll();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case R.id.sendmessage_btn_invert:
                for (int i = 0; i < viewNames.size(); i++) {
                    View view = viewNames.get(i);
                    if (view.getVisibility() == View.VISIBLE) {
                        ArrayList<Object> tag = (ArrayList<Object>) view.getTag();
                        ArrayList<Object> adapterList = (ArrayList<Object>) tag.get(1);
                        if (adapterList != null) {
                            for (int j = 0; j < adapterList.size(); j++) {
                                GridViewAdapter adapter = (GridViewAdapter) adapterList.get(j);
                                adapter.setInverse();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
            case R.id.sendmessage_btn_currunit:
                if (myUnitRevicer != null) {
                    getReciverList(String.valueOf(myUnitRevicer.getTabID()), String.valueOf(myUnitRevicer.getFlag()), myUnitRevicer.getUintName(), 1);
                } else {
                    noRecivers();
                }
                break;
            case R.id.sendmessage_btn_send:
                str_msgcontent = edt_content.getTextString();
                if ("".equals(str_msgcontent)) {
                    ToastUtil.showMessage(mContext, R.string.content_cannot_empty);
                } else {
                    reciverList.clear();
                    for (int i = 0; i < grids.size(); i++) {
                        GridViewAdapter adapter = (GridViewAdapter) grids.get(i).getAdapter();
                        List<Selit> list = adapter.getCheckedSelit();
                        String slit = adapter.getPostTag();
                        for (int j = 0; j < list.size(); j++) {
                            BasicNameValuePair item = new BasicNameValuePair(slit, list.get(j).getSelit());
                            reciverList.add(item);
                        }
                    }
                    if (reciverList == null || reciverList.size() == 0) {
                        ToastUtil.showMessage(mContext, R.string.please_choose_receiver);
                    } else {
                        dialog_send(reciverList.size());
                    }
                }
                break;
            case R.id.sendmessage_img_photo:

                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用android自带的照相机
                    photoPath = JSYApplication.getInstance().FILE_PATH + System.currentTimeMillis() + ".png";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
                    startActivityForResult(intent, 11);
                } catch (Exception e) {
                    ToastUtil.showMessage(mContext, R.string.open_camera_abnormal);
                }
                break;
            case R.id.sendmessage_img_file:
                clickPath = "";
                //如果没有SD卡，则输出提示
                if (FilePerate.getRootFolder() == null) {
                    Toast.makeText(mContext, R.string.noSDCard, Toast.LENGTH_SHORT).show();
                    return;
                }
                //创建一个自定义的对话框
                Intent intent1 = new Intent(mContext, FileChooseActivity.class);
                startActivityForResult(intent1, 10);
                break;
            default:
                break;
        }
    }

    /**
     * 没有权限时 提示
     */
    @SuppressWarnings("unchecked")
    private void noRecivers() {
        btn_send.setEnabled(false);
        boolean haveView = false;
        for (int i = 0; i < viewNames.size(); i++) {
            ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
            String t = (String) tag.get(0);
            if (t.equals("noRecivers")) {
                haveView = true;
                viewNames.get(i).setVisibility(View.VISIBLE);
            } else {
                viewNames.get(i).setVisibility(View.GONE);
            }
        }
        if (!haveView) {
            LinearLayout layout = new LinearLayout(mContext);
            ArrayList<Object> layoutTag = new ArrayList<>();
            layoutTag.add(0, "noRecivers");
            layout.setTag(layoutTag);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setBackgroundResource(R.drawable.edittext_bg);
            TextView tag = new TextView(mContext);
            tag.setText(R.string.no_receiver);
            tag.setPadding(10, 10, 0, 0);
            layout.addView(tag);
            layout_reciver.addView(layout);
            viewNames.add(layout);
        }
    }

    /**
     * 提示框
     *
     * @param count c
     */
    protected void dialog_send(int count) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setMessage("确定要向" + count + "人发送信息吗?");
        builder.setTitle(R.string.hint);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sendMessage();
            }

        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 发送信息
     */
    private void sendMessage() {
        RequestParams params = new RequestParams();
        params.addBodyParameter(reciverList);
//		if (classRevicerList != null && classRevicerList.size()>0) {
//			params.addBodyParameter("unitclassgenCount",String.valueOf(classRevicerList.get(0).getStudentgens_genselit().size()));
//		}else{
        params.addBodyParameter("unitclassgenCount", "0");
//		}
        params.addBodyParameter("grsms", false + "");
        params.addBodyParameter("talkcontent", str_msgcontent);
        if (cb_shotmsg.isChecked()) {
            params.addBodyParameter("SMSFlag", "1");
        } else {
            params.addBodyParameter("SMSFlag", "0");
        }
        params.addBodyParameter("curunitid", curunitid);

        for (int i = 0; i < selectFileList.size(); i++) {
            params.addBodyParameter("ATTfileList" + i, new File(selectFileList.get(i)));
        }
        WorkSendMessageActivityController.getInstance().CreateCommMsg(params);
    }

    @SuppressWarnings("unchecked")
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_work_change:

                cache_commMsgRevicerUnitList = TAG + sp.getString("JiaoBaoHao", "") + sp.getInt("UnitID", 0) + sp.getInt("RoleIdentity", 1) + "CommMsgRevicerUnitList";
                cache_UnitRevicer = TAG + sp.getString("JiaoBaoHao", "") + sp.getInt("RoleIdentity", 1) + "UnitRevicer";
                boolean changeUnit = (Boolean) list.get(1);
                if (changeUnit) {
                    WorkSendMessageActivityController.getInstance().CommMsgRevicerUnitList();
                }
                break;
            case Constant.msgcenter_work_CommMsgRevicerUnitList:
                grids.clear();
                viewNames.clear();
                String commMsgRevicerUnitList_str = (String) list.get(1);
                commMsgRevicerUnitList = GsonUtil.GsonToObject(commMsgRevicerUnitList_str, CommMsgRevicerUnitList.class);

                mCache.put(cache_commMsgRevicerUnitList, commMsgRevicerUnitList_str);
                initRevicerList();
                break;
            case Constant.msgcenter_work_GetUnitRevicer:
                ArrayList<Object> reviceTag = (ArrayList<Object>) list.get(2);
                String unitName = (String) reviceTag.get(0);
                mCache.put(cache_UnitRevicer + unitName, (String) list.get(1));
                int RoleIdentity = (Integer) reviceTag.get(1);
                if (RoleIdentity == 1) {
                    GetUnitRevicer getUnitRevicer = GsonUtil.GsonToObject((String) list.get(1), GetUnitRevicer.class);
                    creatReciverView(getUnitRevicer, unitName);
                } else {
                    GetUnitClassRevicer getUnitRevicer = GsonUtil.GsonToObject((String) list.get(1), GetUnitClassRevicer.class);
                    creatClassReciverView(getUnitRevicer, RoleIdentity, unitName);
                }
                break;
            case Constant.msgcenter_work_CreateCommMsg:
                edt_content.setText("");
                for (int i = 0; i < grids.size(); i++) {
                    GridViewAdapter adapter = (GridViewAdapter) grids.get(i).getAdapter();
                    adapter.setCheckAll();
                    adapter.setInverse();
                    adapter.notifyDataSetChanged();
                }
                try {
                    selectFileList.clear();
                    layout_file.removeAllViews();
                    layout_photofile.removeAllViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ToastUtil.showMessage(mContext, R.string.send_success);
                break;
            case Constant.msgcenter_work_geterror:
                noRecivers();
                break;
            default:
                break;
        }
    }

    /**
     * 处理接收人
     */
    private MyUnit myUnitRevicer;
    private ArrayList<CommMsgRevicerUnitClass> classRevicerList;

    private void initRevicerList() {
        ArrayList<CommMsgRevicerUnit> parentUnitRevicer = commMsgRevicerUnitList.getUnitParents();//上级单位接收人
        myUnitRevicer = commMsgRevicerUnitList.getMyUnit();//本单位接收人
        ArrayList<CommMsgRevicerUnit> subUnitRevicer = commMsgRevicerUnitList.getSubUnits();//下级单位接收人
        classRevicerList = commMsgRevicerUnitList.getUnitClass();//班级接收人（家长或老师）--执教班级

        creatParentName(parentUnitRevicer, 1);//生成上级单位名称
        btn_send.setEnabled(true);
        layout_reciver.removeAllViews();
        if (myUnitRevicer != null) {
            curunitid = myUnitRevicer.getTabIDStr();
            btn_currunit.setText(myUnitRevicer.getUintName());
            getReciverList(String.valueOf(myUnitRevicer.getTabID()), String.valueOf(myUnitRevicer.getFlag()), myUnitRevicer.getUintName(), 1);
        }
        layout_lowermyclass.setVisibility(View.GONE);
        switch (sp.getInt("RoleIdentity", 1)) {
            case 1:
                creatSubName(subUnitRevicer, 1);
                break;
            case 2:
                layout_lowermyclass.setVisibility(View.VISIBLE);
                creatSubName(subUnitRevicer, 1);
                creatTeaClassName(classRevicerList, 2);
                break;
            case 3:
                creatSubClassName(classRevicerList, 3);

                break;
            case 4:
                creatSubClassName(classRevicerList, 4);
                break;
            case 5:

                break;

            default:
                break;
        }
    }

    /**
     * 获取接收人
     */
    private void getReciverList(String tabid, String flag, String unitname, int RoleIdentity) {
        tv_curr.setText(unitname);
        String data = mCache.getAsString(cache_UnitRevicer + unitname);

        if (RoleIdentity == 1) {
            GetUnitRevicer UserList = GsonUtil.GsonToObject(data, GetUnitRevicer.class);
            if (null != UserList) {
                creatReciverView(UserList, unitname);
            } else {
                ArrayList<Object> tag = new ArrayList<>();
                tag.add(0, unitname);
                tag.add(1, RoleIdentity);
                RequestParams params = new RequestParams();
                params.addBodyParameter("unitId", tabid);
                params.addBodyParameter("flag", flag);
                WorkSendMessageActivityController.getInstance().GetUnitRevicer(params, tag);
            }

        } else {
            GetUnitClassRevicer getUnitRevicer = GsonUtil.GsonToObject(data, GetUnitClassRevicer.class);
            if (null != getUnitRevicer) {
                creatClassReciverView(getUnitRevicer, RoleIdentity, unitname);
            } else {
                ArrayList<Object> tag = new ArrayList<>();
                tag.add(0, unitname);
                tag.add(1, RoleIdentity);
                RequestParams params = new RequestParams();
                params.addBodyParameter("unitclassId", tabid);
                params.addBodyParameter("flag", flag);
                WorkSendMessageActivityController.getInstance().GetUnitRevicer(params, tag);
            }
        }
    }

    /**
     * 生成上级单位名称
     */
    private void creatParentName(ArrayList<CommMsgRevicerUnit> parentUnitRevicer, int selit) {
        layout_higherunit.removeAllViews();
        if (parentUnitRevicer != null && parentUnitRevicer.size() != 0) {
            for (int i = 0; i < parentUnitRevicer.size(); i++) {
                String name = parentUnitRevicer.get(i).getUintName();
                Button btn = new Button(mContext);
                btn.setBackground(null);
                btn.setText(name);
                ArrayList<Object> tag = new ArrayList<>();
                tag.add(0, parentUnitRevicer.get(i));
                tag.add(1, selit);
                btn.setTag(tag);
                btn.setOnClickListener(unitClickListener);
                layout_higherunit.addView(btn);
            }
        } else {
            Button no = new Button(mContext);
            no.setBackground(null);
            no.setText("无");
            layout_higherunit.addView(no);
        }
    }

    /**
     * 生成下级单位名称
     */
    Button btn;

    private void creatSubName(ArrayList<CommMsgRevicerUnit> subUnitRevicer, int selit) {
        tv_lowerunit.setText("下级单位:");
        layout_lowerunit.removeAllViews();
        if (subUnitRevicer != null && subUnitRevicer.size() > 0) {
            String name = subUnitRevicer.get(0).getUintName();
            btn = new Button(mContext);
            btn.setBackgroundResource(com.actionbarsherlock.R.drawable.abs__spinner_ab_disabled_holo_light);
            btn.setText(name);
            ArrayList<Object> tag = new ArrayList<>();
            tag.add(0, subUnitRevicer);
            tag.add(1, selit);
            tag.add(2, btn.getText());
            btn.setTag(tag);
            btn.setOnClickListener(juniorClickListener);
            layout_lowerunit.addView(btn);
        } else {
            Button no = new Button(mContext);
            no.setBackground(null);
            no.setText("无");
            layout_lowerunit.addView(no);
        }

    }

    /**
     * 生成执教班级名
     *
     * @param RoleIdentity 当前身份
     */
    private void creatTeaClassName(ArrayList<CommMsgRevicerUnitClass> classRevicerList2, int RoleIdentity) {
        tv_lowerclass.setText(R.string.teach_class_);
        layout_lowerclass.removeAllViews();
        if (classRevicerList2 != null && classRevicerList2.size() > 0) {
            String name = classRevicerList2.get(0).getClsName();
            btn = new Button(mContext);
            btn.setText(name);
            btn.setBackgroundResource(com.actionbarsherlock.R.drawable.abs__spinner_ab_disabled_holo_light);
            ArrayList<Object> tag = new ArrayList<>();
            tag.add(0, classRevicerList2);
            tag.add(1, RoleIdentity);
            tag.add(2, btn.getText());
            btn.setTag(tag);
            btn.setOnClickListener(juniorClickListener);
            layout_lowerclass.addView(btn);
//			for (int i = 0; i < classRevicerList2.size(); i++) {
//				String name = classRevicerList2.get(i).getClsName();
//				Button btn = new Button(mContext);
//				btn.setText(name);
//				ArrayList<Object> tag = new ArrayList<Object>();
//				tag.add(0,classRevicerList2.get(i));
//				tag.add(1,RoleIdentity);
//				btn.setTag(tag);
//				btn.setOnClickListener(juniorClickListener);
//				layout_lowerclass.addView(btn);
//			}
        } else {
            String name = "无";
            Button btn = new Button(mContext);
            btn.setBackground(null);
            btn.setText(name);
            layout_lowerclass.addView(btn);
        }
    }

    /**
     * 生成班级名
     *
     * @param RoleIdentity 当前身份
     */
    private void creatSubClassName(ArrayList<CommMsgRevicerUnitClass> classRevicerList2, int RoleIdentity) {
        tv_lowerunit.setText(R.string.all_class_);
        layout_lowerunit.removeAllViews();
        if (classRevicerList2 != null) {
            String name = classRevicerList2.get(0).getClsName();
            btn = new Button(mContext);
            btn.setBackgroundResource(com.actionbarsherlock.R.drawable.abs__spinner_ab_disabled_holo_light);
            btn.setText(name);
            ArrayList<Object> tag = new ArrayList<>();
            tag.add(0, classRevicerList2);
            tag.add(1, RoleIdentity);
            tag.add(2, btn.getText());
            btn.setTag(tag);
            btn.setOnClickListener(juniorClickListener);
            layout_lowerunit.addView(btn);
        } else {
            String name = getResources().getString(R.string.nothing);
            Button btn = new Button(mContext);
            btn.setBackground(null);
            btn.setText(name);
            layout_lowerunit.addView(btn);
        }
    }

    @SuppressWarnings("unchecked")
    OnClickListener juniorClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
            Intent intent = new Intent(mContext, WorkJuniorUnitListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("tag", tag);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case 0:
                    if (data != null) {
                        ArrayList<Object> tag = (ArrayList<Object>) data.getExtras().getSerializable("tag");
                        int selit = (Integer) tag.get(1);
                        try {
                            CommMsgRevicerUnit RevicerUnit = (CommMsgRevicerUnit) tag.get(0);
                            if (null != RevicerUnit) {
                                btn.setText(RevicerUnit.getUintName());
                                getReciverList(String.valueOf(RevicerUnit.getTabID()), String.valueOf(RevicerUnit.getFlag()), RevicerUnit.getUintName(), selit);
                            }
                        } catch (Exception e) {
                            CommMsgRevicerUnitClass UnitClass = (CommMsgRevicerUnitClass) tag.get(0);
                            if (null != UnitClass) {
                                btn.setText(UnitClass.getClsName());
                                getReciverList(String.valueOf(UnitClass.getTabID()), String.valueOf(UnitClass.getFlag()), UnitClass.getClsName(), selit);
                            }
                        }
                        ArrayList<Object> btntag = (ArrayList<Object>) btn.getTag();
                        btntag.set(2, btn.getText());
                        btn.setTag(btntag);
                    }
                    break;
                case 10:
                    if (data != null) {

                        String path = data.getExtras().getString("path");
                        int size = 0;
                        for (int i = 0; i < selectFileList.size(); i++) {
                            File file = new File(selectFileList.get(i));
                            size += file.length();
                        }
                        if (path == null) {
                            break;
                        }
                        File file = new File(path);
                        size += file.length();
                        if (size > 10 * 1024 * 1024) {
                            ToastUtil.showMessage(mContext, R.string.noMoreThan_10M_content);
                        } else {
                            LinearLayout layout = new LinearLayout(mContext);
                            ImageView delete = new ImageView(mContext);
                            delete.setImageResource(R.drawable.btn_sendmsg_delete);
                            TextView item = new TextView(mContext);
                            String[] paths = path.split("\\/");
                            String[] names = paths[paths.length - 1].split("\\.");
                            if (names.length == 2) {
                                if (names[1].equals("jpg") || names[1].equals("jpeg") || names[1].equals("png") || names[1].equals("bmp")) {
                                    item.setLayoutParams(new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.px_to_dip_100), (int) getResources().getDimension(R.dimen.px_to_dip_100)));
                                    JSYApplication.getInstance().bitmapUtils.display(item, path);
                                } else {
                                    item.setText(paths[paths.length - 1]);
                                }
                            } else {
                                item.setText(paths[paths.length - 1]);
                            }
                            item.setTag(path);
                            ArrayList<Object> tag = new ArrayList<>();
                            tag.add(0, layout);
                            tag.add(1, path);
                            delete.setTag(tag);
                            item.setTag(tag);
                            layout.addView(delete);
                            layout.addView(item);
                            layout_file.addView(layout);
                            selectFileList.add(path);

                            delete.setOnClickListener(deleteListener);
                            item.setOnClickListener(deleteListener);
                        }
                    }
                    break;
                case 11:
                    int size = 0;
                    for (int i = 0; i < selectFileList.size(); i++) {
                        File file = new File(selectFileList.get(i));
                        size += file.length();
                    }
                    File file = new File(photoPath);
                    if (file.length() > 0) {
                        size += file.length();
                        if (size > 10 * 1024 * 1024) {
                            ToastUtil.showMessage(mContext, R.string.noMoreThan_10M_content);
                        } else {
                            LinearLayout layout = new LinearLayout(mContext);
                            ImageView delete = new ImageView(mContext);
                            delete.setImageResource(R.drawable.btn_sendmsg_delete);
                            ImageView item = new ImageView(mContext);
                            item.setLayoutParams(new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.px_to_dip_100), (int) getResources().getDimension(R.dimen.px_to_dip_100)));
                            JSYApplication.getInstance().bitmap.display(item, photoPath);
                            item.setTag(photoPath);
                            ArrayList<Object> tag = new ArrayList<>();
                            tag.add(0, layout);
                            tag.add(1, photoPath);
                            delete.setTag(tag);
                            item.setTag(tag);
                            layout.addView(delete);
                            layout.addView(item);
                            layout_photofile.addView(layout);
                            selectFileList.add(photoPath);

                            delete.setOnClickListener(deleteListener);
                            item.setOnClickListener(deleteListener);
                        }
                    }
                    break;
                default:
                    break;
            }
        }


    }

    @SuppressWarnings("unchecked")
    OnClickListener unitClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
            int selit = (Integer) tag.get(1);
            try {
                CommMsgRevicerUnit RevicerUnit = (CommMsgRevicerUnit) tag.get(0);
                getReciverList(String.valueOf(RevicerUnit.getTabID()), String.valueOf(RevicerUnit.getFlag()), RevicerUnit.getUintName(), selit);
            } catch (Exception e) {
                CommMsgRevicerUnitClass UnitClass = (CommMsgRevicerUnitClass) tag.get(0);
                getReciverList(String.valueOf(UnitClass.getTabID()), String.valueOf(UnitClass.getFlag()), UnitClass.getClsName(), selit);
            }
        }
    };

    @SuppressLint("InflateParams")
    @SuppressWarnings("unchecked")
    private void createClassReciverView(List<Selit> selit, String str_title, String str_selit, LinearLayout layout) {
        if (null != selit && selit.size() > 0) {
            ArrayList<Object> tag = (ArrayList<Object>) layout.getTag();
            ArrayList<Object> adapterList = (ArrayList<Object>) tag.get(1);
            View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
            TextView title = (TextView) view.findViewById(R.id.sendmessage_group_name);
            Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
            Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);

            title.setText(str_title);
            CusGridView gridview = new CusGridView(mContext, null);
            gridview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            gridview.setNumColumns(2);
            GridViewAdapter adapter = new GridViewAdapter(mContext);
            adapter.setReciver(selit);
            adapter.setPostTag(str_selit);
            gridview.setAdapter(adapter);
            toall.setTag(adapter);
            invert.setTag(adapter);
            toall.setOnClickListener(toallClickListener);
            invert.setOnClickListener(invertClickListener);
            layout.addView(view);
            layout.addView(gridview);
            grids.add(gridview);
            adapterList.add(adapter);
            layout.setTag(tag);
        }
    }

    /**
     * 生成接收人列表
     *
     * @param revicer      r
     * @param RoleIdentity r
     * @param unitname     u
     */
    @SuppressWarnings("unchecked")
    private void creatClassReciverView(GetUnitClassRevicer revicer, int RoleIdentity, String unitname) {
        btn_send.setEnabled(true);
        boolean haveView = false;
        for (int i = 0; i < viewNames.size(); i++) {
            ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
            if (unitname.equals(tag.get(0))) {
                haveView = true;
                viewNames.get(i).setVisibility(View.VISIBLE);
            } else {
                viewNames.get(i).setVisibility(View.GONE);
            }
        }
        if (!haveView) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setBackgroundResource(R.drawable.edittext_bg);
            layout.setOrientation(LinearLayout.VERTICAL);
            ArrayList<Object> tag = new ArrayList<>();
            tag.add(0, unitname);
            ArrayList<Object> adapterList = new ArrayList<>();
            tag.add(1, adapterList);
            layout.setTag(tag);
            switch (RoleIdentity) {
                case 2://老师
                    List<Selit> selit2 = revicer.getSelit();
                    List<Selit> genselit2 = revicer.getGenselit();
                    List<Selit> stuselit2 = revicer.getStuselit();
                    if (null == selit2) {
                        selit2 = new ArrayList<>();
                    }
                    if (null == genselit2) {
                        genselit2 = new ArrayList<>();
                    }
                    if (null == stuselit2) {
                        stuselit2 = new ArrayList<>();
                    }
                    ArrayList<Selit> list = new ArrayList<>();
                    list.addAll(genselit2);
                    list.addAll(selit2);
                    list.addAll(stuselit2);
                    if (list.size() > 0) {
                        createClassReciverView(selit2, getResources().getString(R.string.thisClass_teacher), "selit", layout);
                        createClassReciverView(genselit2, getResources().getString(R.string.thisClass_parent), "genselit", layout);
                        createClassReciverView(stuselit2, getResources().getString(R.string.thisClass_student), "stuselit", layout);
                    } else {
                        noRecivers();
                    }
                    break;
                case 3://家长
                    List<Selit> genselit3 = revicer.getGenselit();
                    List<Selit> selit3 = revicer.getSelit();
                    ArrayList<Selit> list1 = new ArrayList<>();
                    list1.addAll(genselit3);
                    list1.addAll(selit3);
                    if (list1.size() > 0) {
                        createClassReciverView(genselit3, getResources().getString(R.string.thisClass_parent), "genselit", layout);
                        createClassReciverView(selit3, getResources().getString(R.string.thisClass_teacher), "selit", layout);
                    } else {
                        noRecivers();
                    }
                    break;
                case 4:
                    List<Selit> stuselit4 = revicer.getStuselit();
                    List<Selit> selit4 = revicer.getSelit();
                    if (null == stuselit4) {
                        stuselit4 = new ArrayList<>();
                    }
                    if (null == selit4) {
                        selit4 = new ArrayList<>();
                    }
                    ArrayList<Selit> list4 = new ArrayList<>();
                    list4.addAll(stuselit4);
                    list4.addAll(selit4);
                    if (list4.size() > 0) {
                        createClassReciverView(selit4, getResources().getString(R.string.thisClass_teacher), "selit", layout);
                        createClassReciverView(stuselit4, getResources().getString(R.string.thisClass_student), "stuselit", layout);
                    } else {
                        noRecivers();
                    }
                    break;

                default:
                    break;
            }
            layout_reciver.addView(layout);
            viewNames.add(layout);
        }

    }

    /**
     * 生成接收人列表
     *
     * @param revicer r
     * @param name    n
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("unchecked")
    private void creatReciverView(GetUnitRevicer revicer, String name) {
        btn_send.setEnabled(true);
//		layout_reciver.removeAllViews();
        boolean haveView = false;
        for (int i = 0; i < viewNames.size(); i++) {
            ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
            if (name.equals(tag.get(0))) {
                haveView = true;
                viewNames.get(i).setVisibility(View.VISIBLE);
            } else {
                viewNames.get(i).setVisibility(View.GONE);
            }
        }
        if (!haveView) {
            List<GroupUserList> UserList = revicer.getSelit();
            if (null != UserList && UserList.size() > 0) {
                LinearLayout layout = new LinearLayout(mContext);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setBackgroundResource(R.drawable.edittext_bg);
                layout.setOrientation(LinearLayout.VERTICAL);
                ArrayList<Object> adapterList = new ArrayList<>();
                for (int i = 0; i < UserList.size(); i++) {
//					boolean have = false;
                    View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
                    TextView title = (TextView) view.findViewById(R.id.sendmessage_group_name);
                    Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
                    Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);

                    List<Selit> selitList = UserList.get(i).getGroupselit_selit();
                    title.setText(UserList.get(i).getGroupName());
                    CusGridView gridview = new CusGridView(mContext, null);
                    gridview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    gridview.setNumColumns(2);
                    GridViewAdapter adapter = new GridViewAdapter(mContext);
                    adapter.setReciver(selitList);
                    adapter.setPostTag("selit");
                    gridview.setAdapter(adapter);
                    toall.setTag(adapter);
                    invert.setTag(adapter);
                    toall.setOnClickListener(toallClickListener);
                    invert.setOnClickListener(invertClickListener);
                    layout.addView(view);
                    layout.addView(gridview);
                    grids.add(gridview);
                    adapterList.add(adapter);
                }
                ArrayList<Object> tag = new ArrayList<>();
                tag.add(0, name);
                tag.add(1, adapterList);
                layout.setTag(tag);
                layout_reciver.addView(layout);
                viewNames.add(layout);
            } else {
                noRecivers();
            }

        }
    }

    OnClickListener invertClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            GridViewAdapter tag = (GridViewAdapter) v.getTag();
            tag.setInverse();
            tag.notifyDataSetChanged();
        }
    };
    OnClickListener toallClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            GridViewAdapter tag = (GridViewAdapter) v.getTag();
            tag.setCheckAll();
            tag.notifyDataSetChanged();
        }
    };

    @Override
    public void onResume() {
        EventBusUtil.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu sub_search = menu.addSubMenu(R.string.change_unit).setIcon(R.drawable.btn_right_swich);
        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                choseUnit();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return false;
    }
}
