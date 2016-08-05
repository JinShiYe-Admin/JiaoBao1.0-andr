package com.jsy_jiaobao.main.appcenter.sign;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.VisitPublicHttp;
import com.jsy_jiaobao.main.workol.Constants;
import com.jsy_jiaobao.po.sign.GetSignWay;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ShowPopup implements ConstantUrl{
	private PopupWindow ppw;
	private Context mcontext;
//	private ListView parent,child;
	private ParentAdapter parentAdapter;
	private ArrayAdapter childAdapter;
	private List<UserIdentity> listUserIdentity;
	private List<UserUnit> userUnit;
	private List<UserClass> UserClasses;
	private SharedPreferences sp;
	private Editor editor;
	private ProgressDialog dialog;
	private String place;
	
	private int uType;
	
	List<String> childData = new ArrayList<String>();
	public ShowPopup(Context context){
		this.mcontext = context;
		sp = mcontext.getSharedPreferences(Constant.SP_TB_USER, mcontext.MODE_PRIVATE);
		editor = sp.edit();
		dialog = BaseUtils.showDialog(mcontext, R.string.getting_checkAttendanceModel_message_waiting);
		dialog.setCanceledOnTouchOutside(false);
		VisitPublicHttp.getInstance().setContext((Activity) context);
	}
	public void setVisitPlace(String place){
		this.place = place;
	}
	@ViewInject(R.id.popup_choseunit_listview_parent)ListView parent;
	@ViewInject(R.id.popup_choseunit_listview_child)ListView child;
	public void showPop(View layout,int y,final List<UserIdentity> listUserIdentity, final Handler mHandler) {
		this.listUserIdentity =listUserIdentity ;
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		final View popupLayout = inflater.inflate(R.layout.popup_choseunit, null);
		ViewUtils.inject( this,popupLayout);
		 
		ppw = new PopupWindow(mcontext);
		ppw.setBackgroundDrawable(new BitmapDrawable());
		ppw.setWidth(LayoutParams.MATCH_PARENT);
		ppw.setHeight( LayoutParams.WRAP_CONTENT);
		ppw.setOutsideTouchable(false);
		ppw.setFocusable(true);
		ppw.setContentView(popupLayout);
		ppw.showAtLocation(layout, Gravity.CENTER|Gravity.TOP, 0, y);
		List<String> parentData = new ArrayList<String>();
		for (int i = 0; i < listUserIdentity.size(); i++) {
			parentData.add(listUserIdentity.get(i).getRoleIdName());
		}
		parentAdapter = new ParentAdapter(mcontext);
		parentAdapter.setData(parentData);
		parentAdapter.setSelect(0);
		uType = listUserIdentity.get(0).getRoleIdentity();
		childData = getChildData(0);
		childAdapter = new ArrayAdapter<String>(mcontext,android.R.layout.simple_list_item_1,childData);
		parent.setAdapter(parentAdapter);
		child.setAdapter(childAdapter);
		parent.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		parent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				uType = listUserIdentity.get(position).getRoleIdentity();
				childData = getChildData(position);
				childAdapter.notifyDataSetChanged();
				parentAdapter.setSelect(position);
				parentAdapter.notifyDataSetChanged();
			}
		});
		child.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				ppw.dismiss();
				editor.putInt("RoleIdentity", uType).commit();

				if (uType == 1) {
					if (mHandler != null) {
						Message msg = new Message();
						msg.obj = userUnit.get(position).getUnitName();
						msg.what = 10;
						mHandler.sendMessage(msg);
					}
					editor.putInt("UnitID", userUnit.get(position).getUnitID());
					editor.putInt("UnitType", userUnit.get(position).getUnitType());
					editor.putString("UnitName", userUnit.get(position).getUnitName());
					editor.putString("TabIDStr", userUnit.get(position).getTabIDStr());
					editor.commit();	
				}else if (uType == 2) {
					if (mHandler != null) {
						Message msg = new Message();
						msg.obj = userUnit.get(position).getUnitName();
						msg.what = 10;
						mHandler.sendMessage(msg);
					}
					editor.putInt("UnitID", userUnit.get(position).getUnitID());
					editor.putInt("UnitType", userUnit.get(position).getUnitType());
					editor.putString("UnitName", userUnit.get(position).getUnitName());
					editor.putString("TabIDStr", userUnit.get(position).getTabIDStr());
					editor.commit();	
				}else{
					if (mHandler != null) {
						Message msg = new Message();
						msg.obj = UserClasses.get(position).getClassName();
						msg.arg1 = 10;
						mHandler.sendMessage(msg);
					}
					editor.putInt("UnitID", UserClasses.get(position).getSchoolID());
					editor.putInt("UnitType", 2);
					editor.putString("UnitName", UserClasses.get(position).getClassName());
					editor.putString("TabIDStr", UserClasses.get(position).getTabIDStr());
					editor.commit();	
					GetLogin(UserClasses.get(position).getClassID());
				}
