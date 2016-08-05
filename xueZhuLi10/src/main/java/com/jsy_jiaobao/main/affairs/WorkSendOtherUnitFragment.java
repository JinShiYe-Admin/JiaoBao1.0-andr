package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HanyuPinyin;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.TreeItemGroupHolder;
import com.jsy.xuezhuli.utils.adapter.TreeItemPersonHolder;
import com.jsy.xuezhuli.utils.adapter.TreeItemPersonHolder.TreeItemPerson;
import com.jsy.xuezhuli.utils.adapter.TreeItemRootHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnit;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitList;
import com.jsy_jiaobao.po.personal.GetUnitRevicer;
import com.jsy_jiaobao.po.personal.MyUnit;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

/**
 * 多单位事务Fragment
 */
public class WorkSendOtherUnitFragment extends Fragment {
	public static final String TAG = WorkSendOtherUnitFragment.class.getName();
	final static int TYPE_MY = 0;//本单位
	final static int TYPE_PARENT = 1;//上级单位
	final static int TYPE_SUB = 2;//下级单位
	private final int type_unit = 1;
	private int start = 0;
	private int end = 0;
	private Uri photoUri;
	private ACache mCache;
	private TreeNode root;
	private List<NameValuePair> reciverList = new ArrayList<NameValuePair>();

	private Activity mActivity;
	private ViewGroup layout_tree;
	private AndroidTreeView tView;

	public static WorkSendOtherUnitFragment newInstance() {
		return 	new WorkSendOtherUnitFragment();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_worksend_otherunit,
				container, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCache.clear();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		WorkSendFragmentController.getInstance().setContext(this);
		mActivity = getActivity();
		mCache = ACache.get(mActivity, "unit_group_selits");
		initViews(savedInstanceState);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
		if (tView != null) {
			outState.putString("tState", tView.getSaveState());
		}
	}

