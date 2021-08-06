package com.c.local.example;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import kotlin.jvm.internal.Intrinsics;

public class EllipsizedTextView extends AppCompatTextView {

	private String ellipsis;
	private int ellipsisColor;
	private SpannableString ellipsisSpannable;
	private SpannableStringBuilder spannableStringBuilder;

	public EllipsizedTextView(@NonNull @NotNull Context context) {
		super(context);
	}

	public EllipsizedTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
		this.ellipsis = String.valueOf(this.getDefaultEllipsis());
		this.ellipsisColor = this.getDefaultEllipsisColor();
		this.spannableStringBuilder = new SpannableStringBuilder();
		if (attrs != null) {
			TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EllipsizedTextView, 0, 0);
			if (typedArray != null) {
				// app:ellipsis="ほげ" の値
				this.ellipsis = typedArray.getString(R.styleable.EllipsizedTextView_ellipsis);
				if (this.ellipsis == null) {
					// デフォルト …
					this.ellipsis = getDefaultEllipsis();
				}
				this.ellipsisColor = typedArray.getColor(R.styleable.EllipsizedTextView_ellipsisColor, this.getDefaultEllipsisColor());
			}
		}
		this.ellipsisSpannable = new SpannableString((CharSequence) this.ellipsis);
		this.ellipsisSpannable.setSpan(new ForegroundColorSpan(this.ellipsisColor), 0, this.ellipsis.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public EllipsizedTextView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float availableScreenWidth = (float) this.getMeasuredWidth() - (float) this.getCompoundPaddingLeft() - (float) this.getCompoundPaddingRight();
		float availableTextWidth = availableScreenWidth * (float) this.getMaxLines();
		CharSequence ellipsizedText = TextUtils.ellipsize(this.getText(), this.getPaint(), availableTextWidth, this.getEllipsize());
		if (Intrinsics.areEqual(ellipsizedText.toString(), this.getText().toString()) ^ true) {
			availableTextWidth = (availableScreenWidth - this.getPaint().measureText(this.ellipsis)) * (float) this.getMaxLines();
			ellipsizedText = TextUtils.ellipsize(this.getText(), this.getPaint(), availableTextWidth, this.getEllipsize());
			Intrinsics.checkNotNullExpressionValue(ellipsizedText, "ellipsizedText");
			int defaultEllipsisStart = ((String) ellipsizedText).indexOf(getDefaultEllipsis());

			int defaultEllipsisEnd = defaultEllipsisStart + 1;
			this.spannableStringBuilder.clear();
			CharSequence text = (CharSequence) this.spannableStringBuilder.append(ellipsizedText).replace(defaultEllipsisStart, defaultEllipsisEnd, (CharSequence) this.ellipsisSpannable);
			this.setText(text);
		}

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	private final String getDefaultEllipsis() {
		return "…";
	}


	private final int getDefaultEllipsisColor() {
		ColorStateList csl = this.getTextColors();
		return csl.getDefaultColor();
	}

	@NotNull
	public String getEllipsis() {
		return this.ellipsis;
	}

	public void setEllipsis(@NotNull String ellipsis) {
		this.ellipsis = ellipsis;
		this.ellipsisSpannable = new SpannableString(ellipsis);
	}

	public int getEllipsisColor() {
		return this.ellipsisColor;
	}

	public void setEllipsisColor(int var1) {
		this.ellipsisColor = var1;
	}


}
