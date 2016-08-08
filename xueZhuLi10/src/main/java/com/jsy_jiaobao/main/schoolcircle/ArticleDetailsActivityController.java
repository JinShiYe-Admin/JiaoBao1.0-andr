package com.jsy_jiaobao.main.schoolcircle;

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
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.Comment;
import com.jsy_jiaobao.po.personal.CommentsList;
import com.jsy_jiaobao.po.personal.RefComment;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 文章详情的Controller
 * 
 * @author admin
 * 
 */
public class ArticleDetailsActivityController implements ConstantUrl {
	private static ArticleDetailsActivityController instance;
	private Activity mContext;

	public static synchronized ArticleDetailsActivityController getInstance() {
		if (instance == null) {
			instance = new ArticleDetailsActivityController();
		}
		return instance;
	}

	public ArticleDetailsActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 取本单位栏目文章 详情 客户端通过本接口获取本单位栏目文章。详情
	 */
	public void ShowArthDetail(RequestParams params) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_notice_ShowArthDetail);
		HttpUtil.InstanceSend(ShowArthDetail, params, callback);
	}

	/**
	 * 取本单位栏目文章 详情 客户端通过本接口获取本单位栏目文章。详情
	 * 
	 * @param params
	 */
	public void GetArthInfo(RequestParams params) {
		CallBack callback = new CallBack();
		System.out.println("-------------GetArthInfo");
		callback.setUserTag(Constant.msgcenter_notice_GetArthInfo);
		HttpUtil.InstanceSend(GetArthInfo, params, callback);
	}

	/**
	 * 功能：对文章进行点赞操作。一篇文章一个用户 （以教宝号为准）只能赞一次。
	 */
	public void LikeIt(String aid, String goflag) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", aid);
		params.addBodyParameter("goflag", goflag);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_article_LikeIt);
		HttpUtil.InstanceSend(LikeIt, params, callback);
	}

	/**
	 * 功能：发表评论。发表评论可以在别人评论的基础上再次评论，称为引用评论。
	 * 
	 * @param aid 文章加密ID
	 * @param comment 内容
	 * @param refid 加密的引用评语ID
	 */
	public void addComment(String aid, String comment, String refid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", aid);
		params.addBodyParameter("comment", comment);
		params.addBodyParameter("refid", refid);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_article_addComment);
		HttpUtil.InstanceSend(addComment, params, callback);
	}

	/**
	 * 功能：获取文章评论列表。
	 * 
	 * @param aid 文章加密ID
	 * @param comment 内容
	 * @param refid 加密的引用评语ID
	 */
	public void CommentsList(String aid, String pageNum) {
		DialogUtil.getInstance().getDialog(mContext, R.string.loading);
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", aid);
		params.addBodyParameter("pageNum", pageNum);
		params.addBodyParameter("numPerPage", "10");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_article_CommentsList);
		HttpUtil.InstanceSend(CommentsList, params, callback);
	}

	/**
	 * 功能：对评论进行顶的操作。
	 * 
	 * @param comment_like 文章加密ID
	 * @param tp 顶=1，踩=0
	 */
	public void AddScoreCommon(Comment comment, String tp) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", comment.getTabIDStr());
		params.addBodyParameter("tp", tp);
		AddScoreCallBack callback = new AddScoreCallBack(tp);
		callback.setUserTag(comment);
		HttpUtil.InstanceSend(AddScore, params, callback);
	}

	/**
	 * 功能：对评论进行踩的操作。
	 * 
	 * @param comment_like 文章加密ID
	 * @param tp 顶=1，踩=0
	 */
	public void AddScoreRefCom(RefComment comment, String tp) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", comment.getTabIDStr());
		params.addBodyParameter("tp", tp);
		AddScoreCallBack callback = new AddScoreCallBack(tp);
		callback.setUserTag(comment);
		HttpUtil.InstanceSend(AddScore, params, callback);
	}

	private class AddScoreCallBack extends RequestCallBack<String> {

		private String tp;

		public AddScoreCallBack(String tp) {
			this.tp = tp;
		}

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
						ArrayList<Object> post = new ArrayList<>();
						post.add(Constant.msgcenter_article_AddScore_callback);
						post.add(this.getUserTag());
						post.add(tp);
						EventBusUtil.post(post);
					}
				} catch (Exception e) {
					dealResponseInfo("", this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_serverconnect) + "r1002");
				}
			}

		}

	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);

				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				DialogUtil.getInstance().cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					switch ((Integer) this.getUserTag()) {
					case Constant.msgcenter_article_LikeIt:
						if ("0".equals(ResultCode)) {
							dealResponseInfo("-1", this.getUserTag());
						} else {
							dealResponseInfo("0", this.getUserTag());
						}
						break;
					case Constant.msgcenter_article_addComment:
						if ("0".equals(ResultCode)) {
							dealResponseInfo("0", this.getUserTag());
						} else {
							dealResponseInfo("-1", this.getUserTag());
						}
						break;
					case Constant.msgcenter_article_AddScore:
						if ("0".equals(ResultCode)) {
							dealResponseInfo("0", this.getUserTag());
						} else {
							dealResponseInfo("-1", this.getUserTag());
						}
						break;

					default:
						if ("0".equals(ResultCode)) {
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());

						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
							dealResponseInfo("", this.getUserTag());
						} else {
							dealResponseInfo("", this.getUserTag());
							System.out.println("------------"
									+ this.getUserTag());
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
						}
						break;
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
		case Constant.msgcenter_notice_ShowArthDetail:
			ArthInfo getArthInfo = GsonUtil
					.GsonToObject(result, ArthInfo.class);
			post.add(getArthInfo);
			break;
		case Constant.msgcenter_notice_GetArthInfo:
			ArthInfo GetArthInfo = GsonUtil
					.GsonToObject(result, ArthInfo.class);
			post.add(GetArthInfo);
			break;
		case Constant.msgcenter_article_LikeIt:
			post.add(result);
			break;
		case Constant.msgcenter_article_addComment:
			post.add(result);
			break;
		case Constant.msgcenter_article_CommentsList:
			CommentsList commentsList = GsonUtil.GsonToObject(result,
					CommentsList.class);
			post.add(commentsList);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
