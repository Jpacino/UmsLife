package com.ums.umslife.fragment;

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
import com.ums.umslife.activity.ClubDetailsActivity;
import com.ums.umslife.adapter.ClubRvAdapter;
import com.ums.umslife.base.BaseFragment;
import com.ums.umslife.bean.ClubBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{

	private ClubRvAdapter clubRvAdapter;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView clubRv;
	private TextView emptyTv;
	private Context mContext;
	private ClubBean clubBean;
	private List<ClubBean.ClubsBean> clubLists = new ArrayList<>();

	@Override
	protected int getContentLayoutRes() {
		return R.layout.fragment_club;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadNetData();
	}

	@Override
	protected void initView(View childView) {
		mContext = getActivity();
		emptyTv = (TextView) childView.findViewById(R.id.tv_empty);
		clubRv = (RecyclerView) childView.findViewById(R.id.rv_club);
		swipeRefreshLayout = (SwipeRefreshLayout) childView.findViewById(R.id.refresh_club);
		swipeRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.red_main));
		clubRvAdapter = new ClubRvAdapter(R.layout.item_club_list,clubLists);
		clubRvAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
		clubRv.setLayoutManager(new LinearLayoutManager(mContext));
		clubRv.setAdapter(clubRvAdapter);
		swipeRefreshLayout.setOnRefreshListener(this);
		clubRvAdapter.setOnItemClickListener(this);
		setEmptyView(clubLists.size(), emptyTv);
	}

	private void loadNetData() {
		SharedPreferences loginShare = mContext.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String phone = loginShare.getString("phone", "");
		HttpUtils.init().getClubBean(phone, "0")
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
									MyUtils.showToast(mContext,""+clubBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext,"数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext,"数据异常");
						}

						clubRvAdapter.notifyDataSetChanged();
						setEmptyView(clubLists.size(), emptyTv);
						MyUtils.complete(swipeRefreshLayout);

					}

					@Override
					public void onFailure(Call<ClubBean> call,
										  Throwable throwable) {
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"连接失败");
						clubRvAdapter.notifyDataSetChanged();
						setEmptyView(clubLists.size(), emptyTv);
						MyUtils.complete(swipeRefreshLayout);

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
	public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		Intent clubDetailIt = new Intent(mContext,
				ClubDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("clubsBean",
				clubBean.getData().get(position));
		clubDetailIt.putExtras(bundle);
		startActivity(clubDetailIt);
	}

	@Override
	public void onRefresh() {
		loadNetData();
	}
}
