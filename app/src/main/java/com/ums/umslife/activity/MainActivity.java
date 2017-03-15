package com.ums.umslife.activity;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ums.umslife.R;
import com.ums.umslife.fragment.ActivityFragment;
import com.ums.umslife.fragment.ClubFragment;
import com.ums.umslife.fragment.MineFragment;
import com.ums.umslife.utils.MyUtils;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends Activity implements
		OnRequestPermissionsResultCallback {

	private RadioGroup rgFirstMain;
	private List<Fragment> mFragments = new ArrayList<>();
	private long exitTime;
	private ActivityFragment activityFragment = new ActivityFragment();
	private ClubFragment clubFragment = new ClubFragment();
	private MineFragment mineFragment = new MineFragment();

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
		rgFirstMain = (RadioGroup) findViewById(R.id.rg_first_main);
		rgFirstMain.setOnCheckedChangeListener(mCheckListener);
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





}
