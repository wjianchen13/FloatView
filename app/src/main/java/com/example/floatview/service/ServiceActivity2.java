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
 */
public class ServiceActivity2 extends AppCompatActivity implements View.OnClickListener {

    private TestService service = null;

    private boolean isBind = false;

    private boolean isLog = true;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            TestService.MyBinder myBinder = (TestService.MyBinder)binder;
            service = myBinder.getService();
            Utils.log(isLog, "ActivityB - onServiceConnected");
            int num = service.getRandomNumber();
            Utils.log(isLog, "ActivityB - getRandomNumber = " + num);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            Utils.log(isLog, "ActivityB - onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service2);

        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnbindService).setOnClickListener(this);
        findViewById(R.id.btnFinish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBindService){
            //单击了“bindService”按钮
            Intent intent = new Intent(this, TestService.class);
            intent.putExtra("from", "ActivityB");
            Utils.log(isLog, "----------------------------------------------------------------------");
            Utils.log(isLog, "ActivityB 执行 bindService");
            bindService(intent, conn, BIND_AUTO_CREATE);
        }else if(v.getId() == R.id.btnUnbindService){
            //单击了“unbindService”按钮
            if(isBind){
                Utils.log(isLog, "----------------------------------------------------------------------");
                Utils.log(isLog, "ActivityB 执行 unbindService");
                unbindService(conn);
            }
        }else if(v.getId() == R.id.btnFinish){
            //单击了“Finish”按钮
            Utils.log(isLog, "----------------------------------------------------------------------");
            Utils.log(isLog, "ActivityB 执行 finish");
            this.finish();
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Utils.log(isLog, "ActivityB - onDestroy");
    }
    

}