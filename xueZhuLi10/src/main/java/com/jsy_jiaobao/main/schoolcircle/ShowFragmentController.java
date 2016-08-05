package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
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
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.GetUnitSectionMessage;
import com.jsy_jiaobao.po.personal.MyAttUnit;
import com.jsy_jiaobao.po.personal.NoticeGetArthInfo;
import com.jsy_jiaobao.po.personal.NoticeGetUnitClass;
import com.jsy_jiaobao.po.sys.GetUserClass;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 学校圈主界面Controller
 * 
 * @author admin
 * 
 */
public class ShowFragmentController implements ConstantUrl {
	private static ShowFragmentController instance;
	private Fragment mcontext;
	private ProgressDialog dialog;
	private Context mContext;

	public static synchronized final ShowFragmentController getInstance() {
		if (instance == null) {
			instance = new ShowFragmentController();
		}
		return instance;
	}

	public ShowFragmentController setContext(Fragment noticeFragment) {
		mcontext = noticeFragment;
		mContext = noticeFragment.getActivity();
		return this;
	}

	private void getDialog(Context pContext, String pMessage) {

		dialog = new ProgressDialog(pContext);
		dialog.setMessage(pMessage);
		dialog.show();
	}

	// private void cannleDialog() {
	// if (dialog != null) {
	// if (dialog.isShowing()) {
	// dialog.dismiss();
	// }
	// }
	// }
	/**
	 * 取我关联的班级 应用系统通过帐户ID和单位ID，获取帐户关联的班级信息
	 * 
	 * @param layout
	 * @param params
	 */
	public void getmyUserClass(String string) {
		getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", string);
		params.addBodyParameter("AccID",
				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getmyUserClass);
		HttpUtil.InstanceSend(getmyUserClass, params, callback);

	}

	/**
	 * 获取指定学校的所有班级基础数据 客户端通过本接口获取指定学校的所有班级基础数据。基础信息数据包括名称，单位ID，栏目文章数量和文章更新数据等
	 */
	public void getSchoolClassInfo(int UnitID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(UnitID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getSchoolClassInfo);
		HttpUtil.InstanceSend(getSchoolClassInfo, params, callback);

	}

