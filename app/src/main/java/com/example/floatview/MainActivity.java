package com.example.floatview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.floatview.background.BackgroundActivity;
import com.example.floatview.drag.DragScaleActivity;
import com.example.floatview.drag.ScaleActivity;
import com.example.floatview.floatwindow.FloatActivity;
import com.example.floatview.gesture.GestureActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onBackground(View v) {
        startActivity(new Intent(this, BackgroundActivity.class));
    }

    public void onFloat(View v) {
        startActivity(new Intent(this, FloatActivity.class));
    }

    public void onDrag(View v) {
        startActivity(new Intent(this, ScaleActivity.class));
    }

    public void onGesture(View v) {
        startActivity(new Intent(this, GestureActivity.class));
    }

    public void onDragScale(View v) {
        startActivity(new Intent(this, DragScaleActivity.class));
    }

    public void onFloatDialog(View v) {
        startActivity(new Intent(this, jnidemo.hlq.com.remoteview.MainActivity.class));
    }
}