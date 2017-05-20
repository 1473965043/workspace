package com.hq.fiveonejrq.jrq.base;

import android.support.v4.app.Fragment;

/**
 * Created by guodong on 2017/2/28.
 */

public class BottomItemView {

    private int iconId;
    private String title;
    private Fragment fragment;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
