package com.example.floatview.floatwindow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;
import com.example.floatview.service.TestService;

public class FloatActivity extends AppCompatActivity {

    private FloatService mService = null;
    private boolean hasBind = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);
    }
    
    public void onShow(View v) {
        Intent intent = new Intent(FloatActivity.this, FloatService.class);
        hasBind = bindService(intent, mVideoServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onHide(View v) {
        if (hasBind){
            unbindService(mVideoServiceConnection);
            hasBind = false;
        }
    }

    public void onTest1(View v) {
        mService.changePosition();
    }

    public void onTest2(View v) {
        mService.scaleTest();
    }

    public void onTest3(View v) {
        mService.scale2();
    }

    public void onTest4(View v) {
        mService.scale3();
    }

    public void onTest5(View v) {
        mService.scale4();
    }
    
    ServiceConnection mVideoServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatService.MyBinder binder = (FloatService.MyBinder) service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    
}