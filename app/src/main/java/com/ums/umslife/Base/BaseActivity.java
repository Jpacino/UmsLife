package com.ums.umslife.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ums.umslife.R;

public abstract class BaseActivity extends AppCompatActivity {

    private TextView title;
    private ImageView back;
    protected final String TAG = "androidjj";
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base);
        // 经测试在代码里直接声明透明状态栏更有效
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
////            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            Window window = getWindow();
        //            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        //                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
        //                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //            window.setStatusBarColor(Color.TRANSPARENT);
        //            window.setNavigationBarColor(Color.TRANSPARENT);
        //            initToolbar();
        //        }
    }

    /**
     * @param msg 标题
     *            设置标题
     */
    protected void setTitle(String msg) {
        if(title != null) {
            title.setText(msg);
        }
    }

    /**
     * 设置点击finish
     */
    protected void setBackBtn() {
        if(back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
        }

    }

    /**
     * @param l 监听
     *          设置返回按钮监听
     */
    protected void setBackClickListener(View.OnClickListener l) {
        if(back != null) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(l);
        }
    }

    /**
     * 初始化ToolBar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if(getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        back = (ImageView) findViewById(R.id.iv_back);
        title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * 不显示ToolBar
     */
    protected void hideToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }

    }

    /**
     * 显示ToolBar
     */
    protected void showToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if(rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT));
        initToolbar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
