package com.jsy_jiaobao.main.studentrecord;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.appcenter.sign.ShowPopup;
import com.jsy_jiaobao.main.system.LoginActivity;
import com.jsy_jiaobao.po.sturecord.BaseInfo;
import com.jsy_jiaobao.po.sturecord.StuRecGenPackage;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

public class StudentRecordActivity extends BaseActivity {
	private static String[] TITLE;//
	@ViewInject(R.id.layout_studentrecord)private LinearLayout layout_ui;
	@ViewInject(R.id.record_layout_pager)private ViewPager pager;
	@ViewInject(R.id.record_tab_indicator)private TabPageIndicator indicator;
	@ViewInject(R.id.record_img_photo)private ImageView img_photo;
	@ViewInject(R.id.record_tv_username)private TextView tv_username;
	@ViewInject(R.id.record_tv_unit)private TextView tv_unit;
	@ViewInject(R.id.record_tv_sex)private TextView tv_sex;
	@ViewInject(R.id.record_tv_bitrh)private TextView tv_bitrh;
	private Context mContext;
	private FragmentPagerAdapter pagerAdapter;
	private BitmapUtils bitmapUtils;
	private TextView newctag;// 最新未读数
	private TextView schctag;// 校园通知未读数
	private TextView clsctag;// 班级通知未读数
	private TextView bxctag;// 日常表现未读数
	private TextView qpctag;// 期评未读数
	private TextView tecctag;// 老师留言未读数
	private TextView genctag;// 家长留言未读数
	
	public static boolean initBaseInfo = false;
	public static int isPack = 0;//1:选择的孩子为档案包类型,0为学生类型
    public static int packid;//档案包ID;
    public static int stuid;//学生ID
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initParentView();
		initViews();

	}
	private void initViews() {
		TITLE = new String[] {
				getResources().getString(R.string.record_function_newstop),// 最新\n更新</string>
				getResources().getString(R.string.record_function_scollnotice),// 学校\n通知</string>
				getResources().getString(R.string.record_function_classnotice),// 班级\n通知</string>
				getResources().getString(R.string.record_function_dayshow),// 日常\n表现</string>
				getResources().getString(R.string.record_function_appraise),// 期评\n记录</string>
				getResources().getString(R.string.record_function_tecahcernotice),// 老师\n留言</string>
				getResources().getString(R.string.record_function_parentsnotice) // 家长\n寄语</string>
		};

		StudentRecordActivityController.getInstance().setContext(this);
		pagerAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager(),TITLE);
		pager.setAdapter(pagerAdapter);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(pageListener);
