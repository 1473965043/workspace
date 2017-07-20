package com.hq.fiveonejrq.jrq.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;

import java.util.List;

/**
 * Created by guodong on 2017/3/21.
 */

public class MyPopupWindow {

    private Activity activity;
    private Context context;
    private boolean isShow;
    private View rootView;
    private PopupWindow popWindow;
    private final int animDuration = 250;//动画执行时间
    private boolean isAniming;//动画是否在执行

    /**
     * @param itemViews item集合包括内容文字
     */
    public MyPopupWindow(Activity activity, List<BottomItemView> itemViews){

        this.activity = activity;
        initLayout(itemViews);
    }

    public MyPopupWindow(Context context, List<BottomItemView> itemViews){

        this.context = context;
        initLayout(itemViews);
    }

    public void initLayout(List<BottomItemView> itemViews){
        if(activity != null){
            rootView = LayoutInflater.from(activity).inflate(R.layout.item_root_hintpopupwindow, null);
            popWindow = new PopupWindow(activity);
        }else{
            if(context != null){
                rootView = LayoutInflater.from(context).inflate(R.layout.item_root_hintpopupwindow, null);
                popWindow = new PopupWindow(context);
            }else{
                try{
                    throw new NullPointerException("activity与context为空，原因可能是未调用构造函数或者传入的上下文对象为空");
                }catch (NullPointerException e){
                    e.printStackTrace();
                    return;
                }
            }
        }
        for (int i = 0; i < itemViews.size(); i++) {
            final int position = i;
            final View view = LayoutInflater.from(activity).inflate(R.layout.item_hint_popupwindow, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_content);
            ImageView img = (ImageView) view.findViewById(R.id.item_icon);
            View line = view.findViewById(R.id.line);
            tv.setText(itemViews.get(i).getTitle());
            img.setImageResource(itemViews.get(i).getIconId());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
            ((LinearLayout)rootView).addView(view, i, lp);
            if(i == itemViews.size()-1){
                line.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWindow.dismiss();
                    mOnItemOnClickListener.onClick(view, position);
                }
            });
        }
    }

    /**
     * 弹出选项弹窗
     * @param locationView 默认在该view的下方弹出, 和popupWindow类似
     */
    public void showPopupWindow(View locationView){
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setBackgroundDrawable(new ColorDrawable(0xb00000));
        popWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWindow.setContentView(rootView);
        popWindow.showAsDropDown(locationView, 0, 30);
        setBackBrightness();
    }

    /**
     * 并设置背景亮度
     */
    public void setBackBrightness(){
        Window dialogWindow = activity.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public interface OnItemOnClickListener{
        void onClick(View view, int position);
    }

    private OnItemOnClickListener mOnItemOnClickListener;

    public void setOnItemOnClickListener(OnItemOnClickListener listener){
        mOnItemOnClickListener = listener;
    }
}
