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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ums.umslife.R;
import com.ums.umslife.adapter.ClubRvAdapter;
import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.bean.ClubBean;
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

public class MyClubActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_club)
    RecyclerView rvClub;
    @BindView(R.id.refresh_club)
    SwipeRefreshLayout refreshClub;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    private Context mContext;
    private List<ClubBean.ClubsBean> clubLists = new ArrayList<>();
    private String phone;
    private ClubBean clubBean;
    private ClubRvAdapter clubRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_club);
        ButterKnife.bind(this);
        init();
        initView();
        initData();
    }


    protected void init() {
        mContext = this;
        setBackBtn();
        setTitle("我的俱乐部");
    }


    protected void initView() {
        refreshClub.setColorSchemeColors(mContext.getResources().getColor(R.color.red_main));
        clubRvAdapter = new ClubRvAdapter(R.layout.item_club_list, clubLists);
        clubRvAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvClub.setLayoutManager(new LinearLayoutManager(mContext));
        rvClub.setAdapter(clubRvAdapter);
        refreshClub.setOnRefreshListener(this);
        clubRvAdapter.setOnItemClickListener(this);
        setEmptyView(clubLists.size(), llEmpty);
    }


    protected void initData() {
        SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        loadNetData();
    }

    private void loadNetData() {
        HttpUtils.init().getClubBean(phone, "1")
                .enqueue(new Callback<ClubBean>() {

                    @Override
                    public void onResponse(Call<ClubBean> call,
                                           Response<ClubBean> response) {
                        clubBean = response.body();
                        if (clubBean != null) {
                            switch (clubBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    clubLists.clear();
                                    clubLists.addAll(clubBean.getData());
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext, "" + clubBean.getReason());

                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }

                        clubRvAdapter.notifyDataSetChanged();
                        setEmptyView(clubLists.size(), llEmpty);
                        // SuccinctProgress.dismiss();
                        MyUtils.complete(refreshClub);

                    }

                    @Override
                    public void onFailure(Call<ClubBean> call,
                                          Throwable throwable) {
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext, "连接失败");
                        clubRvAdapter.notifyDataSetChanged();
                        setEmptyView(clubLists.size(), llEmpty);
                        MyUtils.complete(refreshClub);

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
        Intent clubDetailIt = new Intent(mContext,
                ClubDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clubsBean",
                clubBean.getData().get(position));
        clubDetailIt.putExtras(bundle);
        startActivity(clubDetailIt);
    }
}
