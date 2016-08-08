package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.app.Fragment;

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
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.qiuzhi.GetAllCategory;
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 求知Fragment的Controller
 * 
 * @author admin
 * 
 */
public class QiuZhiFragmentController implements ConstantUrl {
	private static QiuZhiFragmentController instance;
	private Fragment mcontext;
	private Context mContext;

	public static synchronized final QiuZhiFragmentController getInstance() {
		if (instance == null) {
			instance = new QiuZhiFragmentController();
		}
		return instance;
	}

	public QiuZhiFragmentController setContext(Fragment noticeFragment) {
		mcontext = noticeFragment;
		mContext = noticeFragment.getActivity();
		return this;
	}

	/**
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 * flag	是	int	回答标志1，求真回答，0普通回答，-1取全部
	 */
	public void UserIndexQuestion(String pageNum, String RowCount, String flag) {
		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", "10");
		params.addBodyParameter("pageNum", pageNum);
		params.addBodyParameter("RowCount", RowCount);
		params.addBodyParameter("flag", flag);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_UserIndexQuestion);
		HttpUtil.InstanceSend(UserIndexQuestion, params, callback);
	}

	/**
	 * <pre>
	 * 通过指定ID获取一个精选问题集或最新的一个精选问题集
	 * 参数名称	是否必须	类型	描述
	 * tabId	是	int	精选集ID,为0时取最新一期精选
	 */
	public void GetPickedById(int TabID) {
		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("tabId", String.valueOf(TabID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetPickedById);
		HttpUtil.InstanceSend(GetPickedById, params, callback);
	}

	/**
	 * <pre>
	 * 参数名称	是否必须	类型	描述
	 * numPerPage 否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 * flag	是	int	回答标志1，求真回答，0普通回答，-1取全部
	 * uid	是	int	话题Id，
	 * @param pageNum p
	 * @param RowCount r
 	 * @param flag f
	 */
	public void CategoryIndexQuestion(int pageNum, int RowCount, int uid,
			int flag) {
		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", "10");
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		params.addBodyParameter("RowCount", String.valueOf(RowCount));
		params.addBodyParameter("flag", String.valueOf(flag));
		params.addBodyParameter("uid", String.valueOf(uid));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_CategoryIndexQuestion);
		HttpUtil.InstanceSend(CategoryIndexQuestion, params, callback);
	}

