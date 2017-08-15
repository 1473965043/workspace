package com.hq.fiveonejrq.jrq.job;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.widget.FullyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/2/28.
 */

public class JobsFragment extends Fragment {

    /**
     * 标题控件
     */
    private TextView enterprise, hunter;

    /**
     * 企业与猎头界面
     */
    private HunterFragment hunterJobs;
    private CompanyFragment companyJobs;
    private ArrayList<Fragment> fragmentList;

    /**
     * 查询控件
     */
    private ImageView jobSearch;

    /**
     * 筛选控件
     */
    private ImageView jobFiltration;

    /**
     * 内容主体控件
     */
    private ViewPager mViewPager;

    /**
     * 筛选弹出框数据集合
     */
    private ArrayList<FiltrationTypeBean> filtrationList;

    /**
     * 标题栏根布局
     */
    private View rootView;

    /**
     * 弹出框
     */
    private PopupWindow popupWindow;

    /**
     * 弹出框适配器
     */
    private PopupWindowAdapter popupWindowAdapter;

    /**
     * 窗体参数
     */
    private WindowManager.LayoutParams lp;

    private Window popWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.jobs_layout, container, false);
        initViews(rootView);
        initEvents();
        initData();
        return rootView;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initViews(View view) {
        filtrationList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        hunterJobs = new HunterFragment();
        companyJobs = new CompanyFragment();
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        enterprise = (TextView) view.findViewById(R.id.job_of_enterprise);
        hunter = (TextView) view.findViewById(R.id.job_of_hunter);
        jobFiltration = (ImageView) view.findViewById(R.id.filtration);
        jobSearch = (ImageView) view.findViewById(R.id.job_search);
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        jobFiltration.setOnClickListener(listener);
        jobSearch.setOnClickListener(listener);
        enterprise.setOnClickListener(listener);
        hunter.setOnClickListener(listener);
        enterprise.setSelected(true);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        setPopupWindowData(3);
        fragmentList.add(hunterJobs);
        fragmentList.add(companyJobs);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), fragmentList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(enterprise.isSelected()){
                    enterprise.setSelected(false);
                    hunter.setSelected(true);
                }else{
                    enterprise.setSelected(true);
                    hunter.setSelected(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
    }

    /**
     * 点击监听器
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.filtration:
                    if(popupWindow == null){
                        showPopwindow();
                    }else{
                        lp.alpha = 0.7f;
                        popWindow.setAttributes(lp);
                        popupWindow.showAsDropDown(rootView.findViewById(R.id.titlebar));
                    }
                    break;
                case R.id.job_search:
                    startActivity(new Intent(getContext(), SearchActivity.class));
                    break;
                case R.id.job_of_enterprise:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.job_of_hunter:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.determine:
                    //执行关键词查询
                    popupWindow.dismiss();
                    Toast.makeText(getContext(), "功能尚未完成...", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.reset:
                    // 重置关键词
                    ViewGroup viewGroup = (ViewGroup) popupWindow.getContentView();
                    RecyclerView recyclerView = (RecyclerView) viewGroup.getChildAt(0);
                    popupWindowAdapter.clearAllFlag(recyclerView);
                    break;
            }
        }
    };

    /**
     * 设置弹出数据
     * @param size
     */
    private void setPopupWindowData(int size){
        ArrayList<FiltrationTypeBean> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<String> strlist = new ArrayList<>();
            strlist.add("全部");
            strlist.add("测试");
            strlist.add("试试");
            strlist.add("工程师");
            FiltrationTypeBean ft = new FiltrationTypeBean();
            ft.setTitle("标题"+i);
            ft.setList(strlist);
            list.add(ft);
        }
        filtrationList.addAll(list);
    }

    /**
     * 显示弹出框
     */
    private void showPopwindow(){
        popupWindow = new PopupWindow(getContext());
        View pop = LayoutInflater.from(getContext()).inflate(R.layout.job_filtration_layout, null);
        TextView determine = (TextView) pop.findViewById(R.id.determine);
        TextView reset = (TextView) pop.findViewById(R.id.reset);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        determine.setOnClickListener(listener);
        reset.setOnClickListener(listener);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F8FD")));
        popWindow = getActivity().getWindow();
        lp = popWindow.getAttributes();
        lp.alpha = 0.7f;
        popWindow.setAttributes(lp);
        RecyclerView recyclerView = (RecyclerView) pop.findViewById(R.id.filtration_recyclerview);
        //设置布局管理器
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        popupWindowAdapter = new PopupWindowAdapter(filtrationList, getContext());
        //设置适配器
        recyclerView.setAdapter(popupWindowAdapter);
        //设置布局
        popupWindow.setContentView(pop);
        //显示位置
        popupWindow.showAsDropDown(rootView.findViewById(R.id.titlebar));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                popWindow.setAttributes(lp);
                if(popupWindow == null){
                    Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mViewPager != null){
            mViewPager.clearOnPageChangeListeners();
        }
        if(popupWindow != null){
            popupWindow.dismiss();
        }
    }
}
