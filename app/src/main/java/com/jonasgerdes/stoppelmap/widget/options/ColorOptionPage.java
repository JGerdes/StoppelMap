package com.jonasgerdes.stoppelmap.widget.options;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;
import com.jonasgerdes.stoppelmap.views.colorpicker.ColorPicker;
import com.jonasgerdes.stoppelmap.widget.ColorableWidgetPreview;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class ColorOptionPage extends OptionPage<ColorableWidgetPreview>
        implements View.OnClickListener {

    private static final float MIN_LIGHTNESS = 0.2f;
    private static final float MAX_LIGHTNESS = 0.8f;
    @BindView(R.id.widget_color_selection_list)
    ViewGroup mColorList;

    @BindView(R.id.color_secection_dominant)
    CardView mColorSelectionDominant;

    @BindView(R.id.color_secection_vibrant)
    CardView mColorSelectionVibrant;

    @BindView(R.id.color_secection_muted)
    CardView mColorSelectionMuted;

    @BindView(R.id.color_picker)
    ColorPicker mColorPicker;

    @BindViews({ R.id.from_wallpaper_list, R.id.from_wallpaper_text})
    List<View> mFromWallpaper;

    private int mDefaultColor;
    private Palette mPalette;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_color_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        for (int i = 0; i < mColorList.getChildCount(); i++) {
            mColorList.getChildAt(i).setOnClickListener(this);
        }

        mColorPicker.setPreventZeroValues(true)
                .setChangeListener(new ColorPicker.ColorChangeListener() {
                    @Override
                    public void onColorChanged(int newColor) {
                        getEditableWidgetPreview().setColorsBy(newColor);
                        getWidgetPreview().update();
                    }
                })
                .setColor(mDefaultColor);


        setUpPalette();

    }

    private void setUpPalette() {
        if (mPalette == null
                || mColorSelectionDominant == null
                || mColorSelectionVibrant == null
                || mColorSelectionMuted == null) {
            return;
        }

        boolean atLeastOneWallpaperColorUsable = false;

        final int dominantColor = mPalette.getDominantColor(Color.BLACK);
        if (isLightnessIsBetween(dominantColor, MIN_LIGHTNESS, MAX_LIGHTNESS)) {
            mColorSelectionDominant.setCardBackgroundColor(dominantColor);
            mColorSelectionDominant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int color = dominantColor;
                    getEditableWidgetPreview().setColorsBy(color);
                    mColorPicker.setColor(color);
                    getWidgetPreview().update();
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionDominant.setVisibility(View.GONE);
        }

        final int vibrantColor = mPalette.getVibrantColor(Color.BLACK);
        if (isLightnessIsBetween(vibrantColor, MIN_LIGHTNESS, MAX_LIGHTNESS)) {
            mColorSelectionVibrant.setCardBackgroundColor(vibrantColor);
            mColorSelectionVibrant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] colors = {
                            mPalette.getLightVibrantColor(vibrantColor),
                            vibrantColor,
                            mPalette.getDarkVibrantColor(vibrantColor)
                    };
                    mColorPicker.setColor(vibrantColor);
                    getEditableWidgetPreview().setColors(colors);
                    getWidgetPreview().update();
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionVibrant.setVisibility(View.GONE);
        }

        final int mutedColor = mPalette.getMutedColor(Color.BLACK);
        if (isLightnessIsBetween(mutedColor, MIN_LIGHTNESS, MAX_LIGHTNESS)) {
            mColorSelectionMuted.setCardBackgroundColor(mutedColor);
            mColorSelectionMuted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] colors = {
                            mPalette.getLightMutedColor(mutedColor),
                            mutedColor,
                            mPalette.getDarkMutedColor(mutedColor)
                    };
                    mColorPicker.setColor(mutedColor);
                    getEditableWidgetPreview().setColors(colors);
                    getWidgetPreview().update();
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionMuted.setVisibility(View.GONE);
        }

        if (!atLeastOneWallpaperColorUsable) {
            ButterKnife.apply(mFromWallpaper, ViewUtil.HIDE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view instanceof CardView) {
            CardView colorCard = (CardView) view;
            int color = colorCard.getCardBackgroundColor().getDefaultColor();
            getEditableWidgetPreview().setColorsBy(color);
            mColorPicker.setColor(color);
            getWidgetPreview().update();
        }
    }


    public ColorOptionPage setDefaultColor(int color) {
        mDefaultColor = color;
        return this;
    }

    public ColorOptionPage setPalette(Palette palette) {
        mPalette = palette;
        setUpPalette();
        return this;
    }


    private static boolean isLightnessIsBetween(int color, float lowerBound, float upperBound) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);
        return hsl[2] >= lowerBound && hsl[2] <= upperBound;
    }
}
