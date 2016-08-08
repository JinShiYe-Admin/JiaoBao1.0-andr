package com.jsy_jiaobao.main.personalcenter;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.leave.LeaveConstant;
import com.jsy_jiaobao.po.personal.GetUnitSectionMessage;
import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
import com.jsy_jiaobao.po.personal.PublishPermission;
import com.jsy_jiaobao.po.sys.UserClass;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class MessageCenterActivityController implements ConstantUrl {

	private static MessageCenterActivityController instance;
	private Activity mcontext;

	public static synchronized  MessageCenterActivityController getInstance() {
		if (instance == null) {
			instance = new MessageCenterActivityController();
		}
		return instance;
	}

	public MessageCenterActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		return this;
	}

//	public void checkVersion() {
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_checkversion);
//		HttpUtil.getInstance().send(HttpRequest.HttpMethod.GET,
//				AndroidCurrVersionInfo, new RequestCallBack<String>() {
//
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						if (mcontext != null) {
//							LogUtils.e(arg1);
//							if (BaseUtils.isNetworkAvailable(mcontext)) {
//								ToastUtil.showMessage(mcontext,
//										R.string.phone_no_web);
//							}
//						}
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> arg0) {
//						ArrayList<Object> post = new ArrayList<Object>();
//						post.add(Constant.msgcenter_checkversion);
//						VersionInfo versionInfo = GsonUtil.GsonToObject(
//								arg0.result, VersionInfo.class);
//						post.add(versionInfo);
//						EventBusUtil.post(post);
//					}
//				});
//	}
//
//	/**
//	 * 取本单位的基础信息 客户端通过本接口获取单位的基础信息数据。基础信息数据包括名称，类型，区域，上级单位ID，栏目文章数量和文章更新数据等
//	 *
//	 * @param params
//	 */
//	public void GetMyFriends(String JiaoBaoHao) {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("JiaoBaoHao", JiaoBaoHao);
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_GetMyFriends);
//		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, GetMyFriends,
//				params, callback);
//	}
//
//	/**
//	 * 取本单位的基础信息 客户端通过本接口获取单位的基础信息数据。基础信息数据包括名称，类型，区域，上级单位ID，栏目文章数量和文章更新数据等
//	 *
//	 * @param params
//	 */
//	public void getMyUnitInfo(int UnitID) {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("UID", String.valueOf(UnitID));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_getMyUnitInfo);
//		HttpUtil.InstanceSend(getMyUnitInfo, params, callback);
//	}
//
//	/**
//	 * 取本单位的所有上级单位基础信息
//	 * 客户端通过本接口获取上级单位的基础信息数据。基础信息数据包括名称，类型，区域，上级单位ID，栏目文章数量和文章更新数据等。 
//	 */
//	public void getMyParentUnitInfo(int UnitID) {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("UID", String.valueOf(UnitID));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_getMyParentUnitInfo);
//		HttpUtil.InstanceSend(getMyParentUnitInfo, params, callback);
//	}
//
//	/**
//	 * 取最新未读数量---------分享
//	 *
//	 * @param params
//	 */
//	public void getSectionMessageNew() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("SectionID", "_2");
//		params.addBodyParameter("TopFlags", "1");
//		params.addBodyParameter("AccID",
//				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_getSectionMessageNew);
//		HttpUtil.InstanceSend(getSectionMessage, params, callback);
//	}
//
//	/**
//	 * 取推荐未读数量---------分享
//	 *
//	 * @param params
//	 */
//	public void getSectionMessageSuggest() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("SectionID", "_2");
//		params.addBodyParameter("TopFlags", "2");
//		params.addBodyParameter("AccID",
//				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_getSectionMessageSuggest);
//		HttpUtil.InstanceSend(getSectionMessage, params, callback);
//	}

	// /**
	// * 取我相关单位的列表及栏目未读文章数量---------分享
	// */
	// public void getUnitSectionMessages(){
	// RequestParams params = new RequestParams();
	// params.addBodyParameter("SectionID","1");
	// params.addBodyParameter("AccID",BaseActivity.sp.getString("JiaoBaoHao",
	// ""));
	// CallBack callback = new CallBack();
	// callback.setUserTag(Constant.msgcenter_notice_getUnitSectionMessages);
	// HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
	// getUnitSectionMessages, params, callback);
	// }
	// /**
	// * 取最新未读数量---------展示
	// * @param params
	// */
	// public void getShowSectionMessageNew(){
	// RequestParams params = new RequestParams();
	// params.addBodyParameter("SectionID","_1");
	// params.addBodyParameter("TopFlags","1");
	// params.addBodyParameter("AccID",MessageCenterActivity.sp.getString("JiaoBaoHao",
	// ""));
	// CallBack callback = new CallBack();
	// callback.setUserTag(Constant.msgcenter_show_getSectionMessageNew);
	// HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
	// getSectionMessage, params, callback);
	// }
//	/**
//	 * 取推荐未读数量---------展示
//	 *
//	 * @param params
//	 */
//	public void getShowSectionMessageSuggest() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("SectionID", "_1");
//		params.addBodyParameter("TopFlags", "2");
//		params.addBodyParameter("AccID",
//				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_show_getSectionMessageSuggest);
//		HttpUtil.InstanceSend(getSectionMessage, params, callback);
//	}

