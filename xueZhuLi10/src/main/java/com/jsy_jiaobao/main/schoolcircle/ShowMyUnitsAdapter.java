package com.jsy_jiaobao.main.schoolcircle;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.UnitInfo;
import com.jsy_jiaobao.po.personal.UnitSectionMessage;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * 下级单位列表Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class ShowMyUnitsAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;
	private BitmapUtils bitmapUtils;// 照片列表
	private String mainURL;

	public ShowMyUnitsAdapter(Context mContext) {
		this.mContext = mContext;
		bitmapUtils = JSYApplication.getInstance().bitmap;
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
	}

	public void setData(List<T> mData) {
		this.mData = mData;
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.show_myunit_list_item, position);
		TextView name = viewHolder.getView(R.id.unitshow_item_unitname);
		TextView badview = viewHolder.getView(R.id.unitshow_item_badview);
		ImageView icon = viewHolder.getView(R.id.unitshow_item_pic);
		try {
			final UnitSectionMessage unitInfo = (UnitSectionMessage) getItem(position);
			String tv_name = unitInfo.getUnitName();
			name.setText(tv_name);
			badview.setVisibility(View.GONE);
			bitmapUtils.display(icon, mainURL + ConstantUrl.getUnitlogo
					+ "?UnitID=" + unitInfo.getUnitID());
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									UnitSpaceActivity.class);
							intent.putExtra("UnitID", unitInfo.getUnitID());
							intent.putExtra("UnitType", unitInfo.getUnitType());
							intent.putExtra("UnitName", unitInfo.getUnitName());
							intent.putExtra("IsMyUnit", unitInfo.getIsMyUnit());
							mContext.startActivity(intent);
						}
					});
		} catch (Exception e) {
			final UnitInfo unitInfo = (UnitInfo) getItem(position);
			String tv_name = unitInfo.getUintName();
			name.setText(tv_name);
			badview.setVisibility(View.GONE);
			bitmapUtils.display(icon, mainURL + ConstantUrl.getUnitlogo
					+ "?UnitID=" + unitInfo.getTabID());
			viewHolder.getConvertView().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									UnitSpaceActivity.class);
							intent.putExtra("UnitID", unitInfo.getTabID());
							intent.putExtra("UnitType", unitInfo.getUnitType());
							intent.putExtra("UnitName", unitInfo.getUintName());
							intent.putExtra("IsMyUnit", 1);
							mContext.startActivity(intent);
						}
					});
		}
		return viewHolder.getConvertView();
	}
}
