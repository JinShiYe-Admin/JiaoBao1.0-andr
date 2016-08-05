package com.jsy_jiaobao.main.affairs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.OpenFiles;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.customview.XListViewFooter;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.Attlist;
import com.jsy_jiaobao.po.personal.CommMsgTrunToInfo;
import com.jsy_jiaobao.po.personal.FeeBack;
import com.jsy_jiaobao.po.personal.GetAttList;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.jsy_jiaobao.po.sys.GetUserClass;
import com.jsy_jiaobao.po.sys.UserClass;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *                   _ooOoo_
 *                  o8888888o
 *                  88" . "88
 *                  (| -_- |)
 *                  O\  =  /O
 *               ____/`---'\____
 *             .'  \\|     |//  `.
 *            /  \\|||  :  |||//  \
 *           /  _||||| -:- |||||-  \
 *           |   | \\\  -  /// |   |
 *           | \_|  ''\---/''  |   |
 *           \  .-\__  `-`  ___/-. /
 *         ___`. .'  /--.--\  `. . __
 *      ."" '<  `.___\_<|>_/___.'  >'"".
 *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *     \  \ `-.   \_ __\ /__ _/   .-` /  /
 *======`-.____`-.___\_____/___.-`____.-'======
 *                   `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *         		    佛祖保佑       永无BUG
 */
public class WorkDetailsActivity extends BaseActivity implements PublicMethod, OnClickListener{

	private CusListView listView;
	private CusGridView gridView;

	private LinearLayout layout_attlist;

	private TextView tv_author;
	private TextView tv_content;
	private TextView tv_time;

	private TextView tv_relay;//转发按钮

	private IEditText edt_replycontent;//回复
	private WorkDetailsListAdapter listAdapter;
	private GridViewAdapter gridAdapter;
	private Context mContext;
	private SharedPreferences sp_sys;
	private XListViewFooter mFooterView;
	
	private String TabIDStr;
	private String JiaoBaoHao;
	private String MsgTabIDStr;
	private int currPage = 1;
	private boolean havemore = true;
	private List<FeeBack> FeebackList = new ArrayList<>();
	private String str_reylycontent;
	private String MsgRecDate;
	private String str_msgcontent;
	private List<Attlist> attList;//附件列表
	private boolean unitAdminTrun = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			TabIDStr = savedInstanceState.getString("TabIDStr");
			JiaoBaoHao = savedInstanceState.getString("JiaoBaoHao");
			MsgTabIDStr = savedInstanceState.getString("MsgTabIDStr");
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
				TabIDStr = bundle.getString("TabIDStr");
				JiaoBaoHao = bundle.getString("JiaoBaoHao");
				MsgTabIDStr = bundle.getString("MsgTabIDStr");
			}
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("TabIDStr", TabIDStr);
		outState.putString("MsgRecDate", MsgRecDate);
		outState.putString("JiaoBaoHao", JiaoBaoHao);
	}
	@Override
	public void initViews() {
		setContentLayout(R.layout.ui_msgdetails);
		listView=(CusListView) findViewById(R.id.msgdetails_listview);
		gridView=(CusGridView) findViewById(R.id.msgdetails_gridview);
		LinearLayout layout_details;
		layout_details=(LinearLayout) findViewById(R.id.msgdetails_layout_details);
		layout_attlist=(LinearLayout) findViewById(R.id.msgdetails_layout_attlist);
		ImageView img_author;
		img_author=(ImageView) findViewById(R.id.msgdetails_img_photo);
		tv_author=(TextView) findViewById(R.id.msgdetails_tv_author);
		tv_content=(TextView) findViewById(R.id.msgdetails_tv_content);
		tv_time=(TextView) findViewById(R.id.msgdetails_tv_time);
		TextView tv_receiver;//接收人列表
		tv_receiver=(TextView) findViewById(R.id.msgdetails_tv_receiver);
		tv_relay=(TextView) findViewById(R.id.msgdetails_tv_relay);
		Button btn_send;//回复
		btn_send=(Button) findViewById(R.id.msgdetails_btn_send);
		edt_replycontent=(IEditText) findViewById(R.id.msgdetails_edt_mywords);
		ViewUtils.inject(this);
		mContext = this;
		LoginActivityController.getInstance().setContext(this);
		BitmapUtils bitmapUtils;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.photo);
		bitmapUtils.configDefaultLoadingImage(R.drawable.photo);
		sp_sys = getSharedPreferences(Constant.SP_TB_SYS, MODE_PRIVATE);
		
		bitmapUtils.display(img_author, ACache.get(mContext.getApplicationContext()).getAsString("MainUrl")+ConstantUrl.photoURL+"?AccID="+JiaoBaoHao);
		listView.setDivider(null);
		layout_details.setFocusable(true);
		layout_details.setFocusableInTouchMode(true);
		layout_details.requestFocus();
		
		setActionBarTitle(R.string.message_details);
		
		tv_relay.setVisibility(View.GONE);
		mFooterView = new XListViewFooter(mContext);
		listView.addFooterView(mFooterView);
		listAdapter = new WorkDetailsListAdapter(this);
		listView.setAdapter(listAdapter);
		
		gridAdapter = new GridViewAdapter(mContext);
		gridView.setAdapter(gridAdapter);
