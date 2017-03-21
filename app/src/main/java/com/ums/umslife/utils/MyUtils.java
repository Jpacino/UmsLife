package com.ums.umslife.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;

import static android.R.attr.duration;
import static com.ums.umslife.R.styleable.LabelTextView;

public class MyUtils {
	
	public static void startAct(Context context, Class clazz) {
		context.startActivity(new Intent(context, clazz));
	}
	public static void complete(final PullToRefreshListView lv) {
		lv.postDelayed(new Runnable() {
			@Override
			public void run() {
				lv.onRefreshComplete();
			}
		}, 500);
	}
	public static void complete1(final SwipeRefreshLayout refreshLayout) {
		refreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setRefreshing(false);
			}
		}, 1000);
	}
//	private static Toast mToast = null;
//	public static void showToast(Context context, String text) {
//		if (mToast == null) {
//			mToast = Txoast.makeText(context, text, Toast.LENGTH_SHORT);
//		} else {
//			mToast.setText(text);
//			mToast.setDuration(Toast.LENGTH_SHORT);
//		}
//
//		mToast.show();
//	}
	public static void showToast(Context context, String str){
		ToastUtil toastUtil = new ToastUtil();
		toastUtil.Short(context, str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
	}
	
}
