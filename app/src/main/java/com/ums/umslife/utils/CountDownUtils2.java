package com.ums.umslife.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import com.ums.umslife.R;

public class CountDownUtils2 {
	private final static int COUNT_DOWN_NOW = 0;
	private final static int COUNT_DOWN_FINISH = 1;
	private final static long MAX_TIME = 60000;
	private static Button btSendAuthCode;
	private static long mCurrentTime;
	private static Context context;

	public CountDownUtils2(Context context, Button btSendAuthCode) {
		this.context = context;
		this.btSendAuthCode = btSendAuthCode;
	}

	private static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COUNT_DOWN_NOW:
				btSendAuthCode.setText((MAX_TIME - mCurrentTime) / 1000
						+ "s");
				break;
			case COUNT_DOWN_FINISH:
				btSendAuthCode.setText(context.getResources().getString(
						R.string.send_auth_code));
				btSendAuthCode.setEnabled(true);
				break;

			}
		};
	};
	/**
	 * 
	 * 倒计时任务
	 * */
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

	public void startCountDown() {
		mHandler.post(countDownRunnable);
		btSendAuthCode.setEnabled(false);
	}

	public void onDestroy() {
		mHandler.removeCallbacks(countDownRunnable);
		mCurrentTime=0;
	}

}
