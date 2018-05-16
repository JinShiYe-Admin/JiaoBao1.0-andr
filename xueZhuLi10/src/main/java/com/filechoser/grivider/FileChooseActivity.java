/**
 * 文件选择器对话框
 */
package com.filechoser.grivider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.affairs.WorkSendToSbActivity;

import java.io.File;
import java.util.List;

public class FileChooseActivity extends BaseActivity implements OnClickListener {
	private GridView gridView = null;// gridView对象
	private TextView emptyText = null;// 当文件夹为空时显示
	List<String> fileList = null;// 当前路径下的所有子文件列表
	FilePerate filePerate = null;// 文件操作对象
	GridViewAdapter gridAdapter = null;// GridView的适配器
	private TextView title;
	private ImageView back;
	Context context = null;// 上下文对象
  //测试
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.grid_view);
		context = this;
		getSupportActionBar().setCustomView(R.layout.actionbar);
		title = (TextView) findViewById(R.id.actionbar_title);
		back= (ImageView) findViewById(R.id.actionbar_back);
		title.setText("返回上一级");
		back.setOnClickListener(this);
		//title.setOnClickListener(this);
		filePerate = new FilePerate();// 得到一个文件操作对象
		fileList = filePerate.getAllFile(FilePerate.getRootFolder());// 得到根目录下的所有子目录

		emptyText = (TextView) findViewById(R.id.empty_text);// TextView对象，当文件夹为空时显示“文件夹为空"
		gridView = (GridView) findViewById(R.id.grid_view);
		gridAdapter = new GridViewAdapter(filePerate, fileList, context);// 得到适配器
		gridView.setAdapter(gridAdapter);// 为GridView添加适配器

		// 为gridView的子对象设置监听器，即当点击文件时的动作
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = "";
				// 得到选中文件的目录
				path = filePerate.getCurrentPath() + "//"
						+ filePerate.getFileList().get(position);
				File file = new File(path);
				if (file.isDirectory()) {
					fileList = filePerate.getAllFile(path);// 得到选择的文件路径下的文件List
					if (fileList != null) {// 即如果点击的不是文件而是文件夹，更新适配器的数据源
						setEmptyTextState(fileList.size());// 设置TextView的状态
						gridAdapter.notifyDataSetChanged();// 通知适配器，数据源以改变
					}
				} else {
					Intent result = new Intent(context,
							WorkSendToSbActivity.class);
					result.putExtra("path", path);
					setResult(10, result);
					finish();
				}
			}
		});	
	}

	// 键盘监听事件
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:// 如果按下返回键
			// 如果当前已经到跟目录
			if ((filePerate.getCurrentPath())
					.equals(FilePerate.getRootFolder())) {
				this.finish();// 如果已经返回到根目录，则关闭兑换框
				WorkSendToSbActivity.filePath = FilePerate.getRootFolder();// 得到上一级目录
				return false;
			}
			// 否则得到上级目录
			String path = filePerate.getParentFolder(filePerate
					.getCurrentPath());
			fileList = filePerate.getAllFile(path);// 更新数据源
			setEmptyTextState(fileList.size());// 设置TextView的状态
			gridAdapter.notifyDataSetChanged();// 更新GridAdapter
			break;
		default:
			break;
		}
		return false;
	}

	// 设置文本的状态
	public void setEmptyTextState(int num) {
		// 如果该文件夹下存在文件（即num>0)则不显示textView,否则显示
		emptyText.setVisibility(View.VISIBLE);
		if (num > 0) {
			emptyText.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:// 点击返回图标事件
			// 如果当前已经到跟目录
			if ((filePerate.getCurrentPath())
					.equals(FilePerate.getRootFolder())) {
				this.finish();// 如果已经返回到根目录，则关闭兑换框
				WorkSendToSbActivity.filePath = FilePerate.getRootFolder();// 得到上一级目录
				return false;
			}
			// 否则得到上级目录
			String path = filePerate.getParentFolder(filePerate
					.getCurrentPath());
			fileList = filePerate.getAllFile(path);// 更新数据源
			setEmptyTextState(fileList.size());// 设置TextView的状态
			gridAdapter.notifyDataSetChanged();// 更新GridAdapter
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		Log.i("onClick", "onClick");
		switch (v.getId()) {
		case R.id.actionbar_back:
		//case R.id.actionbar_title:	
			if ((filePerate.getCurrentPath())
					.equals(FilePerate.getRootFolder())) {
				this.finish();// 如果已经返回到根目录，则关闭兑换框
				WorkSendToSbActivity.filePath = FilePerate.getRootFolder();// 得到上一级目录				
			}
			// 否则得到上级目录
			String path = filePerate.getParentFolder(filePerate
					.getCurrentPath());
			fileList = filePerate.getAllFile(path);// 更新数据源
			setEmptyTextState(fileList.size());// 设置TextView的状态
			gridAdapter.notifyDataSetChanged();// 更新GridAdapter
			break;

		default:
			break;
		}
	}
}
