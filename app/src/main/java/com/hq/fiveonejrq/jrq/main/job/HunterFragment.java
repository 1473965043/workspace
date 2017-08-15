package com.hq.fiveonejrq.jrq.job;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.widget.FullyLinearLayoutManager;
import com.hq.fiveonejrq.jrq.common.widget.PullToRefreshLayout;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/17.
 */

public class HunterFragment extends Fragment {

    /** 内容主体控件 */
    private RecyclerView recyclerView;
    /** 适配器 */
    private JobsAdapter adapter;
    /** 数据list */
    private ArrayList<JobBean> mJobsList;
    /** refresh控件 */
    private PullToRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jobs_content_container, container, false);
        initViews(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        FullyLinearLayoutManager manager = new FullyLinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new MyAdapter());
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHodler>{

        @Override
        public ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.job_item_layout, parent, false);
            return new ViewHodler(view);
        }

        @Override
        public void onBindViewHolder(ViewHodler holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 8;
        }

        class ViewHodler extends RecyclerView.ViewHolder{

            public ViewHodler(View itemView) {
                super(itemView);
            }
        }
    }
}
