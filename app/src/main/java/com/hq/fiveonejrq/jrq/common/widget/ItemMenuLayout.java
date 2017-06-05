package com.hq.fiveonejrq.jrq.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by guodong on 2017/4/5.
 */

public class ItemMenuLayout extends RelativeLayout {

    /** 根布局 */
    private View mRootView;

    /** 左边图片控件 */
    private ImageView mImageView;

    /** 右边图片控件 */
    private ImageView mArrow;

    /** 标题控件 */
    private TextView mTitle;

    /** 图标 */
    private Drawable mIcon;

    /** 标题 */
    private String text;

    /** 图标 */
    private Drawable mArrowRes;

    /** 布局背景 */
    private int mBackGroundRes;

    /** 标题文字大小 */
    private float mTextSize;

    /** 标题文字颜色 */
    private int mTextColor;

    public ItemMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = LayoutInflater.from(context).inflate(R.layout.itemgroup, this);
        initView();
        initAttrs(context, attrs);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mImageView = (ImageView) mRootView.findViewById(R.id.item_image);
        mTitle = (TextView) mRootView.findViewById(R.id.item_title);
        mArrow = (ImageView) mRootView.findViewById(R.id.item_icon);
    }

    /**
     * 初始化参数以及数据
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.ItemGroup);
        int imageResId = td.getResourceId(R.styleable.ItemGroup_img_item, 0);
        int arrowId = td.getResourceId(R.styleable.ItemGroup_arrow_item, 0);
        int backgroundResId = td.getResourceId(R.styleable.ItemGroup_background_item, 0);
        int textColorResId = td.getResourceId(R.styleable.ItemGroup_textcolor_item, 0);
        int textSize = td.getResourceId(R.styleable.ItemGroup_textsize_item, 0);
        int mTextResid = td.getResourceId(R.styleable.ItemGroup_text_item, 0);

        if(isEmpty(imageResId)){
            mIcon = ContextCompat.getDrawable(context, imageResId);
            mImageView.setImageDrawable(mIcon);
        }

        if(isEmpty(arrowId)){
            mArrowRes = ContextCompat.getDrawable(context, arrowId);
            mArrow.setImageDrawable(mArrowRes);
        }

        if(isEmpty(backgroundResId)){
            mBackGroundRes = ContextCompat.getColor(context, backgroundResId);
            setBackgroundColor(mBackGroundRes);
        }

        if(isEmpty(textColorResId)){
            mTextColor = ContextCompat.getColor(context, textColorResId);
            mTitle.setTextColor(mTextColor);
        }

        if(isEmpty(textSize)){
            mTextSize = context.getResources().getDimension(textSize);
            mTitle.setTextSize(mTextSize);
        }

        if(isEmpty(mTextResid)){
            text = context.getResources().getString(mTextResid);
            mTitle.setText(text);
        }
        td.recycle();
//        setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), mTitle.getText(), Toast.LENGTH_SHORT).show();
                if(null != mListener){
                    mListener.onClick(v);
                }
            }
        });
    }

    private boolean isEmpty(int resId){
        if(resId != 0){
            return true;
        }
        return false;
    }

    public interface OnClickListener{
        void onClick(View view);
    }

    private OnClickListener mListener;

    public void setListener(OnClickListener listener){
        this.mListener = listener;
    }
}
