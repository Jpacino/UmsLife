package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ums.umslife.R;
import com.ums.umslife.adapter.ActRvAdapter;
import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_my_act)
    RecyclerView rvMyAct;
    @BindView(R.id.refresh_act)
    SwipeRefreshLayout refreshAct;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private Context mContext;
    private List<ActivityBean.DataBean.AllActivityListBean> activityLists = new ArrayList<>();
    private String phone;
    private ActivityBean activityBean;
    private ActRvAdapter actRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_act);
        ButterKnife.bind(this);
        init();
        initView();
        initData();
    }


    protected void init() {
        mContext = this;
        setBackBtn();
        setTitle("我的活动");
    }

    protected void initView() {
        actRvAdapter = new ActRvAdapter(R.layout.item_activity_list, activityLists);
        actRvAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        refreshAct.setColorSchemeColors(mContext.getResources().getColor(R.color.red_main));
        rvMyAct.setLayoutManager(new LinearLayoutManager(mContext));
        rvMyAct.setAdapter(actRvAdapter);
        actRvAdapter.setOnItemClickListener(this);
        refreshAct.setOnRefreshListener(this);
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
        setEmptyView(activityLists.size(), tvEmpty);
    }


    protected void initData() {
        SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        loadNetData();
    }

    private void loadNetData() {
        HttpUtils.init().getActivityBean(phone, "1")
                .enqueue(new Callback<ActivityBean>() {
                    @Override
                    public void onResponse(Call<ActivityBean> arg0,
                                           Response<ActivityBean> response) {
                        activityBean = response.body();
                        if (activityBean != null) {
                            switch (activityBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    activityLists.clear();
                                    activityLists.addAll(activityBean.getData().getAllActivityList());
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext, "" + activityBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }

                        actRvAdapter.notifyDataSetChanged();
                        setEmptyView(activityLists.size(), tvEmpty);
                        MyUtils.complete(refreshAct);
                    }

                    @Override
                    public void onFailure(Call<ActivityBean> arg0,
                                          Throwable throwable) {
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext, "连接失败");

                        actRvAdapter.notifyDataSetChanged();
                        setEmptyView(activityLists.size(), tvEmpty);
                        MyUtils.complete(refreshAct);
                    }
                });

    }


    private void setEmptyView(int size, View v) {
        if (size == 0) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRefresh() {
        loadNetData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent actDetailIt = new Intent(mContext,
                ActivityDetailsActivity.class);
        actDetailIt.putExtra("code",MyAppConfig.ALL_ACT_CODE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("allActListBean", activityBean.getData().getAllActivityList()
                .get(position));
        actDetailIt.putExtras(bundle);
        startActivity(actDetailIt);
    }
}
