package com.jsy_jiaobao.main.appcenter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;

public class GalleryGridViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<File> bitmapList;
	public GalleryGridViewAdapter(Context context,ArrayList<File> fileList){
		this.context = context;
		this.bitmapList = fileList;
	}


	@Override
	public int getCount() {
		return bitmapList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,R.layout.gallery_gridview_item, position);
		ImageView image = viewHolder.getView(R.id.gallery_gridview_image);
		ImageButton delete = viewHolder.getView(R.id.gallery_gridview_delete);
//		viewHolder.setImageByUrl(R.id.gallery_gridview_image,bitmapList.get(position).toURI().toString());
		try {
//			ImageLoader.getInstance().loadImage(bitmapList.get(position).toURI().toString(), image);
			JSYApplication.getInstance().bitmap.display(image, bitmapList.get(position).toURI().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bitmapList.remove(position);
				notifyDataSetChanged();
			}
		});
		return viewHolder.getConvertView();
	}
}
