package com.jsy_jiaobao.main.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoAdapter;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoGroupActivity;
import com.jsy_jiaobao.po.personal.Photo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class PersonalInfoActivity extends BaseActivity implements OnClickListener{
	@ViewInject(R.id.personalspace_img_photo)private ImageView imageView;
	@ViewInject(R.id.personalspace_tv_name)private TextView tv_name;
	@ViewInject(R.id.personalspace_gridview)private CusGridView gridView;
	private Context mContext;
	private String JiaoBaoHao;
	private String UserName;
	private String UserID;
	private UnitSpacePhotoAdapter photoAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			JiaoBaoHao = savedInstanceState.getString("JiaoBaoHao");
			UserName = savedInstanceState.getString("UserName");
			UserID = savedInstanceState.getString("UserID");
		}else{
			initPass();
		}
		initViews();
		initDatas();
	}

	private void initPass() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				JiaoBaoHao = bundle.getString("JiaoBaoHao");
				UserName = bundle.getString("UserName");
				UserID = bundle.getString("UserID");
			}
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("JiaoBaoHao", JiaoBaoHao);
		outState.putString("UserName", UserName);
		outState.putString("UserID", UserID);
	}
	private void initViews() {
		setContentView(R.layout.activity_personalinfo);
		ViewUtils.inject(this);
		mContext = this;
		mCache = ACache.get(getApplicationContext());
		PersonalSpaceActivityController.getInstance().setContext(this);
		setActionBarTitle(UserName);
		tv_name.setText(UserName);
		String url = ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+ConstantUrl.photoURL+"?AccID="+JiaoBaoHao;
		JSYApplication.getInstance().bitmap.display(imageView, url);
		PersonalSpaceActivityController.getInstance().GetNewPhoto(JiaoBaoHao,"3");
		
		photoAdapter = new UnitSpacePhotoAdapter(mContext);
		gridView.setAdapter(photoAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent= new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(mContext, UnitSpacePhotoGroupActivity.class);
				bundle.putString("UnitID", JiaoBaoHao);
				bundle.putInt("UnitType", 9);
				bundle.putString("UnitName", UserName);
				bundle.putString("ClickName", getResources().getString(R.string.personal_album));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	private void initDatas() {
		
	}
	@OnClick({R.id.personalinfo_btn_space,R.id.personalinfo_btn_chat})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personalinfo_btn_space:
			Intent intent = new Intent(mContext,PersonalSpaceActivity.class);
			intent.putExtra("JiaoBaoHao", JiaoBaoHao);
			intent.putExtra("UserName", UserName);
			startActivity(intent);
			break;
		case R.id.personalinfo_btn_chat:
			break;
		default:
			break;
		}	
	}
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
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_notice_ArthListIndex:
			break;
		case Constant.msgcenter_personalspace_GetNewPhoto:
			ArrayList<Photo> myGList = (ArrayList<Photo>) list.get(1);
			photoAdapter.setData(myGList);
			photoAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
