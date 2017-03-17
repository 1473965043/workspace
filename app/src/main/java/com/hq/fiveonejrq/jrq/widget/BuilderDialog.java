package com.hq.fiveonejrq.jrq.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;

/**
 * 自定义dialog 方法---Builder模式（静态内部类）
 * Created by guodong on 2017/3/13.
 */

public class BuilderDialog extends Dialog {

    public BuilderDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context;
        private String message;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveListener;
        private DialogInterface.OnClickListener negativeListener;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Builder setPositiveButtonText(String positiveButtonText, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButtonText(String negativeButtonText, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeListener = listener;
            return this;
        }

        public BuilderDialog create(){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_layout, null);
            final BuilderDialog dialog = new BuilderDialog(context, R.style.MyDialog);
            dialog.setCanceledOnTouchOutside(false);
            if(null == title){
                try{
                    throw new NullPointerException("title is null object");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                ((TextView) view.findViewById(R.id.title)).setText(title);
            }

            if(null == message){
                try{
                    throw new NullPointerException("message is null object");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                ((TextView) view.findViewById(R.id.message)).setText(message);
            }

            if(null == positiveButtonText){
                try{
                    throw new NullPointerException("positiveButtonText is null object");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                ((Button) view.findViewById(R.id.yes)).setText(positiveButtonText);
                if(positiveListener != null){
                    view.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }

            if(null == negativeButtonText){
                try{
                    throw new NullPointerException("negativeButtonText is null object");
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else{
                ((Button) view.findViewById(R.id.no)).setText(negativeButtonText);
                if(negativeListener != null){
                    view.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            dialog.setContentView(view);
            return dialog;
        }
    }

    /**
     * 显示dialog，并设置背景亮度
     */
    public void showDialog(){
        Window dialogWindow = getWindow();
        WindowManager manager = dialogWindow.getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.dimAmount = 0.5f;
        dialogWindow.setLayout(d.getWidth()*7/10, d.getHeight()/5);
        dialogWindow.setAttributes(lp);
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        show();
    }

}
