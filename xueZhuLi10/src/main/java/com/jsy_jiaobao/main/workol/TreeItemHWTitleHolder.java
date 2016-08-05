
package com.jsy_jiaobao.main.workol;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * 树形结构
 * @author admin
 *
 */
public class TreeItemHWTitleHolder extends TreeNode.BaseNodeViewHolder<TreeItemRoot> {
    private TextView name;
    private IEditText title;
    public TreeItemHWTitleHolder(Context context) {
        super(context);
    }
    /**
     * 
     */
    @Override
    public View createNodeView(final TreeNode node, final TreeItemRoot value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.tree_workol_stu_hwtitle, null, false);
        view.setPadding(10, 0, 10, 0);
        name = (TextView) view.findViewById(R.id.tree_root_tv_name);
        title = (IEditText) view.findViewById(R.id.tree_root_edt_title);
        name.setText(value.name);
        title.setText(value.select);
        title.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					title.append(" ");
					return true;
				}
				return false;
			}
		});
        title.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				value.select = s.toString();
				
			}
		});
        return view;
    }
    @Override
    public void toggle(boolean active) {
//    	arrowView.setImageResource(active? R.drawable.icon_worksend_selit_minus : R.drawable.icon_worksend_selit_plus);
    }
	@Override
	public void toggleSelectionMode(boolean editModeEnabled) {
	}

	@Override
	public void notifyValue(TreeItemRoot value) {
        name.setText(value.name);
        title.setText(value.select);
	}
}
