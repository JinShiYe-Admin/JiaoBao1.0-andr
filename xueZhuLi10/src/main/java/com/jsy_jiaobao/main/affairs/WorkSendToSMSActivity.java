package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jsy_jiaobao.po.sys.GetSMS;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;

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
 * 短信直通车
 */
public class WorkSendToSMSActivity extends BaseActivity implements PublicMethod, OnClickListener{
	private ScrollView layout_ui;
	
	private LinearLayout layout_unit;
	private LinearLayout layout_sendtoall;
	private Button btn_sendtolowerunit;
	private Button btn_sendtolowerparent;
	private Button btn_sendtolowerstudent;
	private LinearLayout layout_content;
	private HorizontalScrollView layout_higher;
	private HorizontalScrollView layout_lower;
	private LinearLayout layout_reciver;
	private LinearLayout layout_lowerunit;
	private Button btn_currunit;//当前单位
	private LinearLayout layout_higherunit;//上级单位
	private IEditText edt_content;//内容框
	private CheckBox cb_shotmsg;//是否是短信
	private Button btn_toall;//全选
	private Button btn_invert;//反选
	private Button btn_send;//发布
	
	private Context mContext;
	
	private List<CusGridView> grids = new ArrayList<CusGridView>();
	private String str_msgcontent;
	private String curunitid;//当前所在单位加密id
	
	private List<SMSTreeUnitID> reciverSMSListTea = new ArrayList<SMSTreeUnitID>();
	private List<SMSTreeUnitID> reciverSMSListPar = new ArrayList<SMSTreeUnitID>();
	private List<SMSTreeUnitID> reciverSMSListStu = new ArrayList<SMSTreeUnitID>();
	private List<View> viewNames = new ArrayList<View>();
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
		layout_ui=(ScrollView) findViewById(R.id.ui_sendmessage);
		
		layout_unit=(LinearLayout) findViewById(R.id.sendmessage_layout_unit);
		layout_sendtoall=(LinearLayout) findViewById(R.id.sendmessage_layout_sendtoall);
		btn_sendtolowerunit=(Button) findViewById(R.id.sendmessage_btn_lowerunit);
		btn_sendtolowerparent=(Button) findViewById(R.id.sendmessage_btn_lowerparent);
		btn_sendtolowerstudent=(Button) findViewById(R.id.sendmessage_btn_lowerstudent);
		layout_content=(LinearLayout) findViewById(R.id.sendmessage_layout_content);
		layout_higher=(HorizontalScrollView) findViewById(R.id.sendmessage_layout_higher);
		layout_lower=(HorizontalScrollView) findViewById(R.id.sendmessage_layout_lower);
		layout_reciver=(LinearLayout) findViewById(R.id.sendmessage_layout_reciverlist);
		layout_lowerunit=(LinearLayout) findViewById(R.id.sendmessage_layout_lowerunit);
		btn_currunit=(Button) findViewById(R.id.sendmessage_btn_currunit);//当前单位
		layout_higherunit=(LinearLayout) findViewById(R.id.sendmessage_layout_higherunit);//上级单位
		edt_content=(IEditText) findViewById(R.id.sendmessage_edt_content);//内容框
		cb_shotmsg=(CheckBox) findViewById(R.id.sendmessage_checkBox_shotmsg);//是否是短信
		btn_toall=(Button) findViewById(R.id.sendmessage_btn_toall);//全选
		btn_invert=(Button) findViewById(R.id.sendmessage_btn_invert);//反选
		btn_send=(Button) findViewById(R.id.sendmessage_btn_send);//发布
		WorkSendMessageActivityController.getInstance().setContext(this);
		VisitPublicHttp.getInstance().setContext(this);
		
		curunitid = sp.getString("TabIDStr", "");
		
		setActionBarTitle(R.string.message_direct_train);
		
