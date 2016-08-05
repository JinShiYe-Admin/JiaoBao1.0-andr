//package com.jsy_jiaobao.main.affairs;
//
//import java.util.ArrayList;
//
//import org.json.JSONObject;
//
//import android.app.Activity;
//import com.jsy.xuezhuli.utils.BaseUtils;
//import com.jsy.xuezhuli.utils.Constant;
//import com.jsy.xuezhuli.utils.ConstantUrl;
//import com.jsy.xuezhuli.utils.Des;
//import com.jsy.xuezhuli.utils.EventBusUtil;
//import com.jsy.xuezhuli.utils.GsonUtil;
//import com.jsy.xuezhuli.utils.HttpUtil;
//import com.jsy.xuezhuli.utils.ToastUtil;
//import com.jsy_jiaobao.main.BaseActivity;
//import com.jsy_jiaobao.main.R;
//import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
//import com.jsy_jiaobao.main.system.LoginActivityController;
//import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
//import com.jsy_jiaobao.po.sys.GetUserClass;
//import com.jsy_jiaobao.po.sys.UserUnit;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//
///**
// *                   _ooOoo_
// *                  o8888888o
// *                  88" . "88
// *                  (| -_- |)
// *                  O\  =  /O
// *               ____/`---'\____
// *             .'  \\|     |//  `.
// *            /  \\|||  :  |||//  \
// *           /  _||||| -:- |||||-  \
// *           |   | \\\  -  /// |   |
// *           | \_|  ''\---/''  |   |
// *           \  .-\__  `-`  ___/-. /
// *         ___`. .'  /--.--\  `. . __
// *      ."" '<  `.___\_<|>_/___.'  >'"".
// *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
// *     \  \ `-.   \_ __\ /__ _/   .-` /  /
// *======`-.____`-.___\_____/___.-`____.-'======
// *                   `=---='
// *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// *         		    佛祖保佑       永无BUG
// */
//public class WorkDetailsActivityController implements ConstantUrl{
//	private static WorkDetailsActivityController instance;
//	private Activity mcontext;
//
//	public static synchronized final WorkDetailsActivityController getInstance() {
//		if (instance == null) {
//			instance = new WorkDetailsActivityController();
//		}
//		return instance;
//	}
//
//	public WorkDetailsActivityController setContext(Activity pActivity) {
//		mcontext = pActivity;
//		return this;
//	}
//	/**
//	 * 取教师关联的班级 
//	 * @param unitID
//	 */
//	public void getmyUserClass(int UnitID){
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("UID",String.valueOf(UnitID));
//		params.addBodyParameter("AccID",BaseActivity.sp.getString("JiaoBaoHao", ""));
//		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getmyUserClass, params, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				// TODO Auto-generated method stub
//				if(mcontext!=null){
//					if(BaseUtils.isNetworkAvailable(mcontext)){
//						ToastUtil.showMessage(mcontext,R.string.phone_no_web);
//					}
//				}
//
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//					try {
//						JSONObject jsonObj = new JSONObject(arg0.result);
//						String ResultCode = jsonObj.getString("ResultCode");
//						if ("0".equals(ResultCode)) {
//							String result = Des.decrypt(jsonObj.getString("Data"), MessageCenterActivity.sp_sys.getString("ClientKey", ""));
//							ArrayList<Object> post = new ArrayList<Object>();
//							post.add(Constant.msgcenter_train_getmyUserClass);
//							result = "{\"list\":"+result+"}";
//							GetUserClass getUnitClass1 = GsonUtil.GsonToObject(result, GetUserClass.class);
//							post.add(getUnitClass1);
//							EventBusUtil.post(post);
//						}else if("8".equals(ResultCode)){
//							LoginActivityController.getInstance().helloService(mcontext);
//						} else {
//							ToastUtil.showMessage(mcontext,jsonObj.getString("ResultDesc"));
//						}
//					} catch (Exception e) {
//						ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect)+"r1002");
//					}
//				}
//		});
//	}
//	/**
//	 * 取本单位的所有下级单位基础信息
//	 * 客户端通过本接口获取上级单位的基础信息数据。基础信息数据包括名称，类型，区域，上级单位ID，栏目文章数量和文章更新数据等。 
//	 */
//	public void getMySubUnitInfo(UserUnit userUnit){
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("UID",String.valueOf(userUnit.getUnitID()));
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_notice_getMySubUnitInfo);
//		HttpUtil.InstanceSend(getMySubUnitInfo, params, callback);
//
//	}
//	private class CallBack extends RequestCallBack<String>{
//
//		@Override
//		public void onFailure(HttpException arg0, String arg1) {
//			if(mcontext!=null){
//				if (!mcontext.isFinishing()) {
//				}
//				if(BaseUtils.isNetworkAvailable(mcontext)){
//					ToastUtil.showMessage(mcontext,R.string.phone_no_web);
//				}
//			}
//		}
//
//		@Override
//		public void onSuccess(ResponseInfo<String> arg0) {
//			try {
//				JSONObject jsonObj = new JSONObject(arg0.result);
//				String ResultCode = jsonObj.getString("ResultCode");
//				if ("0".equals(ResultCode)) {
//					String data = Des.decrypt(jsonObj.getString("Data"), MessageCenterActivity.sp_sys.getString("ClientKey", ""));
//					dealResponseInfo(data,this.getUserTag());
//
//				}else if("8".equals(ResultCode)){
//					LoginActivityController.getInstance().helloService(mcontext);
//				} else {
//					ToastUtil.showMessage(mcontext,jsonObj.getString("ResultDesc"));
//				}
//			} catch (Exception e) {
//				ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect)+"r1002");
//			}
//		}
//
//	}
//	private void dealResponseInfo(String result, Object userTag) {
//		ArrayList<Object> post = new ArrayList<Object>();
//		switch ((Integer)userTag) {
//		case Constant.msgcenter_notice_getMySubUnitInfo:
//			post.add(Constant.msgcenter_notice_getMySubUnitInfo);
//			result = "{\"list\":"+result+"}";
//			NoticeGetUnitInfo getUnitInfo = GsonUtil.GsonToObject(result, NoticeGetUnitInfo.class);
//			post.add(getUnitInfo);
//			break;
//		default:
//			break;
//		}
//		EventBusUtil.post(post);
//	}
//}
