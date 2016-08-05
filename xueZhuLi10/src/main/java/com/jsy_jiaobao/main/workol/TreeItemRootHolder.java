package com.jsy_jiaobao.main.workol;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jsy_jiaobao.main.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * 树形结构
 * 
 * @author admin
 * 
 */
public class TreeItemRootHolder extends
		TreeNode.BaseNodeViewHolder<TreeItemRoot> {
	private TextView name, select;
	private ImageView arrowView;

	public TreeItemRootHolder(Context context, Handler mHandler) {
		super(context);
	}

	@Override
	public View createNodeView(final TreeNode node, TreeItemRoot value) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.tree_workol_stu_root, null,
				false);
		view.setPadding(10, 0, 10, 0);
		name = (TextView) view.findViewById(R.id.tree_root_tv_name);
		select = (TextView) view.findViewById(R.id.tree_root_tv_select);
		arrowView = (ImageView) view.findViewById(R.id.tree_root_iv_arrow);
		name.setText(value.name.toString());
		select.setText(value.select.toString());
		return view;
	}

	@Override
	public void toggle(boolean active) {
		arrowView
				.setImageResource(active ? R.drawable.icon_worksend_selit_minus
						: R.drawable.icon_worksend_selit_plus);
	}

	@Override
	public void toggleSelectionMode(boolean editModeEnabled) {
	}

	@Override
	public void notifyValue(TreeItemRoot value) {
		name.setText(value.name.toString());
		select.setText(value.select.toString());
	}

}
