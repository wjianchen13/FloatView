package com.example.floatview.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.RelativeLayout;

/**
 * 可缩放Layout
 */
public class ZoomScrollLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;
    private static final float MIN_ZOOM = 0.3f;
    private static final float MAX_ZOOM = 3.0f;
    private Integer mLeft, mTop, mRight, mBottom;
    private int centerX, centerY;
    private float mLastScale = 1.0f;
    private float totleScale = 1.0f;
    // childview
    private View mChildView;
    // 拦截滑动事件
    float mDistansX, mDistansY, mTouchSlop;

    private enum MODE {
        ZOOM, DRAG, NONE
    }

    private MODE mode;
    boolean touchDown;

    public ZoomScrollLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (mode == MODE.DRAG) {
                    if (mChildView == null) {
                        mChildView = getChildAt(0);
                        centerX = getWidth() / 2;
                        centerY = getHeight() / 2;
                    }
                    if (mLeft == null) {
                        mLeft = mChildView.getLeft();
                        mTop = mChildView.getTop();
                        mRight = mChildView.getRight();
                        mBottom = mChildView.getBottom();
                    }
                    // 防抖动
                    if (touchDown) {
                        touchDown = false;
                        return true;
                    }
                    mLeft = mLeft - (int) distanceX;
                    mTop = mTop - (int) distanceY;
                    mRight = mRight - (int) distanceX;
                    mBottom = mBottom - (int) distanceY;
                    
                    
                    mLeft = mLeft < 0 ? 0 : mLeft;
                    mRight = mLeft + mChildView.getWidth();
                    mTop = mTop < 0 ? 0 : mTop;
                    mBottom = mTop + mChildView.getHeight();
                    mChildView.layout(mLeft, mTop, mRight, mBottom);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                touchDown = true;
                return super.onDown(e);
            }
        });
        // 系统最小滑动距离
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        int currentX = (int) e.getX();
        int currentY = (int) e.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //记录上次滑动的位置
                mDistansX = currentX;
                mDistansY = currentY;
                //将当前的坐标保存为起始点
                mode = MODE.DRAG;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDistansX - currentX) >= mTouchSlop || Math.abs(mDistansY - currentY) >= mTouchSlop) { //父容器拦截
                    return true;
                }
                break;
            //指点杆保持按下，并且进行位移
            //有手指抬起，将模式设为NONE
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = MODE.NONE;
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        if (mode == MODE.ZOOM) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float tempScale = mLastScale * scaleFactor;
            System.out.println("===============================> scaleFactor: " + scaleFactor);
            if (tempScale <= MAX_ZOOM && tempScale >= MIN_ZOOM) {
                totleScale = tempScale;
                applyScale(totleScale);
            }
        }
        return false; // return false每次返回的factor都是叠加的，返回true都是相对前一个factor的
    }

    /**
     * 执行缩放操作
     */

    public void applyScale(float scale) {
//        mChildView.setScaleX(scale);
//        mChildView.setScaleY(scale);
        
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mChildView.getLayoutParams();
        if(params != null) {
            params.width = (int)(900 * scale);
            params.height = (int)(900 * scale);
            mChildView.setLayoutParams(params);
        }
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        mode = MODE.ZOOM;
        if (mode == MODE.ZOOM) {
            if (mChildView == null) {
                mChildView = getChildAt(0);
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            mLeft = mChildView.getLeft();
            mTop = mChildView.getTop();
            mRight = mChildView.getRight();
            mBottom = mChildView.getBottom();
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        mLastScale = totleScale;
    }
}