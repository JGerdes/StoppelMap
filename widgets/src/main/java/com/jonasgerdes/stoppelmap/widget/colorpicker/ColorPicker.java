package com.jonasgerdes.stoppelmap.widget.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.core.graphics.ColorUtils;
import com.jonasgerdes.stoppelmap.widget.R;
import com.jonasgerdes.stoppelmap.widget.util.MathUtil;

/**
 * Created by jonas on 22.02.2017.
 */

public class ColorPicker extends LinearLayout {

    public static final float LOWER_LIMIT = 1f / 200f;
    public static final float UPPER_LIMIT = 1f - LOWER_LIMIT;

    public interface ColorChangeListener {
        void onColorChanged(int newColor);
    }

    HueSlider mHueSlider;
    SaturationSlider mSaturationSlider;
    LightnessSlider mLightnessSlider;

    private float[] mColor = new float[3];
    private boolean mPreventZeroValues;

    private ColorChangeListener mChangeListener;


    public ColorPicker(Context context) {
        super(context);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHueSlider = findViewById(R.id.slider_hue);
        mSaturationSlider = findViewById(R.id.slider_saturation);
        mLightnessSlider = findViewById(R.id.slider_lightness);

        mHueSlider.setOnValueChangedListener(new AbstractSlider.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mColor[0] = value;
                mSaturationSlider.setColor(mColor);
                mLightnessSlider.setColor(mColor);
                triggerListener();
            }
        });

        mSaturationSlider.setOnValueChangedListener(new AbstractSlider.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mColor[1] = value;
                mHueSlider.setColor(mColor);
                mLightnessSlider.setColor(mColor);
                triggerListener();
            }
        });

        mLightnessSlider.setOnValueChangedListener(new AbstractSlider.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                mColor[2] = value;
                mHueSlider.setColor(mColor);
                mSaturationSlider.setColor(mColor);
                triggerListener();
            }
        });
    }

    private void triggerListener() {
        if (mChangeListener != null) {
            float[] newValues = mColor;
            if (mPreventZeroValues) {
                newValues = new float[]{
                        mColor[0],
                        MathUtil.limit(LOWER_LIMIT, UPPER_LIMIT, mColor[1]),
                        MathUtil.limit(LOWER_LIMIT, UPPER_LIMIT, mColor[2])
                };
            }
            int newColor = ColorUtils.HSLToColor(newValues);
            mChangeListener.onColorChanged(newColor);
        }
    }

    public void setColor(int color) {
        ColorUtils.colorToHSL(color, mColor);
        mHueSlider.setColor(mColor);
        mSaturationSlider.setColor(mColor);
        mLightnessSlider.setColor(mColor);
    }

    public ColorPicker setChangeListener(ColorChangeListener changeListener) {
        mChangeListener = changeListener;
        return this;
    }

    public ColorPicker setPreventZeroValues(boolean preventZeroValues) {
        mPreventZeroValues = preventZeroValues;
        return this;
    }
}
