package com.ums.umslife.fragment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.activity.ChangePasswordActivity;
import com.ums.umslife.activity.IntegralDetailsActivity;
import com.ums.umslife.activity.LoginActivity;
import com.ums.umslife.activity.MyActActivity;
import com.ums.umslife.activity.MyClubActivity;
import com.ums.umslife.base.BaseFragment;
import com.ums.umslife.bean.UserBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineFragment extends BaseFragment {


    private final static String TAG = "androidjj";
    private final static String SUCCESS_CODE = "0";
    @BindView(R.id.iv_club_logo)
    CircleImageView ivClubLogo;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_dept)
    TextView tvUserDept;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.rl_my_act)
    RelativeLayout rlMyAct;
    @BindView(R.id.rl_my_club)
    RelativeLayout rlMyClub;
    @BindView(R.id.rl_integral_details)
    RelativeLayout rlIntegralDetails;
    @BindView(R.id.rl_change_pwd)
    RelativeLayout rlChangePwd;
    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;
    private UserBean userBean;
    private Context mContext;


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View childView) {
        ButterKnife.bind(this, childView);
        mContext = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        initDatas();
    }

    private void initDatas() {

        SharedPreferences loginShare = getActivity().getSharedPreferences("login",
                Context.MODE_PRIVATE);
        String phone = loginShare.getString("phone", "");
        HttpUtils.init().getUserBean(phone).enqueue(new Callback<UserBean>() {

            @Override
            public void onResponse(Call<UserBean> call,
                                   Response<UserBean> response) {
                userBean = response.body();
                if (userBean != null) {
                    if (userBean.getCode().equals(SUCCESS_CODE)) {
                        Log.d(TAG, "已更新");
                        tvUserName.setText(userBean.getData().getUserName());
                        tvUserDept.setText(userBean.getData().getUserDept());
                        tvPhone.setText(userBean.getData().getPhone());
                        tvIntegral.setText(userBean.getData()
                                .getTotalIntegral());
                    } else {
                        MyUtils.showToast(mContext, "数据异常");
                    }
                } else {
                    MyUtils.showToast(mContext, "数据异常");
                }

            }

            @Override
            public void onFailure(Call<UserBean> arg0, Throwable arg1) {
                MyUtils.showToast(mContext, "连接失败");

            }
        });
    }


    private void logout() {
        getActivity().finish();
        Intent loginIt = new Intent(mContext, LoginActivity.class);
        loginIt.putExtra("isAuto", "00");
        startActivity(loginIt);

    }


        private void showDialog() {
            Builder builder = new Builder(mContext);
            builder.setMessage("确认退出吗？");
            builder.setCancelable(true);
            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.setNegativeButton("确认",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();

                        }

                    });
            builder.create().show();

        }


    @OnClick({R.id.rl_my_act, R.id.rl_my_club, R.id.rl_integral_details, R.id.rl_change_pwd, R.id.rl_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_act:
                MyUtils.startAct(mContext, MyActActivity.class);
                break;
            case R.id.rl_my_club:
                MyUtils.startAct(mContext, MyClubActivity.class);
                break;
            case R.id.rl_integral_details:
                MyUtils.startAct(mContext, IntegralDetailsActivity.class);
                break;
            case R.id.rl_change_pwd:
                MyUtils.startAct(mContext, ChangePasswordActivity.class);
                break;
            case R.id.rl_logout:
                showDialog();
                break;
        }
    }
}
