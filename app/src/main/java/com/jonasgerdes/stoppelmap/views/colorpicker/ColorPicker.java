package com.jonasgerdes.stoppelmap.views.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.jonasgerdes.stoppelmap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 22.02.2017.
 */

public class ColorPicker extends LinearLayout {

    public interface ColorChangeListener {
        void onColorChanged(int newColor);
    }

    @BindView(R.id.slider_hue)
    HueSlider mHueSlider;

    @BindView(R.id.slider_saturation)
    SaturationSlider mSaturationSlider;

    @BindView(R.id.slider_lightness)
    LightnessSlider mLightnessSlider;

    private float[] mColor = new float[3];

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
        ButterKnife.bind(this);

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
            int newColor = ColorUtils.HSLToColor(mColor);
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
}
