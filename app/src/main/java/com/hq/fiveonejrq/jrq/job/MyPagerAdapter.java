package com.hq.fiveonejrq.jrq.job;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/29.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    /**
     * 数据集合
     */
    private ArrayList<Fragment> list;

    /**
     * 构造函数
     * @param fm
     */
    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

}
