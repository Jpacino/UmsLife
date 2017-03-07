package com.ums.umslife.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
import com.ums.umslife.activity.ActivityDetailsActivity;
import com.ums.umslife.adapter.ActivityListAdapter;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFragment extends BaseFragment implements
        OnRefreshListener2<ListView> {

    private PullToRefreshListView activityLv;
    private TextView emptyTv;
    private Context mContext;
    private ActivityBean activityBean;
    private ActivityListAdapter adapter;
    private List<String> imgs = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<ActivityBean.ActivitysBean> activityLists = new ArrayList<>();
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private Banner banner;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void initView(TextView titleTv, ImageView titleIv, View childView) {

        mContext = getActivity();
        titleTv.setText("活动");
        activityLv = (PullToRefreshListView) childView
                .findViewById(R.id.lv_activity_list);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.act_headview, null);
        banner = (Banner) headView.findViewById(R.id.act_banner);
        initBanner();

        activityLv.getRefreshableView().addHeaderView(headView);
        emptyTv = (TextView) childView.findViewById(R.id.empty_tv);
        activityLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        activityLv.setOnRefreshListener(this);
        adapter = new ActivityListAdapter(activityLists, mContext);
        activityLv.setAdapter(adapter);
        activityLv.setOnItemClickListener(menuItemClickListener);
        setEmptyView(activityLists.size(), emptyTv);
    }

    private void initBanner() {
        banner.setImageLoader(new GlideImageLoader());
        for (int i = 0; i < 5; i++) {
            imgs.add("http://pic1.sc.chinaz.com/files/pic/pic9/201702/zzpic1504.jpg");
            titles.add("标题"+i);
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerTitles(titles);
        banner.setImages(imgs);
        banner.start();
    }

    /**
     * 加载网络数据
     */
    private void loadData() {
        SharedPreferences loginShare = getActivity().getSharedPreferences("login",
                Context.MODE_PRIVATE);
        String phone = loginShare.getString("phone", "");
        HttpUtils.init().getActivityBean(phone, "0")
                .enqueue(new Callback<ActivityBean>() {
                    @Override
                    public void onResponse(Call<ActivityBean> arg0,
                                           Response<ActivityBean> response) {
                        activityBean = response.body();
                        if (activityBean != null) {
                            switch (activityBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    activityLists.clear();
                                    activityLists.addAll(activityBean.getData());
                                    // Toast.makeText(getActivity(), "已刷新",
                                    // Toast.LENGTH_SHORT).show();
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    Toast.makeText(getActivity(),
                                            activityBean.getReason(),
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "数据异常",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(getActivity(), "数据异常",
                                    Toast.LENGTH_SHORT).show();
                        }

                        adapter.notifyDataSetChanged();
                        setEmptyView(activityLists.size(), emptyTv);
                        MyUtils.complete(activityLv);
                    }

                    @Override
                    public void onFailure(Call<ActivityBean> arg0,
                                          Throwable throwable) {
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        Toast.makeText(getActivity(), "连接失败",
                                Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        setEmptyView(activityLists.size(), emptyTv);
                        MyUtils.complete(activityLv);

                    }

                });
    }

    /**
     * 设置空数据视图
     */
    private void setEmptyView(int size, View v) {
        if (size == 0) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }

    }

    private OnItemClickListener menuItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Intent actDetailIt = new Intent(mContext,
                        ActivityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activitysBean", activityBean.getData()
                        .get(position - 1));
                actDetailIt.putExtras(bundle);
                startActivity(actDetailIt);
            }
        }

    };

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
    }

}
