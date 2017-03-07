package com.ums.umslife.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ums.umslife.R;

public class BaseActivity extends Activity {
	protected TextView tvBaseTitle;
	protected ImageView ivBaseBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	protected void initTitle(String title) {
		tvBaseTitle = (TextView) findViewById(R.id.tv_base_title);
		tvBaseTitle.setText(title);
	}

	protected void initBackView(boolean isVisible) {
		if (!isVisible) {
			return;
		}
		ivBaseBack = (ImageView) findViewById(R.id.iv_base_back);
		ivBaseBack.setVisibility(View.VISIBLE);
		ivBaseBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
