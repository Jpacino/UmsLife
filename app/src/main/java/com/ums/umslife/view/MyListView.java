package com.ums.umslife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义ListView
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MyListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyListView(Context context) {
		this(context, null);
	}

	private void init() {

	}

	/**
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 * 重写onMeasure，解决ScrollView和ListView的冲突
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