//	/**
//	 * 取我相关单位的列表及栏目未读文章数量---------展示
//	 */
//	public void getShowUnitSectionMessages() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("SectionID", "2");
//		params.addBodyParameter("AccID",
//				BaseActivity.sp.getString("JiaoBaoHao", ""));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_show_getUnitSectionMessages);
//		HttpUtil.InstanceSend(getUnitSectionMessages, params, callback);
//	}

	/**
	 * 取我可发动态的单位
	 */
	public void GetReleaseNewsUnits() {
		DialogUtil.getInstance().getDialog(mcontext,
				R.string.verifying_user_permission_waiting);
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_publish_permission);
		HttpUtil.InstanceSend(GetReleaseNewsUnits, null, callback);
	}

	/**
	 * 取教师关联的班级 
	 * 
	 * @param UnitID y
	 */
	public void getmyUserClass(int UnitID) {
		DialogUtil.getInstance().getDialog(mcontext,
				R.string.getting_myGrade_waiting);
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_publish_getmyUserClass);
		HttpUtil.InstanceSend(getmyUserClass, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mcontext != null) {
				try {
					dealResponseInfo("0", this.getUserTag());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mcontext)) {
					ToastUtil.showMessage(mcontext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext != null) {
				int tag = (Integer) this.getUserTag();

				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					System.out.println("---------ResultCode:" + ResultCode);

					if ("0".equals(ResultCode)) {
						switch (tag) {
						case Constant.msgcenter_publish_permission:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case LeaveConstant.leave_GetLeaveSetting:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						default:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									MessageCenterActivity.sp_sys.getString(
											"ClientKey", ""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}

					} else if ("8".equals(ResultCode)) {
						LoginActivityController.getInstance().helloService(
								mcontext);
					} else {
						ToastUtil.showMessage(mcontext,
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					ToastUtil.showMessage(mcontext, mcontext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object tag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(tag);
		NoticeGetUnitInfo getUnitInfo;
		switch ((Integer) tag) {
		case Constant.msgcenter_GetMyFriends:
			// result = "{\"list\":["+result+"]}";
			// getUnitInfo = GsonUtil.GsonToObject(result,
			// NoticeGetUnitInfo.class);
			// post.add(getUnitInfo);
			break;
		case Constant.msgcenter_getMyUnitInfo:
			result = "{\"list\":[" + result + "]}";
			getUnitInfo = GsonUtil
					.GsonToObject(result, NoticeGetUnitInfo.class);
			post.add(getUnitInfo);
			break;
		// case LeaveConstant.leave_GetLeaveSetting:
		// UnitLeaveGson list2 = GsonUtil.GsonToObject(result,
		// UnitLeaveGson.class);
		// LevelNoteStd LevelNoteStd =
		// GsonUtil.GsonToObject(list2.getLevelNoteStd(), LevelNoteStd.class);
		// ApproveListStd ApproveListStd =
		// GsonUtil.GsonToObject(list2.getApproveListStd(),
		// ApproveListStd.class);
		// LevelNote LevelNote = GsonUtil.GsonToObject(list2.getLevelNote(),
		// LevelNote.class);
		// ApproveList ApproveList =
		// GsonUtil.GsonToObject(list2.getApproveList(), ApproveList.class);
		// UnitLeave leave = new UnitLeave();
		// leave.setApproveLevel(list2.getApproveLevel());
		// leave.setApproveLevelStd(list2.getApproveLevelStd());
		// leave.setApproveList(ApproveList);
		// leave.setApproveListStd(ApproveListStd);
		// leave.setGateGuardList(list2.isGateGuardList());
		// leave.setLevelNote(LevelNote);
		// leave.setLevelNoteStd(LevelNoteStd);
		// leave.setStatus(list2.isStatus());
		// leave.setStatusStd(list2.isStatusStd());
		// post.add(leave);
		// break;
		case Constant.msgcenter_getMyParentUnitInfo:
			result = "{\"list\":" + result + "}";
			getUnitInfo = GsonUtil
					.GsonToObject(result, NoticeGetUnitInfo.class);
			post.add(getUnitInfo);
			break;
		// case Constant.msgcenter_notice_getSectionMessageNew:
		// post.add(String.valueOf(result));
		// break;
		// case Constant.msgcenter_notice_getSectionMessageSuggest:
		// post.add(String.valueOf(result));
		// break;
		// case Constant.msgcenter_notice_getUnitSectionMessages:
		// result = "{\"list\":"+result+"}";
		// GetUnitSectionMessage getUnitSectionMessage =
		// GsonUtil.GsonToObject(result, GetUnitSectionMessage.class);
		// post.add(getUnitSectionMessage);
		// break;
		// case Constant.msgcenter_show_getSectionMessageNew:
		// post.add(Integer.parseInt(result));
		// break;
		case Constant.msgcenter_show_getSectionMessageSuggest:
			post.add(Integer.parseInt(result));
			break;
		case Constant.msgcenter_show_getUnitSectionMessages:
			result = "{\"list\":" + result + "}";
			GetUnitSectionMessage getUnitSectionMessage1 = GsonUtil
					.GsonToObject(result, GetUnitSectionMessage.class);
			post.add(getUnitSectionMessage1);
			break;
		case Constant.msgcenter_publish_permission:
			ArrayList<PublishPermission> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<PublishPermission>>() {
					}.getType());
			post.add(list);
			break;
		case Constant.msgcenter_publish_getmyUserClass:
			ArrayList<UserClass> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UserClass>>() {
					}.getType());
			post.add(list1);
			break;

		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
