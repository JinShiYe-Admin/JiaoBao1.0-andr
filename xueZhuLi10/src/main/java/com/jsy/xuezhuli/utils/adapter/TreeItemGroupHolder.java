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
    private CheckBox cb_all;
    private ImageView arrowView_1;
    private ImageView arrowView_2;
    private Handler mHandler;

    public TreeItemGroupHolder(Context context, Handler mHandler) {
        super(context);
        this.mHandler = mHandler;
    }


    @Override
    public View createNodeView(final TreeNode node, TreeItemGroup value) {
        node.HOLDER_TYPE = value.HOLDER_TYPE;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_worksend_tree_root, null, false);
        view.setPadding(node.getLevel() * 20, 0, 20, 0);
        TextView name = (TextView) view.findViewById(R.id.tree_root_tv_name);
        arrowView_1 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_1);
        arrowView_2 = (ImageView) view.findViewById(R.id.tree_root_iv_arrow_2);
        cb_all = (CheckBox) view.findViewById(R.id.tree_root_cb_all);
        CheckBox cb_invert = (CheckBox) view.findViewById(R.id.tree_root_cb_invert);
        cb_all.setChecked(node.isSelected());
        cb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setChildSelect(node, true);
                    p_y(node);
                } else {
                    p_n(node);
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
        switch (value.HOLDER_TYPE) {
            case TYPE_MY:
                name.setText(value.myUnit.getUintName());
                break;
            case TYPE_PARENT:
                name.setText(value.commMsgRevicerUnit.getUintName());
                break;
            case TYPE_SUB:
                name.setText(value.commMsgRevicerUnit.getUintName());
                break;
            case TYPE_CLASS:
                name.setText(value.commMsgRevicerUnitClass.getClsName());
                break;
            case TYPE_GROUP:
                name.setTextColor(context.getResources().getColor(R.color.color_292929));
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
                name.setText(value.group.getGroupName());
                break;
            default:
                break;
        }
        return view;
    }

    /**
     * 父类是否取消
     */
    private void p_y(TreeNode node) {
        getTreeView().selectNode(node, true);
        TreeNode parent = node.getParent();
        if (parent != null) {
            boolean isSelect = true;
            x:
            for (TreeNode n : parent.getChildren()) {
                if (!n.isSelected()) {
                    isSelect = false;
                    break;
                }
            }
            if (!isSelect) {
                p_n(parent);
            } else {
                getTreeView().selectNode(parent, true);
            }
        }
    }

    /**
     * 父类全取消
     */
    private void p_n(TreeNode node) {
        getTreeView().selectNode(node, false);
        TreeNode parent = node.getParent();
        if (parent != null) {
            p_n(parent);
        }
    }

    private void setChildSelect(TreeNode node, boolean select) {
        if (node != null) {
            getTreeView().selectNode(node, select);
            for (TreeNode n : node.getChildren()) {
                setChildSelect(n, select);
            }
        }
    }

    private void setChildInvert(TreeNode node) {
        for (TreeNode n : node.getChildren()) {
            setChildInvert(n);
            if (n.getValue() instanceof TreeItemPersonHolder.TreeItemPerson) {
                getTreeView().selectNode(n, !n.isSelected());
            }
        }
    }

    @Override
    public void toggle(boolean active) {
        arrowView_1.setImageResource(active ? R.drawable.icon_worksend_selit_minus : R.drawable.icon_worksend_selit_plus);
        arrowView_2.setImageResource(active ? R.drawable.icon_worksend_selit_minus_m : R.drawable.icon_worksend_selit_plus_p);
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

        public TreeItemGroup(MyUnit myUnit, int holderType) {
            this.myUnit = myUnit;
            this.HOLDER_TYPE = holderType;
            this.name = myUnit.getUintName();
        }

        public TreeItemGroup(CommMsgRevicerUnit commMsgRevicerUnit, int holderType) {
            this.commMsgRevicerUnit = commMsgRevicerUnit;
            this.HOLDER_TYPE = holderType;
            this.name = commMsgRevicerUnit.getUintName();
        }

        public TreeItemGroup(GroupUserList group, int holderType) {
            this.HOLDER_TYPE = holderType;
            this.group = group;
            this.name = group.getGroupName();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}