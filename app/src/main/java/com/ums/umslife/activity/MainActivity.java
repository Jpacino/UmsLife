package com.ums.umslife.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ums.umslife.R;
import com.ums.umslife.bean.LoginResponseBean;
import com.ums.umslife.fragment.ActivityFragment;
import com.ums.umslife.fragment.ClubFragment;
import com.ums.umslife.fragment.MineFragment;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import rebus.permissionutils.AskagainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;

public class MainActivity extends Activity implements
		OnRequestPermissionsResultCallback {

	private RadioGroup rgFirstMain;
	private List<Fragment> mFragments = new ArrayList<>();
	private long exitTime;
	private ActivityFragment activityFragment = new ActivityFragment();
	private ClubFragment clubFragment = new ClubFragment();
	private MineFragment mineFragment = new MineFragment();
	private Bundle bundle = new Bundle();
	private ArrayList<PermissionEnum> permissionEnumArrayList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		mFragments.add(activityFragment);
		mFragments.add(clubFragment);
		mFragments.add(mineFragment);
		((RadioButton) rgFirstMain.getChildAt(0)).setChecked(true);
	}

	private void init() {
		Intent mainIt = getIntent();
		LoginResponseBean loginResponseBean = (LoginResponseBean) mainIt.getSerializableExtra("loginResponseBean");
		Log.d("androidjj", "+++"+ loginResponseBean);
		bundle.putSerializable("loginResponseBean", loginResponseBean);
		Log.d("androidjj", "==="+bundle);
		mineFragment.setArguments(bundle);
//		clubFragment.setArguments(bundle);
//		activityFragment.setArguments(bundle);
		rgFirstMain = (RadioGroup) findViewById(R.id.rg_first_main);
		rgFirstMain.setOnCheckedChangeListener(mCheckListener);
		permissionData();
		permissionSelector();
	}

	/**
	 * 监听事件
	 * */
	private RadioGroup.OnCheckedChangeListener mCheckListener = new RadioGroup.OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			View child = group.findViewById(checkedId);
			int index = group.indexOfChild(child);
			Fragment fragment = mFragments.get(index);
			replaceFragment(fragment);
		}
	};

	/**
	 * 替换Fragment
	 * */
	private void replaceFragment(Fragment fragment) {
		getFragmentManager().beginTransaction()
				.replace(R.id.ll_first_container, fragment).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - exitTime > 3000) {
				MyUtils.showToast(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("Override")
	@Override
	public void onRequestPermissionsResult(int arg0, String[] arg1, int[] arg2) {
		PermissionManager.handleResult(arg0, arg1, arg2);
	}

	private void permissionData() {
		// 权限
		permissionEnumArrayList.add(PermissionEnum.CAMERA);
		permissionEnumArrayList.add(PermissionEnum.GET_ACCOUNTS);
		permissionEnumArrayList.add(PermissionEnum.WRITE_EXTERNAL_STORAGE);
		permissionEnumArrayList.add(PermissionEnum.READ_EXTERNAL_STORAGE);
		permissionEnumArrayList.add(PermissionEnum.READ_CONTACTS);
		permissionEnumArrayList.add(PermissionEnum.ACCESS_COARSE_LOCATION);
		permissionEnumArrayList.add(PermissionEnum.ACCESS_FINE_LOCATION);

	}

	public void permissionSelector() {
		// 权限选择
		PermissionManager.with(this).permissions(permissionEnumArrayList)
				.askagain(true).askagainCallback(new AskagainCallback() {
					@Override
					public void showRequestPermission(UserResponse response) {
						showDialog(2);
					}
				}).callback(new FullCallback() {
					@Override
					public void result(
							ArrayList<PermissionEnum> permissionsGranted,
							ArrayList<PermissionEnum> permissionsDenied,
							ArrayList<PermissionEnum> permissionsDeniedForever,
							ArrayList<PermissionEnum> permissionsAsked) {
					}
				}).ask();
	}

}
