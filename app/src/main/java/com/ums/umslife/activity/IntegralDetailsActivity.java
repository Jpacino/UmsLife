package com.ums.umslife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
import com.ums.umslife.adapter.IntegralListAdapter;
import com.ums.umslife.bean.IntegralBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntegralDetailsActivity extends BaseActivity implements
		OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {
	private Context mContext;
	private PullToRefreshListView integralLv;
	private IntegralListAdapter adapter;
	private List<IntegralBean.IntegralsBean> integralLists = new ArrayList<>();
	private String phone;
	private TextView emptyTv;
	private IntegralBean integralBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_details);

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
		integralLv = (PullToRefreshListView) findViewById(R.id.lv_may_act);
		emptyTv = (TextView) findViewById(R.id.empty_tv);
		integralLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		integralLv.setOnRefreshListener(this);
		adapter = new IntegralListAdapter(integralLists);
		integralLv.setAdapter(adapter);
		setEmptyView(integralLists.size(), emptyTv);
	}

	private void initData() {
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
		loadData();
	}

	private void loadData() {
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
									MyUtils.showToast(mContext,"" + integralBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext,"数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext,"数据异常");
						}

						adapter.notifyDataSetChanged();
						setEmptyView(integralLists.size(), emptyTv);
						MyUtils.complete(integralLv);
					}

					@Override
					public void onFailure(Call<IntegralBean> arg0,
										  Throwable throwable) {
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"连接失败");
						adapter.notifyDataSetChanged();
						setEmptyView(integralLists.size(), emptyTv);
						MyUtils.complete(integralLv);

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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

}
