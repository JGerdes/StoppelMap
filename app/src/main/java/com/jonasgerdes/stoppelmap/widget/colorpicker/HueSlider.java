package com.jonasgerdes.stoppelmap.widget.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;


public class HueSlider extends AbstractSlider {
    private static final String TAG = "HueSlider";


    public HueSlider(Context context) {
        super(context);
    }

    public HueSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HueSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawBar(Canvas barCanvas) {
        int width = barCanvas.getWidth();
        int height = barCanvas.getHeight();

        float[] hsl = new float[]{
                color[0],
                1f,
                0.5f
        };
        int l = Math.max(2, width / 256);
        for (int x = 0; x <= width; x += l) {
            hsl[0] = (float) x / (width - 1);
            hsl[0] *= 360;
            barPaint.setColor(ColorUtils.HSLToColor(hsl));
            barCanvas.drawRect(x, 0, x + l, height, barPaint);
        }
    }

    protected void onValueChanged(float value) {
        if (onValueChangedListener != null) {
            onValueChangedListener.onValueChanged(value * 360);
        }
    }

    @Override
    int getHandleColor(float[] color, float value) {
        value *= 360;
        return ColorUtils.HSLToColor(new float[]{
                value,
                1f,
                0.5f
        });
    }

    @Override
    float getValueForColor(float[] color) {
        return color[0] / 360f;
    }


}
