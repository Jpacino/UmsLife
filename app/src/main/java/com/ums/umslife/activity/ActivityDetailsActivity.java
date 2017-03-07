package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.ums.umslife.R;
import com.ums.umslife.bean.ActivityApplyBean;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.bean.ActivitySignBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.utils.ToastUtil;
import com.ums.umslife.view.SuccinctProgress;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailsActivity extends BaseActivity implements
		OnClickListener {
	private TextView signTv, applyTv;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private final static String IS_SIGN = "1";
	private final static String NOT_SIGN = "0";
	private ActivityBean.ActivitysBean activitysBean;
	private TextView themeTv, clubNameTv, signStartTimeTv, signEndTimeTv,
			placeTv, enrollmentTv, stopTimeTv, contentTv, clubNameTv2,integralTv;
	private ActivityApplyBean applyBean;
	private ActivitySignBean signBean = new ActivitySignBean();
	private Context mContext;
	private String applyState, activityNo, phone, integral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		mContext = this;
		initTitle("活动详情");
		init();
		initData();
		initLocation();
	}

	private void init() {
		signTv = (TextView) findViewById(R.id.tv_to_sign);
		applyTv = (TextView) findViewById(R.id.tv_apply);
		ImageView backIv = (ImageView) findViewById(R.id.iv_base_back);
		themeTv = (TextView) findViewById(R.id.activity_name_tv);
		clubNameTv = (TextView) findViewById(R.id.club_tv);
		signStartTimeTv = (TextView) findViewById(R.id.signStartTime_tv);
		signEndTimeTv = (TextView) findViewById(R.id.signEndTime_tv);
		enrollmentTv = (TextView) findViewById(R.id.member_tv);
		ImageView rightIv = (ImageView) findViewById(R.id.img_go_right);
		placeTv = (TextView) findViewById(R.id.address_tv);
		stopTimeTv = (TextView) findViewById(R.id.endTime_tv);
		contentTv = (TextView) findViewById(R.id.content_tv);
		integralTv = (TextView) findViewById(R.id.integral_tv);
		clubNameTv2 = (TextView) findViewById(R.id.club_tv2);
		RelativeLayout clubRl = (RelativeLayout) findViewById(R.id.club_member_rl);
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		backIv.setVisibility(View.VISIBLE);
		clubRl.setOnClickListener(this);
		backIv.setOnClickListener(this);
		signTv.setOnClickListener(this);
		applyTv.setOnClickListener(this);

	}

	private void initData() {
		Intent actDetailIt = getIntent();
		activitysBean = (ActivityBean.ActivitysBean) actDetailIt
				.getSerializableExtra("activitysBean");
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
		activityNo = activitysBean.getActivityNo();
		integral = activitysBean.getIntegral();
		themeTv.setText(activitysBean.getActivityTheme());
		signStartTimeTv.setText(activitysBean.getSignStartTime());
		signEndTimeTv.setText(activitysBean.getSignEndTime());
		enrollmentTv.setText(activitysBean.getEnrollment());
		stopTimeTv.setText(activitysBean.getStopTime());
		integralTv.setText(activitysBean.getIntegral());
		contentTv.setText(activitysBean.getActivityContent());
		if (activitysBean.getClubNo().equals("0")) {
			clubNameTv.setText("全体成员");
			clubNameTv2.setText("全体成员");
		} else {
			clubNameTv.setText(activitysBean.getClubName());
			clubNameTv2.setText(activitysBean.getClubName());
		}
		placeTv.setText(activitysBean.getActivityPlace());
		initState();
	}

	/**
	 * 初始化报名和签到的状态
	 */
	private void initState() {
		if (activitysBean.getJoinState().isEmpty()) {
			applyState = "0";
			applyTv.setText("报名");
			applyTv.setBackgroundResource(R.drawable.bt_count_down_bg_selector);
			applyTv.setClickable(true);
			signTv.setText("签到");
			signTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
			signTv.setClickable(false);
		} else if (activitysBean.getJoinState().equals(NOT_SIGN)) {
			applyState = "1";
			applyTv.setText("取消报名");
			applyTv.setBackgroundResource(R.drawable.bt_count_down_bg_selector);
			applyTv.setClickable(true);
			signTv.setText("签到");
			signTv.setBackgroundResource(R.drawable.bt_count_down_bg_selector);
			signTv.setClickable(true);
		} else if (activitysBean.getJoinState().equals(IS_SIGN)) {
			applyState = "1";
			applyTv.setText("取消报名");
			applyTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
			applyTv.setClickable(false);
			signTv.setText("已签到");
			signTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
			signTv.setClickable(false);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_base_back:
			finish();
			break;
		case R.id.tv_to_sign:
			sign();
			break;
		case R.id.tv_apply:
			apply();
			break;
		case R.id.club_member_rl:
//			MyUtils.startAct(mContext, clazz);
			break;

		default:
			break;
		}
	}

	private void apply() {
		SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
				SuccinctProgress.THEME_LINE, false, false);
		applyTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
		applyTv.setClickable(false);
		Call<ActivityApplyBean> call = HttpUtils.init().getActivityApplyBean(
				phone, activityNo, applyState);
		call.enqueue(new Callback<ActivityApplyBean>() {
			@Override
			public void onResponse(Call<ActivityApplyBean> condition,
								   Response<ActivityApplyBean> response) {
				applyBean = response.body();
				if (applyBean != null) {

					switch (applyBean.getCode()) {
						case MyAppConfig.SUCCESS_CODE:
							activitysBean.setJoinState(applyBean.getData()
									.getJoinState());
							MyUtils.showToast(mContext, "" + applyBean.getReason());
							break;
						case MyAppConfig.DEFEAT_CODE:
							MyUtils.showToast(mContext, "" + applyBean.getReason());
							break;
						case MyAppConfig.TWO_CODE:
							MyUtils.showToast(mContext, "" + applyBean.getReason());
							break;
						case MyAppConfig.THREE_CODE:
							MyUtils.showToast(mContext, "" + applyBean.getReason());
							break;
						default:
							MyUtils.showToast(mContext, "数据异常");
							break;
					}
				} else {
					MyUtils.showToast(mContext, "数据异常");
				}
				SuccinctProgress.dismiss();
				initData();
			}

			@Override
			public void onFailure(Call<ActivityApplyBean> call,
								  Throwable throwable) {
				SuccinctProgress.dismiss();
				initData();
				MyUtils.showToast(mContext, "连接失败");
				Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
			}
		});
	}

	private void sign() {
		SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
				SuccinctProgress.THEME_LINE, false, false);
		getLocation();
		Log.d(MyAppConfig.TAG, "phone" + phone + "activityNo" + activityNo
				+ "integral" + integral);
		HttpUtils.init().getActivitySignBean(phone, activityNo, integral)
				.enqueue(new Callback<ActivitySignBean>() {
					@Override
					public void onResponse(Call<ActivitySignBean> condition,
										   Response<ActivitySignBean> response) {
						signBean = response.body();
						if (signBean != null) {
							switch (signBean.getCode()) {
								case MyAppConfig.SUCCESS_CODE:
									activitysBean.setJoinState(IS_SIGN);
									MyUtils.showToast(mContext,
											"" + signBean.getReason());
									break;
								case MyAppConfig.DEFEAT_CODE:
									MyUtils.showToast(mContext,
											"" + signBean.getReason());
									break;
								case MyAppConfig.TWO_CODE:
									MyUtils.showToast(mContext,
											"" + signBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext, "数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext, "数据异常");
						}
						SuccinctProgress.dismiss();
						initData();
					}

					@Override
					public void onFailure(Call<ActivitySignBean> call,
										  Throwable throwable) {
						SuccinctProgress.dismiss();
						initData();
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext, "数据异常");
					}
				});
	}

	private void getLocation() {
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			logResult(location);
			checkResult(location);
			mLocationClient.stop();
		}
	}

	private void logResult(BDLocation location) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// 单位：公里每小时
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// 单位：米
			sb.append("\ndirection : ");
			sb.append(location.getDirection());// 单位度
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append("\ndescribe : ");
			sb.append("gps定位成功");

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			// 运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
			sb.append("\ndescribe : ");
			sb.append("网络定位成功");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			sb.append("\ndescribe : ");
			sb.append("离线定位成功，离线定位结果也是有效的");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			sb.append("\ndescribe : ");
			sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			sb.append("\ndescribe : ");
			sb.append("网络不同导致定位失败，请检查网络是否通畅");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			sb.append("\ndescribe : ");
			sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
		}
		sb.append("\nlocationdescribe : ");
		sb.append(location.getLocationDescribe());// 位置语义化信息
		List<Poi> list = location.getPoiList();// POI数据
		if (list != null) {
			sb.append("\npoilist size = : ");
			sb.append(list.size());
			for (Poi p : list) {
				sb.append("\npoi= : ");
				sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
			}
		}
		Log.i("BaiduLocationApiDem", sb.toString());
	}

	private void checkResult(BDLocation location) {
		String rusulst = null;
		if (location.getLocType() == BDLocation.TypeGpsLocation
				|| location.getLocType() == BDLocation.TypeNetWorkLocation) {
			// GPS定位结果
			// 网络定位结果
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			rusulst = latitude + "," + longitude;
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			rusulst = latitude + "," + longitude;
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			rusulst = "服定位失败，请检查定位权限是否开启";
		} else if (location.getLocType() == BDLocation.TypeNetWorkException
				|| location.getLocType() == 505) {
			rusulst = "定位失败，请检查网络是否通畅";
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			rusulst = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
		}
		MyUtils.showToast(mContext,rusulst + "");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();

	}
}
