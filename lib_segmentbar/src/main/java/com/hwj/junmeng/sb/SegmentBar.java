package com.hwj.junmeng.sb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类似ios UISegmentedControl的控件
 */
public class SegmentBar extends View {

    private static final String TAG = "SegmentBar";

    private int normalColor = Color.WHITE;
    private int focusColor = 0xFF1784FD;
    private String textArray = "default";               //标签数组，以“|”分割
    private float textSize = sp2px(16);                 //文字大小
    private float borderWidth = dp2px(0.5f);            //边框大小
    private float padding = dp2px(5);                   //文字上下边距
    private float cornerRadius = dp2px(3);              //圆角半径
    private int currentItemIndex = -1;                  //当前选择标签下标


    private float unreadTextSize = sp2px(8);            //未读数大小
    private float unreadMarginTop = dp2px(2);           //未读数上边距
    private float unreadMarginRight = dp2px(5);         //未读数右边距
    private float unreadPadding = dp2px(1);             //未读数内边距
    private int unreadBackgroundColor = Color.RED;      //未读数背景色
    private int unreadTextColor = Color.WHITE;          //未读数颜色


    private Paint paint;
    private Paint unreadPaint;          //用于绘制未读数
    private int width;                  //控件总宽
    private int height;                 //控件总高
    private float minWidth;             //最小标签宽度
    private float textHeight;           //标签文字高度
    private float unreadTextHeight;     //未读数文字高度

    private String[] texts;//标签数组
    private int[] unreadTexts;//标签未读数数组

    OnItemClickListener listener;
    GradientDrawable gradientDrawable = new GradientDrawable();

    public SegmentBar(Context context) {
        this(context, null, 0);
    }

