package com.ums.umslife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ums.umslife.R;
import com.ums.umslife.bean.ChangePwdBean;
import com.ums.umslife.bean.SendMsgBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.CountDownUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.view.LabelTextView;
import com.ums.umslife.view.SuccinctProgress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {

	private CountDownUtils countDown;
	private SharedPreferences loginShare;
	private String phone;
	private String user_pwd;
	private SendMsgBean sendMsgBean;
	private Context mContext = this;
	private EditText pwdEt, pwdAgainEt, verCodeEt;
	private ChangePwdBean changePwdBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		setBackBtn();
		setTitle("密码修改");
		init();
		initData();
	}

	private void init() {
		Button bt_send_auth_code = (Button) findViewById(R.id.bt_send_auth_code);
		Button changePwdBtn = (Button) findViewById(R.id.bt_change_password);
		LabelTextView pwdLtv = (LabelTextView) findViewById(R.id.register_set_password);
		LabelTextView pwdAgainLtv = (LabelTextView) findViewById(R.id.register_password_again);
		LabelTextView verCodeLtv = (LabelTextView) findViewById(R.id.verCode_et);
		pwdEt = pwdLtv.getEditText();
		pwdAgainEt = pwdAgainLtv.getEditText();
		verCodeEt = verCodeLtv.getEditText();
		// 倒计时工具
		countDown = new CountDownUtils(ChangePasswordActivity.this,
				bt_send_auth_code);
		bt_send_auth_code.setOnClickListener(this);
		changePwdBtn.setOnClickListener(this);
	}

	private void initData() {

		loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
		phone = loginShare.getString("phone", "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send_auth_code:
			sendMsg();
			break;
		case R.id.bt_change_password:
			changePwd();
			break;

		default:
			break;
		}
	}

	private void sendMsg() {
		countDown.startCountDown();
		HttpUtils.init().getSendMsgBean(phone)
				.enqueue(new Callback<SendMsgBean>() {

					@Override
					public void onResponse(Call<SendMsgBean> arg0,
										   Response<SendMsgBean> response) {
						sendMsgBean = response.body();

						if (sendMsgBean != null) {
							switch (sendMsgBean.getCode()) {
								case MyAppConfig.SUCCESS_CODE:
									Toast.makeText(mContext,
											sendMsgBean.getReason(),
											Toast.LENGTH_SHORT).show();
									break;
								case MyAppConfig.DEFEAT_CODE:
									Toast.makeText(mContext,
											"" + sendMsgBean.getReason(),
											Toast.LENGTH_SHORT).show();
									break;
								default:
									Toast.makeText(mContext, "数据异常",
											Toast.LENGTH_SHORT).show();
									break;
							}
						} else {
							Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT)
									.show();
						}
					}

					@Override
					public void onFailure(Call<SendMsgBean> arg0,
										  Throwable throwable) {
						Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT)
								.show();

					}
				});
	}

	private void changePwd() {
		int size = pwdEt.getText().toString().trim().length();
		if (size >= 3) {
			if (pwdEt.getText().toString().trim()
					.equals(pwdAgainEt.getText().toString().trim())) {
				if (!verCodeEt.getText().toString().isEmpty()) {
					SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
							SuccinctProgress.THEME_LINE, false, false);
					user_pwd = pwdEt.getText().toString().trim();
					String verCode = verCodeEt.getText().toString().trim();
					Log.d(MyAppConfig.TAG, "phone" + phone);
					Log.d(MyAppConfig.TAG, "verCode" + verCode);
					Log.d(MyAppConfig.TAG, "user_pwd" + user_pwd);
					HttpUtils.init().getChangePwdBean(phone, verCode, user_pwd)
							.enqueue(new Callback<ChangePwdBean>() {
								@Override
								public void onResponse(
										Call<ChangePwdBean> arg0,
										Response<ChangePwdBean> response) {
									changePwdBean = response.body();
									Log.d(MyAppConfig.TAG, "reason"
											+ changePwdBean.getReason());
									Log.d(MyAppConfig.TAG, "Code"
											+ changePwdBean.getCode());
									if (changePwdBean != null) {
										if (changePwdBean.getCode().equals(
												MyAppConfig.SUCCESS_CODE)) {
											Editor editor = loginShare.edit();
											editor.putString("phone", phone);
											editor.putString("user_pwd",
													user_pwd);
											editor.apply();
											finish();
											Toast.makeText(mContext,
													changePwdBean.getReason(),
													Toast.LENGTH_SHORT).show();
										} else if (changePwdBean
												.getCode()
												.equals(MyAppConfig.DEFEAT_CODE)) {
											Toast.makeText(
													mContext,
													""
															+ changePwdBean
																	.getReason(),
													Toast.LENGTH_SHORT).show();
										} else if (changePwdBean.getCode()
												.equals(MyAppConfig.TWO_CODE)) {
											Toast.makeText(
													mContext,
													""
															+ changePwdBean
																	.getReason(),
													Toast.LENGTH_SHORT).show();
										} else if (changePwdBean.getCode()
												.equals(MyAppConfig.THREE_CODE)) {
											Toast.makeText(
													mContext,
													""
															+ changePwdBean
																	.getReason(),
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(mContext, "数据异常",
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(mContext, "数据异常",
												Toast.LENGTH_SHORT).show();
									}
									SuccinctProgress.dismiss();
								}

								@Override
								public void onFailure(Call<ChangePwdBean> arg0,
													  Throwable arg1) {
									SuccinctProgress.dismiss();
									Toast.makeText(mContext, "数据异常",
											Toast.LENGTH_SHORT).show();

								}
							});
				} else {
					Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(mContext, "两次密码输入不相同", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(mContext, "至少输入3位数密码", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		countDown.onDestroy();
		super.onDestroy();
	}

}
