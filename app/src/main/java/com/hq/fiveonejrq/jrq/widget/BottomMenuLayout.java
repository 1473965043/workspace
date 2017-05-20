package com.hq.fiveonejrq.jrq.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.base.BottomItemView;

import java.util.List;

/**
 * Created by guodong on 2016/11/24.
 */

public class BottomMenuLayout extends LinearLayout {

    private List<BottomItemView> mItemView;
    private FragmentManager fragmentManager;
    private Context mContext;
    private int containerId;
    private OnChangeListener mChangeListener;
    private int current;

    public interface OnChangeListener{
        void change(int position);
    }

    public void setChangeListener(OnChangeListener listener){
        mChangeListener = listener;
    }

    /**
     * 构造方法-->new出来时调用
     * @param context
     */
    public BottomMenuLayout(Context context) {
        super(context);
    }

    /**
     * 构造方法-->布局文件中调用
     * @param context
     * @param attrs
     */
    public BottomMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * 测量宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 初始化数据
     */
    public void setDatas(FragmentActivity fragmentActivity, int containerId, List<BottomItemView> itemView){
        fragmentManager = fragmentActivity.getSupportFragmentManager();
        mItemView = itemView;
        this.containerId = containerId;
        initItemView();
        initFragments();
    }

    /**
     * 初始化BottomIteView
     */
    private void initItemView() {
        for(int i=0; i<mItemView.size(); i++){
            View view = LayoutInflater.from(mContext).inflate(R.layout.bottommenu_layout, null);
            addItemView(i, view);
        }
    }

    /**
     * 添加itemView
     * @param position
     * @param view
     */
    private void addItemView(final int position, View view){
        ImageView img = (ImageView) view.findViewById(R.id.bottom_icon);
        TextView title = (TextView) view.findViewById(R.id.bottom_title);
        img.setImageResource(mItemView.get(position).getIconId());
        title.setText(mItemView.get(position).getTitle());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentFragment(position);
                if(null != mChangeListener){
                    mChangeListener.change(position);
                }
            }
        });
        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        addView(view, position, lp_tab);
    }

    /**
     * 更新UI
     * @param current
     */
    private void notifyDataSetChanged(int current){
        for(int i=0; i<getChildCount(); i++){
            if(current == i){
                getChildAt(i).setSelected(true);
                getChildAt(i).findViewById(R.id.bottom_icon).setSelected(true);
                getChildAt(i).findViewById(R.id.bottom_title).setSelected(true);
            }else{
                getChildAt(i).setSelected(false);
                getChildAt(i).findViewById(R.id.bottom_icon).setSelected(false);
                getChildAt(i).findViewById(R.id.bottom_title).setSelected(false);
            }
        }
    }

    /**
     * 设置当前显示的fragment
     */
    public void setCurrentFragment(int current){
        for(int i=0; i<mItemView.size(); i++){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment fragment = mItemView.get(i).getFragment();
            if(i == current){
                ft.show(fragment);
            }else{
                ft.hide(fragment);
            }
            ft.commit();
        }
        this.current = current;
        notifyDataSetChanged(current);
    }

    /**
     * 获取当前fragment的下标
     * @return
     */
    public int getCurrent(){
        return this.current;
    }

    /**
     * 初始化Fragment
     */
    private void initFragments(){
        for (BottomItemView itemView:mItemView) {
            fragmentManager.beginTransaction().add(containerId, itemView.getFragment()).hide(itemView.getFragment()).commit();
        }
        setCurrentFragment(0);
    }
}
