package com.example.floatview.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.floatview.utils.Utils;

public class GestureDemoView extends View {
    private static final String TAG = "ScaleGestureDemoView";

    private GestureDetector mGestureDetector;

    public GestureDemoView(Context context) {
        super(context);
    }

    public GestureDemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initScaleGestureDetector();
    }

    private void initScaleGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Utils.log("onSingleTapUp");
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Utils.log("onLongPress");
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Utils.log("onLongPress");
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Utils.log("onLongPress");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Utils.log("onLongPress");
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Utils.log("onLongPress");
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Utils.log("onDoubleTap");
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Utils.log("onDoubleTapEvent");
                return super.onDoubleTapEvent(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Utils.log("onSingleTapConfirmed");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                Utils.log("onContextClick");
                return super.onContextClick(e);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }
}