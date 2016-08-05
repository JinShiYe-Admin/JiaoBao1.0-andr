package com.jsy_jiaobao.main.appcenter.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocation;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.Maps;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.MyLocationMapView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.VisitPublicHttp;
import com.jsy_jiaobao.po.sign.GetSignWay;
import com.jsy_jiaobao.po.sign.search.FenceUnitInfo;
import com.jsy_jiaobao.po.sign.search.GetFenceUnitInfo;
import com.jsy_jiaobao.po.sys.UserIdentity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 签到页面
 */
public class LocationActivity extends BaseActivity {

	// 定位图层
	MyLocationOverlay myLocationOverlay = null;

	// 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	@ViewInject(R.id.bmapView)
	MyLocationMapView mMapView; // 地图View
	private MapController mMapController = null;

	// UI相关
	@ViewInject(R.id.ui_location)
	RelativeLayout layout_ui;
	@ViewInject(R.id.location_btn_location)
	Button requestLocButton;
	@ViewInject(R.id.bottombar_btn_sign)
	Button btn_sign;
	@ViewInject(R.id.bottombar_btn_record)
	Button btn_search;
	@ViewInject(R.id.location_tv_wayofparent)
	TextView tv_wayofparent;
	@ViewInject(R.id.location_tv_wayofchild)
	TextView tv_wayofchild;
	@ViewInject(R.id.location_tv_place)
	TextView tv_place;
	@ViewInject(R.id.location_view_line)
	View view_line;
	private Context mContext;

	private List<UserIdentity> listUserIdentity = new ArrayList<UserIdentity>();

	// 签到条件选择
	private PopupWindow ppw;
	private int signWay = 5150001;
	boolean isRequest = false;// 是否手动触发请求定位
	boolean isFirstLoc = true;// 是否首次定位
	boolean isFirstLoad = true;// 是否首次进入
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private JSYApplication app;
	private String str_mystreet = "";
	private SharedPreferences sp;
	private String str_visonName;
	private String str_phoneModel = android.os.Build.MODEL;
	// private Date date;
	private String str_month;
	// private String str_hour;
	private String str_day;
	private String str_time;
	private String str_year;
	private ProgressDialog dialog;
	private int str_signWay = 1;
	private int SignInFlag = 0;
	private List<FenceUnitInfo> fenceList;

	// 定位模块**********************************
	private LocationClientOption.LocationMode mLocationMode;// 定位精度
	private boolean mLocationSequency;// 定位频率模式
	private int mScanSpan;// 定位间隔时间
	private boolean mIsNeedAddress;// 反地理编码功能
	private String mCoordType;// 坐标格式
	private boolean mIsNeedDirection;// 是否需要手机头部对应的方向

	private boolean mGeofenceInit;// 是否成功设置地理围栏
	// 高精度地理围栏
	// private String mHightAccuracyLongitude;
	// private String mHightAccuracyLaitude;
	// private String mHightAccuracyRadius;
	// private String mHightAccuracyCoordType;

	// 是否设置了option
	private boolean mLocationInit;
	// 定位单例
	private LocationClient mLocClient;

	private BDLocation location;

	private LocationData locData = null;

	@OnClick({ R.id.location_btn_location, R.id.bottombar_btn_sign,
			R.id.bottombar_btn_record, R.id.location_tv_wayofparent,
			R.id.location_tv_wayofchild })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.location_btn_location:
			// 单次请求定位
			if (!mLocationSequency && mLocClient.isStarted()) {
				mLocClient.requestLocation();
			}
			tv_place.setText(getResources()
					.getString(R.string.location_loading));

