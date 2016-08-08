package com.jsy_jiaobao.main.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.PublishPermission;
import com.jsy_jiaobao.po.personal.UpFiles;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//一些修改1 2016-5-31 MSL
//1.使用图片前判断字数是否达到3000，是：不允许再上传图片
//2.上传图片后判断总字数是否超过3000，是：去掉该图片

/**
 * 发布动态或分享
 *
 * @author an
 */
public class PublishArticaleActivity extends BaseActivity implements
        OnClickListener {


    private IEditText edt_title;// 标题输入框
    private IEditText edt_content;// 内容输入框
    private Spinner sp_chose;// 发布位置的下拉菜单
    private TextView tv_pub;
    private TextView tv_type;


//	Handler mHandler = new Handler();
    /**
     * 录音end
     */
    private Bitmap bitmap;
    private Context mContext;
    private String UnitType;
    private String UnitID;

    private String SectionFlag = "1";
    ArrayList<Map<String, String>> spdata = new ArrayList<>();
    private ArrayList<PublishPermission> permissionlist;// 可发布单位列表
    private ArrayList<UserClass> userclasslist;// 关联班级列表

    // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
    // private UploadManager uploadManager = new UploadManager();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            photoUri = savedInstanceState.getParcelable("photoUri");
            SectionFlag = savedInstanceState.getString("SectionFlag");
            if ("2".equals(SectionFlag)) {
                permissionlist = (ArrayList<PublishPermission>) savedInstanceState
                        .getSerializable("UnitList");
            } else if ("1".equals(SectionFlag)) {
                userclasslist = (ArrayList<UserClass>) savedInstanceState
                        .getSerializable("UnitList");
            }
        } else {
            // 获取传递过来的信息
            initPassData();
        }
        // 初始化界面
        initViews();
        // 加入监听事件
        initListener();
    }

    /**
     * 获取Intent携带的信息
     */
    @SuppressWarnings("unchecked")
    public void initPassData() {
        Intent getPass = getIntent();
        if (getPass != null) {
            Bundle bundle = getPass.getExtras();
            if (bundle != null) {
                SectionFlag = bundle.getString("SectionFlag");
                if ("2".equals(SectionFlag)) {
                    permissionlist = (ArrayList<PublishPermission>) bundle
                            .getSerializable("UnitList");
                } else if ("1".equals(SectionFlag)) {
                    userclasslist = (ArrayList<UserClass>) bundle
                            .getSerializable("UnitList");
                }
            }
        }
    }

    /**
     * 保存意外销毁的的数据
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SectionFlag", SectionFlag);
        if ("2".equals(SectionFlag)) {
            outState.putSerializable("UnitList", permissionlist);
        } else if ("1".equals(SectionFlag)) {
            outState.putSerializable("UnitList", userclasslist);
        }
        outState.putParcelable("photoUri", photoUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            photoUri = savedInstanceState.getParcelable("photoUri");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void initViews() {
        setContentLayout(R.layout.ui_publisharticle);

        findViews();
        mContext = this;
        // Controller注册上下文
        PublishArticleActivityController.getInstance().setContext(this);
        if ("1".equals(SectionFlag)) {
            // 设置ActionBar的title
            setActionBarTitle(R.string.release_activity_share);
            tv_pub.setText(R.string.release_to);
            tv_type.setText(R.string.share_space);
            if (userclasslist != null) {
                // 如果用户班级列表不为空

                for (int j = 0; j < userclasslist.size(); j++) {
                    UserClass unit = userclasslist.get(j);
                    Map<String, String> map = new HashMap<>();
                    map.put("ID", String.valueOf(unit.getClassID()));
                    map.put("Type", "3");
                    map.put("Name",
                            getResources().getString(
                                    R.string.my_relevance_class)
                                    + "-" + unit.getClassName());
                    spdata.add(map);
                }
            }
            if (Constant.listUserIdentity != null
                    && Constant.listUserIdentity.size() > 0) {
                for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
                    UserIdentity userIdentity = Constant.listUserIdentity
                            .get(i);
                    if (userIdentity.getRoleIdentity() == 1) {
                        List<UserUnit> UserUnits = userIdentity.getUserUnits();
                        if (UserUnits != null) {
                            for (int j = 0; j < UserUnits.size(); j++) {
                                UserUnit unit = UserUnits.get(j);
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", String.valueOf(unit.getUnitID()));
                                map.put("Type",
                                        String.valueOf(unit.getUnitType()));
                                map.put("Name",
                                        getResources()
                                                .getString(
                                                        R.string.personal_roleIdentity_boardOfEducation)
                                                + "-" + unit.getUnitName());
                                spdata.add(map);
                            }
                        }
                    } else if (userIdentity.getRoleIdentity() == 2) {
                        // 教师
                        List<UserUnit> UserUnits = userIdentity.getUserUnits();
                        if (UserUnits != null) {
                            for (int j = 0; j < UserUnits.size(); j++) {
                                UserUnit unit = UserUnits.get(j);
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", String.valueOf(unit.getUnitID()));
                                map.put("Type",
                                        String.valueOf(unit.getUnitType()));
                                map.put("Name",
                                        getResources()
                                                .getString(
                                                        R.string.personal_roleIdentity_teacher)
                                                + "-" + unit.getUnitName());
                                spdata.add(map);
                            }
                        }
                        List<UserClass> userClasses = userIdentity
                                .getUserClasses();
                        if (userClasses != null) {
                            for (int j = 0; j < userClasses.size(); j++) {
                                UserClass unit = userClasses.get(j);
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", String.valueOf(unit.getClassID()));
                                map.put("Type", "3");
                                map.put("Name",
                                        getResources()
                                                .getString(
                                                        R.string.personal_roleIdentity_teacher)
                                                + "-" + unit.getClassName());
                                spdata.add(map);
                            }
                        }
                    } else if (userIdentity.getRoleIdentity() == 3) {// 家长
                        List<UserClass> userClasses = userIdentity
                                .getUserClasses();
                        if (userClasses != null) {
                            for (int j = 0; j < userClasses.size(); j++) {
                                UserClass unit = userClasses.get(j);
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", String.valueOf(unit.getClassID()));
                                map.put("Type", "3");
                                map.put("Name",
                                        getResources()
                                                .getString(
                                                        R.string.personal_roleIdentity_parent)
                                                + "-" + unit.getClassName());
                                spdata.add(map);
                            }
                        }
                    } else if (userIdentity.getRoleIdentity() == 4) {// 学生
                        List<UserClass> userClasses = userIdentity
                                .getUserClasses();
                        if (userClasses != null) {
                            for (int j = 0; j < userClasses.size(); j++) {
                                UserClass unit = userClasses.get(j);
                                Map<String, String> map = new HashMap<>();
                                map.put("ID", String.valueOf(unit.getClassID()));
                                map.put("Type", "3");
                                map.put("Name",
                                        getResources()
                                                .getString(
                                                        R.string.personal_roleIdentity_student)
                                                + "-" + unit.getClassName());
                                spdata.add(map);
                            }
                        }
                    }

                }
            }
        } else if ("2".equals(SectionFlag)) {
            setActionBarTitle(R.string.release_unit_news);
            tv_pub.setText(getResources().getString(R.string.release));
            tv_type.setText(getResources().getString(R.string.news));
            if (permissionlist != null && permissionlist.size() > 0) {
                for (PublishPermission item : permissionlist) {
                    Map<String, String> map = new HashMap<>();
                    map.put("Type", String.valueOf(item.getUnitType()));
                    map.put("Name", item.getUnitName());
                    map.put("ID", String.valueOf(item.getUnitId()));
                    spdata.add(map);
                }
            }
        }

        if (spdata.size() > 0) {
            Map<String, String> item = spdata.get(0);
            UnitType = item.get("Type");
            UnitID = item.get("ID");
        } else {
            ToastUtil.showMessage(mContext, R.string.no_permission);
            finish();
        }
        SimpleAdapter spAdapter = new SimpleAdapter(this, spdata,
                R.layout.textview_spinner, new String[]{"Name"},
                new int[]{android.R.id.text1});
        sp_chose.setAdapter(spAdapter);

    }

    private void findViews() {
        // TODO Auto-generated method stub
        Button btn_send;// 发布按钮
        Button btn_camera;// 拍照取图按钮
        Button btn_gallery;// 相册取图按钮
        // layout_ui = (ScrollView) findViewById(R.id.publisharticle_layout);
        btn_send = (Button) findViewById(R.id.publisharticle_btn_send);// 发布
        btn_camera = (Button) findViewById(R.id.publisharticle_btn_camera);// 相机
        btn_gallery = (Button) findViewById(R.id.publisharticle_btn_gallery);// 相册
        edt_title = (IEditText) findViewById(R.id.publisharticle_edt_title);// 标题输入框
        edt_content = (IEditText) findViewById(R.id.publisharticle_edt_content);// 内容输入框
        sp_chose = (Spinner) findViewById(R.id.publisharticle_spinner);// 单位选项下拉菜单
        tv_pub = (TextView) findViewById(R.id.publisharticle_tv_pub);
        tv_type = (TextView) findViewById(R.id.publisharticle_tv_type);
        /** 录音 */
        Button btn_recvoice;
        Button btn_recmovie;
        btn_recvoice = (Button) findViewById(R.id.publisharticle_btn_audio);
        btn_recmovie = (Button) findViewById(R.id.publisharticle_btn_video);
        ImageView img;
        img = (ImageView) findViewById(R.id.img1);
        /**
         * 加载点击事件监听
         */
        img.setOnClickListener(this);
        btn_recmovie.setOnClickListener(this);
        btn_recvoice.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_gallery.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        /**
         * 设置监听，当触摸。。edt_content时，父ViewGroup禁止滚动
         */
        edt_content.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.publisharticle_edt_content) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }

    private String photoPath;
    private Uri photoUri;
    private Thread thread;

    /**
     * 各种控件的点击事件的监听
     */
    @Override
    public void onClick(View v) {
        List<String> list;
        switch (v.getId()) {
            case R.id.publisharticle_btn_camera:// 拍照取图
                // 友盟监听事件
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.PublicArticleActivity_btn_camera));
                list = StringUtils.getImgStr1(edt_content.getText().toString());
                if (list.size() >= 9) {
                    // 限制照片张数
                    ToastUtil.showMessage(mContext, "照片不得多于9张");
                } else if (edt_content.getTextString().length() >= 3000) {
                    // 限制内容字数
                    ToastUtil.showMessage(mContext, "字数超出限制");
                } else {
                    try {
                        // 获取图片路径
                        photoPath = JSYApplication.getInstance().FILE_PATH;
                        File photoFile = PictureUtils.createImageFile(photoPath);
                        if (photoFile != null) {
                            photoPath = photoFile.getAbsolutePath();
                            // 获取图片
                            PictureUtils.dispatchTakePictureIntent(
                                    PublishArticaleActivity.this, photoFile, 1);
                        }

                    } catch (Exception e) {
                        ToastUtil.showMessage(mContext,
                                R.string.open_camera_abnormal);
                    }
                }
                break;
            case R.id.publisharticle_btn_gallery:// 相册取图
                // 友盟的事件监听
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.PublicArticleActivity_btn_gallery));
                // 获取照片String列表
                list = StringUtils.getImgStr1(edt_content.getText().toString());
                if (list.size() >= 9) {
                    // 限制照片张数
                    ToastUtil.showMessage(mContext, "照片不得多于9张");
                } else if (edt_content.getTextString().length() >= 3000) {
                    // 限制内容字数
                    ToastUtil.showMessage(mContext, "字数超出限制");
                } else {
                    // 通过系统相册获取图片
                    Intent ii = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
                    startActivityForResult(ii, 2);
                }
                break;
            case R.id.publisharticle_btn_send:// 发布
                // 友盟事件监听
                MobclickAgent.onEvent(
                        mContext,
                        getResources().getString(
                                R.string.PublicArticleActivity_btn_send));

                String str_title;// 标题内容
                String str_content;// 内容的内容
                str_title = edt_title.getTextString();
                // 获取内容输入内容
                str_content = "<p>" + edt_content.getTextString() + "</p>";
                if ("".equals(str_content) || "".equals(str_title)) {
                    // 如果标题或内容为空，提示
                    ToastUtil.showMessage(mContext, R.string.cannot_empty);
                } else if (str_title.length() > 50 || str_title.length() < 1) {
                    // 标题的内容长度大于50或者小于1，提示
                    ToastUtil.showMessage(mContext,
                            R.string.makeSure_title_noMoreThan50_noLessThan1);
                } else {
                    // 获取照片String的List
                    list = StringUtils.getImgStr1(edt_content.getText().toString());
                    if (list.size() > 9) {
                        // 如果照片张数大于9
                        ToastUtil.showMessage(mContext, "照片不得多于9张");
                    } else {
                        // 加入参数,传递给服务器
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("Title", str_title);// 标题
                        params.addBodyParameter("Context", str_content);// 内容
                        params.addBodyParameter("uType", UnitType);// 单位类型
                        params.addBodyParameter("UnitID", UnitID);// 单位Id
                        params.addBodyParameter("SectionFlag", SectionFlag);// 标记
                        PublishArticleActivityController.getInstance()
                                .savepublishArticle(params);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 系统相机 相册 返回来的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:// 相机
                try {
                    DialogUtil.getInstance().getDialog(mContext, R.string.loading);
                    DialogUtil.getInstance().setCanCancel(false);
                    creatThread();// 开启线程获取照片
                } catch (Exception e1) {
                    bitmap = null;
                    e1.printStackTrace();
                }
                break;
            case 2:// 相册
                Cursor cursor;
                if (data != null) {
                    try {
                        Uri uri1 = data.getData();
                        cursor = this.getContentResolver().query(uri1, null,
                                null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            photoPath = cursor.getString(1);
                            DialogUtil.getInstance().getDialog(mContext,
                                    R.string.loading);
                            DialogUtil.getInstance().setCanCancel(false);
                            creatThread();// 开启线程获取照片
                            if (!cursor.isClosed())
                                cursor.close();
                        } else {
                            Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        bitmap = null;
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 创建线程
     */
    private void creatThread() {
        if (thread == null) {
            thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    bitmap = PictureUtils.getbitmapFromURL(photoPath);
                    if (bitmap == null) {
                        file = null;
                        DialogUtil.getInstance().cannleDialog();
                    } else {
                        file = PictureUtils.saveBitmapFile(bitmap, photoPath);
                    }
                    handler.sendEmptyMessage(789);
                }
            });
        }
        thread.start();
    }

    /**
     * 根据线程返回信息,处理图片
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 789:
                    DialogUtil.getInstance().cannleDialog();
                    thread.interrupt();
                    thread = null;
                    if (file != null) {// 如果照片文件不为空，上传文件
                        uploadFile(file);
                    }
                    break;
                default:
                    break;
            }
        }

    };
    private File file;

    /**
     * 上传文件
     *
     * @param file file
     */
    private void uploadFile(File file) {
        // TODO Auto-generated method stub
        DialogUtil.getInstance().getDialog(mContext, R.string.uploading);
        DialogUtil.getInstance().setCanCancel(false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("file", file);
        PublishArticleActivityController.getInstance().uploadSectionImg(params);
    }

    /**
     * 发布位置的下拉菜单的Item监听事件
     */
    public void initListener() {
        sp_chose.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Map<String, String> item = spdata.get(position);
                UnitType = item.get("Type");
                UnitID = item.get("ID");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 生命周期事件
     */
    @Override
    public void onResume() {
        EventBusUtil.register(this);
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /**
     * 生命周期事件
     */

    @Override
    public void onPause() {
        EventBusUtil.unregister(this);
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 压缩图片
     *
     * @param bitmap 图片
     * @param newWidth 宽度
     * @return  bitmap
     */
    public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        bitmap.recycle();
        return resizedBitmap;
    }

    /**
     * 获取到数据的处理
     *
     * @param list list
     */
    @Subscribe
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        switch (tag) {
            case Constant.msgcenter_notice_uploadSectionImg:// 上传图片
                UpFiles upFile = (UpFiles) list.get(1);
                if (bitmap != null) {
                    if (bitmap.getWidth() > 150) {
                        //压缩图片到150
                        bitmap = ResizeBitmap(bitmap, 150);
                    }
                    String img = "<img src=\"" + upFile.getUrl() + "\"/><br>";
                    int length = img.length()
                            + edt_content.getTextString().length();
                    if (length > 3000) {
                        ToastUtil.showMessage(mContext, "字数超出限制,请删除"
                                + (length - 3000) + "字");
                        bitmap.recycle();
                    } else {
                        //插入图片
                        BaseUtils.insetImage(edt_content, "<img src='"
                                + upFile.getUrl() + "'/>", bitmap);
                    }
                    edt_content.requestFocus();
                }

                break;
            //发布成功 给出提示  清空输入框内容
            case Constant.msgcenter_notice_savepublishArticle:
                ToastUtil.showMessage(mContext, R.string.release_success);
                edt_content.setText("");
                edt_title.setText("");
                break;
            case Constant.msgcenter_work_change:
                break;
            case Constant.msgcenter_notice_uploadSectionAudio:
                @SuppressWarnings("unchecked")
                List<UpFiles> upFiles = (List<UpFiles>) list.get(1);
                if (upFiles != null && upFiles.size() > 0) {
                    UpFiles upFile1 = upFiles.get(0);

                    Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                            R.drawable.chatto_voice_playing);
                    BaseUtils.insetImage(edt_content, "<audio src=\""
                            + upFile1.getUrl() + "\" controls=\"controls\"/>", bmp);
                }
                break;
            case Constant.msgcenter_notice_uploadSectionVideo:
                @SuppressWarnings("unchecked")
                List<UpFiles> upFiles1 = (List<UpFiles>) list.get(1);
                if (upFiles1 != null && upFiles1.size() > 0) {
                    UpFiles upFile1 = upFiles1.get(0);
                    BaseUtils.insetImage(edt_content, "<video src=\""
                                    + upFile1.getUrl() + "\" controls=\"controls\"/>",
                            bitmap);
                    bitmap.recycle();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 系统返回键的监听事件
     * 关闭Activity
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
