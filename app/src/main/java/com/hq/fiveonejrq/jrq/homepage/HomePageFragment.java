package com.hq.fiveonejrq.jrq.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.base.BottomItemView;
import com.hq.fiveonejrq.jrq.homepage.adapter.ActivityAdapter;
import com.hq.fiveonejrq.jrq.homepage.adapter.AdvertisementAdapter;
import com.hq.fiveonejrq.jrq.homepage.adapter.RecommendAdapter;
import com.hq.fiveonejrq.jrq.common.widget.MyPopupWindow;
import com.hq.fiveonejrq.jrq.common.widget.MyScrollview;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/2/28.
 */

public class HomePageFragment extends Fragment {

    /**
     * 搜索布局
     */
    private LinearLayout layout;

    /**
     * 图片搜索
     */
    private ImageView filter;

    /**
     * 内容主体
     */
    private RecyclerView topic, activity, recommend, dynamic;

    /**
     * 滚动布局
     */
    private MyScrollview mScrollView;

    /**
     * 弹出框
     */
    private MyPopupWindow popupWindow;

    /**
     * 广告轮播
     */
    private ViewPager mViewPager;

    /**
     * 广告图片集合
     */
    private ArrayList<Integer> imgList;

    /**
     * 图片数组
     */
    private int[] imgs = {
            R.mipmap.ceshi, R.mipmap.user_ba, R.mipmap.gr
    };

    /**
     * 轮播适配器
     */
    private AdvertisementAdapter advertisementAdapter;

    /**
     * 查看更多
     */
    private TextView more_of_recommend, more_of_activity, more_of_topic;

    /**
     * 控件宽高
     */
    private int searchLayoutTop, rlayoutwidth;

    /**
     * 搜索布局
     */
    private RelativeLayout search;

    /**
     * 继续广播轮播
     */
    private final int CONTINUE = 1;

    /**
     * 停止广播轮播
     */
    private final int STOP = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_layout, container, false);
        initViews(view);
        initEvents();
        initData();
        return view;
    }

    private void initViews(View view) {
        layout = (LinearLayout) view.findViewById(R.id.search_linearlayout);
        filter = (ImageView) view.findViewById(R.id.filter);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        topic = (RecyclerView) view.findViewById(R.id.topic);
        activity = (RecyclerView) view.findViewById(R.id.activity);
        recommend = (RecyclerView) view.findViewById(R.id.recommend);
        dynamic = (RecyclerView) view.findViewById(R.id.dynamic);
        more_of_recommend = (TextView) view.findViewById(R.id.more_of_recommend);
        more_of_activity = (TextView) view.findViewById(R.id.more_of_activity);
        more_of_topic = (TextView) view.findViewById(R.id.more_of_recommend);
        mScrollView = (MyScrollview) view.findViewById(R.id.scrollview);
        search = (RelativeLayout) view.findViewById(R.id.search_bar);
        imgList = new ArrayList<>();
        setBarAlpha(0, 90);
    }

    private void initEvents() {
        layout.setOnClickListener(listener);
        for (int i = 0; i < imgs.length; i++) {
            imgList.add(imgs[i]);
        }
        advertisementAdapter = new AdvertisementAdapter(imgList, getContext());
        mViewPager.setAdapter(advertisementAdapter);
        mViewPager.setCurrentItem(1000);
        mScrollView.setOnScrollListener(scrollListener);
        rlayoutwidth = mViewPager.getMeasuredHeight();
        searchLayoutTop = mViewPager.getBottom();
        mHandler.sendEmptyMessageDelayed(CONTINUE, 3000);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mHandler.sendEmptyMessage(STOP);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(CONTINUE, 3000);
                        break;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initPopWindow();
    }

    private void initPopWindow() {
        ArrayList<BottomItemView> str = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            BottomItemView item = new BottomItemView();
            switch(i){
                case 0:
                    item.setTitle("扫一扫");
                    item.setIconId(R.mipmap.saoyisao);
                    break;
                case 1:
                    item.setTitle("二维码");
                    item.setIconId(R.mipmap.code);
                    break;
            }
            str.add(item);
        }
        //具体初始化逻辑看下面的图
        popupWindow = new MyPopupWindow(getActivity(), str);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showPopupWindow(v);
            }
        });
        popupWindow.setOnItemOnClickListener(new MyPopupWindow.OnItemOnClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(position == 0){
                    new IntentIntegrator(getActivity()).initiateScan();
                }else if(position == 1){

                }
            }
        });
    }

    private void initData() {
        ActivityAdapter activityAdapter = new ActivityAdapter(getContext());
        RecommendAdapter recommendAdapter = new RecommendAdapter(getContext());
        recommend.setLayoutManager(new LinearLayoutManager(getContext()));
        recommend.setAdapter(recommendAdapter);
    }

    /**
     * 点击监听器
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.search_linearlayout:
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            layout, getString(R.string.transitions_name));
                    startActivity(new Intent(getActivity(), SearchActivity.class), optionsCompat.toBundle());
                    break;
            }
        }
    };

    /**
     * 滑动监听器
     */
    private MyScrollview.OnScrollListener scrollListener = new MyScrollview.OnScrollListener() {
        @Override
        public void onScrollChanged(int x, int y, int oldX, int oldY) {
            //变化率
            float headHeight = mViewPager.getMeasuredHeight()
                    - search.getMeasuredHeight();
            int alpha = (int) (((float) y / headHeight) * 255);//透明度变化速率
            int searchAlpha = (int) (((float) y / headHeight) * 255);//透明度变化速率
            if (alpha >= 255){
                alpha = 255;
                searchAlpha = 255;
            }
            if (alpha <= 10){
                alpha = 0;
            }
            if(searchAlpha <= 90){
                searchAlpha = 90;
            }
            setBarAlpha(alpha, searchAlpha);
        }
    };

    /**
     * 设置title透明度
     */
    private void  setBarAlpha(int alpha, int searchAlpha){
        search.getBackground().mutate().setAlpha(alpha);
        layout.getBackground().mutate().setAlpha(searchAlpha);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case CONTINUE:
                    startAd();
                    break;
                case STOP:
                    stopAd();
                    break;
            }
        }
    };

    /**
     * 开始轮播广告
     */
    private void startAd(){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        mHandler.sendEmptyMessageDelayed(CONTINUE, 3000);
    }

    /**
     * 停市广播轮播
     */
    private void stopAd(){
        mHandler.removeMessages(CONTINUE);
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
