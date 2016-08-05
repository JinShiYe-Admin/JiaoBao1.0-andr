package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.GetAllCategory;
import com.jsy_jiaobao.po.qiuzhi.Subject;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义Adapter
 * 
 * @功能 选择关注话题界面的Adapter
 * 
 */
public class QiuZhiExpandableListViewAdapter extends BaseExpandableListAdapter {
	public ArrayList<Subject> group = new ArrayList<Subject>();
	public ArrayList<ArrayList<Subject>> gridViewChild = new ArrayList<ArrayList<Subject>>();
	LayoutInflater mInflater;
	Activity context;
	private boolean isIndex;

	public QiuZhiExpandableListViewAdapter(Activity context,
			ArrayList<GetAllCategory> allCategory, JSONArray myAttIDs) {
		mInflater = LayoutInflater.from(context);
		if (null != allCategory && allCategory.size() > 0) {
			for (int i = 0; i < allCategory.size(); i++) {
				ArrayList<Subject> sublist = allCategory.get(i).getSubitem();
				if (myAttIDs != null) {
					for (Subject children : sublist) {
						for (int j = 0; j < myAttIDs.length(); j++) {
							try {
								if (children.getTabID() == myAttIDs.getInt(j)) {
									children.setAtted(true);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
				gridViewChild.add(sublist);
				Subject subject = allCategory.get(i).getItem();
				group.add(subject);
			}
		}

		this.context = context;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * 子View
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(
					R.layout.channel_expandablelistview_item, parent, false);
			mViewChild.gridView = (GridView) convertView
					.findViewById(R.id.channel_item_child_gridView);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}
		GridAdapter adapter = new GridAdapter(context,
				gridViewChild.get(groupPosition));
		mViewChild.gridView.setAdapter(adapter);
		if (!isIndex) {
			setGridViewListener(mViewChild.gridView);
		}
		mViewChild.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		return convertView;
	}

	private class GridAdapter extends BaseAdapter {
		Activity context;
		ArrayList<Subject> arrayList;

		public GridAdapter(Activity context, ArrayList<Subject> arrayList2) {
			this.context = context;
			this.arrayList = arrayList2;
		}

		@Override
		public int getCount() {
			return arrayList == null ? 0 : arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = ViewHolder.get(context, convertView,
					parent, R.layout.channel_gridview_item, position);
			TextView name = viewHolder.getView(R.id.channel_gridview_item);
			final Subject subject = arrayList.get(position);
			name.setText(subject.getSubject().trim());
			if (subject.isAtted()) {
				name.setTextColor(context.getResources().getColor(R.color.red));
			} else {
				name.setTextColor(context.getResources()
						.getColor(R.color.black));
			}
			if (isIndex) {
				name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent
								.onEvent(
										context,
										context.getResources()
												.getString(
														R.string.QiuZhiChoseClazzActivity_choseAttTopic));
						subject.setAtted(!subject.isAtted());
						GridAdapter.this.notifyDataSetChanged();
					}
				});
			}
			return viewHolder.getConvertView();
		}

	}

	/**
	 * 设置gridView点击事件监听
	 * 
	 * @param gridView
	 */
	private void setGridViewListener(final GridView gridView) {
		gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MobclickAgent.onEvent(
						context,
						context.getResources().getString(
								R.string.QiuZhiChoseClazzActivity_choseTopic));
				Subject subject = (Subject) gridView.getAdapter().getItem(
						position);
				Intent intent = context.getIntent();
				intent.putExtra("Subject", subject);
				context.setResult(3, intent);
				context.finish();
			}
		});
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return gridViewChild.get(groupPosition) == null ? 0 : 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition).getSubject();
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(
					R.layout.channel_expandablelistview, parent, false);
			mViewChild.textView = (TextView) convertView
					.findViewById(R.id.channel_group_name);
			mViewChild.imageView = (ImageView) convertView
					.findViewById(R.id.channel_imageview_orientation);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}
		if (isExpanded) {
			mViewChild.imageView
					.setImageResource(R.drawable.channel_expandablelistview_top_icon);
		} else {
			mViewChild.imageView
					.setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
		}
		mViewChild.textView.setText(getGroup(groupPosition).toString().trim());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean isIndex() {
		return isIndex;
	}

	public void setIndex(boolean isIndex) {
		this.isIndex = isIndex;
	}

	public ArrayList<Integer> getSelectedSubject() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < gridViewChild.size(); i++) {
			ArrayList<Subject> child = gridViewChild.get(i);
			for (Subject children : child) {
				if (children.isAtted()) {
					list.add(children.getTabID());
				}
			}
		}
		return list;
	}

	ViewChild mViewChild;

	static class ViewChild {
		ImageView imageView;
		TextView textView;
		GridView gridView;
	}
}