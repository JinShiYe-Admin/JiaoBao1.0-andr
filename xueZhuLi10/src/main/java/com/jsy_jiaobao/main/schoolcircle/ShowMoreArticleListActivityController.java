package com.jsy_jiaobao.main.schoolcircle;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowMoreArticleListActivityController implements ConstantUrl{
	private static ShowMoreArticleListActivityController instance;
	private Activity mContext;

	public static synchronized ShowMoreArticleListActivityController getInstance() {
		if (instance == null) {
			instance = new ShowMoreArticleListActivityController();
		}
		return instance;
	}

	public ShowMoreArticleListActivityController setContext(Activity pActivity) {
		mContext = pActivity;
		return this;
	}
	/**
	 * 取本单位栏目文章
	 * 客户端通过本接口获取本单位栏目文章。
	 */
	public void UnitArthListIndex(RequestParams params){
		params.addBodyParameter("numPerPage",String.valueOf(20));
		params.addBodyParameter("UnitID",String.valueOf(BaseActivity.sp.getInt("UnitID", 0)));
		params.addBodyParameter("SectionFlag","2");//1共享，2展示,99个人空间文章
		params.addBodyParameter("orderDirection","0");//0按最新排序，1按最热排序，默认为0
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_UnitArthListIndex_more);
		HttpUtil.InstanceSend(UnitArthListIndex, params, callback);
	}
	/**
	 * 取本单位栏目文章
	 * 客户端通过本接口获取本单位栏目文章。
	 */
	public void AllMyClassArthList(RequestParams params){
		params.addBodyParameter("sectionFlag",String.valueOf(2));
		params.addBodyParameter("numPerPage",String.valueOf(20));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_show_AllMyClassArthList_more);
		HttpUtil.InstanceSend(AllMyClassArthList, params, callback);
	}

	private class CallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				if(BaseUtils.isNetworkAvailable(mContext)){
					ToastUtil.showMessage(mContext,R.string.phone_no_web);
				}
				dealResponseInfo("",this.getUserTag());
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
 					if ("0".equals(ResultCode)) {
 						switch ((Integer)this.getUserTag()) {
						case Constant.msgcenter_show_UnitArthListIndex_more:
						case Constant.msgcenter_show_AllMyClassArthList_more:
						case Constant.msgcenter_show_AllMyClassArthList_more_select:
							dealResponseInfo(jsonObj.getString("Data"),this.getUserTag());
							break;
						default:
							String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
							dealResponseInfo(data,this.getUserTag());
							break;
						}
					}else if("8".equals(ResultCode)){
						dealResponseInfo("",this.getUserTag());
						LoginActivityController.getInstance().helloService(mContext);
					} else {
						ToastUtil.showMessage(mContext,jsonObj.getString("ResultDesc"));
						dealResponseInfo("",this.getUserTag());
					}
				} catch (Exception e) {
					dealResponseInfo("",this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}
	}
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer)userTag) {
		case Constant.msgcenter_show_AllMyClassArthList_more_select:
		case Constant.msgcenter_show_UnitArthListIndex_more:
		case Constant.msgcenter_show_AllMyClassArthList_more:
			ArrayList<ArthInfo> list = GsonUtil.GsonToList(result, new TypeToken<ArrayList<ArthInfo>>() {}.getType());
			post.add(list);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