//		if (TextUtils.isEmpty(MsgTabIDStr)) {
//			MarkRead();
//		}
		tv_receiver.setOnClickListener(this);
		tv_relay.setOnClickListener(this);
		btn_send.setOnClickListener(this);
	}
	@Override
	public void initListener() {
	
		mFooterView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (havemore) {
					mFooterView.setState(XListViewFooter.STATE_LOADING);
					mFooterView.setClickable(false);
					currPage++;
					initDeatilsData();
				}else{
					ToastUtil.showMessage(mContext, R.string.no_more);
				}
			}
		});
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.msgdetails_tv_receiver:
			if (gridView.getVisibility() == View.VISIBLE) {
				gridView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}else{
				gridView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
			break;
		case R.id.msgdetails_tv_relay:
			if (unitAdminTrun) {
				//管理员，发给下级管理员  下发通知
				Intent intent = new Intent(mContext,WorkSendToLowActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tomsgid", TabIDStr);
				bundle.putString("content", tv_content.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}else{
				//班主任，群发给班里
				Intent intent = new Intent(mContext,WorkSendToAllActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("tomsgid", TabIDStr);
				bundle.putString("content", tv_content.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
		case R.id.msgdetails_btn_send:
			str_reylycontent = edt_replycontent.getTextString();
			if ("".equals(str_reylycontent)) {
				ToastUtil.showMessage(mContext, R.string.please_input_reply_content);
			}else{
				reply();
			}
			break;
		}
	}

	ArrayList<CommMsgTrunToInfo> trunToInfoList = new ArrayList<>();
	@Override
	public void initDeatilsData() {
		DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("getfb","true");//
		params.addBodyParameter("numPerPage","20");//
		params.addBodyParameter("pageNum",currPage+"");//
		if (!TextUtils.isEmpty(MsgTabIDStr)) {
			params.addBodyParameter("rid",TabIDStr);//
			params.addBodyParameter("uid",MsgTabIDStr);//
		}else{
			params.addBodyParameter("uid",TabIDStr);//
		}
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, ShowDetail,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (!isFinishing()) {
					ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect)+"r1001");
					DialogUtil.getInstance().cannleDialog();
					finish();
				}
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (!isFinishing()) {
					DialogUtil.getInstance().cannleDialog();
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);
						String ResultCode = jsonObj.getString("ResultCode");
						
						if ("0".equals(ResultCode)) {
							String data = Des.decrypt(jsonObj.getString("Data"), sp_sys.getString("ClientKey", ""));
							
							GetWorkMsgDetails getWorkDetails = GsonUtil.GsonToObject(data, GetWorkMsgDetails.class);
							MsgRecDate = getWorkDetails.getModel().getRecDate();
							
							String trun = getWorkDetails.getModel().getTrunToList();
							ArrayList<CommMsgTrunToInfo> CommMsgTrunToInfoList = GsonUtil.GsonToList(trun, new TypeToken<ArrayList<CommMsgTrunToInfo>>() {}.getType());
							
							if (CommMsgTrunToInfoList != null && CommMsgTrunToInfoList.size()>0) {
								for (CommMsgTrunToInfo item : CommMsgTrunToInfoList) {
									if (sp.getString("JiaoBaoHao", "").equals(String.valueOf(item.getJiaoBaoHao()))) {
										trunToInfoList.add(item);
									}
								}
							}
							String str = "";
							if (trunToInfoList.size()>0) {
								for (int ii = 0; ii < trunToInfoList.size(); ii++) {
									CommMsgTrunToInfo commMsgTrunToInfo = trunToInfoList.get(ii);
									if (commMsgTrunToInfo != null) {
										if (commMsgTrunToInfo.getUnitType() == 1) {
											//是否管理员，0不是，1是单位管理员2班主任，3单位管理员同时也是班主任
											if (sp.getInt("isAdmin", 0) == 1||sp.getInt("isAdmin", 0) == 3) {
												tv_relay.setVisibility(View.VISIBLE);
												unitAdminTrun = true;
												if ("tomem".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"人员;";
												}else if ("togen".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"家长;";
												}else if ("tostu".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"学生;";
												}
											}
										}else if (commMsgTrunToInfo.getUnitType() == 2) {
											unitAdminTrun = false;
											if (sp.getInt("isAdmin", 0) == 2||sp.getInt("isAdmin", 0) == 3) {
												tv_relay.setVisibility(View.VISIBLE);
												if ("tomem".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"人员;";
												}else if ("togen".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"家长;";
												}else if ("tostu".equals(commMsgTrunToInfo.getWho())) {
													str = str+"转"+sp.getString("UnitName", "")+"学生;";
												}
//												WorkDetailsActivityController.getInstance().getmyUserClass(sp.getInt("UnitID",0));
											}
										}
//										for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
//											UserIdentity userIdentity = Constant.listUserIdentity.get(i);
//											if (commMsgTrunToInfo.getUnitType() == 1) {//单位
//												for (int j = 0; j < userIdentity.getUserUnits().size(); j++) {
//													for (int j1 = 0; j1 < userIdentity.getUserUnits().size(); j1++) {
//														UserUnit userUnit = userIdentity.getUserUnits().get(j1);
//														WorkDetailsActivityController.getInstance().getMySubUnitInfo(userUnit);
//													}
//												}
//											}else if (commMsgTrunToInfo.getUnitType() == 2) {//班级
//												for (int j = 0; j < userIdentity.getUserUnits().size(); j++) {
//													UserUnit userUnit = userIdentity.getUserUnits().get(j);
//													WorkDetailsActivityController.getInstance().getmyUserClass(userUnit);
//												}
//											}
//										}
									}
								}
								try {
									tv_relay.setText(str.substring(0,str.length()-1));
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							List<FeeBack> list1 = getWorkDetails.getFeebackList();
							FeebackList.addAll(list1);
							havemore=list1.size()>=20;
							if (currPage == 1) {
								String str_attlist = "{\"AttList\":"+getWorkDetails.getModel().getAttList()+"}";
								GetAttList getAttList = GsonUtil.GsonToObject(str_attlist, GetAttList.class);
								attList = getAttList.getAttList();
								if (attList != null) {
									for (int i = 0; i < attList.size(); i++) {
										Attlist att = attList.get(i);
										TextView tv = new TextView(mContext);
										tv.setTag(att);
										tv.setText("附件:"+att.getOrgFilename()+"("+att.getFileSize()+")");
										tv.setOnClickListener(onclickListener);
										tv.setPadding(0, 20, 0, 0);
										tv.setTextColor(getResources().getColor(R.color.topbar_bg));
										layout_attlist.addView(tv);
									}
								}
								JSONArray readerArray = new JSONArray(getWorkDetails.getModel().getReaderList());
								gridAdapter.setData(readerArray);
								gridAdapter.notifyDataSetChanged();
								tv_author.setText(getWorkDetails.getModel().getUserName());
								str_msgcontent = getWorkDetails.getModel().getMsgContent();
								tv_content.setText(str_msgcontent);
								tv_time.setText(getWorkDetails.getModel().getRecDate());
							}
						}else if("8".equals(ResultCode)){
							LoginActivityController.getInstance().helloService(mContext);
						} else {
							finish();
							ToastUtil.showMessage(mContext,jsonObj.getString("ResultDesc"));
						}
					} catch (Exception e) {
						finish();
						ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect)+"r1002");
					} 
					listAdapter.setData(FeebackList);
					listAdapter.notifyDataSetChanged();
					mFooterView.setState(XListViewFooter.STATE_NORMAL);
					mFooterView.setClickable(true);
				}
			}
		});
	}
	OnClickListener onclickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Attlist att = (Attlist) v.getTag();
			dialog_send(att);
		}
	};
	protected void dialog_send(final Attlist att) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.hint);
		builder.setIcon(android.R.drawable.ic_dialog_info).setMessage(getResources().getString(R.string.beSure_toLoad_enclosure)
				+att.getOrgFilename()+"？");
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				downloadAtt(att);
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


	ProgressDialog m_pDialog ;

	/**
	 * 下载附件
	 * @param att att
     */
	private void downloadAtt(final Attlist att) {

		final String filePath = JSYApplication.getInstance().FILE_PATH+att.getOrgFilename();
		HttpHandler handler = HttpUtil.getInstanceNew().download(att.getDlurl(),filePath,true, new RequestCallBack<File>() {


			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				m_pDialog.dismiss();
				BaseUtils.shortToast(getApplicationContext(), R.string.save_success);
				File currentPath = new File(filePath);
				if(currentPath.length()>0&&currentPath.isFile()){
                    String fileName = currentPath.toString();
                    Intent intent;
                    if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingImage))){
                        intent = OpenFiles.getImageFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingWebText))){
                        intent = OpenFiles.getHtmlFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPackage))){
                        intent = OpenFiles.getApkFileIntent(currentPath);
                        startActivity(intent);

                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingAudio))){
                        intent = OpenFiles.getAudioFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingVideo))){
                        intent = OpenFiles.getVideoFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingText))){
                        intent = OpenFiles.getTextFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPdf))){
                        intent = OpenFiles.getPdfFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingWord))){
                        intent = OpenFiles.getWordFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingExcel))){
                        intent = OpenFiles.getExcelFileIntent(currentPath);
                        startActivity(intent);
                    }else if(checkEndsWithInStringArray(fileName, getResources().
                            getStringArray(R.array.fileEndingPPT))){
                        intent = OpenFiles.getPPTFileIntent(currentPath);
                        startActivity(intent);
                    }else{
                    	BaseUtils.shortToast(getApplicationContext(), R.string.cannotOpen_please_installTheSoftware);
                    }
                }else{
                	BaseUtils.shortToast(getApplicationContext(), R.string.file_error);
                }
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				m_pDialog.dismiss();
				BaseUtils.shortToast(getApplicationContext(), R.string.load_failed);

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				try {
					m_pDialog.setProgress((int) (current*100/total));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				m_pDialog = new ProgressDialog(mContext);
				m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				m_pDialog.setTitle(R.string.load_progress);
				m_pDialog.setMax(100);
				m_pDialog.setIndeterminate(false);
				m_pDialog.setCancelable(false);
				m_pDialog.show();
			}

		});
	}
	private boolean checkEndsWithInStringArray(String checkItsEnd,String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	/**
	 * 回复本条消息
	 */
	private void reply() {
		DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",TabIDStr);//TabIDStr
		params.addBodyParameter("feebacktalkcontent",str_reylycontent);//回复内容
		params.addBodyParameter("MsgRecDate",MsgRecDate);//交流信息的RecDate
		
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, addfeeback,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (!isFinishing()) {
					ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect)+"r1001");
					DialogUtil.getInstance().cannleDialog();
				}
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (!isFinishing()) {
					DialogUtil.getInstance().cannleDialog();
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);
						String ResultCode = jsonObj.getString("ResultCode");
						
						if ("0".equals(ResultCode)) {
							List<FeeBack> turn = new ArrayList<>();
							FeeBack feeback =  new FeeBack();
							feeback.setUserName(getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE).getString("TrueName", ""));
							feeback.setFeeBackMsg(str_reylycontent);
							turn.add(feeback);
							turn.addAll(FeebackList);
							FeebackList.clear();
							FeebackList.addAll(turn);
							listAdapter.setData(FeebackList);
							listAdapter.notifyDataSetChanged();
							edt_replycontent.setText("");
							BaseUtils.hidepan(edt_replycontent);
