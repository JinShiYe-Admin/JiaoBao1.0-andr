package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;
import org.json.JSONObject;
import android.content.Context;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.workol.HwPack;
import com.jsy_jiaobao.po.workol.QsPack;
import com.jsy_jiaobao.po.workol.StuSubQs;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 作业详情的Controller
 * 
 * @author admin
 * 
 */

public class WorkDetailsActivityController implements ConstantUrl {
	private static WorkDetailsActivityController instance;
	private Context mContext;

	public static synchronized final WorkDetailsActivityController getInstance() {
		if (instance == null) {
			instance = new WorkDetailsActivityController();
		}
		return instance;
	}

	public WorkDetailsActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 获取单题,作业名称,作业题量,作业开始时间,作业时长,作业上交时间
	 * 参数：HwInfoId 作业ID
	 */
	public void GetStuHW(int HwInfoId, boolean isStu) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("HwInfoId", String.valueOf(HwInfoId));
		params.addBodyParameter("isStu", String.valueOf(!isStu));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHW);
		HttpUtil.InstanceSend(Constants.GetStuHW, params, callback);
	}

	/**
	 * <pre>
	 * 获取服务器时间
	 */
	public void GetSQLDateTIme() {
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetSQLDateTIme);
		HttpUtil.InstanceSend(Constants.GetSQLDateTIme, null, callback);
	}

	/**
	 * <pre>
	 * 获取某作业下某题的作业题及答案
	 * 参数： HwInfoId作业ID，QsId题序号ID
	 */
	public void GetStuHWQs(int HwInfoId, String QsId) {
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext,
					R.string.communicating_waiting);
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("HwInfoId", String.valueOf(HwInfoId));
		params.addBodyParameter("QsId", QsId);
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHWQs);
		HttpUtil.InstanceSend(Constants.GetStuHWQs, params, callback);
	}

	/**
	 * <pre>
	 * 学生提交答案
	 * 参数： HwInfoId作业ID，QsId题序号ID
	 */
	public void StuSubQs(int HwInfoId, String QsId, String answer) {
		DialogUtil.getInstance().getDialog(mContext,
				R.string.communicating_waiting);
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("HwInfoId", String.valueOf(HwInfoId));
		params.addBodyParameter("QsId", QsId);
		params.addBodyParameter("Answer", answer);
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_StuSubQs);
		HttpUtil.InstanceSend(Constants.StuSubQs, params, callback);
	}

	/**
	 * 结果状况，及处理
	 * 
	 * @author admin
	 * 
	 */

	private class CallBack extends RequestCallBack<String> {
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false", this.getUserTag());
				DialogUtil.getInstance().cannleDialog();
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_internet));
				}
			}
		}

		/**
		 * 请求成功
		 */
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				switch ((Integer) this.getUserTag()) {
				case Constants.WORKOL_GetStuHW:
				case Constants.WORKOL_GetStuHWQs:
				case Constants.WORKOL_StuSubQs:
				case Constants.WORKOL_GetSQLDateTIme:
					dealResponseInfo(arg0.result, this.getUserTag());
					break;
				default:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);// {"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
						String ResultCode = jsonObj.getString("ResultCode");
						if ("0".equals(ResultCode)) {
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());
						} else if ("8".equals(ResultCode)) {
							dealResponseInfo("false", this.getUserTag());
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("false", this.getUserTag());
						}
					} catch (Exception e) {
						dealResponseInfo("", this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				}

			}
		}

	}

	/**
	 * 根据Tag请求处理结果
	 * 
	 * @param result
	 * @param userTag
	 */
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_GetSQLDateTIme:
			post.add(result);
			break;
		case Constants.WORKOL_StuSubQs:
			DialogUtil.getInstance().cannleDialog();
			StuSubQs subqs = GsonUtil.GsonToObject(result, StuSubQs.class);
			post.add(subqs);
			break;
		case Constants.WORKOL_GetStuHW:
			HwPack genInfo = GsonUtil.GsonToObject(result, HwPack.class);
			post.add(genInfo);
			break;
		case Constants.WORKOL_GetStuHWQs:
			DialogUtil.getInstance().cannleDialog();
			QsPack genInfo1 = GsonUtil.GsonToObject(result, QsPack.class);
			post.add(genInfo1);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

}
