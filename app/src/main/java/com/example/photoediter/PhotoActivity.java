package com.example.photoediter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.photoediter.photoView.PhotoView;

public class PhotoActivity extends AppCompatActivity {

    private PhotoView photoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        Intent intent = getIntent();
        int id = intent.getIntExtra("drawable", R.drawable.scenery1);
        int x = intent.getIntExtra("x", 0);
        int y = intent.getIntExtra("y", 0);
        photoView = findViewById(R.id.pv_test);
        photoView.setDrawable(getResources().getDrawable(id),x, y, 200, 200);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        photoView.finish();
        overridePendingTransition(R.anim.fade_in_disappear,R.anim.fade_out_disappear);
    }
}
