package com.jsy.xuezhuli.utils.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.Selit;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class TreeItemPersonHolder extends TreeNode.BaseNodeViewHolder<TreeItemPersonHolder.TreeItemPerson> {
    private CheckBox cb_1;
    private Handler mHandler;
    private boolean toggle;
    public TreeItemPersonHolder(Context context, Handler mHandler) {
        super(context);
        this.mHandler = mHandler;
    }


    @Override
    public View createNodeView(final TreeNode node, TreeItemPerson value) {
    	node.HOLDER_TYPE =value.HOLDER_TYPE;
    	final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_treeview_person, null, false);
        view.setPadding(node.getLevel()*20, 0, 20, 0);
        cb_1 = (CheckBox) view.findViewById(R.id.item_treeview_person_cb);
        cb_1.setText(value.person.getName().trim());
        cb_1.setChecked(node.isSelected());
        cb_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					p_n(node);
				}else{
					p_y(node);
				}
			}
		});
        return view;
    }

    /**
     * 父类全取消
     * @param node
     */
    private void p_n(TreeNode node){
    	getTreeView().selectNode(node, false);
    	TreeNode parent = node.getParent();
    	if (parent != null) {
    		p_n(parent);
		}
    }
    /**
     * 父类是否取消
     * @param node
     */
    private void p_y(TreeNode node){
    	getTreeView().selectNode(node, true);
    	TreeNode parent = node.getParent();
    	if (parent != null) {
    		boolean isSelect = true;
    		x:for(TreeNode n : parent.getChildren()){
    			if (!n.isSelected()) {
    				isSelect = false;
    				break x;
				}
    		}
    		if (!isSelect) {
				p_n(parent);
			}else{
				getTreeView().selectNode(parent, isSelect);
			}
    	}
    }
    @Override
    public void toggle(boolean active) {
    	toggle = active;
    }
	@Override
	public void toggleSelectionMode(boolean editModeEnabled) {
		cb_1.setChecked(mNode.isSelected());
	}
    public static class TreeItemPerson {
        /**
         * HOLDER_TYPE
         * 5：单位人员；6：家长；7：学生*/
        public int HOLDER_TYPE;
        private String name;
        public Selit person;
        /**
         * HOLDER_TYPE
         * 5：单位人员；6：家长；7：学生*/
		public TreeItemPerson(Selit person,int holderType) {
			this.HOLDER_TYPE = holderType;
			this.name = person.getName();
			this.person = person;
		}

		@Override
		public String toString() {
			return name;
		}
		
    }
}
