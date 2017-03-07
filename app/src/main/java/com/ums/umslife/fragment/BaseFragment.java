package com.ums.umslife.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ums.umslife.R;

public abstract class BaseFragment extends Fragment {
	private View mRootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_base, container,
					false);
			TextView titleTv = (TextView) mRootView
					.findViewById(R.id.fragment_base_title_tv);
			ImageView titleIv = (ImageView) mRootView
					.findViewById(R.id.fragment_base_title_iv);
			FrameLayout childContainer = (FrameLayout) mRootView
					.findViewById(R.id.fragment_base_child_container);
			View childView = inflater.inflate(getContentLayoutRes(),
					childContainer, true);
			initView(titleTv, titleIv, childView);
			initData();

		}

		return mRootView;
	}

	protected void initData() {

	}

	/**
	 * @return 子类应该通过此方法，返回自己在标题区域下方的布局
	 */
	protected abstract int getContentLayoutRes();

	/**
	 * @param titleTv
	 *            标题文字，应该setText
	 * @param titleIv
	 *            标题图片，应该setVisiblity
	 * @param childView
	 *            中间的布局，应该findViewById .....
	 */
	protected abstract void initView(TextView titleTv, ImageView titleIv,
									 View childView);
}