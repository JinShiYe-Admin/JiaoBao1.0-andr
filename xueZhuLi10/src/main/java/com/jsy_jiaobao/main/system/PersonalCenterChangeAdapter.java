package com.jsy_jiaobao.main.system;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.MyMobileUnit;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 个人中心修改昵称，姓名，密码，加入单位的Adapter
 * 
 * @author admin
 * 
 */
public class PersonalCenterChangeAdapter extends BaseAdapter implements
		ConstantUrl {

	private Context mContext;
	private List<MyMobileUnit> mData = new ArrayList<MyMobileUnit>();

	public PersonalCenterChangeAdapter(Context mContext) {
		this.mContext = mContext;
	}

	public void setData(List<MyMobileUnit> mData) {
		this.mData.clear();
		for (int i = 0; i < mData.size(); i++) {
			if (mData.get(i).getJoinFlag() < 2) {
				boolean have = false;
				if (Constant.listUserIdentity != null) {
					x: for (int i1 = 0; i1 < Constant.listUserIdentity.size(); i1++) {
						UserIdentity userIdentity = Constant.listUserIdentity
								.get(i1);
						if (userIdentity.getRoleIdentity() == 1) {
							List<UserUnit> UserUnits = userIdentity
									.getUserUnits();
							if (UserUnits != null) {
								for (int j = 0; j < UserUnits.size(); j++) {
									UserUnit unit = UserUnits.get(j);
									if (mData.get(i).getTabIdStr()
											.equals(unit.getTabIDStr())) {
										have = true;
										break x;
									}
								}
							}
							// 老师
						} else if (userIdentity.getRoleIdentity() == 2) {
							List<UserUnit> UserUnits = userIdentity
									.getUserUnits();
							if (UserUnits != null) {
								for (int j = 0; j < UserUnits.size(); j++) {
									UserUnit unit = UserUnits.get(j);
									if (mData.get(i).getTabIdStr()
											.equals(unit.getTabIDStr())) {
										have = true;
										break;
									}
								}
							}
							List<UserClass> userClasses = userIdentity
									.getUserClasses();
							if (userClasses != null) {
								for (int j = 0; j < userClasses.size(); j++) {
									UserClass unit = userClasses.get(j);
									if (mData.get(i).getTabIdStr()
											.equals(unit.getTabIDStr())) {
										have = true;
										break;
									}
								}
							}
							// 家长
						} else if (userIdentity.getRoleIdentity() == 3) {
							List<UserClass> userClasses = userIdentity
									.getUserClasses();
							if (userClasses != null) {
								for (int j = 0; j < userClasses.size(); j++) {
									UserClass unit = userClasses.get(j);
									if (mData.get(i).getTabIdStr()
											.equals(unit.getTabIDStr())) {
										have = true;
										break;
									}
								}
							}
							// 学生
						} else if (userIdentity.getRoleIdentity() == 4) {
							List<UserClass> userClasses = userIdentity
									.getUserClasses();
							if (userClasses != null) {
								for (int j = 0; j < userClasses.size(); j++) {
									UserClass unit = userClasses.get(j);
									if (mData.get(i).getTabIdStr()
											.equals(unit.getTabIDStr())) {
										have = true;
										break;
									}
								}
							}
						}
					}
				}
				if (have) {
					if (mData.get(i).getAccId() == 0) {
						mData.get(i).setAccId(
								Integer.parseInt(BaseActivity.sp.getString(
										"JiaoBaoHao", "")));
					}
				}
				this.mData.add(mData.get(i));
			}
		}

	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_personalcenter_change_unitlist, position);
		TextView name = viewHolder.getView(R.id.item_pcc_tv_unitname);
		TextView type = viewHolder.getView(R.id.item_pcc_tv_unittype);
		final TextView join = viewHolder.getView(R.id.item_pcc_tv_join);
		final MyMobileUnit item = (MyMobileUnit) getItem(position);
		name.setText(mContext.getResources().getString(R.string.unit_)
				+ item.getUnitName());
		type.setText(mContext.getResources().getString(R.string.identity_)
				+ item.getIdentity()
				+ mContext.getResources().getString(R.string.name_)
				+ item.getMName());
		if (item.getAccId() == 0) {
			join.setVisibility(0);
		} else {
			join.setVisibility(View.GONE);
		}
		join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					join.setEnabled(false);
					DialogUtil.getInstance().getDialog(mContext,
							R.string.fucking_waiting);
					DialogUtil.getInstance().setCanCancel(false);
					RequestParams params = new RequestParams();
					params.addBodyParameter("accId",
							BaseActivity.sp.getString("JiaoBaoHao", ""));
					params.addBodyParameter("op", "0");
					params.addBodyParameter("tabIdStr", item.getTabIdStr());
					HttpUtil.InstanceSend(JoinUnitOP, params,
							new RequestCallBack<String>() {

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									join.setEnabled(true);
									DialogUtil.getInstance().cannleDialog();
									if (null != mContext) {
										try {
											JSONObject jsonObj = new JSONObject(
													arg0.result);// {"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
											String ResultCode = jsonObj
													.getString("ResultCode");
											if ("0".equals(ResultCode)) {
												VisitPublicHttp
														.getInstance()
														.setContext(
																(Activity) mContext);
												VisitPublicHttp.getInstance()
														.getRoleIdentity();
												item.setAccId(Integer
														.parseInt(BaseActivity.sp
																.getString(
																		"JiaoBaoHao",
																		"")));
												PersonalCenterChangeAdapter.this
														.notifyDataSetChanged();
											}
										} catch (Exception e) {
											ToastUtil
													.showMessage(
															mContext,
															mContext.getResources()
																	.getString(
																			R.string.error_serverconnect)
																	+ "r1002");
										}
									}
								}

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									join.setEnabled(true);
									DialogUtil.getInstance().cannleDialog();
								}
							});
				} catch (Exception e) {
					DialogUtil.getInstance().cannleDialog();
					if (join != null) {
						join.setEnabled(true);
					}
					e.printStackTrace();
				}
			}
		});
		return viewHolder.getConvertView();
	}

}
