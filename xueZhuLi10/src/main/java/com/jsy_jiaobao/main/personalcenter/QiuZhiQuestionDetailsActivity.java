package com.jsy_jiaobao.main.personalcenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.greenrobot.eventbus.Subscribe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.StringUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.WebSetUtils;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.UpFiles;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.UserInfo;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * 问题详情界面
 * 
 * @author admin
 * 
 */
public class QiuZhiQuestionDetailsActivity extends BaseActivity implements
		OnClickListener, Watcher {
	private final static String TAG = "QiuZhiQuestionDetailsActivity";
	private QuestionIndexItem question;// 问题数据
	private int myAnswerID;
	private Context mContext;
	private ScrollView layout;// scrollView
	private TextView tv_question;// 显示问题标题的View
	private TextView tv_topic;// 显示问题所属话题的View
	private TextView tv_answernum;// 显示问题回答数的View
	private TextView tv_attnum;// 显示问题的关注数
	private TextView tv_clicknum;// 显示问题的点击数
	private LinearLayout layout_wv;
	private WebView web_describe;// 显示问题详情的webView
	private TextView tv_awn;
	private TextView tv_ann;
	private TextView tv_takephoto;// 插入图片
	private IEditText edt_answer;// 回答标题输入框
	private IEditText edt_describe;// 回答详情输入框
	private CheckBox checkBox;// 选择框
	private Uri photoUri;
	private boolean isRevamp = false;// 是否匿名
	private ArrayList<Bitmap> bitList;
	private ArrayList<Drawable> drawList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			photoUri = savedInstanceState.getParcelable("photoUri");
			question = (QuestionIndexItem) savedInstanceState
					.getSerializable("QuestionIndexItem");
		} else {
			initPassData();
		}
		initViews();
	}

	/**
	 * @method self defined
	 * @功能 获取Intent携带过来的值
	 */
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				question = (QuestionIndexItem) bundle
						.getSerializable("QuestionIndexItem");
			}
		}
	}

	/**
	 * 覆写方法
	 * 
	 * @功能 保存因意外销毁的数据
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("photoUri", photoUri);
		outState.putSerializable("QuestionIndexItem", question);
	}

	/**
	 * @method 自定义方法
	 * @功能 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_qiuzhi_question_details);
		mContext = this;
		QiuZhiQuestionDetailsActivityController.getInstance().setContext(this);
		WatchedEntityIndexItem.getInstance().addWatcher(this);
		layout = (ScrollView) findViewById(R.id.qiuzhi_question_layout);
		tv_question = (TextView) findViewById(R.id.qiuzhi_question_tv_question);
		tv_topic = (TextView) findViewById(R.id.qiuzhi_question_tv_topic);
		tv_answernum = (TextView) findViewById(R.id.qiuzhi_question_tv_answernum);
		tv_attnum = (TextView) findViewById(R.id.qiuzhi_question_tv_attnum);
		tv_clicknum = (TextView) findViewById(R.id.qiuzhi_question_tv_clicknum);
		layout_wv = (LinearLayout) findViewById(R.id.qiuzhi_question_layout_wv);
		edt_answer = (IEditText) findViewById(R.id.qiuzhi_question_edt_answer);
		edt_describe = (IEditText) findViewById(R.id.qiuzhi_question_edt_describe);
		tv_awn = (TextView) findViewById(R.id.qiuzhi_question_tv_answithname);
		tv_ann = (TextView) findViewById(R.id.qiuzhi_question_tv_ansnoname);
		tv_takephoto = (TextView) findViewById(R.id.qiuzhi_question_tv_photo);
		checkBox = (CheckBox) findViewById(R.id.qiuzhi_question_cb);
		// 各种View注册点击事件的监听
		tv_takephoto.setOnClickListener(this);
		tv_awn.setOnClickListener(this);
		tv_ann.setOnClickListener(this);
		tv_question.requestFocus();
		/**
		 * 设置监听，当触摸。。edt_content时，父ViewGroup禁止滚动
		 */
		edt_describe.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.qiuzhi_question_edt_describe) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});
		// 答案内容 不许有回车 以空格键代替
		edt_answer.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					edt_answer.append(" ");
					return true;
				}
				return false;
			}
		});
		// 问题详情的webView
		web_describe = new WebView(mContext);

		// webView的各项设置
		WebSetUtils.getWebSetting(this, web_describe);

		if (null != question) {
			// 问题不为空 ，请求问题详情
			QiuZhiQuestionDetailsActivityController.getInstance()
					.QuestionDetail(question.getTabID());
			// 设置界面显示
			setActionBarTitle(question.getTitle());
			tv_question.setText(question.getTitle());
			tv_topic.setText(question.getCategorySuject());
		} else {
			ToastUtil.showMessage(mContext, R.string.data_error);
			finish();
		}

	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onResume() {
		super.onResume();
		EventBusUtil.register(this);
		MobclickAgent.onResume(this);
	}

	/**
	 * 生命周期事件
	 */
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		MobclickAgent.onPause(this);
		super.onPause();
	}

	private QuestionDetails questiondetails;
	AnswerDetails answer;

	/**
	 * EventBus功能模块
	 * 
	 * @功能 获取网络请求数据 并处理
	 */

	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_qiuzhi_AnswerDetail:
			// 答案详情处理
			answer = (AnswerDetails) list.get(1);
			if (answer != null) {
				String str = answer.getAContent();
				str = str.replaceAll("</p><p>", "\\\n").replaceAll("<p>", "")
						.replaceAll("</p>", "").replaceAll("'/><br>", "'/>")
						.replaceAll("'/></br>", "'/>")
						.replaceAll("'/><br/>", "'/>")
						.replaceAll("<br><img src", "<img src")
						.replaceAll("</br><img src", "<img src")
						.replaceAll("<br/><img src", "<img src")
						.replaceAll("<br>", "\\\n").replaceAll("</br>", "\\\n")
						.replaceAll("<br/>", "\\\n").replaceAll("&nbsp;", " ")
						.replaceAll("<[^img].*?>", "");

				edt_answer.setText(answer.getATitle());
				edt_describe.setText(str);
				isRevamp = true;
				tv_awn.setText(getResources().getString(R.string.change_answer));
				tv_ann.setText(getResources().getString(
						R.string.anonymity_change));
				// 获取text中的Image
				ArrayList<HashMap<String, String>> a = getImgs(str);
				if (a.size() > 0) {
					for (int i = 0; i < a.size(); i++) {
						HashMap<String, String> map = a.get(i);
						String src = map.get("src");
						final String img = map.get("img");
						String[] b = src.split("\\/");
						String names = b[b.length - 1];
						names = names
								.replaceAll(
										"[`~!@#$%^&*()+=|{}':;'\",\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]",
										"");
						if (!names.endsWith(".jpg") && !names.endsWith(".png")
								&& !names.endsWith(".jpeg")) {
							names = names + ".jpg";
						}
						if (src.startsWith("\"")) {
							src = src.replaceFirst("\"", "");
						}
						if (src.endsWith("/")) {
							src = src.substring(0, src.length() - 1);
						}
						if (src.endsWith("\"")) {
							src = src.substring(0, src.length() - 1);
						}
						String path = JSYApplication.getInstance().FILE_PATH
								+ names;
						HttpUtil.getInstanceNew().download(src, path, true,
								new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										String txt = edt_describe
												.getTextString();
										Editable eb = edt_describe
												.getEditableText();
										// 获得光标所在位置
										int qqPosition = txt.indexOf(img);
										SpannableString ss = new SpannableString(
												img);
										// 定义插入图片
										try {
											Drawable drawable = QiuZhiSuggestShowRecommentActivity.cache
													.get(arg0.result.getPath());
											if (drawable == null) {
												Bitmap bitmap01 = PictureUtils
														.getbitmapFromFile(arg0.result);
												if (bitmap01.getWidth() > 180) {
													bitmap01 = ResizeBitmap(
															bitmap01, 180);
												}

												drawable = new BitmapDrawable(
														getResources(),
														bitmap01);
												QiuZhiSuggestShowRecommentActivity.cache
														.put(arg0.result
																.getPath(),
																drawable);
											}

											ss.setSpan(
													new ImageSpan(
															drawable,
															ImageSpan.ALIGN_BASELINE),
													0,
													ss.length(),
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											drawable.setBounds(
													0,
													0,
													drawable.getIntrinsicWidth(),
													drawable.getIntrinsicHeight());
										} catch (OutOfMemoryError e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// 插入图片
										eb.replace(qqPosition,
												qqPosition + ss.length(), ss);

									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										String txt = edt_describe
												.getTextString();
										Editable eb = edt_describe
												.getEditableText();
										// 获得光标所在位置
										int qqPosition = txt.indexOf(img);
										SpannableString ss = new SpannableString(
												img);
										// 定义插入图片
										try {
											Drawable drawable;
											drawable = getResources()
													.getDrawable(
															R.drawable.pic_no);

											ss.setSpan(
													new ImageSpan(
															drawable,
															ImageSpan.ALIGN_BASELINE),
													0,
													ss.length(),
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
											drawable.setBounds(0, 0, 30, 30);
										} catch (OutOfMemoryError e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// 插入图片
										// eb.insert(qqPosition, ss);
										eb.replace(qqPosition,
												qqPosition + ss.length(), ss);
									}
								});
					}
				}
			}
			break;
		case Constant.msgcenter_qiuzhi_QuestionDetail:
			// 问题详情处理
			questiondetails = (QuestionDetails) list.get(1);
			if (questiondetails == null) {
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ToastUtil.showMessage(mContext, R.string.this_question_deleted);
				layout.setVisibility(8);
			}
			myAnswerID = questiondetails.getMyAnswerId();
			QiuZhiQuestionDetailsActivityController.getInstance().AnswerDetail(
					myAnswerID);
			tv_question.setText(questiondetails.getTitle());
			tv_attnum.setText(String.valueOf(questiondetails.getAttCount()));
			tv_answernum.setText(String.valueOf(questiondetails
					.getAnswersCount()));
			int clickNum = questiondetails.getViewCount() + 1;
			tv_clicknum.setText(String.valueOf(clickNum));
			web_describe.loadUrl(questiondetails.getKnContent());
			layout_wv.removeAllViews();
			layout_wv.addView(web_describe);
			question.setViewCount(clickNum);
			question.setAnswersCount(questiondetails.getAnswersCount());
			WatchedEntityIndexItem.getInstance().notifyWatcher(question);
			if (questiondetails.getQFlag() == 1) {
				edt_describe.setHint(getResources().getString(
						R.string.answer_contentCanChoose)
						+ getResources().getString(
								R.string.answer_questionWithGist));
			} else if (questiondetails.getQFlag() == 0) {
				edt_describe.setHint(getResources().getString(
						R.string.answer_contentCanChoose));
			}

			break;
		case Constant.msgcenter_qiuzhi_AddAnswer:
			// 回答问题返回数据的处理
			String result = (String) list.get(1);
			if (!TextUtils.isEmpty(result)) {
				questiondetails.setAnswersCount(questiondetails
						.getAnswersCount() + 1);
				question.setAnswersCount(questiondetails.getAnswersCount());
				WatchedEntityIndexItem.getInstance().notifyWatcher(question);
				myAnswerID = Integer.parseInt(result);
				isRevamp = true;
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ACache.get(getApplicationContext(), "qiuzhi").put("isOldList",
						"false");
				ACache.get(getApplicationContext(), "qiuzhi").put(
						"isNewQuestion", "true");
			}
			finish();
			break;
		case Constant.msgcenter_qiuzhi_UpdateAnswer:
			// 更新问题答案结果
			String result1 = (String) list.get(1);
			if (!TextUtils.isEmpty(result1)) {
				ACache.get(getApplicationContext(), "qiuzhi").put("isOld",
						"false");
				ACache.get(getApplicationContext(), "qiuzhi").put("isOldList",
						"false");
			}
			finish();
			break;
		case Constant.msgcenter_qiuzhi_uploadSectionImg:
			DialogUtil.getInstance().cannleDialog();
			UpFiles upFile = (UpFiles) list.get(1);
			if (bitmap.getWidth() > 120) {
				bitmap = ResizeBitmap(bitmap, 120);
			}
			String img = "<img src='" + upFile.getUrl() + "'/><br>";
			int length = img.length() + edt_describe.getTextString().length();
			if (length > 20000) {
				ToastUtil.showMessage(mContext, "字数超出限制,请删除" + (length - 20000)
						+ "字");
			} else {
				BaseUtils.insetImage(mContext, edt_describe, img, bitmap);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @功能 判断图片数量是否小于等于20张
	 */
	private boolean CheckPicEnough() {
		String xml = edt_describe.getText().toString();
		int size = StringUtils.getImgStr(xml).size();
		return size <= 20 ? true : false;
	}

	/**
	 * @功能 各种控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		UserInfo userInfo = (UserInfo) ACache.get(getApplicationContext())
				.getAsObject("userInfo");
		switch (v.getId()) {
		case R.id.qiuzhi_question_tv_photo:
			// 获取图片
			MobclickAgent.onEvent(
					mContext,
					mContext.getResources().getString(
							R.string.QuestionDetail_takePicture));
			if (userInfo.isIsKnlFeezeUser()) {
				// 已封号
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_user));
				return;
			}
			if (userInfo.getDUnitId() == 0) {
				// 无单位
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nounit));
				return;
			}
			if (userInfo.getNickName() == null
					|| userInfo.getNickName().equals("")) {
				// 无昵称
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nonick));
				return;
			}
			// 判断图片是否>20张
			if (!CheckPicEnough()) {
				ToastUtil.showMessage(mContext, R.string.noMoreThan_20ToUpLoad);
				return;
			}
			if (edt_describe.getTextString().length() >= 20000) {
				// 是否大于2W
				ToastUtil.showMessage(mContext, "已达两万字上限,请删除多余内容后上传");
				return;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(android.R.drawable.ic_menu_gallery);
			builder.setTitle(getResources().getString(R.string.choose_source));
			// 相机
			builder.setPositiveButton(R.string.camera,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							MobclickAgent.onEvent(
									mContext,
									mContext.getResources().getString(
											R.string.QuestionDetail_camera));
							try {
								photoPath = JSYApplication.getInstance().FILE_PATH;
								File photoFile = PictureUtils
										.createImageFile(photoPath);
								if (photoFile != null) {
									photoPath = photoFile.getAbsolutePath();
									PictureUtils.dispatchTakePictureIntent(
											QiuZhiQuestionDetailsActivity.this,
											photoFile, 1);
								}

							} catch (Exception e) {
								ToastUtil.showMessage(mContext,
										R.string.open_camera_abnormal);
							}
						}
					});
			// 相册
			builder.setNegativeButton(R.string.album,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							MobclickAgent.onEvent(
									mContext,
									mContext.getResources().getString(
											R.string.QuestionDetail_album));
							Intent ii = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
							startActivityForResult(ii, 2);
						}
					});
			builder.create().show();
			break;

		case R.id.qiuzhi_question_tv_answithname:
			// 回答问题
			MobclickAgent.onEvent(
					mContext,
					mContext.getResources().getString(
							R.string.QuestionDetail_answerName));
			if (userInfo.isIsKnlFeezeUser()) {
				// 封号中
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_user));
				return;
			}
			if (userInfo.getDUnitId() == 0) {
				// 无单位
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nounit));
				return;
			}
			if (userInfo.getNickName() == null
					|| userInfo.getNickName().equals("")) {
				// 无昵称
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nonick));
				return;
			}

			if (!CheckPicEnough()) {
				// 图片大于20张
				ToastUtil.showMessage(mContext, R.string.noMoreThan_20ToUpLoad);
				return;
			}
			String str_answer = edt_answer.getTextString();
			String str_describe = BaseUtils.startendNospacing(edt_describe
					.getEditableText().toString());
			String contentString = BaseUtils.startendNospacing(str_describe
					.replaceAll("<img[^>]*/>", "").replaceAll("<br>", "")
					.replaceAll("</br>", ""));
			if (TextUtils.isEmpty(str_answer)) {
				ToastUtil.showMessage(mContext, R.string.answer_cannotEmpty);
			} else if (!TextUtils.isEmpty(str_describe)
					&& contentString.length() < 5) {
				ToastUtil.showMessage(mContext, R.string.content_atLeast_5word);
			} else {
				str_describe = str_describe.replaceAll("\\\n", "<br>")
						.replaceAll("'/><br>", "'/>")
						.replaceAll("'/>", "'/><br>")
						.replaceAll("<br><img src", "<img src")
						.replaceAll("<img src", "<br><img src")
						.replaceAll("<br><br>", "<br>");
				if (str_answer.length() > 100) {
					str_answer = str_answer.substring(0, 99);
				}
				/**
				 * 是否匿名
				 */
				if (isRevamp) {
					QiuZhiQuestionDetailsActivityController.getInstance()
							.UpdateAnswer(myAnswerID,
									checkBox.isChecked() ? 1 : 0, str_answer,
									str_describe, userInfo.getNickName());
				} else {
					QiuZhiQuestionDetailsActivityController.getInstance()
							.AddAnswer(question.getTabID(), str_answer,
									str_describe, checkBox.isChecked() ? 1 : 0,
									userInfo.getNickName());
				}
			}
			break;
		case R.id.qiuzhi_question_tv_ansnoname:
			// 匿名回答问题
			MobclickAgent.onEvent(
					mContext,
					mContext.getResources().getString(
							R.string.QuestionDetail_answerNoName));
			if (userInfo.isIsKnlFeezeUser()) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_user));
				return;
			}
			if (userInfo.getDUnitId() == 0) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nounit));
				return;
			}
			if (userInfo.getNickName() == null
					|| userInfo.getNickName().equals("")) {
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.public_error_nonick));
				return;
			}
			if (!CheckPicEnough()) {
				ToastUtil.showMessage(mContext, R.string.noMoreThan_20ToUpLoad);
				return;
			}
			String str_answer1 = edt_answer.getTextString();
			String str_describe1 = BaseUtils.startendNospacing(edt_describe
					.getEditableText().toString());
			String contentString1 = BaseUtils.startendNospacing(str_describe1
					.replaceAll("<img[^>]*/>", "").replaceAll("<br>", "")
					.replaceAll("</br>", ""));
			if (TextUtils.isEmpty(str_answer1)) {
				ToastUtil.showMessage(mContext, R.string.answer_cannotEmpty);
			} else if (!TextUtils.isEmpty(contentString1)
					&& contentString1.length() < 5) {
				ToastUtil.showMessage(mContext, R.string.content_atLeast_5word);
			} else {
				str_describe1 = str_describe1.replaceAll("\\\n", "<br>");
				str_describe1.replaceAll("'/><br>", "'/>");
				str_describe1.replaceAll("'/>", "'/><br>");
				str_describe1.replaceAll("<br><img src", "<img src");
				str_describe1.replaceAll("<img src", "<br><img src");
				str_describe1.replaceAll("<br><br>", "<br>");

				if (str_answer1.length() > 100) {
					str_answer1 = str_answer1.substring(0, 99);
				}
				// 是否匿名
				if (isRevamp) {
					QiuZhiQuestionDetailsActivityController.getInstance()
							.UpdateAnswer(myAnswerID,
									checkBox.isChecked() ? 1 : 0, str_answer1,
									str_describe1, "");
				} else {
					QiuZhiQuestionDetailsActivityController.getInstance()
							.AddAnswer(question.getTabID(), str_answer1,
									str_describe1,
									checkBox.isChecked() ? 1 : 0, "");
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @功能 获取String中 image列表
	 * 
	 * @param content
	 * @return
	 */
	private ArrayList<HashMap<String, String>> getImgs(String content) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String img = "";
		Pattern p_image;
		Matcher m_image;
		String regEx_img = "<img\\s[^>]+/>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(content);
		while (m_image.find()) {
			HashMap<String, String> map = new HashMap<String, String>();
			img = m_image.group();
			map.put("img", img);
			Matcher m = Pattern.compile("src\\s*=\\s*'?(.*?)('|>|\\s+)")
					.matcher(img);
			while (m.find()) {
				String tempSelected = m.group(1);
				map.put("src", tempSelected);
			}
			list.add(map);
		}
		return list;
	}

	String photoPath;
	Bitmap bitmap = null;
	private Thread thread;
	protected File file;

	/***
	 * 重写方法
	 * 
	 * @功能 获取上个功能模块中的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			try {
				DialogUtil.getInstance().getDialog(mContext, R.string.loading);
				// 开启线程 获取图片
				createThread();

			} catch (Exception e1) {
				bitmap = null;
				e1.printStackTrace();
			}
			break;
		case 2:
			try {
				if (data != null) {
					// 获取图片路径
					Uri uri1 = data.getData();
					Cursor cursor = this.getContentResolver().query(uri1, null,
							null, null, null);
					cursor.moveToFirst();
					photoPath = cursor.getString(1);
					DialogUtil.getInstance().getDialog(mContext,
							R.string.loading);
					// 开启线程 获取图片
					createThread();
				}
			} catch (Exception e) {
				bitmap = null;
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 创建线程
	 * 
	 * @param bitmap
	 * @param photoPath
	 */
	private void createThread() {
		if (thread == null) {
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					bitmap = PictureUtils.getbitmapFromURL(photoPath);
					if (bitmap == null) {
						// 图片为空 文件为空 删除图片
						file = null;
						DialogUtil.getInstance().cannleDialog();
						PictureUtils.DeleteImage(mContext, photoPath);
					} else {
						// 图片部位空 获取图片
						file = PictureUtils.saveBitmapFile(bitmap, photoPath);
					}
					handler.sendEmptyMessage(789);
				}
			});
		}
		thread.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 789:
				DialogUtil.getInstance().cannleDialog();
				thread.interrupt();
				thread = null;
				if (file != null) {
					// 上传图片文件
					uploadFile(file);
				}
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 上传图片文件
	 * 
	 * @param file
	 */
	private void uploadFile(File file) {
		DialogUtil.getInstance().getDialog(mContext, R.string.uploading);
		DialogUtil.getInstance().setCanCancel(false);
		RequestParams params = new RequestParams();
		params.addBodyParameter("file", file);
		QiuZhiQuestionDetailsActivityController.getInstance().uploadSectionImg(
				params);
	}

	/**
	 * @功能 压缩图片至指定宽度
	 * @param bitmap
	 * @param newWidth
	 * @return
	 */
	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * 系统返回按键
	 * 
	 * @功能 结束当前activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 生命周期事件
	 * 
	 * @功能 当要销毁时，释放内存
	 */
	@Override
	protected void onDestroy() {

		if (null != bitmap) {
			bitmap.recycle();
		}
		releaseMemory();
		super.onDestroy();
	}

	/**
	 * 释放图片内存
	 */

	private void releaseMemory() {
		// TODO Auto-generated method stub
		// QiuZhiSuggestShowRecommentActivity.cache.clear();
		if (bitList != null && bitList.size() > 0) {
			for (Bitmap bitmap : bitList) {
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}

			}
			System.gc();
			try { // 下面休息几分钟，让上面的垃圾回收线程运行完成
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (drawList != null && drawList.size() > 0) {
			for (Drawable drawable : drawList) {
				if (drawable != null) {
					drawable.setCallback(null);
					drawable = null;
				}
			}
		}
	}

	@Override
	public void update(QuestionIndexItem qEntity) {
		if (null != qEntity) {
			tv_answernum.setText(String.valueOf(qEntity.getAnswersCount()));
			tv_attnum.setText(String.valueOf(qEntity.getAttCount()));
			tv_clicknum.setText(String.valueOf((qEntity.getViewCount())));
		}
	}

	@Override
	public void update(AnswerItem answer) {
		// TODO Auto-generated method stub

	}
}
