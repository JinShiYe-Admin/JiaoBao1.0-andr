package com.jsy_jiaobao.main.affairs;

import android.content.Context;
import android.support.v4.app.Fragment;
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
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.CommMsg;
import com.jsy_jiaobao.po.personal.GetSendToMeMsg;
import com.jsy_jiaobao.po.personal.MySendMsg;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkFragment2Controller implements ConstantUrl {
	private static WorkFragment2Controller instance;
	private Fragment mContext;
	private Context mAContext;

	public static synchronized  WorkFragment2Controller getInstance() {
		if (instance == null) {
			instance = new WorkFragment2Controller();
		}
		return instance;
	}

	public WorkFragment2Controller setContext(Fragment noticeFragment) {
		mContext = noticeFragment;
		mAContext = noticeFragment.getActivity();
		return this;
	}

//	/**
//	 * 获取我发送的消息列表，列表中包括回复数量，未读回复数量经。比原接口简化了返回的字段且不加密
//	 *
//	 * <pre>
//	 * 参数名称		是否必须	类型	描述
//	 * numPerPage	否	int	取回的记录数量，默认20
//	 * pageNum		否	int	第几页，默认为1
//	 * SendName		否	string	按发送内容检索，默认为空，不需要该检索条件（sendname这里不表示发送者）
//	 * sDate		否	DateTime	开始日间，默认为二个月前（现在时间减去约60天）（以日期字符串赋值）
//	 * eDate		否	DateTime	结束日间，默认为现在的时间 （以日期字符串赋值）
//	 */
//	public void GetMySendMsgList() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("numPerPage", "1");//
//		params.addBodyParameter("pageNum", "1");//
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_work2_GetMySendMsgList);
//		HttpUtil.InstanceSend(GetMySendMsgList, params, callback);
//	}

//	/**
//	 * 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量
//	 *
//	 * @param params
//	 *            request参数
//	 *
//	 *            <pre>
//	 * 参数名称		是否必须	类型	描述
//	 * numPerPage	否	int	取回的记录数量，默认20
//	 * pageNum		否	int	第几页，默认为1
//	 * lastId		否	string	分页标志值，此标志第1页为空，从第2页起须提供。如果取第1页数据后还有后续页记录，则接口结果值中会返回该标志值 。如果返回的值 为空，则表示没有记录（已取加全部记录）。如果有返回值，则在取下一页时向接口提供该标志值。
//	 * SendName	 	否	string	按发送者姓名检索，默认为空，不需要该检索条件
//	 * sDate		否	DateTime	开始日间，默认为二个月前（现在时间减去约60天）（以日期字符串赋值）
//	 * eDate		否	DateTime	结束日间，默认为现在的时间 （以日期字符串赋值）
//	 * readflag		否	int	按阅读标志检索：不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
//	 *
//	 */
//	public void SendToMeUserList(RequestParams params) {
//		params.addBodyParameter("numPerPage", "20");//
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_work2_SendToMeUserList);
//		HttpUtil.InstanceSend(SendToMeUserList, params, callback);
//	}

	/**
	 * 取发给我的交流信息
	 * 
	 * @param params
	 *            request参数
	 * @param cbTag
	 *            CallBack的Tag
	 * 
	 *            <pre>
	 * 参数名称		是否必须	类型	描述 
	 * numPerPage 	否	int	取回的记录数量，默认20
	 * pageNum    	否	int	第几页，默认为1
	 * SendName   	否	string	按发送者姓名检索，默认为空，不需要该检索条件
	 * Content    	否	string	按发送内容检索，默认为空，不需要该检索条件
	 * time       	否	int	按时间排序，0时间降序，1时间升序，默认为0
	 * readflag   	否	int	按阅读标志检索：不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
	 * trun       	否	boolean	按转发标志检查：不提供该参数：查全部，true查询转发记录，false：查全部
	 */
	public void CommList(RequestParams params, int cbTag) {
		Log.d("获取我发布的参数",params.toString());
		params.addBodyParameter("numPerPage", "20");//
		CallBack callback = new CallBack();
		callback.setUserTag(cbTag);
		HttpUtil.InstanceSend(CommListToMe, params, callback);
	}

	/**
	 * 取我发的交流信息
	 * 
	 * @param params
	 *            request参数
	 * @param cbTag
	 *            CallBack的Tag
	 * 
	 *            <pre>
	 * 参数名称		是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认20
	 * pageNum		否	int	第几页，默认为1
	 * SendName		否	string	按发送内容检索，默认为空，不需要该检索条件（sendname这里不表示发送者）
	 */
	public void MySend(RequestParams params, int cbTag) {
		params.addBodyParameter("numPerPage", "20");//
		CallBack callback = new CallBack();
		callback.setUserTag(cbTag);
		HttpUtil.InstanceSend(MySend, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mAContext != null) {
				DialogUtil.getInstance().cannleDialog();
				if (BaseUtils.isNetworkAvailable(mAContext)) {
					ToastUtil.showMessage(mAContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mAContext, R.string.load_failed);
				}
				try {
					if (mContext != null) {
						dealResponseInfo("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mContext.isAdded() && !mContext.isDetached()) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						String data = Des.decrypt(jsonObj.getString("Data"),
								BaseActivity.sp_sys.getString("ClientKey", ""));
						dealResponseInfo(data, this.getUserTag());
					} else if ("8".equals(ResultCode)) {
						dealResponseInfo("0", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext.getActivity());
					} else {
						dealResponseInfo("0", this.getUserTag());
						ToastUtil.showMessage(mContext.getActivity(),
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("0", this.getUserTag());
					ToastUtil.showMessage(
							mContext.getActivity(),
							mContext.getResources().getString(
									R.string.error_serverconnect)
									+ "r1002");
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object userTag) {
		Log.e("回调信息：",result);
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_work2_GetMySendMsgList:
			try {
				ArrayList<MySendMsg> mySendMsg = GsonUtil.GsonToList(result,
						new TypeToken<ArrayList<MySendMsg>>() {
						}.getType());
				post.add(mySendMsg.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case Constant.msgcenter_work2_SendToMeUserList:
			GetSendToMeMsg sendtomeMsg = GsonUtil.GsonToObject(result,
					GetSendToMeMsg.class);
			post.add(sendtomeMsg);
			break;
		case Constant.msgcenter_work2_CommListToMeAll:
		case Constant.msgcenter_work2_CommListToMeCommented:
		case Constant.msgcenter_work2_CommListToMeUnComment:
		case Constant.msgcenter_work2_CommListToMeUnRead:
		case Constant.msgcenter_work2_CommListFromMe:
			List<CommMsg> a = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<CommMsg>>() {
					}.getType());
			post.add(a);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}