package com.ums.umslife.fragment;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.ums.umslife.R;
import com.ums.umslife.activity.ChangePasswordActivity;
import com.ums.umslife.activity.IntegralDetailsActivity;
import com.ums.umslife.activity.LoginActivity;
import com.ums.umslife.activity.MyActActivity;
import com.ums.umslife.activity.MyClubActivity;
import com.ums.umslife.adapter.MineListMenuAdapter;
import com.ums.umslife.base.BaseFragment;
import com.ums.umslife.bean.MineMenuBean;
import com.ums.umslife.bean.UserBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineFragment extends BaseFragment {

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
    @BindView(R.id.mlv_menu)
    MyListView mlvMenu;
    private final static String TAG = "androidjj";
    private final static String SUCCESS_CODE = "0";
    private UserBean userBean;
    private Context mContext;
    private List<MineMenuBean> menuList = new ArrayList<>();

    {
        menuList.add(new MineMenuBean(0, "我的俱乐部", R.drawable.ic_my_club));
        menuList.add(new MineMenuBean(0, "我的活动", R.drawable.ic_my_activity));
        menuList.add(new MineMenuBean(0, "积分明细", R.drawable.ic_integral_detail));
        menuList.add(new MineMenuBean(0, "密码修改", R.drawable.ic_change_pwd));
        menuList.add(new MineMenuBean(0, "注销", R.drawable.ic_logout));
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View childView) {
        ButterKnife.bind(this, childView);
        mContext = getActivity();
        MineListMenuAdapter adapter = new MineListMenuAdapter(menuList);
        mlvMenu.setAdapter(adapter);
        mlvMenu.setOnItemClickListener(mineMenuItemClickListener);
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

    private OnItemClickListener mineMenuItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            switch (position) {
                case 0:
                    MyUtils.startAct(mContext, MyClubActivity.class);
                    break;
                case 1:
                    MyUtils.startAct(mContext, MyActActivity.class);
                    break;
                case 2:
                    MyUtils.startAct(mContext, IntegralDetailsActivity.class);
                    break;
                case 3:
                    MyUtils.startAct(mContext, ChangePasswordActivity.class);
                    break;
                case 4:
                    showDialog();

                    break;
                default:
                    break;
            }
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
    };

}
