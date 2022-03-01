package com.example.floatview.drag;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

/**
 * LineChatlayout 主要处理视频窗口拖动
 */
public class DragScaleLayout extends RelativeLayout {
   
    private ViewDragHelper mViewDragHelper;

    private View mVideoView;

    private boolean isChange;

    private int mLeft;
    private int mTop;

    public DragScaleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewDragHelper();
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
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mVideoView != null && isChange) {
            int childLeft = mLeft;
            int childTop = mTop;
            log("onLayout");
            mVideoView.layout(childLeft, childTop, childLeft + mVideoView.getMeasuredWidth(), childTop + mVideoView.getMeasuredHeight());
        }
    }

}
