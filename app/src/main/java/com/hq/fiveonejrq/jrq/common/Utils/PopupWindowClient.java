package com.hq.fiveonejrq.jrq.common.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by guodong on 2017/7/28.
 * PopupWindiws封装类 --- 采用建造者模式
 */

public class PopupWindowClient {

    private static int[] defaultPixels = {720, 1280};

    private Context mContext;

    private View contentView;

    private int layoutId;

    private Drawable backgroundDrawable;

    private View relativeView, parentView;

    private boolean focusable = false;

    private PopupWindow.OnDismissListener dismissListener;

    private int[] size = new int[2];

    private int[] position = new int[2];

    private int[] pixels = new int[2];

    private PopupWindow mPopupWindow;

    private int animationStyle, gravity;

    private PopupWindowClient(Builder builder){
        this.backgroundDrawable = builder.backgroundDrawable;
        this.contentView = builder.contentView;
        this.dismissListener = builder.dismissListener;
        this.focusable = builder.focusable;
        this.layoutId = builder.layoutId;
        this.mContext = builder.mContext;
        this.pixels = builder.pixels;
        this.position = builder.position;
        this.size = builder.size;
        this.relativeView = builder.relativeView;
        this.animationStyle = builder.animationStyle;
        this.parentView = builder.parentView;
        this.gravity = builder.gravity;
    }

    private void setPopupWindow(PopupWindow popupWindow){
        this.mPopupWindow = popupWindow;
    }

    /**
     * 弹出弹窗
     */
    public void show(){

        if(this.parentView != null){
            mPopupWindow.showAtLocation(parentView, gravity, position[0], position[1]);//设置窗体显示位置
            return;
        }

        if(this.relativeView != null){
            mPopupWindow.showAsDropDown(relativeView, position[0], position[1]);//设置窗体显示位置
        }
//        else{
//            try{
//                throw new NullPointerException("relativeView不能为空");
//            }catch (Exception e){
//                Toast.makeText(mContext, "showAsDropDown方法中的view不能为空！", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
    }

    /**
     * 关闭弹窗
     */
    public void close(){
        if(mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }

    public static class Builder{

        private Context mContext;

        private View contentView;

        private int layoutId;

        private Drawable backgroundDrawable;

        private View relativeView, parentView;

        private boolean focusable = false;

        private PopupWindow.OnDismissListener dismissListener;

        private int[] size = new int[2];

        private int[] position = new int[2];

        private int[] pixels = new int[2];

        private int animationStyle, gravity;

        public Builder(Context context){
            this.mContext = context;
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            this.pixels[0] = dm.widthPixels;
            this.pixels[1] = dm.heightPixels;
        }

        public Builder setContentView(int layoutId){
            this.layoutId = layoutId;
            return this;
        }

        public Builder setContentView(View contentView){
            this.contentView = contentView;
            return this;
        }

        public Builder setBackgroundDrawable(Drawable backgroundDrawable){
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder setOnDismissListener(PopupWindow.OnDismissListener dismissListener){
            this.dismissListener = dismissListener;
            return this;
        }

        /**
         * 宽高直接赋值（默认的分辨率defaultPixels；算按比例来算,用于适配）
         * @param width 宽度
         * @param height 高度
         * @return
         */
        public Builder setDimension(int width, int height){
            if(width == -1 || width == -2){
                this.size[0] = width;
            }else{
                this.size[0] = pixels[0] * width/defaultPixels[0];
            }

            if(height == -1 || height ==-2){
                this.size[1] = height;
            }else{
                this.size[1] = pixels[1] * height/defaultPixels[1];
            }
            return this;
        }

        /**
         * 已屏幕宽高为基础，输入比例来获取宽高
         * @param wp 宽的比例
         * @param hp 高的比例
         * @return
         */
        public Builder setProportion(double wp, double hp){
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            if(wp == -1.0 || wp == -2.0){
                this.size[0] = (int) wp;
            }else{
                this.size[0] = (int) (dm.widthPixels * wp);
            }

            if(hp == -1.0 || wp == -2.0){
                this.size[1] = (int) hp;
            }else{
                this.size[1] = (int) (dm.heightPixels * hp);
            }
            return this;
        }

        public Builder setFocusable(boolean focusable){
            this.focusable = focusable;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle){
            this.animationStyle = animationStyle;
            return this;
        }

        public Builder showAsDropDown(View relativeView){
            this.relativeView = relativeView;
            return this;
        }

        public Builder showAsDropDown(View relativeView, int xoff, int yoff){
            this.relativeView = relativeView;
            this.position[0] = pixels[0] * xoff/defaultPixels[0];
            this.position[1] = pixels[1] * yoff/defaultPixels[1];
            return this;
        }

        public Builder showAtLocation(View parent, int gravity, int xoff, int yoff){
            this.parentView = parent;
            this.gravity = gravity;
            this.position[0] = pixels[0] * xoff/defaultPixels[0];
            this.position[1] = pixels[1] * yoff/defaultPixels[1];
            return this;
        }

        public PopupWindowClient build(){
            PopupWindow popupWindow = new PopupWindow(mContext);
            if(contentView != null){
                popupWindow.setContentView(contentView);
            }else{
                if(layoutId != 0){
                    contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
                    popupWindow.setContentView(contentView);
                }else{
                    try{
                        throw new NullPointerException("contentView不能为空");
                    }catch (Exception e){
                        Toast.makeText(mContext, "contentView不能为空!", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            }
            popupWindow.setBackgroundDrawable(backgroundDrawable);//设置背景
            if(dismissListener != null){
                popupWindow.setOnDismissListener(dismissListener);//设置窗口关闭监听
            }
            if(size[0] > 0 || size[0] == -1 || size[0] == -2){
                popupWindow.setWidth(size[0]);//设置窗口宽度
            }else{
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//设置窗口宽度
            }

            if(size[1] > 0 || size[1] == -1 || size[1] == -2){
                popupWindow.setHeight(size[1]);//设置窗口宽度
            }else{
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置窗口宽度
            }
            popupWindow.setAnimationStyle(animationStyle);
            popupWindow.setFocusable(focusable);
            PopupWindowClient client = new PopupWindowClient(this);
            client.setPopupWindow(popupWindow);
            return client;
        }
    }
}
