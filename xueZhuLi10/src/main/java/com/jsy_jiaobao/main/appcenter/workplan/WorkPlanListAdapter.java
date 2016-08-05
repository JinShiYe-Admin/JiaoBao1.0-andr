package com.jsy_jiaobao.main.appcenter.workplan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.app.workplan.WorkPlanInfo;

import java.util.List;

public class WorkPlanListAdapter extends BaseAdapter {
	private Context context;
	private List<WorkPlanInfo> list_workplan;
	public WorkPlanListAdapter(Context context){
		this.context = context;

	}
	public void setListData(List<WorkPlanInfo> list_workplan){
		this.list_workplan = list_workplan;	
	}
	@Override
	public int getCount() {
		if (list_workplan != null) {
			return list_workplan.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		
		if (list_workplan != null ) {
			return list_workplan.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(context, convertView, parent,R.layout.listview_workplan_item, position);
		TextView endTime = viewHolder.getView(R.id.workplan_item_endtime);
		TextView startTime = viewHolder.getView(R.id.workplan_item_starttime);
		TextView workplace = viewHolder.getView(R.id.workplan_item_workplace);
		TextView workPlan = viewHolder.getView(R.id.workplan_item_workplan);
		TextView uptime = viewHolder.getView(R.id.workplan_item_uptime);
		TextView unit = viewHolder.getView(R.id.workplan_item_unit);
		
		WorkPlanInfo workplanInfo = (WorkPlanInfo) getItem(position);

		unit.setText(workplanInfo.getUnitName());
		endTime.setText(workplanInfo.getdEdate());
		startTime.setText(workplanInfo.getdSdate());
		workplace.setText(workplanInfo.getsWorkPlace());
		workPlan.setText(workplanInfo.getsSubject());
		uptime.setText(workplanInfo.getdUpdateDate());
		return viewHolder.getConvertView();
	}

}
