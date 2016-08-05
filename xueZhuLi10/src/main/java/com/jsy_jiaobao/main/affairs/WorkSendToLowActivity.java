package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.text.TextUtils;
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
import com.jsy_jiaobao.po.sys.GetMsgAllRevicer_toSchoolGen;
import com.jsy_jiaobao.po.sys.GetUnitAdminRevicer;
import com.jsy_jiaobao.po.sys.Selit;
import com.jsy_jiaobao.po.sys.UnitAdminRevicer;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * <pre>
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 			               佛祖保佑       永无BUG
 * 下发通知
 */
public class WorkSendToLowActivity extends BaseActivity implements PublicMethod{
	private static String TAG = WorkSendToLowActivity.class.getName();
	@ViewInject(R.id.ui_sendmessage)ScrollView layout_ui;
	
	@ViewInject(R.id.sendmessage_layout_sendtoall)LinearLayout layout_sendtoall;
	@ViewInject(R.id.sendmessage_btn_lowerunit)Button btn_sendtolowerunit;
	@ViewInject(R.id.sendmessage_btn_lowerparent)Button btn_sendtolowerparent;
	@ViewInject(R.id.sendmessage_btn_lowerstudent)Button btn_sendtolowerstudent;
	@ViewInject(R.id.sendmessage_layout_content)LinearLayout layout_content;
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
	@ViewInject(R.id.sendmessage_tv_curr)TextView tv_curr;//当前单位名
	private Context mContext;
	private List<CusGridView> grids = new ArrayList<CusGridView>();
	private String str_msgcontent;
	private String curunitid;//当前所在单位加密id
	
	private String tomsgid = "";
	private String content = "";
	
	private String cache_UnitAdminRevicer;
	
	/**MsgAll_ToSubUnitMem=true 获取群发下属单位接收对象：GetMsgAllRevicer_toSubUnit*/
	private boolean MsgAll_ToSubUnitMem;
	/**MsgAll_ToGen =true 获取群发家长的接收对象：GetMsgAllRevicer_toSchoolGen*/
	private boolean MsgAll_ToGen;
	
	private String MsgAll_ToGen_Data;
	private String MsgAll_ToSubUnitMem_Data;
	
