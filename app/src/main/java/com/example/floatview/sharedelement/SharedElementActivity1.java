package com.example.floatview.sharedelement;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;

public class SharedElementActivity1 extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewo1;
    private ImageView mImageViewo2;
    private ImageView mImageViewo3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element_1);
        mImageViewo1 = findViewById(R.id.imgv_01);
        mImageViewo2 = findViewById(R.id.imgv_02);
        mImageViewo3 = findViewById(R.id.imgv_03);
        mImageViewo1.setOnClickListener(this);
        mImageViewo2.setOnClickListener(this);
        mImageViewo3.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        Intent intent = new Intent(this,SharedElementActivity2.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, mImageViewo2, "pic02");
            startActivity(intent, activityOptions.toBundle());
        }else {
            startActivity(intent);
        }
    }
    
}