package com.example.photoediter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyImageView extends View {
    private Paint mPaint;
    private Drawable mDrawable;
    private boolean isLarge = false;
    private Rect mDrawableRect = new Rect();
    // private Rect mDrawableOffsetRect = new Rect();
    private Context mContext;
    private float mRation_WH = 0;
    private float mOldX = 0;
    private float mOldY = 0;
    private float mOldX0, mOldY0, mOldX1, mOldY1, mOldK, mOldB, mOldHandsX,
            mOldHandsY;
    private double mD1;
    private boolean isFirst = true;
    private final int SINGALDOWN = 1;// 单点按下
    private final int MUTILDOWM = 2;// 双点按下
    private final int MUTILMOVE = 3;// 双点拖拽
    private int mStatus = 0;


    public MyImageView(Context context) {
        super(context);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(35.0f);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (mDrawable == null || mDrawable.getIntrinsicHeight() == 0
                || mDrawable.getIntrinsicWidth() == 0) {
            return;
        }
        setBounds();
        mDrawable.draw(canvas);
        // Log.i("draw", "draw+++++++++++++++++++++++++++++++++++++++");

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getPointerCount()) {//几个手指操作
            case SINGALDOWN:

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStatus = SINGALDOWN;
                        mOldX = event.getX();
                        mOldY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        checkBounds();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mStatus == SINGALDOWN) {
                            int offsetWidth = (int) (event.getX() - mOldX);
                            int offsetHeight = (int) (event.getY() - mOldY);

                            mOldX = event.getX();
                            mOldY = event.getY();
                            mDrawableRect.offset(offsetWidth, offsetHeight);
                            invalidate();
                        }

                        break;
                    default:
                        break;
                }
                break;
            default:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.d("DOUBLETOWDOWN", "true");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mStatus = MUTILMOVE;
                        float X0 = event.getX(0);
                        float Y0 = event.getY(0);
                        float X1 = event.getX(1);
                        float Y1 = event.getY(1);
                        float k = (Y1 - Y0) / (X1 - X0);
                        float b = (Y0 * X1 - Y1 * X0) / (X1 - X0);
                        /*int RectCenterX = mDrawableRect.centerX();
                        int RectCenterY = mDrawableRect.centerY();
                        float mHandsX = (X0 + X1) / 2;
                        float mHandsY = mHandsX * k + b;*/
                        double mD2 = Math.sqrt(Math.pow(X0 - X1, 2)
                                + Math.pow(Y0 - Y1, 2));

                        if (mD1 < mD2) {
                            if (mDrawableRect.width() < mContext.getResources()
                                    .getDisplayMetrics().widthPixels * 2) {
                                int offsetwidth = 10;
                                int offsettop = (int) (offsetwidth / mRation_WH);
                                mDrawableRect.set(mDrawableRect.left - offsetwidth,
                                        mDrawableRect.top - offsettop,
                                        mDrawableRect.right + offsetwidth,
                                        mDrawableRect.bottom + offsettop);

                                invalidate();
                            }

                        } else {
                            if (mDrawableRect.width() > mContext.getResources()
                                    .getDisplayMetrics().widthPixels / 3) {
                                int offsetwidth = 10;
                                int offsettop = (int) (offsetwidth / mRation_WH);
                                mDrawableRect.set(mDrawableRect.left + offsetwidth,
                                        mDrawableRect.top + offsettop,
                                        mDrawableRect.right - offsetwidth,
                                        mDrawableRect.bottom - offsettop);
                                invalidate();
                                Log.i("GCM", "bbbbbbbbbbbbbbb");
                            }
                        }
                        mD1 = mD2;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("mStatus", "mutildouble_up");
                        mStatus = 0;
                        break;
                    default:
                        break;
                }
                break;
        }

        return true;
    }

    public void setBounds() {
        if (isFirst) {
            mRation_WH = (float) mDrawable.getIntrinsicWidth()
                    / (float) mDrawable.getIntrinsicHeight();
            int px_w = Math.min(getWidth(),
                    dip2px(mContext, mDrawable.getIntrinsicWidth()));
            int px_h = (int) (px_w / mRation_WH);
            int left = (getWidth() - px_w) / 2;
            int top = (getHeight() - px_h) / 2;
            int right = px_w + left;
            int bottom = px_h + top;
            mDrawableRect.set(left, top, right, bottom);
            // mDrawableOffsetRect.set(mDrawableRect);
            isFirst = false;
        }
        mDrawable.setBounds(mDrawableRect);

    }

    public void checkBounds() {
        int newLeft = mDrawableRect.left;
        int newTop = mDrawableRect.top;
        boolean isChange = false;
        if (newLeft < -mDrawableRect.width()) {
            newLeft = -mDrawableRect.width();
            isChange = true;
        }
        if (newTop < -mDrawableRect.height()) {
            newTop = -mDrawableRect.height();
            isChange = true;
        }
        if (newLeft > getWidth()) {
            newLeft = getWidth();
            isChange = true;
        }
        if (newTop > getHeight()) {
            newTop = getHeight();
            isChange = true;
        }
        if (isChange) {
            mDrawableRect.offsetTo(newLeft, newTop);
            invalidate();
        }
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public int dip2px(Context context, int value) {//dp单位转换成px像素单位
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

}


