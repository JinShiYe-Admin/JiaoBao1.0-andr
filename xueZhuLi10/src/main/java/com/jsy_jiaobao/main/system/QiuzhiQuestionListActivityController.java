package com.jsy_jiaobao.main.system;

import android.content.Context;

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
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.MyAttQItem;
import com.jsy_jiaobao.po.qiuzhi.MyComms;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

public class QiuzhiQuestionListActivityController implements ConstantUrl{
	private static QiuzhiQuestionListActivityController instance;
	private Context mContext;
	public static synchronized QiuzhiQuestionListActivityController getInstance() {
		if (instance == null) {
			instance = new QiuzhiQuestionListActivityController();
		}
		return instance;
	}

	public QiuzhiQuestionListActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}
	/**<pre>取消关注某一问题
	 * 参数名称	是否必须	类型	描述
	 * qId	是	int	问题ID
	 */
	public void RemoveMyAttQ(int qId,int position){
		RequestParams params = new RequestParams();
		params.addBodyParameter("qId", String.valueOf(qId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_RemoveMyAttQ);
		callback.setPosition(position);
		HttpUtil.InstanceSend(RemoveMyAttQ, params, callback);
	}
	/**<pre>功能：获取我关注的问题列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void MyAttQIndex(int pageNum,int RowCount,int numPerPage){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage",String.valueOf(numPerPage));
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		params.addBodyParameter("RowCount",String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_MyAttQIndex);
		HttpUtil.InstanceSend(MyAttQIndex,params, callback);
	}
	/**<pre>获取邀请我回答的问题列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void AtMeQIndex(int pageNum,int RowCount){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage","10");
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		params.addBodyParameter("RowCount",String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AtMeQIndex);
		HttpUtil.InstanceSend(AtMeQIndex,params, callback);
	}
	/**<pre>获获取我提出的问题列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void MyQuestionIndex(int pageNum,int RowCount){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage","10");
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		params.addBodyParameter("RowCount",String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_MyQuestionIndex);
		HttpUtil.InstanceSend(MyQuestionIndex,params, callback);
	}
	/**<pre>获获取我提出的问题列表
	 * 参数名称	是否必须	类型	描述
	 * numPerPage	否	int	取回的记录数量，默认10
	 * pageNum	否	int	第几页，默认为1
	 * RowCount	是	int	记录数量，第一页赋值0，第二页由结果对象中的rowCount中取值
	 */
	public void MyAnswerIndex(int pageNum,int RowCount){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage","10");
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		params.addBodyParameter("RowCount",String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_MyAnswerIndex);
		HttpUtil.InstanceSend(MyAnswerIndex,params, callback);
	}
	//获取我我的评论列表
	public void GetMyComms(int pageNum,int RowCount){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("numPerPage","10");
		params.addBodyParameter("pageNum",String.valueOf(pageNum));
		params.addBodyParameter("RowCount",String.valueOf(RowCount));
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetMyComms);
		HttpUtil.InstanceSend(GetMyComms,params, callback);
	}
	/**
	 * <pre>
	 * 功能：获取一个答案明细信息，包括问题内容;
	 * 参数名称	是否必须	类型	描述
	 * AId	是	int	答案ID
	 */
	public void AnswerDetail(int AId, MyComms item){
		RequestParams params = new RequestParams();
		params.addBodyParameter("AId", String.valueOf(AId));
		params.addBodyParameter("byUrl", "1");
		CallBack callback = new CallBack();
		callback.setMyComms(item);
		callback.setUserTag(Constant.msgcenter_qiuzhi_AnswerDetail);
		HttpUtil.InstanceSend(AnswerDetail, params, callback);
		
	}
	/**
	 * 获取我关注的话题ID数组
	 */
	public void GetMyattCate(){
		if (!DialogUtil.getInstance().isDialogShowing()) {
			DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
			DialogUtil.getInstance().setCanCancel(false);
		}
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_GetMyattCate);
		HttpUtil.InstanceSend(GetMyattCate,null, callback);
	}
	/**
	 * 功能：更新我关注的话题
	 * 参数名称	是否必须	类型	描述
	 * uid	是	string	我关注的所有话题ID，用逗号连接成一个字符串,如：11,15,45       
	 */
	public void AddMyattCate(String uid){
		DialogUtil.getInstance().getDialog(mContext, mContext.getResources().getString(R.string.public_loading));
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",uid);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.msgcenter_qiuzhi_AddMyattCate);
		HttpUtil.InstanceSend(AddMyattCate,params, callback);
	}
	private class CallBack extends RequestCallBack<String>{
		private int mPosition;
		private MyComms myComms;
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false",this.getUserTag());
				if(BaseUtils.isNetworkAvailable(mContext)){
					ToastUtil.showMessage(mContext,R.string.phone_no_web);
					}else{
					ToastUtil.showMessage(mContext,mContext.getResources().getString(R.string.error_internet));
					}
			}
		}

	

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);//{"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						switch ((Integer)this.getUserTag()) {
						case Constant.msgcenter_qiuzhi_MyAttQIndex:
						case Constant.msgcenter_qiuzhi_AtMeQIndex:
						case Constant.msgcenter_qiuzhi_MyQuestionIndex:
						case Constant.msgcenter_qiuzhi_MyAnswerIndex:
						case Constant.msgcenter_qiuzhi_GetMyattCate:
						case Constant.msgcenter_qiuzhi_GetMyComms:
							dealResponseInfo(jsonObj.getString("Data"),this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_AnswerDetail:
							dealResponseInfo(jsonObj.getString("Data"),this.getUserTag(),getMyComms());
							break;
						case Constant.msgcenter_qiuzhi_AddMyattCate:
							dealResponseInfo("1",this.getUserTag());
							break;
						case Constant.msgcenter_qiuzhi_RemoveMyAttQ:
							dealResponseInfo(String.valueOf(mPosition), this.getUserTag());
							break;
						default:
							String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
							dealResponseInfo(data,this.getUserTag());
							break;
						}

					}else if("8".equals(ResultCode)){
						dealResponseInfo("false",this.getUserTag());
						LoginActivityController.getInstance().helloService(mContext);
					} else {
						ToastUtil.showMessage(mContext, jsonObj.getString("ResultDesc"));
						dealResponseInfo("false",this.getUserTag());
					}
				} catch (Exception e) {
					dealResponseInfo("",this.getUserTag());
					ToastUtil.showMessage(mContext, mContext.getResources().getString(R.string.error_serverconnect)+"r1002");
				} 
			}
		}



		public MyComms getMyComms() {
			return myComms;
		}

		public void setMyComms(MyComms myComms) {
			this.myComms = myComms;
		}



		public void setPosition(int position) {
			mPosition = position;
		}
		

	}
	private void dealResponseInfo(String result, Object userTag,MyComms myComms) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		AnswerDetails answer = GsonUtil.GsonToObject(result, AnswerDetails.class);
		if (answer != null) {
			if (answer.getState() == 0) {
				post.add(myComms);
			}else{
				myComms.setAnswerDetails(answer);
			}
		}else{
			post.add(myComms);
		}
		EventBusUtil.post(post);
	}
	ArrayList<String> hiddens;
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer)userTag) {
		case Constant.msgcenter_qiuzhi_AddMyattCate:
		case Constant.msgcenter_qiuzhi_RemoveMyAttQ:
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_GetMyattCate:
			DialogUtil.getInstance().cannleDialog();
			post.add(result);
			break;
		case Constant.msgcenter_qiuzhi_GetMyComms:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<MyComms> comms = GsonUtil.GsonToList(result, new TypeToken<ArrayList<MyComms>>() {}.getType());
			if (comms != null) {
				for (MyComms item : comms) {
					AnswerDetail(item.getAId(),item);
				}
			}
			post.add(comms);
			break;
		case Constant.msgcenter_qiuzhi_MyAttQIndex:
			ArrayList<MyAttQItem> list = GsonUtil.GsonToList(result, new TypeToken<ArrayList<MyAttQItem>>() {}.getType());
			if (list != null && list.size()>0) {
				hiddens = list.get(0).getHiddenid();
			}
			post.add(list);
			post.add(hiddens==null?0:hiddens.size());
			break;
		case Constant.msgcenter_qiuzhi_MyQuestionIndex:
		case Constant.msgcenter_qiuzhi_AtMeQIndex:
		case Constant.msgcenter_qiuzhi_MyAnswerIndex:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<MyAttQItem> list1 = GsonUtil.GsonToList(result, new TypeToken<ArrayList<MyAttQItem>>() {}.getType());
			if (list1 != null && list1.size()>0) {
				hiddens = list1.get(0).getHiddenid();
			}
			post.add(list1);
			post.add(hiddens==null?0:hiddens.size());
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}