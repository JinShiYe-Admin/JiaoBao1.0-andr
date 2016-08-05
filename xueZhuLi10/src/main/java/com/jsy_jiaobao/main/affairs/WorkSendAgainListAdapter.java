package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;

public class WorkSendAgainListAdapter extends BaseAdapter{
	private Context mContext;
	protected JSONArray readerArray;
	ArrayList<Boolean> checkedList = new ArrayList<>();
	public WorkSendAgainListAdapter(Context context){
		this.mContext = context;
	}
	
	public void setData(JSONArray readerArray) {
		this.readerArray = readerArray;
		checkedList.clear();
		for (int i = 0; i < readerArray.length(); i++) {
			checkedList.add(i,true);
		}
	}
	@Override
	public int getCount() {
		if (readerArray != null) {
			return readerArray.length();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.chat_friends_item, position);
		TextView nick = viewHolder.getView(R.id.chat_friends_nick);
		ImageView photo = viewHolder.getView(R.id.chat_friends_photo);
		CheckBox checkBox = viewHolder.getView(R.id.chat_friends_checkbox);
		try {
			photo.setVisibility(View.GONE);
			checkBox.setChecked(checkedList.get(position));
			JSONObject item = readerArray.getJSONObject(position);
			int mc = item.getInt("MCState");
			int pc = item.getInt("PCState");
			if (mc == 1 || pc == 1) {
				nick.setText(item.getString("TrueName")+"(已查看)");
			}else{
				nick.setText(item.getString("TrueName"));
			}
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					checkedList.set(position,isChecked);
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}
}