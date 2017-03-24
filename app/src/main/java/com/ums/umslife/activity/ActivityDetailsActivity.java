package com.ums.umslife.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.ums.umslife.R;
import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.bean.ActivityApplyBean;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.bean.ActivitySignBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.utils.PictureUtil;
import com.ums.umslife.view.SuccinctProgress;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ums.umslife.fragment.ActivityFragment.PIC_BASE_URL;

public class ActivityDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_title)
    ImageView ivTitle;
    @BindView(R.id.tv_act_name)
    TextView tvActName;
    @BindView(R.id.tv_club_name)
    TextView tvClubName;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.tv_signStartTime)
    TextView tvSignStartTime;
    @BindView(R.id.tv_signEndTime)
    TextView tvSignEndTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private final static String IS_SIGN = "1";
    private final static String NOT_SIGN = "0";
    private final static Double MAX_DISTANCE = 1000.00;
    private ActivityBean.DataBean.AllActivityListBean allActListBean = new ActivityBean.DataBean.AllActivityListBean();
    private ActivityBean.DataBean.HotActivityListBean hotActListBean = new ActivityBean.DataBean.HotActivityListBean();
    private ActivityApplyBean applyBean;
    private ActivitySignBean signBean = new ActivitySignBean();
    private Context mContext;
    private String applyState, activityNo, phone, integral;
    private BDLocationListener myListener;
    private LocationClient mLocationClient = null;
    private LatLng actLatLng;
    private Double actLat = 0.00, actLng = 0.00;
    private Map<String, RequestBody> paramsMaps = new HashMap<>();
    private BDLocation location;
    private String theme, signStartTime, signEndTime, enrollment,
            stopTime, content, picUrl, actLatLngStr, clubNo, signAtta, place, clubName, details, joinState;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    checkResult(location);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_act_details);
        ButterKnife.bind(this);
        init();
        initView();
        initData();
        initState();
    }


    protected void init() {
        mContext = this;
        setBackBtn();
        setTitle("活动详情");

    }

    protected void initView() {
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        initLocation();
    }

    protected void initData() {
        Intent actDetailIt = getIntent();
        SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        if (actDetailIt.getIntExtra("code", 3) == MyAppConfig.ALL_ACT_CODE) {
            allActListBean = (ActivityBean.DataBean.AllActivityListBean) actDetailIt
                    .getSerializableExtra("allActListBean");
            activityNo = allActListBean.getActivityNo();
            integral = allActListBean.getIntegral();
            picUrl = PIC_BASE_URL + allActListBean.getPicURL();
            theme = allActListBean.getActivityTheme();
            signStartTime = allActListBean.getSignStartTime();
            signEndTime = allActListBean.getSignEndTime();
            enrollment = allActListBean.getEnrollment();
            stopTime = allActListBean.getStopTime();
            integral = allActListBean.getIntegral();
            content = allActListBean.getActivityContent();
            actLatLngStr = allActListBean.getLng_lat();
            Log.d(TAG, "initData: " + actLatLngStr);
            clubNo = allActListBean.getClubNo();
            place = allActListBean.getActivityPlace();
            signAtta = allActListBean.getSign_atta();
            clubName = allActListBean.getClubName();
            details = allActListBean.getDetail();
            joinState = allActListBean.getJoinState();
        } else if (actDetailIt.getIntExtra("code", 3) == MyAppConfig.HOT_ACT_CODE) {
            hotActListBean = (ActivityBean.DataBean.HotActivityListBean) actDetailIt
                    .getSerializableExtra("hotActListBean");
            activityNo = hotActListBean.getActivityNo();
            integral = hotActListBean.getIntegral();
            picUrl = PIC_BASE_URL + hotActListBean.getPicURL();
            theme = hotActListBean.getActivityTheme();
            signStartTime = hotActListBean.getSignStartTime();
            signEndTime = hotActListBean.getSignEndTime();
            enrollment = hotActListBean.getEnrollment();
            stopTime = hotActListBean.getStopTime();
            integral = hotActListBean.getIntegral();
            content = hotActListBean.getActivityContent();
            actLatLngStr = hotActListBean.getLng_lat();
            clubNo = hotActListBean.getClubNo();
            place = hotActListBean.getActivityPlace();
            signAtta = hotActListBean.getSign_atta();
            clubName = hotActListBean.getClubName();
            details = hotActListBean.getDetail();
            joinState = hotActListBean.getJoinState();
        }
    }


    /**
     * 初始化状态
     */
    private void initState() {
        Glide.with(mContext).load(picUrl).error(R.drawable.bg_error_img).into(ivTitle);
        if (!actLatLngStr.isEmpty()) {
            try {
                actLat = Double.valueOf(actLatLngStr.substring(0, actLatLngStr.indexOf(",")));
                actLng = Double.valueOf(actLatLngStr.substring(actLatLngStr.indexOf(",") + 1, actLatLngStr.length()));
            } catch (Exception e) {
                Log.d(MyAppConfig.TAG, "异常" + e.getMessage());
            }
        }
        actLatLng = new LatLng(actLat, actLng);
        tvActName.setText(theme);
        tvSignStartTime.setText(signStartTime);
        tvSignEndTime.setText(signEndTime);
        tvMember.setText(enrollment);
        tvEndTime.setText(stopTime);
        tvIntegral.setText(integral);
        tvContent.setText(content);
        tvAddress.setText(place);
        if (clubNo.equals("0")) {
            tvClubName.setText("全体成员");
        } else {
            tvClubName.setText(clubName);
        }
        if (signAtta.equals(MyAppConfig.ONE_CODE)) {
            tvSign.setVisibility(View.GONE);
            tvUpload.setVisibility(View.VISIBLE);
        } else {
            tvSign.setVisibility(View.VISIBLE);
            tvUpload.setVisibility(View.GONE);
        }
        if (details.isEmpty()) {
            llMore.setVisibility(View.GONE);
        }
        if (joinState.isEmpty()) {
            applyState = "0";
            tvApply.setText("报名");
            tvApply.setEnabled(true);
            tvSign.setText("签到");
            tvSign.setEnabled(false);
            tvUpload.setEnabled(false);
        } else if (joinState.equals(NOT_SIGN)) {
            applyState = "1";
            tvApply.setText("取消报名");
            tvApply.setEnabled(true);
            tvSign.setText("签到");
            tvSign.setEnabled(true);
            tvUpload.setEnabled(true);
        } else if (joinState.equals(IS_SIGN)) {
            applyState = "1";
            tvApply.setText("取消报名");
            tvApply.setEnabled(false);
            tvSign.setText("已签到");
            tvSign.setEnabled(false);
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


    private void upload() {
        RxGalleryFinal.with(mContext)
                .image()
//                .radio()
//                .crop()
                .multiple()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) {
                        File file = null;
                        try {
                            String originalPath = imageMultipleResultEvent.getResult().get(0).getOriginalPath();
                            String smallPath = PictureUtil.bitmapToPath(originalPath);
                            Log.d(TAG, "onEvent: ==========" + smallPath);
                            file = new File(smallPath);
                        } catch (Exception e) {
                            Log.d(TAG, "onEvent: " + e.getMessage());
                            MyUtils.showToast(mContext, "图片路径异常");
                        }
                        if (file != null) {
                            SuccinctProgress.showSuccinctProgress(mContext, "正在上传...",
                                    SuccinctProgress.THEME_LINE, false, false);
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
                            paramsMaps.put("file\";filename=\"" + file.getName(), requestBody);
                            HttpUtils.init().upLoadPic(phone, activityNo, paramsMaps).enqueue(new Callback<ActivitySignBean>() {
                                @Override
                                public void onResponse(Call<ActivitySignBean> call, Response<ActivitySignBean> response) {
                                    signBean = response.body();
                                    if (signBean != null) {
                                        switch (signBean.getCode()) {
                                            case MyAppConfig.SUCCESS_CODE:

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
                                public void onFailure(Call<ActivitySignBean> call, Throwable t) {
                                    SuccinctProgress.dismiss();
                                    initState();
                                    Log.d(MyAppConfig.TAG, "异常" + t.getMessage());
                                    MyUtils.showToast(mContext, "数据异常");
                                }

                            });
                        }

                    }
                }).openGallery();
    }

    /**
     * 百度定位
     */
    private void location() {
        SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
                SuccinctProgress.THEME_LINE, false, false);
        tvSign.setEnabled(false);
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //已经禁止提示了
                SuccinctProgress.dismiss();
                initState();
                Toast.makeText(mContext, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            } else {
                //请求权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }

        } else {
            //有权限，开始定位
            Log.d(TAG, "location: 有权限开始定位");
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
        tvApply.setEnabled(false);
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
                            joinState = applyBean.getData()
                                    .getJoinState();
                            enrollment = applyBean.getData().getEnrollment();
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
                initState();
            }

            @Override
            public void onFailure(Call<ActivityApplyBean> call,
                                  Throwable throwable) {
                SuccinctProgress.dismiss();
                initState();
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
                                    joinState = IS_SIGN;
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
                        initState();
                    }

                    @Override
                    public void onFailure(Call<ActivitySignBean> call,
                                          Throwable throwable) {
                        SuccinctProgress.dismiss();
                        initState();
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext, "数据异常");
                    }
                });
    }


    private void checkResult(BDLocation location) {
        String result = "";
        //获取定位结果
        StringBuilder sb = new StringBuilder(256);

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
                sb.append(p.getId()).append(" ").append(p.getName()).append(" ").append(p.getRank());
            }
        }
        mLocationClient.stop();
        LatLng LocLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Double distance = DistanceUtil.getDistance(actLatLng, LocLatLng);
        Log.d(MyAppConfig.TAG, "距离: " + distance + (distance < MAX_DISTANCE));
        Log.i(TAG, sb.toString());
        int Type = location.getLocType();
        if (Type == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation) {
            if (distance < MAX_DISTANCE) {
                sign();
            } else {
                SuccinctProgress.dismiss();
                MyUtils.showToast(mContext, "距离太远，无法签到");
                initState();
            }
        } else if (result.isEmpty()) {
            SuccinctProgress.dismiss();
            initState();
            MyUtils.showToast(mContext, "定位失败");
        } else {
            SuccinctProgress.dismiss();
            initState();
            MyUtils.showToast(mContext, result);
        }

        Log.i(TAG, sb.toString());
    }

    @OnClick({R.id.tv_apply, R.id.tv_sign, R.id.tv_upload, R.id.ll_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_apply:
                apply();
                break;
            case R.id.tv_sign:
                location();
                break;
            case R.id.tv_upload:
                upload();
                break;
            case R.id.ll_more:
                Intent moreDetailIt = new Intent(mContext, MoreDetailsActivity.class);
                moreDetailIt.putExtra("activityNo", activityNo);
                startActivity(moreDetailIt);
                break;
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            ActivityDetailsActivity.this.location = location;
            mHandler.sendEmptyMessage(1);

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
