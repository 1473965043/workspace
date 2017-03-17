package com.hq.fiveonejrq.jrq.job;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hq.fiveonejrq.jrq.R;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/16.
 */

public class JobsAdapter extends RecyclerView.Adapter {

    /** 头部view的ItemViewType */
    private final int headviewType = 1;
    /** 上下文 */
    private Context mContext;
    /** 数据 */
    private ArrayList<JobBean> jobList;
    /** 头部view */
    private View headView;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == headviewType && headView != null){
            return new ViewHolder(headView);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.job_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * 获取item个数
     * @return
     */
    @Override
    public int getItemCount() {
        int count = jobList.size() == 0?0:jobList.size();
        if(headView != null){
            count ++;
        }
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if(isHaveHeadView(position)){
            return headviewType;
        }
        return super.getItemViewType(position);
    }

    /**
     * 添加头部view
     * @param headView
     */
    public void setHeadView(View headView) {
        this.headView = headView;
    }

    /** 判断是否为头部view */
    private boolean isHaveHeadView(int position){
        return headView != null && position == 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
