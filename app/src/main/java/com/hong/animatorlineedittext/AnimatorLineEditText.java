package com.hong.animatorlineedittext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

public class AnimatorLineEditText extends AppCompatEditText {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float mProgress = 0;
    private int mFocusStrokeWidth = 5;
    private int mDefaultStrokeWidth = 2;
    private int mFocusColor = Color.parseColor("#FF4081");
    private int mDefaultColor = Color.parseColor("#999999");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AnimatorLineEditText(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AnimatorLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatorLineEditText);
        mFocusStrokeWidth = (int) a.getDimension(R.styleable.AnimatorLineEditText_focusStrokeWidth, mFocusStrokeWidth);
        mDefaultStrokeWidth = (int) a.getDimension(R.styleable.AnimatorLineEditText_defaultStrokeWidth, mDefaultStrokeWidth);
        mDefaultColor = a.getColor(R.styleable.AnimatorLineEditText_defaultColor, mDefaultColor);
        mFocusColor = a.getColor(R.styleable.AnimatorLineEditText_focusColor, mFocusColor);
        a.recycle();

        setBackground(null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
        mPaint.setColor(mDefaultColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec) + mFocusStrokeWidth;
        setMeasuredDimension(mWidth, mHeight);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setFocus();
                }else{
                    setUnFocus();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mDefaultColor);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
        canvas.drawLine(0, mHeight - mDefaultStrokeWidth, mWidth, mHeight - mDefaultStrokeWidth, mPaint);
        mPaint.setColor(mFocusColor);
        mPaint.setStrokeWidth(mFocusStrokeWidth);
        canvas.drawLine(mWidth / 2, mHeight - mFocusStrokeWidth, mWidth / 2 - mProgress, mHeight - mFocusStrokeWidth, mPaint);
        canvas.drawLine(mWidth / 2, mHeight - mFocusStrokeWidth, mWidth / 2 + mProgress, mHeight - mFocusStrokeWidth, mPaint);
    }

    private void setFocus(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, mWidth / 2);
        startAnimatorAction(animator);
    }

    private void setUnFocus(){
        ValueAnimator animator = ValueAnimator.ofFloat(mWidth / 2, 0);
        startAnimatorAction(animator);
    }

    private void startAnimatorAction(ValueAnimator animator) {
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
