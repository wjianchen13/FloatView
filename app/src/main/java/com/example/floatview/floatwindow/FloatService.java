package com.example.floatview.floatwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

import java.util.Random;

import jnidemo.hlq.com.remoteview.Main2Activity;

/**
 * 这种方式是直接添加一个又尺寸的View到WindowManager，缩放的时候卡顿，拖动没问题
 */
public class FloatService extends Service implements FloatLayout.CallBack {
    
    private boolean isLog = true;

    private int mWidth;
    private int mHeight;

    private int mWindowWidth;
    private int mWindowHeight;
    
    private int mValidWidth;
    private int mValidHeight;

    private WindowManager winManager;
    private WindowManager.LayoutParams wmParams;
    private LayoutInflater inflater;
    //浮动布局
    private FloatLayout mFloatingLayout;
    private LinearLayout linearLayout;
    
    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public FloatService getService() {
            return FloatService.this;
        } 
    }

    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();

    private final Random generator = new Random();
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            scale();
            scaleTest();
        }
    };

    @Override
    public void onCreate() {
        Utils.log(isLog, "TestTwoService - onCreate - Thread = " + Thread.currentThread().getName());
        super.onCreate();
        mWindowWidth = Utils.getScreenWidth(this);
        mWindowHeight = Utils.getScreenHeight(this);
        mWidth = getResources().getDimensionPixelSize(R.dimen.float_view_width);
        mHeight = getResources().getDimensionPixelSize(R.dimen.float_view_height);
        mValidWidth = mWindowWidth - mWidth;
        mValidHeight = mWindowHeight - mHeight;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.log(isLog, "TestTwoService - onStartCommand - startId = " + startId + ", Thread = " + Thread.currentThread().getName());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.log(isLog, "TestTwoService - onBind - Thread = " + Thread.currentThread().getName());
        initWindow();
        //悬浮框点击事件的处理
        initFloating();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.log(isLog, "TestTwoService - onUnbind - from = " + intent.getStringExtra("from"));
        return false;
    }

    @Override
    public void onDestroy() {
        Utils.log(isLog, "TestTwoService - onDestroy - Thread = " + Thread.currentThread().getName());
        super.onDestroy();
        winManager.removeView(mFloatingLayout);
    }

    //getRandomNumber是Service暴露出去供client调用的公共方法
    public int getRandomNumber() {
        return generator.nextInt();
    }

    public void changePosition() {
        wmParams.x = 100;
        wmParams.y = 100;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    public void scale1() {
        wmParams.width = 400;
        wmParams.height = 400;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    public void scale2() {
        wmParams.width = 300;
        wmParams.height = 300;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    public void scale3() {
        wmParams.width = 200;
        wmParams.height = 200;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    public void scale4() {
        wmParams.width = 100;
        wmParams.height = 100;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    private int i = 0;
    
    public void scaleTest() {
        if(mHandler != null) {
            if(i < 150) {
                mHandler.sendEmptyMessageDelayed(1, 20);
                i ++;
            }
        }
    }
    
    private void scale() {
        int ow = wmParams.width;
        int oh = wmParams.height;
        int nw = ow + 2;
        int nh = oh + 2;
        int dw = nw - ow;
        int dh = nh - oh;
//        wmParams.x = wmParams.x - dw / 2;
//        wmParams.y = wmParams.y - dh / 2;
        wmParams.width = nw;
        wmParams.height = nh;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    @Override
    public void onDrag(int dx, int dy) {
        wmParams.x = wmParams.x + dx;
        wmParams.y = wmParams.y + dy;
        Utils.log("onDrag wmParams.x: " + wmParams.x + "   wmParams.y: " + wmParams.y);
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    @Override
    public void onScale(float factor) {
        int ow = wmParams.width;
        int oh = wmParams.height;
        int nw = (int)(ow * factor);
        int nh = (int)(oh * factor);
        Utils.log("onScale factor: " + factor + "   nw: " + nw + "   nh: " + nh);
        int dw = nw - ow;
        int dh = nh - oh;
//        wmParams.x = wmParams.x - dw / 2;
//        wmParams.y = wmParams.y - dh / 2;
        wmParams.width = nw;
        wmParams.height = nh;
        winManager.updateViewLayout(mFloatingLayout, wmParams);
    }

    /**
     * 初始化窗口
     */
    private void initWindow() {
        winManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置好悬浮窗的参数
        wmParams = getParams();
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        wmParams.x = getX(winManager.getDefaultDisplay().getWidth());
        wmParams.y = getY(210);
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(getApplicationContext());
        // 获取浮动窗口视图所在布局
        mFloatingLayout = (FloatLayout) inflater.inflate(R.layout.view_float, null);
        // 添加悬浮窗的视图
        winManager.addView(mFloatingLayout, wmParams);
        mFloatingLayout.setCallBack(this);
    }

    private WindowManager.LayoutParams getParams() {
        wmParams = new WindowManager.LayoutParams();
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        //设置悬浮窗口长宽数据
//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        wmParams.width = mWidth;
        wmParams.height = mHeight;
        
        return wmParams;
    }
    
    private int getX(int x) {
        if(x < 0) {
            return 0;
        } else if(x > mValidWidth) {
            return mValidWidth;
        } 
        return x;
    }

    private int getY(int y) {
        if(y < 0) {
            return 0;
        } else if(y > mValidHeight) {
            return mValidHeight; 
        }
        return y;
    }

    /**
     * 悬浮窗点击事件
     */
    private void initFloating() {
        linearLayout = mFloatingLayout.findViewById(R.id.line1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloatService.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

//        mFloatingLayout.setOnTouchListener(new View.OnTouchListener() {
//            private int dX;
//            private int dY;
//            private int sX;
//            private int sY;
//            private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(FloatService.this, new myScale());
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        dX = (int) motionEvent.getRawX();
//                        dY = (int) motionEvent.getRawY();
//                        sX = (int) motionEvent.getRawX();
//                        sY = (int) motionEvent.getRawY();
//                        isP2Down = true; //双指按下
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (motionEvent.getPointerCount() == 1) {
//                            //单指拖动
//                            int nX = (int) motionEvent.getRawX();
//                            int nY = (int) motionEvent.getRawY();
//                            int cW = nX - dX;
//                            int cH = nY - dY;
//                            dX = nX;
//                            dY = nY;
//                            Utils.log("nX: "+ nX + "   nY: " + nY + "   cW: " + cW + "   cH: " + cH);
//                            wmParams.x = wmParams.x + cW;
//                            wmParams.y = wmParams.y + cH;
//                            Utils.log("wmParams.x: " + wmParams.x + "   wmParams.y: " + wmParams.y);
//                            winManager.updateViewLayout(mFloatingLayout, wmParams);
//                        } else if (motionEvent.getPointerCount() == 2) {
//                            //双指缩放
//                            scaleGestureDetector.onTouchEvent(motionEvent);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        isP2Down = false; //双指抬起
//                        //如果抬起时的位置和按下时的位置大致相同视作单击事件
//                        int nX2 = (int) motionEvent.getRawX();
//                        int nY2 = (int) motionEvent.getRawY();
//                        int cW2 = nX2 - sX;
//                        int cH2 = nY2 - sY;
//                        //间隔值可能为负值，所以要取绝对值进行比较
//                        if (Math.abs(cW2) < 3 && Math.abs(cH2) < 3) view.performClick();
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

        
        //悬浮框触摸事件，设置悬浮框可拖动
//        linearLayout.setOnTouchListener(new FloatService.FloatingListener());
    }


    private float initSapcing = 0;
    private int initWidth;
    private int initHeight;
    private int initX;
    private int initY;
    private boolean isP2Down = false;

    private class myScale extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            detector.getCurrentSpan();//两点间的距离跨度
//            detector.getCurrentSpanX();//两点间的x距离
//            detector.getCurrentSpanY();//两点间的y距离
//            detector.getFocusX();       //
//            detector.getFocusY();       //
//            detector.getPreviousSpan(); //上次
//            detector.getPreviousSpanX();//上次
//            detector.getPreviousSpanY();//上次
//            detector.getEventTime();    //当前事件的事件
//            detector.getTimeDelta();    //两次事件间的时间差
//            detector.getScaleFactor();  //与上次事件相比，得到的比例因子

            if (isP2Down) {
                //双指按下时最初间距
                initSapcing = detector.getCurrentSpan();
                //双指按下时窗口大小
                initWidth = wmParams.width;
                initHeight = wmParams.height;
                //双指按下时窗口左顶点位置
                initX = wmParams.x;
                initY = wmParams.y;
                isP2Down = false;
            }
            float scale = detector.getCurrentSpan() / initSapcing; //取得缩放比
            int newWidth = (int) (initWidth * scale);
            int newHeight = (int) (initHeight * scale);
            //判断窗口缩放后是否超出屏幕大小
            if (newWidth < 720 && newHeight < 1080) {
                wmParams.width = newWidth;
                wmParams.height = newHeight;
                wmParams.x = initX;
                wmParams.y = initY;
                //缩放后图片会失真重新载入图片
//                Glide.with(MediaFloatService.this)
//                        .load(lstFilePaths.get(currentIndex))
//                        .into(floatView.ivFloatMediaShow);
                //提交更新布局
                winManager.updateViewLayout(mFloatingLayout, wmParams);
            }
            return true;
            //return super.onScale(detector);
        }
    }
    
    
    
    
    
    
    

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
    //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private boolean isMove;

//    private class FloatingListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            int action = event.getAction();
//            switch (action) {
//                case MotionEvent.ACTION_DOWN:
//                    isMove = false;
//                    mTouchStartX = (int) event.getRawX();
//                    mTouchStartY = (int) event.getRawY();
//                    mStartX = (int) event.getX();
//                    mStartY = (int) event.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    mTouchCurrentX = (int) event.getRawX();
//                    mTouchCurrentY = (int) event.getRawY();
//                    wmParams.x += mTouchCurrentX - mTouchStartX;
//                    wmParams.y += mTouchCurrentY - mTouchStartY;
//                    winManager.updateViewLayout(mFloatingLayout, wmParams);
//                    mTouchStartX = mTouchCurrentX;
//                    mTouchStartY = mTouchCurrentY;
//                    break;
//                case MotionEvent.ACTION_UP:
//                    mStopX = (int) event.getX();
//                    mStopY = (int) event.getY();
//                    if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
//                        isMove = true;
//                    }
//                    break;
//                default:
//                    break;
//            }
//
//            //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
//            return isMove;
//        }
//    }
}