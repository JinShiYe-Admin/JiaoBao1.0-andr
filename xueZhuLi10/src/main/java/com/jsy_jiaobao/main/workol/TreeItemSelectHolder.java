package com.jsy_jiaobao.main.workol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.jsy_jiaobao.main.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * 树形结构
 * 
 * @author admin
 * 
 */
public class TreeItemSelectHolder extends
		TreeNode.BaseNodeViewHolder<TreeItemRoot> {
	/** 年级 */
	final static int TYPE_GRADE = 0;
	/** 科目 */
	final static int TYPE_MODE = 1;
	/** 教版 */
	final static int TYPE_SUBJECT = 2;
	/** 章节 */
	final static int TYPE_SESSION = 3;
	/** 标题 */
	final static int TYPE_TITLE = 4;
	private TreeItemRoot value;
	private TreeNode node;
	private Handler mHandler;
	private TextView name;
	private ImageView arrowView;
	private RadioButton radiobutton;

	public TreeItemSelectHolder(Context context, Handler mHandler) {
		super(context);
		this.mHandler = mHandler;
	}

	@Override
	public View createNodeView(TreeNode node, TreeItemRoot value) {
		this.value = value;
		this.node = node;
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View view = inflater.inflate(R.layout.tree_workol_stu_select,
				null, false);
		name = (TextView) view.findViewById(R.id.tree_workol_stu_tv_name);
		radiobutton = (RadioButton) view
				.findViewById(R.id.tree_workol_stu_radiobutton);
		arrowView = (ImageView) view.findViewById(R.id.tree_root_iv_arrow);
		if (value.Padding >= 0) {
			view.setPadding(10 + value.Padding * 40, 0, 0, 0);
		}
		name.setText(value.select);
		if (node.isFirstChild() && node.getLevel() == 2) {
			radiobutton.setChecked(true);
		}
		name.setOnClickListener(onClickListener);
		radiobutton.setOnClickListener(onClickListener);
		return view;
	}

	/**
	 * 点击事件监听
	 */

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Message msg = new Message();
			msg.what = value.TYPE;
			msg.obj = node;
			mHandler.sendMessage(msg);
		}
	};

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
	public void toggleRadioButton(boolean isSelected) {
		if (radiobutton != null) {
			radiobutton.setChecked(isSelected);
		}
	}

	@Override
	public void toggleArrowStatus(boolean isleaf) {
		if (arrowView != null) {
			// 是否叶子点 选择显示 隐藏
			if (isleaf) {
				arrowView.setVisibility(View.INVISIBLE);
			} else {
				arrowView.setVisibility(View.VISIBLE);
			}
		}
	}

}
