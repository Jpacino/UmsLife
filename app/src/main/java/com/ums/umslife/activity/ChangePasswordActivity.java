package com.ums.umslife.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ums.umslife.R;
import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.bean.ChangePwdBean;
import com.ums.umslife.bean.SendMsgBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.CountDownUtils2;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.SuccinctProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements
        OnClickListener {

    @BindView(R.id.et_new_pwd)
    MaterialEditText etNewPwd;
    @BindView(R.id.et_new_pwd_again)
    MaterialEditText etNewPwdAgain;
    @BindView(R.id.et_verification)
    MaterialEditText etVerification;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.btn_change_pwd)
    Button btnChangePwd;
    private CountDownUtils2 countDown;
    private SharedPreferences loginShare;
    private String phone;
    private String user_pwd;
    private SendMsgBean sendMsgBean;
    private Context mContext;
    private ChangePwdBean changePwdBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        init();
        initView();
        initData();
    }


    protected void init() {
        mContext = this;
        setBackBtn();
        setTitle("密码修改");
    }


    protected void initView() {
        countDown = new CountDownUtils2(ChangePasswordActivity.this,
                btnSendCode);

    }


    protected void initData() {
        loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
    }


    /**
     * 发送验证码
     */
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
                                    MyUtils.showToast(mContext, "" + sendMsgBean.getReason());
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext, "" + sendMsgBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }
                        countDown.onDestroy();
                        btnSendCode.setText("发送");
                        btnSendCode.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<SendMsgBean> arg0,
                                          Throwable throwable) {
                        countDown.onDestroy();
                        btnSendCode.setText("发送");
                        btnSendCode.setEnabled(true);
                        MyUtils.showToast(mContext, "连接失败");
                    }
                });
    }

    /**
     * 开始修改密码
     */
    private void changePwd() {
        int size = etNewPwd.getText().toString().trim().length();
        if (size >= 3) {
            if (etNewPwd.getText().toString().trim()
                    .equals(etNewPwdAgain.getText().toString().trim())) {
                if (!etVerification.getText().toString().isEmpty()) {
                    SuccinctProgress.showSuccinctProgress(mContext, "请稍后...",
                            SuccinctProgress.THEME_LINE, false, false);
                    user_pwd = etNewPwd.getText().toString().trim();
                    String verCode = etVerification.getText().toString().trim();
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
                                            MyUtils.showToast(mContext, "" + changePwdBean.getReason());
                                        } else if (changePwdBean
                                                .getCode()
                                                .equals(MyAppConfig.DEFEAT_CODE)) {
                                            MyUtils.showToast(mContext, "" + changePwdBean.getReason());
                                        } else if (changePwdBean.getCode()
                                                .equals(MyAppConfig.TWO_CODE)) {
                                            MyUtils.showToast(mContext, "" + changePwdBean.getReason());
                                        } else if (changePwdBean.getCode()
                                                .equals(MyAppConfig.THREE_CODE)) {
                                            MyUtils.showToast(mContext, "" + changePwdBean.getReason());
                                        } else {
                                            MyUtils.showToast(mContext, "数据异常");
                                        }
                                    } else {
                                        MyUtils.showToast(mContext, "数据异常");
                                    }
                                    SuccinctProgress.dismiss();
                                }

                                @Override
                                public void onFailure(Call<ChangePwdBean> arg0,
                                                      Throwable arg1) {
                                    SuccinctProgress.dismiss();
                                    MyUtils.showToast(mContext, "连接失败");

                                }
                            });
                } else {
                    MyUtils.showToast(mContext, "请输入验证码");
                }
            } else {
                MyUtils.showToast(mContext, "两次密码输入不相同");
            }
        } else {
            MyUtils.showToast(mContext, "至少输入3位数密码");
        }
    }

    @Override
    protected void onDestroy() {
        countDown.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.btn_send_code, R.id.btn_change_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                sendMsg();
                break;
            case R.id.btn_change_pwd:
                changePwd();
                break;
        }
    }
}
