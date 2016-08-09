package com.jsy_jiaobao.main.system;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.workol.Constants;
import com.jsy_jiaobao.po.leave.ApproveList;
import com.jsy_jiaobao.po.leave.ApproveListStd;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.leave.LevelNote;
import com.jsy_jiaobao.po.leave.LevelNoteStd;
import com.jsy_jiaobao.po.leave.UnitLeave;
import com.jsy_jiaobao.po.leave.UnitLeaveGson;
import com.jsy_jiaobao.po.sys.GenInfo;
import com.jsy_jiaobao.po.sys.StuInfo;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jsy.xuezhuli.utils.Constant.listUserIdentity;

/**
 * 登陆访问服务器
 */
public class VisitPublicHttp implements ConstantUrl {
	private static VisitPublicHttp instance;
	private Activity mContext;
	private ProgressDialog dialog;
	private SharedPreferences user_sp, sys_sp;
	private Editor editor;
	private boolean sendmsg;

	public static synchronized  VisitPublicHttp getInstance() {
		if (instance == null) {
			instance = new VisitPublicHttp();
		}
		return instance;
	}

	public VisitPublicHttp setContext(Activity pActivity) {
		this.mContext = pActivity;
		LoginActivityController.getInstance().setContext(pActivity);
		dialog = BaseUtils.showDialog(mContext, R.string.changing_unit_waiting);
		dialog.setCanceledOnTouchOutside(false);
		user_sp = mContext.getSharedPreferences(Constant.SP_TB_USER,
				Context.MODE_PRIVATE);
		sys_sp = mContext.getSharedPreferences(Constant.SP_TB_SYS,
				Context.MODE_PRIVATE);
		editor = user_sp.edit();
		return this;
	}