//	/**
//	 * <pre>
//	 * 参数名称	是否必须	类型	描述
//	 * parentId	否	int	父级话题ID，不提供该参数或参数为null则按Subject查询全部话题
//	 * subject	否	string	话题名称关键字，按话题名称相等来查询
//	 */
//	public void GetCategory(String parentId, String subject) {
//		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
//				mcontext.getResources().getString(R.string.public_loading));
//		DialogUtil.getInstance().setCanCancel(false);
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("parentId", parentId);
//		params.addBodyParameter("subject", subject);
//		CallBack callback = new CallBack();
//		callback.setUserTag(Constant.msgcenter_qiuzhi_GetCategory);
//		HttpUtil.InstanceSend(GetCategory, params, callback);
//	}

	/** 获取主题列表 */
	public void GetAllCategory() {
		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetAllCategory);
		HttpUtil.InstanceSend(GetAllCategory, null, callback);
	}

	/**
	 * <pre>
	 * 获取首页推荐列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount 是	int 记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void RecommentIndex(String pageNum, String RowCount) {
		DialogUtil.getInstance().getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage", "10");
		params.addBodyParameter("pageNum", String.valueOf(pageNum));
		params.addBodyParameter("RowCount", String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_RecommentIndex);
		HttpUtil.InstanceSend(RecommentIndex, params, callback);
	}

	/**
	 * <pre>
	 * 获取指定话题ID的置顶问题。每一个话题只有一个置顶问题，如果该问题状态不可用（删除或屏蔽)或没有置顶问题，则返回的数组为空
	 * 参数名称	是否必须	类型	描述
	 * categoryid	否	int	话题Id
	 */
	public void GetCategoryTopQ(final int categoryid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("categoryid", String.valueOf(categoryid));// parameters
		HttpUtil.InstanceSend(GetCategoryTopQ, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (mcontext.isAdded() && !mcontext.isDetached()) {
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");

								if ("0".equals(ResultCode)) {
									ArrayList<Object> post = new ArrayList<>();
									post.add(Constant.msgcenter_qiuzhi_GetCategoryTopQ);
									ArrayList<QuestionIndexItem> top = GsonUtil.GsonToList(
											jsonObj.getString("Data"),
											new TypeToken<ArrayList<QuestionIndexItem>>() {
											}.getType());
									post.add(top);
									post.add(categoryid);
									EventBusUtil.post(post);
								} else if ("8".equals(ResultCode)) {
									LoginActivityController.getInstance()
											.helloService(
													mcontext.getActivity());
								} else {
									ToastUtil.showMessage(
											mcontext.getActivity(),
											jsonObj.getString("ResultDesc"));
								}
							} catch (Exception e) {
								e.printStackTrace();
								ToastUtil.showMessage(
										mcontext.getActivity(),
										mcontext.getResources().getString(
												R.string.error_serverconnect));
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						DialogUtil.getInstance().cannleDialog();
						if (BaseUtils.isNetworkAvailable(mContext)) {
							ToastUtil.showMessage(mContext,
									R.string.phone_no_web);
						} else {
							ToastUtil.showMessage(mContext,
									R.string.load_failed);
						}
					}
				});
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			DialogUtil.getInstance().cannleDialog();
			if (mContext != null) {
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else if (mcontext.isAdded() && !mcontext.isDetached()) {
					ToastUtil.showMessage(mContext, R.string.load_failed);
				}
			}

		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext.isAdded() && !mcontext.isDetached()) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_UserIndexQuestion:
						case Constant.msgcenter_qiuzhi_GetAllCategory:
						case Constant.msgcenter_qiuzhi_QuestionIndex:
						case Constant.msgcenter_qiuzhi_CategoryIndexQuestion:
						case Constant.msgcenter_qiuzhi_RecommentIndex:
						case Constant.msgcenter_qiuzhi_GetCategoryTopQ:
						case Constant.msgcenter_qiuzhi_GetPickedById:

							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;

						default:
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									MessageCenterActivity.sp_sys.getString(
											"ClientKey", ""));
							dealResponseInfo(data, this.getUserTag());
							break;
						}

					} else if ("8".equals(ResultCode)) {
						LoginActivityController.getInstance().helloService(
								mcontext.getActivity());
						dealResponseInfo("", this.getUserTag());
					} else {
						ToastUtil.showMessage(mcontext.getActivity(),
								jsonObj.getString("ResultDesc"));
						dealResponseInfo("", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
					dealResponseInfo("", this.getUserTag());
					ToastUtil.showMessage(
							mcontext.getActivity(),
							mcontext.getResources().getString(
									R.string.error_serverconnect));
				}
			}
		}
	}

	private void dealResponseInfo(String result, Object tag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(tag);
		switch ((Integer) tag) {
		case Constant.msgcenter_qiuzhi_GetAllCategory:
			ArrayList<GetAllCategory> allCategory = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<GetAllCategory>>() {
					}.getType());
			post.add(allCategory);
			break;
		case Constant.msgcenter_qiuzhi_UserIndexQuestion:
			ArrayList<QuestionIndexItem> questionList = GsonUtil.GsonToList(
					result, new TypeToken<ArrayList<QuestionIndexItem>>() {
					}.getType());
			post.add(questionList);
			break;
		case Constant.msgcenter_qiuzhi_QuestionIndex:
			ArrayList<QuestionItem> questionList1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<QuestionItem>>() {
					}.getType());
			post.add(questionList1);
			break;
		case Constant.msgcenter_qiuzhi_CategoryIndexQuestion:
			ArrayList<QuestionIndexItem> questionList11 = GsonUtil.GsonToList(
					result, new TypeToken<ArrayList<QuestionIndexItem>>() {
					}.getType());
			post.add(questionList11);
			break;
		case Constant.msgcenter_qiuzhi_RecommentIndex:
			ArrayList<RecommentIndexItem> recomments = GsonUtil.GsonToList(
					result, new TypeToken<ArrayList<RecommentIndexItem>>() {
					}.getType());
			post.add(recomments);
			break;
		case Constant.msgcenter_qiuzhi_GetPickedById:
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
