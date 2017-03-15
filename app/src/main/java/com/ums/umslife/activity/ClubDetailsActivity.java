package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ums.umslife.R;
import com.ums.umslife.adapter.ClubMemberListAdapter;
import com.ums.umslife.bean.ClubApplyBean;
import com.ums.umslife.bean.ClubBean;
import com.ums.umslife.bean.ClubDetailBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.SuccinctProgress;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubDetailsActivity extends BaseActivity implements
		OnClickListener {

	private TextView clubNameTv;
	private TextView memberTv;
	private TextView synopsisTv;
	private TextView applyTv;
	private String phone, clubNo, applyState, applyState_out;
	private ClubDetailBean clubDetailBean;
	private ClubApplyBean clubApplyBean;
	private final static String IS_JOIN = "1";
	private final static String CHECKING = "2";
	private final static String CHECK_NOT_PASS = "3";
	private Context mContext = this;
	private List<ClubDetailBean.ClubDetailsBean.ClubUserListBean> clubUserLists = new ArrayList<>();
	private ClubMemberListAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_details);
		setBackBtn();
		setTitle("俱乐部详情");
		init();
		initData();
	}

	private void init() {
		ListView lvClubMemberList = (ListView) findViewById(R.id.lv_club_member_list);
		clubNameTv = (TextView) findViewById(R.id.club_name_tv);
		TextView creatTimeTv = (TextView) findViewById(R.id.createTime_tv);
		memberTv = (TextView) findViewById(R.id.member_tv);
		synopsisTv = (TextView) findViewById(R.id.synopsis_tv);
		applyTv = (TextView) findViewById(R.id.apply_tv);
		applyTv.setOnClickListener(this);
		adapter = new ClubMemberListAdapter(clubUserLists);
		lvClubMemberList.setAdapter(adapter);
	}

	private void initData() {
		Intent clubDetailIt = getIntent();
		ClubBean.ClubsBean clubsBean = (ClubBean.ClubsBean) clubDetailIt.getSerializableExtra("clubsBean");
		applyState = clubsBean.getApplyState();
		clubNo = clubsBean.getClubNo();
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
		clubNameTv.setText(clubsBean.getClubName());
		memberTv.setText(clubsBean.getMember());
		synopsisTv.setText(clubsBean.getSynopsis());
		initState();
		HttpUtils.init().getClubDetailBean(phone, clubNo)
				.enqueue(new Callback<ClubDetailBean>() {
					@Override
					public void onResponse(Call<ClubDetailBean> arg0,
										   Response<ClubDetailBean> response) {
						clubDetailBean = response.body();
						if (clubDetailBean != null) {
							switch (clubDetailBean.getCode()) {
								case MyAppConfig.SUCCESS_CODE:
									clubUserLists.clear();
									clubUserLists.addAll(clubDetailBean.getData()
											.getClubUserList());

									break;
								case MyAppConfig.DEFEAT_CODE:
									MyUtils.showToast(mContext,"" + clubDetailBean.getReason());
									break;
								case MyAppConfig.TWO_CODE:
									MyUtils.showToast(mContext,"" + clubDetailBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext,"数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext,"数据异常");
						}
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Call<ClubDetailBean> arg0,
										  Throwable throwable) {
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"数据异常");
					}
				});
	}

	private void initState() {
		if (applyState.isEmpty()) {
			applyState_out = "0";
			applyTv.setText("加入");
			applyTv.setClickable(true);
			applyTv.setBackgroundResource(R.drawable.bt_count_down_bg_selector);
		} else if (applyState.equals(IS_JOIN)) {
			applyState_out = "1";
			applyTv.setText("已加入");
			applyTv.setClickable(false);
			applyTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
		} else if (applyState.equals(CHECKING)) {
			applyState_out = "";
			applyTv.setText("审核中");
			applyTv.setClickable(false);
			applyTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
		} else if (applyState.equals(CHECK_NOT_PASS)) {
			applyState_out = "0";
			applyTv.setText("未通过");
			applyTv.setClickable(false);
			applyTv.setBackgroundResource(R.drawable.bt_login_unable_shape);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_base_back:
			finish();
			break;
		case R.id.apply_tv:
			apply();
			break;

		default:
			break;
		}

	}

	private void apply() {
		SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
				SuccinctProgress.THEME_LINE, false, false);
		HttpUtils.init().getClubApplyBean(phone, clubNo, applyState_out)
				.enqueue(new Callback<ClubApplyBean>() {

					@Override
					public void onResponse(Call<ClubApplyBean> arg0,
										   Response<ClubApplyBean> response) {
						clubApplyBean = response.body();
						Log.d(MyAppConfig.TAG, "clubApplyBean"+clubApplyBean);
						if (clubApplyBean != null) {
							switch (clubApplyBean.getCode()) {
								case MyAppConfig.SUCCESS_CODE:
									applyState = clubApplyBean.getData()
											.getApplyState();
									MyUtils.showToast(mContext,"" + clubApplyBean.getReason());
									break;
								case MyAppConfig.DEFEAT_CODE:
									MyUtils.showToast(mContext,"" + clubApplyBean.getReason());
									break;
								case MyAppConfig.TWO_CODE:
									MyUtils.showToast(mContext,"" + clubApplyBean.getReason());
									break;
								case MyAppConfig.THREE_CODE:
									MyUtils.showToast(mContext,"" + clubApplyBean.getReason());
									break;
								default:
									MyUtils.showToast(mContext,"数据异常");
									break;
							}
						} else {
							MyUtils.showToast(mContext,"数据异常");
						}
						SuccinctProgress.dismiss();
						initState();
					}

					@Override
					public void onFailure(Call<ClubApplyBean> arg0,
										  Throwable throwable) {
						SuccinctProgress.dismiss();
						initData();
						Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
						MyUtils.showToast(mContext,"连接失败");
					}
				});
	}


}
