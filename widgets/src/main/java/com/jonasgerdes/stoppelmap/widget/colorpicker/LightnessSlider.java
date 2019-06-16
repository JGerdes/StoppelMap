package com.jonasgerdes.stoppelmap.widget.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.core.graphics.ColorUtils;


public class LightnessSlider extends AbstractSlider {
    private static final String TAG = "LightnessSlider";


    public LightnessSlider(Context context) {
        super(context);
    }

    public LightnessSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LightnessSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawBar(Canvas barCanvas) {
        int width = barCanvas.getWidth();
        int height = barCanvas.getHeight();

        float[] hsl = new float[]{
                color[0],
                color[1],
                color[2]
        };
        int l = Math.max(2, width / 256);
        for (int x = 0; x <= width; x += l) {
            hsl[2] = (float) x / (width - 1);
            barPaint.setColor(ColorUtils.HSLToColor(hsl));
            barCanvas.drawRect(x, 0, x + l, height, barPaint);
        }
    }

    @Override
    int getHandleColor(float[] color, float value) {
        float[] hsl = new float[]{
                color[0],
                color[1],
                color[2]
        };
        hsl[2] = value;
        return ColorUtils.HSLToColor(hsl);
    }

    @Override
    float getValueForColor(float[] color) {
        return color[2];
    }


}
