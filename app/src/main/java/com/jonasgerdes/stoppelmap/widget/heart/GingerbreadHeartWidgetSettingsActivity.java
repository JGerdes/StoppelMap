package com.jonasgerdes.stoppelmap.widget.heart;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.WidgetSettingsHelper;
import com.jonasgerdes.stoppelmap.widget.options.ActionOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.ColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.HourOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 31.01.2017.
 */

public class GingerbreadHeartWidgetSettingsActivity extends AbstractWidgetSettingsActivity {

    public static final float MAX_LIGHTNESS = 0.8f;
    private static final int DEFAULT_COLOR = Color.parseColor("#7d56c2");
    public static final float MIN_LIGHTNESS = 0.2f;
    private boolean mDefaultShowHours;
    private int mDefaultAction;
    private int[] mDefaultColors;


    @Override
    protected String getWidgetName() {
        return "Countdown-Widget";
    }

    @Override
    protected List<OptionPage> getOptionPages() {
        List<OptionPage> pages = new ArrayList<>();

        final ColorOptionPage colorOptionPage = new ColorOptionPage()
                .setDefaultColor(mDefaultColors[1])
                .setSelectableColors(
                        0xff7d56c2,
                        ContextCompat.getColor(this, R.color.colorPrimary),
                        0xff2196f3,
                        0xff279056,
                        0xfff57f17,
                        ContextCompat.getColor(this, R.color.colorAccent)
                )
                .setLimits(MIN_LIGHTNESS, MAX_LIGHTNESS);
        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(getWallpaperDrawable());
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                colorOptionPage.setPalette(palette);
            }
        });

        pages.add(colorOptionPage);
        pages.add(HourOptionPage.newInstance(mDefaultShowHours));
        pages.add(ActionOptionPage.newInstance(mDefaultAction));
        return pages;
    }

    @Override
    protected GingerbreadHeartPreview createPreview() {
        GingerbreadHeartPreview preview = new GingerbreadHeartPreview(this);
        if (mDefaultColors[0] == -1) {
            preview.setColorsBy(mDefaultColors[1]);
        } else {
            preview.setColors(mDefaultColors);
        }
        preview.setShowHours(mDefaultShowHours);
        preview.setAction(mDefaultAction);
        return preview;
    }

    @Override
    protected void initWithWidgetId(int appWidgetId) {
        WidgetSettingsHelper settings = new WidgetSettingsHelper(this, appWidgetId);
        mDefaultColors = new int[]{
                settings.getInt(GingerbreadHeartWidgetProvider.SETTING_COLOR_1, -1),
                settings.getInt(GingerbreadHeartWidgetProvider.SETTING_COLOR_2, DEFAULT_COLOR),
                settings.getInt(GingerbreadHeartWidgetProvider.SETTING_COLOR_3, -1)
        };
        mDefaultShowHours = settings.getBoolean(GingerbreadHeartWidgetProvider.SETTING_SHOW_HOUR, false);
        mDefaultAction = settings.getInt(GingerbreadHeartWidgetProvider.SETTING_ACTION,
                ActionOptionPage.ACTION_EDIT_WIDGET);
    }
}
