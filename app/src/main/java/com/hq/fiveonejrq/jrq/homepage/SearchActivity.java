package com.hq.fiveonejrq.jrq.homepage;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.custom.BaseActivity;
import com.hq.fiveonejrq.jrq.common.widget.LuckyRotateView;

public class SearchActivity extends BaseActivity {

    private EditText keywords;
    private TextView cancle;

    @Override
    protected int setLayoutId() {
        return R.layout.search;
    }

    @Override
    protected void initViews() {
        keywords = (EditText) findViewById(R.id.search_keywords);
        cancle = (TextView) findViewById(R.id.cancle);
    }

    @Override
    protected void initEvents() {
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LuckyRotateView) findViewById(R.id.rotateview)).ss();
//                onBackPressed();
            }
        });
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void setStatusBarBackgroundColor(int colorid) {
        super.setStatusBarBackgroundColor(Color.parseColor("#E5E5E5"));
    }

    public void start(View view){
        ((LuckyRotateView) findViewById(R.id.rotateview)).setRunTag(3);
    }
}
