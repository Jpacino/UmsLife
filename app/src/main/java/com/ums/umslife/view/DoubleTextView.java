package com.ums.umslife.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ums.umslife.R;

public class DoubleTextView extends LinearLayout {
	private TextView tv_content;
	private TextView tv_label;

	public DoubleTextView(Context context) {
		this(context, null);
	}

	public DoubleTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public DoubleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.view_double_text, this,
				true);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.DoubleTextView);
		String title = typedArray
				.getString(R.styleable.DoubleTextView_title_text);
		String content = typedArray
				.getString(R.styleable.DoubleTextView_content_text);
		int color = typedArray.getColor(
				R.styleable.DoubleTextView_content_color, Color.BLACK);
		typedArray.recycle();
		tv_label = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		if (title != null) {
			tv_label.setText(title);
		}
		if (content != null) {
			tv_content.setText(content);
		}
		setContentColor(color);

	}

	// 获取输入的内容
	public String getContent() {
		return tv_content.getText().toString();
	}

	// 获取输入的内容
	public String getTitle() {
		return tv_label.getText().toString();
	}

	/**
	 * 更改标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_label.setText(text);
	}

	/**
	 * 更改内容
	 * 
	 * @param text
	 */
	public void setContent(String text) {
		tv_content.setText(text);
	}

	/**
	 * 获得textView
	 * */

	public TextView getLeftView() {
		return tv_label;
	}

	/**
	 * 设置颜色
	 * 
	 *	参数：颜色
	 * */
	public void setContentColor(int color) {
		tv_content.setTextColor(color);
	}

}