	/**
	 * 切换单位
	 */
	public void changeCurUnit(boolean b) {
		this.sendmsg = b;
		try {
			if (user_sp.getInt("UnitID", 0) != 0) {
				if (mContext != null && sendmsg) {
					DialogUtil.getInstance().getDialog(
							mContext,
							mContext.getResources().getString(
									R.string.public_loading));
				}
				RequestParams params = new RequestParams();
				params.addBodyParameter("UID", user_sp.getInt("UnitID", 0) + "");
				params.addBodyParameter("uType",
						user_sp.getInt("RoleIdentity", 0) + "");
				HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
						changeCurUnit, params, new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								if (mContext != null) {
									DialogUtil.getInstance().cannleDialog();
									BaseUtils
											.shortToast(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.error_serverconnect));
								}
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								if (mContext != null) {
									try {
										JSONObject jsonObj = new JSONObject(
												arg0.result);
										String ResultCode = jsonObj
												.getString("ResultCode");

										if ("0".equals(ResultCode)) {
											httpGetUserInfo();
										} else if ("8".equals(ResultCode)) {
											LoginActivityController
													.getInstance()
													.helloService(mContext);
										} else {
											DialogUtil.getInstance()
													.cannleDialog();
											BaseUtils
													.shortToast(
															mContext,
															jsonObj.getString("ResultDesc"));
										}
									} catch (Exception e) {
										DialogUtil.getInstance().cannleDialog();
										BaseUtils
												.shortToast(
														mContext,
														mContext.getResources()
																.getString(
																		R.string.error_swichunit)
																+ "r1002");
									}
								}
							}
						});
			} else {
				editor.putInt("UserID", 0);
				editor.putInt("UserType", 1);
				editor.putInt("isAdmin", 0);// 是否管理员，0不是，1是单位管理员2班主任，3单位管理员同时也是班主任
				editor.putString("UserName", "");
				editor.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取身份信息
	 */
	public void getRoleIdentity() {
		try {
			dialog = BaseUtils.showDialog(mContext,
					R.string.loading_roleInfo_waiting);
			dialog.show();
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
					user_getRoleIdentity, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dialog.dismiss();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							dialog.dismiss();
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");
								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(
											jsonObj.getString("Data"),
											sys_sp.getString("ClientKey", ""));
									listUserIdentity = GsonUtil
											.GsonToList(
													data,
													new TypeToken<ArrayList<UserIdentity>>() {
													}.getType());
									if (user_sp.getInt("UnitID", 0) == 0) {
										try {
											if (listUserIdentity
													.get(listUserIdentity
															.size() - 1)
													.getRoleIdentity() == 5) {
												listUserIdentity
														.remove(listUserIdentity
																.size() - 1);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										editor.putInt("RoleIdentity", 1);
										editor.putInt("UnitID", 0);
										editor.putInt("UnitType", 1);
										editor.putString("UnitName", "");
										editor.putString("TabIDStr", "");
										editor.commit();
										if (listUserIdentity != null
												&& listUserIdentity.size() > 0) {
											if (listUserIdentity.get(0)
													.getRoleIdentity() == 3) {
												List<UserUnit> units = listUserIdentity
														.get(0).getUserUnits();
												if (units == null
														|| units.size() == 0) {
													List<UserClass> UserClasses = listUserIdentity
															.get(0)
															.getUserClasses();
													if (UserClasses != null
															&& UserClasses
																	.size() > 0) {
														editor.putInt(
																"RoleIdentity",
																3);
														editor.putInt(
																"UnitID",
																UserClasses
																		.get(0)
																		.getSchoolID());
														editor.putInt(
																"UnitType", 2);
														editor.putString(
																"UnitName",
																UserClasses
																		.get(0)
																		.getClassName());
														editor.putString(
																"TabIDStr",
																UserClasses
																		.get(0)
																		.getTabIDStr());
														editor.commit();
													}
												}
											} else if (listUserIdentity.get(0)
													.getRoleIdentity() == 4) {
												List<UserUnit> units = listUserIdentity
														.get(0).getUserUnits();
												if (units == null
														|| units.size() == 0) {
													List<UserClass> UserClasses = listUserIdentity
															.get(0)
															.getUserClasses();
													if (UserClasses != null
															&& UserClasses
																	.size() > 0) {
														editor.putInt(
																"RoleIdentity",
																4);
														editor.putInt(
																"UnitID",
																UserClasses
																		.get(0)
																		.getSchoolID());
														editor.putInt(
																"UnitType", 2);
														editor.putString(
																"UnitName",
																UserClasses
																		.get(0)
																		.getClassName());
														editor.putString(
																"TabIDStr",
																UserClasses
																		.get(0)
																		.getTabIDStr());
														editor.commit();
													}
												}
											} else {
												int uid = 0;
												x: for (int i = 0; i < listUserIdentity
														.size(); i++) {
													UserIdentity userIdentity = listUserIdentity
															.get(i);
													uid = userIdentity
															.getDefaultUnitId();

													for (int j = 0; j < userIdentity
															.getUserUnits()
															.size(); j++) {
														UserUnit userUnit = userIdentity
																.getUserUnits()
																.get(j);
														if (uid == userUnit
																.getUnitID()
																|| uid == 0) {
															editor.putInt(
																	"RoleIdentity",
																	userIdentity
																			.getRoleIdentity());
															editor.putInt(
																	"UnitID",
																	userUnit.getUnitID());
															editor.putInt(
																	"UnitType",
																	userUnit.getUnitType());
															editor.putString(
																	"UnitName",
																	userUnit.getUnitName());
															editor.putString(
																	"TabIDStr",
																	userUnit.getTabIDStr());
															editor.commit();
															break x;
														}
													}
												}

											}
										}
										RequestParams params = new RequestParams();
										params.addBodyParameter("UID",
												user_sp.getInt("UnitID", 0)
														+ "");
										params.addBodyParameter(
												"uType",
												user_sp.getInt("RoleIdentity",
														0) + "");
										HttpUtil.getInstance().send(
												HttpRequest.HttpMethod.POST,
												changeCurUnit, params, null);

										httpGetUserInfo();
									}
									changeGenStuUserName();
								} else {
									BaseUtils
											.shortToast(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.get_user_identity_)
															+ ResultCode
															+ "\n"
															+ jsonObj
																	.getString("ResultDesc"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户所在单位的详情
	 */
	private void httpGetUserInfo() {
		try {
			RequestParams params = new RequestParams();
			params.addBodyParameter("AccID",
					user_sp.getString("JiaoBaoHao", ""));
			params.addBodyParameter("UID", user_sp.getInt("UnitID", 0) + "");
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
					user_getUserInfo, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							if (mContext != null) {
								try {
									DialogUtil.getInstance().cannleDialog();
								} catch (Exception e) {
									e.printStackTrace();
								}
								BaseUtils.shortToast(
										mContext,
										mContext.getResources().getString(
												R.string.error_serverconnect));
							}
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							if (mContext != null) {
								try {
									DialogUtil.getInstance().cannleDialog();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								try {
									JSONObject jsonObj = new JSONObject(
											arg0.result);
									String ResultCode = jsonObj
											.getString("ResultCode");
									editor.putInt("UserID", 0);
									editor.putInt("UserType", 1);
									editor.putInt("isAdmin", 0);// 是否管理员，0不是，1是单位管理员2班主任，3单位管理员同时也是班主任
									editor.putString("UserName", "");
									editor.commit();
									if ("0".equals(ResultCode)) {
										String data = Des.decrypt(jsonObj
												.getString("Data"), sys_sp
												.getString("ClientKey", ""));
										if (data != null
												&& !data.equals("null")) {

											JSONObject jsonData = new JSONObject(
													data);
											editor.putInt("UserID",
													jsonData.getInt("UserID"));
											editor.putInt("UserType",
													jsonData.getInt("UserType"));
											editor.putInt("isAdmin",
													jsonData.getInt("isAdmin"));// 是否管理员，0不是，1是单位管理员2班主任，3单位管理员同时也是班主任
											editor.putString(
													"UserName",
													jsonData.getString("UserName"));
											editor.commit();
										}
										httpGetLeaveSetting();

										changeGenStuUserName();
									} else if ("8".equals(ResultCode)) {
										LoginActivityController.getInstance()
												.helloService(mContext);
									} else {
										BaseUtils.shortToast(mContext,
												jsonObj.getString("ResultDesc"));
									}
								} catch (Exception e) {
										e.printStackTrace();
								}
								ArrayList<Object> list = new ArrayList<>();
								list.add(Constant.msgcenter_work_change);
								list.add(sendmsg);
								EventBusUtil.post(list);
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 如果是学生或者家长 获取学生或家长信息
	 */
	private void changeGenStuUserName() {
		int role = user_sp.getInt("RoleIdentity", 1);
		if (role == 3) {
			httpGetGenInfo();
		} else if (role == 4) {
			httpGetStuInfo();
		}
	}

	/**
	 * 获取学生嘻嘻你
	 */
	public void httpGetStuInfo() {
		String jiaobaohao = user_sp.getString("JiaoBaoHao", "");
		if (!"".equals(jiaobaohao)) {
			if (Constant.listUserIdentity != null) {
				for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
					UserIdentity identity = Constant.listUserIdentity.get(i);
					if (identity.getRoleIdentity() == 4) {
						List<UserClass> list = identity.getUserClasses();
						if (list != null && list.size() > 0) {
							UserClass userClass = list.get(0);
							getStuInfo(jiaobaohao, userClass.getClassID());
						}
					}
				}
			}
		}
	}

	/**
	 * 获取学生信息，接口 和返回值
	 */
	private void getStuInfo(String jiaobaohao, int classID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AccID", jiaobaohao);
		params.addBodyParameter("UID", String.valueOf(classID));
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getStuInfo,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (mContext != null) {
							try {
								DialogUtil.getInstance().cannleDialog();
							} catch (Exception e) {
								e.printStackTrace();
							}
							BaseUtils.shortToast(
									mContext,
									mContext.getResources().getString(
											R.string.error_serverconnect));
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (mContext != null) {
							try {
								DialogUtil.getInstance().cannleDialog();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							StuInfo stuInfo;
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");
								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(
											jsonObj.getString("Data"),
											sys_sp.getString("ClientKey", ""));
									stuInfo = GsonUtil.GsonToObject(data,
											StuInfo.class);
									editor.putString("UserName",
											stuInfo.getStdName());
									editor.commit();

								} else if ("8".equals(ResultCode)) {
									LoginActivityController.getInstance()
											.helloService(mContext);
								} else {
									BaseUtils.shortToast(mContext,
											jsonObj.getString("ResultDesc"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							ArrayList<Object> list = new ArrayList<Object>();
							list.add(Constants.WORKOL_getStuInfo);
							EventBusUtil.post(list);
						}
					}
				});
	}

	/**
	 * 获取家长信息
	 */
	public void httpGetGenInfo() {
		String jiaobaohao = user_sp.getString("JiaoBaoHao", "");
		if (!"".equals(jiaobaohao)) {
			if (Constant.listUserIdentity != null) {
				for (int i = 0; i < Constant.listUserIdentity.size(); i++) {
					UserIdentity identity = Constant.listUserIdentity.get(i);
					if (identity.getRoleIdentity() == 3) {
						List<UserClass> list = identity.getUserClasses();
						if (list != null) {
							for (int j = 0; j < list.size(); j++) {
								UserClass userClass = list.get(j);
								getGenInfo(jiaobaohao, userClass.getClassID());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获取家长信息接口和返回值
	 */
	private void getGenInfo(String jiaobaohao, int UID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AccID", jiaobaohao);
		params.addBodyParameter("UID", String.valueOf(UID));
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getGenInfo,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (mContext != null) {
							try {
								DialogUtil.getInstance().cannleDialog();
							} catch (Exception e) {
								e.printStackTrace();
							}
							BaseUtils.shortToast(
									mContext,
									mContext.getResources().getString(
											R.string.error_serverconnect));
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (mContext != null) {
							try {
								DialogUtil.getInstance().cannleDialog();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							GenInfo genInfo;
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");
								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(
											jsonObj.getString("Data"),
											sys_sp.getString("ClientKey", ""));
									genInfo = GsonUtil.GsonToObject(data,
											GenInfo.class);
									// 判断是否为当前单位内的学生
									if (genInfo.getClassName().equals(
											user_sp.getString("UnitName", ""))) {
										editor.putString("UserName",
												genInfo.getStdName() + "的家长");
									}
									editor.commit();

								} else if ("8".equals(ResultCode)) {
									LoginActivityController.getInstance()
											.helloService(mContext);
								} else {
									BaseUtils.shortToast(mContext,
											jsonObj.getString("ResultDesc"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							ArrayList<Object> list = new ArrayList<>();
							list.add(Constants.WORKOL_getGenInfo);
							EventBusUtil.post(list);
						}
					}
				});
	}

	/**
	 * 获取用户所在单位的详情
	 */
	public void httpGetLeaveSetting() {
		try {
			/**
			 * 取本单位的请假信息 客户端通过本接口获取单位的基础信息数据。是否启用学生请假系统,学生请假审核级数（1-5级）
			 * 学生请假的各级审核流程的名称 son ApproveListStd 当前帐户在学生请假各个审批流程中的权限
			 */
			RequestParams params = new RequestParams();
			params.addBodyParameter("unitId",
					String.valueOf(user_sp.getInt("UnitID", 0)));
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
					GetLeaveSetting, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							if (mContext != null) {
								try {
									DialogUtil.getInstance().cannleDialog();
								} catch (Exception e) {
									e.printStackTrace();
								}
								BaseUtils.shortToast(
										mContext,
										mContext.getResources().getString(
												R.string.error_serverconnect));
							}
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							if (mContext != null) {
								try {
									DialogUtil.getInstance().cannleDialog();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								try {
									JSONObject jsonObj = new JSONObject(
											arg0.result);
									String ResultCode = jsonObj
											.getString("ResultCode");
									editor.putBoolean("StatusStd", false);
									editor.putBoolean("Status", false);
									editor.putInt("ApproveLevelStd", 0);
									editor.putInt("ApproveLevel", 0);
									editor.putBoolean("ApproveListStdA", false);
									editor.putBoolean("ApproveListStdB", false);
									editor.putBoolean("ApproveListStdC", false);
									editor.putBoolean("ApproveListStdD", false);
									editor.putBoolean("ApproveListStdE", false);
									editor.putBoolean("ApproveListA", false);
									editor.putBoolean("ApproveListB", false);
									editor.putBoolean("ApproveListC", false);
									editor.putBoolean("ApproveListD", false);
									editor.putBoolean("ApproveListE", false);
									editor.putString("LevelNoteStdA", null);
									editor.putString("LevelNoteStdB", null);
									editor.putString("LevelNoteStdC", null);
									editor.putString("LevelNoteStdD", null);
									editor.putString("LevelNoteStdE", null);
									editor.putString("LevelNoteA", null);
									editor.putString("LevelNoteB", null);
									editor.putString("LevelNoteC", null);
									editor.putString("LevelNoteD", null);
									editor.putString("LevelNoteE", null);
									editor.putBoolean("GateGuardList", false);
									editor.commit();
									if ("0".equals(ResultCode)) {
										Log.i("Data", jsonObj.getString("Data"));
										UnitLeaveGson list2 = GsonUtil
												.GsonToObject(jsonObj
														.getString("Data"),
														UnitLeaveGson.class);
										LevelNoteStd LevelNoteStd = GsonUtil.GsonToObject(
												list2.getLevelNoteStd(),
												LevelNoteStd.class);
										ApproveListStd ApproveListStd = GsonUtil
												.GsonToObject(list2
														.getApproveListStd(),
														ApproveListStd.class);
										LevelNote LevelNote = GsonUtil
												.GsonToObject(
														list2.getLevelNote(),
														LevelNote.class);
										ApproveList ApproveList = GsonUtil
												.GsonToObject(
														list2.getApproveList(),
														ApproveList.class);
										UnitLeave leave = new UnitLeave();
										leave.setApproveLevel(list2
												.getApproveLevel());
										leave.setApproveLevelStd(list2
												.getApproveLevelStd());
										leave.setApproveList(ApproveList);
										leave.setApproveListStd(ApproveListStd);
										leave.setGateGuardList(list2
												.isGateGuardList());
										leave.setLevelNote(LevelNote);
										leave.setLevelNoteStd(LevelNoteStd);
										leave.setStatus(list2.isStatus());
										leave.setStatusStd(list2.isStatusStd());
										editor.putBoolean("StatusStd",
												leave.isStatusStd());
										editor.putBoolean("Status",
												leave.isStatus());
										editor.putInt("ApproveLevelStd",
												leave.getApproveLevelStd());
										editor.putInt("ApproveLevel",
												leave.getApproveLevel());
										editor.putBoolean("ApproveListStdA",
												leave.getApproveListStd()
														.getA());
										editor.putBoolean("ApproveListStdB",
												leave.getApproveListStd()
														.getB());
										editor.putBoolean("ApproveListStdC",
												leave.getApproveListStd()
														.getC());
										editor.putBoolean("ApproveListStdD",
												leave.getApproveListStd()
														.getD());
										editor.putBoolean("ApproveListStdE",
												leave.getApproveListStd()
														.getE());
										editor.putBoolean("ApproveListA", leave
												.getApproveList().getA());
										editor.putBoolean("ApproveListB", leave
												.getApproveList().getB());
										editor.putBoolean("ApproveListC", leave
												.getApproveList().getC());
										editor.putBoolean("ApproveListD", leave
												.getApproveList().getD());
										editor.putBoolean("ApproveListE", leave
												.getApproveList().getE());
										editor.putString("LevelNoteStdA", leave
												.getLevelNoteStd().getA());
										editor.putString("LevelNoteStdB", leave
												.getLevelNoteStd().getB());
										editor.putString("LevelNoteStdC", leave
												.getLevelNoteStd().getC());
										editor.putString("LevelNoteStdD", leave
												.getLevelNoteStd().getD());
										editor.putString("LevelNoteStdE", leave
												.getLevelNoteStd().getE());
										editor.putString("LevelNoteA", leave
												.getLevelNote().getA());
										editor.putString("LevelNoteB", leave
												.getLevelNote().getB());
										editor.putString("LevelNoteC", leave
												.getLevelNote().getC());
										editor.putString("LevelNoteD", leave
												.getLevelNote().getD());
										editor.putString("LevelNoteE", leave
												.getLevelNote().getE());
										editor.putBoolean("GateGuardList",
												leave.isGateGuardList());
										editor.commit();

									} else if ("8".equals(ResultCode)) {
										LoginActivityController.getInstance()
												.helloService(mContext);
									} else {
										BaseUtils.shortToast(mContext,
												jsonObj.getString("ResultDesc"));
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								ArrayList<Object> list = new ArrayList<>();
								list.add(LeaveConstant.leave_GetLeaveSetting);
								list.add(sendmsg);
								EventBusUtil.post(list);
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}