package com.hq.fiveonejrq.jrq.homepage.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/30.
 */

public class AdvertisementAdapter extends PagerAdapter {

    private ArrayList<Integer> list;

    private Context mContext;

    public AdvertisementAdapter(ArrayList<Integer> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView  imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(list.get(position%list.size()));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
