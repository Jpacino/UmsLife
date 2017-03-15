package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
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

public class MyClubActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener2<ListView> {
	private Context mContext;
	private PullToRefreshListView clubLv;
	private ClubListAdapter adapter;
	private List<ClubBean.ClubsBean> clubLists = new ArrayList<>();
	private String phone;
	private TextView emptyTv;
	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;
	private ClubBean clubBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_club);
		mContext = this;
		setBackBtn();
		setTitle("我的俱乐部");
		init();
		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadData();
	}

	private void init() {
		clubLv = (PullToRefreshListView) findViewById(R.id.lv_may_club);
		emptyTv = (TextView) findViewById(R.id.empty_tv);
		clubLv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		clubLv.setOnRefreshListener(this);
		adapter = new ClubListAdapter(clubLists, mContext);
		clubLv.setAdapter(adapter);
		clubLv.setOnItemClickListener(menuItemClickListener);
		setEmptyView(clubLists.size(), emptyTv);
	}

	private void initData() {
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
	}

	private void loadData() {
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
