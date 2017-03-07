package com.ums.umslife.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetHelper {
	/**
	 * 是否联网网络 使用前提是需要加上权限: <uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * */

	public static boolean IsHaveInternet(final Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manger.getActiveNetworkInfo();
			Log.e("info;", info + "..");
			if (info != null) {
				Log.e("info;", info.isConnected() + "..");
			}
			// return (info != null && info.isConnected());
			return (info != null);
		} catch (Exception e) {
			return false;
		}
	}

	public static int netType(Context mContext) {
		int type = 0;
		if (IsHaveInternet(mContext)) {
			ConnectivityManager manager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null) {
				if (info.getType() == 1) {// 1是wifi
					type = 1;
				} else if (info.getType() == 0) {// 2是gprs
					type = 2;
				}
			}
		} else {
			Toast.makeText(mContext, "网络未连接", Toast.LENGTH_LONG).show();
			MyUtils.showToast(mContext,"网络未连接");
		}
		return type;
	}

}
