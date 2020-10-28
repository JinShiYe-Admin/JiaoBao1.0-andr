package com.jsy_jiaobao.main.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 注册用户,重置密码
 */
public class RegistActivity extends BaseActivity implements OnClickListener {
	private Context mContext;
	private IEditText edt_first_phone;
	private IEditText edt_first_picnumber;
	private ImageView iv_first_picnumber;
	private IEditText edt_second_picnumber;
	private ImageView iv_second_picnumber;
	
	private TextView tv_first_getmsgnumber;
	private IEditText edt_send_phone;
	private IEditText edt_pwd1;
	private IEditText edt_pwd2;
	private ViewPager viewpager;
	private CheckBox yinsi_box;
	private TextView yinsi,known;
	private List<View> lists = new ArrayList<>();
	
	private String pageWhat =  "regeit";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			pageWhat = savedInstanceState.getString("page");
		}else{
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				pageWhat = bundle.getString("page");
			}
		}
		setContentView(R.layout.activity_register);
		mContext = this;
		RegistActivityController.getInstance().setContext(this);
		RegistActivityController.getInstance().helloService();
		DialogUtil.getInstance().getDialog(mContext, R.string.be_acessing_server);
		DialogUtil.getInstance().setCanCancel(false);
		if ("regeit".equals(pageWhat)) {
			setActionBarTitle(R.string.welcome_register_account);
		}else if ("reset".equals(pageWhat)) {
			setActionBarTitle(R.string.reset_passwords);
		}
		viewpager = (ViewPager) findViewById(R.id.register_viewpager);
		View view1 = LayoutInflater.from(this).inflate(R.layout.fragment_register_first, null);//获取手机验证码
		View view2 = LayoutInflater.from(this).inflate(R.layout.fragment_register_second, null);//检验手机验证码是否正确
		View view3 = LayoutInflater.from(this).inflate(R.layout.fragment_register_third, null);//重置密码
		initFirst(view1);
		initSecond(view2);
		initThird(view3);
		lists.add(view1);
		lists.add(view2);
		lists.add(view3);
		ViewPagerAdapter adapter = new ViewPagerAdapter(lists);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
		viewpager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (viewpager.getCurrentItem()== 0||viewpager.getCurrentItem()== 1||viewpager.getCurrentItem()== 2) {
					return true;
				}
				return false;
			}
		});
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					RegistActivityController.getInstance().GetValidateCode(iv_first_picnumber);
					break;
				case 1:
					RegistActivityController.getInstance().GetValidateCode(iv_second_picnumber);
					break;
				default:
					break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				viewpager.setCurrentItem(pageNumber);
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	private void initThird(View view) {
		edt_pwd1 = (IEditText) view.findViewById(R.id.regist_edt_pwd);
		edt_pwd2 = (IEditText) view.findViewById(R.id.regist_edt_pwd2);
		TextView tv_third_regist = (TextView) view.findViewById(R.id.regist_tv_thirdregist);
		tv_third_regist.setOnClickListener(this);
		if ("regeit".equals(pageWhat)) {
			tv_third_regist.setText(R.string.register);
		}else if ("reset".equals(pageWhat)) {
			tv_third_regist.setText(R.string.reset);
		}
	}
	private void initSecond(View view) {
		edt_send_phone = (IEditText) view.findViewById(R.id.regist_edt_phonechecknumber);
		TextView tv_second_check = (TextView) view.findViewById(R.id.regist_tv_secondcheck);
		tv_second_check.setOnClickListener(this);
		iv_second_picnumber = (ImageView) view.findViewById(R.id.retist_iv_second_picnumber);
		iv_second_picnumber.setOnClickListener(this);
		edt_second_picnumber = (IEditText) view.findViewById(R.id.regist_edt_second_picnumber);
	}
	private String oldPhoneNumber ;
	private void initFirst(View view) {
		edt_first_phone = (IEditText) view.findViewById(R.id.regist_edt_phone);
		iv_first_picnumber = (ImageView) view.findViewById(R.id.retist_iv_picnumber);
		iv_first_picnumber.setOnClickListener(this);
		yinsi_box= (CheckBox)  view.findViewById(R.id.yinsi_box);
		yinsi= (TextView)  view.findViewById(R.id.yinsi);
		known= (TextView)  view.findViewById(R.id.known);
		edt_first_picnumber = (IEditText) view.findViewById(R.id.regist_edt_picnumber);
		tv_first_getmsgnumber = (TextView) view.findViewById(R.id.regist_tv_getmsgnumber);
		tv_first_getmsgnumber.setOnClickListener(this);
		edt_first_phone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				System.out.println(hasFocus);
				if (!hasFocus) {
					String str = edt_first_phone.getTextString();
					if (!TextUtils.isEmpty(oldPhoneNumber)) {
						if (!str.equals(oldPhoneNumber)) {
							tv_first_getmsgnumber.setEnabled(true);
							tv_first_getmsgnumber.setText(getResources().getString(R.string.get_phone_note));
							if (timer != null) {
								timer.cancel();
							}
							RegistActivityController.getInstance().GetValidateCode(iv_first_picnumber);
						}
					}
					oldPhoneNumber = str;
					DialogUtil.getInstance().getDialog(mContext, R.string.verifying_phone_number);
					RegistActivityController.getInstance().checkMobileAcc(str);
				}
			}
		});
		edt_first_phone.setOnFocusChangeListener(null);

		yinsi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.YINSI_URL));
				it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				mContext.startActivity(it);
			}
		});
		known.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.KNOWN_URL));
				it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
				mContext.startActivity(it);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_tv_secondcheck://验证验证码
			String str_phone2 = edt_first_phone.getTextString();
			String str_pic2 = edt_second_picnumber.getTextString();
			String str_phonenum2 = edt_send_phone.getTextString();
			if (TextUtils.isEmpty(str_phonenum2)) {
				ToastUtil.showMessage(mContext, R.string.input_6length_note);
			}else if(TextUtils.isEmpty(str_pic2)){
				ToastUtil.showMessage(mContext, R.string.input_picture_note);
			}else{
				if ("regeit".equals(pageWhat)) {//注册
					RegistActivityController.getInstance().RegCheckMobileVcode(str_phone2,str_pic2,str_phonenum2);
				}else if ("reset".equals(pageWhat)) {//重置
					RegistActivityController.getInstance().CheckMobileVcode(str_phone2,str_pic2,str_phonenum2);
				}
			}
			break;
		case R.id.retist_iv_picnumber:
			edt_first_picnumber.setText("");
			RegistActivityController.getInstance().GetValidateCode(iv_first_picnumber);
			break;
		case R.id.retist_iv_second_picnumber:
			edt_second_picnumber.setText("");
			RegistActivityController.getInstance().GetValidateCode(iv_second_picnumber);
			break;
		case R.id.regist_tv_getmsgnumber://获取短信验证码
			if(yinsi_box.isChecked()){
				String str_phone1 = edt_first_phone.getTextString();
				String str_pic1 = edt_first_picnumber.getTextString();
				if (TextUtils.isEmpty(str_phone1)) {
					ToastUtil.showMessage(mContext, R.string.input_phone_number);
				}else if(TextUtils.isEmpty(str_pic1)){
					ToastUtil.showMessage(mContext, R.string.input_note);
				}else{
					if ("regeit".equals(pageWhat)) {//注册
						RegistActivityController.getInstance().SendCheckCode(str_phone1, str_pic1);
					}else if ("reset".equals(pageWhat)) {//重置密码
						RegistActivityController.getInstance().ReSendCheckCode(str_phone1, str_pic1);
					}
				}
			}else{
				ToastUtil.showMessage(mContext, "请先阅读并勾选 《用户协议与隐私政策》 再进行下一步操作");
			}
			break;
		case R.id.regist_tv_thirdregist:
			String str_pwd1 = edt_pwd1.getTextString();
			String str_pwd2 = edt_pwd2.getTextString();
			if (TextUtils.isEmpty(str_pwd1)) {
				ToastUtil.showMessage(mContext, R.string.input_passwords);
			}else if(str_pwd1.length()<6 || str_pwd1.length()>18){
				ToastUtil.showMessage(mContext, R.string.input_correct_passwords);
			}else if (TextUtils.isEmpty(str_pwd2)) {
				ToastUtil.showMessage(mContext, R.string.input_passwords_again);
			}else if(str_pwd2.length()<6 || str_pwd2.length()>18){
				ToastUtil.showMessage(mContext, R.string.input_correct_passwords);
			}else if(!str_pwd1.equals(str_pwd2)){
				ToastUtil.showMessage(mContext, R.string.inputed_diffrent_passwords);
			}else{
				if ("regeit".equals(pageWhat)) {
					DialogUtil.getInstance().getDialog(mContext, R.string.registering_waiting);
					RegistActivityController.getInstance().getTime();
				}else if ("reset".equals(pageWhat)) {
					DialogUtil.getInstance().getDialog(mContext, R.string.passwordResetting_waiting);
					RegistActivityController.getInstance().ResetAccPw(oldPhoneNumber,str_pwd1);
				}
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("page", pageWhat);
	}
	@Override
	protected void onResume() {
		super.onResume();
		EventBusUtil.register(this);
	}
	@Override
	protected void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}
	private int pageNumber = 0;
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.user_regist_hello:
			String hello = (String) list.get(1);
			if ("".equals(hello)) {
				RegistActivityController.getInstance().helloService();
			}else{
				RegistActivityController.getInstance().GetValidateCode(iv_first_picnumber);
				DialogUtil.getInstance().cannleDialog();
			}
			break;
		case Constant.user_regist_getTime:
			String str_time = (String) list.get(1);
			if ("".equals(str_time)) {
				RegistActivityController.getInstance().getTime();
			}else{
				String str_phone = edt_first_phone.getTextString();
				String str_pwd1 = edt_pwd1.getTextString();
				RegistActivityController.getInstance().RegAccId(str_phone,str_pwd1,str_time);
			}
			break;
		case Constant.user_regist_RegAccId://注册成功
			String reg = (String) list.get(1);
			if ("success".equals(reg)) {
				ToastUtil.showMessage(mContext, R.string.register_success);
				finish();
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.user_regist_ResetAccPw://密码重置成功
			String reset = (String) list.get(1);
			if ("success".equals(reset)) {
				ToastUtil.showMessage(mContext, R.string.passwords_reset_success);
				finish();
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.user_regist_checkmobileAcc:
			String result = (String) list.get(1);
			if (!"true".equals(result)) {//
				if ("regeit".equals(pageWhat)) {
					edt_first_phone.setText("");
					ToastUtil.showMessage(mContext, R.string.phone_hasRegistered_orError);
				}
			}else{//未注册
				if ("regeit".equals(pageWhat)) {
				}else if ("reset".equals(pageWhat)) {
					edt_first_phone.setText("");
					ToastUtil.showMessage(mContext, R.string.phone_hasNot_registered);
				}
			}
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.user_regist_SendCheckCode://注册获取验证码
		case Constant.user_regist_ReSendCheckCode://重置密码获取验证码
			edt_first_picnumber.setText("");
			RegistActivityController.getInstance().GetValidateCode(iv_first_picnumber);
			DialogUtil.getInstance().cannleDialog();
			String checkcode = (String) list.get(1);
			if ("success".equals(checkcode)) {
				viewpager.setCurrentItem(1);
				pageNumber = 1;
				tv_first_getmsgnumber.setEnabled(false);
				timer = new Timer();
				countdown = 60;
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						mHandler.sendEmptyMessage(0);
						countdown--;
					}
				}, 0,1000);
			}else if("false".equals(checkcode)){}else{
				ToastUtil.showMessage(mContext, R.string.note_inputed_wrong);
			}
			break;
		case Constant.user_regist_RegCheckMobileVcode://注册验证验证码
		case Constant.user_regist_CheckMobileVcode://重置密码验证验证码
			DialogUtil.getInstance().cannleDialog();
			String checkEMS = (String) list.get(1);
			if ("success".equals(checkEMS)) {
				viewpager.setCurrentItem(2);
				pageNumber = 2;
			}else if("false".equals(checkEMS)){
				viewpager.setCurrentItem(0);
				pageNumber = 0;
				edt_send_phone.setText("");
				edt_second_picnumber.setText("");
				ToastUtil.showMessage(mContext, R.string.smsNote_inputedWrong_again);
			}else {
				ToastUtil.showMessage(mContext, R.string.smsNote_inputedWrong);
			}
			break;
		default:
			break;
		}
	}
	private Timer timer;
	private int countdown = 60;
	private Handler mHandler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (countdown>0) {
					tv_first_getmsgnumber.setText("还剩"+String.valueOf(countdown)+"s可再次获取");
				}else{
					tv_first_getmsgnumber.setText(getResources().getString(R.string.get_phone_note));
					tv_first_getmsgnumber.setEnabled(true);
					timer.cancel();
				}
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){ 
			if(pageNumber>0){
				pageNumber--;
				viewpager.setCurrentItem(pageNumber);
			}else{
				finish();
			}
			return false;
		}
        return super.onKeyDown(keyCode, event);
	}

	public class ViewPagerAdapter extends PagerAdapter {

		List<View> viewLists;

		public ViewPagerAdapter(List<View> lists) {
			viewLists = lists;
		}

		@Override
		public int getCount() { // 获得size
			return viewLists.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View view, int position, Object object){
			((ViewPager) view).removeView(viewLists.get(position));
		}

		@Override
		public Object instantiateItem(View view, int position){
			((ViewPager) view).addView(viewLists.get(position), 0);
			return viewLists.get(position);
		}
	}
}
