package com.example.floatview.drag;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.floatview.R;

public class DragScaleActivity extends AppCompatActivity {
    
    private RelativeLayout rlytDrag;
    private DragScaleLayout dslyTest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_scale);
        rlytDrag = findViewById(R.id.rlyt_drag);        
        dslyTest = findViewById(R.id.dsly_test);
        dslyTest.setVideoview(rlytDrag);
    }
}