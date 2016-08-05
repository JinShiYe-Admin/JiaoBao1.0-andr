package com.jsy_jiaobao.main.schoolcircle;

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
import com.jsy_jiaobao.po.personal.Photo;

public class PictureAdapter extends BaseAdapter {
	private JSONArray jsonarray;
	private Context mContext;
	public PictureAdapter(Context mContext, JSONArray jsonarray){
		this.jsonarray = jsonarray;
		this.mContext = mContext;
	}
	public JSONArray getJSONArray(){
		return jsonarray;
	}
	@Override
	public int getCount() {
		return jsonarray == null?0:jsonarray.length();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.imageview, position);
		final ImageView item = viewHolder.getView(R.id.imageview);
		try {
			
			JSYApplication.getInstance().bitmap.display(item, jsonarray.getString(position));
//			String url = jsonarray.getString(position);
//			String[] names = url.split("\\/");
//			String name = names[names.length-1];
//			HttpUtil.getInstanceNew().download(url, JSYApplication.getInstance().FILE_PATH+name, new RequestCallBack<File>() {
//				
//				@Override
//				public void onSuccess(ResponseInfo<File> arg0) {
//					x.image().bind(item, arg0.result.toURI().toString(),imageOptions);
//				}
//
//				
//				@Override
//				public void onFailure(HttpException arg0, String arg1) {
//					
//				}
//			});
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					Intent intent = new Intent(mContext,UnitSpacePhotoExpActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);
					bundle.putSerializable("photoList", list);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
				
			}
		});
		return viewHolder.getConvertView();
	}
}
