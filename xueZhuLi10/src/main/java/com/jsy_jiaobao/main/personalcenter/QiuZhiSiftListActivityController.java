package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONObject;

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
import com.jsy_jiaobao.po.qiuzhi.PickedItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.ShowPicked;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 往期精选 的Controller
 * 
 * @author admin
 * 
 */
public class QiuZhiSiftListActivityController implements ConstantUrl {
	private static QiuZhiSiftListActivityController instance;
	private Activity mContext;

	public static synchronized  QiuZhiSiftListActivityController getInstance() {
		if (instance == null) {
			instance = new QiuZhiSiftListActivityController();
		}
		return instance;
	}

	public QiuZhiSiftListActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 功能：获取各期精选列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void PickedIndex(int pageNum, int RowCount) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", String.valueOf(20));
		params.addBodyParameter("RowCount", String.valueOf(RowCount));
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_PickedIndex);
		HttpUtil.InstanceSend(PickedIndex, params, callback);
	}

	/*
	 * 往期精选
	 */
	public void ShowPicked(int tabId) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(tabId));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_ShowPicked);
		HttpUtil.InstanceSend(ShowPicked, params, callback);
	}

	/**
	 * 功能：获取一个问题明细信息，包括问题内容;
	 */
	public void QuestionDetail(int QId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("QId", String.valueOf(QId));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_QuestionDetail);
		HttpUtil.InstanceSend(QuestionDetail, params, callback);
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
						case Constant.msgcenter_qiuzhi_ShowPicked:
						case Constant.msgcenter_qiuzhi_PickedIndex:
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
		DialogUtil.getInstance().cannleDialog();
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_qiuzhi_ShowPicked:
			ShowPicked picked = GsonUtil.GsonToObject(result, ShowPicked.class);
			post.add(picked);
			break;
		case Constant.msgcenter_qiuzhi_PickedIndex:
			ArrayList<PickedItem> pickedlist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<PickedItem>>() {
					}.getType());
			post.add(pickedlist);
			break;
		case Constant.msgcenter_qiuzhi_QuestionDetail:
			DialogUtil.getInstance().cannleDialog();
			QuestionDetails questiondetails = GsonUtil.GsonToList(result,
					new TypeToken<QuestionDetails>() {
					}.getType());
			post.add(questiondetails);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