    public SegmentBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SegmentBar, defStyleAttr, 0);
        normalColor = ta.getColor(R.styleable.SegmentBar_sbar_normalColor, normalColor);
        focusColor = ta.getColor(R.styleable.SegmentBar_sbar_focusColor, focusColor);
        textArray = ta.getString(R.styleable.SegmentBar_sbar_labelArray);
        textSize = (int) ta.getDimension(R.styleable.SegmentBar_sbar_labelTextSize, textSize);
        borderWidth = (int) ta.getDimension(R.styleable.SegmentBar_sbar_borderWidth, borderWidth);
        padding = (int) ta.getDimension(R.styleable.SegmentBar_sbar_labelPadding, padding);
        cornerRadius = (int) ta.getDimension(R.styleable.SegmentBar_sbar_cornerRadius, cornerRadius);
        currentItemIndex = ta.getInt(R.styleable.SegmentBar_sbar_defaultIndex, currentItemIndex);
        unreadTextSize = (int) ta.getDimension(R.styleable.SegmentBar_sbar_unreadTextSize, unreadTextSize);
        unreadMarginTop = (int) ta.getDimension(R.styleable.SegmentBar_sbar_unreadMarginTop, unreadMarginTop);
        unreadMarginRight = (int) ta.getDimension(R.styleable.SegmentBar_sbar_unreadMarginRight, unreadMarginRight);
        unreadPadding = (int) ta.getDimension(R.styleable.SegmentBar_sbar_unreadPadding, unreadPadding);
        unreadBackgroundColor = ta.getColor(R.styleable.SegmentBar_sbar_unreadBackgroundColor, unreadBackgroundColor);
        unreadTextColor = ta.getColor(R.styleable.SegmentBar_sbar_unreadTextColor, unreadTextColor);
        ta.recycle();

        if (TextUtils.isEmpty(textArray)) {
            textArray = "default";
        }
        texts = textArray.split("\\|");
        unreadTexts = new int[texts.length];

        initPaint();
        //minWidth = paint.measureText("default") + padding * 2;
        textHeight = paint.descent() - paint.ascent();
        for (int i = 0; i < texts.length; i++) {
            minWidth += (paint.measureText(texts[i]) + padding * 2);
        }
    }

    private void initPaint() {
        unreadPaint = new Paint();
        unreadPaint.setAntiAlias(true);//抗锯齿
        unreadPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        unreadPaint.setStrokeCap(Paint.Cap.ROUND);
        unreadPaint.setColor(unreadBackgroundColor);
        unreadPaint.setStyle(Paint.Style.FILL);
        unreadPaint.setTextSize(unreadTextSize);
        unreadTextHeight = unreadPaint.descent() - unreadPaint.ascent();


        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算控件宽高
        width = resolveSize((int) minWidth, widthMeasureSpec);
        height = (int) (textHeight + 2 * padding);
        //设置控件宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画背景及边框
        gradientDrawable.setColor(normalColor);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setStroke((int) borderWidth, focusColor);
        gradientDrawable.setBounds(new Rect(0, 0, width, height));
        gradientDrawable.draw(canvas);

        int count = texts.length;
        int labelWidth = width / count;//每个标签占据的宽度
        for (int i = 0; i < count; i++) {
            if (currentItemIndex == i) {//选中的标签
                gradientDrawable.setColor(focusColor);
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                if (count > 1) {
                    if (i == 0) {//画左半圆角矩形
                        gradientDrawable.setCornerRadii(new float[]{cornerRadius, cornerRadius, 0, 0, 0, 0, cornerRadius, cornerRadius});//四个圆角的顺序为左上，右上，右下，左下,每个角可以指定[X_Radius,Y_Radius]
                    } else if (i == count - 1) {//画右半圆角矩形
                        gradientDrawable.setCornerRadii(new float[]{0, 0, cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0});
                    } else {//画直角矩形
                        gradientDrawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                    }
                } else {
                    gradientDrawable.setCornerRadius(cornerRadius);
                }
                gradientDrawable.setStroke((int) borderWidth, focusColor);
                gradientDrawable.setBounds(new Rect(i * width / count, 0, (i + 1) * width / count, height));
                gradientDrawable.draw(canvas);
                paint.setColor(normalColor);
            } else {
                paint.setColor(focusColor);
            }

            //画文字
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);//居中画文字
            canvas.drawText(texts[i], width * (2 * i + 1) / (count * 2), height / 2 + textHeight / 3, paint);//x:文字中点,y:baseline坐标（baseline坐标无法使文字居中，故在此加上文字高度的三分之一）

            //画红点
            unreadPaint.setTextSize(unreadTextSize);
            unreadPaint.setColor(unreadBackgroundColor);
            int unreadCount = unreadTexts[i];
            int unreadTextWidth = getTextWidth(unreadPaint, "" + unreadTexts[i]);
            unreadTextHeight = unreadPaint.descent() - unreadPaint.ascent();
            float redRadius = unreadTextHeight / 2.0f + unreadPadding;//红点的半径
            float redCenterX = labelWidth * (i + 1) - redRadius - unreadMarginRight;//红点的中心x
            float redCenterY = unreadMarginTop + redRadius;

            if (unreadCount == -1) {//显示红点
                canvas.drawCircle(redCenterX, redCenterY, redRadius, unreadPaint);
            } else if (unreadCount > 0) {//显示红点加数字

                canvas.drawCircle(redCenterX, redCenterY, redRadius, unreadPaint);

                unreadPaint.setColor(unreadTextColor);
                unreadPaint.setTextSize(unreadTextSize);
                unreadPaint.setStrokeWidth(borderWidth);
                //未读数文字的宽度
                canvas.drawText("" + unreadTexts[i], redCenterX - unreadTextWidth / 2.0f, redCenterY + unreadTextHeight / 2.0f - unreadTextHeight / 4.0f, unreadPaint);
            }


            //画分隔线
            if (i != 0) {
                paint.setColor(focusColor);
                paint.setStrokeWidth(borderWidth);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawLine((width * i) / count, 0, (width * i) / count, height, paint);//两点坐标
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int count = texts.length;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                int index = (int) ((event.getX() / width) * count);
                if (listener != null) {
                    listener.onClick(index);
                }
                setCurrentItem(index);
                break;
        }
        return true;

    }




    //*******************************对外开放接口****************************************

    /**
     * 设置未读数文字大小
     *
     * @param sp
     */
    public void setUnreadTextSize(float sp) {
        this.unreadTextSize = sp2px(sp);
        Log.i(TAG, "unreadTextSize=" + unreadTextSize);
        invalidate();
    }

    /**
     * 设置标签未读个数
     *
     * @param labelIndex
     * @param unreadCount
     */
    public void setLabelUnreadCount(int labelIndex, int unreadCount) {
        if (labelIndex < texts.length && labelIndex >= 0) {
            unreadTexts[labelIndex] = unreadCount;
            invalidate();
        }
    }

    /**
     * 设置未读数颜色，默认白
     */
    public void setUnreadTextColor(int color) {
        this.unreadTextColor = color;
        invalidate();
    }

    /**
     * 设置未读数背景色，默认红
     *
     * @param color
     */
    public void setUnreadBackgroundColor(int color) {
        this.unreadBackgroundColor = color;
        invalidate();
    }

    /**
     * 设置未读数上边距
     *
     * @param dp
     */
    public void setUnreadMarginTop(float dp) {
        this.unreadMarginTop = dp2px(dp);
        invalidate();
    }

    /**
     * 设置未读数右边距
     *
     * @param dp
     */
    public void setUnreadMarginRight(float dp) {
        this.unreadMarginRight = dp2px(dp);
        invalidate();
    }

    /**
     * 设置未读数内边距
     *
     * @param dp
     */
    public void setUnreadPadding(float dp) {
        this.unreadPadding = dp2px(dp);
        invalidate();
    }

    /**
     * 设置当前标签下标
     *
     * @param index
     */
    public void setCurrentItem(int index) {
        if (index < texts.length) {
            currentItemIndex = index;
            invalidate();
        }
    }

    /**
     * 返回当前选中标签下标
     *
     * @return 未选中则为-1
     */
    public int getCurrentItem() {
        return currentItemIndex;
    }

    /**
     * 设置标签数组
     *
     * @param texts
     */
    public void setLabelTextArray(String[] texts) {
        if (texts != null) {
            this.texts = texts;
            this.unreadTexts = new int[texts.length];
            invalidate();
        }
    }

    /**
     * 获得标签个数
     *
     * @return
     */
    public int getLabelCount() {
        if (texts != null) {
            return texts.length;
        }
        return 0;
    }

    /**
     * 设置未选中颜色
     *
     * @param normalColor
     */
    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        invalidate();
    }

    /**
     * 设置选中颜色
     *
     * @param focusColor
     */
    public void setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        invalidate();
    }

    /**
     * 设置标签文字大小
     *
     * @param textSize
     */
    public void setLabelTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    /**
     * 设置边框大小
     *
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    /**
     * 设置文字上下边距
     *
     * @param padding
     */
    public void setLabelPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    /**
     * 设置边框圆角半径
     *
     * @param dp
     */
    public void setCornerRadius(float dp) {
        this.cornerRadius = dp2px(dp);
        invalidate();
    }

    /**
     * 设置标签点击监听器
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    /**
     * 计算文字宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public interface OnItemClickListener {
        /**
         * 被点击的标签下标
         *
         * @param index
         */
        void onClick(int index);
    }

}