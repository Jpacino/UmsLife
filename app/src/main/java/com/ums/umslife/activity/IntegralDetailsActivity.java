package com.ums.umslife.activity;

import android.content.Context;
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
import com.ums.umslife.adapter.IntegralRvAdapter;
import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.bean.IntegralBean;
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

public class IntegralDetailsActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_integral)
    RecyclerView rvIntegral;
    @BindView(R.id.refresh_integral)
    SwipeRefreshLayout refreshIntegral;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    private Context mContext;
    private List<IntegralBean.IntegralsBean> integralLists = new ArrayList<>();
    private String phone;
    private IntegralBean integralBean;
    private IntegralRvAdapter integralRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_details);
        ButterKnife.bind(this);

        init();
        initView();
        initData();
    }

    private void init() {
        mContext = this;
        setBackBtn();
        setTitle("积分明细");
    }

    private void initView() {
        refreshIntegral.setColorSchemeColors(mContext.getResources().getColor(R.color.red_main));
        integralRvAdapter = new IntegralRvAdapter(R.layout.item_integral_list, integralLists);
        integralRvAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvIntegral.setLayoutManager(new LinearLayoutManager(mContext));
        rvIntegral.setAdapter(integralRvAdapter);
        refreshIntegral.setOnRefreshListener(this);
        integralRvAdapter.setOnItemClickListener(this);
        setEmptyView(integralLists.size(), llEmpty);
    }

    private void initData() {
        SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        loadNetData();
    }

    /**
     * 加载网络数据
     */
    private void loadNetData() {
        HttpUtils.init().getIntegralBean(phone)
                .enqueue(new Callback<IntegralBean>() {

                    @Override
                    public void onResponse(Call<IntegralBean> arg0,
                                           Response<IntegralBean> response) {
                        integralBean = response.body();
                        if (integralBean != null) {
                            switch (integralBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    integralLists.clear();
                                    integralLists.addAll(integralBean.getData());
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext, "" + integralBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }

                        integralRvAdapter.notifyDataSetChanged();
                        setEmptyView(integralLists.size(), llEmpty);
                        MyUtils.complete(refreshIntegral);
                    }

                    @Override
                    public void onFailure(Call<IntegralBean> arg0,
                                          Throwable throwable) {
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext, "连接失败");
                        integralRvAdapter.notifyDataSetChanged();
                        setEmptyView(integralLists.size(), llEmpty);
                        MyUtils.complete(refreshIntegral);

                    }
                });

    }

    /**
     * @param size 列表数目
     * @param v 展示的视图
     *
     *          设置空数据时的视图
     */
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

    }
}