		layout_content.setFocusable(true);
		layout_content.setFocusableInTouchMode(true);
		layout_content.requestFocus();
		layout_unit.setVisibility(8);
		cb_shotmsg.setChecked(true);
		
//		if (mCache.getAsString("SMSrevicerdata") != null && (sp.getInt("UnitID", 0)+"").equals(mCache.getAsString("SMSUnitID"))&&(sp.getString("JiaoBaoHao", "")).equals(mCache.getAsString("SMSJiaoBaoHao"))){
//			VisitPublicHttp.getInstance().changeCurUnit(false);
//			initRevicerList();
//		}else{
		VisitPublicHttp.getInstance().changeCurUnit(true);
//		}
		btn_toall.setOnClickListener(this);
		btn_invert.setOnClickListener(this);
		btn_currunit.setOnClickListener(this);
		btn_send.setOnClickListener(this);
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
			e.printStackTrace();
			VisitPublicHttp.getInstance().getRoleIdentity();
		}
	}
	@SuppressLint("HandlerLeak") 
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
	@SuppressWarnings("unchecked")	
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
			break;
		case R.id.sendmessage_btn_send:
			str_msgcontent = edt_content.getTextString();
			if ("".equals(str_msgcontent)) {
				ToastUtil.showMessage(mContext, R.string.content_cannot_empty);
			}else{
				for (int i = 0; i < grids.size(); i++) {
					GridViewAdapter adapter = (GridViewAdapter) grids.get(i).getAdapter();
					Map<Integer,List<SMSTreeUnitID>> list = adapter.getCheckedSMS();
					if (list.get(1) != null) {
						reciverSMSListTea = list.get(1);
					}
					if (list.get(2) != null) {
						reciverSMSListPar = list.get(2);
					}
					if (list.get(3) != null) {
						reciverSMSListStu = list.get(3);
					}
					
				}
				if(reciverSMSListTea.size() !=0 ||reciverSMSListPar.size() !=0 ||reciverSMSListStu.size() !=0 ){
					int count = reciverSMSListTea.size()+reciverSMSListPar.size()+reciverSMSListStu.size();
					dialog_send(count);
				}else{
					ToastUtil.showMessage(mContext,R.string.please_choose_receiver);
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
	@SuppressWarnings("unchecked")
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
		builder.setMessage("确定要向"+sp.getString("UnitName", "")+"发送短信吗?");
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
		//int[] MemUnit, int[] GenUnit, int[] StuUnit
		for (int i = 0; i < reciverSMSListTea.size(); i++) {
			params.addBodyParameter("MemUnit",reciverSMSListTea.get(i).getId()+"");
		}
		for (int i = 0; i < reciverSMSListPar.size(); i++) {
			params.addBodyParameter("GenUnit",reciverSMSListPar.get(i).getId()+"");
		}
		for (int i = 0; i < reciverSMSListStu.size(); i++) {
			params.addBodyParameter("StuUnit",reciverSMSListStu.get(i).getId()+"");
		}
		params.addBodyParameter("unitclassgenCount","0");
		params.addBodyParameter("grsms",true+"");
		params.addBodyParameter("talkcontent", str_msgcontent);
		if (cb_shotmsg.isChecked()) {
			params.addBodyParameter("SMSFlag","1");
		}else{
			params.addBodyParameter("SMSFlag","0");
		}
		params.addBodyParameter("curunitid",curunitid);
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
				WorkSendMessageActivityController.getInstance().SMSCommIndex();
			}
			break;
		case Constant.msgcenter_work_SMSCommIndex:
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
	private void initRevicerList() {
		GetSMS getSMS = GsonUtil.GsonToObject(mCache.getAsString("SMSrevicerdata"), GetSMS.class);
		
		List<SMSTreeUnitID> smsList = getSMS.getList();
		
		creatSMSReciver(smsList,"SMSrevicerdata");
	}
	String[] SMSName={"单位人员(老师)","家长","学生"};
	/**
	 * 创建短信接收人列表
	 * @param smsList
	 */
	@SuppressWarnings("unchecked")
	private void creatSMSReciver(List<SMSTreeUnitID> smsList,String viewName) {
		btn_send.setEnabled(true);
		boolean haveView = false;
		for (int i = 0; i < viewNames.size(); i++) {
			ArrayList<Object> tag = (ArrayList<Object>) viewNames.get(i).getTag();
			if (viewName.equals((String)tag.get(0))) {
				haveView = true;
				viewNames.get(i).setVisibility(0);
			}else{
				viewNames.get(i).setVisibility(8);
			}
		}
		if (!haveView) {
			if (smsList != null) {
				LinearLayout layout = new LinearLayout(mContext);
				layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				layout.setBackgroundResource(R.drawable.edittext_bg);
				layout.setOrientation(LinearLayout.VERTICAL);
				ArrayList<Object> adapterList = new ArrayList<Object>();
				
				for (int i = 0; i < 3; i++) {
					View view = LayoutInflater.from(this).inflate(R.layout.include_worksend_grouptitle, null);
					TextView name = (TextView) view.findViewById(R.id.sendmessage_group_name);
					Button toall = (Button) view.findViewById(R.id.sendmessage_group_toall);
					Button invert = (Button) view.findViewById(R.id.sendmessage_group_invert);
					
					name.setText(SMSName[i]);
					CusGridView gridview = new CusGridView(mContext, null);
					gridview.setNumColumns(1);
					GridViewAdapter adapter = new GridViewAdapter(mContext);
					adapter.setSMSReciver(smsList,i+1);
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
				tag.add(0,viewName);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub_search = menu.addSubMenu(R.string.change).setIcon(R.drawable.btn_right_swich);
        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				choseUnit();
				return false;
			}
		});
        
        return super.onCreateOptionsMenu(menu);
    }
}
