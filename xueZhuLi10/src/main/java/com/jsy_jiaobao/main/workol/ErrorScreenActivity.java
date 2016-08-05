package com.jsy_jiaobao.main.workol;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.GenInfo;
import com.jsy_jiaobao.po.sys.StuInfo;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.jsy_jiaobao.po.workol.UnionChapterList;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

/**
 * 错题本条件筛选界面
 * 
 * @author admin
 * 
 */

public class ErrorScreenActivity extends BaseActivity implements
		OnItemSelectedListener {
	/** 年级 */
	final static int TYPE_GRADE = 0;
	/** 科目 */
	final static int TYPE_MODE = 1;
	/** 教版 */
	final static int TYPE_SUBJECT = 2;
	/** 章节 */
	final static int TYPE_SESSION = 3;
	private ViewGroup layout_tree;
	private Context mContext;
	private int role;// 角色
	private GenInfo genInfo;// 家长信息
	private StuInfo stuInfo;// 学生信息
	private TreeNode root;
	private AndroidTreeView tView;
	private SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	private String mGradeName;// 年级名称
	private String mModeName;// 科目名称
	private String mSubjectName;// 教版名称
	private String mChapterName;// 章节名称
	private int ChapterId;// 章节Id
	private int GradeCode;// 年级代码
	private int subCode;// 科目代码
	private int modeId;// 教版代码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		// 获取角色信息

		if (savedInstanceState == null) {
			role = getIntent().getIntExtra("role", 999);
			initData();
		} else {
			role = savedInstanceState.getInt("role");
			switch (role) {
			case 0:
				stuInfo = (StuInfo) savedInstanceState.get("stuInfo");
				break;
			case 1:
				genInfo = (GenInfo) savedInstanceState.get("genInfo");
				break;
			default:
				break;
			}
		}
		initViews();
	}

	// 保存意外销毁的角色信息
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("role", role);
		switch (role) {
		case 0:
			outState.putSerializable("stuInfo", stuInfo);
			break;
		case 1:
			outState.putSerializable("genInfo", genInfo);
			break;
		default:
			break;
		}
	}

	/**
	 * 绑定view
	 */
	private void initViews() {
		setContentLayout(R.layout.workol_activity_error_screen);
		setActionBarTitle("筛选条件");
		ErrorSreenActivityController.getInstance().setContext(mContext);
		layout_tree = (ViewGroup) findViewById(R.id.stu_tree);
		root = TreeNode.root();
		ErrorSreenActivityController.getInstance().GetGradeList();
	}

	// 根据角色类型，获取详细角色信息
	private void initData() {
		switch (role) {
		case 0:
			stuInfo = (StuInfo) getIntent().getSerializableExtra("stuInfo");
			break;
		case 1:
			genInfo = (GenInfo) getIntent().getSerializableExtra("genInfo");
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EventBusUtil.register(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		EventBusUtil.unregister(this);
		super.onPause();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		/**
		 * 是否有题
		 */
		case Constants.WORKOL_TecQs:
			String result = (String) list.get(1);
			if ("true".equals(result)) {
				haveTecQs = true;
			} else {
				ToastUtil.showMessage(
						mContext,
						getResources().getString(
								R.string.workol_session_nocontent));
				haveTecQs = false;
			}
			break;
		// 年级列表
		case Constants.WORKOL_GetGradeList:
			@SuppressWarnings("unchecked")
			ArrayList<TeaGrade> glist = (ArrayList<TeaGrade>) list.get(1);
			TeaGrade allGrade = new TeaGrade();
			allGrade.setTabID(-1);
			allGrade.setGradeCode(-1);
			allGrade.setGradeName("全部年级");
			glist.add(0, allGrade);
			if (glist != null && glist.size() > 0) {
				for (int i = root.getChildren().size() - 1; i >= 0; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				TeaGrade t1 = glist.get(0);
				TreeNode computerRoot = new TreeNode(new TreeItemRoot(
						getResources()
								.getString(R.string.workol_selected_grade), t1))
						.setViewHolder(new TreeItemRootHolder(this, mHandler));
				for (TeaGrade teaGrade : glist) {
					TreeNode select = new TreeNode(new TreeItemRoot(
							getResources().getString(
									R.string.workol_selected_grade), teaGrade))
							.setViewHolder(new TreeItemSelectHolder(this,
									mHandler));
					computerRoot.addChild(select);
				}
				if (tView != null) {
					tView.addNode(root, computerRoot);
				} else {
					root.addChild(computerRoot);
				}
				GradeCode = t1.getGradeCode();
				mGradeName = t1.getGradeName();
				ErrorSreenActivityController.getInstance().GetUnionChapterList(
						TYPE_GRADE, GradeCode, 0, 0, 0);

			}

			break;
		// 章节
		case Constants.WORKOL_GetUnionChapterList:
			// btn_make.setVisibility(View.VISIBLE);
			int typeMode = (Integer) list.get(2);
			DialogUtil.getInstance().cannleDialog();
			try {
				list.get(1);
				if (Integer.valueOf((String) list.get(1).toString()) == -1) {
					switch (typeMode) {
					case TYPE_GRADE:
						getModeTree();
						getSubjectTree();
						getSessionTree();
						break;
					case TYPE_MODE:
						getSubjectTree();
						getSessionTree();
						break;
					case TYPE_SUBJECT:
						getSessionTree();
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				UnionChapterList union = (UnionChapterList) list.get(1);
				ArrayList<TeaMode> args1 = union.getArgs1();
				args1.add(0, getAllMode());
				ArrayList<TeaSubject> args2 = union.getArgs2();
				args2.add(0, getAllSubject());
				ArrayList<TeaSession> args3 = union.getArgs3();
				args3.add(0, getAllSeasion());
				TeaSession selectSession = null;

				switch (typeMode) {
				// 年级
				case TYPE_GRADE:
					// 加载科目数据
					if (args1 != null && args1.size() > 0) {
						subCode = args1.get(0).getSubjectCode();
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_mode),
								args1.get(0)))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaMode teaMode : args1) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_mode),
									teaMode))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							computerRoot.addChild(select);
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_mode),
								new TeaMode()))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					// 加载教版数据
					if (args2 != null && args2.size() > 0) {
						modeId = args2.get(0).getTabID();
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								args2.get(0), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaSubject teaSub : args2) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_subject),
									teaSub, -1))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							computerRoot.addChild(select);
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								new TeaSubject(), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					// 加载章节数据
					if (args3 != null && args3.size() > 0) {
						selectSession = args3.get(0);
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								selectSession, 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaSession session : args3) {
							if (session.getPid() == 0) {
								TreeNode select = new TreeNode(
										new TreeItemRoot(
												getResources()
														.getString(
																R.string.workol_select_session),
												session, 0))
										.setViewHolder(new TreeItemSelectHolder(
												this, mHandler));
								MakeSessionTree(select, session, args3, 1);
								computerRoot.addChild(select);
							}
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								new TeaSession(), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					break;
				// 科目
				case TYPE_MODE:
					// 加载教版数据
					if (args2 != null && args2.size() > 0) {
						modeId = args2.get(0).getTabID();
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								args2.get(0), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaSubject teaSub : args2) {
							TreeNode select = new TreeNode(new TreeItemRoot(
									getResources().getString(
											R.string.workol_select_subject),
									teaSub, -1))
									.setViewHolder(new TreeItemSelectHolder(
											this, mHandler));
							computerRoot.addChild(select);
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_subject),
								new TeaSubject(), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					// 加载章节数据
					if (args3 != null && args3.size() > 0) {
						selectSession = args3.get(0);
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								selectSession, 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaSession session : args3) {
							if (session.getPid() == 0) {
								TreeNode select = new TreeNode(
										new TreeItemRoot(
												getResources()
														.getString(
																R.string.workol_select_session),
												session, 0))
										.setViewHolder(new TreeItemSelectHolder(
												this, mHandler));
								MakeSessionTree(select, session, args3, 1);
								computerRoot.addChild(select);
							}
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								new TeaSession(), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					break;
				// 章节
				case TYPE_SUBJECT:
					if (args3 != null && args3.size() > 0) {
						selectSession = args3.get(0);
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								selectSession, 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						for (TeaSession session : args3) {
							if (session.getChapterCode() == -1) {
								TreeNode select = new TreeNode(
										new TreeItemRoot(
												getResources()
														.getString(
																R.string.workol_select_session),
												session, 0))
										.setViewHolder(new TreeItemSelectHolder(
												this, mHandler));
								computerRoot.addChild(select);
							} else if (session.getPid() == 0) {
								TreeNode select = new TreeNode(
										new TreeItemRoot(
												getResources()
														.getString(
																R.string.workol_select_session),
												session, 0))
										.setViewHolder(new TreeItemSelectHolder(
												this, mHandler));
								MakeSessionTree(select, session, args3, 1);
								computerRoot.addChild(select);
							}
						}
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					} else {
						TreeNode computerRoot = new TreeNode(new TreeItemRoot(
								getResources().getString(
										R.string.workol_select_session),
								new TeaSession(), 0))
								.setViewHolder(new TreeItemRootHolder(this,
										mHandler));
						if (tView != null) {
							tView.addNode(root, computerRoot);
						} else {
							root.addChild(computerRoot);
						}
					}
					break;
				default:
					break;

				}
			}

			if (tView == null) {
				if (root.getChildren().size() > 0) {

					tView = new AndroidTreeView(this, root);
					tView.setDefaultAnimation(false);
					layout_tree.addView(tView.getView());
					tView.setSelectionModeEnabled(true);
				}
			}
			ChapterId = 0;
			if (root.size() > 3) {
				TreeNode parent = root.getChildren().get(3);
				TreeItemRoot value = (TreeItemRoot) parent.getValue();
				ChapterId = value.TabID;

			}
			break;
		//
		}
	}

	/**
	 * 章节
	 */
	private void getSessionTree() {
		// TODO Auto-generated method stub
		TeaSession allSession = getAllSeasion();
		TreeNode computerRoot2 = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_session), allSession, 0))
				.setViewHolder(new TreeItemRootHolder(this, mHandler));

		ArrayList<TeaSession> args3 = new ArrayList<TeaSession>();
		args3.add(allSession);
		TreeNode select2 = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_session), allSession, 0))
				.setViewHolder(new TreeItemSelectHolder(this, mHandler));
		// MakeSessionTree(select2, allSession, args3, 1);
		computerRoot2.addChild(select2);

		if (tView != null) {
			tView.addNode(root, computerRoot2);
		} else {
			root.addChild(computerRoot2);
		}

	}

	/**
	 * 科目
	 */
	private void getSubjectTree() {
		// TODO Auto-generated method stub
		modeId = -1;
		TeaSubject allSubject = getAllSubject();
		TreeNode computerRoot1 = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_subject), allSubject, 0))
				.setViewHolder(new TreeItemRootHolder(this, mHandler));

		TreeNode select1 = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_subject), allSubject, -1))
				.setViewHolder(new TreeItemSelectHolder(this, mHandler));
		computerRoot1.addChild(select1);

		if (tView != null) {
			tView.addNode(root, computerRoot1);
		} else {
			root.addChild(computerRoot1);
		}
	}

	/**
	 * 教版
	 */

	private void getModeTree() {
		// TODO Auto-generated method stub
		subCode = -1;
		TeaMode allMode = getAllMode();
		TreeNode computerRoot = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_mode), allMode))
				.setViewHolder(new TreeItemRootHolder(this, mHandler));

		TreeNode select = new TreeNode(new TreeItemRoot(getResources()
				.getString(R.string.workol_select_mode), allMode))
				.setViewHolder(new TreeItemSelectHolder(this, mHandler));
		computerRoot.addChild(select);

		if (tView != null) {
			tView.addNode(root, computerRoot);
		} else {
			root.addChild(computerRoot);
		}
	}

	// 全部章节
	private TeaSession getAllSeasion() {
		// TODO Auto-generated method stub
		TeaSession allSession = new TeaSession();
		allSession.setPid(0);
		allSession.setTabID(-1);
		allSession.setChapterCode(-1);
		allSession.setChapterName("全部章节");
		return allSession;
	}

	// 全部教版
	private TeaSubject getAllSubject() {
		// TODO Auto-generated method stub
		TeaSubject allSubject = new TeaSubject();
		allSubject.setVersionName("全部教版");
		allSubject.setTabID(-1);
		allSubject.setVersionCode(-1);
		return allSubject;
	}

	// 加载全部科目
	private TeaMode getAllMode() {
		// TODO Auto-generated method stub
		TeaMode allMode = new TeaMode();
		allMode.setTabID(-1);
		allMode.setSubjectCode(-1);
		allMode.setSubjectName("全部科目");
		return allMode;
	}

	boolean haveTecQs = false;

	/**
	 * 生成章节树
	 * 
	 * @param node
	 * @param psession
	 * @param args3
	 * @param Padding
	 */
	private void MakeSessionTree(TreeNode node, TeaSession psession,
			ArrayList<TeaSession> args3, int Padding) {
		for (TeaSession session : args3) {
			if (session.getPid() == psession.getTabID()) {
				TreeNode select = new TreeNode(new TreeItemRoot(getResources()
						.getString(R.string.workol_select_session), session,
						Padding)).setViewHolder(new TreeItemSelectHolder(this,
						mHandler));
				MakeSessionTree(select, session, args3, Padding + 1);
				node.addChild(select);
			}
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			TreeNode node = null;
			TreeItemRoot value;
			switch (msg.what) {
			// 年级
			case TYPE_GRADE:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				TreeNode parentGrade = root.getChildren().get(TYPE_GRADE);
				TreeItemRoot grade = (TreeItemRoot) parentGrade.getValue();
				grade.select = value.select;
				grade.TabID = value.TabID;
				parentGrade.getViewHolder().notifyValue(grade);
				tView.collapseNode(root.getChildren().get(TYPE_GRADE));
				List<TreeNode> list = root.getChildren().get(TYPE_GRADE)
						.getChildren();
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 删除以前的子数据
				for (int i = root.getChildren().size() - 1; i > TYPE_GRADE; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 年级代码 年级名称
				GradeCode = value.TabID;
				mGradeName = value.select;
				// 通过年级获取科目 教版数据
				ErrorSreenActivityController.getInstance().GetUnionChapterList(
						TYPE_GRADE, GradeCode, 0, 0, 0);
				break;
			// 科目
			case TYPE_MODE:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				// 科目Id和科目名称
				subCode = value.TabID;
				mModeName = value.select;
				TreeNode parent = root.getChildren().get(TYPE_MODE);
				TreeItemRoot mode = (TreeItemRoot) parent.getValue();
				// 科目Id和科目名称
				mode.select = value.select;
				mode.TabID = value.TabID;
				parent.getViewHolder().notifyValue(mode);
				tView.collapseNode(root.getChildren().get(TYPE_MODE));
				List<TreeNode> list1 = root.getChildren().get(TYPE_MODE)
						.getChildren();
				/**
				 * 所有选项的选中状态清空
				 */
				for (int i = 0; i < list1.size(); i++) {
					list1.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 删除所有下级菜单
				for (int i = root.getChildren().size() - 1; i > TYPE_MODE; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 通过年级 科目代码 获取 教版 章节数据
				ErrorSreenActivityController.getInstance().GetUnionChapterList(
						TYPE_MODE, GradeCode, subCode, 0, 1);
				break;
			// 教版
			case TYPE_SUBJECT:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				// 选中的教版ID和教版名称
				modeId = value.TabID;
				mSubjectName = value.select;
				TreeNode parentSub = root.getChildren().get(TYPE_SUBJECT);
				TreeItemRoot subValue = (TreeItemRoot) parentSub.getValue();
				subValue.select = value.select;
				subValue.TabID = value.TabID;
				parentSub.getViewHolder().notifyValue(subValue);

				tView.collapseNode(root.getChildren().get(TYPE_SUBJECT));
				List<TreeNode> list11 = root.getChildren().get(TYPE_SUBJECT)
						.getChildren();
				// 清除所有子选项的选中状态
				for (int i = 0; i < list11.size(); i++) {
					list11.get(i).setSelected(false);
				}
				node.setSelected(true);
				// 清除所有下级菜单
				for (int i = root.getChildren().size() - 1; i > TYPE_SUBJECT; i--) {
					TreeNode child = root.getChildren().get(i);
					tView.removeNode(child);
				}
				// 通过年级代码 科目代码 教版代码 获取章节数据
				ErrorSreenActivityController.getInstance().GetUnionChapterList(
						TYPE_SUBJECT, GradeCode, subCode, modeId, 2);
				break;
			// 章节
			case TYPE_SESSION:
				node = (TreeNode) msg.obj;
				value = (TreeItemRoot) node.getValue();
				setTreeNodeSelected(root.getChildren().get(TYPE_SESSION), false);
				node.setSelected(true);
				TreeNode p = root.getChildren().get(TYPE_SESSION);
				if (p != null) {
					TreeItemRoot pv = (TreeItemRoot) p.getValue();
					pv.select = value.select;
					pv.TabID = value.TabID;
					p.getViewHolder().notifyValue(pv);
				}
				TreeNode c = root.getChildren().get(
						root.getChildren().size() - 1);
				if (c != null) {
					TreeItemRoot cv = (TreeItemRoot) c.getValue();
					if (cv.name.equals(getResources().getString(
							R.string.workol_select_title))) {
						TreeItemRoot subvalue = (TreeItemRoot) root
								.getChildren().get(1).getValue();
						/**
						 * 填写默认的作业名称
						 */
						cv.select = formatter.format(new Date())
								+ subvalue.select + value.select + "练习";
						c.getViewHolder().notifyValue(cv);

					}
				}
				ChapterId = value.TabID;
				mChapterName = value.select;
				if (ChapterId != -1) {
					ErrorSreenActivityController.getInstance().TecQs(
							value.TabID);
				}
				break;
			default:
				break;
			}
		}

	};

	private void setTreeNodeSelected(TreeNode node, boolean selected) {
		for (int i = 0; i < node.getChildren().size(); i++) {
			TreeNode child = node.getChildren().get(i);
			setTreeNodeSelected(child, selected);
			child.setSelected(selected);
		}
	}

	/**
	 * 返回数值
	 */
	@Override
	public void setResultForLastActivity() {
		// TODO Auto-generated method stub
		for (int i = 0; i < root.getChildren().size(); i++) {
			TreeNode parent = root.getChildren().get(i);
			TreeItemRoot value = (TreeItemRoot) parent.getValue();
			// 位置
			if (i == 0) {
				// 年级代码
				GradeCode = value.TabID;
				// 年级名称
				mGradeName = value.select;
			} else if (i == 1) {
				// 教版
				subCode = value.TabID;
				mSubjectName = value.select;
			} else if (i == 2) {
				// 学科代码
				modeId = value.TabID;
				// 学科名称
				mModeName = value.select;
			} else if (i == 3) {
				// 章节Id
				ChapterId = value.TabID;
				// 章节名称
				mChapterName = value.select;
			}
		}
		// 所选选章节名称
		String Select = mGradeName + " " + mModeName + " " + mSubjectName + " "
				+ mChapterName;
		// if (haveTecQs) {

		if (root.size() > 3) {
			// 传递数据
			Intent i = new Intent();
			Bundle args = new Bundle();
			args.putInt("GradeCode", GradeCode);
			args.putInt("SubCode", subCode);
			args.putInt("ModeId", modeId);
			args.putInt("ChapterId", ChapterId);
			args.putString("Select", Select);
			i.putExtras(args);
			setResult(999, i);
		}
	}

	// 按下系统返回键的效果
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (haveTecQs) {
			// 判断是否选择了章节
			if (root.size() > 3) {
				// 如果是，传递给上一 Activity
				Intent i = new Intent();
				Bundle args = new Bundle();
				args.putInt("GradeCode", GradeCode);
				args.putInt("SubCode", subCode);
				args.putInt("UId", modeId);
				args.putInt("ChapterId", ChapterId);
				i.putExtras(args);
				setResult(999, i);
				Log.d("999", "999");
			} else {
				// 否则让他们去吃土
				ToastUtil.showMessage(mContext, "mei ");
			}
			finish();

		}
		super.onBackPressed();
	}

}
