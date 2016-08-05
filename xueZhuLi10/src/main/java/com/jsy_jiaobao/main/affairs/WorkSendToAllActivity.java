package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.appcenter.sign.ShowPopup;
import com.jsy_jiaobao.main.system.VisitPublicHttp;
import com.jsy_jiaobao.po.personal.GetAttList;
import com.jsy_jiaobao.po.sys.ClassRevicer;
import com.jsy_jiaobao.po.sys.GetRevicerList;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;
import com.jsy_jiaobao.po.sys.UnitRevicer;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class WorkSendToAllActivity extends BaseActivity implements PublicMethod{
	@ViewInject(R.id.ui_sendmessage)ScrollView layout_ui;
	
	@ViewInject(R.id.sendmessage_layout_sendtoall)LinearLayout layout_sendtoall;
	@ViewInject(R.id.sendmessage_btn_lowerunit)Button btn_sendtolowerunit;
	@ViewInject(R.id.sendmessage_btn_lowerparent)Button btn_sendtolowerparent;
	@ViewInject(R.id.sendmessage_btn_lowerstudent)Button btn_sendtolowerstudent;
	@ViewInject(R.id.sendmessage_layout_content)LinearLayout layout_content;
	@ViewInject(R.id.sendmessage_layout_currunit)LinearLayout layout_currunit;
	@ViewInject(R.id.sendmessage_layout_higher)HorizontalScrollView layout_higher;
	@ViewInject(R.id.sendmessage_layout_lower)HorizontalScrollView layout_lower;
	@ViewInject(R.id.sendmessage_layout_reciverlist)LinearLayout layout_reciver;
	@ViewInject(R.id.sendmessage_layout_lowerunit)LinearLayout layout_lowerunit;
	@ViewInject(R.id.sendmessage_btn_currunit)Button btn_currunit;//当前单位
	@ViewInject(R.id.sendmessage_layout_higherunit)LinearLayout layout_higherunit;//上级单位
	@ViewInject(R.id.sendmessage_edt_content)IEditText edt_content;//内容框
	@ViewInject(R.id.sendmessage_checkBox_shotmsg)CheckBox cb_shotmsg;//是否是短信
	@ViewInject(R.id.sendmessage_btn_toall)Button btn_toall;//全选
	@ViewInject(R.id.sendmessage_btn_invert)Button btn_invert;//反选
	@ViewInject(R.id.sendmessage_btn_send)Button btn_send;//发布
	@ViewInject(R.id.sendmessage_tv_lowerunit)TextView tv_lowerunit;//下级单位 介绍
	
	private Context mContext;
	private List<CusGridView> grids = new ArrayList<CusGridView>();
	private String str_msgcontent;
	private List<NameValuePair> reciverList = new ArrayList<NameValuePair>();
	private String curunitid;//当前所在单位加密id
	
	private String tomsgid;
	private String UnitName;
	private String content;
	private GetAttList getAttList;
	private int unitclassgenCount = 0;
	private List<View> viewNames = new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			getAttList = (GetAttList) savedInstanceState.getSerializable("getAttList");
			tomsgid = savedInstanceState.getString("tomsgid");
			content = savedInstanceState.getString("content");
		}else{
			initPassData();
		}
		initViews();
		initDeatilsData();
		initListener();
	}
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				getAttList = (GetAttList) bundle.getSerializable("getAttList");
				tomsgid = bundle.getString("tomsgid");
				content = bundle.getString("content");
			}
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tomsgid", tomsgid);
		outState.putString("content", content);
		outState.putSerializable("getAttList", getAttList);
	}
	@Override
	public void initViews() {
		setContentLayout(R.layout.fragment_msgcenter_sendmessage);
		ViewUtils.inject(this);
		mContext = this;
		WorkSendMessageActivityController.getInstance().setContext(this);
		VisitPublicHttp.getInstance().setContext(this);
		curunitid = sp.getString("TabIDStr", "");
		
		setActionBarTitle(R.string.transpond_message);
		
		layout_content.setFocusable(true);
		layout_content.setFocusableInTouchMode(true);
		layout_content.requestFocus();
		btn_currunit.setText(sp.getString("UnitName", getResources().getString(R.string.noUnit_pleaseChangeUnit)));
		layout_higher.setVisibility(8);
		UnitName = sp.getString("UnitName", "");
		if (!UnitName.equals("")) {
			tv_lowerunit.setText(UnitName+getResources().getString(R.string.class_));
		}
		edt_content.setText(content);
		cb_shotmsg.setChecked(true);
//		if (mCache.getAsString("sbrevicerdata") !=null &&(sp.getInt("sbUnitID", 0)+"").equals(mCache.getAsString("UnitID"))&&(sp.getString("JiaoBaoHao", "")).equals(mCache.getAsString("SbJiaoBaoHao"))){
//			VisitPublicHttp.getInstance().changeCurUnit(false);
//			initRevicerList();
//		}else{
			VisitPublicHttp.getInstance().changeCurUnit(true);
//		}

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
			showPopup.showPop(layout_ui,statusBarHeight+getSupportActionBar().getHeight()+2,Constant.listUserIdentity,mHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			VisitPublicHttp.getInstance().setContext(this);
			VisitPublicHttp.getInstance().getRoleIdentity();
		}
	}
	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				String title = (String) msg.obj;
				btn_currunit.setText(title);
				break;
			default:
				break;
			}
		};
	};
	@OnClick({R.id.sendmessage_btn_toall,R.id.sendmessage_btn_invert,R.id.sendmessage_btn_currunit,R.id.sendmessage_btn_send})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.sendmessage_btn_toall:
			for (int i = 0; i < viewNames.size(); i++) {
				View view = viewNames.get(i);
				if (view.getVisibility() == 0) {
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
				if (view.getVisibility() == 0) {
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
			unitclassgenCount = 0;
			if (myUnitRevicer != null) {
				creatReciverView(myUnitRevicer.getUserList(),"myUnitRevicer.getUserList","selit");
			}else{
				noRecivers();
			}
			break;
		case R.id.sendmessage_btn_send:
			str_msgcontent = edt_content.getTextString();
			if ("".equals(str_msgcontent)) {
				ToastUtil.showMessage(mContext, R.string.content_cannot_empty);
			}else{
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
				if(reciverList ==null ||reciverList.size()==0){
					ToastUtil.showMessage(mContext, R.string.please_choose_receiver);
				}else{
					dialog_send(reciverList.size());
				}
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 没有权限时 提示
	 */
	private void noRecivers(){
		btn_send.setEnabled(false);
		boolean haveView = false;
		for (int i = 0; i < viewNames.size(); i++) {
			ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
			String t = (String) tag.get(0);
			if (t.equals("noRecivers")) {
				haveView = true;
				viewNames.get(i).setVisibility(0);
			}else{
				viewNames.get(i).setVisibility(8);
			}
		}
		if (!haveView) {
			LinearLayout layout = new LinearLayout(mContext);
			ArrayList<Object> layoutTag = new ArrayList<Object>();
			layoutTag.add(0,"noRecivers");
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
	 * @param count
	 */
	protected void dialog_send(int count) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("确定要向"+count+"人发送信息吗?");
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
		params.addBodyParameter("unitclassgenCount",String.valueOf(unitclassgenCount));
		params.addBodyParameter("grsms",false+"");
		params.addBodyParameter("talkcontent", str_msgcontent);
		if (cb_shotmsg.isChecked()) {
			params.addBodyParameter("SMSFlag","1");
		}else{
			params.addBodyParameter("SMSFlag","0");
		}
		params.addBodyParameter("curunitid",curunitid);
		params.addBodyParameter("tomsgid",tomsgid);
//		params.addBodyParameter("toid",tomsgid);
		WorkSendMessageActivityController.getInstance().CreateCommMsg(params);
	}
	/**
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work_change:
			curunitid = sp.getString("TabIDStr", "");
			boolean changeUnit = (Boolean) list.get(1);
			if (changeUnit) {
				WorkSendMessageActivityController.getInstance().CommMsgRevicerList(false);
			}
			break;
		case Constant.msgcenter_work_CommMsgRevicerList:
			grids.clear();
			viewNames.clear();
			layout_reciver.removeAllViews();
			initRevicerList();
			break;
		case Constant.msgcenter_work_CreateCommMsg:
			edt_content.setText("");
			for (int i = 0; i < grids.size(); i++) {
				GridViewAdapter adapter = (GridViewAdapter) grids.get(i).getAdapter();
				adapter.setCheckAll();
				adapter.setInverse();
				adapter.notifyDataSetChanged();
			}
			ToastUtil.showMessage(mContext,R.string.send_success);
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
	private UnitRevicer myUnitRevicer;
	private List<ClassRevicer> classRevicerList;
	private void initRevicerList() {
		GetRevicerList getRevicerList =GsonUtil.GsonToObject(mCache.getAsString("sbrevicerdata"), GetRevicerList.class);
		List<UnitRevicer> parentUnitRevicer = getRevicerList.getParentUnitRevicer();//上级单位接收人
		myUnitRevicer = getRevicerList.getMyUnitRevicer();//本单位接收人
		List<UnitRevicer> subUnitRevicer = getRevicerList.getSubUnitRevicer();//下级单位接收人
		classRevicerList = getRevicerList.getUnitClassRevicer();//班级接收人（家长或老师）

		creatParentName(parentUnitRevicer);//生成上级单位名称
		btn_send.setEnabled(true);
		layout_reciver.removeAllViews();
		if(myUnitRevicer != null){
			btn_currunit.setText(myUnitRevicer.getUnitName());
			creatReciverView(myUnitRevicer.getUserList(),"myUnitRevicer.getUserList","selit");
		}
		switch (sp.getInt("RoleIdentity", 1)) {
		case 1:
			creatSubName(subUnitRevicer);
			break;
		case 2:
			creatSubClassName(classRevicerList);
			break;
		case 3:
			creatSubClassName(classRevicerList);
			
			break;
		case 4:
			
			break;
		case 5:
			
			break;

		default:
			break;
		}
	}
	/**
	 * 生成上级单位名称
	 */
	private void creatParentName(List<UnitRevicer> parentUnitRevicer){
		layout_higherunit.removeAllViews();
		if (parentUnitRevicer != null && parentUnitRevicer.size() != 0) {
			for (int i = 0; i < parentUnitRevicer.size(); i++) {
				String name = parentUnitRevicer.get(i).getUnitName();
				Button btn = new Button(mContext);
				btn.setText(name);
				btn.setTag(parentUnitRevicer.get(i));
				btn.setOnClickListener(unitClickListener);
				layout_higherunit.addView(btn);
			}
		}else{
			Button no = new Button(mContext);
			no.setText("无");
			layout_higherunit.addView(no);
		}
	}
	/**
	 * 生成下级单位名称
	 */
	private void creatSubName(List<UnitRevicer> subUnitRevicer){
		layout_lowerunit.removeAllViews();
		if (subUnitRevicer != null) {
			for (int i = 0; i < subUnitRevicer.size(); i++) {
				String name = subUnitRevicer.get(i).getUnitName();
				Button btn = new Button(mContext);
				btn.setText(name);
				btn.setTag(subUnitRevicer.get(i));
				btn.setOnClickListener(unitClickListener);
				layout_lowerunit.addView(btn);
			}
		}else{
			Button no = new Button(mContext);
			no.setText("无");
			layout_lowerunit.addView(no);
		}
		
	}
	/**
	 * 生成班级接收人
	 */
	private void creatSubClassName(List<ClassRevicer> classRevicerList){
		layout_lowerunit.removeAllViews();
		if (classRevicerList != null) {
			for (int i = 0; i < classRevicerList.size(); i++) {
				String name = classRevicerList.get(i).getClassName();
				Button btn = new Button(mContext);
				btn.setText(name);
				btn.setTag(classRevicerList.get(i));
				btn.setOnClickListener(unitClickListener);
				layout_lowerunit.addView(btn);
			}
		}
	}
	OnClickListener unitClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
				UnitRevicer tag = (UnitRevicer) v.getTag();
				unitclassgenCount = 0;
				if (tag == null) {
					noRecivers();
				}else{
					creatReciverView(tag.getUserList(),tag.getTabIDStr(),"selit");
				}
			} catch (Exception e) {
				ClassRevicer tag = (ClassRevicer) v.getTag();
				
				if (tag == null) {
					noRecivers();
				}else{
					unitclassgenCount = tag.getStudentgens_genselit().size();
					GroupUserList itemT = new GroupUserList();
					itemT.setGroupName(getResources().getString(R.string.thisClass_teacher));
					itemT.setGroupselit_selit(tag.getTeachers_selit());
					itemT.setMCount(0);
					
					GroupUserList itemS = new GroupUserList();
					itemS.setGroupName(getResources().getString(R.string.thisClass_parent));
					
					itemS.setGroupselit_selit(tag.getStudentgens_genselit());
					itemS.setMCount(0);
					
					List<GroupUserList> UserList = new ArrayList<GroupUserList>();
					UserList.add(itemS);
					if (tag.getTeachers_selit()!= null) {
						UserList.add(itemT);
					}
					creatReciverView(UserList,tag.getClassName(),"genselit");
				}
			}
		}
	};
	
	/**
	 * 生成接收人列表
	 * @param UserList 
	 */
	private void creatReciverView(List<GroupUserList> UserList,String name,String postTag){
		btn_send.setEnabled(true);
//		layout_reciver.removeAllViews();
		boolean haveView = false;
		for (int i = 0; i < viewNames.size(); i++) {
			ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
			if (name.equals((String)tag.get(0))) {
				haveView = true;
				viewNames.get(i).setVisibility(0);
			}else{
				viewNames.get(i).setVisibility(8);
			}
		}
		if (!haveView) {
			if (UserList != null && UserList.size() > 0) {
				LinearLayout layout = new LinearLayout(mContext);
				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				layout.setBackgroundResource(R.drawable.edittext_bg);
				layout.setOrientation(LinearLayout.VERTICAL);
				ArrayList<Object> adapterList = new ArrayList<Object>();
				for (int i = 0; i < UserList.size(); i++) {
					View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
					TextView title = (TextView) view.findViewById(R.id.sendmessage_group_name);
					Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
					Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);
					
					List<Selit> selitList = UserList.get(i).getGroupselit_selit();
					title.setText(UserList.get(i).getGroupName());
					CusGridView gridview = new CusGridView(mContext, null);
					gridview.setNumColumns(2);
					GridViewAdapter adapter = new GridViewAdapter(mContext);
					adapter.setReciver(selitList);
					adapter.setPostTag(postTag);
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
				ArrayList<Object> tag = new ArrayList<Object>();
				tag.add(0,name);
				tag.add(1,adapterList);
				layout.setTag(tag);
				layout_reciver.addView(layout);
				viewNames.add(layout);
			}else{
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
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
			finish();
			return true;
		}
		return false;
	}
}
