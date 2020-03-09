package com.example.photoediter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.photoediter.photoView.PhotoView;

public class MainActivity extends Activity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private Context mContext;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private ImageView iv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        iv1 = findViewById(R.id.iv_1);
        iv2 = findViewById(R.id.iv_2);
        iv3 = findViewById(R.id.iv_3);
        iv4 = findViewById(R.id.iv_4);
        iv5 = findViewById(R.id.iv_5);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        switch (v.getId()){
            case R.id.iv_1:
                intent.putExtra("drawable", R.drawable.tank_three);
                intent.putExtra("x",iv1.getLeft());
                intent.putExtra("y",iv1.getTop());
                startActivity(intent);
                break;
            case R.id.iv_2:
                intent.putExtra("drawable", R.drawable.scenery1);
                intent.putExtra("x",iv2.getLeft());
                intent.putExtra("y",iv2.getTop());
                startActivity(intent);
                break;
            case R.id.iv_3:
                intent.putExtra("drawable", R.drawable.scenery2);
                intent.putExtra("x",iv3.getLeft());
                intent.putExtra("y",iv3.getTop());
                startActivity(intent);
                break;
            case R.id.iv_4:
                intent.putExtra("drawable", R.drawable.scenery3);
                intent.putExtra("x",iv4.getLeft());
                intent.putExtra("y",iv4.getTop());
                startActivity(intent);
                break;
            case R.id.iv_5:
                intent.putExtra("drawable", R.drawable.scenery4);
                intent.putExtra("x",iv5.getLeft());
                intent.putExtra("y",iv5.getTop());
                startActivity(intent);
                break;
        }

    }
}
