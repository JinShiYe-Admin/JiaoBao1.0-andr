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
import com.jsy_jiaobao.po.personal.UpFiles;
import com.jsy_jiaobao.po.qiuzhi.AtMeUser;
import com.jsy_jiaobao.po.qiuzhi.CityMessage;
import com.jsy_jiaobao.po.qiuzhi.GetAccIdbyNickname;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class QiuZhiPublishQuestionActivityController implements ConstantUrl {
	private static QiuZhiPublishQuestionActivityController instance;
	private Activity mContext;

	public static synchronized final QiuZhiPublishQuestionActivityController getInstance() {
		if (instance == null) {
			instance = new QiuZhiPublishQuestionActivityController();
		}
		return instance;
	}

	public QiuZhiPublishQuestionActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 上传图片
	 * 
	 * @param params
	 */
	public void uploadSectionImg(RequestParams params) {
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_uploadSectionImg);
		HttpUtil.InstanceSend(uploadSectionImg, params, callback);
	}

	public void GetAccIdbyNickname(String[] inputnames) {
		RequestParams params = new RequestParams();
		for (int i = 0; i < inputnames.length; i++) {
			params.addBodyParameter("nicknames", inputnames[i]);
		}
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
	 * 取系统中省份信息
	 */
	public void GetProvice() {
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetProvice);
		HttpUtil.InstanceSend(GetProvice, null, callback);
	}

	/**
	 * <pre>
	 * 功能：取指定省份的地市数据或取指定地市的区县数据。
	 * 参数名称	是否必须	类型	描述
	 * cityCode	是	string	当level=1，这个参数是省份代码，level=2，这个参数是地市代码
	 * level	是	int	1取地市数据，2取区县数据
	 */
	public void GetCity(String cityCode) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("cityCode", cityCode);
		params.addBodyParameter("level", String.valueOf(1));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetCity);
		HttpUtil.InstanceSend(GetCity, params, callback);
	}

	/**
	 * <pre>
	 * 功能：取指定省份的地市数据或取指定地市的区县数据。
	 * 参数名称	是否必须	类型	描述
	 * cityCode	是	string	当level=1，这个参数是省份代码，level=2，这个参数是地市代码
	 * level	是	int	1取地市数据，2取区县数据
	 */
	public void GetCounty(String cityCode) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("cityCode", cityCode);
		params.addBodyParameter("level", String.valueOf(2));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetCounty);
		HttpUtil.InstanceSend(GetCity, params, callback);
	}

	/**
	 * <pre>
	 * 功能：发布问题
	 * 参数名称	是否必须	类型	描述
	 * CategoryId	是	int	所属话题Id
	 * Title	是	string	标题 
	 * KnContent	是	string	问题内容
	 * TagsList	否	string	关键字，多个以,隔开
	 * QFlag	否	int	0=对回答无特殊要求，1=要求有证据的回答，默认为0
	 * AreaCode	否	string	区域代码
	 */
	public void NewQuestion(int CategoryId, String Title, String KnContent,
			String TagsList, String AreaCode, int QFlag) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("CategoryId", String.valueOf(CategoryId));
		params.addBodyParameter("Title", Title);
		params.addBodyParameter("KnContent", KnContent);
		params.addBodyParameter("TagsList", TagsList);
		params.addBodyParameter("QFlag", String.valueOf(QFlag));
		params.addBodyParameter("AreaCode", AreaCode);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_NewQuestion);
		HttpUtil.InstanceSend(NewQuestion, params, callback);
	}

	/**
	 * <pre>
	 * 邀请指定的用户回答问题
	 * 参数名称	是否必须	类型	描述
	 * accId	是	int	被邀请的用户教宝号
	 * qId	是	int	问题ID
	 */
	public void AtMeForAnswer(int accId, String qId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("accId", String.valueOf(accId));
		params.addBodyParameter("qId", qId);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AtMeForAnswer);
		HttpUtil.InstanceSend(AtMeForAnswer, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			DialogUtil.getInstance().cannleDialog();
			if (null != mContext) {
				dealResponseInfo("", this.getUserTag());
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, R.string.load_failed);
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
						case Constant.msgcenter_qiuzhi_GetAccIdbyNickname:
						case Constant.msgcenter_qiuzhi_GetProvice:
						case Constant.msgcenter_qiuzhi_GetCity:
						case Constant.msgcenter_qiuzhi_GetCounty:
						case Constant.msgcenter_qiuzhi_GetAtMeUsers:
						case Constant.msgcenter_qiuzhi_AtMeForAnswer:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_NewQuestion:
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
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
						if ((Integer) this.getUserTag() == Constant.msgcenter_qiuzhi_AtMeForAnswer) {
							dealResponseInfo(jsonObj.getString("Data"),
									this.getUserTag());
						} else if ((Integer) this.getUserTag() == Constant.msgcenter_qiuzhi_NewQuestion) {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("0", this.getUserTag());
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("", this.getUserTag());
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
		case Constant.msgcenter_qiuzhi_GetCounty:
			ArrayList<CityMessage> countylist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<CityMessage>>() {
					}.getType());
			post.add(countylist);
			break;
		case Constant.msgcenter_qiuzhi_GetCity:
			ArrayList<CityMessage> citylist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<CityMessage>>() {
					}.getType());
			post.add(citylist);
			break;
		case Constant.msgcenter_qiuzhi_GetProvice:
			ArrayList<CityMessage> provicelist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<CityMessage>>() {
					}.getType());
			post.add(provicelist);
			break;
		case Constant.msgcenter_qiuzhi_GetAccIdbyNickname:
			ArrayList<GetAccIdbyNickname> nicks = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<GetAccIdbyNickname>>() {
					}.getType());
			post.add(nicks);
			break;
		case Constant.msgcenter_qiuzhi_NewQuestion:
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_AtMeForAnswer:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_GetAtMeUsers:
			ArrayList<AtMeUser> users = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<AtMeUser>>() {
					}.getType());
			post.add(users);
			break;
		case Constant.msgcenter_qiuzhi_uploadSectionImg:
			UpFiles list2 = GsonUtil.GsonToObject(result, UpFiles.class);
			post.add(list2);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
