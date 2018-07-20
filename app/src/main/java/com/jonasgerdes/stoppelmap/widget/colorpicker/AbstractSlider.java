package com.jonasgerdes.stoppelmap.widget.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jonasgerdes.stoppelmap.R;


public abstract class AbstractSlider extends View {
    private static final String TAG = "AbstractSlider";

    public interface OnValueChangedListener {
        void onValueChanged(float value);
    }

    protected Bitmap bitmap;
    protected Canvas bitmapCanvas;
    protected Bitmap bar;
    protected Canvas barCanvas;
    protected OnValueChangedListener onValueChangedListener;
    protected int barOffsetX;
    protected int handleRadius = 20;
    protected int barHeight = 5;
    protected float value = 1;
    protected Paint barPaint = new Paint();
    protected Paint solid = new Paint();
    protected Paint clearingStroke = new Paint();
    protected float[] color = new float[3];

    public AbstractSlider(Context context) {
        super(context);
        init();
    }

    public AbstractSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clearingStroke.setColor(0xffffffff);
        clearingStroke.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        barPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        solid.setFlags(Paint.ANTI_ALIAS_FLAG);
        clearingStroke.setFlags(Paint.ANTI_ALIAS_FLAG);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    protected void updateBar() {
        handleRadius = getDimension(R.dimen.color_picker_slider_handler_radius);
        barHeight = getDimension(R.dimen.color_picker_slider_bar_height);
        barOffsetX = handleRadius;

        if (bar == null)
            createBitmaps();
        drawBar(barCanvas);
        invalidate();
    }

    protected void createBitmaps() {
        int width = getWidth();
        int height = getHeight();
        bar = Bitmap.createBitmap(width - barOffsetX * 2, barHeight, Bitmap.Config.ARGB_8888);
        barCanvas = new Canvas(bar);

        if (bitmap == null || bitmap.getWidth() != width || bitmap.getHeight() != height) {
            if (bitmap != null) bitmap.recycle();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bar != null && bitmapCanvas != null) {
            bitmapCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            bitmapCanvas.drawBitmap(bar, barOffsetX, (getHeight() - bar.getHeight()) / 2, null);

            float x = handleRadius + value * (getWidth() - handleRadius * 2);
            float y = getHeight() / 2f;
            drawHandle(bitmapCanvas, x, y);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    protected void onValueChanged(float value) {
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(value);
        }
    }

    protected abstract void drawBar(Canvas barCanvas);

    protected void drawHandle(Canvas canvas, float x, float y) {
        solid.setColor(getHandleColor(color, value));
        canvas.drawCircle(x, y, handleRadius, clearingStroke);
        canvas.drawCircle(x, y, handleRadius * 0.75f, solid);
    }


    public void setColor(float[] color) {
        this.color = color;
        this.value = getValueForColor(color);
        if (bar != null) {
            updateBar();
            invalidate();
        }
    }

    abstract int getHandleColor(float[] color, float value);

    abstract float getValueForColor(float[] color);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateBar();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.UNSPECIFIED)
            width = widthMeasureSpec;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = MeasureSpec.getSize(widthMeasureSpec);
        else if (widthMode == MeasureSpec.EXACTLY)
            width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.UNSPECIFIED)
            height = heightMeasureSpec;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = MeasureSpec.getSize(heightMeasureSpec);
        else if (heightMode == MeasureSpec.EXACTLY)
            height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                if (bar != null) {
                    value = (event.getX() - barOffsetX) / bar.getWidth();
                    value = Math.max(0, Math.min(value, 1));
                    onValueChanged(value);
                    invalidate();
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
            case MotionEvent.ACTION_UP:
                onValueChanged(value);
                invalidate();
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }


    protected int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }
}
