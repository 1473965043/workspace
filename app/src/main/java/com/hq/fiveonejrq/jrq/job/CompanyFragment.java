package com.hq.fiveonejrq.jrq.job;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by Administrator on 2017/3/29.
 */

public class CompanyFragment extends Fragment {

    /** 内容主体控件 */
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_content_container, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View root) {
        recyclerView = (RecyclerView) ((LinearLayout) root.findViewById(R.id.refreshlayout)).getChildAt(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        public class ViewHodler extends RecyclerView.ViewHolder{

            public ViewHodler(View itemView) {
                super(itemView);
            }
        }
    }
}
