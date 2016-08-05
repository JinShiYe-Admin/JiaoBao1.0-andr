package com.jsy_jiaobao.main.schoolcircle;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.UnitClass;
import com.jsy_jiaobao.po.personal.UnitInfo;

/**
 * 下级单位列表的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class NoticeUnitsAdapter<T> extends BaseAdapter implements ConstantUrl {
	private Context context;
	protected List<T> unitList;
	private int level;
	private Intent intent;

	// private String ArtTyep;
	public NoticeUnitsAdapter(Context context, int level) {
		this.context = context;
		this.level = level;

	}

	public void setData(List<T> classList) {
		this.unitList = classList;
	}

	@Override
	public int getCount() {
		if (unitList != null) {
			return unitList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return unitList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,
				R.layout.layout_notice_unitlist_item, position);
		TextView badge = viewHolder.getView(R.id.notice_unititem_badge);
		TextView exp = viewHolder.getView(R.id.notice_unititem_exp);
		TextView name = viewHolder.getView(R.id.notice_unititem_name);
		ImageView icon = viewHolder.getView(R.id.notice_unititem_icon);
		ImageView more = viewHolder.getView(R.id.notice_unititem_more);
		LinearLayout layout = viewHolder.getView(R.id.notice_item);
		more.setBackgroundResource(R.drawable.btn_more);
		exp.setVisibility(8);
		name.setPadding(level * 40, 0, 0, 0);
		try {
			final UnitInfo item = (UnitInfo) getItem(position);
			name.setText(item.getUintName());
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intent = new Intent(context, UnitSpaceActivity.class);
					intent.putExtra("UnitID", item.getTabID());
					intent.putExtra("UnitType", item.getUnitType());
					intent.putExtra("UnitName", item.getUintName());
					context.startActivity(intent);
					// JSYApplication.getInstance().activityArray.get(JSYApplication.getInstance().activityArray.size()-1).finish();
					// JSYApplication.getInstance().activityArray.get(JSYApplication.getInstance().activityArray.size()-1).finish();
				}
			});
			int unitType = item.getUnitType();
			if (unitType == 1) {
				icon.setBackgroundResource(R.drawable.icon_bureau);
			} else if (unitType == 2) {
				icon.setBackgroundResource(R.drawable.icon_school);
			} else {
				icon.setBackgroundResource(R.drawable.icon_class);
			}
			// int upArc = item.getArtUpdate();
			// if (upArc>0) {
			// badge.setText(String.valueOf(upArc));
			// badge.setVisibility(0);
			// }else{
			badge.setVisibility(8);
			// }

		} catch (Exception e) {
			// try {
			// final MyUnitInfo item = (MyUnitInfo) getItem(position);
			// name.setText(item.getUintName());
			// badge.setVisibility(8);
			// more.setBackgroundResource(R.drawable.btn_more_unit);
			// int unitType = item.getUnitType();
			// if (unitType == 1) {
			// icon.setBackgroundResource(R.drawable.icon_bureau);
			// }else if(unitType == 2){
			// icon.setBackgroundResource(R.drawable.icon_school);
			// }else{
			// icon.setBackgroundResource(R.drawable.icon_class);
			// }
			// layout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent intent = new
			// Intent(context,NoticeJuniorListActivity.class);
			// intent.putExtra("UnitID", item.getTabID());
			// intent.putExtra("UnitType", item.getUnitType());
			// intent.putExtra("UnitName", item.getUintName());
			// context.startActivity(intent);
			// }
			// });
			// } catch (Exception e1) {//我关联的班级
			// try {
			// final UserClass item = (UserClass) getItem(position);
			// name.setText(item.getClassName());
			// exp.setVisibility(0);
			// icon.setBackgroundResource(R.drawable.icon_myclass);
			// badge.setVisibility(8);
			// layout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// if ("train".equals(ArtTyep)) {
			// Intent intent1 = new Intent(context,TrainArtListActivity.class);
			// Bundle bundle = new Bundle();
			// ShowUnitInfo unit = new ShowUnitInfo();
			// unit.TabID = item.getClassID();
			// unit.UintName = item.getClassName();
			// unit.UnitType = 3;
			// unit.TabIDStr = item.getTabIDStr();
			// bundle.putSerializable("UserClass", unit);
			// intent1.putExtras(bundle);
			// context.startActivity(intent1);
			// }else{
			// intent.putExtra("UnitID", item.getClassID());
			// intent.putExtra("SchoolID", item.getSchoolID());
			// intent.putExtra("UnitType", 3);
			// intent.putExtra("UnitName", item.getClassName());
			// intent.putExtra("myunit", true);
			// context.startActivity(intent);
			// }
			// }
			// });
			// } catch (Exception e2) {//指定学校的所有班级基础数据
			final UnitClass item = (UnitClass) getItem(position);
			name.setText(item.getClsName());
			icon.setBackgroundResource(R.drawable.icon_class);
			badge.setVisibility(8);
			// int upArc = item.getArtUpdate();
			// if (upArc>0) {
			// badge.setText(String.valueOf(upArc));
			// badge.setVisibility(0);
			// }else{
			// badge.setVisibility(8);
			// }
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					intent = new Intent(context, UnitSpaceActivity.class);
					intent.putExtra("UnitID", item.getTabID());
					intent.putExtra("UnitType", 3);
					intent.putExtra("UnitName", item.getClsName());
					context.startActivity(intent);
					// JSYApplication.getInstance().activityArray.get(JSYApplication.getInstance().activityArray.size()-1).finish();
					// JSYApplication.getInstance().activityArray.get(JSYApplication.getInstance().activityArray.size()-1).finish();
				}
			});
		}
		// }
		// }
		return viewHolder.getConvertView();
	}

	// public void setArtType(String string) {
	// // TODO Auto-generated method stub
	// this.ArtTyep = string;
	// }
}