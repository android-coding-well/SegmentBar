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
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类似ios UISegmentedControl的控件
 */
public class SegmentBar extends View {

    private int normalColor = Color.WHITE;
    private int focusColor = 0xFF1784FD;
    private String textArray = "default";//标签数组，以“|”分割
    private int textSize = sp2px(16);//文字大小
    private int borderWidth = dp2px(0.5f);//边框大小
    private int padding = dp2px(5);//文字上下边距
    private int cornerRadius = dp2px(3);//圆角半径
    private int currentItemIndex = -1;//当前选择标签下标

    private Paint paint;
    private int width;//总宽
    private int height;//总高
    private float minWidth;//最小标签宽度
    private float textHeight;//文字高度
    private String[] texts;//标签数组

    OnItemClickListener listener;
    GradientDrawable gradientDrawable=new GradientDrawable();

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
        textArray = ta.getString(R.styleable.SegmentBar_sbar_textArray);
        textSize = (int) ta.getDimension(R.styleable.SegmentBar_sbar_textSize, textSize);
        borderWidth = (int) ta.getDimension(R.styleable.SegmentBar_sbar_borderWidth, borderWidth);
        padding = (int) ta.getDimension(R.styleable.SegmentBar_sbar_padding, padding);
        cornerRadius = (int) ta.getDimension(R.styleable.SegmentBar_sbar_cornerRadius, cornerRadius);
        currentItemIndex = ta.getInt(R.styleable.SegmentBar_sbar_defaultIndex, currentItemIndex);
        ta.recycle();

        if (TextUtils.isEmpty(textArray)) {
            textArray = "default";
        }
        texts = textArray.split("\\|");

        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(textSize);
        //minWidth = paint.measureText("default") + padding * 2;
        textHeight = paint.descent() - paint.ascent();
        for(int i=0;i<texts.length;i++){
            minWidth+= (paint.measureText(texts[i])+padding * 2);
        }
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
        gradientDrawable.setStroke(borderWidth,focusColor);
        gradientDrawable.setBounds(new Rect(0, 0, width, height));
        gradientDrawable.draw(canvas);

        int count = texts.length;
        for (int i = 0; i < count; i++) {
            if (currentItemIndex == i) {//选中的标签
                gradientDrawable.setColor(focusColor);
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                if(count>1){
                    if(i==0){//画左半圆角矩形
                        gradientDrawable.setCornerRadii(new float[]{cornerRadius,cornerRadius,0,0,0,0,cornerRadius,cornerRadius});//四个圆角的顺序为左上，右上，右下，左下,每个角可以指定[X_Radius,Y_Radius]
                    }else if(i==count-1){//画右半圆角矩形
                        gradientDrawable.setCornerRadii(new float[]{0,0,cornerRadius,cornerRadius,cornerRadius,cornerRadius,0,0});
                    }else{//画直角矩形
                        gradientDrawable.setCornerRadii(new float[]{0,0,0,0,0,0,0,0});
                    }
                }else{
                    gradientDrawable.setCornerRadius(cornerRadius);
                }
                gradientDrawable.setStroke(borderWidth,focusColor);
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

            //画分隔线
            if (i != 0) {
                paint.setColor(focusColor);
                paint.setStrokeWidth(borderWidth * 2 / 3);
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
     * @return 未选中则为-1
     */
    public int getCurrentItem() {
       return currentItemIndex;
    }

    /**
     * 设置标签数组
     * @param texts
     */
    public void setTextArray(String[] texts) {
        if(texts!=null){
            this.texts=texts;
            invalidate();
        }
    }

    /**
     * 设置未选中颜色
     * @param normalColor
     */
    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        invalidate();
    }

    /**
     * 设置选中颜色
     * @param focusColor
     */
    public void setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        invalidate();
    }

    /**
     * 设置标签文字大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置边框大小
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    /**
     * 设置文字上下边距
     * @param padding
     */
    public void setPadding(int padding) {
        this.padding = padding;
        invalidate();
    }

    /**
     * 设置边框圆角半径
     * @param cornerRadius
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    /**
     * 设置标签点击监听器
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

    public interface OnItemClickListener {
        /**
         * 被点击的标签下标
         *
         * @param index
         */
        void onClick(int index);
    }

}