	private void initViews(Bundle savedInstanceState) {
		layout_tree = (ViewGroup) mActivity
				.findViewById(R.id.worksend_otherunit_layout_tree);

		root = TreeNode.root();
		for (int i = root.getChildren().size() - 1; i >= 0; i--) {
			root.deleteChild(root.getChildren().get(i));
		}
		if (WorkSendToSbActivity2.myUnit != null) {// 有本单位
			if ((WorkSendToSbActivity2.SubUnits == null || WorkSendToSbActivity2.SubUnits
					.size() == 0)
					&& (WorkSendToSbActivity2.ParentUnits == null || WorkSendToSbActivity2.ParentUnits
							.size() == 0)) {//没有下级单位和上级单位
				ToastUtil.showMessage(mActivity, R.string.noOther_unit);//提示无其他单位
			} else {
				start = 0;
				DialogUtil.getInstance().getDialog(
						mActivity,
						mActivity.getResources().getString(
								R.string.public_loading));
				DialogUtil.getInstance().setCanCancel(false);
				TreeNode computerRoot = new TreeNode(
						new TreeItemRootHolder.TreeItemRoot("当前单位", -1))
						.setViewHolder(new TreeItemRootHolder(mActivity));
				TreeNode myUnit = new TreeNode(
						new TreeItemGroupHolder.TreeItemGroup(
								WorkSendToSbActivity2.myUnit, 0))
						.setViewHolder(new TreeItemGroupHolder(mActivity,
								mHandler));
				computerRoot.addChild(myUnit);
				WorkSendFragmentController.getInstance().GetUnitRevicer(
						WorkSendToSbActivity2.myUnit.getTabID(),
						WorkSendToSbActivity2.myUnit.getFlag(), type_unit,
						myUnit);
				start++;
				root.addChildren(computerRoot);
			}
		}
		if (WorkSendToSbActivity2.ParentUnits != null
				&& WorkSendToSbActivity2.ParentUnits.size() > 0) {//有上级单位
			TreeNode computerRoot = new TreeNode(
					new TreeItemRootHolder.TreeItemRoot("上级单位", -1))
					.setViewHolder(new TreeItemRootHolder(mActivity));
			for (int i = 0; i < WorkSendToSbActivity2.ParentUnits.size(); i++) {
				TreeNode myUnit = new TreeNode(
						new TreeItemGroupHolder.TreeItemGroup(
								WorkSendToSbActivity2.ParentUnits.get(i), 1))
						.setViewHolder(new TreeItemGroupHolder(mActivity,
								mHandler));
				computerRoot.addChild(myUnit);
				WorkSendFragmentController.getInstance().GetUnitRevicer(
						WorkSendToSbActivity2.ParentUnits.get(i).getTabID(),
						WorkSendToSbActivity2.ParentUnits.get(i).getFlag(),
						type_unit, myUnit);
				start++;
			}
			root.addChildren(computerRoot);
		}
		if (WorkSendToSbActivity2.SubUnits != null
				&& WorkSendToSbActivity2.SubUnits.size() > 0) {//有下级单位
			TreeNode computerRoot = new TreeNode(
					new TreeItemRootHolder.TreeItemRoot("下级单位", -1))
					.setViewHolder(new TreeItemRootHolder(mActivity));
			for (int i = 0; i < WorkSendToSbActivity2.SubUnits.size(); i++) {
				TreeNode myUnit = new TreeNode(
						new TreeItemGroupHolder.TreeItemGroup(
								WorkSendToSbActivity2.SubUnits.get(i), 2))
						.setViewHolder(new TreeItemGroupHolder(mActivity,
								mHandler));
				computerRoot.addChild(myUnit);
				WorkSendFragmentController.getInstance().GetUnitRevicer(
						WorkSendToSbActivity2.SubUnits.get(i).getTabID(),
						WorkSendToSbActivity2.SubUnits.get(i).getFlag(),
						type_unit, myUnit);
				start++;
			}
			root.addChildren(computerRoot);
		}


		if (root.getChildren().size() > 0) {
			tView = new AndroidTreeView(mActivity, root);
			tView.setDefaultAnimation(false);
			// tView.setDefaultNodeClickListener(nodeClickListener);
			layout_tree.addView(tView.getView());
			tView.setSelectionModeEnabled(true);
			layout_tree.setVisibility(View.GONE);
		}
		if (savedInstanceState != null) {
			String state = savedInstanceState.getString("tState");
			if (!TextUtils.isEmpty(state)) {
				tView.restoreState(state);
			}
			photoUri = savedInstanceState.getParcelable("photoUri");
		}

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				break;
			case 101:
				break;
			case 102:
				try {
					tView.collapseAll();
					tView.expandLevel(1);
					layout_tree.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				DialogUtil.getInstance().cannleDialog();
				break;
			default:
				break;
			}
		}
	};
	private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {

		@Override
		public void onClick(TreeNode node, Object value) {
			if (!node.isExpanded()) {
				if (node.getChildren().size() == 0) {
					switch (node.HOLDER_TYPE) {
					case TYPE_MY://本单位
						TreeItemGroupHolder.TreeItemGroup holderMy = (TreeItemGroupHolder.TreeItemGroup) value;
						MyUnit myUnit = holderMy.myUnit;
						ArrayList<GroupUserList> selit = (ArrayList<GroupUserList>) mCache
								.getAsObject("unit_group_selit_"
										+ myUnit.getTabID());
						if (selit != null && selit.size() > 0) {
							for (int i = 0; i < selit.size(); i++) {
								GroupUserList group = selit.get(i);
								TreeNode groupNode = new TreeNode(
										new TreeItemGroupHolder.TreeItemGroup(
												group, 4))
										.setViewHolder(new TreeItemGroupHolder(
												mActivity, mHandler));
								// ----------加人，最后一击
								ArrayList<Selit> personList = group
										.getGroupselit_selit();
								for (int j = 0; j < personList.size(); j++) {
									Selit person1 = personList.get(j);
									TreeNode personNode = new TreeNode(
											new TreeItemPersonHolder.TreeItemPerson(
													person1, 5))
											.setViewHolder(new TreeItemPersonHolder(
													mActivity, mHandler));
									tView.addNode(groupNode, personNode);
								}
								tView.addNode(node, groupNode);
							}
						} else {

							WorkSendFragmentController.getInstance()
									.GetUnitRevicer(myUnit.getTabID(),
											myUnit.getFlag(), type_unit, node);
						}
						break;
					case TYPE_PARENT://上级单位
					case TYPE_SUB://下级单位
						TreeItemGroupHolder.TreeItemGroup holderParent = (TreeItemGroupHolder.TreeItemGroup) value;
						CommMsgRevicerUnit parent = holderParent.commMsgRevicerUnit;
						ArrayList<GroupUserList> selit1 = (ArrayList<GroupUserList>) mCache
								.getAsObject("unit_group_selit_"
										+ parent.getTabID());
						if (selit1 != null && selit1.size() > 0) {
							for (int i = 0; i < selit1.size(); i++) {
								GroupUserList group = selit1.get(i);
								TreeNode groupNode = new TreeNode(
										new TreeItemGroupHolder.TreeItemGroup(
												group, 4))
										.setViewHolder(new TreeItemGroupHolder(
												mActivity, mHandler));
								// ----------加人，最后一击
								ArrayList<Selit> personList = group
										.getGroupselit_selit();
								for (int j = 0; j < personList.size(); j++) {
									Selit person1 = personList.get(j);
									TreeNode personNode = new TreeNode(
											new TreeItemPersonHolder.TreeItemPerson(
													person1, 5))
											.setViewHolder(new TreeItemPersonHolder(
													mActivity, mHandler));
									tView.addNode(groupNode, personNode);
								}
								tView.addNode(node, groupNode);
							}
						} else {

							WorkSendFragmentController.getInstance()
									.GetUnitRevicer(parent.getTabID(),
											parent.getFlag(), type_unit, node);
						}

						break;
					default:
						break;
					}
				}
			}
		}
	};

	private void getSelitSelect(TreeNode node) {
		for (TreeNode n : node.getChildren()) {
			if (n.getValue() instanceof TreeItemPersonHolder.TreeItemPerson) {
				if (n.isSelected()) {
					TreeItemPersonHolder.TreeItemPerson preson = (TreeItemPerson) n
							.getValue();
					if (preson.HOLDER_TYPE == 6) {
						BasicNameValuePair item = new BasicNameValuePair(
								"genselit", preson.person.getSelit());
						reciverList.add(item);
					} else if (preson.HOLDER_TYPE == 7) {
						BasicNameValuePair item = new BasicNameValuePair(
								"stuselit", preson.person.getSelit());
						reciverList.add(item);
					} else if (preson.HOLDER_TYPE == 5) {
						BasicNameValuePair item = new BasicNameValuePair(
								"selit", preson.person.getSelit());
						reciverList.add(item);
					}
				}
			}
			getSelitSelect(n);
		}
	}

	/**
	 * 提示框
	 * 
	 * @param count c
	 */
	protected void dialog_send(int count) {
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage("确定要向" + count + "人发送信息吗?");
		builder.setTitle(R.string.hint);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MobclickAgent
								.onEvent(
										mActivity,
										getResources()
												.getString(
														R.string.WorkSendOtherUnitFragment_send_sure));
						dialog.dismiss();
						sendMessage();
					}

				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MobclickAgent
								.onEvent(
										mActivity,
										getResources()
												.getString(
														R.string.WorkSendOtherUnitFragment_send_cancel));
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("WorkSendOtherUnitFragment");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("WorkSendOtherUnitFragment");
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

	RequestParams params;

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {

		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_worksend_SendBtnClicked:
			DialogUtil.getInstance().cannleDialog();
			Function function = (Function) list.get(1);
			params = (RequestParams) list.get(2);
			if (function == Function.OTHER) {
				reciverList.clear();
				getSelitSelect(root);
				if (reciverList == null || reciverList.size() == 0) {
					ToastUtil.showMessage(mActivity,
							R.string.please_choose_receiver);
				} else {
					dialog_send(reciverList.size());
				}
			}
			break;
		case Constant.msgcenter_select_position:
			DialogUtil.getInstance().cannleDialog();
			break;
		case Constant.msgcenter_work_CommMsgRevicerUnitList:
			DialogUtil.getInstance().cannleDialog();
			String commMsgRevicerUnitList_str = (String) list.get(1);
			CommMsgRevicerUnitList commMsgRevicerUnitList = GsonUtil
					.GsonToObject(commMsgRevicerUnitList_str,
							CommMsgRevicerUnitList.class);
			WorkSendToSbActivity2.myUnit = commMsgRevicerUnitList.getMyUnit();
			WorkSendToSbActivity2.SubUnits = commMsgRevicerUnitList
					.getSubUnits();
			WorkSendToSbActivity2.ParentUnits = commMsgRevicerUnitList
					.getUnitParents();
			// WorkSendToSbActivity2.UnitClass =
			// commMsgRevicerUnitList.getUnitClass();
			start = 0;
			for (int i = root.getChildren().size() - 1; i >= 0; i--) {
				root.deleteChild(root.getChildren().get(i));
			}
			if (WorkSendToSbActivity2.myUnit != null) {
				DialogUtil.getInstance().getDialog(
						mActivity,
						mActivity.getResources().getString(
								R.string.public_loading));
				DialogUtil.getInstance().setCanCancel(false);
				TreeNode computerRoot = new TreeNode(
						new TreeItemRootHolder.TreeItemRoot(getResources()
								.getString(R.string.current_unit), -1))
						.setViewHolder(new TreeItemRootHolder(mActivity));
				TreeNode myUnit = new TreeNode(
						new TreeItemGroupHolder.TreeItemGroup(
								WorkSendToSbActivity2.myUnit, 0))
						.setViewHolder(new TreeItemGroupHolder(mActivity,
								mHandler));
				computerRoot.addChild(myUnit);
				WorkSendFragmentController.getInstance().GetUnitRevicer(
						WorkSendToSbActivity2.myUnit.getTabID(),
						WorkSendToSbActivity2.myUnit.getFlag(), type_unit,
						myUnit);
				start++;
				root.addChildren(computerRoot);
			}
			if (WorkSendToSbActivity2.ParentUnits != null
					&& WorkSendToSbActivity2.ParentUnits.size() > 0) {
				TreeNode computerRoot = new TreeNode(
						new TreeItemRootHolder.TreeItemRoot(getResources()
								.getString(R.string.super_unit), -1))
						.setViewHolder(new TreeItemRootHolder(mActivity));
				for (int i = 0; i < WorkSendToSbActivity2.ParentUnits.size(); i++) {
					TreeNode myUnit = new TreeNode(
							new TreeItemGroupHolder.TreeItemGroup(
									WorkSendToSbActivity2.ParentUnits.get(i), 1))
							.setViewHolder(new TreeItemGroupHolder(mActivity,
									mHandler));
					computerRoot.addChild(myUnit);
					WorkSendFragmentController.getInstance()
							.GetUnitRevicer(
									WorkSendToSbActivity2.ParentUnits.get(i)
											.getTabID(),
									WorkSendToSbActivity2.ParentUnits.get(i)
											.getFlag(), type_unit, myUnit);
					start++;
				}
				root.addChildren(computerRoot);
			}
			if (WorkSendToSbActivity2.SubUnits != null
					&& WorkSendToSbActivity2.SubUnits.size() > 0) {
				TreeNode computerRoot = new TreeNode(
						new TreeItemRootHolder.TreeItemRoot(getResources()
								.getString(R.string.lower_unit), -1))
						.setViewHolder(new TreeItemRootHolder(mActivity));
				for (int i = 0; i < WorkSendToSbActivity2.SubUnits.size(); i++) {
					TreeNode myUnit = new TreeNode(
							new TreeItemGroupHolder.TreeItemGroup(
									WorkSendToSbActivity2.SubUnits.get(i), 2))
							.setViewHolder(new TreeItemGroupHolder(mActivity,
									mHandler));
					computerRoot.addChild(myUnit);
					WorkSendFragmentController.getInstance().GetUnitRevicer(
							WorkSendToSbActivity2.SubUnits.get(i).getTabID(),
							WorkSendToSbActivity2.SubUnits.get(i).getFlag(),
							type_unit, myUnit);
					start++;
				}
				root.addChildren(computerRoot);
			}
			if ((WorkSendToSbActivity2.SubUnits == null || WorkSendToSbActivity2.SubUnits
					.size() == 0)
					&& (WorkSendToSbActivity2.ParentUnits == null || WorkSendToSbActivity2.ParentUnits
							.size() == 0)) {
				ToastUtil.showMessage(mActivity, R.string.noOther_unit);
			}
			// }
			if (root.getChildren().size() > 0) {

				tView = new AndroidTreeView(mActivity, root);
				tView.setDefaultAnimation(true);
				tView.setDefaultNodeClickListener(nodeClickListener);
				layout_tree.addView(tView.getView());
				tView.setSelectionModeEnabled(true);
				layout_tree.setVisibility(View.GONE);
			}
			break;
		case Constant.msgcenter_work_CreateCommMsg:
			DialogUtil.getInstance().cannleDialog();
			int result = (Integer) list.get(1);
			if (result == 1) {
				try {
					tView.deselectAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case Constant.msgcenter_worksend_GetUnitRevicer_otherunit:

			try {
				String data = (String) list.get(1);

				ArrayList<Object> revicertag = (ArrayList<Object>) list.get(2);
				TreeNode node = (TreeNode) revicertag.get(1);
				int type = (Integer) revicertag.get(2);
				if (type == type_unit) {
					GetUnitRevicer getUnitRevicer = GsonUtil.GsonToObject(data,
							GetUnitRevicer.class);
					mCache.put("unit_group_selit_" + revicertag.get(0),
							getUnitRevicer.getSelit());
					ArrayList<GroupUserList> selit = getUnitRevicer.getSelit();// (ArrayList<GroupUserList>)
																				// mCache.getAsObject("unit_group_selit_"+myUnit.getTabID());

					Collections.sort(selit, comparator);
					if ( selit.size() > 0) {
						for (int i = 0; i < selit.size(); i++) {
							GroupUserList group = selit.get(i);
							TreeNode groupNode = new TreeNode(
									new TreeItemGroupHolder.TreeItemGroup(
											group, 4))
									.setViewHolder(new TreeItemGroupHolder(
											mActivity, mHandler));
							// ----------加人，最后一击
							ArrayList<Selit> personList = group
									.getGroupselit_selit();
							Collections.sort(personList, comparatorSelit);
							for (int j = 0; j < personList.size(); j++) {
								Selit person1 = personList.get(j);
								TreeNode personNode = new TreeNode(
										new TreeItemPersonHolder.TreeItemPerson(
												person1, 5))
										.setViewHolder(new TreeItemPersonHolder(
												mActivity, mHandler));
								tView.addNode(groupNode, personNode);
							}
							tView.addNode(node, groupNode);
						}
					}
				}
				//
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			end++;
			if (start == end) {
				end = 0;
				tView.expandAll();
				new Thread() {
					public void run() {
						try {
							sleep(1000);
							mHandler.sendEmptyMessage(102);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
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