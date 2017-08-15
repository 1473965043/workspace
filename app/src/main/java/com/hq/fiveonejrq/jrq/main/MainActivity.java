package com.hq.fiveonejrq.jrq.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.custom.BaseActivity;
import com.hq.fiveonejrq.jrq.common.widget.BottomItemView;
import com.hq.fiveonejrq.jrq.job.JobsFragment;
import com.hq.fiveonejrq.jrq.homepage.HomePageFragment;
import com.hq.fiveonejrq.jrq.main.message.MessageFragment;
import com.hq.fiveonejrq.jrq.main.mine.MineFragment;
import com.hq.fiveonejrq.jrq.common.widget.BottomMenuLayout;

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
//        setStatusBarBackgroundColor("#FB6E05");
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
        bottomMenuLayout.setCurrentFragment(0);
        bottomMenuLayout.setChangeListener(changeListener);
    }

    /**
     * 获取当前显示的fragment的下标
     * @return
     */
    public int getCurrent(){
        if(bottomMenuLayout != null){
            return bottomMenuLayout.getCurrent();
        }
        return -1;
    }

    /**
     * 获取状态栏view
     * @return
     */
    public View getTitleView() {
        return super.getTitleView();
    }

    private BottomMenuLayout.OnChangeListener changeListener = new BottomMenuLayout.OnChangeListener() {
        @Override
        public void change(int position) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (int i = 0; i < fragmentList.size(); i++) {
            if(i == getCurrent()){
                fragmentList.get(i).onActivityResult(requestCode, resultCode, data);
//                return;
            }else{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
