package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import org.json.JSONObject;

import android.app.Activity;
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
import com.jsy_jiaobao.po.personal.UpFiles;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 求知问题详情的Controller
 * 
 * @author admin
 * 
 */
public class QiuZhiQuestionDetailsActivityController implements ConstantUrl {
	private static QiuZhiQuestionDetailsActivityController instance;
	private Activity mContext;

	public static synchronized  QiuZhiQuestionDetailsActivityController getInstance() {
		if (instance == null) {
			instance = new QiuZhiQuestionDetailsActivityController();
		}
		return instance;
	}

	public QiuZhiQuestionDetailsActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 功能：获取一个问题明细信息，包括问题内容; 0返回html 1返回url
	 */
	public void AnswerDetail(int AId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AId", String.valueOf(AId));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AnswerDetail);
		HttpUtil.InstanceSend(AnswerDetail, params, callback);
	}

	/**
	 * 功能：获取一个问题明细信息，包括问题内容; 0返回html 1返回url
	 */
	public void QuestionDetail(int QId) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("QId", String.valueOf(QId));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_QuestionDetail);
		HttpUtil.InstanceSend(QuestionDetail, params, callback);
	}

	/**
	 * <pre>
	 * 功能：修改答案;
	 * 参数名称	是否必须	类型	描述
	 * TabID	是	int	答案Id
	 * Flag	是	int	回答标志，0普通回答，1求真回答
	 * AContent	是	string	回答的内容
	 */
	public void UpdateAnswer(int TabID, int Flag, String Title,
			String AContent, String UserName) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("TabID", String.valueOf(TabID));
		params.addBodyParameter("Flag", String.valueOf(Flag));
		params.addBodyParameter("AContent", AContent);
		params.addBodyParameter("Title", Title);
		params.addBodyParameter("UserName", UserName);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_UpdateAnswer);
		HttpUtil.InstanceSend(UpdateAnswer, params, callback);
	}

	/**
	 * <pre>
	 * 功能：回答问题
	 * 参数名称	是否必须	类型	描述
	 * QId	是	int	问题Id
	 * Flag	是	int	回答标志，0普通回答，1求真回答
	 * Title	是	string	标题
	 * AContent	是	string	回答的内容（依据），系统据此有无内容判定是否有论据的回答
	 * UserName	是	string	用户昵称，若是匿名回答，为空字串符
	 */
	public void AddAnswer(int QId, String Title, String AContent, int Flag,
			String UserName) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("QId", String.valueOf(QId));
		params.addBodyParameter("Title", Title);
		params.addBodyParameter("AContent", AContent);
		params.addBodyParameter("UserName", UserName);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AddAnswer);
		HttpUtil.InstanceSend(AddAnswer, params, callback);
	}

	/**
	 * 上传图片
	 * 
	 * @param params 请求数据
	 */
	public void uploadSectionImg(RequestParams params) {
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_uploadSectionImg);
		HttpUtil.InstanceSend(uploadSectionImg, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			Log.d("发生错误：",arg0.toString());
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			Log.d("获取的返回值：",arg0.toString());
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_AnswerDetail:
						case Constant.msgcenter_qiuzhi_QuestionDetail:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_AddAnswer:
						case Constant.msgcenter_qiuzhi_UpdateAnswer:
							dealResponseInfo("1", this.getUserTag());
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
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
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_qiuzhi_QuestionDetail:
			DialogUtil.getInstance().cannleDialog();
			QuestionDetails questiondetails = GsonUtil.GsonToList(result,
					new TypeToken<QuestionDetails>() {
					}.getType());
			Log.d("获取的响应信息：",questiondetails.toString());
			post.add(questiondetails);
			break;
		case Constant.msgcenter_qiuzhi_AddAnswer:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_UpdateAnswer:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_uploadSectionImg:
			UpFiles list2 = GsonUtil.GsonToObject(result, UpFiles.class);
			post.add(list2);
			break;
		case Constant.msgcenter_qiuzhi_AnswerDetail:
			DialogUtil.getInstance().cannleDialog();
			AnswerDetails answer = GsonUtil.GsonToObject(result,
					AnswerDetails.class);
			post.add(answer);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
