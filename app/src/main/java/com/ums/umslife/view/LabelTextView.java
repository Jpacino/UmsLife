package com.ums.umslife.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ums.umslife.R;

public class LabelTextView extends LinearLayout implements
		OnFocusChangeListener, TextWatcher, OnClickListener {
	private EditText ed_content;
	private TextView tv_label;
	private ImageView iv_delete;

	public LabelTextView(Context context) {
		this(context, null);
	}

	public LabelTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.view_label_text, this,
				true);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.LabelTextView);
		String labelText = typedArray
				.getString(R.styleable.LabelTextView_left_text);
		String rightHint = typedArray
				.getString(R.styleable.LabelTextView_right_hint);
		boolean is_text = typedArray.getBoolean(
				R.styleable.LabelTextView_is_text, false);
		typedArray.recycle();
		tv_label = (TextView) findViewById(R.id.tv_label);
		ed_content = (EditText) findViewById(R.id.ed_content);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		if (labelText != null) {
			tv_label.setText(labelText);
		}
		if (rightHint != null) {
			ed_content.setHint(rightHint);
		}
		if (is_text) {
			isText(is_text);
		}

		ed_content.setOnFocusChangeListener(this);
		ed_content.addTextChangedListener(this);
		iv_delete.setOnClickListener(this);
	}

	// 获取输入的内容
	public String getContent() {
		return ed_content.getText().toString();
	}

	/**
	 * 更改textview内容
	 * 
	 * @param text
	 */
	public void setText(String text) {
		tv_label.setText(text);
	}

	/**
	 * 更改edText的hint内容
	 * 
	 * @param text
	 */
	public void setHint(String text) {
		ed_content.setHint(text);
	}

	/**
	 * 更改edText的InputType
	 * 
	 * @param type
	 *            InputType里的常量
	 */
	public void setInputType(int type) {
		ed_content.setInputType(type);
		
	}

	public void setMaxLength(int size) {
		ed_content.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				size) });

	}

	/**
	 * 获得textView
	 * */

	public TextView getTextView() {
		return tv_label;
	}

	/**
	 * 获得EditText
	 * */

	public EditText getEditText() {
		return ed_content;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			if (!TextUtils.isEmpty(getContent())) {
				iv_delete.setVisibility(View.VISIBLE);
			}

		} else {
			iv_delete.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!TextUtils.isEmpty(getContent())) {
			iv_delete.setVisibility(View.VISIBLE);
		} else {
			iv_delete.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		ed_content.getText().clear();
	}

	public void isText(boolean text) {
		ed_content.setCursorVisible(!text);// 光标隐藏
		ed_content.setFocusable(!text);// 丢失焦点
		ed_content.setFocusableInTouchMode(!text);// 不可获得焦点
	}

}