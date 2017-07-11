package com.hq.fiveonejrq.jrq.job;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.Utils.Util;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = this.getWindowManager();
        DisplayMetrics dp = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dp);
        int screenWidth = dp.widthPixels;
        int screenHeight = dp.heightPixels;
        Toast.makeText(this, ""+Util.getTotalHeight(this), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, screenWidth+"与"+screenHeight, Toast.LENGTH_SHORT).show();
    }

    public void onclick(View view){
        Toast.makeText(this, Util.getScreenHeight(this)+"", Toast.LENGTH_SHORT).show();
    }
}
