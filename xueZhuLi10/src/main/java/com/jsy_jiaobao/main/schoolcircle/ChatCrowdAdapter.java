package com.jsy_jiaobao.main.schoolcircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.PersonalSpaceActivity;
import com.jsy_jiaobao.po.sys.Human;
import com.jsy_jiaobao.po.sys.UnitGroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位成员列表的Adapter
 * @author admin
 */
public class ChatCrowdAdapter extends BaseExpandableListAdapter {
	private int pp = 1;
	private Context mContext;
	private ArrayList<UnitGroupInfo> groupList;
	private ArrayList<ArrayList<Human>> childList;
	private boolean checkBoxVisible = false;
	private ArrayList<Human> checkHumanList = new ArrayList<>();

	public ChatCrowdAdapter(Context fragmentActivity) {
		this.mContext = fragmentActivity;
	}

	public void setData(ArrayList<UnitGroupInfo> list,
			ArrayList<ArrayList<Human>> child) {
		this.groupList = list;
		this.childList = child;
		notifyDataSetChanged();
	}

	public void setPagePosition(int PagePosition) {
		this.pp = PagePosition;
	}

	// -----------------Child----------------//
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (null != childList) {
			return childList.get(groupPosition).get(childPosition);
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (null != childList && childList.size() > 0) {
			List<Human> list = childList.get(groupPosition);
			if (null != list) {
				return list.size();
			}
		}
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.chat_friends_item, childPosition);
		TextView nick = viewHolder.getView(R.id.chat_friends_nick);
		ImageView photo = viewHolder.getView(R.id.chat_friends_photo);
		final CheckBox checkBox = viewHolder
				.getView(R.id.chat_friends_checkbox);
		final Human child = (Human) getChild(groupPosition, childPosition);
		if (child.getUserID().split("_")[1].equals("0")) {
			viewHolder.getConvertView().setEnabled(false);
			String string=child.getUserName() + "(无账号)";
			nick.setText(string);
		} else {
			viewHolder.getConvertView().setEnabled(true);
			nick.setText(child.getUserName());
		}
		JSYApplication.getInstance().bitmap.display(photo, child.getPhotoUrl());
		if (checkBoxVisible) {
			if (child.getUserID().equals(
					"jb_" + BaseActivity.sp.getString("JiaoBaoHao", ""))) {
				checkBox.setClickable(false);
				checkBox.setVisibility(View.INVISIBLE);
			} else {
				checkBox.setClickable(true);
				checkBox.setVisibility(View.VISIBLE);
			}
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						checkHumanList.add(child);
					} else {
						for (Human human : checkHumanList) {
							if (human.getUserID().equals(child.getUserID())) {
								checkHumanList.remove(human);
								break;
							}
						}
					}
				}
			});
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (child.getUserID().equals(
									"jb_"
											+ BaseActivity.sp.getString(
													"JiaoBaoHao", ""))) {
							} else {
								checkBox.setChecked(!checkBox.isChecked());
							}

						}
					});
		} else {
			checkBox.setVisibility(View.INVISIBLE);
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							String nickStr = child.getUserName();
							if (TextUtils.isEmpty(nickStr)) {
								nickStr = child.getUserID().split("_")[1];
							}
							if (1 != pp) {
								Intent intent = new Intent(mContext,
										PersonalSpaceActivity.class);
								Bundle args = new Bundle();
								args.putString("JiaoBaoHao", child.getUserID()
										.split("_")[1]);
								args.putString("UserName", nickStr);
								args.putString("UserID", child.getUserID() + "");
								intent.putExtras(args);
								mContext.startActivity(intent);
							}
						}
					});
		}

		return viewHolder.getConvertView();
	}

	// ----------------Group----------------//
	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getGroupCount() {
		if (null != groupList) {
			return groupList.size();
		}
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.layout_expandlistview_group, groupPosition);
		TextView name = viewHolder.getView(R.id.expan_group_name);
		ImageView flag = viewHolder.getView(R.id.expan_group_flag);
		UnitGroupInfo string = groupList.get(groupPosition);
		flag.setVisibility(View.INVISIBLE);
		name.setText(string.getGroupName());
		return viewHolder.getConvertView();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}