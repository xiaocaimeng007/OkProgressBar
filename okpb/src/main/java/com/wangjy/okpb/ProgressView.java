package com.wangjy.okpb;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * 姓名: 王梦阳
 * 时间： 2019-12-02
 * 描述：
 */
public class ProgressView extends View {

    private Paint mPaint;
    private int mMeasuredWidth;
    private Paint mPaintFill;
    private float percent = 0;
    private int mPercentColor;
    private int mPercentFillColor;
    private float mProgressHeight;
    private Paint mPaintText;
    private float mPercentTextSize;
    private Rect mRectText;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        percent = typedArray.getFloat(R.styleable.ProgressView_percent, 0);
        mProgressHeight = typedArray.getFloat(R.styleable.ProgressView_progressHeight, 30f);
        mPercentColor = typedArray.getColor(R.styleable.ProgressView_percentColor, Color.RED);
        mPercentFillColor = typedArray.getColor(R.styleable.ProgressView_percentFillColor, Color.LTGRAY);
        mPercentTextSize = typedArray.getDimension(R.styleable.ProgressView_percentTextSize, 13);
        typedArray.recycle();

        if (percent > 100) {
            percent = 100;
        }

        mPaint = new Paint();
        mPaint.setColor(mPercentColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mProgressHeight);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintFill = new Paint();
        mPaintFill.setColor(mPercentFillColor);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setStrokeWidth(mProgressHeight);
        mPaintFill.setAntiAlias(true);
        mPaintFill.setStrokeCap(Paint.Cap.ROUND);

        mPaintText = new Paint();
        mPaintText.setTextSize(mPercentTextSize);
        mPaintText.setColor(Color.BLACK);
        mRectText = new Rect();
        String content = (int) percent + "%";
        // 获取包裹文本最小矩形的宽高，坐标等信息
        mPaintText.getTextBounds(content, 0, content.length(), mRectText);

        startProgressAnimation();
    }

    public void startProgressAnimation() {
        ObjectAnimator percent = ObjectAnimator.ofFloat(this, "percent", this.percent);
        percent.setDuration(1000);
        percent.setInterpolator(new LinearInterpolator());
        percent.start();
    }


    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //progress 宽度

        final float redis = mProgressHeight / 2;
        canvas.drawLine(redis, redis, mMeasuredWidth - redis, redis, mPaintFill);
        if (percent > 0)
            canvas.drawLine(redis, redis, mMeasuredWidth * (percent / 100) + redis, redis, mPaint);

        String content = (int) percent + "%";
        // 获取包裹文本最小矩形的宽高，坐标等信息
        mPaintText.getTextBounds(content, 0, content.length(), mRectText);
        canvas.drawText(content, (mMeasuredWidth - 2 * redis) * (percent / 100) + redis,
                mProgressHeight + mRectText.height() + redis, mPaintText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredWidth = getMeasuredWidth();
        setMeasuredDimension(mMeasuredWidth, (int) (mProgressHeight * 2) + mRectText.height());
    }
}
