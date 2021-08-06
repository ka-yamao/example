package com.c.local.example;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.c.local.example.R.styleable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.appcompat.widget.AppCompatTextView;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
		mv = {1, 4, 2},
		bv = {1, 0, 3},
		k = 1,
		d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B%\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0007H\u0002J\u0018\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u0007H\u0014R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"},
		d2 = {"Lcom/c/local/example/EllipsizedTextViewK;", "Landroidx/appcompat/widget/AppCompatTextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "(Landroid/content/Context;Landroid/util/AttributeSet;I)V", "ellipsis", "", "getEllipsis", "()Ljava/lang/String;", "setEllipsis", "(Ljava/lang/String;)V", "ellipsisColor", "getEllipsisColor", "()I", "setEllipsisColor", "(I)V", "ellipsisSpannable", "Landroid/text/SpannableString;", "spannableStringBuilder", "Landroid/text/SpannableStringBuilder;", "getDefaultEllipsis", "", "getDefaultEllipsisColor", "onMeasure", "", "widthMeasureSpec", "heightMeasureSpec", "Example.app"}
)
public final class EllipsizedTextViewJava extends AppCompatTextView {
	@NotNull
	private String ellipsis;
	private int ellipsisColor;
	private SpannableString ellipsisSpannable;
	private SpannableStringBuilder spannableStringBuilder;

	@NotNull
	public final String getEllipsis() {
		return this.ellipsis;
	}

	public final void setEllipsis(@NotNull String var1) {
		Intrinsics.checkNotNullParameter(var1, "<set-?>");
		this.ellipsis = var1;
	}

	public final int getEllipsisColor() {
		return this.ellipsisColor;
	}

	public final void setEllipsisColor(int var1) {
		this.ellipsisColor = var1;
	}

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
			this.setText((CharSequence) this.spannableStringBuilder.append(ellipsizedText).replace(defaultEllipsisStart, defaultEllipsisEnd, (CharSequence) this.ellipsisSpannable));
		}

	}

	private final char getDefaultEllipsis() {
		return '…';
	}

	private final int getDefaultEllipsisColor() {
		ColorStateList var10000 = this.getTextColors();
		Intrinsics.checkNotNullExpressionValue(var10000, "textColors");
		return var10000.getDefaultColor();
	}

	public EllipsizedTextViewJava(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Intrinsics.checkNotNullParameter(context, "context");
		this.ellipsis = String.valueOf(this.getDefaultEllipsis());
		this.ellipsisColor = this.getDefaultEllipsisColor();
		this.spannableStringBuilder = new SpannableStringBuilder();
		if (attrs != null) {
			TypedArray var10000 = context.getTheme().obtainStyledAttributes(attrs, styleable.EllipsizedTextView, 0, 0);
			Intrinsics.checkNotNullExpressionValue(var10000, "context.theme.obtainStyl…EllipsizedTextView, 0, 0)");
			TypedArray typedArray = var10000;
			if (typedArray != null) {
				boolean var6 = false;
				boolean var7 = false;
				boolean var9 = false;
				String var10001 = typedArray.getString(R.styleable.EllipsizedTextView_ellipsis);
				if (var10001 == null) {
					var10001 = String.valueOf(this.getDefaultEllipsis());
				}

				this.ellipsis = var10001;
				this.ellipsisColor = typedArray.getColor(R.styleable.EllipsizedTextView_ellipsisColor, this.getDefaultEllipsisColor());
				typedArray.recycle();
			}
		}

		this.ellipsisSpannable = new SpannableString((CharSequence) this.ellipsis);
		this.ellipsisSpannable.setSpan(new ForegroundColorSpan(this.ellipsisColor), 0, this.ellipsis.length(), 33);
	}

	// $FF: synthetic method
	public EllipsizedTextViewJava(Context var1, AttributeSet var2, int var3, int var4, Object var5) {
		this(var1, var2, var3);
		if ((var4 & 2) != 0) {
			var2 = (AttributeSet) null;
		}
		if ((var4 & 4) != 0) {
			var3 = 0;
		}
	}

	@JvmOverloads
	public EllipsizedTextViewJava(@NotNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 4, null);
	}

	@JvmOverloads
	public EllipsizedTextViewJava(@NotNull Context context) {
		this(context, (AttributeSet) null, 0, 6, null);
	}
}
