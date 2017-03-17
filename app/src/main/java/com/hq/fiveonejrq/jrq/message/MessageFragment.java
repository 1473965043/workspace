package com.hq.fiveonejrq.jrq.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.widget.RefreshView;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MessageFragment extends Fragment {

    RefreshView refreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, container, false);
        MyAdapter adapter = new MyAdapter();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        refreshView = (RefreshView) view.findViewById(R.id.refreshview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshView.setOnRefreshListener(listener);
        return view;
    }

    private Handler mhandler = new Handler();

    private RefreshView.OnRefreshListener listener = new RefreshView.OnRefreshListener() {
        @Override
        public void onRefresh(final RefreshView refreshView) {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshView.refreshFinish();
                }
            }, 1500);
        }

        @Override
        public void onLoadMore(RefreshView refreshView) {

        }
    };

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(getContext());
            tv.setText("你好！");
            tv.setHeight(40);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
