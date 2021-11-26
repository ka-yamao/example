package com.c.local.myapplication.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.c.local.myapplication.R;

import java.util.Arrays;

import androidx.annotation.NonNull;

public class BarGraph extends View {

	// グラフデータ
	private int[] mGraphValues;
	// 下限 index
	private int mMinIndex;
	// 上限 index
	private int mMaxIndex;
	// アクティブカラー
	private int mActiveColor;
	// インアクティブカラー
	private int mInactiveColor;

	/**
	 * コンストラクター
	 * Viewのクラス内では、getContext()でcontextの取得ができる
	 *
	 * @param context
	 */
	public BarGraph(@NonNull Context context) {
		this(context, null);
	}

	public BarGraph(@NonNull Context context, @NonNull AttributeSet attrs) {
		this(context,
				attrs,
				0);
	}

	public BarGraph(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// xml 設定データ
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarGraphView, 0, 0);
		// データ配列の初期化
		mGraphValues = new int[]{};

		// 下限インデックス
		mMinIndex = typedArray.getInt(R.styleable.BarGraphView_minIndex, 0);
		int maxIndex = typedArray.getInt(R.styleable.BarGraphView_minIndex, -1);
		// 上限インデックス
		if (maxIndex > 0) {
			mMaxIndex = maxIndex;
		} else if (mGraphValues.length > 0) {
			// データが存在したら配列のサイズ - 1
			mMaxIndex = mGraphValues.length - 1;
		}
		// 活性カラー
		mActiveColor = typedArray.getColor(R.styleable.BarGraphView_activeColor, context.getColor(R.color.lightBlue));
		// 非活性カラー
		mInactiveColor = typedArray.getColor(R.styleable.BarGraphView_inactiveColor, context.getColor(R.color.grey20));
	}


	/**
	 * AddViewなんかで実際にviewが追加されたときに呼び出される
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	/**
	 * タテヨコの大きさを決める時に呼び出される
	 * Viewの大きさを指定したい場合は、ここで指定
	 */
	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
	}


	/**
	 * レイアウトを決める時に呼び出される
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	/**
	 * 描画
	 */
	@SuppressLint({"RestrictedApi", "ResourceAsColor"})
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Viewの幅、高さを知る
		int width = this.getWidth();
		int height = this.getHeight();

		Paint paint = new Paint();
		Paint paintWhiteLine = new Paint();
		paintWhiteLine.setStrokeWidth((float) 3.0);
		paintWhiteLine.setColor(Color.WHITE);
		// データ数
		int dataSize = mGraphValues.length;
		// TODO 棒線との間に間隔を開けるので データ数 + X (※この数値を上げると棒線の間隔が広くなる)
		int splitNum = dataSize;
		// 棒線の太さ
		paint.setStrokeWidth((float) width / dataSize);
		// 最大値
		int maxValue = Arrays.stream(mGraphValues).reduce(Math::max).getAsInt();
		float unit = (float) maxValue / height;
		// データ数のループ
		for (int i = 0; i < mGraphValues.length; i++) {
			// 棒線のスタート位置
			final float startX = (width / dataSize / 2) + i * width / dataSize;
			final float whiteLine1StartX = i * width / dataSize;
			final float whiteLine2StartX = (i + 1) * width / dataSize;
			// 棒線の高さ計算
			final float stopY = height - (mGraphValues[i] / unit);
			// 高さ
			final float startY = height;
			// 棒線のカラー
			if (mMinIndex == mMaxIndex) {
				paint.setColor(mInactiveColor);
			} else if (i < mMinIndex || i >= mMaxIndex) {
				paint.setColor(mInactiveColor);
			} else {
				paint.setColor(mActiveColor);
			}

			canvas.drawLine(startX, startY, startX, stopY, paint); // 青、グレーの棒グラフの線
			canvas.drawLine(whiteLine1StartX, startY, whiteLine1StartX, stopY, paintWhiteLine); // 間隔を空けるため、青、グレー棒線の左端に白い線
			canvas.drawLine(whiteLine2StartX, startY, whiteLine2StartX, stopY, paintWhiteLine); // 間隔を空けるため、青、グレー棒線の右端に白い線

			Log.d("★", startX + "/" + height + "/" + startX + "/" + stopY);
		}
	}

	public void setGraphValues(int[] mGraphValues) {
		this.mGraphValues = mGraphValues;
	}

	public void setMinIndex(int mMinIndex) {
		this.mMinIndex = mMinIndex;
	}

	public void setMaxIndex(int mMaxIndex) {
		this.mMaxIndex = mMaxIndex;
	}

}

