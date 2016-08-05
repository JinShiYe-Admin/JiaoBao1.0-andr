package com.unnamed.b.atv.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.workol.TreeItemRoot;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.unnamed.b.atv.view.TreeNodeWrapperView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Bogdan Melnychuk on 2/10/15.
 */
public class TreeNode {
    public static final String NODES_ID_SEPARATOR = ":";

    private int mId;
    private TreeNode mParent;
    private boolean mSelected;
    private boolean mSelectable = true;
    private final List<TreeNode> children;
    private BaseNodeViewHolder mViewHolder;
    private TreeNodeClickListener mListener;
    private Object mValue;
    private boolean mExpanded;
    public int HOLDER_TYPE = -1;
    private int mScreenType=0;//0,normal;1,+全部;
    public static TreeNode root() {
        TreeNode root = new TreeNode(null);
        root.setSelectable(false);
        return root;
    }

    public TreeNode(Object value) {
        children = new ArrayList<TreeNode>();
        mValue = value;
    }

    public TreeNode addChild(TreeNode childNode) {
        childNode.mParent = this;
        //TODO think about id generation
        childNode.mId = size();
        children.add(childNode);
        return this;
    }

    public TreeNode addChildren(TreeNode... nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public TreeNode addChildren(Collection<TreeNode> nodes) {
        for (TreeNode n : nodes) {
            addChild(n);
        }
        return this;
    }

    public int deleteChild(TreeNode child) {
        for (int i = 0; i < children.size(); i++) {
            if (child.mId == children.get(i).mId) {
                children.remove(i);
                return i;
            }
        }
        return -1;
    }

    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int size() {
    	if(mScreenType==0){
        return children.size();
        }else{
        	return children.size()+1;
        }
    }

    public TreeNode getParent() {
        return mParent;
    }

    public int getId() {
        return mId;
    }

    public boolean isLeaf() {
        return size() == 0;
    }

    public Object getValue() {
        return mValue;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public TreeNode setExpanded(boolean expanded) {
        mExpanded = expanded;
        return this;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        getViewHolder().toggleRadioButton(mSelected);;
    }

    public boolean isSelected() {
        if (mSelectable) {
            return mSelected;
        } else {
            return false;
        }
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    public boolean isSelectable() {
        return mSelectable;
    }

    public String getPath() {
        final StringBuilder path = new StringBuilder();
        TreeNode node = this;
        while (node.mParent != null) {
            path.append(node.getId());
            node = node.mParent;
            if (node.mParent != null) {
                path.append(NODES_ID_SEPARATOR);
            }
        }
        return path.toString();
    }


    public int getLevel() {
        int level = 0;
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
            level++;
        }
        return level;
    }

    public boolean isLastChild() {
        if (!isRoot()) {
            int parentSize = mParent.children.size();
            if (parentSize > 0) {
                final List<TreeNode> parentChildren = mParent.children;
                return parentChildren.get(parentSize - 1).mId == mId;
            }
        }
        return false;
    }

    public TreeNode setViewHolder(BaseNodeViewHolder viewHolder) {
        mViewHolder = viewHolder;
        if (viewHolder != null) {
            viewHolder.mNode = this;
        }
        return this;
    }

    public TreeNode setClickListener(TreeNodeClickListener listener) {
        mListener = listener;
        return this;
    }


    public TreeNodeClickListener getClickListener() {
        return this.mListener;
    }

    public BaseNodeViewHolder getViewHolder() {
        return mViewHolder;
    }

    public boolean isFirstChild() {
        if (!isRoot()) {
            List<TreeNode> parentChildren = mParent.children;
            return parentChildren.get(0).mId == mId;
        }
        return false;
    }

    public boolean isRoot() {
        return mParent == null;
    }

    public TreeNode getRoot() {
        TreeNode root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }
    

    public int getScreenType() {
		return mScreenType;
	}

	public void setScreenType(int screenType) {
		mScreenType = screenType;
	}


	public interface TreeNodeClickListener {
        void onClick(TreeNode node, Object value);
    }

    public static abstract class BaseNodeViewHolder<E> {
        protected AndroidTreeView tView;
        protected TreeNode mNode;
        private View mView;
        protected int containerStyle;
        protected Context context;
        protected CheckBox cb_all_;
        public BaseNodeViewHolder(Context context) {
            this.context = context;
        }

        public View getView() {
            if (mView != null) {
                return mView;
            }
            final View nodeView = getNodeView();
            final TreeNodeWrapperView nodeWrapperView = new TreeNodeWrapperView(nodeView.getContext(), getContainerStyle());
            nodeWrapperView.insertNodeView(nodeView);
            mView = nodeWrapperView;
            return mView;
        }

        public void setTreeViev(AndroidTreeView treeViev) {
            this.tView = treeViev;
        }

        public AndroidTreeView getTreeView() {
            return tView;
        }

        public void setContainerStyle(int style) {
            containerStyle = style;
        }

        public View getNodeView() {
            return createNodeView(mNode, (E) mNode.getValue());
        }

        public ViewGroup getNodeItemsView() {
            return (ViewGroup) getView().findViewById(R.id.node_items);
        }

        public boolean isInitialized() {
            return mView != null;
        }

        public int getContainerStyle() {
            return containerStyle;
        }


        public abstract View createNodeView(TreeNode node, E value);
        
        public void notifyValue(E value){}
        
        public void toggleRadioButton(boolean isSelected){}
        
        public void toggle(boolean active) {
            // empty
        }

        public void toggleSelectionMode(boolean editModeEnabled) {
            // empty
        }
        public void toggleArrowStatus(boolean isleaf){}
    }


}
