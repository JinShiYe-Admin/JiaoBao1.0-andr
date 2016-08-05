package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.schoolcircle.UnitSpacePhotoExpActivity;
import com.jsy_jiaobao.po.personal.Photo;

/**
 * Picture 的Adapter
 * 
 * @author admin
 * 
 */
public class PictureAdapter extends BaseAdapter {
	private JSONArray jsonarray;
	private Context mContext;

	public PictureAdapter(Context mContext, JSONArray jsonarray) {
		this.jsonarray = jsonarray;
		this.mContext = mContext;
	}

	public JSONArray getJSONArray() {
		return jsonarray;
	}

	@Override
	public int getCount() {
		return jsonarray == null ? 0 : jsonarray.length();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * cell界面
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.imageview, position);
		// cell中ImageView 的引用
		final ImageView item = viewHolder.getView(R.id.imageview);
		try {

			JSYApplication.getInstance().bitmap.display(item,
					jsonarray.getString(position));

		} catch (Exception e) {
			e.printStackTrace();
		}
		// ImageView的点击监听事件
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<Photo> list = new ArrayList<Photo>();
				for (int i = 0; i < jsonarray.length(); i++) {
					Photo photo = new Photo();
					try {
						photo.setBIGPhotoPath(jsonarray.getString(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					list.add(photo);
				}
				if (list.size() > 0) {
					// 如果图片列表不为空
					/**
					 * 传递给Activity 参数并start
					 */
					Intent intent = new Intent(mContext,
							UnitSpacePhotoExpActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);// 位置
					bundle.putSerializable("photoList", list);// 列表
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}

			}
		});
		return viewHolder.getConvertView();
	}
}
