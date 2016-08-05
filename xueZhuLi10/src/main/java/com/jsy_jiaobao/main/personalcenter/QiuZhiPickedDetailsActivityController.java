package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
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
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 求知往期精选Controller
 * 
 * @author admin
 * 
 */
public class QiuZhiPickedDetailsActivityController implements ConstantUrl {
	private static QiuZhiPickedDetailsActivityController instance;
	private Activity mContext;

	public static synchronized final QiuZhiPickedDetailsActivityController getInstance() {
		if (instance == null) {
			instance = new QiuZhiPickedDetailsActivityController();
		}
		return instance;
	}

	public QiuZhiPickedDetailsActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 通过指定ID获取一个精选问题集或最新的一个精选问题集
	 * 参数名称	是否必须	类型	描述
	 * tabId	是	int	精选集ID,为0时取最新一期精选
	 */
	public void GetPickedById(int TabID) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(TabID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetPickedById_pd);
		HttpUtil.InstanceSend(GetPickedById, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_QuestionDetail:
						case Constant.msgcenter_qiuzhi_GetPickedById_pd:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_AddAnswer:
							dealResponseInfo("1", this.getUserTag());
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
						dealResponseInfo("", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mContext);
					} else {
						ToastUtil.showMessage(mContext,
								jsonObj.getString("ResultDesc"));
						dealResponseInfo("", this.getUserTag());
					}
				} catch (Exception e) {
					dealResponseInfo("", this.getUserTag());
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
		case Constant.msgcenter_qiuzhi_GetPickedById_pd:
			DialogUtil.getInstance().cannleDialog();
			GetPicked getPicked = GsonUtil
					.GsonToObject(result, GetPicked.class);
			post.add(getPicked);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
