package com.jsy.xuezhuli.utils.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnit;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;
import com.jsy_jiaobao.po.personal.MyUnit;
import com.jsy_jiaobao.po.sys.GroupUserList;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class TreeItemGroupHolder extends TreeNode.BaseNodeViewHolder<TreeItemGroupHolder.TreeItemGroup> {
    final static int TYPE_MY = 0;
    final static int TYPE_PARENT = 1;
    final static int TYPE_SUB = 2;
    final static int TYPE_CLASS = 3;
    final static int TYPE_GROUP = 4;
    final static int TYPE_PERSON = 5;
    final static int TYPE_GEN = 6;
	private TextView name;
    private CheckBox cb_all;
    private CheckBox cb_invert;
    private ImageView arrowView_1;
    private ImageView arrowView_2;
    private Handler mHandler;
    private boolean toggle;
    public TreeItemGroupHolder(Context context, Handler mHandler) {
        super(context);
        this.mHandler = mHandler;
    }


    @Override
    public View createNodeView(final TreeNode node, TreeItemGroup value) {
    	node.HOLDER_TYPE =value.HOLDER_TYPE;
    	final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_worksend_tree_root, null, false);
        view.setPadding(node.getLevel()*20, 0, 20, 0);
        name = (TextView) view.findViewById(R.id.tree_root_tv_name);
        arrowView_1 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_1);
        arrowView_2 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_2);
        
        cb_all = (CheckBox) view.findViewById(R.id.tree_root_cb_all);
        cb_invert = (CheckBox) view.findViewById(R.id.tree_root_cb_invert);
        cb_all.setChecked(node.isSelected());
        cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					setChildSelect(node, isChecked);
					p_y(node);
				}else{
					p_n(node);
				}
				
			}
		});
        cb_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setChildSelect(node,cb_all.isChecked());
			}
		});
        cb_invert.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		setChildInvert(node);
        	}
        });
        switch (value.HOLDER_TYPE) {
		case TYPE_MY:
			name.setText(value.myUnit.getUintName().toString());
//			view.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Message msg = new Message();
//					msg.what = TYPE_MY;
//					msg.obj = toggle;
//					mHandler.sendMessage(msg);
//				}
//			});
			break;
		case TYPE_PARENT:
			name.setText(value.commMsgRevicerUnit.getUintName().toString());
//			view.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Message msg = new Message();
//					msg.what = TYPE_PARENT;
//					msg.obj = toggle;
//					mHandler.sendMessage(msg);
//				}
//			});
			break;
		case TYPE_SUB:
			name.setText(value.commMsgRevicerUnit.getUintName().toString());
//			view.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Message msg = new Message();
//					msg.what = TYPE_SUB;
//					msg.obj = toggle;
//					mHandler.sendMessage(msg);
//				}
//			});
			break;
		case TYPE_CLASS:
			name.setText(value.commMsgRevicerUnitClass.getClsName().toString());
//			view.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Message msg = new Message();
//					msg.what = TYPE_CLASS;
//					msg.obj = toggle;
//					mHandler.sendMessage(msg);
//				}
//			});
			break;
		case TYPE_GROUP:
			name.setTextColor(context.getResources().getColor(R.color.color_292929));
			view.setBackgroundColor(context.getResources().getColor(R.color.white));
			name.setText(value.group.getGroupName().toString());
			break;

		default:
			break;
		}	

        return view;
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
    private void setSelfSelect(TreeNode node){
    	boolean select = true;
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
    @Override
    public void toggle(boolean active) {
    	toggle = active;
    	arrowView_1.setImageResource(active? R.drawable.icon_worksend_selit_minus : R.drawable.icon_worksend_selit_plus);
    	arrowView_2.setImageResource(active? R.drawable.icon_worksend_selit_minus_m : R.drawable.icon_worksend_selit_plus_p);
    }
	@Override
	public void toggleSelectionMode(boolean editModeEnabled) {
		cb_all.setChecked(mNode.isSelected());
	}
    public static class TreeItemGroup {
        public CommMsgRevicerUnit commMsgRevicerUnit;
        public CommMsgRevicerUnitClass commMsgRevicerUnitClass;
        public MyUnit myUnit;
        public GroupUserList group;
        public int HOLDER_TYPE;
        private String name;
        
		public TreeItemGroup(MyUnit myUnit,int holderType) {
			this.myUnit = myUnit;
			this.HOLDER_TYPE = holderType;
			this.name = myUnit.getUintName();
		}
		
		public TreeItemGroup(CommMsgRevicerUnit commMsgRevicerUnit,int holderType) {
			this.commMsgRevicerUnit = commMsgRevicerUnit;
			this.HOLDER_TYPE = holderType;
			this.name = commMsgRevicerUnit.getUintName();
		}

		public TreeItemGroup(CommMsgRevicerUnitClass commMsgRevicerUnitClass,int holderType) {
			this.commMsgRevicerUnitClass = commMsgRevicerUnitClass;
			this.HOLDER_TYPE = holderType;
			this.name = commMsgRevicerUnitClass.getClsName();
		}
		
		public TreeItemGroup(GroupUserList group, int holderType) {
			this.HOLDER_TYPE = holderType;
			this.group = group;
			this.name = group.getGroupName();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return name;
		}
		
    }
}
