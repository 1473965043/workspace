package com.hq.fiveonejrq.jrq.job;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hq.fiveonejrq.jrq.R;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/16.
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    /** 上下文 */
    private Context mContext;
    /** 数据 */
    private ArrayList<JobBean> jobList;

    /**
     * 构造方法
     * @param mContext
     * @param jobList
     */
    public JobsAdapter(Context mContext, ArrayList<JobBean> jobList) {
        this.mContext = mContext;
        this.jobList = jobList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.job_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    /**
     * 获取item个数
     * @return
     */
    @Override
    public int getItemCount() {
//        return jobList.size();
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
