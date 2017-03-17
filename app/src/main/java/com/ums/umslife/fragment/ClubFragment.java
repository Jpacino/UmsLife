package com.ums.umslife.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.ums.umslife.activity.ClubDetailsActivity;
import com.ums.umslife.adapter.ClubListAdapter;
import com.ums.umslife.bean.ClubBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubFragment extends BaseFragment implements
		OnRefreshListener2<ListView> {

	private PullToRefreshListView clubLv;
	private TextView emptyTv;
	private Context mContext;
	private ClubBean clubBean;
	private ClubListAdapter adapter;
	private List<ClubBean.ClubsBean> clubLists = new ArrayList<>();
	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;

	@Override
	protected int getContentLayoutRes() {
		return R.layout.fragment_club;
	}

	@Override
	public void onStart() {
		super.onStart();
		loadData();
	}

	@Override
	protected void initView(View childView) {
		mContext = getActivity();
		clubLv = (PullToRefreshListView) childView
				.findViewById(R.id.lv_club_list);
		emptyTv = (TextView) childView.findViewById(R.id.empty_tv);
		clubLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		clubLv.setOnRefreshListener(this);
		adapter = new ClubListAdapter(clubLists, mContext);
		clubLv.setAdapter(adapter);
		clubLv.setOnItemClickListener(menuItemClickListener);
		setEmptyView(clubLists.size(), emptyTv);
	}

	private void loadData() {
		SharedPreferences loginShare = getActivity().getSharedPreferences("login",
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

						adapter.notifyDataSetChanged();
						setEmptyView(clubLists.size(), emptyTv);
						// SuccinctProgress.dismiss();
						MyUtils.complete(clubLv);

					}

					@Override
					public void onFailure(Call<ClubBean> call,
										  Throwable throwable) {
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"连接失败");
						adapter.notifyDataSetChanged();
						setEmptyView(clubLists.size(), emptyTv);
						MyUtils.complete(clubLv);

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

	private OnItemClickListener menuItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
				lastClickTime = currentTime;
				Intent clubDetailIt = new Intent(mContext,
						ClubDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("clubsBean",
						clubBean.getData().get(position - 1));
				clubDetailIt.putExtras(bundle);
				startActivity(clubDetailIt);
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
