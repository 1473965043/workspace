package com.hq.fiveonejrq.jrq;

import android.graphics.Color;
import android.support.v4.app.Fragment;

import com.hq.fiveonejrq.jrq.base.BaseActivity;
import com.hq.fiveonejrq.jrq.widget.BottomItemView;
import com.hq.fiveonejrq.jrq.job.JobsFragment;
import com.hq.fiveonejrq.jrq.homepage.HomePageFragment;
import com.hq.fiveonejrq.jrq.message.MessageFragment;
import com.hq.fiveonejrq.jrq.mine.MineFragment;
import com.hq.fiveonejrq.jrq.widget.BottomMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<BottomItemView> mList;
    private BottomMenuLayout bottomMenuLayout;
    private String[] titles = {"首页", "职位", "消息", "我的"};
    private int[] unSelected = {
            R.drawable.enterprise_bar_selected, R.drawable.company_bar_selected, R.drawable.message_bar_selected, R.drawable.mine_bar_selected};
    private List<Fragment> fragmentList;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        bottomMenuLayout = (BottomMenuLayout) findViewById(R.id.bottom_bar);
        fragmentList.add(new HomePageFragment());
        fragmentList.add(new JobsFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new MineFragment());
    }

    @Override
    protected void initEvents() {
        for(int i=0; i<titles.length; i++){
            BottomItemView itemView = new BottomItemView();
            itemView.setFragment(fragmentList.get(i));
            itemView.setIconId(unSelected[i]);
            itemView.setTitle(titles[i]);
            mList.add(itemView);
        }
    }

    @Override
    protected void initDatas() {
        bottomMenuLayout.setDatas(this, R.id.content_container, mList);
        bottomMenuLayout.setCurrentFragment(2);
    }

    public void setStatusBarBackgroundColor(String colorid){
        super.setStatusBarBackgroundColor(Color.parseColor(colorid));
    }

}
