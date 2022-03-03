package com.example.floatview.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DragScaleLayout 主要处理视频窗口拖动
 */
public class DragScaleLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener{

    private static final int MAX_WIDTH = 720;
    private static final float MAX_HEIGHT = 1440;

    public static final int TYPE_NULL = 0;
    public static final int TYPE_DRAG = 1;
    public static final int TYPE_SCALE = 2;

    @IntDef({TYPE_NULL, TYPE_DRAG, TYPE_SCALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private @Type int mType;

    private ViewDragHelper mViewDragHelper;
    private ScaleGestureDetector mScaleDetector;

    private View mVideoView;

    private boolean isChange;

    /**
     * 原始宽高尺寸
     */
    private int mOrgWidth;
    private int mOrgHeight;

    /**
     * 开始缩放尺寸
     */
    private int mStartWidth;
    private int mStartHeight;

    private int mStartLeft;
    private int mStartTop;

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public DragScaleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragScaleLayout);
        mType = TYPE_NULL;
        mOrgWidth = a.getInt(R.styleable.DragScaleLayout_android_layout_width, 240);
        mOrgHeight = a.getInt(R.styleable.DragScaleLayout_android_layout_height, 480);
        a.recycle();
        initViewDragHelper();
        init(context);
    }

    public void setVideoview(int id){
        setVideoview(findViewById(id));
    }

