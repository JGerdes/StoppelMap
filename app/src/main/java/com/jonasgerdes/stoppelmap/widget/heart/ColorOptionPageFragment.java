package com.jonasgerdes.stoppelmap.widget.heart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.views.colorpicker.ColorPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class ColorOptionPageFragment extends FrameLayout implements View.OnClickListener {

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

    private ColorableWidgetPreview mWidgetPreview;

    public ColorOptionPageFragment(Context context) {
        super(context);
        init();
    }

    public ColorOptionPageFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorOptionPageFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorOptionPageFragment(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_settings_color_page, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        for (int i = 0; i < mColorList.getChildCount(); i++) {
            mColorList.getChildAt(i).setOnClickListener(this);
        }

        mColorPicker.setPreventZeroValues(true)
                .setChangeListener(new ColorPicker.ColorChangeListener() {
                    @Override
                    public void onColorChanged(int newColor) {
                        mWidgetPreview.setColorsBy(newColor);
                        mWidgetPreview.update();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view instanceof CardView) {
            CardView colorCard = (CardView) view;
            int color = colorCard.getCardBackgroundColor().getDefaultColor();
            mWidgetPreview.setColorsBy(color);
            mColorPicker.setColor(color);
            mWidgetPreview.update();
        }
    }

    public ColorableWidgetPreview getWidgetPreview() {
        return mWidgetPreview;
    }

    public ColorOptionPageFragment setWidgetPreview(ColorableWidgetPreview widgetPreview) {
        mWidgetPreview = widgetPreview;
        return this;
    }

    public void setColor(int color) {
        mColorPicker.setColor(color);
    }

    public void setWallpaperDrawable(Drawable wallpaper) {
        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(wallpaper);
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(final Palette palette) {
                mColorSelectionDominant
                        .setCardBackgroundColor(palette.getDominantColor(Color.BLACK));
                mColorSelectionVibrant
                        .setCardBackgroundColor(palette.getVibrantColor(Color.BLACK));
                mColorSelectionMuted
                        .setCardBackgroundColor(palette.getMutedColor(Color.BLACK));

                mColorSelectionDominant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int color = palette.getDominantColor(Color.BLACK);
                        mWidgetPreview.setColorsBy(color);
                        mColorPicker.setColor(color);
                        mWidgetPreview.update();
                    }
                });
                mColorSelectionVibrant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] colors = {
                                palette.getLightVibrantColor(Color.BLACK),
                                palette.getVibrantColor(Color.BLACK),
                                palette.getDarkVibrantColor(Color.BLACK)
                        };
                        mColorPicker.setColor(colors[1]);
                        mWidgetPreview.update();
                    }
                });
                mColorSelectionMuted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int[] colors = {
                                palette.getLightMutedColor(Color.BLACK),
                                palette.getMutedColor(Color.BLACK),
                                palette.getDarkMutedColor(Color.BLACK)
                        };
                        mColorPicker.setColor(colors[1]);
                        mWidgetPreview.update();
                    }
                });
            }
        });

    }
}
