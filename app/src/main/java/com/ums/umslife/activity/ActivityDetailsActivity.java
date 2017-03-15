package com.ums.umslife.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.ums.umslife.R;
import com.ums.umslife.bean.ActivityApplyBean;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.bean.ActivitySignBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.SuccinctProgress;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailsActivity extends BaseActivity implements
        OnClickListener {
    private TextView signTv, applyTv;
    private final static String IS_SIGN = "1";
    private final static String NOT_SIGN = "0";
    private final static Double MAX_DISTANCE = 1000.00;
    private ActivityBean.DataBean.AllActivityListBean activitysBean;
    private TextView themeTv, clubNameTv, signStartTimeTv, signEndTimeTv,
            placeTv, enrollmentTv, stopTimeTv, contentTv, integralTv;
    private ActivityApplyBean applyBean;
    private ActivitySignBean signBean = new ActivitySignBean();
    private Context mContext;
    private String applyState, activityNo, phone, integral;
    private BDLocationListener myListener;
    private LocationClient mLocationClient = null;
    private LatLng actLatLng;
    private Double actLat = 0.00, actLng = 0.00;
    private TextView uploadTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_details);
        mContext = this;
        setBackBtn();
        setTitle("活动详情");
        init();
        initData();
        initLocation();
    }

    private void init() {
        signTv = (TextView) findViewById(R.id.tv_to_sign);
        applyTv = (TextView) findViewById(R.id.tv_apply);
        uploadTv = (TextView) findViewById(R.id.tv_to_upload);
        themeTv = (TextView) findViewById(R.id.activity_name_tv);
        clubNameTv = (TextView) findViewById(R.id.club_tv);
        signStartTimeTv = (TextView) findViewById(R.id.signStartTime_tv);
        signEndTimeTv = (TextView) findViewById(R.id.signEndTime_tv);
        enrollmentTv = (TextView) findViewById(R.id.member_tv);
        placeTv = (TextView) findViewById(R.id.address_tv);
        stopTimeTv = (TextView) findViewById(R.id.endTime_tv);
        contentTv = (TextView) findViewById(R.id.content_tv);
        integralTv = (TextView) findViewById(R.id.integral_tv);
        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        signTv.setOnClickListener(this);
        applyTv.setOnClickListener(this);
        uploadTv.setOnClickListener(this);

    }

    private void initData() {
        Intent actDetailIt = getIntent();
        activitysBean = (ActivityBean.DataBean.AllActivityListBean) actDetailIt
                .getSerializableExtra("activitysBean");
        SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        activityNo = activitysBean.getActivityNo();
        integral = activitysBean.getIntegral();
        String actLatLngStr = activitysBean.getLng_lat();
        if (!actLatLngStr.isEmpty()) {
            try {
                actLat = Double.valueOf(actLatLngStr.substring(0, actLatLngStr.indexOf(",")));
                actLng = Double.valueOf(actLatLngStr.substring(actLatLngStr.indexOf(",") + 1, actLatLngStr.length()));
            } catch (Exception e) {
                Log.d(MyAppConfig.TAG, "异常" + e.getMessage());
            }
        }
        actLatLng = new LatLng(actLat, actLng);
        themeTv.setText(activitysBean.getActivityTheme());
        signStartTimeTv.setText(activitysBean.getSignStartTime());
        signEndTimeTv.setText(activitysBean.getSignEndTime());
        enrollmentTv.setText(activitysBean.getEnrollment());
        stopTimeTv.setText(activitysBean.getStopTime());
        integralTv.setText(activitysBean.getIntegral());
        contentTv.setText(activitysBean.getActivityContent());
        if (activitysBean.getClubNo().equals("0")) {
            clubNameTv.setText("全体成员");
        } else {
            clubNameTv.setText(activitysBean.getClubName());
        }
        placeTv.setText(activitysBean.getActivityPlace());
        initState();
    }

    /**
     * 初始化报名和签到的状态
     */
    private void initState() {
        if (activitysBean.getSign_atta().equals(MyAppConfig.ONE_CODE)) {
            signTv.setVisibility(View.GONE);
            uploadTv.setVisibility(View.VISIBLE);
        } else {
            signTv.setVisibility(View.VISIBLE);
            uploadTv.setVisibility(View.GONE);
        }
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

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_back:
                finish();
                break;
            case R.id.tv_to_sign:
                Log.d(MyAppConfig.TAG, "点击签到");
                location();
                break;
            case R.id.tv_apply:
                apply();
                break;
            case R.id.tv_to_upload:
                upload();
                break;
            default:
                break;
        }
    }

    private void upload() {
    }

    /**
     * 百度定位
     */
    private void location() {
        SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
                SuccinctProgress.THEME_LINE, false, false);
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //已经禁止提示了
                Toast.makeText(mContext, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            } else {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }

        } else {
            //有权限，开始定位

            mLocationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    mLocationClient.start();
                } else {
                    MyUtils.showToast(mContext, "禁用了某些权限");
                }
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


    //    private void checkResult(BDLocation location) {
//        String rusulst = null;
//        if (location.getLocType() == BDLocation.TypeGpsLocation
//                || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//            // GPS定位结果
//            // 网络定位结果
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            rusulst = latitude + "," + longitude;
//        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            rusulst = latitude + "," + longitude;
//        } else if (location.getLocType() == BDLocation.TypeServerError) {
//            rusulst = "服定位失败，请检查定位权限是否开启";
//        } else if (location.getLocType() == BDLocation.TypeNetWorkException
//                || location.getLocType() == 505) {
//            rusulst = "定位失败，请检查网络是否通畅";
//        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//            rusulst = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
//        }
//        if (rusulst != null){
//        MyUtils.showToast(mContext, rusulst + "");
//
//        }else{
//            MyUtils.showToast(mContext,"定位失败");
//        }
//    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            String result = "";
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                result = "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因";
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                result = "网络不同导致定位失败，请检查网络是否通畅";
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                result = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            LatLng LocLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            Double distance = DistanceUtil.getDistance(actLatLng, LocLatLng);
            Log.d(MyAppConfig.TAG, "距离: " + distance + (distance < MAX_DISTANCE));
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation) {
                if (distance < MAX_DISTANCE) {
                    sign();
                } else {
                    SuccinctProgress.dismiss();
                    MyUtils.showToast(mContext, "距离太远,无法签到");
                }
            }else if (result.isEmpty()){
                SuccinctProgress.dismiss();
                MyUtils.showToast(mContext, "定位失败");
            }
            else{
                SuccinctProgress.dismiss();
                MyUtils.showToast(mContext, result);
            }

            Log.i("BaiduLocationApiDem", sb.toString());
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