	/**
	 * 客户端通过本接口获取栏目最新和推荐文章。 
	 * 
	 * @param params
	 * @param show
	 */
	public void TopArthListIndex(RequestParams params) {
		getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		params.addBodyParameter("SectionFlag", "2");
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_TopArthListIndex);
		HttpUtil.InstanceSend(TopArthListIndex, params, callback);

	}

	/**
	 * 取本单位栏目文章 客户端通过本接口获取本单位栏目文章。
	 * 
	 * @param params
	 */
	public void ArthListIndex(RequestParams params) {
		getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_ArthListIndex);
		HttpUtil.InstanceSend(ArthListIndex, params, callback);

	}

	/**
	 * 取本单位栏目文章 客户端通过本接口获取本单位栏目文章。
	 * 
	 * @param params
	 */
	public void UnitArthListIndex() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNum", String.valueOf(1));
		params.addBodyParameter("numPerPage", String.valueOf(1));
		params.addBodyParameter("UnitID",
				String.valueOf(BaseActivity.sp.getInt("UnitID", 0)));
		params.addBodyParameter("SectionFlag", "2");// 1共享，2展示,99个人空间文章
		params.addBodyParameter("orderDirection", "0");// 0按最新排序，1按最热排序，默认为0
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_UnitArthListIndex);
		HttpUtil.InstanceSend(UnitArthListIndex, params, callback);

	}

	/**
	 * 取本单位栏目文章 客户端通过本接口获取本单位栏目文章。
	 * 
	 * @param params
	 */
	public void PersonArthListIndex(RequestParams params) {
		params.addBodyParameter("numPerPage", String.valueOf(5));
		params.addBodyParameter("UnitID",
				String.valueOf(BaseActivity.sp.getInt("UnitID", 0)));
		params.addBodyParameter("SectionFlag", "1");// 1共享，2展示,99个人空间文章
		params.addBodyParameter("orderDirection", "0");// 0按最新排序，1按最热排序，默认为0
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_PersonArthListIndex);
		HttpUtil.InstanceSend(UnitArthListIndex, params, callback);

	}

	/**
	 * 取最新单位栏目文章-本地 功能：取单位空间发表的最新或推荐文章,（包含官方文章和非官方文章,即单位展示和单位分享）
	 * 
	 * @param params
	 */
	public void ShowingUnitArthListLocal(RequestParams params) {
		params.addBodyParameter("numPerPage", "5");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_ShowingUnitArthListLocal);
		HttpUtil.InstanceSend(ShowingUnitArthList, params, callback);

	}

	/**
	 * 取最新单位栏目文章-全部 功能：取单位空间发表的最新或推荐文章,（包含官方文章和非官方文章,即单位展示和单位分享）
	 * 
	 * @param params
	 */
	public void ShowingUnitArthList(RequestParams params) {
		params.addBodyParameter("numPerPage", "5");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_ShowingUnitArthListAll);
		HttpUtil.InstanceSend(ShowingUnitArthList, params, callback);

	}

	/**
	 * 取我关注的单位栏目文章 功能：取我关注单位的文章列表,（包含官方文章和非官方文章,即单位展示和单位分享）
	 * 
	 * @param params
	 */
	public void MyAttUnitArthListIndex(RequestParams params) {
		params.addBodyParameter("numPerPage", "5");//
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_MyAttUnitArthListIndex);
		HttpUtil.InstanceSend(MyAttUnitArthListIndex, params, callback);

	}

	/**
	 * 我的班级文章列表 功能：取我所在班级（所有的班级）的所有文章列表，注意取时是按当前身份来获取的。所以要注意切换单位。
	 * 
	 * @param params
	 * @param sectionFlag
	 *            1个人发布文章，2单位动态
	 */
	public void AllMyClassArthList(RequestParams params, int sectionFlag) {
		CallBack callback = new CallBack();
		if (sectionFlag == 1) {
			callback.setUserTag(Constant.msgcenter_show_AllMyClassArthList_1);
			params.addBodyParameter("numPerPage", "5");//
		} else if (sectionFlag == 2) {
			callback.setUserTag(Constant.msgcenter_show_AllMyClassArthList_2);
		}
		HttpUtil.InstanceSend(AllMyClassArthList, params, callback);

	}

	/**
	 * 取本单位栏目文章 客户端通过本接口获取本单位栏目文章。
	 * 
	 * @param params
	 */
	public void SchoolArthListIndex(RequestParams params) {
		getDialog(mcontext.getActivity(),
				mcontext.getResources().getString(R.string.public_loading));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_SchoolArthListIndex);
		HttpUtil.InstanceSend(ArthListIndex, params, callback);

	}

	/**
	 * 取最新未读数量
	 * 
	 * @param params
	 */
	public void getSectionMessageNew() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("SectionID", "_1");
		params.addBodyParameter("TopFlags", "1");
		params.addBodyParameter("AccID",
				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getSectionMessageNew);
		HttpUtil.InstanceSend(getSectionMessage, params, callback);

	}

	/**
	 * 取推荐未读数量
	 * 
	 * @param params
	 */
	public void getSectionMessageSuggest() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("SectionID", "_1");
		params.addBodyParameter("TopFlags", "2");
		params.addBodyParameter("AccID",
				MessageCenterActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getSectionMessageSuggest);
		HttpUtil.InstanceSend(getSectionMessage, params, callback);

	}

	/**
	 * 取我相关单位的列表及栏目未读文章数量
	 */
	public void getUnitSectionMessages() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("SectionID", "2");
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_getUnitSectionMessages);
		HttpUtil.InstanceSend(getUnitSectionMessages, params, callback);

	}

	/**
	 * 取我相关单位的列表及栏目未读文章数量
	 */
	public void GetMyAttUnit() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("JiaoBaoHao",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		HttpUtil.getInstanceNew().send(
				HttpRequest.HttpMethod.POST,
				ACache.get(mcontext.getActivity().getApplicationContext())
						.getAsString("MainUrl") + GetMyAttUnit, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						DialogUtil.getInstance().cannleDialog();

						if (mContext != null) {
							if (BaseUtils.isNetworkAvailable(mContext)) {
								ToastUtil.showMessage(mContext,
										R.string.phone_no_web);
							} else {
								ToastUtil.showMessage(mContext,
										R.string.load_failed);
							}
							try {
								if (mcontext.isAdded()
										&& !mcontext.isDetached()) {
									// cannleDialog();
									dealResponseInfo(
											"0",
											Constant.msgcenter_show_GetMyAttUnit);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (mcontext.isAdded() && !mcontext.isDetached()) {
							// cannleDialog();
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");
								if ("0".equals(ResultCode)) {
									dealResponseInfo(
											jsonObj.getString("Data"),
											Constant.msgcenter_show_GetMyAttUnit);

								} else if ("8".equals(ResultCode)) {
									dealResponseInfo(
											"0",
											Constant.msgcenter_show_GetMyAttUnit);
									LoginActivityController.getInstance()
											.helloService(
													mcontext.getActivity());
								} else {
									dealResponseInfo(
											"0",
											Constant.msgcenter_show_GetMyAttUnit);
									ToastUtil.showMessage(
											mcontext.getActivity(),
											jsonObj.getString("ResultDesc"));
								}
							} catch (Exception e) {
								dealResponseInfo("0",
										Constant.msgcenter_show_GetMyAttUnit);
								ToastUtil.showMessage(
										mcontext.getActivity(),
										mcontext.getResources().getString(
												R.string.error_serverconnect)
												+ "r1002");
							}
						}
					}
				});
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				DialogUtil.getInstance().cannleDialog();

				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, R.string.load_failed);
				}
				try {
					if (mcontext.isAdded() && !mcontext.isDetached()) {
						// cannleDialog();
						dealResponseInfo("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext.isAdded() && !mcontext.isDetached()) {
				// cannleDialog();
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer) getUserTag()) {
						case Constant.msgcenter_show_UnitArthListIndex:
						case Constant.msgcenter_show_PersonArthListIndex:
						case Constant.msgcenter_show_ShowingUnitArthListLocal:
						case Constant.msgcenter_show_ShowingUnitArthListAll:
						case Constant.msgcenter_show_MyAttUnitArthListIndex:
						case Constant.msgcenter_show_AllMyClassArthList_1:
						case Constant.msgcenter_show_AllMyClassArthList_2:
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
						dealResponseInfo("0", this.getUserTag());
						LoginActivityController.getInstance().helloService(
								mcontext.getActivity());
					} else {
						dealResponseInfo("0", this.getUserTag());
					}
				} catch (Exception e) {
					dealResponseInfo("0", this.getUserTag());
					ToastUtil.showMessage(
							mcontext.getActivity(),
							mcontext.getResources().getString(
									R.string.error_serverconnect)
									+ "r1002");
				}
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		NoticeGetArthInfo getArthInfo;
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constant.msgcenter_show_TopArthListIndex:
			result = "{\"list\":" + result + "}";
			getArthInfo = GsonUtil
					.GsonToObject(result, NoticeGetArthInfo.class);
			post.add(getArthInfo);
			break;
		case Constant.msgcenter_show_ArthListIndex:
			result = "{\"list\":" + result + "}";
			getArthInfo = GsonUtil
					.GsonToObject(result, NoticeGetArthInfo.class);
			post.add(getArthInfo);
			break;
		case Constant.msgcenter_show_UnitArthListIndex:
		case Constant.msgcenter_show_PersonArthListIndex:
		case Constant.msgcenter_show_ShowingUnitArthListLocal:
		case Constant.msgcenter_show_ShowingUnitArthListAll:
		case Constant.msgcenter_show_MyAttUnitArthListIndex:
		case Constant.msgcenter_show_AllMyClassArthList_2:
		case Constant.msgcenter_show_AllMyClassArthList_1:
			ArrayList<ArthInfo> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<ArthInfo>>() {
					}.getType());
			post.add(list);
			break;
		case Constant.msgcenter_show_SchoolArthListIndex:
			result = "{\"list\":" + result + "}";
			getArthInfo = GsonUtil
					.GsonToObject(result, NoticeGetArthInfo.class);
			post.add(getArthInfo);
			break;
		case Constant.msgcenter_show_getmyUserClass:
			result = "{\"list\":" + result + "}";
			GetUserClass getUserClass = GsonUtil.GsonToObject(result,
					GetUserClass.class);
			post.add(getUserClass);
			break;
		case Constant.msgcenter_show_getSchoolClassInfo:
			result = "{\"list\":" + result + "}";
			NoticeGetUnitClass getUnitClass = GsonUtil.GsonToObject(result,
					NoticeGetUnitClass.class);
			post.add(getUnitClass);
			break;
		case Constant.msgcenter_show_getSectionMessageNew:
			post.add(Integer.parseInt(result));
			break;
		case Constant.msgcenter_show_getSectionMessageSuggest:
			post.add(Integer.parseInt(result));
			break;
		case Constant.msgcenter_show_getUnitSectionMessages:
			result = "{\"list\":" + result + "}";
			GetUnitSectionMessage getUnitSectionMessage = GsonUtil
					.GsonToObject(result, GetUnitSectionMessage.class);
			post.add(getUnitSectionMessage);
			break;
		case Constant.msgcenter_show_GetMyAttUnit:
			ArrayList<MyAttUnit> getMyAttUnitList = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<MyAttUnit>>() {
					}.getType());
			post.add(getMyAttUnitList);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
