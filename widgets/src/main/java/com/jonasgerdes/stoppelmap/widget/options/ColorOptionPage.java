package com.jonasgerdes.stoppelmap.widget.options;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.ColorableWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.colorpicker.ColorEditText;
import com.jonasgerdes.stoppelmap.widget.colorpicker.ColorPicker;
import com.jonasgerdes.stoppelmap.widget.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 23.02.2017.
 */

public class ColorOptionPage extends OptionPage<ColorableWidgetPreview> {
    ViewGroup mColorList;
    CardView mColorSelectionDominant;
    CardView mColorSelectionVibrant;
    CardView mColorSelectionMuted;
    ColorPicker mColorPicker;

    List<View> mFromWallpaper;

    private int mDefaultColor;
    private Palette mPalette;
    private int[] mSelectableColors;

    private float mMinLightness = 0f;
    private float mMaxLightness = 1f;
    private int mColor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_color_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorList = view.findViewById(R.id.widget_color_selection_list);
        mColorSelectionDominant = view.findViewById(R.id.color_secection_dominant);
        mColorSelectionVibrant = view.findViewById(R.id.color_secection_vibrant);
        mColorSelectionMuted = view.findViewById(R.id.color_secection_muted);
        mColorPicker = view.findViewById(R.id.color_picker);

        mFromWallpaper = new ArrayList<>();
        mFromWallpaper.add(view.findViewById(R.id.from_wallpaper_list));
        mFromWallpaper.add(view.findViewById(R.id.from_wallpaper_text));

        view.findViewById(R.id.hex_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseHex();
            }
        });

        mColorPicker.setPreventZeroValues(true)
                .setChangeListener(new ColorPicker.ColorChangeListener() {
                    @Override
                    public void onColorChanged(int newColor) {
                        getEditableWidgetPreview().setColorsBy(newColor);
                        getWidgetPreview().update();
                        mColor = newColor;
                    }
                })
                .setColor(mDefaultColor);


        setUpColorCards();
        setUpPalette();

    }

    private void setUpColorCards() {
        int cardSize = ViewUtil.dpToPx(getContext(), 38);
        int margin = ViewUtil.dpToPx(getContext(), 2);
        for (int i = 0; i < mSelectableColors.length; i++) {
            final int color = mSelectableColors[i];
            CardView colorCard = new CardView(getContext());
            LinearLayoutCompat.LayoutParams layout = new LinearLayoutCompat.LayoutParams(
                    cardSize, cardSize + margin
            );
            layout.setMargins(margin, 0, 0, 0);
            colorCard.setUseCompatPadding(true);
            colorCard.setLayoutParams(layout);
            colorCard.setCardBackgroundColor(color);
            colorCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onColorSelected(color);
                }
            });
            mColorList.addView(colorCard);
        }
    }

    private void setUpPalette() {
        if (mPalette == null
                || mColorSelectionDominant == null
                || mColorSelectionVibrant == null
                || mColorSelectionMuted == null) {
            return;
        }

        boolean atLeastOneWallpaperColorUsable = false;

        final int dominantColor = getDominantColor(mPalette.getSwatches());//Color.BLACK;
        // mPalette.getDominantColor(Color.BLACK);
        if (isLightnessIsBetween(dominantColor, mMinLightness, mMaxLightness)) {
            mColorSelectionDominant.setCardBackgroundColor(dominantColor);
            mColorSelectionDominant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onColorSelected(dominantColor);
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionDominant.setVisibility(View.GONE);
        }

        final int vibrantColor = mPalette.getVibrantColor(Color.BLACK);
        if (isLightnessIsBetween(vibrantColor, mMinLightness, mMaxLightness)) {
            mColorSelectionVibrant.setCardBackgroundColor(vibrantColor);
            mColorSelectionVibrant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] colors = {
                            mPalette.getLightVibrantColor(vibrantColor),
                            vibrantColor,
                            mPalette.getDarkVibrantColor(vibrantColor)
                    };
                    onColorSelected(vibrantColor);
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionVibrant.setVisibility(View.GONE);
        }

        final int mutedColor = mPalette.getMutedColor(Color.BLACK);
        if (isLightnessIsBetween(mutedColor, mMinLightness, mMaxLightness)) {
            mColorSelectionMuted.setCardBackgroundColor(mutedColor);
            mColorSelectionMuted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] colors = {
                            mPalette.getLightMutedColor(mutedColor),
                            mutedColor,
                            mPalette.getDarkMutedColor(mutedColor)
                    };
                    onColorSelected(mutedColor);
                }
            });
            atLeastOneWallpaperColorUsable = true;
        } else {
            mColorSelectionMuted.setVisibility(View.GONE);
        }

        if (!atLeastOneWallpaperColorUsable) {
            for (View view : mFromWallpaper) {
                view.setVisibility(View.GONE);
            }
        }
    }

    private int getDominantColor(List<Palette.Swatch> swatches) {
        int maxPop = Integer.MIN_VALUE;
        Palette.Swatch maxSwatch = null;
        for (int i = 0, count = swatches.size(); i < count; i++) {
            Palette.Swatch swatch = swatches.get(i);
            if (swatch.getPopulation() > maxPop) {
                maxSwatch = swatch;
                maxPop = swatch.getPopulation();
            }
        }
        if (maxSwatch == null) {
            return Color.BLACK;
        }
        return maxSwatch.getRgb();
    }


    public ColorOptionPage setSelectableColors(int... colors) {
        mSelectableColors = colors;
        return this;
    }

    public ColorOptionPage setLimits(float minLightness, float maxLightness) {
        mMinLightness = minLightness;
        mMaxLightness = maxLightness;
        return this;
    }

    public void onColorSelected(int color) {
        mColor = color;
        mColorPicker.setColor(color);
        getEditableWidgetPreview().setColorsBy(color);
        getWidgetPreview().update();
    }


    public ColorOptionPage setDefaultColor(int color) {
        mDefaultColor = color;
        mColor = mDefaultColor;
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

    void chooseHex() {
        final ColorEditText colorEditText = new ColorEditText(getContext());
        colorEditText.setColor(mColor);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.widget_settings_color_custom_dialog_title)
                .setView(colorEditText)
                .setPositiveButton(R.string.widget_settings_color_custom_dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onColorSelected(colorEditText.getColor());
                    }
                })
                .setNegativeButton(R.string.widget_settings_color_custom_dialog_cancel, null)
                .show();
    }
}
