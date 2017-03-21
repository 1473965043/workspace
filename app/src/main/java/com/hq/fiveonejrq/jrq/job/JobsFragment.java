package com.hq.fiveonejrq.jrq.job;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.widget.FlowLayout;
import com.hq.fiveonejrq.jrq.widget.FullyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/2/28.
 */

public class JobsFragment extends Fragment {

    /**
     * 标题栏textview
     */
    private TextView enterprise, hunter;
    /** */
    private JobBodyFragment body;

    private ImageView jobSearch;

    private ImageView jobFiltration;

    private ViewPager mViewPager;

    private ArrayList<View> list;

    private ArrayList<FiltrationTypeBean> mList;

    private View rootView;

    private PopupWindow popupWindow;

    private WindowManager.LayoutParams lp;

    private Window popWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.jobs_layout, container, false);
        initViews(rootView);
        initEvents();
        return rootView;
    }

    private void initViews(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        list = new ArrayList<>();
        mList = new ArrayList<>();
        enterprise = (TextView) view.findViewById(R.id.job_of_enterprise);
        hunter = (TextView) view.findViewById(R.id.job_of_hunter);
        jobFiltration = (ImageView) view.findViewById(R.id.filtration);
        jobSearch = (ImageView) view.findViewById(R.id.job_search);
    }

    private void initEvents() {
        setData(3);
        jobFiltration.setOnClickListener(listener);
        jobSearch.setOnClickListener(listener);
        enterprise.setOnClickListener(listener);
        hunter.setOnClickListener(listener);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.job_item_layout, null);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.homepage_layout, null);
        list.add(view);
        list.add(view1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
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

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(list.get(position));
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));
            }
        });

        mViewPager.setCurrentItem(0);
        enterprise.setSelected(true);
    }

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
                        popupWindow.showAsDropDown(rootView.findViewById(R.id.rootview));
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
            }
        }
    };

    private void setData(int size){
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
        mList.addAll(list);
    }

    /**
     * 显示弹出框
     */
    private void showPopwindow(){
        popupWindow = new PopupWindow(getContext());
        View pop = LayoutInflater.from(getContext()).inflate(R.layout.job_filtration_layout, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F8FD")));
        popWindow = getActivity().getWindow();
        lp = popWindow.getAttributes();
        lp.alpha = 0.7f;
        popWindow.setAttributes(lp);
        RecyclerView recyclerView = (RecyclerView) pop.findViewById(R.id.filtration_recyclerview);
        //设置布局管理器
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        //设置适配器
        recyclerView.setAdapter(new MyAdapter());
        //设置布局
        popupWindow.setContentView(pop);
        //显示位置
        popupWindow.showAsDropDown(rootView.findViewById(R.id.rootview));
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

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.job_filtration_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FiltrationTypeBean ft = mList.get(position);
            holder.title.setText(ft.getTitle());
            addLabel(holder.flowLayout, ft);
            holder.flowLayout.getChildAt(0).setSelected(true);
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            private FlowLayout flowLayout;
            private TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                flowLayout = (FlowLayout) itemView.findViewById(R.id.flowlayout);
                title = (TextView) itemView.findViewById(R.id.title);
            }
        }

        /**
         * 添加标签
         */
        private void addLabel(final FlowLayout flowLayout, FiltrationTypeBean ft){
            for (int i = 0; i < ft.getList().size(); i++) {
                final int position = i;
                TextView label = new TextView(getContext());
                label.setText(ft.getList().get(i));
                label.setBackgroundResource(R.drawable.jobfiltration_item_selector);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 15, 0, 0);
                label.setLayoutParams(lp);
                label.setPadding(15, 10, 15, 10);
                label.setBackgroundResource(R.drawable.jobfiltration_item_selector);
                label.setTextColor(ContextCompat.getColor(getContext(), R.color.jobfiltration_item_selector));
                flowLayout.addView(label);
                label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position != 0){
                            clearFlag(position, flowLayout);
                        }else{
                            clearAllFlag(flowLayout);
                        }
                    }
                });
            }
        }

        private void clearFlag(int position, FlowLayout flowlayout){
            View child = flowlayout.getChildAt(position);
            if(child.isSelected()){
                child.setSelected(false);
            }else{
                child.setSelected(true);
            }
            flowlayout.getChildAt(0).setSelected(false);
        }

        private void clearAllFlag(FlowLayout flowlayout){
            for (int i = 0; i < flowlayout.getChildCount(); i++) {
                if(i == 0){
                    flowlayout.getChildAt(i).setSelected(true);
                }else{
                    flowlayout.getChildAt(i).setSelected(false);
                }
            }
        }

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

    //    /**
//     * 初始化控件
//     * @param view
//     */
//    private void initViews(View view) {
//        enterprise = (TextView) view.findViewById(R.id.job_of_enterprise);
//        hunter = (TextView) view.findViewById(R.id.job_of_hunter);
//        body = new JobBodyFragment();
//    }

//    /**
//     * 初始化数据
//     */
//    private void initEvents() {
//        enterprise.setSelected(true);
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.job_content_fragment, body).commit();
//        hunter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hunter.setSelected(true);
//                enterprise.setSelected(false);
//            }
//        });
//
//        enterprise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hunter.setSelected(false);
//                enterprise.setSelected(true);
//            }
//        });
//    }
}
