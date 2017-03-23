package com.ums.umslife.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.R;
import com.ums.umslife.fragment.ActivityFragment;
import com.ums.umslife.fragment.ClubFragment;
import com.ums.umslife.fragment.MineFragment;
import com.ums.umslife.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
         {

    private RadioGroup rgMain;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private long exitTime;
    private ActivityFragment activityFragment = new ActivityFragment();
    private ClubFragment clubFragment = new ClubFragment();
    private MineFragment mineFragment = new MineFragment();
    private FragmentTransaction mTransaction;
    private static Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        initData();
    }


    protected void init() {

    }


    protected void initView() {
        rgMain = (RadioGroup) findViewById(R.id.rg_main);
        rgMain.setOnCheckedChangeListener(mCheckListener);
    }

    protected void initData() {
        titles.add("活动");
        titles.add("俱乐部");
        titles.add("个人信息");
        mFragments.add(activityFragment);
        mFragments.add(clubFragment);
        mFragments.add(mineFragment);
        ((RadioButton) rgMain.getChildAt(0)).setChecked(true);
    }
    /**
     * 监听事件
     */
    private RadioGroup.OnCheckedChangeListener mCheckListener = new RadioGroup.OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View child = group.findViewById(checkedId);
            int index = group.indexOfChild(child);
            Fragment fragment = mFragments.get(index);
            ctrlFragment(fragment);
            setTitle(titles.get(index));
            if (index ==2 ){
                hideToolbar();
            }else {
                showToolbar();
            }
        }
    };

             /**
              * 控制Fragment
              * @param fragment 要显示的fragment
              */
    private void ctrlFragment(Fragment fragment){
        mTransaction = getFragmentManager().beginTransaction();
        if(currentFragment!=null&&currentFragment.isAdded()){
            mTransaction.hide(currentFragment);
        }
        if(fragment.isAdded()){
            mTransaction.show(fragment);
        }else{
            mTransaction.add(R.id.ll_first_container,fragment);
        }
        mTransaction.commit();
        currentFragment = fragment;

    }

             /**
              * 重写返回按钮，再按一次退出程序
              */
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
