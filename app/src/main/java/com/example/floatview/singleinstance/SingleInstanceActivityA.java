package com.example.floatview.singleinstance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;

/**
 * SingleInstance相关测试
 */
public class SingleInstanceActivityA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_instance_a);
    }


    public void onActivityB(View v) {
        startActivity(new Intent(this, SingleInstanceActivityB.class));
    }
    
}