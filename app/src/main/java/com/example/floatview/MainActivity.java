package com.example.floatview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.floatview.background.BackgroundActivity;
import com.example.floatview.floatwindow.FloatActivity;

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
    
    
}