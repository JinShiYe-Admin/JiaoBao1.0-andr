package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Coder;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.app.gallery.Gallery;
import com.jsy_jiaobao.po.personal.Photo;
import com.jsy_jiaobao.po.personal.UnitPGroup;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import android.graphics.Bitmap;

/**
 * 相册||照片列表Adapter
 * 
 * @author admin
 * 
 * @param <T>
 */
public class UnitSpacePhotoAdapter<T> extends BaseAdapter {

	private Context mContext;
	private List<T> mData;
	private String mainURL;
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private SparseArray<String> urlMap;

	public UnitSpacePhotoAdapter(Context mContext) {
		this.mContext = mContext;
		urlMap = new SparseArray<>();
		// 创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(mContext);
		if (mImageLoader == null) {
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(configuration);
		}
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.rc_image_download_failure)
				.showImageForEmptyUri(R.drawable.rc_image_download_failure)
				.showImageOnFail(R.drawable.rc_image_download_failure)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
				"MainUrl");
	}

	public void setData(List<T> mData) {
		this.mData = mData;
	}

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
	//View类型
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (mData.get(position) instanceof UnitPGroup) {
			return 0;
		} else if (mData.get(position) instanceof Photo) {
			return 1;
		} else if (mData.get(position) instanceof Gallery) {
			return 2;
		} else {
			return super.getItemViewType(position);
		}
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		switch (getItemViewType(position)) {
		case 0:
			try {
				final UnitPGroup arthInfo = (UnitPGroup) getItem(position);
				ViewHolder viewHolder = ViewHolder
						.get(mContext, convertView, parent,
								R.layout.unitspacephoto_gridview_item, position);
				final ImageView cover = viewHolder.getView(R.id.item_image);
				TextView name = viewHolder.getView(R.id.item_name);
				name.setText(arthInfo.getNameStr());

				String url = urlMap.get(position);
				if (url == null) {
					GalleryCallBack callback = new GalleryCallBack();
					callback.setUserTag(cover);
					callback.setPosition(position);
					HttpUtil.getInstanceNew().send(
							HttpRequest.HttpMethod.POST,
							mainURL + ConstantUrl.GetUnitFristPhotoByGroupID
									+ "?UnitID=" + arthInfo.getUnitID()
									+ "&GroupID=" + arthInfo.getTabID(),
							callback);
				} else {
					mImageLoader.displayImage(url, cover, options);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								MobclickAgent
										.onEvent(
												mContext,
												mContext.getResources()
														.getString(
																R.string.UnitSpacePhotoGroupActivity_albumCover));
								Intent intent = new Intent(mContext,
										UnitSpacePhotoActivity.class);
								Bundle args = new Bundle();
								args.putInt("TabID", arthInfo.getTabID());
								args.putInt("UnitID", arthInfo.getUnitID());
								args.putString("NameStr", arthInfo.getNameStr());
								args.putInt("CreateByjiaobaohao",
										arthInfo.getCreateByjiaobaohao());
								intent.putExtras(args);
								if (arthInfo.getViewType() == 0) {
									mContext.startActivity(intent);
								} else if (arthInfo.getViewType() == 1) {
									String UnitID = arthInfo.getUnitID() + "";
									boolean myunit = false;
									for (int i1 = 0; i1 < Constant.listUserIdentity
											.size(); i1++) {
										UserIdentity userIdentity = Constant.listUserIdentity
												.get(i1);
										for (int j = 0; j < userIdentity
												.getUserUnits().size(); j++) {
											UserUnit userUnit = userIdentity
													.getUserUnits().get(j);
											if (String.valueOf(
													userUnit.getUnitID())
													.equals(UnitID)) {
												myunit = true;
												break;
											}
										}
										for (int j = 0; j < userIdentity
												.getUserClasses().size(); j++) {
											UserClass userUnit = userIdentity
													.getUserClasses().get(j);
											if (String
													.valueOf(
															"-"
																	+ userUnit
																			.getClassID())
													.equals(UnitID)) {
												myunit = true;
												break;
											}
										}
									}
									if (myunit) {
										mContext.startActivity(intent);
									} else {
										ToastUtil.showMessage(mContext,
												R.string.unitMember_canSee);
									}
								}

							}
						});
				return viewHolder.getConvertView();
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		case 1:
			try {
				final Photo arthInfo = (Photo) getItem(position);
				ViewHolder viewHolder = ViewHolder
						.get(mContext, convertView, parent,
								R.layout.unitspacephoto_gridview_item, position);
				ImageView cover = viewHolder.getView(R.id.item_image);
				TextView name = viewHolder.getView(R.id.item_name);
				String[] urls = arthInfo.getSMPhotoPath().split("\\/");
				String[] names = urls[urls.length - 1].split("\\.");
				String formt = names[names.length - 1];
				String str = "";
				for (int i = 0; i < names.length - 1; i++) {
					str = str + names[i] + ".";
				}
				String url = "";// http://www.jb.edu8800.com/JBApp2/UploadPhotoOfUnit/20141125/5150059/20141125091852abe0_2013-03-03
								// 12.49.44.jpg
				for (int i = 0; i < urls.length - 1; i++) {
					url = url + urls[i] + "/";
				}
				String n = Coder.encodeURL(str).replace("+", "%20");
				url = url + "" + n + formt;
				mImageLoader.displayImage(url, cover, options);
				name.setVisibility(8);
				return viewHolder.getConvertView();
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		case 2:
			try {
				final Gallery arthInfo = (Gallery) getItem(position);
				ViewHolder viewHolder = ViewHolder
						.get(mContext, convertView, parent,
								R.layout.unitspacephoto_gridview_item, position);
				ImageView cover = viewHolder.getView(R.id.item_image);
				TextView name = viewHolder.getView(R.id.item_name);
				name.setText(arthInfo.getGroupName());

				String url = urlMap.get(position);
				if (url == null) {
					GalleryCallBack callback = new GalleryCallBack();
					callback.setUserTag(cover);
					callback.setPosition(position);
					HttpUtil.getInstanceNew().send(
							HttpRequest.HttpMethod.POST,
							mainURL + ConstantUrl.GetFristPhotoByGroup
									+ "?JiaoBaoHao=" + JiaoBaoHao
									+ "&GroupInfo=" + arthInfo.getID(),
							callback);
				} else {
					mImageLoader.displayImage(url, cover, options);
				}
				viewHolder.getConvertView().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext,
										UnitSpacePhotoActivity.class);
								Bundle args = new Bundle();
								args.putString("TabID", arthInfo.getID());
								args.putInt("UnitID", 0);
								args.putString("NameStr",
										arthInfo.getGroupName());
								args.putInt("CreateByjiaobaohao",
										Integer.parseInt(JiaoBaoHao));
								intent.putExtras(args);
								mContext.startActivity(intent);
							}
						});
				return viewHolder.getConvertView();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		default:
			return null;
		}
	}

	private String JiaoBaoHao;

	public void setJiaoBaoHao(String unitID) {
		this.JiaoBaoHao = unitID;

	}

	private class GalleryCallBack extends RequestCallBack<String> {
		private int mPosition;

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				System.out.println(arg1);
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
				ImageView cover = (ImageView) this.getUserTag();
				cover.setImageResource(R.drawable.rc_image_download_failure);
			}
		}

		public void setPosition(int position) {
			mPosition = position;
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			try {
				ImageView cover = (ImageView) this.getUserTag();

				JSONObject jsonObj = new JSONObject(arg0.result);
				String ResultCode = jsonObj.getString("ResultCode");
				if ("0".equals(ResultCode)) {
					ArrayList<Photo> getPhotoList = GsonUtil.GsonToList(
							jsonObj.getString("Data"),
							new TypeToken<ArrayList<Photo>>() {
							}.getType());
					if (null != getPhotoList && getPhotoList.size() > 0) {
						String[] urls = getPhotoList.get(0).getSMPhotoPath()
								.split("\\/");
						String[] names = urls[urls.length - 1].split("\\.");
						String formt = names[names.length - 1];
						String str1 = "";
						for (int i1 = 0; i1 < names.length - 1; i1++) {
							str1 = str1 + names[i1] + ".";
						}
						String url = "";// http://www.jb.edu8800.com/JBApp2/UploadPhotoOfUnit/20141125/5150059/20141125091852abe0_2013-03-03
										// 12.49.44.jpg
						for (int i1 = 0; i1 < urls.length - 1; i1++) {
							url = url + urls[i1] + "/";
						}
						String n = Coder.encodeURL(str1).replace("+", "%20");
						url = url + "" + n + formt;
						urlMap.put(mPosition, url);
						mImageLoader.displayImage(url, cover, options);
					} else {
						cover.setImageResource(R.drawable.rc_image_download_failure);
					}
				} else {
					cover.setImageResource(R.drawable.rc_image_download_failure);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}