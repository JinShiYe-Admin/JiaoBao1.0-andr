package com.jsy_jiaobao.main.affairs;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.Attlist;
import com.jsy_jiaobao.po.personal.GetSendToMeMsg;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.jsy_jiaobao.po.personal.MySendMsg;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class Work2DetailsListActivityController implements ConstantUrl {
	private static Work2DetailsListActivityController instance;
	private Activity mContext;
	private ArrayList<ProgressDialog> dialogList = new ArrayList<ProgressDialog>();

	public static synchronized final Work2DetailsListActivityController getInstance() {
		if (instance == null) {
			instance = new Work2DetailsListActivityController();
		}
		return instance;
	}

	public Work2DetailsListActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 我发送的<br>
	 * 获取我发送的消息列表，列表中包括回复数量，未读回复数量经。比原接口简化了返回的字段且不加密
	 * 
	 * @param params
	 *            <pre>
	 * 参数名称		是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认20
	 * pageNum		否	int	第几页，默认为1
	 * SendName		否	string	按发送内容检索，默认为空，不需要该检索条件（sendname这里不表示发送者）
	 * sDate		否	DateTime	开始日间，默认为二个月前（现在时间减去约60天）（以日期字符串赋值）
	 * eDate		否	DateTime	结束日间，默认为现在的时间 （以日期字符串赋值）
	 */
	public void GetMySendMsgList(RequestParams params) {
		params.addBodyParameter("numPerPage", "10");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work2_GetMySendMsgList);
		HttpUtil.InstanceSend(GetMySendMsgList, params, callback);

	}

	/**
	 * 取单个用户发给我消息列表<br>
	 * 取发给我消息的用户列表。包括最新一条消息内容，以及该用户发给我的未读消息数量，未回复数量
	 * 
	 * @param params
	 *            <pre>
	 * 参数名称		是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认20
	 * pageNum		否	int	第几页，默认为1
	 * lastId		否	string	分页标志值，此标志第1页为空，从第2页起须提供。如果取第1页数据后还有后续页记录，则接口结果值中会返回该标志值 。如果返回的值 为空，则表示没有记录（已取加全部记录）。如果有返回值，则在取下一页时向接口提供该标志值。
	 * senderAccId	否	int	发送者教宝号
	 * sDate		否	DateTime	开始日间，默认为二个月前（现在时间减去约60天）（以日期字符串赋值）
	 * eDate		否	DateTime	结束日间，默认为现在的时间 （以日期字符串赋值）
	 * readflag		否	int	按阅读标志检索：不提供该参数：查全部，1：未读，2：已读未回复，3：已回复
	 */
	public void SendToMeMsgList(RequestParams params) {
		params.addBodyParameter("numPerPage", "10");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work2_SendToMeMsgList);
		HttpUtil.InstanceSend(SendToMeMsgList, params, callback);

	}

	/**
	 * 获取信息详情<br>
	 * (warn:未找到该接口的详细说明，所以无法添加详细注释)
	 * 
	 * @param params
	 */
	public void ShowDetail(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		params.addBodyParameter("getfb", "true");//
		params.addBodyParameter("numPerPage", "20");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work2_FirstWorkDetails);
		HttpUtil.InstanceSend(ShowDetail2, params, callback);

	}

	/**
	 * 回复交流信息<br>
	 * 功能：对发给我的交流信息添加回复
	 * 
	 * @param params
	 * 
	 *            <pre>
	 * 参数名称			是否必须	类型	描述
	 * uid			是	string	发给我的交流信息的加密ID(TabIDStr)
	 * feebacktalkcontent	是	string	回复内容
	 * MsgRecDate		是	datetime	交流信息的RecDate
	 */
	public void addfeeback(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_later));
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_work2_addfeeback);
		HttpUtil.InstanceSend(addfeeback, params, callback);

	}

	/**
	 * 标记所有回复我的信息为已读<br>
	 * 功能：标记所有未读的回复我的信息为已读状态<br>
	 * 参数： 无<br>
	 * 返回结果：<br>
	 * 由api结果类定义，返回0表示成功,>0表示出错了
	 * 
	 * @param TabIDStr
	 */
	public void MarkRead(String TabIDStr) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", TabIDStr);//
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, MarkRead,
				params, null);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo("fail", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_internet));
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					DialogUtil.getInstance().cannleDialog();
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_work2_GetMySendMsgList:
						case Constant.msgcenter_work2_SendToMeMsgList:
						case Constant.msgcenter_work2_FirstWorkDetails:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						default:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}

					} else if ("8".equals(ResultCode)) {
						dealResponseInfo("fail", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext);
					} else if ("999999".equals(ResultCode)) {
						dealResponseInfo("fail", this.getUserTag());
						ToastUtil.showMessage(mContext, "发送失败，请将html标签去掉");
						Log.i("有html标签", jsonObj + "-" + this.getUserTag());
					} else {
						dealResponseInfo("fail", this.getUserTag());
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
					}
				} catch (Exception e) {
					dealResponseInfo("fail", this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_work2_GetMySendMsgList:
			ArrayList<MySendMsg> mySendMsg = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<MySendMsg>>() {
					}.getType());
			post.add(mySendMsg);
			break;
		case Constant.msgcenter_work2_SendToMeMsgList:
			GetSendToMeMsg sendToMe = GsonUtil.GsonToObject(result,
					GetSendToMeMsg.class);
			post.add(sendToMe);
			break;
		case Constant.msgcenter_work2_FirstWorkDetails:
			GetWorkMsgDetails getWorkDetails = GsonUtil.GsonToObject(result,
					GetWorkMsgDetails.class);
			post.add(getWorkDetails);
			break;
		case Constant.msgcenter_work2_addfeeback:
			DialogUtil.getInstance().cannleDialog();
			if ("fail".equals(result)) {
				post.add("fail");
			} else {
				post.add("succ");
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

	/**
	 * 下载附件
	 * 
	 * @param att
	 */
	public HttpHandler downloadAtt(final Attlist att) {
		for (int i = 0; i < dialogList.size(); i++) {
			ProgressDialog m_pDialog = dialogList.get(i);
			if (m_pDialog.isShowing()) {
				return null;
			}
		}
		final String filePath = JSYApplication.getInstance().FILE_PATH
				+ System.currentTimeMillis() + att.getOrgFilename();
		HttpHandler handler = HttpUtil.getInstanceNew().download(
				att.getDlurl(), filePath, true, new RequestCallBack<File>() {

					private String TAG = "00";

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						if (mContext != null) {
							ProgressDialog m_pDialog = (ProgressDialog) this
									.getUserTag();
							if (m_pDialog != null && m_pDialog.isShowing()) {
								m_pDialog.dismiss();
							}
							Log.d(TAG, mContext.toString());
							ToastUtil.showMessage(mContext,
									R.string.save_success);
							PictureUtils.openFile(mContext, filePath);

						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (mContext != null) {
							ProgressDialog m_pDialog = (ProgressDialog) this
									.getUserTag();
							if (m_pDialog != null && m_pDialog.isShowing()) {
								m_pDialog.dismiss();
							}
							if (BaseUtils.isNetworkAvailable(mContext)) {
								ToastUtil.showMessage(mContext,
										R.string.phone_no_web);
							} else {
								ToastUtil.showMessage(mContext,
										R.string.load_failed);
							}
						}

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (mContext != null) {
							ProgressDialog m_pDialog = (ProgressDialog) this
									.getUserTag();
							if (m_pDialog != null && m_pDialog.isShowing()) {
								m_pDialog
										.setProgress((int) (current * 100 / total));
							}
						}
					}

					@Override
					public void onStart() {
						if (mContext != null) {
							try {
								ProgressDialog m_pDialog = new ProgressDialog(
										mContext);
								this.setUserTag(m_pDialog);
								m_pDialog
										.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								m_pDialog.setTitle(R.string.load_progress);
								m_pDialog.setMax(100);
								m_pDialog.setIndeterminate(false);
								m_pDialog.setCancelable(false);
								m_pDialog.show();
								dialogList.add(m_pDialog);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onCancelled() {
						super.onCancelled();
						ProgressDialog m_pDialog = (ProgressDialog) this
								.getUserTag();
						if (m_pDialog != null && m_pDialog.isShowing()) {
							m_pDialog.cancel();
						}
					}

				});
		return handler;
	}

}