    public void setVideoview(View view){
        Utils.log("setVideoview execute:" + view);
        mVideoView = view;
        if (mVideoView != null) {
            mLeft = mVideoView.getLeft();
            mTop = mVideoView.getTop();
            Utils.log("setVideoview mVideoView is not null");
        }
    }

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, this);
    }

    /**
     * init ViewDragHelper
     */
    private void initViewDragHelper() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) { // 返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
                return mType == TYPE_DRAG ? child == mVideoView && child.getVisibility() == VISIBLE : false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) { // 对child移动的边界进行控制
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mVideoView.getWidth() - leftBound;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) { // 对child移动的边界进行控制
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mVideoView.getHeight() - topBound;
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }

            /**
             * 如果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，而在判断的过程中会去判断
             * 另外两个回调的方法：getViewHorizontalDragRange和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获
             */
            @Override
            public int getViewHorizontalDragRange(View child) { // childView横向或者纵向的移动的范围
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) { // childView横向或者纵向的移动的范围
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) { // 当captureview的位置发生改变时回调
                Utils.log("onViewPositionChanged mType: " + mType);
                if(mType == TYPE_DRAG) {
                    mLeft = left;
                    mTop = top;
                    isChange = true;
                    Utils.log("onViewPositionChanged");
                    invalidate();
                }
            }

            @Override
            public int getOrderedChildIndex(int index) {
                if(mVideoView != null) {
                    return indexOfChild(mVideoView);
                } else {
                    return super.getOrderedChildIndex(index);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        int action = event.getActionMasked();
//        int currentX = (int) event.getX();
//        int currentY = (int) event.getY();
//        Utils.log("onInterceptTouchEvent action： " + action);
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                Utils.log("ACTION_DOWN");
//                mType = TYPE_DRAG;
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                Utils.log("ACTION_POINTER_DOWN count: " + event.getPointerCount());
//                if(event.getPointerCount() >= 2) {
//                    mType = TYPE_SCALE;   
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Utils.log("ACTION_MOVE count: " + event.getPointerCount());
////                if (Math.abs(mDistansX - currentX) >= mTouchSlop || Math.abs(mDistansY - currentY) >= mTouchSlop) { //父容器拦截
////                    return true;
////                }
//                break;
//            //指点杆保持按下，并且进行位移
//            //有手指抬起，将模式设为NONE
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                Utils.log("ACTION_UP count: " + event.getPointerCount());
//                mType = TYPE_NULL;
//                break;
//        }
        if(mViewDragHelper != null) {
            boolean isTouch = mViewDragHelper.shouldInterceptTouchEvent(event);
            Utils.log("onInterceptTouchEvent isTouch： " + isTouch);
            return isTouch;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        Utils.log("onTouchEvent action： " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
//                Utils.log("onTouchEvent ACTION_DOWN");
                mType = TYPE_DRAG;
//                mViewDragHelper.processTouchEvent(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
//                Utils.log("onTouchEvent ACTION_POINTER_DOWN count: " + event.getPointerCount() + "  mType: " + mType);
                if(event.getPointerCount() >= 2) {
                    mType = TYPE_SCALE;
                }
                break;
            case MotionEvent.ACTION_MOVE:

//                if(event.getPointerCount() >= 2) { // 说明倒数第二个手指抬起
//                    mType = TYPE_SCALE;
//                } else {
//                    mType = TYPE_DRAG;
//                }
//                Utils.log("onTouchEvent ACTION_MOVE count: " + event.getPointerCount() + "  mType: " + mType);
                break;

            case MotionEvent.ACTION_POINTER_UP:
//                Utils.log("onTouchEvent ACTION_POINTER_UP count: " + event.getPointerCount() + "  mType: " + mType);
                if(event.getPointerCount() == 2) { // 说明倒数第二个手指抬起
                    mType = TYPE_NULL;
                }
                break;

            case MotionEvent.ACTION_UP:
//                Utils.log("onTouchEvent ACTION_UP count: " + event.getPointerCount() + "  mType: " + mType);
                mType = TYPE_NULL;
                break;

        }

        if(mViewDragHelper != null &&/* action != MotionEvent.ACTION_DOWN &&*/ mType == TYPE_DRAG)
            mViewDragHelper.processTouchEvent(event);
        if(mScaleDetector != null && mType == TYPE_SCALE) {
            mScaleDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            Utils.log("computeScroll " );
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mVideoView != null && isChange) {
            int childLeft = mLeft;
            int childTop = mTop;
            Utils.log("onLayout mLeft: " + mLeft + "  mTop: " + mTop);
            mVideoView.layout(childLeft, childTop, childLeft + mVideoView.getMeasuredWidth(), childTop + mVideoView.getMeasuredHeight());
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        Utils.log("scaleFactor: " + scaleFactor);
        applyScale(scaleFactor);
        return false;
    }


    /**
     * 执行缩放操作
     */

    public void applyScale(float scale) {
        if(mVideoView != null) {
            int nW = (int) (mStartWidth * scale);
            int nH = (int) (mStartHeight * scale);
            Utils.log("scale: " + scale + "  mStartWidth: " + mStartWidth + "  mStartHeight: " + mStartHeight + "  nW: " + nW + "  nH: " + nH);
            if(nW >= mOrgWidth && nW <= MAX_WIDTH
                    && nH >= mOrgHeight && nH <= MAX_HEIGHT) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
                if(params != null) {
                    params.width = nW;
                    params.height = nH;
                    mVideoView.setLayoutParams(params);
                    mLeft = mStartLeft - (nW - mStartWidth) / 2;
                    Utils.log("applyScale mLeft: " + mLeft + "  mTop: " + mTop);
                    mLeft = mLeft < 0 ? 0 : mLeft;
                    mLeft = mLeft > getWidth() - mVideoView.getWidth() ? getWidth() - mVideoView.getWidth() : mLeft;
                    mTop = mStartTop - (nH - mStartHeight) / 2;
                    mTop = mTop < 0 ? 0 : mTop;
                    mTop = mTop > getHeight() - mVideoView.getHeight() ? getHeight() - mVideoView.getHeight() : mTop;
                    Utils.log("applyScale mLeft: " + mLeft + "  mTop: " + mTop + "   getWidth() - mVideoView.getWidth(): " + (getWidth() - mVideoView.getWidth()));
                    mVideoView.layout(mLeft, mTop, mLeft + mVideoView.getWidth(), mTop + mVideoView.getHeight());
                }
            }
        }
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Utils.log("onScaleBegin: ");
        mType = TYPE_SCALE;
        if (mVideoView == null) {
            mVideoView = getChildAt(0);
        }
        mStartWidth = mVideoView.getWidth();
        mStartHeight = mVideoView.getHeight();
        mStartLeft = mVideoView.getLeft();
        mStartTop = mVideoView.getTop();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Utils.log("onScaleEnd: ");
        mType = TYPE_NULL;
    }
}
