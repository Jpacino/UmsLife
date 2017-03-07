package com.ums.umslife.net;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class OkHttpUtil {
	private static HashSet<String> cookies = new HashSet<>();

	static OkHttpClient getOkHttpClient() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new AddCookiesInterceptor())
				.addInterceptor(new ReceivedCookiesInterceptor())
				.connectTimeout(30, TimeUnit.SECONDS).build();
		return okHttpClient;

	}

	private static class ReceivedCookiesInterceptor implements Interceptor {
		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			okhttp3.Response originalResponse = chain.proceed(chain.request());

			if (!originalResponse.headers("Set-Cookie").isEmpty()) {

				for (String header : originalResponse.headers("Set-Cookie")) {
					cookies.add(header);
				}
			}
			return originalResponse;
		}
	}

	private static class AddCookiesInterceptor implements Interceptor {

		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			Request.Builder builder = chain.request().newBuilder();

			for (String cookie : cookies) {
				builder.addHeader("Cookie", cookie);
				Log.v("OkHttp", "Adding Header: " + cookie);
			}

			return chain.proceed(builder.build());
		}
	}

}
