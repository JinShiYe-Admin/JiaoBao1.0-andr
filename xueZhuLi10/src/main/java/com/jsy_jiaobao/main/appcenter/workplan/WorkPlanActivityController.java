//package com.jsy_jiaobao.main.appcenter.workplan;
//
//import android.app.Activity;
//
//import com.jsy.xuezhuli.utils.ConstantUrl;
//
//public class WorkPlanActivityController implements ConstantUrl{
//	private static WorkPlanActivityController instance;
////	private Activity mcontext;
//	public static synchronized  WorkPlanActivityController getInstance() {
//		if (instance == null) {
//			instance = new WorkPlanActivityController();
//		}
//		return instance;
//	}
//
//	public WorkPlanActivityController setContext(Activity pActivity) {
////		mcontext = pActivity;
//		return this;
//	}
////	public void getUserGroup(RequestParams params) {
////		DialogUtil.getInstance().getDialog(mcontext,mcontext.getString(R.string.public_loading));
////		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getUserGroup, params, new RequestCallBack<String>() {
////
////			@Override
////			public void onFailure(HttpException arg0, String arg1) {
////				EventBusUtil.post(0);
////				DialogUtil.getInstance().cannleDialog();
////				BaseUtils.shortToast(mcontext, mcontext.getResources().getString(R.string.error_serverconnect));
////			}
////
////			@Override
////			public void onSuccess(ResponseInfo<String> arg0) {
////				try {
////					DialogUtil.getInstance().cannleDialog();
////					JSONObject jsonObj = new JSONObject(arg0.result);
////					String ResultCode = jsonObj.getString("ResultCode");
////					if ("0".equals(ResultCode)) {
////						String data = Des.decrypt(jsonObj.getString("Data"), mcontext.getSharedPreferences(Constant.SP_TB_SYS, mcontext.MODE_PRIVATE).getString("ClientKey", ""));
////						data = "{\"list\":"+data+"}";
////						GetUserGroup getList = GsonUtil.GsonToObject(data, GetUserGroup.class);
////						Constant.listUserGroup = getList.getList();
////						if (Constant.listUserGroup != null && Constant.listUserGroup.size() > 0) {
////							Constant.userGroup = Constant.listUserGroup.get(0);
////						}
////						EventBusUtil.post(1);
////					}else{
////						EventBusUtil.post(0);
////						BaseUtils.shortToast(mcontext, "获取失败");
////					}
////				} catch (Exception e) {
////					EventBusUtil.post(0);
////					BaseUtils.shortToast(mcontext, "获取失败1002");
////				}
////
////			}
////		});
////
////	}
//}
