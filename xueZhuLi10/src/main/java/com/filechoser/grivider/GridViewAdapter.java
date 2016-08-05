package com.filechoser.grivider;

import java.io.File;
import java.util.List;

import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class GridViewAdapter extends BaseAdapter{
	List<String> fileList =null;
	LayoutInflater flater = null;
	FilePerate filePerate = null;
	Context context = null;
	
	int fileNum = 0;//文件数
	int folderNum = 0;//目录数
	BitmapUtils bitmap;
	/**
	 * @param path 文件路径
	 * @param context 上下文
	 */
	public GridViewAdapter(FilePerate filePerate,List<String> fileList,Context context) {
		// TODO Auto-generated constructor stub
		flater = LayoutInflater.from(context);
		this.context = context;
		this.fileList = fileList;
		this.filePerate = filePerate;
		bitmap = new BitmapUtils(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		fileNum = filePerate.getFileNum();//获取文件个数
		folderNum = filePerate.getFolderNum();
		ViewHolder viewHolder;
		if(convertView == null){
			convertView = flater.inflate(R.layout.gird_item, null);
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView)convertView.findViewById(R.id.fileIcon);
			viewHolder.title = (TextView)convertView.findViewById(R.id.fileName);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		String path = "";
		//得到选中文件的目录
		path = filePerate.getCurrentPath()+"//"+filePerate.getFileList().get(position);
		File file = new File(path);
		if (file.isDirectory()) {
			viewHolder.image.setImageResource(R.drawable.ic_menu_archive);//目录的图标
		}else {
			String[] names = path.split("\\.");
			if (names[names.length-1].equals("jpg") ||names[names.length-1].equals("jpeg")||names[names.length-1].equals("png")||names[names.length-1].equals("bmp")) {
				bitmap.display(viewHolder.image, path);
			}else{
				viewHolder.image.setImageResource(R.drawable.file);//文件的图标
			}
		}
		viewHolder.title.setText(fileList.get(position));//文件名
		return convertView;
	}
}

class ViewHolder{
	public ImageView image;
	public TextView title;
}