//		indicator.mTabLayout.setBackgroundColor(getResources().getColor(R.color.moccasin));
		
		newctag =  (TextView) indicator.findViewWithTag(TITLE[0]);// 最新未读数
		schctag =  (TextView) indicator.findViewWithTag(TITLE[1]);// 校园通知未读数
		clsctag =  (TextView) indicator.findViewWithTag(TITLE[2]);// 班级通知未读数
		bxctag =  (TextView) indicator.findViewWithTag(TITLE[3]);// 日常表现未读数
		qpctag =  (TextView) indicator.findViewWithTag(TITLE[4]);// 期评未读数
		tecctag =  (TextView) indicator.findViewWithTag(TITLE[5]);// 老师留言未读数
		genctag =  (TextView) indicator.findViewWithTag(TITLE[6]);// 家长留言未读数
		
		initBaseInfo = false;
        StudentRecordActivityController.getInstance().BaseInfo();
	}
	private void initParentView() {
		setContentLayout(R.layout.ui_studentrecord);
		ViewUtils.inject(this);
		bitmapUtils = JSYApplication.getInstance().bitmap;
		mContext = this;
		getSupportActionBar().setTitle(getResources().getString(R.string.function_studentrecord));
	}

	/**
	 * 子页面滚动监听
	 */
	private OnPageChangeListener pageListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		EventBusUtil.unregister(this);
	}
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work_change:
			initBaseInfo = false;
			StudentRecordActivityController.getInstance().BaseInfo();
			break;
		case Constant.sturecord_home_BaseInfo:
			initBaseInfo = true;
			String DATA = BaseActivity.sp.getString("UnitName", "");
			switch (BaseActivity.sp.getInt("RoleIdentity", 1)) {
			case 1:
				DATA = DATA+getResources().getString(R.string.of_member);
				break;
			case 2:
				DATA = DATA+getResources().getString(R.string.of_teacher);
				break;
			case 3:
				DATA = DATA+getResources().getString(R.string.of_parent);
				break;
			case 4:
				DATA = DATA+getResources().getString(R.string.of_student);
				break;

			default:
				break;
			}
			BaseInfo baseInfo = (BaseInfo) list.get(1);
			StuRecGenPackage stubase = baseInfo.getStubase();
			if (stubase.getPHOTO_PATH() != null && stubase.getPHOTO_PATH().length()>0) {
				String photoPath = stubase.getPHOTO_PATH().substring(1);
				bitmapUtils.display(img_photo, ConstantUrl.StuRecordUrl+photoPath);
			}
			tv_bitrh.setText(stubase.getCHI_BIRTH());
			tv_sex.setText(stubase.getCHI_SEX());
			tv_unit.setText(DATA);
			tv_username.setText(stubase.getCHI_NAME());
			int newc = baseInfo.getNewc();
			System.out.println("-----------最新未读数"+newc);
			if (newc >0) {
				newctag.setVisibility(0);// 最新未读数
			}else{
				newctag.setVisibility(8);
			}
			int schc = baseInfo.getSchc();
			System.out.println("-----------校园通知未读数"+schc);
			if (schc > 0) {
				schctag.setVisibility(0); // 校园通知未读数
			}else{
				schctag.setVisibility(8); // 校园通知未读数
			}
			int clsc = baseInfo.getClsc();
			System.out.println("-----------班级通知未读数"+clsc);
			if (clsc > 0) {
				clsctag.setVisibility(0);// 班级通知未读数
			}else{
				clsctag.setVisibility(8);// 班级通知未读数
			}
			int bxc = baseInfo.getBxc();
			System.out.println("-----------日常表现未读数"+bxc);
			if (bxc > 0) {
				bxctag.setVisibility(0); // 日常表现未读数
			}else{
				bxctag.setVisibility(8); // 日常表现未读数
			}
			int qpc = baseInfo.getQpc();
			System.out.println("-----------期评未读数"+qpc);
			if (qpc > 0) {
				qpctag.setVisibility(0); // 期评未读数
			}else{
				qpctag.setVisibility(8); // 期评未读数
			}
			int tecc = baseInfo.getTecc();
			System.out.println("-----------老师留言未读数"+tecc);
			if (tecc > 0) {
				tecctag.setVisibility(0); // 老师留言未读数
			}else{
				tecctag.setVisibility(8); // 老师留言未读数
			}
			int genc = baseInfo.getGenc();
			System.out.println("-----------家长留言未读数"+genc);
			if (genc > 0) {
				genctag.setVisibility(0); // 家长留言未读数
			}else{
				genctag.setVisibility(8); // 家长留言未读数
			}
			
			isPack = baseInfo.getIspack();
			packid = baseInfo.getPackid();
			stuid = baseInfo.getStuid();
			break;
		default:
			break;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SubMenu sub_menu = menu.addSubMenu(R.string.app).setIcon(R.drawable.top_btn_menu);
    	sub_menu.add(2, 1021, 0, getResources().getString(R.string.function_changeunit));
    	sub_menu.add(2, 1022, 0, getResources().getString(R.string.function_changeuser));
    	sub_menu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
       
        SubMenu sys_menu = menu.addSubMenu(R.string.system).setIcon(R.drawable.top_btn_menu);
        sys_menu.add(1, 1011, 0, getResources().getString(R.string.function_changeunit));
        sys_menu.add(1, 1012, 0, getResources().getString(R.string.function_changeuser));
        sys_menu.add(1, 1013, 0, getResources().getString(R.string.function_exit));
        sys_menu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
       
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	break;
		case 1011:
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			ShowPopup showPopup = new ShowPopup(mContext);
			showPopup.showPop(layout_ui,statusBarHeight+getSupportActionBar().getHeight()+2,Constant.listUserIdentity,null);
			
			break;
		case 1012:
			httpLogout();
			startActivity(new Intent(mContext,LoginActivity.class));
			finish();
			break;
		case 1013:
			httpLogout();
			JSYApplication.getInstance().finishActivities();
			break;

		default:
			break;
		}
        return true;
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return false;
	}

}
