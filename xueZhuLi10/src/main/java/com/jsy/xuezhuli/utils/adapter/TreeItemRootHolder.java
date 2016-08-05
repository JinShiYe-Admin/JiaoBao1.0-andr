package com.jsy.xuezhuli.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.MyUnit;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class TreeItemRootHolder extends TreeNode.BaseNodeViewHolder<TreeItemRootHolder.TreeItemRoot> {
    private TextView name;
    public CheckBox cb_all;
    private CheckBox cb_invert;
    private ImageView arrowView_1;
    private ImageView arrowView_2;
    public TreeItemRootHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, TreeItemRoot value) {
    	node.HOLDER_TYPE = value.HOLDER_TYPE;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_worksend_tree_root, null, false);
        view.setPadding(10, 0, 10, 0);
        name = (TextView) view.findViewById(R.id.tree_root_tv_name);
        arrowView_1 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_1);
        arrowView_2 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_2);
        cb_all = (CheckBox) view.findViewById(R.id.tree_root_cb_all);
        cb_invert = (CheckBox) view.findViewById(R.id.tree_root_cb_invert);
        name.setText(value.name.toString());
		name.setTextColor(context.getResources().getColor(R.color.color_292929));
    	cb_all.setChecked(node.isSelected());
        cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					setChildSelect(node, isChecked);
				}
			}
		});
        cb_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setChildSelect(node, cb_all.isChecked());
			}
		});
        cb_invert.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		setChildInvert(node);
        	}
        });
        return view;
    }
    private void setChildSelect(TreeNode node, boolean select){
    	if (node != null) {
    		getTreeView().selectNode(node, select);
			for (TreeNode n : node.getChildren()) {
				setChildSelect(n,select);
			}
		}
    }
    private void setChildInvert(TreeNode node){
    	for (TreeNode n : node.getChildren()) {
    		setChildInvert(n);
    		if (n.getValue() instanceof TreeItemPersonHolder.TreeItemPerson) {
    			getTreeView().selectNode(n, !n.isSelected());
//    			setParentSelect(node);
			}
    	}
    }
    private void setParentSelect(TreeNode node){
		boolean set = true;
		x:for (TreeNode n : node.getChildren()) {
			if (!n.isSelected()) {
				set = false;
				break x;
			}
		}
		if (!set) {
			getTreeView().selectNode(node, set);
		}
    }
    private void setSelfSelect(TreeNode node){
    	boolean select = node.isSelected();
    	boolean isLast = false;
    	x:for(TreeNode n : node.getChildren()){
    		setSelfSelect(n);
    		if (n.getValue() instanceof TreeItemPersonHolder.TreeItemPerson) {
    			isLast = true;
    			if (!n.isSelected()) {
    				select = false;
    				break x;
    			}
			}
    	}
    	if (isLast) {
    		getTreeView().selectNode(node, select);
		}
    }
    @Override
    public void toggle(boolean active) {
    	arrowView_1.setImageResource(active? R.drawable.icon_worksend_selit_minus : R.drawable.icon_worksend_selit_plus);
    	arrowView_2.setImageResource(active? R.drawable.icon_worksend_selit_minus_m : R.drawable.icon_worksend_selit_plus_p);
    }
	@Override
	public void toggleSelectionMode(boolean editModeEnabled) {
		cb_all.setChecked(mNode.isSelected());
	}
    public static class TreeItemRoot {
        public String name;
        public int HOLDER_TYPE;

		public TreeItemRoot(String name,int type) {
			this.name = name;
			this.HOLDER_TYPE =type;
		}


		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name;
		}
		
    }


}