	private final int sub = 1;
	private final int gen = 2;
	private List<NameValuePair> reciverList = new ArrayList<NameValuePair>();
	private List<View> viewNames = new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
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
		Intent intent = getIntent();  
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
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
	}

	@Override
	public void initViews() {
		setContentLayout(R.layout.fragment_msgcenter_sendmessage);
		ViewUtils.inject(this);
		mContext = this;
		WorkSendMessageActivityController.getInstance().setContext(this);
		VisitPublicHttp.getInstance().setContext(this);
		
		setActionBarTitle(R.string.handOut_notice);
		cache_UnitAdminRevicer = sp.getString("JiaoBaoHao", "")+sp.getInt("UnitID", 0)+sp.getInt("RoleIdentity", 1)+"UnitAdminRevicer";
		curunitid = sp.getString("TabIDStr", "");
		layout_content.setFocusable(true);
		layout_content.setFocusableInTouchMode(true);
		layout_content.requestFocus();
		layout_higher.setVisibility(8);
		layout_lower.setVisibility(8);
		btn_sendtolowerstudent.setVisibility(8);
		layout_sendtoall.setVisibility(0);
		if (!TextUtils.isEmpty(content)) {
			edt_content.setText(content);
		}
		btn_currunit.setText(sp.getString("UnitName", getResources().getString(R.string.noUnit_pleaseChangeUnit)));
		cb_shotmsg.setChecked(true);
//		MsgAll_ToGen_Data = mCache.getAsString(cache_UnitAdminRevicer+"MsgAll_ToGen");
//		MsgAll_ToSubUnitMem_Data = mCache.getAsString(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem");
		btn_sendtolowerparent.setTextColor(getResources().getColor(R.color.gray));
		btn_sendtolowerunit.setTextColor(getResources().getColor(R.color.gray));
//		if (TextUtils.isEmpty(MsgAll_ToSubUnitMem_Data)) {
//			btn_sendtolowerunit.setVisibility(8);
//		}else{
//			btn_sendtolowerunit.setVisibility(0);
//		}
//		if (TextUtils.isEmpty(MsgAll_ToGen_Data)) {
//			btn_sendtolowerparent.setVisibility(8);
//		}else{
//			btn_sendtolowerparent.setVisibility(0);
//		}
		VisitPublicHttp.getInstance().changeCurUnit(true);
//		if (TextUtils.isEmpty(MsgAll_ToSubUnitMem_Data)&&TextUtils.isEmpty(MsgAll_ToGen_Data)){
//			VisitPublicHttp.getInstance().changeCurUnit(true);
//		}else if(!TextUtils.isEmpty(MsgAll_ToSubUnitMem_Data)){
//			VisitPublicHttp.getInstance().changeCurUnit(false);
//			initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem",sub);
//		}else{
//			VisitPublicHttp.getInstance().changeCurUnit(false);
//			initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToGen",gen);
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
	@OnClick({R.id.sendmessage_btn_currunit,R.id.sendmessage_btn_lowerunit,R.id.sendmessage_btn_lowerparent,R.id.sendmessage_btn_lowerstudent,R.id.sendmessage_btn_toall,R.id.sendmessage_btn_invert,R.id.sendmessage_btn_send})
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
				
//				List<Selit> reciverList = new ArrayList<Selit>();
//				for (int i = 0; i < grids.size(); i++) {
//					GridViewAdapter adapter = (GridViewAdapter) grids.get(i).getAdapter();
//				    List<Selit> list = adapter.getCheckedSelit();
//				    reciverList.addAll(list);
//				}
//				if(reciverList ==null ||reciverList.size()==0){
//					ToastUtil.showMessage(mContext, "请选择接收人");
//				}else{
//					dialog_send(reciverList.size());
//				}
			}
			break;
		case R.id.sendmessage_btn_lowerunit://群发给下级单位接收人
			tv_curr.setText(R.string.lower_unit);
			btn_sendtolowerparent.setTextColor(getResources().getColor(R.color.gray));
			btn_sendtolowerunit.setTextColor(getResources().getColor(R.color.black));
			initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem",sub);
			break;
		case R.id.sendmessage_btn_lowerparent://群发给学校家长
			tv_curr.setText(R.string.all_parent);
			btn_sendtolowerparent.setTextColor(getResources().getColor(R.color.black));
			btn_sendtolowerunit.setTextColor(getResources().getColor(R.color.gray));
			initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToGen",gen);
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
		
		params.addBodyParameter("grsms",false+"");
		params.addBodyParameter("talkcontent", str_msgcontent);
		if (cb_shotmsg.isChecked()) {
			params.addBodyParameter("SMSFlag","1");
		}else{
			params.addBodyParameter("SMSFlag","0");
		}
		params.addBodyParameter("curunitid",curunitid);
		WorkSendMessageActivityController.getInstance().CreateCommMsg(params);
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_work_change:
			curunitid = sp.getString("TabIDStr", "");
			cache_UnitAdminRevicer = sp.getString("JiaoBaoHao", "")+sp.getInt("UnitID", 0)+sp.getInt("RoleIdentity", 1)+"UnitAdminRevicer";
			boolean changeUnit = (Boolean) list.get(1);
			if (changeUnit) {
				WorkSendMessageActivityController.getInstance().GetMsgAllReviceUnitList();
			}
			break;
		case Constant.msgcenter_work_GetMsgAllReviceUnitList:
			grids.clear();
			viewNames.clear();
			layout_reciver.removeAllViews();
			HashMap<String, Boolean> map = (HashMap<String, Boolean>) list.get(1);
			MsgAll_ToSubUnitMem = map.get("MsgAll_ToSubUnitMem");
			MsgAll_ToGen = map.get("MsgAll_ToGen");
			
			if (MsgAll_ToGen) {
				btn_sendtolowerparent.setVisibility(0);
//				MsgAll_ToGen_Data = mCache.getAsString(cache_UnitAdminRevicer+"MsgAll_ToGen");
//				if (TextUtils.isEmpty(MsgAll_ToGen_Data)) {
					WorkSendMessageActivityController.getInstance().GetMsgAllRevicer_toSchoolGen();
//				}else{
//					tv_curr.setText("所有家长");
//					initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToGen",gen);
//				}
			}else{
				btn_sendtolowerparent.setVisibility(8);
			}
			if (MsgAll_ToSubUnitMem) {
				btn_sendtolowerunit.setVisibility(0);
//				MsgAll_ToSubUnitMem_Data = mCache.getAsString(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem");
//				if (TextUtils.isEmpty(MsgAll_ToSubUnitMem_Data)) {
					WorkSendMessageActivityController.getInstance().GetMsgAllRevicer_toSubUnit();
//				}else{
//					initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem",sub);
//					tv_curr.setText("下级单位");
//				}
			}else{
				btn_sendtolowerunit.setVisibility(8);
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
			ToastUtil.showMessage(mContext, R.string.send_success);
			break;
		case Constant.msgcenter_work_geterror:
			noRecivers();
			break;
		case Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit:
			MsgAll_ToSubUnitMem_Data = (String) list.get(1);
			mCache.put(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem", MsgAll_ToSubUnitMem_Data);
			if (TextUtils.isEmpty(MsgAll_ToGen_Data)) {
				initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToSubUnitMem",sub);
				tv_curr.setText(R.string.lower_unit);
				btn_sendtolowerparent.setTextColor(getResources().getColor(R.color.gray));
				btn_sendtolowerunit.setTextColor(getResources().getColor(R.color.black));
			}
			break;
		case Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen:
			MsgAll_ToGen_Data = (String) list.get(1);
			mCache.put(cache_UnitAdminRevicer+"MsgAll_ToGen", MsgAll_ToGen_Data);
			if (TextUtils.isEmpty(MsgAll_ToSubUnitMem_Data)) {
				btn_sendtolowerparent.setTextColor(getResources().getColor(R.color.black));
				btn_sendtolowerunit.setTextColor(getResources().getColor(R.color.gray));
				initRevicerList(cache_UnitAdminRevicer+"MsgAll_ToGen",gen);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 处理接收人
	 */
	private List<UnitAdminRevicer> selitadmintomemList;
	private void initRevicerList(String cacheName,int flag) {
		if (flag == sub) {
			GetUnitAdminRevicer getUnitAdminRevicer = GsonUtil.GsonToObject(mCache.getAsString(cacheName), GetUnitAdminRevicer.class);
			if (null != getUnitAdminRevicer) {
				selitadmintomemList = getUnitAdminRevicer.getSelitadmintomem();//单位管理员，转发给本单位人员
				creatLowUnitReciverView(selitadmintomemList,cacheName+sub,"selitadmintomem");
			}
//			switch (sp.getInt("RoleIdentity", 1)) {
//			case 1:
//				break;
//			case 2:
//				noRecivers();
//				break;
//			default:
//				break;
//			}
		}else if(flag == gen){
			GetMsgAllRevicer_toSchoolGen getMsgAllRevicer_toSchoolGen = GsonUtil.GsonToObject(mCache.getAsString(cacheName), GetMsgAllRevicer_toSchoolGen.class);
	
			ArrayList<UnitAdminRevicer> selitadmintogen = getMsgAllRevicer_toSchoolGen.getSelitadmintogen();
		
			ArrayList<Selit> selitunitclassadmintogen = getMsgAllRevicer_toSchoolGen.getSelitunitclassadmintogen();
			ArrayList<Selit> selitunitclassidtogen = getMsgAllRevicer_toSchoolGen.getSelitunitclassidtogen();
		
			if (null != selitadmintogen) {
				creatLowUnitReciverView(selitadmintogen,cacheName+gen,"selitadmintogen");
			}
			if(null != selitunitclassadmintogen){
				tv_curr.setText(R.string.parents_administrator);
				creatGenReciverView(selitunitclassadmintogen,cacheName+gen+"selitunitclassadmintogen","selitunitclassadmintogen");
			}
			if(null != selitunitclassidtogen){
				
				creatGenReciverView(selitunitclassidtogen,cacheName+gen+"selitunitclassidtogen","selitunitclassidtogen");
			}
		}
	}
	/**
	 * 生成群发给下级单位的接收人列表
	 * @param selitadmintomemList 
	 * @param postTag 
	 */
	private void creatGenReciverView(List<Selit> selitadmintomemList,String name, String postTag) {
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
			if (selitadmintomemList != null) {
				LinearLayout layout = new LinearLayout(mContext);
				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				layout.setBackgroundResource(R.drawable.edittext_bg);
				layout.setOrientation(LinearLayout.VERTICAL);
				ArrayList<Object> adapterList = new ArrayList<Object>();
				
				View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
				TextView title = (TextView) view.findViewById(R.id.sendmessage_group_name);
				Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
				Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);
				if ("selitunitclassadmintogen".equals(name)) {
					title.setText(R.string.thisClass_administrator);
				}else{
					title.setText(R.string.thisClass_parent);
				}
				
				CusGridView gridview = new CusGridView(mContext, null);
				gridview.setNumColumns(2);
				GridViewAdapter adapter = new GridViewAdapter(mContext);
				adapter.setReciver(selitadmintomemList);
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
	/**
	 * 生成群发给下级单位的接收人列表
	 * @param selitadmintomemList 
	 * @param postTag 
	 */
	private void creatLowUnitReciverView(List<UnitAdminRevicer> selitadmintomemList,String name, String postTag) {
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
			if (selitadmintomemList != null && selitadmintomemList.size()>0) {
				LinearLayout layout = new LinearLayout(mContext);
				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				layout.setBackgroundResource(R.drawable.edittext_bg);
				layout.setOrientation(LinearLayout.VERTICAL);
				ArrayList<Object> adapterList = new ArrayList<Object>();
				for (int i = 0; i < selitadmintomemList.size(); i++) {
					
					
					View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
					TextView title = (TextView) view.findViewById(R.id.sendmessage_group_name);
					Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
					Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);
					
					List<Selit> selitList = selitadmintomemList.get(i).getUserList();
					title.setText(selitadmintomemList.get(i).getGroupName());
					
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
//	/**
//	 * 生成接收人列表  学校管理员身份
//	 * @param UserList 
//	 */
//	private void creatReciverView(UnitClassALLRevicer unitClassAdminRevicer){
//		layout_reciver.removeAllViews();
//		grids.clear();
//		btn_send.setEnabled(true);
//		if (unitClassAdminRevicer == null) {
//			noRecivers();
//		}else{
//			List<Selit> classadmintogen = unitClassAdminRevicer.getSelitunitclassadmintogen();
//			List<Selit> classadmintostu = unitClassAdminRevicer.getSelitunitclassadmintostu();
//			List<Selit> classidtogen = unitClassAdminRevicer.getSelitunitclassidtogen();
//			List<Selit> classidtostu = unitClassAdminRevicer.getSelitunitclassidtostu();
//			TextView title = new TextView(mContext);
//			title.setText("本校班级");
//			layout_reciver.addView(title);
//			if (classadmintogen != null && classadmintogen.size()>0) {
//				CusGridView gridview = new CusGridView(mContext, null);
//				gridview.setNumColumns(2);
//				GridViewAdapter adapter = new GridViewAdapter(mContext);
//				adapter.setReciver(classadmintogen);
//				gridview.setAdapter(adapter);
//				gridview.setTag("selitunitclassadmintogen");
//				layout_reciver.addView(gridview);
//				grids.add(gridview);
//			}
//			if (classadmintostu != null && classadmintostu.size()>0) {
//				CusGridView gridview = new CusGridView(mContext, null);
//				gridview.setNumColumns(2);
//				GridViewAdapter adapter = new GridViewAdapter(mContext);
//				adapter.setReciver(classadmintostu);
//				gridview.setAdapter(adapter);
//				gridview.setTag("selitunitclassadmintostu");
//				layout_reciver.addView(gridview);
//				grids.add(gridview);
//			}
//			if (classidtogen != null && classidtogen.size()>0) {
//				CusGridView gridview = new CusGridView(mContext, null);
//				gridview.setNumColumns(2);
//				GridViewAdapter adapter = new GridViewAdapter(mContext);
//				adapter.setReciver(classidtogen);
//				gridview.setAdapter(adapter);
//				gridview.setTag("selitadmintomem");
//				layout_reciver.addView(gridview);
//				grids.add(gridview);
//			}
//			if (classidtostu != null && classidtostu.size()>0) {
//				CusGridView gridview = new CusGridView(mContext, null);
//				gridview.setNumColumns(2);
//				GridViewAdapter adapter = new GridViewAdapter(mContext);
//				adapter.setReciver(classidtostu);
//				gridview.setAdapter(adapter);
//				gridview.setTag("selitadmintogen");
//				layout_reciver.addView(gridview);
//				grids.add(gridview);
//			}
//		}
//	}
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
