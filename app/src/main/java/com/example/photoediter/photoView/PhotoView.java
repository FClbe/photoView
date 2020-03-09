package com.example.photoediter.photoView;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class PhotoView extends View {

    private final String TAG = "PhotoView";
    private final int duration = 500;//动画时间

    private Paint mPaint;
    private Point currentPoint;
    private int currentWidth = 0;
    private int currentHeight = 0;
    private Drawable mDrawable;
    private Rect mDrawableRect = new Rect();
    private int mStatus = 0;
    private final int SINGALDOWN = 1;// 单点按下
    private final int MUTILDOWM = 2;// 双点按下
    private final int MUTILMOVE = 3;// 双点拖拽
    private float mOldX = 0;
    private float mOldY = 0;

    //是否放大
    private boolean isLarge = false;

    //判断点击是否移动，如果小则为点击事件
    private float judgeX;
    private float judgeY;

    //第一次画
    private boolean isFirst = true;

    //图片位置
    private int viewLeft = 0;
    private int viewTop = 0;
    private int originLeft = 0;
    private int originTop = 0;

    //宽高比
    private float mRation_WH;
    private int mWidth;
    private int mHeight;

    //图片原大小
    private int originalWidth;
    private int originalHeight;
    //点击时大小
    private int clickWidth;
    private int clickHeight;

    private int screenWidth;
    private int screenHeight;
    private int screenCenterX;
    private int screenCenterY;

    private double mD1;

    public PhotoView(Context context){
        super(context);
        getScreenWH();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getScreenWH();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    public PhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        getScreenWH();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null || mDrawable.getIntrinsicHeight() == 0
                || mDrawable.getIntrinsicWidth() == 0) {
            return;
        }else {
            if (isFirst) {//第一次
                getScreenWH();
                currentPoint = new Point(viewLeft, viewTop);
                //画
                setBounds();
                canvas.drawRect(mDrawableRect,mPaint);
                mDrawable.draw(canvas);
                startAnimation();
            } else {
                //画
                setBounds();
                canvas.drawRect(mDrawableRect,mPaint);
                mDrawable.draw(canvas);
            }
        }
        //setBounds();
        //mDrawable.draw(canvas);
    }

    private void setBounds() {
        if (isFirst) {
            //背景置黑
            setBackgroundColor(Color.BLACK);
            originalWidth = dip2px(getContext(),mDrawable.getIntrinsicWidth());
            originalHeight = dip2px(getContext(),mDrawable.getIntrinsicHeight());
            /*Log.d(TAG, "originalWidth: "+ originalWidth + "originalHeight: "+ originalHeight
            + "getWidth" + getWidth() + "getHeight" + getHeight());*/
            viewLeft = getLeft();
            viewTop = getTop();
            Log.d(TAG, "left: " + viewLeft + "  top: " + viewTop);
            mRation_WH = (float) mDrawable.getIntrinsicWidth()
                    / (float) mDrawable.getIntrinsicHeight();
            mWidth = Math.min(getWidth(),
                    dip2px(getContext(), mDrawable.getIntrinsicWidth()));

            mHeight = (int) (mWidth / mRation_WH);
            if (currentWidth == 0 || currentHeight == 0){
                currentWidth = mWidth;
                currentHeight = mHeight;
            }
            int left = (getWidth() - currentWidth) / 2;
            int top = (getHeight() - currentHeight) / 2;
            int right = currentWidth + left;
            int bottom = currentHeight + top;
            mDrawableRect.set(left, top, right, bottom);
            // mDrawableOffsetRect.set(mDrawableRect);
            isFirst = false;
        }
        viewLeft = (int)currentPoint.getX();
        viewTop = (int)currentPoint.getY();
        mDrawableRect.set(viewLeft,viewTop, currentWidth + viewLeft,viewTop + currentHeight);
        mDrawable.setBounds(mDrawableRect);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getPointerCount()){
            case 1:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mStatus =SINGALDOWN;
                        mOldX = event.getX();
                        mOldY = event.getY();
                        judgeX = event.getX();
                        judgeY = event.getY();
                        Log.d(TAG, "onTouchEvent: ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mStatus == SINGALDOWN) {
                            float newX = (event.getX() - mOldX) + currentPoint.getX();
                            float newY = (event.getY() - mOldY) + currentPoint.getY();
                            //比屏幕小不能移出屏幕，比屏幕大有移动限制
                            if (currentWidth < screenWidth){
                                if (newX < 0)newX = 0;
                                if (newX > screenWidth - currentWidth) newX = screenWidth - currentWidth;
                            }else {
                                //限制（未写）
                            }
                            if (currentHeight < screenHeight){
                                if (newY < 0)newY = 0;
                                if (newY > screenHeight - currentHeight)newY = screenHeight - currentHeight;
                            }else{
                                //限制（未写）
                            }

                                currentPoint.setX(newX);
                                currentPoint.setY(newY);
                                mOldX = event.getX();
                                mOldY = event.getY();
                                invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mStatus = 0;
                        float tx = event.getX();
                        float ty = event.getY();
                        double d = Math.sqrt(Math.pow(tx - judgeX, 2)
                                + Math.pow(ty - judgeY, 2));
                        if (d < 2)
                            performClick();
                        break;
                    default:
                        break;
                }
                break;
            default:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        float tempX0 = event.getX(0);
                        float tempY0 = event.getY(0);
                        float tempX1 = event.getX(1);
                        float tempY1 = event.getY(1);
                        mD1 = Math.sqrt(Math.pow(tempX0 - tempX1, 2)
                                + Math.pow(tempY0 - tempY1, 2));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mStatus = MUTILMOVE;
                        float X0 = event.getX(0);
                        float Y0 = event.getY(0);
                        float X1 = event.getX(1);
                        float Y1 = event.getY(1);
                        double mD2 = Math.sqrt(Math.pow(X0 - X1, 2)
                                + Math.pow(Y0 - Y1, 2));

                        if (mD1 < mD2) {
                            //放大
                            if (mDrawableRect.width() < screenWidth * 2) {
                                int offsetWidth = 20;
                                int offsetTop = (int) (offsetWidth / mRation_WH);
                                currentWidth = currentWidth + offsetWidth;
                                currentHeight = currentHeight + offsetTop;
                                currentPoint.setX(currentPoint.getX() - offsetWidth / 2);
                                currentPoint.setY(currentPoint.getY() - offsetTop / 2);
                                invalidate();
                            }

                        } else {
                            //缩小
                            if (mDrawableRect.width() > screenWidth / 3) {
                                int offsetWidth = 20;
                                int offsetTop = (int) (offsetWidth / mRation_WH);
                                currentWidth = currentWidth - offsetWidth;
                                currentHeight = currentHeight - offsetTop;
                                currentPoint.setX(currentPoint.getX() + offsetWidth / 2);
                                currentPoint.setY(currentPoint.getY() + offsetTop / 2);
                                invalidate();

                            }
                        }
                        mD1 = mD2;
                        break;
                    case MotionEvent.ACTION_UP:
                        mStatus = 0;
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }

    /*获取屏幕宽高*/
    private void getScreenWH(){
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        screenCenterX = screenWidth / 2;
        screenCenterY = screenHeight / 2;
        Log.d(TAG, "onTouchEvent: screenWidth"+ screenWidth + " screenHeight: " + screenHeight);
    }

    public int dip2px(Context context, int value) {//dp单位转换成px像素单位
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    private void startAnimation() {
        Point startPoint = new Point(viewLeft, viewTop);
        Point endPoint = new Point(getWidth() / 2 - mWidth / 2, getHeight() / 2 - mHeight / 2);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator anim2 = ValueAnimator.ofInt(clickWidth, mWidth);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentWidth = (int)animation.getAnimatedValue();
                currentHeight = (int)(currentWidth / mRation_WH);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.play(anim).with(anim2);
        animatorSet.start();

    }

    public void setDrawable(Drawable Drawable) {
        this.mDrawable = Drawable;
    }

    /**
     *
     * @param Drawable
     * @param x  图片横坐标
     * @param y  图片纵坐标
     */
    public void setDrawable(Drawable Drawable, int x, int y) {
        this.mDrawable = Drawable;
        viewLeft = x;
        viewTop = y;
        originLeft = x;
        originTop = y;
        invalidate();
    }

    /**
     *
     * @param Drawable
     * @param x 图片横坐标
     * @param y 图片纵坐标
     * @param clickWidth 点击时图片宽度
     * @param clickHeight 点击时图片高度
     */
    public void setDrawable(Drawable Drawable, int x, int y, int clickWidth,
     int clickHeight) {
        this.mDrawable = Drawable;
        viewLeft = x;
        viewTop = y;
        originLeft = x;
        originTop = y;
        this.currentWidth = clickWidth;
        this.currentHeight = clickHeight;
        this.clickWidth = clickWidth;
        this.clickHeight = clickHeight;
        invalidate();
    }

    public void finish(){
        Point startPoint = new Point(currentPoint.getX(), currentPoint.getY());
        Point endPoint = new Point(originLeft, originTop);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator anim2 = ValueAnimator.ofInt(currentWidth, clickWidth);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentWidth = (int)animation.getAnimatedValue();
                currentHeight = (int)(currentWidth / mRation_WH);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.play(anim).with(anim2);
        animatorSet.start();
    }
}
