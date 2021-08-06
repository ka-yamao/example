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
			// app:ellipsis="省略文言" の値
			this.ellipsis = typedArray.getString(R.styleable.EllipsizedTextView_ellipsis);
			if (this.ellipsis == null) {
				// デフォルト省略記号 … を設定
				this.ellipsis = getDefaultEllipsis();
			}
			// app:ellipsisColor="省略文言の色を指定" を設定、なければデフォルトカラー（テキストと同じ）
			ellipsisColor = typedArray.getColor(R.styleable.EllipsizedTextView_ellipsisColor, this.getDefaultEllipsisColor());
			System.out.print(ellipsisColor);
		}
		//
		createEllipsisSpannable();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// テキストの横幅
		float availableScreenWidth = (float) this.getMeasuredWidth() - (float) this.getCompoundPaddingLeft() - (float) this.getCompoundPaddingRight();
		// 最大行数
		float availableTextWidth = availableScreenWidth * (float) this.getMaxLines();
		// 省略記号を追加したテキスト
		CharSequence ellipsizedText = TextUtils.ellipsize(this.getText(), this.getPaint(), availableTextWidth, this.getEllipsize());
		// 省略記号を追加したテキスト と テキストを比較
		if (!ellipsizedText.toString().equals(this.getText().toString())) {
			// テキストの横幅 * 行数 - 省略文言の横幅 ※複数行ある場合は、1行として計算するため ※右に余白が残るため、デフォルト省略記号分の幅を追加
			availableTextWidth = availableScreenWidth * (float) this.getMaxLines() - this.getPaint().measureText(this.ellipsis) + this.getPaint().measureText(this.getDefaultEllipsis());
			// 省略文言を考慮したテキスト横幅を指定し、通常の省略記号で省略した文言を取得
			ellipsizedText = TextUtils.ellipsize(this.getText(), this.getPaint(), availableTextWidth, this.getEllipsize());
			// 省略記号の開始位置
			int defaultEllipsisStart = String.valueOf(ellipsizedText).indexOf(getDefaultEllipsis());
			// 省略記号の終了位置
			int defaultEllipsisEnd = defaultEllipsisStart + 1;
			// テキスト生成用 StringBuilder をクリア
			this.spannableStringBuilder.clear();
			// 省略記号を省略文言に置換してテキストを生成
			CharSequence text = this.spannableStringBuilder.append(ellipsizedText).replace(defaultEllipsisStart, defaultEllipsisEnd, this.ellipsisSpannable);
			// テキストを設定
			this.setText(text);
		}
	}

	/**
	 * デフォルト省略記号
	 *
	 * @return String
	 */
	private String getDefaultEllipsis() {
		return "…";
	}


	/**
	 * デフォルト省略記号のカラー
	 *
	 * @return int
	 */
	private int getDefaultEllipsisColor() {
		ColorStateList csl = this.getTextColors();
		return csl.getDefaultColor();
	}

	/**
	 * 省略文言を設定
	 *
	 * @param ellipsis String
	 */
	public void setEllipsis(@NonNull String ellipsis) {
		this.ellipsis = ellipsis;
		this.createEllipsisSpannable();
	}

	/**
	 * 色の設定を取得
	 *
	 * @return
	 */
	public int getEllipsisColor() {
		return this.ellipsisColor;
	}

	/**
	 * 省略記号のカラーを設定
	 *
	 * @param color
	 */
	public void setEllipsisColor(int color) {
		this.ellipsisColor = color;
		this.createEllipsisSpannable();
	}

	/**
	 * 省略の SpannableString を生成
	 */
	private void createEllipsisSpannable() {
		// Spannableへ設定
		this.ellipsisSpannable = new SpannableString(this.ellipsis);
		this.ellipsisSpannable.setSpan(new ForegroundColorSpan(this.ellipsisColor), 0, this.ellipsis.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

}