//							String data = Des.decrypt(jsonObj.getString("Data"), sp_sys.getString("ClientKey", ""));
						} else if("8".equals(ResultCode)){
							startActivity(new Intent(mContext,LoginActivity.class));
							finish();
						}else {
							ToastUtil.showMessage(mContext,jsonObj.getString("ResultDesc"));
						}
						ToastUtil.showMessage(mContext,jsonObj.getString("ResultDesc"));
					} catch (Exception e) {
						ToastUtil.showMessage(mContext, getResources().getString(R.string.error_serverconnect)+"r1002");
					} 
				}
			}
		});
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
		String str = "";
		String name = "";
		switch (tag) {
		case Constant.msgcenter_notice_getMySubUnitInfo://下级单位
//			NoticeGetUnitInfo getMySubUnitInfo = (NoticeGetUnitInfo) list.get(1);
//			for (int i = 0; i < trunToInfoList.size(); i++) {
//				CommMsgTrunToInfo commMsgTrunToInfo = trunToInfoList.get(i);
//				for (int j = 0; j < getMySubUnitInfo.getList().size(); j++) {
//					UnitInfo uc = getMySubUnitInfo.getList().get(j);
//					if (uc.getTabID() == commMsgTrunToInfo.getUnitID()) {
//							
//						tv_relay.setVisibility(0);
//						UnitName = userUnit.getUnitName();
//						RoleIdentity = 2;
//						UnitID = userUnit.getUnitID();
//						name = uc.getUintName();
//						if ("tomem".equals(commMsgTrunToInfo.getWho())) {
//							str = str+"转"+name+";";
//						}else if ("togen".equals(commMsgTrunToInfo.getWho())) {
//							str = str+"转"+name+"家长;";
//						}else if ("tostu".equals(commMsgTrunToInfo.getWho())) {
//							str = str+"转"+name+"学生;";
//						}
//					}else{
//						if (tv_relay.getVisibility() == 8) {
//							tv_relay.setVisibility(8);
//						}
//					}
//				}
//			}
//			
//			tv_relay.setText(str.substring(0,str.length()-1));
			break;
		case Constant.msgcenter_train_getmyUserClass://获取教师关联班级
			GetUserClass getUnitClass = (GetUserClass) list.get(1);
			for (int i = 0; i < trunToInfoList.size(); i++) {
				CommMsgTrunToInfo commMsgTrunToInfo = trunToInfoList.get(i);
				for (int j = 0; j < getUnitClass.getList().size(); j++) {
					UserClass uc = getUnitClass.getList().get(j);
					if (uc.getClassID() == commMsgTrunToInfo.getUnitID()) {
							
						tv_relay.setVisibility(View.VISIBLE);

						name = uc.getClassName();
						if ("tomem".equals(commMsgTrunToInfo.getWho())) {
							str = str+"转"+name+";";
						}else if ("togen".equals(commMsgTrunToInfo.getWho())) {
							str = str+"转"+name+"家长;";
						}else if ("tostu".equals(commMsgTrunToInfo.getWho())) {
							str = str+"转"+name+"学生;";
						}
					}else{
						if (tv_relay.getVisibility() == View.GONE) {
							tv_relay.setVisibility(View.GONE);
						}
					}
				}
			}
			
			tv_relay.setText(str.substring(0,str.length()-1));
			break;
		default:
			
			break;
		}
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
