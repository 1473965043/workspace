package com.hq.fiveonejrq.jrq.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.Utils.Util;
import com.hq.fiveonejrq.jrq.widget.MyPopupWindow;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/2/28.
 */

public class HomePageFragment extends Fragment {

    private LinearLayout layout;
    private ImageView filter;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private MyPopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_layout, container, false);
        Util.getActivity(getActivity()).setStatusBarBackgroundColor("#FB6E05");
        initViews(view);
        initEvents();
        return view;
    }

    private void initViews(View view) {
        layout = (LinearLayout) view.findViewById(R.id.search_linearlayout);
        filter = (ImageView) view.findViewById(R.id.filter);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerview1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerview2);
        recyclerView3 = (RecyclerView) view.findViewById(R.id.content);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        LinearLayoutManager manager3 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(manager1);
        recyclerView2.setLayoutManager(manager2);
        recyclerView3.setLayoutManager(manager3);
        recyclerView1.setAdapter(new MyAdapter());
        recyclerView2.setAdapter(new MyAdapter());
        recyclerView3.setAdapter(new MyAdapter());
    }

    private void initEvents() {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                //用于屏蔽 activity 默认的转场动画效果
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        //下面的操作是初始化弹出数据
        ArrayList<String> strList = new ArrayList<>();
        strList.add("选项item1");
        strList.add("选项item2");
        strList.add("选项item3");
        ArrayList<View.OnClickListener> clickList = new ArrayList<>();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击事件触发", Toast.LENGTH_SHORT).show();
            }
        };
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);
        clickList.add(clickListener);

        //具体初始化逻辑看下面的图
        popupWindow = new MyPopupWindow(getActivity(), strList, clickList);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showPopupWindow(v);
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.homepage_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
