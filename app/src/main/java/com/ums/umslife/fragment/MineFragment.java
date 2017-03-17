package com.ums.umslife.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ums.umslife.R;
import com.ums.umslife.activity.ChangePasswordActivity;
import com.ums.umslife.activity.IntegralDetailsActivity;
import com.ums.umslife.activity.LoginActivity;
import com.ums.umslife.activity.MyActActivity;
import com.ums.umslife.activity.MyClubActivity;
import com.ums.umslife.adapter.MineListMenuAdapter;
import com.ums.umslife.bean.MineMenuBean;
import com.ums.umslife.bean.UserBean;
import com.ums.umslife.net.HttpUtils;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineFragment extends BaseFragment implements OnClickListener {

	private TextView userNameTv, userDeptTv, phoneTv, totalIntegralTv;
	private final static String TAG = "androidjj";
	private final static String SUCCESS_CODE = "0";
	private UserBean userBean;
	private List<MineMenuBean> menuList = new ArrayList<>();
	{
		menuList.add(new MineMenuBean(0, "俱乐部"));
		menuList.add(new MineMenuBean(0, "已参加活动"));
		menuList.add(new MineMenuBean(0, "积分明细"));
		menuList.add(new MineMenuBean(0, "密码修改"));
		menuList.add(new MineMenuBean(0, "注销"));
	}

	@Override
	protected int getContentLayoutRes() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void initView(View childView) {
		RelativeLayout rlMineToLoginActivity = (RelativeLayout) childView
				.findViewById(R.id.rl_mine_to_login_activity);
		userNameTv = (TextView) childView.findViewById(R.id.user_name_tv);
		userDeptTv = (TextView) childView.findViewById(R.id.user_dept_tv);
		phoneTv = (TextView) childView.findViewById(R.id.phone_tv);
		totalIntegralTv = (TextView) childView
				.findViewById(R.id.total_integral_tv);
		rlMineToLoginActivity.setOnClickListener(this);
		ListView mlv_list_menu = (ListView) childView.findViewById(R.id.mlv_list_menu);
		MineListMenuAdapter adapter = new MineListMenuAdapter(menuList);
		mlv_list_menu.setAdapter(adapter);
		mlv_list_menu.setOnItemClickListener(mineMenuItemClickListener);
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
						userNameTv.setText(userBean.getData().getUserName());
						userDeptTv.setText(userBean.getData().getUserDept());
						phoneTv.setText(userBean.getData().getPhone());
						totalIntegralTv.setText(userBean.getData()
								.getTotalIntegral());
					} else {
						Toast.makeText(getActivity(), "数据异常",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getActivity(), "数据异常",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Call<UserBean> arg0, Throwable arg1) {
				Toast.makeText(getActivity(), "连接失败",
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_mine_to_login_activity:

			break;

		}
	}

	private void logout() {
		getActivity().finish();
		Intent loginIt = new Intent(getActivity(), LoginActivity.class);
		loginIt.putExtra("isAuto", "00");
		startActivity(loginIt);

	}

	private OnItemClickListener mineMenuItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			switch (position) {
			case 0:
				MyUtils.startAct(getActivity(), MyClubActivity.class);
				break;
			case 1:
				MyUtils.startAct(getActivity(), MyActActivity.class);
				break;
			case 2:
				MyUtils.startAct(getActivity(), IntegralDetailsActivity.class);
				break;
			case 3:
				MyUtils.startAct(getActivity(), ChangePasswordActivity.class);
				break;
			case 4:
				showDialog();

				break;
			default:
				break;
			}
		}

		private void showDialog() {
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setMessage("确认退出吗？");
			builder.setCancelable(false);
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