//				ArrayList<Object> post = new ArrayList<Object>();
//				post.add(Constant.msgcenter_work_change);
//				EventBusUtil.post(post);
				if (Constant.listParentSignWay != null) {
					Constant.listParentSignWay.clear();
				}
				VisitPublicHttp.getInstance().changeCurUnit(true);
				if ("sign".equals(place)) {
					dialog.show();
					httpGetSignWay();
				}
			}

		});
	}
    /**
     * 参数名称	是否必须	类型	描述
     * equipmentId	是	int	设备ID(1 android2ios)
     * classId	是	int	班级ID
     * accountId	是	int	教宝号
     * accountType	是	int	角色(1、家长2、学生)
     * @param i 
     */
    private void GetLogin(int i){
    	int RoleIdentity = sp.getInt("RoleIdentity", 0);
    	if (RoleIdentity>2) {
    		RequestParams params = new RequestParams();
    		params.addBodyParameter("equipmentId","1");
    		params.addBodyParameter("accountId",sp.getString("JiaoBaoHao", ""));
    		params.addBodyParameter("classId",String.valueOf(i));
    		params.addBodyParameter("accountType",String.valueOf(RoleIdentity));
    		HttpUtil.getInstanceNew().send(HttpRequest.HttpMethod.POST, Constants.GetLogin, params,null);
		}
    }
	private class ParentAdapter extends BaseAdapter{
		List<String> list;
		Context context;
		private LayoutInflater mInflater;
		int flag;
		public ParentAdapter(Context context){
			this.context = context;
			mInflater = LayoutInflater.from(context);
		}
		public void setData(List<String> list){
			this.list = list;
		}
		public void setSelect(int flag){
			this.flag = flag;
		}
		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder viewHolder = ViewHolder.get(context, convertView, arg2,R.layout.simple_list_item_1, arg0);
			TextView parent = viewHolder.getView(R.id.simple_list_item_1);
			parent.setText(list.get(arg0));
			parent.setBackgroundColor(context.getResources().getColor(R.color.white));
			if (arg0 == flag) {
				parent.setBackgroundColor(context.getResources().getColor(R.color.sendmessage_bg));
			}
			return viewHolder.getConvertView();
		}
		
	}

	private List<String> getChildData(int position){
		if (childData!= null) {
			childData.clear();
		}
		if (uType == 1 ) {
			userUnit = listUserIdentity.get(position).getUserUnits();
			for (int i = 0; i < userUnit.size(); i++) {
				childData.add(userUnit.get(i).getUnitName());
			}
		}else if (uType == 2) {
			userUnit = listUserIdentity.get(position).getUserUnits();
			for (int i = 0; i < userUnit.size(); i++) {
				childData.add(userUnit.get(i).getUnitName());
			}
//			UserClasses = listUserIdentity.get(position).getUserClasses();
//			for (int i = 0; i < UserClasses.size(); i++) {
//				childData.add(UserClasses.get(i).getClassName()+"(班级)");
//			}
		}else{
			UserClasses = listUserIdentity.get(position).getUserClasses();
			for (int i = 0; i < UserClasses.size(); i++) {
				childData.add(UserClasses.get(i).getClassName());
			}
		}
		return childData;
	}
	private void httpGetSignWay(){
		try {
			
			RequestParams params = new RequestParams();
			params.addBodyParameter("unitid",sp.getInt("UnitID", 0)+"");
			new HttpUtils().send(HttpRequest.HttpMethod.POST, getSignWay, params,new RequestCallBack<String>() {
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					dialog.dismiss();
					BaseUtils.shortToast(mcontext, mcontext.getResources().getString(R.string.error_serverconnect));
				}
				
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					dialog.dismiss();
					ppw.dismiss();
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);
						String ResultCode = jsonObj.getString("ResultCode");
						
						if ("0".equals(ResultCode)) {
							GetSignWay getSignWay = GsonUtil.GsonToObject(arg0.result, GetSignWay.class);
							
							Constant.listParentSignWay = getSignWay.getData();
						} else {
							Constant.listParentSignWay.clear();
							BaseUtils.shortToast(mcontext,mcontext.getResources().getString(R.string.get_attendanceModel_list)+jsonObj.getString("ResultDesc"));
						}
					} catch (Exception e) {
						BaseUtils.shortToast(mcontext, R.string.no_attendanceModel);
					} 
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
