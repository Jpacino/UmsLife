package com.ums.umslife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ums.umslife.R;
import com.ums.umslife.bean.LoginResponseBean;
import com.ums.umslife.bean.SendMsgBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.CountDownUtils;
import com.ums.umslife.utils.MyAppConfig;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.utils.NetHelper;
import com.ums.umslife.view.SuccinctProgress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.countDown;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private Button btSendAuthCode;
    private CountDownUtils countDownUtils;
    private TextView loginTypeTv;
    private ImageView autoLoginIv;
    private LinearLayout autoLoginLl;
    private boolean isOpen = false, isAuto;
    private MaterialEditText accountEt, pwdEt;
    private SharedPreferences loginShare;
    private SharedPreferences autoShare;
    private String phone = "", user_pwd = "";
    private long exitTime;
    private int size;
    private LoginResponseBean loginResponseBean;
    private Context mContext;
    private Intent loginIt;
    private SendMsgBean sendMsgBean;
    private TextView forgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initView();
        initData();
    }


    protected void init() {
        mContext = LoginActivity.this;
        loginIt = getIntent();
        noToolbar();
    }


    protected void initView() {
        accountEt = (MaterialEditText) findViewById(R.id.account_et);
        pwdEt = (MaterialEditText) findViewById(R.id.pwd_et);
        btSendAuthCode = (Button) findViewById(R.id.bt_send_auth_code);
        Button loginBtn = (Button) findViewById(R.id.btn_login);
        loginTypeTv = (TextView) findViewById(R.id.tv_login_type);
        autoLoginIv = (ImageView) findViewById(R.id.iv_auto_login);
        autoLoginLl = (LinearLayout) findViewById(R.id.ll_auto_login);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd_tv);
        loginTypeTv.setOnClickListener(this);
        btSendAuthCode.setOnClickListener(this);
        autoLoginIv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
    }


    protected void initData() {
        autoShare = getSharedPreferences("auto", Context.MODE_PRIVATE);
        isAuto = autoShare.getBoolean("autoLogin", false);
        if (loginIt.getStringExtra("isAuto") != null) {
            isAuto = false;
            loadAutoShare(false);
        }
        Log.d(MyAppConfig.TAG, "isAuto: " + isAuto);
        autoLogin(isAuto);
        loginShare = getSharedPreferences("login", Context.MODE_PRIVATE);
        phone = loginShare.getString("phone", "");
        user_pwd = loginShare.getString("user_pwd", "");
        loadLoginData(phone, user_pwd, isAuto);
        countDownUtils = new CountDownUtils(LoginActivity.this, btSendAuthCode);
    }


    private void loadLoginData(String phone, String user_pwd, boolean isAuto) {
        if (isAuto && !phone.isEmpty()) {
            accountEt.setText(phone);
            pwdEt.setText(user_pwd);
            login();
        }
    }

    /**
     * 判断登录方式
     */
    private void changeAuthType(boolean isOpen) {
        if (isOpen) {

            pwdEt.setHint("请输入验证码");
            pwdEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            autoLoginLl.setVisibility(View.INVISIBLE);
            btSendAuthCode.setVisibility(View.VISIBLE);
            loginTypeTv.setText("密码登录");
            pwdEt.setIconLeft(R.drawable.verification);
            pwdEt.setFloatingLabelText("验证码");
        } else {
            pwdEt.setHint("请输入密码");
            pwdEt.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btSendAuthCode.setVisibility(View.GONE);
            loginTypeTv.setText("验证码登录");
            autoLoginLl.setVisibility(View.VISIBLE);
            pwdEt.setIconLeft(R.drawable.password);
            pwdEt.setFloatingLabelText("密码");
        }

    }

    /**
     * 自动登录开关
     *
     * @param isAuto 自动登录是否
     */
    private void autoLogin(boolean isAuto) {
            if (isAuto) {
                autoLoginIv.setImageResource(R.drawable.is_choose1);


            } else {
            autoLoginIv.setImageResource(R.drawable.not_choose1);
        }
    }

    private void loadLoginShare(String phone, String user_pwd) {
        Editor editor = loginShare.edit();
        editor.putString("phone", phone);
        editor.putString("user_pwd", user_pwd);
        editor.apply();
    }

    private void loadAutoShare(boolean isAuto) {
        Editor editor = autoShare.edit();
        editor.putBoolean("autoLogin", isAuto);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_auth_code:
                size = accountEt.getText().toString().trim().length();
                if (size == 11) {
                    sendMsg();
                } else {
                    MyUtils.showToast(mContext, "请输入11位手机号");
                }
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_login_type:
                isOpen = !isOpen;
                pwdEt.setText("");
                changeAuthType(isOpen);

                break;
            case R.id.iv_auto_login:
                isAuto = !isAuto;
                autoLogin(isAuto);
                break;
            case R.id.forget_pwd_tv:
//                MyUtils.startAct(mContext,ChangePasswordActivity.class);
                MyUtils.startAct(mContext,MainActivity.class);
                break;

            default:
                break;
        }
    }

    /**
     * 发送验证接口
     */
    private void sendMsg() {
        countDownUtils.startCountDown();
        phone = accountEt.getText().toString();
        HttpUtils.init().getSendMsgBean(phone)
                .enqueue(new Callback<SendMsgBean>() {
                    @Override
                    public void onResponse(Call<SendMsgBean> arg0,
                                           Response<SendMsgBean> response) {
                        sendMsgBean = response.body();

                        if (sendMsgBean != null) {
                            switch (sendMsgBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    MyUtils.showToast(mContext,
                                            "" + sendMsgBean.getReason());
                                    break;
                                case MyAppConfig.DEFEAT_CODE:

                                    MyUtils.showToast(mContext,
                                            "" + sendMsgBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }
                        countDownUtils.onDestroy();
                        btSendAuthCode.setText("发送");
                        btSendAuthCode.setEnabled(true);

                    }

                    @Override
                    public void onFailure(Call<SendMsgBean> arg0,
                                          Throwable throwable) {
                        countDownUtils.onDestroy();
                        btSendAuthCode.setText("发送");
                        btSendAuthCode.setEnabled(true);
                        MyUtils.showToast(mContext, "连接失败");

                    }
                });
    }

    /**
     * 判断登录方式
     */
    private void login() {
        size = accountEt.getText().toString().trim().length();
        if (NetHelper.IsHaveInternet(this)) { // 判断是否联网

            if (!isOpen) {
                if (size == 11) {
                    if (pwdEt.getText().toString().isEmpty()) {
                        MyUtils.showToast(mContext, "请输入密码");
                    } else {
                        pwdLogin();
                    }
                } else {
                    MyUtils.showToast(mContext, "请输入11位手机号");
                }
            } else {
                if (size == 11) {
                    if (pwdEt.getText().toString().isEmpty()) {
                        MyUtils.showToast(mContext, "请输入验证码");
                    } else {
                        msgLogin();
                    }
                } else {
                    MyUtils.showToast(mContext, "请输入11位手机号");
                }
            }
        } else {
            MyUtils.showToast(mContext, "网络未连接，请重试");
        }

    }

    /**
     * 验证码登录
     */
    private void msgLogin() {
        SuccinctProgress.showSuccinctProgress(mContext, "正在登录···",
                SuccinctProgress.THEME_ARC, false, false);
        phone = accountEt.getText().toString();
        user_pwd = pwdEt.getText().toString();
        HttpUtils.init().getLoginResponseBean2(phone, user_pwd)
                .enqueue(new Callback<LoginResponseBean>() {

                    @Override
                    public void onResponse(Call<LoginResponseBean> call,
                                           Response<LoginResponseBean> response) {
                        loginResponseBean = response.body();
                        if (loginResponseBean != null) {
                            if (loginResponseBean.getCode().equals(
                                    MyAppConfig.SUCCESS_CODE)) {
                                Intent mainIt = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("loginResponseBean",
                                        loginResponseBean);
                                mainIt.putExtras(bundle);
                                startActivity(mainIt);
                                finish();
                                overridePendingTransition(
                                        R.anim.slide_right_to_middle,
                                        R.anim.slide_middle_to_left);
                            }
                            SuccinctProgress.dismiss();
                            MyUtils.showToast(mContext,
                                    "" + loginResponseBean.getReason());
                        } else {
                            SuccinctProgress.dismiss();
                            MyUtils.showToast(mContext, "系统异常");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponseBean> call,
                                          Throwable throwable) {
                        SuccinctProgress.dismiss();
                        MyUtils.showToast(mContext, "连接失败");
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());

                    }
                });

    }

    /**
     * 密码登录
     */
    private void pwdLogin() {
        SuccinctProgress.showSuccinctProgress(mContext, "正在登录···",
                SuccinctProgress.THEME_ARC, false, false);
        phone = accountEt.getText().toString();
        user_pwd = pwdEt.getText().toString();
        HttpUtils.init().getLoginResponseBean(phone, user_pwd)
                .enqueue(new Callback<LoginResponseBean>() {

                    @Override
                    public void onResponse(Call<LoginResponseBean> call,
                                           Response<LoginResponseBean> response) {
                        loginResponseBean = response.body();
                        if (loginResponseBean != null) {
                            switch (loginResponseBean.getCode()) {
                                case MyAppConfig.SUCCESS_CODE:
                                    loadLoginShare(phone, user_pwd);
                                    loadAutoShare(isAuto);
                                    Intent mainIt = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("loginResponseBean",
                                            loginResponseBean);
                                    mainIt.putExtras(bundle);
                                    startActivity(mainIt);
                                    finish();
                                    overridePendingTransition(
                                            R.anim.slide_right_to_middle,
                                            R.anim.slide_middle_to_left);
                                    break;
                                case MyAppConfig.DEFEAT_CODE:
                                    MyUtils.showToast(mContext, ""
                                            + loginResponseBean.getReason());
                                    break;
                                default:
                                    MyUtils.showToast(mContext, "数据异常");
                                    break;
                            }
                        } else {
                            MyUtils.showToast(mContext, "数据异常");
                        }
                        SuccinctProgress.dismiss();
                    }

                    @Override
                    public void onFailure(Call<LoginResponseBean> call,
                                          Throwable throwable) {
                        SuccinctProgress.dismiss();
                        Log.d(MyAppConfig.TAG, "异常" + throwable.getMessage());
                        MyUtils.showToast(mContext, "连接失败");
                    }
                });

    }

    /**
     * 重写按钮事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 3000) {
                MyUtils.showToast(mContext, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        countDownUtils.onDestroy();
        super.onDestroy();
    }

}