			break;
		case R.id.bottombar_btn_sign:
			if ("".equals(sp.getString("UnitName", ""))) {
				BaseUtils.longToast(LocationActivity.this,
						R.string.please_choose_unit);

			} else {
				if (!"".equals(str_mystreet) && mGeofenceInit
						&& fenceList != null && fenceList.size() > 0) {
					showSignDialog();

				} else {
					BaseUtils.longToast(mContext,
							R.string.unitAddress_orPersonalAddress_cannotEmpty);
				}
			}
			break;
		case R.id.bottombar_btn_record:
			startActivity(new Intent(mContext, SearchActivity.class));
			break;
		case R.id.location_tv_wayofparent:
		case R.id.location_tv_wayofchild:
			if (sp.getInt("UnitID", 0) == 0
					|| Constant.listParentSignWay == null
					|| Constant.listParentSignWay.size() == 0) {
				httpGetSignWay();
			} else {
				chosesignway();
			}
			break;
		default:
			break;
		}
	}

	int mSingleChoiceID = -1;

	/**
	 * 选择签到地点
	 */
	private void showSignDialog() {

		FenceUnitInfo selectUnit = fenceList.get(0);
		double uLat = Double.valueOf(selectUnit.getLatitude()).doubleValue();
		double uLon = Double.valueOf(selectUnit.getLongitude()).doubleValue();
		double mLat = location.getLatitude();
		double mLon = location.getLongitude();
		double dis = Maps.getDistatce(uLat, uLon, mLat, mLon);
		if (dis <= Integer.parseInt(selectUnit.getRange())) {
			SignInFlag = 0;
		} else {
			SignInFlag = 1;
			ToastUtil.showMessage(mContext, R.string.not_in);
		}

		String[] mItems = new String[fenceList.size()];
		for (int i = 0; i < fenceList.size(); i++) {
			mItems[i] = fenceList.get(i).getAddressName();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		builder.setTitle(R.string.sign_in);
		builder.setSingleChoiceItems(mItems, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mSingleChoiceID = whichButton;
						FenceUnitInfo selectUnit = fenceList
								.get(mSingleChoiceID);
						double uLat = Double.valueOf(selectUnit.getLatitude())
								.doubleValue();
						double uLon = Double.valueOf(selectUnit.getLongitude())
								.doubleValue();
						double mLat = location.getLatitude();
						double mLon = location.getLongitude();
						double dis = Maps.getDistatce(uLon, uLat, mLon, mLat);
						if (dis <= Integer.parseInt(selectUnit.getRange())) {
							SignInFlag = 0;
						} else {
							SignInFlag = 1;
							ToastUtil.showMessage(mContext, R.string.not_in);
						}
					}
				});
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						getTime(0);
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void httpGetSignWay() {
		try {
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
			RequestParams params = new RequestParams();
			params.addBodyParameter("unitid", sp.getInt("UnitID", 0) + "");
			new HttpUtils().send(
					HttpRequest.HttpMethod.POST,
					ACache.get(mContext.getApplicationContext()).getAsString(
							"KaoQUrl")
							+ getSignWay, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dialog.dismiss();
							BaseUtils.shortToast(mContext, getResources()
									.getString(R.string.error_serverconnect));
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							dialog.dismiss();
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String ResultCode = jsonObj
										.getString("ResultCode");

								if ("0".equals(ResultCode)) {
									GetSignWay getSignWay = GsonUtil
											.GsonToObject(arg0.result,
													GetSignWay.class);

									Constant.listParentSignWay = getSignWay
											.getData();
									if (!isFirstLoad) {
										chosesignway();
									}
									isFirstLoad = false;
								} else {
									if (Constant.listParentSignWay != null) {
										Constant.listParentSignWay.clear();
									}
								}
							} catch (Exception e) {
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择签到方式
	 */
	private void chosesignway() {

		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		ChoseSignPopup choseSignWay = new ChoseSignPopup(mContext);
		choseSignWay.showPop(layout_ui, view_line.getTop() + statusBarHeight
				+ getSupportActionBar().getHeight(), tv_wayofparent,
				tv_wayofchild);
	}

	/**
	 * 切换单位
	 */
	private void choseUnit() {
		try {
			Rect frame = new Rect();
			getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			if (listUserIdentity != null) {
				listUserIdentity.clear();
			}
			for (UserIdentity userIdentity : Constant.listUserIdentity) {
				try {
					if (userIdentity.getUserUnits() != null
							&& userIdentity.getUserUnits().size() != 0) {
						listUserIdentity.add(userIdentity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ShowPopup showPopup = new ShowPopup(mContext);
			showPopup.setVisitPlace("sign");
			showPopup.showPop(layout_ui, statusBarHeight
					+ getSupportActionBar().getHeight(), listUserIdentity,
					mHandler);
		} catch (Exception e) {
			e.printStackTrace();
			VisitPublicHttp.getInstance().getRoleIdentity();
		}
	}

	// 获取网络时间
	private void getTime(final int way) {
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, getcurTime,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtil.showMessage(mContext,
								R.string.get_standardTime_failed);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject jsonObj = new JSONObject(arg0.result);
							str_time = jsonObj.getString("Data");
							mHandler.sendEmptyMessage(way);
						} catch (JSONException e) {
							ToastUtil.showMessage(mContext,
									R.string.get_standardTime_failed);
						}
					}
				});
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				httpSign();
				break;
			case 10:
				String title = (String) msg.obj;
				setActionBarTitle(title);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 签到
	 */
	private void httpSign() {
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		try {
			RequestParams params = new RequestParams();
			String regjson = "{\"SignInDateTime\":\"%s\",\"Longitude\":\"%s\",\"Latitude\":\"%s\",\"Place\":\"%s\",\"UserID\":\"%s\",\"UserName\":\"%s\",\"UserTypeID\":\"%s\",\"UnitID\":\"%s\",\"UnitName\":\"%s\",\"MobileEdition\":\"%s\",\"MobileModel\":\"%s\",\"SignInTypeID\":\"%s\",\"SignInGroupID\":\"%s\",\"Year\":\"%s\",\"Month\":\"%s\",\"day\":\"%s\",\"HandleFlag\":\"%s\",\"SignInTypeName\":\"%s\",\"SignInGroupName\":\"%s\",\"SignInFlag\":\"%s\",\"Remark\":\"%s\"}";
			String SignInJsonData = String.format(regjson, str_time,
					location.getLongitude(), location.getLatitude(),
					str_mystreet, sp.getInt("UserID", 0),
					sp.getString("UserName", ""), sp.getInt("UserType", 0),
					sp.getInt("UnitID", 0), sp.getString("UnitName", ""),
					str_visonName, str_phoneModel,
					Constant.SIGNWAY_P.getGroupID(),
					Constant.SIGNWAY_C.getTabID(), str_year, str_month,
					str_day, 0, Constant.SIGNWAY_P.getGroupTypeName(),
					Constant.SIGNWAY_C.getGroupName(), SignInFlag, "");
			System.out.println("----------" + SignInJsonData);
			params.addBodyParameter("SignInJsonData", SignInJsonData);
			HttpUtils http = new HttpUtils();
			http.send(
					HttpRequest.HttpMethod.POST,
					ACache.get(mContext.getApplicationContext()).getAsString(
							"KaoQUrl")
							+ user_sign, params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dialog.dismiss();
							BaseUtils.shortToast(
									LocationActivity.this,
									getResources().getString(
											R.string.error_serverconnect));
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							dialog.dismiss();
							try {
								JSONObject jsonObj = new JSONObject(arg0.result);
								String result = jsonObj.getString("ResultCode");
								if ("0".equals(result)) {
									ToastUtil.showMessage(mContext, str_time
											+ "签报成功");
								} else {
									ToastUtil.showMessage(
											mContext,
											getResources().getString(
													R.string.error_sign)
													+ ":" + result);
								}
							} catch (JSONException e) {
								ToastUtil.showMessage(
										mContext,
										getResources().getString(
												R.string.error_serverconnect));
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 使用地图sdk前需先初始化BMapManager. BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		app = (JSYApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(new JSYApplication.MyGeneralListener());
		}
		setContentLayout(R.layout.ui_sign_location);
		ViewUtils.inject(this);
		mContext = this;
		EventBusUtil.register(this);
		btn_search.setText(R.string.records);

		mLocClient = app.mLocationClient;
		dialog = BaseUtils.showDialog(this,
				getResources().getString(R.string.public_later));

		str_visonName = BaseUtils.getVersion(this);
		sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
		if ("".equals(sp.getString("UnitName", ""))) {
			setActionBarTitle(R.string.please_choose_unit);
		} else {
			setActionBarTitle(getResources().getString(R.string.recent_unit_)
					+ sp.getString("UnitName", ""));
		}
		initMap();
		initUnitLocation();// 加载用户单位坐标
		initLocation();
		getCustomSign();
		httpGetSignWay();

	}

	// private void initParentView() {
	// RadioGroup radioGroup = getRadioGroup();
	// getRadio0().setChecked(true);
	// radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	// @Override
	// public void onCheckedChanged(RadioGroup arg0, int arg1) {
	// // TODO Auto-generated method stub
	// switch (arg1) {
	// case R.id.radio0:
	// break;
	// case R.id.radio1:
	// startActivity(new Intent(mContext,WorkPlanActivity.class));
	// finish();
	// break;
	// }
	// }
	// });
	// }
	private void initUnitLocation() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("ID", sp.getInt("UnitID", 0) + "");
		HttpUtils http = new HttpUtils();
		http.send(
				HttpRequest.HttpMethod.POST,
				ACache.get(mContext.getApplicationContext()).getAsString(
						"KaoQUrl")
						+ fance_select, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						BaseUtils.shortToast(mContext, getResources()
								.getString(R.string.error_serverconnect));
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject jsonObj = new JSONObject(arg0.result);
							String result = jsonObj.getString("ResultCode");
							if ("0".equals(result)) {
								GetFenceUnitInfo getListFence = GsonUtil
										.GsonToObject(arg0.result,
												GetFenceUnitInfo.class);
								fenceList = getListFence.getData();

							} else {

							}
						} catch (JSONException e) {
							BaseUtils.shortToast(mContext, getResources()
									.getString(R.string.error_serverconnect)
									+ "1002");
						}
						initFence();// 加载围栏
					}
				});
	}

	List<Object> unitOvers = new ArrayList<Object>();

	protected void initFence() {
		try {
			// 地理围栏选项
			// 高精度

			// mHightAccuracyCoordType = BDGeofence.COORD_TYPE_BD09LL;
			mGeofenceInit = true;
			for (int i = 0; i < fenceList.size(); i++) {
				double mLat = Double.valueOf(fenceList.get(i).getLatitude())
						.doubleValue();
				double mLon = Double.valueOf(fenceList.get(i).getLongitude())
						.doubleValue();
				// 绘制文字
				TextOverlay textOverlay = new TextOverlay(mMapView);
				unitOvers.add(textOverlay);
				mMapView.getOverlays().add(textOverlay);
				textOverlay.addText(drawText(mLat, mLon, fenceList.get(i)
						.getAddressName()));
				// 执行地图刷新使生效
				mMapView.refresh();
				addCustomElements(mLat, mLon,
						Integer.parseInt(fenceList.get(i).getRange()));
			}
		} catch (Exception e) {
			mGeofenceInit = false;
			e.printStackTrace();
		}
		// 地理围栏实现
		if (mGeofenceInit) {
			// 高精度
			// 4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
			for (int i = 0; i < fenceList.size(); i++) {
				app.mNotifyLister.SetNotifyLocation(
						Double.valueOf(fenceList.get(i).getLatitude()),
						Double.valueOf(fenceList.get(i).getLongitude()),
						Float.valueOf(fenceList.get(i).getRange()),
						BDGeofence.COORD_TYPE_BD09LL);

			}
			mLocClient.registerNotify(app.mNotifyLister);
		}
	}

	/**
	 * 定位模块************************************
	 */
	private void initLocation() {

		initLocationOverlay();
		getLocationParams();
		setLocationOption();

		// 开始定位
		if (mLocationInit) {
			mLocClient.start();
		} else {
			Toast.makeText(this,
					R.string.please_set_positionRelativeParameters,
					Toast.LENGTH_SHORT).show();
			return;
		}

	}

	/**
	 * 定位图层
	 */
	private void initLocationOverlay() {
		locData = new LocationData();
		// 定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.setLocationMode(LocationMode.NORMAL);
		// 设置定位数据
		myLocationOverlay.setData(locData);
		// 添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// 修改定位数据后刷新图层生效
		mMapView.refresh();

	}

	/**
	 * 获取定位参数
	 */
	private void getLocationParams() {
		// 定位精度
		mLocationMode = LocationClientOption.LocationMode.Battery_Saving;// 低功耗
		// mLocationMode = LocationMode.Device_Sensors;//仅用设备

		// 定位频率模式
		mLocationSequency = true;// 是--需要设置定位间隔时间
		mScanSpan = 10000;// 10秒

		// 是否使用反地理编码功能
		mIsNeedAddress = true;// 使用反地理编码功能
		// mIsNeedAddress = false;//不使用反地理编码功能

		// 设置坐标格式
		// mCoordType = "gcj02";//坐标格式
		// mCoordType = "bd09";
		mCoordType = BDGeofence.COORD_TYPE_BD09LL;

		// 是否获取传感器方向
		// mIsNeedDirection = true;//需要方向
		mIsNeedDirection = false;// 不需要方向

	}

	/**
	 * 设置Option
	 */
	private void setLocationOption() {
		try {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(mLocationMode);
			option.setCoorType(mCoordType);
			option.setScanSpan(mScanSpan);
			option.setNeedDeviceDirect(mIsNeedDirection);
			option.setIsNeedAddress(mIsNeedAddress);
			mLocClient.setLocOption(option);
			mLocationInit = true;
		} catch (Exception e) {
			e.printStackTrace();
			mLocationInit = false;
		}
	}

	/**
	 * 添加点、线、多边形、圆、文字
	 */
	public void addCustomElements(double lat, double lon, int range) {
		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(graphicsOverlay);
		// 添加圆
		graphicsOverlay.setData(drawCircle(lat, lon, range));
		unitOvers.add(graphicsOverlay);
		// 执行地图刷新使生效
		mMapView.refresh();
	}

	/**
	 * 绘制圆，该圆随地图状态变化
	 * 
	 * @return 圆对象
	 */
	public Graphic drawCircle(double mLat, double mLon, int range) {
		int lat = (int) (mLat * 1E6);
		int lon = (int) (mLon * 1E6);
		GeoPoint pt1 = new GeoPoint(lat, lon);

		// 构建圆
		Geometry circleGeometry = new Geometry();

		// 设置圆中心点坐标和半径
		circleGeometry.setCircle(pt1, range);
		// 设置样式
		Symbol circleSymbol = new Symbol();
		Symbol.Color circleColor = circleSymbol.new Color();
		circleColor.red = 0;
		circleColor.green = 0;
		circleColor.blue = 0;
		circleColor.alpha = 0;
		circleSymbol.setSurface(circleColor, 1, 3, new Stroke(3,
				circleSymbol.new Color(0xFFFF0000)));
		// 生成Graphic对象
		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
		return circleGraphic;
	}

	/**
	 * 绘制文字，该文字随地图变化有透视效果
	 * 
	 * @param string
	 * @return 文字对象
	 */
	public TextItem drawText(double mLat, double mLon, String string) {
		int lat = (int) (mLat * 1E6);
		int lon = (int) (mLon * 1E6);
		// 构建文字
		TextItem item = new TextItem();
		// 设置文字位置
		item.pt = new GeoPoint(lat, lon);
		// 设置文件内容
		item.text = string;
		// 设文字大小
		item.fontSize = 20;
		Symbol symbol = new Symbol();
		Symbol.Color bgColor = symbol.new Color();
		// 设置文字背景色
		bgColor.red = 0;
		bgColor.blue = 0;
		bgColor.green = 255;
		bgColor.alpha = 50;

		Symbol.Color fontColor = symbol.new Color();
		// 设置文字着色
		fontColor.alpha = 255;
		fontColor.red = 0;
		fontColor.green = 0;
		fontColor.blue = 255;
		// 设置对齐方式
		item.align = TextItem.ALIGN_CENTER;
		// 设置文字颜色和背景颜色
		item.fontColor = fontColor;
		item.bgColor = bgColor;
		return item;
	}

	/**
	 * 实现进行围栏监听器
	 * 
	 */
	public class GeofenceTriggerListener implements OnGeofenceTriggerListener {

		@Override
		public void onGeofenceTrigger(String geofenceId) {
			// 开发者实现进入围栏的功能逻辑
			try {
				((Vibrator) LocationActivity.this.getApplication()
						.getSystemService(Service.VIBRATOR_SERVICE))
						.vibrate(3000);
				Toast.makeText(
						LocationActivity.this,
						getResources().getString(R.string.been_into_enclosure)
								+ geofenceId, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 定位完成返回结果
	 * 
	 * @param locationMsg
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.appcenter_location_success:
			try {
				BDLocation getLocation = (BDLocation) list.get(1);
				if (getLocation != null) {
					location = getLocation;
					str_mystreet = location.getAddrStr();
					sp.edit().putString("LOCATION_NAME", str_mystreet).commit();
					tv_place.setText(str_mystreet);
					if (location == null)
						return;
					System.out.println("------" + location.getLatitude() + ""
							+ location.getLongitude());
					locData.latitude = location.getLatitude();
					locData.longitude = location.getLongitude();
					// 如果不显示定位精度圈，将accuracy赋值为0即可
					locData.accuracy = location.getRadius();
					// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
					locData.direction = location.getDerect();
					// 更新定位数据
					myLocationOverlay.setData(locData);
					// 更新图层数据执行刷新后生效
					mMapView.refresh();

					GeoPoint ptCenter = new GeoPoint(
							(int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6));
					mMapController.animateTo(ptCenter);
				}
			} catch (Exception e) {
				// ToastUtil.showMessage(mContext, "定位出错啦，请重新定位");
			}
			break;
		case Constant.msgcenter_work_change:
			initUnitLocation();// 加载用户单位坐标
			if (unitOvers != null) {
				mMapView.getOverlays().clear();
				// 定位图层初始化
				myLocationOverlay = new MyLocationOverlay(mMapView);
				myLocationOverlay.setLocationMode(LocationMode.NORMAL);
				// 设置定位数据
				myLocationOverlay.setData(locData);
				// 添加定位图层
				mMapView.getOverlays().add(myLocationOverlay);
				myLocationOverlay.enableCompass();
				// 修改定位数据后刷新图层生效
				mMapView.refresh();
			}
			ToastUtil.showMessage(mContext, R.string.change_success);
			break;
		default:
			break;
		}
	}

	/**
	 * 加载地图
	 */
	private void initMap() {
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(18);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(false);

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		mLocClient.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		mLocClient.start();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出销毁地图
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.destroy();
		EventBus.getDefault().unregister(this);

		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	List<HashMap<String, Object>> customList;

	private void getCustomSign() {
		customList = new ArrayList<HashMap<String, Object>>() {
		};
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", 1);
		map1.put("name", getResources().getString(R.string.parent_party));
		customList.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", 2);
		map2.put("name", getResources().getString(R.string.train));
		customList.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("id", 3);
		map3.put("name", getResources().getString(R.string.business_trip));
		customList.add(map3);
	}

	public void showPop() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View popupLayout = inflater.inflate(R.layout.layout_ppw_signway,
				null);
		Button btn_take = (Button) popupLayout
				.findViewById(R.id.signway_btn_ok);
		RadioGroup radioGroup = (RadioGroup) popupLayout
				.findViewById(R.id.signway_radioGroup);
		RadioButton signin = (RadioButton) popupLayout
				.findViewById(R.id.signway_radio_signin);
		RadioButton signout = (RadioButton) popupLayout
				.findViewById(R.id.signway_radio_signout);
		Spinner spinner = (Spinner) popupLayout
				.findViewById(R.id.signway_spinner);
		switch (signWay) {
		case 1:
			signin.setChecked(true);
			break;
		case 2:
			signout.setChecked(true);
			break;
		default:
			break;
		}
		if (str_signWay == 4) {
			spinner.setVisibility(View.VISIBLE);
			// customsignname = customList.get(0).get("name").toString();
			// customsignid = (Integer) customList.get(0).get("id");
			SimpleAdapter adapter = new SimpleAdapter(this, customList,
					android.R.layout.simple_list_item_1,
					new String[] { "name" }, new int[] { android.R.id.text1 });
			// 设置下拉列表的风格
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// customsignname = customList.get(arg2).get("name")
					// .toString();
					// customsignid = (Integer) customList.get(arg2).get("id");
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.signway_radio_signin:
					signWay = 1;

					break;
				case R.id.signway_radio_signout:
					signWay = 2;
					break;
				default:
					break;
				}
			}
		});
		ppw = new PopupWindow(mContext);
		ppw.setBackgroundDrawable(new BitmapDrawable());
		ppw.setWidth(LayoutParams.WRAP_CONTENT);
		ppw.setHeight(LayoutParams.WRAP_CONTENT);
		ppw.setOutsideTouchable(false);
		ppw.setFocusable(true);
		ppw.setContentView(popupLayout);
		ppw.showAtLocation(layout_ui, Gravity.CENTER, 0, 0);
		btn_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (str_signWay == 4) {
				} else
					ppw.dismiss();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu sub_search = menu.addSubMenu(R.string.change).setIcon(
				R.drawable.btn_right_swich);
		sub_search.getItem().setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		sub_search.getItem().setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						choseUnit();
						return false;
					}
				});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}