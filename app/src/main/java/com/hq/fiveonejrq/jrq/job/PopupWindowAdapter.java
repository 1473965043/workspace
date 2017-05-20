package com.hq.fiveonejrq.jrq.job;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.widget.FlowLayout;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/29.
 */

public class PopupWindowAdapter extends RecyclerView.Adapter<PopupWindowAdapter.ViewHolder> {

    /**
     * 数据集合
     */
    private ArrayList<FiltrationTypeBean> list;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 构造函数
     * @param list 数据
     * @param context 上下文
     */
    public PopupWindowAdapter(ArrayList<FiltrationTypeBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_filtration_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FiltrationTypeBean ft = list.get(position);
        holder.title.setText(ft.getTitle());
        addLabel(holder.flowLayout, ft);
        holder.flowLayout.getChildAt(0).setSelected(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
            TextView label = new TextView(context);
            label.setText(ft.getList().get(i));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(25, 20, 0, 0);
            label.setLayoutParams(lp);
            label.setPadding(20, 20, 20, 20);
            label.setTextSize(14);
            label.setBackgroundResource(R.drawable.jobfiltration_item_selector);
            label.setTextColor(context.getResources().getColorStateList(R.color.jobfiltration_item_selector));
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

    /**
     * 清楚所有的item状态
     * @param recyclerview
     */
    public void clearAllFlag(RecyclerView recyclerview){
        for (int i = 0; i < recyclerview.getChildCount(); i++) {
            View childView = recyclerview.getChildAt(i);
            FlowLayout flowlayout = (FlowLayout) childView.findViewById(R.id.flowlayout);
            clearAllFlag(flowlayout);
        }
    }
}
