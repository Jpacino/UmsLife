package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.ums.umslife.R;
import com.ums.umslife.bean.LoginResponseBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {
	private String resCode = "";
	private boolean  isAuto;
	private String phone = "", user_pwd = "";
	private LoginResponseBean loginResponseBean;
	private Context mContext;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (resCode.equals(MyAppConfig.SUCCESS_CODE)) {
				MyUtils.startAct(mContext, MainActivity.class);
				finish();
				overridePendingTransition(R.anim.slide_right_to_middle,
						R.anim.slide_middle_to_left);
			}else {
				Intent mainIt = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(mainIt);
				finish();
				overridePendingTransition(R.anim.slide_right_to_middle,
						R.anim.slide_middle_to_left);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		initView();
		loadData();
	}

	private void initView() {
		ImageView logoIv = (ImageView) findViewById(R.id.welcome_urovo_logo_iv);
		AlphaAnimation anim = new AlphaAnimation(0, 1);
		anim.setDuration(2500);
		logoIv.startAnimation(anim);
		mHandler.sendEmptyMessageDelayed(1, 3000);// 延迟两秒发消息跳转mainactivity
		initData();
	}

	private void initData() {
		SharedPreferences autoShare = getSharedPreferences("auto", Context.MODE_PRIVATE);
		isAuto = autoShare.getBoolean("autoLogin", false);
		SharedPreferences loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");
		user_pwd = loginShare.getString("user_pwd", "");
	}

	private void loadData() {
		if (isAuto && !phone.isEmpty()) {
			HttpUtils.init().getLoginResponseBean(phone, user_pwd).enqueue(new Callback<LoginResponseBean>() {
				
				@Override
				public void onResponse(Call<LoginResponseBean> arg0,
									   Response<LoginResponseBean> response) {
					loginResponseBean = response.body();
					if (loginResponseBean != null) {
						resCode = loginResponseBean.getCode();
					}
					
				}
				
				@Override
				public void onFailure(Call<LoginResponseBean> arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(1);
	}
}
