package com.ums.umslife.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ums.umslife.R;
import com.ums.umslife.view.LabelTextView;

public class MyUtils {
	
	public static void startAct(Context context, Class clazz) {
		context.startActivity(new Intent(context, clazz));
	}

	public static void setPassWordType(LabelTextView... ltvs) {
		if (ltvs != null && ltvs.length > 0) {
			for (LabelTextView labelTextView : ltvs) {
				if (labelTextView != null) {
					labelTextView.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		}
	}
	public static void complete(final PullToRefreshListView lv) {
		lv.postDelayed(new Runnable() {
			@Override
			public void run() {
				lv.onRefreshComplete();
			}
		}, 500);
	}
	public static void showToast(Context context, String str){
		ToastUtil toastUtil = new ToastUtil();
		toastUtil.Short(context, str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
	}
	
}
