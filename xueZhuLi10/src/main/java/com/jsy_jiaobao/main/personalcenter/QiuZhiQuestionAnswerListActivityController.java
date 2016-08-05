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
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.AtMeUser;
import com.jsy_jiaobao.po.qiuzhi.GetAccIdbyNickname;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.RecommentDetails;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 求知问题答案列表的Controller
 * 
 * @author admin
 * 
 */
public class QiuZhiQuestionAnswerListActivityController implements ConstantUrl {
	private static QiuZhiQuestionAnswerListActivityController instance;
	private Activity mContext;

	public static synchronized final QiuZhiQuestionAnswerListActivityController getInstance() {
		if (instance == null) {
			instance = new QiuZhiQuestionAnswerListActivityController();
		}
		return instance;
	}

	public QiuZhiQuestionAnswerListActivityController setContext(
			Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 功能：获取问题的答案 列表;
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认20
	 * pageNum	否	int	第几页，默认为1
	 * QId	是	int	问题Id
	 * flag	是	string	-1全部，0无内容，2有内容，1有证据的回答
	 */
	public void GetAnswerById(int QId, int pageNum, int flag) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("QId", String.valueOf(QId));
		params.addBodyParameter("flag", String.valueOf(flag));
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		params.addBodyParameter("numPerPage", String.valueOf(10));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetAnswerById);
		HttpUtil.InstanceSend(GetAnswerById, params, callback);
	}

	/**
	 * <pre>
	 * 获取指定ID的推荐详情，可以含多个回答
	 * 参数名称	是否必须	类型	描述
	 * tabid	是	int	推荐ID
	 * @param tabid
	 */
	public void ShowRecomment(int tabid) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabid", String.valueOf(tabid));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_ShowRecomment);
		HttpUtil.InstanceSend(ShowRecomment, params, callback);
	}

	/**
	 * <pre>
	 * 关注某一问题
	 * 参数名称	是否必须	类型	描述
	 * qId	是	int	问题ID
	 */
	public void AddMyAttQ(int qId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("qId", String.valueOf(qId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AddMyAttQ);
		HttpUtil.InstanceSend(AddMyAttQ, params, callback);
	}

	/**
	 * <pre>
	 * 取消关注某一问题
	 * 参数名称	是否必须	类型	描述
	 * qId	是	int	问题ID
	 */
	public void RemoveMyAttQ(int qId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("qId", String.valueOf(qId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_RemoveMyAttQ);
		HttpUtil.InstanceSend(RemoveMyAttQ, params, callback);
	}

	/**
	 * <pre>
	 * 举报答案
	 * 参数名称	是否必须	类型	描述
	 * ansId	是	int	答案ID
	 * repType  0=答案1=问题2=评论
	 * @param AId
	 */
	public void ReportAns(int AId, int repType) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("ansId", String.valueOf(AId));
		params.addBodyParameter("repType", String.valueOf(repType));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_ReportAns);
		HttpUtil.InstanceSend(ReportAns, params, callback);
	}

	/**
	 * <pre>
	 * 邀请指定的用户回答问题
	 * 参数名称	是否必须	类型	描述
	 * accId	是	int	被邀请的用户教宝号
	 * qId	是	int	问题ID
	 */
	public void AtMeForAnswer(int accId, int qId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", String.valueOf(accId));
		params.addBodyParameter("qId", String.valueOf(qId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AtMeForAnswer);
		HttpUtil.InstanceSend(AtMeForAnswer, params, callback);
	}

	public void GetAccIdbyNickname(String inputname) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("nicknames", inputname);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetAccIdbyNickname);
		HttpUtil.InstanceSend(GetAccIdbyNickname, params, callback);
	}

	/**
	 * <pre>
	 * 邀请人回答时，获取回答该话题问题最多的用户列表（4个）
	 * 参数名称	是否必须	类型	描述
	 * uid	否	string	用户账户，可以是昵称，教宝号，手机，邮箱等 ,如果空，则是取回答该话题问题最多的用户列表（4个）如果用户输入该参数，则获取该参数指定的用户信息。
	 * catid	是	int	邀请人回答的问题的话题ID
	 */
	public void GetAtMeUsers(String uid, int catid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid);
		params.addBodyParameter("catid", String.valueOf(catid));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetAtMeUsers);
		HttpUtil.InstanceSend(GetAtMeUsers, params, callback);
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
				DialogUtil.getInstance().setCanCancel(true);
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
						case Constant.msgcenter_qiuzhi_GetAnswerById:
						case Constant.msgcenter_qiuzhi_ShowRecomment:
						case Constant.msgcenter_qiuzhi_AtMeForAnswer:
						case Constant.msgcenter_qiuzhi_GetAccIdbyNickname:
						case Constant.msgcenter_qiuzhi_QuestionDetail:
						case Constant.msgcenter_qiuzhi_ReportAns:
						case Constant.msgcenter_qiuzhi_GetAtMeUsers:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_AddMyAttQ:
						case Constant.msgcenter_qiuzhi_RemoveMyAttQ:
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
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_AtMeForAnswer:
						case Constant.msgcenter_qiuzhi_ReportAns:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						default:
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("", this.getUserTag());
							break;
						}
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
		case Constant.msgcenter_qiuzhi_GetAnswerById:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<AnswerItem> answerlist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<AnswerItem>>() {
					}.getType());
			post.add(answerlist);
			break;
		case Constant.msgcenter_qiuzhi_ShowRecomment:
			DialogUtil.getInstance().cannleDialog();
			RecommentDetails recomment = GsonUtil.GsonToObject(result,
					RecommentDetails.class);
			if (recomment != null) {
				post.add(recomment);
			}
			break;
		case Constant.msgcenter_qiuzhi_RemoveMyAttQ:
		case Constant.msgcenter_qiuzhi_AddMyAttQ:
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_ReportAns:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_AtMeForAnswer:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_GetAccIdbyNickname:
			ArrayList<GetAccIdbyNickname> nicks = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<GetAccIdbyNickname>>() {
					}.getType());
			post.add(nicks);
			break;
		case Constant.msgcenter_qiuzhi_QuestionDetail:
			QuestionDetails questiondetails = GsonUtil.GsonToList(result,
					new TypeToken<QuestionDetails>() {
					}.getType());
			post.add(questiondetails);
			break;
		case Constant.msgcenter_qiuzhi_GetAtMeUsers:
			ArrayList<AtMeUser> users = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<AtMeUser>>() {
					}.getType());
			post.add(users);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
