package com.hq.fiveonejrq.jrq.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by guodong on 2017/4/21.
 * 自定义view实现幸运大转盘效果 来源 http://blog.csdn.net/lmj623565791/article/details/41722441
 * 测试分支
 */

public class LuckyRotateView extends SurfaceView {

    /**
     * 用于获取canvas
     */
    private SurfaceHolder mHolder;

    /** 用于绘制 */
    private Canvas mCanvas;

    /**
     * 标记，用来控制线程
     */
    private boolean isRunning = false;

    /**
     * 抽奖文字
     */
    private String[] strs = {
            "单反相机", "IPAD", "恭喜发财", "IPHONE", "妹子一只", "恭喜发财"
    };

    /**
     * 与文字对应的图片
     */
    private int[] imgs = {
            R.mipmap.danfan, R.mipmap.ipad, R.mipmap.f040, R.mipmap.iphone, R.mipmap.meizi, R.mipmap.f040
    };

    /**
     * 每个模块的颜色
     */
    private int[] colors = {
            0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01
    };

    /**
     * 与图片对应的bitmap数组
     */
    private Bitmap[] bitmaps;

    /**
     * 盘块数量
     */
    private int count = strs.length;

    /**
     * 绘制盘块的范围
     */
    private RectF mRange;

    /**
     * 圆块的直径
     */
    private int mRadius;

    /**
     * 中心位置
     */
    private int mCenter;

    /**
     * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
     */
    private int mPadding;


    /**
     * 绘制盘块的画笔
     */
    private Paint mArcPaint;

    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;

    /**
     * 背景图
     */
    private Bitmap background;

    /**
     * 圆弧的起始位置
     */
    private float startPosition = 0;

    /**
     * 旋转的速率
     */
    private float mSpeed = 16;

    private boolean isShouldStop = false;

    public LuckyRotateView(Context context){
        super(context, null);
    }

    public LuckyRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        mHolder = getHolder();
        mHolder.addCallback(mCallback);

        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常亮
        setKeepScreenOn(true);

        background = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);
    }

    /**
     * 绘制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        //获取圆的直径
        mRadius = width - getPaddingLeft() -getPaddingRight();
        //中心位置
        mCenter = mRadius/2 + getPaddingLeft();
        //获取padding值
        mPadding = getPaddingLeft();
        setMeasuredDimension(width, width);
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        //创建
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //初始化绘制
            initDraw();
            //开始绘制
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        //摧毁
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isRunning = false;
        }
    };

    /**
     * 改变旋转状态
     */
    public void setRunTag(int luckyNumber){
        luckyStart(luckyNumber);
        mHandler.sendEmptyMessage(1);
    }

    /**
     * 画笔绘制前的初始化操作
     */
    private void initDraw(){
        // 初始化绘制画笔与圆弧
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);//防锯齿
        mArcPaint.setDither(true);//防抖动
        // 初始化绘制文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mTextSize);
        // 圆弧的绘制范围
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(),
                mRadius + getPaddingLeft(), mRadius + getPaddingLeft());
        bitmaps = new Bitmap[count];
        //初始化图片
        for (int i = 0; i < count; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), imgs[i]);
        }
    }

    /**
     * 绘制前准备
     */
    private void readyDraw(){
        try {
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();
            if( endTime - startTime < 50){
                Thread.sleep(50 - (endTime - startTime));
            }

            if(isShouldStop){
                mSpeed -= 1;
                if(mSpeed <= 0){
                    mSpeed = 0;
                    isShouldStop = false;
                    mHandler.removeMessages(1);
                }
            }
            // 根据当前旋转的mStartAngle计算当前滚动到的区域
            calInExactArea(startPosition);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * 根据当前旋转的mStartAngle计算当前滚动到的区域
     *
     * @param startAngle
     */
    public void calInExactArea(float startAngle)
    {
        // 让指针从水平向右开始计算
        float rotate = startAngle + 90;
        rotate %= 360.0;
        for (int i = 0; i < count; i++)
        {
            // 每个的中奖范围
            float from = 360 - (i + 1) * (360 / count);
            float to = from + 360 - (i) * (360 / count);

            if ((rotate > from) && (rotate < to))
            {
                Log.d("TAG", strs[i]);
                return;
            }
        }
    }

    /**
     * 画笔绘制
     */
    private void draw(){
        try {
            mCanvas = mHolder.lockCanvas();
            if(mCanvas != null){
                drawBg();
                drawPan();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 绘制背景
     */
    private void drawBg(){
        mCanvas.drawColor(0xfffff000);
        mCanvas.drawBitmap(background, null, new Rect(mPadding / 2, mPadding / 2,
                getMeasuredWidth() - mPadding / 2, getMeasuredWidth() - mPadding / 2), null);
    }

    /**
     * 绘制转盘
     */
    private void drawPan(){
        float start = startPosition;
        float sweepAngle = (float) (360 / count);
        for (int i = 0; i < count; i++) {
            //绘制扇形块
            mArcPaint.setColor(colors[i]);
            mCanvas.drawArc(
                    mRange,// 扇形区域
                    start,// 起始角度
                    sweepAngle, // 扇形角度s
                    true,
                    mArcPaint // 画笔
            );
            drawText(start, sweepAngle, i);
            drawImg(start, i);
            start += sweepAngle;
        }
        startPosition += mSpeed;
    }

    public void ss(){
        startPosition = 0;
        readyDraw();
    }

    /**
     * 绘制文本
     */
    private void drawText(float start, float sweepAngle, int i){
        Path path = new Path();
        path.addArc(
                mRange, //所在区域
                start, //开始位置
                sweepAngle //结束位置
        );
        float textwidth = mTextPaint.measureText(strs[i]);
        // 利用水平偏移让文字居中
        float hOffset = (float) (mRadius*Math.PI/count/2 - textwidth/2);
        float xOffset = mRadius/2/5;
        mCanvas.drawTextOnPath(
                strs[i], //内容
                path,
                hOffset, //垂直位移
                xOffset, //水平位移
                mTextPaint //画笔
        );
    }

    /**
     * 绘制扇形里面的图片
     */
    private void drawImg(float start, int i){
        // 设置图片的宽度为直径的1/8
        int imgWidth = mRadius / 8;
        // 图片中心的角度
        float angle = (float) ((30 + start) * (Math.PI / 180));
        // 图片中心x坐标
        int x = (int) (mCenter + mRadius / 2 * Math.cos(angle) * 4/7);
        // 图片中心y坐标
        int y = (int) (mCenter + mRadius / 2 * Math.sin(angle) * 4/7);
        // 确定绘制图片的位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(bitmaps[i], null, rect, null);
    }

    /**
     * 根据当前旋转的mStartAngle计算当前滚动到的区域
     * @param luckyNumber 幸运数/中奖数
     */
    private void luckyStart(int luckyNumber){
        //每个单元角度的大小
        float angle = (float) 360 / count;
        // 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
        float min = 270 - luckyNumber * angle;
        float max = min + angle;
        // 停下来时旋转的距离
        float targetMin = 4 * 360 + min;
        float targetMax = 4 * 360 + max;
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetMin) - 1) / 2;
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetMax) - 1) / 2;
        mSpeed = (float) (v1 + Math.random() * (v2 - v1));
        Log.e(getClass().getName(), "v == " + mSpeed);
        isShouldStop = true;
    }

    /**
     * 恢复到初始位置
     */
    public void runStartPosition(int luckyNumber){

    }

    /**
     * 绘制用的线程
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    readyDraw();
                    break;
                case 1:
                    readyDraw();
//                    sendEmptyMessage(1);
                    break;
            }
        }
    };
}
