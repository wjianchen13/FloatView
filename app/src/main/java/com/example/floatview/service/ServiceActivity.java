package com.example.floatview.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

/**
 * Service测试
 * https://blog.csdn.net/yangxujia/article/details/43491809
 * https://blog.csdn.net/imxiangzi/article/details/76039978
 */
public class ServiceActivity extends AppCompatActivity {

    private TestService service = null;

    private boolean isBind = false;

    private boolean isLog = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void onStart(View v) {
        //单击了“bindService”按钮
        Intent intent = new Intent(this, TestService.class);
        intent.putExtra("from", "ActivityA");
        Utils.log(isLog, "----------------------------------------------------------------------");
        Utils.log(isLog, "ActivityA 执行 bindService");
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    public void onStop(View v) {
        //单击了“unbindService”按钮
        if (isBind) {
            Utils.log(isLog,
                    "----------------------------------------------------------------------");
            Utils.log(isLog, "ActivityA 执行 unbindService");
            unbindService(conn);
        }
    }

    public void onActivity(View v) {
        //单击了“start ActivityB”按钮
        Intent intent = new Intent(this, ServiceActivity2.class);
        Utils.log(isLog,
                "----------------------------------------------------------------------");
        Utils.log(isLog, "ActivityA 启动 ActivityB");
        startActivity(intent);
    }

    public void onFinish(View v) {
        //单击了“Finish”按钮
        Utils.log(isLog,
                "----------------------------------------------------------------------");
        Utils.log(isLog, "ActivityA 执行 finish");
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.log(isLog, "ActivityA - onDestroy");
    }



    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            TestService.MyBinder myBinder = (TestService.MyBinder) binder;
            service = myBinder.getService();
            Utils.log(isLog, "ActivityA - onServiceConnected");
            int num = service.getRandomNumber();
            Utils.log(isLog, "ActivityA - getRandomNumber = " + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Utils.log(isLog, "ActivityA - onServiceDisconnected");
        }


    };
    
    
}