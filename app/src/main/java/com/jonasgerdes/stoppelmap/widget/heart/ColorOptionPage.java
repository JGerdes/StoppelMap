package com.jonasgerdes.stoppelmap.widget.heart;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.views.colorpicker.ColorPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class ColorOptionPage extends Fragment implements View.OnClickListener {

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

    private int mDefaultColor;
    private Bitmap mWallpaperBitmap;
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
                        getWidgetPreview().setColorsBy(newColor);
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
        mColorSelectionDominant
                .setCardBackgroundColor(mPalette.getDominantColor(Color.BLACK));
        mColorSelectionVibrant
                .setCardBackgroundColor(mPalette.getVibrantColor(Color.BLACK));
        mColorSelectionMuted
                .setCardBackgroundColor(mPalette.getMutedColor(Color.BLACK));

        mColorSelectionDominant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = mPalette.getDominantColor(Color.BLACK);
                getWidgetPreview().setColorsBy(color);
                mColorPicker.setColor(color);
                getWidgetPreview().update();
            }
        });
        mColorSelectionVibrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] colors = {
                        mPalette.getLightVibrantColor(Color.BLACK),
                        mPalette.getVibrantColor(Color.BLACK),
                        mPalette.getDarkVibrantColor(Color.BLACK)
                };
                mColorPicker.setColor(colors[1]);
                getWidgetPreview().update();
            }
        });
        mColorSelectionMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] colors = {
                        mPalette.getLightMutedColor(Color.BLACK),
                        mPalette.getMutedColor(Color.BLACK),
                        mPalette.getDarkMutedColor(Color.BLACK)
                };
                mColorPicker.setColor(colors[1]);
                getWidgetPreview().update();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view instanceof CardView) {
            CardView colorCard = (CardView) view;
            int color = colorCard.getCardBackgroundColor().getDefaultColor();
            getWidgetPreview().setColorsBy(color);
            mColorPicker.setColor(color);
            getWidgetPreview().update();
        }
    }

    public ColorableWidgetPreview getWidgetPreview() {
        WidgetPreview preview = ((GingerbreadHeartWidgetSettingsActivity) getActivity())
                .getWidgetPreview();
        return (ColorableWidgetPreview) preview;
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
}
