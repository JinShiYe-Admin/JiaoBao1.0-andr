package com.jsy_jiaobao.main.schoolcircle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.customview.CusGridView;
import com.jsy_jiaobao.customview.CusListView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.PersonalSpaceActivity;
import com.jsy_jiaobao.po.personal.ArthInfo;
import com.jsy_jiaobao.po.personal.Comment;
import com.jsy_jiaobao.po.personal.CommentsList;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * 学校圈主界面 list 的Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class Show2ArtListAdapter<T> extends BaseAdapter implements ConstantUrl {

	private Context mContext;
	private PopupWindow popupWindow;
	private PopupWindow popupWindowComment;// 评论的Popup
	private View parentView;
	private List<T> mData;
	private BitmapUtils bitmap;
	private String str_todaytime;
	private String[] str_todaytimes;
	private SimpleDateFormat dateFormat;
	private Date today;
	String mainUrl;
	private ShowClickType clickType;
	private ArrayList<PictureAdapter> picAdapterList = new ArrayList<PictureAdapter>();
	private ArrayList<CommitAdapter> commitListUnit = new ArrayList<CommitAdapter>();
	private ArrayList<CommitAdapter> commitListClass = new ArrayList<CommitAdapter>();
	private ArrayList<CommitAdapter> commitListLocation = new ArrayList<CommitAdapter>();
	private ArrayList<CommitAdapter> commitListAtt = new ArrayList<CommitAdapter>();
	private ArrayList<CommitAdapter> commitListAll = new ArrayList<CommitAdapter>();

	private ImageView pp_like;
	private ImageView pp_commit;
	/**
	 * 发送评论
	 */
	private ImageView pp_comment;
	// 评论输入框
	private IEditText pp_input;

	public Show2ArtListAdapter(Context mContext) {
		this.mContext = mContext;
		bitmap = JSYApplication.getInstance().bitmap;
		// 日期格式化
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		today = new Date(System.currentTimeMillis());
		str_todaytime = dateFormat.format(today);
		str_todaytimes = str_todaytime.split(" ");
		mainUrl = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		Constant.ScreenWith = dm.widthPixels;// 宽度
		Constant.ScreenHeight = dm.heightPixels;// 高度
		// 加载PopWindow
		initPopWindow();
		initPopWindowComment();
	}

	// 设置数据
	public void setData(List<T> mData, ShowClickType clickType) {
		this.mData = mData;
		this.clickType = clickType;
		picAdapterList.clear();
	}

	// 数量
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 功能：对文章进行点赞操作。一篇文章一个用户 （以教宝号为准）只能赞一次。
	 * 
	 * @param params
	 */
	public void LikeIt(final ArthInfo arth) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("aid", arth.getTabIDStr());
		params.addBodyParameter("goflag", String.valueOf(arth.getLikeflag()));
		HttpUtil.InstanceSend(LikeIt, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null != mContext) {
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);
						String ResultCode = jsonObj.getString("ResultCode");

						if ("0".equals(ResultCode)) {
							arth.setLikeCount(arth.getLikeCount() + 1);
							arth.setLikeflag(1);
							popupWindow.dismiss();
							ToastUtil.showMessage(mContext,
									R.string.praise_success);
							ArrayList<Object> post = new ArrayList<Object>();
							post.add(Constant.msgcenter_articlelist_addComment);
							EventBusUtil.post(post);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				DialogUtil.getInstance().cannleDialog();
				ToastUtil.showMessage(mContext, R.string.fuck_failed);
			}
		});
	}

	/**
	 * 取本单位栏目文章 详情 客户端通过本接口获取本单位栏目文章。详情
	 * 
	 * @param arth
	 * @param params
	 */
	public void GetArthInfo(final ArthInfo arth) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("sid", arth.getSectionID());
		params.addBodyParameter("aid", arth.getTabIDStr());
		HttpUtil.InstanceSend(GetArthInfo, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (null != mContext) {
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");

								if ("0".equals(ResultCode)) {
									String data = Des.decrypt(jsonObj
											.getString("Data"),
											BaseActivity.sp_sys.getString(
													"ClientKey", ""));
									ArthInfo GetArthInfo = GsonUtil
											.GsonToObject(data, ArthInfo.class);

									if (GetArthInfo.getLikeflag() == 0) {
										LikeIt(arth);
									} else {
										ToastUtil.showMessage(mContext,
												R.string.praised);
									}
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 初始化popWindow
	 * */
	private void initPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View popView = inflater.inflate(R.layout.popup_artlist, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		pp_like = (ImageView) popView.findViewById(R.id.pp_artlist_img_like);
		pp_commit = (ImageView) popView
				.findViewById(R.id.pp_artlist_img_commit);
		pp_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.Show2ArtListAdapter_pp_like));
				ArthInfo arth = (ArthInfo) v.getTag();
				if (arth.getLikeflag() == 0) {
					GetArthInfo(arth);
				} else {
					ToastUtil.showMessage(mContext, R.string.praised);
				}
			}
		});
		pp_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext, mContext.getResources()
						.getString(R.string.Show2ArtListAdapter_pp_commit));
				popupWindow.dismiss();
				pp_comment.setTag(v.getTag());
				showPopComment(null, 0, BaseActivity.sp.getInt(
						"KeyboardHeight", Constant.ScreenHeight / 2));
			}
		});
	}

	/**
	 * 初始化pp_commit（评论）
	 * */
	private void initPopWindowComment() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View popView = inflater.inflate(R.layout.popup_artlistcomment, null);
		popupWindowComment = new PopupWindow(popView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pp_comment = (ImageView) popView
				.findViewById(R.id.item_show2_iv_commit);
		pp_input = (IEditText) popView.findViewById(R.id.item_show2_edt_commit);
		pp_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(
						mContext,
						mContext.getResources().getString(
								R.string.Show2ArtListAdapter_pp_commit_commit));
				ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
				final ArthInfo arthInfo = (ArthInfo) tag.get(0);
				final CusListView listView = (CusListView) tag.get(1);
				final String str_comm = pp_input.getTextString();
				if (TextUtils.isEmpty(str_comm)) {
					ToastUtil.showMessage(mContext,
							R.string.please_input_content);
				} else {
					DialogUtil.getInstance().getDialog(
							mContext,
							mContext.getResources().getString(
									R.string.public_later));
					DialogUtil.getInstance().setCanCancel(false);
					RequestParams params = new RequestParams();
					params.addBodyParameter("aid", arthInfo.getTabIDStr());
					params.addBodyParameter("comment", str_comm);
					params.addBodyParameter("refid", "");
					HttpUtil.InstanceSend(addComment, params,
							new RequestCallBack<String>() {

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									try {
										JSONObject jsonObj = new JSONObject(
												arg0.result);
										String ResultCode = jsonObj
												.getString("ResultCode");
										if ("0".equals(ResultCode)) {
											CommitAdapter adapter = (CommitAdapter) listView
													.getAdapter();
											Comment comment = new Comment();
											comment.setUserName(BaseActivity.sp
													.getString("UserName",
															"新用户"));
											comment.setCommnets(str_comm);
											adapter.addData(comment);
											adapter.notifyDataSetChanged();
											pp_input.setText("");
											DialogUtil.getInstance()
													.cannleDialog();
											ArrayList<Object> post = new ArrayList<Object>();
											post.add(Constant.msgcenter_articlelist_addComment);
											EventBusUtil.post(post);
											// BaseUtils.hidepan(mContext);
											popupWindowComment.dismiss();
										} else {
											popupWindowComment.dismiss();
											DialogUtil.getInstance()
													.cannleDialog();
										}
									} catch (Exception e) {
										popupWindowComment.dismiss();
										DialogUtil.getInstance().cannleDialog();
									}

								}

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									popupWindowComment.dismiss();
									DialogUtil.getInstance().cannleDialog();
								}
							});
				}
			}
		});

	}

	/**
	 * 显示popWindow
	 * 
	 * @param y2
	 * */
	public void showPop(View parent, int with, int x, int y) {
		// 设置popwindow显示位置
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.update();
		popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY,
				Constant.ScreenWith / 2 - with, y);
	}

	/**
	 * 显示popWindow
	 * */
	public void showPopComment(View parent, int x, int y) {
		pp_input.setFocusable(true);
		pp_input.setFocusableInTouchMode(true);
		pp_input.requestFocus();
		pp_input.requestFocusFromTouch();
		BaseUtils.openpan(pp_input);
		popupWindowComment.setFocusable(true);
		popupWindowComment.setOutsideTouchable(true);
		popupWindowComment.setBackgroundDrawable(new PaintDrawable());
		popupWindowComment.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindowComment
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindowComment.update();
		popupWindowComment.showAtLocation(parentView, Gravity.NO_GRAVITY, 0,
				Constant.ScreenHeight);
	}

	/**
	 * cell界面
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
				parent, R.layout.item_listview_show2, position);
		RelativeLayout typeLayout = viewHolder.getView(R.id.item_show2_type);
		LinearLayout contentLayout = viewHolder
				.getView(R.id.item_show2_layout_content);
		TextView typeName = viewHolder.getView(R.id.item_show2_typename);
		ImageView openMore = viewHolder.getView(R.id.item_show2_more);

		TextView author = viewHolder.getView(R.id.item_show2_author);
		TextView unit = viewHolder.getView(R.id.item_show2_unit);
		TextView title = viewHolder.getView(R.id.item_show2_title);
		TextView content = viewHolder.getView(R.id.item_show2_content);
		TextView time = viewHolder.getView(R.id.item_show2_time);
		TextView like = viewHolder.getView(R.id.item_show2_like);
		// TextView comment = viewHolder.getView(R.id.item_show2_comment);
		final TextView showpp = viewHolder.getView(R.id.item_show2_showpp);
		TextView click = viewHolder.getView(R.id.item_show2_click);
		CusGridView gridView = viewHolder.getView(R.id.item_show2_gridview);
		final CusListView listView = viewHolder
				.getView(R.id.item_show2_listview);
		ImageView photo = viewHolder.getView(R.id.item_show2_photo);
		typeLayout.setVisibility(View.GONE);
		boolean isShow = false;
		try {
			final ArthInfo arthInfo = (ArthInfo) getItem(position);

			if (arthInfo == null) {
				listView.setVisibility(View.GONE);
				contentLayout.setVisibility(View.GONE);
				if (clickType == ShowClickType.curunit) {
					// 当前单位
					if (position == 0) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.unit_news));
						openMore.setVisibility(View.VISIBLE);
						openMore.setOnClickListener(null);
					} else if (position == 1) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.activity_share));
						openMore.setVisibility(View.GONE);
					} else {
						typeLayout.setVisibility(View.GONE);
					}
				} else if (clickType == ShowClickType.curclass) {
					// 当前班级
					if (position == 0) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext
								.getString(R.string.class_news));
						openMore.setVisibility(View.VISIBLE);
						openMore.setOnClickListener(null);
					} else if (position == 1) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.activity_share));
						openMore.setVisibility(View.GONE);
					} else {
						typeLayout.setVisibility(View.GONE);
					}
				} else {
					typeLayout.setVisibility(View.GONE);
				}
			} else {
				listView.setVisibility(View.VISIBLE);
				showpp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent
								.onEvent(
										mContext,
										mContext.getResources()
												.getString(
														R.string.Show2ArtListAdapter_showpp));
						int[] location = new int[2];
						showpp.getLocationInWindow(location);
						int x = location[0];
						int y = location[1];
						showPop(viewHolder.getConvertView(), showpp.getWidth(),
								x, y);
						pp_like.setTag(arthInfo);
						ArrayList<Object> tag = new ArrayList<Object>();
						tag.add(arthInfo);
						tag.add(listView);
						pp_commit.setTag(tag);
					}
				});
				/**
				 * ListView Item的点击事件
				 */
				listView.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						MobclickAgent.onEvent(
								mContext,
								mContext.getResources().getString(
										R.string.Show2ArtListAdapter_listView));
						CommitAdapter adapter = (CommitAdapter) listView
								.getAdapter();
						if (adapter.getCount() > 5) {
							// 跳转到文章详情界面
							Intent intent = new Intent(mContext,
									ArticleDetailsActivity.class);
							ShowFragment2.commitAdapter = (CommitAdapter) listView
									.getAdapter();
							intent.putExtra("arthInfo", arthInfo);
							ShowFragment2.clickArthInfo = arthInfo;
							mContext.startActivity(intent);
						}
					}
				});
				isShow = false;
				unit.setTextColor(mContext.getResources().getColor(
						R.color.color_b2b2b2));
				unit.setBackgroundColor(mContext.getResources().getColor(
						R.color.white));
				contentLayout.setVisibility(View.VISIBLE);
				title.setText(arthInfo.getTitle());
				String abs = arthInfo.getAbstracts();
				if (TextUtils.isEmpty(abs)) {
					content.setVisibility(View.GONE);
				} else {
					content.setVisibility(View.VISIBLE);
					content.setText(abs);
				}
				String[] str_times = arthInfo.getRecDate().split(" ");
				if (str_times[0].equals(str_todaytimes[0])) {
					time.setText(str_times[1]);
				} else {
					time.setText(str_times[0]);
				}
				like.setText(String.valueOf(arthInfo.getLikeCount()));
				// comment.setText(String.valueOf(arthInfo.getFeeBackCount()));
				click.setText(String.valueOf(arthInfo.getClickCount()));
				// 图片链接
				String thumbnail = arthInfo.getThumbnail();
				// 非空
				if (!TextUtils.isEmpty(thumbnail)) {
					gridView.setVisibility(View.VISIBLE);
					JSONArray jsonarray = new JSONArray(thumbnail);
					boolean had = false;
					for (PictureAdapter adapter : picAdapterList) {
						if (adapter.getJSONArray().equals(jsonarray)) {
							gridView.setAdapter(adapter);
							had = true;
							break;
						}
					}
					if (!had) {
						PictureAdapter adapter = new PictureAdapter(mContext,
								jsonarray);
						gridView.setAdapter(adapter);
						picAdapterList.add(adapter);
					}
				} else {
					gridView.setVisibility(View.GONE);
				}
				String SectionID = arthInfo.getSectionID();// 1:展示；2：分享
				int UnitType = arthInfo.getUnitType();
				int UnitID = arthInfo.getUnitId();
				if (UnitType == 3) {
					UnitID = arthInfo.getUnitClassID() * -1;
				}
				String unittext = "";
				if (!TextUtils.isEmpty(SectionID)) {
					String[] sectionIDs = SectionID.split("\\_");
					if (sectionIDs.length > 1) {
						if (sectionIDs[1].equals("1")) {
							String url = mainUrl + ConstantUrl.getUnitlogo
									+ "?UnitID=" + UnitID;
							bitmap.display(photo, url);
							author.setText(arthInfo.getUnitName());
							unit.setTextColor(mContext.getResources().getColor(
									R.color.white));
							unit.setBackgroundColor(mContext.getResources()
									.getColor(R.color.color_e68282));
							unittext = mContext.getResources().getString(
									R.string.news);
							isShow = true;
							photo.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									MobclickAgent
											.onEvent(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.ShowFragment2_listPhoto));
									Intent intent = new Intent(mContext,
											UnitSpaceActivity.class);
									if (arthInfo.getUnitType() == 3) {
										intent.putExtra("UnitID",
												arthInfo.getUnitClassID());
									} else {
										intent.putExtra("UnitID",
												arthInfo.getUnitId());
									}
									intent.putExtra("UnitType",
											arthInfo.getUnitType());
									intent.putExtra("IsMyUnit", 2);
									intent.putExtra("UnitName",
											arthInfo.getUnitName());
									mContext.startActivity(intent);
								}
							});
						} else if (sectionIDs[1].equals("2")) {
							String url = mainUrl + ConstantUrl.photoURL
									+ "?AccID=" + arthInfo.getJiaoBaoHao();
							bitmap.display(photo, url);
							author.setText(arthInfo.getUserName());
							unittext = arthInfo.getUnitName();
							photo.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									MobclickAgent
											.onEvent(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.ShowFragment2_listPhoto));
									Intent intent = new Intent(mContext,
											PersonalSpaceActivity.class);
									intent.putExtra("JiaoBaoHao",
											arthInfo.getJiaoBaoHao() + "");
									intent.putExtra("UserName",
											arthInfo.getUserName());
									mContext.startActivity(intent);
								}
							});
						}
					}
				} else {
					String url = mainUrl + ConstantUrl.photoURL + "?AccID="
							+ arthInfo.getJiaoBaoHao();
					bitmap.display(photo, url);
					author.setText(arthInfo.getUserName());
					unittext = arthInfo.getUnitName();
					photo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							MobclickAgent.onEvent(
									mContext,
									mContext.getResources().getString(
											R.string.ShowFragment2_listPhoto));
							Intent intent = new Intent(mContext,
									PersonalSpaceActivity.class);
							intent.putExtra("JiaoBaoHao",
									arthInfo.getJiaoBaoHao() + "");
							intent.putExtra("UserName", arthInfo.getUserName());
							mContext.startActivity(intent);
						}
					});
				}
				if (clickType == ShowClickType.curunit) {
					boolean b = false;
					for (CommitAdapter adapter : commitListUnit) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListUnit.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
					if (position == 0) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.unit_news));
						openMore.setVisibility(View.VISIBLE);
						author.setText(arthInfo.getUnitName());
						// unittext = "";
						openMore.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								Intent intent = new Intent(mContext,
										Show2MoreArticleListActivity.class);
								intent.putExtra("moreType", "unit");
								mContext.startActivity(intent);
							}
						});
					} else if (position == 1) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.activity_share));
						openMore.setVisibility(View.GONE);
					} else {
						typeLayout.setVisibility(View.GONE);
					}
				} else if (clickType == ShowClickType.curclass) {
					boolean b = false;
					for (CommitAdapter adapter : commitListClass) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListClass.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}

					if (position == 0) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.class_news));
						openMore.setVisibility(View.VISIBLE);
						author.setText(arthInfo.getUnitName());
						// unittext = "";
						openMore.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								MobclickAgent
										.onEvent(
												mContext,
												mContext.getResources()
														.getString(
																R.string.ShowFragment2_unitMore));
								Intent intent = new Intent(mContext,
										Show2MoreArticleListActivity.class);
								intent.putExtra("moreType", "class");
								mContext.startActivity(intent);
							}
						});
					} else if (position == 1) {
						typeLayout.setVisibility(View.VISIBLE);
						typeName.setText(mContext.getResources().getString(
								R.string.activity_share));
						openMore.setVisibility(View.GONE);
					} else {
						typeLayout.setVisibility(View.GONE);
					}
				} else if (clickType == ShowClickType.classmore) {
					// unit.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
					author.setText(arthInfo.getUserName());
					unittext = arthInfo.getUnitName();
					boolean b = false;
					for (CommitAdapter adapter : commitListUnit) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListUnit.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
				} else if (clickType == ShowClickType.unitmore) {
					// unit.setTextColor(mContext.getResources().getColor(R.color.color_b2b2b2));
					author.setText(arthInfo.getUserName());
					unittext = arthInfo.getUnitName();
					boolean b = false;
					for (CommitAdapter adapter : commitListUnit) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListUnit.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
				} else if (clickType == ShowClickType.curlocal) {
					boolean b = false;
					for (CommitAdapter adapter : commitListLocation) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListLocation.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
				} else if (clickType == ShowClickType.att) {
					boolean b = false;
					for (CommitAdapter adapter : commitListAtt) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListAtt.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
				} else if (clickType == ShowClickType.all) {
					boolean b = false;
					for (CommitAdapter adapter : commitListAll) {
						if (adapter.getUserTag().equals(arthInfo.getTabIDStr())) {
							b = true;
							listView.setAdapter(adapter);
							if (adapter.getCount() <= 0) {
								listView.setVisibility(View.GONE);
							} else {
								listView.setVisibility(View.VISIBLE);
							}
							break;
						}
					}
					if (!b) {
						CommitAdapter adapter = new CommitAdapter(mContext);
						listView.setAdapter(adapter);
						commitListAll.add(adapter);
						adapter.setUserTag(arthInfo.getTabIDStr());
						RequestParams params = new RequestParams();
						params.addBodyParameter("aid", arthInfo.getTabIDStr());
						params.addBodyParameter("pageNum", "1");
						params.addBodyParameter("numPerPage", "5");
						CallBack callback = new CallBack();
						callback.setUserTag(adapter);
						callback.setListView(listView);
						HttpUtil.InstanceSend(CommentsList, params, callback);
					}
				} else {
					typeLayout.setVisibility(View.GONE);
				}
				title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(mContext, mContext.getResources()
								.getString(R.string.ShowFragment2_listTitle));
						arthInfo.setClickCount(arthInfo.getClickCount() + 1);
						Intent intent = new Intent(mContext,
								ArticleDetailsActivity.class);
						ShowFragment2.commitAdapter = (CommitAdapter) listView
								.getAdapter();
						ShowFragment2.clickArthInfo = arthInfo;
						intent.putExtra("arthInfo", arthInfo);
						mContext.startActivity(intent);
					}
				});
				content.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						MobclickAgent.onEvent(mContext, mContext.getResources()
								.getString(R.string.ShowFragment2_listContent));
						arthInfo.setClickCount(arthInfo.getClickCount() + 1);
						Intent intent = new Intent(mContext,
								ArticleDetailsActivity.class);
						ShowFragment2.commitAdapter = (CommitAdapter) listView
								.getAdapter();
						intent.putExtra("arthInfo", arthInfo);
						ShowFragment2.clickArthInfo = arthInfo;
						mContext.startActivity(intent);
					}
				});
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// MobclickAgent.onEvent(mContext,
								// mContext.getResources().getString(R.string.));
							}
						});
				if (TextUtils.isEmpty(unittext)) {
					unit.setText("");
				} else {
					String[] unittexts = unittext.split("\\(");
					if (isShow) {
						unit.setText(" " + unittexts[0] + " ");
					} else {
						unit.setText("[" + unittexts[0] + "]");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewHolder.getConvertView();
	}

	public class CommitAdapter extends BaseAdapter {

		private Context mContext;
		private String userTag;
		private ArrayList<Comment> commentsList = new ArrayList<Comment>();

		public CommitAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void setData(ArrayList<Comment> commentsList2) {
			commentsList.clear();
			commentsList.addAll(commentsList2);
			addMore();
			notifyDataSetChanged();
		}

		public void addData(Comment comment) {
			commentsList.add(0, comment);
			addMore();
			notifyDataSetChanged();
		}

		/**
		 * 当commentsList的长度>=5时； 截取 List 把长度控制到6
		 */
		private void addMore() {
			if (commentsList.size() >= 5) {
				commentsList = new ArrayList<Comment>(
						commentsList.subList(0, 5));
				Comment more = new Comment();
				more.setUserName("更多>>>");
				more.setCommnets("");
				commentsList.add(more);
			}
		}

		public String getUserTag() {
			return userTag;
		}

		public void setUserTag(String userTag) {
			this.userTag = userTag;
		}

		@Override
		public int getCount() {
			return commentsList == null ? 0 : commentsList.size();
		}

		@Override
		public Object getItem(int position) {
			return commentsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = ViewHolder.get(mContext, convertView,
					parent, R.layout.simple_commit_textview, position);
			TextView item = viewHolder.getView(R.id.simple_commit_textview);
			Comment comment = (Comment) getItem(position);
			String comments = comment.getCommnets();
			String username = comment.getUserName();
			if (TextUtils.isEmpty(username)) {
				username = comment.getJiaoBaoHao() + "";
			}
			// String comments = EmojiUtil.filterEmoji(comment.getCommnets());
			if (getCount() == 6) {
				if (position == getCount() - 1) {
					item.setText(Html.fromHtml("<font color=#5a6992>"
							+ username + "</font>"));
				} else {
					item.setText(Html.fromHtml("<font color=#5a6992>"
							+ username + "</font>" + ":" + comments));
				}
			} else {
				item.setText(Html.fromHtml("<font color=#5a6992>" + username
						+ "</font>" + ":" + comments));

			}
			//
			return viewHolder.getConvertView();
		}

		/**
		 * ImageGetter用于text图文混排
		 * 
		 * @return
		 */
		public ImageGetter getImageGetterInstance() {
			ImageGetter imgGetter = new Html.ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					int id = Integer.parseInt(source);
					@SuppressWarnings("deprecation")
					Drawable d = mContext.getResources().getDrawable(id);
					d.setBounds(0, 0, d.getIntrinsicWidth(),
							d.getIntrinsicHeight());
					return d;
				}
			};
			return imgGetter;
		}
	}

	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	private class CallBack extends RequestCallBack<String> {
		private CusListView listView;

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
			}
		}

		public void setListView(CusListView listView) {
			this.listView = listView;
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");

					if ("0".equals(ResultCode)) {
						String data = Des.decrypt(jsonObj.getString("Data"),
								BaseActivity.sp_sys.getString("ClientKey", ""));
						CommentsList commentsList = GsonUtil.GsonToObject(data,
								CommentsList.class);
						CommitAdapter adapter = (CommitAdapter) this
								.getUserTag();
						ArrayList<Comment> a = commentsList.getCommentsList();
						if (a.size() > 0) {

							listView.setVisibility(View.VISIBLE);
							adapter.setData(a);
							adapter.notifyDataSetChanged();
						} else {
							listView.setVisibility(View.GONE);
						}

					}
				} catch (Exception e) {
				}
			}
		}

	}

	public void setParentView(View findViewById) {
		// TODO Auto-generated method stub
		this.parentView = findViewById;
	}
}
