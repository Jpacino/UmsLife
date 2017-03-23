package com.ums.umslife.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;

import com.ums.umslife.R;


public class MyUtils {

    public static void startAct(Context context, Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void complete(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    public static void showToast(Context context, String str) {
        ToastUtil toastUtil = new ToastUtil();
        toastUtil.Short(context, str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
    }

}
