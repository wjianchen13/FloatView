package com.example.floatview.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

/**
 * LineChatlayout 主要处理视频窗口拖动
 */
public class DragScaleLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener{

    private static final int MAX_WIDTH = 720;
    private static final float MAX_HEIGHT = 1440;
    
    
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
    private int mStartWidht;
    private int mStartHeight;
    
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public DragScaleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragScaleLayout);

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
        log("setVideoview execute:" + view);
        mVideoView = view;
        if (mVideoView != null) {
            mLeft = mVideoView.getLeft();
            mTop = mVideoView.getTop();
            log("setVideoview mVideoView is not null");
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
                return child == mVideoView && child.getVisibility() == VISIBLE;
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
                mLeft = left;
                mTop = top;
                isChange = true;
                log("onViewPositionChanged");
                invalidate();
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

    private void log(String msg){
        if (TextUtils.isEmpty(msg))
            return;
        StringBuilder sb = new StringBuilder(msg);
        sb.append(",mLeft=").append(mLeft);
        sb.append(",mTop=").append(mTop);
        if (mVideoView != null){
            sb.append(",org:left=").append(mVideoView.getLeft());
            sb.append(",org:top=").append(mVideoView.getTop());
            sb.append(",org:right=").append(mVideoView.getRight());
            sb.append(",org:bottom=").append(mVideoView.getBottom());
        } else {
            sb.append(",mVideoView is null");
        }
        Log.d("reset_video_exchanged", sb.toString());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(mViewDragHelper != null) {
            return mViewDragHelper.shouldInterceptTouchEvent(event);
        } 
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mViewDragHelper != null)
            mViewDragHelper.processTouchEvent(event);
        if(mScaleDetector != null) {
            mScaleDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mVideoView != null && isChange && mViewDragHelper != null) {
            int childLeft = mLeft;
            int childTop = mTop;
            Utils.log("onLayout");
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
            int nW = (int) (mStartWidht * scale);
            int nH = (int) (mStartHeight * scale);
            Utils.log("mOrgWidth: " + mOrgWidth + "  mOrgHeight: " + mOrgHeight);
            Utils.log("scale: " + scale + "  mStartWidht: " + mStartWidht + "  mStartHeight: " + mStartHeight + "  nW: " + nW + "  nH: " + nH);
            if(nW >= mOrgWidth && nW <= MAX_WIDTH
                && nH >= mOrgHeight && nH <= MAX_HEIGHT) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                if(params != null) {
                    params.width = nW;
                    params.height = nH;
                    mVideoView.setLayoutParams(params);
                }
            }
        }
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (mVideoView == null) {
            mVideoView = getChildAt(0);
        }
        mStartWidht = mVideoView.getWidth();
        mStartHeight = mVideoView.getHeight();
//        mLeft = mVideoView.getLeft();
//        mTop = mVideoView.getTop();
//        mRight = mVideoView.getRight();
//        mBottom = mVideoView.getBottom();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
