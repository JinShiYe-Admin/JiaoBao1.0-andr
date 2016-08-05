package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HanyuPinyin;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.CusExpandableListView;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.GetUnitRevicer;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

/**
 * 内部事务Fragment
 */
public class WorkSendUnitFragment extends Fragment implements OnClickListener,
		OnGroupClickListener {
	public static final String TAG = WorkSendUnitFragment.class.getName();
	private RequestParams params;
	private List<NameValuePair> reciverList = new ArrayList<NameValuePair>();
	private Activity mActivity;
	private TextView tv_unitname;// 所属单位名称
	private CusExpandableListView listView;// 列表
	private CheckBox cb_invert;// 反选
	private CheckBox cb_selectall;// 全选
	private WorkSendUnitExpanListAdapter expanAdapter;

	public static WorkSendUnitFragment newInstance() {
		WorkSendUnitFragment fragment = new WorkSendUnitFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_worksend_unit, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		WorkSendFragmentController.getInstance().setContext(this);
		mActivity = getActivity();
		initViews(view);
		super.onViewCreated(view, savedInstanceState);
	}

	private void initViews(View view) {
		tv_unitname = (TextView) view
				.findViewById(R.id.worksend_unit_tv_unitname);
		cb_invert = (CheckBox) view.findViewById(R.id.worksend_unit_cb_invert);
		cb_selectall = (CheckBox) view
				.findViewById(R.id.worksend_unit_cb_selectall);
		listView = (CusExpandableListView) view
				.findViewById(R.id.worksend_unit_expanlistview);
		listView.setGroupIndicator(null);
		cb_invert.setOnClickListener(this);
		cb_selectall.setOnClickListener(this);
		expanAdapter = new WorkSendUnitExpanListAdapter(mActivity, mHandler);
		listView.setAdapter(expanAdapter);
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				if ((Boolean) msg.obj) {
					listView.collapseGroup(msg.arg1);
				} else {
					listView.expandGroup(msg.arg1);
				}
				break;
			case 101:
				expanAdapter.notifyDataSetChanged();
				cb_selectall.setChecked(true);
				x: for (int i = 0; i < expanAdapter.mChecked.size(); i++) {
					ArrayList<Boolean> childList = expanAdapter.mChecked.get(i);
					for (int j = 0; j < childList.size(); j++) {
						if (!childList.get(j)) {
							cb_selectall.setChecked(false);
							break x;
						}
					}
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.worksend_unit_cb_invert:// 反选
			MobclickAgent.onEvent(
					mActivity,
					getResources().getString(
							R.string.WorkSendUnitFragment_invert));
			boolean selectall = expanAdapter.setAllSelitInvert();
			cb_selectall.setChecked(selectall);
			expanAdapter.notifyDataSetChanged();
			break;
		case R.id.worksend_unit_cb_selectall:// 全选
			MobclickAgent.onEvent(
					mActivity,
					getResources().getString(
							R.string.WorkSendUnitFragment_selectall));
			expanAdapter.setAllSelitCheckFlag(cb_selectall.isChecked());
			expanAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * 发送时弹出确认提示框
	 * 
	 * @param count
	 */
	protected void dialog_send(int count) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确定要向" + count + "人发送信息吗?");
		builder.setTitle(R.string.hint);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MobclickAgent.onEvent(mActivity, getResources()
								.getString(R.string.WorkSendUnitFragment_sure));
						dialog.dismiss();
						sendMessage();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MobclickAgent.onEvent(
								mActivity,
								getResources().getString(
										R.string.WorkSendUnitFragment_cancel));
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 发送信息
	 */
	private void sendMessage() {
		params.addBodyParameter(reciverList);
		params.addBodyParameter("grsms", String.valueOf(false));
		WorkSendFragmentController.getInstance().CreateCommMsg(params);
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {

		return false;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WorkSendUnitFragment");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WorkSendUnitFragment");
	}

	@Override
	public void onDetach() {
		EventBusUtil.unregister(this);
		super.onDetach();
	}

	@Override
	public void onAttach(Activity activity) {
		EventBusUtil.register(this);
		super.onAttach(activity);
	}

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);

		switch (tag) {
		case Constant.msgcenter_worksend_SendBtnClicked:// 点击发布按钮
			DialogUtil.getInstance().cannleDialog();
			Function function = (Function) list.get(1);
			params = (RequestParams) list.get(2);
			if (function == Function.UNIT) {
				reciverList.clear();
				List<Selit> list1 = expanAdapter.getSelectSelit();
				for (int i = 0; i < list1.size(); i++) {
					BasicNameValuePair item = new BasicNameValuePair("selit",
							list1.get(i).getSelit());
					reciverList.add(item);
				}
				if (reciverList == null || reciverList.size() == 0) {
					ToastUtil.showMessage(mActivity,
							R.string.please_choose_receiver);
				} else {
					dialog_send(reciverList.size());
				}
			}
			break;
		case Constant.msgcenter_work_change:// 切换单位
			DialogUtil.getInstance().cannleDialog();
			break;
		// case Constant.msgcenter_worksend_getUnitGroups:
		// ArrayList<UnitGroupInfo> UnitGroupInfoList =
		// (ArrayList<UnitGroupInfo>) list.get(1);
		// WorkSendFragmentController.getInstance().getUserInfoByUnitID(BaseActivity.sp.getInt("UnitID",
		// 0));
		// break;
		// case Constant.msgcenter_worksend_getUserInfoByUnitID:
		// ArrayList<Userinfo> list1 = (ArrayList<Userinfo>) list.get(1);
		// break;
		case Constant.msgcenter_work_GetUnitRevicer:// 获取单位接收人
			DialogUtil.getInstance().cannleDialog();
			GetUnitRevicer getUnitRevicer = GsonUtil.GsonToObject(
					(String) list.get(1), GetUnitRevicer.class);
			ArrayList<GroupUserList> groupList = getUnitRevicer.getSelit();
			for (int i = 0; i < groupList.size(); i++) {
				ArrayList<Selit> selitList = groupList.get(i)
						.getGroupselit_selit();
				Collections.sort(selitList, comparatorSelit);
			}
			Collections.sort(groupList, comparator);
			expanAdapter.setData(groupList);
			expanAdapter.notifyDataSetChanged();
			tv_unitname.setText(WorkSendToSbActivity2.myUnit.getUintName());
			break;
		case Constant.msgcenter_work_CreateCommMsg:
			DialogUtil.getInstance().cannleDialog();
			int result = (Integer) list.get(1);
			if (result == 1) {//信息发送成功
				try {
					expanAdapter.setAllSelitCheckFlag(false);
					expanAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	HanyuPinyin hanYu = new HanyuPinyin();
	Comparator<GroupUserList> comparator = new Comparator<GroupUserList>() {
		public int compare(GroupUserList s1, GroupUserList s2) {
			String s1name = hanYu.getStringPinYin(s1.getGroupName());
			String s2name = hanYu.getStringPinYin(s2.getGroupName());
			// 按姓名排序
			if (!s1name.equals(s2name)) {
				return s1name.compareTo(s2name);
			} else {
				// 姓名也相同则按学号排序
				return s1.getMCount() - s2.getMCount();
			}
		}
	};
	Comparator<Selit> comparatorSelit = new Comparator<Selit>() {
		public int compare(Selit s1, Selit s2) {
			String s1name = hanYu.getStringPinYin(s1.getName());
			String s2name = hanYu.getStringPinYin(s2.getName());
			// 按姓名排序
			if (!s1name.equals(s2name)) {
				return s1name.compareTo(s2name);
			} else {
				// 姓名也相同则按学号排序
				return (s1.getAccID()).compareTo(s2.getAccID());
			}
		}
	};
}