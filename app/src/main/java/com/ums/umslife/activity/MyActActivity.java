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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
import com.ums.umslife.adapter.ActivityListAdapter;
import com.ums.umslife.bean.ActivityBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyActActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener2<ListView> {
	private Context mContext;
	private PullToRefreshListView lvActivityList;
	private ActivityListAdapter adapter;
	private List<ActivityBean.DataBean.AllActivityListBean> activityLists = new ArrayList<>();
	private String phone;
	private TextView emptyTv;
	public static final int MIN_CLICK_DELAY_TIME = 1000;
	private long lastClickTime = 0;
	private ActivityBean activityBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_act);
		mContext = this;
		setBackBtn();
		setTitle("我的活动");
		init();
		initData();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}

	private void init() {
		lvActivityList = (PullToRefreshListView) findViewById(R.id.lv_may_act);
		emptyTv = (TextView) findViewById(R.id.empty_tv);
		lvActivityList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lvActivityList.setOnRefreshListener(this);
		adapter = new ActivityListAdapter(activityLists,mContext);
		lvActivityList.setAdapter(adapter);
		lvActivityList.setOnItemClickListener(menuItemClickListener);
		setEmptyView(activityLists.size(), emptyTv);
	}

	private void initData() {
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
	}

	private void loadData() {
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
									MyUtils.showToast(mContext, ""+activityBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext, "数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext,"数据异常");
						}

						adapter.notifyDataSetChanged();
						setEmptyView(activityLists.size(), emptyTv);
						MyUtils.complete(lvActivityList);
					}

					@Override
					public void onFailure(Call<ActivityBean> arg0,
										  Throwable throwable) {
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"连接失败");

						adapter.notifyDataSetChanged();
						setEmptyView(activityLists.size(), emptyTv);
						MyUtils.complete(lvActivityList);
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
				Intent actDetailIt = new Intent(mContext,
						ActivityDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("activitysBean", activityBean.getData().getAllActivityList()
						.get(position - 1));
				actDetailIt.putExtras(bundle);
				startActivity(actDetailIt);
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
