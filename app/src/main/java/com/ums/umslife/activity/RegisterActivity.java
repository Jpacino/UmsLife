package com.ums.umslife.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ums.umslife.R;
import com.ums.umslife.utils.MyUtils;
import com.ums.umslife.view.LabelTextView;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private final int COUNT_DOWN_NOW = 0;
	private final int COUNT_DOWN_FINISH = 1;
	private Button bt_send_auth_code;
	private final long MAX_TIME = 30000;
	private long mCurrentTime;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COUNT_DOWN_NOW:
				bt_send_auth_code
						.setText((MAX_TIME - mCurrentTime) / 1000+1 + "s");
				break;
			case COUNT_DOWN_FINISH:
				bt_send_auth_code.setText(getResources().getString(
						R.string.send_auth_code));
				bt_send_auth_code.setEnabled(true);
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initTitle("注册用户");
		initBackView(true);
		init();
	}

	private void init() {
		LabelTextView register_set_password = (LabelTextView) findViewById(R.id.register_set_password);
		LabelTextView register_password_again = (LabelTextView) findViewById(R.id.register_password_again);
		MyUtils.setPassWordType(register_set_password, register_password_again);
		bt_send_auth_code = (Button) findViewById(R.id.bt_send_auth_code);
		bt_send_auth_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send_auth_code:
			mHandler.post(countDownRunnable);
			bt_send_auth_code.setEnabled(false);
			break;

		default:
			break;
		}
	}

	private Runnable countDownRunnable = new Runnable() {

		@Override
		public void run() {
			if (MAX_TIME - mCurrentTime > 0) {
				mHandler.postDelayed(this, 1000);
				mCurrentTime += 1000;
				mHandler.obtainMessage(COUNT_DOWN_NOW).sendToTarget();
			} else {
				mCurrentTime = 0;
				mHandler.obtainMessage(COUNT_DOWN_FINISH).sendToTarget();
			}

		}
	};
	@Override
	protected void onDestroy() {
		Log.d("RegisterActivity", "activity_finish");
		mHandler.removeCallbacks(countDownRunnable);
		super.onDestroy();
	}

}
