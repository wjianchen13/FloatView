package com.example.floatview.floatwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

public class FloatLayout extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    
    private Context mContext;
    
    private int mWidth;
    private int mHeight;
    
    // 属性变量
    private float translationX; // 移动X
    private float translationY; // 移动Y
    private float scale = 1; // 伸缩比例
//    private float rotation; // 旋转角度
    
    private float spacing;
    private float degree;
    private int moveType; // 0=未选择，1=拖动，2=缩放

    private float mLastX;
    private float mLastY;

    private CallBack mCallBack;
    
    private ScaleGestureDetector mScaleDetector;

    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public FloatLayout(Context context) {
        this(context, null);
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mWidth = mContext.getResources().getDimensionPixelSize(R.dimen.float_view_width);
        mHeight = mContext.getResources().getDimensionPixelSize(R.dimen.float_view_height);
        setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Utils.log("FloatLayout onInterceptTouchEvent  ACTION_DOWN");
                moveType = 1;
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();
                return isNeedMove(mLastX, mLastY, mMoveX, mMoveY);
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isNeedMove(float mIstartX, float mIstartY, float mMoveX, float mMoveY) {
        if(Math.abs(mIstartX - mMoveX) > 60 || Math.abs(mIstartY - mMoveY) > 60) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveType = 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                moveType = 2;
                spacing = getSpacing(event);
                degree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if(moveType == 1) {
                    Utils.log("FloatLayout onTouchEvent  ACTION_MOVE");
                    float tx = event.getRawX();
                    float ty = event.getRawY();
                    if (mCallBack != null) {
                        mCallBack.onDrag((int) (tx - mLastX), (int) (ty - mLastY));
                    }
                    mLastX = tx;
                    mLastY = ty;
                } else if(moveType == 2) {
                    float factor = getSpacing(event) / spacing;
//                    setScaleX(scale);
//                    setScaleY(scale);
//                    if(mCallBack != null)
//                        mCallBack.onScale(factor);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                moveType = 0;
        }
        mScaleDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(moveType == 2 && detector != null) {
            if(mCallBack != null)
                mCallBack.onScale(detector.getScaleFactor());
        }
        return true; // return false每次返回的factor都是叠加的，返回true都是相对前一个factor的
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        moveType = 2;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        moveType = 0;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    // 触碰两点间距离
    private float getSpacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // 取旋转角度
    private float getDegree(MotionEvent event) {
        //得到两个手指间的旋转角度
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
    
    public interface CallBack {
        void onDrag(int dx, int dy); // 相对位置改变
        void onScale(float factor);
    }
}

