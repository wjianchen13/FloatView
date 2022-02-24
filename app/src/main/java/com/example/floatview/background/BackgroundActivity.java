package com.example.floatview.background;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;
import com.example.floatview.utils.Utils;

public class BackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
    }

    public void onBackground(View v) {
        moveTaskToBack(true); // 最小化Activity
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.log("BackgroundActivity onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Utils.log("BackgroundActivity onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.log("BackgroundActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.log("BackgroundActivity onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.log("BackgroundActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.log("BackgroundActivity onResume");
    }
}