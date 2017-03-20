package com.ums.umslife.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
import com.ums.umslife.activity.ActivityDetailsActivity;
import com.ums.umslife.adapter.ActRvAdapter;
import com.ums.umslife.adapter.ActivityListAdapter;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,OnBannerListener,SwipeRefreshLayout.OnRefreshListener {
    private static final String PIC_BASE_URL = "http://172.16.87.212:8080/control/picture/";
    private TextView emptyTv;
    private RecyclerView activityRv;
    private Context mContext;
    private ActRvAdapter actRvAdapter;
    private ActivityBean activityBean;
    private Banner banner;
    private List<String> imgs = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<ActivityBean.DataBean.AllActivityListBean> activityLists = new ArrayList<>();
    private List<ActivityBean.DataBean.HotActivityListBean> hotActLists = new ArrayList<>();
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private SwipeRefreshLayout swipeRefreshLayout;

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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void initView(View childView) {
        mContext = getActivity();
        emptyTv = (TextView) childView.findViewById(R.id.empty_tv);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.act_headview,null);
        banner = (Banner) headView.findViewById(R.id.act_banner);
        swipeRefreshLayout = (SwipeRefreshLayout) childView.findViewById(R.id.act_refresh);
        activityRv = (RecyclerView) childView.findViewById(R.id.activity_rv);
        actRvAdapter = new ActRvAdapter(R.layout.item_activity_list,activityLists);
        actRvAdapter.addHeaderView(headView);
        actRvAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.red_main));
        actRvAdapter.setOnItemClickListener(this);
        activityRv.setLayoutManager(new LinearLayoutManager(mContext));
        activityRv.setAdapter(actRvAdapter);
        banner.setOnBannerListener(this);
//        setEmptyView(activityLists.size(), emptyTv);
    }

    private void initBanner() {
        banner.setImageLoader(new GlideImageLoader());
        imgs.clear();
        titles.clear();
        for (int i = 0; i < hotActLists.size(); i++) {
            imgs.add(PIC_BASE_URL+hotActLists.get(i).getPicURL());
            titles.add(activityLists.get(i).getActivityTheme());
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
        SharedPreferences loginShare = mContext.getSharedPreferences("login",
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
                                    hotActLists.clear();
                                    hotActLists.addAll(activityBean.getData().getHotActivityList());
                                    activityLists.addAll(activityBean.getData().getAllActivityList());
                                    initBanner();
                                    // Toast.makeText(getActivity(), "已刷新",
                                    // Toast.LENGTH_SHORT).show();
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext,
                                            "" + activityBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext,
                                            "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext,
                                    "数据异常");
                        }
                        actRvAdapter.notifyDataSetChanged();
//                        swipeRefreshLayout.setRefreshing(false);
//                        setEmptyView(activityLists.size(), emptyTv);
                        MyUtils.complete1(swipeRefreshLayout);
                    }

                    @Override
                    public void onFailure(Call<ActivityBean> arg0,
                                          Throwable throwable) {
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext,
                                "连接失败");
                        actRvAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
//                        setEmptyView(activityLists.size(), emptyTv);
//                        MyUtils.complete(activityLv);

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
                bundle.putSerializable("activitysBean", activityBean.getData().getAllActivityList().get(position - 2));
                actDetailIt.putExtras(bundle);
                startActivity(actDetailIt);
            }
        }

    };



    @Override
    public void OnBannerClick(int position) {
        MyUtils.showToast(mContext,"点击了"+position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent actDetailIt = new Intent(mContext,
                ActivityDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("activitysBean", activityBean.getData().getAllActivityList().get(position));
        actDetailIt.putExtras(bundle);
        startActivity(actDetailIt);
        MyUtils.showToast(mContext,position+"");
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
