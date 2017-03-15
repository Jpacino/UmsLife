package com.ums.umslife.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ums.umslife.R;

public class BaseActivity extends AppCompatActivity {
	private TextView title;
	private ImageView back;
	protected final String TAG = this.getClass().getSimpleName();

	protected void setTitle(String msg) {
		if (title != null) {
			title.setText(msg);
		}
	}
	protected void setBackBtn() {
		if (back != null) {
			back.setVisibility(View.VISIBLE);
			back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}else {
//			Logger.t(TAG).e("back is null ,please check out");
		}

	}
	protected void setBackClickListener(View.OnClickListener l) {
		if (back != null) {
			back.setVisibility(View.VISIBLE);
			back.setOnClickListener(l);
		}else {
//			Logger.t(TAG).e("back is null ,please check out");
		}

	}
	protected void noToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setVisibility(View.GONE);
	}
	private LinearLayout rootLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 这句很关键，注意是调用父类的方法
		super.setContentView(R.layout.activity_base);
		// 经测试在代码里直接声明透明状态栏更有效
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
			localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
		}
		initToolbar();
	}

	private void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
		if (getSupportActionBar() != null) {
			// Enable the Up button
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setDisplayShowTitleEnabled(false);

		}
		back = (ImageView) findViewById(R.id.img_back);
		title = (TextView) findViewById(R.id.title);
	}
	@Override
	public void setContentView(int layoutId) {
		setContentView(View.inflate(this, layoutId, null));
	}

	@Override
	public void setContentView(View view) {
		rootLayout = (LinearLayout) findViewById(R.id.root_layout);
		if (rootLayout == null) return;
		rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		initToolbar();
	}


//	protected void initTitle(String title) {
//		tvBaseTitle = (TextView) findViewById(R.id.tv_base_title);
//		tvBaseTitle.setText(title);
//	}
//
//	protected void initBackView(boolean isVisible) {
//		if (!isVisible) {
//			return;
//		}
//		ivBaseBack = (ImageView) findViewById(R.id.iv_base_back);
//		ivBaseBack.setVisibility(View.VISIBLE);
//		ivBaseBack.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//	}

}
